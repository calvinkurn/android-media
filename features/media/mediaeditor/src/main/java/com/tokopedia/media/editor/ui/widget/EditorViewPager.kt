package com.tokopedia.media.editor.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.EditorViewPagerAdapter
import com.tokopedia.media.editor.ui.adapter.viewPagerTag
import com.tokopedia.media.editor.ui.fragment.EditorFragment
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.showErrorLoadToaster
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.data.MediaException
import com.tokopedia.media.loader.loadImage

class EditorViewPager(context: Context, attrSet: AttributeSet) : ViewPager(context, attrSet),
    EditorViewPagerAdapter.Listener {
    private var editorAdapter: EditorViewPagerAdapter? = null
    private var previousVideoIndex = INITIAL_VIEW_PAGER_INDEX
    private var callback: (position: Int, isVideo: Boolean) -> Unit = { _, _ -> }
    private var data = listOf<EditorUiModel>()

    fun setAdapter(listData: List<EditorUiModel>) {
        data = listData

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
        overlaySecondaryImageUrl: String = "",
        onImageUpdated: () -> Unit = {}
    ) {
        val layout = findViewWithTag<RelativeLayout>(viewPagerTag(index))
        val view = layout?.findViewById<ImageView>(R.id.img_main_preview)
        view?.loadImage(newImageUrl) {
            useCache(EditorFragment.IS_USING_CACHE)
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
        } ?: kotlin.run {
            errorHandler()
        }

        layout?.let {
            it.findViewById<ImageView>(R.id.img_main_overlay)?.apply {
                if (overlayImageUrl.isNotEmpty()) {
                    loadImage(overlayImageUrl)
                } else {
                    setImageDrawable(null)
                }
            } ?: kotlin.run {
                errorHandler()
            }

            it.findViewById<ImageView>(R.id.img_secondary_overlay)?.apply {
                if (overlaySecondaryImageUrl.isNotEmpty()) {
                    loadImage(overlaySecondaryImageUrl)
                } else {
                    setImageDrawable(null)
                }
            } ?: kotlin.run {
                errorHandler()
            }
        }
    }

    fun setOnPageChanged(callback: (position: Int, isVideo: Boolean) -> Unit) {
        this.callback = callback
    }

    fun releaseImage() {
        getActiveIndexList().forEach {
            val layout = findViewWithTag<RelativeLayout>(viewPagerTag(it))
            layout?.findViewById<ImageView>(R.id.img_main_preview)?.let {
                it.clearImage()
            }
        }
    }

    fun reloadImage() {
        adapter?.let {
            getActiveIndexList().forEach {
                val layout = findViewWithTag<RelativeLayout>(viewPagerTag(it))
                layout?.findViewById<ImageView>(R.id.img_main_preview)?.loadImage(
                    data[it].getImageUrl()
                ) {
                    setCacheStrategy(EditorFragment.CACHE_STRATEGY)
                    useCache(EditorFragment.IS_USING_CACHE)
                }
            }
        }
    }

    override fun onErrorImageLoad(exception: MediaException?) {
        errorHandler(exception)
    }

    private fun errorHandler(exception: MediaException? = null) {
        showErrorLoadToaster(this, exception?.message ?: "")
    }

    private fun getActiveIndexList(): List<Int> {
        val indexList = mutableListOf(currentItem, currentItem + 1)

        if (currentItem > 0) {
            indexList.add(0, currentItem - 1)
        }

        return indexList
    }

    companion object {
        private const val INITIAL_VIEW_PAGER_INDEX = 0
    }
}
