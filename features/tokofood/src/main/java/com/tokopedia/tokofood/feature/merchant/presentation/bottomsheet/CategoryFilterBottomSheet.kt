package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.tokofood.databinding.CategoryFilterBottomSheetBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.CategoryFilterAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryFilterListUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryFilterWrapperUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CategoryFilterBottomSheet : BottomSheetUnify(), CategoryFilterAdapter.Listener {

    private var binding by autoClearedNullable<CategoryFilterBottomSheetBinding>()

    private var categoryFilterListener: CategoryFilterListener? = null

    private var categoryFilterList: List<CategoryFilterListUiModel>? = null

    private val categoryFilterAdapter by lazy { CategoryFilterAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CategoryFilterBottomSheetBinding.inflate(LayoutInflater.from(context))
        this.binding = binding
        initializeBottomSheet(binding)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCategoryFilterFromCacheManager()
        setupAdapter()
    }

    override fun onCategoryItemSelected(categoryFilterList: List<CategoryFilterListUiModel>) {
        categoryFilterListener?.onFinishCategoryFilter(categoryFilterList)
        dismiss()
    }

    private fun initializeBottomSheet(viewBinding: CategoryFilterBottomSheetBinding) {
        showCloseIcon = true
        setTitle(getString(com.tokopedia.tokofood.R.string.category_filter_bottom_sheet_title))
        setChild(viewBinding.root)
    }

    private fun setCategoryFilterFromCacheManager() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID)
            )
        }
        val categoryFilterWrapperUiModel = cacheManager?.get(KEY_CATEGORY_FILTER_LIST,
            CategoryFilterWrapperUiModel::class.java) ?: CategoryFilterWrapperUiModel()

        categoryFilterList = categoryFilterWrapperUiModel.categoryFilterListUiModel
    }

    private fun setupAdapter() {
        binding?.rvCategoryFilter?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryFilterAdapter
        }
        categoryFilterAdapter.setCategoryFilterList(categoryFilterList)
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun setCategoryFilterListener(categoryFilterListener: CategoryFilterListener) {
        this.categoryFilterListener = categoryFilterListener
    }

    interface CategoryFilterListener {
        fun onFinishCategoryFilter(categoryFilterList: List<CategoryFilterListUiModel>)
    }

    companion object {

        val TAG: String = CategoryFilterBottomSheet::class.java.simpleName

        const val KEY_CACHE_MANAGER_ID = "key_category_filter_cache_manager_id"
        const val KEY_CATEGORY_FILTER_LIST = "key_category_filter_list"

        fun createInstance(bundle: Bundle): CategoryFilterBottomSheet {
            return CategoryFilterBottomSheet().apply {
                arguments = bundle
            }
        }
    }
}