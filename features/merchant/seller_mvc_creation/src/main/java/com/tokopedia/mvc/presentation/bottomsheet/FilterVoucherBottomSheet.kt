package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetFilterVoucherBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherSource
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.bottomsheet.adapter.FilterVoucherAdapter
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.FilterVoucherViewModel
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class FilterVoucherBottomSheet: BottomSheetUnify() {

    interface FilterVoucherBottomSheetListener {
        fun onFilterVoucherChanged(filter: FilterModel)
    }

    companion object {
        @JvmStatic
        fun newInstance(filter: FilterModel): FilterVoucherBottomSheet {
            return FilterVoucherBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_FILTER, filter)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: FilterVoucherViewModel
    private var binding by autoClearedNullable<SmvcBottomsheetFilterVoucherBinding>()
    private var listener: FilterVoucherBottomSheetListener? = null
    private val filter by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_FILTER) as? FilterModel }

    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filter?.let {
            viewModel.setupFilterData(it)
            viewModel.filterData.observe(viewLifecycleOwner) { filterResult ->
                bottomSheetAction.visible()
                binding?.btnSubmit?.setOnClickListener {
                    listener?.onFilterVoucherChanged(filterResult)
                    dismiss()
                }
            }
            binding?.setupContentViews(it)
            view.post { bottomSheetAction.gone() }
        }
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetFilterVoucherBinding.inflate(inflater, container, false)
        showKnob = true
        showCloseIcon = false
        isDragable = true
        setAction(getString(R.string.smvc_bottomsheet_filter_voucher_action_text), ::onActionTextClicked)
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_bottomsheet_filter_voucher_title))
    }

    private fun SmvcBottomsheetFilterVoucherBinding.setupContentViews(filter: FilterModel) {
        val status = filter.status.map { it.id }
        val type = filter.voucherType.map { it.id }
        val source = filter.source.map { it.id }
        val promoType = filter.promoType.map { it.id.dec() }
        val target = filter.target.map { it.ordinal }
        val targetBuyer = filter.targetBuyer.map { it.id }

        rvStatus.setupListItems(R.array.status_items, status, ::onRvStatusItemClicked)
        rvType.setupListItems(R.array.type_items, type, ::onRvTypeItemClicked)
        rvSource.setupListItems(R.array.source_items, source, ::onRvSourceItemClicked)
        rvPromoType.setupListItems(R.array.promo_type_items, promoType, ::onRvPromoTypeItemClicked)
        rvTarget.setupListItems(R.array.target_items, target, ::onRvTargetItemClicked)
        rvTargetBuyer.setupListItems(R.array.target_buyer_items, targetBuyer, ::onRvTargetBuyerItemClicked)
    }

    private fun onActionTextClicked(view: View) {
        binding?.apply {
            rvStatus.resetSelection()
            rvType.resetSelection()
            rvSource.resetSelection()
            rvPromoType.resetSelection()
            rvTarget.resetSelection()
            rvTargetBuyer.resetSelection()
            viewModel.resetSelection()
            bottomSheetAction.isVisible = false
        }
    }

    private fun onRvStatusItemClicked(position: Int, isSelected: Boolean) {
        val status = VoucherStatus.values().find { it.id == position } ?: return
        binding?.rvStatus.setSelection(position)
        viewModel.setStatusFilter(status)
    }

    private fun onRvTypeItemClicked(position: Int, isSelected: Boolean) {
        val type = VoucherServiceType.values().find { it.id == position } ?: return
        viewModel.setVoucherType(type)
    }

    private fun onRvSourceItemClicked(position: Int, isSelected: Boolean) {
        val source = VoucherSource.values().find { it.id == position } ?: return
        binding?.rvSource?.setSelection(position)
        viewModel.setSource(source)
    }

    private fun onRvPromoTypeItemClicked(position: Int, isSelected: Boolean) {
        val promo = PromoType.values().getOrNull(position) ?: return
        binding?.rvPromoType.setSelection(position)
        viewModel.setPromoType(promo)
    }

    private fun onRvTargetItemClicked(position: Int, isSelected: Boolean) {
        val target = VoucherTarget.values().getOrNull(position) ?: return
        viewModel.setTarget(target)
    }

    private fun onRvTargetBuyerItemClicked(position: Int, isSelected: Boolean) {
        val targetBuyer = VoucherTargetBuyer.values().find { it.id == position } ?: return
        viewModel.setTargetBuyer(targetBuyer)
    }

    private fun RecyclerView.setupListItems(
        itemsResource: Int,
        selectedIds: List<Int>,
        onItemClicked: (Int, Boolean) -> Unit
    ) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        layoutManager = flexboxLayoutManager
        adapter = FilterVoucherAdapter().apply {
            setDataList(getListFromResource(itemsResource, selectedIds))
            setOnClickListener(onItemClicked)
        }
    }

    private fun RecyclerView?.setSelection(position: Int) {
        (this?.adapter as? FilterVoucherAdapter)?.setSelectionAt(position)
    }

    private fun RecyclerView.resetSelection() {
        val adapter = adapter as? FilterVoucherAdapter
        adapter?.resetSelection()
    }

    private fun getListFromResource(itemsResource: Int, selectedIds: List<Int>): List<Pair<String, Boolean>> =
        context?.resources?.getStringArray(itemsResource).orEmpty().toList().mapIndexed { index, s ->
            Pair(s, selectedIds.any { it == index })
        }

    fun setListener(listener: FilterVoucherBottomSheetListener) {
        this.listener = listener
    }

}

