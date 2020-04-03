package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.topads.sdk.utils.ImpresionTask
import javax.inject.Inject

class SendTopAdsUseCase @Inject constructor(){
    fun executeOnBackground(url: String){
        ImpresionTask().execute(url)
    }
}