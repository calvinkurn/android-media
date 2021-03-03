package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

data class ShopPerformanceDetailUiModel(@StringRes var descCalculation: Int = 0,
                                        @StringRes var descTips: Int = 0,
                                        @StringRes var moreInformation: Int = 0,
                                        var urlLink: String = "")