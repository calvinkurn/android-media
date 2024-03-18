package com.tokopedia.topads.sdk.old.view

import com.tokopedia.topads.sdk.domain.model.CpmModel

/**
 * Created by errysuprayogi on 12/28/17.
 */
class BannerAdsContract {
    interface View {
        fun showLoading()
        fun displayAds(cpmModel: CpmModel?, index: Int, isReimagine: Boolean = false)
        fun onCanceled()
        fun hideLoading()
    }
}
