package com.tokopedia.play.data.repository

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.play.data.ProductSection
import com.tokopedia.play.domain.GetProductTagItemsUseCase
import com.tokopedia.play.domain.repository.PlayViewerTagItemRepository
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.tagitem.*
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class PlayViewerTagItemRepositoryImpl @Inject constructor(
    private val getProductTagItemsUseCase: GetProductTagItemsUseCase,
    private val getProductVariantUseCase: GetProductVariantUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val mapper: PlayUiModelMapper,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
): PlayViewerTagItemRepository {

    override suspend fun getTagItem(
        channelId: String
    ): TagItemUiModel = withContext(dispatchers.io) {
        val response = getProductTagItemsUseCase.apply {
            setRequestParams(GetProductTagItemsUseCase.createParam(channelId))
        }.executeOnBackground()

        //TODO() = fake data - change to real use case
        val str = """
            {
                "playGetTagsItemSection": {
                  "sections": [
                    {
                      "type": "active",
                      "title": "Dekor Rumah Minimalis",
                      "countdown": {
                        "copy": "Berakhir Dalam"
                      },
                      "background": {
                        "gradient": ["#ff23de", "#2244aa"],
                        "imageUrl": "https://....."
                      },
                      "startTime": "2022-01-02T15:04:05Z07:00",
                      "endTime": "2022-01-02T16:04:05Z07:00",
                      "serverTime": "2022-01-02T15:14:05Z07:00",
                      "products": [
                        {
                          "ID": "15240013",
                          "Name": "Indomie Soto Lamongan",
                          "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
                          "ShopID": "479887",
                          "OriginalPrice": 60000,
                          "OriginalPriceFormatted": "Rp 60.000",
                          "Discount": 0,
                          "Price": 0,
                          "PriceFormatted": "",
                          "Quantity": 5,
                          "QuantityRender": {
                            "show": true,
                            "copy": "Sisa 5",
                            "color": "#FF5733"
                          },
                          "IsVariant": false,
                          "IsAvailable": false,
                          "Order": 0,
                          "AppLink": "tokopedia://product/15240013",
                          "WebLink": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                          "MinQuantity": 1,
                          "IsFreeShipping":true
                        }
                      ]
                    },
                    {
                      "type": "other",
                      "title": "Produk Lainnya",
                      "countdown": {
                        "copy": ""
                      },
                      "background": {
                        "gradient": [],
                        "imageUrl": ""
                      },
                      "startTime": "",
                      "endTime": "",
                      "serverTime": "",
                      "products": [
                        {
                          "ID": "15240013",
                          "Name": "Indomie Soto Lamongan",
                          "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
                          "ShopID": "479887",
                          "OriginalPrice": 60000,
                          "OriginalPriceFormatted": "Rp 60.000",
                          "Discount": 0,
                          "Price": 0,
                          "PriceFormatted": "",
                          "Quantity": 9988,
                          "QuantityRender": {
                            "show": false,
                            "copy": "",
                            "color": ""
                          },
                          "IsVariant": false,
                          "IsAvailable": true,
                          "Order": 0,
                          "AppLink": "tokopedia://product/15240013",
                          "WebLink": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                          "MinQuantity": 1,
                          "IsFreeShipping":true
                        }
                      ]
                    },
                    {
                      "type": "out_of_stock",
                      "title": "Produk Habis",
                      "countdown": {
                        "copy": ""
                      },
                      "background": {
                        "gradient": [],
                        "imageUrl": ""
                      },
                      "startTime": "",
                      "endTime": "",
                      "serverTime": "",
                      "products": [
                        {
                          "ID": "15240014",
                          "Name": "Lampu LED Multicolor",
                          "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
                          "ShopID": "479888",
                          "OriginalPrice": 70000,
                          "OriginalPriceFormatted": "Rp 70.000",
                          "Discount": 0,
                          "Price": 0,
                          "PriceFormatted": "",
                          "Quantity": 0,
                          "QuantityRender": {
                            "show": false,
                            "copy": "",
                            "color": ""
                          },
                          "IsVariant": false,
                          "IsAvailable": false,
                          "Order": 0,
                          "AppLink": "tokopedia://product/15240013",
                          "WebLink": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                          "MinQuantity": 1,
                          "IsFreeShipping":true
                        }
                      ]
                    }
                  ],
                  "vouchers": [
                    {
                      "ID": "12",
                      "Name": "test date",
                      "ShopID": "105407",
                      "Title": " ",
                      "Subtitle": "min. pembelian ",
                      "VoucherType": 1,
                      "VoucherImage": "https://ecs7.tokopedia.net/img/attachment/2018/10/4/5480066/5480066_4a86d259-d8ce-4501-a1d8-17803320bc35",
                      "VoucherImageSquare": "",
                      "VoucherQuota": 100,
                      "VoucherFinishTime": "2022-12-07T23:30:00Z",
                      "VoucherCode": "KMZWAY87AA",
                      "IsHighlighted": true,
                      "IsVoucherCopyable": true,
                      "IsPrivate" : true
                    }
                  ],
                  "config" : {
                     "peek_product_count" : 15,
                     "title_bottomsheet": "Promo dan Produk Lainnya"
                  }
                }
            }
        """


        try {
            val fake = Gson().fromJson(str, ProductSection.Response::class.java)

            val sectionList = mapper.mapProductSection(
                fake.playGetTagsItem.sectionList
            )

            //TODO() = remove product
            val productList = mapper.mapProductTags(
                fake.playGetTagsItem.sectionList.first().listOfProducts
            )

            val voucherList = mapper.mapMerchantVouchers(
                fake.playGetTagsItem.voucherList
            )

            return@withContext TagItemUiModel(
                product = ProductUiModel(
                    productList = productList as List<PlayProductUiModel.Product>,
                    canShow = true,
                ),
                voucher = VoucherUiModel(
                    voucherList = voucherList,
                ),
                maxFeatured = response.playGetTagsItem.config.peekProductCount,
                resultState = ResultState.Success,
                section = SectionUiModel(sectionList)
            )
        }catch (e: Exception){
            return@withContext TagItemUiModel(product = ProductUiModel( productList = emptyList(), canShow = false),
                voucher = VoucherUiModel(voucherList = emptyList()), maxFeatured = 1, resultState = ResultState.Success, section = SectionUiModel(
                    emptyList())
            )
        }
    }

    override suspend fun getVariant(
        product: PlayProductUiModel.Product
    ): VariantUiModel = withContext(dispatchers.io) {
        val response = getProductVariantUseCase.apply {
            params = getProductVariantUseCase.createParams(product.id)
        }.executeOnBackground()

        val selectedVariants = VariantCommonMapper.mapVariantIdentifierToHashMap(response.data)
        val categories = VariantCommonMapper.processVariant(
            variantData = response.data,
            mapOfSelectedVariant = selectedVariants
        )
        VariantUiModel(
            variantDetail = product,
            parentVariant = response.data,
            selectedVariants = selectedVariants,
            categories = categories.orEmpty(),
            stockWording = "",
        )
    }

    override suspend fun addProductToCart(
        id: String,
        name: String,
        shopId: String,
        minQty: Int,
        price: Double
    ): String = withContext(dispatchers.io) {
        try {
            val response = addToCartUseCase.apply {
                setParams(
                    AddToCartUseCase.getMinimumParams(
                        productId = id,
                        shopId = shopId,
                        quantity = minQty,
                        atcExternalSource = AtcFromExternalSource.ATC_FROM_PLAY,
                        productName = name,
                        price = price.toString(),
                        userId = userSession.userId,
                    )
                )
            }.executeOnBackground()
            if (response.isDataError()) throw MessageErrorException(response.getAtcErrorMessage())
            return@withContext response.data.cartId
        } catch (e: Throwable) {
            if (e is ResponseErrorException) throw MessageErrorException(e.localizedMessage)
            else throw e
        }
    }
}