package com.tokopedia.shop.info.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.digitsOnly
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewDetail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryImage
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewerUserInfo
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.extension.setHyperlinkText
import com.tokopedia.shop.common.extension.showToaster
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.FragmentShopInfoReimagineBinding
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.component.ShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopOperationalHourWithStatus
import com.tokopedia.shop.info.domain.entity.ShopPerformanceDuration
import com.tokopedia.shop.info.domain.entity.ShopRating
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.view.bottomsheet.ShopNoteDetailBottomSheet
import com.tokopedia.shop.info.view.model.ShopInfoUiEffect
import com.tokopedia.shop.info.view.model.ShopInfoUiEvent
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.info.view.viewmodel.ShopInfoReimagineViewModel
import com.tokopedia.shop.report.activity.ReportShopWebViewActivity
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopInfoReimagineFragment : BaseDaggerFragment(), HasComponent<ShopInfoComponent> {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val MARGIN_4_DP = 4
        private const val APP_LINK_QUERY_STRING_REVIEW_SOURCE = "header"
        private const val REQUEST_CODE_REPORT_SHOP = 110
        private const val REQUEST_CODE_LOGIN = 100
        private const val SEVEN_DAY = 7

        @JvmStatic
        fun newInstance(shopId: String): ShopInfoReimagineFragment {
            return ShopInfoReimagineFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                }
            }
        }
    }

    private var binding by autoClearedNullable<FragmentShopInfoReimagineBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopInfoReimagineViewModel::class.java] }

    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID, "").orEmpty() }
    private var review: ShopReview? = null

    override fun getScreenName(): String = ShopInfoReimagineFragment::class.java.canonicalName.orEmpty()

    override fun getComponent() = activity?.run {
        DaggerShopInfoComponent.builder()
            .shopInfoModule(ShopInfoModule())
            .shopComponent(ShopComponentHelper().getComponent(application, this))
            .build()
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopInfoReimagineBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()

        val localCacheModel = ShopUtil.getShopPageWidgetUserAddressLocalData(context ?: return)
        viewModel.processEvent(
            ShopInfoUiEvent.GetShopInfo(
                shopId,
                localCacheModel?.district_id.orEmpty(),
                localCacheModel?.city_id.orEmpty()
            )
        )

        setupView()
        observeUiState()
        observeUiEffect()
    }

    override fun onPause() {
        super.onPause()
        binding?.shopReviewView?.stopAutoScroll()
    }

    override fun onResume() {
        super.onResume()
        binding?.shopReviewView?.startAutoScroll()
    }

    private fun setupView() {
        setupReportShopHyperlink()
        setupClickListener()
        setupHeader()
    }
    private fun setupHeader() {
        binding?.headerUnify?.setNavigationOnClickListener { activity?.finish() }
    }

    private fun setupClickListener() {
        binding?.run {
            tpgCtaExpandPharmacyInfo.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapCtaExpandShopPharmacyInfo)
            }
            iconViewPharmacyLocation.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapCtaViewPharmacyLocation)
            }
            tpgCtaViewPharmacyMap.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapCtaViewPharmacyLocation)
            }
            globalError.setActionClickListener {
                viewModel.processEvent(ShopInfoUiEvent.RetryGetShopInfo)
            }
            iconChevronReview.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopRating)
            }
            tpgSectionTitleBuyerReview.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopRating)
            }
            iconRating.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopRating)
            }
            tpgShopRating.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopRating)
            }
            tpgRatingAndReviewText.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopRating)
            }
        }
    }

    private fun setupReportShopHyperlink() {
        binding?.run {
            val wording = getString(R.string.shop_info_report_shop)
            val hyperlink = getString(R.string.shop_info_report_here)
            tpgReportShop.setHyperlinkText(
                fullText = wording,
                hyperlinkSubstring = hyperlink,
                ignoreCase = true,
                onHyperlinkClick = { viewModel.processEvent(ShopInfoUiEvent.ReportShop) }
            )
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun handleUiState(uiState: ShopInfoUiState) {
        val isLoading = uiState.isLoading
        val isError = uiState.error != null

        when {
            isLoading -> renderLoadingState()
            isError -> renderErrorState()
            else -> renderMainContent(uiState)
        }
    }
    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleEffect(effect: ShopInfoUiEffect) {
        when (effect) {
            is ShopInfoUiEffect.RedirectToGmaps -> redirectToGmaps(effect.gmapsUrl)
            is ShopInfoUiEffect.ShowShopNoteDetailBottomSheet -> showShopNoteDetailBottomSheet(effect.shopNote)
            ShopInfoUiEffect.RedirectToLoginPage -> redirectToLoginPage()
            is ShopInfoUiEffect.RedirectToChatWebView -> redirectToChatWebView(effect.messageId)
            is ShopInfoUiEffect.RedirectToShopReviewPage -> redirectToShopReviewPage(effect.shopId)
            is ShopInfoUiEffect.RedirectToProductReviewPage -> redirectToProductReviewPage(
                effect.reviewImageIndex,
                effect.shopId,
                effect.review
            )

            is ShopInfoUiEffect.RedirectToProductReviewGallery -> redirectToProductReviewGallery(
                effect.productId
            )
        }
    }

    private fun renderLoadingState() {
        binding?.loader?.visible()
        binding?.mainContent?.gone()
        binding?.globalError?.gone()
    }

    private fun renderErrorState() {
        binding?.loader?.gone()
        binding?.mainContent?.gone()
        binding?.globalError?.visible()
    }

    private fun renderMainContent(uiState: ShopInfoUiState) {
        binding?.loader?.gone()
        binding?.mainContent?.visible()
        binding?.globalError?.gone()
        renderContent(uiState)
    }

    private fun renderContent(uiState: ShopInfoUiState) {
        renderShopCoreInfo(uiState)
        renderShopInfo(uiState)
        renderShopPharmacy(uiState)
        renderShopRatingAndReview(uiState)
        renderShopPerformance(uiState)
        renderShopNotes(uiState)
        renderShopDescription(uiState)
        renderShopSupportedShipment(uiState)
        renderShopReport(uiState)
    }

    private fun renderShopCoreInfo(uiState: ShopInfoUiState) {
        val hasPharmacyLicenseBadge = uiState.info.showPharmacyLicenseBadge
        val hasShopBadge = uiState.info.shopBadgeUrl.isNotEmpty()

        binding?.run {
            imgShop.loadImage(uiState.info.shopImageUrl)

            imgShopBadge.isVisible = hasShopBadge
            if (hasShopBadge) {
                imgShopBadge.loadImage(uiState.info.shopBadgeUrl)
            }

            tpgShopName.text = MethodChecker.fromHtml(uiState.info.shopName)
            tpgLicensedPharmacy.isVisible = hasPharmacyLicenseBadge

            val shopDynamicUsp = uiState.info.shopUsp.joinToString(separator = " • ") { it }
            tpgShopUsp.text = MethodChecker.fromHtml(shopDynamicUsp)
            tpgShopUsp.isVisible = uiState.info.shopUsp.isNotEmpty()
        }
    }

    private fun renderShopInfo(uiState: ShopInfoUiState) {
        binding?.run {
            tpgShopInfoLocation.text = uiState.info.mainLocation
            renderOperationalHours(uiState.info.operationalHours)
            tpgShopInfoJoinDate.text = uiState.info.shopJoinDate
            tpgShopInfoTotalProduct.text = uiState.info.totalProduct.splitByThousand()
        }
    }

    private fun renderOperationalHours(operationalHours: Map<String, List<ShopOperationalHourWithStatus>>) {
        binding?.layoutOperationalHoursContainer?.removeAllViews()

        when {
            operationalHours.isEmpty() -> {
                val text = context?.getString(R.string.shop_info_ops_hour_open_twenty_four_hour).orEmpty()
                val operationalHourTypography = createOperationalHoursTypography(text)
                binding?.layoutOperationalHoursContainer?.addView(operationalHourTypography)
            }
            isOpen24HoursEveryday(operationalHours) -> {
                val text = context?.getString(R.string.shop_info_ops_hour_open_24_hours).orEmpty()
                val operationalHourTypography = createOperationalHoursTypography(text)
                binding?.layoutOperationalHoursContainer?.addView(operationalHourTypography)
            }
            isOpenWithSameOpenAndCloseTimeEveryday(operationalHours) -> {
                val groupedDays = context?.getString(R.string.shop_info_ops_hour_monday_to_sunday).orEmpty()
                val operationalHour = getOpenAndCloseTime(operationalHours) ?: return
                val shopOpenCloseTime = context?.getString(R.string.shop_info_ops_hour_placeholder_open_range, operationalHour.startTime, operationalHour.endTime).orEmpty()

                val text = "$groupedDays: $shopOpenCloseTime"
                val operationalHourTypography = createOperationalHoursTypography(text)
                binding?.layoutOperationalHoursContainer?.addView(operationalHourTypography)
            }
            else -> renderShopOpenCloseTime(operationalHours)
        }
    }

    private fun renderShopOpenCloseTime(operationalHours: Map<String, List<ShopOperationalHourWithStatus>>) {
        operationalHours.forEach { (_, shopOpenCloseTimeWithStatus) ->
            val groupedDays = shopOpenCloseTimeWithStatus.joinToString(separator = ", ") {
                it.day.dayName()
            }

            val hour = shopOpenCloseTimeWithStatus.findShopOpenCloseTime()

            val text = "$groupedDays: $hour"
            val operationalHourTypography = createOperationalHoursTypography(text)
            binding?.layoutOperationalHoursContainer?.addView(operationalHourTypography)
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun renderShopPharmacy(uiState: ShopInfoUiState) {
        val isPharmacy = uiState.pharmacy.showPharmacyInfoSection
        val expandPharmacyInfo = uiState.pharmacy.expandPharmacyInfo
        val showCtaViewPharmacyLocation = uiState.pharmacy.nearestPickupAddress.isNotEmpty() && uiState.pharmacy.nearestPickupAddressGmapsUrl.isNotEmpty()

        binding?.run {
            labelShopPharmacyNearestPickup.isVisible = isPharmacy
            labelShopPharmacyPharmacistOpsHour.isVisible = isPharmacy && expandPharmacyInfo
            labelShopPharmacyPharmacistName.isVisible = isPharmacy && expandPharmacyInfo
            labelShopPharmacySiaNumber.isVisible = isPharmacy && expandPharmacyInfo
            labelShopPharmacySipaNumber.isVisible = isPharmacy && expandPharmacyInfo

            if (isPharmacy) {
                if (expandPharmacyInfo) {
                    // If pharmacy info expanded, then hide CTA "Selengkapnya"
                    tpgCtaExpandPharmacyInfo.invisible()
                } else {
                    // By default, show CTA "Selengkapnya"
                    tpgCtaExpandPharmacyInfo.visible()
                }
            } else {
                tpgCtaExpandPharmacyInfo.gone()
            }

            tpgCtaViewPharmacyMap.isVisible = isPharmacy && expandPharmacyInfo && showCtaViewPharmacyLocation
            iconViewPharmacyLocation.isVisible = isPharmacy && expandPharmacyInfo && showCtaViewPharmacyLocation

            tpgSectionTitlePharmacyInformation.isVisible = isPharmacy
            tpgShopPharmacyNearestPickup.isVisible = isPharmacy
            layoutShopPharmacyOpsHourContainer.isVisible = isPharmacy && expandPharmacyInfo
            tpgShopPharmacyPharmacistName.isVisible = isPharmacy && expandPharmacyInfo
            tpgShopPharmacySiaNumber.isVisible = isPharmacy && expandPharmacyInfo
            tpgShopPharmacySipaNumber.isVisible = isPharmacy && expandPharmacyInfo

            if (isPharmacy) {
                tpgShopPharmacyNearestPickup.text = uiState.pharmacy.nearestPickupAddress.ifEmpty { "-" }
                renderPharmacyOperationalHours(uiState.pharmacy.pharmacistOperationalHour)
                tpgShopPharmacyPharmacistName.text = uiState.pharmacy.pharmacistName.ifEmpty { "-" }
                tpgShopPharmacySiaNumber.text = uiState.pharmacy.siaNumber.ifEmpty { "-" }
                tpgShopPharmacySipaNumber.text = uiState.pharmacy.sipaNumber.ifEmpty { "-" }
            }
        }
    }

    private fun renderPharmacyOperationalHours(pharmacyOperationalHours: List<String>) {
        binding?.layoutShopPharmacyOpsHourContainer?.removeAllViews()

        if (pharmacyOperationalHours.isEmpty()) {
            val operationalHourTypography = createOperationalHoursTypography("-")
            binding?.layoutShopPharmacyOpsHourContainer?.addView(operationalHourTypography)
        } else {
            pharmacyOperationalHours.forEach { operationalHour ->
                val operationalHourTypography = createOperationalHoursTypography(operationalHour)
                binding?.layoutShopPharmacyOpsHourContainer?.addView(operationalHourTypography)
            }
        }
    }

    private fun createOperationalHoursTypography(text: String): Typography {
        val textViewOperationalHour = Typography(requireContext()).apply {
            setType(Typography.DISPLAY_2)
            setTextColor(ContextCompat.getColor(this.context, unifyprinciplesR.color.Unify_NN950))

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.topMargin = MARGIN_4_DP.toPx()
            layoutParams = params
        }

        textViewOperationalHour.text = text

        return textViewOperationalHour
    }

    private fun renderShopRatingAndReview(uiState: ShopInfoUiState) {
        renderRatingAndReviewSummary(uiState.rating)
        renderRating(uiState.rating)
        renderTopReview(uiState.review)
    }

    private fun renderRatingAndReviewSummary(rating: ShopRating) {
        val showRating = rating.totalRating.isMoreThanZero()

        binding?.tpgSectionTitleBuyerReview?.isVisible = showRating
        binding?.iconRating?.isVisible = showRating
        binding?.tpgShopRating?.isVisible = showRating
        binding?.tpgRatingAndReviewText?.isVisible = showRating
        binding?.iconChevronReview?.isVisible = showRating

        if (showRating) {
            val ratingAndReviewText = when {
                rating.totalRating.isMoreThanZero() && rating.totalRatingTextAndImage.isMoreThanZero() -> {
                    getString(R.string.shop_info_placeholder_rating_and_review, rating.totalRatingFmt, rating.totalRatingTextAndImageFmt)
                }
                rating.totalRating.isMoreThanZero() -> {
                    getString(R.string.shop_info_placeholder_rating, rating.totalRatingFmt)
                }
                rating.totalRatingTextAndImage.isMoreThanZero() -> {
                    getString(R.string.shop_info_placeholder_review, rating.totalRatingTextAndImageFmt)
                }
                else -> ""
            }

            binding?.tpgRatingAndReviewText?.text = ratingAndReviewText
            binding?.tpgShopRating?.text = rating.ratingScore
        }
    }

    private fun renderRating(rating: ShopRating) {
        val showRating = rating.totalRating.isMoreThanZero()

        binding?.layoutRating?.isVisible = rating.positivePercentageFmt.isNotEmpty()
        binding?.layoutRating?.setOnClickListener {
            viewModel.processEvent(ShopInfoUiEvent.TapShopRating)
        }

        val satisfactionPercentage = rating.positivePercentageFmt.digitsOnly().toInt()
        binding?.tpgBuyerSatisfactionPercentage?.text = getString(R.string.shop_info_placeholder_discount_percentage, satisfactionPercentage)

        binding?.layoutRatingBarContainer?.isVisible = showRating
        binding?.layoutRatingBarContainer?.setOnClickListener {
            viewModel.processEvent(ShopInfoUiEvent.TapShopRating)
        }

        if (showRating) {
            renderRatingList(rating.detail)
        }
    }

    private fun renderRatingList(ratings: List<ShopRating.Detail>) {
        binding?.layoutRatingBarContainer?.removeAllViews()

        ratings.forEach { rating ->

            val ratingView = layoutInflater.inflate(R.layout.item_shop_info_rating, binding?.layoutRatingBarContainer, false)

            val progressBarRating = ratingView.findViewById<ProgressBarUnify?>(R.id.progressBarRating)
            progressBarRating.setValue(rating.percentageFloat.toInt(), isSmooth = false)
            progressBarRating?.progressBarColorType = ProgressBarUnify.COLOR_YELLOW

            val tpgTotalRatingNumber = ratingView.findViewById<Typography?>(R.id.tpgTotalRatingNumber)
            tpgTotalRatingNumber.text = rating.formattedTotalReviews

            val tpgRating = ratingView.findViewById<Typography?>(R.id.tpgRating)
            tpgRating.text = rating.rate.toString()

            binding?.layoutRatingBarContainer?.addView(ratingView)
        }
    }

    private fun renderTopReview(review: ShopReview) {
        if (this.review == review) return
        this.review = review

        val showReview = review.reviews.size.isMoreThanZero()

        binding?.layoutReviewContainer?.isVisible = showReview
        binding?.shopReviewView?.isVisible = showReview

        if (showReview) {
            binding?.shopReviewView?.renderReview(viewLifecycleOwner.lifecycle, this, review)
            binding?.shopReviewView?.setOnReviewImageClick { review, reviewImageIndex ->
                viewModel.processEvent(ShopInfoUiEvent.TapReviewImage(review, reviewImageIndex))
            }
            binding?.shopReviewView?.setOnReviewImageViewAllClick { buyerReview ->
                viewModel.processEvent(ShopInfoUiEvent.TapReviewImageViewAll(buyerReview.product.productId))
            }
            binding?.shopReviewView?.setOnReviewSwiped { reviewIndex ->
                viewModel.processEvent(ShopInfoUiEvent.SwipeReview(reviewIndex))
            }
        }
    }

    private fun renderShopPerformance(uiState: ShopInfoUiState) {
        binding?.run {
            val showShopPerformanceSection = false

            tpgSectionTitleShopPerformance.isVisible = showShopPerformanceSection
            tpgProductSoldCount.isVisible = showShopPerformanceSection
            tpgChatPerformance.isVisible = showShopPerformanceSection
            tpgOrderProcessDuration.isVisible = showShopPerformanceSection

            tpgChatPerformance.isVisible = showShopPerformanceSection
            labelProductSoldCount.isVisible = showShopPerformanceSection
            labelChatPerformance.isVisible = showShopPerformanceSection
            labelOrderProcessTime.isVisible = showShopPerformanceSection

            if (showShopPerformanceSection) {
                labelProductSoldCount.text = uiState.shopPerformance.totalProductSoldCount.toIntOrZero().thousandFormatted(1).ifEmpty { "-" }
                labelChatPerformance.text = when (uiState.shopPerformance.chatPerformance) {
                    is ShopPerformanceDuration.Day -> getString(R.string.shop_info_placeholder_operational_hour_around_day, uiState.shopPerformance.chatPerformance.value)
                    is ShopPerformanceDuration.Hour -> getString(R.string.shop_info_placeholder_operational_hour_around_hour, uiState.shopPerformance.chatPerformance.value)
                    is ShopPerformanceDuration.Minute -> getString(R.string.shop_info_placeholder_operational_hour_around_minute, uiState.shopPerformance.chatPerformance.value)
                }
                labelOrderProcessTime.text = uiState.shopPerformance.orderProcessTime.ifEmpty { "-" }
            }
        }
    }

    private fun renderShopNotes(uiState: ShopInfoUiState) {
        val hasShopNotes = uiState.shopNotes.isNotEmpty()

        binding?.run {
            tpgSectionTitleShopNotes.isVisible = hasShopNotes
            layoutShopNotesContainer.isVisible = hasShopNotes

            if (hasShopNotes) {
                renderShopNoteList(uiState.shopNotes)
            }
        }
    }

    private fun renderShopNoteList(shopNotes: List<ShopNote>) {
        binding?.layoutShopNotesContainer?.removeAllViews()

        shopNotes.forEach { shopNote ->
            val shopNoteView = layoutInflater.inflate(R.layout.item_shop_info_shop_note, binding?.layoutShopNotesContainer, false)
            val shopNoteTitle = shopNoteView.findViewById<Typography?>(R.id.tpgShopNote)
            val shopNoteChevron = shopNoteView.findViewById<IconUnify?>(R.id.iconChevron)

            shopNoteTitle.text = shopNote.title
            shopNoteTitle?.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopNote(shopNote))
            }
            shopNoteChevron.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopNote(shopNote))
            }

            binding?.layoutShopNotesContainer?.addView(shopNoteView)
        }
    }

    private fun renderShopDescription(uiState: ShopInfoUiState) {
        val hasShopDescription = uiState.info.shopDescription.isNotEmpty()

        binding?.tpgSectionTitleShopDescription?.isVisible = hasShopDescription
        binding?.tpgShopDescription?.isVisible = hasShopDescription

        if (hasShopDescription) {
            binding?.tpgShopDescription?.text = MethodChecker.fromHtml(uiState.info.shopDescription)
        }
    }

    private fun renderShopSupportedShipment(uiState: ShopInfoUiState) {
        val hasSupportedShipment = uiState.shipments.isNotEmpty()

        binding?.tpgSectionTitleShopShopSupportedShipment?.isVisible = hasSupportedShipment
        binding?.layoutShopSupportedShipmentContainer?.isVisible = hasSupportedShipment

        if (hasSupportedShipment) {
            renderShopSupportedShipmentList(uiState.shipments)
        }
    }

    private fun renderShopSupportedShipmentList(shipments: List<ShopSupportedShipment>) {
        binding?.layoutShopSupportedShipmentContainer?.removeAllViews()

        shipments.forEachIndexed { index, shipment ->
            val shipmentView = layoutInflater.inflate(R.layout.item_shop_info_shipment, binding?.layoutShopSupportedShipmentContainer, false)
            val imgShipment = shipmentView.findViewById<ImageUnify?>(R.id.imgShipment)
            val tpgShipmentName = shipmentView.findViewById<Typography?>(R.id.tpgShipmentName)
            val tpgShipmentServices = shipmentView.findViewById<Typography?>(R.id.tpgShipmentServices)
            val divider = shipmentView.findViewById<View?>(R.id.dividerShopSupportedShipment)

            imgShipment.loadImage(shipment.imageUrl)
            tpgShipmentName.text = shipment.title
            tpgShipmentServices.text = shipment.serviceNames.joinToString(separator = ", ") { it }

            val isLastItem = index == (shipments.size - Int.ONE)
            divider.isVisible = !isLastItem

            binding?.layoutShopSupportedShipmentContainer?.addView(shipmentView)
        }
    }

    private fun renderShopReport(uiState: ShopInfoUiState) {
        if (uiState.isLoadingShopReport) {
            binding?.tpgReportShop?.invisible()
            binding?.loaderReportShop?.visible()
        } else {
            binding?.tpgReportShop?.visible()
            binding?.loaderReportShop?.gone()
        }
    }

    private fun redirectToGmaps(gmapsUrl: String) {
        if (!isAdded) return
        if (gmapsUrl.isEmpty()) return

        val webViewUrl = "tokopedia://webview?url=$gmapsUrl"

        try {
            RouteManager.route(context, webViewUrl)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun redirectToShopReviewPage(shopId: String) {
        if (!isAdded) return
        if (shopId.isEmpty()) return

        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_REVIEW, shopId, APP_LINK_QUERY_STRING_REVIEW_SOURCE)
        RouteManager.route(context, appLink)
    }

    private fun redirectToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun redirectToChatWebView(messageId: String) {
        val reportUrl = "${TkpdBaseURL.CHAT_REPORT_URL}$messageId${ShopInfoFragment.SOURCE_PAGE}"

        val intent = ReportShopWebViewActivity.getStartIntent(context ?: return, reportUrl)
        startActivityForResult(intent, REQUEST_CODE_REPORT_SHOP)
    }

    private fun redirectToProductReviewPage(
        reviewImageIndex: Int,
        shopId: String,
        review: ShopReview.Review
    ) {
        ReviewMediaGalleryRouter.routeToReviewMediaGallery(
            context = context ?: return,
            pageSource = ReviewMediaGalleryRouter.PageSource.SHOP_INFO_PAGE,
            productID = review.product.productId,
            shopID = shopId,
            isProductReview = true,
            isFromGallery = false,
            mediaPosition = reviewImageIndex.inc(),
            showSeeMore = false,
            preloadedDetailedReviewMediaResult = createReviewModel(review)
        ).run { startActivity(this) }
    }

    private fun createReviewModel(review: ShopReview.Review): ProductrevGetReviewMedia {
        val reviewDetail = ReviewDetail(
            shopId = shopId,
            user = ReviewerUserInfo(
                userId = review.reviewerId,
                fullName = review.reviewerName,
                image = review.avatar,
                url = "",
                label = review.reviewerLabel
            ),
            feedbackId = review.reviewId,
            variantName = review.product.productVariant.variantName,
            rating = review.rating,
            review = review.reviewText,
            createTimestamp = review.reviewTime,
            isReportable = review.state.isReportable,
            isAnonymous = review.state.isAnonymous,
            isLiked = review.likeDislike.likeStatus == Int.ONE,
            totalLike = review.likeDislike.totalLike,
            userStats = emptyList(),
            badRatingReasonFmt = review.badRatingReasonFmt
        )

        val reviewMedia = review.attachments.mapIndexed { index, attachment ->
            ReviewMedia(
                imageId = attachment.attachmentId,
                feedbackId = review.reviewId,
                mediaNumber = index.plus(Int.ONE)
            )
        }

        val reviewGalleryImages = review.attachments.map { attachment ->
            ReviewGalleryImage(
                attachmentId = attachment.attachmentId,
                thumbnailURL = attachment.thumbnailURL,
                fullsizeURL = attachment.fullSizeURL,
                description = review.reviewText,
                feedbackId = review.reviewId
            )
        }

        return ProductrevGetReviewMedia(
            reviewMedia = reviewMedia,
            detail = Detail(
                reviewDetail = listOf(reviewDetail),
                reviewGalleryImages = reviewGalleryImages,
                reviewGalleryVideos = emptyList(),
                mediaCountFmt = review.attachments.size.toString(),
                mediaCount = review.attachments.size.toLong()
            )
        )
    }

    private fun redirectToProductReviewGallery(productId: String) {
        val appLink = UriUtil.buildUri(ApplinkConst.PRODUCT_REPUTATION, productId)
        RouteManager.route(context, appLink)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_REPORT_SHOP -> onReturnFromReportUser(data, resultCode)
            REQUEST_CODE_LOGIN -> onReturnFromLogin(data, resultCode)
        }
    }

    private fun onReturnFromLogin(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != Activity.RESULT_OK) return
        viewModel.processEvent(ShopInfoUiEvent.ReportShop)
    }

    private fun onReturnFromReportUser(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != Activity.RESULT_OK) return

        showToaster(
            message = getString(R.string.label_report_success),
            view = view ?: return,
            ctaText = "",
            onCtaClicked = {},
            anchorView = null
        )
    }

    private fun showShopNoteDetailBottomSheet(shopNote: ShopNote) {
        if (!isAdded) return

        val bottomSheet = ShopNoteDetailBottomSheet.newInstance(shopNote.title, shopNote.description)
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun ShopOperationalHourWithStatus.Day.dayName(): String {
        return when (this) {
            ShopOperationalHourWithStatus.Day.MONDAY -> context?.getString(R.string.shop_info_ops_hour_monday).orEmpty()
            ShopOperationalHourWithStatus.Day.TUESDAY -> context?.getString(R.string.shop_info_ops_hour_tuesday).orEmpty()
            ShopOperationalHourWithStatus.Day.WEDNESDAY -> context?.getString(R.string.shop_info_ops_hour_wednesday).orEmpty()
            ShopOperationalHourWithStatus.Day.THURSDAY -> context?.getString(R.string.shop_info_ops_hour_thursday).orEmpty()
            ShopOperationalHourWithStatus.Day.FRIDAY -> context?.getString(R.string.shop_info_ops_hour_friday).orEmpty()
            ShopOperationalHourWithStatus.Day.SATURDAY -> context?.getString(R.string.shop_info_ops_hour_saturday).orEmpty()
            ShopOperationalHourWithStatus.Day.SUNDAY -> context?.getString(R.string.shop_info_ops_hour_sunday).orEmpty()
            ShopOperationalHourWithStatus.Day.UNDEFINED -> ""
        }
    }

    private fun List<ShopOperationalHourWithStatus>.findShopOpenCloseTime(): String {
        if (isEmpty()) return ""

        val operationalHour = first()

        return when (operationalHour.status) {
            ShopOperationalHourWithStatus.Status.OPEN -> {
                context?.getString(
                    R.string.shop_info_ops_hour_placeholder_open_range,
                    operationalHour.startTime,
                    operationalHour.endTime
                ).orEmpty()
            }
            ShopOperationalHourWithStatus.Status.CLOSED -> {
                context?.getString(R.string.shop_info_ops_hour_closed).orEmpty()
            }
            ShopOperationalHourWithStatus.Status.OPEN24HOURS -> {
                context?.getString(R.string.shop_info_ops_hour_open_twenty_four_hour).orEmpty()
            }
        }
    }

    private fun isOpen24HoursEveryday(operationalHours: Map<String, List<ShopOperationalHourWithStatus>>): Boolean {
        if (operationalHours.isEmpty()) return false

        val operationalHour = operationalHours.entries.firstOrNull()
        if (operationalHour == null) return false

        val (_, operationalHourStatus) = operationalHour

        val status = operationalHourStatus.firstOrNull()?.status
        if (status == null) return false

        val isMondayToSundayHaveSameOperationalHour = operationalHourStatus.size == SEVEN_DAY
        val isMondayToSundayOpen24Hours = status == ShopOperationalHourWithStatus.Status.OPEN24HOURS

        return isMondayToSundayHaveSameOperationalHour && isMondayToSundayOpen24Hours
    }

    private fun isOpenWithSameOpenAndCloseTimeEveryday(operationalHours: Map<String, List<ShopOperationalHourWithStatus>>): Boolean {
        if (operationalHours.isEmpty()) return false

        val operationalHour = operationalHours.entries.firstOrNull()
        if (operationalHour == null) return false

        val (_, operationalHourStatus) = operationalHour

        val status = operationalHourStatus.firstOrNull()?.status
        if (status == null) return false

        val isMondayToSundayHaveSameOperationalHour = operationalHourStatus.size == SEVEN_DAY
        val isMondayToSundayOpenAtSameTime = status == ShopOperationalHourWithStatus.Status.OPEN

        return isMondayToSundayHaveSameOperationalHour && isMondayToSundayOpenAtSameTime
    }

    private fun getOpenAndCloseTime(
        operationalHours: Map<String, List<ShopOperationalHourWithStatus>>
    ): ShopOperationalHourWithStatus? {
        if (operationalHours.isEmpty()) return null

        val operationalHour = operationalHours.entries.firstOrNull()
        if (operationalHour == null) return null

        val (_, operationalHourStatus) = operationalHour

        return operationalHourStatus.firstOrNull()
    }
}
