package com.tokopedia.expresscheckout.view.variant.viewholder

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.VariantChangeListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantOptionVariantViewModel
import kotlinx.android.synthetic.main.item_checkout_variant_option.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantOptionVariantViewHolder( view: View?, val listener: VariantChangeListener) :
        RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_checkout_variant_option
    }

    fun bind(viewModel: CheckoutVariantOptionVariantViewModel?) {
        if (viewModel != null) {
            if (viewModel.currentState != viewModel.STATE_NOT_AVAILABLE) {
                itemView.setOnClickListener {
                    listener.onSelectedVariantChanged(viewModel)
                }
            }
            when {
                viewModel.currentState == viewModel.STATE_SELECTED -> renderSelectedVariant(viewModel)
                viewModel.currentState == viewModel.STATE_NOT_SELECTED -> {
                    renderNotSelectedVariant(viewModel)
                    itemView.ll_not_selected_variant_container.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.bg_variant_item_round_not_selected)
                }
                viewModel.currentState == viewModel.STATE_NOT_AVAILABLE -> {
                    renderNotSelectedVariant(viewModel)
                    itemView.ll_not_selected_variant_container.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.bg_variant_item_round_disabled)
                }
            }
        }
    }

    private fun renderSelectedVariant(viewModel: CheckoutVariantOptionVariantViewModel) {
        itemView.ll_not_selected_variant_container.visibility = View.GONE
        itemView.cv_selected_variant_container.visibility = View.VISIBLE
        if (viewModel.variantHex.isNotBlank()) {
            try {
                itemView.v_variant_color_selected.setBackgroundColor(Color.parseColor(viewModel.variantHex))
                itemView.v_variant_color_selected.visibility = View.VISIBLE
            } catch (e: IllegalArgumentException) {
                itemView.v_variant_color_selected.visibility = View.GONE
            }
        } else {
            itemView.v_variant_color_selected.visibility = View.GONE
        }
        itemView.tv_variant_value_selected.text = viewModel.variantName
    }

    private fun renderNotSelectedVariant(viewModel: CheckoutVariantOptionVariantViewModel) {
        itemView.cv_selected_variant_container.visibility = View.GONE
        itemView.ll_not_selected_variant_container.visibility = View.VISIBLE
        if (viewModel.variantHex.isNotBlank()) {
            try {
                itemView.v_variant_color_not_selected.setBackgroundColor(Color.parseColor(viewModel.variantHex))
                itemView.v_variant_color_not_selected.visibility = View.VISIBLE
            } catch (e: IllegalArgumentException) {
                itemView.v_variant_color_not_selected.visibility = View.GONE
            }
        } else {
            itemView.v_variant_color_not_selected.visibility = View.GONE
        }
        itemView.tv_variant_value_not_selected.text = viewModel.variantName
    }

}