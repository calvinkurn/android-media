package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTypeGlobalError
import com.tokopedia.shop.score.performance.presentation.adapter.GlobalErrorListener
import com.tokopedia.shop.score.performance.presentation.model.ItemShopPerformanceErrorUiModel
import kotlinx.android.synthetic.main.item_shop_penalty_error_state.*
import kotlinx.android.synthetic.main.item_shop_performance_error.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ItemShopPerformanceErrorViewHolder(view: View, private val globalErrorListener: GlobalErrorListener): AbstractViewHolder<ItemShopPerformanceErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_performance_error
    }

    override fun bind(element: ItemShopPerformanceErrorUiModel?) {
        with(itemView) {
            globalErrorShopPerformance.setTypeGlobalError(element?.throwable)
            globalErrorShopPerformance?.errorAction?.setOnClickListener {
                globalErrorListener.onBtnErrorStateClicked()
            }
        }
    }
}