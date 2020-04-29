package com.tokopedia.feedcomponent.view.adapter.posttag

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by yfsx on 22/03/19.
 */
class PostTagAdapter(itemList: List<Visitable<*>>,
                     typeFactory: PostTagTypeFactory)
    : BaseAdapter<PostTagTypeFactory>(typeFactory, itemList) {

    val data: List<Visitable<*>>
        get() = visitables

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}