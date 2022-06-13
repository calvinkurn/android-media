package com.tokopedia.cart.view.viewholder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartSectionHeaderBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartSectionHeaderViewHolder(private val binding: ItemCartSectionHeaderBinding, val listener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_section_header
    }

    fun bind(element: CartSectionHeaderHolderData) {
        with(binding) {
            labelTitle.text = element.title
            if (TextUtils.isEmpty(element.showAllAppLink)) {
                labelShowAll.gone()
                icShowAll.gone()
            } else {
                labelShowAll.setOnClickListener {
                    listener?.onShowAllItem(element.showAllAppLink)
                }
                icShowAll.setOnClickListener {
                    listener?.onShowAllItem(element.showAllAppLink)
                }
                labelShowAll.visible()
                icShowAll.visible()
            }
        }
    }

}