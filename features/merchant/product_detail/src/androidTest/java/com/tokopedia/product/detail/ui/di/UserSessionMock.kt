package com.tokopedia.product.detail.ui.di

import android.content.Context
import com.tokopedia.user.session.UserSession

/**
 * Created by Yehezkiel on 08/04/21
 */

class UserSessionMock(context: Context?) : UserSession(context) {

    override fun getName(): String {
        return "yehezkiel"
    }

    override fun getUserId(): String {
        return "123"
    }

}