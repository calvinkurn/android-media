package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.view.model.Footer
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.Status

object OnGoingPromoStaticData {
    fun provideStaticData() = OnGoingPromoListUiModel(
            title = "Your On Going Promotion",
            items = listOf(
                    OnGoingPromoUiModel(
                            title = "Flash Sale",
                            status = Status(
                                    text = "On Going",
                                    count = 1,
                                    url = ""
                            ),
                            footer = Footer(
                                    text = "See past promotion",
                                    url = ""
                            )
                    ),
                    OnGoingPromoUiModel(
                            title = "Voucher Toko",
                            status = Status(
                                    text = "On Going",
                                    count = 2,
                                    url = ""
                            ),
                            footer = Footer(
                                    text = "See past promotion",
                                    url = ""
                            )
                    ),
                    OnGoingPromoUiModel(
                            title = "TopAds",
                            status = Status(
                                    text = "On Going",
                                    count = 3,
                                    url = ""
                            ),
                            footer = Footer(
                                    text = "See past promotion",
                                    url = ""
                            )
                    ),
                    OnGoingPromoUiModel(
                            title = "Broadcast Chat",
                            status = Status(
                                    text = "On Going",
                                    count = 4,
                                    url = ""
                            ),
                            footer = Footer(
                                    text = "See past promotion",
                                    url = ""
                            )
                    )
            ),
            errorMessage = ""
    )
}