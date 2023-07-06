package com.tokopedia.shop.score.penalty.presentation.old.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.common.plt.ShopPenaltyPerformanceMonitoringListener
import com.tokopedia.shop.score.common.plt.ShopPenaltyPltConstants
import com.tokopedia.shop.score.common.presentation.fragments.ShopPenaltyContainerFragment
import com.tokopedia.shop.score.penalty.di.component.DaggerPenaltyComponent
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.old.fragment.ShopPenaltyPageOldFragment
import com.tokopedia.utils.accelerometer.orientation.AccelerometerOrientationListener

open class ShopPenaltyPageOldActivity : BaseSimpleActivity(), HasComponent<PenaltyComponent>,
    ShopPenaltyPerformanceMonitoringListener {

    private var pageLoadTimePerformance: PageLoadTimePerformanceInterface? = null

    private val accelerometerOrientationListener: AccelerometerOrientationListener by lazy {
        AccelerometerOrientationListener(contentResolver) {
            onAccelerometerOrientationSettingChange(it)
        }
    }

    override fun getNewFragment(): Fragment {
        return if (DeviceScreenInfo.isTablet(this)) {
            ShopPenaltyContainerFragment.newInstance()
        } else {
            ShopPenaltyPageOldFragment.newInstance()
        }
    }

    override fun getComponent(): PenaltyComponent {
        return DaggerPenaltyComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityOrientation()
        startPerformanceMonitoring()
    }

    override fun onResume() {
        super.onResume()
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.register()
        }
    }

    override fun onPause() {
        super.onPause()
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.unregister()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is TkpdBaseV4Fragment) {
                if (it.onFragmentBackPressed()) return
            }
        }
        super.onBackPressed()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformance = PageLoadTimePerformanceCallback(
            ShopPenaltyPltConstants.SHOP_PENALTY_PLT_PREPARE_METRICS,
            ShopPenaltyPltConstants.SHOP_PENALTY_PLT_NETWORK_METRICS,
            ShopPenaltyPltConstants.SHOP_PENALTY_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
        pageLoadTimePerformance?.startMonitoring(ShopPenaltyPltConstants.SHOP_PENALTY_TRACE)
        pageLoadTimePerformance?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformance?.stopMonitoring()
        pageLoadTimePerformance = null
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformance?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformance?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformance?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformance?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformance?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformance?.stopRenderPerformanceMonitoring()
    }

    private fun setActivityOrientation() {
        if (DeviceScreenInfo.isTablet(this)) {
            val isAccelerometerRotationEnabled = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            ) == 1
            requestedOrientation =
                if (isAccelerometerRotationEnabled)
                    ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun onAccelerometerOrientationSettingChange(isEnabled: Boolean) {
        if (DeviceScreenInfo.isTablet(this)) {
            requestedOrientation =
                if (isEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun clearFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            transaction.remove(fragment)
        }
        transaction.commitNowAllowingStateLoss()
    }

    override fun inflateFragment() {
        if (DeviceScreenInfo.isTablet(this)) {
            clearFragments()
            supportFragmentManager.beginTransaction().replace(
                com.tokopedia.abstraction.R.id.parent_view,
                newFragment,
                newFragment::class.java.simpleName
            ).commitNowAllowingStateLoss()
        } else {
            super.inflateFragment()
        }
    }
}
