package com.tokopedia.feedcomponent.view.fragment

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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Comment
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Like
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.activity.VideoPlayerActivity
import com.tokopedia.feedcomponent.view.viewmodel.data.CommentViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.FooterViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.HeaderViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.LikeViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.template.TemplateFooterViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.layout_single_video_fragment.*

/**
 * @author by yfsx on 23/03/19.
 */

class SingleVideoPlayerFragment: BaseDaggerFragment(), MediaPlayer.OnPreparedListener {

    companion object {
        fun getInstance(bundle: Bundle): SingleVideoPlayerFragment {
            val fragment = SingleVideoPlayerFragment()
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

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_single_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewListener()
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.let {
            resizeVideo(it.getVideoWidth(), it.getVideoHeight())
            it.start()
        }
    }

    private fun initView() {
        initUi()
        initPlayer()
    }

    private fun initUi() {
        bindHeader(arguments?.getParcelable(VideoPlayerActivity.PARAM_HEADER))
        bindFooter(arguments?.getParcelable(VideoPlayerActivity.PARAM_FOOTER),
                arguments?.getParcelable(VideoPlayerActivity.PARAM_TEMPLATE_FOOTER))
    }

    private fun initPlayer() {
        val url = arguments?.getString(VideoPlayerActivity.PARAM_SINGLE_URL)
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

    private fun bindHeader(header: HeaderViewModel?) {
        header?.let {
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


    private fun bindFooter(footer: FooterViewModel?, template: TemplateFooterViewModel?) {
        footer?.let {
            if (template!!.like) {
                likeIcon.show()
                likeText.show()
                bindLike(it.like)
            } else {
                likeIcon.hide()
                likeText.hide()
            }

            if (template!!.comment) {
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

    private fun bindLike(like: LikeViewModel) {
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
    private fun bindComment(comment: CommentViewModel) {
        commentText.text =
                if (comment.value == 0) getString(R.string.kol_action_comment)
                else comment.fmt
    }
}