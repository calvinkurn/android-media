package com.tokopedia.product.manage.feature.stockreminder.view.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ActivityStockReminderBinding
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment

class StockReminderActivity : BaseSimpleActivity() {

    companion object {
        private const val SLASH_CHAR = "/"
    }

    private var binding: ActivityStockReminderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLayout(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        var productId = 0L
        var isVariant = false
        val uri = intent.data
        if (uri != null) {
            val (infoProductId, infoProductName, infoIsVariant) = uri.getProductInformation()
            productId = infoProductId
            isVariant = infoIsVariant
        }
        return StockReminderFragment.createInstance(productId, isVariant)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        binding = ActivityStockReminderBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupView()
    }

    override fun getScreenName(): String = StockReminderConst.SCREEN_STOCK_REMINDER

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun onBackPressed() {
        fragment?.let {
            if (it is StockReminderFragment) {
                it.onBackPressed()
            }
        }
    }

    private fun setupView() {
        binding?.header?.run {
            setNavigationOnClickListener {
                onBackPressed()
            }
            headerTitle = getString(R.string.product_stock_reminder_header_title)
        }
    }

    /**
     * Parse uri to get relevant product information
     * This method is used to suffice requirement for product which name contains slash ("/")
     *
     * @return  Triple of productId, productName, and stock
     */
    private fun Uri.getProductInformation(): Triple<Long, String, Boolean> {
        val uriString =
            this.toString().replace(ApplinkConstInternalMarketplace.STOCK_REMINDER_BASE, "")
        val productId = uriString.substringBefore(SLASH_CHAR).toLongOrZero()
        val informationUriString =
            uriString.substringAfter(SLASH_CHAR).substringBeforeLast(SLASH_CHAR)
        val isVariant = informationUriString.substringAfterLast(SLASH_CHAR).toBoolean()
        val productName = informationUriString.substringBeforeLast(SLASH_CHAR)
        return Triple(productId, productName, isVariant)
    }

}
