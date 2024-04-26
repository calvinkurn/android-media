package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.databinding.ItemNotifierBinding
import com.tokopedia.logisticcart.shipping.model.ShipmentTickerModel
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

class ShipmentTickerViewHolder(private val binding: ItemNotifierBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val LAYOUT = 7
        private const val HTML_HYPERLINK_BOLD_FORMAT = "<a href=\"%s\"><b>%s</b></a>"
    }

    fun bindData(data: List<ShipmentTickerModel>) {
        val message = data.map {
            val content = StringBuilder().apply {
                append(it.description)
                it.takeIf { it.actionLabel.isNotEmpty() }?.let { action ->
                    append(" ")
                    appendHyperlinkText(it.actionLabel, it.actionType?.url.orEmpty())
                }
            }.toString()
            TickerData(
                it.title,
                content,
                it.type,
                true,
                it.actionType?.url.orEmpty()
            )
        }
        binding.run {
            val tickerPageAdapter = TickerPagerAdapter(root.context, message)
            tickerPageAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    val link = linkUrl.toString()
                    // todo
//                    if (link.startsWith("tokopedia")) {
//                        onClickApplink(link)
//                    } else {
//                        onClickUrl(link)
//                    }
                }
            })
            tickerNotifier.addPagerView(tickerPageAdapter, message)
        }
    }

    private fun StringBuilder.appendHyperlinkText(label: String, url: String) {
        if (label.isNotBlank() && url.isNotBlank()) {
            append(String.format(HTML_HYPERLINK_BOLD_FORMAT, url, label))
        }
    }
}
