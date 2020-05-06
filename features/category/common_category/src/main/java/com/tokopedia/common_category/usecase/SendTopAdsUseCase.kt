package com.tokopedia.common_category.usecase

import com.tokopedia.topads.sdk.utils.ImpresionTask

class SendTopAdsUseCase{
    fun executeOnBackground(url: String){
        ImpresionTask().execute(url)
    }
}