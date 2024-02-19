package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.order.DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_PARAM_DESCRIPTION
import com.tokopedia.applink.order.DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_PARAM_ORDER_ID
import com.tokopedia.applink.order.DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_PARAM_ORDER_L2_CANCELLATION_REASON
import com.tokopedia.applink.order.DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_PARAM_ORDER_STATUS_CODE
import com.tokopedia.applink.order.DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_PARAM_ORDER_STATUS_TEXT
import com.tokopedia.applink.order.DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_PARAM_PRIMARY_BUTTON_TEXT
import com.tokopedia.applink.order.DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_PARAM_SECONDARY_BUTTON_TEXT
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.buyer_request_cancel.di.BuyerRequestCancelRespondComponent
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.ERROR_ACCEPTING_ORDER
import com.tokopedia.sellerorder.databinding.FragmentBuyerRequestCancelRespondBinding
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class BuyerRequestCancelRespondFragment :
    BaseDaggerFragment(),
    IBuyerRequestCancelRespondListener.Mediator,
    IBuyerRequestCancelRespondListener by BuyerRequestCancelRespondListenerImpl(),
    IBuyerRequestCancelRespondBottomSheetManager.Mediator,
    IBuyerRequestCancelRespondBottomSheetManager by BuyerRequestCancelRespondBottomSheetManagerImpl() {

    companion object {
        private const val SCREEN_NAME = "buyer-request-cancel-respond"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel by lazyThreadSafetyNone {
        ViewModelProvider(this, viewModelFactory)[BuyerRequestCancelRespondViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentBuyerRequestCancelRespondBinding> {
        bottomSheetBuyerRequestCancelRespond?.clearViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createView(inflater, container)
        registerBuyerRequestCancelRespondBottomSheet(this, this, this)
        showBuyerRequestCancelRespondBottomSheet()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAcceptOrderResult()
        observeRejectOrderResult()
        observeRejectCancelOrderResult()
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    @Suppress("UNCHECKED_CAST")
    override fun initInjector() {
        (activity as? HasComponent<BuyerRequestCancelRespondComponent>)
            ?.component
            ?.inject(this)
    }

    override fun onBuyerRequestCancelRespondDismissed() {
        finishActivity()
    }

    override fun getBottomSheetContainer(): CoordinatorLayout? {
        return binding?.root
    }

    override fun getBuyerRequestCancelRespondOrderId(): String {
        return activity?.intent?.getStringExtra(INTENT_PARAM_ORDER_ID) ?: Int.ZERO.toString()
    }

    override fun getBuyerRequestCancelRespondOrderInvoice(): String {
        return String.EMPTY
    }

    override fun getBuyerRequestCancelRespondOrderStatusCode(): Int {
        return activity?.intent?.getStringExtra(INTENT_PARAM_ORDER_STATUS_CODE).orEmpty().toIntOrZero()
    }

    override fun getBuyerRequestCancelRespondOrderStatusText(): String {
        return activity?.intent?.getStringExtra(INTENT_PARAM_ORDER_STATUS_TEXT).orEmpty()
    }

    override fun getBuyerRequestCancelRespondL2CancellationReason(): String {
        return activity?.intent?.getStringExtra(INTENT_PARAM_ORDER_L2_CANCELLATION_REASON).orEmpty()
    }

    override fun getBuyerRequestCancelRespondDescription(): String {
        return activity?.intent?.getStringExtra(INTENT_PARAM_DESCRIPTION).orEmpty()
    }

    override fun getBuyerRequestCancelRespondPrimaryTextButton(): String {
        return activity?.intent?.getStringExtra(INTENT_PARAM_PRIMARY_BUTTON_TEXT).orEmpty()
    }

    override fun getBuyerRequestCancelRespondSecondaryTextButton(): String {
        return activity?.intent?.getStringExtra(INTENT_PARAM_SECONDARY_BUTTON_TEXT).orEmpty()
    }

    override fun getBuyerRequestCancelRespondViewModel(): SomOrderBaseViewModel {
        return viewModel
    }

    private fun createView(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentBuyerRequestCancelRespondBinding.inflate(inflater, container, false)
    }

    private fun observeAcceptOrderResult() {
        viewModel.acceptOrderResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    SomAnalytics.eventClickAcceptOrderPopup(true)
                    if (it.data.acceptOrder.success == 1) {
                        onSuccessRespond(it.data.acceptOrder.listMessage.firstOrNull().orEmpty())
                    } else {
                        onErrorRespond(it.data.acceptOrder.listMessage.first(), ERROR_ACCEPTING_ORDER)
                    }
                }
                is Fail -> onErrorRespond(it.throwable, ERROR_ACCEPTING_ORDER)
            }
        }
    }

    private fun observeRejectOrderResult() {
        viewModel.rejectOrderResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.rejectOrder.success == 1) {
                        onSuccessRespond(it.data.rejectOrder.message.firstOrNull().orEmpty())
                    } else {
                        onErrorRespond(it.data.rejectOrder.message.firstOrNull().orEmpty(), SomConsts.ERROR_REJECT_ORDER)
                    }
                }
                is Fail -> onErrorRespond(it.throwable, SomConsts.ERROR_REJECT_ORDER)
            }
        }
    }

    private fun observeRejectCancelOrderResult() {
        viewModel.rejectCancelOrderResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.rejectCancelRequest.success == 1) {
                        onSuccessRespond(result.data.rejectCancelRequest.message)
                    } else {
                        onErrorRespond(result.data.rejectCancelRequest.message, SomConsts.ERROR_REJECT_CANCEL_ORDER)
                    }
                }
                is Fail -> onErrorRespond(result.throwable, SomConsts.ERROR_REJECT_CANCEL_ORDER)
            }
        }
    }

    private fun onSuccessRespond(message: String) {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_RESULT_SUCCESS, true)
            putExtra(DeeplinkMapperOrder.BuyerRequestCancelRespond.INTENT_RESULT_MESSAGE, message)
        })
        bottomSheetBuyerRequestCancelRespond?.dismiss()
    }

    private fun onErrorRespond(message: String, crashlyticsMessage: String) {
        onErrorRespond(MessageErrorException(message), crashlyticsMessage)
    }

    private fun onErrorRespond(throwable: Throwable, crashlyticsMessage: String) {
        SomErrorHandler.logExceptionToCrashlytics(throwable, crashlyticsMessage)
        SomErrorHandler.logExceptionToServer(
            errorTag = SomErrorHandler.SOM_TAG,
            throwable = throwable,
            errorType = SomErrorHandler.SomMessage.REJECT_CANCEL_REQUEST_ERROR,
            deviceId = userSession.deviceId.orEmpty()
        )
        throwable.showErrorToaster()
    }

    private fun finishActivity() {
        activity?.finish()
        activity?.overridePendingTransition(0, 0)
    }

    private fun Throwable.showErrorToaster() {
        when (this) {
            is MessageErrorException -> showToaster(message.orEmpty(), view, Toaster.TYPE_ERROR)
            is UnknownHostException, is SocketTimeoutException -> showNoInternetConnectionToaster()
            else -> showServerErrorToaster()
        }
    }

    private fun showNoInternetConnectionToaster() {
        showToaster(getString(R.string.som_error_message_no_internet_connection), view, Toaster.TYPE_ERROR)
    }

    private fun showServerErrorToaster() {
        showToaster(getString(R.string.som_error_message_server_fault), view, Toaster.TYPE_ERROR)
    }

    private fun showToaster(message: String, view: View?, type: Int, action: String = SomConsts.ACTION_OK) {
        val toasterError = Toaster
        view?.let { v ->
            if (action.isBlank()) {
                toasterError.build(v, message, Toaster.LENGTH_SHORT, type).show()
            } else {
                toasterError.build(v, message, Toaster.LENGTH_SHORT, type, action).show()
            }
        }
    }
}
