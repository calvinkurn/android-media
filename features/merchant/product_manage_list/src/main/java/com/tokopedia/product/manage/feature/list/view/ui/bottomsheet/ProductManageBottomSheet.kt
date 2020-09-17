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

class ProductManageBottomSheet : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage
        private val TAG: String = ProductManageBottomSheet::class.java.simpleName

        fun createInstance(): ProductManageBottomSheet {
            return ProductManageBottomSheet().apply {
                clearContentPadding = true
            }
        }
    }

    private var menuAdapterListener: ProductMenuListener? = null
    private var sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener? = null
    private var menuAdapter: ProductMenuAdapter? = null
    private var sellerFeatureCarousel: SellerFeatureCarousel? = null
    private var product: ProductViewModel? = null
    private var isPowerMerchantOrOfficialStore: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@ProductManageBottomSheet)?.commit()
        }
    }

    private fun setupView() = view?.run {
        setupMenuAdapter()
        setupSellerCarousel()
    }

    private fun setupMenuAdapter() = view?.run {
        menuAdapterListener?.let {
            val menuList = menuList
            menuAdapter = ProductMenuAdapter(it)
            menuList.adapter = menuAdapter
        }

        product?.let { product ->
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
        }
    }

    private fun setupSellerCarousel() = view?.sellerFeatureCarousel?.run {
        this@ProductManageBottomSheet.sellerFeatureCarousel = this
        if (!GlobalConfig.isSellerApp()) {
            show()
            setListener(sellerFeatureCarouselListener)
            this.addItemDecoration()
        }
    }

    private fun setupChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(LAYOUT, container)
        val menuTitle = itemView.context.getString(R.string.product_manage_bottom_sheet_title)
        setTitle(menuTitle)
        setChild(itemView)
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

    fun init(
            productMenuListener: ProductMenuListener,
            sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener
    ) {

        this.menuAdapterListener = productMenuListener
        this.sellerFeatureCarouselListener = sellerFeatureCarouselListener
    }

    fun show(fm: FragmentManager, product: ProductViewModel, isPowerMerchantOrOfficialStore: Boolean) {
        this.product = product
        this.isPowerMerchantOrOfficialStore = isPowerMerchantOrOfficialStore
        show(fm, TAG)
    }
}