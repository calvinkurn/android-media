package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.inboxcommon.InboxCommonFragment
import com.tokopedia.notifcenter.R

class NotificationFragment : BaseDaggerFragment(), InboxCommonFragment {
    override fun getScreenName(): String = "Notification"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onRoleChanged(role: Int) {
        Toast.makeText(context, "Role Changed, $role", Toast.LENGTH_SHORT).show()
    }

    override fun initInjector() {
        // TODO: impl this
    }
}