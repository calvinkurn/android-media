package com.tokopedia.home_account.consentWithdrawal.ui.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.home_account.consentWithdrawal.ui.ConsentWithdrawalListener
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.delegate.MandatoryPurposeAdapterDelegate
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.delegate.OptionalPurposeAdapterDelegate
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.delegate.TitleDividerAdapterDelegate
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.ConsentWithdrawalUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.MandatoryPurposeUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.OptionalPurposeUiModel

class ConsentWithdrawalAdapter(
    mandatoryListener: ConsentWithdrawalListener.Mandatory,
    optionalListener: ConsentWithdrawalListener.Optional,
) : BaseAdapter<ConsentWithdrawalUiModel>() {

    init {
        delegatesManager
            .addDelegate(TitleDividerAdapterDelegate())
            .addDelegate(MandatoryPurposeAdapterDelegate(mandatoryListener))
            .addDelegate(OptionalPurposeAdapterDelegate(optionalListener))
    }

    private fun getPurposePosition(purposeId: String): Int {
        itemList.forEachIndexed { index, consentWithdrawalUiModel ->
            if (consentWithdrawalUiModel is MandatoryPurposeUiModel) {
                val item = consentWithdrawalUiModel.data
                if (purposeId == item.purposeId) {
                    return index
                }
            }

            if (consentWithdrawalUiModel is OptionalPurposeUiModel) {
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
                if (consentWithdrawalUiModel is MandatoryPurposeUiModel && index == position) {
                    consentWithdrawalUiModel.copy(
                        data = consentWithdrawalUiModel.data.apply {
                        consentStatus = transactionType
                    }
                    )
                } else if (consentWithdrawalUiModel is OptionalPurposeUiModel && index == position) {
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
        notifyItemRangeChanged(FIRST_INDEX, itemCount - 1)
    }

    companion object {
        const val FIRST_INDEX = 0
    }
}
