package com.tokopedia.topads.view.adapter.bidinfo

import android.view.View
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyUiModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemUiModel
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoViewHolder

interface BindInfoAdapterTypeFactory {

    fun type(model:BidInfoEmptyUiModel):Int

    fun type(model:BidInfoItemUiModel):Int

    fun holder(type:Int,view: View):BidInfoViewHolder<*>
}
