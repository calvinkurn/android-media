package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.databinding.UniversalInboxTopadsHeadlineItemBinding
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_NAME
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxTopAdsHeadlineViewHolder(
    itemView: View,
    private val userSession: UserSessionInterface
) : BaseViewHolder(itemView) {

    private val binding: UniversalInboxTopadsHeadlineItemBinding? by viewBinding()

    private var topAdsHeadlineUiModel: UniversalInboxTopadsHeadlineUiModel? = null

    fun bind(uiModel: UniversalInboxTopadsHeadlineUiModel) {
        topAdsHeadlineUiModel = uiModel
        hideHeadlineView()
        if (uiModel.cpmModel != null) {
            uiModel.cpmModel?.let {
                showHeadlineView(it, uiModel.index)
            }
        } else {
            fetchTopadsHeadlineAds(uiModel.topadsHeadLinePage)
        }
    }

    private fun fetchTopadsHeadlineAds(topadsHeadLinePage: Int) {
        binding?.inboxTopadsHeadlineView?.getHeadlineAds(
            getHeadlineAdsParam(topadsHeadLinePage),
            this::onSuccessResponse,
            this::hideHeadlineView
        )
    }

    private fun getHeadlineAdsParam(topadsHeadLinePage: Int): String {
        return UrlParamHelper.generateUrlParamString(mutableMapOf(
            PARAM_DEVICE to VALUE_DEVICE,
            PARAM_PAGE to topadsHeadLinePage,
            PARAM_EP to VALUE_EP,
            PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
            PARAM_ITEM to VALUE_ITEM,
            PARAM_SRC to PAGE_NAME,
            PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
            PARAM_USER_ID to userSession.userId
        ))
    }

    private fun onSuccessResponse(cpmModel: CpmModel) {
        topAdsHeadlineUiModel?.run {
            this.cpmModel = cpmModel
            showHeadlineView(cpmModel, index)
        }
    }

    private fun hideHeadlineView() {
        binding?.inboxTopadsHeadlineView?.hideShimmerView()
        binding?.inboxTopadsHeadlineView?.hide()
    }

    private fun showHeadlineView(cpmModel: CpmModel, index: Int) {
        binding?.inboxTopadsHeadlineView?.hideShimmerView()
        binding?.inboxTopadsHeadlineView?.show()
        binding?.inboxTopadsHeadlineView?.displayAds(cpmModel, index)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_topads_headline_item
    }
}
