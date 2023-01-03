package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.DaggerShippingDurationComponent
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.ShippingDurationModule
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingDurationBottomsheet : ShippingDurationContract.View, ShippingDurationAdapterListener {

    private var pbLoading: LoaderUnify? = null
    private var llNetworkErrorView: LinearLayout? = null
    private var llContent: LinearLayout? = null
    private var rvDuration: RecyclerView? = null
    private var bundle: Bundle? = null

    private var activity: Activity? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var shippingDurationBottomsheetListener: ShippingDurationBottomsheetListener? = null

    private var chooseCourierTracePerformance: PerformanceMonitoring? = null
    private var isChooseCourierTraceStopped = false

    private var isDisableOrderPrioritas = false
    private var isOcc = false
    private var mCartPosition = -1

    private var mRecipientAddress: RecipientAddressModel? = null

    @JvmField
    @Inject
    var presenter: ShippingDurationContract.Presenter? = null

    @JvmField
    @Inject
    var shippingDurationAdapter: ShippingDurationAdapter? = null

    private var mIsCorner = false

    fun show(
        activity: Activity,
        fragmentManager: FragmentManager,
        shippingDurationBottomsheetListener: ShippingDurationBottomsheetListener?,
        shipmentDetailData: ShipmentDetailData,
        selectedServiceId: Int,
        shopShipmentList: List<ShopShipment>,
        recipientAddressModel: RecipientAddressModel? = null,
        cartPosition: Int, codHistory: Int = -1,
        isLeasing: Boolean = false, pslCode: String = "",
        products: ArrayList<Product>, cartString: String,
        isDisableOrderPrioritas: Boolean,
        isTradeInDropOff: Boolean = false,
        isFulFillment: Boolean = false, preOrderTime: Int = -1,
        mvc: String = "", cartData: String, isOcc: Boolean
    ) {
        this.activity = activity
        this.shippingDurationBottomsheetListener = shippingDurationBottomsheetListener
        initData(
            shipmentDetailData, selectedServiceId, shopShipmentList, recipientAddressModel,
            cartPosition, codHistory, isLeasing, pslCode, products, cartString,
            isDisableOrderPrioritas, isTradeInDropOff, isFulFillment, preOrderTime, mvc, cartData
        )
        initBottomSheet(activity)
        initView(activity)
        this.isOcc = isOcc
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

    private fun initData(
        shipmentDetailData: ShipmentDetailData,
        selectedServiceId: Int,
        shopShipmentList: List<ShopShipment>,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        codHistory: Int,
        isLeasing: Boolean,
        pslCode: String,
        products: ArrayList<Product>,
        cartString: String,
        isDisableOrderPrioritas: Boolean,
        isTradeInDropOff: Boolean,
        isFulFillment: Boolean,
        preOrderTime: Int,
        mvc: String,
        cartData: String
    ) {
        bundle = Bundle().apply {
            putParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA, shipmentDetailData)
            putParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST, ArrayList(shopShipmentList))
            putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel)
            putInt(ARGUMENT_CART_POSITION, cartPosition)
            putInt(ARGUMENT_SELECTED_SERVICE_ID, selectedServiceId)
            putInt(ARGUMENT_COD_HISTORY, codHistory)
            putBoolean(ARGUMENT_IS_LEASING, isLeasing)
            putString(ARGUMENT_PSL_CODE, pslCode)
            putParcelableArrayList(ARGUMENT_PRODUCTS, products)
            putString(ARGUMENT_CART_STRING, cartString)
            putBoolean(ARGUMENT_DISABLE_ORDER_PRIORITAS, isDisableOrderPrioritas)
            putBoolean(ARGUMENT_IS_TRADE_IN_DROP_OFF, isTradeInDropOff)
            putBoolean(ARGUMENT_IS_FULFILLMENT, isFulFillment)
            putInt(ARGUMENT_PO_TIME, preOrderTime)
            putString(ARGUMENT_MVC, mvc)
            putString(ARGUMENT_CART_DATA, cartData)
        }
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
        bundle?.let {
            mRecipientAddress = it.getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL)
            mCartPosition = it.getInt(ARGUMENT_CART_POSITION)
            val selectedServiceId = it.getInt(ARGUMENT_SELECTED_SERVICE_ID)
            val codHistory = it.getInt(ARGUMENT_COD_HISTORY)
            mRecipientAddress?.let { recipientAddressModel ->
                mIsCorner = recipientAddressModel.isCornerAddress
            }
            val isDisableCourierPromo = it.getBoolean(ARGUMENT_DISABLE_PROMO_COURIER)
            setupRecyclerView(mCartPosition)
            val shipmentDetailData: ShipmentDetailData =
                it.getParcelable(ARGUMENT_SHIPMENT_DETAIL_DATA)!!
            val shopShipments: List<ShopShipment> =
                it.getParcelableArrayList(ARGUMENT_SHOP_SHIPMENT_LIST)!!
            val isLeasing = it.getBoolean(ARGUMENT_IS_LEASING)
            val pslCode = it.getString(ARGUMENT_PSL_CODE, "")
            val products: ArrayList<Product> = it.getParcelableArrayList(ARGUMENT_PRODUCTS)!!
            val cartString = it.getString(ARGUMENT_CART_STRING, "")
            isDisableOrderPrioritas = it.getBoolean(ARGUMENT_DISABLE_ORDER_PRIORITAS)
            val isTradeInDropOff = it.getBoolean(ARGUMENT_IS_TRADE_IN_DROP_OFF)
            val mvc = it.getString(ARGUMENT_MVC, "")
            val isFulfillment = it.getBoolean(ARGUMENT_IS_FULFILLMENT)
            val preOrderTime = it.getInt(ARGUMENT_PO_TIME)
            val cartData = it.getString(ARGUMENT_CART_DATA, "")

            presenter?.loadCourierRecommendation(
                shipmentDetailData = shipmentDetailData,
                selectedServiceId = selectedServiceId,
                shopShipmentList = shopShipments,
                codHistory = codHistory,
                isCorner = mIsCorner,
                isLeasing = isLeasing,
                pslCode = pslCode,
                products = products,
                cartString = cartString,
                isTradeInDropOff = isTradeInDropOff,
                recipientAddressModel = mRecipientAddress,
                isFulfillment = isFulfillment,
                preOrderTime = preOrderTime,
                mvc = mvc,
                cartData = cartData,
                isOcc = isOcc,
                isDisableCourierPromo = isDisableCourierPromo
            )
        }
    }

    private fun setupRecyclerView(cartPosition: Int) {
        shippingDurationAdapter?.setShippingDurationAdapterListener(this)
        shippingDurationAdapter?.setCartPosition(cartPosition)
        shippingDurationAdapter?.setToggleYearPromotion(isToogleYearEndPromotionOn())
        val linearLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvDuration?.layoutManager = linearLayoutManager
        rvDuration?.adapter = shippingDurationAdapter
    }


    /*
    Section: Shipping Duration View
    */

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

    override fun showData(uiModelList: MutableList<RatesViewModelType>) {
        shippingDurationAdapter?.setShippingDurationViewModels(uiModelList, isDisableOrderPrioritas)
    }

    override fun sendAnalyticCourierPromo(shippingDurationUiModelList: List<ShippingDurationUiModel>) {
        for (shippingDurationUiModel in shippingDurationUiModelList) {
            shippingDurationBottomsheetListener?.onShowDurationListWithCourierPromo(
                shippingDurationUiModel.serviceData.isPromo == 1,
                shippingDurationUiModel.serviceData.serviceName
            )
        }
    }

    override fun sendAnalyticPromoLogistic(promoViewModelList: List<LogisticPromoUiModel>) {
        shippingDurationBottomsheetListener?.onShowLogisticPromo(promoViewModelList)
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

    override fun isToogleYearEndPromotionOn(): Boolean {
        if (isOcc) {
            return false
        } else {
            if (activity != null) {
                val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(activity)
                return remoteConfig.getBoolean("mainapp_enable_year_end_promotion")
            }
            return false
        }
    }

    override fun onShippingDurationAndRecommendCourierChosen(
        shippingCourierUiModelList: List<ShippingCourierUiModel>,
        courierData: ShippingCourierUiModel?,
        cartPosition: Int,
        selectedServiceId: Int,
        serviceData: ServiceData,
        flagNeedToSetPinpoint: Boolean
    ) {
        shippingDurationBottomsheetListener?.let {
            try {
                it.onShippingDurationChoosen(
                    shippingCourierUiModelList,
                    courierData,
                    mRecipientAddress, cartPosition, selectedServiceId, serviceData,
                    flagNeedToSetPinpoint, isDurationClick = true, isClearPromo = true
                )
                bottomSheet?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onLogisticPromoChosen(
        shippingCourierViewModelList: List<ShippingCourierUiModel>,
        courierData: ShippingCourierUiModel,
        serviceData: ServiceData,
        needToSetPinpoint: Boolean,
        promoCode: String,
        serviceId: Int,
        data: LogisticPromoUiModel
    ) {
        try {
            shippingDurationBottomsheetListener?.onLogisticPromoChosen(
                shippingCourierViewModelList, courierData,
                mRecipientAddress, mCartPosition,
                serviceData, false, promoCode, serviceId, data
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bottomSheet?.dismiss()
    }

    override fun showPromoCourierNotAvailable() {
        activity?.let {
            showErrorPage(it.getString(R.string.logistic_promo_serviceid_mismatch_message))
        }
    }

    /*
    Section: Adapter Listener
    */

    override fun onShippingDurationChoosen(
        shippingCourierUiModelList: List<ShippingCourierUiModel>,
        cartPosition: Int, serviceData: ServiceData
    ) {
        presenter?.onChooseDuration(shippingCourierUiModelList, cartPosition, serviceData)
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        presenter?.onLogisticPromoClicked(data)
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
        private const val ARGUMENT_CART_DATA = "ARGUMENT_CART_DATA"
        private const val ARGUMENT_IS_FULFILLMENT = "ARGUMENT_IS_FULFILLMENT"
        private const val ARGUMENT_PO_TIME = "ARGUMENT_PO_TIME"
        private const val CHOOSE_COURIER_TRACE = "mp_choose_courier"
    }
}
