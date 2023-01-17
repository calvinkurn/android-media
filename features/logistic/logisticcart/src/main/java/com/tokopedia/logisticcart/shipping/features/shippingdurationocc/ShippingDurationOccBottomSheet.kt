package com.tokopedia.logisticcart.shipping.features.shippingdurationocc

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ShippingDurationOccBottomSheet : ShippingDurationAdapterListener {

    private lateinit var bottomSheetUnify: BottomSheetUnify
    private lateinit var listener: ShippingDurationOccBottomSheetListener

    fun showBottomSheet(fragment: Fragment, list: List<RatesViewModelType>, listener: ShippingDurationOccBottomSheetListener) {
        fragment.context?.let { context ->
            this.listener = listener
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                clearContentPadding = true
                setTitle(context.getString(R.string.title_bottomsheet_shipment))
                val child = View.inflate(context, R.layout.bottomsheet_shipping_occ, null)
                setupChild(child, list)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(fragment.parentFragmentManager, null)
            }
        }
    }

    private fun setupChild(child: View, list: List<RatesViewModelType>) {
        val rvShipping: RecyclerView = child.findViewById(R.id.rv_shipping)
        val mutableList = list.filterNot { item -> item is ShippingDurationUiModel && item.serviceData.isUiRatesHidden }.toMutableList()
        mutableList.add(0, NotifierModel())

        rvShipping.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        rvShipping.adapter = ShippingDurationOccAdapter(mutableList, this)
    }

    override fun onShippingDurationChoosen(
        shippingCourierUiModelList: List<ShippingCourierUiModel>,
        cartPosition: Int,
        serviceData: ServiceData
    ) {
        val selectedShippingCourierUiModel =
            findSelectedCourier(serviceData, shippingCourierUiModelList)
        var flagNeedToSetPinpoint = false
        shippingCourierUiModelList.forEach { shippingCourierUiModel ->
            shippingCourierUiModel.isSelected =
                shippingCourierUiModel == selectedShippingCourierUiModel
            if (shippingCourierUiModel.productData.error != null &&
                shippingCourierUiModel.productData.error.errorMessage != null &&
                shippingCourierUiModel.productData.error.errorId != null &&
                shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED
            ) {
                flagNeedToSetPinpoint = true
            }
        }
        val selectedServiceId = selectedShippingCourierUiModel.serviceData.serviceId
        listener.onDurationChosen(
            serviceData,
            selectedServiceId,
            selectedShippingCourierUiModel,
            flagNeedToSetPinpoint
        )
        bottomSheetUnify.dismiss()
    }

    private fun findSelectedCourier(
        serviceData: ServiceData,
        shippingCourierUiModelList: List<ShippingCourierUiModel>
    ): ShippingCourierUiModel {
        return shippingCourierUiModelList.takeIf { serviceData.selectedShipperProductId > 0 }
            ?.find { shippingCourierUiModel -> shippingCourierUiModel.productData.shipperProductId == serviceData.selectedShipperProductId }
            ?: shippingCourierUiModelList.firstOrNull { it.productData.isRecommend && !it.productData.isUiRatesHidden }
            ?: shippingCourierUiModelList.firstOrNull { !it.productData.isUiRatesHidden && (it.productData.error?.errorMessage?.isEmpty() != false) }
            ?: shippingCourierUiModelList.first()
    }

    override fun isToogleYearEndPromotionOn(): Boolean {
        return false
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        listener.onLogisticPromoClicked(data)
        bottomSheetUnify.dismiss()
    }
}
