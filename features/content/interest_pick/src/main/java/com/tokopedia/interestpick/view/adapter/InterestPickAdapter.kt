package com.tokopedia.interestpick.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.interestpick.R
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

        holder.itemView.backgroundView.isClickable = list[position].selectable
        if (list[position].selectable) {
            holder.itemView.backgroundView.setOnClickListener {
                list[holder.adapterPosition].isSelected = !list[holder.adapterPosition].isSelected
                notifyItemChanged(holder.adapterPosition)
                listener.onItemSelected(list[holder.adapterPosition].isSelected)
            }
        } else {
            holder.itemView.backgroundView.setOnClickListener(null)
        }
    }

    override fun getItemCount() = list.size

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        ImageHandler.clearImage(holder.itemView.image)
    }

    fun setItemUnClickable(item: InterestPickItemViewModel) {
        val index = list.indexOf(item)
        item.selectable = false
        notifyItemChanged(index)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun getList() = list

    fun setList(list: ArrayList<InterestPickItemViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}