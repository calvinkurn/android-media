package com.tokopedia.logisticseller.ui.reschedulepickup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.di.DaggerReschedulePickupComponent
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleErrorAction
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class ReschedulePickupActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[ReschedulePickupComposeViewModel::class.java]
    }

    private var orderId: String = ""

    private fun injectComponent() {
        DaggerReschedulePickupComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)
        intent?.let {
            orderId = it.getStringExtra(LogisticSellerConst.PARAM_ORDER_ID).orEmpty()
        }
        observeErrorState()
        setContent {
            NestTheme {
                ReschedulePickupScreen(
                    viewModel.uiState.collectAsState(),
                    viewModel.input,
                    onDayChosen = { viewModel.setDay(it) },
                    onTimeChosen = { viewModel.setTime(it) },
                    onReasonChosen = { viewModel.setReason(it) },
                    onSubtitleClicked = {
                        RouteManager.route(this, ApplinkConst.WEBVIEW.plus("?url=$it"))
                    },
                    onOtherReasonChanged = { viewModel.setCustomReason(it) },
                    onSaveReschedule = { viewModel.saveReschedule(orderId) },
                    onBottomSheetClosed = { viewModel.closeBottomSheetState() },
                    onOpenBottomSheet = { viewModel.openBottomSheetState(it) },
                    onCloseDialog = { onClickDialogButton(it) },
                    onClickDialogButton = { onClickDialogButton(it) },
                    onPressBack = { finish() }
                )
            }
        }

        getInitialData()
    }

    private fun observeErrorState() {
        viewModel.errorState.observe(this) {
            when (it.action) {
                RescheduleErrorAction.SHOW_TOASTER_FAILED_SAVE_RESCHEDULE -> {
                    showToaster(it.message) { viewModel.saveReschedule(orderId) }
                }
                RescheduleErrorAction.SHOW_EMPTY_STATE -> {
                    this.window.decorView.findViewById<View>(android.R.id.content)
                        ?.let { rootView ->
                            NetworkErrorHelper.showEmptyState(
                                this,
                                rootView,
                                this::getInitialData
                            )
                        }
                }
                RescheduleErrorAction.SHOW_TOASTER_FAILED_GET_RESCHEDULE -> {
                    showToaster(it.message) { viewModel.getReschedulePickupDetail(orderId) }
                }
            }
        }
    }

    private fun onClickDialogButton(success: Boolean) {
        viewModel.setDialogState(false)
        if (success) {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
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
        viewModel.getReschedulePickupDetail(orderId)
    }
}
