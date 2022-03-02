package com.tokopedia.media.preview.ui.activity.pagers.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.media.preview.ui.activity.pagers.views.ImagePreview
import com.tokopedia.media.preview.ui.activity.pagers.views.VideoPreview
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel

class PreviewPagerAdapter constructor(
    private val context: Context,
    private val elements: MutableList<PreviewUiModel> = mutableListOf()
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = elements[position]

        val preview = if (item.data.isVideo()) {
            VideoPreview(context, item.videoPlayer(context))
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
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any) = POSITION_NONE

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun getCount() = elements.size

    fun remove(element: PreviewUiModel?) {
        val position = elements.indexOf(element)

        if (position > -1 && position < elements.size) {
            elements.removeAt(position)
            notifyDataSetChanged()
        }
    }

}