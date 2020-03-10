package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.PDPViewHolder

class PdpThankYouPageAdapter(visitables: List<Visitable<*>>,
                             typeFactory: PDPThankYouPageFactory) :
        BaseAdapter<PDPThankYouPageFactory>(typeFactory, visitables) {

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        if (holder.itemViewType == PDPViewHolder.LAYOUT_ID) {
            (holder as PDPViewHolder).clearImage()
        }
    }

}