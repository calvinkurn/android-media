package com.tokopedia.shop.info.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
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
        private const val APPLINK_QUERY_STRING_REVIEW_SOURCE = "header"

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
        setupView()
        observeUiState()
        observeUiEffect()

        viewModel.processEvent(ShopInfoUiEvent.GetShopInfo(shopId, localCacheModel ?: return))
        registerBackPressEvent()
    }

    private fun registerBackPressEvent() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupView() {
        binding?.run {
            tpgCtaExpandPharmacyInfo.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapCtaExpandShopPharmacyInfo)
            }
            tpgCtaViewPharmacyMap.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapCtaViewPharmacyLocation)
            }
            iconChevronShopLocation.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapIconViewShopLocation)
            }
            iconChevronReview.setOnClickListener {
                viewModel.processEvent(ShopInfoUiEvent.TapIconViewAllShopReview)
            }
            globalError.setActionClickListener {
                viewModel.processEvent(
                    ShopInfoUiEvent.RetryGetShopInfo(
                        localCacheModel ?: return@setActionClickListener
                    )
                )
            }
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
            is ShopInfoUiEffect.RedirectToReviewDetailPage -> redirectToReviewDetailPage(effect.reviewId)
            is ShopInfoUiEffect.RedirectToShopReviewPage -> redirectToShopReviewPage(effect.shopId)
            is ShopInfoUiEffect.ShowShopLocationBottomSheet -> showShopLocationBottomSheet(effect.locations)
            is ShopInfoUiEffect.RedirectToShopNoteDetailPage -> redirectToShopNoteDetailPage(effect.shopId, effect.noteId)
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
    }

    private fun renderShopCoreInfo(uiState: ShopInfoUiState) {
        val hasUsp = uiState.info.shopUsp.isNotEmpty()
        val hasPharmacyLicenseBadge = uiState.info.showPharmacyLicenseBadge

        binding?.run {
            imgShop.loadImage(uiState.info.shopImageUrl) // TODO add circle border
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

            val isMultiLocation = uiState.info.otherLocations.isNotEmpty()
            tpgShopInfoOtherLocation.isVisible = isMultiLocation
            iconChevronShopLocation.isVisible = isMultiLocation
            if (isMultiLocation) {
                tpgShopInfoOtherLocation.text = getString(
                    R.string.shop_info_placeholder_other_location,
                    uiState.info.otherLocations.size
                )
            }

            renderOperationalHours(uiState.info.operationalHours)
            tpgShopInfoJoinDate.text = uiState.info.shopJoinDate
            tpgShopInfoTotalProduct.text = uiState.info.totalProduct.toString()
        }
    }

    private fun renderOperationalHours(operationalHours: Map<String, List<String>>) {
        val linearLayout = binding?.layoutOperationalHoursContainer
        linearLayout?.removeAllViews()

        operationalHours.forEach {
            val hour = it.key
            val days = it.value
            val groupedDays = days.joinToString(separator = ", ", postfix = ": ") { day -> day }

            val textViewOperationalHour = Typography(context ?: return).apply {
                setType(Typography.DISPLAY_2)
                setTextColor(ContextCompat.getColor(this.context, unifyprinciplesR.color.Unify_NN950))

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = MARGIN_4_DP.toPx()
                layoutParams = params
            }

            val operationalHourFormat = "%s %s"
            textViewOperationalHour.text = String.format(operationalHourFormat, groupedDays, hour)
            linearLayout?.addView(textViewOperationalHour)
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

            tpgSectionTitlePharmacyInformation.isVisible = isPharmacy
            tpgShopPharmacyNearestPickup.isVisible = isPharmacy
            tpgShopPharmacyPharmacistOpsHour.isVisible = isPharmacy && expandPharmacyInfo
            tpgShopPharmacyPharmacistName.isVisible = isPharmacy && expandPharmacyInfo
            tpgShopPharmacySiaNumber.isVisible = isPharmacy && expandPharmacyInfo
            tpgShopPharmacySipaNumber.isVisible = isPharmacy && expandPharmacyInfo

            if (isPharmacy) {
                tpgShopPharmacyNearestPickup.text = uiState.pharmacy.nearestPickupAddress.ifEmpty { "-" }
                tpgShopPharmacyPharmacistOpsHour.text = uiState.pharmacy.pharmacistOperationalHour.joinToString(separator = "\n") { it }.ifEmpty { "-" }
                tpgShopPharmacyPharmacistName.text = uiState.pharmacy.pharmacistName.ifEmpty { "-" }
                tpgShopPharmacySiaNumber.text = uiState.pharmacy.siaNumber.ifEmpty { "-" }
                tpgShopPharmacySipaNumber.text = uiState.pharmacy.sipaNumber.ifEmpty { "-" }
            }
        }
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
            val tpgRating = ratingView.findViewById<Typography?>(R.id.tpgRating)
            val progressBarRating = ratingView.findViewById<ProgressBarUnify?>(R.id.progressBarRating)
            val tpgTotalRatingNumber = ratingView.findViewById<Typography?>(R.id.tpgTotalRatingNumber)

            tpgRating.text = rating.rate.toString()
            progressBarRating.setValue(rating.percentageFloat.toInt()) tpgTotalRatingNumber.text = rating.formattedTotalReviews
            progressBarRating?.progressBarColorType = ProgressBarUnify.COLOR_YELLOW

            binding?.layoutRatingBarContainer?.addView(ratingView)
        }
    }

    private fun renderTopReview(review: ShopReview) {
        val showReview = review.totalReviews.isMoreThanZero()

        binding?.layoutReviewContainer?.isVisible = showReview
        binding?.shopReviewView?.isVisible = showReview

        if (showReview) {
            binding?.shopReviewView?.render(viewLifecycleOwner.lifecycle, this, review)
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
        binding?.tpgShopDescription?.isVisible = hasShopDescription
        binding?.tpgSectionTitleShopNotes?.isVisible = hasShopDescription

        binding?.tpgShopDescription?.text = MethodChecker.fromHtml(uiState.info.shopDescription)
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

        // TODO route to gmap app
    }
    private fun redirectToReviewDetailPage(reviewId: String) {
        if (!isAdded) return
        if (reviewId.isEmpty()) return

        // TODO route to review detail
    }
    private fun redirectToShopReviewPage(shopId: String) {
        if (!isAdded) return
        if (shopId.isEmpty()) return

        val appLink = UriUtil.buildUri(ApplinkConst.SHOP_REVIEW, shopId, APPLINK_QUERY_STRING_REVIEW_SOURCE)
        RouteManager.route(context, appLink)
    }

    private fun redirectToShopNoteDetailPage(shopId: String, noteId: String) {
        if (!isAdded) return
        if (noteId.isEmpty()) return

        val intent = ShopNoteDetailActivity.createIntent(context, shopId, noteId)
        startActivity(intent)
    }

    private fun showShopLocationBottomSheet(locations: List<String>) {
        if (!isAdded) return
        if (locations.isEmpty()) return

        // TODO route to location bottomsheet
    }
}
