package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel

/**
 * @author by milhamj on 07/12/18
 */
class GridPostViewHolder : BasePostViewHolder<GridPostViewModel>() {
    override var layoutRes: Int = R.layout.item_post_grid

    override fun bind(element: GridPostViewModel) {
        val adapter = GridPostAdapter(element)
        adapter.notifyDataSetChanged()
    }
}