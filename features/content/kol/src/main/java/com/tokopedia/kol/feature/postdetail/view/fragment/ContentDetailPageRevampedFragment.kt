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
import androidx.lifecycle.Observer
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
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.mapper.*
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.FeedScrollListenerNew
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopViewModel
import com.tokopedia.kol.feature.postdetail.di.DaggerContentDetailComponent
import com.tokopedia.kol.feature.postdetail.di.module.ContentDetailModule
import com.tokopedia.kol.feature.postdetail.view.activity.ContentDetailActivity
import com.tokopedia.kol.feature.postdetail.view.adapter.ContentDetailPageRevampAdapter
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.ContentDetailPostViewHolder
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailPageAnalyticsDataModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.ARGS_AUTHOR_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.ARGS_IS_POST_FOLLOWED
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.ARGS_POST_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.ARGS_VIDEO
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.COMMENT_ARGS_POSITION
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.COMMENT_ARGS_TOTAL_COMMENT
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.DEFAULT_COMMENT_ARGUMENT_VALUE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.PARAM_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.SHOULD_TRACK
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.SOURCE_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.START_TIME
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.TYPE_CONTENT_PREVIEW_PAGE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.VOD_POST
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampDataUiModel
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailRevampViewModel
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.feedcomponent.R as feedComponentR
import com.tokopedia.kol.R as kolR
import com.tokopedia.network.R as networkR

/**
 * Created by shruti agarwal on 15/06/22
 */

class ContentDetailPageRevampedFragment : BaseDaggerFragment() , ContentDetailPostViewHolder.CDPListener, ShareCallback, ProductItemInfoBottomSheet.Listener{

    private var cdpRecyclerView: RecyclerView? = null
    private var postId = "0"
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var reportBottomSheet: ReportBottomSheet
    private lateinit var productTagBS: ProductItemInfoBottomSheet

    private val adapter = ContentDetailPageRevampAdapter(
        ContentDetailListener = this
    )

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analyticsTracker: ContentDetailNewPageAnalytics

    private lateinit var shareData: LinkerData
    private var shareBottomSheetProduct = false

