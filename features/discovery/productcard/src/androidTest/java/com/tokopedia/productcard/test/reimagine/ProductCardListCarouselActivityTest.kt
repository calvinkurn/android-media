package com.tokopedia.productcard.test.reimagine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.reimagine.ProductCardListCarouselView
import com.tokopedia.productcard.reimagine.ProductCardListView
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.productcard.test.R as productcardtestR

class ProductCardListCarouselActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(productcardtestR.layout.product_card_reimagine_list_carousel_activity_test_layout)

        Typography.isFontTypeOpenSauceOne = true

        val recyclerView = findViewById<RecyclerView>(
            productcardtestR.id.productCardReimagineListCarouselTestRecyclerView
        )
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        private val testData = productCardReimagineListCarouselTestData

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(
                    productcardtestR.layout.product_card_reimagine_list_carousel_item_test_layout,
                    parent,
                    false,
                )

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return testData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(testData[position].first, testData[position].third)
        }

        override fun onViewRecycled(holder: ViewHolder) {
            holder.recycle()
            super.onViewRecycled(holder)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val testDescription: TextView? by lazy {
            itemView.findViewById(productcardtestR.id.productCardReimagineTestDescription)
        }
        private val productCardView: ProductCardListCarouselView? by lazy {
            itemView.findViewById(productcardtestR.id.productCardReimagineListCarouselView)
        }

        init {
            itemView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }

        fun bind(productCardModel: ProductCardModel, description: String) {
            testDescription?.text = description

            productCardView?.run {
                setProductModel(productCardModel)
                setOnClickListener { toast("Click") }
            }
        }

        private fun toast(message: String) {
            val toastMessage = "Position $bindingAdapterPosition, $message"
            Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
        }

        fun recycle() {
        }
    }
}
