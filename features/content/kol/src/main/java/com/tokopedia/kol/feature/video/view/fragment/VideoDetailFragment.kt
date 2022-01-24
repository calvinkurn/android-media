package com.tokopedia.kol.feature.video.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXComments
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXLike
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel
import com.tokopedia.kol.R
import com.tokopedia.kol.common.di.DaggerKolComponent
import com.tokopedia.kol.common.util.UrlUtil
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.comment.view.activity.KolCommentNewActivity.Companion.getCallingIntent
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoplayer.utils.Video
import kotlinx.android.synthetic.main.layout_single_video_fragment.*
import javax.inject.Inject

/**
 * @author by yfsx on 23/03/19.
 */

const val POST_POSITION = "position"
const val PARAM_VIDEO_INDEX = "video_index"
const val PARAM_CALL_SOURCE = "call_source"
const val PARAM_FEED = "feed"
const val PARAM_VIDEO_AUTHOR_TYPE = "video_author_type"
const val PARAM_POST_TYPE = "POST_TYPE"
const val PARAM_IS_POST_FOLLOWED = "IS_FOLLOWED"
const val PARAM_COMMENT_COUNT = "comment_count"
const val PARAM_LIKE_COUNT = "like_count"

class VideoDetailFragment :
    BaseDaggerFragment(),
    VideoDetailContract.View,
    KolPostLikeListener,
    MediaPlayer.OnPreparedListener {

    override val androidContext: Context
        get() = requireContext()

    @Inject
    lateinit var presenter: VideoDetailContract.Presenter

    lateinit var dynamicPostViewModel: DynamicPostUiModel


    @Inject
    override lateinit var userSession: UserSessionInterface

    private var id: String = ""

    private var index: Int = 0

    companion object {
        private const val INTENT_COMMENT = 1234
        private const val LOGIN_CODE = 1383
        private const val LOGIN_FOLLOW_CODE = 1384

        fun getInstance(bundle: Bundle): VideoDetailFragment {
            val fragment = VideoDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerKolComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_single_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString(VideoDetailActivity.PARAM_ID, "") ?: ""
        index = arguments?.getInt(PARAM_VIDEO_INDEX, 0) ?: 0
        presenter.attachView(this)
        initView()
        initViewListener()
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.let { player ->
            activity?.let { it ->
                //video player resize
                val videoSize = Video.resize(it, player.videoWidth, player.videoHeight)
                videoView.setSize(videoSize.videoWidth, videoSize.videoHeight)
                videoView.holder.setFixedSize(videoSize.videoWidth, videoSize.videoHeight)

                //showing media controller
                val mediaController = MediaController(it)
                videoView.setMediaController(mediaController)
                mediaController.setAnchorView(videoView)
            }
            player.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            INTENT_COMMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        calculateTotalComment(
                            it.getIntExtra(
                                KolCommentFragment.ARGS_TOTAL_COMMENT,
                                0
                            )
                        )
                    }
                }
            }
            LOGIN_CODE -> {
                initData()
            }
            LOGIN_FOLLOW_CODE -> {

            }
        }
    }

    override fun onSuccessFollowKol() {
    }

    override fun onErrorFollowKol(error: String) {
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: LikeKolPostUseCase.LikeKolPostAction) {

        val like = dynamicPostViewModel.feedXCard.like
        like.isLiked = !like.isLiked
        if (like.isLiked) {
            try {
                val likeValue = Integer.valueOf(like.countFmt) + 1
                like.countFmt = likeValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            like.count = like.count + 1
        } else {
            try {
                val likeValue = Integer.valueOf(like.countFmt) - 1
                like.countFmt = likeValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            like.count = like.count - 1
        }
        bindLike(like)
    }

    override fun onLikeKolError(message: String) {
        showError(message, null)
    }

    override fun onErrorGetVideoDetail(error: String) {
        NetworkErrorHelper.showRedSnackbar(requireActivity(), error)
        activity?.finish()
    }

    override fun onSuccessGetVideoDetail(visitables: List<Visitable<*>>) {
        if (visitables[0] is DynamicPostUiModel)
        dynamicPostViewModel = visitables[0] as DynamicPostUiModel
        val feedXCard = dynamicPostViewModel.feedXCard
        bindHeader(dynamicPostViewModel.feedXCard)
        bindCaption(dynamicPostViewModel.feedXCard)
        bindFooter(dynamicPostViewModel.feedXCard)

            if (feedXCard.media.isNotEmpty()) {
                feedXCard.media.firstOrNull()?.let { initPlayer(it.mediaUrl) }
            }


    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    private fun initView() {
        val detailId = arguments?.getString(VideoDetailActivity.PARAM_ID, "")
        if (detailId?.isEmpty() == true || detailId == "0") {
            activity?.finish()
        } else {
            initData()
        }
    }

    private fun initData() {
        presenter.getFeedDetail(id)
    }

    private fun initPlayer(url: String) {
        if (URLUtil.isValidUrl(url))
        videoView.setVideoURI(Uri.parse(url))
        videoView.setOnErrorListener { _, p1, p2 ->
            try {
                FirebaseCrashlytics.getInstance().recordException(
                    Throwable(
                        String.format(
                            "%s - what : %s - extra : %s ",
                            VideoDetailFragment::class.java.simpleName, p1.toString(), p2.toString()
                        )
                    )
                )
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

            when (p1) {
                MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                    Toast.makeText(
                        requireContext(),
                        getString(com.tokopedia.videoplayer.R.string.error_unknown),
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                    true
                }
                MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                    Toast.makeText(
                        requireContext(),
                        getString(com.tokopedia.abstraction.R.string.default_request_error_internal_server),
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                    true
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(com.tokopedia.abstraction.R.string.default_request_error_timeout),
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                    true
                }
            }
        }
        videoView.setOnPreparedListener(this)
    }

    private fun initViewListener() {
        ivClose.setOnClickListener {
            val intent = Intent()
            intent.putExtra(POST_POSITION, arguments?.getInt(POST_POSITION))
            intent.putExtra(PARAM_COMMENT_COUNT, dynamicPostViewModel.feedXCard.comments.count)
            intent.putExtra(PARAM_LIKE_COUNT, dynamicPostViewModel.feedXCard.like.isLiked)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }

        likeIcon.setOnClickListener(onLikeSectionClicked())
        likeText.setOnClickListener(onLikeSectionClicked())

        commentIcon.setOnClickListener(onCommentSectionClicked())
        commentText.setOnClickListener(onCommentSectionClicked())
    }

    private fun onLikeSectionClicked(): View.OnClickListener {
        return View.OnClickListener {
            if (userSession.isLoggedIn) {
                presenter.likeKol(id.toInt(), 0, this)
            } else {
                goToLogin()
            }
        }
    }

    private fun onCommentSectionClicked(): View.OnClickListener {
        val callSource = arguments?.getString(PARAM_CALL_SOURCE)
        val authorId = arguments?.getString(PARAM_VIDEO_AUTHOR_TYPE)
        val postType = arguments?.getString(PARAM_POST_TYPE)
        val isFollowed = arguments?.getBoolean(PARAM_IS_POST_FOLLOWED, true)

        return View.OnClickListener {
            if (userSession.isLoggedIn) {
                if (callSource == PARAM_FEED) {
                    val intent = getCallingIntent(
                        requireContext(),
                        id.toInt(),
                        0,
                        authorId,
                        isFollowed,
                        postType
                    )
                    startActivityForResult(intent, INTENT_COMMENT)

                } else {
                    startActivityForResult(
                        KolCommentActivity.getCallingIntent(
                            requireActivity(),
                            id.toInt(),
                            0
                        ), INTENT_COMMENT
                    )
                }
            } else {
                goToLogin()
            }
        }
    }

    private fun bindHeader(feedXCard: FeedXCard) {
        val author = feedXCard.author
        author.let {
            headerLayout.visibility = View.VISIBLE
            if (!TextUtils.isEmpty(it.logoURL)) {
                authorImage.loadImageCircle(it.logoURL)
            } else {
                authorImage.setImageDrawable(
                        MethodChecker.getDrawable(
                                requireActivity(),
                                com.tokopedia.design.R.drawable.error_drawable
                        )
                )
            }
            if (it.badgeURL.isNotBlank()) {
                authorBadge.show()
                authorBadge.loadImage(it.badgeURL)
                authorTitle.setMargin(
                        authorTitle.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),
                        0,
                        authorTitle.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                        0
                )
            } else {
                authorBadge.hide()
                authorTitle.setMargin(
                        authorTitle.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                        0,
                        authorTitle.getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                        0
                )
            }

            authorTitle.text = MethodChecker.fromHtml(it.name)
        }

        authorSubtitile.text = TimeConverter.generateTime(requireActivity(), feedXCard.publishedAt)
    }

    private fun bindCaption(feedXCard: FeedXCard) {
        feedXCard.let {
            if (it.text.isEmpty()) {
                captionLayout.visibility = View.GONE
            } else {
                caption.text = it.text.replace(DynamicPostViewHolder.NEWLINE, " ")
            }
        }
    }

    private fun bindFooter(feedXCard: FeedXCard) {
        feedXCard.let {
            bottomLayout.visibility = View.VISIBLE
            val like = feedXCard.like
            if (like.likedBy.isNotEmpty() || like.count != 0) {
                likeIcon.show()
                likeText.show()
                bindLike(it.like)
            } else {
                likeIcon.hide()
                likeText.hide()
            }
            val comments = feedXCard.comments

            if (comments.count != 0) {
                commentIcon.show()
                commentText.show()
                bindComment(it.comments)
            } else {
                commentIcon.hide()
                commentText.hide()
            }


                shareIcon.show()
                shareText.show()
                var desc = context?.getString(com.tokopedia.feedcomponent.R.string.feed_share_default_text)

            shareIcon.setOnClickListener {
                    doShare(
                        String.format(
                            "%s",
                            desc?.replace("%s", feedXCard.author.name),
                        ), feedXCard.author.name + " `post"
                    )
                }

        }

    }

    private fun doShare(body: String, title: String) {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body)
        startActivity(
            Intent.createChooser(sharingIntent, title)
        )
    }

    private fun bindLike(like: FeedXLike) {
        when {
            like.isLiked -> {
                likeIcon.loadImageWithoutPlaceholder(com.tokopedia.feedcomponent.R.drawable.ic_thumb_green)
                likeText.text = like.countFmt
                likeText.setTextColor(
                    MethodChecker.getColor(
                        likeText.context,
                        com.tokopedia.design.R.color.tkpd_main_green
                    )
                )
            }
            like.count > 0 -> {
                likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_white)
                likeText.text = like.countFmt
                likeText.setTextColor(
                    MethodChecker.getColor(
                        likeText.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
            }
            else -> {
                likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_white)
                likeText.setText(com.tokopedia.feedcomponent.R.string.kol_action_like)
                likeText.setTextColor(
                    MethodChecker.getColor(
                        likeIcon.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
            }
        }
    }

    private fun bindComment(comments: FeedXComments) {
        commentText.text =
            if (comments.count == 0) getString(com.tokopedia.feedcomponent.R.string.kol_action_comment)
            else comments.countFmt
    }

    private fun calculateTotalComment(totalNewComment: Int) {
        val comment: FeedXComments = dynamicPostViewModel.feedXCard.comments
        try {
            val commentValue = Integer.valueOf(comment.countFmt) + totalNewComment
            comment.countFmt = commentValue.toString()
        } catch (ignored: NumberFormatException) {
        }

        comment.count = comment.count + totalNewComment
        commentText.text = comment.countFmt
    }

    private fun goToLogin() {
        startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.LOGIN), LOGIN_CODE)
    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        listener?.let {
            Toaster.build(
                requireView(),
                message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.title_try_again),
                it
            ).show()
        }
    }


}