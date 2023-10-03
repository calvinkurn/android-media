package com.tokopedia.tokofood.feature.ordertracking.presentation.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.tokopedia.tokochat.config.util.TokoChatConnection
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.tokochat.common.util.TokoChatValueUtil
import com.tokopedia.tokofood.feature.ordertracking.base.presentation.fragment.BaseTokoFoodOrderTrackingFragment
import com.tokopedia.tokofood.feature.ordertracking.di.component.DaggerTokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import javax.inject.Inject

open class TokoFoodOrderTrackingActivity :
    BaseSimpleActivity(),
    HasComponent<TokoFoodOrderTrackingComponent> {

    @Inject
    lateinit var viewModel: TokoFoodOrderTrackingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreSavedInstanceState()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        viewModel.onSavedInstanceState()
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.data?.let {
            Bundle().apply {
                val params = UriUtil.uriQueryParamsToMap(it)
                val orderIdParam = params[DeeplinkMapperTokoFood.PATH_ORDER_ID].orEmpty()
                putString(DeeplinkMapperTokoFood.PATH_ORDER_ID, orderIdParam)
            }
        }?.apply {
            putBoolean(
                TokoChatValueUtil.IS_FROM_BUBBLE_KEY,
                intent.getBooleanExtra(TokoChatValueUtil.IS_FROM_BUBBLE_KEY, false)
            )
        }
        return BaseTokoFoodOrderTrackingFragment.newInstance(bundle)
    }

    override fun getComponent(): TokoFoodOrderTrackingComponent {
        return DaggerTokoFoodOrderTrackingComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .tokoChatConfigComponent(TokoChatConnection.getComponent(this))
            .build()
    }

    protected open fun initInjector() {
        DaggerTokoFoodOrderTrackingComponent
            .builder()
            .baseAppComponent((this.applicationContext as BaseMainApplication).baseAppComponent)
            .tokoChatConfigComponent(TokoChatConnection.getComponent(this))
            .build()
            .inject(this)
    }
}
