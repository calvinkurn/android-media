package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.analytics.UohAnalytics
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohListItemBinding
import com.tokopedia.unifyorderhistory.util.UohConsts.BELI_LAGI_LABEL
import com.tokopedia.unifyorderhistory.util.UohConsts.TICKER_LABEL
import com.tokopedia.unifyorderhistory.util.UohConsts.TICKER_URL
import com.tokopedia.unifyorderhistory.util.UohConsts.ULAS_LABEL
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter
import com.tokopedia.unifyorderhistory.view.widget.review_rating.UohReviewRatingWidget
import com.tokopedia.unifyorderhistory.view.widget.review_rating.UohReviewRatingWidgetConfig

/**
 * Created by fwidjaja on 25/07/20.
 */
class UohOrderListViewHolder(
    private val binding: UohListItemBinding,
    private val actionListener: UohItemAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is UohListOrder.UohOrders.Order) {
            binding.clDataProduct.visible()
            ImageHandler.loadImage(
                itemView.context,
                binding.icUohVertical,
                item.dataObject.metadata.verticalLogo,
                null
            )
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
                            setDescriptionClickEvent(object : TickerCallback {
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
                                    actionListener?.onTickerDetailInfoClicked(linkUrl.toString())
                                }

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
                    binding.ivUohProduct.loadProductImage(
                        url = item.dataObject.metadata.products.firstOrNull()?.imageURL.orEmpty(),
                        archivedUrl = TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_SMALL,
                        cornerRadius = 6f.toPx()
                    )
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
                binding.uohBtnAction1.run {
                    visible()
                    text = item.dataObject.metadata.buttons[0].label
                    buttonType =
                        UohUtils.getButtonType(item.dataObject.metadata.buttons[0].variantColor)
                    buttonVariant =
                        UohUtils.getButtonVariant(item.dataObject.metadata.buttons[0].type)
                }
                if (item.dataObject.metadata.buttons[0].label == BELI_LAGI_LABEL) {
                    UohAnalytics.sendViewBeliLagiButtonEvent()
                }
            } else {
                binding.uohBtnAction1.gone()
            }

            if (item.dataObject.metadata.buttons.size > 1) {
                binding.uohBtnAction2.run {
                    visible()
                    text = item.dataObject.metadata.buttons[1].label
                    buttonType =
                        UohUtils.getButtonType(item.dataObject.metadata.buttons[1].variantColor)
                    buttonVariant =
                        UohUtils.getButtonVariant(item.dataObject.metadata.buttons[1].type)
                }
                if (item.dataObject.metadata.buttons[1].label == ULAS_LABEL) {
                    UohAnalytics.sendViewBeriUlasanButtonEvent()
                }
            } else {
                binding.uohBtnAction2.gone()
            }

            binding.clDataProduct.setOnClickListener {
                actionListener?.onListItemClicked(item.dataObject, position)
            }

            binding.uohBtnAction1.setOnClickListener {
                if (item.dataObject.metadata.buttons.isNotEmpty()) {
                    actionListener?.onActionButtonClicked(
                        item.dataObject,
                        position,
                        FIRST_BUTTON_INDEX
                    )
                }
            }

            binding.uohBtnAction2.setOnClickListener {
                if (item.dataObject.metadata.buttons.size > 1) {
                    actionListener?.onActionButtonClicked(
                        item.dataObject,
                        position,
                        SECOND_BUTTON_INDEX
                    )
                }
            }

            setupReviewRatingWidget(item.dataObject, item.dataObject.orderUUID)

            actionListener?.trackViewOrderCard(item.dataObject, position)
        }
    }

    private fun setupReviewRatingWidget(
        order: UohListOrder.UohOrders.Order,
        orderUUID: String
    ) {
        binding.layoutReviewRating.apply {
            setContent {
                val componentData = order.metadata.getReviewRatingComponent()
                val config = remember(orderUUID, componentData, actionListener) {
                    mutableStateOf(
                        if (componentData == null) {
                            UohReviewRatingWidgetConfig()
                        } else {
                            UohReviewRatingWidgetConfig(
                                show = true,
                                componentData = componentData,
                                onRatingChanged = { appLink ->
                                    actionListener?.onReviewRatingClicked(
                                        index = bindingAdapterPosition,
                                        order = order,
                                        appLink = appLink
                                    )
                                }
                            )
                        }
                    )
                }
                NestTheme {
                    UohReviewRatingWidget(config = config.value)
                }
            }

            showWithCondition(order.metadata.getReviewRatingComponent() != null)
        }
    }

    companion object {
        const val FIRST_BUTTON_INDEX = 0
        const val SECOND_BUTTON_INDEX = 1
    }
}
