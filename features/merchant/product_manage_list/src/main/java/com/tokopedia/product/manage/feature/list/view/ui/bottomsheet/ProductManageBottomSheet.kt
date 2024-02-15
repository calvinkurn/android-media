package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.util.ProductManageConfig
import com.tokopedia.product.manage.databinding.BottomSheetProductManageBinding
import com.tokopedia.product.manage.feature.list.view.adapter.ProductMenuAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder.ProductMenuListener
import com.tokopedia.product.manage.feature.list.view.model.ProductItemDivider
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ProductManageBottomSheet : BottomSheetUnify() {

    companion object {
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
    private var sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener? =
        null
    private var menuAdapter: ProductMenuAdapter? = null
    private var sellerFeatureCarousel: SellerFeatureCarousel? = null
    private var product: ProductUiModel? = null
    private var isPowerMerchantOrOfficialStore: Boolean = false
    private var isProductCouponEnabled: Boolean = true
    var menuLayoutManager: LinearLayoutManager? = null

    private val access by lazy { arguments?.getParcelable<ProductManageAccess>(EXTRA_FEATURE_ACCESS) }

    private var binding by autoClearedNullable<BottomSheetProductManageBinding>()

    val menuList: RecyclerView?
        get() = binding?.menuList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRemoteConfigValue()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@ProductManageBottomSheet)?.commit()
        }
    }

    fun init(
        productMenuListener: ProductMenuListener,
        sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener
    ) {
        this.menuAdapterListener = productMenuListener
        this.sellerFeatureCarouselListener = sellerFeatureCarouselListener
    }

    fun show(
        fm: FragmentManager,
        product: ProductUiModel,
        isPowerMerchantOrOfficialStore: Boolean
    ) {
        this.product = product
        this.isPowerMerchantOrOfficialStore = isPowerMerchantOrOfficialStore
        show(fm, TAG)
    }

    fun dismiss(fm: FragmentManager) {
        (fm.findFragmentByTag(TAG) as? BottomSheetUnify)?.dismissAllowingStateLoss()
    }

    private fun initRemoteConfigValue() {
        isProductCouponEnabled =
            try {
                context?.let {
                    FirebaseRemoteConfigImpl(it).getBoolean(
                        RemoteConfigKey.ENABLE_MVC_PRODUCT,
                        true
                    )
                }.orFalse()
            } catch (ex: Exception) {
                false
            }
    }

    private fun setupView(binding: BottomSheetProductManageBinding) {
        setupSellerCarousel(binding)
        setupMenuAdapter(binding)
    }

    private fun setupMenuAdapter(binding: BottomSheetProductManageBinding) = binding.run {
        menuAdapterListener?.let {
            val menuList = menuList
            menuAdapter = ProductMenuAdapter(it)
            menuList.adapter = menuAdapter
            menuList.layoutManager = LinearLayoutManager(requireContext())
        }

        product?.let { product ->
            val menu = createProductManageMenu(
                product,
                isPowerMerchantOrOfficialStore,
                isProductCouponEnabled
            )

            if (!ProductManageConfig.IS_SELLER_APP) {
                val sellerFeatureList = createSellerFeatureList(product)
                sellerFeatureCarousel.setItems(sellerFeatureList)
            }

            menuAdapter?.clearAllElements()
            menuAdapter?.addElement(menu)
            menuLayoutManager = menuList.layoutManager as LinearLayoutManager
        }
    }

    private fun setupSellerCarousel(binding: BottomSheetProductManageBinding) =
        binding.sellerFeatureCarousel.run {
            this@ProductManageBottomSheet.sellerFeatureCarousel = this
            if (!ProductManageConfig.IS_SELLER_APP) {
                show()
                setListener(sellerFeatureCarouselListener)
                this.addItemDecoration()
            }
        }

    private fun setupChildView(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomSheetProductManageBinding.inflate(
            inflater,
            container,
            false
        ).also {
            setupView(it)
        }
        val menuTitle = context?.getString(R.string.product_manage_bottom_sheet_title).orEmpty()
        setTitle(menuTitle)
        setChild(binding?.root)
    }

    private fun createProductManageMenu(
        product: ProductUiModel,
        isPowerMerchantOrOfficialStore: Boolean,
        isProductCouponEnabled: Boolean
    ): List<Visitable<*>> {
        val menuList = mutableListOf<Visitable<*>>()

        access?.run {
            menuList.apply {
                val isFeatured = product.isFeatured == true

                add(Preview(product))

                if (duplicateProduct) {
                    add(Duplicate(product))
                }

                if (ProductManageConfig.IS_SELLER_APP && setStockReminder) {
                    add(StockReminder(product))
                }

                if (deleteProduct) {
                    add(Delete(product))
                }

                if (ProductManageConfig.IS_SELLER_APP) {
                    add(ProductItemDivider)

                    if (setTopAds) {
                        when {
                            product.hasTopAds() -> add(SeeTopAds(product))
                            else -> add(SetTopAds(product))
                        }
                    }

                    if (isProductCouponEnabled) {
                        add(CreateProductCoupon(product))
                    }

                    if (broadcastChat) {
                        add(CreateBroadcastChat(product))
                    }

                    if (isFeatured && isPowerMerchantOrOfficialStore && setFeatured) {
                        add(RemoveFeaturedProduct(product))
                    }

                    if (!isFeatured && product.isActive() && setFeatured) {
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
                if (multiSelect) {
                    add(SellerFeatureUiModel.MultiEditFeatureWithDataUiModel(product))
                }

                if (setTopAds) {
                    add(SellerFeatureUiModel.TopAdsFeatureWithDataUiModel(product))
                }

                if (setFeatured) {
                    add(SellerFeatureUiModel.FeaturedProductFeatureWithDataUiModel(product))
                }

                if (setStockReminder) {
                    add(SellerFeatureUiModel.StockReminderFeatureWithDataUiModel(product))
                }

                if (broadcastChat) {
                    add(SellerFeatureUiModel.BroadcastChatProductManageUiModel(product))
                }
            }
        }

        return featureList
    }
}
