package com.tokopedia.kategori.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kategori.analytics.CategoryAnalytics.Companion.categoryAnalytics
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.kategori.Constants
import com.tokopedia.kategori.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_category_product_header_view.view.*
import kotlinx.android.synthetic.main.item_category_product_header_view.view.product_name
import kotlinx.android.synthetic.main.item_category_product_view.view.*
import kotlinx.android.synthetic.main.item_category_text_header.view.*
import kotlinx.android.synthetic.main.item_category_yang_lagi_hit.view.*

private const val totalColumns = 3
private const val rowZero = 0
private const val rowOne = 1

class CategoryLevelTwoAdapter(private val list: MutableList<CategoryChildItem>,
                              private val trackingQueue: TrackingQueue?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewMap = HashMap<Int, Boolean>()
    private var productTrackerList = ArrayList<CategoryChildItem>()
    private var yangLangHitTrackerList = ArrayList<CategoryChildItem>()
    private var seringKamuTrackerList = ArrayList<CategoryChildItem>()

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
            Constants.ProductView -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_product_view, parent, false)
                ProductViewHolder(view)
            }
            Constants.HeaderShimmer -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shimmer_product_header, parent, false)
                ShimmerProductHeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shimmer_product, parent, false)
                ShimmerProductViewHolder(view)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position < itemCount) {
            list[position].itemType ?: 0
        } else {
            0
        }
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
            Constants.ProductView -> {
                initProductViewHolderLayout(holder as ProductViewHolder, position)
            }
        }
    }

    private fun initProductViewHolderLayout(productViewHolder: ProductViewHolder, position: Int) {
        val item = list[position]
        ImageHandler.loadImage(productViewHolder.itemView.context, productViewHolder.productImage, item.iconImageUrl, R.drawable.square_shimmer)
        with(productViewHolder.productName) {setTextAndContentDescription(item.name, R.string.content_desc_category_product_name)}
        productViewHolder.productRootLayout.setOnClickListener {
            fireApplink(productViewHolder.itemView.context, item.applinks)

            if (item.isSeringKamuLihat) {
                categoryAnalytics.eventSeringKamuLihatClick(item, item.categoryPosition)
            } else {
                categoryAnalytics.eventBannerCategoryLevelTwoClick(item, position)
            }
        }
        // product border Logic

        if (isLastRowElement(item.sameCategoryTotalCount, item.categoryPosition)) {
            productViewHolder.bottomBorder.hide()
        }
        when (item.categoryPosition % totalColumns) {
            rowZero -> {
                productViewHolder.leftBorder.hide()
                productViewHolder.rightBorder.hide()
            }

            rowOne -> {
                productViewHolder.leftBorder.hide()
                if (item.categoryPosition != item.sameCategoryTotalCount) {
                    productViewHolder.rightBorder.hide()
                }
            }
        }
    }

    private fun isLastRowElement(sameCategoryTotalCount: Int, categoryPosition: Int): Boolean {
        val div = sameCategoryTotalCount / totalColumns
        val mod = sameCategoryTotalCount % totalColumns
        return if (mod == 0) {
            categoryPosition > (sameCategoryTotalCount - totalColumns)
        } else {
            categoryPosition > div * totalColumns
        }
    }


    private fun initProductHeaderViewHolderLayout(productHeaderViewHolder: ProductHeaderViewHolder, position: Int) {
        val item = list[position]
        ImageHandler.loadImage(productHeaderViewHolder.itemView.context, productHeaderViewHolder.productHeaderImage, item.iconImageUrl, R.drawable.category_ic_broken_image)
        with(productHeaderViewHolder.productHeaderName) {setTextAndContentDescription(item.name, R.string.content_desc_product_name)}
        item.hexColor?.let {
            if (it.isEmpty()) {
                val shape = GradientDrawable()
                shape.cornerRadius = 17.0f
                shape.setColor(productHeaderViewHolder.itemView.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_B500))
                productHeaderViewHolder.productHeaderRoot.background = shape
            } else {
                setDrawableRoundedImage(productHeaderViewHolder.productHeaderRoot, it)
            }
        }
        productHeaderViewHolder.productHeaderRoot.setOnClickListener {
            fireApplink(productHeaderViewHolder.itemView.context, item.applinks)
            categoryAnalytics.eventBannerCategoryLevelOneClick(list[position])
        }
    }

    private fun initYangLagiHitViewHolderLayout(yangLagiHitsViewHolder: YangLagiHitsViewHolder, position: Int) {
        val item = list[position]
        when (position % 2) {
            0 -> {
                yangLagiHitsViewHolder.ylhRootLayout.setMargin(convertDpToPx(yangLagiHitsViewHolder.itemView.context, 4), 0, 0, convertDpToPx(yangLagiHitsViewHolder.itemView.context, 8))
            }

            1 -> {
                yangLagiHitsViewHolder.ylhRootLayout.setMargin(0, 0, convertDpToPx(yangLagiHitsViewHolder.itemView.context, 4), convertDpToPx(yangLagiHitsViewHolder.itemView.context, 8))
            }
        }
        setDrawableRoundedImage(yangLagiHitsViewHolder.ylhRootLayout, item.hexColor)
        ImageHandler.loadImage(yangLagiHitsViewHolder.itemView.context, yangLagiHitsViewHolder.ylhProductImage, item.iconImageUrl, R.drawable.square_shimmer)
        with(yangLagiHitsViewHolder.ylhProductName) {setTextAndContentDescription(item.name, R.string.content_desc_item_name)}
        yangLagiHitsViewHolder.ylhRootLayout.setOnClickListener {
            fireApplink(yangLagiHitsViewHolder.itemView.context, item.applinks)
            categoryAnalytics.eventYangLagiHitClick(list[position], position)
        }
    }

    private fun initTextHeaderViewHolderLayout(textHeaderViewHolder: TextHeaderViewHolder, position: Int) {
        list[position].name?.let {
            with(textHeaderViewHolder.headerTitle) {setTextAndContentDescription(it, R.string.content_desc_header_title)}
            if (it.toLowerCase().contains("sering kamu lihat")) {
                textHeaderViewHolder.headerTitle.setMargin(0, convertDpToPx(textHeaderViewHolder.itemView.context, 20), 0, 0)
            }
        }
    }


    private fun setDrawableRoundedImage(view: ConstraintLayout, color: String?) {
        try {
            val shape = GradientDrawable()
            shape.cornerRadius = 17.0f
            shape.setColor(Color.parseColor((color?.trim())))
            view.background = shape
        } catch (e: Exception) {

        }
    }

    private fun fireApplink(context: Context?, applinks: String?) {
        RouteManager.route(context, applinks)
    }

    class YangLagiHitsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ylhProductImage: ImageView = view.item_icon
        val ylhProductName: TextView = view.item_name
        val ylhRootLayout: ConstraintLayout = view.yang_lagi_layout
    }

    class TextHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTitle: TextView = view.header_title
    }

    class ProductHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productHeaderImage: ImageView = view.product_image
        val productHeaderName: TextView = view.product_name
        val productHeaderRoot: ConstraintLayout = view.product_header_parent
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.category_product_image
        val productName: TextView = view.category_product_name
        val productRootLayout: ConstraintLayout = view.category_parent_layout
        val leftBorder: View = view.border_left
        val rightBorder: View = view.border_right
        val bottomBorder: View = view.border_bottom
    }

    class ShimmerProductHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ShimmerProductViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap.containsKey(position)) {
            viewMap[position] = true
            val item = list[position]
            when (holder.itemViewType) {
                Constants.ProductView -> {
                    if (list[position].isSeringKamuLihat) {
                        seringKamuTrackerList.add(item)
                    } else {
                        item.categoryPosition = position
                        productTrackerList.add(item)
                    }
                }
                Constants.YangLagiHitsView -> {
                    item.categoryPosition = position
                    yangLangHitTrackerList.add(item)
                }
                Constants.ProductHeaderView -> {
                    categoryAnalytics.eventCategoryLevelOneBannerView(item, position)
                }
            }
        }
    }

    private fun convertDpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun pushTrackingEvents() {
        if (seringKamuTrackerList.isNotEmpty()) {
            categoryAnalytics.eventSeringkamuLihatView(seringKamuTrackerList)
        }

        if (productTrackerList.isNotEmpty()) {
            categoryAnalytics.eventCategoryLevelTwoView(productTrackerList)
        }
        if (yangLangHitTrackerList.isNotEmpty()) {
            categoryAnalytics.eventYangLagiHitView(yangLangHitTrackerList)
        }
        seringKamuTrackerList.clear()
        productTrackerList.clear()
        yangLangHitTrackerList.clear()
    }
}
