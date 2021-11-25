package com.tokopedia.cmhomewidget.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cmhomewidget.databinding.ActivityDummyTestCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.component.DaggerDummyTestCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.component.DummyTestCMHomeWidgetComponent
import com.tokopedia.cmhomewidget.di.module.DummyTestCMHomeWidgetModule
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCloseClickListener
import com.tokopedia.cmhomewidget.viewmodel.DummyTestCMHomeWidgetViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_dummy_test_cm_home_widget.*
import timber.log.Timber
import javax.inject.Inject

// DeepLink-> tokopedia://dummy-cm-home-widget

// todo delete cm home widget dummy things
class DummyTestCMHomeWidgetActivity : AppCompatActivity(),
    HasComponent<DummyTestCMHomeWidgetComponent>,
    CMHomeWidgetCloseClickListener {

    private val dummyTestCMHomeWidgetComponent: DummyTestCMHomeWidgetComponent by lazy { initInjector() }

    private fun initInjector() =
        DaggerDummyTestCMHomeWidgetComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication).baseAppComponent
            )
            .dummyTestCMHomeWidgetModule(DummyTestCMHomeWidgetModule(this))
            .build()

    @Inject
    lateinit var binding: ActivityDummyTestCmHomeWidgetBinding

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val dummyTestCMHomeWidgetViewModel: DummyTestCMHomeWidgetViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(DummyTestCMHomeWidgetViewModel::class.java)
    }

    override fun getComponent() = dummyTestCMHomeWidgetComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        //inject dependencies
        dummyTestCMHomeWidgetComponent.inject(this)
        super.onCreate(savedInstanceState)
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
                    result.data.cmHomeWidgetData?.let { cmHomeWidget.loadCMHomeWidgetData(it) }
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
                    Timber.e("Success")
                }
                is Fail -> {
                    Timber.e(result.throwable, "Failed")
                    showCMHomeWidget()
                }
            }
        }
        )
    }

    private fun getCMHomeWidgetData() {
        dummyTestCMHomeWidgetViewModel.getCMHomeWidgetData()
    }

    private fun hideCMHomeWidget() {
        cmHomeWidget.visibility = View.GONE
    }

    private fun showCMHomeWidget() {
        cmHomeWidget.visibility = View.VISIBLE
    }

    override fun onCMHomeWidgetDismissClick(parentID: Long, campaignID: Long) {
        hideCMHomeWidget()
        //passing 0,0 intensionally to avoid data deletion and adding data again and again.
        dummyTestCMHomeWidgetViewModel.deleteCMHomeWidgetData(parentID, campaignID)
    }
}