package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.FilterAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class FilterAdapter(onItemClick: (String) -> Unit) : BaseAdapter<FilterAdapterFactoryImpl>(FilterAdapterFactoryImpl(onItemClick)) {

    val items: List<Visitable<*>>
        get() = visitables
}