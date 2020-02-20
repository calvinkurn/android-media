package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.activity_preference_edit.*

class PreferenceEditActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_edit)
        initViews()
    }

    private fun initViews() {
        btn_back.setOnClickListener {
            onBackPressed()
        }
        showStepper()
        setStepperValue(30, true)
//        supportFragmentManager.beginTransaction().replace(R.id.container, CartFragment()).commit()
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    fun setStepperValue(value: Int, isSmooth: Boolean = true) {
        stepper.setValue(value, isSmooth)
    }

    fun showStepper() {
        tv_subtitle.visible()
        stepper.visible()
    }

    fun hideStepper() {
        tv_subtitle.gone()
        stepper.gone()
    }
}
