package com.tokopedia.travelhomepage.homepage.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageLegoBannerAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_travel_homepage_lego_banner_widget.view.*

/**
 * @author by jessica on 2020-02-28
 */

class TravelHomepageLegoBannerWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var titleText: String = ""
    var subtitleText: String = ""
    lateinit var adapter: TravelHomepageLegoBannerAdapter
    var listener: ActionListener? = null

    init {
        View.inflate(context, R.layout.layout_travel_homepage_lego_banner_widget, this)
    }

    fun buildView(bannerList: List<LegoBannerItemModel>) {
        travel_homepage_lego_banner_title.text = titleText

        if (subtitleText.isNotEmpty()) {
            travel_homepage_lego_banner_subtitle.show()
            travel_homepage_lego_banner_subtitle.text = subtitleText
        } else travel_homepage_lego_banner_subtitle.hide()

        val listSize = bannerList.size
        adapter = if (listSize > 3 && listSize % 3 != 0) {
            TravelHomepageLegoBannerAdapter(bannerList.subList(0, listSize - (listSize % 3)), listener)
        } else
            TravelHomepageLegoBannerAdapter(bannerList, listener)

        travel_homepage_lego_banner_rv.layoutManager = GridLayoutManager(context, 3)
        travel_homepage_lego_banner_rv.adapter = adapter

    }

    fun setShimmeringVisibility(isShow: Boolean) {
        lego_banner_shimmering.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setLayoutVisibility(isShow: Boolean) {
        travel_homepage_lego_banner_layout.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    interface ActionListener {
        fun onItemClickListener(item: LegoBannerItemModel, position: Int)
    }
}