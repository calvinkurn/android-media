package com.tokopedia.onboarding.view.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.common.IOnBackPressed
import com.tokopedia.onboarding.di.DaggerOnboardingComponent
import com.tokopedia.onboarding.di.OnboardingComponent
import com.tokopedia.onboarding.di.module.DynamicOnboardingQueryModule
import com.tokopedia.onboarding.domain.model.ConfigDataModel
import com.tokopedia.onboarding.view.fragment.DynamicOnboardingFragment
import com.tokopedia.onboarding.view.fragment.OnboardingFragment
import com.tokopedia.onboarding.view.viewmodel.DynamicOnboardingViewModel
import com.tokopedia.onboarding.view.viewmodel.DynamicOnboardingViewModel.Companion.JOB_WAS_CANCELED
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2020-02-08.
 * ade.hadian@tokopedia.com
 */

class OnboardingActivity : BaseSimpleActivity(), HasComponent<OnboardingComponent> {

    private val URI_COACHMARK = "coachmark"
    private val URI_COACHMARK_ENABLE = "enable"
    private val URI_COACHMARK_DISABLE = "disable"

    @Inject
    lateinit var onboardingAnalytics: OnboardingAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(DynamicOnboardingViewModel::class.java) }

    private lateinit var remoteConfig: RemoteConfig

    private var loadTime = 0L

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int = R.layout.activity_onboarding

    override fun getParentViewResourceID(): Int = R.id.parent_view

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun getComponent(): OnboardingComponent = DaggerOnboardingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .dynamicOnboardingQueryModule(DynamicOnboardingQueryModule(this))
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        val intent = intent
        var uri: Uri? = null

        var isCoachmarkApplink = false
        if (intent != null) {
            uri = intent.data
            if (uri != null) {
                isCoachmarkApplink = uri.getPathSegments().size == 3 && uri.getPathSegments().get(1) == URI_COACHMARK
            }
        }
        if (isCoachmarkApplink) {
            handleCoachmarkUri(uri)
        } else {
            initObserver()
            loadTime = System.currentTimeMillis()
            viewModel.getData()
            fetchAbTesting()
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
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

    private fun handleCoachmarkUri(uri: Uri?) {
        uri?.lastPathSegment?.let {
            if (it.startsWith(URI_COACHMARK_DISABLE)) {
                CoachMark2.isCoachmmarkShowAllowed = false
                finish()
            }
        }
    }

    private fun initObserver() {
        viewModel.configData.observe(this, Observer { result ->
            when(result) {
                is Success -> { onGetDynamicOnboardingSuccess(result.data) }
                is Fail -> { onGetDynamicOnboardingFailed(result.throwable) }
            }
        })
    }

    private fun onGetDynamicOnboardingSuccess(data: ConfigDataModel) {
        loadTime = System.currentTimeMillis() - loadTime
        onboardingAnalytics.trackDynamicOnboardingPage(true, loadTime, "")

        val bundle = Bundle()
        intent.extras?.let { bundle.putAll(it) }
        bundle.putParcelable(DynamicOnboardingFragment.ARG_DYNAMIC_ONBAORDING_DATA, data)

        val dynamicOnboardingFragment = DynamicOnboardingFragment.createInstance(bundle)
        directPageTo(dynamicOnboardingFragment)
    }

    private fun onGetDynamicOnboardingFailed(throwable: Throwable) {
        loadTime = System.currentTimeMillis() - loadTime

        if (throwable.message == JOB_WAS_CANCELED) {
            onboardingAnalytics.trackDynamicOnboardingPage(false, loadTime, JOB_WAS_CANCELED)
        } else {
            onboardingAnalytics.trackDynamicOnboardingPage(false, loadTime, throwable.message ?: "")
        }

        showStaticOnboarding()
    }

    private fun showStaticOnboarding() {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        val onboardingFragment = OnboardingFragment.createInstance(bundle)
        directPageTo(onboardingFragment)
    }

    private fun directPageTo(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(parentViewResourceID, fragment, tagFragment)
                .commit()
    }

    private fun fetchAbTesting() {
        try {
            RemoteConfigInstance.getInstance().abTestPlatform.fetch(object : RemoteConfig.Listener{
                override fun onComplete(remoteConfig: RemoteConfig?) {}
                override fun onError(e: Exception?) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}