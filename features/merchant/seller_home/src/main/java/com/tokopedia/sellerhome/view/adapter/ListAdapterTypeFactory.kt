package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.model.PostUiModel
import com.tokopedia.sellerhome.view.viewholder.PostListViewHolder.PostViewHolder

class ListAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(post: PostUiModel): Int {
        return PostViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PostViewHolder.RES_LAYOUT -> PostViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}