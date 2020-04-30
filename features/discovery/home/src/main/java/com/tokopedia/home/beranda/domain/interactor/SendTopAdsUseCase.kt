package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.topads.sdk.utils.ImpresionTask
import javax.inject.Inject

class SendTopAdsUseCase @Inject constructor(){
    val className = "com.tokopedia.home.beranda.domain.interactor.SendTopAdsUseCase"
    fun executeOnBackground(url: String){
        ImpresionTask(className).execute(url)
    }
}