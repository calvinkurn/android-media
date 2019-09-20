package com.tokopedia.feedplus.view.adapter.viewholder.onboarding

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import kotlinx.android.synthetic.main.item_feed_onboarding.view.*

/**
 * @author by yoasfs on 2019-09-18
 */
class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.Holder>() {
    private val list: MutableList<OnboardingDataViewModel> = arrayListOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_feed_onboarding, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], position)

//        if (position != MAX_ITEM_SIZE -1) {
//        } else {
//            holder.bind(OnboardingDataViewModel(
//                    0,
//                    OnboardingDataViewModel.defaultLihatSemuaText,
//                    "",
//                    false,
//                    true),
//                    position)
//        }
    }

    fun setList(list: List<OnboardingDataViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class Holder(v: View): RecyclerView.ViewHolder(v) {
        fun bind(item: OnboardingDataViewModel, positionInAdapter: Int) {
            initView(item, positionInAdapter)
            initViewListener(item, positionInAdapter)
        }

        private fun initView(item: OnboardingDataViewModel, positionInAdapter: Int) {
            itemView.tv_onboarding_item.text = item.name
            if (item.isLihatSemuaItem) {
                ImageHandler.LoadImage(itemView.iv_onboarding_item, item.image)
            } else {
                itemView.tv_onboarding_item.setTextColor(itemView.context.resources.getColor(R.color.tkpd_main_green))
                itemView.iv_onboarding_item.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_chevron_right_green_24dp))
            }
        }

        private fun initViewListener(item: OnboardingDataViewModel, positionInAdapter: Int) {

        }
    }
}