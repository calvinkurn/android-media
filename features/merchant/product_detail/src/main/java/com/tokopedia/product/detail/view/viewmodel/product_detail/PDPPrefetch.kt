package com.tokopedia.product.detail.view.viewmodel.product_detail

import com.tokopedia.product.detail.common.ProductDetailPrefetch
import com.tokopedia.product.detail.common.data.model.media.Media
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.CacheState
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant

object PDPPrefetch {

    private const val SOCIAL_PROOF_ICON_STAR =
        "https://images.tokopedia.net/img/pdp/info/icon/star_filled.png"

    fun toProductDetailDataModel(
        productId: String,
        data: ProductDetailPrefetch.Data
    ): ProductDetailDataModel {
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
                    icon = SOCIAL_PROOF_ICON_STAR
                )
            )
        }

        val listOfLayout = mutableListOf<DynamicPdpDataModel>(
            ProductMediaDataModel(
                type = ProductDetailConstant.MEDIA,
                name = ProductDetailConstant.MEDIA,
                isPrefetch = true
            ),
            ProductContentDataModel(
                type = ProductDetailConstant.PRODUCT_CONTENT,
                name = ProductDetailConstant.PRODUCT_CONTENT,
                freeOngkirImgUrl = data.freeShippingLogo
            )
        )

        if (socialProofItems.isNotEmpty()) {
            listOfLayout.add(
                ProductMiniSocialProofDataModel(
                    type = ProductDetailConstant.MINI_SOCIAL_PROOF,
                    name = ProductDetailConstant.MINI_SOCIAL_PROOF,
                    items = socialProofItems
                )
            )
        }

        return ProductDetailDataModel(
            layoutData = ProductInfoP1(
                basic = BasicInfo(
                    productID = productId
                ),
                data = ComponentData(
                    media = listOf(
                        Media(
                            type = ProductMediaDataModel.IMAGE_TYPE,
                            uRLOriginal = data.image
                        ).apply { prefetch = true }
                    ),
                    name = data.name,
                    price = Price(
                        value = data.price
                    )
                ),
                cacheState = CacheState(
                    isPrefetch = true
                )
            ),
            listOfLayout = listOfLayout
        )
    }
}
