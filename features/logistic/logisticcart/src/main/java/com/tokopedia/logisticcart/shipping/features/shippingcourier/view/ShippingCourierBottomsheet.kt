package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.FragmentShipmentCourierChoiceBinding
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.DaggerShippingCourierComponent
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.ShippingCourierModule
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingCourierBottomsheet : ShippingCourierAdapterListener, BottomSheetUnify() {

    companion object {
        private const val INSTAN_VIEW_TYPE = "Instan"
        private const val SAME_DAY_VIEW_TYPE = "Same Day"
        private const val TAG = "Shipping Courier Bottom Sheet"

        fun show(
            fragmentManager: FragmentManager,
            shippingCourierBottomsheetListener: ShippingCourierBottomsheetListener,
            shippingCourierUiModels: List<ShippingCourierUiModel>?,
            recipientAddressModel: RecipientAddressModel?,
            cartPosition: Int,
            isOcc: Boolean
        ) {
            val bottomSheet = ShippingCourierBottomsheet()
            bottomSheet.shippingCourierBottomsheetListener = shippingCourierBottomsheetListener
            bottomSheet.isOcc = isOcc
            bottomSheet.mCourierModelList = shippingCourierUiModels ?: listOf()
            bottomSheet.mRecipientAddress = recipientAddressModel
            bottomSheet.cartPosition = cartPosition
            fragmentManager.apply {
                bottomSheet.show(this, TAG)
            }
        }
    }

    private var shippingCourierBottomsheetListener: ShippingCourierBottomsheetListener? = null

    private var mCourierModelList: List<ShippingCourierUiModel> = ArrayList()
    private var mRecipientAddress: RecipientAddressModel? = null
    private var cartPosition: Int = 0
    private var isOcc: Boolean = false

    private var binding by autoCleared<FragmentShipmentCourierChoiceBinding>()

    @Inject
    lateinit var shippingCourierAdapter: ShippingCourierAdapter

    @Inject
    lateinit var courierConverter: ShippingCourierConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        context?.let {
            setTitle(it.getString(R.string.title_shipment_courier_bottomsheet))
        }
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        isDragable = true
        isHideable = true
        setShowListener {
            setupRecyclerView()
        }
        setCloseClickListener {
            shippingCourierBottomsheetListener?.onCourierShipmentRecommendationCloseClicked()
            dismiss()
        }
        initView()
    }

    private fun initializeInjector() {
        val component = DaggerShippingCourierComponent.builder()
            .shippingCourierModule(ShippingCourierModule())
            .build()
        component.inject(this)
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        binding =
            FragmentShipmentCourierChoiceBinding.inflate(LayoutInflater.from(context), null, false)
        setChild(binding.root)
    }

    private fun setupRecyclerView() {
        shippingCourierAdapter.setShippingCourierAdapterListener(this)
        shippingCourierAdapter.setShippingCourierViewModels(convertCourierListToUiModel(mCourierModelList, mPreOrderModel))
        shippingCourierAdapter.setCartPosition(cartPosition)
        val linearLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvCourier.layoutManager = linearLayoutManager
        binding.rvCourier.adapter = shippingCourierAdapter
    }

    override fun onCourierChoosen(
        shippingCourierUiModel: ShippingCourierUiModel,
        cartPosition: Int,
        isNeedPinpoint: Boolean
    ) {
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
        val isCod = productData.codProductData.isCodAvailable == 1
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
        dismiss()
    }

    private fun showErrorPage(message: String) {
        pbLoading?.visibility = View.GONE
        llContent?.visibility = View.GONE
        llNetworkErrorView?.visibility = View.VISIBLE
        NetworkErrorHelper.showEmptyState(activity, llNetworkErrorView, message) {
            showLoading()
        }
    }

    private fun convertCourierListToUiModel(
        shippingCourierUiModels: List<ShippingCourierUiModel>,
        preOrderModel: PreOrderModel?
    ): MutableList<RatesViewModelType> {
        val eligibleCourierList =
            shippingCourierUiModels.filter { courier -> !courier.productData.isUiRatesHidden }.toMutableList()
        val uiModel: MutableList<RatesViewModelType> = mutableListOf()
        uiModel.addAll(eligibleCourierList)
        eligibleCourierList.getOrNull(0)?.let { firstCourier ->
            setNotifierModel(uiModel, firstCourier)
            firstCourier.productShipmentDetailModel?.let { productShipmentDetailModel ->
                uiModel.add(
                    0,
                    productShipmentDetailModel
                )
            }
        }
        return uiModel
    }

    private fun setNotifierModel(
        uiModel: MutableList<RatesViewModelType>,
        shippingCourierUiModel: ShippingCourierUiModel
    ) {
        val textServiceTicker = shippingCourierUiModel.serviceData.texts.textServiceTicker
        if (textServiceTicker.isNotEmpty()) {
            uiModel.add(0, NotifierModel(textServiceTicker))
        }
    }
}
