package com.tokopedia.vouchercreation.product.list.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcCategoryFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.CategoryListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.CategorySelection

class CategoryBottomSheet : BottomSheetUnify() {

    interface OnApplyButtonClickListener {
        fun onApplyCategoryFilter(selectedCategories: List<CategorySelection>)
    }

    companion object {

        private const val CATEGORY_SELECTIONS = "CATEGORY_SELECTIONS"

        @JvmStatic
        fun createInstance(categorySelections: List<CategorySelection>,
                           clickListener: OnApplyButtonClickListener): CategoryBottomSheet {
            return CategoryBottomSheet().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_SELECTIONS, ArrayList(categorySelections))
                }
            }
        }
    }

    private var listAdapter: CategoryListAdapter? = CategoryListAdapter()

    private var binding: BottomsheetMvcCategoryFilterLayoutBinding? = null

    private var clickListener: OnApplyButtonClickListener? = null

    private val categorySelections: ArrayList<CategorySelection> by lazy {
        arguments?.getParcelableArrayList(CATEGORY_SELECTIONS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetMvcCategoryFilterLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.mvc_category))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        listAdapter?.setCategorySelections(categorySelections)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetMvcCategoryFilterLayoutBinding?) {
        binding?.rvCategoryList?.let {
            it.adapter = listAdapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding?.applyButton?.setOnClickListener {
            val selectedCategories = listAdapter?.getSelectedCategories() ?: listOf()
            clickListener?.onApplyCategoryFilter(selectedCategories)
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}