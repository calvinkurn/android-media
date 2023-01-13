package com.tokopedia.logisticseller.ui.reschedulepickup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.di.DaggerReschedulePickupComponent
import com.tokopedia.logisticseller.di.ReschedulePickupComponent
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.RescheduleDayBottomSheet
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.RescheduleReasonBottomSheet
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.RescheduleTimeBottomSheet
import com.tokopedia.logisticseller.ui.reschedulepickup.dialog.ReschedulePickupResultDialog
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ReschedulePickupComposeFragment :
    BaseDaggerFragment(),
    RescheduleTimeBottomSheet.ChooseTimeListener,
    RescheduleDayBottomSheet.ChooseDayListener,
    RescheduleReasonBottomSheet.ChooseReasonListener {
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ReschedulePickupComposeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ReschedulePickupComposeViewModel::class.java)
    }

    private var toaster: Snackbar? = null
    private var orderId: String = ""
    override fun getScreenName(): String = ""

    override fun initInjector() {
        val component: ReschedulePickupComponent = DaggerReschedulePickupComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(LogisticSellerConst.PARAM_ORDER_ID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                NestTheme {
                    ReschedulePickupScreen(
                        viewModel.uiState.collectAsState(),
                        viewModel.input,
                        onDayClicked = { openDaySelectionBottomSheet(it) },
                        onTimeClicked = { openTimeSelectionBottomSheet(it) },
                        onReasonClicked = { openReasonSelectionBottomSheet(it) },
                        onSubtitleClicked = {
                            RouteManager.route(context, ApplinkConst.WEBVIEW.plus("?url=$it"))
                        },
                        onOtherReasonChanged = { viewModel.setCustomReason(it) },
                        onSaveReschedule = { viewModel.saveReschedule(orderId) }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInitialData()
    }

    private fun getInitialData() {
        viewModel.getReschedulePickupDetail(orderId)
    }

    private fun openDaySelectionBottomSheet(daysOption: List<RescheduleDayOptionModel>) {
        RescheduleDayBottomSheet().apply {
            setDayOptions(daysOption)
            setListener(this@ReschedulePickupComposeFragment)
        }.show(parentFragmentManager)
    }

    private fun openTimeSelectionBottomSheet(timeOption: List<RescheduleTimeOptionModel>) {
        RescheduleTimeBottomSheet().apply {
            setTimeOptions(timeOption)
            setListener(this@ReschedulePickupComposeFragment)
        }.show(parentFragmentManager)
    }

    private fun openReasonSelectionBottomSheet(reasonOption: List<RescheduleReasonOptionModel>) {
        RescheduleReasonBottomSheet().apply {
            setOptions(reasonOption)
            setListener(this@ReschedulePickupComposeFragment)
        }.show(parentFragmentManager)
    }

    private fun showResultDialog(
        bodyText: String,
        isSuccess: Boolean
    ) {
        context?.let {
            ReschedulePickupResultDialog(it, setResultDialogListener(isSuccess)).apply {
                init()
                if (isSuccess) {
                    setSuccessMessage(bodyText)
                } else {
                    setErrorMessage(bodyText)
                }
                show()
            }
        }
    }

    private fun setResultDialogListener(isSuccess: Boolean): ReschedulePickupResultDialog.ReschedulePickupResultDialogListener {
        return object : ReschedulePickupResultDialog.ReschedulePickupResultDialogListener {
            override fun onClickDialog() {
                super.onClickDialog()
                if (isSuccess) {
                    activity?.run {
                        setResult(Activity.RESULT_OK, Intent())
                        finish()
                    }
                }
            }
        }
    }

    private fun showErrorToaster(
        message: String,
        onClick: () -> Unit
    ) {
        view?.let {
            toaster = Toaster.build(
                it,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR,
                getString(R.string.title_cta_error_reschedule_pickup)
            ) { onClick() }
        }
        if (toaster?.isShown == false) {
            toaster?.show()
        }
    }

    override fun onDayChosen(dayChosen: RescheduleDayOptionModel) {
        viewModel.setDay(dayChosen)
    }

    override fun onReasonChosen(reasonChosen: RescheduleReasonOptionModel) {
        viewModel.setReason(reasonChosen)
    }

    override fun onTimeChosen(timeChosen: RescheduleTimeOptionModel) {
        viewModel.setTime(timeChosen)
    }

    companion object {
        fun newInstance(bundle: Bundle): ReschedulePickupComposeFragment {
            return ReschedulePickupComposeFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        LogisticSellerConst.PARAM_ORDER_ID,
                        bundle.getString(LogisticSellerConst.PARAM_ORDER_ID)
                    )
                }
            }
        }
    }
}
