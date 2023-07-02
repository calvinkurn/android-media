package com.tokopedia.notifcenter.test.robot

import com.tokopedia.notifcenter.test.robot.detail.DetailResult
import com.tokopedia.notifcenter.test.robot.filter.FilterResult
import com.tokopedia.notifcenter.test.robot.general.GeneralResult

fun generalResult(func: GeneralResult.() -> Unit) = GeneralResult.apply(func)
fun detailResult(func: DetailResult.() -> Unit) = DetailResult.apply(func)
fun filterResult(func: FilterResult.() -> Unit) = FilterResult.apply(func)
