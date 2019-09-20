package com.tokopedia.feedplus.view.adapter.viewholder.onboarding

import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import kotlinx.android.synthetic.main.item_feed_onboarding.view.*

/**
 * @author by yoasfs on 2019-09-18
 */
class OnboardingAdapter(val itemList: List<OnboardingDataViewModel>, val listener: InterestPickItemListener): RecyclerView.Adapter<OnboardingAdapter.Holder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_feed_onboarding, p0, false), listener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position], position)
    }

    class Holder(v: View, val listener: InterestPickItemListener): RecyclerView.ViewHolder(v) {

        val VAL_ICON_SIZE = 30

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
                itemView.iv_onboarding_item.maxHeight = convertDpToPixel(VAL_ICON_SIZE)
                itemView.iv_onboarding_item.maxWidth = convertDpToPixel(VAL_ICON_SIZE)
            }
            setBackgroundColor(item)
        }

        private fun initViewListener(item: OnboardingDataViewModel, positionInAdapter: Int) {
            itemView.setOnClickListener {
                if (item.isLihatSemuaItem) {
//                    listener.onLihatSemuaItemClicked()
                } else {
                    item.isSelected = !item.isSelected
                    setBackgroundColor(item)
                    listener.onInterestPickItemClicked(item)
                }
            }
        }

        private fun setBackgroundColor(item: OnboardingDataViewModel) {
            if (item.isSelected) {
                itemView.bg_selected.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.tkpd_main_green))
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.white))
            } else{
                itemView.bg_selected.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.white))
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.Neutral_N700))
            }
        }
        private fun convertDpToPixel(dp: Int): Int {
            val metrics = itemView.context.resources.displayMetrics
            return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        }
    }

    fun getSelectedItems() : List<OnboardingDataViewModel> {
        return itemList.filter {it.isSelected}
    }

    public interface InterestPickItemListener {
        fun onInterestPickItemClicked(item: OnboardingDataViewModel)
        fun onLihatSemuaItemClicked(selectedItemList: List<OnboardingDataViewModel>)
        fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<OnboardingDataViewModel>)
    }
}