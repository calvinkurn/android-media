package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel
import kotlinx.android.synthetic.main.item_army.view.*


class ArmyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_army
    }

    fun bindData(data: LogisticPromoViewModel, listener: ShippingDurationAdapterListener) {
        itemView.tv_title.text = data.title
        itemView.tv_info.text = MethodChecker.fromHtml(data.description)
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