package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailResolutionBinding
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class OrderResolutionViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator,
    private val listener: OrderResolutionViewHolder.OrderResolutionListener
) : AbstractViewHolder<OrderResolutionUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_resolution
    }

    private val binding by viewBinding<ItemBuyerOrderDetailResolutionBinding>()

    override fun bind(uiModel: OrderResolutionUiModel) {
        binding?.run {
            bindResolutionIcon(uiModel.picture)
            bindResolutionTitle(uiModel.title)
            bindResolutionStatus(uiModel.status, uiModel.resolutionStatusFontColor)
            bindResolutionDescription(uiModel.description.parseAsHtml())
            bindResolutionDeadline(
                uiModel.deadlineDateTime,
                uiModel.backgroundColor,
                uiModel.showDeadline
            )
            bindListener(uiModel.redirectPath)
        }
    }

    override fun bind(element: OrderResolutionUiModel, payloads: MutableList<Any>) {
        binding?.run {
            payloads.firstOrNull()?.let {
                if (it is Pair<*, *>) {
                    val (oldItem, newItem) = it
                    if (oldItem is OrderResolutionUiModel && newItem is OrderResolutionUiModel) {
                        root.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                        if (oldItem.picture != newItem.picture) {
                            bindResolutionIcon(newItem.picture)
                        }
                        if (oldItem.title != newItem.title) {
                            bindResolutionTitle(newItem.title)
                        }
                        if (
                            oldItem.status != newItem.status ||
                            oldItem.resolutionStatusFontColor != newItem.resolutionStatusFontColor
                        ) {
                            bindResolutionStatus(newItem.status, newItem.resolutionStatusFontColor)
                        }
                        if (oldItem.description != newItem.description) {
                            bindResolutionDescription(newItem.description.parseAsHtml())
                        }
                        if (
                            oldItem.deadlineDateTime != newItem.deadlineDateTime ||
                            oldItem.backgroundColor != newItem.backgroundColor ||
                            oldItem.showDeadline != newItem.showDeadline
                        ) {
                            bindResolutionDeadline(
                                newItem.deadlineDateTime,
                                newItem.backgroundColor,
                                newItem.showDeadline
                            )
                        }
                        if (oldItem.redirectPath != newItem.redirectPath) {
                            bindListener(newItem.redirectPath)
                        }
                        root.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                        return
                    }
                }
            }
        }
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionIcon(url: String) {
        ivDisplay.loadImage(url)
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionTitle(title: String) {
        tvTitle.text = title
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionStatus(
        status: String,
        fontColor: String
    ) {
        val color = Utils.parseUnifyColorHex(
            context = tvStatus.context,
            colorHex = fontColor,
            defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_TN600
        )
        tvStatus.text = status
        tvStatus.setTextColor(color)
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionDescription(description: CharSequence) {
        tvDescription.text = description
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionDeadline(
        deadlineDateTime: String,
        backgroundColor: String,
        showDeadline: Boolean
    ) {
        if (showDeadline) {
            tvDueResponseTitle.show()
            tvDueResponse.show()
            tvDueResponseValue.text = deadlineDateTime
            val deadlineBackground = Utils.getColoredResoDeadlineBackground(
                context = root.context,
                colorHex = backgroundColor,
                defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_YN600
            )
            tvDueResponse.background = deadlineBackground
        } else {
            tvDueResponseTitle.gone()
            tvDueResponse.gone()
        }
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindListener(redirectPath: String) {
        root.setOnClickListener {
            navigator.openAppLink(redirectPath, true)
            listener.onResolutionWidgetClicked()
        }
    }

    interface OrderResolutionListener {
        fun onResolutionWidgetClicked()
    }
}
