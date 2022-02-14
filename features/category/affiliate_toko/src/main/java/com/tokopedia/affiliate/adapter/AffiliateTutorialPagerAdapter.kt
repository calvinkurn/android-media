package com.tokopedia.affiliate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tkpd.remoteresourcerequest.view.ImageDensityType
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateTutorialPagerAdapter(val list : ArrayList<LoginTutorialData>)
    : PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(
                LoginTutorialViewHolder.LAYOUT,
                container,
                false
        )
        val viewHolder = LoginTutorialViewHolder(view)
        viewHolder.bind(list[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    class LoginTutorialViewHolder(private val itemView: View) {

        fun bind(model: LoginTutorialData) {
            itemView.findViewById<Typography>(R.id.affiliate_tutorial_title).text = model.title
            itemView.findViewById<Typography>(R.id.affiliate_tutorial_subtitle).text = model.subTitle
            itemView.findViewById<DeferredImageView>(R.id.affiliate_tutorial_iv).loadRemoteImageDrawable(model.imageUrl,
                    ImageDensityType.SUPPORT_SINGLE_DPI)
        }
        companion object{
            val LAYOUT = R.layout.affiliate_login_tutorial_layout
        }
    }

    data class LoginTutorialData(val title : String, val subTitle : String, val imageUrl : String)
}