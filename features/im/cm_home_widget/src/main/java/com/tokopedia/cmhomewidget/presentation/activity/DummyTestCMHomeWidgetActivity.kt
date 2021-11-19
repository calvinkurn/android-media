package com.tokopedia.cmhomewidget.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cmhomewidget.databinding.ActivityDummyTestCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.component.CMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.component.DaggerCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.cmhomewidget.viewmodel.DummyTestCMHomeWidgetViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_dummy_test_cm_home_widget.*
import timber.log.Timber
import java.util.logging.Logger
import javax.inject.Inject

// DeepLink-> tokopedia://dummy-cm-home-widget
// todo delete cm home widget dummy things
class DummyTestCMHomeWidgetActivity : AppCompatActivity(), HasComponent<CMHomeWidgetComponent>,
    CMHomeWidgetCloseClickListener {

    private lateinit var binding: ActivityDummyTestCmHomeWidgetBinding
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
        //inject dependencies
        cmHomeWidgetComponent.inject(this)
        super.onCreate(savedInstanceState)
        //view binding
        binding = ActivityDummyTestCmHomeWidgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        addObservers()
        getCMHomeWidgetData()
    }

    private fun initListener() {
        cmHomeWidget.setOnCMHomeWidgetCloseClickListener(this)
    }

    private fun addObservers() {
        dummyTestCMHomeWidgetViewModel.getCMHomeWidgetDataLiveData.observe(this, { result ->
            when (result) {
                is Success -> {
                    result.data.cmHomeWidgetData?.let { cmHomeWidget.onCMHomeWidgetDataReceived(it) }
                        ?: hideCMHomeWidget()
                }
                is Fail -> {
                    Timber.e(result.throwable, "Failed")
                    hideCMHomeWidget()
                }
            }
        }
        )

        dummyTestCMHomeWidgetViewModel.deleteCMHomeWidgetDataLiveData.observe(this, { result ->
            when (result) {
                is Success -> {
                    hideCMHomeWidget()
                }
                is Fail -> {
                    Timber.e(result.throwable, "Failed")
                }
            }
        }
        )
    }

    private fun getCMHomeWidgetData() {
        hideCMHomeWidget()
        dummyTestCMHomeWidgetViewModel.getCMHomeWidgetData()
    }

    private fun hideCMHomeWidget() {
        cmHomeWidget.hideCMHomeWidget()
    }

    override fun onCMHomeWidgetDismissClick(parentID: Long, campaignID: Long) {
        dummyTestCMHomeWidgetViewModel.deleteCMHomeWidgetData(0, 0)
    }
}