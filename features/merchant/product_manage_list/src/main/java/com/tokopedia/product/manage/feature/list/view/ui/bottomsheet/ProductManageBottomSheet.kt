package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.feature.list.view.adapter.ProductMenuAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder.ProductMenuListener
import com.tokopedia.product.manage.feature.list.view.model.ProductItemDivider
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.*
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage.view.*

class ProductManageBottomSheet : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage
        private val TAG: String? = ProductManageBottomSheet::class.java.canonicalName

        private const val EXTRA_FEATURE_ACCESS = "extra_feature_access"

        fun createInstance(access: ProductManageAccess): ProductManageBottomSheet {
            return ProductManageBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_FEATURE_ACCESS, access)
                }
                clearContentPadding = true
            }
        }
    }

    private var menuAdapterListener: ProductMenuListener? = null
    private var sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener? = null
    private var menuAdapter: ProductMenuAdapter? = null
    private var sellerFeatureCarousel: SellerFeatureCarousel? = null
    private var product: ProductUiModel? = null
    private var isPowerMerchantOrOfficialStore: Boolean = false
    
    private val access by lazy { arguments?.getParcelable<ProductManageAccess>(EXTRA_FEATURE_ACCESS) }

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
        setupSellerCarousel()
        setupMenuAdapter()
    }

    private fun setupMenuAdapter() = view?.run {
        menuAdapterListener?.let {
            val menuList = menuList
            menuAdapter = ProductMenuAdapter(it)
            menuList.adapter = menuAdapter
        }

        if(GlobalConfig.isSellerApp()) {
            menuList.clearItemDecoration()
        }

        product?.let { product ->
            val menu = createProductManageMenu(product, isPowerMerchantOrOfficialStore)
            
            if (!GlobalConfig.isSellerApp()) {
                val sellerFeatureList = createSellerFeatureList(product)
                sellerFeatureCarousel?.setItems(sellerFeatureList)
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

    private fun createProductManageMenu(
        product: ProductUiModel,
        isPowerMerchantOrOfficialStore: Boolean
    ): List<Visitable<*>> {
        val menuList = mutableListOf<Visitable<*>>()

        access?.run {
            menuList.apply {
                val isFeatured = product.isFeatured == true

                add(Preview(product))

                if(duplicateProduct) {
                    add(Duplicate(product))
                }

                if(GlobalConfig.isSellerApp() && setStockReminder) {
                    add(StockReminder(product))
                }

                if(deleteProduct) {
                    add(Delete(product))
                }

                if(GlobalConfig.isSellerApp()) {
                    add(ProductItemDivider)

                    if(setTopAds) {
                        when {
                            product.hasTopAds() -> add(SeeTopAds(product))
                            else -> add(SetTopAds(product))
                        }
                    }

                    if(broadcastChat) {
                        add(CreateBroadcastChat(product))
                    }

                    if(setCashBack) {
                        add(SetCashBack(product))
                    }

                    if(isFeatured && isPowerMerchantOrOfficialStore && setFeatured) {
                        add(RemoveFeaturedProduct(product))
                    }

                    if(!isFeatured && product.isActive() && setFeatured) {
                        add(SetFeaturedProduct(product))
                    }
                }
            }
        }

        return menuList
    }
    
    private fun createSellerFeatureList(product: ProductUiModel): List<SellerFeatureUiModel> {
        val featureList = mutableListOf<SellerFeatureUiModel>()

        access?.run {
            featureList.apply {
                if(multiSelect) {
                    add(SellerFeatureUiModel.MultiEditFeatureWithDataUiModel(product))
                }

                if(setTopAds) {
                    add(SellerFeatureUiModel.TopAdsFeatureWithDataUiModel(product))
                }

                if(setCashBack) {
                    add(SellerFeatureUiModel.SetCashbackFeatureWithDataUiModel(product))
                }

                if(setFeatured) {
                    add(SellerFeatureUiModel.FeaturedProductFeatureWithDataUiModel(product))
                }

                if(setStockReminder) {
                    add(SellerFeatureUiModel.StockReminderFeatureWithDataUiModel(product))
                }

                if(broadcastChat) {
                   add(SellerFeatureUiModel.BroadcastChatProductManageUiModel(product))
                }
            }
        }

        return featureList
    }

    fun init(
            productMenuListener: ProductMenuListener,
            sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener
    ) {

        this.menuAdapterListener = productMenuListener
        this.sellerFeatureCarouselListener = sellerFeatureCarouselListener
    }

    fun show(fm: FragmentManager, product: ProductUiModel, isPowerMerchantOrOfficialStore: Boolean) {
        this.product = product
        this.isPowerMerchantOrOfficialStore = isPowerMerchantOrOfficialStore
        show(fm, TAG)
    }

    fun dismiss(fm: FragmentManager) {
        (fm.findFragmentByTag(TAG) as? BottomSheetUnify)?.dismissAllowingStateLoss()
    }
}