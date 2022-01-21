package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.databinding.BottomSheetProductManageStockLocationBinding
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductManageStockLocationBottomSheet: BottomSheetUnify() {

    companion object {

        private val TAG = ProductManageStockLocationBottomSheet::class.java.canonicalName

        fun newInstance(): ProductManageStockLocationBottomSheet {
            return ProductManageStockLocationBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetProductManageStockLocationBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        ).apply {
            stockLocationInfo.run {
                setImageUrl(ProductManageUrl.ILLUSTRATION_STOCK_LOCATION)
                setPrimaryCTAClickListener {
                    dismiss()
                }
            }
        }
        setChild(binding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}