package com.tokopedia.privacycenter.consentwithdrawal.ui.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.privacycenter.consentwithdrawal.ui.ConsentWithdrawalListener
import com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.delegate.PurposeAdapterDelegate
import com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.delegate.TitleDividerAdapterDelegate
import com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.uimodel.ConsentWithdrawalUiModel
import com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.uimodel.PurposeUiModel

class ConsentWithdrawalAdapter(
    mandatoryListener: ConsentWithdrawalListener.Mandatory,
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

        return -1
    }

    fun updatePurposeState(purposeId: String, transactionType: String) {
        val position = getPurposePosition(purposeId)
        if (position == -1) return

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
    }
}
