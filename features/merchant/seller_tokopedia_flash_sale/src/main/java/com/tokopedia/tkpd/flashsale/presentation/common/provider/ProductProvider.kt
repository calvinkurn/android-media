package com.tokopedia.tkpd.flashsale.presentation.common.provider

import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ChildProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup

object ProductProvider {

    fun generateProduct(): Product = Product(
        listOf(
            ChildProduct(
                "",
                false,
                false,
                true,
                "Product",
                "https://images.tokopedia.net/img/android/campaign/fs-tkpd/seller_toped.png",
                ChildProduct.Price("Rp 10.000", "Rp. 10.000", "Rp 10.000"),
                2148022738,
                "",
                1000,
                "https://www.tokopedia.com/trimegatech/mini-pc-z85-z83-4gb-64gb-windows-10-intel-x5-quadcore-z8350",
                listOf(
                    Warehouse(
                        341941,
                        "WH-101",
                        1000,
                        10000,
                        DiscountSetup(30, 21000, 40),
                        true,
                        true,
                        false,
                        ""
                    )
                )
            )
        ),
        false,
        true,
        "Product",
        "https://images.tokopedia.net/img/android/campaign/fs-tkpd/seller_toped.png",
        Product.Price(10000, 10000, 10000),
        Product.ProductCriteria(645, 100, 20, 5000, 10, 10, 1000),
        2148022738,
        "",
        1000,
        "https://www.tokopedia.com/trimegatech/mini-pc-z85-z83-4gb-64gb-windows-10-intel-x5-quadcore-z8350",
        listOf(
            Warehouse(
                341941,
                "WH-101",
                1000,
                10000,
                DiscountSetup(30, 21000, 40),
                true,
                true,
                false,
                ""
            )
        )

    )

}