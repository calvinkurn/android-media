package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.notifcenter.R

class NotificationFragment : BaseDaggerFragment() {
    override fun getScreenName(): String = "Notification"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun initInjector() {
        // TODO: impl this
    }
}