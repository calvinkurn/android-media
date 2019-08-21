package com.tokopedia.instantloan.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.GqlLendingCategoryData
import com.tokopedia.instantloan.network.InstantLoanUrl


class LendingCategoryAdapter(lendingCategoryList: ArrayList<GqlLendingCategoryData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categoryList = lendingCategoryList
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.il_lending_category_item, null)
        return LendingCategoryAdapter.LeCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LendingCategoryAdapter.LeCategoryViewHolder).bindData(categoryList.get(position), position)
    }

    class LeCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var imageView: ImageView
        var heading: TextView
        var context: Context

        init {
            context = view.context
            imageView = view.findViewById(R.id.category_image_view)
            heading = view.findViewById(R.id.category_heading)
        }

        fun bindData(categoryItem: GqlLendingCategoryData, position: Int) {
            heading.text = categoryItem.categoryName
            ImageHandler.LoadImage(imageView, categoryItem.categoryIconUrl)
            itemView.tag = categoryItem.categoryNameSlug
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val rUrl = String.format(InstantLoanUrl.COMMON_URL.IL_CATEGORY_URL, view?.tag as String)
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    rUrl))
        }
    }
}