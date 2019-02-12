package com.tokopedia.affiliate.feature.education.view.adapter

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.education.view.viewmodel.EducationItemViewModel
import com.tokopedia.design.viewpager.WrapContentViewPager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_af_aducation_carousel.view.*

/**
 * @author by milhamj on 15/01/19.
 */
class EducationAdapter(val list: List<EducationItemViewModel>) : PagerAdapter() {

    private var currentPosition = -1

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(
                R.layout.item_af_aducation_carousel,
                container,
                false
        )
        val viewHolder = EducationViewHolder(view)
        viewHolder.bind(list[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != currentPosition) {
            val view = `object` as View?
            val pager = container as WrapContentViewPager?
            if (view != null) {
                currentPosition = position
                pager?.measureCurrentView(view)
            }
        }
    }

    class EducationViewHolder(val itemView: View) {
        fun bind(model: EducationItemViewModel) {
            itemView.image.loadImage(model.imageUrl)
            itemView.title.text = model.title

            if (model.number != null) {
                itemView.numberBackground.show()
                itemView.numberText.show()
                itemView.numberText.text = model.number.toString()
            } else {
                itemView.numberBackground.hide()
                itemView.numberText.hide()
            }
        }
    }
}