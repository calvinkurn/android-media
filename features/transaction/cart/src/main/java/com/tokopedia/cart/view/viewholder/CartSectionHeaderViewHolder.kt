package com.tokopedia.cart.view.viewholder

import android.text.TextUtils
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartSectionHeaderBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartSectionHeaderActionType
import com.tokopedia.cart.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartSectionHeaderViewHolder(private val binding: ItemCartSectionHeaderBinding, val listener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_section_header
    }

    fun bind(element: CartSectionHeaderHolderData) {
        with(binding) {
            labelTitle.text = Utils.getHtmlFormat(element.title)
            if (TextUtils.isEmpty(element.showAllAppLink)) {
                labelShowAll.gone()
                icShowAll.gone()
                flAction.gone()
            } else {
                when (element.type) {
                    CartSectionHeaderActionType.TEXT_BUTTON -> {
                        labelShowAll.setOnClickListener {
                            listener?.onShowAllItem(element.showAllAppLink)
                        }
                        icShowAll.setOnClickListener {
                            listener?.onShowAllItem(element.showAllAppLink)
                        }
                        labelShowAll.visible()
                        icShowAll.visible()
                        flAction.gone()
                        labelTitle.setType(Typography.DISPLAY_2)
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(clContainer)
                        constraintSet.connect(
                            R.id.label_title,
                            ConstraintSet.END,
                            R.id.label_show_all,
                            ConstraintSet.START
                        )
                        constraintSet.applyTo(clContainer)
                    }
                    CartSectionHeaderActionType.ICON_BUTTON -> {
                        flAction.setOnClickListener {
                            listener?.onShowAllItem(element.showAllAppLink)
                        }
                        labelShowAll.gone()
                        icShowAll.gone()
                        flAction.visible()
                        labelTitle.setType(Typography.HEADING_2)
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(clContainer)
                        constraintSet.connect(
                            R.id.label_title,
                            ConstraintSet.END,
                            R.id.fl_action,
                            ConstraintSet.START
                        )
                        constraintSet.applyTo(clContainer)
                    }
                }
            }
        }
    }
}
