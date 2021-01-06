package com.tokopedia.loginregister.external_register.ovo.view

import android.os.Bundle
import android.view.View
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.fragment.ExternalAccountFinalFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseFinalListener

/**
 * Created by Yoris Prayogo on 06/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class OvoErrorFragment: ExternalAccountFinalFragment(), BaseFinalListener {

    companion object {
        fun createInstance(): OvoFinalFragment {
            return OvoFinalFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.run {
            setTitle(getString(R.string.ovo_external_register_title_error))
            setDescription(getString(R.string.ovo_external_register_description_error))
            setMainImage(imgResId = R.drawable.img_ovo_error)
            setSuccessListener(this@OvoErrorFragment)
        }
    }

    override fun onMainSuccessButtonClicked() {
        //
    }
}