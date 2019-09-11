package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingnotif.R

class PushNotifCheckerFragment : BaseDaggerFragment() {

    companion object { }

    override fun getScreenName(): String = "Push Notification Troubleshooter"

    override fun initInjector() { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_push_notif_checker, container, false).also {
            setupToolbar()
        }
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

}