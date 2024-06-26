package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reading.di.DaggerReadReviewComponent
import com.tokopedia.review.feature.reading.di.ReadReviewComponent
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewSortFilterViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import javax.inject.Inject

class ReadReviewFilterBottomSheet : BottomSheetUnify(), HasComponent<ReadReviewComponent> {

    companion object {
        const val TAG = "ReadReviewFilterBottomSheet Tag"
        fun newInstance(
            title: String,
            filterList: ArrayList<ListItemUnify>,
            readReviewFilterBottomSheetListener: ReadReviewFilterBottomSheetListener,
            sortFilterBottomSheetType: SortFilterBottomSheetType,
            selectedFilter: Set<String> = setOf(),
            selectedSort: String = "",
            index: Int
        ): ReadReviewFilterBottomSheet {
            return ReadReviewFilterBottomSheet().apply {
                setTitle(title)
                this.filterData = filterList
                this.listener = readReviewFilterBottomSheetListener
                this.sortFilterBottomSheetType = sortFilterBottomSheetType
                this.previouslySelectedFilter = selectedFilter
                this.previouslySelectedSortOption = selectedSort
                this.index = index
                clearContentPadding = true
            }
        }
    }

    @Inject
    lateinit var sortFilterViewModel: ReadReviewSortFilterViewModel

    private var listUnify: ListUnify? = null
    private var submitButton: UnifyButton? = null

    private var filterData: ArrayList<ListItemUnify> = arrayListOf()
    private var sortFilterBottomSheetType: SortFilterBottomSheetType? = null
    private var index: Int = 0
    private var previouslySelectedFilter: Set<String> = setOf()
    private var previouslySelectedSortOption: String = ""
    private var listener: ReadReviewFilterBottomSheetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.bottomsheet_read_review_filter, null)
        setChild(view)
        setShowListener {
            sortFilterViewModel.setInitialValues(previouslySelectedFilter, filterData)
            setListUnifyData()
            if (isSortMode()) {
                setSortItemListener()
                setSubmitSortButton()
                listUnify?.onLoadFinish {
                    configSortData()
                }
            } else {
                observeSubmitButtonState()
                observeResetButtonState()
                setFilterItemListener()
                setSubmitFilterButton()
                listUnify?.onLoadFinish {
                    configFilterData()
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getComponent(): ReadReviewComponent? {
        return activity?.run {
            DaggerReadReviewComponent.builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .build()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
    }

    private fun bindViews(view: View) {
        listUnify = view.findViewById(R.id.read_review_filter_list)
        submitButton = view.findViewById(R.id.read_review_submit_filter)
    }

    private fun observeSubmitButtonState() {
        sortFilterViewModel.buttonState.observe(viewLifecycleOwner, {
            submitButton?.isEnabled = it
        })
    }

    private fun observeResetButtonState() {
        sortFilterViewModel.resetButtonState.observe(viewLifecycleOwner, {
            if(it) {
                bottomSheetAction.apply {
                    text = getString(R.string.review_reading_reset_filter)
                    setOnClickListener { resetFilters() }
                    show()
                }
            } else {
                bottomSheetAction.text = ""
            }
        })
    }

    private fun setListUnifyData() {
        listUnify?.setData(getFilterData())
    }

    private fun setSortItemListener() {
        listUnify?.setOnItemClickListener { _, _, position, _ ->
            val selectedSortOption = (listUnify?.adapter?.getItem(position) as? ListItemUnify)
            selectedSortOption?.listRightRadiobtn?.toggle()
        }
    }

    private fun setFilterItemListener() {
        listUnify?.setOnItemClickListener { _, _, position, _ ->
            (listUnify?.adapter?.getItem(position) as? ListItemUnify)?.let {
                it.listRightCheckbox?.toggle()
            }
        }
    }

    private fun configSortData() {
        getFilterData().forEachIndexed { index, listItemUnify ->
            if (listItemUnify.listTitleText == previouslySelectedSortOption && previouslySelectedSortOption.isNotBlank()) {
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked =
                    true
            }
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                onSortCheckChange(isChecked, listItemUnify, index)
            }
        }
    }

    private fun configFilterData() {
        getFilterData().forEachIndexed { index, listItemUnify ->
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.setOnCheckedChangeListener { _, isChecked ->
                onFilterCheckChange(isChecked, listItemUnify)
            }
            val originalFilters = sortFilterViewModel.getOriginalFilters()
            if (originalFilters.contains(listItemUnify.listTitleText) && originalFilters.isNotEmpty()) {
                sortFilterViewModel.updateSelectedFilter(listItemUnify)
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked = true
            }
        }
    }

    private fun setSubmitSortButton() {
        submitButton?.hide()
    }

    private fun setSubmitFilterButton() {
        submitButton?.setOnClickListener {
            dismiss()
            listener?.onFilterSubmitted(
                this.bottomSheetTitle.text.toString(),
                sortFilterViewModel.getSelectedFilters(),
                sortFilterBottomSheetType
                    ?: SortFilterBottomSheetType.RatingFilterBottomSheet,
                index
            )
        }
    }

    private fun clearOtherItems(position: Int) {
        getFilterData().forEachIndexed { index, _ ->
            if (position != index) {
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked =
                    false
            }
        }
    }

    private fun resetFilters() {
        getFilterData().forEachIndexed { index, _ ->
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked =
                false
        }
        clearAllFilters()
    }

    private fun isSortMode(): Boolean {
        return sortFilterBottomSheetType is SortFilterBottomSheetType.SortBottomSheet
    }

    private fun getFilterData(): ArrayList<ListItemUnify> {
        return sortFilterViewModel.getFilterData()
    }

    private fun clearAllFilters() {
        sortFilterViewModel.clearAllFilters()
    }

    private fun onFilterCheckChange(isChecked: Boolean, itemUnify: ListItemUnify) {
        sortFilterViewModel.onFilterCheckChange(isChecked, itemUnify)
    }

    private fun onSortCheckChange(
        isChecked: Boolean,
        selectedSortOption: ListItemUnify,
        position: Int
    ) {
        if (isChecked) {
            clearOtherItems(position)
            dismiss()
            listener?.onSortSubmitted(selectedSortOption)
        }
    }
}