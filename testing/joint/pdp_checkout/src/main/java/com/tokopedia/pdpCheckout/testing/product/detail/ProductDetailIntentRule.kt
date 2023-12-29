package com.tokopedia.pdpCheckout.testing.product.detail

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.product.detail.view.activity.ProductDetailActivity

/**
 * Created by Yehezkiel on 14/09/21
 */
class ProductDetailIntentRule :
    IntentsTestRule<ProductDetailActivity>(
        ProductDetailActivity::class.java,
        false,
        false
    )
