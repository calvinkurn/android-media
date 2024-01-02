package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class BuyerReviewUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val title: String,
    val items: List<ItemBuyerReviewUiModel>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

    data class ItemBuyerReviewUiModel(
        val shopIcon: String,
        val shopName: String,
        val avatar: String,
        val reviewerName: String,
        val reviewerStatus: String? = null,
        val totalCompleteReview: Int? = null,
        val totalHelpedPeople: Int? = null,
        val description: String,
        val datetime: String? = null,
        val rating: Float,
        val variantName: String? = null,
        val images: List<ImgReview>
    )

    data class ImgReview(
        val id: String,
        val imgUrl: String,
        val fullsizeImgUrl: String = ""
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        fun dummyBuyerReviewData() = BuyerReviewUiModel(
            "dummyBuyerReview",
            "",
            "",
            title = "Ulasan dari pembeli di Tokopedia",
            items = listOf(
                ItemBuyerReviewUiModel(
                    shopIcon = "https://images.tokopedia.net/img/official_store/badge_os.png",
                    shopName = "Super-Gameshop",
                    reviewerName = "Agung",
                    avatar = "https://static.wikia.nocookie.net/spongebob/images/7/7b/Krabs_artwork.png/revision/latest/scale-to-width-down/1200?cb=20220807045807",
                    reviewerStatus = "Juara Ulasan Fashion Wanita",
                    totalCompleteReview = 6,
                    totalHelpedPeople = 7,
                    description = "Barang ORI, fullset. Kurir nya mantap, lion parcel.. lain kali mesen di sini lagi. thanks",
                    datetime = "6 bulan lalu",
                    rating = 5.toFloat(),
                    variantName = "Avocado Green",
                    images = listOf(
                        ImgReview(
                            id = "98415167",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/2/16/453ea20d-0bc7-4475-bbb8-280f4bf8635b.jpg"
                        ),
                        ImgReview(
                            id = "98415168",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/2/16/7c476729-37c8-4915-b152-28f68c298206.jpg"
                        ),
                        ImgReview(
                            id = "98415169",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/2/16/a45b6c3b-6a2c-4e02-baf6-5fc1d86e1e84.jpg"
                        )
                    )
                ),
                ItemBuyerReviewUiModel(
                    shopIcon = "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro.png",
                    shopName = "Kaosproid",
                    reviewerName = "Megaman",
                    avatar = "https://w7.pngwing.com/pngs/934/991/png-transparent-mega-man-5-mega-man-10-mega-man-v-mega-man-6-megaman-miscellaneous-video-game-fictional-character-thumbnail.png",
                    description = "prosesnya lama,sempet 2 x transakis krn transaksi yg pertama dibatalkan. utk kaos sih bagus tp gambar trlalu kecil, cm 10x10 tdk sesuai deskripsi",
                    datetime = "6 hari lalu",
                    rating = (4).toFloat(),
                    images = listOf(
                        ImgReview(
                            id = "109846422",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/8/22/7a4e41a8-037d-4bdb-81ab-ea1d9b38aea1.jpg"
                        )
                    )
                ),
                ItemBuyerReviewUiModel(
                    shopIcon = "https://images.tokopedia.net/img/official_store/badge_os.png",
                    shopName = "Souvigameshop",
                    reviewerName = "Mastodon",
                    avatar = "https://e7.pngegg.com/pngimages/288/608/png-clipart-spongebob-squarepants-plankton-spongebob-squarepants-creature-from-the-krusty-krab-spongebob-squarepants-featuring-nicktoons-globs-of-doom-plankton-and-karen-patrick-star-mr-krabs.png",
                    reviewerStatus = "Juara Panjat Pinang Nasional",
                    totalCompleteReview = 29,
                    totalHelpedPeople = 4,
                    description = "I'm 57 years old and I have dry skin! I absolutely love this product. Has nice consistency that slips in nicely after I wash my face. When I test testt testtt testttt",
                    datetime = "4 minggu lalu",
                    rating = 5.toFloat(),
                    variantName = "Navy Blue",
                    images = listOf(
                        ImgReview(
                            id = "108609455",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/7/30/d9153adc-f8ed-46ca-a393-425798f798f6.jpg"
                        ),
                        ImgReview(
                            id = "108609456",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/7/30/cc870090-8b92-44b7-9b4c-6c5b33bd03cd.jpg"
                        ),
                        ImgReview(
                            id = "108609457",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/7/30/7f238c6e-bc23-486c-bac2-ddbc7189e139.jpg"
                        ),
                        ImgReview(
                            id = "108609458",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/7/30/4c794455-349e-450e-ae61-b55f6b2740f3.jpg"
                        ),
                        ImgReview(
                            id = "108609459",
                            imgUrl = "https://images.tokopedia.net/img/cache/200-square/bjFkPX/2023/7/30/ad526b29-fb94-49ea-8ab2-f30f5e7f4101.jpg"
                        )
                    )
                )
            )
        )
    }
}
