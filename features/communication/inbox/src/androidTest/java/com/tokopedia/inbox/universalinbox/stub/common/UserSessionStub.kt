package com.tokopedia.inbox.universalinbox.stub.common

import android.content.Context
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class UserSessionStub @Inject constructor(context: Context?) : UserSession(context) {

    var shopData: Pair<String, String>? = Pair(
        "Toko with long nameeeeeeeeeeeee",
        "https://images.tokopedia.net/img/cache/300/tPxBYm/2023/1/20/198655c8-1736-4533-83c4-b1a1c273a3ed.jpg"
    )

    override fun isLoggedIn(): Boolean {
        return true
    }

    override fun hasShop(): Boolean {
        return shopData != null
    }

    override fun getShopAvatar(): String {
        return shopData?.second ?: ""
    }

    override fun getShopName(): String {
        return shopData?.first ?: ""
    }
}
