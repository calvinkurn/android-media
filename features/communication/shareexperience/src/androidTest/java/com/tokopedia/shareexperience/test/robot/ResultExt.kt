package com.tokopedia.shareexperience.test.robot

import com.tokopedia.shareexperience.test.robot.general.GeneralResult
import com.tokopedia.shareexperience.test.robot.properties.PropertiesResult

fun generalResult(func: GeneralResult.() -> Unit) = GeneralResult.apply(func)
fun propertiesResult(func: PropertiesResult.() -> Unit) = PropertiesResult.apply(func)
