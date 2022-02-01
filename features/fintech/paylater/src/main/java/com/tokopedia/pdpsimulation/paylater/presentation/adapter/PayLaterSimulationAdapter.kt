package com.tokopedia.pdpsimulation.paylater.presentation.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.pdpsimulation.paylater.domain.model.BasePayLaterWidgetUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel

class PayLaterSimulationAdapter(adapterFactory: PayLaterAdapterFactoryImpl) :
    BaseListAdapter<BasePayLaterWidgetUiModel, PayLaterAdapterFactoryImpl>(adapterFactory) {

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
        val diffCallback = RatingDiffCallback(visitables, element)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(element)
        if (element.isEmpty()) {
            showEmptyState()
        }
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