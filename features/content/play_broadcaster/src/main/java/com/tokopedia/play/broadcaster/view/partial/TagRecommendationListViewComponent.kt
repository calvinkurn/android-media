package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.itemdecoration.TagItemDecoration
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationListViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val rvTagsRecommendation: RecyclerView = rootView as RecyclerView

    private val adapter = TagRecommendationListAdapter()

    init {
        rvTagsRecommendation.adapter = adapter
        rvTagsRecommendation.addItemDecoration(TagItemDecoration(rvTagsRecommendation.context))
    }

    fun setTags(tags: List<String>) {
        adapter.setItemsAndAnimateChanges(tags)
    }
}