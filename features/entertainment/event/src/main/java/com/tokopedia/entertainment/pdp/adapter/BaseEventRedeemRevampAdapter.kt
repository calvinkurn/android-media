package com.tokopedia.entertainment.pdp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.entertainment.pdp.adapter.diffutil.BaseEventRedeemRevampDiffer

/**
 * Author firmanda on 17,Nov,2022
 */

open class BaseEventRedeemRevampAdapter <T, F: AdapterTypeFactory>(
    baseListAdapterTypeFactory: F,
    private val differ: BaseEventRedeemRevampDiffer,
): BaseListAdapter<T, F>(baseListAdapterTypeFactory) {

    fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }
}
