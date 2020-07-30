package com.tokopedia.home_wishlist.domain

import com.tokopedia.topads.sdk.utils.ImpresionTask

class SendTopAdsUseCase{

    companion object{
        private const val className: String = "com.tokopedia.home_wishlist.domain.SendTopAdsUseCase"
    }

    fun executeOnBackground(url: String){
        ImpresionTask(className).execute(url)
    }
}