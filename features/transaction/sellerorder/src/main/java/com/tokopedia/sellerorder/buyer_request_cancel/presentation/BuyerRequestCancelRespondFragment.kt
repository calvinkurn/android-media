package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.buyer_request_cancel.di.BuyerRequestCancelRespondComponent
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.databinding.FragmentBuyerRequestCancelRespondBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class BuyerRequestCancelRespondFragment : BaseDaggerFragment(),
    IBuyerRequestCancelRespondListener.Mediator,
    IBuyerRequestCancelRespondListener by BuyerRequestCancelRespondListenerImpl() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BuyerRequestCancelRespondViewModel::class.java]
    }

    private var bottomSheet: BuyerRequestCancelRespondBottomSheet? = null

    private var binding by autoClearedNullable<FragmentBuyerRequestCancelRespondBinding> {
        bottomSheet?.clearViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createView(inflater, container)
        setupBottomSheet()
        return binding?.root
    }

    override fun getScreenName(): String {
        return "buyer-request-cancel-respond"
    }

    @Suppress("UNCHECKED_CAST")
    override fun initInjector() {
        (activity as? HasComponent<BuyerRequestCancelRespondComponent>)
            ?.component
            ?.inject(this)
    }

    override fun getBuyerRequestCancelRespondOrderId(): String {
        return activity?.intent?.getStringExtra("ORDER_ID") ?: "0"
    }

    override fun getBuyerRequestCancelRespondOrderInvoice(): String {
        return String.EMPTY
    }

    override fun getBuyerRequestCancelRespondOrderStatusCodeString(): String {
        return getOrderStatusCode().toString()
    }

    override fun getBuyerRequestCancelRespondOrderStatusText(): String {
        return activity?.intent?.getStringExtra("ORDER_STATUS_TEXT").orEmpty()
    }

    override fun getBuyerRequestCancelRespondViewModel(): SomOrderBaseViewModel {
        return viewModel
    }

    private fun getOrderStatusCode(): Int {
        return activity?.intent?.getIntExtra("ORDER_STATUS_CODE", 0).orZero()
    }

    private fun getCancellationReason(): String {
        return activity?.intent?.getStringExtra("CANCELLATION_REASON").orEmpty()
    }

    private fun getDescription(): String {
        return activity?.intent?.getStringExtra("DESCRIPTION").orEmpty()
    }

    private fun getPrimaryButtonText(): String {
        return activity?.intent?.getStringExtra("PRIMARY_BUTTON_TEXT").orEmpty()
    }

    private fun getSecondaryButtonText(): String {
        return activity?.intent?.getStringExtra("SECONDARY_BUTTON_TEXT").orEmpty()
    }

    private fun createView(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentBuyerRequestCancelRespondBinding.inflate(inflater, container, false)
    }

    private fun setupBottomSheet() {
        binding?.run {
            bottomSheet = bottomSheet?.apply {
                setupBuyerRequestCancelBottomSheet(
                    buyerRequestCancelRespondBottomSheet = this,
                    orderStatusCode = getOrderStatusCode(),
                    reason = getCancellationReason(),
                    description = getDescription(),
                    primaryButtonText = getPrimaryButtonText(),
                    secondaryButtonText = getSecondaryButtonText()
                )
            } ?: BuyerRequestCancelRespondBottomSheet(root.context).apply {
                setupBuyerRequestCancelBottomSheet(
                    buyerRequestCancelRespondBottomSheet = this,
                    orderStatusCode = getOrderStatusCode(),
                    reason = getCancellationReason(),
                    description = getDescription(),
                    primaryButtonText = getPrimaryButtonText(),
                    secondaryButtonText = getSecondaryButtonText()
                )
            }
            bottomSheet?.setOnDismiss { finishActivity() }
            bottomSheet?.init(root)
            bottomSheet?.show()
        }
    }

    private fun setupBuyerRequestCancelBottomSheet(
        buyerRequestCancelRespondBottomSheet: BuyerRequestCancelRespondBottomSheet,
        orderStatusCode: Int = 0,
        reason: String = "",
        description: String,
        primaryButtonText: String,
        secondaryButtonText: String
    ) {
        buyerRequestCancelRespondBottomSheet.apply {
            registerBuyerRequestCancelRespondListenerMediator(this@BuyerRequestCancelRespondFragment)
            setListener(this@BuyerRequestCancelRespondFragment)
            init(reason, orderStatusCode, description, primaryButtonText, secondaryButtonText)
            hideKnob()
            showCloseButton()
        }
    }

    private fun finishActivity() {
        activity?.finish()
        activity?.overridePendingTransition(0, 0)
    }
}
