package com.tokopedia.kol.feature.video.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel
import com.tokopedia.kol.R
import com.tokopedia.kol.common.di.DaggerKolComponent
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.user.session.UserSession
import com.tokopedia.videoplayer.utils.Video
import com.tokopedia.videoplayer.view.widget.VideoPlayerView
import kotlinx.android.synthetic.main.kol_comment_item.*
import kotlinx.android.synthetic.main.layout_single_video_fragment.*
import javax.inject.Inject

/**
 * @author by yfsx on 23/03/19.
 */

class VideoDetailFragment:
        BaseDaggerFragment(),
        VideoDetailContract.View,
        KolPostListener.View.Like,
        MediaPlayer.OnPreparedListener {


    @Inject
    lateinit var presenter: VideoDetailContract.Presenter

    lateinit var dynamicPostViewModel: DynamicPostViewModel
    lateinit var videoViewModel: VideoViewModel

    private var id: String = ""
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
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_single_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments!!.getString(VideoDetailActivity.PARAM_ID, "")
        presenter.attachView(this)
        initView()
        initViewListener()
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.let {player ->
            activity?.let { it ->
                //video player resize
                val videoSize = Video.resize(it, player.videoWidth, player.videoHeight)
                videoView.setSize(videoSize.videoWidth, videoSize.videoHeight)
                videoView.holder.setFixedSize(videoSize.videoWidth, videoSize.videoHeight)

                //showing media controller
                val mediaController = MediaController(it)
                videoView.setMediaController(mediaController)
                mediaController.setAnchorView(videoView)
                mediaController.show()
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
                        calculateTotalComment(it.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0))
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

    override fun getContext(): Context {
        return activity!!
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: Int) {

        val like = dynamicPostViewModel.footer.like
        like.isChecked = !like.isChecked
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
        bindLike(like)
    }

    override fun onLikeKolError(message: String?) {
        showError(message!!, null)
    }

    override fun onErrorGetVideoDetail(error: String) {
        NetworkErrorHelper.showRedSnackbar(activity!!, error)
        activity?.finish()
    }

    override fun onSuccessGetVideoDetail(visitables: List<Visitable<*>>) {
        dynamicPostViewModel = visitables.get(0) as DynamicPostViewModel
        bindHeader(dynamicPostViewModel.header)
        bindCaption(dynamicPostViewModel.caption, dynamicPostViewModel.template.cardpost.body)
        bindFooter(dynamicPostViewModel.footer, dynamicPostViewModel.template.cardpost.footer)

        videoViewModel = dynamicPostViewModel.contentList[0] as VideoViewModel
        initPlayer(videoViewModel.url)

    }

    override fun getUserSession(): UserSession = UserSession(context)

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    private fun initView() {
        val detailId = arguments!!.getString(VideoDetailActivity.PARAM_ID, "")
        if (detailId.isEmpty() || detailId == "0") {
            activity?.finish()
        } else {
            initData()
        }
    }

    private fun initData() {
        presenter.getFeedDetail(id)
    }

    private fun initPlayer(url: String) {
        videoView.setVideoURI(Uri.parse(url))
        videoView.setOnErrorListener { _, p1, p2 ->
            try {
                Crashlytics.logException(Throwable(String.format("%s - what : %s - extra : %s ",
                        VideoDetailFragment::class.java.simpleName, p1.toString(), p2.toString())))
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

            when(p1) {
                MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                    Toast.makeText(context, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    true
                }
                MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                    Toast.makeText(context, getString(R.string.default_request_error_internal_server), Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    true
                }
                else -> {
                    Toast.makeText(context, getString(R.string.default_request_error_timeout), Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    true
                }
            }
        }
        videoView.setOnPreparedListener(this)
    }

    private fun initViewListener() {
        ivClose.setOnClickListener {
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish() }

        likeIcon.setOnClickListener(onLikeSectionClicked())
        likeText.setOnClickListener(onLikeSectionClicked())

        commentIcon.setOnClickListener(onCommentSectionClicked())
        commentText.setOnClickListener(onCommentSectionClicked())
    }

    private fun onLikeSectionClicked(): View.OnClickListener {
        return View.OnClickListener {
            if (getUserSession().isLoggedIn) {
                presenter.likeKol(id.toInt(), 0, this)
            } else{
                goToLogin()
            }
        }
    }

    private fun onCommentSectionClicked(): View.OnClickListener {
        return View.OnClickListener {
            if (getUserSession().isLoggedIn) {
                startActivityForResult(KolCommentActivity.getCallingIntent(activity!!, id.toInt(), 0), INTENT_COMMENT)
            } else{
                goToLogin()
            }
        }
    }

    private fun bindHeader(header: Header) {
        header.let {
            headerLayout.visibility = View.VISIBLE
            if (!TextUtils.isEmpty(it.avatar)) {
                authorImage.loadImageCircle(it.avatar)
            } else {
                authorImage.setImageDrawable(
                        MethodChecker.getDrawable(activity!!, R.drawable.error_drawable)
                )
            }
            if (it.avatarBadgeImage.isNotBlank()) {
                authorBadge.show()
                authorBadge.loadImage(it.avatarBadgeImage)
                authorTitle.setMargin(authorTitle.getDimens(R.dimen.dp_4), 0, authorTitle.getDimens(R.dimen.dp_8), 0)
            } else {
                authorBadge.hide()
                authorTitle.setMargin(authorTitle.getDimens(R.dimen.dp_8), 0, authorTitle.getDimens(R.dimen.dp_8), 0)
            }

            authorTitle.text = it.avatarTitle

            it.avatarDate = TimeConverter.generateTime(activity!!, it.avatarDate)
            authorSubtitile.text = it.avatarDate
        }
    }

    private fun bindCaption(captionModel: Caption, template: TemplateBody) {
        captionModel.let {
            if (it.text.isEmpty()) {
                captionLayout.visibility = View.GONE
            } else {
                caption.text = it.text.replace(DynamicPostViewHolder.NEWLINE, " ")
            }
        }
    }

    private fun bindFooter(footer: Footer, template: TemplateFooter?) {
        footer.let {
            bottomLayout.visibility = View.VISIBLE
            if (template!!.like) {
                likeIcon.show()
                likeText.show()
                bindLike(it.like)
            } else {
                likeIcon.hide()
                likeText.hide()
            }

            if (template.comment) {
                commentIcon.show()
                commentText.show()
                bindComment(it.comment)
            } else {
                commentIcon.hide()
                commentText.hide()
            }

            if (template.share) {
                shareIcon.show()
                shareText.show()
                shareText.text = footer.share.text
                shareIcon.setOnClickListener {
                    doShare(String.format("%s %s", dynamicPostViewModel.footer.share.description, dynamicPostViewModel.footer.share.url)
                            , dynamicPostViewModel.footer.share.title)
                }
            } else {
                shareIcon.hide()
                shareText.hide()
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

    private fun bindLike(like: Like) {
        when {
            like.isChecked -> {
                likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_green)
                likeText.text = like.fmt
                likeText.setTextColor(
                        MethodChecker.getColor(likeText.context, R.color.tkpd_main_green)
                )
            }
            like.value > 0 -> {
                likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_white)
                likeText.text = like.fmt
                likeText.setTextColor(
                        MethodChecker.getColor(likeText.context, R.color.white)
                )
            }
            else -> {
                likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb_white)
                likeText.setText(R.string.kol_action_like)
                likeText.setTextColor(
                        MethodChecker.getColor(likeIcon.context, R.color.white)
                )
            }
        }
    }
    private fun bindComment(comment: Comment) {
        commentText.text =
                if (comment.value == 0) getString(R.string.kol_action_comment)
                else comment.fmt
    }

    private fun calculateTotalComment(totalNewComment: Int) {
        val comment: Comment = dynamicPostViewModel.footer.comment
        try {
            val commentValue = Integer.valueOf(comment.fmt) + totalNewComment
            comment.fmt = commentValue.toString()
        } catch (ignored: NumberFormatException) {
        }

        comment.value = comment.value + totalNewComment
        commentText.text = comment.fmt
    }

    private fun goToLogin() {
        startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.LOGIN), LOGIN_CODE)
    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        ToasterError.make(view, message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show()
    }



}