package com.tokopedia.productcard.test

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v3.ProductCardGridView
import kotlin.math.cos
import kotlin.math.roundToInt

internal class ProductCardGridActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_card_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.productCardSmallGridTestRecyclerView)
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
        recyclerView.addItemDecoration(createItemDecoration())
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return ItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16))
    }

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_item_test_layout, null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return productCardModelTestData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(productCardModelTestData[position])
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(productCardModel: ProductCardModel) {
            val productCardView = itemView.findViewById<ProductCardGridView>(R.id.productCardSmallGrid)

            productCardView?.setProductModel(productCardModel)
            productCardView?.setOnClickListener { Toast.makeText(itemView.context, adapterPosition.toString(), Toast.LENGTH_SHORT).show() }
        }
    }

    class ItemDecoration(private val spacing: Int): RecyclerView.ItemDecoration() {

        private var verticalCardViewOffset = 0
        private var horizontalCardViewOffset = 0

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            verticalCardViewOffset = getVerticalCardViewOffset(view)
            horizontalCardViewOffset = getHorizontalCardViewOffset(view)

            val absolutePos = parent.getChildAdapterPosition(view)
            val relativePos = getProductItemRelativePosition(view)
            val totalSpanCount = getTotalSpanCount(parent)

            outRect.left = getLeftOffset(relativePos, totalSpanCount)
            outRect.top = getTopOffset(absolutePos, totalSpanCount)
            outRect.right = getRightOffset(relativePos, totalSpanCount)
            outRect.bottom = getBottomOffset()
        }

        private fun getVerticalCardViewOffset(view: View): Int {
            if (view is CardView) {
                val maxElevation = view.maxCardElevation
                val radius = view.radius
                return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
            }

            return 0
        }

        private fun getHorizontalCardViewOffset(view: View): Int {
            if (view is CardView) {
                val maxElevation = view.maxCardElevation
                val radius = view.radius
                return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
            }

            return 0
        }

        private fun getProductItemRelativePosition(view: View): Int {
            return if (view.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                getProductItemRelativePositionStaggeredGrid(view)
            } else 0
        }

        private fun getProductItemRelativePositionStaggeredGrid(view: View): Int {
            val staggeredGridLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
            return staggeredGridLayoutParams.spanIndex
        }

        private fun getTotalSpanCount(parent: RecyclerView): Int {
            val layoutManager = parent.layoutManager
            return if (layoutManager is StaggeredGridLayoutManager) layoutManager.spanCount else 0
        }

        private fun getLeftOffset(relativePos: Int, totalSpanCount: Int): Int {
            return if (isFirstInRow(relativePos, totalSpanCount)) getLeftOffsetFirstInRow() else getLeftOffsetNotFirstInRow()
        }

        private fun isFirstInRow(relativePos: Int, spanCount: Int): Boolean {
            return relativePos % spanCount == 0
        }

        private fun getLeftOffsetFirstInRow(): Int {
            return spacing - horizontalCardViewOffset
        }

        private fun getLeftOffsetNotFirstInRow(): Int {
            return spacing / 4 - horizontalCardViewOffset
        }

        private fun getTopOffset(absolutePos: Int, totalSpanCount: Int): Int {
            return if (isTopProductItem(absolutePos, totalSpanCount)) getTopOffsetTopItem() else getTopOffsetNotTopItem()
        }

        private fun isTopProductItem(absolutePos: Int, totalSpanCount: Int): Boolean {
            return absolutePos < totalSpanCount
        }

        private fun getTopOffsetTopItem(): Int {
            return spacing - verticalCardViewOffset
        }

        private fun getTopOffsetNotTopItem(): Int {
            return spacing / 4 - verticalCardViewOffset
        }

        private fun getRightOffset(relativePos: Int, totalSpanCount: Int): Int {
            return if (isLastInRow(relativePos, totalSpanCount)) getRightOffsetLastInRow() else getRightOffsetNotLastInRow()
        }

        private fun isLastInRow(relativePos: Int, spanCount: Int): Boolean {
            return relativePos % spanCount == spanCount - 1
        }

        private fun getRightOffsetLastInRow(): Int {
            return spacing - horizontalCardViewOffset
        }

        private fun getRightOffsetNotLastInRow(): Int {
            return spacing / 4 - horizontalCardViewOffset
        }

        private fun getBottomOffset(): Int {
            return spacing / 4 - verticalCardViewOffset
        }
    }
}