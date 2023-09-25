package com.tokopedia.sellerorder.detail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.sellerorder.detail.presentation.adapter.diffutil.SomDetailTransparencyDiffUtilCallback
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFee
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeErrorStateUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLoadingUiModel

class SomDetailTransparencyFeeAdapter(private val typeFactory: DetailTransparencyFeeAdapterFactoryImpl) :
    BaseAdapter<DetailTransparencyFeeAdapterFactoryImpl>(typeFactory) {

    fun updateItems(newTransparencyFeeList: List<BaseTransparencyFee>) {
        val diffCallback = SomDetailTransparencyDiffUtilCallback(
            visitables as List<BaseTransparencyFee>,
            newTransparencyFeeList,
            typeFactory
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(newTransparencyFeeList)
    }

    fun showLoadingShimmer() {
        if (visitables.firstOrNull() !is TransparencyFeeLoadingUiModel) {
            val newList = visitables.toMutableList()
            newList.removeAll { it is BaseTransparencyFee }
            newList.add(TransparencyFeeLoadingUiModel())
            val transparencyFeeList = newList as? List<BaseTransparencyFee>
            transparencyFeeList?.let { updateItems(it) }
        }
    }

    fun hideLoadingShimmer() {
        if (visitables.firstOrNull() is TransparencyFeeLoadingUiModel) {
            val newList = visitables.toMutableList()
            newList.removeFirst { it is TransparencyFeeLoadingUiModel }
            val transparencyFeeList = newList as? List<BaseTransparencyFee>
            transparencyFeeList?.let { updateItems(it) }
        }
    }

    fun showError(item: TransparencyFeeErrorStateUiModel) {
        if (visitables.firstOrNull() !is TransparencyFeeErrorStateUiModel) {
            val newList = visitables.toMutableList()
            newList.removeAll { it is BaseTransparencyFee }
            newList.add(item)
            val transparencyFeeList = newList as? List<BaseTransparencyFee>
            transparencyFeeList?.let { updateItems(it) }
        }
    }

    fun hideError() {
        if (visitables.getOrNull(firstIndex) is TransparencyFeeErrorStateUiModel) {
            val newList = visitables.toMutableList()
            newList.removeFirst { it is TransparencyFeeErrorStateUiModel }
            val transparencyFeeList = newList as? List<BaseTransparencyFee>
            transparencyFeeList?.let { updateItems(it) }
        }
    }
}

