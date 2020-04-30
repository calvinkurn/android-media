package com.tokopedia.discovery.categoryrevamp.domain.usecase

import com.tokopedia.topads.sdk.utils.ImpresionTask

class SendTopAdsUseCase{
    val className = "com.tokopedia.discovery.categoryrevamp.domain.usecase.SendTopAdsUseCase"
    fun executeOnBackground(url: String){
        ImpresionTask(className).execute(url)
    }
}