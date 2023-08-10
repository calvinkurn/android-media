package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.databinding.FragmentShopProductCarouselBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeProductCarouselAdapter
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlin.collections.ArrayList

class ShopProductCarouselFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_WIDGETS = "widgets"

        @JvmStatic
        fun newInstance(
            products: List<ShopHomeProductCarouselUiModel.Tab.Component>
        ): ShopProductCarouselFragment {
            return ShopProductCarouselFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(BUNDLE_KEY_WIDGETS, ArrayList(products))
                }
            }
        }

    }


    private val widgets by lazy {
        arguments?.getParcelableArrayList<ShopHomeProductCarouselUiModel.Tab.Component>(
            BUNDLE_KEY_WIDGETS
        )?.toList().orEmpty()
    }

    private var onMainBannerClick : (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit = {}
    private var onProductClick : (ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild) -> Unit = {}

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

        val mainBanner = widgets.firstOrNull { widget ->  widget.type == ShopHomeProductCarouselUiModel.ComponentType.BANNER_SINGLE}
        val products = widgets.takeWhile { widgets ->
            widgets.type == ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITH_PRODUCT_INFO
                ||
                widgets.type == ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITHOUT_PRODUCT_INFO
        }

        setupMainBanner(mainBanner)
        setupProductRecyclerView(products)
    }

    private fun setupMainBanner(mainBanner: ShopHomeProductCarouselUiModel.Tab.Component?) {
        val hasMainBanner = mainBanner != null
        if (hasMainBanner) {
            val mainBannerImageUrl = mainBanner?.componentChild?.getOrNull(0)?.imageUrl.orEmpty()
            if (mainBannerImageUrl.isNotEmpty()) {
                binding?.imgMainBanner?.visible()
                binding?.imgMainBanner?.loadImage(mainBannerImageUrl)
            }
        }
    }

    private fun setupProductRecyclerView(products: List<ShopHomeProductCarouselUiModel.Tab.Component>) {
        binding?.recyclerView?.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        productAdapter.setOnProductClick { selectedProduct ->
            onProductClick(selectedProduct)
        }

        val formattedProducts =
            mutableListOf<ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild>()

        products.forEach { component ->

            component.componentChild.map {componentChild ->

                formattedProducts.add(
                    ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild(
                        imageId = 0,
                        imageUrl = "",
                        ctaLink = "",
                        linkId = componentChild.linkId,
                        linkType = componentChild.linkType
                    )
                )
            }

        }

        productAdapter.submit(formattedProducts)
    }

    fun setOnMainBannerClick(onMainBannerClick: (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit) {
        this.onMainBannerClick = onMainBannerClick
    }

    fun setOnProductClick(onProductClick: (ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild) -> Unit) {
        this.onProductClick = onProductClick
    }

}
