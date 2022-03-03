package com.tokopedia.topads.dashboard.view.adapter.education

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_4
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_5
import com.tokopedia.topads.dashboard.data.model.ListArticle
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TopadsEducationRvAdapter(private val list: ListArticle) :
    RecyclerView.Adapter<TopadsEducationRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_education, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.image.setImageDrawable(holder.view.context.getIcon(holder.adapterPosition))
        holder.title.text = item.categoryName
        holder.view.setOnClickListener { itemClick?.invoke(item) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var itemClick: ((ListArticle.ListArticleItem) -> Unit)? = null

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageUnify = view.findViewById(R.id.ivEducation)
        val title: Typography = view.findViewById(R.id.txtTitleEducation)
    }

    private fun Context.getIcon(id: Int): Drawable? {
        return when (id) {
            CONST_0 -> ContextCompat.getDrawable(
                this,
                com.tokopedia.unifycomponents.R.drawable.iconunify_promo
            )
            CONST_2 -> ContextCompat.getDrawable(
                this, com.tokopedia.unifycomponents.R.drawable.iconunify_clipboard
            )
            CONST_1 -> ContextCompat.getDrawable(
                this,
                com.tokopedia.unifycomponents.R.drawable.iconunify_wallet
            )
            CONST_3 -> ContextCompat.getDrawable(
                this,
                com.tokopedia.unifycomponents.R.drawable.iconunify_graph
            )
            CONST_5 -> ContextCompat.getDrawable(
                this, com.tokopedia.unifycomponents.R.drawable.iconunify_user_success
            )
            CONST_4 -> ContextCompat.getDrawable(
                this, com.tokopedia.unifycomponents.R.drawable.iconunify_lightbulb
            )
            else -> ContextCompat.getDrawable(
                this,
                com.tokopedia.unifycomponents.R.drawable.iconunify_product_promo
            )
        }
    }
}