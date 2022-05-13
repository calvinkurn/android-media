package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils
import timber.log.Timber

class CourierInfoViewHolder(itemView: View?, private val listener: CourierInfoViewHolderListener) : AbstractViewHolder<ShipmentInfoUiModel.CourierInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_courier
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val ivBuyerOrderDetailFreeShipmentBadge = itemView?.findViewById<ImageUnify>(R.id.ivBuyerOrderDetailFreeShipmentBadge)
    private val tvBuyerOrderDetailCourierValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCourierValue)
    private val tvBuyerOrderDetailArrivalEstimationValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailArrivalEstimationValue)

    private val containerPod = itemView?.findViewById<LinearLayout>(R.id.containerPod)
    private val ivPod = itemView?.findViewById<ImageUnify>(R.id.iv_pod)

    override fun bind(element: ShipmentInfoUiModel.CourierInfoUiModel?) {
        element?.let {
            setupCourierNameAndProductName(it.courierNameAndProductName)
            setupFreeShippingBadge(it.isFreeShipping, it.boBadgeUrl)
            setupArrivalEstimation(it.arrivalEstimation, it.isFreeShipping, it.etaChanged, it.etaUserInfo)

            //TODO IRPAN - remove dummy to real data
            setupPod("https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/8/24/0710dcad-abbf-4de3-a254-d0224d59a963.jpg")
        }
    }

    override fun bind(element: ShipmentInfoUiModel.CourierInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ShipmentInfoUiModel.CourierInfoUiModel && newItem is ShipmentInfoUiModel.CourierInfoUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.courierNameAndProductName != newItem.courierNameAndProductName) {
                        setupCourierNameAndProductName(newItem.courierNameAndProductName)
                    }
                    if (oldItem.isFreeShipping != newItem.isFreeShipping || oldItem.boBadgeUrl != newItem.boBadgeUrl) {
                        setupFreeShippingBadge(newItem.isFreeShipping, newItem.boBadgeUrl)
                    }
                    if (oldItem.arrivalEstimation != newItem.arrivalEstimation || oldItem.isFreeShipping != newItem.isFreeShipping
                        || oldItem.etaChanged != newItem.etaChanged || oldItem.etaUserInfo != newItem.etaUserInfo
                    ) {
                        setupArrivalEstimation(newItem.arrivalEstimation, newItem.isFreeShipping, newItem.etaChanged, newItem.etaUserInfo)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupCourierNameAndProductName(courierNameAndProductName: String) {
        tvBuyerOrderDetailCourierValue?.text = courierNameAndProductName
    }

    private fun setupFreeShippingBadge(freeShipping: Boolean, boBadgeUrl: String) {
        ivBuyerOrderDetailFreeShipmentBadge?.apply {
            ImageHandler.loadImage2(this, boBadgeUrl, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_error)
            showWithCondition(freeShipping)
        }
    }

    private fun setupArrivalEstimation(arrivalEstimation: String, freeShipping: Boolean, etaChanged: Boolean, etaChangedDescription: String) {
        tvBuyerOrderDetailArrivalEstimationValue?.text = arrivalEstimation
        tvBuyerOrderDetailArrivalEstimationValue?.setPadding(0, getArrivalEstimationTopMargin(freeShipping), 0, 0)
        tvBuyerOrderDetailArrivalEstimationValue?.showWithCondition(arrivalEstimation.isNotBlank())
        if (etaChanged) {
            tvBuyerOrderDetailArrivalEstimationValue?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, com.tokopedia.logisticCommon.R.drawable.eta_info, 0)
            tvBuyerOrderDetailArrivalEstimationValue?.setOnClickListener {
                listener.onEtaChangedClicked(etaChangedDescription)
            }
        }
    }

    //proof of delivery view
    private fun setupPod(imagePod: String) {
        val canBeShow = imagePod != "" //TODO update logic showing

        containerPod?.showWithCondition(canBeShow)
        containerPod?.setOnClickListener {

            //TODO - update dummy later
            listener.onPodClicked("kero-21e88f99-771e-4b60-aa40-871877101a24",167075392, "Foto sepenuhnya diambil dan merupakan tanggung jawab kurir. Tokopedia tidak bertanggung jawab atas hasil foto yang terlampir")

        }
        ivPod?.apply {
            ImageUtils.loadImage2(ivPod, imagePod, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_error)
        }
    }

    private fun getArrivalEstimationTopMargin(freeShipping: Boolean): Int {
        return if (freeShipping) 0 else itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).toInt()
    }

    interface CourierInfoViewHolderListener {
        fun onEtaChangedClicked(delayedInfo: String)
        fun onPodClicked(imageId: String, orderId: Long, description: String)
    }
}