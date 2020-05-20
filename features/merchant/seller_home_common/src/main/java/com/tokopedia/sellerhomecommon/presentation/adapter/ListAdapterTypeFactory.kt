package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.View
import com.tokopedia.sellerhomecommon.presentation.model.PostUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.PostViewHolder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created By @ilhamsuaib on 20/05/20
 */

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