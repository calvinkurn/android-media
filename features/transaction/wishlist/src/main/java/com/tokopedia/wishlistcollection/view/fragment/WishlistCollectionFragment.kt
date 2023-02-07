package com.tokopedia.wishlistcollection.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.databinding.FragmentCollectionWishlistBinding
import com.tokopedia.wishlist.util.WishlistV2Analytics
import com.tokopedia.wishlist.util.WishlistV2Consts.EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter.Companion.LAYOUT_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionCarouselEmptyStateData
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.di.DaggerWishlistCollectionComponent
import com.tokopedia.wishlistcollection.di.WishlistCollectionModule
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_ID
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_NAME
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_PRIVATE
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_PUBLIC
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.DELAY_REFETCH_PROGRESS_DELETION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.REQUEST_CODE_COLLECTION_DETAIL
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_SHARE
import com.tokopedia.wishlistcollection.util.WishlistCollectionPrefs
import com.tokopedia.wishlistcollection.util.WishlistCollectionSharingUtils
import com.tokopedia.wishlistcollection.view.activity.WishlistCollectionEditActivity
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter.Companion.LAYOUT_DIVIDER
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter.Companion.LAYOUT_LOADER
import com.tokopedia.wishlistcollection.view.adapter.itemdecoration.WishlistCollectionItemOffsetDecoration
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetKebabMenuWishlistCollection
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetOnboardingWishlistCollection
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetUpdateWishlistCollectionName
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerBottomSheetMenu
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerFromCollectionPage
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionViewModel
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt

