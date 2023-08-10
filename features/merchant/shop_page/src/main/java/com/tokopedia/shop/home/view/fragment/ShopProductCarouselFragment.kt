package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.databinding.FragmentShopProductCarouselBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlin.collections.ArrayList

class ShopProductCarouselFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_MAIN_BANNER = "main_banner"
        private const val BUNDLE_KEY_PRODUCTS = "products"

        @JvmStatic
        fun newInstance(
            mainBanner: ShopHomeProductCarouselUiModel.Tab.Component,
            products: List<ShopHomeProductCarouselUiModel.Tab.Component>
        ): ShopProductCarouselFragment {
            return ShopProductCarouselFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_MAIN_BANNER, mainBanner)
                    putParcelableArrayList(BUNDLE_KEY_PRODUCTS, ArrayList(products))
                }
            }
        }

    }

    private val mainBanner by lazy {
        arguments?.getParcelable<ShopHomeProductCarouselUiModel.Tab.Component>(
            BUNDLE_KEY_MAIN_BANNER
        )
    }
    private val products by lazy {
        arguments?.getParcelableArrayList<ShopHomeProductCarouselUiModel.Tab.Component>(
            BUNDLE_KEY_PRODUCTS
        )?.toList().orEmpty()
    }

    private var onMainBannerClick : (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit = {}
    private var onProductClick : (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit = {}

    private var binding by autoClearedNullable<FragmentShopProductCarouselBinding>()

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
    }

    fun setOnMainBannerClick(onMainBannerClick: (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit) {
        this.onMainBannerClick = onMainBannerClick
    }

    fun setOnProductClick(onProductClick: (ShopHomeProductCarouselUiModel.Tab.Component) -> Unit) {
        this.onProductClick = onProductClick
    }

}
