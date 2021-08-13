package com.tokopedia.otp.common.abstraction

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.common.di.OtpComponentBuilder

abstract class BaseOtpActivity : BaseSimpleActivity(), HasComponent<OtpComponent> {

    private var otpComponent: OtpComponent? = null

    override fun getLayoutRes(): Int {
        return com.tokopedia.otp.R.layout.activity_otp
    }

    override fun getParentViewResourceID(): Int {
        return com.tokopedia.otp.R.id.parent_view
    }

    override fun getComponent(): OtpComponent = otpComponent ?: initializeOtpComponent()

    override fun getTagFragment(): String = TAG

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    protected open fun initializeOtpComponent(): OtpComponent =
            OtpComponentBuilder.getComponent(application as BaseMainApplication, this).also {
                otpComponent = it
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

    companion object {
        val TAG = BaseOtpActivity::class.java.name
    }
}