package com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet

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
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseFilterUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseFilterUiModel.*
import com.tokopedia.vouchercreation.voucherlist.view.adapter.FilterAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_filter.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_filter.view.*

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
        chipsFilter = listOf<ChipsUnify>(chip_cashback, chip_public, chip_special, chip_seller_created, chip_subsidy, chip_vps)
        setupView(view)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!applyFilter) {
            onCancelApply(tmpFilterList)
        }
    }

    private fun initBottomSheet() {
        context?.run {
            setTitle(getString(R.string.mvc_filter))
            val childView = View.inflate(this, R.layout.bottomsheet_mvc_filter, null)
            setChild(childView)
            showKnob = true
            setAction(getString(R.string.mvc_reset)) {
                resetFilters()
            }
        }
    }

    private fun setupView(view: View) = with(view) {
        rvMcvFilter.layoutManager = LinearLayoutManager(context)
        rvMcvFilter.adapter = filterAdapter
        rvMcvFilter.addItemDecoration(getFilterItemDecoration())

        selectedVoucherType?.run { chip_cashback.chipType = ChipsUnify.TYPE_SELECTED }
        if (selectedVoucherTarget.isNotEmpty() && selectedVoucherTarget.first() == VoucherTargetType.PUBLIC) chip_public.chipType = ChipsUnify.TYPE_SELECTED
        if (selectedVoucherTarget.isNotEmpty() && selectedVoucherTarget.first() == VoucherTargetType.PRIVATE) chip_special.chipType = ChipsUnify.TYPE_SELECTED
        if (isSellerCreated) chip_seller_created.chipType = ChipsUnify.TYPE_SELECTED
        if (isSubsidy) chip_subsidy.chipType = ChipsUnify.TYPE_SELECTED
        if (isVps) chip_vps.chipType = ChipsUnify.TYPE_SELECTED

        chip_cashback.setOnClickListener {
            chip_cashback.chipType = if(chip_cashback.chipType == ChipsUnify.TYPE_NORMAL) {
                selectedVoucherType = VoucherTypeConst.CASHBACK
                chip_free_shipping.chipType = ChipsUnify.TYPE_NORMAL
                ChipsUnify.TYPE_SELECTED
            } else {
                selectedVoucherType = null
                ChipsUnify.TYPE_NORMAL
            }
        }

        chip_free_shipping.setOnClickListener {
            chip_free_shipping.chipType = if(chip_free_shipping.chipType == ChipsUnify.TYPE_NORMAL) {
                selectedVoucherType = VoucherTypeConst.FREE_ONGKIR
                chip_cashback.chipType = ChipsUnify.TYPE_NORMAL
                ChipsUnify.TYPE_SELECTED
            } else {
                selectedVoucherType = null
                ChipsUnify.TYPE_NORMAL
            }
        }

        chip_public.setOnClickListener {
            chip_public.chipType = if(chip_public.chipType == ChipsUnify.TYPE_NORMAL) {
                selectedVoucherTarget = listOf(VoucherTargetType.PUBLIC)
                chip_special.chipType = ChipsUnify.TYPE_NORMAL
                ChipsUnify.TYPE_SELECTED
            } else {
                selectedVoucherTarget = listOf()
                ChipsUnify.TYPE_NORMAL
            }
        }

        chip_special.setOnClickListener {
            chip_special.chipType = if(chip_special.chipType == ChipsUnify.TYPE_NORMAL) {
                selectedVoucherTarget = listOf(VoucherTargetType.PRIVATE)
                chip_public.chipType = ChipsUnify.TYPE_NORMAL
                ChipsUnify.TYPE_SELECTED
            } else {
                selectedVoucherTarget = listOf()
                ChipsUnify.TYPE_NORMAL
            }
        }

        chip_seller_created.setOnClickListener {
            chip_seller_created.chipType = if(chip_seller_created.chipType == ChipsUnify.TYPE_NORMAL) {
                isSellerCreated = true
                ChipsUnify.TYPE_SELECTED
            } else {
                isSellerCreated = false
                ChipsUnify.TYPE_NORMAL
            }
        }

        chip_subsidy.setOnClickListener {
            chip_subsidy.chipType = if(chip_subsidy.chipType == ChipsUnify.TYPE_NORMAL) {
                isSubsidy = true
                ChipsUnify.TYPE_SELECTED
            } else {
                isSubsidy = false
                isSellerCreated = false
                ChipsUnify.TYPE_NORMAL
            }
        }

        chip_vps.setOnClickListener {
            chip_vps.chipType = if(chip_vps.chipType == ChipsUnify.TYPE_NORMAL) {
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

    private fun resetFilters() {

        // reset state
        selectedVoucherType= null
        selectedVoucherTarget = listOf()
        isSellerCreated = false
        isSubsidy = false
        isVps = false

        // reset ui
        chip_cashback.chipType = ChipsUnify.TYPE_NORMAL
        chip_public.chipType = ChipsUnify.TYPE_NORMAL
        chip_special.chipType = ChipsUnify.TYPE_NORMAL
        chip_seller_created.chipType = ChipsUnify.TYPE_NORMAL
        chip_subsidy.chipType = ChipsUnify.TYPE_NORMAL
        chip_vps.chipType = ChipsUnify.TYPE_NORMAL
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
        view?.rvMcvFilter?.post {
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