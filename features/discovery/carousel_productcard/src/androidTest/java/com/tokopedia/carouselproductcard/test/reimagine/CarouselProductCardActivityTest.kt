package com.tokopedia.carouselproductcard.test.reimagine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.reimagine.CarouselProductCardModel
import com.tokopedia.carouselproductcard.reimagine.CarouselProductCardView
import com.tokopedia.carouselproductcard.reimagine.grid.CarouselProductCardGridModel
import com.tokopedia.carouselproductcard.test.R
import com.tokopedia.productcard.reimagine.ProductCardModel

class CarouselProductCardActivityTest: AppCompatActivity() {

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

    internal class ViewHolder(
        itemView: View,
        private val recycledViewPool: RecyclerView.RecycledViewPool,
    ): RecyclerView.ViewHolder(itemView) {

        companion object {
            val LAYOUT = R.layout.carousel_product_card_reimagine_item_test_layout
        }

        private val item: CarouselProductCardView? by lazy {
            itemView as? CarouselProductCardView
        }

        fun bind(testCase: CarouselProductCardTestCase) {
            val carouselProductCardGridModels =
                testCase.productCardModelList.map(::carouselProductCardGridModel) +
                    listOfNotNull(testCase.viewAllCard)

            item?.bind(CarouselProductCardModel(
                itemList = carouselProductCardGridModels,
                recycledViewPool = recycledViewPool,
            ))
        }

        private fun carouselProductCardGridModel(productCardModel: ProductCardModel) =
            CarouselProductCardGridModel(
                productCardModel = productCardModel,
                onClick = { onClick(productCardModel) },
            )

        private fun onClick(productCardModel: ProductCardModel) {
            Toast.makeText(itemView.context, productCardModel.name, Toast.LENGTH_SHORT).show()
        }

        fun recycle() {
            item?.recycle()
        }
    }
}
