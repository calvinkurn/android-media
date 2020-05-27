package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.uimodel.ProductUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

/**
 * Created by jegul on 27/05/20
 */
class ProductSelectableViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
    private val cbSelected: CheckboxUnify = itemView.findViewById(R.id.cb_selected)
    private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val tvProductAmount: TextView = itemView.findViewById(R.id.tv_product_amount)
    private val lblEmptyStock: Label = itemView.findViewById(R.id.lbl_empty_stock)

    init {
        lblEmptyStock.unlockFeature = true
        lblEmptyStock.setLabelType(
                "#${Integer.toHexString(MethodChecker.getColor(lblEmptyStock.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))}"
        )
    }

    fun bind(item: ProductUiModel) {
        cbSelected.isChecked = item.isSelected
        ivImage.loadImage(item.imageUrl)

        tvProductName.text = item.name
        tvProductAmount.text = getString(R.string.play_product_stock_amount, item.stock)

        if (item.hasStock) {
            lblEmptyStock.gone()
            cbSelected.isEnabled = true
        } else {
            lblEmptyStock.show()
            cbSelected.isEnabled = false
        }

        itemView.setOnClickListener {
            if (cbSelected.isEnabled)
                cbSelected.isChecked = !cbSelected.isChecked
        }
    }

    companion object {

        val LAYOUT = R.layout.item_product_selectable
    }
}