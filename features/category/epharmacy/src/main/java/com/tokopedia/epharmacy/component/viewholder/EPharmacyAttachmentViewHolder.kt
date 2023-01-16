package com.tokopedia.epharmacy.component.viewholder

import android.animation.Animator
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAttachmentDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.utils.EPharmacyConsultationStatus
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.PRODUCT_COMPONENT
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class EPharmacyAttachmentViewHolder(private val view: View, private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyAttachmentDataModel?>(view) {

    private val productText = view.findViewById<Typography>(R.id.lbl_PAP_productName)
    private val shopNameText = view.findViewById<Typography>(R.id.lbl_PAP_shopName)
    private val shopIcon = view.findViewById<ImageUnify>(R.id.shop_icon)
    private val enablerImage = view.findViewById<ImageUnify>(R.id.enabler_image)
    private val partnerTitle = view.findViewById<Typography>(R.id.partner_title)
    private val productQuantity = view.findViewById<Typography>(R.id.lbl_PAP_productWeight)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val productImageCard = view.findViewById<CardUnify2>(R.id.product_image_card)
    private val divider = view.findViewById<DividerUnify>(R.id.divider)
    private val productAccordionView = view.findViewById<LinearLayout>(R.id.btn_PAP_minimize)
    private val productAccordionRV = view.findViewById<RecyclerView>(R.id.accordion_expandable_rv)
    private val productAccordionChevron = view.findViewById<IconUnify>(R.id.iv_expand_other_product)
    private val productChevronTitle = view.findViewById<Typography>(R.id.tv_expand_other_product)
    private val chatDokterUploadLayout = view.findViewById<LinearLayout>(R.id.btn_PAP_chatDokter)
    private val containerUploadPrescription = view.findViewById<ConstraintLayout>(R.id.container_upload_prescription)
    private val chatDokterUploadText = view.findViewById<Typography>(R.id.upload_prescription_text)
    private val chatDokterUploadSubText = view.findViewById<Typography>(R.id.upload_description_text)
    private val chatDokterUploadIcon = view.findViewById<ImageUnify>(R.id.upload_icon)
    private val topView = view.findViewById<View>(R.id.transparent_view_top)
    private val bottomView = view.findViewById<View>(R.id.transparent_view_bottom)
    private val ticker = view.findViewById<Ticker>(R.id.ticker)
    private val divisionSingleProduct = view.findViewById<View>(R.id.division_single_card)

    companion object {
        val LAYOUT = R.layout.epharmacy_prescription_attachment_view_item
        private const val VIBRATION_ANIMATION_DURATION = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }

    private val ePharmacyAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { EPharmacyAdapterFactoryImpl(null) }

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
        renderPartnerData()
        renderShopData()
        renderProductsData()
        renderButton()
        renderDivider()
        renderObstruction()
    }

    private fun renderError() {
        if (dataModel?.isError == true && dataModel?.showUploadWidget == true && EPharmacyUtils.checkIsError(dataModel)) {
            containerUploadPrescription.setBackgroundResource(com.tokopedia.epharmacy.R.drawable.epharmacy_bg_rounded_red)
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
        if (dataModel?.consultationStatus == EPharmacyConsultationStatus.REJECTED.status ||
            dataModel?.consultationStatus == EPharmacyConsultationStatus.EXPIRED.status
        ) {
            topView?.run {
                show()
                setOnClickListener {}
            }
            bottomView.run {
                show()
                setOnClickListener {}
            }
            ticker.show()
            productImageCard.alpha = 0.5f
        } else {
            topView.hide()
            bottomView.hide()
            ticker.hide()
            productImageCard.alpha = 1f
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
        shopNameText.displayTextOrHide(dataModel?.shopInfo?.shopName ?: "")
        shopIcon.show()
        shopIcon.loadImage(dataModel?.shopInfo?.shopLogoUrl)
    }

    private fun renderProductsData() {
        dataModel?.shopInfo?.products?.firstOrNull()?.let { firstProduct ->
            productText.text = firstProduct.name
            productQuantity.text = itemView.context.getString(R.string.epharmacy_quantity_weight_text, firstProduct.quantity, firstProduct.productTotalWeightFmt)
            productImageUnify.loadImage(firstProduct.productImage)
        }

        if ((dataModel?.shopInfo?.products?.size ?: 0) > 1) {
            productAccordionView.show()
            if (productAccordionRV.adapter == null) {
                productAccordionRV.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                productAccordionRV.adapter = ePharmacyAdapter
                productAccordionView.setOnClickListener {
                    ePharmacyListener?.onInteractAccordion(bindingAdapterPosition, dataModel?.productsIsExpanded ?: false, dataModel?.name)
                }

            }

            dataModel?.shopInfo?.let { products ->
                ePharmacyAdapter.submitList(getProductVisitablesWithoutFirst(products))
            }

            if (dataModel?.productsIsExpanded == true) {
                productChevronTitle.text = view.context.getString(R.string.epharmacy_show_less)
                productAccordionRV.show()
                productAccordionChevron.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
            } else {
                productChevronTitle.text = view.context.getString(R.string.epharmacy_show_more)
                productAccordionChevron.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
                productAccordionRV.hide()
            }
        } else {
            productAccordionView.hide()
        }
    }

    private fun getProductVisitablesWithoutFirst(shopInfo : EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?)
    : List<BaseEPharmacyDataModel> {
        val productSubList = arrayListOf<EPharmacyAccordionProductDataModel>()
        shopInfo?.products?.forEachIndexed { index, product ->
            if (index != 0) {
                productSubList.add(EPharmacyAccordionProductDataModel("${PRODUCT_COMPONENT}_${product?.productId}",PRODUCT_COMPONENT,product))
            }
        }
        return productSubList
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
        if(dataModel?.shopInfo?.products?.size == 1 && dataModel?.showUploadWidget == false){
            divisionSingleProduct.show()
        }else {
            divisionSingleProduct.hide()
        }
        if (dataModel?.showDivider == true) divider.show() else divider.hide()
    }
}
