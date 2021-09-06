package com.tokopedia.tokopedianow.recentpurchase.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.differ.RepurchaseSortFilterOnBuyingDiffer
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterOnBuyingUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseSortFilterOnBuyingViewHolder.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton


class TokoNowRepurchaseSortFilterOnBuyingBottomSheet :
    BottomSheetUnify(),
    RepurchaseSortFilterOnBuyingViewHolderListener {

    companion object {
        private val TAG = TokoNowRepurchaseSortFilterOnBuyingBottomSheet::class.simpleName
        private const val LAST_BOUGHT = 1
        private const val FREQUENTLY_BOUGHT = 2

        fun newInstance(): TokoNowRepurchaseSortFilterOnBuyingBottomSheet {
            return TokoNowRepurchaseSortFilterOnBuyingBottomSheet()
        }
    }

    private var rvSort: RecyclerView? = null
    private var btnApplyFilter: UnifyButton? = null
    private var listener: TokoNowRepurchaseSortFilterOnBuyingBottomSheetListener? = null
    private var sortValue: Int = FREQUENTLY_BOUGHT
    private var listTitles: List<RepurchaseSortFilterOnBuyingUiModel> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupBtnFilter()
    }

    override fun onClickItem(isChecked: Boolean, position: Int, value: Int) {
        val newItemList = mutableListOf<RepurchaseSortFilterOnBuyingUiModel>()
        listTitles.forEachIndexed { index, repurchaseSortFilterOnBuyingUiModel ->
            newItemList.add(repurchaseSortFilterOnBuyingUiModel.copy(isChecked = index == position))
        }
        adapter.submitList(newItemList)
        sortValue = value
    }

    fun show(fm: FragmentManager, sortValue: Int, listener: TokoNowRepurchaseSortFilterOnBuyingBottomSheetListener) {
        show(fm, TAG)
        this.listener = listener
        this.sortValue = sortValue

        listTitles = listOf(
            RepurchaseSortFilterOnBuyingUiModel(
                titleRes = R.string.tokopedianow_repurchase_sort_filter_on_buying_item_most_frequently_bought_bottomsheet,
                isChecked = sortValue == FREQUENTLY_BOUGHT,
                isLastItem = false,
                value = FREQUENTLY_BOUGHT
            ),
            RepurchaseSortFilterOnBuyingUiModel(
                titleRes = R.string.tokopedianow_repurchase_sort_filter_on_buying_item_last_bought_bottomsheet,
                isChecked = sortValue == LAST_BOUGHT,
                isLastItem = true,
                value = LAST_BOUGHT
            )
        )
    }

    private val adapter by lazy {
        RepurchaseSortFilterOnBuyingAdapter(
            RepurchaseSortFilterOnBuyingAdapterTypeFactory(this),
            RepurchaseSortFilterOnBuyingDiffer()
        )
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        isHideable = false
        setupItemView(inflater, container)
        setTitle(getString(R.string.tokopedianow_repurchase_sort_filter_on_buying_title_bottomsheet))
    }

    private fun setupItemView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(R.layout.bottomsheet_tokopedianow_repurchase_sort_filter_on_buying, container)
        rvSort = itemView.findViewById(R.id.rv_sort_based_on_buying)
        btnApplyFilter = itemView.findViewById(R.id.btnApplyFilter)
        setChild(itemView)
    }

    private fun setupRecyclerView() {
        rvSort?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@TokoNowRepurchaseSortFilterOnBuyingBottomSheet.adapter
            itemAnimator = null
        }
        adapter.submitList(listTitles)
    }

    private fun setupBtnFilter() {
        btnApplyFilter?.setOnClickListener {
            listener?.onApplySortFilterOnBuying(sortValue)
            dismiss()
        }
    }

    interface TokoNowRepurchaseSortFilterOnBuyingBottomSheetListener {
        fun onApplySortFilterOnBuying(value: Int)
    }
}