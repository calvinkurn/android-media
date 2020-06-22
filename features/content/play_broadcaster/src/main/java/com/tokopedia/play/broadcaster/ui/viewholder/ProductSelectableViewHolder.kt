package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.compatTransitionName
import com.tokopedia.play.broadcaster.view.state.NotSelectable
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

/**
 * Created by jegul on 27/05/20
 */
class ProductSelectableViewHolder(
        itemView: View,
        private val listener: Listener? = null,
        showSelection: Boolean = true
) : BaseViewHolder(itemView) {

    private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
    private val cbSelected: CheckboxUnify = itemView.findViewById(R.id.cb_selected)
    private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val tvProductAmount: TextView = itemView.findViewById(R.id.tv_product_amount)
    private val lblEmptyStock: Label = itemView.findViewById(R.id.lbl_empty_stock)

    private var onCheckedChangeListener: (CompoundButton, Boolean) -> Unit = { _ , _ -> }

    init {
        if (showSelection) cbSelected.show()
        else cbSelected.gone()

        lblEmptyStock.unlockFeature = true
        lblEmptyStock.setLabelType(
                "#${Integer.toHexString(MethodChecker.getColor(lblEmptyStock.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))}"
        )
    }

    fun bind(item: ProductContentUiModel) {
        cbSelected.forceSetCheckbox(item.isSelectedHandler(item.id))
        ivImage.loadImage(item.imageUrl)
        ivImage.compatTransitionName = item.transitionName

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
            if (cbSelected.isEnabled) {
                val isSelecting = !cbSelected.isChecked
                when (val state = item.isSelectable(isSelecting)) {
                    Selectable -> triggerCheckbox()
                    is NotSelectable -> listener?.onProductSelectError(state.reason)
                }
            }
        }

        onCheckedChangeListener = { _, isChecked ->
            listener?.onProductSelectStateChanged(item.id, isChecked)
        }
        cbSelected.setOnCheckedChangeListener(onCheckedChangeListener)
    }

    private fun triggerCheckbox() {
        cbSelected.isChecked = !cbSelected.isChecked
    }

    private fun CheckBox.forceSetCheckbox(isChecked: Boolean) {
        setOnCheckedChangeListener(null)
        cbSelected.isChecked = isChecked
        setOnCheckedChangeListener(onCheckedChangeListener)
    }

    companion object {

        val LAYOUT = R.layout.item_play_product_selectable
    }

    interface Listener {

        fun onProductSelectStateChanged(productId: Long, isSelected: Boolean)
        fun onProductSelectError(reason: Throwable)
    }
}