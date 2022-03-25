package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.databinding.LayoutTagRecommendationBinding
import com.tokopedia.play.broadcaster.ui.itemdecoration.TagItemDecoration
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagPlaceholderUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 18/02/21
 */
class TagListViewComponent(
        container: ViewGroup,
        private val binding: LayoutTagRecommendationBinding,
        private val listener: Listener
) : ViewComponent(container, binding.root.id), TagViewHolder.Listener {

    private val placeholder by lazy(mode = LazyThreadSafetyMode.NONE) {
        List(PLACEHOLDER_COUNT) {
            TagRecommendationListAdapter.Model.Placeholder(
                data = PlayTagPlaceholderUiModel(
                    size = when((0..2).random() % 3) {
                        0 -> PlayTagPlaceholderUiModel.Size.SMALL
                        1 -> PlayTagPlaceholderUiModel.Size.MEDIUM
                        2 -> PlayTagPlaceholderUiModel.Size.LARGE
                        else -> PlayTagPlaceholderUiModel.Size.MEDIUM
                    }
                )
            )
        }
    }

    private val adapter = TagRecommendationListAdapter(this)

    init {
        val layoutManager = FlexboxLayoutManager(binding.rvTagsRecommendation.context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvTagsRecommendation.layoutManager = layoutManager
        binding.rvTagsRecommendation.adapter = adapter
        binding.rvTagsRecommendation.addItemDecoration(TagItemDecoration(binding.rvTagsRecommendation.context))

        binding.localLoadTagError.refreshBtn?.setOnClickListener {
            listener.onTagRefresh(this)
        }
    }

    override fun onTagClicked(tag: PlayTagUiModel) {
        listener.onTagClicked(this, tag)
    }

    fun setTags(tags: List<PlayTagUiModel>) {
        binding.localLoadTagError.hide()
        adapter.setItemsAndAnimateChanges(
            tags.map {
                TagRecommendationListAdapter.Model.Tag(it)
            }
        )
    }

    fun setPlaceholder() {
        binding.localLoadTagError.hide()
        adapter.setItemsAndAnimateChanges(placeholder)
    }

    fun setError() {
        adapter.setItemsAndAnimateChanges(emptyList())
        binding.localLoadTagError.show()
    }

    interface Listener {

        fun onTagClicked(view: TagListViewComponent, tag: PlayTagUiModel)
        fun onTagRefresh(view: TagListViewComponent)
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 9
    }
}