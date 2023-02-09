package com.tokopedia.productcard_compact.productcard.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard_compact.test.R
import com.tokopedia.productcard_compact.productcard.presentation.adapter.ProductCardCompactProductCardAdapter
import com.tokopedia.productcard_compact.productcard.presentation.decoration.GridDecoration
import com.tokopedia.productcard_compact.productcard.helper.ProductCardCompactProductCardModelMatcherData.getProductCardModelMatcherData

internal class ProductCardCompactProductCardGridActivityTest: AppCompatActivity() {

    companion object {
        private const val SPAN_COUNT = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_product_card_test)

        val rvProductCard = findViewById<RecyclerView>(R.id.rv_product_card)
        rvProductCard.apply {
            adapter = ProductCardCompactProductCardAdapter(getProductCardModelMatcherData(isCarousel = false))
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            addItemDecoration(GridDecoration())
        }
    }

}
