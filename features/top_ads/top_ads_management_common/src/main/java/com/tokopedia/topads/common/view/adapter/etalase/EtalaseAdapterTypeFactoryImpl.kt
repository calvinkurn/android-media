package com.tokopedia.topads.common.view.adapter.etalase

import android.view.View
import com.tokopedia.topads.common.view.adapter.etalase.viewholder.EtalaseViewHolder
import com.tokopedia.topads.common.view.adapter.etalase.viewholder.EtalaseItemViewHolder
import com.tokopedia.topads.common.view.adapter.etalase.viewholder.EtalaseShimerViewHolder
import com.tokopedia.topads.common.view.adapter.etalase.uimodel.EtalaseItemUiModel
import com.tokopedia.topads.common.view.adapter.etalase.uimodel.EtalaseShimerUiModel


class EtalaseAdapterTypeFactoryImpl(var actionClick: ((pos:Int) -> Unit)?) : EtalaseAdapterTypeFactory {

    override fun type(model: EtalaseItemUiModel) = EtalaseItemViewHolder.LAYOUT

    override fun type(model: EtalaseShimerUiModel) = EtalaseShimerViewHolder.LAYOUT

    override fun holder(type: Int, view: View): EtalaseViewHolder<*> {
        return when(type){
            EtalaseShimerViewHolder.LAYOUT -> EtalaseShimerViewHolder(view)
            EtalaseItemViewHolder.LAYOUT -> EtalaseItemViewHolder(view, actionClick)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}