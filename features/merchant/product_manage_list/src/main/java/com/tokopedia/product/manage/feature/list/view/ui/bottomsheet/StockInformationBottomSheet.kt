
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

class StockInformationBottomSheet(
    private val fm: FragmentManager? = null
): BottomSheetUnify() {

    companion object {
        private val TAG: String = StockInformationBottomSheet::class.java.simpleName
    }

    init {
        val title = context?.getString(R.string.product_manage_stock_information).orEmpty()

        setTitle(title)
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
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@StockInformationBottomSheet)?.commit()
        }
    }

    fun show() {
        fm?.let { show(it, TAG) }
    }

    private fun setupView() {
        val description = context?.getString(com.tokopedia.product.manage.common.R.string.product_manage_stock_info_description)
            .orEmpty()
        val padding = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()

        binding?.textDescription?.text = description
        binding?.root?.setPadding(0, 0, 0, padding)
    }
}