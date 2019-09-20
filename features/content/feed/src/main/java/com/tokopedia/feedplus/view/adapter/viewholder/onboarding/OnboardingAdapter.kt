package com.tokopedia.feedplus.view.adapter.viewholder.onboarding

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import kotlinx.android.synthetic.main.item_feed_onboarding.view.*

/**
 * @author by yoasfs on 2019-09-18
 */
class OnboardingAdapter(val itemList: List<OnboardingDataViewModel>, listener: InterestPickItemListener): RecyclerView.Adapter<OnboardingAdapter.Holder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_feed_onboarding, p0, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position], position)
    }

    class Holder(v: View, listener: InterestPickItemListener): RecyclerView.ViewHolder(v) {
        fun bind(item: OnboardingDataViewModel, positionInAdapter: Int) {
            initView(item, positionInAdapter)
            initViewListener(item, positionInAdapter)
        }

        private fun initView(item: OnboardingDataViewModel, positionInAdapter: Int) {
            itemView.tv_onboarding_item.text = item.name
            if (!item.isLihatSemuaItem) {
                ImageHandler.LoadImage(itemView.iv_onboarding_item, item.image)
            } else {
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.tkpd_main_green))
                itemView.iv_onboarding_item.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.ic_chevron_right_green_24dp))
            }
            setBackgroundColor(item)
        }

        private fun initViewListener(item: OnboardingDataViewModel, positionInAdapter: Int) {
            itemView.setOnClickListener {
                item.isSelected = !item.isSelected
                setBackgroundColor(item)
            }
        }

        private fun setBackgroundColor(item: OnboardingDataViewModel) {
            if (item.isSelected) {
                itemView.bg_selected.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.tkpd_main_green))
            } else{
                itemView.bg_selected.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.white))
            }
        }
    }

    public interface InterestPickItemListener {
        fun onItemClicked(item: OnboardingDataViewModel)
    }
}