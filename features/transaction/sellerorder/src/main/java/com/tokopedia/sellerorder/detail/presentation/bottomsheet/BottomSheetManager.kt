package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerorder.buyer_request_cancel.presentation.BuyerRequestCancelRespondBottomSheetManagerImpl
import com.tokopedia.sellerorder.buyer_request_cancel.presentation.IBuyerRequestCancelRespondBottomSheetManager
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderEditAwbBottomSheet
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.sellerorder.orderextension.presentation.delegate.ISomBottomSheetOrderExtensionRequestManager
import com.tokopedia.sellerorder.orderextension.presentation.delegate.SomBottomSheetOrderExtensionRequestManagerImpl

class BottomSheetManager(
    private val view: ViewGroup,
    private val fragmentManager: FragmentManager
): IBuyerRequestCancelRespondBottomSheetManager by BuyerRequestCancelRespondBottomSheetManagerImpl(),
    ISomBottomSheetOrderExtensionRequestManager by SomBottomSheetOrderExtensionRequestManagerImpl() {
    private var secondaryBottomSheet: SomDetailSecondaryActionBottomSheet? = null
    private var somRejectReasonBottomSheet: SomRejectReasonBottomSheet? = null
    private var somProductEmptyBottomSheet: SomBottomSheetProductEmpty? = null
    private var somShopClosedBottomSheet: SomBottomSheetShopClosed? = null
    private var bottomSheetCourierProblems: SomBottomSheetCourierProblem? = null
    private var bottomSheetBuyerNoResponse: SomBottomSheetBuyerNoResponse? = null
    private var bottomSheetBuyerOtherReason: SomBottomSheetBuyerOtherReason? = null
    private var bottomSheetChangeAwb: SomOrderEditAwbBottomSheet? = null
    private var bottomSheetSetDelivered: SomBottomSheetSetDelivered? = null

    private fun createSomRejectReasonBottomSheet(
        actionListener: SomBottomSheetRejectReasonsAdapter.ActionListener
    ): SomRejectReasonBottomSheet {
        return SomRejectReasonBottomSheet(view.context, actionListener)
    }

    private fun reInitSomRejectReasonBottomSheet(
        data: SomReasonRejectData.Data,
        orderDetail: SomDetailOrder.GetSomDetail?
    ) {
        somRejectReasonBottomSheet?.run {
            init(view)
            setupTicker(
                orderDetail?.penaltyRejectInfo?.isPenaltyReject.orFalse(),
                orderDetail?.penaltyRejectInfo?.penaltyRejectWording.orEmpty()
            )
            setReasons(data.listSomRejectReason.toMutableList())
        }
    }

    private fun createSecondaryActionBottomSheet(
        listener: SomBottomSheetRejectOrderAdapter.ActionListener
    ): SomDetailSecondaryActionBottomSheet {
        return SomDetailSecondaryActionBottomSheet(view.context, listener)
    }

    private fun reInitSecondaryActionBottomSheet(actions: java.util.HashMap<String, String>) {
        secondaryBottomSheet?.run {
            init(view)
            setActions(actions)
        }
    }

    private fun createSomOrderEditAwbBottomSheet(): SomOrderEditAwbBottomSheet {
        return SomOrderEditAwbBottomSheet(view.context)
    }

    private fun reInitSomOrderEditAwbBottomSheet(
        listener: SomOrderEditAwbBottomSheet.SomOrderEditAwbBottomSheetListener
    ) {
        bottomSheetChangeAwb?.run {
            setListener(listener)
            init(view)
        }
    }

    private fun createSomBottomSheetProductEmpty(): SomBottomSheetProductEmpty {
        return SomBottomSheetProductEmpty(view.context)
    }

    private fun reInitSomBottomSheetProductEmpty(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderDetail: SomDetailOrder.GetSomDetail?,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ) {
        somProductEmptyBottomSheet?.run {
            setProducts(orderDetail?.getProductList().orEmpty())
            setOrderId(orderId)
            setListener(listener)
            setRejectReason(rejectReason)
            init(view)
        }
    }

    private fun createSomBottomSheetShopClosed(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener,
        fragmentManager: FragmentManager
    ): SomBottomSheetShopClosed {
        return SomBottomSheetShopClosed(
            view.context,
            fragmentManager,
            rejectReason,
            orderId,
            listener
        )
    }

    private fun reInitSomBottomSheetShopClosed(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String
    ) {
        somShopClosedBottomSheet?.run {
            setOrderId(orderId)
            setRejectReason(rejectReason)
            init(view)
        }
    }

    private fun createSomBottomSheetCourierProblem(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ): SomBottomSheetCourierProblem {
        return SomBottomSheetCourierProblem(view.context, rejectReason, orderId, listener)
    }

    private fun reInitSomBottomSheetCourierProblem(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String
    ) {
        bottomSheetCourierProblems?.run {
            setOrderId(orderId)
            setRejectReason(rejectReason)
            init(view)
        }
    }

    private fun createSomBottomSheetBuyerNoResponse(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ): SomBottomSheetBuyerNoResponse {
        return SomBottomSheetBuyerNoResponse(view.context, rejectReason, orderId, listener)
    }

    private fun reInitSomBottomSheetBuyerNoResponse(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String
    ) {
        bottomSheetBuyerNoResponse?.run {
            setOrderId(orderId)
            setRejectReason(rejectReason)
            init(view)
        }
    }

    private fun createSomBottomSheetBuyerOtherReason(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ): SomBottomSheetBuyerOtherReason {
        return SomBottomSheetBuyerOtherReason(view.context, rejectReason, orderId, listener)
    }

    private fun reInitSomBottomSheetBuyerOtherReason(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String
    ) {
        bottomSheetBuyerOtherReason?.run {
            setOrderId(orderId)
            setRejectReason(rejectReason)
            init(view)
        }
    }

    private fun createSomBottomSheetSetDelivered(
        listener: SomBottomSheetSetDelivered.SomBottomSheetSetDeliveredListener
    ): SomBottomSheetSetDelivered {
        return SomBottomSheetSetDelivered(view.context, listener)
    }

    fun showSomRejectReasonBottomSheet(
        data: SomReasonRejectData.Data,
        orderDetail: SomDetailOrder.GetSomDetail?,
        actionListener: SomBottomSheetRejectReasonsAdapter.ActionListener
    ) {
        somRejectReasonBottomSheet = somRejectReasonBottomSheet
            ?: createSomRejectReasonBottomSheet(actionListener)
        reInitSomRejectReasonBottomSheet(data, orderDetail)
        somRejectReasonBottomSheet?.show()
    }

    fun showSomDetailSecondaryActionBottomSheet(
        actions: HashMap<String, String>,
        listener: SomBottomSheetRejectOrderAdapter.ActionListener
    ) {
        secondaryBottomSheet = secondaryBottomSheet ?: createSecondaryActionBottomSheet(listener)
        reInitSecondaryActionBottomSheet(actions)
        secondaryBottomSheet?.show()
    }

    fun showSomOrderEditAwbBottomSheet(
        listener: SomOrderEditAwbBottomSheet.SomOrderEditAwbBottomSheetListener
    ) {
        bottomSheetChangeAwb = bottomSheetChangeAwb ?: createSomOrderEditAwbBottomSheet()
        reInitSomOrderEditAwbBottomSheet(listener)
        bottomSheetChangeAwb?.show()
    }

    fun showSomBottomSheetProductEmpty(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderDetail: SomDetailOrder.GetSomDetail?,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ) {
        somProductEmptyBottomSheet = somProductEmptyBottomSheet
            ?: createSomBottomSheetProductEmpty()
        reInitSomBottomSheetProductEmpty(rejectReason, orderDetail, orderId, listener)
        somProductEmptyBottomSheet?.show()
    }

    fun showSomBottomSheetShopClosed(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener,
        fragmentManager: FragmentManager
    ) {
        somShopClosedBottomSheet = somShopClosedBottomSheet
            ?: createSomBottomSheetShopClosed(rejectReason, orderId, listener, fragmentManager)
        reInitSomBottomSheetShopClosed(rejectReason, orderId)
        somShopClosedBottomSheet?.show()
    }

    fun showSomBottomSheetCourierProblem(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ) {
        bottomSheetCourierProblems = bottomSheetCourierProblems
            ?: createSomBottomSheetCourierProblem(rejectReason, orderId, listener)
        reInitSomBottomSheetCourierProblem(rejectReason, orderId)
        bottomSheetCourierProblems?.show()
    }

    fun showSomBottomSheetBuyerNoResponse(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ) {
        bottomSheetBuyerNoResponse = bottomSheetBuyerNoResponse
            ?: createSomBottomSheetBuyerNoResponse(rejectReason, orderId, listener)
        reInitSomBottomSheetBuyerNoResponse(rejectReason, orderId)
        bottomSheetBuyerNoResponse?.show()
    }

    fun showSomBottomSheetBuyerOtherReason(
        rejectReason: SomReasonRejectData.Data.SomRejectReason,
        orderId: String,
        listener: SomBaseRejectOrderBottomSheet.SomRejectOrderBottomSheetListener
    ) {
        bottomSheetBuyerOtherReason = bottomSheetBuyerOtherReason
            ?: createSomBottomSheetBuyerOtherReason(rejectReason, orderId, listener)
        reInitSomBottomSheetBuyerOtherReason(rejectReason, orderId)
        bottomSheetBuyerOtherReason?.show()
    }

    fun showSomBottomSheetSetDelivered(
        listener: SomBottomSheetSetDelivered.SomBottomSheetSetDeliveredListener
    ) {
        bottomSheetSetDelivered = bottomSheetSetDelivered ?: createSomBottomSheetSetDelivered(listener)
        bottomSheetSetDelivered?.init(view)
        bottomSheetSetDelivered?.show()
    }

    fun dismissSecondaryActionBottomSheet() {
        secondaryBottomSheet?.dismiss()
    }

    fun getSecondaryActionBottomSheet(): SomDetailSecondaryActionBottomSheet? {
        return secondaryBottomSheet
    }

    fun getSomRejectReasonBottomSheet(): SomRejectReasonBottomSheet? {
        return somRejectReasonBottomSheet
    }

    fun getSomBottomSheetSetDelivered(): SomBottomSheetSetDelivered? {
        return bottomSheetSetDelivered
    }

    fun clearViewBindings() {
        secondaryBottomSheet?.clearViewBinding()
        bottomSheetBuyerRequestCancelRespond?.clearViewBinding()
        somRejectReasonBottomSheet?.clearViewBinding()
        somProductEmptyBottomSheet?.clearViewBinding()
        somShopClosedBottomSheet?.clearViewBinding()
        bottomSheetCourierProblems?.clearViewBinding()
        bottomSheetBuyerNoResponse?.clearViewBinding()
        bottomSheetBuyerOtherReason?.clearViewBinding()
        bottomSheetChangeAwb?.clearViewBinding()
        somBottomSheetOrderExtensionRequest?.clearViewBinding()
        bottomSheetSetDelivered?.clearViewBinding()
    }

    fun dismissBottomSheets(): Boolean {
        var bottomSheetDismissed = false
        bottomSheetDismissed = secondaryBottomSheet?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = bottomSheetBuyerRequestCancelRespond?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = somRejectReasonBottomSheet?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = somProductEmptyBottomSheet?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = somShopClosedBottomSheet?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = bottomSheetCourierProblems?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = bottomSheetBuyerNoResponse?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = bottomSheetBuyerOtherReason?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = bottomSheetSetDelivered?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = bottomSheetChangeAwb?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = somBottomSheetOrderExtensionRequest?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = bottomSheetSetDelivered?.dismiss() == true || bottomSheetDismissed
        return bottomSheetDismissed
    }
}
