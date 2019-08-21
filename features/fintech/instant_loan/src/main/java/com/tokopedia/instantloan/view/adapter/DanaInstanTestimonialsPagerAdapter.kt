package com.tokopedia.instantloan.view.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.TestimonialEntity
import com.tokopedia.instantloan.view.ui.HeightWrappingViewPager
import kotlinx.android.synthetic.main.item_pager_testimonial.view.*

class DanaInstanTestimonialsPagerAdapter(private val context: Context, private val list: ArrayList<TestimonialEntity>) : PagerAdapter() {

    private val testimonialList: ArrayList<TestimonialEntity> = list
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun destroyItem(container: View, position: Int, any: Any) {
        (container as ViewPager).removeView(any as View)
    }

    override fun getCount(): Int {
        return testimonialList.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val banner = inflater.inflate(R.layout.item_pager_testimonial, view, false) as CardView
        ImageHandler.loadImageCircle2(context, banner.il_person_image, testimonialList[position].imageURL)
        banner.il_testimonial_text.text = testimonialList[position].review
        banner.il_testimonial_person_name.text = testimonialList[position].name
        view.addView(banner)
        return banner
    }

    private var mCurrentPosition: Int = -1;

    override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
        super.setPrimaryItem(container, position, any)
        if (container !is HeightWrappingViewPager) {
            throw UnsupportedOperationException("ViewPager is not a WrappingViewPager")
        }

        if (position != mCurrentPosition) {
            val view = any as CardView
            val pager = container as HeightWrappingViewPager?
            if (view != null) {
                mCurrentPosition = position
                pager!!.onPageChanged(view)
            }
        }
    }

}