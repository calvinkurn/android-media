package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.graphics.ColorFilter
import android.graphics.ColorMatrixColorFilter
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.databinding.FilterCategoryDetailLevelOneViewHolderBinding
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.utils.view.binding.viewBinding

internal class FilterCategoryLevelOneViewHolder(
        view: View,
        private val callback: HeaderViewHolderCallback
) : RecyclerView.ViewHolder(view) {

    companion object {
        private val GRAYSCALE_MATRIX : FloatArray = floatArrayOf(
                0.3f, 0.59f, 0.11f, 0f, 0f,
                0.3f, 0.59f, 0.11f, 0f, 0f,
                0.3f, 0.59f, 0.11f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
        )
    }

    private val colorFilter: ColorFilter = ColorMatrixColorFilter(GRAYSCALE_MATRIX)
    private var binding: FilterCategoryDetailLevelOneViewHolderBinding? by viewBinding()

    init {
        binding?.filterCategoryDetailHeaderContainer?.setOnClickListener {
            callback.onHeaderItemClick(adapterPosition)
        }
    }

    fun bind(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel) {
        val binding = binding ?: return

        binding.filterCategoryIcon.loadImage(filterCategoryLevelOneViewModel.option.iconUrl)
        binding.filterCategoryName.text = filterCategoryLevelOneViewModel.option.name

        bindSelection(filterCategoryLevelOneViewModel)
    }

    private fun bindSelection(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel) {
        if (filterCategoryLevelOneViewModel.isSelected) bindAsSelected()
        else bindAsUnSelected()
    }

    private fun getColor(@ColorRes colorId: Int): Int {
        return ResourcesCompat.getColor(itemView.resources, colorId, null)
    }

    private fun bindAsSelected() {
        val binding = binding ?: return

        binding.filterCategoryIcon.clearColorFilter()
        binding.filterCategoryName.setTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
        binding.root.setBackgroundColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    private fun bindAsUnSelected() {
        val binding = binding ?: return

        binding.filterCategoryIcon.colorFilter = colorFilter
        binding.filterCategoryName.setTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
        binding.root.setBackgroundColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN50))
    }

    internal interface HeaderViewHolderCallback {
        fun onHeaderItemClick(position: Int)
    }
}