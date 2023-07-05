package com.tokopedia.media.preview.ui.component

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.R
import com.tokopedia.media.preview.ui.activity.pagers.adapter.PreviewPagerAdapter
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.uimodel.MediaUiModel

class PreviewPagerComponent(
    parent: ViewGroup
) : UiComponent(parent, R.id.vp_preview) {

    private val medias = arrayListOf<PreviewUiModel>()
    private var previousViewPagerIndex = 0

    private val viewPager by lazy {
        container() as ViewPager
    }

    private val adapter by lazy {
        PreviewPagerAdapter(
            context = context,
            elements = this.medias
        )
    }

    init {
        viewPager.addOnPageChangeListener(viewPagerListener())
    }

    fun setupView(medias: List<MediaUiModel>, firstRenderIndex: Int) {
        setData(medias)
        viewPager.adapter = adapter
        viewPager.currentItem = firstRenderIndex
        viewPager.addOnAttachStateChangeListener(attachStateListener())
    }

    fun removeData(media: MediaUiModel): Int {
        val element = getData(media)
        val removedIndex = adapter.remove(element)

        // play video if new selected index mVideoPlayer is ready
        adapter.getItem(viewPager.currentItem)?.mVideoPlayer?.start()

        return removedIndex
    }

    fun moveToOf(media: MediaUiModel): Int {
        val element = getData(media)
        val index = medias.indexOf(element)
        viewPager.setCurrentItem(index, false)
        return index
    }

    fun getSelectedIndex(): Int {
        return viewPager.currentItem
    }

    fun pauseVideoPlayer() {
        adapter.getItem(viewPager.currentItem)?.mVideoPlayer?.pause()
    }

    override fun release() {
        adapter.getItem(viewPager.currentItem)?.mVideoPlayer?.release()
    }

    private fun viewPagerListener() = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, posOffset: Float, posOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            val currentItem = medias[position]
            currentItem.mVideoPlayer?.let {
                if (!it.player().isPlaying) {
                    currentItem.mVideoPlayer?.start()
                }
            }

            if (previousViewPagerIndex in 0 until adapter.count) {
                val previousItem = medias[previousViewPagerIndex]
                if (previousItem.data.file?.isVideo() == true) {
                    previousItem.mVideoPlayer?.stop()
                }
            }

            previousViewPagerIndex = position
        }
    }

    private fun setData(medias: List<MediaUiModel>) {
        val toPreviewUiModel = medias.map {
            PreviewUiModel(it)
        }

        this.medias.clear()
        this.medias.addAll(toPreviewUiModel)
    }

    private fun getData(media: MediaUiModel) = medias.firstOrNull {
        it.data == media
    }

    private fun attachStateListener() = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View) {
            view.post { adapter.getItem(viewPager.currentItem)?.mVideoPlayer?.start() }
        }

        override fun onViewDetachedFromWindow(v: View) {
            adapter.getItem(viewPager.currentItem)?.mVideoPlayer?.stop()
        }
    }
}
