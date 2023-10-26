package com.tokopedia.shop.info.view.model

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.tokopedia.shop.info.domain.entity.EpharmacyInfo
import com.tokopedia.shop.info.domain.entity.Review
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformanceMetric
import com.tokopedia.shop.info.domain.entity.ShopRatingAndReviews
import com.tokopedia.shop.info.domain.entity.ShopRatingAndTopics
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment

class ShopInfoPreviewParameterProvider : PreviewParameterProvider<ShopInfoUiState> {
    override val values: Sequence<ShopInfoUiState>
        get() = sequenceOf(
            shopInfoAllData,
            noEpharmachyScenario,
            noShopNotesScenario,
            noMultilocationScenario,
            noSupportedShipmentScenario
        )
    
    private val shopInfoAllData = ShopInfoUiState(
        shopImageUrl = "https://images.tokopedia.net/img/official_store/badge_os.png",
        shopBadgeUrl = "https://images.tokopedia.net/img/official_store/badge_os.png",
        shopName = "Kimia Farma",
        shopDescription = "Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups",
        mainLocation = "Jakarta Selatan",
        otherLocation = "+10 lainnya",
        operationalHours = "07:00 - 18:00",
        shopJoinDate = "9 Mar 2017",
        shopPerformanceMetrics = listOf(
            ShopPerformanceMetric(metricName = "Produk terjual", metricValue = "51rb"),
            ShopPerformanceMetric(metricName = "Performa chat", metricValue = ">1 jam"),
            ShopPerformanceMetric(metricName = "Pesanan diproses", metricValue = "1 menit")
        ),
        shopNotes = listOf(
            ShopNote("1", "Kebijakan pengembalian produk"),
            ShopNote("2", "Syarat dan ketentuan"),
            ShopNote("3", "Masa berlaku garansi")
        ),
        shipments = listOf(
            ShopSupportedShipment("JNE", ""),
            ShopSupportedShipment("J&T", ""),
            ShopSupportedShipment("Gojek", ""),
            ShopSupportedShipment("Grab", "")
        ),
        ratingAndReview = ShopRatingAndReviews(
            rating = ShopRatingAndTopics(
                rating = ShopRatingAndTopics.Rating(
                    positivePercentageFmt = "99% pembeli merasa puas",
                    ratingScore = "4.7",
                    totalRating = 217750,
                    totalRatingFmt = "217,7rb",
                    detail = listOf(
                        ShopRatingAndTopics.Rating.Detail(
                            formattedTotalReviews = "210,1rb",
                            percentageFloat = 96.53,
                            rate = 5,
                            totalReviews = 210193
                        ),
                        ShopRatingAndTopics.Rating.Detail(
                            formattedTotalReviews = "6.292",
                            percentageFloat = 2.89,
                            rate = 4,
                            totalReviews = 6292
                        ),
                        ShopRatingAndTopics.Rating.Detail(
                            formattedTotalReviews = "722",
                            percentageFloat = 0.33,
                            rate = 3,
                            totalReviews = 722
                        ),
                        ShopRatingAndTopics.Rating.Detail(
                            formattedTotalReviews = "118",
                            percentageFloat = 0.05,
                            rate = 2,
                            totalReviews = 118
                        ),
                        ShopRatingAndTopics.Rating.Detail(
                            formattedTotalReviews = "425",
                            percentageFloat = 0.2,
                            rate = 1,
                            totalReviews = 425
                        )
                    )
                )
            ),
            reviews = listOf(
                Review(
                    rating = 4,
                    reviewTime = "Kemarin",
                    reviewText = "Paket diterima dengan baik dan aman, handphone original, sellernya amanah dikirim sesuai waktu yang disepakati. Res...",
                    reviewerName = "Karina"
                ),
                Review(
                    rating = 5,
                    reviewTime = "Minggu lalu",
                    reviewText = "Packaging aman",
                    reviewerName = "Ajay"
                )
            )
        ),
        showEpharmacyInfo = true,
        epharmacy = EpharmacyInfo(
            nearestPickupAddress = "Epicentrum Walk, Jl. H. R. Rasuna said, Karet Kuningan, Setiabudi, Jakarta Selatan, Setiabudi 12940",
            nearPickupAddressAppLink = "tokopedia://logistic/epharmacy",
            pharmacistOperationalHour = "09:00 - 18:00",
            pharmacistName = "apt. Epin Ambarwati, S.Farm",
            siaNumber = "201221003602300001"
        )
    )

    private val noEpharmachyScenario = shopInfoAllData.copy(showEpharmacyInfo = false)
    private val noShopNotesScenario = shopInfoAllData.copy(shopNotes = emptyList())
    private val noMultilocationScenario = shopInfoAllData.copy(otherLocation = "")
    private val noSupportedShipmentScenario = shopInfoAllData.copy(shipments = emptyList())
}
