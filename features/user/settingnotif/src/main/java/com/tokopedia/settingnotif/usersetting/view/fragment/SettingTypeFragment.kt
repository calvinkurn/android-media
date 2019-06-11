package com.tokopedia.settingnotif.usersetting.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingTypeAdapter
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingType

class SettingTypeFragment : BaseDaggerFragment() {

    private lateinit var rvSettingType: RecyclerView

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
            setupSettingTypes()
        }
    }

    private fun bindView(view: View) {
        rvSettingType = view.findViewById(R.id.rv_setting_type)
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

    private fun setupSettingTypes() {
        with (rvSettingType) {
            setHasFixedSize(true)
            adapter = SettingTypeAdapter(SettingType.createSettingTypes())
        }
    }
}