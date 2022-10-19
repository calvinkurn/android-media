package com.tokopedia.media.editor.ui.adapter

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.utils.isVideoFormat

fun viewPagerTag(value: Int): String {
    return "editor_viewpager_$value"
}

class EditorViewPagerAdapter(
    private val context: Context,
    private val editorList: List<EditorUiModel>
) : PagerAdapter() {
    private val currentPlayer: Array<Player?> = Array(editorList.size) { null }

    fun playVideo(index: Int) {
        currentPlayer[index]?.let {
            it.playWhenReady = true

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                it.previous()
            }
        }
    }

    fun stopVideo(index: Int) {
        currentPlayer[index]?.playWhenReady = false
    }

    fun isVideo(index: Int): Boolean {
        return editorList[index].isVideo
    }

    override fun getCount(): Int {
        return editorList.size
    }

    override fun isViewFromObject(view: View, targetObject: Any): Boolean {
        return view == targetObject
    }

    override fun destroyItem(container: ViewGroup, position: Int, targetObject: Any) {
        container.removeView(targetObject as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = container.inflateLayout(R.layout.viewpager_main_editor)
        layout.tag = viewPagerTag(position)

        val uiModel = editorList[position]
        val filePath = uiModel.getImageUrl()
        if (isVideoFormat(filePath)) {
            val vidPreviewRef = layout.findViewById<PlayerView>(R.id.vid_main_preview)
            vidPreviewRef.visible()

            val exoplayer = SimpleExoPlayer.Builder(context).build()
            vidPreviewRef.player = exoplayer
            currentPlayer[position] = exoplayer

            val loopingMediaSource = LoopingMediaSource(getOrCreateMediaSource(Uri.parse(filePath)))
            exoplayer.prepare(loopingMediaSource, true, false)

            vidPreviewRef.controllerShowTimeoutMs = 0
        } else {
            val imgPreviewRef = layout.findViewById<ImageView>(R.id.img_main_preview)
            imgPreviewRef.visible()

            imgPreviewRef.loadImage(filePath) {
                listener(
                    onSuccess = { bitmap, _ ->
                        bitmap?.let {
                            uiModel.originalRatio = bitmap.width.toFloat() / bitmap.height
                        }
                    }
                )
            }
        }

        container.addView(layout)
        return layout
    }

    private fun getOrCreateMediaSource(uri: Uri): MediaSource {
        val userAgent = Util.getUserAgent(context, "Tokopedia")
        val dataSourceFactory = DefaultDataSourceFactory(context, userAgent)
        val mediaSourceFactory = generateMediaSourceFactory(uri, dataSourceFactory)
        return mediaSourceFactory.createMediaSource(uri)
    }

    private fun generateMediaSourceFactory(
        uri: Uri,
        dsFactory: DataSource.Factory
    ): MediaSourceFactory {
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dsFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dsFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dsFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dsFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }
}