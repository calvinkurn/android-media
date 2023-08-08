package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo as EProductInfo

class EPharmacyAccordionProductItemViewHolder(val view: View, private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyAccordionProductDataModel>(view) {

    private val productText = view.findViewById<Typography>(R.id.lbl_PAP_productName)
    private val productQuantity = view.findViewById<Typography>(R.id.lbl_PAP_productWeight)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val quantityChangedLayout = view.findViewById<ConstraintLayout>(R.id.quantity_change_layout)
    private val medicalProductQuantity = view.findViewById<Typography>(R.id.initial_product_quantity)
    private val quantityChangedEditor = view.findViewById<QuantityEditorUnify>(R.id.quantity_change)
    private val productQuantityType = view.findViewById<Typography>(R.id.quantity_type)
    private val totalQuantity = view.findViewById<Typography>(R.id.total_quantity)
    private val totalAmount = view.findViewById<Typography>(R.id.total_amount)
    companion object {
        val LAYOUT = R.layout.epharmacy_accordion_product_view_item
    }

    private var dataModel: EPharmacyAccordionProductDataModel? = null

    override fun bind(element: EPharmacyAccordionProductDataModel?) {
        dataModel = element
        renderProductData(element?.product)
        //renderQuantityChangedLayout()
    }

    private fun renderProductData(product: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?) {
        productText.text = product?.name ?: ""
        productQuantity.text = java.lang.String.format(
            itemView.context.getString(R.string.epharmacy_quantity_weight_text),
            product?.quantity ?: "",
            product?.productTotalWeightFmt ?: ""
        )
        productImageUnify.loadImage(product?.productImage)
    }

    private fun renderQuantityChangedLayout() {
        if(dataModel?.product?.qtyComparison != null){
            quantityChangedLayout?.show()
            medicalProductQuantity.text = "${dataModel?.product?.qtyComparison?.initialQty.toString()} barang"
            productQuantityType.text = "barang"
            if(dataModel?.product?.qtyComparison?.currentQty != quantityChangedEditor.getValue() && quantityChangedEditor.getValue() != 1)
                quantityChangedEditor.setValue(dataModel?.product?.qtyComparison?.recommendedQty ?: 0)
            if(quantityChangedEditor.getValue() >= (dataModel?.product?.qtyComparison?.recommendedQty ?: 0)){
                quantityChangedEditor.addButton.isEnabled = false
            }
            totalAmount.displayTextOrHide(getTotalAmount(dataModel?.product))
            totalQuantity.displayTextOrHide("Subtotal (${dataModel?.product?.qtyComparison?.currentQty.toString()} barang)")
            quantityChangedEditor.setValueChangedListener { newValue, oldValue, isOver ->
                if(newValue == 1){
                    ePharmacyListener?.onToast(Toaster.TYPE_ERROR,"Jumlah barang tidak boleh melebihi yang sudah diresepkan atau kurang dari 1.")
                    quantityChangedEditor.subtractButton.isEnabled = false
                } else {
                    quantityChangedEditor.subtractButton.isEnabled = true
                    quantityChangedEditor.addButton.isEnabled = true
                    dataModel?.product?.qtyComparison?.currentQty = newValue
                }
                renderQuantityChangedLayout()
            }
        }else {
            quantityChangedLayout?.hide()
        }
    }

    // TODO Format
    private fun getTotalAmount(product: EProductInfo.Product?): String {
        return "Rp${(product?.qtyComparison?.currentQty?.toDouble() ?: 0.0) * ((dataModel?.product?.qtyComparison?.productPrice?: 0.0))}"
    }
}
