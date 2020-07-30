package com.tokopedia.product.manage.feature.stockreminder.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.stockreminder.constant.AppScreen
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment
import kotlinx.android.synthetic.main.activity_stock_reminder.*

class StockReminderActivity : BaseSimpleActivity() {

    private var productName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)

        header.setNavigationOnClickListener {
            onBackPressed()
        }
        header.headerTitle = getString(R.string.product_stock_reminder_header_title)
        header.headerSubTitle = productName
    }

    override fun getNewFragment(): Fragment? {
        var productId = 0L
        var stock = 0
        val uri = intent.data
        if (uri != null) {
            val segments = uri.pathSegments
            productId = segments[segments.size - 3].toLongOrZero()
            productName = segments[segments.size - 2]
            stock = segments[segments.size - 1].toIntOrZero()
        }
        return StockReminderFragment.createInstance(productId, productName, stock)
    }

    override fun getLayoutRes(): Int = R.layout.activity_stock_reminder

    override fun getScreenName(): String = AppScreen.SCREEN_STOCK_REMINDER

    override fun getParentViewResourceID(): Int = R.id.parent_view
}
