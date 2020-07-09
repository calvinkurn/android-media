package com.tokopedia.product.detail.usecase

import com.tokopedia.topads.sdk.utils.ImpresionTask
import javax.inject.Inject

/**
 * Created by Yehezkiel on 01/04/20
 */
class SendTopAdsUseCase @Inject constructor() {

    companion object {
        private const val className: String = "com.tokopedia.product.detail.usecase.SendTopAdsUseCase"
    }

    fun executeOnBackground(url: String) {
        ImpresionTask(className).execute(url)
    }
} 