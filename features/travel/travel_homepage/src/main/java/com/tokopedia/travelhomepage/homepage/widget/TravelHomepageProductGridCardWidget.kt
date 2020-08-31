package com.tokopedia.travelhomepage.homepage.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.TravelHomepageProductGridCardAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_travel_homepage_product_widget.view.*

/**
 * @author by jessica on 2020-02-28
 */

class TravelHomepageProductGridCardWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
    var titleText: String = ""
    var subtitleText: String = ""
    var buttonText: String = ""
    lateinit var adapter: TravelHomepageProductGridCardAdapter
    var hasSeeAllButton = true

    init {
        View.inflate(context, R.layout.layout_travel_homepage_product_widget, this)
    }

    fun buildView(productList: List<ProductGridCardItemModel>) {

        if (titleText.isNotEmpty()) {
            travel_homepage_product_widget_title.show()
            travel_homepage_product_widget_title.text = titleText
        } else travel_homepage_product_widget_title.hide()

        if (subtitleText.isNotEmpty()) {
            travel_homepage_product_widget_subtitle.show()
            travel_homepage_product_widget_subtitle.text = subtitleText
        } else travel_homepage_product_widget_subtitle.hide()

        val listSize = productList.size
        adapter = if (listSize > 2 && listSize % 2 != 0) {
            TravelHomepageProductGridCardAdapter(productList.subList(0, listSize - (listSize % 2)), listener)
        } else TravelHomepageProductGridCardAdapter(productList, listener)

        travel_homepage_product_rv.layoutManager = GridLayoutManager(context, 2)

        travel_homepage_product_rv.adapter = adapter

        if (hasSeeAllButton) {
            travel_homepage_product_widget_see_all.show()
            travel_homepage_product_widget_see_all.text = buttonText
            travel_homepage_product_widget_see_all.setOnClickListener { listener?.onClickSeeAllListener() }
        } else travel_homepage_product_widget_see_all.hide()
    }

    fun setLayoutVisibility(isShow: Boolean) {
        travel_homepage_product_widget_layout.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setShimmeringVisibility(isShow: Boolean) {
        travel_product_widget_shimmering.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    interface ActionListener {
        fun onItemClickListener(item: ProductGridCardItemModel, position: Int)
        fun onClickSeeAllListener()
    }
}