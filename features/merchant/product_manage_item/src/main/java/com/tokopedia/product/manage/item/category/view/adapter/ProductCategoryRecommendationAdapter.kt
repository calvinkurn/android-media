package com.tokopedia.product.manage.item.category.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import kotlinx.android.synthetic.main.item_product_category_recommendation.view.*

class ProductCategoryRecommendationAdapter(private val categoryRecommendationList: MutableList<ProductCategory>,
                                           var listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPosition = -1

    interface Listener {
        fun onCategoryRecommendationChoosen(productCategory: ProductCategory)
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_category_recommendation, parent, false)
        return ProductCategoryRecommendationViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.labelCategoryRecommendation.title = categoryRecommendationList[position].categoryName
        categoryRecommendationList[position].categoryList?.run {
            var title = convertTitle(this)
            var subtitle = convertSubtitle(this)
            if(TextUtils.isEmpty(title)){
                title = subtitle
                subtitle = ""
            }
            holder.itemView.labelCategoryRecommendation.title = title
            holder.itemView.labelCategoryRecommendation.subTitleText(subtitle)
        }
        holder.itemView.labelCategoryRecommendation.isChecked = selectedPosition == position
    }

    private fun convertSubtitle(strings: Array<String>): String {
        if(strings.size > 0){
            return strings.get(strings.size -1)
        }
        return ""
    }

    private fun convertTitle(strings: Array<String>): String {
        var tempName = ""
        for (i in 0 until strings.size -1) {
            if (!TextUtils.isEmpty(tempName)) {
                tempName += " / "
            }
            tempName += strings[i]
        }
        return tempName
    }


    override fun getItemCount() = categoryRecommendationList.size

    override fun getItemId(position: Int): Long {
        return categoryRecommendationList[position].categoryId.toLong()
    }

    fun replaceData(categoryRecommendationList: List<ProductCategory>) {
        this.categoryRecommendationList.clear()
        this.categoryRecommendationList.addAll(categoryRecommendationList)
        notifyDataSetChanged()
    }

    private fun getSelectedCategory(): ProductCategory {
        return categoryRecommendationList[selectedPosition]
    }

    private fun isWithinDataset(position: Int): Boolean {
        return position >= 0 && position <= categoryRecommendationList.size - 1
    }

    fun setSelectedCategory(productCategory: ProductCategory) {
        var isMatchCategory = false
        for ((index, value) in categoryRecommendationList.withIndex()) {
            if (value.categoryId == productCategory.categoryId) {
                selectedPosition = index
                isMatchCategory = true
                break
            }
        }
        if(!isMatchCategory){
            selectedPosition = -1
        }
        notifyDataSetChanged()
    }

    inner class ProductCategoryRecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            setView()
        }

        fun setView() {
            itemView.labelCategoryRecommendation.setOnClickListener {
                onClickItemCategory()
            }
            itemView.setOnClickListener {
                onClickItemCategory()
            }
        }

        private fun onClickItemCategory() {
            selectedPosition = adapterPosition
            notifyDataSetChanged()
            if(isWithinDataset(selectedPosition)){
                listener.onCategoryRecommendationChoosen(getSelectedCategory())
            }
        }
    }
}