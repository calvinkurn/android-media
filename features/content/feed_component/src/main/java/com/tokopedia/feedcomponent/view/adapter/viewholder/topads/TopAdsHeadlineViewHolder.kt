package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.view.View
import android.widget.ViewFlipper
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.mapper.TopadsFeedXMapper.cpmModelToFeedXDataModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.listener.TopAdsShopFollowBtnClickListener
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView
import com.tokopedia.user.session.UserSessionInterface

const val TOPADS_HEADLINE_VALUE_SRC = "fav_product"

class TopAdsHeadlineViewHolder(
    view: View, private val userSession: UserSessionInterface,
    private val topAdsHeadlineListener: TopAdsHeadlineListener? = null,
    private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
    private val videoViewListener: VideoViewHolder.VideoViewListener,
    private val gridItemListener: GridPostAdapter.GridItemListener,
    private val imagePostListener: ImagePostViewHolder.ImagePostListener
) : AbstractViewHolder<TopadsHeadlineUiModel>(view), TopAdsShopFollowBtnClickListener, TopAdsBannerClickListener {

    private val topadsHeadlineView: TopAdsHeadlineView = view.findViewById(R.id.topads_headline_view)
    private val topadsPostDynamic: PostDynamicViewNew = view.findViewById(R.id.item_post_dynamic_view)
    private val topadsContainer: ViewFlipper = view.findViewById(R.id.ads_container)
    private var topadsHeadlineUiModel: TopadsHeadlineUiModel? = null
    private var impressHolder: ImpressHolder? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_headline_container
        const val SHOW_SHIMMER = 0
        const val SHOW_VARIANTDEFAULT = 1
        const val SHOW_VARIANTEXPERIMENT = 2
    }

    init {
        topadsContainer.displayedChild = SHOW_SHIMMER
        topadsHeadlineView.setTopAdsBannerClickListener(this)
        topadsHeadlineView.setFollowBtnClickListener(this)
        topadsHeadlineView.setTopAdsProductItemListsner(object : TopAdsItemImpressionListener() {
            override fun onImpressionProductAdsItem(position: Int, product: Product?, cpmData: CpmData) {
                product?.let {
                    topAdsHeadlineListener?.onTopAdsProductItemListsner(position, it, cpmData)
                }
            }
        })
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
                PARAM_SRC to TOPADS_HEADLINE_VALUE_SRC,
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
        topadsHeadlineView.hideShimmerView()
        topadsHeadlineView.hide()
    }

    override fun bind(element: TopadsHeadlineUiModel?) {
        topadsHeadlineUiModel = element
        impressHolder = topadsHeadlineUiModel?.impressHolder
        hideHeadlineView()
        topadsHeadlineUiModel?.run {
            if (cpmModel != null) {
                showHeadlineView(cpmModel)
            } else {
                fetchTopadsHeadlineAds(topadsHeadlineUiModel?.topadsHeadLinePage ?: 0)
            }
        }
    }

    private fun showHeadlineView(cpmModel: CpmModel?) {
        val rand = (1..2).random()
        if (rand == 1) {
            topadsContainer.displayedChild = SHOW_VARIANTDEFAULT
            topadsHeadlineView.hideShimmerView()
            topadsHeadlineView.show()
            cpmModel?.let {
                topadsHeadlineView.displayAds(it)
            }
            topadsHeadlineUiModel?.let { setImpressionListener(it) }
        } else {
            topadsContainer.displayedChild = SHOW_VARIANTEXPERIMENT
            cpmModel?.let {
                topadsPostDynamic.bindData(  dynamicPostListener,
                    gridItemListener,
                    videoViewListener,
                    adapterPosition,
                    userSession,
                    cpmModelToFeedXDataModel(impressHolder?:ImpressHolder(),it),
                    imagePostListener
                )
            }
        }
    }

    interface TopAdsHeadlineListener {
        fun onFollowClick(positionInFeed: Int, shopId: String, adId: String)
        fun onTopAdsHeadlineImpression(position: Int, cpmModel: CpmModel)
        fun onTopAdsProductItemListsner(position: Int, product: Product, cpmData: CpmData)
        fun onTopAdsHeadlineAdsClick(position: Int, applink: String?, cpmData: CpmData)
    }

    override fun onFollowClick(shopId: String, adId: String) {
        topAdsHeadlineListener?.onFollowClick(adapterPosition, shopId, adId)
    }

    private fun setImpressionListener(element: TopadsHeadlineUiModel) {
        element.cpmModel?.let {
            topadsHeadlineView.addOnImpressionListener(element.impressHolder) {
                topAdsHeadlineListener?.onTopAdsHeadlineImpression(adapterPosition, it)
            }
        }
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        data?.let {
            topAdsHeadlineListener?.onTopAdsHeadlineAdsClick(position, applink, it)
        }
    }
}
