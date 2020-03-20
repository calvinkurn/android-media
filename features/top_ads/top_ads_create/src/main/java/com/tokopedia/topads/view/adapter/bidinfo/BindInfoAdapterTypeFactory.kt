package com.tokopedia.topads.view.adapter.bidinfo

import android.view.View
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoEmptyViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.BidInfoViewHolder

interface BindInfoAdapterTypeFactory {

    fun type(model:BidInfoEmptyViewModel):Int

    fun type(model:BidInfoItemViewModel):Int

    fun holder(type:Int,view: View):BidInfoViewHolder<*>
}