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
import com.tokopedia.filter.common.helper.createFilterChildDividerItemDecoration
import com.tokopedia.filter.databinding.FilterCategoryDetailLevelTwoViewHolderBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.viewBinding

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

        private val binding: FilterCategoryDetailLevelTwoViewHolderBinding? by viewBinding()

        fun bind(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            val binding = binding ?: return

            binding.filterCategoryDetailLevelTwoTitle.text = filterCategoryLevelTwoViewModel.levelTwoCategory.name
            binding.filterCategoryDetailLevelTwoFoldIcon.showWithCondition(filterCategoryLevelTwoViewModel.isExpandable)
            binding.filterCategoryDetailLevelTwoRadioButton.showWithCondition(!filterCategoryLevelTwoViewModel.isExpandable)

            val shouldShowLevelThreeCategory = filterCategoryLevelTwoViewModel.isExpandable && filterCategoryLevelTwoViewModel.isSelectedOrExpanded
            binding.filterCategoryDetailLevelThreeRecyclerView.showWithCondition(shouldShowLevelThreeCategory)

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
            val binding = binding ?: return
            binding.filterCategoryDetailLevelTwoContainer.setOnClickListener {
                filterCategoryLevelTwoViewModel.isSelectedOrExpanded = !filterCategoryLevelTwoViewModel.isSelectedOrExpanded
                bindFoldableIcon(filterCategoryLevelTwoViewModel)
                binding.filterCategoryDetailLevelThreeRecyclerView.showWithCondition(filterCategoryLevelTwoViewModel.isSelectedOrExpanded)
            }
        }

        private fun bindFoldableIcon(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            val foldImageDrawable = getFoldImageDrawable(filterCategoryLevelTwoViewModel.isSelectedOrExpanded)
            binding?.filterCategoryDetailLevelTwoFoldIcon?.setImageDrawable(foldImageDrawable)
        }

        private fun getFoldImageDrawable(isExpanded: Boolean): Drawable? {
            return if (isExpanded) ContextCompat.getDrawable(itemView.context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_gray_24)
            else ContextCompat.getDrawable(itemView.context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_gray_24)
        }

        private fun bindLevelThreeCategoryRecyclerView(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            binding?.filterCategoryDetailLevelThreeRecyclerView?.let {
                val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
                val itemDecoration = createFilterChildDividerItemDecoration(itemView.context, layoutManager.orientation, 0)

                it.adapter = createLevelThreeCategoryAdapter(filterCategoryLevelTwoViewModel)
                it.layoutManager = layoutManager
                it.addItemDecorationIfNotExists(itemDecoration)
            }
        }

        private fun createLevelThreeCategoryAdapter(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel): FilterCategoryLevelThreeAdapter {
            return FilterCategoryLevelThreeAdapter(filterCategoryLevelTwoViewModel.levelThreeCategoryViewModelList, callback)
        }

        private fun bindContainerCheckBox() {
            val binding = binding ?: return

            binding.filterCategoryDetailLevelTwoContainer.setOnClickListener {
                binding.filterCategoryDetailLevelTwoRadioButton.isChecked = binding.filterCategoryDetailLevelTwoRadioButton.isChecked != true
            }
        }

        private fun bindCheckbox(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
            val binding = binding ?: return
            binding.filterCategoryDetailLevelTwoRadioButton.setOnCheckedChangeListener(null)
            binding.filterCategoryDetailLevelTwoRadioButton.isChecked = filterCategoryLevelTwoViewModel.isSelectedOrExpanded
            binding.filterCategoryDetailLevelTwoRadioButton.setOnCheckedChangeListener { _, _ ->
                callback.onLevelTwoCategoryClicked(filterCategoryLevelTwoViewModel)
            }
        }
    }
}