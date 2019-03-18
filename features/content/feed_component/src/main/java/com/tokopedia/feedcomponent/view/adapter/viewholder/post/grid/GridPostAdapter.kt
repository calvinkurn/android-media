package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_grid.view.*

/**
 * @author by milhamj on 07/12/18.
 */
class GridPostAdapter(private val contentPosition: Int,
                      private val gridPostViewModel: GridPostViewModel,
                      private val listener: GridItemListener)
    : RecyclerView.Adapter<GridPostAdapter.GridItemViewHolder>() {

    companion object {
        private const val MAX_FEED_SIZE = 6
        private const val MAX_FEED_SIZE_SMALL = 3
        private const val LAST_FEED_POSITION = 5
        private const val LAST_FEED_POSITION_SMALL = 2

        private const val EXTRA_DETAIL_ID = "{extra_detail_id}"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return GridItemViewHolder(view, gridPostViewModel.positionInFeed, contentPosition, listener)
    }

    override fun getItemCount(): Int {
        return when {
            gridPostViewModel.itemList.size >= MAX_FEED_SIZE -> MAX_FEED_SIZE
            gridPostViewModel.itemList.size >= MAX_FEED_SIZE_SMALL -> MAX_FEED_SIZE_SMALL
            else -> gridPostViewModel.itemList.size
        }
    }

    @Suppress("ConvertTwoComparisonsToRangeCheck")
    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        holder.bindImage(
                gridPostViewModel.itemList[position].thumbnail,
                gridPostViewModel.itemList.size
        )

        if (gridPostViewModel.showGridButton
                && gridPostViewModel.totalItems > MAX_FEED_SIZE
                && position == LAST_FEED_POSITION) {
            val extraProduct = gridPostViewModel.totalItems - LAST_FEED_POSITION
            holder.bindOthers(
                    extraProduct,
                    gridPostViewModel.actionText,
                    gridPostViewModel.actionLink,
                    gridPostViewModel.postId
            )

        } else if (gridPostViewModel.showGridButton
                && gridPostViewModel.totalItems < MAX_FEED_SIZE
                && gridPostViewModel.totalItems > MAX_FEED_SIZE_SMALL
                && position == LAST_FEED_POSITION_SMALL) {
            val extraProduct = gridPostViewModel.totalItems - LAST_FEED_POSITION_SMALL
            holder.bindOthers(
                    extraProduct,
                    gridPostViewModel.actionText,
                    gridPostViewModel.actionLink,
                    gridPostViewModel.postId
            )

        } else {
            holder.bindProduct(gridPostViewModel.itemList[position])
        }
    }

    class GridItemViewHolder(val v: View,
                             private val positionInFeed: Int,
                             private val contentPosition: Int,
                             private val listener: GridItemListener) : RecyclerView.ViewHolder(v) {
        fun bindImage(image: String, listSize: Int) {
            itemView.productImage.loadImage(image)
            setImageMargins(listSize)
        }

        fun bindProduct(item: GridItemViewModel) {
            itemView.extraProduct.background = null
            itemView.extraProduct.hide()

            itemView.text.setTextColor(MethodChecker.getColor(itemView.context, R.color.orange_red))
            itemView.text.text = item.price

            itemView.setOnClickListener {
                listener.onGridItemClick(positionInFeed, contentPosition, item.redirectLink)
                if (!item.trackingList.isEmpty()) {
                    listener.onAffiliateTrackClicked(item.trackingList)
                }
            }
        }

        fun bindOthers(numberOfExtraProduct: Int, actionText: String,
                       actionLink: String, postId: Int) {
            val extra = String.format("+%d", numberOfExtraProduct)
            itemView.extraProduct.background = ColorDrawable(
                    MethodChecker.getColor(itemView.context, R.color.black_38)
            )
            itemView.extraProduct.show()
            itemView.extraProduct.text = extra

            itemView.text.setTextColor(MethodChecker.getColor(itemView.context, R.color.black_54))
            itemView.text.text = actionText

            itemView.setOnClickListener {
                listener.onGridItemClick(
                        positionInFeed,
                        contentPosition,
                        if (!TextUtils.isEmpty(actionLink)) actionLink
                        else ApplinkConst.FEED_DETAILS.replace(EXTRA_DETAIL_ID, postId.toString())
                )
            }
        }

        private fun setImageMargins(listSize: Int) {
            if (listSize == 1) {
                itemView.productLayout.setMargin(0,0,0,0)
                itemView.productLayout.radius = 0f
                itemView.text.setPadding(
                        itemView.getDimens(R.dimen.dp_16),
                        0,
                        itemView.getDimens(R.dimen.dp_16),
                        0
                )
            } else {
                itemView.productLayout.setMargin(
                        itemView.getDimens(R.dimen.dp_2),
                        0,
                        itemView.getDimens(R.dimen.dp_2),
                        0
                )
                itemView.productLayout.radius = itemView.getDimens(R.dimen.dp_4).toFloat()
                itemView.text.setPadding(
                        itemView.getDimens(R.dimen.dp_4),
                        0,
                        itemView.getDimens(R.dimen.dp_4),
                        0
                )
            }
        }
    }

    interface GridItemListener {
        fun onGridItemClick(positionInFeed: Int, contentPosition: Int, redirectLink: String)

        fun onAffiliateTrackClicked(trackList : MutableList<TrackingViewModel>)
    }
}