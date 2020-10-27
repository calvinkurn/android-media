package com.tokopedia.topads.common.view.adapter.etalase

import android.view.View
import com.tokopedia.topads.common.view.adapter.etalase.viewholder.EtalaseViewHolder
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseShimerViewModel

/**
 * Author errysuprayogi on 11,November,2019
 */
interface EtalaseAdapterTypeFactory {

    fun type(model: EtalaseItemViewModel): Int

    fun type(model: EtalaseShimerViewModel): Int

    fun holder(type: Int, view: View): EtalaseViewHolder<*>

}