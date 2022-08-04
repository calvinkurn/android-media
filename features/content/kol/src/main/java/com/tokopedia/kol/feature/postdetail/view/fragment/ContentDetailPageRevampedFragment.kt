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
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.FeedScrollListenerNew
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.di.DaggerContentDetailComponent
import com.tokopedia.kol.feature.postdetail.di.module.ContentDetailModule
import com.tokopedia.kol.feature.postdetail.view.activity.ContentDetailActivity
import com.tokopedia.kol.feature.postdetail.view.adapter.ContentDetailPageRevampAdapter
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.ContentDetailPostViewHolder
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
import com.tokopedia.kol.feature.postdetail.view.datamodel.ShopFollowModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
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
        setupView(view)
        viewModel.getCDPPostDetailFirstData(postId)


        observeWishlist()
        observeFollowShop()
        observeDeleteContent()
        observeReportContent()
        observeTrackVisitChannel()
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

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        val newList = adapter.getList()
        if (newList.size > rowNumber) {
            val item = newList[rowNumber]
            item.comments.count = totalNewComment
            item.comments.countFmt =
                (item.comments.count).toString()
        }
        adapter.notifyItemChanged(rowNumber, DynamicPostNewViewHolder.PAYLOAD_COMMENT)
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

    override fun onLikeClicked(feedXCard: FeedXCard, postPosition : Int) {
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

    override fun onCommentClicked(feedXCard: FeedXCard, postPosition : Int) {
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
        activity?.let {
            val urlString = when {
                feedXCard.isTypeProductHighlight -> {
                    String.format(getString(kolR.string.feed_share_asgc_weblink), id.toString())
                }
                feedXCard.isTypeVOD -> {
                    String.format(getString(kolR.string.feed_vod_share_weblink), feedXCard.playChannelID)
                }
                else -> {
                    String.format(getString(kolR.string.feed_share_weblink), id.toString())
                }
            }
            val shareDataBuilder = LinkerData.Builder.getLinkerBuilder().setId(id.toString())
                .setName(String.format(getString(kolR.string.feed_share_title), feedXCard.author.name))
                .setDescription(String.format(getString(kolR.string.feed_share_desc_text), feedXCard.author.name))
                .setDesktopUrl(urlString)
                .setType(LinkerData.FEED_TYPE)
                .setImgUri(feedXCard.media.firstOrNull()?.mediaUrl ?: "")
                .setDeepLink(feedXCard.appLink)

                shareBottomSheetProduct = false
                shareDataBuilder.apply {
                    setUri(urlString)
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
        id: Int,
        title: String,
        description: String,
        url: String,
        imageUrl: String,
    ) {
        if (::productTagBS.isInitialized) {
            productTagBS.dismissedByClosing = true
            productTagBS.dismiss()
        }
        shareBottomSheetProduct = false
        activity?.let {
            val linkerBuilder = LinkerData.Builder.getLinkerBuilder().setId(id.toString())
                .setName(title)
                .setDescription(description)
                .setImgUri(imageUrl)
                .setUri(url)
                .setDeepLink(url)
                .setType(LinkerData.FEED_TYPE)
                .setDesktopUrl(url)

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
        viewModel.followShop(
            shopId = feedXCard.author.id,
            action = ShopFollowAction.getFollowAction(feedXCard.followers.isFollowed),
            rowNumber = postPosition
        )
    }

    override fun onClickOnThreeDots(feedXCard: FeedXCard, postPosition : Int) {
        if (context != null) {
            val sheet = MenuOptionsBottomSheet.newInstance(
                feedXCard.reportable, feedXCard.followers.isFollowed,
                feedXCard.deletable
            )
            sheet.show(childFragmentManager, "")
            sheet.onReport = {

                if (userSession.isLoggedIn) {
                    context?.let {
                        reportBottomSheet = ReportBottomSheet.newInstance(
                            feedXCard.id.toIntOrZero(),
                            context = object : ReportBottomSheet.OnReportOptionsClick {
                                override fun onReportAction(
                                    reasonType: String,
                                    reasonDesc: String
                                ) {
                                    viewModel.sendReport(
                                        postPosition,
                                        feedXCard.id,
                                        reasonType,
                                        reasonDesc,
                                    )
                                }
                            })
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
                if (userSession.isLoggedIn)
                    onFollowUnfollowClicked(
                        feedXCard,
                        postPosition
                    ) else onGoToLogin()
            }
            sheet.onDelete = { createDeleteDialog(feedXCard.id, postPosition) }
            sheet.onDismiss = {

            }
            sheet.onEdit = {
                openEditPostPage(feedXCard.text, postId, feedXCard.author.id)
            }

            sheet.onClosedClicked = {


            }
        }
    }
    override fun onFullScreenButtonClicked(
        feedXCard: FeedXCard,
        postPosition: Int,
        currentTime: Long,
        shouldTrack: Boolean
    ) {
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

    override fun onShopHeaderItemClicked(link: String) {
        onGoToLink(link)
    }

    override fun addViewsToVOD(
        feedXCard: FeedXCard,
        rowNumber: Int,
        time: Long,
        hitTrackerApi: Boolean
    ) {
        if (hitTrackerApi) {
            if (feedXCard.isTypeLongVideo)
                viewModel.trackLongVideoView(feedXCard.id, rowNumber)
            else
                viewModel.trackVisitChannel(feedXCard.playChannelID, rowNumber)
        }
    }

    override fun onLihatProdukClicked(
        feedXCard: FeedXCard,
        postPosition: Int,
        products: List<FeedXProduct>
    ) {
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

            }
            productTagBS.disMissed = {
            }
        }
    }

    override fun onCekSekarangButtonClicked(feedXCard: FeedXCard, postPosition: Int) {
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

    override fun onPostTagBubbleClicked(redirectUrl: String) {
        onGoToLink(redirectUrl)
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
        val finalID =
            if (item.postType == TYPE_FEED_X_CARD_PLAY) item.playChannelId else item.postId.toString()
        val bundle = Bundle()
        bundle.putBoolean("isLogin", userSession.isLoggedIn)
        val sheet = ProductActionBottomSheet.newInstance(bundle)
        sheet.show(childFragmentManager, "")
        sheet.shareProductCB = {
            onShareProduct(
                item.id.toIntOrZero(),
                item.text,
                item.description,
                item.weblink,
                item.imgUrl
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
        //add analytic for impression
    }

    override fun onTaggedProductCardClicked(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        itemPosition: Int,
        mediaType: String
    ) {
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
        if (userSession.isLoggedIn) {
            if (::productTagBS.isInitialized) {
                productTagBS.dismissedByClosing = true
                productTagBS.dismiss()
            }
            viewModel.addToCart(
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
        val finalId = if (type == TYPE_FEED_X_CARD_PLAY) playChannelId else postId
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

    private fun observeFollowShop() {
        viewModel.followShopObservable.observe(viewLifecycleOwner, Observer {
            when (it) {
                ContentDetailResult.Loading -> {
                    // todo: add loading state?
                }
                is ContentDetailResult.Success -> onSuccessFollowShop(it.data)
                is ContentDetailResult.Failure -> {
                    when (it.error) {
                        is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                            showNoInterNetDialog(requireContext())
                        }
                        else -> {
                            val errorMessage = if (it.error is CustomUiMessageThrowable) {
                                requireContext().getString(it.error.errorMessageId)
                            } else ErrorHandler.getErrorMessage(requireContext(), it.error)

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
        })
    }

    private fun observeDeleteContent() {
        viewModel.deletePostResp.observe(viewLifecycleOwner, {
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
                            val errorMessage = ErrorHandler.getErrorMessage(requireContext(), it.error)
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
        })
    }

    private fun observeReportContent() {
        viewModel.reportResponse.observe(viewLifecycleOwner, Observer {
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
                            val errorMessage = ErrorHandler.getErrorMessage(requireContext(), it.error)
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
        })
    }

    private fun observeTrackVisitChannel() {
        viewModel.vodViewData.observe(viewLifecycleOwner, Observer {
            when (it) {
                ContentDetailResult.Loading -> {}
                is ContentDetailResult.Success ->  onSuccessAddViewVODPost(it.data.rowNumber)
                is ContentDetailResult.Failure -> {
                    //TODO fail case
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

}