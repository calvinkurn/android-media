package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.LayoutShopHomeV4TerlarisWidgetBinding
import com.tokopedia.shop.home.util.loadImageRounded
import com.tokopedia.shop.home.view.adapter.ShopHomeV4TerlarisAdapter
import com.tokopedia.shop.home.view.model.ShopHomeV4TerlarisItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeV4TerlarisUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeV4TerlarisViewHolder(
    itemView: View,
    private val listener: ShopHomeV4TerlarisViewHolderListener
) : AbstractViewHolder<ShopHomeV4TerlarisUiModel>(itemView) {

    interface ShopHomeV4TerlarisViewHolderListener {
        fun onProductClick(productId: String)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_v4_terlaris_widget
    }

    init {
        initView()
        initRecyclerView()
    }

    private val viewBinding: LayoutShopHomeV4TerlarisWidgetBinding? by viewBinding()
    private val PRODUCT_ZERO = 0
    private val PRODUCT_THREE = 3
    private val PRODUCT_SIX = 6
    private val PRODUCT_NINE = 9
    private var rvProductCarousel: RecyclerView? = null
    private var widgetTitle: TextView? = null
    private var widgetSubtitle: TextView? = null
    private var containerThreeProducts: ConstraintLayout? = null
    private var terlarisWidgetContainer: ConstraintLayout? = null
    private var terlarisWidgetAdapter: ShopHomeV4TerlarisAdapter? = null
    private var prodcutCard1: ConstraintLayout? = null
    private var productImg1: ImageUnify? = null
    private var productName1: TextView? = null
    private var productPrice1: TextView? = null
    private var prodcutCard2: ConstraintLayout? = null
    private var productImg2: ImageUnify? = null
    private var productName2: TextView? = null
    private var productPrice2: TextView? = null
    private var prodcutCard3: ConstraintLayout? = null
    private var productImg3: ImageUnify? = null
    private var productName3: TextView? = null
    private var productPrice3: TextView? = null

    override fun bind(element: ShopHomeV4TerlarisUiModel?) {
        element?.let {
            if (it.productList.size.orZero() == PRODUCT_ZERO) {
                // Hide the widget if the product is empty
                hideTheContainer()
            } else if (it.productList.size.orZero() == PRODUCT_THREE) {
                // Show product list with total 3
                showTheContainer()
                showLayoutThreeItem(productList = it.productList)
                hideHorizontalProductCarousel()
            } else if (it.productList.size.orZero() == PRODUCT_SIX || it.productList.size.orZero() == PRODUCT_NINE) {
                // Show product list with total 6 or 9
                showTheContainer()
                hideLayoutThreeItem()
                showHorizontalProductCarousel()
                val productListCarouselData = getCarouselData(it.productList)
                terlarisWidgetAdapter?.updateData(productList = productListCarouselData)
            } else {
                hideTheContainer()
            }
        }
    }

    private fun initView() {
        rvProductCarousel = viewBinding?.rvTerlarisWidget
        widgetTitle = viewBinding?.terlarisWidgetTitle
        widgetSubtitle = viewBinding?.terlarisWidgetSubtitle
        terlarisWidgetContainer = viewBinding?.shopHomeV4TerlarisWidgetContainer
        containerThreeProducts = viewBinding?.terlarisContainerItemProduct
        prodcutCard1 = viewBinding?.terlarisProductDetail1
        productImg1 = viewBinding?.terlarisImgProduct1
        productName1 = viewBinding?.terlarisProductName1
        productPrice1 = viewBinding?.terlarisProductPrice1
        prodcutCard2 = viewBinding?.terlarisProductDetail2
        productImg2 = viewBinding?.terlarisImgProduct2
        productName2 = viewBinding?.terlarisProductName2
        productPrice2 = viewBinding?.terlarisProductPrice2
        prodcutCard3 = viewBinding?.terlarisProductDetail3
        productImg3 = viewBinding?.terlarisImgProduct3
        productName3 = viewBinding?.terlarisProductName3
        productPrice3 = viewBinding?.terlarisProductPrice3
        terlarisWidgetAdapter = ShopHomeV4TerlarisAdapter(listener)
    }

    private fun initRecyclerView() {
        rvProductCarousel?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = terlarisWidgetAdapter
        }
    }

    private fun hideTheContainer() {
        // Hide the widget due to product list equals to 0
        terlarisWidgetContainer?.visibility = View.GONE
    }
    private fun showTheContainer() {
        terlarisWidgetContainer?.visibility = View.VISIBLE
    }

    private fun showLayoutThreeItem(productList: List<ShopHomeV4TerlarisItemUiModel>) {
        containerThreeProducts?.visibility = View.VISIBLE
        prodcutCard1?.setOnClickListener {
            listener.onProductClick(productList[0].id)
        }
        productImg1?.loadImageRounded(url = productList[0].imgUrl)
        productName1?.text = productList[0].name
        productPrice1?.text = productList[0].price
        prodcutCard2?.setOnClickListener {
            listener.onProductClick(productList[1].id)
        }
        productImg2?.loadImageRounded(url = productList[1].imgUrl)
        productName2?.text = productList[1].name
        productPrice2?.text = productList[1].price
        prodcutCard3?.setOnClickListener {
            listener.onProductClick(productList[2].id)
        }
        productImg3?.loadImageRounded(url = productList[2].imgUrl)
        productName3?.text = productList[2].name
        productPrice3?.text = productList[2].price
    }

    private fun getCarouselData(productList: List<ShopHomeV4TerlarisItemUiModel>): List<List<ShopHomeV4TerlarisItemUiModel>> {
        val chunkSize = 3
        return productList.chunked(chunkSize)
    }

    private fun hideLayoutThreeItem() {
        containerThreeProducts?.visibility = View.GONE
    }

    private fun showHorizontalProductCarousel() {
        rvProductCarousel?.visibility = View.VISIBLE
    }

    private fun hideHorizontalProductCarousel() {
        rvProductCarousel?.visibility = View.GONE
    }
}
