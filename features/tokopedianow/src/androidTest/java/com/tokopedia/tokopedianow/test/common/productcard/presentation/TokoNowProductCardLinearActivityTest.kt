package com.tokopedia.tokopedianow.test.common.productcard.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.test.R
import com.tokopedia.tokopedianow.test.common.productcard.adapter.TokoNowProductCardAdapter
import com.tokopedia.tokopedianow.test.common.productcard.decoration.LinearDecoration
import com.tokopedia.tokopedianow.test.common.productcard.utils.TokoNowProductCardModelMatcherData.getProductCardModelMatcherData

internal class TokoNowProductCardLinearActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_product_card_test)

        val rvProductCard = findViewById<RecyclerView>(R.id.rv_product_card)
        rvProductCard.apply {
            adapter = TokoNowProductCardAdapter(getProductCardModelMatcherData(isCarousel = true))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(LinearDecoration())
        }
    }

}
