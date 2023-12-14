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
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
    private val initialProductQuantity = view.findViewById<Typography>(R.id.lblQuantityOrderInit)
    private val quantityChangedEditor = view.findViewById<QuantityEditorUnify>(R.id.quantity_change)
    private val productQuantityType = view.findViewById<Typography>(R.id.quantity_type)
    private val totalQuantity = view.findViewById<Typography>(R.id.lblSubtotalProductQuantity)
    private val totalAmount = view.findViewById<Typography>(R.id.lblFinalProductPrice)
    companion object {
        val LAYOUT = R.layout.epharmacy_accordion_product_view_item
    }

    private var dataModel: EPharmacyAccordionProductDataModel? = null

    override fun bind(element: EPharmacyAccordionProductDataModel?) {
        dataModel = element
        renderProductData(element?.product)
        initializeSums()
        renderQuantityChangeViews()
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
        if (dataModel?.product?.qtyComparison != null) {
            renderEditorLayout()
        } else {
            quantityEditorLayout?.hide()
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
                ePharmacyListener?.onEditorQuantityToast(
                    Toaster.TYPE_ERROR,
                    itemView.context.resources?.getString(epharmacyR.string.epharmacy_minimum_quantity_reached)
                        .orEmpty(),
                    dataModel?.enablerName,
                    dataModel?.tConsultationId,
                    dataModel?.groupId
                )
            }
            dataModel?.product?.qtyComparison?.currentQty = newValue
            val changeInTotal = reCalculateSubTotal()
            ePharmacyListener?.onQuantityChanged(
                changeInTotal,
                dataModel?.product?.productId.toString(),
                dataModel?.enablerName,
                dataModel?.tConsultationId,
                dataModel?.groupId
            )
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

    private fun renderQuantityChangeViews() {
        if (ePharmacyListener is EPharmacyQuantityChangeFragment) {
            productAmount.show()
            productAmount.text = EPharmacyUtils.getTotalAmountFmt(dataModel?.product?.price)
            productQuantityType.text = itemView.context.getString(R.string.epharmacy_barang)
            initialProductQuantity.text = java.lang.String.format(
                itemView.context.getString(epharmacyR.string.epharmacy_barang_quantity),
                dataModel?.product?.qtyComparison?.initialQty.toString()
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
}
