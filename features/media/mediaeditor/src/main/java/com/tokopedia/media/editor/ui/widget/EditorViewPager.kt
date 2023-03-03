package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.EditorViewPagerAdapter
import com.tokopedia.media.editor.ui.adapter.viewPagerTag
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.LOAD_IMAGE_FAILED
import com.tokopedia.media.editor.utils.newRelicLog
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.MediaException
import com.tokopedia.unifycomponents.Toaster

class EditorViewPager(context: Context, attrSet: AttributeSet) : ViewPager(context, attrSet),
    EditorViewPagerAdapter.Listener {
    private var editorAdapter: EditorViewPagerAdapter? = null
    private var previousVideoIndex = INITIAL_VIEW_PAGER_INDEX
    private var callback: (position: Int, isVideo: Boolean) -> Unit = { _, _ -> }

    fun setAdapter(listData: List<EditorUiModel>) {
        editorAdapter = EditorViewPagerAdapter(context, listData, this)
        adapter = editorAdapter

        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                stopVideoPlayer(previousVideoIndex)

                val isVideo = editorAdapter?.isVideo(position) ?: false
                if (isVideo) {
                    playVideoPlayer(position)
                    previousVideoIndex = position
                }

                callback(position, isVideo)
            }
        })

        editorAdapter?.let {
            if (it.isVideo(INITIAL_VIEW_PAGER_INDEX)) {
                it.playVideo(INITIAL_VIEW_PAGER_INDEX)
            }
        }
    }

    fun playVideoPlayer(index: Int) {
        editorAdapter?.playVideo(index)
    }

    fun stopVideoPlayer(index: Int) {
        editorAdapter?.stopVideo(index)
    }

    fun updateImage(
        index: Int,
        newImageUrl: String,
        overlayImageUrl: String = "",
        onImageUpdated: () -> Unit = {}
    ) {
        val layout = findViewWithTag<RelativeLayout>(viewPagerTag(index))
        val view = layout.findViewById<ImageView>(R.id.img_main_preview)
        view?.loadImage(newImageUrl) {
            listener(
                onSuccess = { _, _ ->
                    view.post {
                        onImageUpdated()
                    }
                },
                onError = {
                    errorHandler(it)
                }
            )
        }

        layout.findViewById<ImageView>(R.id.img_main_overlay).apply {
            if (overlayImageUrl.isNotEmpty()) {
                loadImage(overlayImageUrl)
            } else {
                setImageDrawable(null)
            }
        }
    }

    fun setOnPageChanged(callback: (position: Int, isVideo: Boolean) -> Unit) {
        this.callback = callback
    }

    override fun onErrorImageLoad(exception: MediaException?) {
        errorHandler(exception)
    }

    private fun errorHandler(exception: MediaException?) {
        newRelicLog(
            mapOf(
                LOAD_IMAGE_FAILED to "${exception?.message}"
            )
        )
        errorLoadToaster()
    }

    private fun errorLoadToaster() {
        Toaster.build(
            this,
            context.resources.getString(R.string.editor_activity_failed_load_image),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR
        ).show()
    }

    companion object {
        private const val INITIAL_VIEW_PAGER_INDEX = 0
    }
}
