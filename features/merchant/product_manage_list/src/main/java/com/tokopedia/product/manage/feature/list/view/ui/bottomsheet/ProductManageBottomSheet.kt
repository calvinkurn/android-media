package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.ProductMenuAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder.ProductMenuListener
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage.view.*

class ProductManageBottomSheet(
    container: View? = null,
    listener: ProductMenuListener? = null,
    private val fm: FragmentManager? = null
): BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage
        private val TAG: String = ProductManageBottomSheet::class.java.simpleName
    }

    private var menuAdapter: ProductMenuAdapter? = null

    init {
        if (container != null && listener != null && fm != null) {
            val itemView = LayoutInflater.from(container?.context)
                    .inflate(LAYOUT, (container as ViewGroup), false)

            val menuList = itemView.menuList
            val menuTitle = itemView.context
                    .getString(R.string.product_manage_bottom_sheet_title)

            menuAdapter = ProductMenuAdapter(listener)
            menuList.adapter = menuAdapter

            setTitle(menuTitle)
            setChild(itemView)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@ProductManageBottomSheet)?.commit()
        }
    }

    private fun createProductManageMenu(product: ProductViewModel, isPowerMerchantOrOfficialStore: Boolean): List<ProductMenuViewModel> {
        val menuList = mutableListOf(
            Preview(product),
            Duplicate(product),
            StockReminder(product),
            Delete(product)
        )

        if (product.status != ProductStatus.EMPTY) {
            menuList.add(SetTopAds(product))
        }

        menuList.add(SetCashBack(product))

        if(product.isFeatured == true && isPowerMerchantOrOfficialStore) {
            menuList.add(RemoveFeaturedProduct(product))
        } else {
            if(product.isActive()) {
                menuList.add(SetFeaturedProduct(product))
            }
        }

        return menuList
    }

    fun show(product: ProductViewModel, isPowerMerchantOrOfficialStore: Boolean) {
        val menu = createProductManageMenu(product, isPowerMerchantOrOfficialStore)

        menuAdapter?.clearAllElements()
        menuAdapter?.addElement(menu)

        fm?.let { show(it, TAG) }
    }
}