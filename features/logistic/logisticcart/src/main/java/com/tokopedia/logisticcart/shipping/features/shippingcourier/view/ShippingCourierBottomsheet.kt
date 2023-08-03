package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.DaggerShippingCourierComponent
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.ShippingCourierModule
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.ProductShipmentDetailModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingCourierBottomsheet : ShippingCourierContract.View, ShippingCourierAdapterListener {

    private var llContent: LinearLayout? = null
    private var rvCourier: RecyclerView? = null
    private var llNetworkErrorView: LinearLayout? = null
    private var pbLoading: LoaderUnify? = null

    private var activity: Activity? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var bundle: Bundle? = null
    private var shippingCourierBottomsheetListener: ShippingCourierBottomsheetListener? = null

    private var mCourierModelList: List<ShippingCourierUiModel> = ArrayList()
    private var productShipmentDetailModel: ProductShipmentDetailModel? = null
    private var mRecipientAddress: RecipientAddressModel? = null
    private var isOcc: Boolean = false

    @Inject
    lateinit var shippingCourierAdapter: ShippingCourierAdapter

    @Inject
    lateinit var courierConverter: ShippingCourierConverter

    fun show(
        activity: Activity,
        fragmentManager: FragmentManager,
        shippingCourierBottomsheetListener: ShippingCourierBottomsheetListener,
        shippingCourierUiModels: List<ShippingCourierUiModel>?,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        isOcc: Boolean,
        weight: Double? = null,
        shopCity: String? = null
    ) {
        this.activity = activity
        this.shippingCourierBottomsheetListener = shippingCourierBottomsheetListener
        this.isOcc = isOcc
        initData(shippingCourierUiModels, recipientAddressModel, cartPosition)
        initBottomSheet(activity)
        initView(activity)
        bottomSheet?.show(fragmentManager, this.javaClass.simpleName)
    }

    private fun initData(shippingCourierUiModels: List<ShippingCourierUiModel>?, recipientAddressModel: RecipientAddressModel?, cartPosition: Int) {
        bundle = Bundle()
        if (shippingCourierUiModels != null) {
            bundle?.putParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST, ArrayList(shippingCourierUiModels))
            bundle?.putParcelable(ARGUMENT_PRODUCT_SHIPMENT_DETAIL, shippingCourierUiModels[0].productShipmentDetailModel)
        }
        bundle?.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel)
        bundle?.putInt(ARGUMENT_CART_POSITION, cartPosition)
    }

    private fun initBottomSheet(activity: Activity) {
        bottomSheet = BottomSheetUnify()
        bottomSheet?.showCloseIcon = true
        bottomSheet?.setTitle(activity.getString(R.string.title_shipment_courier_bottomsheet))
        bottomSheet?.clearContentPadding = true
        bottomSheet?.customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        bottomSheet?.isDragable = true
        bottomSheet?.isHideable = true
        bottomSheet?.setCloseClickListener {
            shippingCourierBottomsheetListener?.onCourierShipmentRecommendationCloseClicked()
            bottomSheet?.dismiss()
        }
        bottomSheet?.setOnDismissListener { bottomSheet = null }
    }

    private fun initializeInjector() {
        val component = DaggerShippingCourierComponent.builder()
            .shippingCourierModule(ShippingCourierModule())
            .build()
        component.inject(this)
    }

    @SuppressLint("InflateParams")
    private fun initView(activity: Activity) {
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.fragment_shipment_courier_choice, null)
        llContent = view.findViewById(R.id.ll_content)
        rvCourier = view.findViewById(R.id.rv_courier)
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view)
        pbLoading = view.findViewById(R.id.pb_loading)
        bottomSheet?.setChild(view)
        initializeInjector()
        loadData()
    }

    private fun loadData() {
        bundle?.let {
            mRecipientAddress = it.getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL)
            val cartPosition = it.getInt(ARGUMENT_CART_POSITION)
            val shippingCourierUiModels: ArrayList<ShippingCourierUiModel>? = it.getParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST)
            productShipmentDetailModel = it.getParcelable(ARGUMENT_PRODUCT_SHIPMENT_DETAIL)
            if (shippingCourierUiModels != null) {
                mCourierModelList = shippingCourierUiModels
                setupRecyclerView(cartPosition)
            } else if (activity != null) {
                showErrorPage(activity!!.getString(R.string.message_error_shipping_general))
            }
        }
    }

    private fun setupRecyclerView(cartPosition: Int) {
        shippingCourierAdapter.setShippingCourierAdapterListener(this)
        shippingCourierAdapter.setShippingCourierViewModels(convertCourierListToUiModel(mCourierModelList, productShipmentDetailModel, isOcc))
        shippingCourierAdapter.setCartPosition(cartPosition)
        val linearLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvCourier?.layoutManager = linearLayoutManager
        rvCourier?.adapter = shippingCourierAdapter
    }

    override fun onCourierChoosen(shippingCourierUiModel: ShippingCourierUiModel, cartPosition: Int, isNeedPinpoint: Boolean) {
        val productData = shippingCourierUiModel.productData
        val spId = shippingCourierUiModel.productData.shipperProductId
        if (!isOcc) {
            if (shippingCourierUiModel.productData.error != null) {
                // Not updating when it has Error Pinpoint Needed
                if (shippingCourierUiModel.productData.error.errorId != ErrorProductData.ERROR_PINPOINT_NEEDED) {
                    courierConverter.updateSelectedCourier(mCourierModelList, spId)
                }
            } else {
                courierConverter.updateSelectedCourier(mCourierModelList, spId)
            }
        }
        val courierItemData = courierConverter.convertToCourierItemData(shippingCourierUiModel)
        val isCod = productData.codProductData != null && productData.codProductData.isCodAvailable == 1
        shippingCourierBottomsheetListener?.onCourierChoosen(
            shippingCourierUiModel,
            courierItemData,
            mRecipientAddress,
            cartPosition,
            isCod,
            !TextUtils.isEmpty(productData.promoCode),
            isNeedPinpoint,
            mCourierModelList
        )
        bottomSheet?.dismiss()
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

    private fun showErrorPage(message: String) {
        pbLoading?.visibility = View.GONE
        llContent?.visibility = View.GONE
        llNetworkErrorView?.visibility = View.VISIBLE
        NetworkErrorHelper.showEmptyState(activity, llNetworkErrorView, message) {
            showLoading()
        }
    }

    private fun convertCourierListToUiModel(shippingCourierUiModels: List<ShippingCourierUiModel>, productShipmentDetailModel: ProductShipmentDetailModel?, isOcc: Boolean): MutableList<RatesViewModelType> {
        val eligibleCourierList = shippingCourierUiModels.filter { courier -> !courier.productData.isUiRatesHidden }.toMutableList()
        val uiModel: MutableList<RatesViewModelType> = mutableListOf()
        uiModel.addAll(eligibleCourierList)
        eligibleCourierList.getOrNull(0)?.let {
                firstCourier ->
            setNotifierModel(uiModel, firstCourier, isOcc)
        }

        productShipmentDetailModel?.run { uiModel.add(0, this)}
        return uiModel
    }

    private fun setNotifierModel(
        uiModel: MutableList<RatesViewModelType>,
        shippingCourierUiModel: ShippingCourierUiModel,
        isOcc: Boolean
    ) {
        if (isOcc && shippingCourierUiModel.productData.shipperId in CourierConstant.INSTANT_SAMEDAY_COURIER) {
            uiModel.add(0, NotifierModel(NotifierModel.TYPE_DEFAULT))
        } else if (shippingCourierUiModel.serviceData.serviceName == INSTAN_VIEW_TYPE) {
            uiModel.add(0, NotifierModel(NotifierModel.TYPE_INSTAN))
        } else if (shippingCourierUiModel.serviceData.serviceName == SAME_DAY_VIEW_TYPE) {
            uiModel.add(0, NotifierModel(NotifierModel.TYPE_SAMEDAY))
        }
    }

    companion object {
        private const val INSTAN_VIEW_TYPE = "Instan"
        private const val SAME_DAY_VIEW_TYPE = "Same Day"
        const val ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST = "ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST"
        const val ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION"
        const val ARGUMENT_RECIPIENT_ADDRESS_MODEL = "ARGUMENT_RECIPIENT_ADDRESS_MODEL"
        const val ARGUMENT_PRODUCT_SHIPMENT_DETAIL = "ARGUMENT_PRODUCT_SHIPMENT_DETAIL"
    }
}
