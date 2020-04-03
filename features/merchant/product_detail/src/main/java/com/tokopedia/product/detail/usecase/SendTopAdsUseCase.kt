package com.tokopedia.product.detail.usecase

import com.tokopedia.topads.sdk.utils.ImpresionTask
import javax.inject.Inject

/**
 * Created by Yehezkiel on 01/04/20
 */
class SendTopAdsUseCase @Inject constructor() {
    fun executeOnBackground(url: String) {
        ImpresionTask().execute(url)
    }
} 