package com.tokopedia.people.listener

import android.content.Intent

interface FollowerFollowingListener {
    fun callstartActivityFromFragment(intent: Intent, requestCode: Int)
    fun callstartActivityFromFragment(applink: String, requestCode: Int)
}
