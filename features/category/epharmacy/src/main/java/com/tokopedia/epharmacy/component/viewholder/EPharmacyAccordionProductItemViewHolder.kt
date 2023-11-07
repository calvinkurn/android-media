package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.viewholder.EPharmacyAttachmentViewHolder.Companion.MIN_VALUE_OF_PRODUCT_EDITOR
import com.tokopedia.epharmacy.ui.fragment.EPharmacyQuantityChangeFragment
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.epharmacy.R as epharmacyR

class EPharmacyAccordionProductItemViewHolder(val view: View, private val ePharmacyListener: EPharmacyListener?) : AbstractViewHolder<EPharmacyAccordionProductDataModel>(view) {

    private val productText = view.findViewById<Typography>(R.id.lbl_PAP_productName)
    private val productQuantity = view.findViewById<Typography>(R.id.lbl_PAP_productWeight)
    private val productAmount = view.findViewById<Typography>(R.id.lbl_PAP_productAmount)
    private val productImageUnify = view.findViewById<ImageView>(R.id.product_image)
    private val quantityEditorLayout = view.findViewById<ConstraintLayout>(R.id.quantity_editor_layout)
    private val initialProductQuantity = view.findViewById<Typography>(R.id.initial_product_quantity)
    private val quantityChangedEditor = view.findViewById<QuantityEditorUnify>(R.id.quantity_change)
    private val productQuantityType = view.findViewById<Typography>(R.id.quantity_type)
    private val totalQuantity = view.findViewById<Typography>(R.id.total_quantity)
    private val totalAmount = view.findViewById<Typography>(R.id.qc_total_amount)
    companion object {
        val LAYOUT = R.layout.epharmacy_accordion_product_view_item
    }

    private var dataModel: EPharmacyAccordionProductDataModel? = null

    override fun bind(element: EPharmacyAccordionProductDataModel?) {
        dataModel = element
        renderProductData(element?.product)
        renderQuantityChangedLayout()
    }

    private fun renderProductData(product: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?) {
        productText.text = product?.name.orEmpty()
        productQuantity.text = java.lang.String.format(
            itemView.context.getString(R.string.epharmacy_quantity_weight_text),
            product?.quantity.orEmpty(),
            product?.productTotalWeightFmt.orEmpty()
        )
        productImageUnify.loadImage(product?.productImage)
    }

    private fun renderQuantityChangedLayout() {
        if(dataModel?.product?.qtyComparison != null){
            productAmount.text = EPharmacyUtils.getTotalAmountFmt(dataModel?.product?.price)
            if(dataModel?.product?.qtyComparison?.currentQty?.orZero().isZero()){
                dataModel?.product?.qtyComparison?.currentQty = dataModel?.product?.qtyComparison?.recommendedQty.orZero()
            }

            quantityEditorLayout?.show()
            initialProductQuantity.text = java.lang.String.format(
                itemView.context.getString(epharmacyR.string.epharmacy_barang_quantity),
                dataModel?.product?.qtyComparison?.initialQty.toString()
            )
            productQuantityType.text = itemView.context.getString(R.string.epharmacy_barang)
            quantityChangedEditor.maxValue = dataModel?.product?.qtyComparison?.recommendedQty.orZero()
            quantityChangedEditor.minValue = MIN_VALUE_OF_PRODUCT_EDITOR

            quantityChangedEditor.setValueChangedListener { newValue, _, _ ->
                if(newValue == MIN_VALUE_OF_PRODUCT_EDITOR){
                    ePharmacyListener?.onToast(Toaster.TYPE_ERROR,
                        itemView.context.resources?.getString(epharmacyR.string.epharmacy_minimum_quantity_reached) ?: "")
                    quantityChangedEditor.subtractButton.isEnabled = false
                }
                dataModel?.product?.qtyComparison?.currentQty = newValue
                reCalculateSubTotal()
                ePharmacyListener?.onQuantityChanged()
            }
            quantityChangedEditor.setValue(dataModel?.product?.qtyComparison?.currentQty.orZero())
            reCalculateSubTotal()
        }else {
            quantityEditorLayout?.hide()
        }
    }

    private fun reCalculateSubTotal() {
        val subtotal = EPharmacyUtils.getTotalAmount(dataModel?.product?.qtyComparison?.currentQty, dataModel?.product?.qtyComparison?.productPrice)
        dataModel?.product?.qtyComparison?.subTotal = subtotal
        totalAmount.displayTextOrHide(EPharmacyUtils.getTotalAmountFmt(subtotal))
        totalQuantity.displayTextOrHide(java.lang.String.format(
            itemView.context.getString(epharmacyR.string.epharmacy_subtotal_quantity_change),
            dataModel?.product?.qtyComparison?.currentQty.toString()
        ))
    }
}
