package com.tokopedia.shop.score.stub.penalty.domain.mapper

import android.content.Context
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper

class PenaltyMapperStub(
    context: Context,
    shopScorePrefManager: ShopScorePrefManager
): PenaltyMapper(context, shopScorePrefManager)
