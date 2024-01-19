package com.tokopedia.productcard.test.reimagine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.reimagine.ProductCardGridCarouselView
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.productCardGridCarouselHeight
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.productcard.test.R as productcardtestR
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ProductCardGridCarouselActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(productcardtestR.layout.product_card_reimagine_grid_carousel_activity_test_layout)

        Typography.isFontTypeOpenSauceOne = true

        val recyclerView = findViewById<RecyclerView>(
            productcardtestR.id.productCardReimagineGridCarouselTestRecyclerView
        )
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        private val testData = productCardReimagineCarouselGridTestData

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(
                    productcardtestR.layout.product_card_reimagine_grid_carousel_item_test_layout,
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
        private val productCardContainerCalculator: FrameLayout? by lazy {
            itemView.findViewById(productcardtestR.id.productCardContainerHeightCalculator)
        }
        private val productCardView: ProductCardGridCarouselView? by lazy {
            itemView.findViewById(productcardtestR.id.productCardReimagineGridCarouselView)
        }

        fun bind(productCardModel: ProductCardModel, description: String) {
            val productCardGridCarouselHeight =
                productCardGridCarouselHeight(itemView.context, productCardModel)

            productCardContainerCalculator?.layoutParams?.apply {
                height = productCardGridCarouselHeight
            }

            setBackgroundContainer(productCardModel, itemView)

            testDescription?.text =
                "$bindingAdapterPosition $description, " +
                "\nHeight: $productCardGridCarouselHeight px; ${productCardGridCarouselHeight.toDp()} dp"

            productCardView?.run {
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

        private fun setBackgroundContainer(productCardModel: ProductCardModel, view: View) {
            val contextResource = view.context
            if(productCardModel.isInBackground) {
                view.setBackgroundColor(contextResource.getColor(unifycomponentsR.color.Unify_GN100))
            } else {
                view.setBackgroundColor(contextResource.getColor(unifycomponentsR.color.Unify_NN0))
            }
        }
    }
}
