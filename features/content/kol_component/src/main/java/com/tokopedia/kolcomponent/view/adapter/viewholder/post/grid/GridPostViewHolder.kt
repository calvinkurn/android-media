package com.tokopedia.kolcomponent.view.adapter.viewholder.post.grid

import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.kolcomponent.view.viewmodel.post.grid.GridPostViewModel

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