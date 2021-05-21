package com.tokopedia.travelhomepage.homepage.presentation

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelMetaModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.travelhomepage.destination.model.TravelHomepageOrderListModel
import com.tokopedia.travelhomepage.destination.model.TravelHomepageRecommendationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel

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

val DUMMY_ORDER_LIST = TravelHomepageOrderListModel(
        orders = arrayListOf(
                TravelHomepageOrderListModel.Order(
                        product = "HOTEL",
                        title = "Harris Hotel",
                        subtitle = "22 Feb 2019 - 25 Feb 2019",
                        prefix = "Check-In",
                        value = "02.00-20.00 WIB",
                        webUrl = "https://www.tokopedia.com/hotel",
                        appUrl = "https://www.tokopedia.com/hotel",
                        imageUrl = "https://tvlk.imgix.net/imageResource/2019/07/01/1561969742861-54d523759500f217d6b7a094c3db0e43.jpeg?auto=compress%2Cformat&amp;cs=srgb&amp;fm=pjpg&amp;ixlib=java-1.1.12&amp;q=75"
                ),
                TravelHomepageOrderListModel.Order(
                        product = "DEALS",
                        title = "Rice Bowl",
                        subtitle = "Voucher value Rp 150.000",
                        prefix = "Berlaku hingga",
                        value = "29 Mar 2019",
                        webUrl = "https://www.tokopedia.com/deals/i/voucher-senilai-rp-150000-untuk-semua-makanan-17996/",
                        appUrl = "https://www.tokopedia.com/deals/i/voucher-senilai-rp-150000-untuk-semua-makanan-17996/",
                        imageUrl = "https://image-assets.access.myfave.gdn/attachments/030cc596a1f59fc72d5be22eeb6df9e3ec5e5ca8/store/fill/200/200/9914b5572972c08bf35ac7e279753f846f29f81643aa4d197e02f3bd0f60/logo.jpg"
                ),
                TravelHomepageOrderListModel.Order(
                        product = "FLIGHT",
                        title = "JAKARTA (CGK) -> SINGAPORE (SIN)",
                        subtitle = "",
                        prefix = "",
                        value = "Sel, 23 Okt 2019 | 13.45",
                        webUrl = "https://www.tokopedia.com/flight/",
                        appUrl = "https://www.tokopedia.com/flight/",
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2017/12/20/5512496/5512496_7f755496-e7cd-480d-bcc0-081c84828d37.png"
                ),
                TravelHomepageOrderListModel.Order(
                        product = "TRAIN",
                        title = "JAKARTA (GMR) -> SOLO (SLO)",
                        subtitle = "",
                        prefix = "",
                        value = "Sel, 23 Okt 2019 | 13.45",
                        webUrl = "https://tiket.tokopedia.com/kereta-api/",
                        appUrl = "https://tiket.tokopedia.com/kereta-api/",
                        imageUrl = "https://ecs7.tokopedia.net/tiket-production/kereta-api/img/kai-illustration-01.png"
                ),
                TravelHomepageOrderListModel.Order(
                        product = "EVENTS",
                        title = "Running Man",
                        subtitle = "Istora Senayan, Jakarta",
                        prefix = "",
                        value = "20 Jan 19",
                        webUrl = "https://www.tokopedia.com/events/detail/running-man",
                        appUrl = "https://www.tokopedia.com/events/detail/running-man",
                        imageUrl = "https://ecs7.tokopedia.net/img/banner/2019/7/11/19887731/19887731_a3fb9963-a7ee-430a-814f-0503cf9275a3.jpg"
                )
        ),
        travelMeta = TravelMetaModel(
                title = "Lihat Semua",
                appUrl = "tokopedia.com://orderlist/flight",
                webUrl = "https://www.tokopedia.com/order/list"
        )
)

