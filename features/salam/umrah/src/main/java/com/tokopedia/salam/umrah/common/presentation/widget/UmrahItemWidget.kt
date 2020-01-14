package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahItemWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_item.view.*

/**
 * @author by M on 22/10/19
 */
class UmrahItemWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr){

    lateinit var umrahItemWidgetModel: UmrahItemWidgetModel
    init {
        View.inflate(context, R.layout.widget_umrah_item, this)
    }

    fun buildView() {
        if (::umrahItemWidgetModel.isInitialized) {
            hideLoading()

            if(umrahItemWidgetModel.imageUri!="") iv_widget_umrah_pdp_item.loadImageCircle(umrahItemWidgetModel.imageUri)
            else {
                iv_widget_umrah_pdp_item.loadImageDrawable(umrahItemWidgetModel.imageDrawable)
                umrah_pdp_item_circle_bg.visibility = View.GONE
            }
            tg_widget_umrah_pdp_item_title.text = umrahItemWidgetModel.title
            tg_widget_umrah_pdp_item_desc.text = umrahItemWidgetModel.desc
        } else {
            showLoading()
        }
    }

    fun setPermissionPdp(){
        tg_widget_umrah_pdp_item_desc.text = resources.getString(R.string.umrah_pdp_permission_number,umrahItemWidgetModel.desc)
        tg_widget_umrah_pdp_item_title.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_96))
        tg_widget_umrah_pdp_item_title.setWeight(2)
        tg_widget_umrah_pdp_item_desc.setTextColor(resources.getColor(R.color.umrah_green_permission))
        tg_widget_umrah_pdp_item_desc.setWeight(2)
    }

    fun setVerifiedTravel(){
        iv_umrah_verified_blue.visibility = View.VISIBLE
    }

    private fun showLoading(){
        container_widget_umrah_pdp_item_shimmering.visibility = View.VISIBLE
        container_widget_umrah_pdp_item.visibility = View.GONE
    }
    private fun hideLoading(){
        container_widget_umrah_pdp_item_shimmering.visibility = View.GONE
        container_widget_umrah_pdp_item.visibility = View.VISIBLE
    }
}