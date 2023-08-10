package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.FragmentShopProductCarouselBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeProductCarouselAdapter
import com.tokopedia.shop.home.view.model.Product
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopProductCarouselViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.collections.ArrayList

class ShopProductCarouselFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val BUNDLE_KEY_WIDGETS = "widgets"
        private const val SORT_ID_MOST_SOLD = 8
        private const val SORT_ID_NEWEST = 2

        @JvmStatic
        fun newInstance(
            shopId: String,
            widgets: List<ShopHomeProductCarouselUiModel.Tab.Component>
        ): ShopProductCarouselFragment {
            return ShopProductCarouselFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                    putParcelableArrayList(BUNDLE_KEY_WIDGETS, ArrayList(widgets))
                }
            }
        }

    }


    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID).orEmpty() }
    private val widgets by lazy {
        arguments?.getParcelableArrayList<ShopHomeProductCarouselUiModel.Tab.Component>(
            BUNDLE_KEY_WIDGETS
        )?.toList().orEmpty()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopProductCarouselViewModel::class.java] }

    private var onMainBannerClick : (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit = {}
    private var onProductClick : (Product) -> Unit = {}

    private var binding by autoClearedNullable<FragmentShopProductCarouselBinding>()
    private val productAdapter = ShopHomeProductCarouselAdapter()

    override fun getScreenName(): String = ShopProductCarouselFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopProductCarouselFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopProductCarouselBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProducts()
        setupMainBanner()
        setupProductRecyclerView()
        getProducts()
    }

    private fun setupMainBanner() {
        val mainBanner = widgets.firstOrNull { widget -> widget.type == ShopHomeProductCarouselUiModel.ComponentType.BANNER_SINGLE }

        val hasMainBanner = mainBanner != null
        if (hasMainBanner) {
            val mainBannerImageUrl = mainBanner?.componentChild?.getOrNull(0)?.imageUrl.orEmpty()
            if (mainBannerImageUrl.isNotEmpty()) {
                binding?.imgMainBanner?.visible()
                binding?.imgMainBanner?.loadImage(mainBannerImageUrl)
            }
        }
    }

    private fun setupProductRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        productAdapter.setOnProductClick { selectedProduct ->
            onProductClick(selectedProduct)
        }

        val showProductInfo = widgets
            .filter { widget -> widget.type == ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITH_PRODUCT_INFO }
            .size
            .isMoreThanZero()

        productAdapter.setShowProductInfo(showProductInfo)
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> renderProducts(result.data)
                is Fail -> {}
            }
        }
    }


    private fun getProducts() {
        val products = widgets.takeWhile { widgets ->
            widgets.type == ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITH_PRODUCT_INFO
                ||
                widgets.type == ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITHOUT_PRODUCT_INFO
        }

        val firstProduct = products.getOrNull(0)?.componentChild
        val firstProductLinkType = firstProduct?.getOrNull(0)?.linkType.orEmpty()

        val sortId = when (firstProductLinkType) {
            "terlaris" -> SORT_ID_MOST_SOLD
            "terbaru" -> SORT_ID_NEWEST
            else -> SORT_ID_MOST_SOLD
        }

        val userAddress = ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
        viewModel.getShopProduct(sortId, shopId, userAddress)
    }
    private fun renderProducts(products: List<Product>) {
        productAdapter.submit(products)
    }

    fun setOnMainBannerClick(onMainBannerClick: (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit) {
        this.onMainBannerClick = onMainBannerClick
    }

    fun setOnProductClick(onProductClick: (Product) -> Unit) {
        this.onProductClick = onProductClick
    }

}
