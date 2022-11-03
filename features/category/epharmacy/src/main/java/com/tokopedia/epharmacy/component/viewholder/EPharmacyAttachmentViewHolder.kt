package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAttachmentProductAccordionAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class EPharmacyAttachmentViewHolder(private val view: View, private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyAttachmentDataModel?>(view) {

    private val productText = view.findViewById<Typography>(R.id.product_name)
    private val shopNameText = view.findViewById<Typography>(R.id.shop_name)
    private val shopIcon = view.findViewById<ImageUnify>(R.id.shop_icon)
    private val enablerImage = view.findViewById<ImageUnify>(R.id.enabler_image)
    private val partnerTitle = view.findViewById<Typography>(R.id.partner_title)
    private val productQuantity = view.findViewById<Typography>(R.id.product_quantity)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val divider = view.findViewById<DividerUnify>(R.id.divider)
    private val productAccordionView = view.findViewById<LinearLayout>(R.id.product_accordion_view)
    private val productAccordionRV = view.findViewById<RecyclerView>(R.id.accordion_expandable_rv)
    private val productAccordionChevron = view.findViewById<IconUnify>(R.id.iv_expand_other_product)
    private val chatDokterUploadLayout = view.findViewById<LinearLayout>(R.id.chat_dokter_upload_layout)
    private val chatDokterUploadText = view.findViewById<Typography>(R.id.upload_prescription_text)
    private val chatDokterUploadIcon = view.findViewById<ImageUnify>(R.id.upload_icon)

    companion object {
        val LAYOUT = R.layout.epharmacy_prescription_attachment_view_item
    }

    private var dataModel : EPharmacyAttachmentDataModel? = null

    override fun bind(element: EPharmacyAttachmentDataModel?) {
        dataModel = element
        renderGroupData()
    }

    private fun renderGroupData() {
        renderPartnerData()
        renderShopData()
        renderProductsData()
        renderButton()
        renderDivider()
    }

    private fun renderPartnerData() {
        enablerImage.show()
        partnerTitle.show()
        enablerImage.loadImage(dataModel?.partnerLogo)
    }

    private fun renderShopData() {
        shopNameText.displayTextOrHide(dataModel?.shopInfo?.shopName ?: "")
        shopIcon.show()
        shopIcon.loadImage(dataModel?.shopInfo?.shopLogoUrl)
    }

    private fun renderProductsData() {
        dataModel?.shopInfo?.products?.firstOrNull()?.let {  firstProduct ->
            productText.text = firstProduct.name
            productQuantity.text = "${firstProduct.quantity} Barang (${firstProduct.productTotalWeightFmt})"
            productImageUnify.loadImage(firstProduct.productImage)
        }

        if(!dataModel?.shopInfo?.products.isNullOrEmpty() && dataModel?.shopInfo?.products?.size ?: 0 > 1){
            productAccordionView.show()
            if(productAccordionRV.adapter == null){
                productAccordionRV.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                productAccordionRV.adapter = dataModel?.shopInfo?.products?.let { products ->
                    getAttachmentAccordionAdapter(
                        products
                    )
                }
                productAccordionView.setOnClickListener {
                    ePharmacyListener?.onInteractAccordion(bindingAdapterPosition,dataModel?.productsIsExpanded ?: false, dataModel?.name)
                }
            }else {
                dataModel?.shopInfo?.products?.let { products ->
                    (productAccordionRV.adapter as EPharmacyAttachmentProductAccordionAdapter).setData(
                        getProductsWithoutFirst(products)
                    )
                }
            }

            if(dataModel?.productsIsExpanded == true){
                productAccordionRV.show()
                productAccordionChevron.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
            }else{
                productAccordionChevron.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
                productAccordionRV.hide()
            }
        }else {
            productAccordionView.hide()
        }
    }

    private fun getAttachmentAccordionAdapter(products: ArrayList<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?>): EPharmacyAttachmentProductAccordionAdapter {
        return EPharmacyAttachmentProductAccordionAdapter(getProductsWithoutFirst(products))
    }

    private fun getProductsWithoutFirst(products: ArrayList<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?>): ArrayList<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?> {
        val productSubList = arrayListOf<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?>()
        products.forEachIndexed { index, product ->
            if(index != 0){ productSubList.add(product) }
        }
        return productSubList
    }

    private fun renderButton() {
        if(dataModel?.uploadWidget == true){
            chatDokterUploadLayout.show()
//            chatDokterUploadText.text = dataModel?.uploadWidgetText
//            chatDokterUploadIcon.loadImage(dataModel?.uploadWidgetIcon)
            chatDokterUploadLayout.setOnClickListener {
                ePharmacyListener?.onCTACClick(bindingAdapterPosition,dataModel?.name)
            }
        }else {
            chatDokterUploadLayout.hide()
        }
    }

    private fun renderDivider(){
        divider.show()
    }
}
