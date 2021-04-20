package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
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
    }

    override fun onTagClicked(tag: PlayTagUiModel) {
        listener.onTagClicked(this, tag.tag)
    }

    fun setTags(tags: List<PlayTagUiModel>) {
        adapter.setItemsAndAnimateChanges(tags)
    }

    companion object {

        private const val TAG_PER_ROW = 7
    }

    interface Listener {

        fun onTagClicked(view: TagRecommendationListViewComponent, tag: String)
    }
}