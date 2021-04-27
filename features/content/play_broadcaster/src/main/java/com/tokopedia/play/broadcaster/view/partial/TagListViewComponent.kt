package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tokopedia.play.broadcaster.ui.itemdecoration.TagItemDecoration
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 18/02/21
 */
class TagListViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes), TagRecommendationViewHolder.Listener {

    private val rvTagsRecommendation: RecyclerView = rootView as RecyclerView

    private val adapter = TagRecommendationListAdapter(this)

    init {
        val layoutManager = FlexboxLayoutManager(rvTagsRecommendation.context);
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        rvTagsRecommendation.layoutManager = layoutManager
        rvTagsRecommendation.adapter = adapter
        rvTagsRecommendation.addItemDecoration(TagItemDecoration(rvTagsRecommendation.context))
    }

    override fun onTagClicked(tag: PlayTagUiModel) {
        listener.onTagClicked(this, tag.tag)
    }

    fun setTags(tags: List<PlayTagUiModel>) {
        adapter.setItemsAndAnimateChanges(tags)
    }

    interface Listener {

        fun onTagClicked(view: TagListViewComponent, tag: String)
    }
}