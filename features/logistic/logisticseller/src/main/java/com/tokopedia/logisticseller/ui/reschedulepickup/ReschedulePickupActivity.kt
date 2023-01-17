package com.tokopedia.logisticseller.ui.reschedulepickup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.di.DaggerReschedulePickupComponent
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
        setContent {
            NestTheme {
                ReschedulePickupScreen(
                    viewModel.uiState.collectAsState(),
                    viewModel.input,
                    onDayChosen = { onDayChosen(it) },
                    onTimeChosen = { onTimeChosen(it) },
                    onReasonChosen = { onReasonChosen(it) },
                    onSubtitleClicked = {
                        RouteManager.route(this, ApplinkConst.WEBVIEW.plus("?url=$it"))
                    },
                    onOtherReasonChanged = { viewModel.setCustomReason(it) },
                    onSaveReschedule = { viewModel.saveReschedule(orderId) },
                    onBottomSheetClosed = { viewModel.closeBottomSheetState() },
                    onOpenBottomSheet = { viewModel.openBottomSheetState(it) },
                    onCloseDialog = { onClickDialogButton(it) },
                    onClickDialogButton = { onClickDialogButton(it) }
                )
            }
        }

        getInitialData()
    }

    private fun onClickDialogButton(success: Boolean) {
        viewModel.setDialogState(false)
        if (success) {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }

    private fun getInitialData() {
        viewModel.getReschedulePickupDetail(orderId)
    }

    private fun onDayChosen(dayChosen: RescheduleDayOptionModel) {
        viewModel.setDay(dayChosen)
    }

    private fun onReasonChosen(reasonChosen: RescheduleReasonOptionModel) {
        viewModel.setReason(reasonChosen)
    }

    private fun onTimeChosen(timeChosen: RescheduleTimeOptionModel) {
        viewModel.setTime(timeChosen)
    }

//    private fun showErrorToaster(
//        message: String,
//        onClick: () -> Unit
//    ) {
//        view?.let {
//            toaster = Toaster.build(
//                it,
//                message,
//                Toaster.LENGTH_SHORT,
//                Toaster.TYPE_ERROR,
//                getString(R.string.title_cta_error_reschedule_pickup)
//            ) { onClick() }
//        }
//        if (toaster?.isShown == false) {
//            toaster?.show()
//        }
//    }
}
