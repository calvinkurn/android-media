package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetProductManageStockInformationBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class StockReminderInformationBottomSheet(
    private val fm: FragmentManager? = null,
    private val stockAlertCount: Int,
    private val stockAlertActive: Boolean
) : BottomSheetUnify() {

    companion object {
        private val TAG: String = StockReminderInformationBottomSheet::class.java.simpleName
    }

    private var binding by autoClearedNullable<BottomSheetProductManageStockInformationBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageStockInformationBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = context?.getString(R.string.product_manage_stock_reminder_info).orEmpty()
        setTitle(title)
        setupView()

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@StockReminderInformationBottomSheet)?.commit()
        }

    }

    fun show() {
        fm?.let {
            show(it, TAG)
        }
    }

    private fun setupView() {

        val description = if (stockAlertActive) {
            context?.getString(
                com.tokopedia.product.manage.common.R.string.product_manage_stock_description_information_stock_reminder_active,
                stockAlertCount
            ).orEmpty()
        } else {
            context?.getString(
                com.tokopedia.product.manage.common.R.string.product_manage_stock_description_information_stock_reminder,
                stockAlertCount
            ).orEmpty()
        }
        val padding =
            context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                .orZero()

        binding?.textDescription?.text = description
        binding?.root?.setPadding(0, 0, 0, padding)
    }
}