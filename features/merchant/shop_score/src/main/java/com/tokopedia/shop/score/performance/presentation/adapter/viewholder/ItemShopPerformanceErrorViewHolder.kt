package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTypeGlobalError
import com.tokopedia.shop.score.databinding.ItemShopPerformanceErrorBinding
import com.tokopedia.shop.score.performance.presentation.adapter.GlobalErrorListener
import com.tokopedia.shop.score.performance.presentation.model.ItemShopPerformanceErrorUiModel
import com.tokopedia.utils.view.binding.viewBinding


class ItemShopPerformanceErrorViewHolder(
    view: View,
    private val globalErrorListener: GlobalErrorListener
) : AbstractViewHolder<ItemShopPerformanceErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_performance_error
    }

    private val binding: ItemShopPerformanceErrorBinding? by viewBinding()

    override fun bind(element: ItemShopPerformanceErrorUiModel?) {
        binding?.apply {
            globalErrorShopPerformance.setTypeGlobalError(element?.throwable)
            globalErrorShopPerformance.errorAction.setOnClickListener {
                globalErrorListener.onBtnErrorStateClicked()
            }
        }
    }
}