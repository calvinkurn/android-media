package com.tokopedia.interest_pick_common.view.adapter

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.interest_pick_common.R
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import kotlinx.android.synthetic.main.item_new_interest_pick.view.*

/**
 * @author by yoasfs on 2019-09-18
 */

private const val VAL_ICON_SIZE = 20

class InterestPickAdapter(private val listener: InterestPickItemListener, val source: String) : RecyclerView.Adapter<InterestPickAdapter.Holder>() {

    companion object {
        const val SOURCE_FEED = "feeds"
        const val SOURCE_ACCOUNTS = "accounts"
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

    private val list: MutableList<InterestPickDataViewModel> = mutableListOf()
    private var selectedListId : List<Int> = arrayListOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(p0.context).inflate(R.layout.item_new_interest_pick, p0, false), listener)
    }

    override fun getItemCount(): Int {
        when (source) {
            SOURCE_FEED -> return 6
        }
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = if(source == SOURCE_FEED && position == 5) {
            getOnboardingDataSeeAll()
        } else list[position]
        holder.bind(data, list)
    }

    fun setList(list: List<InterestPickDataViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun getList() :List<InterestPickDataViewModel> {
        return list
    }

    class Holder(v: View, val listener: InterestPickItemListener) : RecyclerView.ViewHolder(v) {

        fun bind(item: InterestPickDataViewModel, list: List<InterestPickDataViewModel>) {
            initView(item)
            initViewListener(item, list)
        }

        private fun initView(item: InterestPickDataViewModel) {
            itemView.tv_onboarding_item.text = item.name
            setBackgroundColor(item)
            if (!item.isLihatSemuaItem) {
                ImageHandler.LoadImage(itemView.iv_onboarding_item, item.image)
            } else {
                itemView.bg_selected.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.bg_onboarding_see_all))
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
                itemView.iv_onboarding_item.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.ip_com_ic_chevron_right_green_24dp))
                itemView.iv_onboarding_item.maxHeight = convertDpToPixel()
                itemView.iv_onboarding_item.maxWidth = convertDpToPixel()
            }
        }

        private fun initViewListener(item: InterestPickDataViewModel, list: List<InterestPickDataViewModel>) {
            itemView.setOnClickListener {
                if (item.isLihatSemuaItem) {
                    listener.onLihatSemuaItemClicked(list.filter { it.isSelected })
                } else {
                    if (item.isClickable) {
                        item.isSelected = !item.isSelected
                        setBackgroundColor(item)
                        listener.onInterestPickItemClicked(item)
                    }
                }
            }

        }

        private fun setBackgroundColor(item: InterestPickDataViewModel) {
            if (item.isSelected) {
                itemView.bg_selected.background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_interespick_selected)
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.Unify_N0))
            } else {
                itemView.bg_selected.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.Unify_N0))
                itemView.tv_onboarding_item.setTextColor(MethodChecker.getColor(itemView.context, R.color.Unify_N700))
            }
        }

        private fun convertDpToPixel(): Int {
            val metrics = itemView.context.resources.displayMetrics
            return VAL_ICON_SIZE * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        }
    }

    private fun getOnboardingDataSeeAll(): InterestPickDataViewModel {
        return InterestPickDataViewModel(
                0,
                InterestPickDataViewModel.defaultLihatSemuaText,
                "",
                isSelected = false,
                isLihatSemuaItem = true)
    }

    fun getSelectedItems(): List<InterestPickDataViewModel> {
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
        fun onInterestPickItemClicked(item: InterestPickDataViewModel)
        fun onLihatSemuaItemClicked(selectedItemList: List<InterestPickDataViewModel>)
        fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<InterestPickDataViewModel>)
    }
}