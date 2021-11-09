package com.tokopedia.carouselproductcard.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.productcard.ProductCardModel


internal class CarouselProductCardGridActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carousel_product_card_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.carouselProductCardRecyclerView)
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
            return carouselProductCardTestData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(carouselProductCardTestData[position])
        }

        override fun onViewRecycled(holder: ViewHolder) {
            holder.recycle()
            super.onViewRecycled(holder)
        }
    }

    internal class ViewHolder(itemView: View, private val recycledViewPool: RecyclerView.RecycledViewPool): RecyclerView.ViewHolder(itemView) {

        companion object {
            val LAYOUT = R.layout.carousel_product_card_item_test_layout
        }

        private val item: CarouselProductCardView? by lazy {
            itemView.findViewById<CarouselProductCardView>(R.id.carouselProductCard)
        }

        fun bind(productCardModelList: List<ProductCardModel>) {
            item?.bindCarouselProductCardViewGrid(
                    productCardModelList = productCardModelList,
                    recyclerViewPool = recycledViewPool,
                    carouselProductCardOnItemATCNonVariantClickListener = object: CarouselProductCardListener.OnATCNonVariantClickListener {
                        override fun onATCNonVariantClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int, quantity: Int) {
                            Toast.makeText(itemView.context, "ATC non variant position $carouselProductCardPosition, quantity $quantity", Toast.LENGTH_SHORT).show()
                        }
                    },
                    carouselProductCardOnItemAddVariantClickListener = object: CarouselProductCardListener.OnAddVariantClickListener {
                        override fun onAddVariantClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                            Toast.makeText(itemView.context, "ATC variant position $carouselProductCardPosition", Toast.LENGTH_SHORT).show()
                        }
                    },
                    showSeeMoreCard = true
            )
        }

        fun recycle() {
            item?.recycle()
        }
    }
}