package com.tokopedia.entertainment.pdp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.entertainment.pdp.adapter.diffutil.BaseEventRedeemRevampDiffer
import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel
import com.tokopedia.kotlin.extensions.view.ONE

/**
 * Author firmanda on 17,Nov,2022
 */

open class BaseEventRedeemRevampAdapter <T, F: AdapterTypeFactory>(
    baseListAdapterTypeFactory: F,
    private val differ: BaseEventRedeemRevampDiffer,
    private val eventRedeemBaseAdapterListener: EventRedeemBaseAdapterListener,
): BaseListAdapter<T, F>(baseListAdapterTypeFactory) {

    var listCheckedIds: MutableList<Pair<String, Boolean>> = mutableListOf()

    fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    fun updateAllListItem(isChecked: Boolean, titlePosition: Int) {
        val size = (visitables.size - Int.ONE)
        if (titlePosition < size) {
            if (visitables[titlePosition] is ParticipantTitleUiModel) {
                (visitables[titlePosition] as ParticipantTitleUiModel).isChecked = isChecked
            }
            notifyItemChanged(titlePosition)

            for (itemPosition in (titlePosition+Int.ONE)..size) {
                if (visitables[itemPosition] is ParticipantTitleUiModel) {
                    break
                } else if ((visitables[itemPosition] is ParticipantUiModel)
                    && !(visitables[itemPosition] as ParticipantUiModel).isDisabled) {
                    updateCheckedItem(isChecked, itemPosition, (visitables[itemPosition] as ParticipantUiModel))
                }
            }
            eventRedeemBaseAdapterListener.updateListChecked(listCheckedIds)
        }
    }

    fun updateOneItem(isChecked: Boolean, itemPosition: Int) {
        if ((visitables[itemPosition] is ParticipantUiModel)) {
            updateCheckedItem(isChecked, itemPosition, (visitables[itemPosition] as ParticipantUiModel))
            updateItemIsAllChecked((visitables[itemPosition] as ParticipantUiModel))
            eventRedeemBaseAdapterListener.updateListChecked(listCheckedIds)
        }
    }

    private fun updateItemIsAllChecked(participantUiModel: ParticipantUiModel) {
        val isAllChecked = isAllItemChecked(participantUiModel)
        val index = getIndexParent(participantUiModel)

        if (index != null) {
            (visitables[index] as ParticipantTitleUiModel).isChecked = isAllChecked
            notifyItemChanged(index)
        }
    }

    private fun updateCheckedItem(isChecked: Boolean, itemPosition: Int, participantUiModel: ParticipantUiModel) {
        participantUiModel.isChecked = isChecked
        notifyItemChanged(itemPosition)
        updateListCheckedStatus(isChecked, participantUiModel)
    }

    private fun updateListCheckedStatus(isChecked: Boolean, participantUiModel: ParticipantUiModel) {
        val index = listCheckedIds.mapIndexed { index, pair ->
            Pair(pair.first, index)
        }.firstOrNull{
            participantUiModel.id == it.first
        }
        if (index != null) {
            listCheckedIds[index.second] = Pair(participantUiModel.id, isChecked)
        } else {
            listCheckedIds.add(Pair(participantUiModel.id, isChecked))
        }
    }

    private fun isAllItemChecked(participantUiModel: ParticipantUiModel): Boolean {
        var isAllChecked = true
        run breaking@ {
            visitables.forEach {
                if ((it is ParticipantUiModel) && !it.isDisabled &&
                    it.day == participantUiModel.day && !it.isChecked) {
                    isAllChecked = false
                    return@breaking
                }
            }
        }
        return isAllChecked
    }

    private fun getIndexParent(participantUiModel: ParticipantUiModel): Int? {
        val index = visitables.mapIndexed { index, visitable ->
            Pair(index, visitable)
        }.firstOrNull{
            (it.second is ParticipantTitleUiModel) &&
                (it.second as ParticipantTitleUiModel).day == participantUiModel.day
        }
        return index?.first
    }


    fun interface EventRedeemBaseAdapterListener {
        fun updateListChecked(listCheckedIds: MutableList<Pair<String, Boolean>>)
    }
}
