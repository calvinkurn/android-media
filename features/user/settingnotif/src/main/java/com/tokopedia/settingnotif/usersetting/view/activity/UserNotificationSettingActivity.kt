package com.tokopedia.settingnotif.usersetting.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingnotif.usersetting.const.Unify.Neutral_N0
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingTypeFragment
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingTypeDataView

typealias ParentActivity = UserNotificationSettingActivity

class UserNotificationSettingActivity : BaseSimpleActivity(),
        SettingTypeFragment.SettingTypeContract {

    private var fragmentContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        setupView()
    }

    private fun bindView() {
        fragmentContainer = findViewById(parentViewResourceID)
    }

    private fun setupView() {
        val color = ContextCompat.getColor(this, Neutral_N0)
        fragmentContainer?.setBackgroundColor(color)
    }

    override fun getNewFragment(): Fragment {
        return SettingTypeFragment()
    }

    override fun openSettingField(settingType: SettingTypeDataView) {
        val fragment = supportFragmentManager
                .findFragmentByTag(getString(settingType.name))
                ?: settingType.createNewFragmentInstance()

        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(parentViewResourceID, fragment, getString(settingType.name))
                .commit()
    }

    fun openSellerFiled() {
        openSettingField(SettingTypeDataView.createSellerType())
    }

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

}
