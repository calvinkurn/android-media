package com.tokopedia.product.detail.view.viewmodel.product_detail

import com.tokopedia.product.detail.common.ProductDetailPrefetch
import com.tokopedia.product.detail.common.data.model.pdplayout.CacheState
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant

object PDPMock {

    fun toProductDetailDataModel(data: ProductDetailPrefetch.Data): ProductDetailDataModel {
        val socialProofItems = mutableListOf<SocialProofUiModel>()

        if (data.integrity.isNotEmpty()) {
            socialProofItems.add(
                SocialProofUiModel(
                    type = SocialProofUiModel.Type.Text,
                    title = data.integrity
                )
            )
        }
        if (data.rating.isNotEmpty()) {
            socialProofItems.add(
                SocialProofUiModel(
                    type = SocialProofUiModel.Type.Chip,
                    title = data.rating,
                    icon = "https://images.tokopedia.net/img/pdp/info/icon/star_filled.png"
                )
            )
        }

        return ProductDetailDataModel(
            layoutData = DynamicProductInfoP1(
                cacheState = CacheState(
                    isPrefetch = true
                ),
                data = ComponentData(
                    media = listOf(
                        Media(
                            type = "image",
                            uRLOriginal = data.image
                        ).apply { prefetch = true }
                    ),
                    name = data.name,
                    price = Price(
                        value = data.price
                    ),
                    containerType = data.containerType
                )
            ),
            listOfLayout = mutableListOf(
                ProductMediaDataModel(
                    type = ProductDetailConstant.MEDIA,
                    name = ProductDetailConstant.MEDIA
                ),
                ProductContentDataModel(
                    type = ProductDetailConstant.PRODUCT_CONTENT,
                    name = ProductDetailConstant.PRODUCT_CONTENT,
                    freeOngkirImgUrl = data.freeShippingLogo
                ),
                ProductMiniSocialProofDataModel(
                    type = ProductDetailConstant.MINI_SOCIAL_PROOF,
                    name = ProductDetailConstant.MINI_SOCIAL_PROOF,
                    items = socialProofItems
                )
            )
        )
    }
}
