package com.tokopedia.shop.home.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.FragmentShopBannerProductGroupWidgetTabBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeBannerProductGroupTabAdapter
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShimmerItemType
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShopHomeBannerProductGroupItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType
import com.tokopedia.shop.home.view.viewmodel.ShopBannerProductGroupWidgetTabViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.ComponentType

class ShopBannerProductGroupWidgetTabFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val BUNDLE_KEY_WIDGETS = "widgets"
        private const val BUNDLE_KEY_WIDGET_STYLE = "widget_style"
        private const val BUNDLE_KEY_OVERRIDE_THEME = "override_theme"
        private const val BUNDLE_KEY_COLOR_SCHEME = "color_scheme"
        private const val BUNDLE_KEY_TAB_LABEL = "tab_label"

        @JvmStatic
        fun newInstance(
            shopId: String,
            widgets: List<ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList>,
            widgetStyle: String,
            overrideTheme: Boolean,
            colorScheme: ShopPageColorSchema,
            tabLabel: String
        ): ShopBannerProductGroupWidgetTabFragment {
            return ShopBannerProductGroupWidgetTabFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                    putParcelableArrayList(BUNDLE_KEY_WIDGETS, ArrayList(widgets))
                    putString(BUNDLE_KEY_WIDGET_STYLE, widgetStyle)
                    putBoolean(BUNDLE_KEY_OVERRIDE_THEME, overrideTheme)
                    putParcelable(BUNDLE_KEY_COLOR_SCHEME, colorScheme)
                    putString(BUNDLE_KEY_TAB_LABEL, tabLabel)
                }
            }
        }

    }


    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID).orEmpty() }
    private val widgets by lazy {
        arguments?.getParcelableArrayList<ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList>(
            BUNDLE_KEY_WIDGETS
        )?.toList().orEmpty()
    }
    private val widgetStyle by lazy { arguments?.getString(BUNDLE_KEY_WIDGET_STYLE).orEmpty() }
    private val overrideTheme by lazy { arguments?.getBoolean(BUNDLE_KEY_OVERRIDE_THEME).orFalse() }

    private val colorScheme by lazy {
        arguments?.getParcelable(BUNDLE_KEY_COLOR_SCHEME) ?: ShopPageColorSchema()
    }
    private val tabLabel by lazy { arguments?.getString(BUNDLE_KEY_TAB_LABEL).orEmpty() }
    private var onProductSuccessfullyLoaded: (Boolean) -> Unit = {}

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopBannerProductGroupWidgetTabViewModel::class.java] }

    private var onMainBannerClick : (ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data) -> Unit = {}
    private var onProductClick : (ProductItemType) -> Unit = {}
    private var onVerticalBannerClick : (VerticalBannerItemType) -> Unit = {}

    private var binding by autoClearedNullable<FragmentShopBannerProductGroupWidgetTabBinding>()
    private val bannerProductGroupAdapter = ShopHomeBannerProductGroupTabAdapter()

    override fun getScreenName(): String = ShopBannerProductGroupWidgetTabFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopBannerProductGroupWidgetTabFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBannerProductGroupWidgetTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCarouselsWidgets()
        setupRecyclerView()
        getCarouselWidgets()
    }

    private fun showMainBanner() {
        val singleBanners = widgets.firstOrNull { widget -> widget.componentType == ComponentType.DISPLAY_SINGLE_COLUMN }

        val hasMainBanner = singleBanners != null
        if (hasMainBanner) {
            val mainBanner = singleBanners?.data?.getOrNull(0)
            val mainBannerImageUrl = mainBanner?.imageUrl.orEmpty()

            if (mainBannerImageUrl.isNotEmpty()) {
                binding?.imgMainBanner?.visible()
                binding?.imgMainBanner?.loadImage(mainBannerImageUrl)
                binding?.imgMainBanner?.setOnClickListener {
                    onMainBannerClick(mainBanner ?: return@setOnClickListener)
                }
            }

        } else {
            binding?.imgMainBanner?.gone()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = bannerProductGroupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        bannerProductGroupAdapter.setOnProductClick { selectedProduct ->
            onProductClick(selectedProduct)
        }

        bannerProductGroupAdapter.setOnVerticalBannerClick { verticalBanner ->
            onVerticalBannerClick(verticalBanner)
        }
    }

    private fun observeCarouselsWidgets() {
        viewModel.carouselWidgets.observe(viewLifecycleOwner) { result ->
            when(result) {
                ShopBannerProductGroupWidgetTabViewModel.UiState.Loading -> {
                    bannerProductGroupAdapter.submit(listOf(ShimmerItemType(showShimmer = true)))
                }

                is ShopBannerProductGroupWidgetTabViewModel.UiState.Success -> {
                    println("Fetch products: Success fetching $tabLabel. Size is ${result.data.size }. Data: ${result.data}")
                    showResult(result.data)
                    onProductSuccessfullyLoaded(true)
                }

                is ShopBannerProductGroupWidgetTabViewModel.UiState.Error -> {
                    removeShimmerFromProductCarousel()
                    onProductSuccessfullyLoaded(false)
                }
            }
        }
    }

    private fun showResult(carouselItems: List<ShopHomeBannerProductGroupItemType>) {
        if (carouselItems.isEmpty()) {
            removeShimmerFromProductCarousel()
        } else {
            showProductCarousel(carouselItems)
            showMainBanner()
        }
    }

    private fun removeShimmerFromProductCarousel() {
        bannerProductGroupAdapter.submit(listOf(ShimmerItemType(showShimmer = false)))
    }

    @SuppressLint("PII Data Exposure")
    private fun getCarouselWidgets() {
        val userAddress = ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
        viewModel.getCarouselWidgets(widgets, shopId, userAddress, widgetStyle, overrideTheme, colorScheme)
    }

    private fun showProductCarousel(carouselItems: List<ShopHomeBannerProductGroupItemType>) {
        bannerProductGroupAdapter.submit(carouselItems)
    }

    fun setOnMainBannerClick(onMainBannerClick: (ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data) -> Unit) {
        this.onMainBannerClick = onMainBannerClick
    }

    fun setOnVerticalBannerClick(onVerticalBannerClick: (VerticalBannerItemType) -> Unit) {
        this.onVerticalBannerClick = onVerticalBannerClick
    }

    fun setOnProductClick(onProductClick: (ProductItemType) -> Unit) {
        this.onProductClick = onProductClick
    }

    fun setOnProductSuccessfullyLoaded(onProductSuccessfullyLoaded: (Boolean) -> Unit) {
        this.onProductSuccessfullyLoaded = onProductSuccessfullyLoaded
    }
}
