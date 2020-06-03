package com.tokopedia.flight.dummy

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel

/**
 * @author by furqan on 08/05/2020
 */

val BANNER_DATA = TravelCollectiveBannerModel(
        arrayListOf(
                TravelCollectiveBannerModel.Banner(
                        "1",
                        "Banner Dummy 1",
                        TravelCollectiveBannerModel.Attribute(
                                "Description Banner Dummy 1",
                                "",
                                "tokopedia://dummy",
                                "www.dummyimage.com",
                                ""
                        )
                ),
                TravelCollectiveBannerModel.Banner(
                        "2",
                        "Banner Dummy 2",
                        TravelCollectiveBannerModel.Attribute(
                                "Description Banner Dummy 2",
                                "",
                                "tokopedia://dummy",
                                "www.dummyimage.com",
                                ""
                        )
                )
        )
)

val TICKER_DATA = TravelTickerModel(
        "Dummy Ticker Title",
        "Dummy Ticker Message",
        "www.dummyticker.com",
        1,
        1,
        "",
        "",
        1,
        "Dummy Page",
        false
)