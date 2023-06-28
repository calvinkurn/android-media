package com.tokopedia.notifcenter.test.robot

import com.tokopedia.notifcenter.test.robot.detail.DetailResult
import com.tokopedia.notifcenter.test.robot.general.GeneralResult

fun generalResult(func: GeneralResult.() -> Unit) = GeneralResult.apply(func)
fun detailResult(func: DetailResult.() -> Unit) = DetailResult.apply(func)
