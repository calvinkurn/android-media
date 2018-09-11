package com.tokopedia.interestpick.view.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.R.id.backgroundView
import com.tokopedia.interestpick.R.id.category
import com.tokopedia.interestpick.view.listener.InterestPickContract
import com.tokopedia.interestpick.view.viewmodel.InterestPickItemViewModel
import kotlinx.android.synthetic.main.item_interest_pick.view.*

/**
 * @author by milhamj on 07/09/18.
 */

class InterestPickAdapter(val listener: InterestPickContract.View)
    : RecyclerView.Adapter<InterestPickAdapter.ViewHolder>() {

    private var list: ArrayList<InterestPickItemViewModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.item_interest_pick, parent, false).let {
            return ViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorId = if (list[position].isSelected)
            R.color.interest_background_active else
            R.color.interest_background_inactive
        holder.itemView.backgroundView.setBackgroundColor(
                MethodChecker.getColor(holder.itemView.context, colorId)
        )
        holder.itemView.category.text = list[position].categoryName
        ImageHandler.LoadImage(holder.itemView.image, list[position].image)

        holder.itemView.backgroundView.setOnClickListener {
            list[holder.adapterPosition].isSelected = !list[holder.adapterPosition].isSelected
            notifyItemChanged(holder.adapterPosition)
        }
    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    fun getList() = list

    fun setList(list: ArrayList<InterestPickItemViewModel>) {
        val diffResult = DiffUtil.calculateDiff(Callback(this.list, list))
        diffResult.dispatchUpdatesTo(this)

        this.list.clear()
        this.list.addAll(list)
    }

    internal class Callback(private val oldList: List<InterestPickItemViewModel>,
                            private val newList: List<InterestPickItemViewModel>)
        : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.isSelected == newItem.isSelected
                    && oldItem.categoryName.equals(newItem.categoryName)
                    && oldItem.image.equals(newItem.image)
        }
    }
}