val DUMMY_RECENT_SEARCH = TravelRecentSearchModel(
        items = arrayListOf(
                TravelRecentSearchModel.Item(
                        product = "HOTEL",
                        title = "Harris Hotel",
                        subtitle = "Stay 10 Jan - 12 Jan 19",
                        prefix = "Check in jam 14.00",
                        prefixStyling = "normal",
                        value = "",
                        webUrl = "",
                        appUrl = "",
                        imageUrl = ""
                ),
                TravelRecentSearchModel.Item(
                        product = "DEALS",
                        title = "Sour Sally",
                        subtitle = "Voucher value Rp 250.000",
                        prefix = "Rp 200.000",
                        prefixStyling = "strikethrough",
                        value = "Rp 190.000",
                        webUrl = "",
                        appUrl = "",
                        imageUrl = ""
                ),
                TravelRecentSearchModel.Item(
                        product = "FLIGHT",
                        title = "Jakarta - Singapore",
                        subtitle = "",
                        prefix = "Mulai Dari",
                        prefixStyling = "normal",
                        value = "Rp 1.990.000",
                        webUrl = "",
                        appUrl = "",
                        imageUrl = ""
                ),
                TravelRecentSearchModel.Item(
                        product = "TRAIN",
                        title = "Jakarta - Bandung",
                        subtitle = "",
                        prefix = "Mulai Dari",
                        prefixStyling = "normal",
                        value = "Rp 130.000",
                        webUrl = "",
                        appUrl = "",
                        imageUrl = ""
                ),
                TravelRecentSearchModel.Item(
                        product = "EVENTS",
                        title = "Running Man",
                        subtitle = "Running Man Fans Meeting Jakarta",
                        prefix = "Mulai Dari",
                        prefixStyling = "normal",
                        value = "Rp 1.000.000",
                        webUrl = "",
                        appUrl = "",
                        imageUrl = ""
                )
        ),
        travelMeta = TravelMetaModel(
                title = "Terakhir dicari",
                appUrl = "",
                webUrl = ""
        )
)

val DUMMY_RECOMMENDATION = TravelHomepageRecommendationModel(
        items = arrayListOf(
                TravelHomepageRecommendationModel.Item(
                        product = "DEALS",
                        title = "Sour Sally",
                        subtitle = "Voucher value Rp 250.000",
                        prefix = "Rp 200.000",
                        prefixStyling = "strikethrough",
                        value = "Rp 190.000",
                        webUrl = "",
                        appUrl = "",
                        imageUrl = ""
                ),
                TravelHomepageRecommendationModel.Item(
                        product = "DEALS",
                        title = "KFC",
                        subtitle = "Voucher value",
                        prefix = "Mulai Dari",
                        prefixStyling = "normal",
                        value = "Rp 50.000",
                        webUrl = "",
                        appUrl = "",
                        imageUrl = ""
                )
        ),
        travelMeta = TravelMetaModel(
                title = "Pilihan DEALS terbaik",
                appUrl = "https://www.tokopedia.com/order/list",
                webUrl = "tokopedia.com://orderlist/flight"
        )
)

val DUMMY_DESTINATION = TravelHomepageDestinationModel(
        destination = arrayListOf(
                TravelHomepageDestinationModel.Destination(
                        attributes = TravelHomepageDestinationModel.Attribute(
                                title = "Bandung",
                                subtitle = "Kota Pahlawan",
                                webUrl = "https://www.tokopedia.com/blog/bandung",
                                appUrl = "tokopedia://subhomepage/destination?city_id=1",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2018/7/28/5253715/5253715_60e64e5f-135e-47f6-aba3-d4b7cc15aa68.jpg"
                        )
                ),
                TravelHomepageDestinationModel.Destination(
                        attributes = TravelHomepageDestinationModel.Attribute(
                                title = "Yogyakarta",
                                subtitle = "Kota Bersejarah",
                                webUrl = "https://www.tokopedia.com/blog/yogyakarta",
                                appUrl = "tokopedia://subhomepage/destination?city_id=2",
                                imageUrl = "https://ecs7.tokopedia.net/img/attachment/2018/7/28/5253715/5253715_60e64e5f-135e-47f6-aba3-d4b7cc15aa68.jpg"
                        )
                )
        ),
        meta = TravelHomepageDestinationModel.MetaModel(
                title = "Destinasi Impian"
        )
)