package com.tokopedia.loginregister.external_register.ovo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.fragment.ExternalAccountFinalFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseFinalListener

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoFinalFragment: ExternalAccountFinalFragment(), BaseFinalListener {

    companion object {
        fun createInstance(): OvoFinalFragment {
            return OvoFinalFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.run {
            setTitle(getString(R.string.ovo_external_register_title_success))
            setDescription(getString(R.string.ovo_external_register_description_success))
            setMainImage(imgResId = R.drawable.img_ovo_collaboration)
            setSuccessListener(this@OvoFinalFragment)
        }
    }

    override fun onMainSuccessButtonClicked() {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        activity?.finish()
    }
}