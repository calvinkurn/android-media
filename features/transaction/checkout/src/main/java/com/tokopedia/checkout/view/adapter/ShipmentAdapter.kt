package com.tokopedia.checkout.view.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.CheckoutHolderItemEmasBinding
import com.tokopedia.checkout.databinding.ItemCrossSellBinding
import com.tokopedia.checkout.databinding.ItemDonationBinding
import com.tokopedia.checkout.databinding.ItemInsuranceTncBinding
import com.tokopedia.checkout.databinding.ItemPromoCheckoutBinding
import com.tokopedia.checkout.databinding.ItemShipmentButtonPaymentBinding
import com.tokopedia.checkout.databinding.ItemShipmentTickerErrorBinding
import com.tokopedia.checkout.databinding.ItemTickerShippingCompletionBinding
import com.tokopedia.checkout.databinding.ItemUpsellBinding
import com.tokopedia.checkout.databinding.ViewItemShipmentCostDetailsBinding
import com.tokopedia.checkout.databinding.ViewItemShipmentRecipientAddressBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.converter.RatesDataConverter
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
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
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemBottomViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemExpandViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemTopViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCostViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCrossSellViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentDonationViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentEmasViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentInsuranceTncViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentNewUpsellViewHolder
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
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder.Companion.ITEM_VIEW_UPLOAD
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
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

