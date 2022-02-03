package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeRecommendationBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.data.PLACEHOLDER_RES_UNIFY
import com.tokopedia.media.loader.loadImage

class RechargeItemRecommendationBannerAdapter(
    val items: List<RechargeHomepageSections.Item>,
    val listener: RechargeHomepageItemListener
) : RecyclerView.Adapter<RechargeItemRecommendationBannerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutDigitalHomeRecommendationBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        val binding: LayoutDigitalHomeRecommendationBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: RechargeHomepageSections.Item) {
            with(binding.ivRechargeRecommendationBanner) {
                if (element.mediaUrl.isNotEmpty()) {
                    loadImage(element.mediaUrl)
                    show()
                } else {
                    setImageResource(PLACEHOLDER_RES_UNIFY)
                    show()
                }
            }
        }
    }
}