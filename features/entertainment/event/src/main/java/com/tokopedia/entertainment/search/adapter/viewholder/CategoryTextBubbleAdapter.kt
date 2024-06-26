package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntSearchCategoryTextItemBinding
import com.tokopedia.entertainment.search.analytics.EventCategoryPageTracking
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.ChipsUnify

class CategoryTextBubbleAdapter(val onClicked: ((String) -> Unit)? ): RecyclerView.Adapter<CategoryTextBubbleAdapter.CategoryTextBubbleViewHolder>() {

    data class CategoryTextBubble(
            val id: String,
            val category: String
    )

    var listCategory: List<CategoryTextBubble> = listOf()
    var hashSet: HashSet<String> = hashSetOf()
    private val FIRST_ITEM = 100
    private val LAST_ITEM = 101

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryTextBubbleViewHolder {
        val viewholder = CategoryTextBubbleViewHolder(EntSearchCategoryTextItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), onClicked, hashSet)

        //Manually set first and last item margin
        // 18dp = 47
        // 8dp = 21
        if (viewType == FIRST_ITEM) {
            viewholder.binding.root.setMargin(parent.context.resources.getDimensionPixelSize(R.dimen.dimen_dp_18),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0))
        } else if (viewType == LAST_ITEM) {
            viewholder.binding.root.setMargin(parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                    parent.context.resources.getDimensionPixelSize(R.dimen.dimen_dp_18),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0))
        }
        return viewholder
    }

    override fun getItemViewType(position: Int): Int {
        if (!listCategory.isNullOrEmpty()) {
            if (position == 0) {
                return FIRST_ITEM
            } else if (position == listCategory.size - 1) {
                return LAST_ITEM
            } else return 0
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = listCategory.size

    override fun onBindViewHolder(holder: CategoryTextBubbleViewHolder, position: Int) {
        holder.bind(listCategory[position])
    }

    class CategoryTextBubbleViewHolder(val binding: EntSearchCategoryTextItemBinding, val onClicked: ((String) -> Unit)?, val hashSet: HashSet<String>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryTextBubble) {
            var clicked = false
            with(binding) {
                categoryChipEvent.chipText = category.category
                categoryChipEvent.setOnClickListener {
                    clicked = setClicked(categoryChipEvent, clicked)
                    EventCategoryPageTracking.getInstance().onClickCategoryBubble(category)
                    if (onClicked != null) {
                        onClicked.invoke(category.id)
                    }
                }
                if (hashSet.contains(category.id)) {
                    clicked = setClicked(categoryChipEvent, clicked)
                }
            }
        }
        private fun setClicked(chipsUnify: ChipsUnify, clicked: Boolean): Boolean {
            if (clicked) {
                chipsUnify.chipType = ChipsUnify.TYPE_NORMAL
                return false
            } else {
                chipsUnify.chipType = ChipsUnify.TYPE_SELECTED
                return true
            }
        }
    }
}
