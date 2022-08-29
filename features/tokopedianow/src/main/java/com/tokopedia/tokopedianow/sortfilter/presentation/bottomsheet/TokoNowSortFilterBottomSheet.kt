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
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder.ChipListener
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

class TokoNowSortFilterBottomSheet : BottomSheetUnify(),
    SortFilterViewHolderListener,
    ChipListener {

    companion object {
        const val LAST_BOUGHT = 1
        const val FREQUENTLY_BOUGHT = 2

        private val TAG = TokoNowSortFilterBottomSheet::class.simpleName

        fun newInstance(): TokoNowSortFilterBottomSheet {
            return TokoNowSortFilterBottomSheet()
        }
    }

    var sortValue: Int = FREQUENTLY_BOUGHT
    var sortFilterItems: List<Visitable<*>> = listOf()

    private var binding by autoClearedNullable<BottomsheetTokopedianowSortFilterBinding>()

    private var rvSort: RecyclerView? = null
    private var btnApplyFilter: UnifyButton? = null

    private val adapter by lazy {
        SortFilterAdapter(
            SortFilterAdapterTypeFactory(
                sortFilterListener = this,
                chipListener = this
            ),
            SortFilterDiffer()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBtnFilter()
    }

    override fun onClickSortItem(isChecked: Boolean, position: Int, value: Int) {
        val newItemList = mutableListOf<SortFilterUiModel>()
        sortFilterItems.filterIsInstance<SortFilterUiModel>().forEachIndexed { index, model ->
            newItemList.add(model.copy(isChecked = index == position))
        }
        adapter.submitList(newItemList)
        sortValue = value
    }

    override fun onClickChipItem(chipUiModel: TokoNowChipUiModel) {

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initView() {
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        setupItemView()
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
        adapter.submitList(sortFilterItems)
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