package com.tokopedia.product.manage.feature.stockreminder.view.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ActivityStockReminderBinding
import com.tokopedia.product.manage.feature.stockreminder.constant.AppScreen
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment

class StockReminderActivity : BaseSimpleActivity() {

    companion object {
        private const val SLASH_CHAR = "/"
    }

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
            val (infoProductId, infoProductName, infoStock) = uri.getProductInformation()
            productId = infoProductId
            productName = infoProductName
            stock = infoStock
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

    /**
     * Parse uri to get relevant product information
     * This method is used to suffice requirement for product which name contains slash ("/")
     *
     * @return  Triple of productId, productName, and stock
     */
    private fun Uri.getProductInformation(): Triple<Long, String, Int> {
        val uriString =
            this.toString().replace(ApplinkConstInternalMarketplace.STOCK_REMINDER_BASE, "")
        val productId = uriString.substringBefore(SLASH_CHAR).toLongOrZero()
        val informationUriString = uriString.substringAfter(SLASH_CHAR).substringBeforeLast(SLASH_CHAR)
        val stock = informationUriString.substringAfterLast(SLASH_CHAR).toIntOrZero()
        val productName = informationUriString.substringBeforeLast(SLASH_CHAR)
        return Triple(productId, productName, stock)
    }

}
