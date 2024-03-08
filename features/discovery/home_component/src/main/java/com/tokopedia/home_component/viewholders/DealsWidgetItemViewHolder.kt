package com.tokopedia.home_component.viewholders

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.databinding.LayoutDealsWidgetItemBinding
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelStyleUtil
import com.tokopedia.home_component.util.loadImageNormal
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created by frenzel
 */
class DealsWidgetItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_widget_item
        private const val IMAGE_CORNER = 0
        private const val RATIO_FULL = "1:1"
    }

    private val binding: LayoutDealsWidgetItemBinding? by viewBinding()
    private val template = "%s %s"

    fun bind(item: ChannelGrid, parentPosition: Int, listener: VpsWidgetListener?, channelModel: ChannelModel, isCacheData: Boolean) {
        renderCard()
        item.imageUrl.renderImage()
        item.label.renderRibbon()
        binding?.textName?.text = item.name
        binding?.textDesc?.text = constructBoldFont(item.benefit.type, item.benefit.value)
        itemView.addOnImpressionListener(item) {
            if (!isCacheData) {
                listener?.onItemImpressed(channelModel, item, item.position, parentPosition)
            }
        }
        binding?.cardDeals?.setOnClickListener {
            listener?.onItemClicked(channelModel, item, item.position, parentPosition)
        }
    }

    private fun renderCard() {
        binding?.cardDeals?.apply {
            cardType = CardUnify2.TYPE_SHADOW
            animateOnPress = CardUnify2.ANIMATE_NONE
        }
    }

    private fun String.renderImage() {
        val layoutParams = (binding?.image?.layoutParams as ConstraintLayout.LayoutParams)
        layoutParams.dimensionRatio = RATIO_FULL
        binding?.image?.scaleType = ImageView.ScaleType.CENTER_CROP
        binding?.image?.layoutParams = layoutParams
        binding?.image?.apply {
            loadImageNormal(this@renderImage)
            cornerRadius = IMAGE_CORNER
        }
    }

    private fun String.renderRibbon() {
        binding?.dealsRibbon?.render(this)
    }

    private fun constructBoldFont(type: String, value: String): SpannableStringBuilder {
        val text = SpannableStringBuilder(String.format(template, type, value))
        val startIndexBold = text.indexOf(value)
        val endIndexBold = startIndexBold + value.length
        val spanStyle = StyleSpan(Typeface.BOLD)
        text.setSpan(spanStyle, startIndexBold, endIndexBold, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        return text
    }
}
