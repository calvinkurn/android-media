package com.tokopedia.talk.feature.sellersettings.common.util

interface UserSessionListener {
    fun getUserId(): String
    fun getShopId(): String
}