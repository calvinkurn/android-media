package com.tokopedia.home_component.viewholders

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.databinding.LayoutVpsWidgetItemBinding
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.loadImageNormal
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

class VpsWidgetComponentViewHolder(v: View): RecyclerView.ViewHolder(v) {
    companion object {
        private const val IMAGE_CORNER = 0
    }

    private val binding: LayoutVpsWidgetItemBinding? by viewBinding()
    private val template = "%s %s"

    fun bind(item: ChannelGrid, parentPosition: Int, listener: VpsWidgetListener?, channelModel: ChannelModel, isCacheData: Boolean) {
        binding?.cardVps?.apply {
            cardType = CardUnify2.TYPE_BORDER
            animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE
        }
        binding?.textName?.text = item.name
        binding?.image?.apply {
            loadImageNormal(item.imageUrl)
            cornerRadius = IMAGE_CORNER
        }
        binding?.textDesc?.text = constructBoldFont(item.benefit.type, item.benefit.value)
        itemView.addOnImpressionListener(item){
            if (!isCacheData) {
                listener?.onItemImpressed(channelModel, item, adapterPosition, parentPosition)
            }
        }
        binding?.cardVps?.setOnClickListener {
            listener?.onItemClicked(channelModel, item, adapterPosition, parentPosition)
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