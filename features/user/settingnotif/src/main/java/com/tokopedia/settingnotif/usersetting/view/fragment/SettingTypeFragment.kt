package com.tokopedia.settingnotif.usersetting.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Button
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingnotif.R

class SettingTypeFragment : BaseDaggerFragment() {

    private lateinit var dummyButton: Button

    private lateinit var settingTypeContract: SettingTypeContract

    interface SettingTypeContract {
        fun openSettingField(field: String)
    }

    override fun getScreenName(): String {
        return "Notifikasi"
    }

    override fun initInjector() {}

    override fun onAttachActivity(context: Context?) {
        if (context is SettingTypeContract) {
            settingTypeContract = context
        } else {
            throw IllegalStateException("The activity must implement SettingTypeContract interface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_setting_type, container, false).also {
            bindView(it)
            setupToolbar()
            addListenerDummyButton()
        }
    }

    private fun bindView(view: View) {
        dummyButton = view.findViewById(R.id.dummy_button)
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

    private fun addListenerDummyButton() {
        dummyButton.setOnClickListener {
            settingTypeContract.openSettingField("Push Notification")
        }
    }
}