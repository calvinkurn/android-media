package com.tokopedia.kolcomponent.view.adapter.viewholder.post.grid

import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.viewmodel.post.grid.GridItemViewModel
import com.tokopedia.kolcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_grid.view.*

/**
 * @author by milhamj on 07/12/18.
 */
class GridPostAdapter(private val gridPostViewModel: GridPostViewModel)
    : RecyclerView.Adapter<GridPostAdapter.GridItemViewHolder>() {

    companion object {
        private const val MAX_FEED_SIZE = 6
        private const val LAST_FEED_POSITION = 5
        private const val LAST_FEED_POSITION_SMALL = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return GridItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        holder.bindImage(gridPostViewModel.itemList[position].thumbnail)

        if (gridPostViewModel.itemList.size > MAX_FEED_SIZE && position == LAST_FEED_POSITION) {
            val extraProduct = gridPostViewModel.totalItems - LAST_FEED_POSITION
            holder.bindOthers(
                    extraProduct,
                    gridPostViewModel.actionText,
                    gridPostViewModel.actionLink
            )

        } else if (gridPostViewModel.itemList.size in LAST_FEED_POSITION_SMALL..LAST_FEED_POSITION
                && position == LAST_FEED_POSITION_SMALL) {
            val extraProduct = gridPostViewModel.totalItems - LAST_FEED_POSITION_SMALL
            holder.bindOthers(
                    extraProduct,
                    gridPostViewModel.actionText,
                    gridPostViewModel.actionLink
            )

        } else {
            holder.bindProduct(gridPostViewModel.itemList[position])
        }
    }

    override fun getItemCount() = gridPostViewModel.itemList.size

    class GridItemViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        fun bindImage(image: String) {
            itemView.productImage.loadImage(image)
        }

        fun bindProduct(item: GridItemViewModel) {
            itemView.extraProduct.background = null
            itemView.extraProduct.gone()

            itemView.text.setTextColor(MethodChecker.getColor(itemView.context, R.color.orange_red))
            itemView.text.text = item.price
        }

        fun bindOthers(numberOfExtraProduct: Int, actionText: String,
                       actionLink: String) {
            val extra = String.format("+%d", numberOfExtraProduct)
            itemView.extraProduct.background = ColorDrawable(
                    MethodChecker.getColor(itemView.context, R.color.black_38)
            )
            itemView.extraProduct.visible()
            itemView.extraProduct.text = extra

            itemView.text.setTextColor(MethodChecker.getColor(itemView.context, R.color.black_54))
            itemView.text.text = actionText
        }
    }
}