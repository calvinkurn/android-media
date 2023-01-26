package com.tokopedia.tokopedianow.test.common.productcard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.test.R
import com.tokopedia.tokopedianow.test.common.productcard.adapter.TokoNowProductCardAdapter
import com.tokopedia.tokopedianow.test.common.productcard.utils.TokoNowProductCardItemLinearDecoration
import com.tokopedia.tokopedianow.test.common.productcard.utils.TokoNowProductCardModelMatcherData.getProductCardModelMatcherData

internal class TokoNowProductCardActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_product_card_test)

        val rvNormalProductCard = findViewById<RecyclerView>(R.id.rv_product_card)
        rvNormalProductCard.adapter = TokoNowProductCardAdapter(getProductCardModelMatcherData())
        rvNormalProductCard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvNormalProductCard.addItemDecoration(TokoNowProductCardItemLinearDecoration())
    }

}
