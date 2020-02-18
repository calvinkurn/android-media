package com.tokopedia.carouselproductcard.test

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.carouselproductcard.test.R


internal class CarouselProductCardGridActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carousel_product_card_grid_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.carouselProductCardGridRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = Adapter()
    }

    internal class Adapter : RecyclerView.Adapter<ViewHolder>() {

        private val recycledViewPool = RecyclerView.RecycledViewPool()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false)
            return ViewHolder(view, recycledViewPool)
        }

        override fun getItemCount(): Int {
            return carouselProductCardGridTestData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(carouselProductCardGridTestData[position])
        }
    }

    internal class ViewHolder(itemView: View, private val recycledViewPool: RecyclerView.RecycledViewPool): RecyclerView.ViewHolder(itemView) {

        companion object {
            val LAYOUT = R.layout.carousel_product_card_grid_item_test_layout
        }

        fun bind(productCardModelList: List<ProductCardModel>) {
            val item = itemView.findViewById<CarouselProductCardView>(R.id.carouselProductCard)
            item.bindCarouselProductCardViewGrid(
                    productCardModelList = productCardModelList,
                    recyclerViewPool = recycledViewPool
            )
        }
    }
}