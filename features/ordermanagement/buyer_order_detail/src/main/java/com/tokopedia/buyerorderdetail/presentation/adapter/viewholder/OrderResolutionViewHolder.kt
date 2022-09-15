package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailResolutionBinding
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.utils.view.binding.viewBinding

class OrderResolutionViewHolder(
    itemView: View?,
    private val orderResolutionListener: OrderResolutionListener
) : AbstractViewHolder<OrderResolutionUIModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_resolution
    }

    private val binding by viewBinding<ItemBuyerOrderDetailResolutionBinding>()

    override fun bind(uiModel: OrderResolutionUIModel) {
        binding?.run {
            bindResolutionIcon(uiModel.picture)
            bindResolutionTitle(uiModel.title)
            bindResolutionStatus(uiModel.status, uiModel.resolutionStatusFontColor)
            bindResolutionDescription(uiModel.description.parseAsHtml())
            bindResolutionDeadline(uiModel.deadlineDateTime, uiModel.backgroundColor, uiModel.showDeadline)
            bindListener(uiModel.redirectPath)
        }
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionIcon(url: String) {
        ivDisplay.loadImage(url)
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionTitle(title: String) {
        tvTitle.text = title
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionStatus(status: String, fontColor: String) {
        val unifyColor = stringToUnifyColor(root.context, fontColor)
        tvStatus.text = status
        tvStatus.setTextColor(unifyColor.unifyColor ?: unifyColor.defaultColor)
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionDescription(description: CharSequence) {
        tvDescription.text = description
    }

    private fun ItemBuyerOrderDetailResolutionBinding.bindResolutionDeadline(
        deadlineDateTime: String, backgroundColor: String, showDeadline: Boolean
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
        root.setOnClickListener { orderResolutionListener.onResolutionWidgetClicked(redirectPath) }
    }

    interface OrderResolutionListener {
        fun onResolutionWidgetClicked(redirectPath: String?)
    }
}