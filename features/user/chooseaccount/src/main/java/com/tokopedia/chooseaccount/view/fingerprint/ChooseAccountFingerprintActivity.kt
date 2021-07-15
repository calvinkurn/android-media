package com.tokopedia.chooseaccount.view.fingerprint

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.chooseaccount.di.ChooseAccountComponent
import com.tokopedia.chooseaccount.di.DaggerChooseAccountComponent
import com.tokopedia.chooseaccount.common.di.DaggerLoginRegisterPhoneComponent

class ChooseAccountFingerprintActivity: BaseSimpleActivity(), HasComponent<ChooseAccountComponent> {

    override fun getScreenName(): String {
        return ""
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null)
            bundle.putAll(intent.extras)
        return ChooseAccountFingerprintFragment.createInstance(bundle)
    }

    override fun getComponent(): ChooseAccountComponent {
        val appComponent = (application as BaseMainApplication)
            .baseAppComponent
        val loginRegisterPhoneComponent = DaggerLoginRegisterPhoneComponent.builder()
            .baseAppComponent(appComponent).build()
        return DaggerChooseAccountComponent.builder()
            .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
            .build()
    }

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setWindowFlag(on: Boolean) {
        val winParams = window.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        window.attributes = winParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }
}