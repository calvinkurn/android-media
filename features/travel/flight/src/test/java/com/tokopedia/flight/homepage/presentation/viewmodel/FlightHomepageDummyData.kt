package com.tokopedia.flight.homepage.presentation.viewmodel

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel

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