package com.tokopedia.mvcwidget

import android.text.SpannableString

data class MvcData(val animatedInfoList: List<AnimatedInfos?>?)

interface MvcListItem
data class MvcCouponListItem(val urlList: List<String?>?, val title1: String, val title2: String, val title3: SpannableString) : MvcListItem
data class TickerText(val text: String,val removeTickerTopMargin:Boolean) : MvcListItem