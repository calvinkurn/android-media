package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.SortUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.SortAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class SortAdapter(onItemClick: (sort: SortUiModel) -> Unit) : BaseAdapter<SortAdapterFactoryImpl>(SortAdapterFactoryImpl(onItemClick)) {

    val sortItems: List<SortUiModel>
        get() = visitables.filterIsInstance<SortUiModel>()
}