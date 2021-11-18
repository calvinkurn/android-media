package com.tokopedia.cmhomewidget.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cmhomewidget.R
import com.tokopedia.cmhomewidget.di.component.CMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.component.DaggerCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.viewmodel.DummyTestCMHomeWidgetViewModel
import javax.inject.Inject

// todo delete cm home widget dummy things
class DummyTestCMHomeWidgetActivity : AppCompatActivity(), HasComponent<CMHomeWidgetComponent> {


    private val cmHomeWidgetComponent: CMHomeWidgetComponent by lazy { initInjector() }

    private fun initInjector() =
        DaggerCMHomeWidgetComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val dummyTestCMHomeWidgetViewModel: DummyTestCMHomeWidgetViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(DummyTestCMHomeWidgetViewModel::class.java)
    }

    override fun getComponent() = cmHomeWidgetComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        cmHomeWidgetComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy_test_cm_home_widget)
        addObservers()
        getCMHomeWidgetData()
//        deleteCMHomeWidgetData()
    }

    private fun addObservers() {
        dummyTestCMHomeWidgetViewModel.getCMHomeWidgetDataLiveData.observe(this, {
            Log.e("GET CM HOME DATA", it.toString())
        }
        )

        dummyTestCMHomeWidgetViewModel.deleteCMHomeWidgetDataLiveData.observe(this, {
            Log.e("DELETE CM HOME DATA", it.toString())
        }
        )
    }

    fun getCMHomeWidgetData() {
        dummyTestCMHomeWidgetViewModel.getCMHomeWidgetData()
    }

    fun deleteCMHomeWidgetData() {
        dummyTestCMHomeWidgetViewModel.deleteCMHomeWidgetData(234, 123)
    }

//   DeepLink-> tokopedia://dummy-cm-home-widget

}