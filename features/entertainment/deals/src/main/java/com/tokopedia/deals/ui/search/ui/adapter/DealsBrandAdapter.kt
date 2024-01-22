package com.tokopedia.deals.ui.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.ui.search.listener.DealsSearchListener
import com.tokopedia.deals.ui.search.mapper.DealsSearchMapper
import com.tokopedia.deals.ui.search.ui.adapter.viewholder.BrandViewHolder

class DealsBrandAdapter(private val searchListener: DealsSearchListener) : RecyclerView.Adapter<BrandViewHolder>() {

    var brandList = listOf<Brand>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deals_brand_home, parent, false)
        return BrandViewHolder(view, searchListener)
    }

    override fun getItemCount(): Int {
        return if(brandList.size > DealsSearchMapper.MAX_NUM_BRAND) {
            DealsSearchMapper.MAX_NUM_BRAND
        } else {
            brandList.size
        }
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        if(position < DealsSearchMapper.MAX_NUM_BRAND) {
            val item = brandList[position]
            holder.bindData(item, position)
        }
    }
}
