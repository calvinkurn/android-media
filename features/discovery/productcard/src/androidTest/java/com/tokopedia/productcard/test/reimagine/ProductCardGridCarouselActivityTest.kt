package com.tokopedia.productcard.test.reimagine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.reimagine.ProductCardGridCarouselView
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.test.R
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.productcard.R as productcardR

class ProductCardGridCarouselActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_card_reimagine_grid_carousel_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.productCardReimagineGridCarouselTestRecyclerView)
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        private val testData = productCardReimagineCarouselGridTestData

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_reimagine_grid_carousel_item_test_layout, null)

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
            itemView.findViewById(R.id.productCardReimagineTestDescription)
        }
        private val productCardView: ProductCardGridCarouselView? by lazy {
            itemView.findViewById(R.id.productCardReimagineGridCarouselView)
        }

        fun bind(productCardModel: ProductCardModel, description: String) {
            testDescription?.text = description

            productCardView?.findViewById<CardUnify2?>(
                productcardR.id.productCardCardUnifyContainer
            )?.run {
                layoutParams = layoutParams?.apply { height = WRAP_CONTENT }
            }

            productCardView?.run {
                layoutParams = layoutParams?.apply { height = WRAP_CONTENT }

                setProductModel(productCardModel)
                setOnClickListener { toast("Click") }
                setAddToCartOnClickListener { toast("Click ATC") }
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
