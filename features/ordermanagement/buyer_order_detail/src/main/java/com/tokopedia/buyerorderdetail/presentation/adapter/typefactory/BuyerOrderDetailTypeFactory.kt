package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.CourierButtonListener
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.AddonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.AwbInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CopyableKeyValueViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierDriverInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.DigitalRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.DriverTippingInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.EpharmacyInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderInsuranceViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderResolutionViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderStatusHeaderViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderStatusInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PartialProductItemViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PaymentGrandTotalViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PaymentInfoItemViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PgRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PlainHeaderViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PlatformFeeInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofHeaderLabelViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofRefundInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductListHeaderViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductListToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ThickDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ThinDashedDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ThinDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.TickerViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.DigitalRecommendationUiModel
import com.tokopedia.buyerorderdetail.presentation.model.EpharmacyInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocBomDetailSectionUiModel
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
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData
import com.tokopedia.order_management_common.presentation.typefactory.BuyMoreGetMoreTypeFactory
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmSectionViewHolder
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.typefactory.ScpRewardsMedalTouchPointWidgetTypeFactory
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel.ScpRewardsMedalTouchPointWidgetUiModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.viewholder.ScpRewardsMedalTouchPointWidgetViewHolder

