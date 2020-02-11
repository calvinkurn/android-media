package com.tokopedia.onboarding.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.data.OnboardingScreenItem
import com.tokopedia.unifycomponents.UnifyButton
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
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen = inflater.inflate(R.layout.layout_onboard_item, null)

        val imgContent = layoutScreen.findViewById<ImageView>(R.id.image_content)
        val titleContent = layoutScreen.findViewById<Typography>(R.id.title_content)

        val item = listScreen[position]
        imgContent.setImageDrawable(item.image)
        titleContent.text = item.title

        container.addView(layoutScreen)
        return layoutScreen
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = listScreen.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}