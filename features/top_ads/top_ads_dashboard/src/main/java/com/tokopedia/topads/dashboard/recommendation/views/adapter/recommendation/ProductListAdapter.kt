package com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductListUiModel
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ProductListAdapter :
    ListAdapter<ProductListUiModel, RecyclerView.ViewHolder>(ProductListDiffUtilCallBack()){

    inner class ProductListItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view){

        private val title: com.tokopedia.unifyprinciples.Typography = view.findViewById(R.id.title)
        private val description: com.tokopedia.unifyprinciples.Typography = view.findViewById(R.id.description)
        private val image: ImageUnify = view.findViewById(R.id.image)
        private val checkboxUnify : CheckboxUnify = view.findViewById(R.id.checkbox)

        fun bind(item : ProductItemUiModel){
            title.text = item.productName
            description.text = item.searchCount
            image.urlSrc = item.imgUrl
            checkboxUnify.isChecked = item.isSelected
        }
    }

    inner class EmptyStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(){}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.topads_potential_product_item_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_potential_product_item_layout, parent, false)
                ProductListItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_potential_product_item_layout, parent, false)
                EmptyStateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)){
            is ProductItemUiModel -> {
                (holder as? ProductListItemViewHolder)?.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is ProductItemUiModel -> R.layout.topads_potential_product_item_layout
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }
}

class ProductListDiffUtilCallBack: DiffUtil.ItemCallback<ProductListUiModel>(){
    override fun areItemsTheSame(
        oldItem: ProductListUiModel,
        newItem: ProductListUiModel
    ): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(
        oldItem: ProductListUiModel,
        newItem: ProductListUiModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }

}
