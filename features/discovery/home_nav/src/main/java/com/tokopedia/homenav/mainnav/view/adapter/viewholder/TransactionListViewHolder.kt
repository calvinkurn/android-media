package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionListBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.OrderListTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderListAdapter
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.TransactionListItemDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.utils.view.binding.viewBinding

class TransactionListViewHolder(itemView: View,
                                val mainNavListener: MainNavListener
): AbstractViewHolder<TransactionListItemDataModel>(itemView) {
    private var binding: HolderTransactionListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_list
    }

    override fun bind(element: TransactionListItemDataModel) {
        val context = itemView.context
        val adapter = OrderListAdapter(OrderListTypeFactoryImpl(mainNavListener))

        val edgeMargin = 16f.toDpInt()
        val spacingBetween = 8f.toDpInt()

        binding?.transactionRv?.adapter = adapter
        binding?.transactionRv?.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
        )
        if (binding?.transactionRv?.itemDecorationCount == 0) {
            binding?.transactionRv?.addItemDecoration(
                    NavOrderSpacingDecoration(spacingBetween, edgeMargin)
            )
        }
        val visitableList = mutableListOf<Visitable<*>>()
        visitableList.addAll(element.orderListModel.paymentList.map { OrderPaymentModel(it) })
        visitableList.addAll(element.orderListModel.orderList.map { OrderProductModel(it) })
        if (element.othersTransactionCount.isMoreThanZero()) {
            visitableList.add(OtherTransactionModel(element.othersTransactionCount))
        }
        adapter.setVisitables(visitableList)
    }
}