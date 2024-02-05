package com.tokopedia.catalog.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.catalog.R
import com.tokopedia.catalog.analytics.CatalogReimagineDetailAnalytics
import com.tokopedia.catalog.analytics.CatalogTrackerConstant
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_CLICK_FAQ
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_CLICK_NAVIGATION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_CLICK_SEE_MORE_SPECIFICATION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_CLICK_VIDEO_EXPERT_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_BANNER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_DOUBLE_BANNER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_EXPERT_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_FAQ
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_HERO_IMAGE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_NAVIGATION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_PRICE_BOTTOM_SHEET
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_TEXT_DESCRIPTION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_TOP_FEATURE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_TRUSTMAKER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_ACTION_SEE_OPTIONS
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CLICK_CHANGE_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CLICK_ON_IMAGE_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CLICK_ON_IMAGE_REVIEW_BS
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CLICK_ON_SELENGKAPNYA_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CLICK_SEE_MORE_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_IMPRESSION_COLUMN_INFO_BANNER_WIDGET
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_IMPRESSION_COLUMN_INFO_WIDGET
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_IMPRESSION_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_IMPRESSION_REVIEW_WIDGET
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_IMPRESSION_VIDEO_BANNER_WIDGET
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_IMPRESSION_VIDEO_WIDGET
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_REVIEW_BANNER_IMPRESSION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_TOP_FEATURE_IMPRESSION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_TRUSTMAKER_IMPRESSION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_VIEW_CLICK_PG
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_VIEW_ITEM
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_VIEW_PG_IRIS
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.SCREEN_NAME_CATALOG_DETAIL_PAGE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CHANGE_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_BUTTON_CHOOSE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_FAQ
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_NAVIGATION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_ON_IMAGE_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_ON_IMAGE_REVIEW_BS
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_ON_SELENGKAPNYA_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_SEE_MORE_COLUMN_INFO
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_SEE_MORE_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_VIDEO_EXPERT
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_BANNER_ONE_BY_ONE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_BANNER_THREE_BY_FOUR
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_BANNER_TWO_BY_ONE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_COLUMN_INFO
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_DOUBLE_BANNER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_EXPERT_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_FAQ
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_HERO_BANNER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_NAVIGATION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_PRICE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_REVIEW_WIDGET
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_TEXT_DESCRIPTION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_TOP_FEATURE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_TRUSTMAKER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_VIDEO
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_OPEN_PAGE_CATALOG_DETAIL
import com.tokopedia.catalog.databinding.FragmentCatalogReimagineDetailPageBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.activity.CatalogComparisonDetailActivity
import com.tokopedia.catalog.ui.activity.CatalogImagePreviewActivity
import com.tokopedia.catalog.ui.activity.CatalogProductListActivity.Companion.EXTRA_CATALOG_URL
import com.tokopedia.catalog.ui.activity.CatalogSwitchingComparisonActivity
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_CATALOG_ID
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_CATEGORY_ID
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_COMPARE_CATALOG_ID
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.model.PriceCtaProperties
import com.tokopedia.catalog.ui.model.PriceCtaSellerOfferingProperties
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalog.util.CatalogShareUtil
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.bottomsheet.BuyerReviewDetailBottomSheet
import com.tokopedia.catalogcommon.bottomsheet.ColumnedInfoBottomSheet
import com.tokopedia.catalogcommon.customview.CatalogToolbar
import com.tokopedia.catalogcommon.listener.AccordionListener
import com.tokopedia.catalogcommon.listener.BannerListener
import com.tokopedia.catalogcommon.listener.CharacteristicListener
import com.tokopedia.catalogcommon.listener.ColumnedInfoListener
import com.tokopedia.catalogcommon.listener.DoubleBannerListener
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.listener.PanelImageListener
import com.tokopedia.catalogcommon.listener.SellerOfferingListener
import com.tokopedia.catalogcommon.listener.SliderImageTextListener
import com.tokopedia.catalogcommon.listener.SupportFeatureListener
import com.tokopedia.catalogcommon.listener.TextDescriptionListener
import com.tokopedia.catalogcommon.listener.TopFeatureListener
import com.tokopedia.catalogcommon.listener.TrustMakerListener
import com.tokopedia.catalogcommon.listener.VideoExpertListener
import com.tokopedia.catalogcommon.listener.VideoListener
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.uimodel.VideoUiModel
import com.tokopedia.catalogcommon.util.DrawableExtension
import com.tokopedia.catalogcommon.viewholder.BuyerReviewViewHolder
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
import com.tokopedia.catalogcommon.viewholder.StickyNavigationListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oldcatalog.usecase.detail.InvalidCatalogComparisonException
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogDetailPageFragment :
    BaseDaggerFragment(),
    HeroBannerListener,
    StickyNavigationListener,
    AccordionListener,
    BannerListener,
    TrustMakerListener,
    TextDescriptionListener,
    VideoExpertListener,
    TopFeatureListener,
    DoubleBannerListener,
    ComparisonViewHolder.ComparisonItemListener,
    BuyerReviewViewHolder.BuyerReviewListener,
    ColumnedInfoListener,
    VideoListener,
    SupportFeatureListener,
    SliderImageTextListener,
    CharacteristicListener,
    PanelImageListener,
    SellerOfferingListener {

    companion object {
        private const val QUERY_CATALOG_ID = "catalog_id"
        private const val QUERY_PRODUCT_SORTING_STATUS = "product_sorting_status"
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val COLOR_VALUE_MAX = 255
        private const val LOGIN_REQUEST_CODE = 1001
        const val CATALOG_COMPARE_REQUEST_CODE = 1002
        const val CATALOG_CAMPARE_SWITCHING_REQUEST_CODE = 1003
        private const val POSITION_THREE_IN_WIDGET_LIST = 3
        private const val POSITION_TWO_IN_WIDGET_LIST = 2
        private const val NAVIGATION_SCROLL_DURATION = 800L
        const val CATALOG_DETAIL_PAGE_FRAGMENT_TAG = "CATALOG_DETAIL_PAGE_FRAGMENT_TAG"

        fun newInstance(catalogId: String): CatalogDetailPageFragment {
            val fragment = CatalogDetailPageFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: CatalogDetailPageViewModel

    private var binding by autoClearedNullable<FragmentCatalogReimagineDetailPageBinding>()
    private val widgetAdapter by lazy {
        WidgetCatalogAdapter(
            CatalogAdapterFactoryImpl(
                heroBannerListener = this,
                navListener = this,
                accordionListener = this,
                bannerListener = this,
                trustMakerListener = this,
                textDescriptionListener = this,
                videoExpertListener = this,
                topFeatureListener = this,
                doubleBannerListener = this,
                comparisonItemListener = this,
                columnedInfoListener = this,
                videoListener = this,
                buyerReviewListener = this,
                supportFeatureListener = this,
                imageTextListener = this,
                characteristicListener = this,
                panelImageListener = this,
                sellerOfferingListener = this
            )
        )
    }

    private var title = ""
    private var productSortingStatus = 0
    private var catalogId = ""
    private var categoryId = ""
    private var catalogUrl = ""
    private var brand = ""

    private var compareCatalogId = ""
    private var selectNavigationFromScroll = true
    private var retriedCompareCatalogIds = listOf<String>()
    private val seenTracker = mutableListOf<String>()

    private val userSession: UserSession by lazy {
        UserSession(activity)
    }

    private val insetsController: WindowInsetsControllerCompat? by lazy {
        activity?.window?.decorView?.let(ViewCompat::getWindowInsetsController)
    }

    private fun sendOnTimeImpression(uniqueId: String, trackerFunction: () -> Unit) {
        if (!seenTracker.any { it == uniqueId }) {
            seenTracker.add(uniqueId)
            trackerFunction.invoke()
        }
    }

    private fun showPreviewImages(
        carouselItem: BuyerReviewUiModel.ItemBuyerReviewUiModel,
        position: Int,
        isFromBottomsheet: Boolean
    ) {
        val imageUrl = carouselItem.images.map { it.fullsizeImgUrl }
        val intent = context?.let {
            CatalogImagePreviewActivity.createIntent(it, imageUrl, position)
        }
        CatalogImagePreviewActivity.setTrackerDataIntent(intent, catalogId, carouselItem)
        startActivity(intent)
        CatalogReimagineDetailAnalytics.sendEventPG(
            action = if (isFromBottomsheet) EVENT_CLICK_ON_IMAGE_REVIEW_BS else EVENT_CLICK_ON_IMAGE_REVIEW,
            category = EVENT_CATEGORY_CATALOG_PAGE,
            labels = "${carouselItem.catalogName} - $catalogId - feedback_id:${carouselItem.reviewId}",
            trackerId = if (isFromBottomsheet) TRACKER_ID_CLICK_ON_IMAGE_REVIEW_BS else TRACKER_ID_CLICK_ON_IMAGE_REVIEW
        )
    }

    private fun addToCart(atcModel: CatalogProductAtcUiModel) {
        if (viewModel.isUserLoggedIn()) {
            if (atcModel.isVariant) {
                openVariantBottomSheet(atcModel)
            } else {
                viewModel.addProductToCart(atcModel)
            }
        } else {
            goToLoginPage()
        }
    }

    private fun openVariantBottomSheet(atcModel: CatalogProductAtcUiModel) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                it,
                atcModel.productId,
                VariantPageSource.CATALOG_PAGESOURCE,
                shopId = atcModel.shopId,
                dismissAfterTransaction = true,
                startActivitResult = { intent, reqCode ->
                    startActivityForResult(intent, reqCode)
                }
            )
        }
    }

    override fun getScreenName() = CatalogDetailPageFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogReimagineDetailPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers(view)
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
            viewModel.getProductCatalog(catalogId, "")
            viewModel.refreshNotification()
        }
        sendOpenPageTracker()
    }

    override fun onNavBackClicked() {
        activity?.finish()
    }

    override fun onNavShareClicked() {
        // no-op
    }

    override fun onNavMoreMenuClicked() {
        // no-op
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshNotification()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.refreshNotification()
        }
        if (requestCode == CATALOG_COMPARE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val comparedCatalogId = data?.getStringArrayListExtra(ARG_PARAM_COMPARE_CATALOG_ID)
            if (!comparedCatalogId.isNullOrEmpty()) changeComparison(comparedCatalogId)
        }
        if (requestCode == CATALOG_CAMPARE_SWITCHING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newComparedCatalogId =
                data?.getStringArrayListExtra(CatalogSwitchingComparisonFragment.ARG_COMPARISON_CATALOG_ID)
            if (!newComparedCatalogId.isNullOrEmpty()) changeComparison(newComparedCatalogId)
        }
        AtcVariantHelper.onActivityResultAtcVariant(context ?: return, requestCode, data) {
            viewModel.refreshNotification()
            RouteManager.route(context, ApplinkConst.CART)
        }
    }

    override fun onClickSeeMore(carouselItem: BuyerReviewUiModel.ItemBuyerReviewUiModel) {
        BuyerReviewDetailBottomSheet.show(
            manager = childFragmentManager,
            reviewData = carouselItem
        ) { position ->
            showPreviewImages(carouselItem, position, true)
        }
        CatalogReimagineDetailAnalytics.sendEventPG(
            action = EVENT_CLICK_ON_SELENGKAPNYA_REVIEW,
            category = EVENT_CATEGORY_CATALOG_PAGE,
            labels = "${carouselItem.catalogName} - $catalogId - feedback_id:${carouselItem.reviewId}",
            trackerId = TRACKER_ID_CLICK_ON_SELENGKAPNYA_REVIEW
        )
    }

    override fun onClickImage(
        carouselItem: BuyerReviewUiModel.ItemBuyerReviewUiModel,
        position: Int
    ) {
        showPreviewImages(carouselItem, position, false)
    }

    override fun onBuyerReviewImpression(buyerReviewUiModel: BuyerReviewUiModel) {
        viewModel.emitScrollEvent(buyerReviewUiModel.widgetName)
        sendOnTimeImpression(TRACKER_ID_IMPRESSION_REVIEW_WIDGET) {
            CatalogReimagineDetailAnalytics.sendEventPG(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_IMPRESSION_REVIEW_WIDGET,
                category = EVENT_CATEGORY_CATALOG_PAGE,
                labels = "${buyerReviewUiModel.items.firstOrNull()?.catalogName.orEmpty()} - $catalogId",
                trackerId = TRACKER_ID_IMPRESSION_REVIEW_WIDGET
            )
        }
    }

    override fun onNavigateWidget(anchorTo: String, tabPosition: Int, tabTitle: String?) {
        selectNavigationFromScroll = false
        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        val anchorToPosition = widgetAdapter.findPositionWidget(anchorTo)
        val layoutManager = binding?.rvContent?.layoutManager as? LinearLayoutManager
        if (anchorToPosition >= Int.ZERO) {
            when (tabPosition) {
                Int.ZERO -> {
                    var newAnchorPosition = anchorToPosition - POSITION_THREE_IN_WIDGET_LIST
                    if (newAnchorPosition == -1) {
                        newAnchorPosition = anchorToPosition
                    }
                    smoothScroller.targetPosition = newAnchorPosition
                    layoutManager?.startSmoothScroll(smoothScroller)
                }

                (widgetAdapter.findNavigationCount().dec()) -> {
                    smoothScroller.targetPosition = anchorToPosition
                    layoutManager?.startSmoothScroll(smoothScroller)
                }

                else -> {
                    smoothScroller.targetPosition = anchorToPosition - POSITION_TWO_IN_WIDGET_LIST
                    layoutManager?.startSmoothScroll(smoothScroller)
                }
            }
            widgetAdapter.changeNavigationTabActive(tabPosition)
            Handler(Looper.getMainLooper()).postDelayed({
                selectNavigationFromScroll = true
            }, NAVIGATION_SCROLL_DURATION)
        }

        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_CLICK_PG,
            action = EVENT_ACTION_CLICK_NAVIGATION,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = "$catalogId - item: {$tabTitle}",
            trackerId = TRACKER_ID_CLICK_NAVIGATION
        )
    }

    private fun setupObservers(view: View) {
        observeAddToCartDataModel(view)
        viewModel.catalogDetailDataModel.observe(viewLifecycleOwner) {
            if (it is Success) {
                productSortingStatus = it.data.productSortingStatus
                catalogUrl = it.data.catalogUrl
                categoryId = it.data.priceCtaProperties.departmentId
                brand = it.data.priceCtaProperties.brand
                title = it.data.navigationProperties.title
                binding?.setupToolbar(it.data.navigationProperties)
                binding?.setupRvWidgets(it.data.navigationProperties)
                binding?.setupPriceCtaWidget(it.data.priceCtaProperties)
                binding?.setupPriceCtaSellerOfferingWidget(it.data.priceCtaSellerOfferingProperties)
                widgetAdapter.addWidget(it.data.widgets)
                binding?.stickySingleHeaderView?.stickyPosition =
                    widgetAdapter.findPositionNavigation()
                binding?.toolbar?.shareButton?.setOnClickListener { view ->
                    CatalogShareUtil(
                        this@CatalogDetailPageFragment,
                        it.data.shareProperties
                    ).showUniversalShareBottomSheet(
                        viewModel.getUserId()
                    )
                }

                val comparison = it.data.widgets.find {
                    it is ComparisonUiModel
                } as? ComparisonUiModel

                comparison?.let {
                    compareCatalogId = comparison.content.joinToString(",") {
                        it.id
                    }
                }
            } else if (it is Fail) {
                binding?.showPageError(it.throwable)
            }
        }
        viewModel.totalCartItem.observe(viewLifecycleOwner) {
            binding?.toolbar?.cartCount = it
        }
        viewModel.errorsToaster.observe(viewLifecycleOwner) {
            val errorMessage = ErrorHandler.getErrorMessage(view.context, it)
            Toaster.build(
                view,
                errorMessage,
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR
            ).show()
        }
        viewModel.errorsToasterGetComparison.observe(viewLifecycleOwner) {
            val errorMessage = when (it) {
                is UnknownHostException -> {
                    getString(R.string.catalog_error_message_no_connection)
                }

                is InvalidCatalogComparisonException -> {
                    getString(R.string.catalog_error_message_inactive, it.invalidCatalogCount)
                }

                else -> {
                    ErrorHandler.getErrorMessage(requireView().context, it)
                }
            }

            if (it is InvalidCatalogComparisonException) {
                Toaster.build(
                    view,
                    errorMessage,
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_NORMAL,
                    actionText = getString(R.string.catalog_label_oke)
                ) {
                }.show()
            } else {
                Toaster.build(
                    view,
                    errorMessage,
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.catalog_retry_action)
                ) {
                    changeComparison(retriedCompareCatalogIds)
                }.show()
            }
        }
        viewModel.comparisonUiModel.observe(viewLifecycleOwner) {
            if (it != null) {
                compareCatalogId = it.content.joinToString(",") {
                    it.id
                }
                widgetAdapter.changeComparison(it)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.scrollEvents.debounce(300).collect {
                if (selectNavigationFromScroll) {
                    widgetAdapter.autoSelectNavigation(it)
                }
            }
        }
    }

    private fun observeAddToCartDataModel(view: View) {
        viewModel.addToCartDataModel.observe(viewLifecycleOwner) {
            if (it.isStatusError()) {
                Toaster.build(
                    view,
                    it.getAtcErrorMessage().orEmpty(),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR
                ).show()
            } else {
                RouteManager.route(context, ApplinkConst.CART)
                viewModel.refreshNotification()
            }
        }
    }

    private fun FragmentCatalogReimagineDetailPageBinding.setupRvWidgets(
        navigationProperties: NavigationProperties
    ) {
        val layoutManager = LinearLayoutManager(context)
        rvContent.layoutManager = layoutManager
        rvContent.adapter = widgetAdapter
        rvContent.setBackgroundColor(navigationProperties.bgColor)
        rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val bannerHeight = layoutManager.findViewByPosition(Int.ZERO)?.height.orZero()
                val bannerRect = Rect()
                layoutManager.findViewByPosition(Int.ZERO)?.getGlobalVisibleRect(bannerRect)
                val scrollProgress = Int.ONE - if (bannerRect.height()
                    .isMoreThanZero() && bannerHeight.isMoreThanZero()
                ) {
                    bannerRect.height() / bannerHeight.toFloat()
                } else {
                    Int.ZERO.toFloat()
                }
                toolbar.updateToolbarAppearance(scrollProgress, navigationProperties)
            }
        })
    }

    private fun FragmentCatalogReimagineDetailPageBinding.setupToolbar(
        navigationProperties: NavigationProperties
    ) {
        val colorBgGradient = MethodChecker.getColor(
            context,
            unifyprinciplesR.color.Unify_Static_Black_44
        )
        val colorFontDark = MethodChecker.getColor(
            context,
            unifyprinciplesR.color.Unify_Static_White
        )
        val colorFontLight = MethodChecker.getColor(
            context,
            unifyprinciplesR.color.Unify_Static_White
        )
        val colorFont = if (navigationProperties.isDarkMode) colorFontDark else colorFontLight

        insetsController?.isAppearanceLightStatusBars = !navigationProperties.isDarkMode
        toolbarShadow.background =
            DrawableExtension.createGradientDrawable(colorTop = colorBgGradient)
        toolbar.setColors(colorFont)
        toolbarBg.setBackgroundColor(navigationProperties.bgColor)
        toolbar.title = navigationProperties.title
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        toolbar.shareButton?.show()
        toolbar.cartButton?.setOnClickListener {
            if (viewModel.isUserLoggedIn()) {
                RouteManager.route(context, ApplinkConst.CART)
            } else {
                goToLoginPage()
            }
        }
    }

    private fun CatalogToolbar.updateToolbarAppearance(
        scrollProgress: Float,
        navigationProperties: NavigationProperties
    ) {
        if (!navigationProperties.isDarkMode) {
            val colorProgress: Int = COLOR_VALUE_MAX - (COLOR_VALUE_MAX * scrollProgress).toInt()
            setColors(Color.rgb(colorProgress, colorProgress, colorProgress))
        }
        binding?.toolbarBg?.alpha = scrollProgress
        binding?.toolbar?.tpgTitle?.alpha = scrollProgress
        binding?.statusBar?.alpha = scrollProgress
    }

    private fun FragmentCatalogReimagineDetailPageBinding.setupPriceCtaWidget(properties: PriceCtaProperties) {
        icCtaNormal.apply {
            root.showWithCondition(properties.isVisible)
            containerPriceCta.setBackgroundColor(properties.bgColor)
            tgpCatalogName.setTextColor(properties.textColor)
            tgpPriceRanges.setTextColor(properties.textColor)

            tgpCatalogName.text = properties.productName
            tgpPriceRanges.text = properties.price

            btnProductList.setOnClickListener {
                goToProductListPage()
                CatalogReimagineDetailAnalytics.sendEvent(
                    event = EVENT_VIEW_CLICK_PG,
                    action = EVENT_ACTION_SEE_OPTIONS,
                    category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                    labels = catalogId,
                    trackerId = TRACKER_ID_CLICK_BUTTON_CHOOSE
                )
            }
        }

        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_PRICE_BOTTOM_SHEET,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = catalogId,
            trackerId = TRACKER_ID_IMPRESSION_PRICE
        )
    }

    private fun FragmentCatalogReimagineDetailPageBinding.setupPriceCtaSellerOfferingWidget(
        properties: PriceCtaSellerOfferingProperties
    ) {
        icCtaSellerOffering.apply {
            viewModel.atcModel = CatalogProductAtcUiModel(
                productId = properties.productId,
                shopId = properties.shopId,
                warehouseId = properties.warehouseId,
                isVariant = properties.isVariant
            )
            root.showWithCondition(properties.isVisible)
            containerPriceCta.setBackgroundColor(properties.bgColor)
            ctaAtc.setPrice(properties.price)
            ctaAtc.setSlashPrice(properties.slashPrice)
            ctaAtc.setShopName(properties.shopName)
            ctaAtc.setBadge(properties.badge)
            ctaAtc.setSold(properties.sold)
            ctaAtc.setRating(properties.shopRating)
            ctaAtc.setTheme(properties.isDarkTheme)
            ctaAtc.setOnClick {
                addToCart(viewModel.atcModel)
            }
            btnProductList.setOnClickListener {
                goToProductListPage()
            }

            if (properties.isDarkTheme) {
                btnProductList.cardType = CardUnify2.TYPE_BORDER
            } else {
                btnProductList.cardType = CardUnify2.TYPE_BORDER_ACTIVE
            }
            tvOther.setTextColor(MethodChecker.getColor(context, properties.colorBorderButton))
        }
    }

    private fun FragmentCatalogReimagineDetailPageBinding.showPageError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                gePageError.setType(GlobalError.NO_CONNECTION)
            }

            else -> {
                gePageError.setType(GlobalError.SERVER_ERROR)
            }
        }
        gePageError.errorDescription.text = errorMessage
        groupContent.gone()
        stickySingleHeaderView.show()
    }

    private fun goToLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }

    private fun goToChatPage(shopId: String) {
        if (viewModel.isUserLoggedIn()) {
            RouteManager.route(context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId)
        } else {
            goToLoginPage()
        }
    }

    private fun goToProductListPage() {
        val catalogProductList =
            Uri.parse(UriUtil.buildUri(ApplinkConst.DISCOVERY_CATALOG_PRODUCT_LIST))
                .buildUpon()
                .appendQueryParameter(QUERY_CATALOG_ID, catalogId)
                .appendQueryParameter(
                    QUERY_PRODUCT_SORTING_STATUS,
                    productSortingStatus.toString()
                )
                .appendPath(title).toString()

        RouteManager.getIntent(context, catalogProductList).apply {
            putExtra(EXTRA_CATALOG_URL, catalogUrl)
            startActivity(this)
        }
    }

    private fun sendOpenPageTracker() {
        CatalogReimagineDetailAnalytics.sendEventOpenScreen(
            screenName = "$SCREEN_NAME_CATALOG_DETAIL_PAGE - $catalogId",
            trackerId = TRACKER_ID_OPEN_PAGE_CATALOG_DETAIL,
            userId = userSession.userId
        )
    }

    override fun onHeroBannerImpression(
        currentPositionVisibility: Int,
        imageDescription: List<String>,
        brandImageUrl: List<String>
    ) {
        if (!(isAdded && isVisible)) return
        val list = arrayListOf<HashMap<String, String>>()
        for (index in brandImageUrl.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] =
                imageDescription.getOrNull(index).orEmpty()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.inc().toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] =
                CatalogTrackerConstant.EVENT_IMAGE_BANNER_IMPRESSION
            list.add(promotions)
        }

        sendOnTimeImpression(TRACKER_ID_IMPRESSION_HERO_BANNER) {
            CatalogReimagineDetailAnalytics.sendEventImpressionList(
                event = EVENT_VIEW_ITEM,
                action = EVENT_ACTION_IMPRESSION_HERO_IMAGE,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_HERO_BANNER,
                userId = userSession.userId,
                promotion = list
            )
        }
    }

    override fun onStickyNavigationImpression() {
        sendOnTimeImpression(TRACKER_ID_IMPRESSION_NAVIGATION) {
            CatalogReimagineDetailAnalytics.sendEvent(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESSION_NAVIGATION,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_NAVIGATION
            )
        }
    }

    override fun onBannerThreeByFourImpression(ratio: String) {
        when (ratio) {
            BannerCatalogUiModel.Ratio.THREE_BY_FOUR.ratioName -> {
                sendOnTimeImpression(TRACKER_ID_IMPRESSION_BANNER_THREE_BY_FOUR) {
                    CatalogReimagineDetailAnalytics.sendEvent(
                        event = EVENT_VIEW_PG_IRIS,
                        action = "$EVENT_ACTION_IMPRESSION_BANNER $ratio",
                        category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                        labels = catalogId,
                        trackerId = TRACKER_ID_IMPRESSION_BANNER_THREE_BY_FOUR
                    )
                }
            }

            BannerCatalogUiModel.Ratio.TWO_BY_ONE.ratioName -> {
                sendOnTimeImpression(TRACKER_ID_IMPRESSION_BANNER_TWO_BY_ONE) {
                    CatalogReimagineDetailAnalytics.sendEvent(
                        event = EVENT_VIEW_PG_IRIS,
                        action = "$EVENT_ACTION_IMPRESSION_BANNER $ratio",
                        category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                        labels = catalogId,
                        trackerId = TRACKER_ID_IMPRESSION_BANNER_TWO_BY_ONE
                    )
                }
            }

            BannerCatalogUiModel.Ratio.ONE_BY_ONE.ratioName -> {
                sendOnTimeImpression(TRACKER_ID_IMPRESSION_BANNER_ONE_BY_ONE) {
                    CatalogReimagineDetailAnalytics.sendEvent(
                        event = EVENT_VIEW_PG_IRIS,
                        action = "$EVENT_ACTION_IMPRESSION_BANNER $ratio",
                        category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                        labels = catalogId,
                        trackerId = TRACKER_ID_IMPRESSION_BANNER_ONE_BY_ONE
                    )
                }
            }
        }
    }

    override fun onBannerImpression(element: BannerCatalogUiModel) {
        viewModel.emitScrollEvent(element.widgetName)
    }

    override fun onTextDescriptionImpression(widgetName: String) {
        sendOnTimeImpression(TRACKER_ID_IMPRESSION_TEXT_DESCRIPTION) {
            CatalogReimagineDetailAnalytics.sendEvent(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESSION_TEXT_DESCRIPTION,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_TEXT_DESCRIPTION
            )
        }

        viewModel.emitScrollEvent(widgetName)
    }

    override fun onVideoImpression(
        itemHasSaw: List<VideoUiModel.ItemVideoUiModel>,
        widgetName: String
    ) {
        val catalogDetail = viewModel.catalogDetailDataModel.value as? Success<CatalogDetailUiModel>
        val catalogTitle = catalogDetail?.data?.navigationProperties?.title.orEmpty()
        val list = arrayListOf<HashMap<String, String>>()
        for (index in itemHasSaw.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] = catalogTitle
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.inc().toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = EVENT_IMPRESSION_VIDEO_BANNER_WIDGET
            list.add(promotions)
        }
        sendOnTimeImpression(TRACKER_ID_IMPRESSION_VIDEO) {
            CatalogReimagineDetailAnalytics.sendEventImpressionListGeneral(
                event = EVENT_VIEW_ITEM,
                action = EVENT_IMPRESSION_VIDEO_WIDGET,
                category = EVENT_CATEGORY_CATALOG_PAGE,
                labels = "$catalogTitle - $catalogId",
                trackerId = TRACKER_ID_IMPRESSION_VIDEO,
                userId = userSession.userId,
                promotion = list
            )
        }
    }

    override fun onClickVideoExpert() {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_CLICK_PG,
            action = EVENT_ACTION_CLICK_VIDEO_EXPERT_REVIEW,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = catalogId,
            trackerId = TRACKER_ID_CLICK_VIDEO_EXPERT
        )
    }

    override fun onVideoExpertImpression(model: ExpertReviewUiModel) {
        val list = arrayListOf<HashMap<String, String>>()
        val itemHasSaw = model.content
        for (index in itemHasSaw.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] = itemHasSaw[index].title
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.inc().toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = EVENT_REVIEW_BANNER_IMPRESSION
            list.add(promotions)
        }

        sendOnTimeImpression(TRACKER_ID_IMPRESSION_EXPERT_REVIEW) {
            CatalogReimagineDetailAnalytics.sendEventImpressionList(
                event = EVENT_VIEW_ITEM,
                action = EVENT_ACTION_IMPRESSION_EXPERT_REVIEW,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_EXPERT_REVIEW,
                userId = userSession.userId,
                promotion = list
            )
        }
        viewModel.emitScrollEvent(model.widgetName)
    }

    override fun onImpressionAccordionInformation(widgetName: String) {
        sendOnTimeImpression(TRACKER_ID_IMPRESSION_FAQ) {
            CatalogReimagineDetailAnalytics.sendEvent(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESSION_FAQ,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_FAQ
            )
        }
        viewModel.emitScrollEvent(widgetName)
    }

    override fun onClickItemAccordionInformation(
        position: Int,
        item: AccordionInformationUiModel.ItemAccordionInformationUiModel
    ) {
        val label = "$catalogId - position: $position - question:${item.title}"
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_CLICK_PG,
            action = EVENT_ACTION_CLICK_FAQ,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = label,
            trackerId = TRACKER_ID_CLICK_FAQ
        )
    }

    override fun onTrustMakerImpression(
        currentVisibleTrustMaker: List<TrustMakerUiModel.ItemTrustMakerUiModel>,
        widgetName: String
    ) {
        val list = arrayListOf<HashMap<String, String>>()
        for (index in currentVisibleTrustMaker.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] =
                currentVisibleTrustMaker[index].title
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.inc().toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = EVENT_TRUSTMAKER_IMPRESSION
            list.add(promotions)
        }

        sendOnTimeImpression(TRACKER_ID_IMPRESSION_TRUSTMAKER) {
            CatalogReimagineDetailAnalytics.sendEventImpressionList(
                event = EVENT_VIEW_ITEM,
                action = EVENT_ACTION_IMPRESSION_TRUSTMAKER,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_TRUSTMAKER,
                userId = userSession.userId,
                promotion = list
            )
        }
    }

    override fun onTopFeatureImpression(
        items: List<TopFeaturesUiModel.ItemTopFeatureUiModel>,
        widgetName: String
    ) {
        val list = arrayListOf<HashMap<String, String>>()
        for (index in items.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] = items[index].name
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.inc().toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = EVENT_TOP_FEATURE_IMPRESSION
            list.add(promotions)
        }

        sendOnTimeImpression(TRACKER_ID_IMPRESSION_TOP_FEATURE) {
            CatalogReimagineDetailAnalytics.sendEventImpressionList(
                event = EVENT_VIEW_ITEM,
                action = EVENT_ACTION_IMPRESSION_TOP_FEATURE,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_TOP_FEATURE,
                userId = userSession.userId,
                promotion = list
            )
        }
        viewModel.emitScrollEvent(widgetName)
    }

    override fun onDoubleBannerImpression(widgetName: String) {
        sendOnTimeImpression(TRACKER_ID_IMPRESSION_DOUBLE_BANNER) {
            CatalogReimagineDetailAnalytics.sendEvent(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_ACTION_IMPRESSION_DOUBLE_BANNER,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_IMPRESSION_DOUBLE_BANNER
            )
        }
    }

    override fun onComparisonSwitchButtonClicked(items: List<ComparisonUiModel.ComparisonContent>) {
        val label = "$catalogId | compared catalog id: $compareCatalogId"
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_CLICK_PG,
            action = EVENT_CLICK_CHANGE_COMPARISON,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = label,
            trackerId = TRACKER_ID_CHANGE_COMPARISON
        )

        val catalogComparedId = arrayListOf<String>()
        catalogComparedId.addAll(items.map { it.id })

        Intent(activity ?: return, CatalogSwitchingComparisonActivity::class.java).apply {
            putExtra(CatalogSwitchingComparisonFragment.ARG_CATALOG_ID, catalogId)
            putStringArrayListExtra(
                CatalogSwitchingComparisonFragment.ARG_COMPARISON_CATALOG_ID,
                catalogComparedId
            )
            putExtra(CatalogSwitchingComparisonFragment.ARG_EXTRA_CATALOG_BRAND, brand)
            putExtra(CatalogSwitchingComparisonFragment.ARG_EXTRA_CATALOG_CATEGORY_ID, categoryId)
            startActivityForResult(this, CATALOG_CAMPARE_SWITCHING_REQUEST_CODE)
        }
    }

    override fun onComparisonSeeMoreButtonClicked(
        items: List<ComparisonUiModel.ComparisonContent>
    ) {
        val label = "$catalogId | compared catalog id: ${items.joinToString { it.id }}"
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_CLICK_PG,
            action = EVENT_CLICK_SEE_MORE_COMPARISON,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = label,
            trackerId = TRACKER_ID_CLICK_SEE_MORE_COMPARISON
        )

        Intent(activity ?: return, CatalogComparisonDetailActivity::class.java).apply {
            putExtra(ARG_PARAM_CATALOG_ID, catalogId)
            putExtra(ARG_PARAM_CATEGORY_ID, categoryId)
            putStringArrayListExtra(ARG_PARAM_COMPARE_CATALOG_ID, ArrayList(items.map { it.id }))
            startActivityForResult(this, CATALOG_COMPARE_REQUEST_CODE)
        }
    }

    override fun onComparisonProductClick(id: String) {
        val catalogProductList =
            Uri.parse(UriUtil.buildUri(ApplinkConst.DISCOVERY_CATALOG))
                .buildUpon()
                .appendPath(id).toString()
        RouteManager.getIntent(context, catalogProductList).apply {
            startActivity(this)
        }
    }

    override fun onComparisonImpression(id: String, widgetName: String) {
        val label = "$catalogId | compared catalog id: $id"

        sendOnTimeImpression(TRACKER_ID_IMPRESSION_COMPARISON) {
            CatalogReimagineDetailAnalytics.sendEvent(
                event = EVENT_VIEW_PG_IRIS,
                action = EVENT_IMPRESSION_COMPARISON,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = label,
                trackerId = TRACKER_ID_IMPRESSION_COMPARISON
            )
        }
        viewModel.emitScrollEvent(widgetName)
    }

    override fun onComparisonScrolled(dx: Int, dy: Int, scrollProgress: Int) {
        // no-op
    }

    private fun changeComparison(compareCatalogIds: List<String>) {
        retriedCompareCatalogIds = compareCatalogIds
        viewModel.getProductCatalogComparisons(catalogId, compareCatalogIds)
    }

    override fun onColumnedInfoSeeMoreClicked(
        sectionTitle: String,
        columnData: List<ColumnedInfoUiModel.ColumnData>
    ) {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_CLICK_PG,
            action = EVENT_ACTION_CLICK_SEE_MORE_SPECIFICATION,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = "$catalogId",
            trackerId = TRACKER_ID_CLICK_SEE_MORE_COLUMN_INFO
        )
        ColumnedInfoBottomSheet.show(childFragmentManager, sectionTitle, columnData)
    }

    override fun onColumnedInfoImpression(columnedInfoUiModel: ColumnedInfoUiModel) {
        viewModel.emitScrollEvent(columnedInfoUiModel.widgetName)
        val catalogDetail = viewModel.catalogDetailDataModel.value as? Success<CatalogDetailUiModel>
        val catalogTitle = catalogDetail?.data?.navigationProperties?.title.orEmpty()
        val list = arrayListOf<HashMap<String, String>>()
        if (columnedInfoUiModel.widgetContent.rowData.isNotEmpty()) {
            columnedInfoUiModel.widgetContent.rowData.forEachIndexed { index, _ ->
                val promotions = hashMapOf<String, String>()
                promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] = catalogTitle
                promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.inc().toString()
                promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
                promotions[CatalogTrackerConstant.KEY_ITEM_NAME] =
                    EVENT_IMPRESSION_COLUMN_INFO_BANNER_WIDGET
                list.add(promotions)
            }
        } else {
            columnedInfoUiModel.widgetContentThreeColumn.forEachIndexed { index, _ ->
                columnedInfoUiModel.widgetContentThreeColumn[index].rowData.forEachIndexed { subindex, pair ->
                    val promotions = hashMapOf<String, String>()
                    promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] = catalogTitle
                    promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] =
                        "${index.inc()}.${subindex.inc()}"
                    promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
                    promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = pair.second
                    list.add(promotions)
                }
            }
        }

        sendOnTimeImpression(TRACKER_ID_IMPRESSION_COLUMN_INFO + columnedInfoUiModel.idWidget) {
            CatalogReimagineDetailAnalytics.sendEventImpressionListGeneral(
                event = EVENT_VIEW_ITEM,
                action = EVENT_IMPRESSION_COLUMN_INFO_WIDGET,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = "$catalogTitle - $catalogId",
                trackerId = TRACKER_ID_IMPRESSION_COLUMN_INFO,
                userId = userSession.userId,
                promotion = list
            )
        }
    }

    override fun onSupportFeatureImpression(widgetName: String) {
        viewModel.emitScrollEvent(widgetName)
    }

    override fun onSliderImageTextImpression(widgetName: String) {
        viewModel.emitScrollEvent(widgetName)
    }

    override fun onCharacteristicImpression(widgetName: String) {
        viewModel.emitScrollEvent(widgetName)
    }

    override fun onPanelImageImpression(widgetName: String) {
        viewModel.emitScrollEvent(widgetName)
    }

    override fun onSellerOfferingAtcButtonClicked() {
        addToCart(viewModel.atcModel)
    }

    override fun onSellerOfferingChatButtonClicked() {
        goToChatPage(viewModel.atcModel.shopId)
    }

    override fun onSellerOfferingProductImageClicked(productId: String) {
        RouteManager.route(context, ApplinkConst.PRODUCT_INFO, productId)
    }

    override fun onSellerOfferingVariantArrowClicked(productId: String) {
        TODO("Not yet implemented")
    }
}
