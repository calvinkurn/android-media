package com.tokopedia.cartrevamp.view.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemSelectedAmountBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography

class CartSelectedAmountViewHolder(
    private val binding: ItemSelectedAmountBinding,
    val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_selected_amount
    }

    fun bind(data: CartSelectedAmountHolderData) {
        if (data.selectedAmount == Int.ZERO) {
            binding.root.gone()
            binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            binding.root.visible()
            binding.root.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        binding.textSelectedAmount.text = String.format(
            binding.root.resources.getString(R.string.cart_label_selected_amount),
            data.selectedAmount
        )
        binding.textActionDelete.setOnClickListener {
            listener?.onGlobalDeleteClicked()
        }
    }

    fun getTextActionDeleteView(): Typography {
        return binding.textActionDelete
    }
}
