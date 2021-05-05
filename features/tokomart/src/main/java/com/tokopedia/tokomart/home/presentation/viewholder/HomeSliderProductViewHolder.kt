package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokomart.home.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderProductUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class HomeSliderProductViewHolder (
        itemView: View
): AbstractViewHolder<HomeSliderProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_slider_product
    }

    private var tpTitle: Typography? = null
    private var tpCta: Typography? = null
    private var headerCarouselProduct: View? = null
    private var carouselProductCardView: CarouselProductCardView? = null
    private var carouselProductUiModel: HomeSliderProductUiModel? = null

    init {
        initView(itemView)
    }

    private fun initView(view: View) {
        tpTitle = view.findViewById(R.id.tp_title)
        tpCta = view.findViewById(R.id.tp_cta)
        headerCarouselProduct = view.findViewById(R.id.header_carousel_product)
        carouselProductCardView = view.findViewById(R.id.carousel_product_card_view)
    }

    override fun bind(element: HomeSliderProductUiModel?) {
        carouselProductUiModel = element
        val title = "Testing"
        val ctaText = "Lihat Semua"

        val list = listOf(ProductCardUiModel(), ProductCardUiModel(), ProductCardUiModel(), ProductCardUiModel(), ProductCardUiModel())

        bindShopProductCarousel(list)
        if (title.isBlank() and ctaText.isBlank()) {
            headerCarouselProduct?.hide()
        }

        tpTitle?.text = MethodChecker.fromHtml(title)
        tpCta?.apply {
            if (ctaText.isNotEmpty()) {
                show()
                text = MethodChecker.fromHtml(ctaText)
                setOnClickListener {
                    //listener for see all button
                }
            } else {
                hide()
            }
        }
    }

    private fun bindShopProductCarousel(shopHomeProductViewModelList: List<ProductCardUiModel>) {
        carouselProductCardView?.bindCarouselProductCardViewGrid(
                productCardModelList = shopHomeProductViewModelList.map {
                    mapToProductCardModel()
                },
        )
    }

    private fun mapToProductCardModel(): ProductCardModel {
        return ProductCardModel(
                productImageUrl = "https://cdn4.iconfinder.com/data/icons/small-n-flat/24/user-alt-512.png",
                productName = "Street?",
                discountPercentage = "20",
                slashedPrice = "2000",
                formattedPrice = "2000",
                countSoldRating = "5.0",
                freeOngkir = ProductCardModel.FreeOngkir(),
                labelGroupList = listOf(mapToProductCardLabelGroup(labelGroupUiModel = LabelGroupUiModel())),
                hasThreeDots = true,
                hasAddToCartButton = true,
                addToCartButtonType = UnifyButton.Type.MAIN,
                isWideContent = false
        )
    }

    private fun mapToProductCardLabelGroup(labelGroupUiModel: LabelGroupUiModel): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(
                position = labelGroupUiModel.position,
                title = labelGroupUiModel.title,
                type = labelGroupUiModel.type,
                imageUrl = labelGroupUiModel.url
        )
    }

    data class ProductCardUiModel(
            val id: String = ""
    )

    data class LabelGroupUiModel(
            val position: String = "",
            val type: String = "",
            val title: String = "",
            val url: String = ""
    )

}