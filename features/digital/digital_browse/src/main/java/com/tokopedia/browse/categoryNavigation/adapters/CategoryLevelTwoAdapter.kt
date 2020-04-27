package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_category_level_two_type_two.view.*
import kotlinx.android.synthetic.main.item_exclusive_level_two.view.*
import kotlinx.android.synthetic.main.item_level_two_child.view.product_image
import kotlinx.android.synthetic.main.item_level_two_child.view.product_name

class CategoryLevelTwoAdapter(private val list: MutableList<ChildItem>,
                              private val trackingQueue: TrackingQueue?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val TYPE_ONE = 1
    private val TYPE_TWO = 2

    private var expanded_item_pos = -1

    private var childList: MutableList<ChildItem>? = ArrayList()

    val viewMap1 = HashMap<Int, Boolean>()
    val viewMap2 = HashMap<Int, Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ONE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exclusive_level_two, parent, false)
            ExclusiveViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_level_two_type_two, parent, false)
            DefaultViewHolder(view)
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
            initLayoutOne(holder as ExclusiveViewHolder, position)
        } else {
            initLayoutTwo(holder as DefaultViewHolder, position)
        }
    }

    private fun initLayoutTwo(holder: DefaultViewHolder, position: Int) {
        val item = list[position]

        //default view holder impression
        holder.itemView.addOnImpressionListener(
                item, object : ViewHintListener {
            override fun onViewHint() {
                if (!viewMap2.containsKey(position)) {
                    viewMap2[position] = true
                    trackingQueue?.let {
                        CategoryAnalytics.createInstance().eventDropDownPromoView(it, list[position], position)
                    }
                }
            }
        })

        ImageHandler.loadImage(holder.itemView.context, holder.itemImage, item.iconImageUrl, R.drawable.loading_page)
        holder.itemName.text = item.name

        if (holder.itemChildRecycler.adapter == null) {
            val gridLayoutManager = GridLayoutManager(holder.itemView.context, 3, LinearLayoutManager.VERTICAL, false)

            holder.itemChildRecycler.show()
            holder.itemChildRecyclerDivider.show()

            holder.itemChildRecycler.apply {
                layoutManager = gridLayoutManager
                adapter = LevelTwoChildAdapter(childList, trackingQueue)
            }
        }

        if (item.child == null && !(item.child!!.size > 0)) {
            holder.carrot.hide()
            holder.itemChildRecycler.hide()
            holder.itemChildRecyclerDivider.hide()

        } else if (item.isExpanded) {
            childList?.clear()
            childList?.addAll(item.child)
            holder.itemChildRecycler.show()
            holder.itemChildRecyclerDivider.show()

            holder.carrot.setImageResource(R.drawable.carrot_up)
            holder.itemChildRecycler.adapter?.notifyDataSetChanged()

        } else {
            holder.carrot.setImageResource(R.drawable.carrot_down)
            holder.itemChildRecycler.hide()
            holder.itemChildRecyclerDivider.hide()

        }



        holder.parentLayout.setOnClickListener {

            CategoryAnalytics.createInstance().eventDropDownPromoClick(list[position], position)

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


    private fun initLayoutOne(holder: ExclusiveViewHolder, position: Int) {
        val item = list[position]

        //exclusive viewholder impression
        holder.itemView.addOnImpressionListener(
                item, object : ViewHintListener {
            override fun onViewHint() {
                if (!viewMap1.containsKey(position)) {
                    viewMap1[position] = true
                    trackingQueue?.let {
                        CategoryAnalytics.createInstance().eventPromoView(it, list[position], position)
                    }
                }
            }
        })

        ImageHandler.loadImage(holder.itemView.context, holder.itemImageV1, item.iconImageUrl, R.drawable.loading_page)
        holder.itemNameV1.text = item.name

        holder.itemImageV1.setOnClickListener {
            fireApplink(holder.itemImageV1.context, list[position].applinks)
            CategoryAnalytics.createInstance().eventPromoClick(list[position], position)
        }

        holder.itemNameV1.setOnClickListener {
            fireApplink(holder.itemNameV1.context, list[position].applinks)
            CategoryAnalytics.createInstance().eventPromoClick(list[position], position)
        }

        holder.productParentName.text = item.parentName

    }

    private fun fireApplink(context: Context?, applinks: String?) {
        RouteManager.route(context, applinks)
    }

    class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.item_icon
        val itemName: TextView = view.item_name
        val itemChildRecycler: RecyclerView = view.child_recycler
        val itemChildRecyclerDivider: View = view.child_divider
        val parentLayout: ConstraintLayout = view.parent_layout
        val carrot: ImageView = view.carrot

    }

    class ExclusiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImageV1: ImageView = view.product_image
        val itemNameV1: TextView = view.product_name
        val productParentName: TextView = view.product_parent_name

    }

}
