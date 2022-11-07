package com.tokopedia.tkpd.flashsale.presentation.list.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantActivity
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.ManageProductMultiLocationVariantActivity

class FlashSaleListActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, FlashSaleListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutRes() = R.layout.stfs_activity_flash_sale_list_container
    override fun getNewFragment() = FlashSaleContainerFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_flash_sale_list_container)
    }
}
