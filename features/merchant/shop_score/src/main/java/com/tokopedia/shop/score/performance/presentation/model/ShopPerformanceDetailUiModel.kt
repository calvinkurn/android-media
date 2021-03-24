package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

data class ShopPerformanceDetailUiModel(@StringRes var descCalculation: Int? = null,
                                        @StringRes var descTips: Int? = null,
                                        @StringRes var moreInformation: Int? = null,
                                        var urlLink: String = "")