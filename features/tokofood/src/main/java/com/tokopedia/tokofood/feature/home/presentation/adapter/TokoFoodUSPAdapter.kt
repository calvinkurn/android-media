package com.tokopedia.tokofood.feature.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.databinding.ItemTokofoodUspBottomsheetBinding
import com.tokopedia.tokofood.feature.home.domain.data.USP

class TokoFoodUSPAdapter(
    private val uspList: List<USP>
): RecyclerView.Adapter<TokoFoodUSPAdapter.USPViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): USPViewHolder {
        val view = ItemTokofoodUspBottomsheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return USPViewHolder(view)
    }

    override fun onBindViewHolder(holder: USPViewHolder, position: Int) {
        holder.bind(uspList[position])
    }

    override fun getItemCount(): Int {
        return uspList.size
    }


    inner class USPViewHolder(
        private val binding: ItemTokofoodUspBottomsheetBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(usp: USP) {
            with(binding){
                imgListUsp.loadImage(usp.iconUrl)
                tgListUsp.text = MethodChecker.fromHtml(usp.formatted)
            }
        }
    }
}