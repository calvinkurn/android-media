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
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CLICK_SEE_MORE_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_IMPRESSION_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_REVIEW_BANNER_IMPRESSION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_TOP_FEATURE_IMPRESSION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_TRUSTMAKER_IMPRESSION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_VIEW_CLICK_PG
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_VIEW_ITEM
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_VIEW_PG_IRIS
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_BUTTON_CHOOSE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_FAQ
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_NAVIGATION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_SEE_MORE_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_VIDEO_EXPERT
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_BANNER_ONE_BY_ONE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_BANNER_THREE_BY_FOUR
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_BANNER_TWO_BY_ONE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_COMPARISON
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_DOUBLE_BANNER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_EXPERT_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_FAQ
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_HERO_BANNER
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_NAVIGATION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_PRICE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_TEXT_DESCRIPTION
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_TOP_FEATURE
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_IMPRESSION_TRUSTMAKER
import com.tokopedia.catalog.databinding.FragmentCatalogReimagineDetailPageBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.activity.CatalogComparisonDetailActivity
import com.tokopedia.catalog.ui.activity.CatalogProductListActivity.Companion.EXTRA_CATALOG_URL
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_CATALOG_ID
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_CATEGORY_ID
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_COMPARE_CATALOG_ID
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.model.PriceCtaProperties
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.customview.CatalogToolbar
import com.tokopedia.catalogcommon.listener.AccordionListener
import com.tokopedia.catalogcommon.listener.BannerListener
import com.tokopedia.catalogcommon.listener.DoubleBannerListener
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.listener.TextDescriptionListener
import com.tokopedia.catalogcommon.listener.TopFeatureListener
import com.tokopedia.catalogcommon.listener.TrustMakerListener
import com.tokopedia.catalogcommon.listener.VideoExpertListener
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.util.DrawableExtension
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
import com.tokopedia.catalogcommon.viewholder.StickyNavigationListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.ui.bottomsheet.CatalogComponentBottomSheet
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
    CatalogDetailListener {

    companion object {
        private const val QUERY_CATALOG_ID = "catalog_id"
        private const val QUERY_PRODUCT_SORTING_STATUS = "product_sorting_status"
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val COLOR_VALUE_MAX = 255
        private const val LOGIN_REQUEST_CODE = 1001
        private const val CATALOG_COMPARE_REQUEST_CODE = 1002
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
                comparisonItemListener = this
            )
        )
    }

    private var title = ""
    private var productSortingStatus = 0
    private var catalogId = ""
    private var categoryId = ""
    private var catalogUrl = ""
    private var compareCatalogId = ""
    private var selectNavigationFromScroll = true

    private val userSession: UserSession by lazy {
        UserSession(activity)
    }

    private val recyclerViewScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                binding?.rvContent?.post {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                            val indexVisible = layoutManager?.findLastVisibleItemPosition().orZero()
                            viewModel.emitScrollEvent(indexVisible)
                        } else {
                            val indexVisible =
                                layoutManager?.findFirstVisibleItemPosition().orZero()
                            viewModel.emitScrollEvent(indexVisible)
                        }
                    }
                }
            }
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
            val comparedCatalogId = data?.getStringExtra(ARG_PARAM_COMPARE_CATALOG_ID)
            if (!comparedCatalogId.isNullOrEmpty()) changeComparison(comparedCatalogId)
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
        widgetAdapter.changeNavigationTabActive(tabPosition)
        if (anchorToPosition >= Int.ZERO) {
            when (tabPosition) {
                Int.ZERO -> {
                    smoothScroller.targetPosition = anchorToPosition - POSITION_THREE_IN_WIDGET_LIST
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
        viewModel.catalogDetailDataModel.observe(viewLifecycleOwner) {
            if (it is Success) {
                productSortingStatus = it.data.productSortingStatus
                catalogUrl = it.data.catalogUrl
                categoryId = it.data.priceCtaProperties.departmentId
                title = it.data.navigationProperties.title
                binding?.setupToolbar(it.data.navigationProperties)
                binding?.setupRvWidgets(it.data.navigationProperties)
                binding?.setupPriceCtaWidget(it.data.priceCtaProperties)
                widgetAdapter.addWidget(it.data.widgets)
                binding?.stickySingleHeaderView?.stickyPosition =
                    widgetAdapter.findPositionNavigation()
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
            val errorMessage = if (it is UnknownHostException) {
                getString(R.string.catalog_error_message_no_connection)
            } else {
                ErrorHandler.getErrorMessage(requireView().context, it)
            }

            Toaster.build(
                view,
                errorMessage,
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR,
                actionText = getString(R.string.catalog_retry_action)
            ) {
                changeComparison(compareCatalogId)
            }.show()
        }
        viewModel.comparisonUiModel.observe(viewLifecycleOwner) {
            // COMPARISON_CHANGED_POSITION is hardcoded position, will changed at next phase
            if (it == null) {
                Toaster.build(
                    view,
                    getString(R.string.catalog_error_message_inactive)
                ).show()
            } else {
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

    private fun FragmentCatalogReimagineDetailPageBinding.setupRvWidgets(
        navigationProperties: NavigationProperties
    ) {
        val layoutManager = LinearLayoutManager(context)
        rvContent.layoutManager = layoutManager
        rvContent.adapter = widgetAdapter
        rvContent.addOnScrollListener(recyclerViewScrollListener)
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

        toolbarShadow.background =
            DrawableExtension.createGradientDrawable(colorTop = colorBgGradient)
        toolbar.setColors(colorFont)
        toolbarBg.setBackgroundColor(navigationProperties.bgColor)
        toolbar.title = navigationProperties.title
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        toolbar.shareButton?.gone()
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
    }

    // Call this methods if you want to override the CTA & Price widget's theme
    private fun FragmentCatalogReimagineDetailPageBinding.setupPriceCtaWidget(properties: PriceCtaProperties) {
        containerPriceCta.setBackgroundColor(properties.bgColor)
        tgpCatalogName.setTextColor(properties.textColor)
        tgpPriceRanges.setTextColor(properties.textColor)

        tgpCatalogName.text = properties.productName
        tgpPriceRanges.text = properties.price

        btnProductList.setOnClickListener {
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

            CatalogReimagineDetailAnalytics.sendEvent(
                event = EVENT_VIEW_CLICK_PG,
                action = EVENT_ACTION_SEE_OPTIONS,
                category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                labels = catalogId,
                trackerId = TRACKER_ID_CLICK_BUTTON_CHOOSE
            )
        }

        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_PRICE_BOTTOM_SHEET,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = catalogId,
            trackerId = TRACKER_ID_IMPRESSION_PRICE
        )
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

    override fun onHeroBannerImpression(
        currentPositionVisibility: Int,
        imageDescription: List<String>,
        brandImageUrl: List<String>
    ) {
        val impressionImageDescription = if (imageDescription.isNotEmpty()) {
            imageDescription.subList(Int.ZERO, currentPositionVisibility + 1)
        } else {
            emptyList()
        }
        val impressionbrandImageUrl = brandImageUrl.subList(Int.ZERO, currentPositionVisibility + 1)
        val list = arrayListOf<HashMap<String, String>>()
        for (index in impressionbrandImageUrl.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] =
                impressionImageDescription.getOrNull(index).orEmpty()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] =
                CatalogTrackerConstant.EVENT_IMAGE_BANNER_IMPRESSION
            list.add(promotions)
        }

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

    override fun onStickyNavigationImpression() {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_NAVIGATION,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = catalogId,
            trackerId = TRACKER_ID_IMPRESSION_NAVIGATION
        )
    }

    override fun onBannerThreeByFourImpression(ratio: String) {
        when (ratio) {
            BannerCatalogUiModel.Ratio.THREE_BY_FOUR.ratioName -> {
                CatalogReimagineDetailAnalytics.sendEvent(
                    event = EVENT_VIEW_PG_IRIS,
                    action = "$EVENT_ACTION_IMPRESSION_BANNER $ratio",
                    category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                    labels = catalogId,
                    trackerId = TRACKER_ID_IMPRESSION_BANNER_THREE_BY_FOUR
                )
            }

            BannerCatalogUiModel.Ratio.TWO_BY_ONE.ratioName -> {
                CatalogReimagineDetailAnalytics.sendEvent(
                    event = EVENT_VIEW_PG_IRIS,
                    action = "$EVENT_ACTION_IMPRESSION_BANNER $ratio",
                    category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
                    labels = catalogId,
                    trackerId = TRACKER_ID_IMPRESSION_BANNER_TWO_BY_ONE
                )
            }

            BannerCatalogUiModel.Ratio.ONE_BY_ONE.ratioName -> {
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

    override fun onTextDescriptionImpression() {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_TEXT_DESCRIPTION,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = catalogId,
            trackerId = TRACKER_ID_IMPRESSION_TEXT_DESCRIPTION
        )
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

    override fun onVideoExpertImpression(itemHasSaw: List<ExpertReviewUiModel.ItemExpertReviewUiModel>) {
        val list = arrayListOf<HashMap<String, String>>()
        for (index in itemHasSaw.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] = itemHasSaw[index].title
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = EVENT_REVIEW_BANNER_IMPRESSION
            list.add(promotions)
        }

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

    override fun onImpressionAccordionInformation() {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_FAQ,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = catalogId,
            trackerId = TRACKER_ID_IMPRESSION_FAQ
        )
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

    override fun onTrustMakerImpression(currentVisibleTrustMaker: List<TrustMakerUiModel.ItemTrustMakerUiModel>) {
        val list = arrayListOf<HashMap<String, String>>()
        for (index in currentVisibleTrustMaker.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] =
                currentVisibleTrustMaker[index].title
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = EVENT_TRUSTMAKER_IMPRESSION
            list.add(promotions)
        }

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

    override fun onTopFeatureImpression(items: List<TopFeaturesUiModel.ItemTopFeatureUiModel>) {
        val list = arrayListOf<HashMap<String, String>>()
        for (index in items.indices) {
            val promotions = hashMapOf<String, String>()
            promotions[CatalogTrackerConstant.KEY_CREATIVE_NAME] = items[index].name
            promotions[CatalogTrackerConstant.KEY_CREATIVE_SLOT] = index.toString()
            promotions[CatalogTrackerConstant.KEY_ITEM_ID] = catalogId
            promotions[CatalogTrackerConstant.KEY_ITEM_NAME] = EVENT_TOP_FEATURE_IMPRESSION
            list.add(promotions)
        }

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

    override fun onDoubleBannerImpression() {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_DOUBLE_BANNER,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = catalogId,
            trackerId = TRACKER_ID_IMPRESSION_DOUBLE_BANNER
        )
    }

    override fun onComparisonSwitchButtonClicked(
        position: Int,
        item: ComparisonUiModel.ComparisonContent
    ) {
        compareCatalogId = item.id
//        val label = "$catalogId | compared catalog id: $compareCatalogId"
//        CatalogReimagineDetailAnalytics.sendEvent(
//            event = EVENT_VIEW_CLICK_PG,
//            action = EVENT_CLICK_CHANGE_COMPARISON_ON_DETAIL,
//            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
//            labels = label,
//            trackerId = TRACKER_ID_CHANGE_COMPARISON
//        )
        CatalogComponentBottomSheet.newInstance(
            "",
            catalogId,
            "",
            categoryId,
            compareCatalogId,
            CatalogComponentBottomSheet.ORIGIN_ULTIMATE_VERSION,
            this
        ).show(childFragmentManager, "")
    }

    override fun onComparisonSeeMoreButtonClicked() {
        val label = "$catalogId | compared catalog id: $compareCatalogId"
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
            putExtra(ARG_PARAM_COMPARE_CATALOG_ID, compareCatalogId)
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

    override fun onComparisonImpression(id: String) {
        compareCatalogId = id
        val label = "$catalogId | compared catalog id: $id"

        CatalogReimagineDetailAnalytics.sendEvent(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_IMPRESSION_COMPARISON,
            category = EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE,
            labels = label,
            trackerId = TRACKER_ID_IMPRESSION_COMPARISON
        )
    }

    override fun changeComparison(selectedComparedCatalogId: String) {
        compareCatalogId = selectedComparedCatalogId
        viewModel.getProductCatalogComparisons(catalogId, compareCatalogId)
    }
}
