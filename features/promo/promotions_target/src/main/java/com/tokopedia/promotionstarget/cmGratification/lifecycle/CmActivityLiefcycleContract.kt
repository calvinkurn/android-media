package com.tokopedia.promotionstarget.cmGratification.lifecycle

import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType

interface CmActivityLiefcycleContract {
    fun cancelJob(entityHashCode: Int, @GratifCancellationExceptionType reason: String?)
}