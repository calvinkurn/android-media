package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionReviewBinding
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderReviewModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class OrderReviewViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderReviewModel>(itemView) {
    private var binding: HolderTransactionReviewBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_review
    }

    override fun bind(element: OrderReviewModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(paymentModel: OrderReviewModel) {
        val context = itemView.context


    }
}