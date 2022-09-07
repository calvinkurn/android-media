package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailResolutionBinding
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class OrderResolutionViewHolder(
    itemView: View?,
    private val orderResolutionListener: OrderResolutionListener
) : AbstractViewHolder<OrderResolutionUIModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_resolution
    }

    val binding by viewBinding<ItemBuyerOrderDetailResolutionBinding>()

    override fun bind(uiModel: OrderResolutionUIModel?) {
        itemView.context?.let { context ->
            uiModel?.let {
                binding?.tvTitle?.text = uiModel.title
                binding?.tvStatus?.text = uiModel.status
                binding?.tvDescription?.text = uiModel.description
                binding?.ivDisplay?.loadImage(uiModel.picture)
                showDeadline(context, uiModel)
                itemView.setOnClickListener {
                    orderResolutionListener.onResolutionWidgetClicked(uiModel.redirectPath)
                }
            }
        }
    }

    fun showDeadline(context: Context, uiModel: OrderResolutionUIModel?) {
        if (!uiModel?.showDeadline.orFalse()) {
            binding?.tvDueResponseTitle?.visibility = View.GONE
            binding?.tvDueResponse?.visibility = View.GONE
        } else {
            binding?.tvDueResponseTitle?.visibility = View.VISIBLE
            binding?.tvDueResponse?.visibility = View.VISIBLE
            binding?.tvDueResponseValue?.text = uiModel?.deadlineDateTime
            val deadlineBackground = Utils.getColoredResoDeadlineBackground(
                context = context,
                colorHex = uiModel?.backgroundColor.orEmpty(),
                defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_YN600
            )
            binding?.tvDueResponse?.background = deadlineBackground
        }
    }

    interface OrderResolutionListener {
        fun onResolutionWidgetClicked(redirectPath: String?)
    }
}