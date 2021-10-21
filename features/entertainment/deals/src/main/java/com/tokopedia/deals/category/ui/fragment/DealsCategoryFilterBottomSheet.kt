package com.tokopedia.deals.category.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.deals.R
import com.tokopedia.deals.category.listener.DealsCategoryFilterBottomSheetListener
import com.tokopedia.deals.common.listener.DealsChipListener
import com.tokopedia.deals.common.ui.adapter.DealsChipsAdapter
import com.tokopedia.deals.common.ui.dataview.ChipDataView
import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView
import com.tokopedia.deals.databinding.LayoutDealsCategoryFilterBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class DealsCategoryFilterBottomSheet(private val listener: DealsCategoryFilterBottomSheetListener) :
        BottomSheetUnify(), DealsChipListener {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private val chipsAdapter = DealsChipsAdapter(this)
    private var chips: DealsChipsDataView? = null
    private lateinit var binding :  LayoutDealsCategoryFilterBottomSheetBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = LayoutDealsCategoryFilterBottomSheetBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
        setTitle(getString(R.string.deals_category_filter_bottom_sheet_title))
        setAction(getString(R.string.deals_category_filter_bottom_sheet_action_label)) {
            chipsAdapter.chips = chipsAdapter.chips.map { it.copy(isSelected = false) }.toMutableList()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lstDealsCategoryFilter.apply {
            adapter = chipsAdapter
            layoutManager = ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
        }
        binding.btnDealsCategoryFilterApply.setOnClickListener {
            chips?.let { chips ->
                listener.onFilterApplied(chips.copy(chipList = chipsAdapter.chips))
            }
            dismiss()
        }
    }

    override fun onChipClicked(itemView: View, chip: ChipDataView, position: Int) {
        chipsAdapter.chips[position] = chip.copy(isSelected = !chip.isSelected)
        chipsAdapter.notifyItemChanged(position)
    }

    fun showCategories(chips: DealsChipsDataView) {
        this.chips = chips
        chipsAdapter.chips = chips.chipList.toMutableList()
    }
}