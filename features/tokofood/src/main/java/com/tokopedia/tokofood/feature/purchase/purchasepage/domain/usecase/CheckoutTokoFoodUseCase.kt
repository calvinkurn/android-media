package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.additionalattributes.CartAdditionalAttributesTokoFood
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodAvailabilitySection
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodConsentBottomSheet
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantOption
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodPromo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShop
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingCostBreakdown
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingCostBreakdownItem
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingDiscountBreakdown
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSummary
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSurge
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingTotal
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodSummaryDetail
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodSummaryItemDetail
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodSummaryItemDetailBottomSheet
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodSummaryItemDetailInfo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodTicker
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodTickerInfo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodUserAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckoutTokoFoodUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper,
    dispatchers: CoroutineDispatchers
): FlowUseCase<String, CheckoutTokoFood>(dispatchers.io) {

    private val isDebug = false

    companion object {
        private const val PARAMS_KEY = "params"

        private fun generateParams(additionalAttributes: String,
                                   source: String): Map<String, Any> {
            val params = CheckoutTokoFoodParam(
                additionalAttributes = additionalAttributes,
                source = source
            )
            return mapOf(PARAMS_KEY to params)
        }
    }

    override fun graphqlQuery(): String = """
        query LoadCartTokofood($$PARAMS_KEY: cartTokofoodParams!) {
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
              error_tickers {
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
              error_unblocking
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
                  quantity
                  variants {
                    variant_id
                    name
                    rules {
                      selection_rule {
                        type
                        max_quantity
                        min_quantity
                        required
                      }
                    }
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
                  quantity
                  variants {
                    variant_id
                    name
                    rules {
                      selection_rule {
                        type
                        max_quantity
                        min_quantity
                        required
                      }
                    }
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
                hide_promo
                title
                subtitle
              }
              checkout_consent_bottomsheet {
                is_show_bottomsheet
                image_url
                title
                description   
                terms_and_condition
              }
              shopping_summary {
                total {
                  cost
                  savings
                }
                cost_breakdown {
                  total_cart_price {
                    original_amount
                    amount
                  }
                  outlet_fee {
                    original_amount
                    amount
                  }
                  platform_fee {
                    original_amount
                    amount
                  }
                  delivery_fee {
                    original_amount
                    amount
                    surge {
                      is_surge_price
                      factor
                    }
                  }
                  reimbursement_fee {
                    original_amount
                    amount
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
              checkout_additional_data {
                data_type
                checkout_business_id
              }
              summary_detail {
                hide_summary
                total_items
                total_price
                details {
                  title
                  price_fmt
                  info {
                    image_url
                    bottomsheet {
                      title
                      description
                    }
                  }
                }
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): Flow<CheckoutTokoFood> = flow {
        if (isDebug) {
            kotlinx.coroutines.delay(1000)
            emit(getDummyResponse())
        } else {
            val additionalAttributes = CartAdditionalAttributesTokoFood(
                chosenAddressRequestHelper.getChosenAddress()
            )
            val param = generateParams(additionalAttributes.generateString(), params)
            val response =
                repository.request<Map<String, Any>, CheckoutTokoFoodResponse>(graphqlQuery(), param)
            if (response.cartListTokofood.isSuccess()) {
                emit(response.cartListTokofood)
            } else {
                throw MessageErrorException(response.cartListTokofood.getMessageIfError())
            }
        }
    }

    private fun getDummyResponse(): CheckoutTokoFood {
        return CheckoutTokoFood(
            status = TokoFoodCartUtil.SUCCESS_STATUS,
            data = CheckoutTokoFoodData(
                shop = CheckoutTokoFoodShop(
                    name = "Kedai Kopi, Mantapp",
                    distance = "Jarak Pengiriman: 0,5 km"
                ),
                tickers = CheckoutTokoFoodTicker(
                    top = CheckoutTokoFoodTickerInfo(
                        message = "Terkait COVID-19, waktu pengiriman diperpanjang untuk layanan kurir konvensional, instant, same day"
                    ),
                    bottom = CheckoutTokoFoodTickerInfo(
                        message = "Pesanan dari resto ini tidak bisa dibatalkan, ya."
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
                            quantity = 1,
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
                            notes = "",
                            quantity = 1
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
                            quantity = 1,
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
                            notes = "",
                            quantity = 1
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
                    hidePromo = false,
                    title = "Kamu bisa hemat Rp.12.000",
                    subtitle = "1 promo dipakai"
                ),
                checkoutConsentBottomSheet = CheckoutTokoFoodConsentBottomSheet(
                    isShowBottomsheet = true,
                    imageUrl = "https://i.pinimg.com/736x/74/fc/48/74fc4817f31d9d36537366c740d08015.jpg",
                    title = "Pesananmu dilayani restoran GoFood",
                    description = "Gojek perlu beberapa datamu biar pesananmu sampai tujuan. ",
                    termsAndCondition = "Saya menyetujui Syarat & Ketentuan yang sedang berlaku"
                ),
                shoppingSummary = CheckoutTokoFoodShoppingSummary(
                    total = CheckoutTokoFoodShoppingTotal(
                        cost = 133000.00,
                        savings = 16000.00
                    ),
                    costBreakdown = CheckoutTokoFoodShoppingCostBreakdown(
                        totalCartPrice = CheckoutTokoFoodShoppingCostBreakdownItem(
                            originalAmount = 10000.00,
                            amount = 10000.00,
                        ),
                        takeAwayFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            originalAmount = 10000.00,
                            amount = 10000.00,
                        ),
                        convenienceFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            originalAmount = 10000.00,
                            amount = 10000.00,
                        ),
                        deliveryFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            originalAmount = 10000.00,
                            amount = 10000.00,
                            surge = CheckoutTokoFoodShoppingSurge(
                                isSurgePrice = true,
                                factor = 1.5
                            )
                        ),
                        parkingFee = CheckoutTokoFoodShoppingCostBreakdownItem(
                            originalAmount = 0.00,
                            amount = 0.00
                        )
                    ),
                    discountBreakdown = listOf(CheckoutTokoFoodShoppingDiscountBreakdown(
                        title = "Total Diskon Item",
                        amount = 12000.00
                    ))
                ),
                summaryDetail = CheckoutTokoFoodSummaryDetail(
                    hideSummary = false,
                    totalItems = 0,
                    totalPrice = "Rp 133.000",
                    details = listOf(
                        CheckoutTokoFoodSummaryItemDetail(
                            title = "Total Harga (2 item)",
                            priceFmt = "Rp 125.000"
                        ),
                        CheckoutTokoFoodSummaryItemDetail(
                            title = "Total Diskon Item",
                            priceFmt = "-Rp 12.000"
                        ),
                        CheckoutTokoFoodSummaryItemDetail(
                            title = "Biaya Bungkus dari Restoran",
                            priceFmt = "Rp 6.000"
                        ),
                        CheckoutTokoFoodSummaryItemDetail(
                            title = "Biaya Jasa Aplikasi",
                            priceFmt = "Rp 4.000"
                        ),
                        CheckoutTokoFoodSummaryItemDetail(
                            title = "Total Ongkos Kirim",
                            priceFmt = "Rp 16.000",
                            info = CheckoutTokoFoodSummaryItemDetailInfo(
                                imageUrl = "https://icons.veryicon.com/png/o/miscellaneous/zol-m-station/icon-top-arrow.png",
                                bottomSheet = CheckoutTokoFoodSummaryItemDetailBottomSheet(
                                    title = "Ongkos kirim kamu naik, ya",
                                    description = "Ongkos kirim kamu disesuaikan karena jam sibuk atau ketersediaan penyedia layanan. "
                                )
                            )
                        ),
                        CheckoutTokoFoodSummaryItemDetail(
                            title = "Total Diskon Ongkos Kirim",
                            priceFmt = "-Rp 6.000"
                        )
                    )
                )
            )
        )
    }

}