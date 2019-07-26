package com.tokopedia.shop.feed.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.util.FeedScrollListener
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.common.util.PostMenuListener
import com.tokopedia.kol.common.util.createBottomMenu
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.feed.di.DaggerFeedShopComponent
import com.tokopedia.shop.feed.domain.WhitelistDomain
import com.tokopedia.shop.feed.view.adapter.factory.FeedShopFactoryImpl
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.EmptyFeedShopViewModel
import com.tokopedia.shop.feed.view.model.WhitelistViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_feed_shop.*
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
        VideoViewHolder.VideoViewListener,
        FeedMultipleImageView.FeedMultipleImageViewListener,
        FeedShopContract.View {

    private lateinit var createPostUrl: String
    private lateinit var shopId: String
    private var isLoading = false
    private var isForceRefresh = false

    @Inject
    lateinit var presenter: FeedShopContract.Presenter

    @Inject
    lateinit var postTagAnalytics: PostTagAnalytics

    companion object {
        private const val YOUTUBE_URL = "{youtube_url}"
        private const val TEXT_PLAIN = "text/plain"
        private val CREATE_POST = 888
        private val OPEN_DETAIL = 54
        private const val LOGIN_CODE = 1383
        private const val LOGIN_FOLLOW_CODE = 1384
        private const val OPEN_CONTENT_REPORT = 1130
        private const val KOL_COMMENT_CODE = 13
        val PARAM_CREATE_POST_URL: String= "PARAM_CREATE_POST_URL"
        val PARAM_SHOP_ID: String= "PARAM_SHOP_ID"
        fun createInstance(shopId: String, createPostUrl: String): FeedShopFragment {
            val fragment = FeedShopFragment()
            val bundle = Bundle()
            bundle.putString(PARAM_SHOP_ID, shopId)
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
        initVar()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    private fun initVar() {
        hideFAB()
        arguments?.let {
            shopId = it.getString(PARAM_SHOP_ID) ?: ""
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    if (hasFeed()
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        recyclerView?.let {
                            FeedScrollListener.onFeedScrolled(it, adapter.list)
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }

        })
    }

    private fun hasFeed(): Boolean {
        return (adapter.list != null
                && !adapter.list.isEmpty()
                && adapter.list.size > 1
                && adapter.list[0] !is EmptyModel)
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
                this,
                this,
                userSession)
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerFeedShopComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                OPEN_CONTENT_REPORT -> {
                    if (data!!.getBooleanExtra(ContentReportActivity.RESULT_SUCCESS, false)) {
                        onSuccessReportContent()
                    } else {
                        onErrorReportContent(
                                data.getStringExtra(ContentReportActivity.RESULT_ERROR_MSG)
                        )
                    }
                }
                OPEN_DETAIL -> {
                    showSnackbar(data!!.getStringExtra("message"))
                }
                LOGIN_CODE -> {
                    loadInitialData()
                }
            }
        }
    }

    override fun loadData(page: Int) {
        if (shopId.isNotEmpty() && !isLoading) {
            isLoading = true
            if (isLoadingInitialData) {
                presenter.getFeedFirstPage(shopId, isForceRefresh)
            } else {
                presenter.getFeed(shopId)
            }
        }
    }

    override fun onSwipeRefresh() {
        hideSnackBarRetry()
        swipeToRefresh.isRefreshing = true
        presenter.getFeedFirstPage(shopId, true)
    }

    override fun onSuccessGetFeedFirstPage(element: List<Visitable<*>>, lastCursor: String, whitelistDomain: WhitelistDomain) {
        val dataList = ArrayList<Visitable<*>>()
        isForceRefresh = true
        isLoading = false
        if (element.isNotEmpty()) {
            if (shopId.equals(userSession.shopId) && !whitelistDomain.authors.isEmpty()) {
                showFAB(whitelistDomain)
            } else {
                hideFAB()
            }
            dataList.addAll(element)
            renderList(dataList, lastCursor.isNotEmpty())
        } else {
            dataList.add(getEmptyResultViewModel())
            renderList(dataList)
        }
        trackImpression(dataList)

    }

    override fun onSuccessGetFeedNotLoginFirstPage(element: List<Visitable<*>>, lastCursor: String) {
        val dataList = ArrayList<Visitable<*>>()
        isLoading = false
        if (element.isNotEmpty()) {
            dataList.addAll(element)
            renderList(dataList, lastCursor.isNotEmpty())
        } else {
            dataList.add(getEmptyResultViewModel())
            renderList(dataList)
        }
    }

    override fun onWhitelistClicked(element: WhitelistViewModel) {
        goToCreatePost()
    }

    override fun onEmptyFeedButtonClicked() {
        goToCreatePost()
    }

    override fun onSuccessGetFeed(visitables: List<Visitable<*>>, lastCursor: String) {
        isLoading = false
        updateCursor(lastCursor)
        renderList(visitables, lastCursor.isNotEmpty())
        trackImpression(visitables)
    }

    override fun updateCursor(cursor: String) {
        presenter.cursor = cursor
    }

    override fun onSuccessFollowKol() {
    }

    override fun onErrorFollowKol(errorMessage: String) {
    }

    override fun onSuccessDeletePost(rowNumber: Int) {
    }

    override fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int) {
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: Int) {
        if (adapter.data.size > rowNumber
                && adapter.data[rowNumber] != null
                && adapter.data[rowNumber] is DynamicPostViewModel) {
            val model = adapter.data[rowNumber] as DynamicPostViewModel
            val like = model.footer.like
            like.isChecked = !model.footer.like.isChecked
            if (like.isChecked) {
                try {
                    val likeValue = Integer.valueOf(like.fmt) + 1
                    like.fmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                }

                like.value = like.value + 1
            } else {
                try {
                    val likeValue = Integer.valueOf(like.fmt) - 1
                    like.fmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                }

                like.value = like.value - 1
            }
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_LIKE)
        }
    }

    override fun onLikeKolError(message: String) = showError(message)

    override fun getUserSession(): UserSession = UserSession(activity)

    override fun onGoToKolProfile(rowNumber: Int, userId: String?, postId: Int) {
    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String?) {
    }

    override fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String?, url: String?) {
        onGoToLink(url ?: "")
    }

    override fun trackContentClick(hasMultipleContent: Boolean, activityId: String?, activityType: String?, position: String?) {
    }

    override fun trackTooltipClick(hasMultipleContent: Boolean, activityId: String?, activityType: String?, position: String?) {
    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {
    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {
    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String?) {
        if (userSession.isLoggedIn) {
            presenter.likeKol(id, rowNumber, this)
        } else {
            goToLogin()
        }
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String?) {
        if (userSession.isLoggedIn) {
            presenter.unlikeKol(id, rowNumber, this)
        } else {
            goToLogin()
        }
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int, hasMultipleContent: Boolean, activityType: String?) {
        val intent = KolCommentActivity.getCallingIntent(
                context, id, rowNumber
        )
        startActivityForResult(intent, KOL_COMMENT_CODE)
    }

    override fun onEditClicked(hasMultipleContent: Boolean, activityId: String?, activityType: String?) {
    }

    override fun onMenuClicked(rowNumber: Int, element: BaseKolViewModel) {
        context?.let {
            val menus = createBottomMenu(it, element, object : PostMenuListener {
                override fun onDeleteClicked() {
                    createDeleteDialog(rowNumber, element.contentId).show()
                }

                override fun onReportClick() {
                    goToContentReport(element.contentId)
                }

                override fun onEditClick() {

                }
            })
            menus.show()
        }
    }

    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String) {

    }

    override fun onHeaderActionClick(positionInFeed: Int, id: String, type: String, isFollow: Boolean) {
        if (type == FollowCta.AUTHOR_USER) {
            var userIdInt = 0
            try {
                userIdInt = Integer.valueOf(id)
            } catch (ignored: NumberFormatException) {
            }
            if (isFollow) {
                onUnfollowKolClicked(positionInFeed, userIdInt)
            } else {
                onFollowKolClicked(positionInFeed, userIdInt)
            }
        }
    }

    override fun onMenuClick(positionInFeed: Int, postId: Int, reportable: Boolean, deletable: Boolean, editable: Boolean) {
        context?.let {
            val menus = createBottomMenu(it, deletable, reportable, editable, object : PostMenuListener {
                override fun onDeleteClicked() {
                    createDeleteDialog(positionInFeed, postId).show()
                }

                override fun onReportClick() {
                    goToContentReport(postId)
                }

                override fun onEditClick() {
                    goToEditPostShop(postId)
                }
            })
            menus.show()
        }
    }

    private fun goToEditPostShop(postId: Int) {
        context?.let { RouteManager.route(it, ApplinkConstInternalContent.SHOP_POST_EDIT, postId.toString()) }
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean) {
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id, false, "")
        } else {
            onLikeKolClicked(positionInFeed, id, false, "")
        }
    }

    override fun onCommentClick(positionInFeed: Int, id: Int) {
        onGoToKolComment(positionInFeed, id, false, "")
    }

    override fun onShareClick(positionInFeed: Int, id: Int, title: String, description: String, url: String, iamgeUrl: String) {
        activity?.let {
            ShareBottomSheets().show(activity!!.supportFragmentManager,
                    ShareBottomSheets.constructShareData("", iamgeUrl, url, description, title),
                    object : ShareBottomSheets.OnShareItemClickListener {
                        override fun onShareItemClicked(packageName: String) {

                        }
                    })
        }
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onPostTagItemClick(positionInFeed: Int, redirectUrl: String, postTagItem: PostTagItem, itemPosition: Int) {
        onGoToLink(redirectUrl)
    }

    override fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean) {
        for (tracking in trackList) {
            if (isClick) {
                presenter.trackPostClickUrl(tracking.clickURL)
            } else {
                presenter.trackPostClickUrl(tracking.viewURL)
            }
        }
    }

    override fun onPostTagItemBuyClicked(positionInFeed: Int, postTagItem: PostTagItem) {
        presenter.addPostTagItemToCart(postTagItem)
    }

    override fun onActionPopup() {
    }

    override fun onTitleCtaClick(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onImageClick(positionInFeed: Int, contentPosition: Int, redirectLink: String) {
        onGoToLink(redirectLink)
    }

    override fun onBannerItemClick(positionInFeed: Int, adapterPosition: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: com.tokopedia.topads.sdk.domain.model.Shop) {
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: com.tokopedia.topads.sdk.domain.model.Data) {
    }

    override fun onRecommendationAvatarClick(positionInFeed: Int, adapterPosition: Int, redirectLink: String) {
        onGoToLink(redirectLink)
    }

    override fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int, id: String, type: String, isFollow: Boolean) {
    }

    override fun onActionRedirect(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int, youtubeId: String) {
        val redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId)

        if (context != null) {
            RouteManager.route(
                    context!!,
                    redirectUrl
            )
        }
    }

    override fun onPollOptionClick(positionInFeed: Int, contentPosition: Int, option: Int, pollId: String, optionId: String, isVoted: Boolean, redirectLink: String) {
        if (isVoted) {
            onGoToLink(redirectLink)
        }
    }

    override fun onGridItemClick(positionInFeed: Int, contentPosition: Int, productPosition: Int, redirectLink: String) {
        onGoToLink(redirectLink)
    }

    override fun onVideoPlayerClicked(positionInFeed: Int, contentPosition: Int, postId: String) {
        activity?.let {
            startActivity(VideoDetailActivity.getInstance(
                    it,
                    postId))
        }
    }

    override fun onAddToCartSuccess() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
    }

    override fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    fun hideFAB() {
        fab_feed.hide()
    }

    fun showFAB(whitelistDomain: WhitelistDomain) {
        fab_feed.show()
        val author = whitelistDomain.authors.get(0)
        fab_feed.setOnClickListener {
            onGoToLink(author.link)
        }

    }

    fun updateShopInfo(shopInfo: ShopInfo) {
        shopId = shopInfo.shopCore.shopID
        loadInitialData()
    }

    private fun goToCreatePost() {
        if (activity != null) {
            startActivityForResult(
                    RouteManager.getIntent(activity, ApplinkConst.CONTENT_CREATE_POST),
                    CREATE_POST
            )
        }
    }

    private fun onGoToLink(url: String) {
        if (RouteManager.isSupportApplink(activity, url)) {
            RouteManager.route(activity, url)
        } else {
            RouteManager.route(
                    activity,
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
            )
        }
    }

    private fun createDeleteDialog(rowNumber: Int, id: Int): Dialog {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.kol_delete_post))
        dialog.setDesc(getString(R.string.kol_delete_post_desc))
        dialog.setBtnOk(getString(R.string.kol_title_delete))
        dialog.setBtnCancel(getString(R.string.kol_title_cancel))
        dialog.setOnOkClickListener {
            presenter.deletePost(id, rowNumber)
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener { dialog.dismiss() }
        return dialog
    }

    private fun goToContentReport(contentId: Int) {
        if (context != null) {
            if (userSession.isLoggedIn) {
                val intent = ContentReportActivity.createIntent(
                        context!!,
                        contentId
                )
                startActivityForResult(intent, OPEN_CONTENT_REPORT)
            } else {
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        activity?.let {
            startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), LOGIN_CODE)
        }
    }

    private fun getEmptyResultViewModel(): EmptyFeedShopViewModel {
       return EmptyFeedShopViewModel()
    }

    private fun onSuccessReportContent() {
        ToasterNormal
                .make(view,
                        getString(R.string.feed_content_reported),
                        BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close) { v -> }
                .show()
    }

    private fun onErrorReportContent(errorMsg: String) {
        ToasterError
                .make(view, errorMsg, BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close) { v -> }
                .show()
    }

    private fun showSnackbar(s: String) {
        NetworkErrorHelper.showSnackbar(activity, s)
    }

    private fun doShare(link: String, formatString: String, shareTitle: String) {
        val shareBody = String.format(formatString, link)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = TEXT_PLAIN
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(
                Intent.createChooser(sharingIntent, shareTitle)
        )
    }

    private fun trackImpression(visitableList: List<Visitable<*>>) {
        visitableList.forEachIndexed { position, model ->
            val adapterPosition = adapter.data.size + position
            when (model) {
                is DynamicPostViewModel -> {
                    onAffiliateTrackClicked(model.tracking, false)
                }
            }
        }
    }

    private fun showError(message: String) {
        showError(message, null)
    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        ToasterError.make(view, message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setRefresh() {
        isForceRefresh = true
    }

}