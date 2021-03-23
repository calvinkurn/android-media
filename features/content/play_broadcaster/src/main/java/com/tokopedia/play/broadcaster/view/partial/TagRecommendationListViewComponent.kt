package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.itemdecoration.TagItemDecoration
import com.tokopedia.play.broadcaster.ui.model.tag.TagRecommendationRowUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationRowListAdapter
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

    private val adapter = TagRecommendationRowListAdapter(this)

    init {
        rvTagsRecommendation.adapter = adapter
    }

    override fun onTagClicked(tag: String) {
        listener.onTagClicked(this, tag)
    }

    fun setTags(tags: List<String>) {
        val tagsRow = tags.chunked(TAG_PER_ROW) { TagRecommendationRowUiModel(it.toList()) }
        adapter.setItemsAndAnimateChanges(tagsRow)
    }

    companion object {

        private const val TAG_PER_ROW = 7
    }

    interface Listener {

        fun onTagClicked(view: TagRecommendationListViewComponent, tag: String)
    }
}