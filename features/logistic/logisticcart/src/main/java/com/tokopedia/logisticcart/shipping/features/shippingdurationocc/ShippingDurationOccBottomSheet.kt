package com.tokopedia.logisticcart.shipping.features.shippingdurationocc

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_shipping_occ.view.*

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
        val rvShipping = child.rv_shipping
        val mutableList = list.toMutableList()
        mutableList.add(0, NotifierModel())

        rvShipping.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        rvShipping.adapter = ShippingDurationOccAdapter(mutableList, this)
    }

    override fun onShippingDurationChoosen(shippingCourierViewModelList: MutableList<ShippingCourierUiModel>, cartPosition: Int, serviceData: ServiceData) {
        var flagNeedToSetPinpoint = false
        var selectedServiceId = 0
        var selectedShippingCourierUiModel = shippingCourierViewModelList[0]
        for (shippingCourierUiModel in shippingCourierViewModelList) {
            val recommend = shippingCourierUiModel.productData.isRecommend
            if (recommend) {
                selectedShippingCourierUiModel = shippingCourierUiModel
            }
            shippingCourierUiModel.isSelected = recommend
            selectedServiceId = shippingCourierUiModel.serviceData.serviceId
            if (shippingCourierUiModel.productData.error != null && shippingCourierUiModel.productData.error.errorMessage != null && shippingCourierUiModel.productData.error.errorId != null && shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                flagNeedToSetPinpoint = true
            }
        }
        listener.onDurationChosen(serviceData, selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
        bottomSheetUnify.dismiss()
    }

    override fun isToogleYearEndPromotionOn(): Boolean {
        return false
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        listener.onLogisticPromoClicked(data)
        bottomSheetUnify.dismiss()
    }
}