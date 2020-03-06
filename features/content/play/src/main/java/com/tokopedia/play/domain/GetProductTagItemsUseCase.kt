package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.play.data.Product
import com.tokopedia.play.data.ProductTaggingItems
import com.tokopedia.play.data.Voucher
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 2020-03-06.
 */
class GetProductTagItemsUseCase @Inject constructor(val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) :
        UseCase<ProductTaggingItems>() {

    var channelId: String = ""

    override suspend fun executeOnBackground(): ProductTaggingItems {
        val params = RequestParams.create()
        params.putString("channelId", channelId)

        return ProductTaggingItems(
                listOfProducts = listOf(
                        Product(id = 14265830,
                                appLink = "tokopedia://product/14265830",
                                discount = 0,
                                image = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg",
                                isAvailable = true,
                                isVariant = true,
                                minimumQuantity = 1,
                                name = "Ramayana - Kemeja Pria Blue Camouflage RAF (07901447)",
                                order = 0,
                                originalPrice = 400000,
                                originalPriceFormatted = "Rp 400.000",
                                price = 0,
                                priceFormatted = "",
                                quantity = 0,
                                shopId = "479057",
                                webLink = "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447"),
                        Product(id = 14265830,
                                appLink = "tokopedia://product/14265830",
                                discount = 0,
                                image = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg",
                                isAvailable = true,
                                isVariant = true,
                                minimumQuantity = 1,
                                name = "Ramayana - Kemeja Pria Blue Camouflage RAF (07901447)",
                                order = 0,
                                originalPrice = 400000,
                                originalPriceFormatted = "Rp 400.000",
                                price = 0,
                                priceFormatted = "",
                                quantity = 0,
                                shopId = "479057",
                                webLink = "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447")
                ),
                listOfVouchers = listOf(
                        Voucher(
                                title = "Cashback 2rb",
                                subtitle = "min. pembelian 100rb",
                                thumbnailUrl = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg"
                        ),
                        Voucher(
                                title = "Cashback 2rb",
                                subtitle = "min. pembelian 100rb",
                                thumbnailUrl = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg"
                        ),
                        Voucher(
                                title = "Cashback 2rb",
                                subtitle = "min. pembelian 100rb",
                                thumbnailUrl = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg"
                        ),
                        Voucher(
                                title = "Cashback 2rb",
                                subtitle = "min. pembelian 100rb",
                                thumbnailUrl = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg"
                        )
                )
        )
    }
}