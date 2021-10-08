package com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowSortFilterBinding
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity.Companion.SORT_VALUE
import com.tokopedia.tokopedianow.sortfilter.presentation.adapter.SortFilterAdapter
import com.tokopedia.tokopedianow.sortfilter.presentation.differ.SortFilterDiffer
import com.tokopedia.tokopedianow.sortfilter.presentation.typefactory.SortFilterAdapterTypeFactory
import com.tokopedia.tokopedianow.sortfilter.presentation.uimodel.SortFilterUiModel
import com.tokopedia.tokopedianow.sortfilter.presentation.viewholder.SortFilterViewHolder.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoNowSortFilterBottomSheet :
    BottomSheetUnify(),
    SortFilterViewHolderListener {

    companion object {
        private val TAG = TokoNowSortFilterBottomSheet::class.simpleName
        const val LAST_BOUGHT = 1
        const val FREQUENTLY_BOUGHT = 2

        fun newInstance(): TokoNowSortFilterBottomSheet {
            return TokoNowSortFilterBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowSortFilterBinding>()

    private var rvSort: RecyclerView? = null
    private var btnApplyFilter: UnifyButton? = null
    private var sortValue: Int = FREQUENTLY_BOUGHT
    private var listTitles: List<SortFilterUiModel> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBtnFilter()
    }

    override fun onClickItem(isChecked: Boolean, position: Int, value: Int) {
        val newItemList = mutableListOf<SortFilterUiModel>()
        listTitles.forEachIndexed { index, model ->
            newItemList.add(model.copy(isChecked = index == position))
        }
        adapter.submitList(newItemList)
        sortValue = value
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun show(fm: FragmentManager, sortValue: Int) {
        show(fm, TAG)
        this.sortValue = sortValue

        listTitles = listOf(
            SortFilterUiModel(
                titleRes = R.string.tokopedianow_sort_filter_item_most_frequently_bought_bottomsheet,
                isChecked = sortValue == FREQUENTLY_BOUGHT,
                isLastItem = false,
                value = FREQUENTLY_BOUGHT
            ),
            SortFilterUiModel(
                titleRes = R.string.tokopedianow_sort_filter_item_last_bought_bottomsheet,
                isChecked = sortValue == LAST_BOUGHT,
                isLastItem = true,
                value = LAST_BOUGHT
            )
        )
    }

    private val adapter by lazy {
        SortFilterAdapter(
            SortFilterAdapterTypeFactory(this),
            SortFilterDiffer()
        )
    }

    private fun initView() {
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        setupItemView()
        setTitle(getString(R.string.tokopedianow_sort_filter_title_bottomsheet))
    }

    private fun setupItemView() {
        binding = BottomsheetTokopedianowSortFilterBinding.inflate(LayoutInflater.from(context))
        rvSort = binding?.rvSortBasedOnBuying
        btnApplyFilter = binding?.btnApplyFilter
        setChild(binding?.root)
    }

    private fun setupRecyclerView() {
        rvSort?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TokoNowSortFilterBottomSheet.adapter
            itemAnimator = null
        }
        adapter.submitList(listTitles)
    }

    private fun setupBtnFilter() {
        btnApplyFilter?.setOnClickListener {
            val intent = Intent()
            intent.putExtra(SORT_VALUE, sortValue)
            activity?.setResult(Activity.RESULT_OK, intent)
            dismiss()
        }
    }
}