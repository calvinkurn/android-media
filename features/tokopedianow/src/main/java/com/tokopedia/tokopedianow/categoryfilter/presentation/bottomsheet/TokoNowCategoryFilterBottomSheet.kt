package com.tokopedia.tokopedianow.categoryfilter.presentation.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categoryfilter.di.component.DaggerCategoryFilterComponent
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.EXTRA_SELECTED_CATEGORY_FILTER
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.categoryfilter.presentation.view.CategoryFilterChipView
import com.tokopedia.tokopedianow.categoryfilter.presentation.viewmodel.TokoNowCategoryFilterViewModel
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowCategoryFilterBinding
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryFilterBottomSheet : BottomSheetUnify() {

    companion object {
        fun newInstance(selectedFilterId: SelectedSortFilter?): TokoNowCategoryFilterBottomSheet {
            return TokoNowCategoryFilterBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SELECTED_CATEGORY_FILTER, selectedFilterId)
                }
            }
        }

        private val TAG: String = TokoNowCategoryFilterBottomSheet::class.java.simpleName
    }

    @Inject
    lateinit var viewModel: TokoNowCategoryFilterViewModel

    private var binding by autoClearedNullable<BottomsheetTokopedianowCategoryFilterBinding>()

    private var filterChipViewList = mutableListOf<CategoryFilterChipView>()
    private var accordionFilter: AccordionUnify? = null
    private var btnApply: UnifyButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetTokopedianowCategoryFilterBinding.inflate(LayoutInflater.from(context))
        val title = context?.getString(R.string.tokopedianow_category_filter_title).orEmpty()
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(title)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setupActionButton()
        setupApplyButton()
        getCategoryList()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initView() {
        accordionFilter = binding?.accordionFilter
        btnApply = binding?.btnApply
    }

    private fun setupActionButton() {
        setAction(getString(R.string.tokopedianow_text_reset)) {
            filterChipViewList.forEach {
                it.resetAllFilterChip()
            }
            viewModel.clearSelectedFilter()
        }
    }

    private fun enableResetButton() {
        setActionTextColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
    }

    private fun disableResetButton() {
        setActionTextColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
    }

    private fun setActionTextColor(@ColorRes colorId: Int) {
        val textColor = ContextCompat.getColor(requireContext(), colorId)
        bottomSheetAction.setTextColor(textColor)
    }

    private fun setupApplyButton() {
        btnApply?.setOnClickListener {
            viewModel.applyFilter()
        }
    }

    private fun observeLiveData() {
        observe(viewModel.categoryList) {
            if (it is Success) {
                filterChipViewList.clear()
                it.data.forEach { category ->
                    val filterChipView = createFilterChipView(category)

                    val item = AccordionDataUnify(
                        title = category.title,
                        expandableView = filterChipView,
                        isExpanded = true
                    ).apply {
                        borderBottom = false
                    }

                    accordionFilter?.addGroup(item)
                    filterChipViewList.add(filterChipView)
                }
            }
        }

        observe(viewModel.applyFilter) {
            val intent = Intent().apply {
                putExtra(EXTRA_SELECTED_CATEGORY_FILTER, it)
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            dismiss()
        }

        observe(viewModel.selectedFilter) {
            toggleResetButton(it)
        }
    }

    private fun createFilterChipView(category: CategoryFilterChip): CategoryFilterChipView {
        val categoryFilterChipView = CategoryFilterChipView(requireContext())
        categoryFilterChipView.setOnClickListener {
            viewModel.updateSelectedFilter(it)
        }
        categoryFilterChipView.submitList(category.childList)
        return categoryFilterChipView
    }

    private fun initInjector() {
        DaggerCategoryFilterComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun getCategoryList() {
        val selectedFilter = arguments
            ?.getParcelable<SelectedSortFilter>(EXTRA_SELECTED_CATEGORY_FILTER)
        viewModel.getCategoryList(selectedFilter)
    }

    private fun toggleResetButton(selectedFilter: SelectedSortFilter?) {
        if (selectedFilter == null) {
            disableResetButton()
        } else {
            enableResetButton()
        }
    }
}
