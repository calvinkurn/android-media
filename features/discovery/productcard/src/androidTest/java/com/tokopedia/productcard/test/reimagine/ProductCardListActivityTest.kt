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
import com.tokopedia.productcard.reimagine.ProductCardListView
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.productcard.test.R as productcardtestR

class ProductCardListActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(productcardtestR.layout.product_card_reimagine_list_activity_test_layout)

        Typography.isFontTypeOpenSauceOne = true

        val recyclerView = findViewById<RecyclerView>(
            productcardtestR.id.productCardReimagineListTestRecyclerView
        )
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = createLayoutManager()
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    class Adapter: RecyclerView.Adapter<ViewHolder>() {

        private val testData = productCardReimagineListTestData

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(
                    productcardtestR.layout.product_card_reimagine_list_item_test_layout,
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
        private val productCardView: ProductCardListView? by lazy {
            itemView.findViewById(productcardtestR.id.productCardReimagineListView)
        }

        init {
            itemView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }

        fun bind(productCardModel: ProductCardModel, description: String) {
            setBackgroundContainer(productCardModel)

            testDescription?.text = "$bindingAdapterPosition $description"

            productCardView?.run {
                setProductModel(productCardModel)
                setOnClickListener { toast("Click") }
                setThreeDotsClickListener { toast("Three dots click") }
                setAddToCartOnClickListener { toast("Click ATC") }
            }
        }

        private fun setBackgroundContainer(productCardModel: ProductCardModel) {
            val contextResource = itemView.context
            if(productCardModel.isInBackground) {
                itemView.setBackgroundColor(contextResource.getColor(unifycomponentsR.color.Unify_GN100))
            } else {
                itemView.setBackgroundColor(contextResource.getColor(unifycomponentsR.color.Unify_NN0))
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
