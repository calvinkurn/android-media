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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.FragmentShopBannerProductGroupWidgetTabBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.ShopHomeBannerProductGroupTabRecyclerViewAdapter
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShimmerItemType
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShopHomeBannerProductGroupItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType
import com.tokopedia.shop.home.view.viewmodel.ShopBannerProductGroupWidgetTabViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel.Tab.ComponentList.ComponentName
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel.WidgetStyle

class ShopBannerProductGroupWidgetTabFragment : BaseDaggerFragment() {
    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val BUNDLE_KEY_WIDGETS = "widgets"
        private const val BUNDLE_KEY_WIDGET_STYLE = "widget_style"
        private const val BUNDLE_KEY_OVERRIDE_THEME = "override_theme"
        private const val BUNDLE_KEY_IS_FESTIVITY = "is_festivity"
        private const val BUNDLE_KEY_COLOR_SCHEME = "color_scheme"
        private const val CORNER_RADIUS_IMAGE_BANNER = 12f
        private const val BUNDLE_KEY_BACKGROUND_COLOR = "background_color"
        private const val BUNDLE_KEY_PATTERN_COLOR_TYPE = "pattern_color"

        @JvmStatic
        fun newInstance(
            shopId: String,
            widgets: List<BannerProductGroupUiModel.Tab.ComponentList>,
            widgetStyle: String,
            overrideTheme: Boolean,
            colorScheme: ShopPageColorSchema,
            backgroundColor: String,
            patternColorType: String,
            isFestivity: Boolean
        ): ShopBannerProductGroupWidgetTabFragment {
            return ShopBannerProductGroupWidgetTabFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                    putParcelableArrayList(BUNDLE_KEY_WIDGETS, ArrayList(widgets))
                    putString(BUNDLE_KEY_WIDGET_STYLE, widgetStyle)
                    putBoolean(BUNDLE_KEY_OVERRIDE_THEME, overrideTheme)
                    putBoolean(BUNDLE_KEY_IS_FESTIVITY, isFestivity)
                    putParcelable(BUNDLE_KEY_COLOR_SCHEME, colorScheme)
                    putString(BUNDLE_KEY_BACKGROUND_COLOR, backgroundColor)
                    putString(BUNDLE_KEY_PATTERN_COLOR_TYPE, patternColorType)
                }
            }
        }

    }

    private val horizontalMainBannerCornerRadiusPx by lazy { CORNER_RADIUS_IMAGE_BANNER.toPx() }
    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID).orEmpty() }
    private val widgets by lazy {
        arguments?.getParcelableArrayList<BannerProductGroupUiModel.Tab.ComponentList>(
            BUNDLE_KEY_WIDGETS
        )?.toList().orEmpty()
    }
    private val widgetStyle by lazy { arguments?.getString(BUNDLE_KEY_WIDGET_STYLE).orEmpty() }
    private val overrideTheme by lazy { arguments?.getBoolean(BUNDLE_KEY_OVERRIDE_THEME).orFalse() }
    private val isFestivity by lazy { arguments?.getBoolean(BUNDLE_KEY_IS_FESTIVITY).orFalse() }

    private val colorScheme by lazy {
        arguments?.getParcelable(BUNDLE_KEY_COLOR_SCHEME) ?: ShopPageColorSchema()
    }
    private val backgroundColor by lazy { arguments?.getString(BUNDLE_KEY_BACKGROUND_COLOR).orEmpty() }
    private val patternColorType by lazy { arguments?.getString(BUNDLE_KEY_PATTERN_COLOR_TYPE).orEmpty() }
    private var onProductSuccessfullyLoaded: (Boolean) -> Unit = {}

    @Inject
    lateinit var tracker: ShopPageHomeTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopBannerProductGroupWidgetTabViewModel::class.java] }

    private var onMainBannerClick : (BannerProductGroupUiModel.Tab.ComponentList.Data) -> Unit = {}
    private var onProductClick : (ProductItemType) -> Unit = {}
    private var onVerticalBannerClick : (VerticalBannerItemType) -> Unit = {}

    private var binding by autoClearedNullable<FragmentShopBannerProductGroupWidgetTabBinding>()
    private val bannerProductGroupAdapter by lazy {
        ShopHomeBannerProductGroupTabRecyclerViewAdapter(
            isOverrideTheme = overrideTheme,
            backgroundColor = backgroundColor,
            patternColorType = patternColorType,
            isFestivity = isFestivity
        )
    }

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
        observeVerticalProductCarousel()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getCarouselWidgets()
        sendProductCarouselImpressionTracker(widgets)
    }

    private fun showMainBanner() {
        val displaySingleColumnComponent = widgets.firstOrNull { widget -> widget.componentName == ComponentName.DISPLAY_SINGLE_COLUMN }
        val displaySingleColumnComponentId = displaySingleColumnComponent?.componentId.toString()
        val displaySingleColumnComponentName = displaySingleColumnComponent?.componentName?.id?.lowercase().toString()

        val hasMainBanner = displaySingleColumnComponent != null
        val isHorizontalMainBanner = widgetStyle == WidgetStyle.HORIZONTAL.id
        val isUnspecifiedOrientationMainBanner = widgetStyle.isEmpty()
        val showMainBanner = (hasMainBanner && isHorizontalMainBanner) || (hasMainBanner && isUnspecifiedOrientationMainBanner) 
        
        if (showMainBanner) {
            binding?.spaceMainBannerTop?.visible()
            
            binding?.imgMainBanner?.visible()
            
            binding?.spaceProductCarouselTop?.visible()
            
            val mainBanner = displaySingleColumnComponent?.data?.getOrNull(0)
            val mainBannerImageUrl = mainBanner?.imageUrl.orEmpty()

            renderMainBannerImage(
                mainBannerImageUrl,
                displaySingleColumnComponentId,
                displaySingleColumnComponentName,
                mainBanner
            )

        } else {
            binding?.spaceMainBannerTop?.gone()
            binding?.imgMainBanner?.gone()
            binding?.spaceProductCarouselTop?.gone()
        }
    }

    private fun renderMainBannerImage(
        imageUrl: String,
        displaySingleColumnComponentId: String,
        displaySingleColumnComponentName: String,
        mainBanner: BannerProductGroupUiModel.Tab.ComponentList.Data?
    ) {
        if (imageUrl.isNotEmpty()) {
            binding?.imgMainBanner?.loadImage(imageUrl) {
                setRoundedRadius(horizontalMainBannerCornerRadiusPx)
            }
            binding?.imgMainBanner?.setOnClickListener {
                tracker.sendProductCarouselBannerClick(
                    displaySingleColumnComponentId,
                    displaySingleColumnComponentName,
                    WidgetStyle.HORIZONTAL.id,
                    shopId,
                    userSession.userId
                )
                onMainBannerClick(mainBanner ?: return@setOnClickListener)
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = bannerProductGroupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        bannerProductGroupAdapter.setOnProductClick { selectedProduct, index ->
            onProductClick(selectedProduct)
            tracker.sendProductCarouselProductCardClick(
                selectedProduct,
                widgetStyle,
                shopId,
                userSession.userId,
                index
            )
        }
        
        bannerProductGroupAdapter.setOnProductCardDrawn { productCardHeight -> 
            if (widgetStyle == WidgetStyle.VERTICAL.id) {
                viewModel.refreshVerticalBannerHeight(productCardHeight)   
            }
        }

        bannerProductGroupAdapter.setOnVerticalBannerClick { verticalBanner ->
            onVerticalBannerClick(verticalBanner)
            tracker.sendProductCarouselBannerClick(
                verticalBanner.componentId.orZero().toString(),
                verticalBanner.componentName?.id?.lowercase().toString(),
                WidgetStyle.VERTICAL.id,
                shopId,
                userSession.userId
            )
        }
    }

    private fun observeCarouselsWidgets() {
        viewModel.carouselWidgets.observe(viewLifecycleOwner) { result ->
            when(result) {
                ShopBannerProductGroupWidgetTabViewModel.UiState.Loading -> {
                    binding?.imgMainBanner?.gone()
                    bannerProductGroupAdapter.submit(listOf(ShimmerItemType(showShimmer = true)))
                }

                is ShopBannerProductGroupWidgetTabViewModel.UiState.Success -> {
                    showMainBanner()
                    showProducts(result.data)
                    onProductSuccessfullyLoaded(true)
                    tracker.sendProductCarouselImpression(
                        widgetStyle,
                        widgets,
                        result.data,
                        shopId,
                        userSession.userId
                    )
                }

                is ShopBannerProductGroupWidgetTabViewModel.UiState.Error -> {
                    removeShimmerFromProductCarousel()
                    onProductSuccessfullyLoaded(false)
                }
            }
        }
    }

    private fun observeVerticalProductCarousel() {
        viewModel.verticalProductCarousel.observe(viewLifecycleOwner) { verticalProductCarousels ->
            showProducts(verticalProductCarousels)
        }
    }

    private fun showProducts(carouselItems: List<ShopHomeBannerProductGroupItemType>) {
        if (carouselItems.isEmpty()) {
            removeShimmerFromProductCarousel()
        } else {
            showProductCarousel(carouselItems)
        }
    }

    private fun removeShimmerFromProductCarousel() {
        bannerProductGroupAdapter.submit(listOf(ShimmerItemType(showShimmer = false)))
    }

    @SuppressLint("PII Data Exposure")
    private fun getCarouselWidgets() {
        val userAddress = ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
        viewModel.getCarouselWidgets(
            widgets = widgets,
            shopId = shopId,
            userAddress = userAddress,
            widgetStyle = widgetStyle,
            overrideTheme = overrideTheme,
            colorSchema = colorScheme
        )
    }

    private fun showProductCarousel(carouselItems: List<ShopHomeBannerProductGroupItemType>) {
        bannerProductGroupAdapter.submit(carouselItems)
    }

    fun setOnMainBannerClick(onMainBannerClick: (BannerProductGroupUiModel.Tab.ComponentList.Data) -> Unit) {
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

    private fun sendProductCarouselImpressionTracker(widgets: List<BannerProductGroupUiModel.Tab.ComponentList>) {
        tracker.sendBannerOnProductCarouselImpression(
            widgetStyle,
            widgets,
            shopId,
            userSession.userId
        )
    }
}
