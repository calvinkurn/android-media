package com.tokopedia.logisticcart.shipping.features.shippingcourierocc

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierAdapterListener
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ShippingCourierOccBottomSheet : ShippingCourierAdapterListener, ShippingDurationAdapterListener {

    private lateinit var listener: ShippingCourierOccBottomSheetListener
    private lateinit var bottomSheetUnify: BottomSheetUnify

    fun showBottomSheet(fragment: Fragment, list: List<RatesViewModelType>, listener: ShippingCourierOccBottomSheetListener) {
        fragment.context?.let { context ->
            this.listener = listener
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                clearContentPadding = true
                setTitle(context.getString(R.string.title_shipment_courier_bottomsheet_occ))
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

        rvShipping.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        rvShipping.adapter = ShippingCourierOccAdapter(list, this, this)
    }

    override fun onCourierChoosen(shippingCourierUiModel: ShippingCourierUiModel, cartPosition: Int, isNeedPinpoint: Boolean) {
        listener.onCourierChosen(shippingCourierUiModel)
        bottomSheetUnify.dismiss()
    }

    override fun onShippingDurationChoosen(shippingCourierUiModelList: List<ShippingCourierUiModel>, cartPosition: Int, serviceData: ServiceData) {

    }

    override fun isToogleYearEndPromotionOn(): Boolean {
        return false
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        listener.onLogisticPromoClicked(data)
        bottomSheetUnify.dismiss()
    }
}