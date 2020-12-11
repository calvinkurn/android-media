package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_stock_location.*

class ProductManageStockLocationBottomSheet: BottomSheetUnify() {

    companion object {

        private val TAG = ProductManageStockLocationBottomSheet::class.java.canonicalName
        private val LAYOUT = R.layout.bottom_sheet_product_manage_stock_location

        fun newInstance(): ProductManageStockLocationBottomSheet {
            return ProductManageStockLocationBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(LAYOUT, container)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stockLocationInfo.apply {
            setImageUrl(ProductManageUrl.ILLUSTRATION_STOCK_LOCATION)
            setPrimaryCTAClickListener {
                dismiss()
            }
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}