package com.tokopedia.tokopedianow.categoryfilter.presentation.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categoryfilter.di.component.DaggerCategoryFilterComponent
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.EXTRA_SELECTED_FILTER
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.PARAM_WAREHOUSE_ID
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.categoryfilter.presentation.view.CategoryFilterChipView
import com.tokopedia.tokopedianow.categoryfilter.presentation.viewmodel.TokoNowCategoryFilterViewModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class TokoNowCategoryFilterBottomSheet : BottomSheetUnify() {

    companion object {
        fun newInstance(
            warehouseId: String,
            selectedFilterId: SelectedSortFilter?
        ): TokoNowCategoryFilterBottomSheet {
            return TokoNowCategoryFilterBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PARAM_WAREHOUSE_ID, warehouseId)
                    putParcelable(EXTRA_SELECTED_FILTER, selectedFilterId)
                }
            }
        }

        private val TAG: String = TokoNowCategoryFilterBottomSheet::class.java.simpleName
    }

    @Inject
    lateinit var viewModel:  TokoNowCategoryFilterViewModel

    private var accordionFilter: AccordionUnify? = null
    private var btnApply: UnifyButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemView = View.inflate(context,
            R.layout.bottomsheet_tokopedianow_category_filter, null)
        val title = context?.getString(R.string.tokopedianow_category_filter_title).orEmpty()
        clearContentPadding = true
        setChild(itemView)
        setTitle(title)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupApplyButton()
        observeLiveData()
        setSelectedFilter()
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
        accordionFilter = view?.findViewById(R.id.accordion_filter)
        btnApply = view?.findViewById(R.id.btn_apply)
    }

    private fun setupApplyButton() {
        btnApply?.setOnClickListener {
            viewModel.applyFilter()
        }
    }

    private fun observeLiveData() {
        observe(viewModel.categoryList) {
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
            }
        }

        observe(viewModel.applyFilter) {
            val intent = Intent().apply {
                putExtra(EXTRA_SELECTED_FILTER, it)
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            dismiss()
        }
    }

    private fun createFilterChipView(category: CategoryFilterChip): CategoryFilterChipView {
        val categoryFilterChipView = CategoryFilterChipView(requireContext())
        categoryFilterChipView.setOnClickListener { viewModel.setSelectedFilter(it) }
        categoryFilterChipView.submitList(category.childList)
        return categoryFilterChipView
    }

    private fun initInjector() {
        DaggerCategoryFilterComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setSelectedFilter() {
        val selectedFilter = arguments?.getParcelable<SelectedSortFilter>(EXTRA_SELECTED_FILTER)
        viewModel.setSelectedFilterIds(selectedFilter)
    }

    private fun getCategoryList() {
        val warehouseId = arguments?.getString(PARAM_WAREHOUSE_ID).orEmpty()
        viewModel.getCategoryList(warehouseId)
    }
}