package com.tokopedia.shop.feed.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kol.analytics.PostTagAnalytics
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.adapter.factory.FeedShopFactoryImpl
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by yfsx on 08/05/19.
 */
class FeedShopFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        KolPostListener.View.ViewHolder, KolPostListener.View.Like,
        DynamicPostViewHolder.DynamicPostListener,
        BannerAdapter.BannerItemListener,
        TopadsShopViewHolder.TopadsShopListener,
        RecommendationCardAdapter.RecommendationCardListener,
        CardTitleView.CardTitleListener,
        ImagePostViewHolder.ImagePostListener,
        YoutubeViewHolder.YoutubePostListener,
        PollAdapter.PollOptionListener,
        GridPostAdapter.GridItemListener,
        VideoViewHolder.VideoViewListener, FeedShopContract.View {

    private lateinit var createPostUrl: String
    private lateinit var emptyResultViewModel: EmptyResultViewModel

    @Inject
    lateinit var presenter: FeedShopContract.Presenter

    @Inject
    lateinit var postTagAnalytics: PostTagAnalytics

    companion object {
        private val CREATE_POST = 888
        val PARAM_CREATE_POST_URL: String= "PARAM_CREATE_POST_URL"
        fun createInstance(createPostUrl: String): FeedShopFragment {
            val fragment = FeedShopFragment()
            val bundle:Bundle = Bundle.EMPTY
            bundle.putString(PARAM_CREATE_POST_URL, createPostUrl)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_shop, container, false)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return view!!.findViewById(R.id.recyclerView)
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view!!.findViewById(R.id.swipeToRefresh)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return FeedShopFactoryImpl(this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                userSession)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {
        presenter.getProfilePost()
    }

    override fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCursor(cursor: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessFollowKol() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorFollowKol(errorMessage: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessDeletePost(rowNumber: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLikeKolError(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserSession(): UserSession = UserSession(activity)

    override fun onGoToKolProfile(rowNumber: Int, userId: String?, postId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String?, url: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackContentClick(hasMultipleContent: Boolean, activityId: String?, activityType: String?, position: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackTooltipClick(hasMultipleContent: Boolean, activityId: String?, activityType: String?, position: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditClicked(hasMultipleContent: Boolean, activityId: String?, activityType: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMenuClicked(rowNumber: Int, element: BaseKolViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHeaderActionClick(positionInFeed: Int, id: String, type: String, isFollow: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMenuClick(positionInFeed: Int, postId: Int, reportable: Boolean, deletable: Boolean, editable: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCommentClick(positionInFeed: Int, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShareClick(positionInFeed: Int, id: Int, title: String, description: String, url: String, iamgeUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPostTagItemClick(positionInFeed: Int, redirectUrl: String, postTagItem: PostTagItem, itemPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActionPopup() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTitleCtaClick(redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onImageClick(positionInFeed: Int, contentPosition: Int, redirectLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBannerItemClick(positionInFeed: Int, adapterPosition: Int, redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: com.tokopedia.topads.sdk.domain.model.Shop) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: com.tokopedia.topads.sdk.domain.model.Data) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRecommendationAvatarClick(positionInFeed: Int, adapterPosition: Int, redirectLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int, id: String, type: String, isFollow: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActionRedirect(redirectUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int, youtubeId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPollOptionClick(positionInFeed: Int, contentPosition: Int, option: Int, pollId: String, optionId: String, isVoted: Boolean, redirectLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGridItemClick(positionInFeed: Int, contentPosition: Int, redirectLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVideoPlayerClicked(positionInFeed: Int, contentPosition: Int, postId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun goToCreatePost() {
        if (activity != null) {
            startActivityForResult(
                    RouteManager.getIntent(activity, ApplinkConst.CONTENT_CREATE_POST),
                    CREATE_POST
            )
        }
    }

    private fun getEmptyResultViewModel(): EmptyResultViewModel {
        if (emptyResultViewModel == null) {
            emptyResultViewModel = EmptyResultViewModel()
            emptyResultViewModel.setIconRes(com.tokopedia.kol.R.drawable.ic_empty_shop_feed)
            emptyResultViewModel.setTitle(getString(com.tokopedia.kol.R.string.empty_own_feed_title))
            emptyResultViewModel.setContent(getString(com.tokopedia.kol.R.string.empty_own_feed_subtitle))

            if (!TextUtils.isEmpty(createPostUrl)) {
                emptyResultViewModel.setButtonTitle(getString(com.tokopedia.kol.R.string.empty_own_feed_button))
                emptyResultViewModel.setCallback(object : BaseEmptyViewHolder.Callback {
                    override fun onEmptyContentItemTextClicked() {

                    }

                    override fun onEmptyButtonClicked() {
                        goToCreatePost()
                    }
                })
            }
        }
        return emptyResultViewModel
    }

}