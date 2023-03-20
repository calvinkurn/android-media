package com.tokopedia.checkout.view.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.converter.RatesDataConverter
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.CrossSellOrderSummaryModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.checkout.view.uimodel.ShipmentInsuranceTncModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.checkout.view.uimodel.ShippingCompletionTickerModel
import com.tokopedia.checkout.view.viewholder.PromoCheckoutViewHolder
import com.tokopedia.checkout.view.viewholder.PromoCheckoutViewHolder.Companion.ITEM_VIEW_PROMO_CHECKOUT
import com.tokopedia.checkout.view.viewholder.ShipmentButtonPaymentViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentButtonPaymentViewHolder.Companion.ITEM_VIEW_PAYMENT_BUTTON
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemExpandViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCostViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCrossSellViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentDonationViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentEmasViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentInsuranceTncViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentNewUpsellViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentOrderBottomViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentOrderTopViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentRecipientAddressViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentTickerAnnouncementViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentTickerErrorViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentTickerErrorViewHolder.Companion.ITEM_VIEW_SHIPMENT_TICKER_ERROR
import com.tokopedia.checkout.view.viewholder.ShipmentUpsellViewHolder
import com.tokopedia.checkout.view.viewholder.ShippingCompletionTickerViewHolder
import com.tokopedia.checkout.view.viewholder.ShippingCompletionTickerViewHolder.Companion.ITEM_VIEW_TICKER_SHIPPING_COMPLETION
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemExpandModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel.CREATOR.TYPE_CASHBACK
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel.CREATOR.TYPE_DISCOUNT
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel.CREATOR.TYPE_PRODUCT_DISCOUNT
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel.CREATOR.TYPE_SHIPPING_DISCOUNT
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder.Companion.ITEM_VIEW_UPLOAD
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder.Companion.LAYOUT
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author Irfan Khoirul on 23/04/18.
 */
