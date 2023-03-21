package com.tokopedia.tokopedianow.test.common.productcard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.test.R
import com.tokopedia.tokopedianow.test.common.productcard.model.TokoNowProductCardMatcherModel

internal class TokoNowProductCardAdapter(
    private val productCardModelMatchers: List<TokoNowProductCardMatcherModel>
): RecyclerView.Adapter<TokoNowProductCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoNowProductCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tokopedianow_product_card_test, null)
        return TokoNowProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productCardModelMatchers.size
    }

    override fun onBindViewHolder(holder: TokoNowProductCardViewHolder, position: Int) {
        holder.bind(productCardModelMatchers[position].model)
    }
}
