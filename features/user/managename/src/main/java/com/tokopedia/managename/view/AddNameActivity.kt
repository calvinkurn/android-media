package com.tokopedia.managename

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.base.di.component.AppComponent


/**
 * Created by Yoris Prayogo on 2020-06-03.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddNameActivity : BaseSimpleActivity(), HasComponent {
    protected val newFragment: androidx.fragment.app.Fragment?
        protected get() = AddNameFragment.newInstance(Bundle())

    val component: AppComponent
        get() = (getApplication() as MainApplication).getAppComponent()

    companion object {
        fun newInstance(context: android.content.Context?): Intent {
            return Intent(context, AddNameActivity::class.java)
        }

        @DeepLink(ApplinkConst.ADD_NAME_PROFILE)
        fun getCallingApplinkIntent(context: android.content.Context?, bundle: Bundle): Intent {
            val uri: android.net.Uri.Builder = android.net.Uri.parse(bundle.getString(DeepLink.URI)).buildUpon()
            val intent: Intent = newInstance(context)
            return intent.setData(uri.build())
        }
    }
}
