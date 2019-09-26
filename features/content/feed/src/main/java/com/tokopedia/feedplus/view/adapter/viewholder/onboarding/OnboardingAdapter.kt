package com.tokopedia.feedplus.view.adapter.viewholder.onboarding

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
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
class OnboardingAdapter(private val listener: InterestPickItemListener, val source: String) : RecyclerView.Adapter<OnboardingAdapter.Holder>() {

    companion object {
        val SOURCE_FEED = "feeds"
        fun getItemDecoration(): RecyclerView.ItemDecoration {
            return object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                            state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val position = parent.getChildAdapterPosition(view)

                    if (position < 0 && position >= state.itemCount) {
                        return
                    }

                    val resources = view.resources
                    val layoutParams = view.layoutParams as? GridLayoutManager.LayoutParams
                    val layoutManager = parent.layoutManager  as? GridLayoutManager
                    val spanIndex = layoutParams?.spanIndex
                    val spanCount = layoutManager?.spanCount
                    if (spanIndex == spanCount) {
                        outRect.right = resources.getDimension(R.dimen.dp_0).toInt()
                    }
                }
            }
        }
    }

    private val list: MutableList<OnboardingDataViewModel> = arrayListOf()
    private var selectedListId : List<Int> = arrayListOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_feed_onboarding, p0, false), listener)
    }

    override fun getItemCount(): Int {
        when (source) {
            SOURCE_FEED -> return 6
        }
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var data = list[position]
        if (source.equals(SOURCE_FEED) && position == 5)  {
            data = getOnboardingDataSeeAll()
        }
        holder.bind(data, position, list)
    }

    fun setList(list: List<OnboardingDataViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class Holder(v: View, val listener: InterestPickItemListener) : RecyclerView.ViewHolder(v) {

        private val VAL_ICON_SIZE = 20

        fun bind(item: OnboardingDataViewModel, positionInAdapter: Int, list: List<OnboardingDataViewModel>) {
            initView(item, positionInAdapter)
            initViewListener(item, positionInAdapter, list)
        }

        private fun initView(item: OnboardingDataViewModel, positionInAdapter: Int) {
            itemView.tv_onboarding_item.text = item.name
            if (!item.isLihatSemuaItem) {
                ImageHandler.LoadImage(itemView.iv_onboarding_item, item.image)
            } else {
                itemView.bg_selected.visibility = View.GONE
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.tkpd_main_green))
                itemView.iv_onboarding_item.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.ic_chevron_right_green_24dp))
                itemView.iv_onboarding_item.maxHeight = convertDpToPixel(VAL_ICON_SIZE)
                itemView.iv_onboarding_item.maxWidth = convertDpToPixel(VAL_ICON_SIZE)
            }
            setBackgroundColor(item)
        }

        private fun initViewListener(item: OnboardingDataViewModel, positionInAdapter: Int, list: List<OnboardingDataViewModel>) {
            itemView.setOnClickListener {
                if (item.isLihatSemuaItem) {
                    listener.onLihatSemuaItemClicked(list.filter { it.isSelected })
                } else {
                    item.isSelected = !item.isSelected
                    setBackgroundColor(item)
                    listener.onInterestPickItemClicked(item)
                }
            }
        }

        private fun setBackgroundColor(item: OnboardingDataViewModel) {
            if (item.isSelected) {
                itemView.bg_selected.background = itemView.context.resources.getDrawable(R.drawable.bg_interespick_selected)
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.white))
            } else {
                itemView.bg_selected.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.white))
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.Neutral_N700))
            }
        }

        private fun convertDpToPixel(dp: Int): Int {
            val metrics = itemView.context.resources.displayMetrics
            return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        }
    }

    private fun getOnboardingDataSeeAll(): OnboardingDataViewModel {
        return OnboardingDataViewModel(
                0,
                OnboardingDataViewModel.defaultLihatSemuaText,
                "",
                false,
                true)
    }

    fun getSelectedItems(): List<OnboardingDataViewModel> {
        return list.filter { it.isSelected }
    }

    fun getSelectedItemIdList(): List<Int> {
        selectedListId = getSelectedItems().map{ it.id }
        return selectedListId
    }

    fun setSelectedItemIds(selectedIds: List<Int>) {
        selectedListId = selectedIds
        for (item in list) {
            item.isSelected = selectedIds.contains(item.id)
        }
        notifyDataSetChanged()
    }

    interface InterestPickItemListener {
        fun onInterestPickItemClicked(item: OnboardingDataViewModel)
        fun onLihatSemuaItemClicked(selectedItemList: List<OnboardingDataViewModel>)
        fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<OnboardingDataViewModel>)
    }
}