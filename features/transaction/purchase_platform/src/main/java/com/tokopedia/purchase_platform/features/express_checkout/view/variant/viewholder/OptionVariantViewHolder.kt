package com.tokopedia.purchase_platform.features.express_checkout.view.variant.viewholder

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.VariantChangeListener
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel.Companion.STATE_SELECTED
import kotlinx.android.synthetic.main.item_checkout_variant_option.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class OptionVariantViewHolder(view: View, val listener: VariantChangeListener) :
        RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_checkout_variant_option
    }

    fun bind(uiModel: OptionVariantUiModel?) {
        if (uiModel != null) {
            if (!uiModel.hasAvailableChild) uiModel.currentState = STATE_NOT_AVAILABLE
            if (uiModel.currentState != STATE_NOT_AVAILABLE) {
                itemView.setOnClickListener {
                    listener.onSelectedVariantChanged(uiModel)
                }
            }
            when {
                uiModel.currentState == STATE_SELECTED -> renderSelectedVariant(uiModel)
                uiModel.currentState == STATE_NOT_SELECTED -> {
                    renderNotSelectedVariant(uiModel)
                    itemView.ll_not_selected_variant_container.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.bg_variant_item_round_not_selected)
                }
                uiModel.currentState == STATE_NOT_AVAILABLE -> {
                    renderNotSelectedVariant(uiModel)
                    itemView.ll_not_selected_variant_container.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.bg_variant_item_round_disabled)
                }
            }
        }
    }

    private fun renderSelectedVariant(uiModel: OptionVariantUiModel) {
        itemView.ll_not_selected_variant_container.visibility = View.GONE
        itemView.cv_selected_variant_container.visibility = View.VISIBLE
        if (uiModel.variantHex.isNotBlank()) {
            try {
                val backgroundDrawable = getBackgroundDrawable(uiModel)
                itemView.v_variant_color_selected.background = backgroundDrawable
                itemView.v_variant_color_selected.visibility = View.VISIBLE
            } catch (e: IllegalArgumentException) {
                itemView.v_variant_color_selected.visibility = View.GONE
            }
        } else {
            itemView.v_variant_color_selected.visibility = View.GONE
        }
        itemView.tv_variant_value_selected.text = uiModel.variantName
    }

    private fun getBackgroundDrawable(uiModel: OptionVariantUiModel): Drawable? {
        if (uiModel.variantHex.equals("#ffffff") || uiModel.variantHex.equals("#fff")) {
            return ContextCompat.getDrawable(itemView.context, R.drawable.circle_color_variant_indicator_white)
        }
        val backgroundDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.circle_color_variant_indicator)
        backgroundDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor(uiModel.variantHex), PorterDuff.Mode.SRC_ATOP)
        return backgroundDrawable
    }

    private fun renderNotSelectedVariant(uiModel: OptionVariantUiModel) {
        itemView.cv_selected_variant_container.visibility = View.GONE
        itemView.ll_not_selected_variant_container.visibility = View.VISIBLE
        if (uiModel.variantHex.isNotBlank()) {
            try {
                val backgroundDrawable = getBackgroundDrawable(uiModel)
                itemView.v_variant_color_not_selected.background = backgroundDrawable
                itemView.v_variant_color_not_selected.visibility = View.VISIBLE
            } catch (e: IllegalArgumentException) {
                itemView.v_variant_color_not_selected.visibility = View.GONE
            }
        } else {
            itemView.v_variant_color_not_selected.visibility = View.GONE
        }
        itemView.tv_variant_value_not_selected.text = uiModel.variantName
    }

}