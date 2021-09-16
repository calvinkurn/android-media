package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.utils.GoldMerchantUtil.setTypeGlobalError
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.GlobalErrorListener
import com.tokopedia.shop.score.performance.presentation.model.ItemShopPerformanceErrorUiModel
import kotlinx.android.synthetic.main.item_shop_performance_error.view.*


class ItemShopPerformanceErrorViewHolder(
    view: View,
    private val globalErrorListener: GlobalErrorListener
) : AbstractViewHolder<ItemShopPerformanceErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_performance_error
    }

    override fun bind(element: ItemShopPerformanceErrorUiModel?) {
        with(itemView) {
            setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
            globalErrorShopPerformance?.setTypeGlobalError(element?.throwable)
            globalErrorShopPerformance?.errorAction?.setOnClickListener {
                globalErrorListener.onBtnErrorStateClicked()
            }
        }
    }
}