    private val viewModel: ContentDetailRevampViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(ContentDetailRevampViewModel::class.java)
    }

    companion object {

        const val REQUEST_LOGIN = 345
        private const val OPEN_KOL_COMMENT = 101
        const val OPEN_VIDEO_DETAIL = 1311
        private const val COMMENT_ARGS_SERVER_ERROR_MSG = "ARGS_SERVER_ERROR_MSG"
        private const val SHARE_TYPE = "text/plain"


        @JvmStatic
        fun newInstance(bundle: Bundle?): ContentDetailPageRevampedFragment {
            val fragment = ContentDetailPageRevampedFragment()
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
        postId = arguments?.getString(ContentDetailActivity.PARAM_POST_ID) ?: ContentDetailActivity.DEFAULT_POST_ID
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.run {
            getCDPPostFirstPostData.observe(viewLifecycleOwner, {
                when (it) {
                    is Success -> {
                        onSuccessGetFirstPostCDPData(it.data)
                    }
                    else -> {
                        showToast(getString(feedComponentR.string.feed_video_tab_error_reminder), Toaster.TYPE_ERROR)
                    }
                }
            })
            cDPPostRecomData.observe(viewLifecycleOwner, {
                when (it) {
                    is Success -> {
                        onSuccessGetCDPRecomData(ContentDetailRevampDataUiModel(postList = it.data.posts))
                    }
                    else -> {
                        showToast(getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder), Toaster.TYPE_ERROR)
                    }
                }
            })
            getLikeKolResp.observe(viewLifecycleOwner, { it ->
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessLikeDislikeKolPost(data.rowNumber)
                        } else {
                            data.errorMessage = getString(kolR.string.feed_like_error_message)
                            onErrorLikeDislikeKolPost(data.errorMessage)
                        }
                    }
                    is Fail -> {
                        when (it.throwable.cause) {
                            is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                view?.let { v ->
                                    showNoInterNetDialog(v.context)
                                }
                            }
                            else -> {
                                val message = getString(kolR.string.feed_like_error_message)
                                showToast(message, Toaster.TYPE_ERROR)
                            }
                        }
                    }
                }
            })
            toggleFollowUnfollowResp.observe(viewLifecycleOwner, {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessToggleFavoriteShop(data)
                        } else {
                            data.errorMessage =
                                ErrorHandler.getErrorMessage(context, RuntimeException())
                            onErrorToggleFavoriteShop(data)
                        }
                    }
                    is Fail -> {
                        when (it.throwable.cause) {
                            is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                view?.let { v ->
                                    showNoInterNetDialog(v.context)
                                }
                            }
                            else -> {
                                it.throwable.let { throwable ->
                                    val message = if (throwable is CustomUiMessageThrowable) {
                                        context?.getString(throwable.errorMessageId)
                                    } else {
                                        it.throwable.message
                                            ?: getString(networkR.string.default_request_error_unknown)
                                    }
                                    message?.let { errormessage ->
                                        showToast(errormessage, Toaster.TYPE_ERROR)
                                    }
                                }
                            }
                        }

                    }
                }

            })
            vodViewData.observe(viewLifecycleOwner,  {
                when (it) {
                    is Success -> {
                        onSuccessAddViewVODPost(it.data.rowNumber)
                    }
                    //TODO fail case
                    else ->{}
                }
            })
            longVideoViewTrackResponse.observe(viewLifecycleOwner,  {
                when (it) {
                    is Success -> {
                        onSuccessAddViewVODPost(it.data.rowNumber)
                    }
                    //TODO fail case
                    else ->{}
                }
            })

            atcRespData.observe(viewLifecycleOwner,  {
                when (it) {
                    is Success -> {
                        val data = it.data
                        when {
                            data.isSuccess -> {
                                Toaster.build(
                                    requireView(),
                                    getString(kolR.string.feed_added_to_cart),
                                    Toaster.LENGTH_LONG,
                                    Toaster.TYPE_NORMAL,
                                    getString(kolR.string.feed_go_to_cart),
                                    View.OnClickListener {
                                        onAddToCartSuccess()
                                    }).show()
                            }
                            data.errorMsg.isNotEmpty() -> {
                                showToast(data.errorMsg, Toaster.TYPE_ERROR)
                            }
                            else -> {
                                onAddToCartFailed(data.applink)
                            }
                        }
                    }
                    is Fail -> {
                        Timber.e(it.throwable)
                        val errorMessage = it.throwable.message ?: getString(networkR.string.default_request_error_unknown)
                        showToast(
                            errorMessage,
                            Toaster.TYPE_ERROR
                        )
                    }
                }
            })

            reportResponse.observe(viewLifecycleOwner, {
                when (it) {
                    is Fail -> {
                        when (it.throwable) {
                            is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                                view?.let {
                                    reportBottomSheet.dismiss()
                                    showNoInterNetDialog(it.context)
                                }
                            }
                            else -> {
                                val message = it.throwable.localizedMessage ?: ""
                                showToast(message, Toaster.TYPE_ERROR)
                            }
                        }

                    }
                    is Success -> {
                        reportBottomSheet.setFinalView()
                        onSuccessDeletePost(it.data.rowNumber)
                    }
                }
            })

            deletePostResp.observe(viewLifecycleOwner,  {
                when (it) {
                    is Success -> {
                        val data = it.data
                        if (data.isSuccess) {
                            onSuccessDeletePost(data.rowNumber)
                        } else {
                            data.errorMessage = getString(networkR.string.default_request_error_unknown)
                            onErrorDeletePost(data)
                        }
                    }
                    is Fail -> {
                        val message = getString(networkR.string.default_request_error_unknown)
                        showToast(message, Toaster.TYPE_ERROR)
                    }
                }
            })



        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(kolR.layout.fragment_content_detail_revamp_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cdpRecyclerView = view.findViewById(kolR.id.cdp_recycler_view)
        setupView(view)
        viewModel.getCDPPostDetailFirstData(postId)


        observeWishlist()

    }

    private fun setupView(view: View) {
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
                viewModel.getCDPRecomData(postId)
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
                    //TODO check toaster is as per requirement
                    view?.let {
                        Toaster.build(
                            it,
                            serverErrorMsg ?: "",
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(kolR.string.cta_refresh_feed))
                    }
                } else {
                    onSuccessAddDeleteKolComment(
                        data.getIntExtra(COMMENT_ARGS_POSITION, DEFAULT_COMMENT_ARGUMENT_VALUE),
                        data.getIntExtra(COMMENT_ARGS_TOTAL_COMMENT, 0)
                    )
                }
            }
            else -> {
            }
        }
    }


    private fun onSuccessGetFirstPostCDPData(contentDetailRevampDataUiModel: ContentDetailRevampDataUiModel){
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(viewModel.currentCursor.isNotEmpty())
        adapter.setItemsAndAnimateChanges(contentDetailRevampDataUiModel.postList)
        viewModel.getCDPRecomData(postId)

    }
    private fun onSuccessGetCDPRecomData(contentDetailRevampDataUiModel: ContentDetailRevampDataUiModel){
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(viewModel.currentCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(contentDetailRevampDataUiModel.postList)

    }

    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false)
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        else {
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
        if (isDoubleTap)
            analyticsTracker.sendClickDoubleTapLikeUnlikeSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = trackerId
                ),
                !feedXCard.like.isLiked
            )
        else analyticsTracker.sendClickLikeSgcImageEvent(
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
                    trackerIdAsgc = "34108"
                )
            )
        )

        if (feedXCard.like.isLiked) {
            onUnlikeKolClicked(postPosition, id)
        } else {
            onLikeKolClicked(postPosition, id)
        }
    }

     private fun onLikeKolClicked(
        rowNumber: Int, id: Int
    ) {
        if (userSession.isLoggedIn) {
            viewModel.doLikeKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }

     private fun onUnlikeKolClicked(
        rowNumber: Int, id: Int
    ) {
        if (userSession.isLoggedIn) {
            viewModel.doUnlikeKol(id, rowNumber)
        } else {
            onGoToLogin()
        }
    }
    private fun showNoInterNetDialog(context: Context) {
        val sheet = FeedNetworkErrorBottomSheet.newInstance(false)
        sheet.show(childFragmentManager, "")

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

    private fun onSuccessToggleFavoriteShop(data: FavoriteShopViewModel) {

        val rowNumber = data.rowNumber
        if (rowNumber < adapter.getList().size) {
             val feedXCardData = adapter.getList()[rowNumber]

                feedXCardData.followers.isFollowed = !feedXCardData.followers.isFollowed
                if (!feedXCardData.followers.isFollowed && data.isUnfollowFromShopsMenu) {
                    showToast(
                        getString(com.tokopedia.feedcomponent.R.string.feed_component_unfollow_success_toast),
                        Toaster.TYPE_NORMAL
                    )
                }

                if (feedXCardData.followers.isFollowed)
                    feedXCardData.followers.transitionFollow = true

                val followUnfollowPayload = Bundle().apply {
                    putBoolean(ContentDetailPostViewHolder.IMAGE_POST_FOLLOW_UNFOLLOW, true)
                }
                adapter.notifyItemChanged(
                    rowNumber,
                    followUnfollowPayload
                )

        }
    }

    private fun onSuccessAddViewVODPost(postPosition: Int){
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

    private fun onErrorLikeDislikeKolPost(errorMessage: String) {
        showToast(errorMessage, Toaster.TYPE_ERROR)
    }

    private fun onErrorToggleFavoriteShop(data: FavoriteShopViewModel) {
        Toaster.build(
            requireView(),
            data.errorMessage,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again)
        ) {
            viewModel.doToggleFavoriteShop(
                data.rowNumber,
                data.shopId
            )
        }
    }

    private fun openCommentPage(
        rowNumber: Int, id: Int, authorType: String,
        isVideo: Boolean, isFollowed: Boolean, type: String
    ) {
        val intent = RouteManager.getIntent(
            requireContext(),
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalContent.COMMENT_NEW,
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
        startActivityForResult(intent, OPEN_KOL_COMMENT)
    }

    override fun onCommentClicked(feedXCard: FeedXCard, postPosition : Int, isSeeMoreComment: Boolean) {
        val trackerId = getTrackerID(
            feedXCard,
            trackerIdSgc = "33262",
            trackerIdVod = "34153",
            trackerIdVodRecomm = "34171",
            trackerIdSgcRecom = "34284",
            trackerIdSgcVideo = "34614"
        )
        if (isSeeMoreComment)
            analyticsTracker.sendClickLehatSemuaCommentClick(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = getTrackerID(feedXCard, trackerIdVod = "34154", trackerIdVodRecomm = "34172" )
                )
            )
        else
            analyticsTracker.sendClickCommentSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = trackerId
                )
            )

        val media = feedXCard.media.firstOrNull()
        var authId = ""
        val authorType = feedXCard.author.type
        if (authorType != 1)
            authId = feedXCard.author.id
        openCommentPage(
            rowNumber = postPosition,
            id = feedXCard.id.toIntOrZero(),
            authorType = authId,
            isVideo = media?.isVideo ?: false,
            isFollowed = feedXCard.followers.isFollowed,
            type = media?.type?:""
        )
    }

    override fun onSharePostClicked(feedXCard: FeedXCard, postPosition: Int) {

        val trackerId = getTrackerID(
            feedXCard,
            trackerIdSgc = "33263",
            trackerIdVod = "34155",
            trackerIdAsgcRecom = "34097",
            trackerIdVodRecomm = "34173",
            trackerIdSgcVideo = "34615",
            trackerIdAsgc = "34109"
        )
        analyticsTracker.sendClickShareSgcImageEvent(getContentDetailAnalyticsData(feedXCard, trackerId = trackerId))

        activity?.let {
            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder().setId(id.toString())
                .setName(String.format(getString(kolR.string.feed_share_title), feedXCard.author.name))
                .setDescription(String.format(getString(kolR.string.feed_share_desc_text), feedXCard.author.name))
                .setDesktopUrl(feedXCard.webLink)
                .setType(LinkerData.FEED_TYPE)
                .setImgUri(feedXCard.media.firstOrNull()?.mediaUrl ?: "")
                .setDeepLink(feedXCard.appLink)

                shareBottomSheetProduct = false
                shareDataBuilder.apply {
                    setUri(feedXCard.webLink)
                }

            shareData = shareDataBuilder.build()

            val linkerShareData = DataMapper().getLinkerShareData(shareData)
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    linkerShareData,
                    this
                )
            )
        }

    }

    private fun onShareProduct(
        item: ProductPostTagViewModelNew
    ) {
        analyticsTracker.sendClickShareSgcImageBottomSheet(
            ContentDetailPageAnalyticsDataModel(
                activityId = item.postId.toString(),
                shopId = item.shopId,
                isFollowed = item.isFollowed,
                type = item.postType,
                trackerId = getTrackerID(
                    item,
                    trackerIdSgc = "33272",
                    trackerIdAsgcRecom = "34094",
                    trackerIdVod = "34166",
                    trackerIdVodRecomm = "34185",
                    trackerIdSgcRecom = "34281",
                    trackerIdAsgc = "34106"
                )
            ),
            ""
        )
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        shareBottomSheetProduct = false
        activity?.let {
            val linkerBuilder = LinkerData.Builder.getLinkerBuilder().setId(id.toString())
                .setName(item.text)
                .setDescription(item.description)
                .setImgUri(item.imgUrl)
                .setUri(item.weblink)
                .setDeepLink(item.weblink)
                .setType(LinkerData.FEED_TYPE)
                .setDesktopUrl(item.weblink)

            shareData = linkerBuilder.build()
            val linkerShareData = DataMapper().getLinkerShareData(shareData)
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    linkerShareData,
                    this
                )
            )
        }

    }

    override fun onFollowUnfollowClicked(feedXCard: FeedXCard, postPosition : Int) {
        if (!feedXCard.followers.isFollowed)
            analyticsTracker.sendClickFollowAsgcRecomEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    postPosition,
                    trackerId = getTrackerID(
                        feedXCard,
                        trackerIdAsgcRecom = "34086",
                        trackerIdVodRecomm = "34169",
                        trackerIdSgcRecom = "34271",
                        trackerIdSgcVideo = "34603"
                    )
                )
            )
        viewModel.doToggleFavoriteShop(
            rowNumber = postPosition,
            shopId = feedXCard.author.id,
            feedXCard.followers.isFollowed
        )
    }

    override fun onClickOnThreeDots(feedXCard: FeedXCard, postPosition : Int) {
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
                    trackerIdAsgc = "34111"
                )
            )
        )
        if (context != null) {
            val sheet = MenuOptionsBottomSheet.newInstance(
                feedXCard.reportable, feedXCard.followers.isFollowed,
                feedXCard.deletable
            )
            sheet.show(childFragmentManager, "")
            sheet.onReport = {
                if (feedXCard.isTypeProductHighlight && feedXCard.followers.isFollowed)
                    analyticsTracker.sendClickThreeDotsMenuLaporkanAsgcEvent(
                        getContentDetailAnalyticsData(feedXCard)
                    )
                else
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
                analyticsTracker.sendClickThreeDotsMenuSgcImageEvent(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33291",
                            trackerIdVod = "34158",
                            trackerIdVodRecomm = "34176",
                            trackerIdSgcRecom = "34307"
                        )
                    ), selectedOption = "laporkan"
                )
                if (userSession.isLoggedIn) {
                    context?.let {
                        reportBottomSheet = ReportBottomSheet.newInstance(
                            feedXCard.id.toIntOrZero(),
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
                                        feedXCard.id.toIntOrZero(),
                                        reasonType,
                                        reasonDesc,
                                        "content"
                                    )
                                }
                            })
                        reportBottomSheet.onClosedClicked = {
                            analyticsTracker.sendClickCancelReportSgcImageEvent(
                                getContentDetailAnalyticsData(
                                    feedXCard,
                                    trackerId = getTrackerID(
                                        feedXCard,
                                        trackerIdSgcRecom = getTrackerID(
                                            feedXCard,
                                            trackerIdSgc = "34299"
                                        )
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
                                        trackerIdSgcRecom = "34301",
                                    )
                                ),
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
                            trackerIdSgcRecom = "34307"
                        )
                    ), selectedOption = "unfollow"
                )
                if (userSession.isLoggedIn)
                    onFollowUnfollowClicked(
                        feedXCard,
                        postPosition
                    ) else onGoToLogin()
            }
            sheet.onDelete = {
                val media =
                    if (feedXCard.media.size > feedXCard.lastCarouselIndex) feedXCard.media[feedXCard.lastCarouselIndex] else null
                createDeleteDialog(feedXCard.id.toIntOrZero(), postPosition)

            }
            sheet.onDismiss = {
                analyticsTracker.sendClickGreyAreaSgcImageEvent(
                    getContentDetailAnalyticsData(
                        feedXCard,
                        trackerId = getTrackerID(
                            feedXCard,
                            trackerIdSgc = "33273",
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
                            feedXCard, trackerIdSgc = "33294", trackerIdSgcRecom = "34600"
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

        if (isFullScreen)
            analyticsTracker.sendClickFullScreen(
                getContentDetailAnalyticsData(feedXCard)
            )
        else
            analyticsTracker.sendClickLanjutMenontonVod(
                getContentDetailAnalyticsData(feedXCard)
            )

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
        if (feedXCard.isTypeLongVideo)
            onVideoPlayerClicked(
                postPosition,
                0,
                feedXCard.id,
                feedXCard.author.type.toString(),
                feedXCard.followers.isFollowed,
                currentTime
            )
        else
            onGoToLink(finalApplink)
    }

    override fun onShopHeaderItemClicked(feedXCard: FeedXCard, isShopNameBelow: Boolean) {
        if (isShopNameBelow)
            analyticsTracker.sendClickShopNameBelowSgcImageEvent(
                getContentDetailAnalyticsData(
                    feedXCard,
                    trackerId = getTrackerID(
                        feedXCard, trackerIdSgc = "33264", trackerIdSgcRecom = "34302"
                )
            )
            )
        else
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
                        trackerIdSgcVideo = "34602"
                    )
                )
            )
        onGoToLink(feedXCard.appLink)
    }

    override fun addViewsToVOD(
        feedXCard: FeedXCard,
        rowNumber: Int,
        time: Long,
        hitTrackerApi: Boolean
    ) {
        analyticsTracker.eventWatchVideo(
            getContentDetailAnalyticsData(
                feedXCard,
                trackerId = getTrackerID(
                    feedXCard,
                    trackerIdVod = "34187",
                    trackerIdVodRecomm = "34189"
                )
            )
        )
        if (hitTrackerApi) {
            if (feedXCard.isTypeLongVideo)
                viewModel.trackLongVideoView(feedXCard.id, rowNumber)
            else
                viewModel.trackVisitChannel(feedXCard.playChannelID, rowNumber)
        }
    }

    override fun onVolumeClicked(feedXCard: FeedXCard, mute: Boolean, mediaType: String) {
        val  trackerId = getTrackerID(feedXCard, trackerIdVod = "34159",trackerIdVodRecomm = "34177")
        analyticsTracker.sendClickSoundSgcPlayLongVideoEvent(getContentDetailAnalyticsData(feedXCard, trackerId = trackerId))
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
            trackerIdAsgc = "34113"
        )
        analyticsTracker.sendClickLihatProdukSgcImageEvent(getContentDetailAnalyticsData(feedXCard, trackerId = trackerId))
        val media =
            if (feedXCard.lastCarouselIndex < feedXCard.media.size) feedXCard.media[feedXCard.lastCarouselIndex] else null
        if (products.isNotEmpty()) {
            productTagBS = ProductItemInfoBottomSheet()
            productTagBS.show(
                childFragmentManager,
                products,
                this,
                feedXCard.id.toIntOrZero(),
                feedXCard.author.id,
                feedXCard.typename,
                feedXCard.followers.isFollowed,
                postPosition,
                feedXCard.playChannelID,
                shopName = feedXCard.author.name,
                mediaType = media?.type?:""
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
                            trackerIdAsgc = "34102",
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
            trackerIdSgcRecom = "34303"
        )
        analyticsTracker.sendClickLihatSelengkapnyaSgcImageEvent(getContentDetailAnalyticsData(feedXCard, trackerId = trackerId))
    }

    override fun onHashtagClicked(hashTag: String, feedXCard: FeedXCard) {
        analyticsTracker.sendClickHashtagSgcImageEvent(
            getContentDetailAnalyticsData (
                feedXCard,
                hashTag = hashTag,
                trackerId = getTrackerID(feedXCard, trackerIdSgc = "33295")
            )
        )
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
        val intent = RouteManager.getIntent(context, feedXCard.appLinkProductList)
        intent.putExtra(
            ContentDetailRevampArgumentModel.IS_FOLLOWED,
            feedXCard.followers.isFollowed
        )
        intent.putExtra(ContentDetailRevampArgumentModel.PARAM_SHOP_ID, feedXCard.author.id)
        intent.putExtra(ContentDetailRevampArgumentModel.SHOP_NAME, feedXCard.author.name)
        intent.putExtra(ContentDetailRevampArgumentModel.PARAM_ACTIVITY_ID, postId)
        intent.putExtra(ContentDetailRevampArgumentModel.PARAM_POST_TYPE, feedXCard.typename)
        requireActivity().startActivity(intent)

    }

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
                    feedXCard, trackerIdSgc = "33258", trackerIdSgcRecom = "34274"
                )
            )
        )
    }

    override fun onPostImpressed(feedXCard: FeedXCard, postPosition: Int) {
        if (feedXCard.isTypeVOD)
            analyticsTracker.sendImpressionPostVOD(
                getContentDetailAnalyticsData(
                    feedXCard, postPosition, trackerId = getTrackerID(
                        feedXCard, trackerIdVod = "34150", trackerIdVodRecomm = "34167"
                    )
                )
            )
        else
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

    override fun onDestroyView() {
        super.onDestroyView()
        if (::productTagBS.isInitialized) {
            productTagBS.onDestroy()
        }
        if (::reportBottomSheet.isInitialized) {
            reportBottomSheet.onDestroy()
        }
    }

    override fun urlCreated(linkerShareData: LinkerShareResult?) {
        val intent: Intent = if (shareBottomSheetProduct) {
            getIntent(linkerShareData?.url ?: "")
        } else {
            getIntent()
        }
        activity?.startActivity(Intent.createChooser(intent, shareData.name))
    }

    private fun createDeleteDialog(id: Int, rowNumber: Int) {
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
          viewModel.doDeletePost(id, rowNumber)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openEditPostPage(caption: String, postId: String, authorId: String) {
        var createPostViewModel = CreatePostViewModel()
        createPostViewModel.caption = caption
        createPostViewModel.postId = postId
        createPostViewModel.editAuthorId = authorId

        val intent = RouteManager.getIntent(context,
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
        //TODO check start time logic
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

    private fun getIntent(shareUrl: String = ""): Intent {
        val shareUri: String = if (shareUrl.isNotEmpty()) {
            shareUrl
        } else {
            shareData.uri
        }
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = SHARE_TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, shareData.name)
            putExtra(Intent.EXTRA_SUBJECT, shareData.name)
            putExtra(Intent.EXTRA_TEXT, shareData.description + "\n" + shareUri)
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


    override fun onError(linkerError: LinkerError?) {
    }

    override fun onBottomSheetThreeDotsClicked(
        item: ProductPostTagViewModelNew,
        context: Context,
        shopId: String
    ) {
        analyticsTracker.sendClickThreeDotsSgcImageEventForBottomSheet(
           ContentDetailPageAnalyticsDataModel(
               activityId = item.postId.toString(),
               shopId = item.shopId,
               isFollowed = item.isFollowed,
               type = item.postType,
               mediaType = item.mediaType,
               trackerId = getTrackerID(
                   item,
                   trackerIdSgc = "33269",
                   trackerIdAsgcRecom = "34091",
                   trackerIdVod = "34157",
                   trackerIdVodRecomm = "34182",
                   trackerIdSgcRecom = "34279",
                   trackerIdAsgc = "34103"
               )
           ))
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId.toString()
        val bundle = Bundle()
        bundle.putBoolean("isLogin", userSession.isLoggedIn)
        val sheet = ProductActionBottomSheet.newInstance(bundle)
        sheet.show(childFragmentManager, "")
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
                item.mediaType
            )
        }
    }

    override fun onTaggedProductCardImpressed(
        activityId: String,
        postTagItemList: List<FeedXProduct>,
        type: String,
        shopId: String,
        isFollowed: Boolean,
        mediaType: String
    ) {
        analyticsTracker.sendImpressionProductSgcImageEvent(
            ContentDetailPageAnalyticsDataModel(
                activityId = activityId,
                type = type,
                shopId = shopId,
                isFollowed = isFollowed,
                mediaType = mediaType,
                trackerId = getTrackerID(
                    ProductPostTagViewModelNew(
                        postType = type,
                        mediaType = type,
                        isFollowed = isFollowed
                    ),
                    trackerIdSgc = "33267",
                    trackerIdVod = "34162",
                    trackerIdVodRecomm = "34180",
                    trackerIdSgcRecom = "34277",
                    trackerIdSgcVideo = "34609",
                    trackerIdAsgc = "34100",
                    trackerIdAsgcRecom = "34089"
                )
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
            if (item.tags.isNotEmpty())
                analyticsTracker.sendClickProductSgcImageEvent(
                    ContentDetailPageAnalyticsDataModel(
                        activityId = item.id,
                        feedXProduct = postTagItem,
                        isFollowed = item.followers.isFollowed,
                        shopId = item.author.id,
                        type = item.typename,
                        trackerId = getTrackerID(
                            item,
                            trackerIdSgc = "33268",
                            trackerIdVod = "34163",
                            trackerIdVodRecomm = "34181",
                            trackerIdSgcRecom = "34278",
                            trackerIdSgcVideo = "34610",
                            trackerIdAsgcRecom = "34090",
                            trackerIdAsgc = "34101"
                        )
                    )
                )
        }

        onGoToLink(redirectUrl)
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
                shopId = shopId,
                isFollowed = isFollowed,
                shopName = shopName,
                mediaType = mediaType,
                trackerId = getTrackerID(
                    ProductPostTagViewModelNew(
                        postType = type,
                        mediaType = type,
                        isFollowed = isFollowed
                    ), trackerIdAsgc = "34112", trackerIdVod = "34165", trackerIdVodRecomm = "34184"

                )
            )
        )
        if (userSession.isLoggedIn) {
            if (::productTagBS.isInitialized) {
                productTagBS.dismissedByClosing = true
                productTagBS.dismiss()
            }
            viewModel.doAddToCart(
                postTagItem.id,
                postTagItem.productName,
                postTagItem.price.toString(),
                shopId,
                postTagItem.appLink
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
        mediaType: String
    ) {
        analyticsTracker.sendClickWishlistSgcImageEvent(
            ContentDetailPageAnalyticsDataModel(
                activityId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId,
                type = type,
                isFollowed = isFollowed,
                mediaType = mediaType,
                trackerId = getTrackerID(
                    ProductPostTagViewModelNew(
                        postType = type,
                        mediaType = type,
                        isFollowed = isFollowed
                    ),
                    trackerIdSgc = "33270",
                    trackerIdVod = "34164",
                    trackerIdVodRecomm = "34183",
                    trackerIdSgcRecom = "34280",
                    trackerIdSgcVideo = "34611",
                    trackerIdAsgc = "34104",
                    trackerIdAsgcRecom = "34092"
                )
            )
        )
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        viewModel.addToWishlist(productId)
    }

    private fun observeWishlist() {
        viewModel.observableWishlist.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(it.data, requireContext(), requireView())
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(requireContext(), it.throwable)
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, requireView())
                }
            }
        })
    }

    private fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    private fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }
    private fun onSuccessDeletePost(rowNumber: Int) {
        if (adapter.getList().size > rowNumber) {
            adapter.getList().removeAt(rowNumber)
            adapter.notifyItemRemoved(rowNumber)
            Toaster.build(
                requireView(),
                getString(kolR.string.feed_post_deleted),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(com.tokopedia.affiliatecommon.R.string.af_title_ok)
            ).show()
        }
        if (adapter.getList().isEmpty()) {
            //TODO handle empty list view
//            showRefresh()
//            onRefresh()
        }
    }
    private fun onErrorDeletePost(data: DeletePostViewModel) {
        Toaster.build(
            requireView(),
            data.errorMessage,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.title_try_again),
            View.OnClickListener {
                viewModel.doDeletePost(data.id, data.rowNumber)
            })
    }
    private fun getContentDetailAnalyticsData(feedXCard: FeedXCard, postPosition: Int = 0, trackerId : String= "", hashTag: String= "", duration: Long = 0L, product: FeedXProduct = FeedXProduct()) = ContentDetailPageAnalyticsDataModel(
        activityId = if (feedXCard.isTypeVOD) feedXCard.playChannelID else feedXCard.id,
        shopId = feedXCard.author.id,
        rowNumber = postPosition,
        isFollowed = feedXCard.followers.isFollowed,
        type = feedXCard.typename,
        mediaType = if (feedXCard.lastCarouselIndex < feedXCard.media.size) feedXCard.media[feedXCard.lastCarouselIndex].type else "",
        trackerId = trackerId,
        hashtag = hashTag,
        mediaUrl = feedXCard.media.firstOrNull()?.mediaUrl ?: "",
        itemName = feedXCard.title,
        duration = duration,
        feedXProduct = product
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
        trackerIdSgcVideo: String = "",

    ) = when {
        feedXCard.isTypeVOD -> trackerIdVod
        feedXCard.isTypeLongVideo -> trackerIdLongVideo
        feedXCard.isTypeVOD && feedXCard.followers.isFollowed -> trackerIdVodRecomm
        feedXCard.isTypeProductHighlight && feedXCard.followers.isFollowed -> trackerIdAsgc
        feedXCard.isTypeProductHighlight && !feedXCard.followers.isFollowed -> trackerIdAsgcRecom
        feedXCard.isTypeSGC && feedXCard.followers.isFollowed -> trackerIdSgc
        feedXCard.isTypeSGC && !feedXCard.followers.isFollowed -> trackerIdSgcRecom
        feedXCard.isTypeSgcVideo && feedXCard.followers.isFollowed -> trackerIdSgcVideo
        else -> ""

    }
    private fun getTrackerID(
        item: ProductPostTagViewModelNew,
        trackerIdSgc: String = "",
        trackerIdSgcRecom: String = "",
        trackerIdAsgc: String = "",
        trackerIdAsgcRecom: String = "",
        trackerIdVod: String = "",
        trackerIdVodRecomm: String = "",
        trackerIdLongVideo: String = "",
        trackerIdSgcVideo: String = "",
    ) = when {
        item.postType == TYPE_FEED_X_CARD_PLAY -> trackerIdVod
        item.postType == TYPE_FEED_X_CARD_POST && item.mediaType == TYPE_LONG_VIDEO -> trackerIdLongVideo
        item.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && item.isFollowed -> trackerIdAsgc
        item.postType == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT  && !item.isFollowed -> trackerIdAsgcRecom
        item.postType == TYPE_FEED_X_CARD_POST  && item.isFollowed -> trackerIdSgc
        item.postType == TYPE_FEED_X_CARD_POST && !item.isFollowed -> trackerIdSgcRecom
        item.postType == TYPE_FEED_X_CARD_POST && item.mediaType == TYPE_VIDEO  -> trackerIdSgcVideo

        else -> ""

    }





}