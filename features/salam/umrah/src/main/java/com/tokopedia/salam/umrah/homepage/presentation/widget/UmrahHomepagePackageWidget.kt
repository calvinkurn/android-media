package com.tokopedia.salam.umrah.homepage.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.data.model.UmrahHomepagePackageWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_homepage_package.view.*

class UmrahHomepagePackageWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr){

    private lateinit var umrahHomepagePackageWidgetModel: UmrahHomepagePackageWidgetModel
    init {
        View.inflate(context, R.layout.widget_umrah_homepage_package, this)
    }

    fun buildView() {
        if (::umrahHomepagePackageWidgetModel.isInitialized) {
            hideLoading()

            tg_umrah_package_name.text = umrahHomepagePackageWidgetModel.packageName
            tg_umrah_price.text = umrahHomepagePackageWidgetModel.price
        } else {
            showLoading()
        }
    }

    private fun showLoading(){
        container_umrah_homepage_package_shimmering.visibility = View.VISIBLE
        container_umrah_homepage_package.visibility = View.GONE
    }
    private fun hideLoading(){
        container_umrah_homepage_package_shimmering.visibility = View.GONE
        container_umrah_homepage_package.visibility = View.VISIBLE
    }
}