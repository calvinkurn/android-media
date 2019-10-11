package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.presentation.model.MyUmrahWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_my_umrah.view.*

/**
 * @author by furqan on 10/10/2019
 */
class MyUmrahWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var myUmrahModel: MyUmrahWidgetModel

    init {
        View.inflate(context, R.layout.widget_umrah_my_umrah, this)

        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.MyUmrahWidgetView)

            if (styledAttributes.getBoolean(R.styleable.MyUmrahWidgetView_showLoadingState, true)) {
                showLoadingState()
            }

            styledAttributes.recycle()
        }
    }

    fun buildView() {
        if (::myUmrahModel.isInitialized) {
            hideLoadingState()

            tg_umrah_package.text = myUmrahModel.header
            tg_umrah_departure.text = myUmrahModel.subHeader
            tg_umrah_next.text = myUmrahModel.nextActionText
            btn_my_umrah_detail.text = myUmrahModel.mainButton.text
            btn_my_umrah_detail.setOnClickListener { RouteManager.route(context, myUmrahModel.mainButton.link) }
        } else {
            showLoadingState()
        }
    }

    fun showLoadingState() {
        tg_umrah_departure_loading.visibility = View.VISIBLE
        iv_my_umrah_loading.visibility = View.VISIBLE
        btn_my_umrah_detail_loading.visibility = View.VISIBLE
        tg_umrah_next_label_loading.visibility = View.VISIBLE
        tg_umrah_next_loading.visibility = View.VISIBLE
        tg_umrah_package_loading.visibility = View.VISIBLE

        tg_umrah_departure.visibility = View.GONE
        iv_my_umrah.visibility = View.GONE
        btn_my_umrah_detail.visibility = View.GONE
        tg_umrah_next_label.visibility = View.GONE
        tg_umrah_next.visibility = View.GONE
        tg_umrah_package.visibility = View.GONE
    }

    fun hideLoadingState() {
        tg_umrah_departure_loading.visibility = View.GONE
        iv_my_umrah_loading.visibility = View.GONE
        btn_my_umrah_detail_loading.visibility = View.GONE
        tg_umrah_next_label_loading.visibility = View.GONE
        tg_umrah_next_loading.visibility = View.GONE
        tg_umrah_package_loading.visibility = View.GONE

        tg_umrah_departure.visibility = View.VISIBLE
        iv_my_umrah.visibility = View.VISIBLE
        btn_my_umrah_detail.visibility = View.VISIBLE
        tg_umrah_next_label.visibility = View.VISIBLE
        tg_umrah_next.visibility = View.VISIBLE
        tg_umrah_package.visibility = View.VISIBLE
    }
}