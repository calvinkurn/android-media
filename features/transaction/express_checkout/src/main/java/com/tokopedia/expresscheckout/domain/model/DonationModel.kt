package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class DonationModel(
        var title: String? = null,
        var nominal: Int = 0,
        var description: String? = null
)