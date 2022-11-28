package com.tokopedia.productcard.test.generator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.getProductCardModelMatcherData

internal class ProductCardIDGeneratorActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_card_id_generator_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.productCardIDGeneratorTestRecyclerView)
        recyclerView.adapter = Adapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    class Adapter: RecyclerView.Adapter<BaseTestViewHolder>() {

        companion object {
            private const val ITEM_SIZE = 4
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTestViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(viewType, null)

            return when(viewType) {
                GridViewHolder.LAYOUT -> GridViewHolder(view)
                GridViewStubViewHolder.LAYOUT -> GridViewStubViewHolder(view)
                ListViewHolder.LAYOUT -> ListViewStubViewHolder(view)
                else -> ListViewStubViewHolder(view)
            }
        }

        override fun getItemCount(): Int {
            return ITEM_SIZE
        }

        override fun onBindViewHolder(holder: BaseTestViewHolder, position: Int) {
            holder.bind(getProductCardModelMatcherData(false)[0].productCardModel)
        }

        override fun getItemViewType(position: Int): Int {
            return when(position) {
                0 -> GridViewHolder.LAYOUT
                1 -> GridViewStubViewHolder.LAYOUT
                2 -> ListViewHolder.LAYOUT
                else -> ListViewStubViewHolder.LAYOUT
            }
        }

        override fun onViewRecycled(holder: BaseTestViewHolder) {
            holder.recycle()
            super.onViewRecycled(holder)
        }
    }

    abstract class BaseTestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract val productCardGridView: ProductCardGridView?
        abstract val productCardListView: ProductCardListView?

        fun bind(productCardModel: ProductCardModel) {
            productCardGridView?.setProductModel(productCardModel)
            productCardListView?.setProductModel(productCardModel)
        }

        fun recycle() {
            productCardGridView?.recycle()
            productCardListView?.recycle()
        }
    }

    class GridViewHolder(itemView: View): BaseTestViewHolder(itemView) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.product_card_grid_item_test_layout
        }

        override val productCardGridView: ProductCardGridView? = itemView.findViewById(R.id.productCardGrid)
        override val productCardListView: ProductCardListView? = null
    }

    class GridViewStubViewHolder(itemView: View): BaseTestViewHolder(itemView) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.product_card_grid_item_viewstub_test_layout
        }

        override val productCardGridView: ProductCardGridView? = itemView.findViewById(R.id.productCardGrid)
        override val productCardListView: ProductCardListView? = null
    }

    class ListViewHolder(itemView: View): BaseTestViewHolder(itemView) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.product_card_list_item_test_layout
        }

        override val productCardGridView: ProductCardGridView? = null
        override val productCardListView: ProductCardListView? = itemView.findViewById(R.id.productCardList)
    }

    class ListViewStubViewHolder(itemView: View): BaseTestViewHolder(itemView) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.product_card_list_item_viewstub_test_layout
        }

        override val productCardGridView: ProductCardGridView? = null
        override val productCardListView: ProductCardListView? = itemView.findViewById(R.id.productCardList)
    }
}

