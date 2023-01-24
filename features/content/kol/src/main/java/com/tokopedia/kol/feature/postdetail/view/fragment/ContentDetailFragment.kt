package com.tokopedia.kol.feature.postdetail.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.feedcomponent.bottomsheets.*
import com.tokopedia.feedcomponent.data.bottomsheet.ProductBottomSheetData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.domain.mapper.*
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.FeedScrollListenerNew
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedAsgcCampaignResponseModel
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.di.DaggerContentDetailComponent
import com.tokopedia.kol.feature.postdetail.di.module.ContentDetailModule
import com.tokopedia.kol.feature.postdetail.view.activity.ContentDetailActivity
import com.tokopedia.kol.feature.postdetail.view.activity.ContentDetailActivity.Companion.SOURCE_USER_PROFILE
import com.tokopedia.kol.feature.postdetail.view.adapter.ContentDetailAdapter
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.ContentDetailPostViewHolder
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_AUTHOR_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_IS_POST_FOLLOWED
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_POST_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_VIDEO
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARG_HASHTAG
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARG_IS_FROM_CONTENT_DETAIL_PAGE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.COMMENT_ARGS_POSITION
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.COMMENT_ARGS_TOTAL_COMMENT
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.CONTENT_DETAIL_PAGE_SOURCE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.DEFAULT_COMMENT_ARGUMENT_VALUE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.PARAM_AUTHOR_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.PARAM_POST_POSITION
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.PARAM_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.SHOULD_TRACK
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.SOURCE_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.START_TIME
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.TYPE_CONTENT_PREVIEW_PAGE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.VOD_POST
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import com.tokopedia.kol.feature.video.view.fragment.*
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.feedcomponent.R as feedComponentR
import com.tokopedia.kol.R as kolR
import com.tokopedia.network.R as networkR
import com.tokopedia.wishlist_common.R as wishlistR

/**
 * Created by shruti agarwal on 15/06/22
 */

