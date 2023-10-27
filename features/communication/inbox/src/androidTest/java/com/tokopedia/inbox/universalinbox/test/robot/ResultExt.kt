package com.tokopedia.inbox.universalinbox.test.robot

import com.tokopedia.inbox.universalinbox.test.robot.general.GeneralResult
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult
import com.tokopedia.inbox.universalinbox.test.robot.topads.TopAdsResult
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult

fun generalResult(func: GeneralResult.() -> Unit) = GeneralResult.apply(func)
fun menuResult(func: MenuResult.() -> Unit) = MenuResult.apply(func)
fun widgetResult(func: WidgetResult.() -> Unit) = WidgetResult.apply(func)
fun recommendationResult(func: RecommendationResult.() -> Unit) = RecommendationResult.apply(func)
fun topAdsResult(func: TopAdsResult.() -> Unit) = TopAdsResult.apply(func)
