package com.tokopedia.referral.domain.model

/**
 * Created by ashwanityagi on 06/06/18.
 */

class ShareApps(private var packageName: String?, var icon: Int) {
    var id: Int = 0
    var name: String? = null
    var index: Int = 0

    fun getpackageName(): String? {
        return packageName
    }

    fun setpackageName(packageName: String) {
        this.packageName = packageName
    }
}
