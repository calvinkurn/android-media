package com.tokopedia.loginregister.external_register.ovo.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.app.TaskStackBuilder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.fragment.ExternalAccountFinalFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseFinalListener
import com.tokopedia.loginregister.external_register.ovo.OvoConstants

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoSuccessFragment: ExternalAccountFinalFragment(), BaseFinalListener {

    companion object {
        fun createInstance(): OvoSuccessFragment {
            return OvoSuccessFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.run {
            ovoCreationAnalytics.trackViewOvoSuccessPage()
            setTitle(getString(R.string.ovo_external_register_title_success))
            setDescription(getString(R.string.ovo_external_register_description_success))
            setMainImage(imgUrl = OvoConstants.IMG_SUCCESS_OVO)
            setSuccessListener(this@OvoSuccessFragment)
        }
    }

    override fun onMainSuccessButtonClicked() {
        context?.run {
            ovoCreationAnalytics.trackClickOvoSuccessBtn()
            val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME)
            val nuzIntent = RouteManager.getIntent(context, ApplinkConst.DISCOVERY_NEW_USER)
            val task = TaskStackBuilder.create(this)
            task.addNextIntent(homeIntent)
            task.addNextIntent(nuzIntent)
            task.startActivities()
            activity?.finish()
        }
    }
}