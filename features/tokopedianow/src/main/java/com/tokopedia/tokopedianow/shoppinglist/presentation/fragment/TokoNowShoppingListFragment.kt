package com.tokopedia.tokopedianow.shoppinglist.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.home_component.customview.pullrefresh.ParentIconSwipeRefreshLayout
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowHeaderViewHolder
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowShoppingListBinding
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.shoppinglist.di.component.DaggerShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListContextModule
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.activity.TokoNowShoppingListActivity
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.ShoppingListAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.ShoppingListAdapterTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.resources.isDarkMode
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.searchbar.R as searchbarR

class TokoNowShoppingListFragment :
    Fragment(),
    TokoNowView,
    TokoNowChooseAddressWidgetListener,
    ScreenShotListener,
    ShareBottomsheetListener
{
    companion object {
        private const val PAGE_NAME = "Toko Now Shopping List"
        fun newInstance(): TokoNowShoppingListFragment = TokoNowShoppingListFragment()
    }

    /**
     * -- lateinit variable section --
     */

    @Inject
    lateinit var viewModel: TokoNowShoppingListViewModel

    /**
     * -- private variable section --
     */

    private val adapter: ShoppingListAdapter by lazy {
        ShoppingListAdapter(
            ShoppingListAdapterTypeFactory(
                tokoNowView = this@TokoNowShoppingListFragment,
                headerListener = headerCallback(),
                chooseAddressListener = this@TokoNowShoppingListFragment
            )
        )
    }

    private val userId: String
        get() = viewModel.getUserId()

    private var binding
        by autoClearedNullable<FragmentTokopedianowShoppingListBinding>()

    private var shareTokoNow: ShareTokonow? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector: ScreenshotDetector? = null

    /**
     * -- internal variable section --
     */

    internal val strRefreshLayout: ParentIconSwipeRefreshLayout?
        get() = binding?.strRefreshLayout

    internal val navToolbar: NavToolbar?
        get() = binding?.navToolbar

    /**
     * -- override function section --
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareTokoNow = createShareTokoNow()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowShoppingListBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchToDarkStatusBar()

        setupRecyclerView()
        setupNavigationToolbar()
        setupOnScrollListener()

        observeLiveData()

        loadFirstPage()
    }

    override fun onResume() {
        super.onResume()
        screenshotDetector?.start()
    }

    override fun onStop() {
        SharingUtil.clearState(screenshotDetector)
        super.onStop()
    }

    override fun onDestroy() {
        SharingUtil.clearState(screenshotDetector)
        super.onDestroy()
    }

    override fun screenShotTaken(path: String) {
        updateShareHomeData(
            isScreenShot = true,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_share_thumbnail_title_ss).orEmpty()
        )

        showUniversalShareBottomSheet(shareTokoNow, path)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        TokoNowUniversalShareUtil.shareOptionRequest(
            shareModel = shareModel,
            shareTokoNowData = shareTokoNow,
            activity = activity,
            view = view,
            onSuccess = {
                universalShareBottomSheet?.dismiss()
            }
        )
    }

    override fun onCloseOptionClicked() { /* do nothing */ }

    /**
     * -- private function section --
     */

    private fun initInjector() {
        DaggerShoppingListComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .shoppingListContextModule(ShoppingListContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun loadFirstPage() {
        binding?.navToolbar?.let { navToolbar ->
            navToolbar.post {
                setHeaderSpace(navToolbar)
                setHeaderModel()
                viewModel.loadFirstPage()
            }
        }
    }

    private fun setHeaderSpace(
        navToolbar: NavToolbar
    ) {
        val navToolbarHeight = if (navToolbar.height.isZero()) context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero() else navToolbar.height
        viewModel.headerSpace = navToolbarHeight
    }

    private fun setHeaderModel() {
        context?.apply {
            viewModel.headerModel = HeaderModel(
                pageTitle = getString(R.string.tokopedianow_shopping_list_page_title),
                pageTitleColor = MethodChecker.getColor(this, unifyprinciplesR.color.Unify_NN0),
                ctaText = getString(R.string.tokopedianow_shopping_list_repurchase),
                ctaTextColor = MethodChecker.getColor(this, unifyprinciplesR.color.Unify_NN0),
                ctaChevronIsShown = true,
                ctaChevronColor = MethodChecker.getColor(this, unifyprinciplesR.color.Unify_NN0),
                backgroundGradientColor = TokoNowHeaderUiModel.GradientColor(
                    startColor = MethodChecker.getColor(this, unifyprinciplesR.color.Unify_GN500),
                    endColor = MethodChecker.getColor(this, unifyprinciplesR.color.Unify_GN400)
                )
            )
        }
    }

    private fun setupRecyclerView() {
        binding?.rvShoppingList?.apply {
            adapter = this@TokoNowShoppingListFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupNavigationToolbar() {
        binding?.navToolbar?.apply {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setupToolbarWithStatusBar(activity = requireActivity())
            setToolbarPageName(PAGE_NAME)
            setIcon(
                IconBuilder()
                    .addShare()
                    .addCart()
                    .addNavGlobal()
            )
            bringToFront()
        }
    }

    private fun shareClicked(shareTokoNow: ShareTokonow?) {
        if (SharingUtil.isCustomSharingEnabled(context)) {
            showUniversalShareBottomSheet(
                shareTokoNow = shareTokoNow
            )
        } else {
            LinkerManager.getInstance().executeShareRequest(
                TokoNowUniversalShareUtil.shareRequest(
                    context = context,
                    shareTokonow = shareTokoNow
                )
            )
        }
    }

    private fun observeLiveData() {
        viewModel.firstPage.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupOnScrollListener() {
        binding?.apply {
            val callback = createNavRecyclerViewOnScrollCallback(navToolbar)
            binding?.rvShoppingList?.addOnScrollListener(callback)
        }
    }

    private fun getNavToolbarHeight(navToolbar: NavToolbar): Int {
        val defaultHeight = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_default_toolbar_height).orZero()
        return if (navToolbar.height.isZero()) {
            defaultHeight
        } else {
            navToolbar.height
        }
    }

    private fun createShareTokoNow(): ShareTokonow {
        return ShareTokonow(
            thumbNailImage = TokoNowHomeFragment.THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            ogImageUrl = TokoNowHomeFragment.THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            linkerType = LinkerData.NOW_TYPE
        )
    }

    private fun showUniversalShareBottomSheet(shareTokoNow: ShareTokonow?, path: String? = null) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            setFeatureFlagRemoteConfigKey()
            enableDefaultShareIntent()
            path?.let {
                setImageOnlySharingOption(true)
                setScreenShotImagePath(path)
            }
            init(this@TokoNowShoppingListFragment)
            setUtmCampaignData(
                pageName = "Tokonow",
                userId = userId.getOrDefaultZeroString(),
                pageIdConstituents = shareTokoNow?.pageIdConstituents.orEmpty(),
                feature = "share"
            )
            setMetaData(
                tnTitle = shareTokoNow?.thumbNailTitle.orEmpty(),
                tnImage = shareTokoNow?.thumbNailImage.orEmpty()
            )

            // set the Image Url of the Image that represents page
            setLinkProperties(
                LinkProperties(
                    ogImageUrl = shareTokoNow?.ogImageUrl.orEmpty()
                )
            )
        }
        universalShareBottomSheet?.show(childFragmentManager, this, screenshotDetector)
    }

    private fun updateShareHomeData(isScreenShot: Boolean, thumbNailTitle: String) {
        shareTokoNow?.isScreenShot = isScreenShot
        shareTokoNow?.thumbNailTitle = thumbNailTitle
    }

    /**
     * -- internal function section --
     */

    internal fun switchToDarkStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToDarkToolbar()

    internal fun switchToLightStatusBar() = (activity as? TokoNowShoppingListActivity)?.switchToLightToolbar()

    internal fun IconBuilder.addNavGlobal(): IconBuilder = addIcon(
        iconId = IconList.ID_NAV_GLOBAL,
        disableRouteManager = false,
        disableDefaultGtmTracker = false
    ) { /* nothing to do */ }

    internal fun IconBuilder.addCart(): IconBuilder = addIcon(
        iconId = IconList.ID_CART,
        disableRouteManager = false,
        disableDefaultGtmTracker = true
    ) { /* nothing to do */ }

    internal fun IconBuilder.addShare() = addIcon(
        iconId = IconList.ID_SHARE,
        disableRouteManager = false,
        disableDefaultGtmTracker = false
    ) {
        updateShareHomeData(
            isScreenShot = false,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_share_thumbnail_title).orEmpty()
        )
        shareClicked(shareTokoNow)
    }

    internal fun NavToolbar.setBackButtonColor(color: Int) {
        context?.let {
            setCustomBackButton(color = ContextCompat.getColor(it, color))
        }
    }

    /**
     * -- callback function section --
     */
    private fun headerCallback() = object : TokoNowHeaderViewHolder.TokoNowHeaderListener {
        override fun onClickCtaHeader() {
            RouteManager.route(
                context,
                ApplinkConstInternalTokopediaNow.REPURCHASE
            )
        }

        override fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView) {
            strRefreshLayout?.setContentChildViewPullRefresh(view)
        }
    }

    private fun createNavRecyclerViewOnScrollCallback(
        navToolbar: NavToolbar
    ): RecyclerView.OnScrollListener {
        val transitionRange = context?.resources?.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range).orZero()
        return NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = getNavToolbarHeight(navToolbar) - transitionRange - transitionRange,
            toolbarTransitionRangePixel = transitionRange,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */ }

                override fun onSwitchToLightToolbar() {
                    navToolbar.setBackButtonColor(if (navToolbar.context.isDarkMode()) unifyprinciplesR.color.Unify_Static_White else searchbarR.color.searchbar_dms_state_light_icon)
                    switchToLightStatusBar()
                    navToolbar.setToolbarTitle(getString(R.string.tokopedianow_shopping_list_page_title))
                    navToolbar.removeShareIconView()
                }

                override fun onYposChanged(yOffset: Int) { /* nothing to do */ }

                override fun onSwitchToDarkToolbar() {
                    if (navToolbar.context.isDarkMode()) {
                        navToolbar.setBackButtonColor(unifyprinciplesR.color.Unify_Static_White)
                        switchToLightStatusBar()
                    } else {
                        navToolbar.setBackButtonColor(unifyprinciplesR.color.Unify_Static_White)
                        switchToDarkStatusBar()
                    }
                    navToolbar.setToolbarTitle(String.EMPTY)
                    navToolbar.hideShadow()
                    navToolbar.setIcon(
                        IconBuilder()
                            .addShare()
                            .addCart()
                            .addNavGlobal()
                    )
                }
            }
        )
    }

    override fun getFragmentPage(): Fragment = this@TokoNowShoppingListFragment

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() {  }

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) { }

    override fun onChooseAddressWidgetRemoved() { }

    override fun onClickChooseAddressWidgetTracker() { }
}
