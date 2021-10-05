package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.databinding.UohListItemBinding
import com.tokopedia.unifyorderhistory.util.UohConsts.TICKER_LABEL
import com.tokopedia.unifyorderhistory.util.UohConsts.TICKER_URL

/**
 * Created by fwidjaja on 25/07/20.
 */
class UohOrderListViewHolder(private val binding: UohListItemBinding, private val actionListener: UohItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohListOrder.Data.UohOrders.Order) {
            binding.clDataProduct.visible()
            ImageHandler.loadImage(itemView.context, binding.icUohVertical, item.dataObject.metadata.verticalLogo, null)
            binding.tvUohCategories.text = item.dataObject.metadata.verticalLabel
            binding.tvUohDate.text = item.dataObject.metadata.paymentDateStr

            val textColorLabel = item.dataObject.metadata.status.textColor
            val bgColorLabel = item.dataObject.metadata.status.bgColor

            binding.labelUohOrder.text = item.dataObject.metadata.status.label
            if (textColorLabel.isNotEmpty() && bgColorLabel.isNotEmpty()) {
                binding.labelUohOrder.run {
                    unlockFeature = true
                    fontColorByPass = textColorLabel
                    setLabelType(bgColorLabel)
                }
            }

            if (item.dataObject.metadata.tickers.isNotEmpty()) {
               binding.tickerInfoInsideCard.visible()
                if (item.dataObject.metadata.tickers.size > 1) {
                    val listTickerData = arrayListOf<TickerData>()
                    item.dataObject.metadata.tickers.forEach {
                        var desc = it.text
                        if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                            desc += " ${itemView.context.getString(R.string.uoh_ticker_info_selengkapnya)
                                    .replace(TICKER_URL, it.action.appUrl)
                                    .replace(TICKER_LABEL, it.action.label)}"
                        }
                        listTickerData.add(TickerData(it.title, desc, UohUtils.getTickerType(it.text), true))
                    }
                    itemView.context?.let {
                        val adapter = TickerPagerAdapter(it, listTickerData)
                        adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                                actionListener?.onTickerDetailInfoClicked(linkUrl.toString())
                            }
                        })
                        binding.tickerInfoInsideCard.run {
                            setDescriptionClickEvent(object: TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                                override fun onDismiss() {
                                }
                            })
                            addPagerView(adapter, listTickerData)
                        }
                    }
                } else {
                    item.dataObject.metadata.tickers.first().let { ticker ->
                        binding.tickerInfoInsideCard.tickerTitle = ticker.title
                        var desc = ticker.text
                        if (ticker.action.appUrl.isNotEmpty() && ticker.action.label.isNotEmpty()) {
                            desc += " ${itemView.context.getString(R.string.uoh_ticker_info_selengkapnya)
                                    .replace(TICKER_URL, ticker.action.appUrl)
                                    .replace(TICKER_LABEL, ticker.action.label)}"
                        }
                        binding.tickerInfoInsideCard.run {
                            setHtmlDescription(desc)
                            tickerType = UohUtils.getTickerType(ticker.type)
                            setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    actionListener?.onTickerDetailInfoClicked(linkUrl.toString()) }

                                override fun onDismiss() {}
                            })
                        }
                    }
                }
            } else {
                binding.tickerInfoInsideCard.gone()
            }

            if (item.dataObject.metadata.products.isNotEmpty()) {
                binding.tvUohProductName.text = item.dataObject.metadata.products.first().title
                binding.tvUohProductDesc.text = item.dataObject.metadata.products.first().inline1.label
                if (item.dataObject.metadata.products.first().imageURL.isNotEmpty()) {
                    binding.ivUohProduct.visible()
                    ImageHandler.loadImageRounded2(itemView.context, binding.ivUohProduct, item.dataObject.metadata.products.first().imageURL, 6f.toPx())
                } else {
                    binding.ivUohProduct.gone()
                }
            }

            if (item.dataObject.metadata.dotMenus.isNotEmpty()) {
                binding.ivKebabMenu.setOnClickListener {
                    actionListener?.onKebabMenuClicked(item.dataObject, position)
                }
            }

            if (item.dataObject.metadata.otherInfo.label.isNotEmpty()) {
                binding.labelOtherInfo.run {
                    visible()
                    text = item.dataObject.metadata.otherInfo.label
                }
            } else {
                binding.labelOtherInfo.gone()
            }

            binding.tvUohTotalBelanja.text = item.dataObject.metadata.totalPrice.label
            binding.tvUohTotalBelanjaValue.text = item.dataObject.metadata.totalPrice.value
            if (item.dataObject.metadata.buttons.isNotEmpty()) {
                binding.uohBtnAction.run {
                    visible()
                    text = item.dataObject.metadata.buttons[0].label
                    buttonType = UohUtils.getButtonType(item.dataObject.metadata.buttons[0].variantColor)
                    buttonVariant = UohUtils.getButtonVariant(item.dataObject.metadata.buttons[0].type)
                }
            } else {
                binding.uohBtnAction.gone()
            }

            binding.clDataProduct.setOnClickListener {
                actionListener?.onListItemClicked(item.dataObject, position)
            }

            binding.uohBtnAction.setOnClickListener {
                if (item.dataObject.metadata.buttons.isNotEmpty()) {
                    actionListener?.onActionButtonClicked(item.dataObject, position)
                }
            }

            actionListener?.trackViewOrderCard(item.dataObject, position)
        }
    }
}