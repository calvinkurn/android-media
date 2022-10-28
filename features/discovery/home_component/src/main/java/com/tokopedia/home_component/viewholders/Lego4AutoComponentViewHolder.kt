package com.tokopedia.home_component.viewholders

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.loadImageNormal
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.Lego4AutoItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography

class Lego4AutoComponentViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val itemLayout: ConstraintLayout = v.findViewById(R.id.item_lego_auto)
    private val itemImage: ImageView = v.findViewById(R.id.item_image)
    private val itemName: Typography = v.findViewById(R.id.item_name)
    private val itemDesc: Typography = v.findViewById(R.id.item_desc)
    private val template = "%s %s"


    fun bind(item: Lego4AutoItem, parentPosition: Int, listener: Lego4AutoBannerListener?, channelModel: ChannelModel, isCacheData: Boolean) {
        itemName.text = item.grid.name
        itemImage.loadImageNormal(item.grid.imageUrl)
        itemDesc.text = constructBoldFont(item.grid.benefit.type, item.grid.benefit.value)
        if (item.grid.textColor.isNotEmpty()) {
            itemName.setTextColor(Color.parseColor(item.grid.textColor))
            itemDesc.setTextColor(Color.parseColor(item.grid.textColor))
        }
        if (item.grid.backColor.isNotEmpty()) {
            itemLayout.setGradientBackground(arrayListOf(item.grid.backColor))
        }
        itemLayout.addOnImpressionListener(item.impressHolder){
            if (!isCacheData) {
                listener?.onLegoItemImpressed(channelModel, item.grid, adapterPosition, parentPosition)
            }
        }
        itemLayout.setOnClickListener {
            listener?.onLegoItemClicked(channelModel, item.grid, adapterPosition, parentPosition)
        }
    }

    private fun constructBoldFont(type: String, value : String): SpannableStringBuilder {
        val text = SpannableStringBuilder(String.format(template, type, value))
        val startIndexBold = text.indexOf(value)
        val endIndexBold = startIndexBold + value.length
        val spanStyle = StyleSpan(Typeface.BOLD)
        text.setSpan(spanStyle, startIndexBold, endIndexBold, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        return text
    }
}