package com.tokopedia.productcard.test.reimagine

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.tokopedia.productcard.reimagine.ProductCardGridCarouselView
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.utils.ProductCardItemDecoration
import com.tokopedia.unifycomponents.toPx

class ProductCardGridCarouselActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_card_reimagine_grid_carousel_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.productCardReimagineGridCarouselTestRecyclerView)
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
        recyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                outRect.bottom = 16.toPx()
            }
        })
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager =
        StaggeredGridLayoutManager(1, VERTICAL)

    private fun createItemDecoration(): RecyclerView.ItemDecoration = ProductCardItemDecoration(
        resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
    )

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_reimagine_grid_carousel_item_test_layout, null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return productCardReimagineCarouselGridTestData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(productCardReimagineCarouselGridTestData[position].first)
        }

        override fun onViewRecycled(holder: ViewHolder) {
            holder.recycle()
            super.onViewRecycled(holder)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val productCardView: ProductCardGridCarouselView? by lazy {
            itemView.findViewById(R.id.productCardReimagineGridCarouselView)
        }

        fun bind(productCardModel: ProductCardModel) {
            productCardView?.setProductModel(productCardModel)
            productCardView?.setOnClickListener { toast("Click") }
        }

        private fun toast(message: String) {
            val toastMessage = "Position $bindingAdapterPosition, $message"
            Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
        }

        fun recycle() {
        }
    }
}
