
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

class StockInformationBottomSheet(
    container: View? = null,
    private val fm: FragmentManager? = null
): BottomSheetUnify() {

    companion object {
        private val TAG: String = StockInformationBottomSheet::class.java.simpleName
    }

    init {
        (container as? ViewGroup)?.let { vg ->
            val binding = BottomSheetProductManageStockInformationBinding.inflate(
                LayoutInflater.from(context),
                vg,
                false
            )

            val title = context?.getString(R.string.product_manage_stock_information).orEmpty()
            val description = context?.getString(com.tokopedia.product.manage.common.R.string.product_manage_stock_info_description).orEmpty()
            val padding = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()

            binding.textDescription.text = description
            binding.root.setPadding(0, 0, 0, padding)

            setTitle(title)
            setChild(binding.root)
        }
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
}