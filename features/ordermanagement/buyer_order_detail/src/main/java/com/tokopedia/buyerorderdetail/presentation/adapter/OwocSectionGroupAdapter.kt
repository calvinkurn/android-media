package com.tokopedia.buyerorderdetail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.OwocSectionGrouplDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocSectionGroupTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.OwocErrorUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel
import com.tokopedia.kotlin.extensions.view.ZERO

class OwocSectionGroupAdapter(private val typeFactory: OwocSectionGroupTypeFactoryImpl) :
    BaseAdapter<OwocSectionGroupTypeFactoryImpl>(typeFactory) {

    fun updateItems(newOwocSectionGroupList: List<Visitable<OwocSectionGroupTypeFactoryImpl>>) {
        val diffCallback = OwocSectionGrouplDiffUtilCallback(
            visitables as List<Visitable<OwocSectionGroupTypeFactoryImpl>>,
            newOwocSectionGroupList,
            typeFactory
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(newOwocSectionGroupList)
    }

    fun showLoadingShimmer() {
        if (visitables.firstOrNull() !is OwocShimmerUiModel) {
            visitables.add(OwocShimmerUiModel())
            notifyItemInserted(Int.ZERO)
        }
    }

    fun hideLoadingShimmer() {
        if (visitables.getOrNull(lastIndex) is OwocShimmerUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun showError(item: OwocErrorUiModel) {
        if (visitables.firstOrNull() !is OwocErrorUiModel) {
            visitables.add(item)
            notifyItemInserted(Int.ZERO)
        }
    }

    fun hideError() {
        if (visitables.getOrNull(lastIndex) is OwocErrorUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }
}