@Suppress("UNUSED_PARAMETER")
open class BuyerOrderDetailTypeFactory(
    private val productBundlingViewListener: ProductBundlingViewHolder.Listener,
    private val tickerViewHolderListener: TickerViewHolder.TickerViewHolderListener,
    private val digitalRecommendationData: DigitalRecommendationData,
    private val digitalRecommendationListener: DigitalRecommendationViewHolder.ActionListener,
    private val courierInfoViewHolderListener: CourierInfoViewHolder.CourierInfoViewHolderListener,
    private val productListToggleListener: ProductListToggleViewHolder.Listener,
    private val pofRefundInfoListener: PofRefundInfoViewHolder.Listener,
    private val scpRewardsMedalTouchPointWidgetListener: ScpRewardsMedalTouchPointWidgetViewHolder.ScpRewardsMedalTouchPointWidgetListener,
    private val owocInfoListener: OwocInfoViewHolder.Listener,
    private val bmgmListener: BmgmSectionViewHolder.Listener,
    private val productBenefitListener: AddOnViewHolder.Listener,
    private val orderResolutionListener: OrderResolutionViewHolder.OrderResolutionListener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool,
    protected val productViewListener: PartialProductItemViewHolder.ProductViewListener,
    protected val bottomSheetListener: PartialProductItemViewHolder.ShareProductBottomSheetListener,
    protected val navigator: BuyerOrderDetailNavigator,
    protected val buyerOrderDetailBindRecomWidgetListener: PgRecommendationViewHolder.BuyerOrderDetailBindRecomWidgetListener,
    protected val courierButtonListener: CourierButtonListener,
    protected val addOnListener: AddOnViewHolder.Listener
) : BaseAdapterTypeFactory(),
    ScpRewardsMedalTouchPointWidgetTypeFactory,
    BuyMoreGetMoreTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AwbInfoViewHolder.LAYOUT -> AwbInfoViewHolder(parent)
            CopyableKeyValueViewHolder.LAYOUT -> CopyableKeyValueViewHolder(parent)
            CourierDriverInfoViewHolder.LAYOUT -> CourierDriverInfoViewHolder(parent, navigator, courierButtonListener)
            CourierInfoViewHolder.LAYOUT -> CourierInfoViewHolder(parent, courierInfoViewHolderListener, navigator)
            OrderStatusHeaderViewHolder.LAYOUT -> OrderStatusHeaderViewHolder(parent, navigator)
            OrderStatusInfoViewHolder.LAYOUT -> OrderStatusInfoViewHolder(parent, navigator)
            PaymentGrandTotalViewHolder.LAYOUT -> PaymentGrandTotalViewHolder(parent)
            PaymentInfoItemViewHolder.LAYOUT -> PaymentInfoItemViewHolder(parent)
            PlainHeaderViewHolder.LAYOUT -> PlainHeaderViewHolder(parent)
            ProductListHeaderViewHolder.LAYOUT -> ProductListHeaderViewHolder(parent, navigator)
            ProductViewHolder.LAYOUT -> ProductViewHolder(parent, productViewListener, bottomSheetListener, addOnListener, navigator)
            ProductBundlingViewHolder.LAYOUT -> ProductBundlingViewHolder(parent, addOnListener, productBundlingViewListener, navigator)
            ThickDividerViewHolder.LAYOUT -> ThickDividerViewHolder(parent)
            ThinDashedDividerViewHolder.LAYOUT -> ThinDashedDividerViewHolder(parent)
            ThinDividerViewHolder.LAYOUT -> ThinDividerViewHolder(parent)
            TickerViewHolder.LAYOUT -> TickerViewHolder(parent, navigator, tickerViewHolderListener)
            DigitalRecommendationViewHolder.LAYOUT -> DigitalRecommendationViewHolder(parent, digitalRecommendationData, digitalRecommendationListener)
            PgRecommendationViewHolder.LAYOUT -> PgRecommendationViewHolder(parent, buyerOrderDetailBindRecomWidgetListener)
            DriverTippingInfoViewHolder.LAYOUT -> DriverTippingInfoViewHolder(parent, navigator)
            AddonsViewHolder.LAYOUT -> AddonsViewHolder(parent, addOnListener)
            OrderResolutionViewHolder.LAYOUT -> OrderResolutionViewHolder(parent, navigator, orderResolutionListener)
            PlatformFeeInfoViewHolder.LAYOUT -> PlatformFeeInfoViewHolder(parent, navigator)
            OrderInsuranceViewHolder.LAYOUT -> OrderInsuranceViewHolder(parent, navigator)
            EpharmacyInfoViewHolder.LAYOUT -> EpharmacyInfoViewHolder(parent)
            ProductListToggleViewHolder.LAYOUT -> ProductListToggleViewHolder(parent, productListToggleListener)
            PofHeaderLabelViewHolder.LAYOUT -> PofHeaderLabelViewHolder(parent)
            PofRefundInfoViewHolder.LAYOUT -> PofRefundInfoViewHolder(parent, pofRefundInfoListener)
            ScpRewardsMedalTouchPointWidgetViewHolder.LAYOUT -> ScpRewardsMedalTouchPointWidgetViewHolder(parent, scpRewardsMedalTouchPointWidgetListener)
            OwocInfoViewHolder.LAYOUT -> OwocInfoViewHolder(parent, owocInfoListener)
            BmgmSectionViewHolder.LAYOUT -> BmgmSectionViewHolder(
                parent,
                recyclerViewSharedPool,
                addOnListener,
                bmgmListener,
                productBenefitListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(uiModel: ScpRewardsMedalTouchPointWidgetUiModel): Int = ScpRewardsMedalTouchPointWidgetViewHolder.LAYOUT

    fun type(awbInfoUiModel: ShipmentInfoUiModel.AwbInfoUiModel): Int {
        return AwbInfoViewHolder.LAYOUT
    }

    fun type(courierDriverInfoUiModel: ShipmentInfoUiModel.CourierDriverInfoUiModel): Int {
        return CourierDriverInfoViewHolder.LAYOUT
    }

    fun type(courierUiModel: ShipmentInfoUiModel.CourierInfoUiModel): Int {
        return CourierInfoViewHolder.LAYOUT
    }

    fun type(orderStatusHeaderUiModel: OrderStatusUiModel.OrderStatusHeaderUiModel): Int {
        return OrderStatusHeaderViewHolder.LAYOUT
    }

    fun type(orderStatusInfoUiModel: OrderStatusUiModel.OrderStatusInfoUiModel): Int {
        return OrderStatusInfoViewHolder.LAYOUT
    }

    fun type(paymentGrandTotalUiModel: PaymentInfoUiModel.PaymentGrandTotalUiModel): Int {
        return PaymentGrandTotalViewHolder.LAYOUT
    }

    fun type(paymentInfoUiModel: PaymentInfoUiModel.PaymentInfoItemUiModel): Int {
        return PaymentInfoItemViewHolder.LAYOUT
    }

    fun type(plainHeaderUiModel: PlainHeaderUiModel): Int {
        return PlainHeaderViewHolder.LAYOUT
    }

    fun type(productListHeaderUiModel: ProductListUiModel.ProductListHeaderUiModel): Int {
        return ProductListHeaderViewHolder.LAYOUT
    }

    fun type(productUiModel: ProductListUiModel.ProductUiModel): Int {
        return ProductViewHolder.LAYOUT
    }

    fun type(productBundlingUiModel: ProductListUiModel.ProductBundlingUiModel): Int {
        return ProductBundlingViewHolder.LAYOUT
    }

    fun type(thickDividerUiModel: ThickDividerUiModel): Int {
        return ThickDividerViewHolder.LAYOUT
    }

    fun type(thinDashedDividerUiModel: ThinDashedDividerUiModel): Int {
        return ThinDashedDividerViewHolder.LAYOUT
    }

    fun type(thinDividerUiModel: ThinDividerUiModel): Int {
        return ThinDividerViewHolder.LAYOUT
    }

    fun type(tickerUiModel: TickerUiModel): Int {
        return TickerViewHolder.LAYOUT
    }

    fun type(simpleCopyableKeyValueUiModel: SimpleCopyableKeyValueUiModel): Int {
        return CopyableKeyValueViewHolder.LAYOUT
    }

    fun type(digitalRecommendationUiModel: DigitalRecommendationUiModel): Int =
        DigitalRecommendationViewHolder.LAYOUT

    fun type(pgRecommendationWidgetUiModel: PGRecommendationWidgetUiModel): Int =
        PgRecommendationViewHolder.LAYOUT

    fun type(driverTippingInfoUiModel: ShipmentInfoUiModel.DriverTippingInfoUiModel): Int {
        return DriverTippingInfoViewHolder.LAYOUT
    }

    fun type(orderLevelAddOn: ProductListUiModel.OrderLevelAddOn): Int {
        return AddonsViewHolder.LAYOUT
    }

    fun type(orderResolutionUIModel: OrderResolutionUiModel): Int {
        return OrderResolutionViewHolder.LAYOUT
    }

    fun type(platformFeeInfoUiModel: PlatformFeeInfoUiModel): Int {
        return PlatformFeeInfoViewHolder.LAYOUT
    }

    fun type(orderInsuranceUiModel: OrderInsuranceUiModel): Int {
        return OrderInsuranceViewHolder.LAYOUT
    }

    fun type(ePharmarcyUiModel: EpharmacyInfoUiModel): Int {
        return EpharmacyInfoViewHolder.LAYOUT
    }

    fun type(productListCollapseUiModel: ProductListUiModel.ProductListToggleUiModel): Int {
        return ProductListToggleViewHolder.LAYOUT
    }

    fun type(productPofHeaderLabelUiModel: ProductListUiModel.ProductPofHeaderLabelUiModel): Int {
        return PofHeaderLabelViewHolder.LAYOUT
    }

    fun type(pofRefundInfoUiModel: PofRefundInfoUiModel): Int {
        return PofRefundInfoViewHolder.LAYOUT
    }

    fun type(owocBomDetailSectionUiModel: OwocBomDetailSectionUiModel): Int {
        return OwocInfoViewHolder.LAYOUT
    }

    override fun type(uiModel: ProductBmgmSectionUiModel): Int {
        return BmgmSectionViewHolder.LAYOUT
    }
}
