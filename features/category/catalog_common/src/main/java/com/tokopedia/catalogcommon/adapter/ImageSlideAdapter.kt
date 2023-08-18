package com.tokopedia.catalogcommon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.catalogcommon.databinding.ItemSliderTextImageBinding
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImageRounded

class ImageSlideAdapter(private var imageList: List<SliderImageTextUiModel.ItemSliderImageText>) :
    PagerAdapter() {

    private val views = mutableListOf<View>()

    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemSliderTextImageBinding.inflate(
            LayoutInflater.from(container.context),
            container,
            false
        )
        val layoutParam = LinearLayout.LayoutParams(
            binding.ivImage.layoutParams.width,
            binding.ivImage.layoutParams.height
        )
        val displayMetrics = container.resources.displayMetrics

        binding.ivImage.layoutParams = layoutParam

        imageList[position].let { item ->
            when (imageList.indexOf(item)) {
                Int.ZERO -> {
                    layoutParam.marginStart = 16.dpToPx(displayMetrics)
                }

                imageList.size - 1 -> {
                    layoutParam.marginEnd = 16.dpToPx(displayMetrics)
                }

                else -> {
                    layoutParam.marginEnd = 16.dpToPx(displayMetrics)
                    layoutParam.marginStart = 16.dpToPx(displayMetrics)
                }
            }
            binding.ivImage.loadImageRounded(item.image)
        }

        binding.root.tag = position.toString()
        val vp = container as ViewPager
        views.add(binding.root)
        vp.addView(binding.root, 0)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        setSelectedItemColor(position)
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getPageWidth(position: Int): Float {
        return (0.75f)
    }

    private fun setSelectedItemColor(currentSelectionPosition: Int) {
        val previousSelectionPosition = currentSelectionPosition - 1
        val nexSelectionPosition = currentSelectionPosition + 1

        var view = views[currentSelectionPosition]
        view.alpha = 1f
        view.requestLayout()

        if (previousSelectionPosition != -1) {
            view = views[previousSelectionPosition]
            view.alpha = 0.3f
            view.requestLayout()
        }

        if (nexSelectionPosition <= imageList.size - 1) {
            view = views[nexSelectionPosition]
            view.alpha = 0.3f
            view.requestLayout()
        }
    }

}
