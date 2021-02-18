package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.play.broadcaster.ui.itemdecoration.TagItemDecoration
import com.tokopedia.play.broadcaster.ui.viewholder.TagAddedViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TagAddedListAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 18/02/21
 */
class TagAddedListViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes), TagAddedViewHolder.Listener {

    private val rvTagsAdded: RecyclerView = rootView as RecyclerView

    private val adapter = TagAddedListAdapter(this)

    init {
        rvTagsAdded.layoutManager = FlexboxLayoutManager(
                rvTagsAdded.context,
                FlexDirection.ROW,
                FlexWrap.WRAP
        ).apply { alignItems = AlignItems.FLEX_START }
        rvTagsAdded.adapter = adapter
        rvTagsAdded.addItemDecoration(TagItemDecoration(rvTagsAdded.context))
    }

    override fun onTagRemoved(tag: String) {
        listener.onTagRemoved(this, tag)
    }

    fun setTags(tags: List<String>) {
        adapter.setItemsAndAnimateChanges(tags)
    }

    interface Listener {

        fun onTagRemoved(view: TagAddedListViewComponent, tag: String)
    }
}