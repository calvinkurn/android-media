package com.tokopedia.home_account.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.*
import com.tokopedia.home_account.R
import com.tokopedia.home_account.UrlParamHelper
import com.tokopedia.home_account.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView
import com.tokopedia.user.session.UserSessionInterface

class TopAdsHeadlineViewHolder(view: View, private val userSession: UserSessionInterface) : BaseViewHolder(view) {

    private val topadsHeadlineView: TopAdsHeadlineView = view.findViewById(R.id.topads_headline_view)
    private var topadsHeadlineUiModel: TopadsHeadlineUiModel? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_headline
    }

    private fun fetchTopadsHeadlineAds(topadsHeadLinePage: Int) {
        topadsHeadlineView.getHeadlineAds(getHeadlineAdsParam(topadsHeadLinePage), this::onSuccessResponse, this::hideHeadlineView)
    }

    private fun getHeadlineAdsParam(topadsHeadLinePage: Int): String {
        return UrlParamHelper.generateUrlParamString(mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to topadsHeadLinePage,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to VALUE_SRC,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userSession.userId
        ))
    }

    private fun onSuccessResponse(cpmModel: CpmModel) {
        topadsHeadlineUiModel?.run {
            this.cpmModel = cpmModel
            showHeadlineView(cpmModel)
        }
    }

    private fun hideHeadlineView() {
        topadsHeadlineView.showShimmerView()
        topadsHeadlineView.hide()
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
        topadsHeadlineView.hideShimmerView()
        topadsHeadlineView.show()
        topadsHeadlineView.displayAds(cpmModel)
    }

}