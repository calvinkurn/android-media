package com.tokopedia.cmhomewidget.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.di.component.CMHomeWidgetComponent

// todo delete cm home widget dummy things
class DummyTestCMHomeWidgetActivity : AppCompatActivity(), HasComponent<CMHomeWidgetComponent> {

    private val kycComponent: CMHomeWidgetComponent by lazy { initInjector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy_test_cm_home_widget)
    }

    private fun initInjector() =
        DaggerCMHomeWidgetComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

    override fun getComponent() = kycComponent

}