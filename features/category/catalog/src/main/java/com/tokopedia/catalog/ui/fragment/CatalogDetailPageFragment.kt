package com.tokopedia.catalog.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogAnimationListener
import com.tokopedia.catalog.adapter.CatalogDetailAdapter
import com.tokopedia.catalog.adapter.CatalogDetailDiffUtil
import com.tokopedia.catalog.adapter.CatalogLinearLayoutManager
import com.tokopedia.catalog.adapter.decorators.DividerItemDecorator
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactoryImpl
import com.tokopedia.catalog.analytics.CatalogDetailAnalytics
import com.tokopedia.catalog.analytics.CatalogUniversalShareAnalytics
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogComparisonNewDataModel
import com.tokopedia.catalog.model.datamodel.CatalogFullSpecificationDataModel
import com.tokopedia.catalog.model.raw.*
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.model.util.CatalogUiUpdater
import com.tokopedia.catalog.model.util.CatalogUtil
import com.tokopedia.catalog.model.util.nestedrecyclerview.NestedRecyclerView
import com.tokopedia.catalog.ui.activity.CatalogGalleryActivity
import com.tokopedia.catalog.ui.activity.CatalogYoutubePlayerActivity
import com.tokopedia.catalog.ui.bottomsheet.CatalogComponentBottomSheet
import com.tokopedia.catalog.ui.bottomsheet.CatalogPreferredProductsBottomSheet
import com.tokopedia.catalog.ui.bottomsheet.CatalogSpecsAndDetailBottomSheet
import com.tokopedia.catalog.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalog.viewmodel.CatalogDetailProductListingViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_catalog_detail_page.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogDetailPageFragment :
    Fragment(),
    HasComponent<CatalogComponent>,
    CatalogDetailListener,
    ShareBottomsheetListener,
    ScreenShotListener,
    PermissionListener,
    CatalogAnimationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var catalogDetailPageViewModel: CatalogDetailPageViewModel

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var sharedViewModel: CatalogDetailProductListingViewModel

    private val widgetTrackingSet = HashSet<String>()

    private var catalogUiUpdater: CatalogUiUpdater = CatalogUiUpdater(mutableMapOf())
    private var fullSpecificationDataModel = CatalogFullSpecificationDataModel(arrayListOf())

    private var catalogId: String = ""
    private var catalogUrl: String = ""
    private var catalogName: String = ""
    private var catalogBrand: String = ""
    private var catalogDepartmentId: String = ""
    private var catalogImages = arrayListOf<CatalogImage>()
    private var comparisonCatalogId: String = ""
    private var recommendedCatalogId: String = ""

    private var navToolbar: NavToolbar? = null
    private var cartLocalCacheHandler: LocalCacheHandler? = null

    private var catalogPageRecyclerView: NestedRecyclerView? = null
    private var catalogLinearLayoutManager: CatalogLinearLayoutManager? = null
    private var shimmerLayout: ScrollView? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null
    private var mToBottomLayout: LinearLayout? = null
    private var mProductsCountText: Typography? = null
    private var mToTopLayout: LinearLayout ? = null

    private lateinit var userSession: UserSession

    private val catalogAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { CatalogDetailAdapterFactoryImpl(this) }

    private val catalogDetailAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel> = AsyncDifferConfig.Builder(CatalogDetailDiffUtil())
            .build()
        CatalogDetailAdapter(
            requireActivity(),
            this,
            catalogId,
            asyncDifferConfig,
            catalogAdapterFactory
        )
    }

    var isBottomSheetOpen = false
    private var lastDetachedItemPosition: Int = 0
    private var lastAttachItemPosition: Int = 0
    private var isScrollDownButtonClicked = false

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        const val CATALOG_DETAIL_PAGE_FRAGMENT_TAG = "CATALOG_DETAIL_PAGE_FRAGMENT_TAG"
        fun newInstance(catalogId: String): CatalogDetailPageFragment {
            val fragment = CatalogDetailPageFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getComponent(): CatalogComponent {
        return DaggerCatalogComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_catalog_detail_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectComponents()
        initViews(view)
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
        }
        activity?.let { observer ->
            userSession = UserSession(observer)
            val viewModelProvider = ViewModelProvider(observer, viewModelFactory)
            catalogDetailPageViewModel = viewModelProvider.get(CatalogDetailPageViewModel::class.java)
            catalogDetailPageViewModel.getProductCatalog(catalogId, comparisonCatalogId, userSession.userId, CatalogConstant.DEVICE)
            catalogDetailPageViewModel.fetchProductListing(CatalogUtil.getProductsParams(catalogId))
            showShimmer()
        }

        setupRecyclerView(view)
        setObservers()
        setUpUniversalShare()
        setUpAnimationViews()
    }

    private fun injectComponents() {
        component.inject(this)
        activity?.let { observer ->
            val viewModelProvider = ViewModelProvider(observer, viewModelFactory)
            sharedViewModel = viewModelProvider.get(CatalogDetailProductListingViewModel::class.java)
        }
    }

    private fun setUpAnimationViews() {
        mToBottomLayout?.apply {
            setOnClickListener {
                isScrollDownButtonClicked = true
                val userPressedLastTopPosition = if ((catalogPageRecyclerView?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == RecyclerView.NO_POSITION) {
                    (catalogPageRecyclerView?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                } else {
                    (catalogPageRecyclerView?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                }
                catalogLinearLayoutManager?.scrollToBottom(userPressedLastTopPosition)
                CatalogDetailAnalytics.sendEvent(
                    CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                    CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                    CatalogDetailAnalytics.ActionKeys.CLICK_FLOATING_BUTTON_PRODUCT,
                    "$catalogName - $catalogId",
                    userSession.userId,
                    catalogId
                )
                slideDownMoreProductsView()
            }
        }

        mToTopLayout?.apply {
            setOnClickListener {
                catalogLinearLayoutManager?.scrollToTop()
                CatalogDetailAnalytics.sendEvent(
                    CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                    CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                    CatalogDetailAnalytics.ActionKeys.CLICK_FLOATING_BUTTON_LAST_SCROLL,
                    "$catalogName - $catalogId",
                    userSession.userId,
                    catalogId
                )
                slideUpMoreProductsView()
            }
        }
    }

    private fun initViews(parentView: View) {
        shimmerLayout = view?.findViewById(R.id.shimmer_layout)
        activity?.let {
            cartLocalCacheHandler = LocalCacheHandler(it, CatalogConstant.CART_LOCAL_CACHE_NAME)
        }
        mToBottomLayout = parentView.findViewById(R.id.toBottomLayout)
        mProductsCountText = parentView.findViewById(R.id.products_count_text)
        mToTopLayout = parentView.findViewById(R.id.toTopLayout)
        initNavToolbar()
        bottom_sheet_fragment_container.hide()
    }

    private fun setUpUniversalShare() {
        context?.let {
            screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
                it,
                this,
                this,
                addFragmentLifecycleObserver = true,
                permissionListener = this
            )
        }
    }

    private fun setUpBottomSheet() {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.bottom_sheet_fragment_container,
            CatalogPreferredProductsBottomSheet.newInstance(
                catalogName,
                catalogId,
                catalogUrl,
                catalogDepartmentId,
                catalogBrand
            ),
            CatalogPreferredProductsBottomSheet.PREFFERED_PRODUCT_BOTTOMSHEET_TAG
        ).commit()

        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_fragment_container)
        mBottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    isBottomSheetOpen = false
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    isBottomSheetOpen = true
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    if (!isBottomSheetOpen) {
                        CatalogDetailAnalytics.sendEvent(
                            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                            CatalogDetailAnalytics.ActionKeys.DRAG_IMAGE_KNOB,
                            "$catalogName - $catalogId",
                            userSession.userId,
                            catalogId
                        )
                    }
                }
            }
        })
    }

    private fun setObservers() {
        catalogDetailPageViewModel.getCatalogResponseData().observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        it.data.listOfComponents.forEach { component ->
                            catalogUiUpdater.updateModel(component)
                            if (component is CatalogComparisonNewDataModel && comparisonCatalogId.isBlank()) {
                                recommendedCatalogId = component.specsList?.firstOrNull()?.subcard?.firstOrNull()?.featureRightData?.id ?: ""
                            }
                        }

                        catalogUrl = catalogUiUpdater.productInfoMap?.url ?: ""
                        fullSpecificationDataModel = it.data.fullSpecificationDataModel
                        catalogImages = catalogUiUpdater.productInfoMap?.images ?: arrayListOf()
                        catalogName = catalogUiUpdater.productInfoMap?.productName ?: ""
                        catalogBrand = catalogUiUpdater.productInfoMap?.productBrand ?: ""
                        catalogDepartmentId = catalogUiUpdater.productInfoMap?.departmentId ?: ""
                        if (comparisonCatalogId.isNotBlank()) {
                            recommendedCatalogId = catalogUiUpdater.productInfoMap?.comparisonInfoCatalogId ?: ""
                        }
                        updateUi()
                        setCatalogUrlForTracking()
                    }
                    is Fail -> {
                        onError(it.throwable)
                    }
                }
            }
        )

        observerProductCount()
        observerSharedProductCount()
    }

    private fun observerProductCount() {
        catalogDetailPageViewModel.mProductCount.observe(viewLifecycleOwner, { totalProducts ->
            totalProducts?.let {
                sharedViewModel.mProductCount.value = it
            }
        })
    }

    private fun observerSharedProductCount() {
        sharedViewModel.mProductCount.observe(viewLifecycleOwner, { filterProductCount ->
            filterProductCount?.let {
                setProductCountText(it)
            }
        })
    }

    private fun setProductCountText(productCount: Int) {
        if (productCount == CatalogConstant.ZERO_VALUE) {
            mProductsCountText?.text = getString(
                com.tokopedia.catalog.R.string.catalog_product_count_view_text_empty
            )
            mToBottomLayout?.hide()
        } else {
            mProductsCountText?.text = getString(
                com.tokopedia.catalog.R.string.catalog_product_count_view_text,
                productCount
            )
        }
    }

    private fun setCatalogUrlForTracking() {
        activity?.supportFragmentManager?.findFragmentByTag(CatalogPreferredProductsBottomSheet.PREFFERED_PRODUCT_BOTTOMSHEET_TAG)?.let { fragment ->
            if (fragment is CatalogPreferredProductsBottomSheet) {
                fragment.setData(catalogName, catalogUrl, catalogId, catalogDepartmentId, catalogBrand)
            }
        }
    }

    private fun onError(e: Throwable) {
        shimmerLayout?.hide()
        catalogPageRecyclerView?.hide()
        bottom_sheet_fragment_container.hide()
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            global_error.setType(GlobalError.NO_CONNECTION)
        } else {
            global_error.setType(GlobalError.SERVER_ERROR)
        }

        global_error.show()
        global_error.setOnClickListener {
            catalogPageRecyclerView?.show()
            shimmerLayout?.show()
            bottom_sheet_fragment_container.show()
            global_error.hide()
            catalogDetailPageViewModel.getProductCatalog(catalogId, comparisonCatalogId, userSession.userId, CatalogConstant.DEVICE)
        }
    }

    private fun initNavToolbar() {
        navToolbar = view?.findViewById(R.id.catalog_navtoolbar)
        navToolbar?.apply {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                IconBuilder()
                    .addIcon(IconList.ID_SHARE) {
                        generateCatalogShareData(catalogId, true)
                    }
                    .addIcon(IconList.ID_CART) {}
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            setupSearchbar(listOf(HintData(context.getString(R.string.catalog_nav_bar_search_hint))))
            setBadgeCounter(IconList.ID_CART, getCartCounter())
            setOnBackButtonClickListener {
                CatalogDetailAnalytics.sendEvent(
                    CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                    CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                    CatalogDetailAnalytics.ActionKeys.CLICK_BACK_BUTTON,
                    "$catalogName - $catalogId",
                    userSession.userId,
                    catalogId
                )
                activity?.onBackPressed()
            }
            show()
        }
    }

    private fun getCartCounter(): Int {
        return cartLocalCacheHandler?.getInt(CatalogConstant.TOTAL_CART_CACHE_KEY, 0).orZero()
    }

    private fun showShimmer() {
        if (catalogUiUpdater.mapOfData.size ?: 0 == 0) {
            shimmerLayout?.show()
        }
    }

    private fun hideShimmer() {
        if (catalogUiUpdater.mapOfData.size ?: 0 > 0) {
            shimmerLayout?.hide()
        }
    }

    private fun updateUi() {
        hideShimmer()
        catalogPageRecyclerView?.show()
        catalogLinearLayoutManager?.lastComponentIndex = catalogUiUpdater.mapOfData.size - 1
        val newData = catalogUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitables: List<BaseCatalogDataModel>) {
        catalogDetailAdapter.submitList(visitables)
    }

    private fun setupRecyclerView(view: View) {
        catalogPageRecyclerView = view.findViewById(R.id.catalog_detail_rv)
        catalogPageRecyclerView?.apply {
            CatalogLinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false).also {
                catalogLinearLayoutManager = it
                layoutManager = it
            }
            setHasFixedSize(true)
            setNestedCanScroll(true)
            itemAnimator = null
            ContextCompat.getDrawable(context, R.drawable.catalog_divider)?.let {
                addItemDecoration(DividerItemDecorator(it))
            }
            adapter = catalogDetailAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > CatalogLinearLayoutManager.MINIMUM_SCROLL_FOR_ANIMATION.toPx()) {
                        slideUpMoreProductsView()
                    }
                }
            })
        }
        catalogLinearLayoutManager?.setCatalogAnimationListener(this)
    }

    private fun slideUpMoreProductsView() {
        if (sharedViewModel.mProductCount.value != 0 && mToTopLayout?.visibility != View.VISIBLE && mToBottomLayout?.visibility != View.VISIBLE) {
            val slideUp: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            mToBottomLayout?.startAnimation(slideUp)
            mToBottomLayout?.visibility = View.VISIBLE
        }
        catalogLinearLayoutManager?.removeOldAnimation { slideDownMoreProductsView() }
    }

    private fun slideDownMoreProductsView() {
        if (sharedViewModel.mProductCount.value != 0 && mToTopLayout?.visibility != View.VISIBLE && mToBottomLayout?.visibility != View.INVISIBLE) {
            mToBottomLayout?.startAnimation(catalogLinearLayoutManager?.getProductCountViewSlideDownAnimation())
            mToBottomLayout?.visibility = View.INVISIBLE
        }
    }

    private fun slideUpToTopView() {
        mToTopLayout?.startAnimation(catalogLinearLayoutManager?.getSlideUpAnimation())
        mToTopLayout?.visibility = View.VISIBLE
    }

    private fun slideDownToTopView() {
        mToTopLayout?.startAnimation(catalogLinearLayoutManager?.getSlideDownAnimation())
        mToTopLayout?.visibility = View.INVISIBLE
    }

    private fun viewMoreClicked(openPage: String, jumpToFullSpecIndex: Int = 0) {
        val catalogSpecsAndDetailView = CatalogSpecsAndDetailBottomSheet.newInstance(
            catalogName,
            catalogId,
            catalogUiUpdater.productInfoMap?.description ?: "",
            fullSpecificationDataModel.fullSpecificationsList,
            openPage,
            jumpToFullSpecIndex
        )
        catalogSpecsAndDetailView.show(childFragmentManager, "")
    }

    private fun generateCatalogShareData(catalogId: String, isUniversalShare: Boolean = false) {
        val linkerShareData = linkerDataMapper(catalogId)
        onCatalogShareButtonClicked(isUniversalShare, linkerShareData)
    }

    private fun onCatalogShareButtonClicked(isUniversalShare: Boolean = false, linkerShareData: LinkerShareData) {
        if (isUniversalShare) {
            CatalogUniversalShareAnalytics.navBarShareButtonClickedGTM(catalogId, userSession.userId)
            showUniversalShareBottomSheet(catalogImages)
        } else {
            CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_SHARE,
                "$catalogName - $catalogId",
                userSession.userId,
                catalogId
            )
            CatalogUtil.shareData(
                requireActivity(),
                linkerShareData.linkerData.description,
                linkerShareData.linkerData.uri
            )
        }
    }

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector: ScreenshotDetector? = null
    private var shareType: Int = 1

    private fun showUniversalShareBottomSheet(catalogImages: ArrayList<CatalogImage>) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@CatalogDetailPageFragment)
            setUtmCampaignData(
                CatalogConstant.CATALOG,
                if (UserSession(this@CatalogDetailPageFragment.context).userId.isNullOrEmpty()) {
                    "0"
                } else {
                    UserSession(this@CatalogDetailPageFragment.context).userId
                },
                catalogId,
                CatalogConstant.CATALOG_SHARE
            )
            setMetaData(
                "${CatalogConstant.KATALOG} $catalogName",
                catalogImages.firstOrNull()?.imageURL ?: "",
                "",
                CatalogUtil.getImagesFromCatalogImages(catalogImages)
            )
            setOgImageUrl(catalogImages.firstOrNull()?.imageURL ?: "")
        }
        universalShareBottomSheet?.show(
            requireActivity().supportFragmentManager,
            this@CatalogDetailPageFragment,
            screenshotDetector
        )
        shareType = UniversalShareBottomSheet.getShareBottomSheetType()
        if (UniversalShareBottomSheet.getShareBottomSheetType() == UniversalShareBottomSheet.CUSTOM_SHARE_SHEET) {
            CatalogUniversalShareAnalytics.shareBottomSheetAppearGTM(catalogId, userSession.userId)
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val linkerShareData = linkerDataMapper(catalogId)
        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if (shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        if (UniversalShareBottomSheet.getShareBottomSheetType() == UniversalShareBottomSheet.CUSTOM_SHARE_SHEET) {
            CatalogUniversalShareAnalytics.sharingChannelSelectedGTM(shareModel.channel ?: "", catalogId, userSession.userId)
        } else {
            CatalogUniversalShareAnalytics.sharingChannelScreenShotSelectedGTM(shareModel.channel ?: "", catalogId, userSession.userId)
        }

        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        context?.resources?.getString(
                            com.tokopedia.catalog.R.string.catalog_share_string,
                            catalogName,
                            linkerShareData?.url
                        )?.let { shareString ->
                            SharingUtil.executeShareIntent(
                                shareModel,
                                linkerShareData,
                                activity,
                                view,
                                shareString
                            )
                            universalShareBottomSheet?.dismiss()
                        }
                    }

                    override fun onError(linkerError: LinkerError?) {
                        universalShareBottomSheet?.dismiss()
                        generateCatalogShareData(catalogId, false)
                    }
                }
            )
        )
    }

    override fun onCloseOptionClicked() {
        if (UniversalShareBottomSheet.getShareBottomSheetType() == UniversalShareBottomSheet.CUSTOM_SHARE_SHEET) {
            CatalogUniversalShareAnalytics.dismissShareBottomSheetGTM(catalogId, userSession.userId)
        } else {
            CatalogUniversalShareAnalytics.userClosesScreenShotBottomSheetGTM(catalogId, userSession.userId)
        }
        universalShareBottomSheet?.dismiss()
    }

    override fun screenShotTaken() {
        CatalogUniversalShareAnalytics.userTakenScreenShotGTM(catalogId, userSession.userId)
        showUniversalShareBottomSheet(catalogImages)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenshotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    override fun permissionAction(action: String, label: String) {
        CatalogUniversalShareAnalytics.allowPopupGTM(label, catalogId, userSession.userId)
    }

    private fun linkerDataMapper(catalogId: String): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = catalogId
        linkerData.type = LinkerData.CATALOG_TYPE
        linkerData.name = getString(com.tokopedia.catalog.R.string.catalog_share_link_name, catalogName)
        linkerData.uri = CatalogUtil.getShareURI(catalogUrl)
        linkerData.description = getString(com.tokopedia.catalog.R.string.catalog_share_link_description)
        linkerData.isThrowOnError = true
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun showImage(currentItem: Int) {
        catalogUiUpdater.run {
            productInfoMap?.let {
                if (catalogUiUpdater.productInfoMap?.images?.isNotEmpty() == true) {
                    context?.startActivity(CatalogGalleryActivity.newIntent(context, catalogName, catalogId, currentItem, catalogUiUpdater.productInfoMap!!.images!!))
                }
            }
        }
    }

    override fun onProductImageClick(catalogImage: CatalogImage, position: Int) {
        showImage(position)
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.EventKeys.EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_CATALOG_IMAGE,
            "$catalogName - $catalogId",
            userSession.userId,
            catalogId
        )
    }

    override fun onViewMoreSpecificationsClick(topModel: TopSpecificationsComponentData?) {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_MORE_SPECIFICATIONS,
            "$catalogName - $catalogId",
            userSession.userId,
            catalogId
        )
        var positionInFullSpecs = 0
        topModel?.let { safeTopModel ->
            fullSpecificationDataModel.fullSpecificationsList.forEachIndexed {
                    index, fullSpecificationsComponentData ->
                if (safeTopModel.key == fullSpecificationsComponentData.name) {
                    positionInFullSpecs = index
                }
            }
        }
        viewMoreClicked(CatalogSpecsAndDetailBottomSheet.SPECIFICATION, jumpToFullSpecIndex = positionInFullSpecs)
    }

    override fun playVideo(catalogVideo: VideoComponentData, position: Int) {
        context?.let {
            CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_VIDEO_WIDGET,
                "$catalogName - $catalogId",
                userSession.userId,
                catalogId
            )
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                == YouTubeInitializationResult.SUCCESS
            ) {
                catalogVideo.url?.let { videoUrl ->
                    // Sending only one video so selectedIndex to be 0 always
                    startActivity(CatalogYoutubePlayerActivity.createIntent(it, listOf(videoUrl), 0))
                }
            } else {
                // Handle if user didn't have any apps to open Youtube * Usually rooted phone
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(CatalogConstant.URL_YOUTUBE + catalogVideo.videoId)
                        )
                    )
                } catch (e: Throwable) {
                }
            }
        }
    }

    override fun comparisonCatalogClicked(comparisonCatalogId: String) {
        context?.let {
            CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_COMPARISION_CATALOG,
                "origin: $catalogName - $catalogId - destination: $comparisonCatalogId",
                userSession.userId,
                catalogId
            )
            RouteManager.route(it, "${CatalogConstant.CATALOG_URL}$comparisonCatalogId")
        }
    }

    override fun openComparisonBottomSheet(comparisonCatalog: ComparisionModel?) {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_GANTI_PERBANDINGAN,
            "catalog page: $catalogId | catalog comparison: ${comparisonCatalog?.id ?: ""}",
            userSession.userId,
            catalogId
        )

        val catalogComparisonBottomSheet = CatalogComponentBottomSheet.newInstance(
            catalogName,
            catalogId,
            catalogBrand,
            catalogDepartmentId,
            recommendedCatalogId,
            CatalogComponentBottomSheet.ORIGIN_ULTIMATE_VERSION,
            this
        )
        catalogComparisonBottomSheet.show(childFragmentManager, "")
    }

    override fun openComparisonNewBottomSheet(comparisonNewModel: ComparisonNewModel?) {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_GANTI_PERBANDINGAN,
            "catalog page: $catalogId | catalog comparison: ${comparisonNewModel?.id ?: ""}",
            userSession.userId,
            catalogId,
            CatalogDetailAnalytics.TrackerId.OPEN_BOTTOMSHEET
        )

        val catalogComparisonBottomSheet = CatalogComponentBottomSheet.newInstance(
            catalogName,
            catalogId,
            catalogBrand,
            catalogDepartmentId,
            recommendedCatalogId,
            CatalogComponentBottomSheet.ORIGIN_ULTIMATE_VERSION,
            this
        )
        catalogComparisonBottomSheet.show(childFragmentManager, "")
    }

    override fun comparisonNewCatalogClicked(comparisonCatalogId: String) {
        context.let {
            CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_NEXT_CATALOG_PAGE_PERBANDINGAN_PRODUK,
                "catalog page: $catalogId | next catalog page: $comparisonCatalogId",
                userSession.userId,
                catalogId,
                CatalogDetailAnalytics.TrackerId.CLICK_NEXT_CATALOG_PAGE
            )
            RouteManager.route(it, "${CatalogConstant.CATALOG_URL}$comparisonCatalogId")
        }
    }

    override fun accordionDropUp(tabName: String?) {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_DROP_UP_BUTTON_PERBANDINGAN_PRODUK,
            "$catalogName - $catalogId - tabName: $tabName",
            userSession.userId,
            catalogId,
            CatalogDetailAnalytics.TrackerId.CLICK_DROP_UP_BUTTON
        )
    }

    override fun accordionDropDown(tabName: String?) {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_DROP_DOWN_BUTTON_PERBANDINGAN_PRODUK,
            "$catalogName - $catalogId - tabName: $tabName",
            userSession.userId,
            catalogId,
            CatalogDetailAnalytics.TrackerId.CLICK_DROP_DOWN_BUTTON
        )
    }

    override fun changeComparison(comparedCatalogId: String) {
        comparisonCatalogId = comparedCatalogId
        recommendedCatalogId = comparedCatalogId
        catalogDetailPageViewModel.getProductCatalog(catalogId, comparedCatalogId, userSession.userId, CatalogConstant.DEVICE)
    }

    override fun readMoreReviewsClicked(catalogId: String) {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_ON_LIHAT_SEMUA_REVIEW,
            "$catalogName - $catalogId",
            userSession.userId,
            catalogId
        )
        val catalogAllReviewBottomSheet = CatalogComponentBottomSheet.newInstance(
            catalogName,
            catalogId,
            "",
            "",
            "",
            CatalogComponentBottomSheet.ORIGIN_ALL_REVIEWS,
            this
        )
        catalogAllReviewBottomSheet.show(childFragmentManager, "")
    }

    override fun onReviewImageClicked(
        position: Int,
        items: ArrayList<CatalogImage>,
        reviewId: String,
        isFromBottomSheet: Boolean
    ) {
        if (isFromBottomSheet) {
            CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_IMAGE_ON_LIST_REVIEW,
                "$catalogName - $catalogId - $reviewId",
                userSession.userId,
                catalogId
            )
        } else {
            CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_IMAGE_ON_REVIEW,
                "$catalogName - $catalogId - $reviewId",
                userSession.userId,
                catalogId
            )
        }
        context?.startActivity(
            CatalogGalleryActivity.newIntent(
                context,
                catalogName,
                catalogId,
                position,
                items,
                showBottomGallery = false,
                showNumbering = true,
                reviewId
            )
        )
    }

    override fun hideFloatingLayout() {
        slideDownMoreProductsView()
        slideUpToTopView()
    }

    override fun showFloatingLayout() {
        slideDownToTopView()
        slideUpMoreProductsView()
    }

    override fun onViewMoreDescriptionClick() {
        CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_MORE_DESCRIPTION,
            "$catalogName - $catalogId",
            userSession.userId,
            catalogId
        )
        viewMoreClicked(CatalogSpecsAndDetailBottomSheet.DESCRIPTION)
    }

    override fun sendWidgetImpressionEvent(
        widgetImpressionActionName: String,
        widgetImpressionItemName: String,
        adapterPosition: Int
    ) {
        val uniqueTrackingKey = "$widgetImpressionActionName-$adapterPosition"
        if (!widgetTrackingSet.contains(uniqueTrackingKey)) {
            CatalogDetailAnalytics.sendImpressionEventInQueue(
                trackingQueue,
                CatalogDetailAnalytics.EventKeys.EVENT_PROMO_VIEW,
                widgetImpressionActionName,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                "$catalogName - $catalogId",
                catalogId,
                adapterPosition,
                userSession.userId,
                catalogId,
                catalogName,
                widgetImpressionItemName,
                widgetImpressionActionName
            )
            widgetTrackingSet.add(uniqueTrackingKey)
        }
    }

    override fun sendWidgetTrackEvent(actionName: String) {
        if (!widgetTrackingSet.contains(actionName)) {
            CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_VIEW_PG_IRIS,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                actionName,
                "$catalogName - $catalogId",
                userSession.userId,
                catalogId
            )
            widgetTrackingSet.add(actionName)
        }
    }

    fun onBackPressed() {
        isBottomSheetOpen = false
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun getChildsFragmentManager(): FragmentManager? {
        return childFragmentManager
    }

    override fun getWindowHeight(): Int {
        return if (activity != null) {
            catalog_layout.height
        } else {
            0
        }
    }

    override fun setLastDetachedItemPosition(adapterPosition: Int) {
        super.setLastDetachedItemPosition(adapterPosition)
        if (!isScrollDownButtonClicked) {
            lastDetachedItemPosition = adapterPosition
        }
    }

    override fun setLastAttachItemPosition(adapterPosition: Int) {
        super.setLastDetachedItemPosition(adapterPosition)
        if (!isScrollDownButtonClicked) {
            lastAttachItemPosition = adapterPosition
        }
    }

    override fun onPause() {
        super.onPause()
        catalogLinearLayoutManager?.removeAllHandlers()
    }

    override fun setIsScrollButtonDown(value: Boolean) {
        isScrollDownButtonClicked = false
    }
}
