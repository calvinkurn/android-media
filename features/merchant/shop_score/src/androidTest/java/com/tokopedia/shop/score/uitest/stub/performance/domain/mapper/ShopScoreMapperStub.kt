package com.tokopedia.shop.score.uitest.stub.performance.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.user.session.UserSessionInterface

class ShopScoreMapperStub(
    userSessionInterface: UserSessionInterface,
    context: Context,
    shopScorePrefManager: ShopScorePrefManager
): ShopScoreMapper(userSessionInterface, context, shopScorePrefManager)