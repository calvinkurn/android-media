package com.tokopedia.top_ads_headline.data

import androidx.annotation.StringRes

data class TopAdsHeadlineTabModel(var count: Int = 0,
                                  var id: Int = 0,
                                  @StringRes var name: Int = 0,
                                  var isSelected: Boolean = false)