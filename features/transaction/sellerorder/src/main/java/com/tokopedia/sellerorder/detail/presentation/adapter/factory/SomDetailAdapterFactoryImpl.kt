package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.order_management_common.presentation.typefactory.BuyMoreGetMoreTypeFactory
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmSectionViewHolder
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailAddOnsOrderLevelViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailDividerViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailHeaderViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailIncomeViewHolder
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
import com.tokopedia.sellerorder.detail.presentation.model.SomDetailAddOnOrderLevelUiModel

class SomDetailAdapterFactoryImpl(
    private val actionListener: ActionListener,
    private val addOnListener: AddOnViewHolder.Listener,
    private val productBenefitListener: AddOnViewHolder.Listener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) : SomDetailAdapterFactory, BaseAdapterTypeFactory(), BuyMoreGetMoreTypeFactory {
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
            SomConsts.DETAIL_INCOME_TYPE -> {
                SomDetailIncomeViewHolder.LAYOUT
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

    override fun type(productBmgmSectionUiModel: ProductBmgmSectionUiModel): Int {
        return BmgmSectionViewHolder.LAYOUT
    }

    override fun type(somDetailAddOnOrderLevelUiModel: SomDetailAddOnOrderLevelUiModel): Int {
        return SomDetailAddOnsOrderLevelViewHolder.LAYOUT
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
                SomDetailNonProductBundleCardViewHolder(actionListener, addOnListener, recyclerViewSharedPool, parent)
            }
            SomDetailProductBundleCardViewHolder.RES_LAYOUT -> {
                SomDetailProductBundleCardViewHolder(actionListener, addOnListener, recyclerViewSharedPool, parent)
            }
            SomDetailDividerViewHolder.LAYOUT -> {
                SomDetailDividerViewHolder(parent)
            }
            SomDetailPofDataViewHolder.LAYOUT -> {
                SomDetailPofDataViewHolder(parent)
            }
            BmgmSectionViewHolder.LAYOUT -> {
                BmgmSectionViewHolder(
                    parent,
                    recyclerViewSharedPool,
                    addOnListener,
                    actionListener,
                    productBenefitListener
                )
            }
            SomDetailIncomeViewHolder.LAYOUT -> {
                SomDetailIncomeViewHolder(actionListener, parent)
            }
            SomDetailAddOnsOrderLevelViewHolder.LAYOUT -> {
                SomDetailAddOnsOrderLevelViewHolder(addOnListener, recyclerViewSharedPool, parent)
            }

            else -> super.createViewHolder(parent, type)
        }
    }

    interface ActionListener : BmgmSectionViewHolder.Listener {
        fun onTextCopied(label: String, str: String, readableDataName: String)
        fun onInvalidResiUpload(awbUploadUrl: String)
        fun onDialPhone(strPhoneNo: String)
        fun onShowInfoLogisticAll(logisticInfoList: List<SomDetailOrder.GetSomDetail.LogisticInfo.All>)
        fun onShowBookingCode(bookingCode: String, bookingType: String)
        fun onShowBuyerRequestCancelRespondBottomSheet(it: SomDetailOrder.GetSomDetail.Button)
        fun onSeeInvoice(invoiceUrl: String, invoice: String)
        fun onCopiedInvoice(invoice: String, str: String)
        fun onClickProduct(orderDetailId: Long)
        fun onCopiedAddress(address: String, str: String)
        fun onResoClicked(redirectPath: String)
        fun onDropOffButtonClicked(url: String)
        fun onDetailIncomeClicked()
    }
}
