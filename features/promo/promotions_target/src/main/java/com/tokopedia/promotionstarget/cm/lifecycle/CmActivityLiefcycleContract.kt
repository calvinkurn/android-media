package com.tokopedia.promotionstarget.cm.lifecycle

import com.tokopedia.promotionstarget.domain.presenter.GratifCancellationExceptionType

interface CmActivityLiefcycleContract {
    fun cancelJob(entityHashCode: Int, @GratifCancellationExceptionType reason: String?)
}