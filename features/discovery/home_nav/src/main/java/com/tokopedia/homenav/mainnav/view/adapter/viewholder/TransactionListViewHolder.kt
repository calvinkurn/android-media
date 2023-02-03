package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentRevampModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductRevampModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderEmptyModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionRevampModel
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
        private const val SIZE_LAYOUT_SHOW_VIEW_ALL_CARD = 5
        private var EDGE_MARGIN = 16f.toDpInt()
        private var SPACING_BETWEEN = 8f.toDpInt()
        private var TRANSACTION_CARD_HEIGHT = 80f.toDpInt()
        private const val OTHER_TRANSACTION_THRESHOLD = 0
    }

    private fun RecyclerView.setHeightBasedOnTransactionCardMaxHeight(element: TransactionListItemDataModel) {
        if (element.isMePageUsingRollenceVariant) {

            val carouselLayoutParams = this.layoutParams
            carouselLayoutParams?.height = TRANSACTION_CARD_HEIGHT
            this.layoutParams = carouselLayoutParams
        }
    }

    override fun bind(element: TransactionListItemDataModel) {
        val context = itemView.context
        val adapter = OrderListAdapter(OrderListTypeFactoryImpl(mainNavListener))

        binding?.transactionRv?.adapter = adapter
        binding?.transactionRv?.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
        )
        if (binding?.transactionRv?.itemDecorationCount == 0) {
            binding?.transactionRv?.addItemDecoration(
                NavOrderSpacingDecoration(SPACING_BETWEEN, EDGE_MARGIN)
            )
        }
        val visitableList = mutableListOf<Visitable<*>>()
        if (element.isMePageUsingRollenceVariant) {
            visitableList.addAll(element.orderListModel.paymentList.map { OrderPaymentRevampModel(it) })
            visitableList.addAll(element.orderListModel.orderList.map { OrderProductRevampModel(it) })
            if (visitableList.isEmpty()) {
                visitableList.add(OrderEmptyModel())
            } else if (visitableList.size == SIZE_LAYOUT_SHOW_VIEW_ALL_CARD && element.othersTransactionCount > OTHER_TRANSACTION_THRESHOLD) {
                visitableList.add(OtherTransactionRevampModel())
                binding?.transactionRv?.setHeightBasedOnTransactionCardMaxHeight(element)
            }
        }
        else {
            visitableList.addAll(element.orderListModel.paymentList.map { OrderPaymentModel(it) })
            visitableList.addAll(element.orderListModel.orderList.map { OrderProductModel(it) })
            if (element.othersTransactionCount.isMoreThanZero()) {
                visitableList.add(OtherTransactionModel(element.othersTransactionCount))
            }
        }
        adapter.setVisitables(visitableList)
    }
}