class ShipmentAdapter @Inject constructor(
    private val shipmentAdapterActionListener: ShipmentAdapterActionListener,
    private val ratesDataConverter: RatesDataConverter,
    private val sellerCashbackListener: SellerCashbackListener,
    private val uploadPrescriptionListener: UploadPrescriptionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ShipmentCartItemTopViewHolder.Listener,
    ShipmentCartItemViewHolder.Listener,
    ShipmentCartItemExpandViewHolder.Listener,
    ShipmentCartItemBottomViewHolder.Listener {

    companion object {
        const val DEFAULT_ERROR_POSITION = -1
        const val HEADER_POSITION = 0
        const val SECOND_HEADER_POSITION = 1
        private const val LAST_THREE_DIGIT_MODULUS: Long = 1000
    }

    private val shipmentDataList: ArrayList<Any> = ArrayList()
    private var tickerAnnouncementHolderData: TickerAnnouncementHolderData? = null
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
                return ShipmentCartItemTopViewHolder.LAYOUT
            }

            is ShipmentCartItemModel -> {
                return ShipmentCartItemBottomViewHolder.LAYOUT
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
                return ShipmentRecipientAddressViewHolder(ViewItemShipmentRecipientAddressBinding.bind(view), shipmentAdapterActionListener)
            }

            ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST -> {
                return ShipmentCostViewHolder(ViewItemShipmentCostDetailsBinding.bind(view), layoutInflater)
            }

            ITEM_VIEW_PROMO_CHECKOUT -> {
                return PromoCheckoutViewHolder(ItemPromoCheckoutBinding.bind(view), shipmentAdapterActionListener)
            }

            ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC -> {
                return ShipmentInsuranceTncViewHolder(ItemInsuranceTncBinding.bind(view), shipmentAdapterActionListener)
            }

            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK -> {
                return ShipmentSellerCashbackViewHolder(view, sellerCashbackListener)
            }

            ShipmentDonationViewHolder.ITEM_VIEW_DONATION -> {
                return ShipmentDonationViewHolder(ItemDonationBinding.bind(view), shipmentAdapterActionListener)
            }

            ShipmentCrossSellViewHolder.ITEM_VIEW_CROSS_SELL -> {
                return ShipmentCrossSellViewHolder(ItemCrossSellBinding.bind(view), shipmentAdapterActionListener)
            }

            ShipmentEmasViewHolder.ITEM_VIEW_EMAS -> {
                return ShipmentEmasViewHolder(CheckoutHolderItemEmasBinding.bind(view), shipmentAdapterActionListener)
            }

            ITEM_VIEW_PAYMENT_BUTTON -> {
                return ShipmentButtonPaymentViewHolder(
                    ItemShipmentButtonPaymentBinding.bind(view),
                    shipmentAdapterActionListener,
                    compositeSubscription!!
                )
            }

            LAYOUT -> {
                return ShipmentTickerAnnouncementViewHolder(view, null)
            }

            ITEM_VIEW_TICKER_SHIPPING_COMPLETION -> {
                return ShippingCompletionTickerViewHolder(ItemTickerShippingCompletionBinding.bind(view), shipmentAdapterActionListener)
            }

            ITEM_VIEW_SHIPMENT_TICKER_ERROR -> {
                return ShipmentTickerErrorViewHolder(ItemShipmentTickerErrorBinding.bind(view))
            }

            ITEM_VIEW_UPLOAD -> {
                return UploadPrescriptionViewHolder(view, uploadPrescriptionListener)
            }

            ShipmentUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                return ShipmentUpsellViewHolder(ItemUpsellBinding.bind(view), shipmentAdapterActionListener)
            }

            ShipmentNewUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                return ShipmentNewUpsellViewHolder(view, shipmentAdapterActionListener)
            }

            ShipmentCartItemTopViewHolder.LAYOUT -> {
                return ShipmentCartItemTopViewHolder(view)
            }

            ShipmentCartItemViewHolder.LAYOUT -> {
                return ShipmentCartItemViewHolder(view, this@ShipmentAdapter)
            }

            ShipmentCartItemExpandViewHolder.LAYOUT -> {
                return ShipmentCartItemExpandViewHolder(view, this@ShipmentAdapter)
            }

            ShipmentCartItemBottomViewHolder.LAYOUT -> {
                return ShipmentCartItemBottomViewHolder(
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
                    data as RecipientAddressModel,
                    isShowOnboarding
                )
            }

            ITEM_VIEW_PROMO_CHECKOUT -> {
                (holder as PromoCheckoutViewHolder).bindViewHolder(data as LastApplyUiModel)
            }

            ShipmentCostViewHolder.ITEM_VIEW_SHIPMENT_COST -> {
                (holder as ShipmentCostViewHolder).bindViewHolder(data as ShipmentCostModel)
            }

            ShipmentInsuranceTncViewHolder.ITEM_VIEW_INSURANCE_TNC -> {
                (holder as ShipmentInsuranceTncViewHolder).bindViewHolder(
                    data as ShipmentInsuranceTncModel,
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
                (holder as ShipmentCrossSellViewHolder).bindViewHolder(data as ShipmentCrossSellModel)
            }

            ShipmentEmasViewHolder.ITEM_VIEW_EMAS -> {
                (holder as ShipmentEmasViewHolder).bindViewHolder(egoldAttributeModel!!)
            }

            ITEM_VIEW_PAYMENT_BUTTON -> {
                (holder as ShipmentButtonPaymentViewHolder).bindViewHolder(data as ShipmentButtonPaymentModel)
            }

            LAYOUT -> {
                (holder as TickerAnnouncementViewHolder).bind(data as TickerAnnouncementHolderData)
            }

            ITEM_VIEW_TICKER_SHIPPING_COMPLETION -> {
                (holder as ShippingCompletionTickerViewHolder).bindViewHolder(data as ShippingCompletionTickerModel)
            }

            ITEM_VIEW_SHIPMENT_TICKER_ERROR -> {
                (holder as ShipmentTickerErrorViewHolder).bind(data as ShipmentTickerErrorModel)
            }

            ITEM_VIEW_UPLOAD -> {
                (holder as UploadPrescriptionViewHolder).bindViewHolder(data as UploadPrescriptionUiModel)
            }

            ShipmentUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                (holder as ShipmentUpsellViewHolder).bind(data as ShipmentUpsellModel)
            }

            ShipmentNewUpsellViewHolder.ITEM_VIEW_UPSELL -> {
                (holder as ShipmentNewUpsellViewHolder).bind(data as ShipmentNewUpsellModel)
            }

            ShipmentCartItemTopViewHolder.LAYOUT -> {
                (holder as ShipmentCartItemTopViewHolder).bind((data as ShipmentCartItemTopModel))
            }

            ShipmentCartItemViewHolder.LAYOUT -> {
                (holder as ShipmentCartItemViewHolder).bind(data as CartItemModel)
            }

            ShipmentCartItemExpandViewHolder.LAYOUT -> {
                (holder as ShipmentCartItemExpandViewHolder).bind(data as CartItemExpandModel)
            }

            ShipmentCartItemBottomViewHolder.LAYOUT -> {
                (holder as ShipmentCartItemBottomViewHolder).bind(
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
        (holder as? ShipmentCartItemBottomViewHolder)?.unsubscribeDebouncer()
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

    fun addLastApplyUiDataModel(lastApplyUiModel: LastApplyUiModel) {
        shipmentDataList.add(lastApplyUiModel)
    }

    fun addUploadPrescriptionUiDataModel(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        if (uploadPrescriptionUiModel != null) {
            this.uploadPrescriptionUiModel = uploadPrescriptionUiModel
            shipmentDataList.add(uploadPrescriptionUiModel)
        }
    }

    fun addShipmentCostData(shipmentCostModel: ShipmentCostModel) {
//        if (shipmentCostModel != null) {
        this.shipmentCostModel = shipmentCostModel
        shipmentDataList.add(shipmentCostModel)
//            updateShipmentCostModel()
//        }
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

    fun addShipmentButtonPaymentModel(shipmentButtonPaymentModel: ShipmentButtonPaymentModel) {
//        if (shipmentButtonPaymentModel != null) {
        shipmentDataList.add(shipmentButtonPaymentModel)
//        }
    }

    fun updateShipmentButtonPaymentModel(shipmentButtonPaymentModel: ShipmentButtonPaymentModel): Boolean {
        val item = shipmentDataList.lastOrNull()
        if (item is ShipmentButtonPaymentModel) {
            shipmentDataList.removeLast()
            shipmentDataList.add(shipmentButtonPaymentModel)
            return true
        }
        return false
    }

    fun updateLastApplyUiModel(lastApplyUiModel: LastApplyUiModel): Boolean {
        val item = shipmentDataList.getOrNull(promoCheckoutPosition)
        if (item is LastApplyUiModel) {
            shipmentDataList[promoCheckoutPosition] = lastApplyUiModel
            return true
        }
        return false
    }

    fun updateShipmentCostModel(shipmentCostModel: ShipmentCostModel): Boolean {
        val item = shipmentDataList.getOrNull(shipmentCostPosition)
        if (item is ShipmentCostModel) {
            shipmentDataList[shipmentCostPosition] = shipmentCostModel
            return true
        }
        return false
    }

    fun updateEgold(egoldAttributeModel: EgoldAttributeModel): Int {
        val index = shipmentDataList.indexOfLast { it is EgoldAttributeModel }
        if (index > -1) {
            shipmentDataList[index] = egoldAttributeModel
            return index
        }
        return -1
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
            shipmentAdapterActionListener.updateShipmentCostModel()
//            notifyItemChanged(shipmentCostPosition)
        }
    }

    fun addListShipmentCrossSellModel(shipmentCrossSellModelList: List<ShipmentCrossSellModel>?) {
        if (shipmentCrossSellModelList != null && shipmentCrossSellModelList.isNotEmpty()) {
            this.shipmentCrossSellModelList = shipmentCrossSellModelList
            shipmentDataList.addAll(shipmentCrossSellModelList)
        }
    }

    fun updateCrossSell(checked: Boolean, crossSellModel: CrossSellModel) {
        if (shipmentCrossSellModelList != null && shipmentCrossSellModelList!!.isNotEmpty()) {
            for (shipmentCrossSellModel in shipmentCrossSellModelList!!) {
                if (shipmentCrossSellModel.crossSellModel.id == crossSellModel.id) {
                    shipmentCrossSellModel.isChecked = checked
                    shipmentAdapterActionListener.updateShipmentCostModel()
//                        notifyItemChanged(shipmentCostPosition)
                    break
                }
            }
        }
    }

    fun updateEgold(checked: Boolean) {
        if (egoldAttributeModel != null) {
            egoldAttributeModel!!.isChecked = checked
            shipmentAdapterActionListener.updateShipmentCostModel()
//            notifyItemChanged(shipmentCostPosition)
        }
    }

    fun resetCourier(cartPosition: Int) {
        if (shipmentDataList[cartPosition] is ShipmentCartItemModel) {
            val shipmentCartItemModel = shipmentDataList[cartPosition] as ShipmentCartItemModel?
            if (shipmentCartItemModel!!.selectedShipmentDetailData != null) {
                shipmentCartItemModel.selectedShipmentDetailData = null
                shipmentCartItemModel.voucherLogisticItemUiModel = null
                shipmentAdapterActionListener.updateShipmentCostModel()
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
        shipmentAdapterActionListener.updateShipmentCostModel()
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
        get() = shipmentDataList.indexOfFirst { it is TickerAnnouncementHolderData }

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
                shipmentAdapterActionListener.updateShipmentCostModel()
                checkDataForCheckout()
                break
            }
        }
        if (index > 0) {
//            notifyItemChanged(shipmentCostPosition)
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
            shipmentAdapterActionListener.updateShipmentCostModel()
            checkDataForCheckout()
        }
//        notifyItemChanged(shipmentCostPosition)
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
                if (!passCheckShipmentFromPaymentClick) {
                    shipmentAdapterActionListener.onFinishChoosingShipment(
                        lastSelectedCourierOrderIndex,
                        lastSelectedCourierOrdercartString,
                        forceHitValidateUse,
                        skipValidateUse
                    )
                }
//                shipmentAdapterActionListener.updateCheckoutRequest(requestData.checkoutRequestData)
                return true
            }
        }
        return false
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
            return -1
        }

    fun updateItemAndTotalCost(position: Int) {
        notifyItemChanged(position)
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

    fun getShipmentCartItemModelByIndex(index: Int): ShipmentCartItemModel? {
        return if (shipmentDataList.isNotEmpty() && index >= 0 && index < shipmentDataList.size) {
            if (shipmentDataList[index] is ShipmentCartItemModel) shipmentDataList[index] as ShipmentCartItemModel? else null
        } else {
            null
        }
    }

    fun getShipmentCartItemModelPosition(shipmentCartItemModel: ShipmentCartItemModel): Int {
        return shipmentDataList.indexOf(shipmentCartItemModel)
    }

    private fun getShipmentCartItemTopByCartString(cartString: String): Pair<Int, ShipmentCartItemTopModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is ShipmentCartItemTopModel && data.shipmentCartItemModel.cartString == cartString
        }
        return Pair(index, shipmentDataList[index] as ShipmentCartItemTopModel)
    }

    private fun updateShipmentCartItemTop(shipmentCartItemModel: ShipmentCartItemModel) {
        val (position, data) = getShipmentCartItemTopByCartString(shipmentCartItemModel.cartString)
        val updatedData = data.copy(shipmentCartItemModel = shipmentCartItemModel)
        shipmentDataList[position] = updatedData
        notifyItemChanged(position)
    }

    private fun getFirstCartItemByCartString(cartString: String): Pair<Int, CartItemModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is CartItemModel && data.cartString == cartString && data.cartItemPosition == 0
        }
        return Pair(index, shipmentDataList[index] as CartItemModel)
    }

    private fun updateCartItems(shipmentCartItemModel: ShipmentCartItemModel) {
        val (firstItemPosition, _) = getFirstCartItemByCartString(shipmentCartItemModel.cartString)
        val (expandPosition, expandData) = getCartItemExpandByCartString(shipmentCartItemModel.cartString)

        if (expandPosition != RecyclerView.NO_POSITION && expandData != null) {
            if (expandData.isExpanded) {
                shipmentCartItemModel.cartItemModels.forEachIndexed { index, cartItemModel ->
                    val position = firstItemPosition + index
                    shipmentDataList[position] = cartItemModel
                }
                notifyItemRangeChanged(firstItemPosition, shipmentCartItemModel.cartItemModels.size)
            } else {
                shipmentDataList[firstItemPosition] = shipmentCartItemModel.cartItemModels.first()
                notifyItemChanged(firstItemPosition)
            }
        } else {
            shipmentDataList[firstItemPosition] = shipmentCartItemModel.cartItemModels.first()
            notifyItemChanged(firstItemPosition)
        }
    }

    private fun getFirstErrorCartItemByCartString(cartString: String): Pair<Int, CartItemModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is CartItemModel && data.cartString == cartString && data.isError
        }
        return Pair(index, shipmentDataList[index] as CartItemModel)
    }

    private fun getCartItemExpandByCartString(cartString: String): Pair<Int, CartItemExpandModel?> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is CartItemExpandModel && data.cartString == cartString
        }
        return if (index == RecyclerView.NO_POSITION) {
            Pair(index, null)
        } else {
            Pair(index, shipmentDataList[index] as CartItemExpandModel)
        }
    }

    private fun updateCartItemExpand(shipmentCartItemModel: ShipmentCartItemModel) {
        val (position, data) = getCartItemExpandByCartString(shipmentCartItemModel.cartString)
        if (data != null) {
            shipmentDataList[position] = data.copy(
                isExpanded = shipmentCartItemModel.isStateAllItemViewExpanded,
                cartSize = shipmentCartItemModel.cartItemModels.size
            )
            notifyItemChanged(position)
        }
    }

    fun getShipmentCartItemByCartString(cartString: String): Pair<Int, ShipmentCartItemModel> {
        val index = shipmentDataList.indexOfFirst { data ->
            data is ShipmentCartItemModel && data.cartString == cartString
        }
        return Pair(index, shipmentDataList[index] as ShipmentCartItemModel)
    }

    private fun updateShipmenCartItem(shipmentCartItemModel: ShipmentCartItemModel) {
        val (position, data) = getShipmentCartItemByCartString(shipmentCartItemModel.cartString)
        shipmentDataList[position] = data
        notifyItemChanged(position)
    }

    fun updateShipmentCartItemGroup(shipmentCartItemModel: ShipmentCartItemModel) {
        updateShipmentCartItemTop(shipmentCartItemModel)
        updateCartItems(shipmentCartItemModel)
        updateCartItemExpand(shipmentCartItemModel)
        updateShipmenCartItem(shipmentCartItemModel)
    }

    override fun onViewFreeShippingPlusBadge() {
        shipmentAdapterActionListener.onViewFreeShippingPlusBadge()
    }

    override fun onClickLihatOnTickerOrderError(shopId: String, errorMessage: String) {
        shipmentAdapterActionListener.onClickLihatOnTickerOrderError(shopId, errorMessage)
    }

    override fun onErrorShouldExpandProduct(shipmentCartItemModel: ShipmentCartItemModel) {
        val (position, data) = getCartItemExpandByCartString(shipmentCartItemModel.cartString)
        if (data != null) {
            onClickExpandGroupProduct(position, data)
        }
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
            shipmentCartItem.cartItemModels =
                shipmentCartItem.cartItemModels.toMutableList().apply {
                    set(position, cartItem)
                }
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
        if (position != RecyclerView.NO_POSITION) {
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
    }

    override fun onClickExpandGroupProduct(
        position: Int,
        cartItemExpandModel: CartItemExpandModel
    ) {
        if (position != RecyclerView.NO_POSITION) {
            shipmentDataList[position] = cartItemExpandModel
            notifyItemChanged(position)

            val (_, shipmentCartItem) = getShipmentCartItemByCartString(cartItemExpandModel.cartString)
            val (firstCartItemPosition, _) = getFirstCartItemByCartString(cartItemExpandModel.cartString)
            val newCartItems = shipmentCartItem.cartItemModels.drop(1)
            shipmentDataList.addAll(firstCartItemPosition + 1, newCartItems)
            notifyItemRangeInserted(firstCartItemPosition + 1, newCartItems.size)
        }
    }

    override fun onClickExpandSubtotal(
        position: Int,
        shipmentCartItemModel: ShipmentCartItemModel
    ) {
        shipmentDataList[position] = shipmentCartItemModel
        notifyItemChanged(position)
    }
}
