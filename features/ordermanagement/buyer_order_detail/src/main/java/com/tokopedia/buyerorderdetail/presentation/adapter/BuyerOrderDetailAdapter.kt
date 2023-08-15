package com.tokopedia.buyerorderdetail.presentation.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.BuyerOrderDetailDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BaseVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.DigitalRecommendationUiModel
import com.tokopedia.buyerorderdetail.presentation.model.EpharmacyInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlatformFeeInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.SimpleCopyableKeyValueUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ThickDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ThinDashedDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ThinDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderInsuranceUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ScpRewardsMedalTouchPointWidgetUiState
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel.ScpRewardsMedalTouchPointWidgetUiModel

@Suppress("UNCHECKED_CAST")
open class BuyerOrderDetailAdapter(private val typeFactory: BuyerOrderDetailTypeFactory) :
    BaseAdapter<BuyerOrderDetailTypeFactory>(typeFactory) {

    private fun setupNewItems(
        context: Context?,
        uiState: BuyerOrderDetailUiState.HasData
    ): List<Visitable<BuyerOrderDetailTypeFactory>> {
        return mutableListOf<Visitable<BuyerOrderDetailTypeFactory>>().apply {
            setupOrderStatusSection(context, uiState.orderStatusUiState.data)
            setupOrderResolutionSection(context, uiState.orderResolutionUiState)
            setupProductListSection(context, uiState.productListUiState.data)
            setupOrderInsuranceSection(context, uiState.orderInsuranceUiState)
            setupEpharmacyInfoSection(context, uiState.epharmacyInfoUiState.data)
            setupScpRewardsMedalTouchPointSection(uiState.scpRewardsMedalTouchPointWidgetUiState)
            setupShipmentInfoSection(context, uiState.shipmentInfoUiState.data)
            setupPaymentInfoSection(context, uiState.paymentInfoUiState.data)
            setUpPhysicalRecommendationSection(uiState.pgRecommendationWidgetUiState.data)
            setupDigitalRecommendationSection()
        }
    }

    protected open fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setUpPhysicalRecommendationSection(
        pgRecommendationWidgetUiModel: PGRecommendationWidgetUiModel
    ) {
        add(ThickDividerUiModel())
        add(pgRecommendationWidgetUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupOrderStatusSection(
        context: Context?,
        orderStatusUiModel: OrderStatusUiModel
    ) {
        val shouldAddThinDivider = addOrderStatusHeaderSection(
            context,
            orderStatusUiModel.orderStatusHeaderUiModel
        ).or(
            addTickerSection(context, orderStatusUiModel.ticker)
        ).and(orderStatusUiModel.orderStatusInfoUiModel.shouldShow(context))
        if (shouldAddThinDivider) {
            addThinDividerSection()
        }
        addOrderStatusInfoSection(context, orderStatusUiModel.orderStatusInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupOrderResolutionSection(
        context: Context?,
        orderResolutionUiState: OrderResolutionTicketStatusUiState
    ) {
        if (orderResolutionUiState is OrderResolutionTicketStatusUiState.HasData) {
            if (orderResolutionUiState.data.shouldShow(context)) {
                addThickDividerSection()
                add(orderResolutionUiState.data)
            }
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupProductListSection(
        context: Context?,
        productListUiModel: ProductListUiModel
    ) {
        addThickDividerSection()
        addProductListHeaderSection(context, productListUiModel.productListHeaderUiModel)
        addTickerDetailsSection(context, productListUiModel.tickerInfo)
        addPofHeaderSection(context, productListUiModel.productFulfilledHeaderLabel)
        addProductBmgmListSection(productListUiModel.productBmgmList)
        addProductBundlingListSection(productListUiModel.productBundlingList)
        addProductListSection(context, productListUiModel.productList)
        addAddonsListSection(productListUiModel.addonsListUiModel)
        addPofHeaderSection(context, productListUiModel.productUnfulfilledHeaderLabel)
        addProductListSection(context, productListUiModel.productUnFulfilledList)
        addProductListToggleSection(productListUiModel.productListToggleUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupOrderInsuranceSection(
        context: Context?,
        orderInsuranceUiState: OrderInsuranceUiState
    ) {
        if (orderInsuranceUiState is OrderInsuranceUiState.HasData) {
            if (orderInsuranceUiState.data.shouldShow(context)) {
                addThickDividerSection()
                addOrderInsuranceSection(orderInsuranceUiState.data)
            }
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupScpRewardsMedalTouchPointSection(
        scpRewardsMedalTouchPointWidgetUiState: ScpRewardsMedalTouchPointWidgetUiState
    ) {
        if (scpRewardsMedalTouchPointWidgetUiState is ScpRewardsMedalTouchPointWidgetUiState.HasData.Showing) {
            addThickDividerSection()
            addScpRewardsMedalTouchPointSection(
                uiModel = scpRewardsMedalTouchPointWidgetUiState.uiModel
            )
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupEpharmacyInfoSection(
        context: Context?,
        epharmacyInfoUiModel: EpharmacyInfoUiModel
    ) {
        if (epharmacyInfoUiModel.shouldShow(context)) {
            addThinDividerSection()
            add(epharmacyInfoUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupShipmentInfoSection(
        context: Context?,
        shipmentInfoUiModel: ShipmentInfoUiModel
    ) {
        addThickDividerSection()
        addPlainHeaderSection(context, shipmentInfoUiModel.headerUiModel)
        addTickerSection(context, shipmentInfoUiModel.ticker)
        addCourierInfoSection(context, shipmentInfoUiModel.courierInfoUiModel)
        addCourierDriverInfoSection(context, shipmentInfoUiModel.courierDriverInfoUiModel)
        addDriverTippingInfoSection(context, shipmentInfoUiModel.driverTippingInfoUiModel)
        addAwbInfoSection(context, shipmentInfoUiModel.awbInfoUiModel)
        addReceiverAddressInfoSection(context, shipmentInfoUiModel.receiverAddressInfoUiModel)
        addDropShipperInfoSection(context, shipmentInfoUiModel.dropShipperInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupPaymentInfoSection(
        context: Context?,
        paymentInfoUiModel: PaymentInfoUiModel
    ) {
        addThickDividerSection()
        addPlainHeaderSection(context, paymentInfoUiModel.headerUiModel)
        addPaymentMethodSection(context, paymentInfoUiModel.paymentMethodInfoItem)
        addThinDividerSection()
        addPaymentInfoSection(context, paymentInfoUiModel.paymentInfoItems)
        addThinDividerSection()
        addPaymentGrandTotalSection(context, paymentInfoUiModel.paymentGrandTotal)
        addPaymentRefundSection(context, paymentInfoUiModel.pofRefundInfoUiModel)
        addPlatformFeeInfoSection()
        addTickerSection(context, paymentInfoUiModel.ticker)
    }

    protected open fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupDigitalRecommendationSection() {
        add(ThickDividerUiModel())
        add(DigitalRecommendationUiModel())
    }

    protected open fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPlainHeaderSection(
        context: Context?,
        headerUiModel: PlainHeaderUiModel
    ) {
        if (headerUiModel.shouldShow(context)) add(headerUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addTickerSection(
        context: Context?,
        tickerUiModel: TickerUiModel
    ): Boolean {
        return if (tickerUiModel.shouldShow(context)) {
            add(tickerUiModel)
            true
        } else {
            false
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addThinDashedDividerSection() {
        add(ThinDashedDividerUiModel())
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addThickDividerSection() {
        add(ThickDividerUiModel())
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addThinDividerSection() {
        add(ThinDividerUiModel())
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addScpRewardsMedalTouchPointSection(
        uiModel: ScpRewardsMedalTouchPointWidgetUiModel
    ) {
        (uiModel as? Visitable<BuyerOrderDetailTypeFactory>)?.let { add(it) }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addOrderStatusHeaderSection(
        context: Context?,
        orderStatusHeaderUiModel: OrderStatusUiModel.OrderStatusHeaderUiModel
    ): Boolean {
        return if (orderStatusHeaderUiModel.shouldShow(context)) {
            add(orderStatusHeaderUiModel)
            true
        } else {
            false
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addOrderStatusInfoSection(
        context: Context?,
        orderStatusInfoUiModel: OrderStatusUiModel.OrderStatusInfoUiModel
    ) {
        if (orderStatusInfoUiModel.shouldShow(context)) add(orderStatusInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductListHeaderSection(
        context: Context?,
        productListHeaderUiModel: ProductListUiModel.ProductListHeaderUiModel
    ) {
        if (productListHeaderUiModel.shouldShow(context)) add(productListHeaderUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addTickerDetailsSection(
        context: Context?,
        tickerUiModel: TickerUiModel?
    ) {
        if (tickerUiModel != null && tickerUiModel.shouldShow(context)) {
            tickerUiModel.marginBottom = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.unify_space_8
            )
            tickerUiModel.marginTop = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.unify_space_8
            )

            add(tickerUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductListSection(
        context: Context?,
        productList: List<ProductListUiModel.ProductUiModel>?
    ) {
        productList?.filter { it.shouldShow(context) }?.also { addAll(it) }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPofHeaderSection(
        context: Context?,
        pofHeaderLabelUiModel: ProductListUiModel.ProductPofHeaderLabelUiModel?
    ) {
        if (pofHeaderLabelUiModel?.shouldShow(context) == true) {
            add(pofHeaderLabelUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPofUnfulfilledHeaderSection(
        context: Context?,
        pofHeaderLabelUiModel: ProductListUiModel.ProductPofHeaderLabelUiModel
    ) {
        if (pofHeaderLabelUiModel.shouldShow(context)) {
            add(pofHeaderLabelUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addCourierInfoSection(
        context: Context?,
        courierInfoUiModel: ShipmentInfoUiModel.CourierInfoUiModel
    ) {
        if (courierInfoUiModel.shouldShow(context)) add(courierInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductBmgmListSection(
        productBmgmList: List<ProductBmgmSectionUiModel>
    ) {
        (productBmgmList as? List<Visitable<BuyerOrderDetailTypeFactory>>)?.let { addAll(it) }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductBundlingListSection(
        productBundlingList: List<ProductListUiModel.ProductBundlingUiModel>
    ) {
        addAll(productBundlingList)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addAddonsListSection(
        addonsListUiModel: AddonsListUiModel?
    ) {
        if (addonsListUiModel != null && addonsListUiModel.addonsItemList.isNotEmpty()) {
            add(addonsListUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductListToggleSection(
        productListToggleUiModel: ProductListUiModel.ProductListToggleUiModel?
    ) {
        if (productListToggleUiModel != null) {
            add(productListToggleUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addOrderInsuranceSection(
        data: OrderInsuranceUiModel
    ) {
        add(data)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addCourierDriverInfoSection(
        context: Context?,
        courierDriverInfoUiModel: ShipmentInfoUiModel.CourierDriverInfoUiModel
    ) {
        if (courierDriverInfoUiModel.shouldShow(context)) {
            addThinDashedDividerSection()
            add(courierDriverInfoUiModel)
            addThinDashedDividerSection()
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addDriverTippingInfoSection(
        context: Context?,
        driverTippingInfoUiModel: ShipmentInfoUiModel.DriverTippingInfoUiModel
    ) {
        if (driverTippingInfoUiModel.shouldShow(context)) {
            add(driverTippingInfoUiModel)
            addThinDividerSection()
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addAwbInfoSection(
        context: Context?,
        awbInfoUiModel: ShipmentInfoUiModel.AwbInfoUiModel
    ) {
        if (awbInfoUiModel.shouldShow(context)) {
            add(awbInfoUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addReceiverAddressInfoSection(
        context: Context?,
        receiverAddressInfoUiModel: SimpleCopyableKeyValueUiModel
    ) {
        if (receiverAddressInfoUiModel.shouldShow(context)) {
            add(receiverAddressInfoUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addDropShipperInfoSection(
        context: Context?,
        dropShipperInfoUiModel: SimpleCopyableKeyValueUiModel
    ) {
        if (dropShipperInfoUiModel.shouldShow(context)) {
            add(dropShipperInfoUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentMethodSection(
        context: Context?,
        paymentMethodInfoItem: PaymentInfoUiModel.PaymentInfoItemUiModel
    ) {
        if (paymentMethodInfoItem.shouldShow(context)) add(paymentMethodInfoItem)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentInfoSection(
        context: Context?,
        paymentInfoItems: List<PaymentInfoUiModel.PaymentInfoItemUiModel>
    ) {
        paymentInfoItems.filter { it.shouldShow(context) }.also { addAll(it) }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentGrandTotalSection(
        context: Context?,
        paymentGrandTotal: PaymentInfoUiModel.PaymentGrandTotalUiModel
    ) {
        if (paymentGrandTotal.shouldShow(context)) add(paymentGrandTotal)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentRefundSection(
        context: Context?,
        paymentRefund: PofRefundInfoUiModel?
    ) {
        if (paymentRefund != null && paymentRefund.shouldShow(context)) add(paymentRefund)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPlatformFeeInfoSection() {
        add(PlatformFeeInfoUiModel())
    }

    private inline fun <reified T : Any> removeRecommendationWidget() {
        val index = visitables.indexOfLast { it is T }

        if (index != -1 && visitables.getOrNull(index - 1) is ThickDividerUiModel) {
            visitables.removeAt(index)
            visitables.removeAt(index - 1)
            notifyItemRangeRemoved(index - 1, 2)
        } else if (index != -1) {
            visitables.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateItems(context: Context?, uiState: BuyerOrderDetailUiState.HasData) {
        val newItems = setupNewItems(context, uiState)
        val diffCallback = BuyerOrderDetailDiffUtilCallback(
            visitables as List<Visitable<BuyerOrderDetailTypeFactory>>,
            newItems,
            typeFactory
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(newItems)
    }

    fun removeDigitalRecommendation() {
        removeRecommendationWidget<DigitalRecommendationUiModel>()
    }

    fun removePgRecommendation() {
        removeRecommendationWidget<PGRecommendationWidgetUiModel>()
    }

    fun getItemPosition(uiModel: BaseVisitableUiModel?): Int {
        return visitables.indexOf(uiModel)
    }

    fun getBaseVisitableUiModels(): List<BaseVisitableUiModel> {
        return visitables.filterIsInstance<BaseVisitableUiModel>()
    }
}
