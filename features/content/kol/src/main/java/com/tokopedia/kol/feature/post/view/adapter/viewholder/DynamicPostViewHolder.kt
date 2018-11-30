package com.tokopedia.kol.feature.post.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.post.view.viewmodel.DynamicPostViewModel

/**
 * @author by milhamj on 28/11/18.
 */
class DynamicPostViewHolder(val v: View) : AbstractViewHolder<DynamicPostViewModel>(v) {

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_dynamic_post
    }

    override fun bind(element: DynamicPostViewModel?) {

    }
}
