package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.filter.common.helper.createFilterDividerItemDecoration
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.filter_category_detail_level_two_view_holder.view.*

internal class FilterCategoryLevelTwoAdapter(
        private val callback: FilterCategoryDetailCallback
): RecyclerView.Adapter<FilterCategoryLevelTwoAdapter.FilterCategoryLevelTwoViewHolder>() {

    private val filterCategoryLevelTwoViewModelList = mutableListOf<FilterCategoryLevelTwoViewModel>()

    fun setList(filterCategoryLevelTwoViewModelList: List<FilterCategoryLevelTwoViewModel>) {
        val previousSize = this.filterCategoryLevelTwoViewModelList.size
        this.filterCategoryLevelTwoViewModelList.clear()
        notifyItemRangeRemoved(0, previousSize)

        this.filterCategoryLevelTwoViewModelList.addAll(filterCategoryLevelTwoViewModelList)
        notifyItemRangeInserted(0, filterCategoryLevelTwoViewModelList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryLevelTwoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_category_detail_level_two_view_holder, parent, false)

        return FilterCategoryLevelTwoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterCategoryLevelTwoViewModelList.size
    }

    override fun onBindViewHolder(holder: FilterCategoryLevelTwoViewHolder, position: Int) {
        holder.bind(filterCategoryLevelTwoViewModelList[position])
    }

    inner class FilterCategoryLevelTwoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            itemView.filterCategoryDetailLevelTwoTitle?.text = filterCategoryLevelTwoViewModel.levelTwoCategory.name
            itemView.filterCategoryDetailLevelTwoFoldIcon?.showWithCondition(filterCategoryLevelTwoViewModel.isExpandable)
            itemView.filterCategoryDetailLevelTwoRadioButton?.showWithCondition(!filterCategoryLevelTwoViewModel.isExpandable)

            val shouldShowLevelThreeCategory = filterCategoryLevelTwoViewModel.isExpandable && filterCategoryLevelTwoViewModel.isSelectedOrExpanded
            itemView.filterCategoryDetailLevelThreeRecyclerView?.showWithCondition(shouldShowLevelThreeCategory)

            if (filterCategoryLevelTwoViewModel.isExpandable) {
                bindContainerExpandable(filterCategoryLevelTwoViewModel)
                bindFoldableIcon(filterCategoryLevelTwoViewModel)
                bindLevelThreeCategoryRecyclerView(filterCategoryLevelTwoViewModel)
            }
            else {
                bindContainerCheckBox()
                bindCheckbox(filterCategoryLevelTwoViewModel)
            }
        }

        private fun bindContainerExpandable(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            itemView.filterCategoryDetailLevelTwoContainer?.setOnClickListener {
                filterCategoryLevelTwoViewModel.isSelectedOrExpanded = !filterCategoryLevelTwoViewModel.isSelectedOrExpanded
                bindFoldableIcon(filterCategoryLevelTwoViewModel)
                itemView.filterCategoryDetailLevelThreeRecyclerView?.showWithCondition(filterCategoryLevelTwoViewModel.isSelectedOrExpanded)
            }
        }

        private fun bindFoldableIcon(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            val foldImageDrawable = getFoldImageDrawable(filterCategoryLevelTwoViewModel.isSelectedOrExpanded)
            itemView.filterCategoryDetailLevelTwoFoldIcon?.setImageDrawable(foldImageDrawable)
        }

        private fun getFoldImageDrawable(isExpanded: Boolean): Drawable? {
            return if (isExpanded) ContextCompat.getDrawable(itemView.context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_gray_24)
            else ContextCompat.getDrawable(itemView.context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_gray_24)
        }

        private fun bindLevelThreeCategoryRecyclerView(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            val itemDecoration = createFilterDividerItemDecoration(itemView.context, layoutManager.orientation, 0)

            itemView.filterCategoryDetailLevelThreeRecyclerView?.adapter = createLevelThreeCategoryAdapter(filterCategoryLevelTwoViewModel)
            itemView.filterCategoryDetailLevelThreeRecyclerView?.layoutManager = layoutManager
            itemView.filterCategoryDetailLevelThreeRecyclerView?.addItemDecorationIfNotExists(itemDecoration)
        }

        private fun createLevelThreeCategoryAdapter(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel): FilterCategoryLevelThreeAdapter {
            return FilterCategoryLevelThreeAdapter(filterCategoryLevelTwoViewModel.levelThreeCategoryViewModelList, callback)
        }

        private fun bindContainerCheckBox() {
            itemView.filterCategoryDetailLevelTwoContainer?.setOnClickListener {
                itemView.filterCategoryDetailLevelTwoRadioButton?.isChecked = itemView.filterCategoryDetailLevelTwoRadioButton?.isChecked != true
            }
        }

        private fun bindCheckbox(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            itemView.filterCategoryDetailLevelTwoRadioButton?.setOnCheckedChangeListener(null)
            itemView.filterCategoryDetailLevelTwoRadioButton?.isChecked = filterCategoryLevelTwoViewModel.isSelectedOrExpanded
            itemView.filterCategoryDetailLevelTwoRadioButton?.setOnCheckedChangeListener { _, isChecked ->
                callback.onLevelTwoCategoryClicked(filterCategoryLevelTwoViewModel, isChecked)
            }
        }
    }
}