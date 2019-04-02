package com.tokopedia.expresscheckout.view.variant.viewholder

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.VariantChangeListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_SELECTED
import kotlinx.android.synthetic.main.item_checkout_variant_option.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class OptionVariantViewHolder(view: View?, val listener: VariantChangeListener) :
        RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_checkout_variant_option
    }

    fun bind(viewModel: OptionVariantViewModel?) {
        if (viewModel != null) {
            if (!viewModel.hasAvailableChild) viewModel.currentState = STATE_NOT_AVAILABLE
            if (viewModel.currentState != STATE_NOT_AVAILABLE) {
                itemView.setOnClickListener {
                    listener.onSelectedVariantChanged(viewModel)
                }
            }
            when {
                viewModel.currentState == STATE_SELECTED -> renderSelectedVariant(viewModel)
                viewModel.currentState == STATE_NOT_SELECTED -> {
                    renderNotSelectedVariant(viewModel)
                    itemView.ll_not_selected_variant_container.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.bg_variant_item_round_not_selected)
                }
                viewModel.currentState == STATE_NOT_AVAILABLE -> {
                    renderNotSelectedVariant(viewModel)
                    itemView.ll_not_selected_variant_container.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.bg_variant_item_round_disabled)
                }
            }
        }
    }

    private fun renderSelectedVariant(viewModel: OptionVariantViewModel) {
        itemView.ll_not_selected_variant_container.visibility = View.GONE
        itemView.cv_selected_variant_container.visibility = View.VISIBLE
        if (viewModel.variantHex.isNotBlank()) {
            try {
                val backgroundDrawable = getBackgroundDrawable(viewModel)
                itemView.v_variant_color_selected.background = backgroundDrawable
                itemView.v_variant_color_selected.visibility = View.VISIBLE
            } catch (e: IllegalArgumentException) {
                itemView.v_variant_color_selected.visibility = View.GONE
            }
        } else {
            itemView.v_variant_color_selected.visibility = View.GONE
        }
        itemView.tv_variant_value_selected.text = viewModel.variantName
    }

    private fun getBackgroundDrawable(viewModel: OptionVariantViewModel): Drawable? {
        if (viewModel.variantHex.equals("#ffffff") || viewModel.variantHex.equals("#fff")) {
            return ContextCompat.getDrawable(itemView.context, R.drawable.circle_color_variant_indicator_white)
        }
        val backgroundDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.circle_color_variant_indicator)
        backgroundDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor(viewModel.variantHex), PorterDuff.Mode.SRC_ATOP)
        return backgroundDrawable
    }

    private fun renderNotSelectedVariant(viewModel: OptionVariantViewModel) {
        itemView.cv_selected_variant_container.visibility = View.GONE
        itemView.ll_not_selected_variant_container.visibility = View.VISIBLE
        if (viewModel.variantHex.isNotBlank()) {
            try {
                val backgroundDrawable = getBackgroundDrawable(viewModel)
                itemView.v_variant_color_not_selected.background = backgroundDrawable
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