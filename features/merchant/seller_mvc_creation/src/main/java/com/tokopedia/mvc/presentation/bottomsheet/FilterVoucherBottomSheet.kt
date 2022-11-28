package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetFilterVoucherBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.presentation.bottomsheet.adapter.FilterVoucherAdapter
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.FilterVoucherViewModel
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class FilterVoucherBottomSheet(
    var filter: FilterModel = FilterModel()
): BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetFilterVoucherBinding>()
    private var listener: FilterVoucherBottomSheetListener? = null

    @Inject
    lateinit var viewModel: FilterVoucherViewModel

    interface FilterVoucherBottomSheetListener {
        fun onFilterVoucherChanged(filter: FilterModel)
    }

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
        binding?.setupContentViews()
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetFilterVoucherBinding.inflate(inflater, container, false)
        showKnob = true
        showCloseIcon = false
        isDragable = true
        setAction("Reset", ::onActionTextClicked)
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_bottomsheet_filter_voucher_title))
    }

    private fun SmvcBottomsheetFilterVoucherBinding.setupContentViews() {
        rvStatus.setupListItems(R.array.status_items, ::onRvStatusItemClicked)
        rvType.setupListItems(R.array.type_items, ::onRvTypeItemClicked)
        rvSource.setupListItems(R.array.source_items, ::onRvSourceItemClicked)
        rvPromoType.setupListItems(R.array.promo_type_items, ::onRvPromoTypeItemClicked)
        rvTarget.setupListItems(R.array.target_items, ::onRvTargetItemClicked)
        rvTargetBuyer.setupListItems(R.array.target_buyer_items, ::onRvTargetBuyerItemClicked)
        btnSubmit.setOnClickListener {
            listener?.onFilterVoucherChanged(filter)
            dismiss()
        }
    }

    private fun onActionTextClicked(view: View) {

    }

    private fun onRvStatusItemClicked(position: Int, isSelected: Boolean) {
        val value = VoucherStatus.values().getOrNull(position) ?: return
        if (filter.status.indexOf(value).isLessThanZero()) {
            filter.status.add(value)
        } else {
            filter.status.remove(value)
        }

    }

    private fun onRvTypeItemClicked(position: Int, isSelected: Boolean) {

    }

    private fun onRvSourceItemClicked(position: Int, isSelected: Boolean) {

    }

    private fun onRvPromoTypeItemClicked(position: Int, isSelected: Boolean) {

    }

    private fun onRvTargetItemClicked(position: Int, isSelected: Boolean) {

    }

    private fun onRvTargetBuyerItemClicked(position: Int, isSelected: Boolean) {

    }

    private fun RecyclerView.setupListItems(itemsResource: Int, onItemClicked: (Int, Boolean) -> Unit) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        layoutManager = flexboxLayoutManager
        adapter = FilterVoucherAdapter().apply {
            setDataList(getListFromResource(itemsResource))
            setOnClickListener(onItemClicked)
        }
    }

    private fun getListFromResource(itemsResource: Int): List<Pair<String, Boolean>> =
        context?.resources?.getStringArray(itemsResource).orEmpty().toList().map {
            Pair(it, true)
        }

    fun setListener(listener: FilterVoucherBottomSheetListener) {
        this.listener = listener
    }

}

