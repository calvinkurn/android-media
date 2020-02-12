package com.tokopedia.productcard.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.test.utils.ProductCardItemDecoration
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.ProductCardListView

internal class ProductCardListActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_card_list_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.productCardListTestRecyclerView)
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
        recyclerView.addItemDecoration(createItemDecoration())
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return ProductCardItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16))
    }

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_list_item_test_layout, null)

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
            val productCardView = itemView.findViewById<ProductCardListView>(R.id.productCardList)

            productCardView?.setProductModel(productCardModel)
            productCardView?.setOnClickListener { Toast.makeText(itemView.context, adapterPosition.toString(), Toast.LENGTH_SHORT).show() }
        }
    }
}