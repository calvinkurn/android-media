package com.tokopedia.shop.info.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintSet
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
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.constant.TkpdBaseURL
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
import com.tokopedia.shop.info.domain.entity.ShopRating
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.view.model.ShopInfoUiEffect
import com.tokopedia.shop.info.view.model.ShopInfoUiEvent
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.info.view.viewmodel.ShopInfoReimagineViewModel
import com.tokopedia.shop.report.activity.ReportShopWebViewActivity
import com.tokopedia.shop_widget.note.view.activity.ShopNoteDetailActivity
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopInfoReimagineFragment : BaseDaggerFragment(), HasComponent<ShopInfoComponent> {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val MARGIN_4_DP = 4
        private const val APP_LINK_QUERY_STRING_REVIEW_SOURCE = "header"
        private const val REQUEST_CODE_REPORT_SHOP = 110
        private const val REQUEST_CODE_LOGIN = 100

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
    private val localCacheModel by lazy { ShopUtil.getShopPageWidgetUserAddressLocalData(context) }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Typography.setUnifyTypographyOSO(true)
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
        viewModel.processEvent(
            ShopInfoUiEvent.Setup(
                shopId,
                localCacheModel?.district_id.orEmpty(),
                localCacheModel?.city_id.orEmpty()
            )
        )
        setupView()
        observeUiState()
        observeUiEffect()

        viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)
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
            iconChevronReview.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapIconViewAllShopReview)
            }
            globalError.setActionClickListener {
                viewModel.processEvent(ShopInfoUiEvent.RetryGetShopInfo)
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
            is ShopInfoUiEffect.RedirectToShopNoteDetailPage -> redirectToShopNoteDetailPage(effect.shopId, effect.noteId)
            ShopInfoUiEffect.RedirectToLoginPage -> redirectToLoginPage()
            is ShopInfoUiEffect.RedirectToChatWebView -> redirectToChatWebView(effect.messageId)
            is ShopInfoUiEffect.RedirectToShopReviewPage -> redirectToShopReviewPage(effect.shopId)
            is ShopInfoUiEffect.RedirectToProductReviewPage -> redirectToProductReviewPage(effect.productId)
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
        val hasUsp = uiState.info.shopUsp.isNotEmpty()
        val hasPharmacyLicenseBadge = uiState.info.showPharmacyLicenseBadge

        binding?.run {
            imgShop.loadImage(uiState.info.shopImageUrl)
            imgShopBadge.loadImage(uiState.info.shopBadgeUrl)
            tpgShopName.text = uiState.info.shopName
            tpgLicensedPharmacy.isVisible = hasPharmacyLicenseBadge
            tpgShopUsp.text = uiState.info.shopUsp.joinToString(separator = " • ") { it }
            tpgShopUsp.isVisible = uiState.info.shopUsp.isNotEmpty()
        }

        val shopNameOnly = !hasUsp && !hasPharmacyLicenseBadge
        if (shopNameOnly) {
            makeShopNameCenteredVertically()
        }
    }

    private fun renderShopInfo(uiState: ShopInfoUiState) {
        binding?.run {
            tpgShopInfoLocation.text = uiState.info.mainLocation
            renderOperationalHours(uiState.info.operationalHours)
            tpgShopInfoJoinDate.text = uiState.info.shopJoinDate
            tpgShopInfoTotalProduct.text = uiState.info.totalProduct.toString()
        }
    }

    private fun renderOperationalHours(operationalHours: Map<String, List<String>>) {
        binding?.layoutOperationalHoursContainer?.removeAllViews()

        if (operationalHours.isEmpty()) {
            val operationalHourTypography = createOperationalHoursTypography("-")
            binding?.layoutOperationalHoursContainer?.addView(operationalHourTypography)
        } else {
            operationalHours.forEach { operationalHour ->
                val hour = operationalHour.key
                val days = operationalHour.value
                val groupedDays = days.joinToString(separator = ", ", postfix = ": ") { day -> day }

                val operationalHourFormat = "%s %s"
                val text = String.format(operationalHourFormat, groupedDays, hour)

                val operationalHourTypography = createOperationalHoursTypography(text)
                binding?.layoutOperationalHoursContainer?.addView(operationalHourTypography)
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun renderShopPharmacy(uiState: ShopInfoUiState) {
        val isPharmacy = uiState.pharmacy.showPharmacyInfoSection
        val expandPharmacyInfo = uiState.pharmacy.expandPharmacyInfo

        binding?.run {
            labelShopPharmacyNearestPickup.isVisible = isPharmacy
            labelShopPharmacyPharmacistOpsHour.isVisible = isPharmacy && expandPharmacyInfo
            labelShopPharmacyPharmacistName.isVisible = isPharmacy && expandPharmacyInfo
            labelShopPharmacySiaNumber.isVisible = isPharmacy && expandPharmacyInfo
            labelShopPharmacySipaNumber.isVisible = isPharmacy && expandPharmacyInfo

            tpgCtaExpandPharmacyInfo.isVisible = isPharmacy && !expandPharmacyInfo
            tpgCtaViewPharmacyMap.isVisible = isPharmacy && expandPharmacyInfo
            iconViewPharmacyLocation.isVisible = isPharmacy && expandPharmacyInfo

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
        val satisfactionPercentage = rating.positivePercentageFmt.digitsOnly().toInt()
        binding?.tpgBuyerSatisfactionPercentage?.text = getString(R.string.shop_info_placeholder_discount_percentage, satisfactionPercentage)

        binding?.layoutRatingBarContainer?.isVisible = showRating

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

        val showReview = review.totalReviews.isMoreThanZero()

        binding?.layoutReviewContainer?.isVisible = showReview
        binding?.shopReviewView?.isVisible = showReview

        if (showReview) {
            binding?.shopReviewView?.renderReview(viewLifecycleOwner.lifecycle, this, review)
            binding?.shopReviewView?.setOnReviewImageClick { buyerReview ->
                viewModel.processEvent(ShopInfoUiEvent.TapReviewImage(buyerReview.product.productId))
            }
            binding?.shopReviewView?.setOnReviewImageViewAllClick { buyerReview ->
                viewModel.processEvent(ShopInfoUiEvent.TapReviewImageViewAll(buyerReview.product.productId))
            }
        }
    }

    private fun renderShopPerformance(uiState: ShopInfoUiState) {
        binding?.run {
            labelProductSoldCount.text = uiState.shopPerformance.totalProductSoldCount.toIntOrZero().thousandFormatted(1).ifEmpty { "-" }
            labelChatPerformance.text = uiState.shopPerformance.chatPerformance.ifEmpty { "-" }
            labelOrderProcessTime.text = uiState.shopPerformance.orderProcessTime.ifEmpty { "-" }
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
                viewModel.processEvent(ShopInfoUiEvent.TapShopNote(shopNote.id))
            }
            shopNoteChevron.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapShopNote(shopNote.id))
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

    private fun makeShopNameCenteredVertically() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding?.constraintLayout)

        constraintSet.connect(R.id.tpgShopName, ConstraintSet.TOP, R.id.imgShop, ConstraintSet.TOP)
        constraintSet.connect(R.id.tpgShopName, ConstraintSet.BOTTOM, R.id.imgShop, ConstraintSet.BOTTOM)

        constraintSet.connect(R.id.imgShopBadge, ConstraintSet.TOP, R.id.imgShop, ConstraintSet.TOP)
        constraintSet.connect(R.id.imgShopBadge, ConstraintSet.BOTTOM, R.id.imgShop, ConstraintSet.BOTTOM)

        constraintSet.applyTo(binding?.constraintLayout)
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

    private fun redirectToShopNoteDetailPage(shopId: String, noteId: String) {
        if (!isAdded) return
        if (noteId.isEmpty()) return

        val intent = ShopNoteDetailActivity.createIntent(context, shopId, noteId)
        startActivity(intent)
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

    private fun redirectToProductReviewPage(productId: String) {
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
}
