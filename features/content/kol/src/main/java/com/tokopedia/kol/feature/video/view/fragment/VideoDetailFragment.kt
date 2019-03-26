package com.tokopedia.kol.feature.video.view.fragment

import android.content.Context
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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.kol.R
import com.tokopedia.kol.common.di.DaggerKolComponent
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.layout_single_video_fragment.*

/**
 * @author by yfsx on 23/03/19.
 */

class VideoDetailFragment:
        BaseDaggerFragment(),
        VideoDetailContract.View,
        KolPostListener.View.Like,
        MediaPlayer.OnPreparedListener {


    private var id: String = ""
    companion object {
        fun getInstance(bundle: Bundle): VideoDetailFragment {
            val fragment = VideoDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    init {

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
        initView()
        initViewListener()
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.let {
            resizeVideo(it.getVideoWidth(), it.getVideoHeight())
            it.start()
        }
    }

    override fun onSuccessFollowKol() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorFollowKol(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContext(): Context {
        return activity!!
    }

    override fun onLikeKolSuccess(rowNumber: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLikeKolError(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorGetVideoDetail(error: String) {
        NetworkErrorHelper.showRedSnackbar(activity!!, error)
        activity!!.finish()
    }

    override fun onSuccessGetVideoDetail(visitables: List<Visitable<*>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserSession(): UserSession = UserSession(context)

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    private fun initView() {
//        initUi()
//        initPlayer()
    }

    private fun initUi() {

    }

    private fun initPlayer(url: String) {
        val mediaController = MediaController(activity!!)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(url))
        videoView.setOnErrorListener(object : MediaPlayer.OnErrorListener{
            override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                when(p1) {
                    MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                        Toast.makeText(context, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                        return true
                    }
                    MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                        Toast.makeText(context, getString(R.string.default_request_error_internal_server), Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                        return true
                    }
                    else -> {
                        Toast.makeText(context, getString(R.string.default_request_error_timeout), Toast.LENGTH_SHORT).show()
                        activity!!.finish()
                        return true
                    }
                }
            }
        })
        videoView.setOnPreparedListener(this)
    }

    private fun initViewListener() {

    }

    private fun resizeVideo(mVideoWidth: Int, mVideoHeight: Int) {
        var videoWidth = mVideoWidth
        var videoHeight = mVideoHeight
        val displaymetrics = DisplayMetrics()
        activity!!.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics)

        val heightRatio = videoHeight.toFloat() / displaymetrics.widthPixels.toFloat()
        val widthRatio = videoWidth.toFloat() / displaymetrics.heightPixels.toFloat()

        if (videoWidth > videoHeight) {
            videoWidth = Math.ceil((videoWidth.toFloat() * widthRatio).toDouble()).toInt()
            videoHeight = Math.ceil((videoHeight.toFloat() * widthRatio).toDouble()).toInt()
        } else {
            videoWidth = Math.ceil((videoWidth.toFloat() * heightRatio).toDouble()).toInt()
            videoHeight = Math.ceil((videoHeight.toFloat() * heightRatio).toDouble()).toInt()
        }

        videoView.setSize(videoWidth, videoHeight)
        videoView.holder.setFixedSize(videoWidth, videoHeight)
    }

    private fun bindHeader(header: Header) {
        header.let {
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
                caption.visibility = View.GONE
            } else if (caption.text.length > DynamicPostViewHolder.MAX_CHAR) {
                caption.visibility = View.VISIBLE
                val captionText = caption.text.substring(0, DynamicPostViewHolder.CAPTION_END)
                        .replace(DynamicPostViewHolder.NEWLINE, "<br />")
                        .plus("... ")
                        .plus("<font color='#ffffff'><b>")
                        .plus(captionModel.buttonName)
                        .plus("</b></font>")

                caption.text = MethodChecker.fromHtml(captionText)
                caption.setOnClickListener {
                    if (!TextUtils.isEmpty(captionModel.appLink)) {
//                        listener.onCaptionClick(adapterPosition, caption.appLink)
                    } else {
                        caption.text = caption.text
                    }
                }
            } else {
//                caption.text = caption.text.replace(DynamicPostViewHolder.NEWLINE, " ")
            }
        }
    }


    private fun bindFooter(footer: Footer, template: TemplateFooter?) {
        footer.let {
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
//                commentIcon.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
//                commentText.setOnClickListener { listener.onCommentClick(adapterPosition, id) }
                bindComment(it.comment)
            } else {
                commentIcon.hide()
                commentText.hide()
            }

//            if (template.share) {
//                shareIcon.show()
//                shareText.show()
//                shareText.text = footer.share.text
//                shareIcon.setOnClickListener {
//                    listener.onShareClick(
//                            adapterPosition,
//                            id,
//                            footer.share.title,
//                            footer.share.description,
//                            footer.share.url,
//                            footer.share.imageUrl
//                    )
//                }
//                shareText.setOnClickListener {
//                    listener.onShareClick(
//                            adapterPosition,
//                            id,
//                            footer.share.title,
//                            footer.share.description,
//                            footer.share.url,
//                            footer.share.imageUrl
//                    )
//                }
//
//            } else {
//                shareIcon.hide()
//                shareText.hide()
//            }
        }

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
                likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb)
                likeText.text = like.fmt
                likeText.setTextColor(
                        MethodChecker.getColor(likeText.context, R.color.black_54)
                )
            }
            else -> {
                likeIcon.loadImageWithoutPlaceholder(R.drawable.ic_thumb)
                likeText.setText(R.string.kol_action_like)
                likeText.setTextColor(
                        MethodChecker.getColor(likeIcon.context, R.color.black_54)
                )
            }
        }
    }
    private fun bindComment(comment: Comment) {
        commentText.text =
                if (comment.value == 0) getString(R.string.kol_action_comment)
                else comment.fmt
    }
}