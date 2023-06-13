package com.tokopedia.logisticseller.ui.reschedulepickup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.di.DaggerReschedulePickupComponent
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleErrorAction
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupAction
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupErrorState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupUiEvent
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ReschedulePickupActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[ReschedulePickupViewModel::class.java]
    }

    private val orderId by lazy {
        intent?.getStringExtra(LogisticSellerConst.PARAM_ORDER_ID)
            ?: intent?.data?.getQueryParameter(LogisticSellerConst.PARAM_ORDER_ID).orEmpty()
    }

    private fun injectComponent() {
        DaggerReschedulePickupComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)
        viewModel.onEvent(ReschedulePickupUiEvent.LoadRescheduleInfo(orderId))
        setContent {
            LaunchedEffect(key1 = viewModel.uiEffect, block = {
                viewModel.uiEffect.collectLatest {
                    when (it) {
                        is ReschedulePickupAction.OpenTnCWebView -> goToWebView(it.url)
                        is ReschedulePickupAction.ClosePage -> onClickDialogButton(it.success)
                        is ReschedulePickupAction.ShowError -> mapErrorState(error = it.error)
                    }
                }
            })
            NestTheme {
                val state = viewModel.uiState.collectAsState()
                ReschedulePickupScreen(
                    state = state.value,
                    input = viewModel.input,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }

    private fun mapErrorState(error: ReschedulePickupErrorState) {
        when (error.action) {
            RescheduleErrorAction.SHOW_TOASTER_FAILED_SAVE_RESCHEDULE -> {
                showToaster(error.message) {
                    viewModel.onEvent(
                        ReschedulePickupUiEvent.SaveReschedule
                    )
                }
            }
            RescheduleErrorAction.SHOW_EMPTY_STATE -> {
                this.window.decorView.findViewById<View>(android.R.id.content)
                    ?.let { rootView ->
                        NetworkErrorHelper.showEmptyState(
                            this,
                            rootView,
                            this@ReschedulePickupActivity::getInitialData
                        )
                    }
            }
            RescheduleErrorAction.SHOW_TOASTER_FAILED_GET_RESCHEDULE -> {
                showToaster(error.message) {
                    getInitialData()
                }
            }
        }
    }

    private fun goToWebView(url: String) {
        RouteManager.route(
            this@ReschedulePickupActivity,
            ApplinkConst.WEBVIEW.plus("?url=$url")
        )
    }

    private fun onClickDialogButton(success: Boolean) {
        if (success) {
            setResult(Activity.RESULT_OK, Intent())
        }
        finish()
    }

    private fun showToaster(message: String, onClick: () -> Unit) {
        val rootView: View? = this.window.decorView.findViewById(android.R.id.content)
        rootView?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR
            ) {
                onClick()
            }.show()
        }
    }

    private fun getInitialData() {
        viewModel.onEvent(
            ReschedulePickupUiEvent.LoadRescheduleInfo(
                orderId = orderId
            )
        )
    }
}
