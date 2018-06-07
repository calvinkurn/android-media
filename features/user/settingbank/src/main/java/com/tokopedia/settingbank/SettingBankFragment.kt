package com.tokopedia.settingbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * @author by nisie on 6/7/18.
 */

class SettingBankFragment : SettingBankContract.View, BaseDaggerFragment() {

    lateinit var presenter: SettingBankPresenter

    companion object {

        fun newInstance(): SettingBankFragment {
            return SettingBankFragment()
        }
    }


    override fun getScreenName(): String {
        return SettingBankAnalytics.SCREEN_NAME
    }

    override fun initInjector() {
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_setting_bank, container, false)
        return view
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

}