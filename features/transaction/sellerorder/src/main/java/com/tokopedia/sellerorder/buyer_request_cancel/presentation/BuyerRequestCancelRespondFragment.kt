package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderRequestCancelBottomSheet
import com.tokopedia.sellerorder.databinding.FragmentBuyerRequestCancelRespondBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class BuyerRequestCancelRespondFragment : BaseDaggerFragment() {

    private var bottomSheet: SomOrderRequestCancelBottomSheet? = null

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

    override fun initInjector() {
    }

    private fun getOrderId(): Int {
        return activity?.intent?.getIntExtra("ORDER_ID", 0).orZero()
    }

    private fun getOrderStatusId(): Int {
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
                    somOrderRequestCancelBottomSheet = this,
                    orderStatusId = getOrderStatusId(),
                    reason = getCancellationReason(),
                    description = getDescription(),
                    primaryButtonText = getPrimaryButtonText(),
                    secondaryButtonText = getSecondaryButtonText()
                )
            } ?: SomOrderRequestCancelBottomSheet(root.context).apply {
                setupBuyerRequestCancelBottomSheet(
                    somOrderRequestCancelBottomSheet = this,
                    orderStatusId = getOrderStatusId(),
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
        somOrderRequestCancelBottomSheet: SomOrderRequestCancelBottomSheet,
        orderStatusId: Int = 0,
        reason: String = "",
        description: String,
        primaryButtonText: String,
        secondaryButtonText: String
    ) {
        somOrderRequestCancelBottomSheet.apply {
            setListener(object : SomOrderRequestCancelBottomSheet.SomOrderRequestCancelBottomSheetListener {
                override fun onAcceptOrder(actionName: String) {
                    onAcceptOrderButtonClicked()
                }

                override fun onRejectOrder(reasonBuyer: String) {

                }

                override fun onRejectCancelRequest() {

                }
            })
            init(reason, orderStatusId, description, primaryButtonText, secondaryButtonText)
            hideKnob()
            showCloseButton()
        }
    }

    private fun onAcceptOrderButtonClicked() {
        Log.d("SOMLog", "onAcceptOrderButtonClicked")
    }

    private fun finishActivity() {
        activity?.finish()
        activity?.overridePendingTransition(0, 0)
    }
}
