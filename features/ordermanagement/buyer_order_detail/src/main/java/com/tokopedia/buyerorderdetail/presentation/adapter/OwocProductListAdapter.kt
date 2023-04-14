package com.tokopedia.buyerorderdetail.presentation.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.OwocProductListDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocSectionGroupTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel

class OwocProductListAdapter(private val typeFactory: OwocProductListTypeFactoryImpl) :
    BaseAdapter<OwocProductListTypeFactoryImpl>(typeFactory) {

    fun updateItems(context: Context?, newItem: OwocProductListUiModel) {
        val newItems = listOf<Visitable<OwocProductListTypeFactoryImpl>>()
        val diffCallback = OwocProductListDiffUtilCallback(
            visitables as List<Visitable<OwocProductListTypeFactoryImpl>>,
            newItems,
            typeFactory
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(newItems)
    }

    fun getItemPosition(uiModel: Visitable<OwocProductListTypeFactoryImpl>?): Int {
        return visitables.indexOf(uiModel)
    }

    fun getBaseVisitableUiModels(): List<Visitable<OwocSectionGroupTypeFactoryImpl>> {
        return visitables.filterIsInstance<Visitable<OwocSectionGroupTypeFactoryImpl>>()
    }
}
