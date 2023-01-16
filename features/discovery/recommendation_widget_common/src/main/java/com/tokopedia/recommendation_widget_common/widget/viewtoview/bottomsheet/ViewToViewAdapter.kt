package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewBinding
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewShimmeringBinding


class ViewToViewAdapter(
    private val listener: ViewToViewListener,
    diffUtilItemCallback: ViewToViewDiffUtilItemCallback = ViewToViewDiffUtilItemCallback(),
) : ListAdapter<ViewToViewDataModel, ViewToViewViewHolder>(
    diffUtilItemCallback
) {
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is ViewToViewDataModel.Product) ViewToViewViewHolder.Product.LAYOUT else ViewToViewViewHolder.Loading.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewToViewViewHolder {
        return when(viewType) {
            R.layout.item_view_to_view -> {
                val view = ItemViewToViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewToViewViewHolder.Product(listener, view.root)
            }
            else -> {
                val view = ItemViewToViewShimmeringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewToViewViewHolder.Loading(view.root)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewToViewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
