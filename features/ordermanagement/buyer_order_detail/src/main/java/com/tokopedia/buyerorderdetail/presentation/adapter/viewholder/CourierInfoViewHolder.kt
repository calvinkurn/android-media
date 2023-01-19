package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailShipmentInfoCourierBinding
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.loadImagePod
import com.tokopedia.utils.view.binding.viewBinding

class CourierInfoViewHolder(
    itemView: View?,
    private val listener: CourierInfoViewHolderListener,
    private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<ShipmentInfoUiModel.CourierInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_courier
    }

    private val binding by viewBinding<ItemBuyerOrderDetailShipmentInfoCourierBinding>()

    override fun bind(element: ShipmentInfoUiModel.CourierInfoUiModel?) {
        element?.let {
            setupCourierNameAndProductName(it.courierNameAndProductName)
            setupFreeShippingBadge(it.isFreeShipping, it.boBadgeUrl)
            setupArrivalEstimation(it.arrivalEstimation, it.isFreeShipping, it.etaChanged, it.etaUserInfo)
            setupPod(it.pod)
        }
    }

    override fun bind(element: ShipmentInfoUiModel.CourierInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ShipmentInfoUiModel.CourierInfoUiModel && newItem is ShipmentInfoUiModel.CourierInfoUiModel) {
                    binding?.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.courierNameAndProductName != newItem.courierNameAndProductName) {
                        setupCourierNameAndProductName(newItem.courierNameAndProductName)
                    }
                    if (oldItem.isFreeShipping != newItem.isFreeShipping || oldItem.boBadgeUrl != newItem.boBadgeUrl) {
                        setupFreeShippingBadge(newItem.isFreeShipping, newItem.boBadgeUrl)
                    }
                    if (oldItem.arrivalEstimation != newItem.arrivalEstimation || oldItem.isFreeShipping != newItem.isFreeShipping
                        || oldItem.etaChanged != newItem.etaChanged || oldItem.etaUserInfo != newItem.etaUserInfo) {
                        setupArrivalEstimation(newItem.arrivalEstimation, newItem.isFreeShipping, newItem.etaChanged, newItem.etaUserInfo)
                    }
                    if (oldItem.pod != newItem.pod) {
                        setupPod(newItem.pod)
                    }
                    binding?.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupCourierNameAndProductName(courierNameAndProductName: String) {
        binding?.tvBuyerOrderDetailCourierValue?.text = courierNameAndProductName
    }

    private fun setupFreeShippingBadge(freeShipping: Boolean, boBadgeUrl: String) {
        binding?.ivBuyerOrderDetailFreeShipmentBadge?.apply {
            ImageHandler.loadImage2(this, boBadgeUrl, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_error)
            showWithCondition(freeShipping)
        }
    }

    private fun setupArrivalEstimation(arrivalEstimation: String, freeShipping: Boolean, etaChanged: Boolean, etaChangedDescription: String) {
        binding?.tvBuyerOrderDetailArrivalEstimationValue?.text = arrivalEstimation
        binding?.tvBuyerOrderDetailArrivalEstimationValue?.setPadding(0, getArrivalEstimationTopMargin(freeShipping), 0, 0)
        binding?.tvBuyerOrderDetailArrivalEstimationValue?.showWithCondition(arrivalEstimation.isNotBlank())
        if (etaChanged) {
            binding?.tvBuyerOrderDetailArrivalEstimationValue?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, com.tokopedia.logisticCommon.R.drawable.eta_info, 0)
            binding?.tvBuyerOrderDetailArrivalEstimationValue?.setOnClickListener {
                listener.onEtaChangedClicked(etaChangedDescription)
            }
        }
    }

    private fun setupPod(pod: ShipmentInfoUiModel.CourierInfoUiModel.Pod?) = binding?.run {
        if (pod == null) {
            layoutBuyerOrderDetailPod.gone()
        } else {
            setupPodImage(pod)
            tvBuyerOrderDetailPodLabel.text = pod.podLabel
            tvBuyerOrderDetailPodCta.text = pod.podCtaText
            contentBuyerOrderDetailPod.setOnClickListener {
                listener.onClickPodPreview()
                navigator.openAppLink(appLink = pod.podCtaUrl, shouldRefreshWhenBack = false)
            }
            layoutBuyerOrderDetailPod.show()
        }
    }

    private fun getArrivalEstimationTopMargin(freeShipping: Boolean): Int {
        return if (freeShipping) 0 else itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).toInt()
    }

    private fun setupPodImage(pod: ShipmentInfoUiModel.CourierInfoUiModel.Pod) = binding?.run {
        ivBuyerOrderDetailPodPicture.loadImagePod(
            context = root.context,
            accessToken = pod.accessToken,
            url = pod.podPictureUrl,
            drawableImagePlaceholder = ContextCompat.getDrawable(
                root.context,
                com.tokopedia.unifycomponents.R.drawable.imagestate_placeholder
            ),
            drawableImageError = ContextCompat.getDrawable(
                root.context,
                com.tokopedia.unifycomponents.R.drawable.imagestate_error
            )
        )
    }

    interface CourierInfoViewHolderListener {
        fun onEtaChangedClicked(delayedInfo: String)
        fun onClickPodPreview()
    }
}
