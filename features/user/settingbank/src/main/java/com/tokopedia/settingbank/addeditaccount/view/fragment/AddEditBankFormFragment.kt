package com.tokopedia.settingbank.addeditaccount.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.analytics.AddEditBankAnalytics
import com.tokopedia.settingbank.addeditaccount.view.listener.AddEditBankContract

/**
 * @author by nisie on 6/21/18.
 */

class AddEditBankFormFragment : AddEditBankContract.View,
        BaseDaggerFragment() {

    override fun getScreenName(): String {
       return AddEditBankAnalytics.SCREEN_NAME_ADD
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_add_edit_bank_form, container, false)
    }
}
