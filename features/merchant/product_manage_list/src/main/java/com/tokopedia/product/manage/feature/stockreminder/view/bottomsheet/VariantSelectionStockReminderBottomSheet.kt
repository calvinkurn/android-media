package com.tokopedia.product.manage.feature.stockreminder.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetVariantStockReminderSelectionBinding
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.GroupVariantEditSelectAdapter
import com.tokopedia.product.manage.feature.stockreminder.view.data.GroupVariantProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class VariantSelectionStockReminderBottomSheet(
    private val fm: FragmentManager? = null,
) : BottomSheetUnify(), GroupVariantEditSelectAdapter.OnSelectionListener {

    companion object {
        private val TAG: String = VariantSelectionStockReminderBottomSheet::class.java.simpleName
    }

    private var binding by autoClearedNullable<BottomSheetVariantStockReminderSelectionBinding>()

    private var groupVariantAdapter: GroupVariantEditSelectAdapter? = null

    private var productSelection: List<ProductStockReminderUiModel>? = null

    private var setOnNextListener: (productSelection: List<ProductStockReminderUiModel>) -> Unit =
        {}

    init {
        groupVariantAdapter = GroupVariantEditSelectAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetVariantStockReminderSelectionBinding.inflate(
            inflater,
            container,
            false
        )
        setupView()
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@VariantSelectionStockReminderBottomSheet)?.commit()
        }
    }

    override fun onSelection(productSelection: List<ProductStockReminderUiModel>) {
        this.productSelection = productSelection
        binding?.buttonNext?.isEnabled = productSelection.isNotEmpty()
    }

    fun setupView() {
        val title =
            context?.getString(R.string.product_stock_reminder_title_choose_variant).orEmpty()
        setTitle(title)
        binding?.rvGroupVariant?.apply {
            adapter = groupVariantAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding?.checkboxSelectAll?.setOnCheckedChangeListener { compoundButton, isCheck ->
            groupVariantAdapter?.setCheckAll(isCheck)
        }

        binding?.buttonNext?.setOnClickListener {
            setOnNextListener(productSelection.orEmpty())
            dismiss()
        }
    }

    fun setData(data: List<GroupVariantProductStockReminderUiModel>) {
        groupVariantAdapter?.setItems(data)
    }

    fun show() {
        fm?.let { show(it, TAG) }
    }

    fun setOnNextListener(setOnNextListener: (productSelection: List<ProductStockReminderUiModel>) -> Unit) {
        this.setOnNextListener = setOnNextListener
    }
}