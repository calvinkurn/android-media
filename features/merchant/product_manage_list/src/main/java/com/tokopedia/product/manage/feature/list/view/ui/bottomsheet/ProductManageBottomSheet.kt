package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.ProductMenuAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder.ProductMenuListener
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage.view.*

class ProductManageBottomSheet(
        container: View? = null,
        listener: ProductMenuListener? = null,
        sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener,
        private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage
        private val TAG: String = ProductManageBottomSheet::class.java.simpleName
    }

    private var menuAdapter: ProductMenuAdapter? = null
    private var sellerFeatureCarousel: SellerFeatureCarousel? = null

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

            sellerFeatureCarousel = itemView.sellerFeatureCarousel

            sellerFeatureCarousel?.run {
                if (!GlobalConfig.isSellerApp()) {
                    show()
                    setListener(sellerFeatureCarouselListener)
                    this.addItemDecoration()
                }
            }

            clearContentPadding = true
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@ProductManageBottomSheet)?.commit()
        }
    }

    private fun createProductManageMenu(product: ProductViewModel, isPowerMerchantOrOfficialStore: Boolean): List<ProductMenuViewModel> {
        return mutableListOf<ProductMenuViewModel>().apply {
            add(Preview(product))
            add(Duplicate(product))
            if (GlobalConfig.isSellerApp()) {
                add(StockReminder(product))
            }
            add(Delete(product))
            if (GlobalConfig.isSellerApp()) {
                if (product.status != ProductStatus.EMPTY) {
                    add(SetTopAds(product))
                }
                add(SetCashBack(product))
                if (product.isFeatured == true && isPowerMerchantOrOfficialStore) {
                    add(RemoveFeaturedProduct(product))
                } else {
                    if (product.isActive()) {
                        add(SetFeaturedProduct(product))
                    }
                }
            }
        }
    }

    fun show(product: ProductViewModel, isPowerMerchantOrOfficialStore: Boolean) {
        val menu = createProductManageMenu(product, isPowerMerchantOrOfficialStore)

        if (!GlobalConfig.isSellerApp()) {
            sellerFeatureCarousel?.setItems(listOf(
                    SellerFeatureUiModel.MultiEditFeatureWithDataUiModel(product),
                    SellerFeatureUiModel.TopAdsFeatureWithDataUiModel(product),
                    SellerFeatureUiModel.SetCashbackFeatureWithDataUiModel(product),
                    SellerFeatureUiModel.FeaturedProductFeatureWithDataUiModel(product),
                    SellerFeatureUiModel.StockReminderFeatureWithDataUiModel(product)
            ))
        }

        menuAdapter?.clearAllElements()
        menuAdapter?.addElement(menu)

        fm?.let { show(it, TAG) }
    }
}