package com.tokopedia.topads.common.view.adapter.etalase

import android.view.View
import com.tokopedia.topads.common.view.adapter.etalase.viewholder.EtalaseViewHolder
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseItemUiModel
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseShimerUiModel

/**
 * Author errysuprayogi on 11,November,2019
 */
interface EtalaseAdapterTypeFactory {

    fun type(model: EtalaseItemUiModel): Int

    fun type(model: EtalaseShimerUiModel): Int

    fun holder(type: Int, view: View): EtalaseViewHolder<*>

}