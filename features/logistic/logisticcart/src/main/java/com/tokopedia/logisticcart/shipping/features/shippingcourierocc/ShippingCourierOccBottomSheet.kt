package com.tokopedia.logisticcart.shipping.features.shippingcourierocc

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierAdapterListener
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_shipping_occ.view.*

class ShippingCourierOccBottomSheet : ShippingCourierAdapterListener, ShippingDurationAdapterListener {

    private lateinit var listener: ShippingCourierOccBottomSheetListener
    private lateinit var bottomSheetUnify: BottomSheetUnify
    private var isNewOccFlow = false

    fun showBottomSheet(fragment: Fragment, isNewOccFlow: Boolean, list: List<RatesViewModelType>, listener: ShippingCourierOccBottomSheetListener) {
        this.isNewOccFlow = isNewOccFlow
        fragment.context?.let { context ->
            fragment.fragmentManager?.let { fm ->
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
                    show(fm, null)
                }
            }
        }
    }

    private fun setupChild(child: View, list: List<RatesViewModelType>) {
        val rvShipping = child.rv_shipping

        rvShipping.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        rvShipping.adapter = ShippingCourierOccAdapter(list, isNewOccFlow, this, this)
    }

    override fun onCourierChoosen(shippingCourierViewModel: ShippingCourierUiModel, cartPosition: Int, isNeedPinpoint: Boolean) {
        listener.onCourierChosen(shippingCourierViewModel)
        bottomSheetUnify.dismiss()
    }

    override fun onShippingDurationChoosen(shippingCourierViewModelList: MutableList<ShippingCourierUiModel>?, cartPosition: Int, serviceData: ServiceData?) {

    }

    override fun isToogleYearEndPromotionOn(): Boolean {
        return false
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        listener.onLogisticPromoClicked(data)
        bottomSheetUnify.dismiss()
    }
}