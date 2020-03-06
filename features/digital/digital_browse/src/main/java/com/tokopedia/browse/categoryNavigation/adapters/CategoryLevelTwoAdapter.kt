package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryChildItem
import com.tokopedia.browse.categoryNavigation.utils.Constants
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_category_product_header_view.view.*
import kotlinx.android.synthetic.main.item_category_product_header_view.view.product_name
import kotlinx.android.synthetic.main.item_category_product_view.view.*
import kotlinx.android.synthetic.main.item_category_text_header.view.*
import kotlinx.android.synthetic.main.item_category_yang_lagi_hit.view.*

class CategoryLevelTwoAdapter(private val list: MutableList<CategoryChildItem>,
                              private val trackingQueue: TrackingQueue?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            Constants.TextHeaderView -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_text_header, parent, false)
                TextHeaderViewHolder(view)
            }
            Constants.YangLagiHitsView -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_yang_lagi_hit, parent, false)
                YangLagiHitsViewHolder(view)
            }
            Constants.ProductHeaderView -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_product_header_view, parent, false)
                ProductHeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_product_view, parent, false)
                ProductViewHolder(view)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return list[position].itemType ?: 0
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            Constants.TextHeaderView -> {
                initTextHeaderViewHolderLayout(holder as TextHeaderViewHolder, position)
            }
            Constants.YangLagiHitsView -> {
                initYangLagiHitViewHolderLayout(holder as YangLagiHitsViewHolder, position)
            }
            Constants.ProductHeaderView -> {
                initProductHeaderViewHolderLayout(holder as ProductHeaderViewHolder, position)
            }
            else -> {
                initProductViewHolderLayout(holder as ProductViewHolder, position)
            }
        }
    }

    private fun initProductViewHolderLayout(productViewHolder: ProductViewHolder, position: Int) {
        val item = list[position]
        ImageHandler.loadImage(productViewHolder.itemView.context, productViewHolder.category_product_image, item.iconImageUrl, R.drawable.loading_page)
        productViewHolder.category_product_name.text = item.name
        productViewHolder.category_parent_layout.setOnClickListener {
            fireApplink(productViewHolder.itemView.context, item.applinks)
        }
    }

    private fun initProductHeaderViewHolderLayout(productHeaderViewHolder: ProductHeaderViewHolder, position: Int) {
        val item = list[position]
        ImageHandler.loadImage(productHeaderViewHolder.itemView.context, productHeaderViewHolder.product_image, item.iconImageUrl, R.drawable.loading_page)
        productHeaderViewHolder.product_name.text = item.name
        setDrawableRoundedImage(productHeaderViewHolder.product_header_parent, item.hexColor)

        productHeaderViewHolder.product_header_parent.setOnClickListener {
            fireApplink(productHeaderViewHolder.itemView.context, item.applinks)
        }
    }

    private fun initYangLagiHitViewHolderLayout(yangLagiHitsViewHolder: YangLagiHitsViewHolder, position: Int) {
        val item = list[position]
        setDrawableRoundedImage(yangLagiHitsViewHolder.yang_lagi_layout, item.hexColor)
        ImageHandler.loadImage(yangLagiHitsViewHolder.itemView.context, yangLagiHitsViewHolder.item_icon, item.iconImageUrl, R.drawable.loading_page)
        yangLagiHitsViewHolder.item_name.text = item.name
        yangLagiHitsViewHolder.yang_lagi_layout.setOnClickListener {
            fireApplink(yangLagiHitsViewHolder.itemView.context, item.applinks)
        }
    }

    private fun initTextHeaderViewHolderLayout(textHeaderViewHolder: TextHeaderViewHolder, position: Int) {
        val item = list[position]
        textHeaderViewHolder.header_title.text = item.name
    }


    private fun setDrawableRoundedImage(view: ConstraintLayout, color: String?) {
        try {
            val shape = GradientDrawable()
            shape.cornerRadius = 15.0f
            shape.setColor(Color.parseColor((color?.trim())))
            view.background = shape
        } catch (e: Exception) {

        }
    }

    private fun fireApplink(context: Context?, applinks: String?) {
        RouteManager.route(context, applinks)
    }

    class YangLagiHitsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item_icon: ImageView = view.item_icon
        val item_name: TextView = view.item_name
        val yang_lagi_layout: ConstraintLayout = view.yang_lagi_layout
    }

    class TextHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header_title: TextView = view.header_title
    }

    class ProductHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val product_image: ImageView = view.product_image
        val product_name: TextView = view.product_name
        val product_header_parent: ConstraintLayout = view.product_header_parent
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category_product_image: ImageView = view.category_product_image
        val category_product_name: TextView = view.category_product_name
        val category_parent_layout: ConstraintLayout = view.category_parent_layout
    }

}
