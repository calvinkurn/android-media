package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.filter.R
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.filter_category_detail_level_one_view_holder.view.*

internal class FilterCategoryLevelOneScrollViewAdapter(
        private val filterCategoryDetailLevelOneLinearLayout: LinearLayout,
        private val callback: Callback
) {

    private val filterCategoryLevelOneViewModelList = mutableListOf<FilterCategoryLevelOneViewModel>()
    private val headerItemList = mutableListOf<HeaderItem>()

    fun setList(filterCategoryLevelOneViewModelList: List<FilterCategoryLevelOneViewModel>) {
        init()

        this.filterCategoryLevelOneViewModelList.addAll(filterCategoryLevelOneViewModelList)

        processList()
    }

    private fun init() {
        filterCategoryLevelOneViewModelList.clear()
        headerItemList.clear()
        filterCategoryDetailLevelOneLinearLayout.removeAllViews()
    }

    private fun processList() {
        filterCategoryLevelOneViewModelList.forEach {
            val headerItem = createView(it)

            filterCategoryDetailLevelOneLinearLayout.addView(headerItem)
            headerItemList.add(headerItem)
        }
    }

    private fun createView(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel): HeaderItem {
        return HeaderItem(filterCategoryDetailLevelOneLinearLayout.context).also {
            it.bind(filterCategoryLevelOneViewModel)
        }
    }

    fun scrollToSelected(scrollView: HorizontalScrollView?) {
        if (scrollView == null) return

        val selectedIndex = filterCategoryLevelOneViewModelList.indexOfFirst { it.isSelected }
        if (isPositionInvalid(selectedIndex)) return

        val view = headerItemList[selectedIndex]
        scrollView.scrollTo(view.left, 0)
    }

    fun notifyItemChanged(position: Int) {
        if (isPositionInvalid(position)) return

        val data = filterCategoryLevelOneViewModelList[position]
        val view = headerItemList[position]

        view.bindSelection(data)
    }

    private fun isPositionInvalid(position: Int) =
            position < 0 || position >= filterCategoryLevelOneViewModelList.size || position >= headerItemList.size

    inner class HeaderItem @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int = 0
    ): LinearLayout(context, attrs, defStyle) {

        private val itemView =
                View.inflate(filterCategoryDetailLevelOneLinearLayout.context, R.layout.filter_category_detail_level_one_view_holder, this)

        fun bind(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel) {
            itemView.filterCategoryDetailHeaderContainer?.setOnClickListener {
                callback.onHeaderItemClick(filterCategoryLevelOneViewModel)
            }

            itemView.filterCategoryIcon?.loadImage(filterCategoryLevelOneViewModel.option.iconUrl)
            itemView.filterCategoryName?.text = filterCategoryLevelOneViewModel.option.name

            bindSelection(filterCategoryLevelOneViewModel)
        }

        fun bindSelection(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel) {
            if (filterCategoryLevelOneViewModel.isSelected) bindAsSelected()
            else bindAsUnSelected()
        }

        private fun bindAsSelected() {
            val selectedTextColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            itemView.filterCategoryName?.setTextColor(selectedTextColor)
            itemView.filterCategorySelectedIndicator?.visibility = View.VISIBLE
        }

        private fun bindAsUnSelected() {
            val selectedTextColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            itemView.filterCategoryName?.setTextColor(selectedTextColor)
            itemView.filterCategorySelectedIndicator?.visibility = View.GONE
        }
    }

    internal interface Callback {
        fun onHeaderItemClick(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel)
    }
}