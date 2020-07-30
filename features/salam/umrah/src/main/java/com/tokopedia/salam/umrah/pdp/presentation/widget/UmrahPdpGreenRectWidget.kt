package com.tokopedia.salam.umrah.pdp.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpGreenRectWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_pdp_green_rect.view.*
import kotlinx.android.synthetic.main.widget_umrah_pdp_green_rect_shimmering.view.*

/**
 * @author by M on 22/10/19
 */
class UmrahPdpGreenRectWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var umrahPdpGreenRectWidgetModel: UmrahPdpGreenRectWidgetModel

    init {
        View.inflate(context, R.layout.widget_umrah_pdp_green_rect, this)
    }

    fun buildView() {
        if (::umrahPdpGreenRectWidgetModel.isInitialized) {
            hideLoading()

            val sizeInDP = 4
            val marginInDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDP.toFloat(), resources.displayMetrics).toInt()
            tg_widget_umrah_pdp_green_rect_title.text = umrahPdpGreenRectWidgetModel.title
            tg_widget_umrah_pdp_green_rect_first_line.text = umrahPdpGreenRectWidgetModel.descFirstLine
            iv_widget_umrah_pdp_green_rect_action.loadImageDrawable(umrahPdpGreenRectWidgetModel.imageActionUrl)
            checkIfImageFirstLineNotNull(marginInDp)
            checkIfImageSecondLineNotNull(marginInDp)
            checkIfDescSecondLineNotNull()
        } else {
            showLoading()
        }
    }

    private fun checkIfDescSecondLineNotNull() {
        umrahPdpGreenRectWidgetModel.descSecondLine?.let {
            tg_widget_umrah_pdp_green_rect_second_line.visibility = View.VISIBLE
            tg_widget_umrah_pdp_green_rect_second_line.text = umrahPdpGreenRectWidgetModel.descSecondLine
        }
    }

    private fun checkIfImageSecondLineNotNull(marginInDp: Int) {
        umrahPdpGreenRectWidgetModel.imageSecondLineUrl?.let {
            iv_widget_umrah_pdp_green_rect_second_line.loadImageDrawable(it)
            tg_widget_umrah_pdp_green_rect_second_line.setMargin(marginInDp, 0, 0, 0)
            iv_widget_umrah_pdp_green_rect_second_line.visibility = View.VISIBLE
        }
    }

    private fun checkIfImageFirstLineNotNull(marginInDp: Int) {
        umrahPdpGreenRectWidgetModel.imageFirstLineUrl?.let {
            iv_widget_umrah_pdp_green_rect_first_line.loadImageDrawable(it)
            iv_widget_umrah_pdp_green_rect_first_line.visibility = View.VISIBLE
            tg_widget_umrah_pdp_green_rect_first_line.setMargin(marginInDp, 0, 0, 0)

        }
    }

    private fun showLoading() {
        container_widget_umrah_pdp_green_rect_shimmering.visibility = View.VISIBLE
        container_widget_umrah_pdp_green_rect.visibility = View.GONE
    }

    private fun hideLoading() {
        container_widget_umrah_pdp_green_rect_shimmering.visibility = View.GONE
        container_widget_umrah_pdp_green_rect.visibility = View.VISIBLE
    }
}