@Suppress("LateinitUsage")
class ContentDetailFragment :
    BaseDaggerFragment(),
    ContentDetailPostViewHolder.CDPListener,
    ProductItemInfoBottomSheet.Listener,
    ShareBottomsheetListener,
    FeedFollowersOnlyBottomSheet.Listener {

    private var cdpRecyclerView: RecyclerView? = null
    private var postId = "0"
    private var visitedUserID = ""
    private var visitedUserEncryptedID = ""
    private var currentPosition = 0
    private var contentDetailSource = ""
    private var rowNumberWhenShareClicked = 0
    private var dissmisByGreyArea = true
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var reportBottomSheet: ReportBottomSheet
    private lateinit var productTagBS: ProductItemInfoBottomSheet
    private var feedFollowersOnlyBottomSheet: FeedFollowersOnlyBottomSheet? = null

    private val adapter = ContentDetailAdapter(
        ContentDetailListener = this
    )

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analyticsTracker: ContentDetailNewPageAnalytics

    var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private lateinit var shareData: LinkerData
    private var isShareFromProductBottomSheet: Boolean = false

    private val viewModel: ContentDetailViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(ContentDetailViewModel::class.java)
    }

    companion object {
        private const val DEFAULT_INVALID_POSITION_VALUE = -1
        private const val AUTHOR_USER_TYPE_VALUE = 1
        const val REQUEST_LOGIN = 345
        private const val OPEN_KOL_COMMENT = 101
        const val OPEN_VIDEO_DETAIL = 1311
        const val OPEN_FEED_DETAIL = 1313
        private const val COMMENT_ARGS_SERVER_ERROR_MSG = "ARGS_SERVER_ERROR_MSG"

        @JvmStatic
        fun newInstance(bundle: Bundle?): ContentDetailFragment {
            val fragment = ContentDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
        retainInstance = true
    }

    private fun initVar() {
        contentDetailSource = arguments?.getString(ContentDetailActivity.PARAM_ENTRY_POINT)
            ?: ContentDetailActivity.SHARE_LINK
        postId = arguments?.getString(ContentDetailActivity.PARAM_POST_ID)
            ?: ContentDetailActivity.DEFAULT_POST_ID

        if (contentDetailSource == SOURCE_USER_PROFILE) {
            currentPosition = arguments?.getInt(ContentDetailActivity.PARAM_POSITION) ?: 0
            visitedUserID =
                arguments?.getString(ContentDetailActivity.PARAM_VISITED_USER_ID).orEmpty()
            visitedUserEncryptedID =
                arguments?.getString(ContentDetailActivity.PARAM_VISITED_USER_ENCRYPTED_ID)
                    .orEmpty()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.run {
            userProfileFeedPost.observe(
                viewLifecycleOwner
            ) {
                when (it) {
                    is Success -> onSuccessGetUserProfileFeedPost(it.data)
                    is Fail -> showToast(
                        getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder),
                        Toaster.TYPE_ERROR
                    )
                }
            }
            getCDPPostFirstPostData.observe(viewLifecycleOwner) {
                when (it) {
                    is Success -> {
                        onSuccessGetFirstPostCDPData(it.data)
                    }
                    else -> {
                        showToast(
                            getString(feedComponentR.string.feed_video_tab_error_reminder),
                            Toaster.TYPE_ERROR
                        )
                    }
                }
            }
            cDPPostRecomData.observe(viewLifecycleOwner) {
                when (it) {
                    is Success -> {
                        onSuccessGetCDPRecomData(it.data)
                    }
                    else -> {
                        showToast(
                            getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder),
                            Toaster.TYPE_ERROR
                        )
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(kolR.layout.fragment_content_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as ContentDetailActivity

        val backBtn = activity.getHeaderView()
            ?.findViewById<AppCompatImageView>(kolR.id.content_detail_back_icon)
        backBtn?.setOnClickListener {
            viewModel.getCDPPostFirstPostData.value?.let {
                if (it is Success && it.data.postList.firstOrNull()?.isTypeSgcVideo == true) {
                    analyticsTracker.sendClickBackOnContentDetailpage(
                        getContentDetailAnalyticsData(
                            it.data.postList.first()
                        )
                    )
                }
            }
            activity.onBackPressed()
        }

        setupView(view)

        if (contentDetailSource == SOURCE_USER_PROFILE) {
            viewModel.fetchUserProfileFeedPost(visitedUserID, currentPosition)
        } else {
            viewModel.getContentDetail(postId)
        }

        observeLikeContent()
        observeWishlist()
        observeFollowUnfollow()
        observeDeleteContent()
        observeReportContent()
        observeVideoViewData()
        observeAddToCart()
        observeReminderBtnInitialState()
        observeReminderBtnSetState()
        observeFeedWidgetUpdatedData()
    }

    private fun setupView(view: View) {
        cdpRecyclerView = view.findViewById(kolR.id.cdp_recycler_view)

        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            cdpRecyclerView?.addOnScrollListener(it)
            it.resetState()
        }

        cdpRecyclerView?.adapter = adapter
    }

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(cdpRecyclerView?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (contentDetailSource == SOURCE_USER_PROFILE) {
                    viewModel.fetchUserProfileFeedPost(visitedUserID)
                } else {
                    viewModel.getContentDetailRecommendation(postId)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            FeedScrollListenerNew.onCDPScrolled(
                                recyclerView,
                                adapter.getList()
                            )
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {
            OPEN_KOL_COMMENT -> if (resultCode == Activity.RESULT_OK) {
                val serverErrorMsg = data.getStringExtra(COMMENT_ARGS_SERVER_ERROR_MSG)
                if (!TextUtils.isEmpty(serverErrorMsg)) {
                    // TODO check toaster is as per requirement
                    view?.let {
                        Toaster.build(
                            it,
                            serverErrorMsg ?: "",
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(kolR.string.cta_refresh_feed)
                        )
                    }
                } else {
                    onSuccessAddDeleteKolComment(
                        data.getIntExtra(COMMENT_ARGS_POSITION, DEFAULT_COMMENT_ARGUMENT_VALUE),
                        data.getIntExtra(COMMENT_ARGS_TOTAL_COMMENT, 0)
                    )
                }
            }
            OPEN_FEED_DETAIL -> if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(ContentDetailArgumentModel.IS_FOLLOWED, false)) {
                    val rowNumber =
                        data.getIntExtra(PARAM_POST_POSITION, DEFAULT_INVALID_POSITION_VALUE)
                    if (rowNumber in 0 until adapter.getList().size) {
                        onSuccessFollowShop(
                            ShopFollowModel(
                                rowNumber = rowNumber,
                                action = ShopFollowAction.Follow,
                                isFollowedFromRSRestrictionBottomSheet = true
                            )
                        )
                    }
                }
            }
        }
    }

    private fun onSuccessGetUserProfileFeedPost(data: ContentDetailUiModel) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(viewModel.currentCursor.isNotEmpty())
        adapter.setItemsAndAnimateChanges(data.postList)
        cdpRecyclerView?.scrollToPosition(currentPosition)
        currentPosition = adapter.lastIndex
    }

    private fun onSuccessGetFirstPostCDPData(data: ContentDetailUiModel) {
        (activity as? ContentDetailActivity)?.setContentDetailMainPostData(
            data.postList.firstOrNull()
        )

        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(viewModel.currentCursor.isNotEmpty())
        adapter.setItemsAndAnimateChanges(data.postList)
        viewModel.getContentDetailRecommendation(postId)
    }

    private fun onSuccessGetCDPRecomData(data: ContentDetailUiModel) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(viewModel.currentCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(data.postList)
    }

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false) {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        } else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()
        }
    }

    override fun getScreenName(): String {
        return "CDP Revamp Fragment"
    }

    override fun initInjector() {
        DaggerContentDetailComponent.builder()
            .baseAppComponent(
                (requireContext().applicationContext as BaseMainApplication).baseAppComponent
            )
            .contentDetailModule(ContentDetailModule())
            .build()
            .inject(this)
    }

    private fun onGoToLogin() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            requireActivity().startActivityForResult(intent, REQUEST_LOGIN)
        }
    }

    override fun onLikeClicked(feedXCard: FeedXCard, postPosition: Int, isDoubleTap: Boolean) {
        val trackerId = getTrackerID(
            feedXCard,
            trackerIdSgc = if (!feedXCard.like.isLiked) "33260" else "33261",
            trackerIdSgcRecom = if (!feedXCard.like.isLiked) "34304" else "34305"
        )
        if (isDoubleTap) {
            analyticsTracker.sendClickDoubleTapLikeUnlikeSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = trackerId
                ),
                !feedXCard.like.isLiked
            )
        } else {
            analyticsTracker.sendClickLikeSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdSgc = "33259",
                        trackerIdVod = "34152",
                        trackerIdVodRecomm = "34170",
                        trackerIdSgcRecom = "34283",
                        trackerIdAsgcRecom = "34096",
                        trackerIdSgcVideo = "34613",
                        trackerIdAsgc = "34108",
                        trackerIdLongVideo = "34503",
                        trackerIdLongVideoRecomm = "34521"
                    )
                )
            )
        }

        if (!userSession.isLoggedIn) {
            onGoToLogin()
            return
        }
        val contentLikeAction = ContentLikeAction.getLikeAction(feedXCard.like.isLiked)
        viewModel.likeContent(
            contentId = feedXCard.id,
            action = contentLikeAction,
            rowNumber = postPosition
        )
    }

    private fun showNoInterNetDialog(context: Context) {
        val sheet = FeedNetworkErrorBottomSheet.newInstance(false)
        if (!sheet.isAdded) {
            sheet.show(childFragmentManager, "")
        }
    }

    private fun onSuccessLikeDislikeKolPost(rowNumber: Int) {
        val newList = adapter.getList()
        if (newList.size > rowNumber) {
            val item = newList[rowNumber]
            val like = item.like
            like.isLiked = !like.isLiked
            if (like.isLiked) {
                try {
                    val likeValue = Integer.valueOf(like.countFmt) + 1
                    like.countFmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                    Timber.e(ignored)
                }

                like.count = like.count + 1
            } else {
                try {
                    val likeValue = Integer.valueOf(like.countFmt) - 1
                    like.countFmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                    Timber.e(ignored)
                }

                like.count = like.count - 1
            }
            val likePayload = Bundle().apply {
                putBoolean(ContentDetailPostViewHolder.IMAGE_POST_LIKED_UNLIKED, true)
            }
            adapter.notifyItemChanged(rowNumber, likePayload)
        }
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        val newList = adapter.getList()
        if (newList.size > rowNumber) {
            val item = newList[rowNumber]
            item.comments.count = totalNewComment
            item.comments.countFmt =
                (item.comments.count).toString()

            val commentPayload = Bundle().apply {
                putBoolean(ContentDetailPostViewHolder.IMAGE_POST_COMMENT_ADD_DELETE, true)
            }

            adapter.notifyItemChanged(rowNumber, commentPayload)
        }
    }

    private fun onSuccessFollowShop(data: ShopFollowModel) {
        val rowNumber = data.rowNumber
        if (rowNumber < adapter.getList().size) {
            val feedXCardData = adapter.getList()[rowNumber]

            feedXCardData.followers.isFollowed = !feedXCardData.followers.isFollowed
            if (!feedXCardData.followers.isFollowed && data.action.isUnFollowing) {
                showToast(
                    getString(com.tokopedia.feedcomponent.R.string.feed_component_unfollow_success_toast),
                    Toaster.TYPE_NORMAL
                )
            } else {
                showToast(
                    getString(com.tokopedia.feedcomponent.R.string.feed_component_follow_success_toast),
                    Toaster.TYPE_NORMAL
                )
            }

            if (feedXCardData.followers.isFollowed) {
                feedXCardData.followers.transitionFollow = true
            }

            val followUnfollowPayload = Bundle().apply {
                putBoolean(ContentDetailPostViewHolder.IMAGE_POST_FOLLOW_UNFOLLOW, true)
            }
            adapter.notifyItemChanged(
                rowNumber,
                followUnfollowPayload
            )
        }
    }

    private fun onSuccessAddViewVODPost(postPosition: Int) {
        val newList = adapter.getList()
        if (newList.size > postPosition) {
            val item = newList[postPosition]
            val view = item.views
            try {
                val viewValue = Integer.valueOf(view.countFmt) + 1
                view.countFmt = viewValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            view.count = view.count + 1
        }
    }

    private fun openCommentPage(
        rowNumber: Int,
        id: Int,
        authorType: String,
        isVideo: Boolean,
        isFollowed: Boolean,
        type: String
    ) {
        val intent = RouteManager.getIntent(
            requireContext(),
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalContent.COMMENT,
                mapOf(
                    COMMENT_ARGS_POSITION to rowNumber.toString()
                )
            ),
            id.toString()
        )
        intent.putExtra(ARGS_AUTHOR_TYPE, authorType)
        intent.putExtra(ARGS_VIDEO, isVideo)
        intent.putExtra(ARGS_POST_TYPE, type)
        intent.putExtra(ARGS_IS_POST_FOLLOWED, isFollowed)
        intent.putExtra(ARG_IS_FROM_CONTENT_DETAIL_PAGE, true)
        intent.putExtra(CONTENT_DETAIL_PAGE_SOURCE, contentDetailSource)
        startActivityForResult(intent, OPEN_KOL_COMMENT)
    }

    override fun onCommentClicked(
        feedXCard: FeedXCard,
        postPosition: Int,
        isSeeMoreComment: Boolean
    ) {
        val trackerId = getTrackerID(
            feedXCard,
            trackerIdSgc = "33262",
            trackerIdVod = "34153",
            trackerIdVodRecomm = "34171",
            trackerIdSgcRecom = "34284",
            trackerIdSgcVideo = "34614",
            trackerIdLongVideo = "34504",
            trackerIdLongVideoRecomm = "34522"

        )
        if (isSeeMoreComment) {
            analyticsTracker.sendClickLehatSemuaCommentClick(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdVod = "34154",
                        trackerIdVodRecomm = "34172",
                        trackerIdLongVideo = "34505",
                        trackerIdLongVideoRecomm = "34523"
                    )
                )
            )
        } else {
            analyticsTracker.sendClickCommentSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = trackerId
                )
            )
        }

        val media = feedXCard.media.firstOrNull()
        var authId = ""
        val authorType = feedXCard.author.type
        if (authorType != 1) {
            authId = feedXCard.author.id
        }
        openCommentPage(
            rowNumber = postPosition,
            id = feedXCard.id.toIntOrZero(),
            authorType = authId,
            isVideo = media?.isVideo ?: false,
            isFollowed = feedXCard.followers.isFollowed,
            type = media?.type ?: ""
        )
    }

    override fun onSharePostClicked(feedXCard: FeedXCard, postPosition: Int) {
        rowNumberWhenShareClicked = postPosition

        val trackerId = getTrackerID(
            feedXCard,
            trackerIdSgc = "33263",
            trackerIdVod = "34155",
            trackerIdAsgcRecom = "34097",
            trackerIdVodRecomm = "34173",
            trackerIdSgcVideo = "34615",
            trackerIdAsgc = "34109",
            trackerIdLongVideo = "34506",
            trackerIdLongVideoRecomm = "34524"
        )
        analyticsTracker.sendClickShareSgcImageEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = trackerId
            )
        )

        activity?.let {
            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder()
                .setId(feedXCard.id)
                .setName(
                    String.format(
                        getString(kolR.string.feed_share_title),
                        feedXCard.author.name
                    )
                )
                .setDescription(
                    String.format(
                        getString(kolR.string.feed_share_desc_text),
                        feedXCard.author.name
                    )
                )
                .setDesktopUrl(feedXCard.webLink)
                .setType(LinkerData.FEED_TYPE)
                .setImgUri(feedXCard.media.firstOrNull()?.mediaUrl ?: "")
                .setDeepLink(feedXCard.appLink)
                .setUri(feedXCard.webLink)

            shareData = shareDataBuilder.build()
            isShareFromProductBottomSheet = false
            showUniversalShareBottomSheet(getContentShareDataModel(feedXCard))
        }
    }

    private fun getContentShareDataModel(feedXCard: FeedXCard): ContentShareDataModel {
        val mediaUrl =
            if (feedXCard.isTypeProductHighlight) {
                feedXCard.products.firstOrNull()?.coverURL ?: ""
            } else {
                feedXCard.media.firstOrNull()?.mediaUrl ?: ""
            }

        return ContentShareDataModel(
            id = feedXCard.id,
            name = feedXCard.author.name,
            tnTitle = (
                String.format(
                    context?.getString(kolR.string.feed_share_title) ?: "",
                    feedXCard.author.name
                )
                ),
            tnImage = mediaUrl,
            ogUrl = mediaUrl
        )
    }

    private fun getContentShareDataModel(product: ProductPostTagModelNew) =
        ContentShareDataModel(
            id = product.id,
            name = product.shopName,
            tnTitle = (
                String.format(
                    context?.getString(kolR.string.feed_share_title) ?: "",
                    product.shopName
                )
                ),
            tnImage = product.imgUrl,
            ogUrl = product.imgUrl
        )

    private fun showUniversalShareBottomSheet(contentShareDataModel: ContentShareDataModel) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@ContentDetailFragment)
            setUtmCampaignData(
                pageName = "Content Detail Page",
                userSession.userId,
                contentShareDataModel.id,
                "share"
            )

            setMetaData(
                tnTitle = contentShareDataModel.tnTitle,
                tnImage = contentShareDataModel.tnImage
            )
        }
        universalShareBottomSheet?.setOnDismissListener {
            if (dissmisByGreyArea) {
                if (adapter.getList().size > rowNumberWhenShareClicked) {
                    val card = adapter.getList()[rowNumberWhenShareClicked]
                    analyticsTracker.sendClickGreyAreaShareBottomSheet(
                        getContentDetailAnalyticsData(
                            card
                        )
                    )
                }
            } else {
                dissmisByGreyArea = true
            }
        }
        universalShareBottomSheet?.let {
            if (!it.isAdded) {
                it.show(fragmentManager, this, null)
            }
        }
    }

    private fun onShareProduct(
        item: ProductPostTagModelNew
    ) {
        rowNumberWhenShareClicked = item.positionInFeed
        if (!item.isFollowed && item.postType == TYPE_FEED_X_CARD_POST && item.mediaType == TYPE_IMAGE) {
            analyticsTracker.sendClickShareProductSgcRecommEvent(
                ContentDetailPageAnalyticsDataModel(
                    activityId = item.postId,
                    shopId = item.shopId,
                    isFollowed = item.isFollowed,
                    type = item.postType,
                    productId = item.id,
                    source = contentDetailSource,
                    authorType = ""
                )
            )
        } else {
            analyticsTracker.sendClickShareSgcImageBottomSheet(
                ContentDetailPageAnalyticsDataModel(
                    activityId = item.postId,
                    shopId = item.shopId,
                    isFollowed = item.isFollowed,
                    type = item.postType,
                    productId = item.id,
                    mediaType = item.mediaType,
                    trackerId = getTrackerID(
                        item,
                        trackerIdAsgcRecom = "34094",
                        trackerIdVod = "34166",
                        trackerIdVodRecomm = "34185",
                        trackerIdAsgc = "34106",
                        trackerIdLongVideo = "34517",
                        trackerIdLongVideoRecomm = "34536"
                    ),
                    source = contentDetailSource,
                    authorType = ""
                )
            )
        }
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        val desc = context?.getString(feedComponentR.string.cdp_share_default_text)?.let {
            String.format(
                it,
                item.product.name,
                "%s",
                item.shopName,
                item.priceFmt
            )
        }

        activity?.let {
            val linkerBuilder = LinkerData.Builder.getLinkerBuilder()
                .setId(item.id)
                .setName(item.text)
                .setDescription(desc)
                .setImgUri(item.imgUrl)
                .setUri(item.weblink)
                .setDeepLink(item.applink)
                .setType(LinkerData.FEED_TYPE)
                .setDesktopUrl(item.weblink)

            shareData = linkerBuilder.build()
            isShareFromProductBottomSheet = true
            showUniversalShareBottomSheet(getContentShareDataModel(item))
        }
    }

    override fun onFollowUnfollowClicked(
        feedXCard: FeedXCard,
        postPosition: Int,
        isFollowedFromRSRestrictionBottomSheet: Boolean
    ) {
        if (!feedXCard.followers.isFollowed) {
            analyticsTracker.sendClickFollowAsgcRecomEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    postPosition,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdAsgcRecom = "34086",
                        trackerIdVodRecomm = "34169",
                        trackerIdSgcRecom = "34271",
                        trackerIdSgcVideo = "34603",
                        trackerIdLongVideoRecomm = "34520"
                    )
                )
            )
        }

        if (feedXCard.isTypeUGC) {
            viewModel.followUnFollowUser(
                isFollow = feedXCard.followers.isFollowed,
                encryptedUserID = visitedUserEncryptedID,
                currentPosition = postPosition
            )
        } else {
            viewModel.followShop(
                shopId = feedXCard.author.id,
                action = ShopFollowAction.getFollowAction(feedXCard.followers.isFollowed),
                rowNumber = postPosition,
                isFollowedFromRSRestrictionBottomSheet = isFollowedFromRSRestrictionBottomSheet
            )
        }
    }

    override fun onClickOnThreeDots(feedXCard: FeedXCard, postPosition: Int) {
        analyticsTracker.sendClickThreeDotsSgcImageEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = getTrackerID(
                    feedXCard,
                    trackerIdSgc = "33255",
                    trackerIdVod = "34157",
                    trackerIdVodRecomm = "34175",
                    trackerIdSgcRecom = "34285",
                    trackerIdSgcVideo = "34604",
                    trackerIdAsgc = "34111",
                    trackerIdLongVideo = "34508",
                    trackerIdLongVideoRecomm = "34526"
                )
            )
        )
        if (context != null) {
            val sheet = MenuOptionsBottomSheet.newInstance(
                isReportable = feedXCard.reportable,
                canUnfollow = feedXCard.followers.isFollowed,
                isDeletable = feedXCard.deletable
            )
            if (!sheet.isAdded) {
                sheet.show(childFragmentManager, "")
            }
            sheet.onReport = {
                if (feedXCard.isTypeProductHighlight && feedXCard.followers.isFollowed) {
                    analyticsTracker.sendClickThreeDotsMenuLaporkanAsgcEvent(
                        getContentDetailAnalyticsData(feedXCard)
                    )
                } else {
                    analyticsTracker.sendClickReportSgcImageEvent(
                        getContentDetailAnalyticsData(
                            feedXCard,
                            trackerId = getTrackerID(
                                feedXCard,
                                trackerIdSgc = "33293",
                                trackerIdSgcRecom = "34309",
                                trackerIdSgcVideo = "34605"
                            )
                        )
                    )
                }
                analyticsTracker.sendClickThreeDotsMenuSgcImageEvent(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33291",
                            trackerIdVod = "34158",
                            trackerIdVodRecomm = "34176",
                            trackerIdSgcRecom = "34307",
                            trackerIdLongVideo = "34509",
                            trackerIdLongVideoRecomm = "34527"
                        )
                    ),
                    selectedOption = "laporkan"
                )
                if (userSession.isLoggedIn) {
                    context?.let {
                        reportBottomSheet = ReportBottomSheet.newInstance(
                            context = object : ReportBottomSheet.OnReportOptionsClick {
                                override fun onReportAction(
                                    reasonType: String,
                                    reasonDesc: String
                                ) {
                                    analyticsTracker.sendClickReportReasonSgcImageEvent(
                                        getContentDetailAnalyticsData(
                                            feedXCard,
                                            trackerId = getTrackerID(
                                                feedXCard,
                                                trackerIdSgc = "33288",
                                                trackerIdSgcRecom = "34300"
                                            )
                                        ),
                                        reportReason = reasonDesc
                                    )
                                    viewModel.sendReport(
                                        postPosition,
                                        feedXCard.id,
                                        reasonType,
                                        reasonDesc
                                    )
                                }
                            }
                        )
                        reportBottomSheet.onClosedClicked = {
                            analyticsTracker.sendClickXThreeDotsReportSheet(
                                getContentDetailAnalyticsData(
                                    feedXCard,
                                    trackerId = getTrackerID(
                                        feedXCard,
                                        trackerIdSgcRecom = "34299"
                                    )
                                )
                            )
                        }
                        reportBottomSheet.onDismiss = {
                            analyticsTracker.sendClickGreyAreaReportBottomSheet(
                                getContentDetailAnalyticsData(
                                    feedXCard,
                                    trackerId = getTrackerID(
                                        feedXCard,
                                        trackerIdSgc = "33289",
                                        trackerIdSgcRecom = "34301"
                                    )
                                )
                            )
                        }
                        reportBottomSheet.show(
                            childFragmentManager,
                            ""
                        )
                    }
                } else {
                    onGoToLogin()
                }
            }
            sheet.onFollow = {
                analyticsTracker.sendClickThreeDotsMenuSgcImageEvent(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33291",
                            trackerIdVod = "34158",
                            trackerIdSgcRecom = "34307",
                            trackerIdLongVideo = "34509",
                            trackerIdLongVideoRecomm = "34527"
                        )
                    ),
                    selectedOption = "unfollow"
                )
                if (userSession.isLoggedIn) {
                    onFollowUnfollowClicked(
                        feedXCard,
                        postPosition
                    )
                } else {
                    onGoToLogin()
                }
            }
            sheet.onDelete = { createDeleteDialog(feedXCard.id, postPosition) }
            sheet.onDismiss = {
                analyticsTracker.sendClickGreyAreaSgcImageEvent(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33273"
                        )
                    )
                )
                analyticsTracker.sendClickGreyAreaThreeDotsMenu(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33292",
                            trackerIdSgcRecom = "34308"
                        )
                    )
                )
            }
            sheet.onEdit = {
                openEditPostPage(feedXCard.text, postId, feedXCard.author.id)
            }

            sheet.onClosedClicked = {
                analyticsTracker.sendClickXThreeDotsBottomSHeet(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33290",
                            trackerIdSgcRecom = "34306"
                        )
                    )
                )
                analyticsTracker.sendClickCancelReportSgcImageEvent(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33294",
                            trackerIdSgcRecom = "34600"
                        )
                    )
                )
            }
        }
    }

    override fun onFullScreenButtonClicked(
        feedXCard: FeedXCard,
        postPosition: Int,
        currentTime: Long,
        shouldTrack: Boolean,
        isFullScreen: Boolean
    ) {
        if (isFullScreen) {
            if (feedXCard.isTypeVOD) {
                analyticsTracker.sendClickFullScreenVOD(
                    getContentDetailAnalyticsData(feedXCard)
                )
            } else if (feedXCard.isTypeLongVideo) {
                analyticsTracker.sendClickFullScreenLongVideo(
                    getContentDetailAnalyticsData(feedXCard)
                )
            }
        } else {
            if (feedXCard.isTypeVOD) {
                analyticsTracker.sendClickLanjutMenontonVod(
                    getContentDetailAnalyticsData(feedXCard)
                )
            } else if (feedXCard.isTypeLongVideo) {
                analyticsTracker.sendClickLanjutMenontonLongVideo(
                    getContentDetailAnalyticsData(feedXCard)
                )
            }
        }

        val finalApplink = if (!shouldTrack) {
            Uri.parse(feedXCard.appLink)
                .buildUpon()
                .appendQueryParameter(START_TIME, currentTime.toString())
                .appendQueryParameter(SHOULD_TRACK, shouldTrack.toString())
                .build().toString()
        } else {
            Uri.parse(feedXCard.appLink)
                .buildUpon()
                .appendQueryParameter(START_TIME, currentTime.toString())
                .appendQueryParameter(SOURCE_TYPE, VOD_POST)
                .build().toString()
        }
        if (feedXCard.isTypeLongVideo) {
            onVideoPlayerClicked(
                postPosition,
                0,
                feedXCard.id,
                feedXCard.author.type.toString(),
                feedXCard.followers.isFollowed,
                currentTime
            )
        } else {
            onGoToLink(finalApplink)
        }
    }

    override fun onShopHeaderItemClicked(feedXCard: FeedXCard, isShopNameBelow: Boolean) {
        if (isShopNameBelow) {
            analyticsTracker.sendClickShopNameBelowSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdSgc = "33264",
                        trackerIdSgcRecom = "34302"
                    )
                )
            )
        } else {
            analyticsTracker.sendClickShopSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdSgc = "33254",
                        trackerIdSgcRecom = "34272",
                        trackerIdVod = "34151",
                        trackerIdVodRecomm = "34168",
                        trackerIdAsgcRecom = "34085",
                        trackerIdAsgc = "34099",
                        trackerIdSgcVideo = "34602",
                        trackerIdLongVideo = "34502",
                        trackerIdLongVideoRecomm = "34519"
                    )
                )
            )
        }
        onGoToLink(feedXCard.author.appLink)
    }

    override fun addViewsToVOD(
        feedXCard: FeedXCard,
        rowNumber: Int,
        time: Long,
        hitTrackerApi: Boolean
    ) {
        if (hitTrackerApi) {
            if (feedXCard.isTypeLongVideo) {
                viewModel.trackLongVideoView(feedXCard.id, rowNumber)
            } else {
                viewModel.trackVisitChannel(feedXCard.playChannelID, rowNumber)
            }
        }
    }

    override fun onVolumeClicked(feedXCard: FeedXCard, mute: Boolean, mediaType: String) {
        val trackerId = getTrackerID(
            feedXCard,
            trackerIdVod = "34159",
            trackerIdVodRecomm = "34177",
            trackerIdLongVideo = "34510",
            trackerIdLongVideoRecomm = "34528"
        )
        analyticsTracker.sendClickSoundSgcPlayLongVideoEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = trackerId
            )
        )
    }

    override fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long) {
        analyticsTracker.eventWatchVideo(
            getContentDetailAnalyticsData(
                feedXCard,
                duration = duration,
                trackerId = getTrackerID(feedXCard, trackerIdSgcVideo = "34607")
            )
        )
    }

    override fun onSgcVideoTapped(feedXCard: FeedXCard) {
        analyticsTracker.sendClickVideoSgcVideoEvent(getContentDetailAnalyticsData(feedXCard))
    }

    override fun sendWatchVODTracker(feedXCard: FeedXCard, duration: Long) {
        analyticsTracker.eventWatchVideo(
            getContentDetailAnalyticsData(
                feedXCard,
                duration = duration,
                trackerId = getTrackerID(
                    feedXCard,
                    trackerIdVod = "34187",
                    trackerIdVodRecomm = "34189",
                    trackerIdLongVideo = "34538",
                    trackerIdLongVideoRecomm = "34540"
                )
            )
        )
    }

    override fun onIngatkanSayaBtnImpressed(card: FeedXCard, positionInFeed: Int) {
        viewModel.checkUpcomingCampaignInitialReminderStatus(
            card.campaign.campaignId,
            positionInFeed
        )
    }

    override fun onIngatkanSayaBtnClicked(card: FeedXCard, positionInFeed: Int) {
        viewModel.setUnsetReminder(card.campaign, positionInFeed)
    }

    override fun changeUpcomingWidgetToOngoing(card: FeedXCard, positionInFeed: Int) {
        if (::productTagBS.isInitialized) {
            productTagBS.dismiss()
        }
        viewModel.fetchLatestFeedPostWidgetData(card.id, positionInFeed)
    }

    override fun removeOngoingCampaignSaleWidget(card: FeedXCard, positionInFeed: Int) {
        if (adapter.getList().size > positionInFeed) {
            adapter.getList().removeAt(positionInFeed)
            adapter.notifyItemRemoved(positionInFeed)
        }
    }

    override fun onLihatProdukClicked(
        feedXCard: FeedXCard,
        postPosition: Int,
        products: List<FeedXProduct>
    ) {
        val trackerId = getTrackerID(
            feedXCard,
            trackerIdSgc = "33256",
            trackerIdSgcRecom = "34275",
            trackerIdVod = "34161",
            trackerIdVodRecomm = "34179",
            trackerIdAsgc = "34113",
            trackerIdLongVideo = "34512",
            trackerIdLongVideoRecomm = "34530"
        )
        analyticsTracker.sendClickLihatProdukSgcImageEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = trackerId
            )
        )
        val media =
            if (feedXCard.lastCarouselIndex < feedXCard.media.size) feedXCard.media[feedXCard.lastCarouselIndex] else null
        if (products.isNotEmpty()) {
            productTagBS = ProductItemInfoBottomSheet()
            productTagBS.show(
                childFragmentManager,
                this,
                ProductBottomSheetData(
                    products = products,
                    postId = feedXCard.id,
                    shopId = feedXCard.author.id,
                    postType = feedXCard.typename,
                    isFollowed = feedXCard.followers.isFollowed,
                    positionInFeed = postPosition,
                    playChannelId = feedXCard.playChannelID,
                    shopName = feedXCard.author.name,
                    mediaType = media?.type ?: "",
                    saleStatus = feedXCard.campaign.status,
                    saleType = feedXCard.campaign.name,
                    hasVoucher = feedXCard.hasVoucher,
                    authorType = feedXCard.author.type.toString()
                ),
                viewModelFactory
            )
            productTagBS.closeClicked = {
                analyticsTracker.sendClickXSgcImageEvent(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33266",
                            trackerIdSgcRecom = "34276",
                            trackerIdAsgcRecom = "34088",
                            trackerIdAsgc = "34102"
                        )
                    )
                )
            }
            productTagBS.disMissed = {
                analyticsTracker.sendClickGreyAreaProductBottomSheet(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33266",
                            trackerIdSgcRecom = "34282",
                            trackerIdAsgcRecom = "34095",
                            trackerIdAsgc = "34107"
                        )
                    )
                )
            }
            if (shouldShowFollowerBottomSheet(feedXCard)) {
                showFollowerBottomSheet(postPosition, feedXCard.campaign.status)
            }
        }
    }

    override fun onImageClicked(feedXCard: FeedXCard) {
        analyticsTracker.sendClickImageSgcImageEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = getTrackerID(
                    feedXCard,
                    trackerIdSgc = "33257",
                    trackerIdSgcRecom = "34273"
                )
            )
        )
    }

    override fun onReadMoreClicked(feedXCard: FeedXCard) {
        val trackerId = getTrackerID(
            feedXCard,
            trackerIdSgc = "33265",
            trackerIdVod = "34156",
            trackerIdVodRecomm = "34174",
            trackerIdSgcRecom = "34303",
            trackerIdLongVideo = "34507",
            trackerIdLongVideoRecomm = "34525"
        )
        analyticsTracker.sendClickLihatSelengkapnyaSgcImageEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = trackerId
            )
        )
    }

    override fun onHashtagClicked(hashTag: String, feedXCard: FeedXCard) {
        analyticsTracker.sendClickHashtagSgcImageEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                hashTag = hashTag,
                trackerId = getTrackerID(feedXCard, trackerIdSgc = "33295")
            )
        )
        val encodeHashtag = URLEncoder.encode(hashTag, "UTF-8")
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
        intent.putExtra(ARG_IS_FROM_CONTENT_DETAIL_PAGE, true)
        intent.putExtra(CONTENT_DETAIL_PAGE_SOURCE, contentDetailSource)
        intent.putExtra(ARG_HASHTAG, hashTag)
        startActivity(intent)
    }

    override fun onCekSekarangButtonClicked(feedXCard: FeedXCard, postPosition: Int) {
        analyticsTracker.sendAsgcMoreProductClicked(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = getTrackerID(
                    feedXCard,
                    trackerIdAsgcRecom = "34087",
                    trackerIdAsgc = "34110"
                )
            )
        )
        val authorType =
            if (feedXCard.author.type == AUTHOR_USER_TYPE_VALUE) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP

        val intent = RouteManager.getIntent(context, feedXCard.appLinkProductList)
        intent.putExtra(
            ContentDetailArgumentModel.IS_FOLLOWED,
            feedXCard.followers.isFollowed
        )
        intent.putExtra(ContentDetailArgumentModel.PARAM_SHOP_ID, feedXCard.author.id)
        intent.putExtra(ContentDetailArgumentModel.SHOP_NAME, feedXCard.author.name)
        intent.putExtra(ContentDetailArgumentModel.PARAM_ACTIVITY_ID, postId)
        intent.putExtra(ContentDetailArgumentModel.PARAM_POST_TYPE, feedXCard.typename)
        intent.putExtra(PARAM_POST_POSITION, postPosition)
        intent.putExtra(PARAM_AUTHOR_TYPE, authorType)
        intent.putExtra(ContentDetailArgumentModel.PARAM_SALE_TYPE, feedXCard.campaign.name)
        intent.putExtra(ContentDetailArgumentModel.PARAM_SALE_STATUS, feedXCard.campaign.status)
        if (shouldShowFollowerBottomSheet(feedXCard)) {
            startActivityForResult(intent, OPEN_FEED_DETAIL)
        } else {
            startActivity(intent)
        }
    }

    private fun shouldShowFollowerBottomSheet(card: FeedXCard) =
        card.campaign.isRilisanSpl && !card.followers.isFollowed && card.campaign.isRSFollowersRestrictionOn

    override fun onPostTagBubbleClicked(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct
    ) {
        if (adapter.getList().size > positionInFeed) {
            val item = (adapter.getList()[positionInFeed])
            analyticsTracker.sendClickProductTagAsgcEvent(
                getContentDetailAnalyticsData(
                    item,
                    product = postTagItem
                )
            )
        }

        onGoToLink(redirectUrl)
    }

    override fun onCarouselItemImpressed(feedXCard: FeedXCard, postPosition: Int) {
        analyticsTracker.sendImpressionImageSgcImageEvent(
            getContentDetailAnalyticsData(
                feedXCard,
                postPosition,
                trackerId = getTrackerID(
                    feedXCard,
                    trackerIdSgc = "33258",
                    trackerIdSgcRecom = "34274"
                )
            )
        )
    }

    override fun onPostImpressed(feedXCard: FeedXCard, postPosition: Int) {
        if (feedXCard.isTypeVOD) {
            analyticsTracker.sendImpressionPostVOD(
                getContentDetailAnalyticsData(
                    feedXCard,
                    postPosition,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdVod = "34150",
                        trackerIdVodRecomm = "34167",
                        trackerIdLongVideo = "34501",
                        trackerIdLongVideoRecomm = "34518"
                    )
                )
            )
        } else {
            analyticsTracker.sendImpressionPost(
                getContentDetailAnalyticsData(
                    feedXCard,
                    postPosition,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdSgc = "33274",
                        trackerIdAsgc = "34098",
                        trackerIdAsgcRecom = "34084",
                        trackerIdSgcRecom = "34270",
                        trackerIdSgcVideo = "34601"
                    )
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::productTagBS.isInitialized) {
            productTagBS.onDestroy()
        }
        if (::reportBottomSheet.isInitialized) {
            reportBottomSheet.onDestroy()
        }
    }

    private fun createDeleteDialog(contentId: String, rowNumber: Int) {
        val dialog =
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(kolR.string.feed_delete_post))
        dialog.setDescription(getString(kolR.string.feed_after_delete_cant))
        dialog.setPrimaryCTAText(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
        dialog.setSecondaryCTAText(getString(kolR.string.feed_delete))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            viewModel.deleteContent(contentId, rowNumber)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openEditPostPage(caption: String, postId: String, authorId: String) {
        var createPostViewModel = CreatePostViewModel()
        createPostViewModel.caption = caption
        createPostViewModel.postId = postId
        createPostViewModel.editAuthorId = authorId

        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2
        )
        intent.putExtra(PARAM_TYPE, TYPE_CONTENT_PREVIEW_PAGE)
        intent.putExtra(CreatePostViewModel.TAG, createPostViewModel)
        startActivity(intent)
    }

    private fun onVideoPlayerClicked(
        positionInFeed: Int,
        contentPosition: Int,
        postId: String,
        authorType: String,
        isFollowed: Boolean,
        startTime: Long

    ) {
        // TODO check start time logic
        if (activity != null) {
            val videoDetailIntent =
                RouteManager.getIntent(context, ApplinkConstInternalContent.VIDEO_DETAIL, postId)
            videoDetailIntent.putExtra(PARAM_CALL_SOURCE, PARAM_FEED)
            videoDetailIntent.putExtra(POST_POSITION, positionInFeed)
            videoDetailIntent.putExtra(PARAM_VIDEO_INDEX, contentPosition)
            videoDetailIntent.putExtra(PARAM_VIDEO_AUTHOR_TYPE, authorType)
            videoDetailIntent.putExtra(PARAM_POST_TYPE, "sgc")
            videoDetailIntent.putExtra(PARAM_IS_POST_FOLLOWED, isFollowed)
            videoDetailIntent.putExtra(PARAM_START_TIME, startTime)
            startActivityForResult(videoDetailIntent, OPEN_VIDEO_DETAIL)
        }
    }

    private fun onGoToLink(link: String) {
        context?.let {
            if (!TextUtils.isEmpty(link)) {
                if (RouteManager.isSupportApplink(it, link)) {
                    RouteManager.route(it, link)
                } else {
                    RouteManager.route(
                        it,
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, link)
                    )
                }
            }
        }
    }

    override fun onBottomSheetThreeDotsClicked(
        item: ProductPostTagModelNew,
        context: Context,
        shopId: String
    ) {
        if (item.postType == TYPE_FEED_X_CARD_POST && !item.isFollowed && item.mediaType == TYPE_IMAGE) {
            analyticsTracker.sendClickThreeDotsSgcRecomm(
                ContentDetailPageAnalyticsDataModel(
                    activityId = item.postId,
                    shopId = item.shopId,
                    isFollowed = item.isFollowed,
                    type = item.postType,
                    productId = item.id,
                    mediaType = item.mediaType,
                    source = contentDetailSource,
                    authorType = ""
                )
            )
        } else {
            analyticsTracker.sendClickThreeDotsSgcImageEventForBottomSheet(
                ContentDetailPageAnalyticsDataModel(
                    activityId = item.postId,
                    shopId = item.shopId,
                    isFollowed = item.isFollowed,
                    type = item.postType,
                    productId = item.id,
                    mediaType = item.mediaType,
                    trackerId = getTrackerID(
                        item,
                        trackerIdSgc = "33269",
                        trackerIdAsgcRecom = "34091",
                        trackerIdVod = "34157",
                        trackerIdVodRecomm = "34182",
                        trackerIdAsgc = "34103",
                        trackerIdLongVideoRecomm = "34533"
                    ),
                    source = contentDetailSource,
                    authorType = ""
                )
            )
        }
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId
        val bundle = Bundle()
        bundle.putBoolean("isLogin", userSession.isLoggedIn)
        val sheet = ProductActionBottomSheet.newInstance(bundle)
        if (!sheet.isAdded) {
            sheet.show(childFragmentManager, "")
        }
        sheet.shareProductCB = {
            onShareProduct(
                item
            )
        }
        sheet.addToCartCB = {
            onTagSheetItemBuy(
                finalID,
                item.positionInFeed,
                item.product,
                item.shopId,
                item.postType,
                item.isFollowed,
                item.playChannelId,
                item.shopName,
                item.mediaType
            )
        }
        sheet.addToWIshListCB = {
            addToWishList(
                finalID,
                item.id,
                item.postType,
                item.isFollowed,
                item.shopId,
                item.playChannelId,
                item.mediaType,
                item.positionInFeed
            )
        }
    }

    override fun onTaggedProductCardImpressed(
        activityId: String,
        postTagItemList: List<FeedXProduct>,
        type: String,
        shopId: String,
        isFollowed: Boolean,
        mediaType: String,
        hasVoucher: Boolean,
        authorType: String
    ) {
        analyticsTracker.sendImpressionProductSgcImageEvent(
            ContentDetailPageAnalyticsDataModel(
                activityId = activityId,
                type = type,
                shopId = shopId,
                isFollowed = isFollowed,
                mediaType = mediaType,
                productId = postTagItemList.firstOrNull()?.id ?: "",
                trackerId = getTrackerID(
                    ProductPostTagModelNew(
                        postType = type,
                        mediaType = mediaType,
                        isFollowed = isFollowed
                    ),
                    trackerIdSgc = "33267",
                    trackerIdVod = "34162",
                    trackerIdVodRecomm = "34180",
                    trackerIdSgcRecom = "34277",
                    trackerIdSgcVideo = "34609",
                    trackerIdAsgc = "34100",
                    trackerIdAsgcRecom = "34089",
                    trackerIdLongVideo = "34513",
                    trackerIdLongVideoRecomm = "34531"
                ),
                source = contentDetailSource,
                authorType = authorType
            ),
            postTagItemList
        )
    }

    override fun onTaggedProductCardClicked(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        itemPosition: Int,
        mediaType: String
    ) {
        if (adapter.getList().size > positionInFeed) {
            val item = adapter.getList()[positionInFeed]
            if (item.tags.isNotEmpty()) {
                analyticsTracker.sendClickProductSgcImageEvent(
                    ContentDetailPageAnalyticsDataModel(
                        activityId = item.id,
                        feedXProduct = postTagItem,
                        isFollowed = item.followers.isFollowed,
                        shopId = item.author.id,
                        type = item.typename,
                        productId = postTagItem.id,
                        mediaType = mediaType,
                        trackerId = getTrackerID(
                            item,
                            trackerIdSgc = "33268",
                            trackerIdVod = "34163",
                            trackerIdVodRecomm = "34181",
                            trackerIdSgcRecom = "34278",
                            trackerIdSgcVideo = "34610",
                            trackerIdAsgcRecom = "34090",
                            trackerIdAsgc = "34101",
                            trackerIdLongVideo = "34514",
                            trackerIdLongVideoRecomm = "34532"
                        ),
                        source = contentDetailSource,
                        authorType = ""
                    )
                )
            }
        }

        onGoToLink(redirectUrl)
    }

    override fun onAddToWishlistButtonClicked(item: ProductPostTagModelNew, rowNumber: Int) {
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId.toString()

        addToWishList(
            finalID,
            item.id,
            item.postType,
            item.isFollowed,
            item.shopId,
            item.playChannelId,
            item.mediaType,
            item.positionInFeed
        )
    }

    override fun onAddToCartButtonClicked(item: ProductPostTagModelNew) {
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId.toString()

        val list = adapter.getList()
        val position = item.positionInFeed
        var card: FeedXCard? = null
        if (position in 0 until list.size) {
            card = list[position]
        }
        if (card != null && shouldShowFollowerBottomSheet(card)) {
            showFollowerBottomSheet(item.positionInFeed, card.campaign.status)
        } else {
            onTagSheetItemBuy(
                finalID,
                item.positionInFeed,
                item.product,
                item.shopId,
                item.postType,
                item.isFollowed,
                item.playChannelId,
                item.shopName,
                item.mediaType
            )
        }
    }

    private fun onTagSheetItemBuy(
        activityId: String,
        positionInFeed: Int,
        postTagItem: FeedXProduct,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        playChannelId: String,
        shopName: String,
        mediaType: String
    ) {
        analyticsTracker.sendClickAddToCartAsgcEvent(
            ContentDetailPageAnalyticsDataModel(
                activityId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else activityId,
                rowNumber = positionInFeed,
                feedXProduct = postTagItem,
                productId = postTagItem.id,
                type = type,
                shopId = shopId,
                isFollowed = isFollowed,
                shopName = shopName,
                mediaType = mediaType,
                trackerId = getTrackerID(
                    ProductPostTagModelNew(
                        postType = type,
                        mediaType = type,
                        isFollowed = isFollowed
                    ),
                    trackerIdAsgc = "34112",
                    trackerIdVod = "34165",
                    trackerIdVodRecomm = "34184",
                    trackerIdLongVideo = "34516",
                    trackerIdLongVideoRecomm = "34535"

                ),
                source = contentDetailSource,
                authorType = ""
            )
        )
        if (userSession.isLoggedIn) {
            if (::productTagBS.isInitialized) {
                productTagBS.dismissedByClosing = true
                productTagBS.dismiss()
            }
            viewModel.addToCart(
                postTagItem.id,
                postTagItem.productName,
                postTagItem.price.toString(),
                shopId
            )
        } else {
            onGoToLogin()
        }
    }

    private fun addToWishList(
        postId: String,
        productId: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        playChannelId: String,
        mediaType: String,
        rowNumber: Int
    ) {
        if (type == TYPE_FEED_X_CARD_POST && !isFollowed && mediaType == TYPE_IMAGE) {
            analyticsTracker.sendClickWishlistProductSgcRecommEvent(
                ContentDetailPageAnalyticsDataModel(
                    activityId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId,
                    type = type,
                    isFollowed = isFollowed,
                    mediaType = mediaType,
                    productId = productId,
                    shopId = shopId,
                    source = contentDetailSource,
                    authorType = ""
                )
            )
        } else {
            analyticsTracker.sendClickWishlistProductEvent(
                ContentDetailPageAnalyticsDataModel(
                    activityId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId,
                    type = type,
                    isFollowed = isFollowed,
                    mediaType = mediaType,
                    productId = productId,
                    shopId = shopId,
                    trackerId = getTrackerID(
                        ProductPostTagModelNew(
                            postType = type,
                            mediaType = mediaType,
                            isFollowed = isFollowed
                        ),
                        trackerIdSgc = "33270",
                        trackerIdVod = "34164",
                        trackerIdVodRecomm = "34183",
                        trackerIdSgcVideo = "34611",
                        trackerIdAsgc = "34104",
                        trackerIdAsgcRecom = "34092",
                        trackerIdLongVideo = "34515",
                        trackerIdLongVideoRecomm = "34534"
                    ),
                    source = contentDetailSource,
                    authorType = ""
                )
            )
        }
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        viewModel.addToWishlist(productId, rowNumber)
    }

    private fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun onSuccessDeletePost(rowNumber: Int) {
        if (contentDetailSource == SOURCE_USER_PROFILE) {
            (activity as ContentDetailActivity).setActionToRefresh(true)
        }
        if (adapter.getList().size > rowNumber) {
            adapter.getList().removeAt(rowNumber)
            adapter.notifyItemRemoved(rowNumber)
            Toaster.build(
                requireView(),
                getString(kolR.string.feed_post_deleted),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(com.tokopedia.kolcommon.R.string.content_action_ok)
            ).show()
        }
    }

    private fun showFollowerBottomSheet(positionInFeed: Int, campaignStatus: String) {
        feedFollowersOnlyBottomSheet =
            FeedFollowersOnlyBottomSheet.getOrCreate(childFragmentManager)
        feedFollowersOnlyBottomSheet?.show(
            childFragmentManager,
            this,
            positionInFeed,
            status = campaignStatus
        )
    }

    private fun onSuccessFetchStatusCampaignReminderButton(
        data: FeedAsgcCampaignResponseModel,
        shouldShowToaster: Boolean = false
    ) {
        val newList = adapter.getList()
        val rowNumber = data.rowNumber
        if (rowNumber in 0 until newList.size) {
            val card = newList[rowNumber]
            val campaign = card.campaign
            if (campaign.campaignId == data.campaignId) {
                campaign.reminder = data.reminderStatus
            }
            if (shouldShowToaster) {
                showToastOnSuccessReminderSetForFSTorRS(card)
            }

            val reminderBtnPayload = Bundle().apply {
                putBoolean(ContentDetailPostViewHolder.PAYLOAD_REMINDER_BTN_STATUS_UPDATED, true)
            }
            adapter.notifyItemChanged(rowNumber, reminderBtnPayload)
        }
    }

    private fun showToastOnSuccessReminderSetForFSTorRS(card: FeedXCard) {
        when {
            card.campaign.reminder is FeedASGCUpcomingReminderStatus.On && card.campaign.isFlashSaleToko -> showToast(
                getString(com.tokopedia.feedcomponent.R.string.feed_asgc_reminder_activate_fst_message),
                Toaster.TYPE_NORMAL
            )
            card.campaign.reminder is FeedASGCUpcomingReminderStatus.On && card.campaign.isRilisanSpl -> showToast(
                getString(com.tokopedia.feedcomponent.R.string.feed_asgc_reminder_activate_rs_message),
                Toaster.TYPE_NORMAL
            )
            card.campaign.reminder is FeedASGCUpcomingReminderStatus.Off -> showToast(
                getString(
                    com.tokopedia.feedcomponent.R.string.feed_asgc_reminder_deactivate_message
                ),
                Toaster.TYPE_NORMAL
            )
        }
    }

    private fun onSuccessFetchLatestFeedWidgetData(data: FeedXCard, rowNumber: Int) {
        val newList = adapter.getList()
        if (newList.size > rowNumber) {
            newList[rowNumber] = data
        }
        adapter.setItemsAndAnimateChanges(newList)
    }

    //region observer
    private fun observeWishlist() {
        viewModel.observableWishlist.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ContentDetailResult.Success -> onWishListSuccess(
                    it.data.rowNumber,
                    it.data.productId
                )
                is ContentDetailResult.Failure -> {
                    val errorMessage = ErrorHandler.getErrorMessage(requireContext(), it.error)
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(
                        errorMessage,
                        requireView()
                    )
                }
            }
        }
    }

    private fun observeFollowUnfollow() {
        viewModel.followShopObservable.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ContentDetailResult.Success -> {
                    if (it.data.isFollowedFromRSRestrictionBottomSheet && it.data.action.isFollowing) {
                        if (::productTagBS.isInitialized) {
                            productTagBS.showToasterOnBottomSheetOnSuccessFollow(
                                getString(com.tokopedia.feedcomponent.R.string.feed_follow_bottom_sheet_success_toaster_text),
                                Toaster.TYPE_NORMAL,
                                getString(com.tokopedia.feedcomponent.R.string.feed_asgc_campaign_toaster_action_text)
                            )
                            feedFollowersOnlyBottomSheet?.dismiss()
                        }
                    }
                    onSuccessFollowShop(it.data)
                }
                is ContentDetailResult.Failure -> {
                    when (it.error) {
                        is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                            showNoInterNetDialog(requireContext())
                        }
                        else -> {
                            val errorMessage = if (it.error is CustomUiMessageThrowable) {
                                requireContext().getString(it.error.errorMessageId)
                            } else {
                                ErrorHandler.getErrorMessage(requireContext(), it.error)
                            }

                            Toaster.build(
                                requireView(),
                                errorMessage,
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(com.tokopedia.abstraction.R.string.title_try_again)
                            ) { view ->
                                it.onRetry()
                            }
                                .show()
                        }
                    }
                }
            }
        }
        viewModel.followUserObservable.observe(viewLifecycleOwner) {
            when (it) {
                is ContentDetailResult.Success -> {
                    (activity as ContentDetailActivity).setActionToRefresh(true)
                    currentPosition = it.data.currentPosition

                    val toastMessage = if (it.data.isFollow) {
                        getString(com.tokopedia.feedcomponent.R.string.feed_unfollow_ugc_success_toaster_text)
                    } else {
                        getString(com.tokopedia.feedcomponent.R.string.feed_follow_ugc_success_toaster_text)
                    }
                    showToast(toastMessage, Toaster.TYPE_NORMAL)
                }
                is ContentDetailResult.Failure -> {
                    when (it.error) {
                        is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                            showNoInterNetDialog(requireContext())
                        }
                        else -> {
                            val errorMessage = if (it.error is CustomUiMessageThrowable) {
                                requireContext().getString(it.error.errorMessageId)
                            } else {
                                ErrorHandler.getErrorMessage(requireContext(), it.error)
                            }

                            Toaster.build(
                                requireView(),
                                errorMessage,
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(com.tokopedia.abstraction.R.string.title_try_again)
                            ) { _ ->
                                it.onRetry()
                            }.show()
                        }
                    }
                }
                ContentDetailResult.Loading -> {}
            }
        }
    }

    private fun observeDeleteContent() {
        viewModel.deletePostResp.observe(viewLifecycleOwner) {
            when (it) {
                ContentDetailResult.Loading -> {
                    // todo: add loading state?
                }
                is ContentDetailResult.Success -> onSuccessDeletePost(it.data.rowNumber)
                is ContentDetailResult.Failure -> {
                    when (it.error) {
                        is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                            showNoInterNetDialog(requireContext())
                        }
                        else -> {
                            val errorMessage =
                                ErrorHandler.getErrorMessage(requireContext(), it.error)
                            Toaster.build(
                                requireView(),
                                errorMessage,
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(com.tokopedia.abstraction.R.string.title_try_again)
                            ) { view ->
                                it.onRetry()
                            }
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun observeReportContent() {
        viewModel.reportResponse.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                ContentDetailResult.Loading -> {
                    // todo: add loading state?
                }
                is ContentDetailResult.Success -> {
                    reportBottomSheet.setFinalView()
                    onSuccessDeletePost(it.data.rowNumber)
                }
                is ContentDetailResult.Failure -> {
                    reportBottomSheet.dismiss()
                    when (it.error) {
                        is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                            showNoInterNetDialog(requireContext())
                        }
                        else -> {
                            val errorMessage =
                                ErrorHandler.getErrorMessage(requireContext(), it.error)
                            Toaster.build(
                                requireView(),
                                errorMessage,
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(com.tokopedia.abstraction.R.string.title_try_again)
                            ) { view ->
                                it.onRetry()
                            }
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun observeVideoViewData() {
        viewModel.vodViewData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                ContentDetailResult.Loading -> {}
                is ContentDetailResult.Success -> onSuccessAddViewVODPost(it.data.rowNumber)
                is ContentDetailResult.Failure -> {
                    // TODO fail case
                }
            }
        }
    }

    private fun observeLikeContent() {
        viewModel.getLikeKolResp.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                ContentDetailResult.Loading -> {}
                is ContentDetailResult.Success -> {
                    onSuccessLikeDislikeKolPost(it.data.rowNumber)
                }
                is ContentDetailResult.Failure -> {
                    when (it.error) {
                        is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                            showNoInterNetDialog(requireContext())
                        }
                        else -> {
                            val errorMessage = if (it.error is CustomUiMessageThrowable) {
                                requireContext().getString(it.error.errorMessageId)
                            } else {
                                ErrorHandler.getErrorMessage(requireContext(), it.error)
                            }
                            showToast(errorMessage, Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
        }
    }

    private fun onWishListSuccess(
        rowNumber: Int,
        productId: String
    ) {
        val list = adapter.getList()
        if (rowNumber in 0 until list.size) {
            val feedXCard = list[rowNumber]
            Toaster.build(
                requireView(),
                getString(wishlistR.string.on_success_add_to_wishlist_msg),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(wishlistR.string.cta_success_add_to_wishlist),
                View.OnClickListener {
                    analyticsTracker.sendClickLihatWishlistSgcImageEvent(
                        ContentDetailPageAnalyticsDataModel(
                            activityId = feedXCard.id,
                            type = feedXCard.typename,
                            isFollowed = feedXCard.followers.isFollowed,
                            shopId = feedXCard.author.id,
                            productId = productId,
                            mediaType = if (feedXCard.lastCarouselIndex < feedXCard.media.size) feedXCard.media[feedXCard.lastCarouselIndex].type else "",
                            source = contentDetailSource,
                            trackerId = getTrackerID(
                                feedXCard,
                                trackerIdSgc = "33271",
                                trackerIdAsgc = "34105",
                                trackerIdAsgcRecom = "34093"
                            ),
                            authorType = feedXCard.author.type.toString()
                        )
                    )
                    RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
                }
            ).show()
            // row number is set to 0 as asgc product bottomsheet always has 1 product
            productTagBS.changeWishlistIconOnWishlistSuccess(0)
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        dissmisByGreyArea = false
        if (adapter.getList().size > rowNumberWhenShareClicked) {
            val card = adapter.getList()[rowNumberWhenShareClicked]
            if (!isShareFromProductBottomSheet) {
                analyticsTracker.sendClickShareOptionInShareBottomSheet(
                    getContentDetailAnalyticsData(
                        card,
                        shareMedia = shareModel.socialMediaName ?: ""
                    )
                )
            }
            // hit only for sgc
            if (isShareFromProductBottomSheet) {
                isShareFromProductBottomSheet = false
                analyticsTracker.sendClickShareSgcImageBottomSheet(
                    getContentDetailAnalyticsData(
                        card,
                        trackerId = getTrackerID(
                            card,
                            trackerIdSgc = "33272"
                        ),
                        shareMedia = shareModel.socialMediaName ?: ""
                    )
                )
            }
            universalShareBottomSheet?.dismiss()
        }

        val linkerShareData = DataMapper().getLinkerShareData(shareData)
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0, linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        context?.let {
                            var shareString =
                                if (shareData.description.contains("%s")) {
                                    String.format(
                                        shareData.description,
                                        linkerShareData?.shareUri ?: ""
                                    )
                                } else {
                                    shareData.description + "\n" + linkerShareData?.shareUri
                                }

                            SharingUtil.executeShareIntent(
                                shareModel,
                                linkerShareData,
                                activity,
                                view,
                                shareString
                            )

                            universalShareBottomSheet?.dismiss()
                        }
                    }

                    override fun onError(linkerError: LinkerError?) {
                        // Most of the error cases are already handled for you. Let me know if you want to add your own error handling.
                    }
                }
            )
        )
    }

    override fun onCloseOptionClicked() {
        dissmisByGreyArea = false
        if (adapter.getList().size > rowNumberWhenShareClicked) {
            val card = adapter.getList()[rowNumberWhenShareClicked]
            analyticsTracker.sendClickXShareDetailPage(getContentDetailAnalyticsData(card))
        }
    }

    private fun getContentDetailAnalyticsData(
        feedXCard: FeedXCard,
        postPosition: Int = 0,
        trackerId: String = "",
        hashTag: String = "",
        duration: Long = 0L,
        shareMedia: String = "",
        product: FeedXProduct = FeedXProduct()
    ) = ContentDetailPageAnalyticsDataModel(
        activityId = if (feedXCard.isTypeVOD) feedXCard.playChannelID else feedXCard.id,
        shopId = feedXCard.author.id,
        productId = product.id,
        rowNumber = postPosition,
        isFollowed = feedXCard.followers.isFollowed,
        type = feedXCard.typename,
        mediaType = if (feedXCard.lastCarouselIndex < feedXCard.media.size) feedXCard.media[feedXCard.lastCarouselIndex].type else "",
        trackerId = trackerId,
        hashtag = hashTag,
        mediaUrl = feedXCard.media.firstOrNull()?.mediaUrl ?: "",
        itemName = feedXCard.title,
        duration = duration,
        feedXProduct = product,
        shareMedia = shareMedia,
        source = contentDetailSource,
        authorType = feedXCard.author.type.toString()
    )

    private fun getTrackerID(
        feedXCard: FeedXCard,
        trackerIdSgc: String = "",
        trackerIdSgcRecom: String = "",
        trackerIdAsgc: String = "",
        trackerIdAsgcRecom: String = "",
        trackerIdVod: String = "",
        trackerIdVodRecomm: String = "",
        trackerIdLongVideo: String = "",
        trackerIdLongVideoRecomm: String = "",
        trackerIdSgcVideo: String = ""

    ) = when {
        feedXCard.isTypeVOD && feedXCard.followers.isFollowed -> trackerIdVod
        feedXCard.isTypeLongVideo && feedXCard.followers.isFollowed -> trackerIdLongVideo
        feedXCard.isTypeLongVideo && !feedXCard.followers.isFollowed -> trackerIdLongVideoRecomm
        feedXCard.isTypeVOD && !feedXCard.followers.isFollowed -> trackerIdVodRecomm
        feedXCard.isTypeProductHighlight && feedXCard.followers.isFollowed -> trackerIdAsgc
        feedXCard.isTypeProductHighlight && !feedXCard.followers.isFollowed -> trackerIdAsgcRecom
        feedXCard.isTypeSGC && !feedXCard.isTypeSgcVideo && feedXCard.followers.isFollowed -> trackerIdSgc
        feedXCard.isTypeSGC && !feedXCard.isTypeSgcVideo && !feedXCard.followers.isFollowed -> trackerIdSgcRecom
        feedXCard.isTypeSgcVideo -> trackerIdSgcVideo
        else -> ""
    }

    private fun getTrackerID(
        item: ProductPostTagModelNew,
        trackerIdSgc: String = "",
        trackerIdSgcRecom: String = "",
        trackerIdAsgc: String = "",
        trackerIdAsgcRecom: String = "",
        trackerIdVod: String = "",
        trackerIdVodRecomm: String = "",
        trackerIdLongVideo: String = "",
        trackerIdLongVideoRecomm: String = "",
        trackerIdSgcVideo: String = ""
    ) = when {
        item.postType == TYPE_FEED_X_CARD_PLAY && item.isFollowed -> trackerIdVod
        item.postType == TYPE_FEED_X_CARD_PLAY && !item.isFollowed -> trackerIdVodRecomm
        item.postType == TYPE_FEED_X_CARD_POST && item.mediaType == TYPE_LONG_VIDEO -> trackerIdLongVideo
        item.postType == TYPE_FEED_X_CARD_POST && item.mediaType == TYPE_LONG_VIDEO && !item.isFollowed -> trackerIdLongVideoRecomm
        item.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && item.isFollowed -> trackerIdAsgc
        item.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !item.isFollowed -> trackerIdAsgcRecom
        item.postType == TYPE_FEED_X_CARD_POST && item.isFollowed && item.mediaType != TYPE_VIDEO -> trackerIdSgc
        item.postType == TYPE_FEED_X_CARD_POST && !item.isFollowed && item.mediaType != TYPE_VIDEO -> trackerIdSgcRecom
        item.postType == TYPE_FEED_X_CARD_POST && item.mediaType == TYPE_VIDEO -> trackerIdSgcVideo
        else -> ""
    }

    override fun onPause() {
        super.onPause()
        analyticsTracker.sendPendingAnalytics()
    }

    private fun observeAddToCart() {
        viewModel.atcRespData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                ContentDetailResult.Loading -> {
                    // todo: add loading state?
                }
                is ContentDetailResult.Failure -> {
                    val errorMessage =
                        it.error.message
                            ?: getString(networkR.string.default_request_error_unknown)
                    showToast(
                        errorMessage,
                        Toaster.TYPE_ERROR
                    )
                }
                is ContentDetailResult.Success -> {
                    Toaster.build(
                        requireView(),
                        getString(kolR.string.feed_added_to_cart),
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        getString(kolR.string.feed_go_to_cart)
                    ) {
                        onAddToCartSuccess()
                    }.show()
                }
            }
        }
    }

    private fun observeReminderBtnInitialState() {
        viewModel.asgcReminderButtonInitialStatus.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ContentDetailResult.Success -> {
                    onSuccessFetchStatusCampaignReminderButton(it.data)
                }
            }
        }
    }

    private fun observeReminderBtnSetState() {
        viewModel.asgcReminderButtonStatus.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is ContentDetailResult.Success -> {
                    onSuccessFetchStatusCampaignReminderButton(it.data, true)
                }
                is ContentDetailResult.Failure -> {
                    showToast(
                        ErrorHandler.getErrorMessage(context, it.error),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observeFeedWidgetUpdatedData() {
        viewModel.feedWidgetLatestData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Success -> {
                    onSuccessFetchLatestFeedWidgetData(it.data.feedXCard, it.data.rowNumber)
                }
            }
        }
    }

    override fun onFollowClickedFromFollowBottomSheet(position: Int) {
        if (position in 0 until adapter.getList().size) {
            val card = adapter.getList()[position]
            onFollowUnfollowClicked(card, position, isFollowedFromRSRestrictionBottomSheet = true)
        }
    }
    //endregion
}
