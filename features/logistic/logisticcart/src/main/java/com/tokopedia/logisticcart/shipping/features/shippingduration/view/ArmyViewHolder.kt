package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import kotlinx.android.synthetic.main.item_army.view.*


class ArmyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_army
        const val ESTIMASI_TIDAK_TERSEDIA = "Estimasi tidak tersedia"
    }

    fun bindData(data: LogisticPromoUiModel, listener: ShippingDurationAdapterListener) {
        itemView.tv_title.text = data.title
        itemView.tv_info.text = MethodChecker.fromHtml(data.description)

        if (data.etaData.errorCode == 0 && data.etaData.textEta.isNotEmpty()) {
            itemView.tv_eta.visibility = View.VISIBLE
            itemView.tv_eta.text = data.etaData.textEta
        } else if (data.etaData.errorCode == 0 && data.etaData.textEta.isEmpty()) {
            itemView.tv_eta.visibility = View.VISIBLE
            itemView.tv_eta.text = ESTIMASI_TIDAK_TERSEDIA
        } else {
            itemView.tv_eta.visibility = View.GONE
        }

        if (data.codData.isCodAvailable == 1) {
            itemView.lbl_cod_available_eta.apply {
                visibility = View.VISIBLE
                text = data.codData.codText
            }
        } else {
            itemView.lbl_cod_available_eta.visibility = View.GONE
        }

        if (data.description.isEmpty()) itemView.tv_info.visibility = View.GONE
        ImageHandler.LoadImage(itemView.img_logo, data.imageUrl)

        val fontColor = if (data.disabled) {
            ContextCompat.getColor(itemView.context, R.color.font_disabled)
        } else {
            ContextCompat.getColor(itemView.context, R.color.font_black_primary_70)
        }

        itemView.tv_title.setTextColor(fontColor)
        itemView.tv_info.setTextColor(fontColor)

        if (!data.disabled) {
            itemView.setOnClickListener {
                listener.onLogisticPromoClicked(data)
            }
            itemView.fl_image_container.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_enabled_item)
        } else {
            itemView.fl_image_container.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_disabled_item)
            itemView.setOnClickListener(null)
        }

        if (data.isApplied) {
            itemView.img_check.visibility = View.VISIBLE
        } else {
            itemView.img_check.visibility = View.GONE
        }
    }

}