@Keep
class WishlistCollectionFragment :
    BaseDaggerFragment(),
    WishlistCollectionAdapter.ActionListener,
    ActionListenerFromCollectionPage,
    BottomSheetUpdateWishlistCollectionName.ActionListener,
    BottomSheetOnboardingWishlistCollection.ActionListener,
    ActionListenerBottomSheetMenu {
    private var onlyAllCollection: Boolean = false
    private var binding by autoClearedNullable<FragmentCollectionWishlistBinding>()
    private lateinit var collectionAdapter: WishlistCollectionAdapter
    private var activityWishlistCollection = ""
    private var isEligibleAddNewCollection = false
    private var wordingMaxLimitCollection = ""
    private var bottomSheetOnboarding: BottomSheetOnboardingWishlistCollection? = BottomSheetOnboardingWishlistCollection()
    private var bottomSheetKebabMenu: BottomSheetKebabMenuWishlistCollection? = BottomSheetKebabMenuWishlistCollection()
    private var _allCollectionView: View? = null
    private var _createCollectionView: View? = null
    private var _firstAnchorKebabMenuView: View? = null
    private var _firstCollectionId: String = ""
    private var _firstCollectionName: String = ""
    private var _firstActionsCollection: List<GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Action> = emptyList()
    private var _firstCollectionIndicatorTitle: String = ""
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var rvScrollListener: EndlessRecyclerViewScrollListener
    private var onLoadMore = false
    private var isFetchRecommendation = false
    private var currRecommendationListPage = 1
    private var hitCountDeletion = false
    private var isOnProgressDeleteWishlist = false
    private var _collectionIdShared = ""
    private val handler = Handler(Looper.getMainLooper())
    private val progressDeletionRunnable = Runnable {
        getDeleteWishlistProgress()
    }
    private val wishlistCollectionPref: WishlistCollectionPrefs? by lazy {
        activity?.let { WishlistCollectionPrefs(it) }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val collectionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WishlistCollectionViewModel::class.java]
    }
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }

    private val coachMarkItem = arrayListOf<CoachMark2Item>()
    private var coachMark: CoachMark2? = null

    private val coachMarkItemSharing1 = arrayListOf<CoachMark2Item>()
    private var coachMarkSharing1: CoachMark2? = null

    private val coachMarkItemSharing2 = arrayListOf<CoachMark2Item>()
    private var coachMarkSharing2: CoachMark2? = null

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
        private const val COACHMARK_WISHLIST_ONBOARDING = "coachmark-wishlist-onboarding"
        private const val COACHMARK_WISHLIST = "coachmark-wishlist"
        private const val COACHMARK_WISHLIST_SHARING = "coachmark-wishlist-sharing"
        private const val WISHLIST_PAGE = "wishlist page"
        private const val EDIT_WISHLIST_COLLECTION_REQUEST_CODE = 188
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WishlistCollectionAnalytics.sendWishListHomePageOpenedEvent(userSession.isLoggedIn, userSession.userId)
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
        if (!CoachMarkPreference.hasShown(requireContext(), COACHMARK_WISHLIST_ONBOARDING)) {
            showBottomSheetOnboarding()
            CoachMarkPreference.setShown(requireContext(), COACHMARK_WISHLIST_ONBOARDING, true)
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        manageCoachmark(isVisibleToUser)
    }

    private fun manageCoachmark(isVisibleToUser: Boolean) {
        if (!isVisibleToUser) {
            coachMark?.dismissCoachMark()
            coachMarkSharing1?.dismissCoachMark()
            coachMarkSharing2?.dismissCoachMark()
        }
    }

    override fun onDestroyView() {
        bottomSheetOnboarding = null
        bottomSheetKebabMenu = null
        super.onDestroyView()
    }

    private fun observingData() {
        observingWishlistCollections()
        observingWishlistData()
        observingDeleteWishlistCollection()
        observingDeleteProgress()
        observeGetCollectionSharingData()
        observeUpdateAccessWishlistCollection()
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
                        LAYOUT_LOADER -> 1
                        WishlistCollectionAdapter.LAYOUT_COLLECTION_TICKER -> 2
                        LAYOUT_RECOMMENDATION_TITLE -> 2
                        WishlistCollectionAdapter.LAYOUT_EMPTY_COLLECTION -> 2
                        LAYOUT_DIVIDER -> 2
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
                val margins = (this.layoutParams as RelativeLayout.LayoutParams).apply {
                    leftMargin = 10
                    rightMargin = 10
                }
                layoutParams = margins
                layoutManager = glm
                adapter = collectionAdapter
                addItemDecoration(WishlistCollectionItemOffsetDecoration(requireContext(), com.tokopedia.abstraction.R.dimen.dp_8))
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
    }

    private fun getWishlistCollections() {
        collectionViewModel.getWishlistCollections()
    }

    private fun observingWishlistCollections() {
        showLoader()
        collectionViewModel.collections.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    if (result.data.status == OK) {
                        showRvWishlistCollection()
                        wishlistCollectionPref?.getHasClosed()
                            ?.let { collectionAdapter.setTickerHasClosed(it) }

                        // check empty state
                        if (result.data.data.isEmptyState) {
                            val items = arrayListOf<Any>()
                            result.data.data.emptyState.messages.forEach { item ->
                                items.add(WishlistCollectionCarouselEmptyStateData(img = item.imageUrl, desc = item.description))
                            }
                            collectionAdapter.setCarouselEmptyData(items)
                        }

                        // check if need to show delete progress
                        if (result.data.data.showDeleteProgress) {
                            showDeletionProgress()
                            if (!hitCountDeletion) {
                                hitCountDeletion = true
                                isOnProgressDeleteWishlist = true
                                getDeleteWishlistProgress()
                            }
                        } else {
                            hideDeletionProgress()
                        }

                        // check eligible to add new collection
                        if (result.data.data.totalCollection >= result.data.data.maxLimitCollection) {
                            isEligibleAddNewCollection = false
                            wordingMaxLimitCollection = result.data.data.wordingMaxLimitCollection
                        } else {
                            isEligibleAddNewCollection = true
                        }

                        // check if need to show any coachmarks
                        if (result.data.data.collections.size == 1) {
                            onlyAllCollection = true
                            checkOnboarding()
                        }
                    } else {
                        showGlobalErrorWishlistCollection(ResponseErrorException())
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_v2_common_error_msg
                            )
                        }
                        errorMessage?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    showGlobalErrorWishlistCollection(result.throwable)
                    finishRefresh()
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun getDeleteWishlistProgress() {
        collectionViewModel.getDeleteWishlistProgress()
    }

    private fun showRvWishlistCollection() {
        binding?.run {
            rlWishlistCollectionError.gone()
            rlWishlistCollectionContent.visible()
        }
    }

    private fun showGlobalErrorWishlistCollection(throwable: Throwable) {
        val errorType = when (throwable) {
            is MessageErrorException -> null
            is SocketTimeoutException, is UnknownHostException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
        if (errorType == null) {
            binding?.run {
                rlWishlistCollectionContent.gone()
                rlWishlistCollectionError.visible()
                globalErrorWishlistCollection.gone()
                emptyStateWishlistCollection.apply {
                    visible()
                    showMessageExceptionError(throwable)
                }
            }
        } else {
            binding?.run {
                rlWishlistCollectionContent.gone()
                rlWishlistCollectionError.visible()
                emptyStateWishlistCollection.gone()
                globalErrorWishlistCollection.apply {
                    visible()
                    setType(errorType)
                    setActionClickListener {
                        doRefresh()
                    }
                }
            }
        }
    }

    private fun EmptyStateUnify.showMessageExceptionError(throwable: Throwable) {
        var errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: ""
        if (errorMessage.isEmpty()) {
            errorMessage =
                getString(R.string.wishlist_v2_failed_to_get_information)
        }
        setDescription(errorMessage)
    }

    override fun onPause() {
        stopProgressDeletionHandler()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding?.run {
            wishlistCollectionStickyProgressDeletionWidget.rlDeletionProgress.gone()
        }
        checkProgressDeletion()
    }

    private fun checkProgressDeletion() {
        if (isOnProgressDeleteWishlist) {
            getDeleteWishlistProgress()
        }
    }

    private fun stopProgressDeletionHandler() {
        handler.removeCallbacks(progressDeletionRunnable)
    }

    private fun stopDeletionAndShowToasterError(message: String) {
        showToasterActionOke(message, Toaster.TYPE_ERROR)
        finishDeletionWidget(DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress())
        doRefresh()
    }

    private fun finishDeletionWidget(data: DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress) {
        isOnProgressDeleteWishlist = false
        stopProgressDeletionHandler()
        collectionViewModel.deleteWishlistProgressResult.removeObservers(this)
        if (data.totalItems > 0 && data.toasterMessage.isNotEmpty()) {
            val finishData =
                DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress(
                    totalItems = data.totalItems,
                    successfullyRemovedItems = data.totalItems,
                    message = data.message,
                    tickerColor = data.tickerColor,
                    success = data.success,
                    toasterMessage = data.toasterMessage
                )
            updateDeletionWidget(finishData)
            showToasterActionOke(data.toasterMessage, Toaster.TYPE_NORMAL)
        }
        hideDeletionProgress()
        doRefresh()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDeletionWidget(progressData: DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress) {
        var message = getString(R.string.wishlist_v2_default_message_deletion_progress)
        if (progressData.message.isNotEmpty()) message = progressData.message

        val percentage = progressData.successfullyRemovedItems.toDouble() / progressData.totalItems
        val indicatorProgressBar = percentage * 100

        binding?.run {
            wishlistCollectionStickyProgressDeletionWidget.stickyDeletionCard.cardType =
                CardUnify2.TYPE_SHADOW
            wishlistCollectionStickyProgressDeletionWidget.rlDeletionProgress.visible()
            wishlistCollectionStickyProgressDeletionWidget.deletionMessage.text =
                message
            wishlistCollectionStickyProgressDeletionWidget.deletionProgressBar.setValue(
                indicatorProgressBar.roundToInt(),
                true
            )
            wishlistCollectionStickyProgressDeletionWidget.labelProgressBar.text =
                "${progressData.successfullyRemovedItems}/${progressData.totalItems}"
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
                        showToasterActionOke(result.data.data.message, Toaster.TYPE_NORMAL)
                    } else {
                        val errorMessage = if (result.data.errorMessage.isNotEmpty()) {
                            result.data.errorMessage.firstOrNull() ?: ""
                        } else if (result.data.data.message.isNotEmpty()) {
                            result.data.data.message
                        } else {
                            getString(com.tokopedia.wishlist.R.string.wishlist_v2_common_error_msg)
                        }
                        showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingDeleteProgress() {
        collectionViewModel.deleteWishlistProgressResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        val data = result.data.data
                        if (data.success) {
                            if (data.successfullyRemovedItems >= data.totalItems) {
                                finishDeletionWidget(data)
                            } else {
                                updateDeletionWidget(data)
                                handler.postDelayed(
                                    progressDeletionRunnable,
                                    DELAY_REFETCH_PROGRESS_DELETION
                                )
                            }
                        } else {
                            stopDeletionAndShowToasterError(data.toasterMessage)
                        }
                    } else {
                        if (result.data.errorMessage.isNotEmpty()) {
                            stopDeletionAndShowToasterError(result.data.errorMessage[0])
                        }
                    }
                }
                is Fail -> {
                    val errorMessage = getString(R.string.wishlist_v2_common_error_msg)
                    stopDeletionAndShowToasterError(errorMessage)
                }
            }
        }
    }

    private fun observeGetCollectionSharingData() {
        collectionViewModel.getWishlistCollectionSharingDataResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        activity?.let { fragmentActivity ->
                            view?.let { view ->
                                WishlistCollectionSharingUtils().showUniversalShareWithMediaBottomSheet(
                                    activity = fragmentActivity,
                                    data = result.data.data,
                                    paramImageGenerator = WishlistCollectionSharingUtils().mapParamImageGenerator(result.data.data),
                                    userId = userSession.userId,
                                    view = view,
                                    childFragmentManager = childFragmentManager,
                                    fragment = this@WishlistCollectionFragment
                                )
                            }
                        }
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                com.tokopedia.wishlist.R.string.wishlist_v2_common_error_msg
                            )
                        }
                        errorMessage?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeUpdateAccessWishlistCollection() {
        collectionViewModel.updateWishlistCollectionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.data.success && result.data.status == OK) {
                        collectionViewModel.getWishlistCollectionSharingData(_collectionIdShared.toLongOrZero())
                        getWishlistCollections()
                    } else if (result.data.errorMessage.isNotEmpty()) {
                        showToasterActionOke(result.data.errorMessage[0], Toaster.TYPE_ERROR)
                    } else {
                        context?.getString(R.string.wishlist_v2_common_error_msg)
                            ?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun finishRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCloseTicker() {
        wishlistCollectionPref?.setHasClosed(true)
        collectionAdapter.setTickerHasClosed(true)
        WishlistCollectionAnalytics.sendClickXOnIntroductionSectionEvent()
    }

    override fun onKebabMenuClicked(
        collectionId: String,
        collectionName: String,
        actions: List<GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Action>,
        collectionIndicatorTitle: String
    ) {
        showBottomSheetKebabMenu(collectionId, collectionName, actions, collectionIndicatorTitle)
        WishlistCollectionAnalytics.sendClickThreeDotsOnCollectionFolderEvent()
    }

    private fun showDeletionProgress() {
        binding?.run {
            wishlistCollectionStickyProgressDeletionWidget.rlDeletionProgress.visible()
        }
    }

    private fun hideDeletionProgress() {
        binding?.run {
            wishlistCollectionStickyProgressDeletionWidget.rlDeletionProgress.gone()
        }
    }

    private fun showBottomSheetKebabMenu(
        collectionId: String,
        collectionName: String,
        actions: List<GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Action>,
        collectionIndicatorTitle: String
    ) {
        bottomSheetKebabMenu =
            BottomSheetKebabMenuWishlistCollection.newInstance(collectionName, collectionId, actions, collectionIndicatorTitle)
        bottomSheetKebabMenu?.setListener(this@WishlistCollectionFragment)
        if (bottomSheetKebabMenu?.isAdded == true || childFragmentManager.isStateSaved) return
        bottomSheetKebabMenu?.show(childFragmentManager)
    }

    override fun onCreateNewCollectionClicked() {
        if (isEligibleAddNewCollection) {
            showBottomSheetCreateNewCollection()
        } else {
            showToasterActionOke(wordingMaxLimitCollection, Toaster.TYPE_ERROR)
        }
    }

    private fun showBottomSheetCreateNewCollection() {
        val bottomSheetCreateCollection = BottomSheetCreateNewCollectionWishlist.newInstance(
            arrayListOf(),
            WISHLIST_PAGE
        )
        bottomSheetCreateCollection.setListener(this@WishlistCollectionFragment)
        if (bottomSheetCreateCollection.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetCreateCollection.show(childFragmentManager)
    }

    override fun onCollectionItemClicked(id: String) {
        val intentCollectionDetail = RouteManager.getIntent(context, WISHLIST_COLLECTION_DETAIL_INTERNAL, id)
        startActivityForResult(intentCollectionDetail, REQUEST_CODE_COLLECTION_DETAIL)
    }

    override fun onEditCollection(collectionId: String, collectionName: String, actionText: String) {
        bottomSheetKebabMenu?.dismiss()
        val intent = Intent(context, WishlistCollectionEditActivity::class.java)
        intent.putExtra(COLLECTION_ID, collectionId)
        intent.putExtra(COLLECTION_NAME, collectionName)
        startActivityForResult(intent, EDIT_WISHLIST_COLLECTION_REQUEST_CODE)
    }

    override fun onDeleteCollection(collectionId: String, collectionName: String, actionText: String) {
        bottomSheetKebabMenu?.dismiss()
        showDialogDeleteCollection(collectionId, collectionName)
    }

    override fun onShareCollection(
        collectionId: String,
        collectionName: String,
        actionText: String,
        _collectionIndicatorTitle: String
    ) {
        _collectionIdShared = collectionId
        bottomSheetKebabMenu?.dismiss()
        var collectionType = ""
        if (_collectionIndicatorTitle.isEmpty()) {
            collectionType = COLLECTION_PRIVATE
            showDialogSharePermission(collectionId, collectionName)
        } else {
            collectionType = COLLECTION_PUBLIC
            collectionViewModel.getWishlistCollectionSharingData(collectionId.toLongOrZero())
        }
        WishlistCollectionAnalytics.sendClickShareButtonCollectionEvent(collectionId, collectionType, userSession.userId)
    }

    private fun showDialogSharePermission(collectionId: String, collectionName: String) {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(R.string.sharing_collection_confirmation_title))
        dialog?.setDescription(getString(R.string.sharing_collection_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(R.string.sharing_collection_primary_button))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            updateCollectionAccess(collectionId, collectionName)
        }
        dialog?.setSecondaryCTAText(getString(R.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun updateCollectionAccess(collectionId: String, collectionName: String) {
        val params = UpdateWishlistCollectionParams(
            id = collectionId.toLongOrZero(),
            name = collectionName,
            access = TYPE_COLLECTION_SHARE.toLong()
        )
        collectionViewModel.updateAccessWishlistCollection(params)
    }

    override fun onManageItemsInCollection(actionText: String) {
        // used in WishlistCollectionDetail
    }

    override fun onSuccessCreateNewCollection(dataCreate: CreateWishlistCollectionResponse.CreateWishlistCollection.DataCreate, newCollectionName: String) {
        val intentCollectionDetail = RouteManager.getIntent(context, WISHLIST_COLLECTION_DETAIL_INTERNAL, dataCreate.id)
        intentCollectionDetail.putExtra(EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL, dataCreate.message)
        startActivityForResult(intentCollectionDetail, REQUEST_CODE_COLLECTION_DETAIL)
    }

    override fun onCreateCollectionItemBind(allCollectionView: View, createCollectionView: View) {
        if (!CoachMarkPreference.hasShown(requireContext(), COACHMARK_WISHLIST) &&
            onlyAllCollection
        ) {
            _allCollectionView = allCollectionView
            _createCollectionView = createCollectionView
        }
    }

    override fun onShareItemShown(anchorView: View) {
        if (!CoachMarkPreference.hasShown(requireContext(), COACHMARK_WISHLIST_SHARING) && userVisibleHint) {
            showCoachmarkKebabItem2(anchorView)
        }
    }

    override fun onFirstCollectionItemBind(
        anchorKebabMenuView: View,
        collectionId: String,
        collectionName: String,
        actions: List<GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Action>,
        collectionIndicatorTitle: String
    ) {
        _firstAnchorKebabMenuView = anchorKebabMenuView
        _firstCollectionId = collectionId
        _firstCollectionName = collectionName
        _firstActionsCollection = actions
        _firstCollectionIndicatorTitle = collectionIndicatorTitle
        if (!CoachMarkPreference.hasShown(requireContext(), COACHMARK_WISHLIST_SHARING) && userVisibleHint) {
            showWishlistCollectionSharingCoachMark(anchorKebabMenuView, collectionId, collectionName, actions, collectionIndicatorTitle)
        }
    }

    override fun onCariBarangClicked() {
        RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
    }

    private fun showBottomSheetOnboarding() {
        bottomSheetOnboarding = BottomSheetOnboardingWishlistCollection.newInstance()
        bottomSheetOnboarding?.setListener(this@WishlistCollectionFragment)
        if (bottomSheetOnboarding?.isAdded == true || childFragmentManager.isStateSaved) return
        bottomSheetOnboarding?.show(childFragmentManager)
    }

    private fun showWishlistCollectionSharingCoachMark(
        anchorKebabMenuView: View,
        collectionId: String,
        collectionName: String,
        actions: List<GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Action>,
        collectionIndicatorTitle: String
    ) {
        if (coachMarkItemSharing1.isEmpty()) {
            coachMarkItemSharing1.add(
                CoachMark2Item(
                    anchorKebabMenuView,
                    "",
                    getString(R.string.collection_coachmark_wishlist_sharing_1),
                    CoachMark2.POSITION_BOTTOM
                )
            )
            coachMarkItemSharing1.add(
                CoachMark2Item(
                    anchorKebabMenuView,
                    "",
                    getString(R.string.collection_coachmark_wishlist_sharing_1),
                    CoachMark2.POSITION_BOTTOM
                )
            )
        }
        if (coachMarkSharing1 == null) {
            coachMarkSharing1 = CoachMark2(requireContext())
        }

        coachMarkSharing1?.let {
            it.onFinishListener = {
                showBottomSheetKebabMenu(
                    collectionId,
                    collectionName,
                    actions,
                    collectionIndicatorTitle
                )
            }

            it.stepButtonTextLastChild =
                getString(R.string.collection_coachmark_lanjut)

            if (!it.isShowing && coachMarkItemSharing1.isNotEmpty()) {
                it.showCoachMark(coachMarkItemSharing1, null, 1)
                it.stepPrev?.visibility = View.GONE
                it.stepPagination?.visibility = View.GONE
            }
        }
    }

    private fun showCoachmarkKebabItem2(view: View) {
        if (!userVisibleHint) return
        if (coachMarkItemSharing2.isEmpty()) {
            coachMarkItemSharing2.add(
                CoachMark2Item(
                    view,
                    "",
                    getString(R.string.collection_coachmark_wishlist_sharing_2),
                    CoachMark2.POSITION_TOP
                )
            )
            coachMarkItemSharing2.add(
                CoachMark2Item(
                    view,
                    "",
                    getString(R.string.collection_coachmark_wishlist_sharing_2),
                    CoachMark2.POSITION_TOP
                )
            )
        }
        if (coachMarkSharing2 == null) {
            coachMarkSharing2 = CoachMark2(requireContext())
        }

        coachMarkSharing2?.let {
            it.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    if (currentIndex == 0) {
                        coachMarkSharing2?.hideCoachMark()
                        bottomSheetKebabMenu?.dismiss()
                        _firstAnchorKebabMenuView?.let { it1 ->
                            showWishlistCollectionSharingCoachMark(
                                it1,
                                _firstCollectionId,
                                _firstCollectionName,
                                _firstActionsCollection,
                                _firstCollectionIndicatorTitle
                            )
                        }
                    }
                }
            })
            it.stepButtonTextLastChild =
                getString(R.string.collection_coachmark_finish)

            if (!it.isShowing && coachMarkItemSharing2.isNotEmpty()) {
                it.showCoachMark(coachMarkItemSharing2, null, 1)
                it.stepPrev?.visibility = View.GONE
                it.stepPagination?.visibility = View.GONE
            }
            CoachMarkPreference.setShown(requireContext(), COACHMARK_WISHLIST_SHARING, true)
        }
    }

    private fun showWishlistCollectionCoachMark(view1: View, view2: View) {
        if (!userVisibleHint) return
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
        if (coachMark == null) {
            coachMark = CoachMark2(requireContext())
        }

        coachMark?.let {
            it.onFinishListener = {
                showBottomSheetCreateNewCollection()
            }
            it.stepButtonTextLastChild =
                getString(R.string.collection_coachmark_try_create_wishlist)
            it.stepPrev?.text = getString(R.string.collection_coachmark_back)

            if (!it.isShowing && coachMarkItem.isNotEmpty()) {
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
        dialog?.setDescription(getString(R.string.wishlist_collection_detail_delete_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(R.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doDeleteCollection(collectionId)
        }
        dialog?.setSecondaryCTAText(getString(R.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun doDeleteCollection(collectionId: String) {
        collectionViewModel.deleteWishlistCollection(collectionId)
        WishlistCollectionAnalytics.sendClickHapusOnCollectionFolderEvent()
    }

    override fun onSuccessUpdateCollectionName(message: String) {
        showToasterActionOke(message, Toaster.TYPE_NORMAL)
        getWishlistCollections()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_COLLECTION_DETAIL || requestCode == EDIT_WISHLIST_COLLECTION_REQUEST_CODE) {
            getWishlistCollections()
            binding?.run { rvWishlistCollection.scrollToPosition(0) }

            val isSuccess = data?.getBooleanExtra(ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS, false)
            val messageToaster =
                data?.getStringExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER)
            if (messageToaster != null && isSuccess == true) {
                showToasterActionOke(messageToaster, Toaster.TYPE_NORMAL)
            }
        }
    }

    override fun onClickShowCoachmarkButton() {
        bottomSheetOnboarding?.dismiss()
        _allCollectionView?.let { v1 ->
            _createCollectionView?.let { v2 ->
                showWishlistCollectionCoachMark(v1, v2)
            }
        }
    }

    override fun onClickSkipOnboardingButton() {
        bottomSheetOnboarding?.dismiss()
    }

    override fun onRecommendationItemImpression(recommendationItem: RecommendationItem, position: Int) {
        if (recommendationItem.isTopAds) {
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
        if (recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                recommendationItem.clickUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        activity?.let {
            val intent = if (recommendationItem.appUrl.isNotEmpty()) {
                RouteManager.getIntent(it, recommendationItem.appUrl)
            } else {
                RouteManager.getIntent(
                    it,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    recommendationItem.productId.toString()
                )
            }
            startActivityForResult(intent, REQUEST_CODE_COLLECTION_DETAIL)
        }
    }

    private fun showLoader() {
        collectionAdapter.showLoader()
        binding?.run {
            wishlistCollectionStickyProgressDeletionWidget.rlDeletionProgress.gone()
        }
    }

    private fun showToasterActionOke(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(
                v,
                message,
                Toaster.LENGTH_LONG,
                type,
                getString(R.string.collection_CTA_oke)
            ) {}.show()
        }
    }
}
