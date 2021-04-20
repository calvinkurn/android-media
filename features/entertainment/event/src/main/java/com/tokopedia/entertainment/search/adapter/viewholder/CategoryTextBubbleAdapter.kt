package com.tokopedia.entertainment.search.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.analytics.EventCategoryPageTracking
import com.tokopedia.kotlin.extensions.view.setMargin
import kotlinx.android.synthetic.main.ent_search_category_text_item.view.*

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
        val view = CategoryTextBubbleViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.ent_search_category_text_item, parent, false))

        //Manually set first and last item margin
        // 18dp = 47
        // 8dp = 21
        if (viewType == FIRST_ITEM) {
            view.view.setMargin(parent.context.resources.getDimensionPixelSize(R.dimen.dimen_dp_18),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0))
        } else if (viewType == LAST_ITEM) {
            view.view.setMargin(parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                    parent.context.resources.getDimensionPixelSize(R.dimen.dimen_dp_18),
                    parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0))
        }
        return view
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
        val category = listCategory.get(position)
        var clicked = false
        with(holder.view) {
            name_category.text = category.category
            name_category.setOnClickListener {
                clicked = setClicked(this, context, clicked)
                EventCategoryPageTracking.getInstance().onClickCategoryBubble(category)
                if (onClicked != null) {
                    onClicked.invoke(category.id)
                }
            }
            if (hashSet.contains(category.id)) {
                clicked = setClicked(this, context, clicked)
            }
        }
    }

    private fun setClicked(view: View, context: Context, clicked: Boolean): Boolean {
        if (clicked) {
            view.name_category.background = ContextCompat.getDrawable(context, R.drawable.bg_text_category)
            view.name_category.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
            return false
        } else {
            view.name_category.background = ContextCompat.getDrawable(context, R.drawable.bg_text_category_green)
            view.name_category.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            return true
        }
    }

    class CategoryTextBubbleViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}