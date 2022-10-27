package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import java.math.BigDecimal

class EPharmacyAttachmentViewHolder(val view: View) : AbstractViewHolder<EPharmacyAttachmentDataModel>(view) {

    private val orderName = view.findViewById<Typography>(R.id.orderName)
    private val productText = view.findViewById<Typography>(R.id.product_name)
    private val shopNameText = view.findViewById<Typography>(R.id.shop_name)
    private val shopIcon = view.findViewById<ImageUnify>(R.id.shop_icon)
    private val enablerImage = view.findViewById<ImageUnify>(R.id.enabler_image)
    private val productQuantity = view.findViewById<Typography>(R.id.product_quantity)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val divider = view.findViewById<DividerUnify>(R.id.divider)
    private val dividerProducts = view.findViewById<DividerUnify>(R.id.divider_products)
    private val productAccordionView = view.findViewById<AccordionUnify>(R.id.product_accordion_view)
    private val chatDokterUploadLayout = view.findViewById<LinearLayout>(R.id.chat_dokter_upload_layout)
    private val chatDokterUploadText = view.findViewById<Typography>(R.id.upload_prescription_text)
    private val chatDokterUploadIcon = view.findViewById<ImageUnify>(R.id.upload_icon)
    private var childAccordionView: View? = null

    companion object {
        val LAYOUT = R.layout.epharmacy_prescription_attachment_view_item
        private const val KILOGRAM_DIVIDER = 1000.0f
        private const val DIGIT_AFTER_COMMA = 2
        private const val LABEL_KILOGRAM = " kg"
        private const val LABEL_GRAM = " gr"
        val LAYOUT_ACCORDION = R.layout.epharmacy_accordion_expanded_layout
    }

    override fun bind(element: EPharmacyAttachmentDataModel) {
        renderGroupData(element)
    }

    private fun renderGroupData(dataModel: EPharmacyAttachmentDataModel) {
        renderPartnerData(dataModel)
        renderShopData(dataModel)
        renderProductsData(dataModel)
        renderButton(dataModel)
        renderDivider(dataModel)
    }

    private fun renderPartnerData(dataModel: EPharmacyAttachmentDataModel) {
        orderName.displayTextOrHide(dataModel.orderName ?: "")
        enablerImage.loadImage(dataModel.partnerLogo)
    }

    private fun renderShopData(dataModel: EPharmacyAttachmentDataModel) {
        shopNameText.displayTextOrHide(dataModel.shopInfo?.shopName ?: "")
        shopIcon.loadImage(dataModel.shopInfo?.shopLogoUrl)
    }

    private fun renderProductsData(dataModel: EPharmacyAttachmentDataModel) {
        dataModel.shopInfo?.products?.firstOrNull()?.let {  firstProduct ->
            productText.text = firstProduct.name
            productQuantity.text = firstProduct.quantityString
            productImageUnify.loadImage(firstProduct.productImage)
        }

        if(!dataModel.shopInfo?.products.isNullOrEmpty()){
            productAccordionView.show()
            childAccordionView = View.inflate(view.context, LAYOUT_ACCORDION, null)
            childAccordionView?.findViewById<RecyclerView>(R.id.accordion_expandable_rv)?.apply {
                //adapter = getAccordionAdapter(specList)
                //layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
            addAccordionData("Tampilkan lebih sedikit")
        }
    }

    private fun addAccordionData(title : String, isExpanded : Boolean = false) {
        productAccordionView.apply {
            childAccordionView?.let {
                val accordionUnifyData = AccordionDataUnify(
                    title = title,
                    expandableView = it,
                    isExpanded = isExpanded
                )
                accordionUnifyData.setContentPadding(8.toPx(), 0.toPx(), 8.toPx(), 16.toPx())
                addGroup(accordionUnifyData)
            }
        }
    }

    private fun renderButton(dataModel: EPharmacyAttachmentDataModel) {
        if(dataModel.uploadWidget){
            chatDokterUploadLayout.show()
            chatDokterUploadText.text = dataModel.uploadWidgetText
            chatDokterUploadIcon.loadImage(dataModel.uploadWidgetIcon)
            chatDokterUploadLayout.setOnClickListener {

            }
        }
    }

    private fun renderDivider(dataModel: EPharmacyAttachmentDataModel){
        divider.show()
    }
}
