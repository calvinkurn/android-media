package com.tokopedia.sellerorder.orderextension.presentation.fragment

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
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_ID
import com.tokopedia.applink.order.DeeplinkMapperOrder.Soe.INTENT_PARAM_ORDER_ID
import com.tokopedia.applink.order.DeeplinkMapperOrder.Soe.Seller.INTENT_RESULT_MESSAGE
import com.tokopedia.applink.order.DeeplinkMapperOrder.Soe.Seller.INTENT_RESULT_SUCCESS
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.FragmentSellerOrderExtensionRequestBinding
import com.tokopedia.sellerorder.orderextension.di.SomOrderExtensionRequestComponent
import com.tokopedia.sellerorder.orderextension.presentation.delegate.ISomBottomSheetOrderExtensionRequestManager
import com.tokopedia.sellerorder.orderextension.presentation.delegate.SomBottomSheetOrderExtensionRequestManagerImpl
import com.tokopedia.sellerorder.orderextension.presentation.viewmodel.SomOrderExtensionViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SomOrderExtensionRequestFragment : BaseDaggerFragment(),
    ISomBottomSheetOrderExtensionRequestManager.Mediator,
    ISomBottomSheetOrderExtensionRequestManager.Listener,
    ISomBottomSheetOrderExtensionRequestManager by SomBottomSheetOrderExtensionRequestManagerImpl() {

    companion object {
        private const val SCREEN_NAME = "som-order-extension-request"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazyThreadSafetyNone {
        ViewModelProvider(this, viewModelFactory)[SomOrderExtensionViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentSellerOrderExtensionRequestBinding> {
        somBottomSheetOrderExtensionRequest?.clearViewBinding()
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    @Suppress("UNCHECKED_CAST")
    override fun initInjector() {
        (activity as? HasComponent<SomOrderExtensionRequestComponent>)
            ?.component
            ?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createView(inflater, container)
        registerSomBottomSheetOrderExtensionRequest(this, this)
        showSomBottomSheetOrderExtensionRequest()
        return binding?.root
    }

    override fun getBottomSheetContainer(): CoordinatorLayout? {
        return binding?.root
    }

    override fun getSomOrderExtensionOrderId(): String {
        return activity?.intent?.getStringExtra(INTENT_PARAM_ORDER_ID)
            ?: activity?.intent?.getStringExtra(PARAM_ORDER_ID)
            ?: Int.ZERO.toString()
    }

    override fun getSomOrderExtensionViewModel(): SomOrderExtensionViewModel {
        return viewModel
    }

    override fun onSuccessRequestOrderExtension(message: String) {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(INTENT_RESULT_SUCCESS, true)
            putExtra(INTENT_RESULT_MESSAGE, message)
        })
        somBottomSheetOrderExtensionRequest?.dismiss()
    }

    override fun onFailedRequestOrderExtension(message: String, throwable: Throwable?) {
        if (throwable == null && message.isNotBlank()) {
            showToaster(message, view, Toaster.TYPE_ERROR)
        } else throwable?.showErrorToaster()
    }

    override fun onSellerOrderExtensionRequestDismissed() {
        finishActivity()
    }

    private fun createView(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSellerOrderExtensionRequestBinding.inflate(inflater, container, false)
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
