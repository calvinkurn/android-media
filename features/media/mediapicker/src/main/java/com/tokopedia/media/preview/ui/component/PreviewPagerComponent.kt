package com.tokopedia.media.preview.ui.component

import android.view.ViewGroup
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.R
import com.tokopedia.media.common.basecomponent.UiComponent
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.preview.ui.activity.pagers.adapter.PreviewPagerAdapter
import com.tokopedia.media.preview.ui.player.PickerVideoPlayer
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel

class PreviewPagerComponent(
    parent: ViewGroup
) : UiComponent(parent, R.id.vp_preview) {

    private val medias = arrayListOf<PreviewUiModel>()
    private var previousViewPagerIndex = 0

    private val viewPager by lazy {
        componentView() as ViewPager
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

    fun setupView(medias: List<MediaUiModel>) {
        setData(medias)

        viewPager.adapter = adapter

        // play if the current page is video
        //TODO still null
        getVideoPlayerActive()?.start()
    }

    fun removeData(media: MediaUiModel) {
        val element = medias.first { it.data == media }
        adapter.removeItem(element)
    }

    fun moveToOf(media: MediaUiModel) {
        val element = medias.first { it.data == media }
        val index = medias.indexOf(element)

        viewPager.setCurrentItem(index, false)
    }

    override fun release() {
        getVideoPlayerActive()?.release()
    }

    private fun viewPagerListener() = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, posOffset: Float, posOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            val currentItem = medias[position]
            if (currentItem.data.isVideo() && viewPager.currentItem == position) {
                getVideoPlayerActive()?.start()
            }

            val previousItem = medias[previousViewPagerIndex]
            if (previousItem.data.isVideo()) {
                getVideoPlayerActive()?.stop()
            }

            previousViewPagerIndex = position
        }
    }

    private fun setData(medias: List<MediaUiModel>) {
        this.medias.clear()
        this.medias.addAll(medias.map {
            PreviewUiModel(it)
        })
    }

    private fun getVideoPlayerActive(
        position: Int = viewPager.currentItem
    ): PickerVideoPlayer? {
        if (viewPager.currentItem < 0) return null
        if (medias.isEmpty()) return null

        return medias[position].mVideoPlayer
    }

    interface Listener {

    }

}