class ShipmentAdapter @Inject constructor(
    private val shipmentAdapterActionListener: ShipmentAdapterActionListener,
    private val shipmentDataRequestConverter: ShipmentDataRequestConverter,
    private val ratesDataConverter: RatesDataConverter,
    private val sellerCashbackListener: SellerCashbackListener,
    private val uploadPrescriptionListener: UploadPrescriptionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ShipmentOrderTopViewHolder.Listener,
    ShipmentCartItemViewHolder.Listener,
    ShipmentCartItemExpandViewHolder.Listener,
    ShipmentOrderBottomViewHolder.Listener {

    companion object {
        const val DEFAULT_ERROR_POSITION = -1
        const val HEADER_POSITION = 0
        const val SECOND_HEADER_POSITION = 1
        private const val LAST_THREE_DIGIT_MODULUS: Long = 1000
    }

    private val shipmentDataList: ArrayList<Any> = ArrayList()
    private var tickerAnnouncementHolderData: TickerAnnouncementHolderData? = null
    private var lastApplyUiModel: LastApplyUiModel? = null
    private var uploadPrescriptionUiModel: UploadPrescriptionUiModel? = null
    var shipmentCartItemModelList: List<ShipmentCartItemModel>? = null
        private set
    var addressShipmentData: RecipientAddressModel? = null
        private set
    private var shipmentCostModel: ShipmentCostModel? = null
    private var shipmentInsuranceTncModel: ShipmentInsuranceTncModel? = null
    private var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null
    private var shipmentDonationModel: ShipmentDonationModel? = null
    private var shipmentCrossSellModelList: List<ShipmentCrossSellModel>? = null
    private var egoldAttributeModel: EgoldAttributeModel? = null
    private var shippingCompletionTickerModel: ShippingCompletionTickerModel? = null
    private var shipmentButtonPaymentModel: ShipmentButtonPaymentModel? = null
    private var shipmentUpsellModel: ShipmentUpsellModel? = null
    private var shipmentNewUpsellModel: ShipmentNewUpsellModel? = null
    private var compositeSubscription: CompositeSubscription? = CompositeSubscription()
        get() {
            if (field == null || field?.isUnsubscribed == false) {
                return CompositeSubscription()
            }
            return field
        }
    private var scheduleDeliverySubscription: CompositeSubscription? = CompositeSubscription()
        get() {
            if (field == null || field?.isUnsubscribed == false) {
                return CompositeSubscription()
            }
            return field
        }
    private var isShowOnboarding = false
    var lastChooseCourierItemPosition = 0
    var lastServiceId = 0

    fun setShowOnboarding(showOnboarding: Boolean) {
        isShowOnboarding = showOnboarding
    }

    override fun getItemViewType(position: Int): Int {
        when (shipmentDataList[position]) {
            is RecipientAddressModel -> {
                return ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS
            }

            is LastApplyUiModel -> {
                return ITEM_VIEW_PROMO_CHECKOUT
            }

            is ShipmentCostModel -> {
                return ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST
            }

            is ShipmentInsuranceTncModel -> {
                return ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC
            }

            is ShipmentSellerCashbackModel -> {
                return ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK
            }

            is ShipmentDonationModel -> {
                return ShipmentDonationViewHolder.ITEM_VIEW_DONATION
            }

            is ShipmentCrossSellModel -> {
                return ShipmentCrossSellViewHolder.ITEM_VIEW_CROSS_SELL
            }

            is EgoldAttributeModel -> {
                return ShipmentEmasViewHolder.ITEM_VIEW_EMAS
            }

            is ShipmentButtonPaymentModel -> {
                return ITEM_VIEW_PAYMENT_BUTTON
            }

            is TickerAnnouncementHolderData -> {
                return LAYOUT
            }

            is ShippingCompletionTickerModel -> {
                return ITEM_VIEW_TICKER_SHIPPING_COMPLETION
            }

            is ShipmentTickerErrorModel -> {
                return ITEM_VIEW_SHIPMENT_TICKER_ERROR
            }

            is UploadPrescriptionUiModel -> {
                return ITEM_VIEW_UPLOAD
            }

            is ShipmentUpsellModel -> {
                return ShipmentUpsellViewHolder.ITEM_VIEW_UPSELL
            }

            is ShipmentNewUpsellModel -> {
                return ShipmentNewUpsellViewHolder.ITEM_VIEW_UPSELL
            }

            is ShipmentCartItemTopModel -> {
                return ShipmentOrderTopViewHolder.LAYOUT
            }

            is ShipmentCartItemModel -> {
                return ShipmentOrderBottomViewHolder.LAYOUT
            }

            is CartItemModel -> {
                return ShipmentCartItemViewHolder.LAYOUT
            }

            is CartItemExpandModel -> {
                return ShipmentCartItemExpandViewHolder.LAYOUT
            }

            else -> return super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        when (viewType) {
            ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS -> {
                return ShipmentRecipientAddressViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST -> {
                return ShipmentCostViewHolder(view, layoutInflater)
            }

            ITEM_VIEW_PROMO_CHECKOUT -> {
                return PromoCheckoutViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC -> {
                return ShipmentInsuranceTncViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                return ShipmentSellerCashbackViewHolder(view, sellerCashbackListener)
            }

            ShipmentDonationViewHolder.ITEM_VIEW_DONATION -> {
                return ShipmentDonationViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentCrossSellViewHolder.ITEM_VIEW_CROSS_SELL -> {
                return ShipmentCrossSellViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentEmasViewHolder.ITEM_VIEW_EMAS -> {
                return ShipmentEmasViewHolder(view, shipmentAdapterActionListener)
            }

            ITEM_VIEW_PAYMENT_BUTTON -> {
                return ShipmentButtonPaymentViewHolder(
                    view,
                    shipmentAdapterActionListener,
                    compositeSubscription!!
                )
            }

            LAYOUT -> {
                return ShipmentTickerAnnouncementViewHolder(view, null)
            }

            ITEM_VIEW_TICKER_SHIPPING_COMPLETION -> {
                return ShippingCompletionTickerViewHolder(view, shipmentAdapterActionListener)
            }

            ITEM_VIEW_SHIPMENT_TICKER_ERROR -> {
                return ShipmentTickerErrorViewHolder(view, shipmentAdapterActionListener)
            }

            ITEM_VIEW_UPLOAD -> {
                return UploadPrescriptionViewHolder(view, uploadPrescriptionListener)
            }

            ShipmentUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                return ShipmentUpsellViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentNewUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                return ShipmentNewUpsellViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentOrderTopViewHolder.LAYOUT -> {
                return ShipmentOrderTopViewHolder(view)
            }

            ShipmentCartItemViewHolder.LAYOUT -> {
                return ShipmentCartItemViewHolder(view, this@ShipmentAdapter)
            }

            ShipmentCartItemExpandViewHolder.LAYOUT -> {
                return ShipmentCartItemExpandViewHolder(view, this@ShipmentAdapter)
            }

            ShipmentOrderBottomViewHolder.LAYOUT -> {
                return ShipmentOrderBottomViewHolder(
                    view,
                    ratesDataConverter,
                    this@ShipmentAdapter,
                    shipmentAdapterActionListener,
                    scheduleDeliverySubscription
                )
            }

            else -> throw RuntimeException("No view holder type found")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val data = shipmentDataList[position]
        when (viewType) {
            ShipmentRecipientAddressViewHolder.ITEM_VIEW_RECIPIENT_ADDRESS -> {
                (holder as ShipmentRecipientAddressViewHolder).bindViewHolder(
                    (data as RecipientAddressModel?)!!,
                    isShowOnboarding
                )
            }

            ITEM_VIEW_PROMO_CHECKOUT -> {
                (holder as PromoCheckoutViewHolder).bindViewHolder(lastApplyUiModel!!)
            }

            ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST -> {
                (holder as ShipmentCostViewHolder).bindViewHolder((data as ShipmentCostModel?)!!)
            }

            ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC -> {
                (holder as ShipmentInsuranceTncViewHolder).bindViewHolder(
                    (data as ShipmentInsuranceTncModel?)!!,
                    itemCount
                )
            }

            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                (holder as ShipmentSellerCashbackViewHolder).bindViewHolder(
                    shipmentSellerCashbackModel!!
                )
            }

            ShipmentDonationViewHolder.ITEM_VIEW_DONATION -> {
                (holder as ShipmentDonationViewHolder).bindViewHolder(shipmentDonationModel!!)
            }

            ShipmentCrossSellViewHolder.ITEM_VIEW_CROSS_SELL -> {
                (holder as ShipmentCrossSellViewHolder).bindViewHolder((data as ShipmentCrossSellModel?)!!)
            }

            ShipmentEmasViewHolder.ITEM_VIEW_EMAS -> {
                (holder as ShipmentEmasViewHolder).bindViewHolder(egoldAttributeModel!!)
            }

            ITEM_VIEW_PAYMENT_BUTTON -> {
                (holder as ShipmentButtonPaymentViewHolder).bindViewHolder((data as ShipmentButtonPaymentModel?)!!)
            }

            LAYOUT -> {
                (holder as TickerAnnouncementViewHolder).bind((data as TickerAnnouncementHolderData?)!!)
            }

            ITEM_VIEW_TICKER_SHIPPING_COMPLETION -> {
                (holder as ShippingCompletionTickerViewHolder).bindViewHolder((data as ShippingCompletionTickerModel?)!!)
            }

            ITEM_VIEW_SHIPMENT_TICKER_ERROR -> {
                (holder as ShipmentTickerErrorViewHolder).bind((data as ShipmentTickerErrorModel?)!!)
            }

            ITEM_VIEW_UPLOAD -> {
                (holder as UploadPrescriptionViewHolder).bindViewHolder((data as UploadPrescriptionUiModel?)!!)
            }

            ShipmentUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                (holder as ShipmentUpsellViewHolder).bind((data as ShipmentUpsellModel?)!!)
            }

            ShipmentNewUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                (holder as ShipmentNewUpsellViewHolder).bind((data as ShipmentNewUpsellModel?)!!)
            }

            ShipmentOrderTopViewHolder.LAYOUT -> {
                (holder as ShipmentOrderTopViewHolder).bind((data as ShipmentCartItemTopModel))
            }

            ShipmentCartItemViewHolder.LAYOUT -> {
                (holder as ShipmentCartItemViewHolder).bind(data as CartItemModel)
            }

            ShipmentCartItemExpandViewHolder.LAYOUT -> {
                (holder as ShipmentCartItemExpandViewHolder).bind(data as CartItemExpandModel)
            }

            ShipmentOrderBottomViewHolder.LAYOUT -> {
                (holder as ShipmentOrderBottomViewHolder).bind(
                    (data as ShipmentCartItemModel),
                    addressShipmentData
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return shipmentDataList.size
    }

    fun getShipmentDataList(): List<Any> {
        return shipmentDataList
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as? ShipmentOrderBottomViewHolder)?.unsubscribeDebouncer()
    }

    fun clearCompositeSubscription() {
        compositeSubscription?.clear()
        scheduleDeliverySubscription?.clear()
    }

    fun clearData() {
        shipmentDataList.clear()
        shipmentCartItemModelList = null
        addressShipmentData = null
        shipmentCostModel = null
        shipmentInsuranceTncModel = null
        shipmentSellerCashbackModel = null
        shipmentDonationModel = null
        egoldAttributeModel = null
        shipmentButtonPaymentModel = null
        lastApplyUiModel = null
        shippingCompletionTickerModel = null
        shipmentUpsellModel = null
        shipmentNewUpsellModel = null
        notifyDataSetChanged()
    }

    fun addTickerErrorData(shipmentTickerErrorModel: ShipmentTickerErrorModel) {
        shipmentDataList.add(HEADER_POSITION, shipmentTickerErrorModel)
    }

    fun addTickerAnnouncementData(tickerAnnouncementHolderData: TickerAnnouncementHolderData?) {
        if (tickerAnnouncementHolderData != null) {
            shipmentDataList.add(SECOND_HEADER_POSITION, tickerAnnouncementHolderData)
            this.tickerAnnouncementHolderData = tickerAnnouncementHolderData
        }
    }

    fun addUpsellData(shipmentUpsellModel: ShipmentUpsellModel) {
        shipmentDataList.add(shipmentUpsellModel)
        this.shipmentUpsellModel = shipmentUpsellModel
    }

    fun addNewUpsellData(shipmentNewUpsellModel: ShipmentNewUpsellModel) {
        shipmentDataList.add(shipmentNewUpsellModel)
        this.shipmentNewUpsellModel = shipmentNewUpsellModel
    }

    fun addAddressShipmentData(recipientAddressModel: RecipientAddressModel?) {
        if (recipientAddressModel != null) {
            addressShipmentData = recipientAddressModel
            shipmentDataList.add(recipientAddressModel)
        }
    }

    fun addCartItemDataList(shipmentCartItems: List<ShipmentCartItemModel>) {
        this.shipmentCartItemModelList = shipmentCartItems
        shipmentCartItems.forEach { shipmentCartItem ->
            shipmentDataList.add(ShipmentCartItemTopModel(shipmentCartItem))
            if (shipmentCartItem.isStateAllItemViewExpanded) {
                shipmentCartItem.cartItemModels.forEach {
                    shipmentDataList.add(it)
                }
            } else {
                shipmentDataList.add(shipmentCartItem.cartItemModels.first())
            }
            if (shipmentCartItem.cartItemModels.size > 1) {
                shipmentDataList.add(
                    CartItemExpandModel(
                        cartString = shipmentCartItem.cartString,
                        cartSize = shipmentCartItem.cartItemModels.size
                    )
                )
            }
            shipmentDataList.add(shipmentCartItem)
        }
        checkDataForCheckout()
    }

    fun addLastApplyUiDataModel(lastApplyUiModel: LastApplyUiModel?) {
        if (lastApplyUiModel != null) {
            this.lastApplyUiModel = lastApplyUiModel
            shipmentDataList.add(lastApplyUiModel)
        }
    }

    fun addUploadPrescriptionUiDataModel(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        if (uploadPrescriptionUiModel != null) {
            this.uploadPrescriptionUiModel = uploadPrescriptionUiModel
            shipmentDataList.add(uploadPrescriptionUiModel)
        }
    }

    fun addShipmentCostData(shipmentCostModel: ShipmentCostModel?) {
        if (shipmentCostModel != null) {
            this.shipmentCostModel = shipmentCostModel
            shipmentDataList.add(shipmentCostModel)
            updateShipmentCostModel()
        }
    }

    fun addEgoldAttributeData(egoldAttributeModel: EgoldAttributeModel?) {
        if (egoldAttributeModel != null) {
            this.egoldAttributeModel = egoldAttributeModel
            shipmentDataList.add(egoldAttributeModel)
        }
    }

    fun addShipmentDonationModel(shipmentDonationModel: ShipmentDonationModel?) {
        if (shipmentDonationModel != null) {
            this.shipmentDonationModel = shipmentDonationModel
            shipmentDataList.add(shipmentDonationModel)
        }
    }

    fun addShipmentButtonPaymentModel(shipmentButtonPaymentModel: ShipmentButtonPaymentModel?) {
        if (shipmentButtonPaymentModel != null) {
            this.shipmentButtonPaymentModel = shipmentButtonPaymentModel
            shipmentDataList.add(shipmentButtonPaymentModel)
        }
    }

    fun updateCheckoutButtonData(defaultTotal: String?) {
        if (shipmentCostModel != null && shipmentCartItemModelList != null) {
            var cartItemCounter = 0
            var cartItemErrorCounter = 0
            for (shipmentCartItemModel in shipmentCartItemModelList!!) {
                if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                    if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null && !shipmentAdapterActionListener.isTradeInByDropOff || shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null && shipmentAdapterActionListener.isTradeInByDropOff) {
                        cartItemCounter++
                    }
                }
                if (shipmentCartItemModel.isError) {
                    cartItemErrorCounter++
                }
            }
            if (cartItemCounter > 0 && cartItemCounter <= shipmentCartItemModelList!!.size) {
                val priceTotal: Double =
                    if (shipmentCostModel!!.totalPrice <= 0) 0.0 else shipmentCostModel!!.totalPrice
                val priceTotalFormatted =
                    removeDecimalSuffix(convertPriceValueToIdrFormat(priceTotal.toLong(), false))
                shipmentAdapterActionListener.onTotalPaymentChange(priceTotalFormatted, true)
            } else {
                shipmentAdapterActionListener.onTotalPaymentChange(
                    "-",
                    cartItemErrorCounter < shipmentCartItemModelList!!.size
                )
            }
        } else if (defaultTotal != null) {
            shipmentAdapterActionListener.onTotalPaymentChange(defaultTotal, false)
        }
    }

    fun updateShipmentSellerCashbackVisibility() {
        var cashback = 0.0
        if (shipmentCartItemModelList != null && shipmentCartItemModelList!!.isNotEmpty()) {
            for (shipmentCartItemModel in shipmentCartItemModelList!!) {
                for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                    if (cartItemModel.isCashback) {
                        val cashbackPercentageString = cartItemModel.cashback.replace("%", "")
                        val cashbackPercentage = cashbackPercentageString.toDouble()
                        cashback += cashbackPercentage / 100.0f * cartItemModel.price * cartItemModel.quantity
                    }
                }
            }
        }
        if (cashback > 0) {
            if (shipmentSellerCashbackModel == null) {
                shipmentSellerCashbackModel = ShipmentSellerCashbackModel()
            }
            shipmentSellerCashbackModel!!.isVisible = true
            shipmentSellerCashbackModel!!.sellerCashbackFmt = removeDecimalSuffix(
                convertPriceValueToIdrFormat(cashback.toLong(), false)
            )
            shipmentDataList.add(shipmentSellerCashbackModel!!)
        }
    }

    fun updateShippingCompletionTickerVisibility() {
        var positionDiff = 1
        if (shipmentInsuranceTncModel != null) {
            positionDiff++
        }
        if (!hasSetAllCourier()) {
            if (shippingCompletionTickerModel == null) {
                shippingCompletionTickerModel =
                    ShippingCompletionTickerModel("Pilih pengiriman dulu sebelum lanjut bayar.")
                shipmentDataList.add(
                    shipmentCostPosition + positionDiff,
                    shippingCompletionTickerModel!!
                )
                notifyItemInserted(shipmentCostPosition + positionDiff)
            }
        } else {
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is ShippingCompletionTickerModel) {
                    shippingCompletionTickerModel = null
                    shipmentDataList.removeAt(i)
                    notifyItemRemoved(i)
                    break
                }
            }
        }
    }

    fun updateInsuranceTncVisibility() {
        if (checkItemUseInsuranceExist()) {
            if (shipmentInsuranceTncModel == null) {
                shipmentInsuranceTncModel = ShipmentInsuranceTncModel()
                shipmentInsuranceTncModel!!.isVisible = true
                shipmentDataList.add(shipmentCostPosition + 1, shipmentInsuranceTncModel!!)
                notifyItemInserted(shipmentCostPosition + 1)
            }
        } else {
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is ShipmentInsuranceTncModel) {
                    shipmentInsuranceTncModel = null
                    shipmentDataList.removeAt(i)
                    notifyItemRemoved(i)
                    break
                }
            }
        }
    }

    fun updateUploadPrescription(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        this.uploadPrescriptionUiModel = uploadPrescriptionUiModel
        notifyItemChanged(uploadPrescriptionPosition)
    }

    private fun checkItemUseInsuranceExist(): Boolean {
        for (shipmentData in shipmentDataList) {
            if (shipmentData is ShipmentCartItemModel) {
                if (shipmentData.selectedShipmentDetailData != null && shipmentData.selectedShipmentDetailData!!.selectedCourier != null && shipmentData.selectedShipmentDetailData!!.useInsurance != null && shipmentData.selectedShipmentDetailData!!.useInsurance!!) {
                    return true
                }
                for (cartItemModel in shipmentData.cartItemModels) {
                    if (cartItemModel.isProtectionOptIn) return true
                }
            }
        }
        return false
    }

    fun updateDonation(checked: Boolean) {
        if (shipmentDonationModel != null) {
            shipmentDonationModel!!.isChecked = checked
            updateShipmentCostModel()
            notifyItemChanged(shipmentCostPosition)
        }
    }

    fun addListShipmentCrossSellModel(shipmentCrossSellModelList: List<ShipmentCrossSellModel>?) {
        if (shipmentCrossSellModelList != null && shipmentCrossSellModelList.isNotEmpty()) {
            this.shipmentCrossSellModelList = shipmentCrossSellModelList
            shipmentDataList.addAll(shipmentCrossSellModelList)
        }
    }

    fun updateCrossSell(checked: Boolean, crossSellModel: CrossSellModel?) {
        if (shipmentCrossSellModelList != null && !shipmentCrossSellModelList!!.isEmpty()) {
            for (shipmentCrossSellModel in shipmentCrossSellModelList!!) {
                if (crossSellModel != null) {
                    if (shipmentCrossSellModel.crossSellModel.id == crossSellModel.id) {
                        shipmentCrossSellModel.isChecked = checked
                        updateShipmentCostModel()
                        notifyItemChanged(shipmentCostPosition)
                        break
                    }
                }
            }
        }
    }

    private fun updateEmasCostModel() {
        val totalPrice = shipmentCostModel!!.totalPrice.toLong()
        var valueTOCheck = 0
        var buyEgoldValue: Long = 0
        if (egoldAttributeModel!!.isTiering) {
            egoldAttributeModel!!.egoldTieringModelArrayList.sortWith { o1, o2 -> (o1.minTotalAmount - o2.minTotalAmount).toInt() }
            var egoldTieringModel = EgoldTieringModel()
            for (data in egoldAttributeModel!!.egoldTieringModelArrayList) {
                if (totalPrice >= data.minTotalAmount) {
                    valueTOCheck = (totalPrice % data.basisAmount).toInt()
                    egoldTieringModel = data
                }
            }
            buyEgoldValue = calculateBuyEgoldValue(
                valueTOCheck,
                egoldTieringModel.minAmount.toInt(),
                egoldTieringModel.maxAmount.toInt(),
                egoldTieringModel.basisAmount
            )
        } else {
            valueTOCheck = (totalPrice % LAST_THREE_DIGIT_MODULUS).toInt()
            buyEgoldValue = calculateBuyEgoldValue(
                valueTOCheck,
                egoldAttributeModel!!.minEgoldRange,
                egoldAttributeModel!!.maxEgoldRange,
                LAST_THREE_DIGIT_MODULUS
            )
        }
        egoldAttributeModel!!.buyEgoldValue = buyEgoldValue
    }

    private fun calculateBuyEgoldValue(
        valueTOCheck: Int,
        minRange: Int,
        maxRange: Int,
        basisAmount: Long
    ): Long {
        if (basisAmount == 0L) {
            return 0
        }
        var buyEgoldValue: Long = 0
        for (i in minRange..maxRange) {
            if ((valueTOCheck + i) % basisAmount == 0L) {
                buyEgoldValue = i.toLong()
                break
            }
        }
        return buyEgoldValue
    }

    fun updateEgold(checked: Boolean) {
        if (egoldAttributeModel != null) {
            egoldAttributeModel!!.isChecked = checked
            updateShipmentCostModel()
            notifyItemChanged(shipmentCostPosition)
        }
    }

    fun resetCourier(cartPosition: Int) {
        if (shipmentDataList[cartPosition] is ShipmentCartItemModel) {
            val shipmentCartItemModel = shipmentDataList[cartPosition] as ShipmentCartItemModel?
            if (shipmentCartItemModel!!.selectedShipmentDetailData != null) {
                shipmentCartItemModel.selectedShipmentDetailData = null
                shipmentCartItemModel.voucherLogisticItemUiModel = null
                updateShipmentCostModel()
                updateInsuranceTncVisibility()
            }
        }
        notifyItemChanged(cartPosition)
    }

    fun resetAllCourier() {
        var eligibleNewShippingExperience = false
        for (position in shipmentDataList.indices) {
            if (shipmentDataList[position] is ShipmentCartItemModel) {
                val shipmentCartItemModel = shipmentDataList[position] as ShipmentCartItemModel?
                if (shipmentCartItemModel!!.selectedShipmentDetailData != null) {
                    shipmentCartItemModel.selectedShipmentDetailData = null
                    shipmentCartItemModel.voucherLogisticItemUiModel = null
                    notifyItemChanged(position)
                    eligibleNewShippingExperience =
                        shipmentCartItemModel.isEligibleNewShippingExperience
                }
            }
        }
        updateShipmentCostModel()
        updateInsuranceTncVisibility()
        if (eligibleNewShippingExperience) {
            updateShippingCompletionTickerVisibility()
        }
    }

    val recipientAddressModelPosition: Int
        get() {
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is RecipientAddressModel) {
                    return i
                }
            }
            return RecyclerView.NO_POSITION
        }
    val tickerAnnouncementHolderDataIndex: Int
        get() = shipmentDataList.indexOf(tickerAnnouncementHolderData!!)

    private fun checkDataForCheckout() {
        var availableCheckout = true
        for (shipmentData in shipmentDataList) {
            if (shipmentData is ShipmentCartItemModel) {
                if (shipmentData.selectedShipmentDetailData == null || shipmentData.isError) {
                    availableCheckout = false
                }
            }
        }
        if (availableCheckout) {
            shipmentAdapterActionListener.onDataEnableToCheckout()
        } else {
            shipmentAdapterActionListener.onDataDisableToCheckout(null)
        }
    }

    fun checkDropshipperValidation() {
        val hasSelectAllCourier = checkHasSelectAllCourier(true, -1, "", false, false)
        if (hasSelectAllCourier) {
            var availableCheckout = true
            var errorPosition = DEFAULT_ERROR_POSITION
            var errorSelectedShipmentData: Any? = null
            var isPrescriptionFrontEndValidationError = false
            for (i in shipmentDataList.indices) {
                val shipmentData = shipmentDataList[i]
                if (shipmentData is ShipmentCartItemModel) {
                    val shipmentCartItemModel = shipmentData
                    if (shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.selectedShipmentDetailData!!.useDropshipper != null && shipmentCartItemModel.selectedShipmentDetailData!!.useDropshipper!!) {
                        if (TextUtils.isEmpty(shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperName) || TextUtils.isEmpty(
                                shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperPhone
                            ) || !shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid || !shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid
                        ) {
                            availableCheckout = false
                            errorPosition = i
                            errorSelectedShipmentData = shipmentData
                            break
                        }
                    }
                    if (uploadPrescriptionUiModel != null && uploadPrescriptionUiModel!!.showImageUpload && uploadPrescriptionUiModel!!.frontEndValidation && shipmentCartItemModel.hasEthicalProducts && !shipmentCartItemModel.isError) {
                        for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                            if (!cartItemModel.isError && cartItemModel.ethicalDrugDataModel.needPrescription) {
                                val prescriptionIdsEmpty =
                                    shipmentCartItemModel.prescriptionIds.isEmpty()
                                val consultationEmpty =
                                    TextUtils.isEmpty(shipmentCartItemModel.tokoConsultationId) || TextUtils.isEmpty(
                                        shipmentCartItemModel.partnerConsultationId
                                    ) || shipmentCartItemModel.tokoConsultationId == "0" || shipmentCartItemModel.partnerConsultationId == "0" || TextUtils.isEmpty(
                                        shipmentCartItemModel.consultationDataString
                                    )
                                if (prescriptionIdsEmpty && consultationEmpty) {
                                    isPrescriptionFrontEndValidationError = true
                                    availableCheckout = false
                                    break
                                }
                            }
                        }
                    }
                }
            }
            shipmentAdapterActionListener.onCheckoutValidationResult(
                availableCheckout,
                errorSelectedShipmentData,
                errorPosition,
                isPrescriptionFrontEndValidationError
            )
        } else {
            var errorPosition = 0
            if (shipmentCartItemModelList != null) {
                for (shipmentCartItemModel in shipmentCartItemModelList!!) {
                    if (shipmentCartItemModel.selectedShipmentDetailData == null || shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier == null) {
                        errorPosition = shipmentDataList.indexOf(shipmentCartItemModel)
                        break
                    }
                }
            }
            shipmentAdapterActionListener.onCheckoutValidationResult(
                false,
                null,
                errorPosition,
                false
            )
        }
    }

    fun setSelectedCourierTradeInPickup(courierItemData: CourierItemData?) {
        // Should be only one invoice
        var index = 0
        var shipmentCartItemModel: ShipmentCartItemModel? = null
        for (`object` in shipmentDataList) {
            if (`object` is ShipmentCartItemModel) {
                index = shipmentDataList.indexOf(`object`)
                shipmentCartItemModel = `object`
                if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                    val shipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
                    shipmentDetailData!!.selectedCourierTradeInDropOff = courierItemData
                } else {
                    val shipmentDetailData = ShipmentDetailData()
                    shipmentDetailData.selectedCourierTradeInDropOff = courierItemData
                    shipmentCartItemModel.selectedShipmentDetailData = shipmentDetailData
                }
                updateShipmentCostModel()
                checkDataForCheckout()
                break
            }
        }
        if (index > 0) {
            notifyItemChanged(shipmentCostPosition)
            notifyItemChanged(index)
            checkHasSelectAllCourier(false, index, shipmentCartItemModel!!.cartString, false, false)
            if (shipmentCartItemModel.isEligibleNewShippingExperience) {
                updateShippingCompletionTickerVisibility()
            }
        }
    }

    fun setSelectedCourier(
        position: Int,
        newCourierItemData: CourierItemData,
        isForceReload: Boolean,
        skipValidateUse: Boolean
    ): ShipmentCartItemModel? {
        var shipmentCartItemModel: ShipmentCartItemModel? = null
        val currentShipmentData = shipmentDataList[position]
        if (currentShipmentData is ShipmentCartItemModel) {
            shipmentCartItemModel = currentShipmentData
            if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                shipmentCartItemModel.selectedShipmentDetailData!!.useInsurance = null
                shipmentCartItemModel.selectedShipmentDetailData!!.isOrderPriority = null
                shipmentCartItemModel.isShippingBorderRed = false
                shipmentCartItemModel.isHideChangeCourierCard =
                    newCourierItemData.isHideChangeCourierCard
                shipmentCartItemModel.durationCardDescription =
                    newCourierItemData.durationCardDescription
                val oldCourierItemData =
                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier
                checkAppliedCourierPromo(
                    position,
                    oldCourierItemData,
                    newCourierItemData,
                    shipmentCartItemModel
                )
                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier =
                    newCourierItemData
                if (!newCourierItemData.isAllowDropshiper) {
                    shipmentCartItemModel.selectedShipmentDetailData!!.useDropshipper = null
                }
                shipmentCartItemModel.isShowScheduleDelivery =
                    newCourierItemData.scheduleDeliveryUiModel != null
            } else {
                val shipmentDetailData = ShipmentDetailData()
                shipmentDetailData.selectedCourier = newCourierItemData
                shipmentDetailData.shipmentCartData = shipmentCartItemModel.shipmentCartData
                shipmentCartItemModel.selectedShipmentDetailData = shipmentDetailData
                shipmentCartItemModel.isShippingBorderRed = false
                shipmentCartItemModel.isHideChangeCourierCard =
                    newCourierItemData.isHideChangeCourierCard
                shipmentCartItemModel.durationCardDescription =
                    newCourierItemData.durationCardDescription
                if (!newCourierItemData.isAllowDropshiper) {
                    shipmentCartItemModel.selectedShipmentDetailData!!.useDropshipper = null
                }
                shipmentCartItemModel.isShowScheduleDelivery =
                    newCourierItemData.scheduleDeliveryUiModel != null
            }
            updateShipmentCostModel()
            checkDataForCheckout()
        }
        notifyItemChanged(shipmentCostPosition)
        notifyItemChanged(position)
        val tmpPosition = if (isForceReload) position else -1
        if (shipmentCartItemModel != null && shipmentCartItemModel.isEligibleNewShippingExperience) {
            checkHasSelectAllCourier(
                false,
                tmpPosition,
                shipmentCartItemModel.cartString,
                false,
                skipValidateUse
            )
            updateShippingCompletionTickerVisibility()
        }
        return shipmentCartItemModel
    }

    private fun checkAppliedCourierPromo(
        position: Int,
        oldCourierItemData: CourierItemData?,
        newCourierItemData: CourierItemData,
        shipmentCartItemModel: ShipmentCartItemModel?
    ) {
        // Do this section if toggle year end promo is on
        if (shipmentCartItemModel!!.selectedShipmentDetailData!!.selectedCourier != null) {
            // Check if promo applied on current selected courier
            if (shipmentCartItemModel.selectedShipmentDetailData!!.isCourierPromoApplied && TextUtils.isEmpty(
                    newCourierItemData.promoCode
                )
            ) {
                shipmentCartItemModel.selectedShipmentDetailData!!.isCourierPromoApplied = false
                // If applied on current selected courier but not on new selected courier then
                // check all item if promo still exist
                var courierPromoStillExist = false
                for (i in shipmentDataList.indices) {
                    if (i != position && shipmentDataList[i] is ShipmentCartItemModel) {
                        val model = shipmentDataList[i] as ShipmentCartItemModel?
                        if (model!!.selectedShipmentDetailData != null && model.selectedShipmentDetailData!!.isCourierPromoApplied) {
                            courierPromoStillExist = true
                            break
                        }
                    }
                }
                // If courier promo not exist anymore, cancel promo
                if (!courierPromoStillExist) {
                    shipmentAdapterActionListener.onCourierPromoCanceled(
                        oldCourierItemData!!.name,
                        oldCourierItemData.promoCode
                    )
                }
            }
        }
    }

    val isCourierPromoStillExist: Boolean
        get() {
            var courierPromoStillExist = false
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is ShipmentCartItemModel) {
                    val model = shipmentDataList[i] as ShipmentCartItemModel?
                    if (model!!.selectedShipmentDetailData != null && model.selectedShipmentDetailData!!.isCourierPromoApplied) {
                        courierPromoStillExist = true
                        break
                    }
                }
            }
            return courierPromoStillExist
        }

    fun cancelAllCourierPromo() {
        for (shipmentCartItemModel in shipmentCartItemModelList!!) {
            if (shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.selectedShipmentDetailData!!.isCourierPromoApplied) {
                shipmentCartItemModel.selectedShipmentDetailData!!.isCourierPromoApplied = false
            }
        }
    }

    fun setShippingCourierViewModels(
        shippingCourierUiModels: List<ShippingCourierUiModel>,
        recommendedCourier: CourierItemData,
        position: Int
    ) {
        for (shippingCourierUiModel in shippingCourierUiModels) {
            shippingCourierUiModel.isSelected = false
        }
        val currentShipmentData = shipmentDataList[position]
        if (currentShipmentData is ShipmentCartItemModel) {
            val cartItemModel = currentShipmentData
            if (cartItemModel.selectedShipmentDetailData != null && cartItemModel.selectedShipmentDetailData!!.selectedCourier != null) {
                for (shippingCourierUiModel in shippingCourierUiModels) {
                    if (shippingCourierUiModel.productData.shipperProductId == recommendedCourier.shipperProductId) {
                        shippingCourierUiModel.isSelected = true
                        break
                    }
                }
                cartItemModel.selectedShipmentDetailData!!.shippingCourierViewModels =
                    shippingCourierUiModels
            }
        }
    }

    fun checkHasSelectAllCourier(
        passCheckShipmentFromPaymentClick: Boolean,
        lastSelectedCourierOrderIndex: Int,
        lastSelectedCourierOrdercartString: String?,
        forceHitValidateUse: Boolean,
        skipValidateUse: Boolean
    ): Boolean {
        var cartItemCounter = 0
        if (shipmentCartItemModelList != null) {
            for (shipmentCartItemModel in shipmentCartItemModelList!!) {
                if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                    if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null && !shipmentAdapterActionListener.isTradeInByDropOff || shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null && shipmentAdapterActionListener.isTradeInByDropOff) {
                        cartItemCounter++
                    }
                } else if (shipmentCartItemModel.isError) {
                    cartItemCounter++
                }
            }
            if (cartItemCounter == shipmentCartItemModelList!!.size) {
                val requestData = getRequestData(null, null, false)
                if (!passCheckShipmentFromPaymentClick) {
                    shipmentAdapterActionListener.onFinishChoosingShipment(
                        lastSelectedCourierOrderIndex,
                        lastSelectedCourierOrdercartString,
                        forceHitValidateUse,
                        skipValidateUse
                    )
                }
                shipmentAdapterActionListener.updateCheckoutRequest(requestData.checkoutRequestData)
                return true
            }
        }
        return false
    }

    fun updateShipmentCostModel() {
        if (shipmentCostModel == null) return
        var totalWeight = 0.0
        var totalPrice = 0.0
        var additionalFee = 0.0
        var totalItemPrice = 0.0
        var tradeInPrice = 0.0
        var totalItem = 0
        var totalPurchaseProtectionPrice = 0.0
        var totalPurchaseProtectionItem = 0
        var shippingFee = 0.0
        var insuranceFee = 0.0
        var orderPriorityFee = 0.0
        var totalBookingFee = 0
        var hasAddOnSelected = false
        var totalAddOnPrice = 0.0
        for (shipmentData in shipmentDataList) {
            if (shipmentData is ShipmentCartItemModel) {
                val shipmentSingleAddressItem = shipmentData
                val cartItemModels = shipmentSingleAddressItem.cartItemModels
                for (cartItem in cartItemModels) {
                    if (!cartItem.isError) {
                        totalWeight += cartItem.weight * cartItem.quantity
                        totalItem += cartItem.quantity
                        if (cartItem.isProtectionOptIn) {
                            totalPurchaseProtectionItem += cartItem.quantity
                            totalPurchaseProtectionPrice += cartItem.protectionPrice
                        }
                        if (cartItem.isValidTradeIn) {
                            tradeInPrice += cartItem.oldDevicePrice.toDouble()
                        }
                        if (cartItem.isBundlingItem) {
                            if (cartItem.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                                totalItemPrice += cartItem.bundleQuantity * cartItem.bundlePrice
                            }
                        } else {
                            totalItemPrice += cartItem.quantity * cartItem.price
                        }
                        if (cartItem.addOnProductLevelModel.status == 1) {
                            if (cartItem.addOnProductLevelModel.addOnsDataItemModelList.isNotEmpty()) {
                                for (addOnsData in cartItem.addOnProductLevelModel.addOnsDataItemModelList) {
                                    totalAddOnPrice += addOnsData.addOnPrice
                                    hasAddOnSelected = true
                                }
                            }
                        }
                    }
                }
                if (shipmentData.selectedShipmentDetailData != null && !shipmentData.isError) {
                    val useInsurance = shipmentData.selectedShipmentDetailData!!.useInsurance
                    val isOrderPriority = shipmentData.selectedShipmentDetailData!!.isOrderPriority
                    val isTradeInPickup = shipmentAdapterActionListener.isTradeInByDropOff
                    if (isTradeInPickup) {
                        if (shipmentData.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                            shippingFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperPrice.toDouble()
                            if (useInsurance != null && useInsurance) {
                                insuranceFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.insurancePrice.toDouble()
                            }
                            if (isOrderPriority != null && isOrderPriority) {
                                orderPriorityFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.priorityPrice.toDouble()
                            }
                            additionalFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.additionalPrice.toDouble()
                        } else {
                            shippingFee = 0.0
                            insuranceFee = 0.0
                            orderPriorityFee = 0.0
                            additionalFee = 0.0
                        }
                    } else if (shipmentData.selectedShipmentDetailData!!.selectedCourier != null) {
                        shippingFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperPrice.toDouble()
                        if (useInsurance != null && useInsurance) {
                            insuranceFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.insurancePrice.toDouble()
                        }
                        if (isOrderPriority != null && isOrderPriority) {
                            orderPriorityFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourier!!.priorityPrice.toDouble()
                        }
                        additionalFee += shipmentSingleAddressItem.selectedShipmentDetailData!!.selectedCourier!!.additionalPrice.toDouble()
                    }
                }
                if (shipmentSingleAddressItem.isLeasingProduct) {
                    totalBookingFee += shipmentSingleAddressItem.bookingFee
                }
                if (shipmentData.addOnsOrderLevelModel != null) {
                    val addOnsDataModel = shipmentData.addOnsOrderLevelModel
                    if (addOnsDataModel != null && addOnsDataModel.status == 1 && !addOnsDataModel.addOnsDataItemModelList.isEmpty()) {
                        for ((addOnPrice) in addOnsDataModel.addOnsDataItemModelList) {
                            totalAddOnPrice += addOnPrice
                            hasAddOnSelected = true
                        }
                    }
                }
            }
        }
        var finalShippingFee = shippingFee - shipmentCostModel!!.shippingDiscountAmount
        if (finalShippingFee < 0) {
            finalShippingFee = 0.0
        }
        totalPrice =
            totalItemPrice + finalShippingFee + insuranceFee + orderPriorityFee + totalPurchaseProtectionPrice + additionalFee + totalBookingFee - shipmentCostModel!!.productDiscountAmount - tradeInPrice + totalAddOnPrice
        shipmentCostModel!!.totalWeight = totalWeight
        shipmentCostModel!!.additionalFee = additionalFee
        shipmentCostModel!!.totalItemPrice = totalItemPrice
        shipmentCostModel!!.totalItem = totalItem
        shipmentCostModel!!.shippingFee = shippingFee
        shipmentCostModel!!.insuranceFee = insuranceFee
        shipmentCostModel!!.priorityFee = orderPriorityFee
        shipmentCostModel!!.totalPurchaseProtectionItem = totalPurchaseProtectionItem
        shipmentCostModel!!.purchaseProtectionFee = totalPurchaseProtectionPrice
        shipmentCostModel!!.tradeInPrice = tradeInPrice
        shipmentCostModel!!.totalAddOnPrice = totalAddOnPrice
        shipmentCostModel!!.hasAddOn = hasAddOnSelected
        if (shipmentDonationModel != null && shipmentDonationModel!!.isChecked) {
            shipmentCostModel!!.donation = shipmentDonationModel!!.donation.nominal.toDouble()
        } else {
            if (shipmentCostModel!!.donation > 0) {
                shipmentCostModel!!.donation = 0.0
            }
        }
        totalPrice += shipmentCostModel!!.donation
        shipmentCostModel!!.totalPrice = totalPrice
        var upsellCost: ShipmentCrossSellModel? = null
        if (shipmentNewUpsellModel != null && shipmentNewUpsellModel!!.isSelected && shipmentNewUpsellModel!!.isShow) {
            val crossSellModel = CrossSellModel()
            crossSellModel.orderSummary =
                CrossSellOrderSummaryModel(shipmentNewUpsellModel!!.summaryInfo, "")
            crossSellModel.price = shipmentNewUpsellModel!!.price.toDouble()
            upsellCost = ShipmentCrossSellModel(crossSellModel, true, true, -1)
        }
        val listCheckedCrossModel = ArrayList<ShipmentCrossSellModel>()
        if (shipmentCrossSellModelList != null && !shipmentCrossSellModelList!!.isEmpty()) {
            for (crossSellModel in shipmentCrossSellModelList!!) {
                if (crossSellModel!!.isChecked) {
                    listCheckedCrossModel.add(crossSellModel)
                    totalPrice += crossSellModel.crossSellModel.price
                    shipmentCostModel!!.totalPrice = totalPrice
                }
            }
        }
        if (upsellCost != null) {
            listCheckedCrossModel.add(upsellCost)
            totalPrice += upsellCost.crossSellModel.price
            shipmentCostModel!!.totalPrice = totalPrice
        }
        shipmentCostModel!!.listCrossSell = listCheckedCrossModel
        if (egoldAttributeModel != null && egoldAttributeModel!!.isEligible) {
            updateEmasCostModel()
            if (egoldAttributeModel!!.isChecked) {
                totalPrice += egoldAttributeModel!!.buyEgoldValue.toDouble()
                shipmentCostModel!!.totalPrice = totalPrice
                shipmentCostModel!!.emasPrice = egoldAttributeModel!!.buyEgoldValue.toDouble()
            } else if (shipmentCostModel!!.emasPrice > 0) {
                shipmentCostModel!!.emasPrice = 0.0
            }
            notifyDataSetChanged()
        }
        shipmentCostModel!!.bookingFee = totalBookingFee
        updateCheckoutButtonData(null)
    }

    fun clearTotalPromoStackAmount() {
        shipmentCostModel!!.totalPromoStackAmount = 0
        shipmentCostModel!!.totalDiscWithoutCashback = 0
    }

    val uploadPrescriptionPosition: Int
        get() {
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is UploadPrescriptionUiModel) {
                    return i
                }
            }
            return 0
        }
    val shipmentCostPosition: Int
        get() {
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is ShipmentCostModel) {
                    return i
                }
            }
            return 0
        }
    val firstShopPosition: Int
        get() {
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is ShipmentCartItemModel) {
                    return i
                }
            }
            return 0
        }

    fun getAddOnOrderLevelPosition(cartString: String): Int {
        for (i in shipmentDataList.indices) {
            if (shipmentDataList[i] is ShipmentCartItemModel) {
                val shipmentCartItemModel = shipmentDataList[i] as ShipmentCartItemModel
                if (shipmentCartItemModel.cartString == cartString && shipmentCartItemModel.addOnsOrderLevelModel != null) {
                    if (shipmentCartItemModel.addOnsOrderLevelModel!!.addOnsButtonModel.title.isNotEmpty()) {
                        return i
                    }
                }
            }
        }
        return 0
    }

    fun getAddOnProductLevelPosition(cartString: String): Int {
        for (i in shipmentDataList.indices) {
            if (shipmentDataList[i] is ShipmentCartItemModel) {
                val shipmentCartItemModel = shipmentDataList[i] as ShipmentCartItemModel
                if (shipmentCartItemModel.cartString == cartString && shipmentCartItemModel.cartItemModels.isNotEmpty()) {
                    for (j in shipmentCartItemModel.cartItemModels.indices) {
                        if (shipmentCartItemModel.cartItemModels[j].addOnProductLevelModel.addOnsButtonModel.title.isNotEmpty()) {
                            return i
                        }
                    }
                }
            }
        }
        return 0
    }

    val promoCheckoutPosition: Int
        get() {
            for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is LastApplyUiModel) {
                    return i
                }
            }
            return 0
        }

    fun updateItemAndTotalCost(position: Int) {
        notifyItemChanged(shipmentCostPosition)
        notifyItemChanged(position)
    }

    fun setPromoBenefit(benefitSummaries: List<SummariesItemUiModel>) {
        if (shipmentCostModel != null) {
            for (benefitSummary in benefitSummaries) {
                if (benefitSummary.type == TYPE_DISCOUNT) {
                    if (benefitSummary.details.isNotEmpty()) {
                        shipmentCostModel!!.isHasDiscountDetails = true
                        for (detail in benefitSummary.details) {
                            if (detail.type == TYPE_SHIPPING_DISCOUNT) {
                                shipmentCostModel!!.shippingDiscountAmount = detail.amount
                                shipmentCostModel!!.shippingDiscountLabel = detail.description
                            } else if (detail.type == TYPE_PRODUCT_DISCOUNT) {
                                shipmentCostModel!!.productDiscountAmount = detail.amount
                                shipmentCostModel!!.productDiscountLabel = detail.description
                            }
                        }
                    } else if (hasSetAllCourier()) {
                        shipmentCostModel!!.isHasDiscountDetails = false
                        shipmentCostModel!!.discountAmount = benefitSummary.amount
                        shipmentCostModel!!.discountLabel = benefitSummary.description
                    }
                } else if (benefitSummary.type == TYPE_CASHBACK) {
                    shipmentCostModel!!.cashbackAmount = benefitSummary.amount
                    shipmentCostModel!!.cashbackLabel = benefitSummary.description
                }
            }
        }
    }

    fun resetPromoBenefit() {
        shipmentCostModel?.isHasDiscountDetails = false
        shipmentCostModel?.discountAmount = 0
        shipmentCostModel?.discountLabel = ""
        shipmentCostModel?.shippingDiscountAmount = 0
        shipmentCostModel?.shippingDiscountLabel = ""
        shipmentCostModel?.productDiscountAmount = 0
        shipmentCostModel?.productDiscountLabel = ""
        shipmentCostModel?.cashbackAmount = 0
        shipmentCostModel?.cashbackLabel = ""
    }

    fun resetCourierPromoState() {
        if (shipmentCartItemModelList != null) {
            for (shipmentCartItemModel in shipmentCartItemModelList!!) {
                shipmentCartItemModel.isStateHasLoadCourierState = false
            }
        }
    }

    fun updateShipmentDestinationPinpoint(latitude: String?, longitude: String?) {
        if (latitude != null && longitude != null) {
            if (addressShipmentData != null) {
                addressShipmentData!!.latitude = latitude
                addressShipmentData!!.longitude = longitude
            }
            for (shipmentCartItemModel in shipmentCartItemModelList!!) {
                if (shipmentCartItemModel.shipmentCartData != null) {
                    shipmentCartItemModel.shipmentCartData!!.destinationLatitude = latitude
                    shipmentCartItemModel.shipmentCartData!!.destinationLongitude = longitude
                }
            }
        }
    }

    fun hasSetAllCourier(): Boolean {
        for (itemData in shipmentDataList) {
            if (itemData is ShipmentCartItemModel) {
                if (itemData.selectedShipmentDetailData == null && !itemData.isError) {
                    return false
                }
            }
        }
        return true
    }

    fun setCourierPromoApplied(position: Int) {
        if (shipmentDataList[position] is ShipmentCartItemModel) {
            val shipmentCartItemModel = shipmentDataList[position] as ShipmentCartItemModel?
            if (shipmentCartItemModel!!.selectedShipmentDetailData != null) {
                shipmentCartItemModel.selectedShipmentDetailData!!.isCourierPromoApplied = true
            }
        }
    }

    fun getRequestData(
        recipientAddressModel: RecipientAddressModel?,
        shipmentCartItemModelList: List<ShipmentCartItemModel>?,
        isAnalyticsPurpose: Boolean
    ): RequestData {
        val addressModel: RecipientAddressModel? = recipientAddressModel ?: addressShipmentData
        if (shipmentCartItemModelList != null) {
            this.shipmentCartItemModelList = shipmentCartItemModelList
        }
        return shipmentDataRequestConverter.generateRequestData(
            this.shipmentCartItemModelList,
            addressModel,
            isAnalyticsPurpose,
            shipmentAdapterActionListener.isTradeInByDropOff
        )
    }

    fun getShipmentCartItemModelByIndex(index: Int): ShipmentCartItemModel? {
        return if (shipmentDataList.isNotEmpty() && index < shipmentDataList.size) {
            if (shipmentDataList[index] is ShipmentCartItemModel) shipmentDataList[index] as ShipmentCartItemModel? else null
        } else {
            null
        }
    }

    class RequestData {
        var checkoutRequestData: List<DataCheckoutRequest> = ArrayList()
    }

    fun getShipmentCartItemModelPosition(shipmentCartItemModel: ShipmentCartItemModel): Int {
        return shipmentDataList.indexOf(shipmentCartItemModel)
    }

    fun updatePromoCheckoutData(promoUiModel: PromoUiModel) {
        lastApplyUiModel = mapValidateUsePromoUiModelToLastApplyUiModel(
            promoUiModel
        )
    }

    fun resetPromoCheckoutData() {
        lastApplyUiModel = LastApplyUiModel()
    }

    private fun getGroupHeaderByCartString(cartString: String): Pair<Int, ShipmentCartItemModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is ShipmentCartItemTopModel && data.shipmentCartItemModel.cartString == cartString
        }
        return Pair(index, shipmentDataList[index] as ShipmentCartItemModel)
    }

    private fun getFirstCartItemByCartString(cartString: String): Pair<Int, CartItemModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is CartItemModel && data.cartString == cartString && data.cartItemPosition == 0
        }
        return Pair(index, shipmentDataList[index] as CartItemModel)
    }

    private fun getFirstErrorCartItemByCartString(cartString: String): Pair<Int, CartItemModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is CartItemModel && data.cartString == cartString && data.isError
        }
        return Pair(index, shipmentDataList[index] as CartItemModel)
    }

    private fun getGroupCartExpandByCartString(cartString: String): Pair<Int, CartItemExpandModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is CartItemExpandModel && data.cartString == cartString
        }
        return Pair(index, shipmentDataList[index] as CartItemExpandModel)
    }

    private fun getShipmentCartItemByCartString(cartString: String): Pair<Int, ShipmentCartItemModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is ShipmentCartItemModel && data.cartString == cartString
        }
        return Pair(index, shipmentDataList[index] as ShipmentCartItemModel)
    }

    override fun onViewFreeShippingPlusBadge() {
        shipmentAdapterActionListener.onViewFreeShippingPlusBadge()
    }

    override fun onClickLihatOnTickerOrderError(shopId: String?, errorMessage: String?) {
        shipmentAdapterActionListener.onClickLihatOnTickerOrderError(shopId, errorMessage)
    }

    override fun onErrorShouldExpandProduct(shipmentCartItemModel: ShipmentCartItemModel) {
        val (position, data) = getGroupCartExpandByCartString(shipmentCartItemModel.cartString)
        onClickExpandGroupProduct(position, data)
    }

    override fun onErrorShouldScrollToProduct(shipmentCartItemModel: ShipmentCartItemModel) {
        val (position, _) = getFirstErrorCartItemByCartString(shipmentCartItemModel.cartString)
        shipmentAdapterActionListener.scrollToPositionWithOffset(position)
    }

    override fun onCheckPurchaseProtection(position: Int, cartItem: CartItemModel) {
        val (shipmentCartItemPosition, shipmentCartItem) =
            getShipmentCartItemByCartString(cartItem.cartString)
        if (cartItem.isProtectionOptIn && shipmentCartItem.selectedShipmentDetailData?.useDropshipper == true) {
            shipmentCartItem.selectedShipmentDetailData?.useDropshipper = false
            shipmentCartItem.cartItemModels[cartItem.cartItemPosition] = cartItem
            shipmentDataList[shipmentCartItemPosition] = shipmentCartItem
            notifyItemChanged(shipmentCartItemPosition)
            shipmentDataList[position] = cartItem
            notifyItemChanged(position)
            shipmentAdapterActionListener.onPurchaseProtectionLogicError()
        }
        shipmentAdapterActionListener.onNeedUpdateRequestData()
        shipmentAdapterActionListener.onPurchaseProtectionChangeListener(position)
    }

    override fun onClickPurchaseProtectionTooltip(cartItem: CartItemModel) {
        shipmentAdapterActionListener.navigateToProtectionMore(cartItem)
    }

    override fun onClickAddOnProductLevel(
        cartItem: CartItemModel,
        addOnWording: AddOnWordingModel
    ) {
        shipmentAdapterActionListener.openAddOnProductLevelBottomSheet(cartItem, addOnWording)
    }

    override fun onImpressionAddOnProductLevel(productId: String) {
        shipmentAdapterActionListener.addOnProductLevelImpression(productId)
    }

    override fun onClickCollapseGroupProduct(
        position: Int,
        cartItemExpandModel: CartItemExpandModel
    ) {
        shipmentDataList[position] = cartItemExpandModel
        notifyItemChanged(position)

        val (firstCartItemPosition, _) =
            getFirstCartItemByCartString(cartItemExpandModel.cartString)
        val removedCartItemSize =
            cartItemExpandModel.cartSize - 1
        repeat(removedCartItemSize) {
            shipmentDataList.removeAt(firstCartItemPosition + 1)
        }
        notifyItemRangeRemoved(firstCartItemPosition + 1, removedCartItemSize)
    }

    override fun onClickExpandGroupProduct(
        position: Int,
        cartItemExpandModel: CartItemExpandModel
    ) {
        shipmentDataList[position] = cartItemExpandModel
        notifyItemChanged(position)

        val (_, shipmentCartItem) = getShipmentCartItemByCartString(cartItemExpandModel.cartString)
        val (firstCartItemPosition, _) = getFirstCartItemByCartString(cartItemExpandModel.cartString)
        val newCartItems = shipmentCartItem.cartItemModels.drop(1)
        shipmentDataList.addAll(firstCartItemPosition + 1, newCartItems)
        notifyItemRangeInserted(firstCartItemPosition + 1, newCartItems.size)
    }

    override fun onClickExpandSubtotal(
        position: Int,
        shipmentCartItemModel: ShipmentCartItemModel
    ) {
        shipmentDataList[position] = shipmentCartItemModel
        notifyItemChanged(position)
    }
}
