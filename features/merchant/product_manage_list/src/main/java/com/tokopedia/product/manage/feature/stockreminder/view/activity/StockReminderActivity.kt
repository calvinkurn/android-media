package com.tokopedia.product.manage.feature.stockreminder.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ActivityStockReminderBinding
import com.tokopedia.product.manage.feature.stockreminder.constant.AppScreen
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment

class StockReminderActivity : BaseSimpleActivity() {

    private var productName: String = ""

    private var binding: ActivityStockReminderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLayout(savedInstanceState)
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

    override fun setupLayout(savedInstanceState: Bundle?) {
        binding = ActivityStockReminderBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupView()
    }

    override fun getScreenName(): String = AppScreen.SCREEN_STOCK_REMINDER

    override fun getParentViewResourceID(): Int = R.id.parent_view

    private fun setupView() {
        binding?.header?.run {
            setNavigationOnClickListener {
                onBackPressed()
            }
            headerTitle = getString(R.string.product_stock_reminder_header_title)
            headerSubTitle = productName
        }
    }
}
