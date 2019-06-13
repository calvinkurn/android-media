package com.tokopedia.settingnotif.usersetting.view.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.presenter.SettingFieldPresenter
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import javax.inject.Inject

abstract class SettingFieldFragment : BaseDaggerFragment(), SettingFieldContract.View {

    @Inject
    lateinit var presenter: SettingFieldPresenter

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val userSettingComponent = DaggerUserSettingComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()

            userSettingComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_setting_field, container, false).also {
            setupToolbar()
        }
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

}