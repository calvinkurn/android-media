package com.tokopedia.explore.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.explore.R
import com.tokopedia.explore.view.listener.ContentExploreContract
import com.tokopedia.explore.view.viewmodel.ExploreCategoryViewModel

import java.util.ArrayList

import javax.inject.Inject

/**
 * @author by milhamj on 20/07/18.
 */

class ExploreCategoryAdapter @Inject
constructor(val listener: ContentExploreContract.View) : RecyclerView.Adapter<ExploreCategoryAdapter.ViewHolder>() {

    companion object {
        const val CAT_ID_AFFILIATE = 2000001
    }
    private var list: List<ExploreCategoryViewModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_explore_tag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.tag.context
        holder.tag.text = list[position].name
        holder.tag.setOnClickListener { v ->
            listener.onCategoryClicked(
                    position,
                    list[position].id,
                    list[position].name,
                    holder.itemView
            )
        }
        if (list[position].isActive) {
            holder.tag.background = MethodChecker.getDrawable(context, R.drawable.explore_tag_selected)
        } else {
            holder.tag.background = MethodChecker.getDrawable(context, R.drawable.explore_tag_neutral)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getList(): List<ExploreCategoryViewModel>? {
        return list
    }

    fun setList(list: List<ExploreCategoryViewModel>) {
        this.list = list
        notifyDataSetChanged()
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: TextView
        init {
            tag = itemView.findViewById(R.id.tag)
        }
    }
}
