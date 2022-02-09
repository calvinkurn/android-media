package com.tokopedia.pdpsimulation.paylater.presentation.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdpsimulation.common.analytics.PayLaterProductImpressionEvent
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.paylater.domain.model.BasePayLaterWidgetUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel
import com.tokopedia.pdpsimulation.paylater.presentation.viewholder.PayLaterDetailViewHolder

class PayLaterSimulationAdapter(val adapterFactory: PayLaterAdapterFactoryImpl) :
    BaseListAdapter<BasePayLaterWidgetUiModel, PayLaterAdapterFactoryImpl>(adapterFactory) {

    private val impressionMap = hashSetOf<Int>()

    private fun showEmptyState() {
        //addElement(EmptyModel())
        //notifyItemChanged(0)
    }

    fun showLoadingInAdapter() {
        removeErrorNetwork()
        showLoading()
    }

    fun showInitialLoadingFailed(throwable: Throwable) {
        //setErrorNetworkModel(TransactionErrorModel(throwable))
        //showErrorNetwork()
    }

    fun addAllElements(element: List<Visitable<*>>) {
        hideLoading()
        impressionMap.clear()
        val diffCallback = RatingDiffCallback(visitables, element)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(element)
        //if (element.isEmpty()) showEmptyState()
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateOptionList(adapterPosition: Int) {
        if (visitables[adapterPosition] is SeeMoreOptionsUiModel) {
            val moreOptionList = (visitables[adapterPosition] as SeeMoreOptionsUiModel).remainingItems
            // remove see more model
            visitables.removeAt(adapterPosition)
            // add remaining options
            visitables.addAll(adapterPosition, moreOptionList)
            notifyItemRangeChanged(adapterPosition, moreOptionList.size+1)
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is PayLaterDetailViewHolder) {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION && impressionMap.add(position)) {
                // not seen
                val item = visitables[position] as Detail
                val event = PayLaterProductImpressionEvent().apply {
                    tenureOption = item.tenure ?: 0
                    userStatus = item.userState ?: ""
                    payLaterPartnerName = item.gatewayDetail?.name?:""
                    action = PdpSimulationAnalytics.IMPRESSION_PARTNER_CARD
                    emiAmount = item.installment_per_month_ceil?.toString() ?:""
                }
                adapterFactory.interaction.invokeAnalytics(event)
            }
        }
    }

    class RatingDiffCallback(
        private val oldList: List<Visitable<*>>,
        private val newList: List<Visitable<*>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] === newList[newPosition]
        }

        @Nullable
        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return super.getChangePayload(oldPosition, newPosition)
        }
    }

}