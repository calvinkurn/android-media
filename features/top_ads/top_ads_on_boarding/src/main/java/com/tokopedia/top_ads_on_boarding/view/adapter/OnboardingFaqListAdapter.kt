package com.tokopedia.top_ads_on_boarding.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.top_ads_on_boarding.R

class OnboardingFaqListAdapter :
    ListAdapter<com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel, RecyclerView.ViewHolder>(
        FaqListDiffUtilCallBack()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_onboarding_faq_item_layout, parent, false)
        return com.tokopedia.top_ads_on_boarding.view.adapter.viewholders.OnboardingFaqItemViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? com.tokopedia.top_ads_on_boarding.view.adapter.viewholders.OnboardingFaqItemViewHolder)?.bind(
            getItem(position),
            ::onAccordianItemClicked
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onAccordianItemClicked(id : Int){
        currentList.map { it.isExpanded = it.id == id && !it.isExpanded }
        notifyDataSetChanged()
    }
}

class FaqListDiffUtilCallBack :
    DiffUtil.ItemCallback<com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel>() {
    override fun areItemsTheSame(
        oldItem: com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel,
        newItem: com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel,
        newItem: com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel
    ): Boolean {
        return oldItem.title == newItem.title
    }
}
