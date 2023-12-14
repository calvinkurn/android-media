package com.tokopedia.epharmacy.component.viewholder

import android.animation.Animator
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAttachmentDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.ui.fragment.EPharmacyQuantityChangeFragment
import com.tokopedia.epharmacy.utils.EPHARMACY_FULL_ALPHA
import com.tokopedia.epharmacy.utils.EPHARMACY_HALF_ALPHA
import com.tokopedia.epharmacy.utils.EPharmacyConsultationStatus
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.EventKeys
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.track.builder.Tracker
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.epharmacy.R as epharmacyR

class EPharmacyAttachmentViewHolder(private val view: View, private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyAttachmentDataModel?>(view) {

    private val productText = view.findViewById<Typography>(R.id.lbl_PAP_productName)
    private val orderTitleText = view.findViewById<Typography>(R.id.pesanan_title)
    private val shopNameText = view.findViewById<Typography>(R.id.lbl_PAP_shopName)
    private val shopIcon = view.findViewById<ImageUnify>(R.id.shop_icon)
    private val enablerImage = view.findViewById<ImageUnify>(R.id.enabler_image)
    private val partnerTitle = view.findViewById<Typography>(R.id.partner_title)
    private val productQuantity = view.findViewById<Typography>(R.id.lbl_PAP_productWeight)
    private val productAmount = view.findViewById<Typography>(R.id.lbl_PAP_productAmount)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val productImageCard = view.findViewById<CardUnify2>(R.id.product_image_card)
    private val divider = view.findViewById<DividerUnify>(R.id.divider)
    private val productAccordionView = view.findViewById<LinearLayout>(R.id.btn_PAP_minimize)
    private val productAccordionViewRL = view.findViewById<RelativeLayout>(R.id.rl_expand_other_product)
    private val productAccordionRV = view.findViewById<RecyclerView>(R.id.accordion_expandable_rv)
    private val productAccordionChevron = view.findViewById<IconUnify>(R.id.iv_expand_other_product)
    private val productChevronTitle = view.findViewById<Typography>(R.id.tv_expand_other_product)
    private val chatDokterUploadLayout = view.findViewById<LinearLayout>(R.id.btn_PAP_chatDokter)
    private val containerUploadPrescription = view.findViewById<ConstraintLayout>(R.id.container_upload_prescription)
    private val chatDokterUploadText = view.findViewById<Typography>(R.id.lblPrescriptionAttached)
    private val chatDokterUploadSubText = view.findViewById<Typography>(R.id.lblPrescriptionEnablerName)
    private val chatDokterUploadIcon = view.findViewById<ImageUnify>(R.id.upload_icon)
    private val topView = view.findViewById<View>(R.id.transparent_view_top)
    private val bottomView = view.findViewById<View>(R.id.transparent_view_bottom)
    private val ticker = view.findViewById<Ticker>(R.id.lblChangingProductWarning)
    private val divisionSingleProduct = view.findViewById<View>(R.id.division_single_card)
    private val quantityEditorLayout = view.findViewById<ConstraintLayout>(R.id.quantity_editor_layout)
    private val initialProductQuantity = view.findViewById<Typography>(R.id.lblQuantityOrderInit)
    private val quantityChangedEditor = view.findViewById<QuantityEditorUnify>(R.id.quantity_change)
    private val productQuantityType = view.findViewById<Typography>(R.id.quantity_type)
    private val totalQuantity = view.findViewById<Typography>(R.id.lblSubtotalProductQuantity)
    private val totalAmount = view.findViewById<Typography>(R.id.lblFinalProductPrice)
    companion object {
        val LAYOUT = R.layout.epharmacy_prescription_attachment_view_item
        private const val VIBRATION_ANIMATION_DURATION = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
        const val MIN_VALUE_OF_PRODUCT_EDITOR = 1
    }

    private val ePharmacyAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { EPharmacyAdapterFactoryImpl(ePharmacyListener) }

    private val ePharmacyAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseEPharmacyDataModel> = AsyncDifferConfig.Builder(
            EPharmacyAttachmentDetailDiffUtil()
        )
            .build()
        EPharmacyAdapter(asyncDifferConfig, ePharmacyAdapterFactory)
    }

    private var dataModel: EPharmacyAttachmentDataModel? = null

    override fun bind(element: EPharmacyAttachmentDataModel?) {
        dataModel = element
        renderGroupData()
    }

    private fun renderGroupData() {
        renderError()
        renderOrderTitle()
        initializeSums()
        renderQuantityChangeViews()
        renderQuantityChangedLayout()
        renderPartnerData()
        renderShopData()
        renderProductsData()
        renderButton()
        renderDivider()
        renderTicker()
        renderObstruction()
    }

    private fun renderTicker() {
        if (!dataModel?.ticker?.message.isNullOrBlank()) {
            ticker.show()
            ticker.tickerType = dataModel?.ticker?.tickerType ?: Ticker.TYPE_INFORMATION
            ticker.setHtmlDescription(dataModel?.ticker?.message.orEmpty())
            sendViewChangedQuantityTickerEvent("${dataModel?.enablerName} - ${dataModel?.epharmacyGroupId} - ${dataModel?.tokoConsultationId}")
        } else {
            ticker.hide()
        }
    }

    private fun renderQuantityChangedLayout() {
        if (dataModel?.product?.qtyComparison != null && dataModel?.isAccordionEnable.orFalse()) {
            renderEditorLayout()
        } else {
            quantityEditorLayout?.hide()
        }
    }

    private fun renderQuantityChangeViews() {
        if (ePharmacyListener is EPharmacyQuantityChangeFragment) {
            productAmount.show()
            productAmount.text = EPharmacyUtils.getTotalAmountFmt(dataModel?.product?.price)
            initialProductQuantity.text = java.lang.String.format(
                itemView.context.getString(epharmacyR.string.epharmacy_barang_quantity),
                dataModel?.product?.qtyComparison?.initialQty.toString()
            )
            productQuantityType.text = itemView.context.getString(R.string.epharmacy_barang)
        }
    }

    private fun initializeSums() {
        if (dataModel?.product?.qtyComparison?.currentQty.isZero()) {
            dataModel?.product?.qtyComparison?.currentQty = dataModel?.product?.qtyComparison?.recommendedQty.orZero()
        }
        var quantity = dataModel?.product?.quantity.toIntOrZero()
        if (dataModel?.product?.qtyComparison != null) {
            quantity = dataModel?.product?.qtyComparison?.recommendedQty.orZero()
        }
        if (dataModel?.product?.subTotal == 0.0) {
            dataModel?.product?.subTotal = quantity.toDouble() * (dataModel?.product?.price.orZero())
        }
    }

    private fun renderEditorLayout() {
        quantityEditorLayout?.show()
        quantityChangedEditor.autoHideKeyboard = true
        quantityChangedEditor.maxValue = dataModel?.product?.qtyComparison?.recommendedQty.orZero()
        quantityChangedEditor.minValue = MIN_VALUE_OF_PRODUCT_EDITOR
        reCalculateSubTotal()
        quantityChangedEditor.setValue(dataModel?.product?.qtyComparison?.currentQty.orZero())
        quantityChangedEditor.setValueChangedListener { newValue, _, _ ->
            if (newValue == MIN_VALUE_OF_PRODUCT_EDITOR || newValue == dataModel?.product?.qtyComparison?.recommendedQty) {
                ePharmacyListener?. onEditorQuantityToast(
                    Toaster.TYPE_ERROR,
                    itemView.context.resources?.getString(epharmacyR.string.epharmacy_minimum_quantity_reached)
                        .orEmpty(),
                    dataModel?.enablerName,
                    dataModel?.tokoConsultationId,
                    dataModel?.epharmacyGroupId
                )
            }
            dataModel?.product?.qtyComparison?.currentQty = newValue
            val changeInTotal = reCalculateSubTotal()
            ePharmacyListener?.onQuantityChanged(
                changeInTotal,
                dataModel?.product?.productId.toString(),
                dataModel?.enablerName,
                dataModel?.tokoConsultationId,
                dataModel?.epharmacyGroupId
            )
        }
    }

    private fun reCalculateSubTotal(): Double {
        val newTotal = EPharmacyUtils.getTotalAmount(dataModel?.product?.qtyComparison?.currentQty, dataModel?.product?.price)
        val changeInTotal = newTotal.minus(dataModel?.product?.subTotal ?: 0.0)
        dataModel?.product?.subTotal = newTotal
        totalAmount.displayTextOrHide(EPharmacyUtils.getTotalAmountFmt(newTotal))
        totalQuantity.displayTextOrHide(
            java.lang.String.format(
                itemView.context.getString(epharmacyR.string.epharmacy_subtotal_quantity_change),
                dataModel?.product?.qtyComparison?.currentQty.toString()
            )
        )
        return changeInTotal
    }

    private fun renderOrderTitle() {
        if (!dataModel?.orderTitle.isNullOrBlank()) {
            orderTitleText.show()
            orderTitleText.text = dataModel?.orderTitle
        } else {
            orderTitleText.hide()
        }
    }

    private fun renderError() {
        if (dataModel?.isError == true && dataModel?.showUploadWidget == true && EPharmacyUtils.checkIsError(dataModel)) {
            containerUploadPrescription.setBackgroundResource(epharmacyR.drawable.epharmacy_bg_rounded_red)
            if (dataModel?.isFirstError == true) {
                chatDokterUploadLayout.animate()
                    .translationX(VIBRATION_ANIMATION_TRANSLATION_X.toFloat())
                    .setDuration(VIBRATION_ANIMATION_DURATION.toLong())
                    .setInterpolator(CycleInterpolator(VIBRATION_ANIMATION_CYCLE))
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {
                            ePharmacyListener?.onEndAnimation(bindingAdapterPosition, dataModel?.name)
                        }

                        override fun onAnimationCancel(animator: Animator) {}
                        override fun onAnimationRepeat(animator: Animator) {}
                    })
                    .start()
            }
            ePharmacyListener?.onError(bindingAdapterPosition, dataModel?.name)
        } else {
            containerUploadPrescription.setBackgroundResource(R.drawable.epharmacy_bg_rounded_grey)
        }
    }

    private fun renderObstruction() {
        if (dataModel?.consultationStatus == EPharmacyConsultationStatus.REJECTED.status
        ) {
            topView?.run {
                show()
                setOnClickListener {}
            }
            bottomView.run {
                show()
                setOnClickListener {}
            }
            productImageCard.alpha = EPHARMACY_HALF_ALPHA
        } else {
            topView.hide()
            bottomView.hide()
            productImageCard.alpha = EPHARMACY_FULL_ALPHA
        }
    }

    private fun renderPartnerData() {
        partnerTitle.show()
        if (dataModel?.enablerLogo.isNullOrBlank()) {
            enablerImage.hide()
        } else {
            enablerImage.show()
            enablerImage.loadImage(dataModel?.enablerLogo)
        }
    }

    private fun renderShopData() {
        shopNameText.displayTextOrHide(dataModel?.shopInfo?.shopName.orEmpty())
        shopIcon.show()
        shopIcon.loadImage(dataModel?.shopInfo?.shopLogoUrl)
    }

    private fun renderProductsData() {
        dataModel?.shopInfo?.products?.firstOrNull()?.let { firstProduct ->
            productText.text = firstProduct.name
            productQuantity.text = itemView.context.getString(R.string.epharmacy_quantity_weight_text, firstProduct.quantity, firstProduct.productTotalWeightFmt)
            productImageUnify.loadImage(firstProduct.productImage)
        }

        if ((dataModel?.shopInfo?.products?.size.orZero()) > 1) {
            productAccordionView.show()

            if (ePharmacyListener is EPharmacyQuantityChangeFragment) {
                productAccordionViewRL.hide()
                dataModel?.productsIsExpanded = true
            }

            if (productAccordionRV.adapter == null) {
                productAccordionRV.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                productAccordionRV.adapter = ePharmacyAdapter
                productAccordionViewRL.setOnClickListener {
                    ePharmacyListener?.onInteractAccordion(bindingAdapterPosition, dataModel?.productsIsExpanded.orFalse(), dataModel?.name)
                }
            }

            ePharmacyAdapter.submitList(dataModel?.subProductsDataModel)
            toggleProductListExpansion(dataModel?.productsIsExpanded.orFalse())
        } else {
            productAccordionView.hide()
        }
    }

    private fun toggleProductListExpansion(isExpanded: Boolean) {
        if (isExpanded) {
            productChevronTitle.text = view.context.getString(R.string.epharmacy_show_less)
            productAccordionRV.show()
            productAccordionChevron.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
        } else {
            productChevronTitle.text = view.context.getString(R.string.epharmacy_show_more)
            productAccordionChevron.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
            productAccordionRV.hide()
        }
    }

    private fun renderButton() {
        if (dataModel?.showUploadWidget == true && !dataModel?.prescriptionCTA?.title.isNullOrBlank()) {
            chatDokterUploadLayout.show()
            chatDokterUploadText.text = dataModel?.prescriptionCTA?.title
            if (!dataModel?.prescriptionCTA?.subtitle.isNullOrBlank()) {
                chatDokterUploadSubText.show()
                chatDokterUploadSubText.text = dataModel?.prescriptionCTA?.subtitle
            } else {
                chatDokterUploadSubText.hide()
            }
            chatDokterUploadIcon.loadImage(dataModel?.prescriptionCTA?.logoUrl)
            chatDokterUploadLayout.setOnClickListener {
                ePharmacyListener?.onCTACClick(bindingAdapterPosition, dataModel?.name)
            }
        } else {
            chatDokterUploadLayout.hide()
        }
    }

    private fun renderDivider() {
        if (((((dataModel?.shopInfo?.products?.size.orZero()) == 1) && dataModel?.showUploadWidget == false))) {
            divisionSingleProduct.show()
        } else {
            divisionSingleProduct.hide()
        }
        if (dataModel?.showDivider == true) divider.show() else divider.hide()
    }

    private fun sendViewChangedQuantityTickerEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction("view changed quantity ticker")
            .setEventCategory("epharmacy attach prescription page")
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "45873")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
