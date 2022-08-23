package com.tokopedia.vouchercreation.shop.voucherlist.view.widget.filterbottomsheet

import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcFilterBinding
import com.tokopedia.vouchercreation.shop.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel.*
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.FilterAdapter

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class FilterBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(): FilterBottomSheet = FilterBottomSheet().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }

        const val TAG: String = "FilterBottomSheet"

        fun getMvcFilterItems(context: Context): MutableList<BaseFilterUiModel> {
            return mutableListOf(
                    FilterGroup(context.getString(R.string.mvc_type_of_voucher)),
                    FilterItem(context.getString(R.string.mvc_cashback), FilterBy.CASHBACK),
                    FilterItem(context.getString(R.string.mvc_free_shipping), FilterBy.FREE_SHIPPING),
                    FilterDivider,
                    FilterGroup(context.getString(R.string.mvc_voucher_target)),
                    FilterItem(context.getString(R.string.mvc_public), FilterBy.PUBLIC),
                    FilterItem(context.getString(R.string.mvc_special), FilterBy.SPECIAL)
            )
        }
    }

    private var binding by autoClearedNullable<BottomsheetMvcFilterBinding>()

    private var applyFilter = false
    private var onItemClick: (String) -> Unit = {}
    private var onApplyClick: () -> Unit = {}
    private var onCancelApply: (items: List<BaseFilterUiModel>) -> Unit = {}
    private val filterAdapter by lazy { FilterAdapter(this::onItemFilterClick) }
    private var tmpFilterList = emptyList<BaseFilterUiModel>()

    private var selectedVoucherType: Int? = null
    private var selectedVoucherTarget: List<Int> = listOf()
    private var isSellerCreated = false
    private var isSubsidy = false
    private var isVps = false
    private var chipsFilter:List<ChipsUnify> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            chipsFilter = listOf(chipCashback, chipPublic, chipSpecial, chipSellerCreated, chipSubsidy, chipVps)
            setupView(view)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!applyFilter) {
            onCancelApply(tmpFilterList)
        }
    }

    private fun initBottomSheet() {
        context?.run {
            binding = BottomsheetMvcFilterBinding.inflate(LayoutInflater.from(context))
            setTitle(getString(R.string.mvc_filter))
            setChild(binding?.root)
            showKnob = true
            setAction(getString(R.string.mvc_reset)) {
                resetFilters()
            }
        }
    }

    private fun setupView(view: View) = with(view) {
        binding?.apply {
            rvMcvFilter.layoutManager = LinearLayoutManager(context)
            rvMcvFilter.adapter = filterAdapter
            rvMcvFilter.addItemDecoration(getFilterItemDecoration())

            selectedVoucherType?.run { chipCashback.chipType = ChipsUnify.TYPE_SELECTED }
            if (selectedVoucherTarget.isNotEmpty() && selectedVoucherTarget.first() == VoucherTargetType.PUBLIC) chipPublic.chipType = ChipsUnify.TYPE_SELECTED
            if (selectedVoucherTarget.isNotEmpty() && selectedVoucherTarget.first() == VoucherTargetType.PRIVATE) chipSpecial.chipType = ChipsUnify.TYPE_SELECTED
            if (isSellerCreated) chipSellerCreated.chipType = ChipsUnify.TYPE_SELECTED
            if (isSubsidy) chipSubsidy.chipType = ChipsUnify.TYPE_SELECTED
            if (isVps) chipVps.chipType = ChipsUnify.TYPE_SELECTED

            chipCashback.setOnClickListener {
                chipCashback.chipType = if(chipCashback.chipType == ChipsUnify.TYPE_NORMAL) {
                    selectedVoucherType = VoucherTypeConst.CASHBACK
                    chipFreeShipping.chipType = ChipsUnify.TYPE_NORMAL
                    ChipsUnify.TYPE_SELECTED
                } else {
                    selectedVoucherType = null
                    ChipsUnify.TYPE_NORMAL
                }
            }

            chipFreeShipping.setOnClickListener {
                chipFreeShipping.chipType = if(chipFreeShipping.chipType == ChipsUnify.TYPE_NORMAL) {
                    selectedVoucherType = VoucherTypeConst.FREE_ONGKIR
                    chipCashback.chipType = ChipsUnify.TYPE_NORMAL
                    ChipsUnify.TYPE_SELECTED
                } else {
                    selectedVoucherType = null
                    ChipsUnify.TYPE_NORMAL
                }
            }

            chipPublic.setOnClickListener {
                chipPublic.chipType = if(chipPublic.chipType == ChipsUnify.TYPE_NORMAL) {
                    selectedVoucherTarget = listOf(VoucherTargetType.PUBLIC)
                    chipSpecial.chipType = ChipsUnify.TYPE_NORMAL
                    ChipsUnify.TYPE_SELECTED
                } else {
                    selectedVoucherTarget = listOf()
                    ChipsUnify.TYPE_NORMAL
                }
            }

            chipSpecial.setOnClickListener {
                chipSpecial.chipType = if(chipSpecial.chipType == ChipsUnify.TYPE_NORMAL) {
                    selectedVoucherTarget = listOf(VoucherTargetType.PRIVATE)
                    chipPublic.chipType = ChipsUnify.TYPE_NORMAL
                    ChipsUnify.TYPE_SELECTED
                } else {
                    selectedVoucherTarget = listOf()
                    ChipsUnify.TYPE_NORMAL
                }
            }

            chipSellerCreated.setOnClickListener {
                chipSellerCreated.chipType = if(chipSellerCreated.chipType == ChipsUnify.TYPE_NORMAL) {
                    isSellerCreated = true
                    ChipsUnify.TYPE_SELECTED
                } else {
                    isSellerCreated = false
                    ChipsUnify.TYPE_NORMAL
                }
            }

            chipSubsidy.setOnClickListener {
                chipSubsidy.chipType = if(chipSubsidy.chipType == ChipsUnify.TYPE_NORMAL) {
                    isSubsidy = true
                    ChipsUnify.TYPE_SELECTED
                } else {
                    isSubsidy = false
                    isSellerCreated = false
                    ChipsUnify.TYPE_NORMAL
                }
            }

            chipVps.setOnClickListener {
                chipVps.chipType = if(chipVps.chipType == ChipsUnify.TYPE_NORMAL) {
                    isVps = true
                    ChipsUnify.TYPE_SELECTED
                } else {
                    isVps = false
                    isSellerCreated = false
                    ChipsUnify.TYPE_NORMAL
                }
            }

            btnMvcApplyFilter.setOnClickListener {
                applyFilter = true
                onApplyClick()
                dismissAllowingStateLoss()
            }
        }
    }

    private fun resetFilters() {

        // reset state
        selectedVoucherType= null
        selectedVoucherTarget = listOf()
        isSellerCreated = false
        isSubsidy = false
        isVps = false

        // reset ui
        binding?.apply {
            chipCashback.chipType = ChipsUnify.TYPE_NORMAL
            chipPublic.chipType = ChipsUnify.TYPE_NORMAL
            chipSpecial.chipType = ChipsUnify.TYPE_NORMAL
            chipSellerCreated.chipType = ChipsUnify.TYPE_NORMAL
            chipSubsidy.chipType = ChipsUnify.TYPE_NORMAL
            chipVps.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun getFilterItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.top = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
            }
            if (position == filterAdapter.items.size.minus(1)) {
                outRect.bottom = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            }
        }
    }

    private fun onItemFilterClick(key: String) {
        onItemClick(key)
        val filterItems = filterAdapter.items.filterIsInstance<FilterItem>()
        val canReset = filterItems.any { it.isSelected }

        setupResetButton(canReset)
    }

    private fun setupResetButton(canReset: Boolean) {
        if (canReset) {
            setAction(context?.getString(R.string.mvc_reset).toBlankOrString()) {
                resetFilter()
            }
        } else {
            clearAction()
        }
    }

    private fun resetFilter() {
        filterAdapter.items.filterIsInstance<FilterItem>().forEach { it.isSelected = false }
        binding?.rvMcvFilter?.post {
            filterAdapter.notifyDataSetChanged()
        }
        clearAction()
    }

    fun setOnItemClickListener(action: (String) -> Unit): FilterBottomSheet {
        onItemClick = action
        return this
    }

    fun setOnApplyClickListener(action: () -> Unit): FilterBottomSheet {
        onApplyClick = action
        return this
    }

    fun setCancelApplyFilter(callback: (items: List<BaseFilterUiModel>) -> Unit): FilterBottomSheet {
        this.onCancelApply = callback
        return this
    }

    private fun List<BaseFilterUiModel>.copy(): List<BaseFilterUiModel> {
        return this.map {
            return@map if (it is FilterItem) it.copy() else it
        }
    }

    fun show(fm: FragmentManager, items: List<BaseFilterUiModel>) {
        applyFilter = false
        tmpFilterList = items.copy()

        filterAdapter.clearAllElements()
        filterAdapter.addElement(items)

        show(fm, TAG)
    }

    fun getSelectedVoucherType(): Int? {
        return selectedVoucherType
    }

    fun getSelectedVoucherTarget(): List<Int> {
        return selectedVoucherTarget
    }

    fun isSellerCreated(): Boolean {
        return isSellerCreated
    }

    fun isSubsidy(): Boolean {
        return isSubsidy
    }

    fun isVps(): Boolean {
        return isVps
    }

    fun getFilterCounter(): Int {
        return chipsFilter.filter { chipsUnify ->
            chipsUnify.chipType == ChipsUnify.TYPE_SELECTED
        }.count()
    }
}