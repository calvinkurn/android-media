package com.tokopedia.travelhomepage.homepage.presentation

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel

/**
 * @author by furqan on 13/02/2020
 */

val DUMMY_BANNER = TravelCollectiveBannerModel(
        banners = arrayListOf(
                TravelCollectiveBannerModel.Banner(id = "48",
                        product = "HOTEL",
                        attribute = TravelCollectiveBannerModel.Attribute(
                                description = "Saat Gajian, Paling Pas buat Bayar Kebutuhan",
                                webUrl = "https://www.tokopedia.com/promo/digital-payday",
                                appUrl = "tokopedia://promo/digital-payday",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2018/7/28/5253715/5253715_60e64e5f-135e-47f6-aba3-d4b7cc15aa68.jpg",
                                promoCode = "GAJIANSERU"
                        )
                ),
                TravelCollectiveBannerModel.Banner(id = "103",
                        product = "HOTEL",
                        attribute = TravelCollectiveBannerModel.Attribute(
                                description = "Promo Tokopedia diskon sampai dengan 250rb",
                                webUrl = "https://www.tokopedia.com/promo/finansial-reksadana-1/",
                                appUrl = "tokopedia://promo/finansial-reksadana-1/",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/5/3/5515561/5515561_d4b1d67f-cab5-459f-9e9b-30a14193d4d8.jpg",
                                promoCode = ""
                        )
                ),
                TravelCollectiveBannerModel.Banner(id = "45",
                        product = "FLIGHT",
                        attribute = TravelCollectiveBannerModel.Attribute(
                                description = "1 Hari Saja! Cashback hingga Rp 250rb untuk pesan tiket pesawat!",
                                webUrl = "https://www.tokopedia.com/promo/cashback-digital-9/",
                                appUrl = "tokopedia://promo/cashback-digital-9/",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2018/6/6/5253715/5253715_7f2d4394-e239-4512-a78f-4a97cba0e723.png",
                                promoCode = "PESAWATKEJUTAN"
                        )
                ),
                TravelCollectiveBannerModel.Banner(id = "53",
                        product = "TRAIN",
                        attribute = TravelCollectiveBannerModel.Attribute(
                                description = "Cras nunc ante, imperdiet in velit nec, bibendum semper orci. Maecenas convallis velit ut mollis bibendum. Morbi ut rhoncus urna. Maecenas pellentesque, erat nec sodales pharetra, odio purus sodales nisl, ac fringilla elit neque vitae justo.",
                                webUrl = "https://www.tokopedia.com/promo/digital-cashback",
                                appUrl = "tokopedia://promo/digital-cashback",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2018/10/2/5515593/5515593_814f7e79-9f4c-42dc-bb0c-6deb9637b2a3.jpg",
                                promoCode = "LOREM"
                        )
                )
        ),
        meta = TravelCollectiveBannerModel.MetaModel(
                title = "Lihat Semua",
                webUrl = "https://www.tokopedia.com/promo",
                appUrl = "tokopedia://promo"
        )
)

val DUMMY_CATEGORIES = TravelHomepageCategoryListModel(
        categories = arrayListOf(
                TravelHomepageCategoryListModel.Category(
                        product = "FLIGHT",
                        attributes = TravelHomepageCategoryListModel.Attribute(
                                title = "Pesawat",
                                webUrl = "https://www.tokopedia.com/flight",
                                appUrl = "tokopedia://pesawat",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2018/7/28/5253715/5253715_60e64e5f-135e-47f6-aba3-d4b7cc15aa68.jpg"
                        )
                ),
                TravelHomepageCategoryListModel.Category(
                        product = "HOTELS",
                        attributes = TravelHomepageCategoryListModel.Attribute(
                                title = "Hotel",
                                webUrl = "tokopedia://hotel",
                                appUrl = "tokopedia://hotel",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2018/7/28/5253715/5253715_60e64e5f-135e-47f6-aba3-d4b7cc15aa68.jpg"
                        )
                )
        )
)