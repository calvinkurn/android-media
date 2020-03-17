package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_stock_information.view.*

class StockInformationBottomSheet(
    container: View?,
    private val fm: FragmentManager?
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
        val description = itemView.context.getString(R.string.product_manage_stock_info_description)
        itemView.textDescription.text = description

        setTitle(title)
        setChild(itemView)
    }

    fun show() {
        fm?.let { show(it, TAG) }
    }
}