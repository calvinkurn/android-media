package com.tokopedia.videoplayer.view.main

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.exoplayer2.Player.STATE_BUFFERING
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.tokopedia.videoplayer.R
import com.tokopedia.videoplayer.utils.sendViewToBack
import com.tokopedia.videoplayer.utils.showToast
import com.tokopedia.videoplayer.view.player.TkpdVideoPlayer
import com.tokopedia.videoplayer.view.player.VideoPlayerListener
import kotlinx.android.synthetic.main.fragment_video_preview.*

/**
 * Created by isfaaghyth on 25/04/19.
 * github: @isfaaghyth
 */
class VideoDetailPlayer: BottomSheetDialogFragment() {

    companion object {
        //keys
        private const val VIDEO_SOURCE = "video_uri"

        fun set(videoSource: String): BottomSheetDialogFragment {
            val videoPlayer = VideoDetailPlayer()
            val bundle = Bundle()
            bundle.putString(VIDEO_SOURCE, videoSource)
            videoPlayer.arguments = bundle
            return videoPlayer
        }

        fun show(videoSource: String, fragmentManager: FragmentManager) {
            val tag = VideoDetailPlayer::class.java.simpleName
            set(videoSource).show(fragmentManager, tag)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss()
                    }
                }
            })
        }

        initView()
    }

    fun initView() {
        btnClose.setOnClickListener { dismiss() }

        //showing close button on top
        sendViewToBack(playerView)

        //get video source path
        val videoSource = arguments?.getString(VIDEO_SOURCE, "")
        if (videoSource == null || videoSource.isEmpty()) {
            showToast(R.string.videoplayer_file_not_found)
            dismiss()
        } else {
            btnFile.setOnClickListener {
                TkpdVideoPlayer.Builder()
                        .transaction(R.id.playerView, childFragmentManager)
                        .videoSource(videoSource)
                        .listener(object : VideoPlayerListener {
                            override fun onPlayerStateChanged(playbackState: Int) {
                                when (playbackState) {
                                    STATE_BUFFERING -> showToast("lagi buffer")
                                }
                            }
                            override fun onPlayerError() {
                                showToast("error bro")
                            }
                        })
                        .build()
            }

            btnHttp.setOnClickListener {
                TkpdVideoPlayer.Builder()
                        .transaction(R.id.playerView, childFragmentManager)
                        .videoSource("https://www.w3schools.com/html/mov_bbb.mp4")
                        .listener(object : VideoPlayerListener {
                            override fun onPlayerStateChanged(playbackState: Int) {
                                when (playbackState) {
                                    STATE_BUFFERING -> showToast("lagi buffer")
                                    STATE_ENDED -> showToast("selesai!")
                                }
                            }
                            override fun onPlayerError() {
                                showToast("error bro")
                            }
                        })
                        .build()
            }

            btnRtmp.setOnClickListener {

            }
        }
    }

}