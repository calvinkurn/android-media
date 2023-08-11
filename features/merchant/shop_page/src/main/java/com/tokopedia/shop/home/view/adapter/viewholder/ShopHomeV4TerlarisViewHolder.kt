package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.LayoutShopHomeV4TerlarisWidgetBinding
import com.tokopedia.shop.home.util.loadImageRounded
import com.tokopedia.shop.home.view.adapter.ShopHomeV4TerlarisAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeV4TerlarisUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeV4TerlarisViewHolder(
    itemView: View,
    private val listener: ShopHomeV4TerlarisViewHolderListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    interface ShopHomeV4TerlarisViewHolderListener {
        fun onProductClick(productId: String)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_v4_terlaris_widget
    }

//    init {
//        initView()
//    }

    private val viewBinding: LayoutShopHomeV4TerlarisWidgetBinding? by viewBinding()
    private val PRODUCT_ZERO = 0
    private val PRODUCT_THREE = 3
    private val PRODUCT_SIX = 6
    private val PRODUCT_NINE = 9
    private var rvProductCarousel: RecyclerView? = viewBinding?.rvTerlarisWidget
    private var terlarisWidgetAdapter: ShopHomeV4TerlarisAdapter? = null
    private var widgetTitle: TextView? = viewBinding?.terlarisWidgetTitle
    private var widgetSubtitle: TextView? = viewBinding?.terlarisWidgetSubtitle
    private var terlarisWidgetContainer: ConstraintLayout? = viewBinding?.terlarisWidgetContainer
    private var containerThreeProducts: ConstraintLayout? = viewBinding?.terlarisContainerItemProduct
    private var prodcutCard1: ConstraintLayout? = viewBinding?.terlarisProductDetail1
    private var productImg1: ImageUnify? = viewBinding?.terlarisImgProduct1
    private var productName1: TextView? = viewBinding?.terlarisProductName1
    private var productPrice1: TextView? = viewBinding?.terlarisProductPrice1
    private var productRank1: TextView? = viewBinding?.terlarisProductRankNumber1
    private var prodcutCard2: ConstraintLayout? = viewBinding?.terlarisProductDetail2
    private var productImg2: ImageUnify? = viewBinding?.terlarisImgProduct2
    private var productName2: TextView? = viewBinding?.terlarisProductName2
    private var productPrice2: TextView? = viewBinding?.terlarisProductPrice2
    private var productRank2: TextView? = viewBinding?.terlarisProductRankNumber2
    private var prodcutCard3: ConstraintLayout? = viewBinding?.terlarisProductDetail3
    private var productImg3: ImageUnify? = viewBinding?.terlarisImgProduct3
    private var productName3: TextView? = viewBinding?.terlarisProductName3
    private var productPrice3: TextView? = viewBinding?.terlarisProductPrice3
    private var productRank3: TextView? = viewBinding?.terlarisProductRankNumber3

//    private fun initView() {
//        terlarisWidgetAdapter = ShopHomeV4TerlarisAdapter(listener)
//        rvProductCarousel?.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = terlarisWidgetAdapter
//        }
//    }

    private fun hideTheContainer() {
        // Hide the widget due to product list equals to 0
        terlarisWidgetContainer?.visibility = View.GONE
    }
    private fun showTheContainer() {
        terlarisWidgetContainer?.visibility = View.VISIBLE
    }

    private fun showLayoutThreeItem(productList: List<ShopHomeProductUiModel>) {
        containerThreeProducts?.visibility = View.VISIBLE
        prodcutCard1?.setOnClickListener {
            listener.onProductClick(productList[0].id)
        }
        productImg1?.loadImageRounded(url = productList[0].imageUrl.orEmpty())
        productName1?.text = productList[0].name
        productPrice1?.text = productList[0].displayedPrice
        productRank1?.text = "1"
        prodcutCard2?.setOnClickListener {
            listener.onProductClick(productList[1].id)
        }
        productImg2?.loadImageRounded(url = productList[1].imageUrl.orEmpty())
        productName2?.text = productList[1].name
        productPrice2?.text = productList[1].displayedPrice
        productRank2?.text = "2"
        prodcutCard3?.setOnClickListener {
            listener.onProductClick(productList[2].id)
        }
        productImg3?.loadImageRounded(url = productList[2].imageUrl.orEmpty())
        productName3?.text = productList[2].name
        productPrice3?.text = productList[2].displayedPrice
        productRank3?.text = "3"
    }

    private fun getCarouselData(productList: List<ShopHomeProductUiModel>): List<List<ShopHomeProductUiModel>> {
//        val chunkSize = 3
//        return productList.chunked(chunkSize)
        val chunkSize = 3
        val chunkedData = productList.chunked(chunkSize)
        val filteredChunkedData = chunkedData.map { _chunkedData ->
            _chunkedData.takeIf {
                it.size == PRODUCT_THREE
            }.orEmpty()
        }
        return filteredChunkedData
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

    private fun getThreeDatas(productList: List<ShopHomeProductUiModel>): List<ShopHomeProductUiModel> {
        return if (productList.isNotEmpty() && productList.size >= PRODUCT_THREE) {
            productList.slice(0..2)
        } else {
            listOf()
        }
    }

    private fun setFontColor(fontColor: Int) {
        widgetTitle?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
        widgetSubtitle?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
        productName1?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
        productPrice1?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
    }

    override fun bind(element: ShopHomeCarousellProductUiModel?) {
        element?.let {
            // Testing purpose
            val linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            terlarisWidgetAdapter = ShopHomeV4TerlarisAdapter(listener)
            rvProductCarousel?.apply {
                isNestedScrollingEnabled = false
                layoutManager = linearLayoutManager
                adapter = terlarisWidgetAdapter
            }
            val _productListCarouselData = getCarouselData(it.productList)
            terlarisWidgetAdapter?.updateData(productList = _productListCarouselData)

            hideLayoutThreeItem()
            showHorizontalProductCarousel()
//            val _data = getThreeDatas(it.productList)
//            val _productListCarouselData = getCarouselData(it.productList)
//            terlarisWidgetAdapter?.updateData(productList = _productListCarouselData)

//            if (_productListCarouselData.size.orZero() == PRODUCT_ZERO) {
//                // Hide the widget if the product is empty
//                hideTheContainer()
//            } else if (_productListCarouselData.size.orZero() == PRODUCT_THREE) {
//                // Show product list with total 3
//                showTheContainer()
//                showLayoutThreeItem(productList = _data)
//                hideHorizontalProductCarousel()
//            } else if (_productListCarouselData.size.orZero() == PRODUCT_SIX || _productListCarouselData.size.orZero() == PRODUCT_NINE) {
//                // Show product list with total 6 or 9
//                showTheContainer()
//                hideLayoutThreeItem()
//                showHorizontalProductCarousel()
//                val productListCarouselData = getCarouselData(_data)
//                terlarisWidgetAdapter?.updateData(productList = _productListCarouselData)
//            } else {
//                hideTheContainer()
//                hideLayoutThreeItem()
//                hideHorizontalProductCarousel()
//            }
            // Testing purpose

//            if (it.productList.size.orZero() == PRODUCT_ZERO) {
//                // Hide the widget if the product is empty
//                hideTheContainer()
//            } else if (it.productList.size.orZero() == PRODUCT_THREE) {
//                // Show product list with total 3
//                showTheContainer()
//                showLayoutThreeItem(productList = it.productList)
//                hideHorizontalProductCarousel()
//            } else if (it.productList.size.orZero() == PRODUCT_SIX || it.productList.size.orZero() == PRODUCT_NINE) {
//                // Show product list with total 6 or 9
//                showTheContainer()
//                hideLayoutThreeItem()
//                showHorizontalProductCarousel()
//                val productListCarouselData = getCarouselData(it.productList)
//                terlarisWidgetAdapter?.updateData(productList = productListCarouselData)
//            } else {
//                hideTheContainer()
//                hideLayoutThreeItem()
//                hideHorizontalProductCarousel()
//            }
        }
    }
}
