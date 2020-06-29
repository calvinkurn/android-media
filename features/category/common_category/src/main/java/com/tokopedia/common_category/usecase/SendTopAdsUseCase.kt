package com.tokopedia.common_category.usecase

import com.tokopedia.topads.sdk.utils.ImpresionTask

class SendTopAdsUseCase{

    companion object {
        private const val className: String = "com.tokopedia.common_category.usecase.SendTopAdsUseCase";
    }

    fun executeOnBackground(url: String){
        ImpresionTask(className).execute(url)
    }
}