package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.util.TopadsRollenceUtil
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.mapper.TopadsFeedXMapper.cpmModelToFeedXDataModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView
import com.tokopedia.user.session.UserSessionInterface

const val TOPADS_VARIANT_EXPERIMENT_CLEAN = 2
const val TOPADS_VARIANT_EXPERIMENT_INFO = 3

open class TopAdsHeadlineV2ViewHolder(
    view: View, private val userSession: UserSessionInterface,
    private val topAdsHeadlineListener: TopAdsHeadlineListener?=null,
    private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
    private val videoViewListener: VideoViewHolder.VideoViewListener,
    private val gridItemListener: GridPostAdapter.GridItemListener,
    private val imagePostListener: ImagePostViewHolder.ImagePostListener
) : AbstractViewHolder<TopadsHeadLineV2Model>(view) {

    private val topadsHeadlineView =  TopAdsHeadlineView(itemView.context)
    private val topadsPostDynamic: PostDynamicViewNew = view.findViewById(R.id.item_post_dynamic_view)
    private val topadsContainer: ViewFlipper = view.findViewById(R.id.ads_container)
    private val container : LinearLayout = view.findViewById(R.id.container_ll)
    private var topadsHeadlineUiModel: TopadsHeadLineV2Model? = null
    private var impressHolder: ImpressHolder? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_headline_container
        const val SHOW_SHIMMER = 0
        const val VARIANT_EXPERIMENT = 1
        const val PAYLOAD_ANIMATE_FOLLOW = 7
        const val PAYLOAD_POST_VISIBLE = 77
    }

    init {
        topadsContainer.displayedChild = SHOW_SHIMMER
    }

    private fun fetchTopadsHeadlineAds(topadsHeadLinePage: Int) {
        topadsHeadlineView.getHeadlineAds(getHeadlineAdsParam(topadsHeadLinePage), this::onSuccessResponse, this::removeTopadsView)
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
            val layoutType = cpmModel?.data?.firstOrNull()?.cpm?.layout
            this.feedXCard = cpmModelToFeedXDataModel(impressHolder,cpmModel,layoutType?:0)
            showHeadlineView(cpmModel)
        }
    }

    private fun hideHeadlineView() {
        this.itemView.hide()
        val params: ViewGroup.LayoutParams = this.itemView.layoutParams
        params.height = 0
        params.width = 0
        this.itemView.layoutParams = params
    }

    override fun bind(element: TopadsHeadLineV2Model?, payloads: MutableList<Any>) {
        if (element == null) {
            itemView.hide()
            return
        }
        when (payloads.firstOrNull() as Int) {
            PAYLOAD_POST_VISIBLE -> topadsPostDynamic.bindImage(
                    element.feedXCard.tags,
                    element.feedXCard.media[element.feedXCard.lastCarouselIndex],
                    element.feedXCard
            )
            PAYLOAD_ANIMATE_FOLLOW -> topadsPostDynamic.bindFollow(element.feedXCard)
        }
    }

    override fun bind(element: TopadsHeadLineV2Model?) {
        topadsContainer.displayedChild = SHOW_SHIMMER
        topadsHeadlineUiModel = element
        impressHolder = topadsHeadlineUiModel?.impressHolder
        topadsHeadlineUiModel?.run {
            if (cpmModel != null && !cpmModel?.data.isNullOrEmpty()) {
                showHeadlineView(cpmModel)
            } else {
                fetchTopadsHeadlineAds(topadsHeadlineUiModel?.topadsHeadLinePage ?: 0)
            }
        }
    }

    private fun showHeadlineView(cpmModel: CpmModel?) {
        this.itemView.show()
        val params = itemView.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        itemView.layoutParams = params
        if (TopadsRollenceUtil.shouldShowFeedNewDesign(itemView.context)) {
            topadsContainer.displayedChild = VARIANT_EXPERIMENT
            cpmModel.let {
                topadsPostDynamic.bindData(
                    dynamicPostListener,
                    gridItemListener,
                    videoViewListener,
                    adapterPosition,
                    userSession,
                    this.topadsHeadlineUiModel?.feedXCard?: FeedXCard(),
                    imagePostListener, topAdsHeadlineListener
                )
            }
            container.setMargin(
                    itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
                    itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12),
                    itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_0),
                    itemView.context.resources.getDimensionPixelSize(R.dimen.unify_space_12)
            )
            topadsHeadlineUiModel?.let { setImpressionListener(it) }
        } else {
            removeTopadsView()
        }
    }

    private fun setImpressionListener(element: TopadsHeadLineV2Model) {
        element.cpmModel?.let {
                topAdsHeadlineListener?.onTopAdsHeadlineImpression(adapterPosition, it,true)
        }
    }

    private fun removeTopadsView(){
        this.itemView.hide()
        val params: ViewGroup.LayoutParams = this.itemView.layoutParams
        params.height = 0
        params.width = 0
        this.itemView.layoutParams = params
        topAdsHeadlineListener?.hideTopadsView(adapterPosition)
    }

    fun onItemDetach(context: Context, visitable: Visitable<*>) {
        try {
            topadsPostDynamic.detach(false, visitable)
        } catch (e: Exception) {
        }
    }
    fun onItemAttach(context: Context, visitable: Visitable<*>) {
        try {
            topadsPostDynamic.attach( visitable)
        } catch (e: Exception) {
        }
    }

}
