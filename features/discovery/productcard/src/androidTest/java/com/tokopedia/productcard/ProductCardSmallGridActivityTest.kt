package com.tokopedia.productcard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.productcard.test.R.layout

class ProductCardSmallGridActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.product_card_small_grid_activity_test_layout)
    }
}