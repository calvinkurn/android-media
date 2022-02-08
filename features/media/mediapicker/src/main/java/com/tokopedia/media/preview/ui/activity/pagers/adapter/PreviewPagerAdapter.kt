package com.tokopedia.media.preview.ui.activity.pagers.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.preview.ui.activity.pagers.views.BasePagerPreview
import com.tokopedia.media.preview.ui.activity.pagers.views.ImagePreview
import com.tokopedia.media.preview.ui.activity.pagers.views.VideoPreview

class PreviewPagerAdapter constructor(
    private val context: Context,
    private val elements: List<MediaUiModel> = emptyList()
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount() = elements.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = elements[position]

        val preview: BasePagerPreview = if (item.isVideo()) {
            VideoPreview(context)
        } else {
            ImagePreview(context)
        }

        return preview
            .setupView(item)
            .also {
                container.addView(it)
            }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

}