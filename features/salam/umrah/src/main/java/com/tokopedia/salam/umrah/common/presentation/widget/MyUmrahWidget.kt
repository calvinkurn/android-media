package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
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

    fun buildView(trackingUmrahUtil: TrackingUmrahUtil) {
        if (::myUmrahModel.isInitialized) {
            hideLoadingState()

            tg_umrah_package.text = myUmrahModel.subHeader
            tg_umrah_departure.text = myUmrahModel.header
            tg_umrah_next.text = myUmrahModel.nextActionText
            btn_my_umrah_detail.text = myUmrahModel.mainButtonText
            btn_my_umrah_detail.setOnClickListener {
                trackingUmrahUtil.umrahOrderDetailUmrahSaya()
                RouteManager.route(context, myUmrahModel.mainButtonLink)
            }
        } else {
            showLoadingState()
        }
    }

    fun showLoadingState() {
        changeVisibility(View.VISIBLE, View.GONE)
    }

    fun hideLoadingState() {
        changeVisibility(View.GONE, View.VISIBLE)
    }

    private fun changeVisibility(loadingState: Int, contentState: Int) {
        tg_umrah_departure_loading.visibility = loadingState
        iv_my_umrah_loading.visibility = loadingState
        btn_my_umrah_detail_loading.visibility = loadingState
        tg_umrah_next_label_loading.visibility = loadingState
        tg_umrah_next_loading.visibility = loadingState
        tg_umrah_package_loading.visibility = loadingState

        tg_umrah_departure.visibility = contentState
        iv_my_umrah.visibility = contentState
        btn_my_umrah_detail.visibility = contentState
        tg_umrah_next_label.visibility = contentState
        tg_umrah_next.visibility = contentState
        tg_umrah_package.visibility = contentState
    }
}