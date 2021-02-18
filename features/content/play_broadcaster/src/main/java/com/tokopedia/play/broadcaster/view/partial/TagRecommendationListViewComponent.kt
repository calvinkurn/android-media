package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.itemdecoration.TagItemDecoration
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationListViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes), TagRecommendationViewHolder.Listener {

    private val rvTagsRecommendation: RecyclerView = rootView as RecyclerView

    private val adapter = TagRecommendationListAdapter(this)

    init {
        rvTagsRecommendation.adapter = adapter
        rvTagsRecommendation.addItemDecoration(TagItemDecoration(rvTagsRecommendation.context))
    }

    override fun onTagClicked(tag: String) {
        listener.onTagClicked(this, tag)
    }

    fun setTags(tags: List<String>) {
        adapter.setItemsAndAnimateChanges(tags)
    }

    interface Listener {

        fun onTagClicked(view: TagRecommendationListViewComponent, tag: String)
    }
}