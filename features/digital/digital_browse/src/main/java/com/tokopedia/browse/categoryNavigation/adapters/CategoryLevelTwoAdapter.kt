package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import kotlinx.android.synthetic.main.item_category_level_two_type_two.view.*
import kotlinx.android.synthetic.main.item_exclusive_level_two.view.*
import kotlinx.android.synthetic.main.item_level_two_child.view.product_image
import kotlinx.android.synthetic.main.item_level_two_child.view.product_name

class CategoryLevelTwoAdapter(private val list: MutableList<ChildItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val TYPE_ONE = 1
    private val TYPE_TWO = 2

    private var expanded_item_pos = -1

    private var childList: MutableList<ChildItem>? = ArrayList<ChildItem>()

    val viewMap1 = HashMap<Int, Boolean>()
    val viewMap2 = HashMap<Int, Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ONE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exclusive_level_two, parent, false)
            ViewHolder1(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_level_two_type_two, parent, false)
            ViewHolder2(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].type == 1) {
            TYPE_ONE
        } else {
            TYPE_TWO
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_ONE) {
            initLayoutOne(holder as ViewHolder1, position)
        } else {
            initLayoutTwo(holder as ViewHolder2, position)
        }
    }

    private fun initLayoutTwo(holder: ViewHolder2, position: Int) {
        val item = list[position]

        ImageHandler.loadImage(holder.itemView.context, holder.item_image, item.iconImageUrl, R.drawable.loading_page)
        holder.item_name.text = item.name

        if (holder.item_child_recycler.adapter == null) {
            val gridLayoutManager = GridLayoutManager(holder.itemView.context, 3, LinearLayoutManager.VERTICAL, false)

            holder.item_child_recycler.visibility = View.VISIBLE
            holder.item_child_recycler_divider.visibility = View.VISIBLE

            holder.item_child_recycler.apply {
                layoutManager = gridLayoutManager
                adapter = LevelTwoChildAdapter(childList)
            }
        }

        if (item.child == null && !(item.child!!.size > 0)) {
            holder.carrot.visibility = View.GONE
            holder.item_child_recycler.visibility = View.GONE
            holder.item_child_recycler_divider.visibility = View.GONE

        } else if (item.isExpanded) {
            childList?.clear()
            childList?.addAll(item.child)
            holder.item_child_recycler.visibility = View.VISIBLE
            holder.item_child_recycler_divider.visibility = View.VISIBLE

            holder.carrot.setImageResource(R.drawable.carrot_up)
            holder.item_child_recycler.adapter.notifyDataSetChanged()

        } else {
            holder.carrot.setImageResource(R.drawable.carrot_down)
            holder.item_child_recycler.visibility = View.GONE
            holder.item_child_recycler_divider.visibility = View.GONE

        }



        holder.parent_layout.setOnClickListener {

            CategoryAnalytics.createInstance().eventDropDownPromoClick(holder.itemView.context, list[position], position)

            if (expanded_item_pos < 0 || expanded_item_pos == position) {
                item.isExpanded = !item.isExpanded
                notifyItemChanged(position)
                expanded_item_pos = position
            } else {

                list[expanded_item_pos].isExpanded = false
                list[position].isExpanded = true
                notifyItemChanged(expanded_item_pos)
                notifyItemChanged(position)
                expanded_item_pos = position

            }


        }
    }


    private fun initLayoutOne(holder: ViewHolder1, position: Int) {
        val item = list[position]

        ImageHandler.loadImage(holder.itemView.context, holder.item_image_v1, item.iconImageUrl, R.drawable.loading_page)
        holder.item_name_v1.text = item.name

        holder.item_image_v1.setOnClickListener {
            fireApplink(holder.item_image_v1.context, list[position].applinks)
            CategoryAnalytics.createInstance().eventPromoClick(holder.itemView.context, list[position], position)
        }

        holder.item_name_v1.setOnClickListener {
            fireApplink(holder.item_name_v1.context, list[position].applinks)
            CategoryAnalytics.createInstance().eventPromoClick(holder.itemView.context, list[position], position)
        }

        holder.product_parent_name.text = item.parentName

    }

    private fun fireApplink(context: Context?, applinks: String?) {
        RouteManager.route(context, applinks)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is ViewHolder1) {
            val position = holder.adapterPosition
            if (!viewMap1.containsKey(position)) {
                viewMap1[position] = true
                CategoryAnalytics.createInstance().eventPromoView(holder.itemView.context, list[position], position)
            }

        } else if (holder is ViewHolder2) {
            val position = holder.adapterPosition
            if (!viewMap2.containsKey(position)) {
                viewMap2[position] = true
                CategoryAnalytics.createInstance().eventDropDownPromoView(holder.itemView.context, list[position], position)
            }
        }
    }


    class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        val item_image = view.item_icon
        val item_name = view.item_name
        val item_child_recycler = view.child_recycler
        val item_child_recycler_divider = view.child_divider
        val parent_layout = view.parent_layout
        val carrot = view.carrot

    }

    class ViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        val item_image_v1 = view.product_image
        val item_name_v1 = view.product_name
        val product_parent_name = view.product_parent_name

    }

}
