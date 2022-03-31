package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter.factory.MoreMenuAdapterFactoryImpl
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel

class MoreMenuAdapter(
    callback: (MoreMenuUiModel) -> Unit,
    private val differ: MoreMenuDiffer
) : BaseListAdapter<Visitable<*>, MoreMenuAdapterFactoryImpl>(MoreMenuAdapterFactoryImpl(callback)) {

    fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }
}