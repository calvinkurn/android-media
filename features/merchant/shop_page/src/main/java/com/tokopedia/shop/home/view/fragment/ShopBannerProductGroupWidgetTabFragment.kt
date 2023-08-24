package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.FragmentShopBannerProductGroupWidgetTabBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeBannerProductGroupTabAdapter
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselProductCard
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselShimmer
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerItemType
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner
import com.tokopedia.shop.home.view.viewmodel.ShopBannerProductGroupWidgetTabViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel.Tab.ComponentList.ComponentType

class ShopBannerProductGroupWidgetTabFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val BUNDLE_KEY_WIDGETS = "widgets"

        @JvmStatic
        fun newInstance(
            shopId: String,
            widgets: List<ShopHomeProductCarouselUiModel.Tab.ComponentList>
        ): ShopBannerProductGroupWidgetTabFragment {
            return ShopBannerProductGroupWidgetTabFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                    putParcelableArrayList(BUNDLE_KEY_WIDGETS, ArrayList(widgets))
                }
            }
        }

    }


    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID).orEmpty() }
    private val widgets by lazy {
        arguments?.getParcelableArrayList<ShopHomeProductCarouselUiModel.Tab.ComponentList>(
            BUNDLE_KEY_WIDGETS
        )?.toList().orEmpty()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopBannerProductGroupWidgetTabViewModel::class.java] }

    private var onMainBannerClick : (ShopHomeProductCarouselUiModel.Tab.ComponentList.Data) -> Unit = {}
    private var onProductClick : (ShopHomeProductCarouselProductCard) -> Unit = {}
    private var onVerticalBannerClick : (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit = {}

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
                    bannerProductGroupAdapter.submit(listOf(ShopHomeProductCarouselShimmer))
                }

                is ShopBannerProductGroupWidgetTabViewModel.UiState.Success -> {
                    showProductCarousel(result.data)
                    showMainBanner()
                }

                is ShopBannerProductGroupWidgetTabViewModel.UiState.Error -> {
                    bannerProductGroupAdapter.submit(emptyList())
                }
            }
        }
    }


    private fun getCarouselWidgets() {
        val userAddress = ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
        viewModel.getCarouselWidgets(widgets, shopId, userAddress)
    }

    private fun showProductCarousel(widgets: List<ShopHomeProductCarouselVerticalBannerItemType>) {
        bannerProductGroupAdapter.submit(widgets)
    }

    fun setOnMainBannerClick(onMainBannerClick: (ShopHomeProductCarouselUiModel.Tab.ComponentList.Data) -> Unit) {
        this.onMainBannerClick = onMainBannerClick
    }

    fun setOnVerticalBannerClick(onVerticalBannerClick: (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit) {
        this.onVerticalBannerClick = onVerticalBannerClick
    }

    fun setOnProductClick(onProductClick: (ShopHomeProductCarouselProductCard) -> Unit) {
        this.onProductClick = onProductClick
    }
}
