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
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentRevampModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductRevampModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionRevampModel
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.utils.view.binding.viewBinding

class TransactionListViewHolder(itemView: View,
                                val mainNavListener: MainNavListener
): AbstractViewHolder<TransactionListItemDataModel>(itemView) {
    private var binding: HolderTransactionListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_list
        private const val EDGE_MARGIN = 16f
        private const val SPACING_BETWEEN = 8f
        private const val MEPAGE_CARD_INNER_PADDING = 4f
        private const val TRANSACTION_CARD_HEIGHT = 80f
        private const val MAX_CARDS_SHOWN = 5
    }

    private val adapter = OrderListAdapter(OrderListTypeFactoryImpl(mainNavListener))

    override fun bind(element: TransactionListItemDataModel) {
        val context = itemView.context

        binding?.transactionRv?.adapter = adapter
        binding?.transactionRv?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        addItemDecoration(element)

        if (element.isMePageUsingRollenceVariant) submitListMePage(element)
        else submitListControl(element)

    }

    private fun addItemDecoration(element: TransactionListItemDataModel) {
        var spacingBetween = SPACING_BETWEEN
        var edgeMargin = EDGE_MARGIN
        if(element.isMePageUsingRollenceVariant) {
            spacingBetween -= 2 * MEPAGE_CARD_INNER_PADDING
            edgeMargin -= MEPAGE_CARD_INNER_PADDING
        }
        binding?.transactionRv?.addItemDecoration(
            NavOrderSpacingDecoration(
                spacingBetween.toDpInt(),
                edgeMargin.toDpInt()
            )
        )
    }

    private fun submitListMePage(element: TransactionListItemDataModel) {
        val visitableList = mutableListOf<Visitable<*>>()
        visitableList.addAll(
            element.orderListModel.paymentList.mapIndexed { index, it ->
                OrderPaymentRevampModel(it, visitableList.count() + index)
            }
        )
        visitableList.addAll(
            element.orderListModel.orderList.mapIndexed { index, it ->
                OrderProductRevampModel(it, visitableList.count() + index)
            }
        )
        adapter.setVisitables(
            visitableList.take(MAX_CARDS_SHOWN)
                .plus(OtherTransactionRevampModel())
        )
        binding?.transactionRv?.setHeightBasedOnTransactionCardMaxHeight(element)
    }

    private fun submitListControl(element: TransactionListItemDataModel) {
        val visitableList = mutableListOf<Visitable<*>>()
        visitableList.addAll(element.orderListModel.paymentList.mapIndexed { index, it ->
            OrderPaymentModel(it, visitableList.count() + index) })
        visitableList.addAll(element.orderListModel.orderList.mapIndexed { index, it ->
            OrderProductModel(it, visitableList.count() + index) })
        if (element.othersTransactionCount.isMoreThanZero())
            visitableList.add(OtherTransactionModel(element.othersTransactionCount))
        adapter.setVisitables(visitableList)
    }

    private fun RecyclerView.setHeightBasedOnTransactionCardMaxHeight(element: TransactionListItemDataModel) {
        if (element.isMePageUsingRollenceVariant) {

            val carouselLayoutParams = this.layoutParams
            carouselLayoutParams?.height = TRANSACTION_CARD_HEIGHT.toDpInt()
            this.layoutParams = carouselLayoutParams
        }
    }
}
