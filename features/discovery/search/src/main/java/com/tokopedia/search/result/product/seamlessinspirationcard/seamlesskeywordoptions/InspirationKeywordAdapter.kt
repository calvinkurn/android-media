package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.product.changeview.ViewType
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.GridInspirationKeywordItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.InspirationKeywordItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.ListInspirationKeywordItemViewHolder

class InspirationKeywordAdapter(
    private val inspirationKeywordDataView: List<InspirationKeywordDataView>,
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val styleView: ViewType
) : RecyclerView.Adapter<InspirationKeywordItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InspirationKeywordItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(getLayoutId(styleView), parent, false)

        return getAdapter(styleView, view, inspirationKeywordDataView.size)
    }

    override fun getItemCount(): Int {
        return inspirationKeywordDataView.size
    }

    override fun onBindViewHolder(holder: InspirationKeywordItemViewHolder, position: Int) {
        holder.bind(inspirationKeywordDataView[position])
    }

    private fun getAdapter(viewType: ViewType, view: View, totalItem: Int) =
        when (viewType) {
            ViewType.LIST, ViewType.BIG_GRID ->
                ListInspirationKeywordItemViewHolder(
                    view,
                    inspirationKeywordListener,
                    totalItem,
                    styleView
                )

            else ->
                GridInspirationKeywordItemViewHolder(view, inspirationKeywordListener)
        }

    private fun getLayoutId(viewType: ViewType): Int =
        when (viewType) {
            ViewType.LIST, ViewType.BIG_GRID ->
                ListInspirationKeywordItemViewHolder.LAYOUT

            else ->
                GridInspirationKeywordItemViewHolder.LAYOUT
        }
}
