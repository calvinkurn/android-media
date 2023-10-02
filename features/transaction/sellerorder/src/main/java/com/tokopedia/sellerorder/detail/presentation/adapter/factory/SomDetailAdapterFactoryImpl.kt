package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailDividerViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailHeaderViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailMVCUsageViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailNonProductBundleCardViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailPaymentsViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailPofDataViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailProductBundleCardViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailProductsViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailResolutionViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailShippingViewHolder
import com.tokopedia.sellerorder.detail.presentation.model.DividerUiModel
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel

class SomDetailAdapterFactoryImpl(
    private val actionListener: ActionListener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) : SomDetailAdapterFactory, BaseAdapterTypeFactory() {
    override fun type(typeLayout: String): Int {
        return when (typeLayout) {
            SomConsts.DETAIL_HEADER_TYPE -> {
                SomDetailHeaderViewHolder.LAYOUT
            }
            SomConsts.DETAIL_RESO_TYPE -> {
                SomDetailResolutionViewHolder.LAYOUT
            }
            SomConsts.DETAIL_PRODUCTS_TYPE -> {
                SomDetailProductsViewHolder.LAYOUT
            }
            SomConsts.DETAIL_SHIPPING_TYPE -> {
                SomDetailShippingViewHolder.LAYOUT
            }
            SomConsts.DETAIL_PAYMENT_TYPE -> {
                SomDetailPaymentsViewHolder.LAYOUT
            }
            SomConsts.DETAIL_MVC_USAGE_TYPE -> {
                SomDetailMVCUsageViewHolder.LAYOUT
            }
            SomConsts.DETAIL_POF_DATA_TYPE -> {
                SomDetailPofDataViewHolder.LAYOUT
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun type(model: NonProductBundleUiModel): Int {
        return SomDetailNonProductBundleCardViewHolder.RES_LAYOUT
    }

    override fun type(model: ProductBundleUiModel): Int {
        return SomDetailProductBundleCardViewHolder.RES_LAYOUT
    }

    override fun type(dividerUiModel: DividerUiModel): Int {
        return SomDetailDividerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SomDetailHeaderViewHolder.LAYOUT -> {
                SomDetailHeaderViewHolder(parent, actionListener)
            }

            SomDetailResolutionViewHolder.LAYOUT -> {
                SomDetailResolutionViewHolder(parent, actionListener)
            }

            SomDetailProductsViewHolder.LAYOUT -> {
                SomDetailProductsViewHolder(parent)
            }
            SomDetailShippingViewHolder.LAYOUT -> {
                SomDetailShippingViewHolder(parent, actionListener)
            }
            SomDetailPaymentsViewHolder.LAYOUT -> {
                SomDetailPaymentsViewHolder(parent)
            }
            SomDetailMVCUsageViewHolder.LAYOUT -> {
                SomDetailMVCUsageViewHolder(parent)
            }
            SomDetailNonProductBundleCardViewHolder.RES_LAYOUT -> {
                SomDetailNonProductBundleCardViewHolder(actionListener, recyclerViewSharedPool, parent)
            }
            SomDetailProductBundleCardViewHolder.RES_LAYOUT -> {
                SomDetailProductBundleCardViewHolder(actionListener, recyclerViewSharedPool, parent)
            }
            SomDetailDividerViewHolder.LAYOUT -> {
                SomDetailDividerViewHolder(parent)
            }
            SomDetailPofDataViewHolder.LAYOUT -> {
                SomDetailPofDataViewHolder(parent)
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    interface ActionListener {
        fun onTextCopied(label: String, str: String, readableDataName: String)
        fun onInvalidResiUpload(awbUploadUrl: String)
        fun onDialPhone(strPhoneNo: String)
        fun onShowInfoLogisticAll(logisticInfoList: List<SomDetailOrder.Data.GetSomDetail.LogisticInfo.All>)
        fun onShowBookingCode(bookingCode: String, bookingType: String)
        fun onShowBuyerRequestCancelReasonBottomSheet(it: SomDetailOrder.Data.GetSomDetail.Button)
        fun onSeeInvoice(invoiceUrl: String, invoice: String)
        fun onCopiedInvoice(invoice: String, str: String)
        fun onClickProduct(orderDetailId: Long)
        fun onCopiedAddress(address: String, str: String)
        fun onCopyAddOnDescription(label: String, description: CharSequence)
        fun onResoClicked(redirectPath: String)
        fun onDropOffButtonClicked(url: String)
    }
}
