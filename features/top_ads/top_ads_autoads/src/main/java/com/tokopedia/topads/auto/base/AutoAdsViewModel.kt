package com.tokopedia.topads.auto.base

import android.arch.lifecycle.ViewModel
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoy

/**
 * Author errysuprayogi on 16,May,2019
 */
abstract class AutoAdsViewModel(
        private val adsRepositoy: AutoTopAdsRepositoy
) : ViewModel() {


}