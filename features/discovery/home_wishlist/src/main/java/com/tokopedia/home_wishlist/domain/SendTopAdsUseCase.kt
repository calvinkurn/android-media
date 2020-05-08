package com.tokopedia.home_wishlist.domain

import com.tokopedia.topads.sdk.utils.ImpresionTask

class SendTopAdsUseCase{
    fun executeOnBackground(url: String){
        ImpresionTask().execute(url)
    }
}