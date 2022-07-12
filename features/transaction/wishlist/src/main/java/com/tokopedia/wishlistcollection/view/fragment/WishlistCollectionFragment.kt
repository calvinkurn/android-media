package com.tokopedia.wishlistcollection.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.CollectionWishlistResponse
import com.tokopedia.wishlist.databinding.FragmentCollectionWishlistBinding
import com.tokopedia.wishlistcollection.di.WishlistCollectionModule
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_CREATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_TICKER
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment
import com.tokopedia.wishlistcollection.di.DaggerWishlistCollectionComponent
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetKebabMenuWishlistCollectionItem
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionViewModel
import javax.inject.Inject


class WishlistCollectionFragment : BaseDaggerFragment(), WishlistCollectionAdapter.ActionListener {
    private var binding by autoClearedNullable<FragmentCollectionWishlistBinding>()
    private lateinit var collectionAdapter: WishlistCollectionAdapter
    private var activityWishlistCollection = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val collectionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WishlistCollectionViewModel::class.java]
    }
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
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
    }

    private fun prepareLayout() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        setToolbarTitle(DEFAULT_TITLE)
        setSwipeRefreshLayout()
        collectionAdapter = WishlistCollectionAdapter().apply {
            setActionListener(this@WishlistCollectionFragment)
        }
        binding?.run {
            activityWishlistCollection = arguments?.getString(PARAM_ACTIVITY_WISHLIST_COLLECTION, "") ?: ""

            val pageSource: String
            val icons: IconBuilder
            viewLifecycleOwner.lifecycle.addObserver(wishlistCollectionNavtoolbar)
            if(activityWishlistCollection != PARAM_HOME) {
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

            rvWishlistCollection.apply {
                layoutManager = GridLayoutManager(context, 2).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (collectionAdapter.getItemViewType(position)) {
                                WishlistCollectionAdapter.LAYOUT_COLLECTION_TICKER -> 2
                                else -> 1
                            }
                        }
                    }
                }
                adapter = collectionAdapter
            }
        }
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
                        collectionAdapter.addList(mapCollection(result.data.data))
                    } else {
                        // TODO: show global error page?
                        val errorMessage = result.data.errorMessage.first().ifEmpty { context?.getString(
                            R.string.wishlist_v2_common_error_msg) }
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

    private fun mapCollection(data: CollectionWishlistResponse.Data.GetWishlistCollections.WishlistCollectionResponseData): List<WishlistCollectionTypeLayoutData> {
        val listCollection = arrayListOf<WishlistCollectionTypeLayoutData>()
        val tickerObject = WishlistCollectionTypeLayoutData(data.ticker, TYPE_COLLECTION_TICKER)
        listCollection.add(tickerObject)

        data.collections.forEach { item ->
            val collectionItemObject = WishlistCollectionTypeLayoutData(item, TYPE_COLLECTION_ITEM)
            listCollection.add(collectionItemObject)
        }

        val createNewItem = WishlistCollectionTypeLayoutData(data.placeholder, TYPE_COLLECTION_CREATE)
        listCollection.add(createNewItem)

        return listCollection
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

    override fun onKebabMenuClicked() {
        showBottomSheetKebabMenu()
    }

    private fun showBottomSheetKebabMenu() {
        val bottomSheetKebabMenu = BottomSheetKebabMenuWishlistCollectionItem.newInstance()
        if (bottomSheetKebabMenu.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetKebabMenu.show(childFragmentManager)
    }

    override fun onCreateNewCollectionClicked() {
        showBottomSheetCreateNewCollection(childFragmentManager)
    }

    private fun showBottomSheetCreateNewCollection(fragmentManager: FragmentManager) {
        val bottomSheetCreateCollection = BottomSheetCreateNewCollectionWishlist.newInstance("")
        if (bottomSheetCreateCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCreateCollection.show(fragmentManager)
    }

    override fun onCollectionItemClicked(id: String) {
        val detailCollection = "${ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL}?${ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID}=$id"
        val intentCollectionDetail = RouteManager.getIntent(context, detailCollection)
        startActivity(intentCollectionDetail)
    }

    override fun onCreateCollectionItemBind(allCollectionView: View, createCollectionView: View) {
        showWishlistCollectionCoachMark(allCollectionView, createCollectionView)
    }

    private fun showWishlistCollectionCoachMark(view1: View, view2: View) {
        val coachMarkItem = ArrayList<CoachMark2Item>()
        val coachMark = CoachMark2(requireContext())
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
        coachMark.onFinishListener = {
            showBottomSheetCreateNewCollection(childFragmentManager)
        }
        coachMark.stepButtonTextLastChild = getString(R.string.collection_coachmark_try_create_wishlist)
        coachMark.stepPrev?.text = getString(R.string.collection_coachmark_back)
        coachMark.showCoachMark(coachMarkItem, null)
    }
}