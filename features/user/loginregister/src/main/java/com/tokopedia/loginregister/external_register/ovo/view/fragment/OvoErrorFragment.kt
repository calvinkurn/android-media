package com.tokopedia.loginregister.external_register.ovo.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.fragment.ExternalAccountFinalFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseFinalListener
import com.tokopedia.loginregister.external_register.ovo.OvoConstants
import com.tokopedia.loginregister.external_register.ovo.view.activity.OvoFinalPageActivity

/**
 * Created by Yoris Prayogo on 06/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class OvoErrorFragment: ExternalAccountFinalFragment(), BaseFinalListener {

    companion object {
        fun createInstance(): OvoErrorFragment {
            return OvoErrorFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.run {
            ovoCreationAnalytics.trackViewOvoFailPage()
            setTitle(getString(R.string.ovo_external_register_title_error))
            setDescription(getString(R.string.ovo_external_register_description_error))
            setMainImage(imgUrl = OvoConstants.IMG_ERROR_OVO)
            setButtonText(getString(R.string.base_add_name_next))
            setSuccessListener(this@OvoErrorFragment)
        }
    }

    override fun onMainSuccessButtonClicked() {
        ovoCreationAnalytics.trackClickOvoFailBtn()
        if(arguments?.getBoolean(OvoFinalPageActivity.KEY_GOTO_REGISTER) == true){
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.INIT_REGISTER)
            intent.putExtra(OvoFinalPageActivity.KEY_GOTO_REGISTER, true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            activity?.setResult(Activity.RESULT_CANCELED)
        }
        activity?.finish()
    }

    fun onBackButtonClicked() {
        ovoCreationAnalytics.trackClickOvoFailBackBtn()
    }
}