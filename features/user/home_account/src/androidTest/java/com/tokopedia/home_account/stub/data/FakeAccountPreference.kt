package com.tokopedia.home_account.stub.data

import android.content.Context
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.pref.AccountPreference
import javax.inject.Inject

class FakeAccountPreference @Inject constructor(context: Context) : AccountPreference(context) {

    override fun isShowCoachmark(): Boolean {
        return false
    }
}