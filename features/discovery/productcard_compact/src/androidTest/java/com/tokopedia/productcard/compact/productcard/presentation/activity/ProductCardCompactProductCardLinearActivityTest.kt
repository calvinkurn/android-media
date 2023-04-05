package com.tokopedia.productcard.compact.productcard.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.compact.test.R
import com.tokopedia.productcard.compact.productcard.presentation.adapter.ProductCardCompactProductCardAdapter
import com.tokopedia.productcard.compact.productcard.presentation.decoration.ProductCardCompactProductCardLinearDecoration
import com.tokopedia.productcard.compact.productcard.helper.ProductCardCompactProductCardModelMatcherData.getProductCardModelMatcherData

internal class ProductCardCompactProductCardLinearActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_product_card_test)

        val rvProductCard = findViewById<RecyclerView>(R.id.rv_product_card)
        rvProductCard.apply {
            adapter = ProductCardCompactProductCardAdapter(getProductCardModelMatcherData(isCarousel = true))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ProductCardCompactProductCardLinearDecoration())
        }
    }

}
