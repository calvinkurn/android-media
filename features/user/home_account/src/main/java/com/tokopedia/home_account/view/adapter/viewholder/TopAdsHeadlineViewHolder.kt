package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.ItemTopadsHeadlineBinding
import com.tokopedia.home_account.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_5
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

class TopAdsHeadlineViewHolder(view: View, private  val shopAdsNewPositionCallback: (Int, CpmModel) -> Unit, private val userSession: UserSessionInterface) :
    BaseViewHolder(view) {

    private val binding: ItemTopadsHeadlineBinding? by viewBinding()

    private var topadsHeadlineUiModel: TopadsHeadlineUiModel? = null

    private fun fetchTopadsHeadlineAds(topadsHeadLinePage: Int) {
        binding?.topadsHeadlineView?.getHeadlineAds(
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
                PARAM_USER_ID to userSession.userId
            )
        )
    }

    private fun onSuccessResponse(cpmModel: CpmModel) {
        topadsHeadlineUiModel?.run {
            this.cpmModel = cpmModel
            showHeadlineView(cpmModel)
            val position = cpmModel.data.getOrNull(Int.ONE)?.cpm?.position
            if (cpmModel.header.totalData > Int.ONE && position == HEADLINE_POSITION) {
                val cpmModelNew = TopAdsHeadlineHelper.getCpmDataOfSameLayout(cpmModel, LAYOUT_5)
                shopAdsNewPositionCallback.invoke(position + Int.ONE, cpmModelNew)
            }
        }
    }

    private fun hideHeadlineView() {
        binding?.topadsHeadlineView?.hideShimmerView()
        binding?.topadsHeadlineView?.hide()
    }

    fun bind(element: TopadsHeadlineUiModel?) {
        topadsHeadlineUiModel = element
        hideHeadlineView()
        topadsHeadlineUiModel?.run {
            if (cpmModel != null) {
                showHeadlineView(cpmModel!!)
            } else {
                fetchTopadsHeadlineAds(topadsHeadlineUiModel?.topadsHeadLinePage ?: 0)
            }
        }
    }

    private fun showHeadlineView(cpmModel: CpmModel) {
        binding?.topadsHeadlineView?.hideShimmerView()
        binding?.topadsHeadlineView?.show()
        binding?.topadsHeadlineView?.displayAds(cpmModel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_headline

        const val TOPADS_HEADLINE_VALUE_SRC = "account"
        const val HEADLINE_POSITION = 13
    }
}