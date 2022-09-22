package com.tokopedia.tkpd.flashsale.presentation.list.container

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant.BUNDLE_KEY_PRODUCT
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.multilocation.varian.ManageProductMultiLocationVariantActivity

class FlashSaleListActivity : BaseSimpleActivity() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, FlashSaleListActivity::class.java)
            context.startActivity(intent)
        }

        const val RETURN_OBJECT_OF_MULTI_LOCATION = 2011
    }

    override fun getLayoutRes() = R.layout.stfs_activity_flash_sale_list_container
    override fun getNewFragment() = FlashSaleContainerFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_flash_sale_list_container)
        val intent = ManageProductMultiLocationVariantActivity.start(this, createDummyProduct())
        startActivityForResult(intent, RETURN_OBJECT_OF_MULTI_LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RETURN_OBJECT_OF_MULTI_LOCATION -> onReturnFromActivity(data, resultCode)
        }
    }

    private fun onReturnFromActivity(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != Activity.RESULT_OK) return
        val payload =
            data.extras?.getParcelable<ReservedProduct.Product>(BUNDLE_KEY_PRODUCT)
        payload?.let {
            Toast.makeText(this, "${it.childProducts[0].warehouses[0].stock}", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "${it.childProducts[0].warehouses[0].discountSetup.discount}", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "${it.childProducts[0].warehouses[0].price}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createDummyProduct(): ReservedProduct.Product {
        return ReservedProduct.Product(
            childProducts = createDummyChildsProduct(),
            isMultiWarehouse = false,
            isParentProduct = true,
            name = "Judul Produk Bisa Sepanjang Dua Baris Kebawah",
            picture = "https://placekitten.com/100/100",
            price = ReservedProduct.Product.Price(
                price = 5000,
                lowerPrice = 4000,
                upperPrice = 6000
            ),
            productCriteria = ReservedProduct.Product.ProductCriteria(
                criteriaId = 0,
                maxCustomStock = 100,
                maxDiscount = 70,
                maxFinalPrice = 6000,
                minCustomStock = 11,
                minDiscount = 10,
                minFinalPrice = 2000
            ),
            productId = 0,
            sku = "SK-0918",
            stock = 30,
            url = "",
            warehouses = listOf()
        )
    }

    private fun createDummyChildsProduct(): List<ReservedProduct.Product.ChildProduct> {
        val childsProduct: MutableList<ReservedProduct.Product.ChildProduct> = mutableListOf()
        for (child in 1 until 6) {
            val childProduct = ReservedProduct.Product.ChildProduct(
                disabledReason = "i don't know",
                isDisabled = false,
                isMultiwarehouse = true,
                isToggleOn = false,
                name = "Data | L",
                picture = "",
                price = ReservedProduct.Product.ChildProduct.Price(
                    "4000",
                    "5000",
                    "6000"
                ),
                0,
                0,
                productId = child.toLong(),
                sku = "SKU-$child",
                stock = 80,
                url = "",
                warehouses =  createWarehouseDummy()
            )
            childsProduct.add(childProduct)
        }
        return childsProduct
    }

    private fun createWarehouseDummy() : List<ReservedProduct.Product.Warehouse> {
        val listWarehouse = arrayListOf<ReservedProduct.Product.Warehouse>()
        for (child in 1 until 6) {
            val warehouse = ReservedProduct.Product.Warehouse(
                 warehouseId = child.toLong(),
             name = "Warehouse - ${child}",
             stock = (10*child).toLong(),
             price = 5000,
             discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                 discount = 10,
                 price = 5000,
                 stock = (10*child).toLong()
             ),
             isDilayaniTokopedia = true,
             isToggleOn = false,
             isDisabled = false,
             disabledReason = ""
            )
            listWarehouse.add(warehouse)
        }

        return listWarehouse
    }
}