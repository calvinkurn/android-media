package com.tokopedia.product.manage.feature.stockreminder.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.stockreminder.constant.AppScreen
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment
import kotlinx.android.synthetic.main.activity_stock_reminder.*

class StockReminderActivity : BaseSimpleActivity() {

    private var productName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)

        header.backButtonView?.setOnClickListener { onBackPressed() }
        header.subTitle = productName
    }

    override fun getNewFragment(): Fragment? {
        var productId = 0L
        val uri = intent.data
        if (uri != null) {
            val segments = uri.pathSegments
            productId = segments[segments.size - 2].toLong()
            productName = segments[segments.size - 1]
        }
        return StockReminderFragment.createInstance(productId, productName)
    }

    override fun getLayoutRes(): Int = R.layout.activity_stock_reminder

    override fun getScreenName(): String = AppScreen.SCREEN_STOCK_REMINDER

}
