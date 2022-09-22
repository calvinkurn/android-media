package com.tokopedia.tkpd.flashsale.presentation.list.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantActivity

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
        ManageProductNonVariantActivity.start(this, ReservedProduct.Product(
            childProducts = emptyList(),
            isMultiWarehouse = true,
            isParentProduct = true,
            name = "Alat Pemuas Batin",
            picture = "https://placekitten.com/100/100",
            price = ReservedProduct.Product.Price(1000, 1000, 1000),
            productCriteria = ReservedProduct.Product.ProductCriteria(
                criteriaId = 11,
                minCustomStock = 10,
                maxCustomStock = 100,
                minDiscount = 10,
                maxDiscount = 50,
                minFinalPrice = 100,
                maxFinalPrice = 10000,
            ),
            productId = 111,
            sku = "222",
            stock = 11,
            url = "222",
            warehouses = listOf(
                ReservedProduct.Product.Warehouse(
                    warehouseId = 123,
                    name = "JKT",
                    stock = 1,
                    price = 1000,
                    discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                        discount = 0,
                        price = 0,
                        stock = 0,
                    ),
                    isDilayaniTokopedia = false,
                    isToggleOn = true,
                    isDisabled = false,
                    disabledReason = "",
                ),
                ReservedProduct.Product.Warehouse(
                    warehouseId = 122,
                    name = "JKTSEL",
                    stock = 10,
                    price = 2000,
                    discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                        discount = 0,
                        price = 0,
                        stock = 0,
                    ),
                    isDilayaniTokopedia = false,
                    isToggleOn = true,
                    isDisabled = false,
                    disabledReason = "",
                )
            ),
        ))
    }
}