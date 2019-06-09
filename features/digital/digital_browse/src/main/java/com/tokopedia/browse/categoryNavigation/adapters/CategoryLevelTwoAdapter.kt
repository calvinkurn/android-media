package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import kotlinx.android.synthetic.main.item_category_level_two_type_two.view.*
import kotlinx.android.synthetic.main.item_level_two_child.view.*

class CategoryLevelTwoAdapter(private val list: MutableList<ChildItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    private val TYPE_ONE = 1
    private val TYPE_TWO = 2

    private var expanded_item_id = null
    private var expanded_item_pos = -1


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
        Glide.with(holder.itemView.context)
                .load(item.iconImageUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .into(holder.item_image)
        holder.item_name.text = item.name


        if (item.child == null && item.child!!.size == 0) {
            holder.carrot.visibility = View.GONE
            holder.item_child_recycler.visibility = View.GONE

        } else if (item.isExpanded) {
            holder.item_child_recycler.visibility = View.VISIBLE
            holder.carrot.setImageResource(R.drawable.carrot_down)

            if (holder.item_child_recycler.adapter == null) {
                val gridLayoutManager = GridLayoutManager(holder.itemView.context, 3, LinearLayoutManager.VERTICAL, false)

                holder.item_child_recycler.visibility = View.VISIBLE
                holder.item_child_recycler.apply {
                    layoutManager = gridLayoutManager
                    adapter = LevelTwoChildAdapter(item.child)
                    recycledViewPool = viewPool
                }
            }

        } else {
            holder.carrot.setImageResource(R.drawable.carrot_up)
            holder.item_child_recycler.visibility = View.GONE
        }

        holder.parent_layout.setOnClickListener {
            item.isExpanded = !item.isExpanded
            notifyItemChanged(position)
        }
    }


    private fun initLayoutOne(holder: ViewHolder1, position: Int) {
        val item = list[position]
        Glide.with(holder.itemView.context)
                .load(item.iconImageUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .into(holder.item_image_p)
        holder.item_name_p.text = item.name

        holder.item_image_p.setOnClickListener {
            fireApplink(holder.item_image_p.context, list[position].applinks)
        }

        holder.item_name_p.setOnClickListener {
            fireApplink(holder.item_name_p.context, list[position].applinks)
        }

    }

    private fun fireApplink(context: Context?, applinks: String?) {
        RouteManager.route(context, applinks)
    }


    class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        val item_image = view.item_icon
        val item_name = view.item_name
        val item_child_recycler = view.child_recycler
        val parent_layout = view.parent_layout
        val carrot = view.carrot

    }

    class ViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        val item_image_p = view.product_image
        val item_name_p = view.product_name

    }

}
