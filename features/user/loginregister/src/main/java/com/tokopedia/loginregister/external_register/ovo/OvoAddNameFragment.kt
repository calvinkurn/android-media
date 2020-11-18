package com.tokopedia.loginregister.external_register.ovo

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.external_register.base.fragment.BaseAddNameFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseAddNameListener

/**
 * Created by Yoris Prayogo on 16/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoAddNameFragment: BaseAddNameFragment(), BaseAddNameListener {

    override fun onNextButtonClicked() {}

    override fun onOtherMethodButtonClicked() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseAddNameListener = this
    }

    companion object {
        fun createInstance(bundle: Bundle? = null): Fragment {
            val fragment = OvoAddNameFragment()
            bundle?.run {
                return fragment.apply {
                    arguments = this@run
                }
            }
            return fragment
        }
    }
}