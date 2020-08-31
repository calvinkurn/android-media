package com.tokopedia.onboarding.view.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.data.OnboardingScreenItem
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Ade Fulki on 2020-02-09.
 * ade.hadian@tokopedia.com
 */

class OnboardingViewPagerAdapter(
        private val context: Context,
        val listScreen: List<OnboardingScreenItem>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val displayMetrics = DisplayMetrics()
        val windowManager =
                context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceHeight = displayMetrics.heightPixels

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen = inflater.inflate(R.layout.layout_onboard_item, null)

        val imgContent = layoutScreen.findViewById<ImageView>(R.id.image_content)
        val titleContent = layoutScreen.findViewById<Typography>(R.id.title_content)

        val item = listScreen[position]
        ImageUtils.loadImage(imgContent, item.imageUrl, item.placeholder)

        titleContent.text = item.title

        if (deviceHeight <= minimumHeight) {
            titleContent.setType(Typography.HEADING_2)
        }

        container.addView(layoutScreen)
        return layoutScreen
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = listScreen.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    companion object {
        private const val minimumHeight = 800
    }
}