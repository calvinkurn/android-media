package com.tokopedia.productcard.test.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.utils.ProductCardItemDecoration

internal class ProductCardGridActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_card_grid_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.productCardGridTestRecyclerView)
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
        recyclerView.addItemDecoration(createItemDecoration())
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        return staggeredGridLayoutManager
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return ProductCardItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16))
    }

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_grid_item_test_layout, null)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return productCardGridTestData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(productCardGridTestData[position].productCardModel)
        }

        override fun onViewRecycled(holder: ViewHolder) {
            holder.recycle()
            super.onViewRecycled(holder)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val productCardView: ProductCardGridView? by lazy {
            itemView.findViewById(R.id.productCardGrid)
        }

        fun bind(productCardModel: ProductCardModel) {
            productCardView?.setProductModel(productCardModel)
            productCardView?.setOnClickListener { toast("Click") }
            productCardView?.setThreeDotsOnClickListener { toast("Three dots") }
            productCardView?.setAddToCartOnClickListener { toast("Add to cart") }
            productCardView?.setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    toast("Quantity changed to $quantity")
                }
            })
            productCardView?.setAddVariantClickListener { toast("Add Variant") }
        }

        private fun toast(message: String) {
            val toastMessage = "Position $adapterPosition, $message"
            Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
        }

        fun recycle() {
            productCardView?.recycle()
        }
    }

}
