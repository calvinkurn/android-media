
package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_stock_information.view.*

class StockInformationBottomSheet(
    container: View? = null,
    private val fm: FragmentManager? = null
): BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage_stock_information
        private val TAG: String = StockInformationBottomSheet::class.java.simpleName
    }

    init {
        val itemView = LayoutInflater.from(container?.context)
            .inflate(LAYOUT, (container as ViewGroup), false)

        val title = itemView.context.getString(R.string.product_manage_stock_information)
        val description = itemView.context.getString(com.tokopedia.product.manage.common.R.string.product_manage_stock_info_description)
        val padding = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

        itemView.textDescription.text = description
        itemView.setPadding(0, 0, 0, padding)

        setTitle(title)
        setChild(itemView)
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