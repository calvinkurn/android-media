package com.tokopedia.tokopoints.view.pointhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.PointHistoryBase
import com.tokopedia.tokopoints.view.model.PointHistoryItem
import kotlinx.android.synthetic.main.tp_item_point_history.view.*

import javax.inject.Inject

class PointHistoryListAdapter @Inject constructor(private val callback: PointHistoryViewModel) : BaseAdapter<PointHistoryItem>(callback) {

    inner class ViewHolder(view: View) : BaseVH(view) {

        override fun bindView(item: PointHistoryItem, position: Int) {
            setData(this, item, position)
        }
    }

    private fun setData(holder: ViewHolder, item: PointHistoryItem, position: Int) {
            holder.itemView.apply {
                ImageHandler.loadImageFitCenter(img_icon.context, img_icon, item.getIcon())
                text_title.text = item.getTitle()
                text_date.text = item.getCreateTimeDesc()
                text_txn_id.text = String.format("#%d", item.getId())

                text_note.text = if (item.getNotes().isEmpty()) "" else item.getNotes()

                if (item.getRewardPoints() != 0) {
                    text_point.text = if (item.getRewardPoints() > 0)
                        String.format(" +%d", item.getRewardPoints())
                    else
                        String.format(" %d", item.getRewardPoints())
                } else {
                    text_point.text = " -"
                }

                if (item.getMemberPoints() != 0) {
                    text_loyalty.text = if (item.getMemberPoints() > 0)
                        String.format(" +%d", item.getMemberPoints())
                    else
                        String.format(" %d", item.getMemberPoints())
                } else {
                    text_loyalty.text = " -"
                }
            }

    }

    override fun getItemViewHolder(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.tp_item_point_history, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(currentPageIndex: Int) {
        super.loadData(currentPageIndex)
        callback.loadData(currentPageIndex)
    }

    fun showData(data: PointHistoryBase) {
        loadCompleted(data.pointHistory.items, data)
        isLastPage = !data.pointHistory.pageInfo.isHasNext
    }
}
