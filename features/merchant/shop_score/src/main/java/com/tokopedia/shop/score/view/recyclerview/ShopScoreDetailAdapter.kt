package com.tokopedia.shop.score.view.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.view.model.ShopScoreDetailItem
import java.util.*

class ShopScoreDetailAdapter : RecyclerView.Adapter<ShopScoreDetailViewHolder>() {

    private var data: List<ShopScoreDetailItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ShopScoreDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop_score_detail, parent, false)

        return ShopScoreDetailViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopScoreDetailViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size

    fun updateData(viewModel: List<ShopScoreDetailItem>) {
        data = viewModel
        notifyDataSetChanged()
    }
}