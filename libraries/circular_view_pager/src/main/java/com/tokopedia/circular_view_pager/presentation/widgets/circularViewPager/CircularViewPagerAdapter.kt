package com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager

import android.annotation.SuppressLint
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * A Pager Adapter that supports infinite loop.
 * This is achieved by adding a fake item at both beginning and the last,
 * And then silently changing to the same, real item, thus looks like infinite.
 */
@Suppress("unused")
@SuppressLint("SyntheticAccessor")
abstract class CircularViewPagerAdapter(itemList: List<CircularModel>, private val circularListener: CircularListener) : RecyclerView.Adapter<CircularViewHolder>() {
    private var itemList: List<CircularModel> = listOf()
    open var viewCache = SparseArray<View>()
    var isInfinite = false
        protected set
    open var canInfinite = true


    fun setIsInfinite(isInfinite: Boolean){
        this.isInfinite = isInfinite
    }

    fun setItemList(newItemList: List<CircularModel>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    fun getItem(listPosition: Int): CircularModel? {
        return if (listPosition >= 0 && listPosition < itemList.size) {
            itemList[listPosition]
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: CircularViewHolder, position: Int) {
        val listPosition = if (isInfinite && canInfinite) getListPosition(position) else position
        if(listPosition != -1) {
            holder.bind(itemList[listPosition], object : CircularListener {
                override fun onClick(position: Int) {
                    circularListener.onClick(listPosition)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        val count: Int = itemList.size
        return if (isInfinite && canInfinite) {
            count + 2
        } else {
            count
        }
    }

    val listCount: Int
        get() = itemList.size

    private fun getListPosition(position: Int): Int {
        if (!(isInfinite && canInfinite)) return position
        return when {
            position == 0 -> {
                itemCount - 1 - 2 //First item is a dummy of last item
            }
            position > itemCount - 2 -> {
                0 //Last item is a dummy of first item
            }
            else -> {
                position - 1
            }
        }
    }

    val lastItemPosition: Int
        get() = if (isInfinite) {
            itemList.size
        } else {
            itemList.size - 1
        }

    init {
        this.isInfinite = isInfinite
        setItemList(itemList)
    }

}
data class CircularModel(val id: Int, val url: String)
