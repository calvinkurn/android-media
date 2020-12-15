package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.view.View
import androidx.annotation.LayoutRes
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.*
import com.tokopedia.feedcomponent.util.util.UrlParamHelper
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.widget.TopAdsHeadlineView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.listener.TopAdsShopFollowBtnClickListener
import com.tokopedia.user.session.UserSessionInterface

class TopAdsHeadlineViewHolder(view: View, private val userSession: UserSessionInterface,
                               private val topAdsHeadlineListener: TopAdsHeadlineListener? = null) : AbstractViewHolder<TopadsHeadlineUiModel>(view), TopAdsShopFollowBtnClickListener {

    private val topadsHeadlineView: TopAdsHeadlineView = view.findViewById(R.id.topads_headline_view)
    private val shimmerView: LoaderImageView = view.findViewById(R.id.shimmer_view)
    private var topadsHeadlineUiModel: TopadsHeadlineUiModel? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_headline
    }

    init {
        topadsHeadlineView.setFollowBtnClickListener(this)
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
        shimmerView.show()
        topadsHeadlineView.hide()
    }

    override fun bind(element: TopadsHeadlineUiModel?) {
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
        shimmerView.hide()
        topadsHeadlineView.show()
        topadsHeadlineView.displayAds(cpmModel)
    }

    interface TopAdsHeadlineListener {
        fun onFollowClick(positionInFeed: Int, shopId: String)
    }

    override fun onFollowClick(shopId: String) {
        topAdsHeadlineListener?.onFollowClick(adapterPosition, shopId)
    }

}