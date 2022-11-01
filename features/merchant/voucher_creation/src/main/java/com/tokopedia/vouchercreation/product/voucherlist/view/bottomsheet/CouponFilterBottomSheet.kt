package com.tokopedia.vouchercreation.product.voucherlist.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.BottomsheetCouponFilterBinding

class CouponFilterBottomSheet : BottomSheetUnify() {

    private var nullableBinding: BottomsheetCouponFilterBinding? = null
    private val binding: BottomsheetCouponFilterBinding
        get() = requireNotNull(nullableBinding)

    private var onFilterSelected: (FilterType, FilterTarget) -> Unit = { _, _ -> }
    private var onResetFilter: (FilterType, FilterTarget) -> Unit = { _, _ -> }
    private var selectedFilterType = FilterType.NOT_SELECTED
    private var selectedFilterTarget = FilterTarget.NOT_SELECTED

    enum class FilterType {
        FREE_SHIPPING,
        CASHBACK,
        NOT_SELECTED
    }

    enum class FilterTarget {
        PUBLIC,
        PRIVATE,
        NOT_SELECTED
    }

    companion object {
        fun newInstance(
            selectedFilterType: FilterType,
            selectedFilterTarget: FilterTarget,
            onFilterSelected: (FilterType, FilterTarget) -> Unit,
            onResetFilter: (FilterType, FilterTarget) -> Unit,
        ): CouponFilterBottomSheet {
            return CouponFilterBottomSheet().apply {
                this.selectedFilterType = selectedFilterType
                this.selectedFilterTarget = selectedFilterTarget
                this.onFilterSelected = onFilterSelected
                this.onResetFilter = onResetFilter
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nullableBinding = BottomsheetCouponFilterBinding.inflate(inflater, container, false)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        setTitle(getString(R.string.mvc_filter))
        showKnob = true
        showCloseIcon = false
        setChild(binding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChipSelectionListener()
        setupViews()
        displayPreviouslySelectedTypeFilter()
        displayPreviousSelectedTargetFilter()
        setupResetMenu()
    }


    private fun setupResetMenu() {
        if (hasPreviouslySelectTypeFilter() || hasPreviouslySelectTargetFilter()) {
            showResetMenu()
        }
    }

    private fun displayPreviouslySelectedTypeFilter() {
        when (selectedFilterType) {
            FilterType.FREE_SHIPPING -> binding.chipTypeFreeShipping.chipType =
                ChipsUnify.TYPE_SELECTED
            FilterType.CASHBACK -> binding.chipTypeCashback.chipType = ChipsUnify.TYPE_SELECTED
            FilterType.NOT_SELECTED -> clearAction()
        }
    }

    private fun displayPreviousSelectedTargetFilter() {
        when (selectedFilterTarget) {
            FilterTarget.PUBLIC -> binding.chipTargetPublic.chipType = ChipsUnify.TYPE_SELECTED
            FilterTarget.PRIVATE -> binding.chipTargetPrivate.chipType = ChipsUnify.TYPE_SELECTED
            FilterTarget.NOT_SELECTED -> clearAction()
        }
    }

    private fun setupChipSelectionListener() {
        with(binding) {
            chipTypeFreeShipping.selectedChangeListener = { isSelected ->
                if (isSelected) {
                    selectedFilterType = FilterType.FREE_SHIPPING
                    showApplyButton()
                    showResetMenu()
                }
            }
            chipTypeCashback.selectedChangeListener = { isSelected ->
                if (isSelected) {
                    selectedFilterType = FilterType.CASHBACK
                    showApplyButton()
                    showResetMenu()
                }
            }
            chipTargetPublic.selectedChangeListener = { isSelected ->
                if (isSelected) {
                    selectedFilterTarget = FilterTarget.PUBLIC
                    showApplyButton()
                    showResetMenu()
                }
            }

            chipTargetPrivate.selectedChangeListener = { isSelected ->
                if (isSelected) {
                    selectedFilterTarget = FilterTarget.PRIVATE
                    showApplyButton()
                    showResetMenu()
                }
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            chipTypeFreeShipping.chip_container.setOnClickListener {
                chipTypeFreeShipping.chipType = ChipsUnify.TYPE_SELECTED
                chipTypeCashback.chipType = ChipsUnify.TYPE_NORMAL
            }

            chipTypeCashback.chip_container.setOnClickListener {
                chipTypeFreeShipping.chipType = ChipsUnify.TYPE_NORMAL
                chipTypeCashback.chipType = ChipsUnify.TYPE_SELECTED
            }

            chipTargetPublic.chip_container.setOnClickListener {
                chipTargetPublic.chipType = ChipsUnify.TYPE_SELECTED
                chipTargetPrivate.chipType = ChipsUnify.TYPE_NORMAL

            }
            chipTargetPrivate.chip_container.setOnClickListener {
                chipTargetPublic.chipType = ChipsUnify.TYPE_NORMAL
                chipTargetPrivate.chipType = ChipsUnify.TYPE_SELECTED
            }
            btnApply.setOnClickListener {
                onFilterSelected(selectedFilterType, selectedFilterTarget)
                dismiss()
            }
        }

    }

    private fun showResetMenu() {
        setAction(getString(R.string.mvc_reset)) { onResetFilterApplied() }
    }

    private fun showApplyButton() {
        binding.btnApply.visible()
    }

    private fun hideApplyButton() {
        binding.btnApply.gone()
    }

    private fun onResetFilterApplied() {
        clearAction()
        clearChipSelection()
        onResetFilter(selectedFilterType, selectedFilterTarget)
    }

    private fun clearChipSelection() {
        with(binding) {
            chipTypeFreeShipping.chipType = ChipsUnify.TYPE_NORMAL
            chipTypeCashback.chipType = ChipsUnify.TYPE_NORMAL
            chipTargetPublic.chipType = ChipsUnify.TYPE_NORMAL
            chipTargetPrivate.chipType = ChipsUnify.TYPE_NORMAL
            selectedFilterType = FilterType.NOT_SELECTED
            selectedFilterTarget = FilterTarget.NOT_SELECTED
        }
    }


    private fun hasPreviouslySelectTypeFilter() : Boolean{
        return selectedFilterType != FilterType.NOT_SELECTED
    }

    private fun hasPreviouslySelectTargetFilter() : Boolean {
        return selectedFilterTarget != FilterTarget.NOT_SELECTED
    }
}
