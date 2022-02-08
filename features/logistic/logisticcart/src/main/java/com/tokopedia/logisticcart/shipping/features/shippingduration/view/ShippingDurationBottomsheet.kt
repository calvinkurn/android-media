package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.DaggerShippingDurationComponent
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.ShippingDurationModule
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingDurationBottomsheet : ShippingDurationContract.View, ShippingDurationAdapterListener {

    private var pbLoading: ProgressBar? = null
    private var llNetworkErrorView: LinearLayout? = null
    private var llContent: LinearLayout? = null
    private var rvDuration: RecyclerView? = null
    private var bundle: Bundle? = null

    private var activity: Activity? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var shippingDurationBottomsheetListener: ShippingDurationBottomsheetListener? = null

    private var chooseCourierTracePerformance: PerformanceMonitoring? = null
    private var isChooseCourierTraceStopped = false

    private var isDisableCourierPromo = false
    private var isDisableOrderPrioritas = false
    private var mCartPosition = -1

    private var mRecipientAddress: RecipientAddressModel? = null

    @JvmField
    @Inject
    var presenter: ShippingDurationContract.Presenter? = null

    @JvmField
    @Inject
    var shippingDurationAdapter: ShippingDurationAdapter? = null

    @JvmField
    @Inject
    var mPromoTracker: CheckoutAnalyticsCourierSelection? = null

    private var mIsCorner = false

    fun show(activity: Activity,
             fragmentManager: FragmentManager,
             shippingDurationBottomsheetListener: ShippingDurationBottomsheetListener?,
             shipmentDetailData: ShipmentDetailData,
             selectedServiceId: Int,
             shopShipmentList: List<ShopShipment>,
             recipientAddressModel: RecipientAddressModel,
             cartPosition: Int, codHistory: Int,
             isLeasing: Boolean, pslCode: String,
             products: ArrayList<Product>, cartString: String,
             isDisableOrderPrioritas: Boolean,
             isTradeInDropOff: Boolean, isFulFillment: Boolean,
             preOrderTime: Int, mvc: String) {
        this.activity = activity
        this.shippingDurationBottomsheetListener = shippingDurationBottomsheetListener
        initData(shipmentDetailData, selectedServiceId, shopShipmentList, recipientAddressModel,
                cartPosition, codHistory, isLeasing, pslCode, products, cartString,
                isDisableOrderPrioritas, isTradeInDropOff, isFulFillment, preOrderTime, mvc)
        initBottomSheet(activity)
        initView(activity)
        bottomSheet?.show(fragmentManager, this.javaClass.simpleName)
    }

    private fun initBottomSheet(activity: Activity) {
        bottomSheet = BottomSheetUnify()
        bottomSheet?.showCloseIcon = true
        bottomSheet?.setTitle(activity.getString(R.string.title_bottomsheet_shipment_duration))
        bottomSheet?.clearContentPadding = true
        bottomSheet?.customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        bottomSheet?.isDragable = true
        bottomSheet?.isHideable = true
        bottomSheet?.setShowListener {
            chooseCourierTracePerformance = PerformanceMonitoring.start(CHOOSE_COURIER_TRACE)
            presenter?.attachView(this)
            loadData()
        }
        bottomSheet?.setOnDismissListener {
            presenter?.detachView()
        }
        bottomSheet?.setCloseClickListener {
            shippingDurationBottomsheetListener?.onShippingDurationButtonCloseClicked()
            bottomSheet?.dismiss()
        }
    }

    private fun initData(shipmentDetailData: ShipmentDetailData, selectedServiceId: Int, shopShipmentList: List<ShopShipment>, recipientAddressModel: RecipientAddressModel, cartPosition: Int, codHistory: Int, isLeasing: Boolean, pslCode: String, products: ArrayList<Product>, cartString: String, isDisableOrderPrioritas: Boolean, isTradeInDropOff: Boolean, isFulFillment: Boolean, preOrderTime: Int, mvc: String) {
        bundle = Bundle()
        bundle?.putParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA, shipmentDetailData)
        bundle?.putParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST, ArrayList(shopShipmentList))
        bundle?.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel)
        bundle?.putInt(ARGUMENT_CART_POSITION, cartPosition)
        bundle?.putInt(ARGUMENT_SELECTED_SERVICE_ID, selectedServiceId)
        bundle?.putInt(ARGUMENT_COD_HISTORY, codHistory)
        bundle?.putBoolean(ARGUMENT_IS_LEASING, isLeasing)
        bundle?.putString(ARGUMENT_PSL_CODE, pslCode)
        bundle?.putParcelableArrayList(ARGUMENT_PRODUCTS, products)
        bundle?.putString(ARGUMENT_CART_STRING, cartString)
        bundle?.putBoolean(ARGUMENT_DISABLE_ORDER_PRIORITAS, isDisableOrderPrioritas)
        bundle?.putBoolean(ARGUMENT_IS_TRADE_IN_DROP_OFF, isTradeInDropOff)
        bundle?.putBoolean(ARGUMENT_IS_FULFILLMENT, isFulFillment)
        bundle?.putInt(ARGUMENT_PO_TIME, preOrderTime)
        bundle?.putString(ARGUMENT_MVC, mvc)
    }

    private fun initializeInjector() {
        val baseMainApplication = activity!!.application as BaseMainApplication
        val component = DaggerShippingDurationComponent.builder()
                .baseAppComponent(baseMainApplication.baseAppComponent)
                .shippingDurationModule(ShippingDurationModule())
                .build()
        component.inject(this)
    }

    @SuppressLint("InflateParams")
    private fun initView(activity: Activity) {
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.fragment_shipment_duration_choice, null)
        pbLoading = view.findViewById(R.id.pb_loading)
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view)
        llContent = view.findViewById(R.id.ll_content)
        rvDuration = view.findViewById(R.id.rv_duration)
        bottomSheet!!.setChild(view)
        initializeInjector()
    }

    private fun loadData() {
        if (bundle != null) {
            mRecipientAddress = bundle!!.getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL)
            mCartPosition = bundle!!.getInt(ARGUMENT_CART_POSITION)
            val selectedServiceId = bundle!!.getInt(ARGUMENT_SELECTED_SERVICE_ID)
            val codHistory = bundle!!.getInt(ARGUMENT_COD_HISTORY)
            if (mRecipientAddress != null) {
                mIsCorner = mRecipientAddress!!.isCornerAddress
            }
            isDisableCourierPromo = bundle!!.getBoolean(ARGUMENT_DISABLE_PROMO_COURIER)
            setupRecyclerView(mCartPosition)
            val shipmentDetailData: ShipmentDetailData = bundle!!.getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA)!!
            val shopShipments: List<ShopShipment> = bundle!!.getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST)!!
            val isLeasing = bundle!!.getBoolean(ARGUMENT_IS_LEASING)
            val pslCode = bundle!!.getString(ARGUMENT_PSL_CODE, "")
            val products: ArrayList<Product> = bundle!!.getParcelableArrayList(ARGUMENT_PRODUCTS)!!
            val cartString = bundle!!.getString(ARGUMENT_CART_STRING)
            isDisableOrderPrioritas = bundle!!.getBoolean(ARGUMENT_DISABLE_ORDER_PRIORITAS)
            val isTradeInDropOff = bundle!!.getBoolean(ARGUMENT_IS_TRADE_IN_DROP_OFF)
            val mvc = bundle!!.getString(ARGUMENT_MVC, "")
            val isFulfillment = bundle!!.getBoolean(ARGUMENT_IS_FULFILLMENT)
            val preOrderTime = bundle!!.getInt(ARGUMENT_PO_TIME)
            presenter!!.loadCourierRecommendation(shipmentDetailData, selectedServiceId,
                    shopShipments, codHistory, mIsCorner, isLeasing, pslCode, products, cartString!!, isTradeInDropOff, mRecipientAddress!!, isFulfillment, preOrderTime, mvc)
        }
    }

    private fun setupRecyclerView(cartPosition: Int) {
        shippingDurationAdapter?.setShippingDurationAdapterListener(this)
        shippingDurationAdapter?.setCartPosition(cartPosition)
        val linearLayoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false)
        rvDuration?.layoutManager = linearLayoutManager
        rvDuration?.adapter = shippingDurationAdapter
    }

    override fun showLoading() {
        llContent?.visibility = View.GONE
        llNetworkErrorView?.visibility = View.GONE
        pbLoading?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading?.visibility = View.GONE
        llNetworkErrorView?.visibility = View.GONE
        llContent?.visibility = View.VISIBLE
    }

    override fun showErrorPage(message: String) {
        pbLoading?.visibility = View.GONE
        llContent?.visibility = View.GONE
        llNetworkErrorView?.visibility = View.VISIBLE
        NetworkErrorHelper.showEmptyState(activity, llNetworkErrorView, message) { loadData() }
    }

    override fun showData(serviceDataList: List<ShippingDurationUiModel>, promoViewModelList: List<LogisticPromoUiModel>, preOrderModel: PreOrderModel?) {
        shippingDurationAdapter?.setShippingDurationViewModels(serviceDataList,
            promoViewModelList, isDisableOrderPrioritas, preOrderModel)
//        todo showcase ?
//        if (promoViewModelList?.etaData != null && promoViewModelList.etaData.textEta.isEmpty() && promoViewModelList.etaData.errorCode == 1) shippingDurationAdapter!!.initiateShowcase()
        if (promoViewModelList.any { it.etaData.textEta.isEmpty() && it.etaData.errorCode == 1 }) shippingDurationAdapter!!.initiateShowcase()

        val hasCourierPromo = checkHasCourierPromo(serviceDataList)
        if (hasCourierPromo) {
            sendAnalyticCourierPromo(serviceDataList)
        }
        promoViewModelList.forEach {
            mPromoTracker?.eventViewPromoLogisticTicker(it.promoCode)
            if (it.disabled) {
                mPromoTracker?.eventViewPromoLogisticTickerDisable(it.promoCode)
            }
        }

    }

    private fun checkHasCourierPromo(shippingDurationUiModelList: List<ShippingDurationUiModel>): Boolean {
        var hasCourierPromo = false
        for (shippingDurationUiModel in shippingDurationUiModelList) {
            if (shippingDurationUiModel.serviceData.isPromo == 1) {
                hasCourierPromo = true
                break
            }
        }
        return hasCourierPromo
    }

    private fun sendAnalyticCourierPromo(shippingDurationUiModelList: List<ShippingDurationUiModel>) {
        for (shippingDurationUiModel in shippingDurationUiModelList) {
            shippingDurationBottomsheetListener?.onShowDurationListWithCourierPromo(
                    shippingDurationUiModel.serviceData.isPromo == 1,
                    shippingDurationUiModel.serviceData.serviceName
            )
        }
    }

    override fun showNoCourierAvailable(message: String?) {
        shippingDurationBottomsheetListener?.onNoCourierAvailable(message)
        bottomSheet?.dismiss()
    }

    override fun stopTrace() {
        if (!isChooseCourierTraceStopped) {
            chooseCourierTracePerformance?.stopTrace()
            isChooseCourierTraceStopped = true
        }
    }

    override fun getActivity(): Activity {
        return activity!!
    }

    override fun isDisableCourierPromo(): Boolean {
        return isDisableCourierPromo
    }

    override fun onShippingDurationChoosen(shippingCourierUiModelList: List<ShippingCourierUiModel>,
                                           cartPosition: Int, serviceData: ServiceData) {
        var flagNeedToSetPinpoint = false
        var selectedServiceId = 0
        if (isToogleYearEndPromotionOn()) {
            if (serviceData.error != null && serviceData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED &&
                    !TextUtils.isEmpty(serviceData.error.errorMessage)) {
                flagNeedToSetPinpoint = true
                selectedServiceId = serviceData.serviceId
            }
        } else {
            for (shippingCourierUiModel in shippingCourierUiModelList) {
                shippingCourierUiModel.isSelected = shippingCourierUiModel.productData.isRecommend
                if (shippingCourierUiModel.productData.error != null && shippingCourierUiModel.productData.error.errorMessage != null && shippingCourierUiModel.productData.error.errorId != null && shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                    flagNeedToSetPinpoint = true
                    selectedServiceId = shippingCourierUiModel.serviceData.serviceId
                    shippingCourierUiModel.serviceData.texts.textRangePrice = shippingCourierUiModel.productData.error.errorMessage
                }
            }
        }
        if (shippingDurationBottomsheetListener != null) {
            try {
                shippingDurationBottomsheetListener?.onShippingDurationChoosen(
                        shippingCourierUiModelList, presenter!!.getCourierItemData(shippingCourierUiModelList),
                        mRecipientAddress, cartPosition, selectedServiceId, serviceData,
                        flagNeedToSetPinpoint, isDurationClick = true, isClearPromo = true)
                bottomSheet?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun isToogleYearEndPromotionOn(): Boolean {
        if (activity != null) {
            val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(activity)
            return remoteConfig.getBoolean("mainapp_enable_year_end_promotion")
        }
        return false
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        mPromoTracker?.eventClickPromoLogisticTicker(data.promoCode)
        // Project Army
        val serviceData = shippingDurationAdapter?.getRatesDataFromLogisticPromo(data.serviceId)
        if (serviceData == null) {
            showErrorPage(activity!!.getString(R.string.logistic_promo_serviceid_mismatch_message))
            return
        }
        val courierData = presenter?.getCourierItemDataById(data.shipperProductId, serviceData.shippingCourierViewModelList)
        if (courierData == null) {
            showErrorPage(activity!!.getString(R.string.logistic_promo_serviceid_mismatch_message))
            return
        }
        courierData.logPromoCode = data.promoCode
        courierData.logPromoMsg = data.disableText
        courierData.discountedRate = data.discountedRate
        courierData.shippingRate = data.shippingRate
        courierData.benefitAmount = data.benefitAmount
        courierData.promoTitle = data.title
        courierData.isHideShipperName = data.hideShipperName
        courierData.shipperName = data.shipperName
        courierData.etaText = data.etaData.textEta
        courierData.etaErrorCode = data.etaData.errorCode
        courierData.freeShippingChosenCourierTitle = data.freeShippingChosenCourierTitle
        try {
            shippingDurationBottomsheetListener?.onLogisticPromoChosen(
                    serviceData.shippingCourierViewModelList, courierData,
                    mRecipientAddress, mCartPosition,
                    serviceData.serviceData, false, data.promoCode, data.serviceId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bottomSheet?.dismiss()
    }

    companion object {
        private const val ARGUMENT_SHIPMENT_DETAIL_DATA = "ARGUMENT_SHIPMENT_DETAIL_DATA"
        private const val ARGUMENT_SHOP_SHIPMENT_LIST = "ARGUMENT_SHOP_SHIPMENT_LIST"
        private const val ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION"
        private const val ARGUMENT_RECIPIENT_ADDRESS_MODEL = "ARGUMENT_RECIPIENT_ADDRESS_MODEL"
        private const val ARGUMENT_SELECTED_SERVICE_ID = "ARGUMENT_SELECTED_SERVICE_ID"
        private const val ARGUMENT_COD_HISTORY = "ARGUMENT_COD_HISTORY"
        private const val ARGUMENT_DISABLE_PROMO_COURIER = "ARGUMENT_DISABLE_PROMO_COURIER"
        private const val ARGUMENT_IS_LEASING = "ARGUMENT_IS_LEASING"
        private const val ARGUMENT_PSL_CODE = "ARGUMENT_PSL_CODE"
        private const val ARGUMENT_PRODUCTS = "ARGUMENT_PRODUCTS"
        private const val ARGUMENT_CART_STRING = "ARGUMENT_CART_STRING"
        private const val ARGUMENT_DISABLE_ORDER_PRIORITAS = "ARGUMENT_DISABLE_ORDER_PRIORITAS"
        private const val ARGUMENT_IS_TRADE_IN_DROP_OFF = "ARGUMENT_IS_TRADE_IN_DROP_OFF"
        private const val ARGUMENT_MVC = "ARGUMENT_MVC"
        private const val ARGUMENT_IS_FULFILLMENT = "ARGUMENT_IS_FULFILLMENT"
        private const val ARGUMENT_PO_TIME = "ARGUMENT_PO_TIME"
        private const val CHOOSE_COURIER_TRACE = "mp_choose_courier"
    }
}