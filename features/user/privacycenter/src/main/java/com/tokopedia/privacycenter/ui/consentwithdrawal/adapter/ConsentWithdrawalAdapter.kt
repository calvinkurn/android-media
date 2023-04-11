package com.tokopedia.privacycenter.ui.consentwithdrawal.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalListener
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.delegate.PurposeAdapterDelegate
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.delegate.TitleDividerAdapterDelegate
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.ConsentWithdrawalUiModel
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.PurposeUiModel

class ConsentWithdrawalAdapter(
    mandatoryListener: ConsentWithdrawalListener.Mandatory
) : BaseAdapter<ConsentWithdrawalUiModel>() {

    init {
        delegatesManager
            .addDelegate(TitleDividerAdapterDelegate())
            .addDelegate(PurposeAdapterDelegate(mandatoryListener))
    }

    private fun getPurposePosition(purposeId: String): Int {
        itemList.forEachIndexed { index, consentWithdrawalUiModel ->
            if (consentWithdrawalUiModel is PurposeUiModel) {
                val item = consentWithdrawalUiModel.data
                if (purposeId == item.purposeId) {
                    return index
                }
            }
        }

        return INDEX_NOT_FOUND
    }

    fun updatePurposeState(purposeId: String, transactionType: String) {
        val position = getPurposePosition(purposeId)
        if (position == INDEX_NOT_FOUND) return

        setItems(
            itemList.mapIndexed { index, consentWithdrawalUiModel ->
                if (consentWithdrawalUiModel is PurposeUiModel && index == position) {
                    consentWithdrawalUiModel.copy(
                        data = consentWithdrawalUiModel.data.apply {
                            consentStatus = transactionType
                        }
                    )
                } else {
                    consentWithdrawalUiModel
                }
            }
        )

        notifyItemChanged(position)
    }

    fun notifyNewItems() {
        notifyItemRangeChanged(FIRST_INDEX, itemCount)
    }

    companion object {
        const val FIRST_INDEX = 0
        const val INDEX_NOT_FOUND = -1
    }
}
