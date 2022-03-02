package com.tokopedia.media.preview.ui.component

import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.R
import com.tokopedia.media.common.basecomponent.UiComponent
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.preview.ui.activity.pagers.adapter.PreviewPagerAdapter
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
    }

    fun removeData(media: MediaUiModel) {
        val element = getData(media)
        adapter.remove(element)
    }

    fun moveToOf(media: MediaUiModel) {
        val element = getData(media)
        val index = medias.indexOf(element)
        viewPager.setCurrentItem(index, false)
    }

    override fun release() {

    }

    private fun viewPagerListener() = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, posOffset: Float, posOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
//            val currentItem = medias[position]
//            if (currentItem.data.isVideo() && viewPager.currentItem == position) {
//                // getVideoPlayerActive()?.start()
//            }
//
//            val previousItem = medias[previousViewPagerIndex]
//            if (previousItem.data.isVideo()) {
//                // getVideoPlayerActive()?.stop()
//            }

            previousViewPagerIndex = position
        }
    }

    private fun setData(medias: List<MediaUiModel>) {
        val asPreviewUiModel = medias.map {
            PreviewUiModel(it)
        }

        this.medias.clear()
        this.medias.addAll(asPreviewUiModel)
    }

    private fun getData(media: MediaUiModel)
        = medias.firstOrNull {
            it.data == media
        }

    interface Listener {

    }

}