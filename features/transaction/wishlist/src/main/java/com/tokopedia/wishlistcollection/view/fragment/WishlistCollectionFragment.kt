package com.tokopedia.wishlistcollection.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.FragmentCollectionWishlistBinding
import com.tokopedia.wishlist.util.WishlistV2Analytics
import com.tokopedia.wishlist.util.WishlistV2Consts.EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter.Companion.LAYOUT_RECOMMENDATION_TITLE
import com.tokopedia.wishlistcollection.di.WishlistCollectionModule
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionCarouselEmptyStateData
import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse
import com.tokopedia.wishlistcollection.di.DaggerWishlistCollectionComponent
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.REQUEST_CODE_COLLECTION_DETAIL
import com.tokopedia.wishlistcollection.util.WishlistCollectionOnboardingPreference
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetKebabMenuWishlistCollectionItem
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetOnboardingWishlistCollection
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetUpdateWishlistCollectionName
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerFromCollectionPage
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionViewModel
import javax.inject.Inject


class WishlistCollectionFragment : BaseDaggerFragment(), WishlistCollectionAdapter.ActionListener,
    BottomSheetKebabMenuWishlistCollectionItem.ActionListener, ActionListenerFromCollectionPage,
    BottomSheetUpdateWishlistCollectionName.ActionListener,
    BottomSheetOnboardingWishlistCollection.ActionListener {
    private var onlyAllCollection: Boolean = false
    private var binding by autoClearedNullable<FragmentCollectionWishlistBinding>()
    private lateinit var collectionAdapter: WishlistCollectionAdapter
    private var activityWishlistCollection = ""
    private var isEligibleAddNewCollection = false
    private var wordingMaxLimitCollection = ""
    private var bottomSheetOnboarding = BottomSheetOnboardingWishlistCollection()
    private var _allCollectionView: View? = null
    private var _createCollectionView: View? = null
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var rvScrollListener: EndlessRecyclerViewScrollListener
    private var onLoadMore = false
    private var isFetchRecommendation = false
    private var currRecommendationListPage = 1

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val collectionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WishlistCollectionViewModel::class.java]
    }
    private val onboardingPref: WishlistCollectionOnboardingPreference? by lazy {
        activity?.let { WishlistCollectionOnboardingPreference(it) }
    }
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }

    private val coachMarkItem = ArrayList<CoachMark2Item>()
    private var coachMark: CoachMark2? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let { activity ->
            DaggerWishlistCollectionComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .wishlistCollectionModule(WishlistCollectionModule(activity))
                .build()
                .inject(this)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    companion object {
        @JvmStatic
        fun newInstance(): WishlistCollectionFragment {
            return WishlistCollectionFragment()
        }

        const val DEFAULT_TITLE = "Wishlist"
        const val OK = "OK"
        private const val PARAM_ACTIVITY_WISHLIST_COLLECTION = "activity_wishlist_collection"
        const val PARAM_HOME = "home"
        private const val COACHMARK_WISHLIST = "coachmark-wishlist"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
        initTrackingQueue()
    }

    private fun initTrackingQueue() {
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            getWishlistCollections()
        } else {
            startActivityForResult(
                RouteManager.getIntent(context, ApplinkConst.LOGIN),
                WishlistV2Fragment.REQUEST_CODE_LOGIN
            )
        }
    }

    private fun checkOnboarding() {
        if (onboardingPref?.hasOnboardingShown() == false) {
            showBottomSheetOnboarding()
            onboardingPref?.setShown(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionWishlistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    private fun observingData() {
        observingWishlistCollections()
        observingWishlistData()
        observingDeleteWishlistCollection()
    }

    private fun prepareLayout() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
            )
        }
        setToolbarTitle(DEFAULT_TITLE)
        setSwipeRefreshLayout()
        collectionAdapter = WishlistCollectionAdapter().apply {
            setActionListener(this@WishlistCollectionFragment)
        }
        binding?.run {
            activityWishlistCollection =
                arguments?.getString(PARAM_ACTIVITY_WISHLIST_COLLECTION, "") ?: ""

            val pageSource: String
            val icons: IconBuilder
            viewLifecycleOwner.lifecycle.addObserver(wishlistCollectionNavtoolbar)
            if (activityWishlistCollection != PARAM_HOME) {
                wishlistCollectionNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                icons = IconBuilder(IconBuilderFlag()).apply {
                    addIcon(IconList.ID_CART) {}
                    addIcon(IconList.ID_NAV_GLOBAL) {}
                }
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_COLLECTION
                wishlistCollectionNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
                icons = IconBuilder(IconBuilderFlag(pageSource = pageSource)).apply {
                    addIcon(IconList.ID_MESSAGE) {}
                    addIcon(IconList.ID_NOTIFICATION) {}
                    addIcon(IconList.ID_CART) {}
                    addIcon(IconList.ID_NAV_GLOBAL) {}
                }
            }
            wishlistCollectionNavtoolbar.setIcon(icons)
        }
        addEndlessScrollListener()
    }

    private fun addEndlessScrollListener() {
        val glm = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (collectionAdapter.getItemViewType(position)) {
                        WishlistCollectionAdapter.LAYOUT_COLLECTION_TICKER -> 2
                        LAYOUT_RECOMMENDATION_TITLE -> 2
                        WishlistCollectionAdapter.LAYOUT_EMPTY_COLLECTION -> 2
                        else -> 1
                    }
                }
            }
        }

        rvScrollListener = object : EndlessRecyclerViewScrollListener(glm) {

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                currentPage += 1
                onLoadMore = true
                if (isFetchRecommendation) {
                    loadRecommendationList()
                }
            }
        }

        binding?.run {
            rvWishlistCollection.apply {
                layoutManager = glm
                adapter = collectionAdapter
                addOnScrollListener(rvScrollListener)
            }
        }
    }

    private fun loadRecommendationList() {
        currRecommendationListPage += 1
        collectionViewModel.loadRecommendation(currRecommendationListPage)
    }

    private fun setToolbarTitle(title: String) {
        binding?.run {
            wishlistCollectionNavtoolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
            wishlistCollectionNavtoolbar.setToolbarTitle(title)
        }
    }

    private fun setSwipeRefreshLayout() {
        binding?.run {
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.setOnRefreshListener {
                doRefresh()
            }
        }
    }

    private fun doRefresh() {
        onLoadMore = false
        isFetchRecommendation = false
        currRecommendationListPage = 1
        getWishlistCollections()
        collectionAdapter.resetTicker()
    }

    private fun getWishlistCollections() {
        collectionViewModel.getWishlistCollections()
    }

    private fun observingWishlistCollections() {
        collectionViewModel.collections.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    if (result.data.status == OK) {
                        if (result.data.data.isEmptyState) {
                            val items = arrayListOf<Any>()
                            result.data.data.emptyState.messages.forEach { item ->
                                items.add(WishlistCollectionCarouselEmptyStateData(img = item.imageUrl, desc = item.description))
                            }
                            collectionAdapter.setCarouselEmptyData(items)
                        }
                        if (result.data.data.totalCollection >= result.data.data.maxLimitCollection) {
                            isEligibleAddNewCollection = false
                            wordingMaxLimitCollection = result.data.data.wordingMaxLimitCollection
                        } else {
                            isEligibleAddNewCollection = true
                        }
                        if (result.data.data.collections.size == 1) {
                            onlyAllCollection = true
                            checkOnboarding()
                        }
                    } else {
                        // TODO: show global error page?
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_v2_common_error_msg
                            )
                        }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    // TODO: show global error page?
                    finishRefresh()
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingWishlistData() {
        collectionViewModel.collectionData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    collectionAdapter.addList(result.data)
                }
            }
        }
    }

    private fun observingDeleteWishlistCollection() {
        collectionViewModel.deleteCollectionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    if (result.data.status == OK && result.data.data.success) {
                        getWishlistCollections()
                        showToaster(result.data.data.message, "", Toaster.TYPE_NORMAL)
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_v2_common_error_msg
                            )
                        }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText).show()
        }
    }

    private fun finishRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCloseTicker() {
        collectionAdapter.hideTicker()
    }

    override fun onKebabMenuClicked(collectionId: String, collectionName: String) {
        showBottomSheetKebabMenu(collectionId, collectionName)
    }

    private fun showBottomSheetKebabMenu(collectionId: String, collectionName: String) {
        val bottomSheetKebabMenu =
            BottomSheetKebabMenuWishlistCollectionItem.newInstance(collectionName, collectionId)
        bottomSheetKebabMenu.setListener(this@WishlistCollectionFragment)
        if (bottomSheetKebabMenu.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetKebabMenu.show(childFragmentManager)
    }

    override fun onCreateNewCollectionClicked() {
        if (isEligibleAddNewCollection && wordingMaxLimitCollection.isEmpty()) {
            showBottomSheetCreateNewCollection()
        } else {
            showToaster(wordingMaxLimitCollection, getString(R.string.wishlist_oke_label), Toaster.TYPE_ERROR)
        }
    }

    private fun showBottomSheetCreateNewCollection() {
        val bottomSheetCreateCollection = BottomSheetCreateNewCollectionWishlist.newInstance("")
        bottomSheetCreateCollection.setListener(this@WishlistCollectionFragment)
        if (bottomSheetCreateCollection.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetCreateCollection.show(childFragmentManager)
    }

    override fun onCollectionItemClicked(id: String) {
        val detailCollection =
            "${ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL}?${ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID}=$id"
        val intentCollectionDetail = RouteManager.getIntent(context, detailCollection)
        startActivityForResult(intentCollectionDetail, REQUEST_CODE_COLLECTION_DETAIL)
    }

    override fun onChangeCollectionName(collectionId: String, collectionName: String) {
        showUpdateWishlistCollectionNameBottomSheet(collectionId, collectionName)
    }

    private fun showUpdateWishlistCollectionNameBottomSheet(collectionId: String, collectionName: String) {
        val bottomSheetUpdateWishlistCollectionName = BottomSheetUpdateWishlistCollectionName.newInstance(collectionId, collectionName)
        bottomSheetUpdateWishlistCollectionName.setListener(this@WishlistCollectionFragment)
        if (bottomSheetUpdateWishlistCollectionName.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetUpdateWishlistCollectionName.show(childFragmentManager)
    }

    override fun onDeleteCollectionItem(collectionId: String, collectionName: String) {
        showDialogDeleteCollection(collectionId, collectionName)
    }

    override fun onSuccessCreateNewCollection(dataCreate: CreateWishlistCollectionResponse.CreateWishlistCollection.DataCreate, newCollectionName: String) {
        val detailCollection =
            "${ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL}?${ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID}=${dataCreate.id}"
        val intentCollectionDetail = RouteManager.getIntent(context, detailCollection)
        intentCollectionDetail.putExtra(EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL, dataCreate.message)
        startActivity(intentCollectionDetail)
    }

    override fun onCreateCollectionItemBind(allCollectionView: View, createCollectionView: View) {
        if (!CoachMarkPreference.hasShown(requireContext(), COACHMARK_WISHLIST)
            && onlyAllCollection
        ) {
            _allCollectionView = allCollectionView
            _createCollectionView = createCollectionView
        }
    }

    override fun onCariBarangClicked() {
        RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
    }

    private fun showBottomSheetOnboarding() {
        bottomSheetOnboarding = BottomSheetOnboardingWishlistCollection.newInstance()
        bottomSheetOnboarding.setListener(this@WishlistCollectionFragment)
        if (bottomSheetOnboarding.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetOnboarding.show(childFragmentManager)
    }

    private fun showWishlistCollectionCoachMark(view1: View, view2: View) {
        if (coachMarkItem.isEmpty()) {
            coachMarkItem.add(
                CoachMark2Item(
                    view1,
                    "",
                    getString(R.string.collection_coachmark_see_all_wishlist),
                    CoachMark2.POSITION_BOTTOM
                )
            )
            coachMarkItem.add(
                CoachMark2Item(
                    view2,
                    "",
                    getString(R.string.collection_coachmark_create_collection),
                    CoachMark2.POSITION_BOTTOM
                )
            )
        }
        if (coachMark == null)
            coachMark = CoachMark2(requireContext())

        coachMark?.let {
            it.onFinishListener = {
                showBottomSheetCreateNewCollection()
            }
            it.stepButtonTextLastChild =
                getString(R.string.collection_coachmark_try_create_wishlist)
            it.stepPrev?.text = getString(R.string.collection_coachmark_back)

            if (!it.isShowing) {
                it.showCoachMark(coachMarkItem, null)
            }
            CoachMarkPreference.setShown(requireContext(), COACHMARK_WISHLIST, true)
        }
    }

    private fun showDialogDeleteCollection(collectionId: String, collectionName: String) {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(
            getString(
                R.string.wishlist_collection_detail_delete_confirmation_title,
                collectionName
            )
        )
        dialog?.dialogDesc?.gone()
        dialog?.setPrimaryCTAText(getString(R.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doDeleteCollection(collectionId)
        }
        dialog?.setSecondaryCTAText(getString(R.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            // WishlistV2Analytics.clickBatalOnPopUpMultipleWishlistProduct()
        }
        dialog?.show()
    }

    private fun doDeleteCollection(collectionId: String) {
        collectionViewModel.deleteWishlistCollection(collectionId)
    }

    override fun onSuccessUpdateCollectionName(message: String) {
        showToaster(message, "", Toaster.TYPE_NORMAL)
        getWishlistCollections()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_COLLECTION_DETAIL && resultCode == Activity.RESULT_OK) {
            getWishlistCollections()
            /*val isNeedRefresh = data?.getBooleanExtra(EXTRA_NEED_REFRESH, false)
            if (isNeedRefresh == true) getWishlistCollections()*/

            val isSuccess = data?.getBooleanExtra(ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS, false)
            val messageToaster =
                data?.getStringExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER)
            if (messageToaster != null) {
                if (isSuccess == true) {
                    showToaster(messageToaster, "", Toaster.TYPE_NORMAL)
                } else {
                    showToaster(messageToaster, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    override fun onClickShowCoachmarkButton() {
        bottomSheetOnboarding.dismiss()
        _allCollectionView?.let { v1 ->
            _createCollectionView?.let { v2 ->
                showWishlistCollectionCoachMark(v1, v2) }
        }
    }

    override fun onClickSkipOnboardingButton() {
        bottomSheetOnboarding.dismiss()
    }

    override fun onRecommendationItemImpression(recommendationItem: RecommendationItem, position: Int) {
        if(recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitImpressionUrl(
                this::class.java.simpleName,
                recommendationItem.trackerImageUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        WishlistV2Analytics.impressionEmptyWishlistRecommendation(trackingQueue, recommendationItem, position)
    }

    override fun onRecommendationItemClick(recommendationItem: RecommendationItem, position: Int) {
        WishlistV2Analytics.clickRecommendationItem(recommendationItem, position, userSession.userId)
        if(recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                recommendationItem.clickUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        activity?.let {
            if (recommendationItem.appUrl.isNotEmpty()) {
                RouteManager.route(it, recommendationItem.appUrl)
            } else {
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    recommendationItem.productId.toString())
                startActivity(intent)
            }
        }
    }
}