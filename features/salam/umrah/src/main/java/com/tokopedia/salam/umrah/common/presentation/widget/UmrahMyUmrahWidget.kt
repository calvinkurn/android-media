package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.common.presentation.model.UmrahMyUmrahWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_my_umrah.view.*

/**
 * @author by furqan on 10/10/2019
 */
class UmrahMyUmrahWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var umrahMyUmrahModel: UmrahMyUmrahWidgetModel

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
        if (::umrahMyUmrahModel.isInitialized) {
            hideLoadingState()

            tg_umrah_package.text = umrahMyUmrahModel.subHeader
            tg_umrah_departure.text = umrahMyUmrahModel.header
            tg_umrah_next.text = umrahMyUmrahModel.nextActionText
            btn_my_umrah_detail.text = umrahMyUmrahModel.mainButtonText
            btn_my_umrah_detail.setOnClickListener {
                trackingUmrahUtil.umrahOrderDetailUmrahSaya(umrahMyUmrahModel.nextActionText)
                RouteManager.route(context, umrahMyUmrahModel.mainButtonLink)
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