package com.tokopedia.catalogcommon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.catalogcommon.databinding.ItemSliderTextImageBinding
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImageRounded

class ImageSlidePagerAdapter(private var imageList: List<SliderImageTextUiModel.ItemSliderImageText>) :
    PagerAdapter() {

    companion object{
        private const val THIRTY_PERCENT_TRANSPARANT = 0.3f
        private const val SEVENTY_FIVE_PERCENT_TRANSPARANT = 0.75f
        private const val NOT_TRANSPARANT = 1f

    }
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
        return SEVENTY_FIVE_PERCENT_TRANSPARANT
    }

    private fun setSelectedItemColor(currentSelectionPosition: Int) {
        val view = views[currentSelectionPosition]
        view.alpha = NOT_TRANSPARANT
        view.requestLayout()
    }

    fun resetAlphaView(){
        views.forEach {view ->
            view.alpha = THIRTY_PERCENT_TRANSPARANT
            view.requestLayout()
        }
    }

}
