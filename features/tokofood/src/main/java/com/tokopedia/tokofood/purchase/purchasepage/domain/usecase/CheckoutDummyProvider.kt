package com.tokopedia.tokofood.purchase.purchasepage.domain.usecase

import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodAvailabilitySection
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodConsentBottomSheet
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

object CheckoutDummyProvider {

    fun getShopClosedResponse(): CheckoutTokoFoodResponse {
        return CheckoutTokoFoodResponse(
            status = 1,
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
                userAddress = CheckoutTokoFoodUserAddress(
                    addressId = "123",
                    addressName = "Rumah",
                    address = "Tokopedia Tower Ciputra World 2, Jl. Prof. DR. Satrio No.Kav. 11, Karet Semanggi, Setiabudi, Jakarta Selatan",
                    phone = "081234567890",
                    receiverName = "Adrian",
                    status = 2
                ),
                unavailableSectionHeader = "Tidak bisa diproses (3)",
                unavailableSection = CheckoutTokoFoodAvailabilitySection(
                    title = "Toko tutup",
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
                promo = CheckoutTokoFoodPromo(
                    hidePromo = true,
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
                    hideSummary = true,
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



    fun getAddressNotAvailableResponse(): CheckoutTokoFoodResponse {
        return CheckoutTokoFoodResponse(
            status = 1,
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
                tickerErrorMessage = "Alamatmu ada di luar area pengiriman restoran ini. Ubah alamatmu atau cek restoran lain, yuk!",
                unavailableSectionHeader = "Tidak bisa diproses (3)",
                unavailableSection = CheckoutTokoFoodAvailabilitySection(
                    title = "Toko tutup",
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
                    name = "Pengiriman tidak tersedia",
                    logoUrl = "https://1000logos.net/wp-content/uploads/2020/11/Gojek-Logo-1024x640.png",
                    eta = "Atur ulang alamatmu dulu, ya.",
                    price = 0.0,
                    priceFmt = ""
                ),
                promo = CheckoutTokoFoodPromo(
                    hidePromo = true,
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
                    hideSummary = true,
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