package com.tokopedia.filter.newdynamicfilter.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Category

class CategoryChildAdapter(private val clickListener: OnItemClickListener) : MultiLevelExpIndListAdapter() {

    var activePosition = 0
    private var lastSelectedCategoryId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        @SuppressLint("InflateParams") val v = LayoutInflater.from(
                parent.context).inflate(R.layout.filter_item_category_child, null
        )
        return ItemRowHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = getItemAt(position) as Category
        val itemRowHolder = holder as ItemRowHolder
        itemRowHolder.bindData(category)
        itemRowHolder.categoryContainer?.setOnClickListener {
            val categoryClicked = getItemAt(position) as Category
            if (categoryClicked.children.isNotEmpty()) {
                activePosition = position
                toggleSelectedChild()
                notifyDataSetChanged()
            } else {
                activePosition = position
                clickListener.onChildClicked(categoryClicked)
            }
        }
    }


    inner class ItemRowHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryContainer: LinearLayout? = view.findViewById<View>(R.id.category_child_container) as LinearLayout
        var categoryName: TextView? = view.findViewById<View>(R.id.category_child_text) as TextView
        private var dropdown: ImageView? = view.findViewById<View>(R.id.category_child_dropdown) as ImageView
        private var colorTextNormal = view.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
        private var colorTextSelected: Int = view.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)

        fun bindData(category: Category) {
            this.categoryName?.text = category.name
            if (category.children.isNotEmpty() && !category.isGroup) {
                categoryName?.setTextColor(colorTextNormal)
                dropdown?.setImageResource(R.drawable.carret_up)
                dropdown?.visibility = View.VISIBLE
            } else if (category.hasChild) {
                categoryName?.setTextColor(colorTextNormal)
                dropdown?.setImageResource(R.drawable.carret_down)
                dropdown?.visibility = View.VISIBLE
            } else {
                dropdown?.visibility = View.GONE
                categoryName?.setTextColor(if (category.id == lastSelectedCategoryId)
                    colorTextSelected
                else
                    colorTextNormal)
            }
            val pad = this.getPaddingPixels(10)
            if (category.indentation > 2) {
                categoryContainer?.setPadding(getPaddingPixels(25), pad, pad, pad)
            } else {
                categoryContainer?.setPadding(pad, pad, pad, pad)
            }
        }

        private fun getPaddingPixels(dpValue: Int): Int {
            val scale = categoryContainer?.context?.resources?.displayMetrics?.density?: 0F
            return (dpValue * scale + 0.5f).toInt()
        }

    }

    private fun toggleSelectedChild() {
        notifyDataSetChanged()
        toggleGroup(activePosition)
    }

    fun toggleSelectedChildbyId(categoryId: String) {
        for (i in 0 until itemCount) {
            val childLevel2 = getItemAt(i) as Category
            if (childLevel2.id == categoryId) {
                activePosition = i
                if (childLevel2.children.isNotEmpty()) {
                    toggleSelectedChild()
                    return
                } else {
                    break // search for the parent
                }
            }
        }
        for (i in 0 until itemCount) {
            val childLevel2 = getItemAt(i) as Category
            if (childLevel2.children.isNotEmpty()) {
                for (childLevel3 in childLevel2.children as List<Category>) {
                    if (childLevel3.id == categoryId) {
                        activePosition = i
                        toggleSelectedChild()
                        break
                    }
                }
            }
        }
    }

    fun setLastSelectedCategoryId(lastSelectedCategoryId: String) {
        this.lastSelectedCategoryId = lastSelectedCategoryId
    }

    interface OnItemClickListener {
        fun onChildClicked(category: Category)
    }

}