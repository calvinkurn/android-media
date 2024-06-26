package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.TopadsHeadlineUiModel
import com.tokopedia.product.detail.databinding.PdpItemTopadsHeadlineInboxBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.LAYOUT_5
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.LAYOUT_6
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.PARAM_DEVICE
import com.tokopedia.topads.sdk.utils.PARAM_EP
import com.tokopedia.topads.sdk.utils.PARAM_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.PARAM_ITEM
import com.tokopedia.topads.sdk.utils.PARAM_PAGE
import com.tokopedia.topads.sdk.utils.PARAM_SRC
import com.tokopedia.topads.sdk.utils.PARAM_TEMPLATE_ID
import com.tokopedia.topads.sdk.utils.PARAM_USER_ID
import com.tokopedia.topads.sdk.utils.UrlParamHelper
import com.tokopedia.topads.sdk.utils.VALUE_DEVICE
import com.tokopedia.topads.sdk.utils.VALUE_EP
import com.tokopedia.topads.sdk.utils.VALUE_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.VALUE_ITEM
import com.tokopedia.topads.sdk.utils.VALUE_TEMPLATE_ID

const val TOPADS_HEADLINE_VALUE_SRC = "pdp"
const val PRODUCT_ID = "product_id"

class TopAdsHeadlineViewHolder(
    val view: View,
    val userId: String,
    private val listener: ProductDetailListener
) : AbstractViewHolder<TopadsHeadlineUiModel>(view) {

    private val binding = PdpItemTopadsHeadlineInboxBinding.bind(view)

    private var topadsHeadlineUiModel: TopadsHeadlineUiModel? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.pdp_item_topads_headline_inbox
    }

    private fun fetchTopadsHeadlineAds(topadsHeadLinePage: Int) {
        binding.topadsHeadlineView.getHeadlineAds(
            getHeadlineAdsParam(topadsHeadLinePage),
            this::onSuccessResponse,
            this::hideHeadlineView
        )
    }

    private fun getHeadlineAdsParam(topadsHeadLinePage: Int): String {
        return UrlParamHelper.generateUrlParamString(
            mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to topadsHeadLinePage,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to TOPADS_HEADLINE_VALUE_SRC,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userId,
                PRODUCT_ID to topadsHeadlineUiModel?.productId
            )
        )
    }

    private fun onSuccessResponse(cpmModel: CpmModel) {
        topadsHeadlineUiModel?.run {
            this.cpmModel = cpmModel
            showHeadlineView(cpmModel)
        }
    }

    private fun hideHeadlineView() {
        binding.topadsHeadlineView.hideShimmerView()
        binding.divider.hide()
        binding.titleHeadline.hide()
        binding.topadsHeadlineView.hide()
    }

    override fun bind(element: TopadsHeadlineUiModel?) {
        topadsHeadlineUiModel = element
        hideHeadlineView()
        topadsHeadlineUiModel?.apply {
            if (cpmModel != null) {
                showHeadlineView(cpmModel!!)
            } else if (!isHeadlineDataFetched) {
                isHeadlineDataFetched = true
                fetchTopadsHeadlineAds(topadsHeadlineUiModel?.topadsHeadLinePage ?: 0)
            }
        }
    }

    private fun showHeadlineView(cpmModel: CpmModel) {
        binding.topadsHeadlineView.hideShimmerView()
        binding.divider.show()
        binding.topadsHeadlineView.show()
        binding.topadsHeadlineView.displayAds(cpmModel)
        topadsHeadlineUiModel?.impressHolder?.let {
            view.addOnImpressionListener(
                holder = it,
                holders = listener.getImpressionHolders(),
                name = cpmModel.data.hashCode().toString(),
                useHolders = listener.isRemoteCacheableActive()
            ) {
                topadsHeadlineUiModel?.let { element ->
                    listener.onImpressComponent(getComponentTrackData(element))
                }
            }
        }
        handlingHeadlineTitle(cpmModel.data)
    }

    private fun handlingHeadlineTitle(data: List<CpmData>?) {
        data?.let {
            val cpmLayout = it.firstOrNull()?.cpm?.layout
            val hideTitleHeadline = cpmLayout != null && (cpmLayout == LAYOUT_6 || cpmLayout == LAYOUT_5)
            binding.titleHeadline.showWithCondition(hideTitleHeadline.not())
        }
    }

    private fun getComponentTrackData(
        element: TopadsHeadlineUiModel
    ) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}
