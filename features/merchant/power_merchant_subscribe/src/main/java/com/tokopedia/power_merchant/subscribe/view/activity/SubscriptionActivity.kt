package com.tokopedia.power_merchant.subscribe.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscriptionFragment
import com.tokopedia.power_merchant.subscribe.view.viewmodel.SubscriptionActivityViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_pm_subsription.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class SubscriptionActivity : BaseActivity(), HasComponent<PowerMerchantSubscribeComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel: SubscriptionActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
                .get(SubscriptionActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView(R.layout.activity_pm_subsription)
        window.decorView.setBackgroundColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))

        observePmSettingInfo()
        setupView()
    }

    override fun getComponent(): PowerMerchantSubscribeComponent {
        val appComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerPowerMerchantSubscribeComponent.builder()
                .baseAppComponent(appComponent)
                .build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setupView() {
        setSupportActionBar(toolbarPmSubscription)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setWhiteStatusBar()
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun observePmSettingInfo() {
        getPowerMerchantSettingInfo()
        mViewModel.powerMerchantSettingInfo.observe(this, Observer {
            when (it) {
                is Success -> renderPage(it.data)
                is Fail -> showErrorToaster(it.throwable) {
                    getPowerMerchantSettingInfo()
                }
            }
            loaderPmSubscription.gone()
        })
    }

    private fun getPowerMerchantSettingInfo() {
        mViewModel.getPowerMerchantSettingInfo()
        loaderPmSubscription.visible()
    }

    private fun renderPage(data: PowerMerchantSettingInfoUiModel) {
        val fragment = getFragmentByPeriod(data.periodeType)

        val containerViewId = R.id.framePmFragmentContainer
        val isFragmentNotAttachedYet = supportFragmentManager.findFragmentById(containerViewId) == null
        if (isFragmentNotAttachedYet) {
            supportFragmentManager.beginTransaction()
                    .replace(containerViewId, fragment)
                    .commit()
        }
    }

    private fun getFragmentByPeriod(periodeType: String): Fragment {
        return when (periodeType) {
            PeriodType.FINAL_PERIOD, PeriodType.TRANSITION_PERIOD -> PowerMerchantSubscriptionFragment.createInstance()
            else -> PowerMerchantSubscribeFragment.createInstance()
        }
    }

    private fun showErrorToaster(throwable: Throwable, action: () -> Unit) {
        Toaster.build(framePmFragmentContainer, ErrorHandler.getErrorMessage(this, throwable),
                Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                getString(R.string.error_cancellation_tryagain),
                View.OnClickListener {
                    action()
                }
        ).show()
    }
}