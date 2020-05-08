package com.tokopedia.discovery.categoryrevamp.domain.usecase

import com.tokopedia.topads.sdk.utils.ImpresionTask

class SendTopAdsUseCase{
    fun executeOnBackground(url: String){
        ImpresionTask().execute(url)
    }
}