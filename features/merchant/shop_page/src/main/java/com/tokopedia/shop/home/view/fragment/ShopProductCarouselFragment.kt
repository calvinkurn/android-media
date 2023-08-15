package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
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
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselProductCardVerticalBanner
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerItemType
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner
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

        @JvmStatic
        fun newInstance(
            shopId: String,
            widgets: List<ShopHomeProductCarouselUiModel.Tab.ComponentList>
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
        arguments?.getParcelableArrayList<ShopHomeProductCarouselUiModel.Tab.ComponentList>(
            BUNDLE_KEY_WIDGETS
        )?.toList().orEmpty()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopProductCarouselViewModel::class.java] }

    private var onMainBannerClick : (ShopHomeProductCarouselUiModel.Tab.ComponentList.Data) -> Unit = {}
    private var onProductClick : (ShopHomeProductCarouselProductCardVerticalBanner) -> Unit = {}
    private var onVerticalBannerClick : (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit = {}

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
        observeCarouselsWidgets()
        setupMainBanner()
        setupProductRecyclerView()
        getCarouselWidgets()
    }

    private fun setupMainBanner() {
        val singleBanners = widgets.firstOrNull { widget -> widget.componentType == ShopHomeProductCarouselUiModel.ComponentType.BANNER_SINGLE }

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

    private fun setupProductRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        productAdapter.setOnProductClick { selectedProduct ->
            onProductClick(selectedProduct)
        }

        productAdapter.setOnVerticalBannerClick { verticalBanner ->
            onVerticalBannerClick(verticalBanner)
        }

        val showProductInfo = widgets
            .filter { widget -> widget.componentType == ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITH_PRODUCT_INFO }
            .size
            .isMoreThanZero()

        productAdapter.setShowProductInfo(showProductInfo)
    }

    private fun observeCarouselsWidgets() {
        viewModel.carouselWidgets.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> renderCarouselWidgets(result.data)
                is Fail -> {}
            }
        }
    }


    private fun getCarouselWidgets() {
        val userAddress = ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
        viewModel.getCarouselWidgets(widgets, shopId, userAddress)
    }

    private fun renderCarouselWidgets(widgets: List<ShopHomeProductCarouselVerticalBannerItemType>) {
        productAdapter.submit(widgets)
    }

    fun setOnMainBannerClick(onMainBannerClick: (ShopHomeProductCarouselUiModel.Tab.ComponentList.Data) -> Unit) {
        this.onMainBannerClick = onMainBannerClick
    }

    fun setOnVerticalBannerClick(onVerticalBannerClick: (ShopHomeProductCarouselVerticalBannerVerticalBanner) -> Unit) {
        this.onVerticalBannerClick = onVerticalBannerClick
    }

    fun setOnProductClick(onProductClick: (ShopHomeProductCarouselProductCardVerticalBanner) -> Unit) {
        this.onProductClick = onProductClick
    }

}
