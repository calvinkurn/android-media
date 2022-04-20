package com.tokopedia.tokofood.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodAvailabilitySection
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantOption
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodPromo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShop
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingCostBreakdown
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingCostBreakdownItem
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingDiscountBreakdown
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSummary
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSurge
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSurgeBottomsheet
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingTotal
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodTicker
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodTickerInfo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodUserAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckoutTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
): FlowUseCase<String, CheckoutTokoFoodResponse>(dispatchers.io) {

    private val isDebug = true

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: String): Map<String, Any> {
            val params = CheckoutTokoFoodParam(
                additionalAttributes = additionalAttributes
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

    override fun graphqlQuery(): String = """
        query LoadCartTokofood($$PARAMS_KEY: CheckoutTokoFoodParam!) {
          cart_list_tokofood(params: $$PARAMS_KEY) {
            message
            status
            data {
              popup_message
              popup_error_message
              shop {
                shop_id
                name
                distance
              }
              tickers {
                top {
                  id
                  message
                  page
                }
                bottom {
                  id
                  message
                  page
                }
              }
              errors_unblocking
              user_address {
                address_id
                address_name
                address
                phone
                receiver_name
                status
              }
              available_section {
                products {
                  cart_id
                  product_id
                  name
                  description
                  image_url
                  price
                  price_fmt
                  original_price
                  original_price_fmt
                  discount_percentage
                  notes
                  variants {
                    variant_id
                    name
                    options {
                      is_selected
                      option_id
                      name
                      price
                      price_fmt
                      status
                    }
                  }
                }
              }
              unavailable_section_header
              unavailable_section {
                title
                products {
                  cart_id
                  product_id
                  name
                  description
                  image_url
                  price
                  price_fmt
                  original_price
                  original_price_fmt
                  discount_percentage
                  notes
                  variants {
                    variant_id
                    name
                    options {
                      is_selected
                      option_id
                      name
                      price
                      price_fmt
                      status
                    }
                  }
                }
              }
              shipping {
                name
                logo_url
                eta
                price
                price_fmt
              }
              promo {
                title
                subtitle
                promo_breakdown {
                  promo_id
                  title
                  amount
                  scope
                  type
                }
              }
              shopping_summary {
                total {
                  cost
                  cost_fmt
                  savings
                  savings_fmt
                }
                cost_breakdown {
                  total_cart_price {
                    title
                    original_amount
                    original_amount_fmt
                    amount
                    amount_fmt
                  }
                  takeaway_fee {
                    title
                    original_amount
                    original_amount_fmt
                    amount
                    amount_fmt
                  }
                  convenience_fee {
                    title
                    original_amount
                    original_amount_fmt
                    amount
                    amount_fmt
                  }
                  delivery_fee {
                    title
                    original_amount
                    original_amount_fmt
                    amount
                    amount_fmt
                    surge {
                      is_surge_price
                      bottomsheet {
                        title
                        description
                      }
                    }
                  }
                  parking_fee {
                    title
                    original_amount
                    original_amount_fmt
                    amount
                    amount_fmt
                  }
                }
                discount_breakdown {
                  discount_id
                  title
                  amount
                  scope
                  type
                }
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): Flow<CheckoutTokoFoodResponse> = flow {
        if (isDebug) {
            kotlinx.coroutines.delay(1000)
            emit(getDummyResponse())
        } else {
            val additionalAttributes = CartAdditionalAttributesTokoFood(
                chosenAddressRequestHelper.getChosenAddress(),
                params
            )
            val param = generateParams(additionalAttributes.generateString())
            val response =
                repository.request<Map<String, Any>, CheckoutTokoFoodResponse>(graphqlQuery(), param)
            emit(response)
        }
    }

    private fun getDummyResponse(): CheckoutTokoFoodResponse {
        return CheckoutTokoFoodResponse(
            status = 1,
            data = CheckoutTokoFoodData(
                shop = CheckoutTokoFoodShop(
                    name = "Kedai Kopi, Mantapp",
                    distance = "Jarak Pengiriman: 0,5 km"
                ),
                tickers = CheckoutTokoFoodTicker(
                    top = CheckoutTokoFoodTickerInfo(
                        message = "This will be note relevant to any info and error on checkout"
                    ),
                    bottom = CheckoutTokoFoodTickerInfo(
                        message = "This will be note relevant to any info and error on checkout"
                    )
                ),
                errorsUnblocking = "Yah, ada 3 item tidak bisa diproses. Kamu bisa lanjut pesan yang lainnya, ya. <a href=\\\"\\\">Lihat</a>",
                userAddress = CheckoutTokoFoodUserAddress(
                    addressId = "123",
                    addressName = "Rumah",
                    address = "Tokopedia Tower Ciputra World 2, Jl. Prof. DR. Satrio No.Kav. 11, Karet Semanggi, Setiabudi, Jakarta Selatan",
                    phone = "081234567890",
                    receiverName = "Adrian",
                    status = 2
                ),
                availableSection = CheckoutTokoFoodAvailabilitySection(
                    products = listOf(
                        CheckoutTokoFoodProduct(
                            productId = "1",
                            productName = "Milo Macchiato",
                            imageUrl = "https://img-global.cpcdn.com/recipes/1db6e302172f3f01/680x482cq70/es-milo-macchiato-janji-jiwa-foto-resep-utama.jpg",
                            price = 25000.00,
                            priceFmt = "Rp 25.000",
                            originalPrice = 50000.00,
                            originalPriceFmt = "Rp 50.000",
                            discountPercentage = "50%",
                            notes = "Pesanannya jangan sampai salah ya! udah haus bang. Pesanannya jangan sampai salah ya! udah haus bang...",
                            variants = listOf(
                                CheckoutTokoFoodProductVariant(
                                    name = "Sugar Level",
                                    options = listOf(
                                        CheckoutTokoFoodProductVariantOption(
                                            name = "Normal"
                                        )
                                    )
                                ),
                                CheckoutTokoFoodProductVariant(
                                    name = "Topping",
                                    options = listOf(
                                        CheckoutTokoFoodProductVariantOption(
                                            name = "Regal"
                                        ),
                                        CheckoutTokoFoodProductVariantOption(
                                            name = "Ice Cream"
                                        )
                                    )
                                )
                            )
                        ),
                        CheckoutTokoFoodProduct(
                            productId = "2",
                            productName = "Milo Maacih",
                            imageUrl = "https://img-global.cpcdn.com/recipes/1db6e302172f3f01/680x482cq70/es-milo-macchiato-janji-jiwa-foto-resep-utama.jpg",
                            price = 100000.00,
                            priceFmt = "Rp 100.000",
                            originalPrice = 150000.00,
                            originalPriceFmt = "Rp 200.000",
                            discountPercentage = "33%",
                            notes = ""
                        )
                    )
                ),
                unavailableSectionHeader = "Tidak bisa diproses (3)",
                unavailableSection = CheckoutTokoFoodAvailabilitySection(
                    title = "Stok Habis",
                    products = listOf(
                        CheckoutTokoFoodProduct(
                            productId = "3",
                            productName = "Milo Macchiato",
                            imageUrl = "https://img-global.cpcdn.com/recipes/1db6e302172f3f01/680x482cq70/es-milo-macchiato-janji-jiwa-foto-resep-utama.jpg",
                            price = 25000.00,
                            priceFmt = "Rp 25.000",
                            originalPrice = 50000.00,
                            originalPriceFmt = "Rp 50.000",
                            discountPercentage = "50%",
                            notes = "Pesanannya jangan sampai salah ya! udah haus bang. Pesanannya jangan sampai salah ya! udah haus bang...",
                            variants = listOf(
                                CheckoutTokoFoodProductVariant(
                                    name = "Sugar Level",
                                    options = listOf(
                                        CheckoutTokoFoodProductVariantOption(
                                            name = "Normal"
                                        )
                                    )
                                ),
                                CheckoutTokoFoodProductVariant(
                                    name = "Topping",
                                    options = listOf(
                                        CheckoutTokoFoodProductVariantOption(
                                            name = "Regal"
                                        ),
                                        CheckoutTokoFoodProductVariantOption(
                                            name = "Ice Cream"
                                        )
                                    )
                                )
                            )
                        ),
                        CheckoutTokoFoodProduct(
                            productId = "4",
                            productName = "Milo Maacih",
                            imageUrl = "https://img-global.cpcdn.com/recipes/1db6e302172f3f01/680x482cq70/es-milo-macchiato-janji-jiwa-foto-resep-utama.jpg",
                            price = 100000.00,
                            priceFmt = "Rp 100.000",
                            originalPrice = 150000.00,
                            originalPriceFmt = "Rp 200.000",
                            discountPercentage = "33%",
                            notes = ""
                        )
                    )
                ),
                shipping = CheckoutTokoFoodShipping(
                    name = "Gojek Instan (Rp0)",
                    logoUrl = "https://1000logos.net/wp-content/uploads/2020/11/Gojek-Logo-1024x640.png",
                    eta = "Tiba dalam 30-60 menit",
                    price = 0.0,
                    priceFmt = "Rp0"
                ),
                promo = CheckoutTokoFoodPromo(
                    title = "",
                    subtitle = "1 promo dipakai"
                ),
                shoppingSummary = CheckoutTokoFoodShoppingSummary(
                    total = CheckoutTokoFoodShoppingTotal(
                        cost = 133000.00,
                        costFmt = "Rp 133.000"
                    ),
                    costBreakdown = CheckoutTokoFoodShoppingCostBreakdown(
                        totalCartPrice = CheckoutTokoFoodShoppingCostBreakdownItem(
                            title = "Total Harga (2 item)",
                            amount = 125000.00
                        ),
                        takeAwayFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            title = "Biaya Bungkus dari Restoran",
                            amount = 6000.00
                        ),
                        convenienceFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            title = "Biaya Jasa Aplikasi",
                            amount = 4000.00
                        ),
                        deliveryFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            title = "Total Ongkos Kirim",
                            amount = 16000.00,
                            surge = CheckoutTokoFoodShoppingSurge(
                                isSurgePrice = true,
                                bottomsheet = CheckoutTokoFoodShoppingSurgeBottomsheet(
                                    title = "Ongkos kirim kamu naik, ya",
                                    description = "Ongkos kirim kamu disesuaikan karena jam sibuk atau ketersediaan penyedia layanan. "
                                )
                            )
                        ),
                        parkingFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            title = "Biaya Parkir",
                            amount = 0.00
                        )
                    ),
                    discountBreakdown = CheckoutTokoFoodShoppingDiscountBreakdown(
                        title = "Total Diskon Item",
                        amount = 12000.00
                    )
                )
            )
        )
    }

}