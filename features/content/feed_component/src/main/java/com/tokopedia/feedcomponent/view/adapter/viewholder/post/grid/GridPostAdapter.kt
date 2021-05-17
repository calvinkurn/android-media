package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_grid.view.*

/**
 * @author by milhamj on 07/12/18.
 */

private const val TYPE_DISCOUNT = "discount"
private const val TYPE_CASHBACK = "cashback"
private const val MAX_FEED_SIZE = 6
private const val MAX_FEED_SIZE_SMALL = 3
private const val LAST_FEED_POSITION = 5
private const val LAST_FEED_POSITION_SMALL = 2
private const val RAD_10f = 10f
private const val EXTRA_DETAIL_ID = "{extra_detail_id}"

class GridPostAdapter(private val contentPosition: Int,
                      private val gridPostViewModel: GridPostViewModel,
                      private val listener: GridItemListener)
    : RecyclerView.Adapter<GridPostAdapter.GridItemViewHolder>() {

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
            itemView.productImage.loadImageRounded(image, RAD_10f)
            setImageMargins(listSize)
        }

        fun bindProduct(item: GridItemViewModel) {
            itemView.extraProduct.background = null
            itemView.extraProduct.hide()

            itemView.priceText.text = item.price

            item.tagsList.firstOrNull()?.let {
                when (it.type) {
                    TYPE_CASHBACK -> {
                        setCashBackTypeTag(itemView.tagTypeText, it.text)
                        itemView.textSlashedPrice.hide()
                    }
                    TYPE_DISCOUNT -> {
                        setDiscountTypeTag(itemView.tagTypeText, it.text)
                        setSlashedPriceText(itemView.textSlashedPrice, item.priceOriginal)
                    }
                    else -> {
                        itemView.tagTypeText.hide()
                        itemView.textSlashedPrice.hide()
                    }
                }
            }

            itemView.setOnClickListener {
                listener.onGridItemClick(positionInFeed, contentPosition, adapterPosition, item.redirectLink)
                if (item.trackingList.isNotEmpty()) {
                    listener.onAffiliateTrackClicked(item.trackingList, true)
                }
            }
        }

        private fun setSlashedPriceText(textSlashedPrice: Typography?, priceOriginal: String) {
            textSlashedPrice?.run {
                text = priceOriginal
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                show()
            }
        }

        private fun setDiscountTypeTag(tagTypeText: Typography?, typeText: String) {
            tagTypeText?.run {
                text = typeText
                background = MethodChecker.getDrawable(itemView.context, R.drawable.discount_text_background)
                setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
                show()
            }

        }

        private fun setCashBackTypeTag(tagTypeText: Typography?, typeText: String) {
            tagTypeText?.run {
                text = typeText
                background = MethodChecker.getDrawable(itemView.context, R.drawable.cashback_text_background)
                setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                show()
            }

        }

        fun bindOthers(numberOfExtraProduct: Int, actionText: String,
                       actionLink: String, postId: Int) {
            val extra = "+$numberOfExtraProduct $actionText"
            itemView.extraProduct.background = ColorDrawable(
                    MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
            )
            itemView.extraProduct.show()
            itemView.extraProduct.text = extra

            itemView.priceText.hide()

            itemView.setOnClickListener {
                listener.onGridItemClick(
                    positionInFeed,
                    contentPosition,
                    adapterPosition,
                    if (!TextUtils.isEmpty(actionLink)) actionLink
                    else ApplinkConst.FEED_DETAILS.replace(EXTRA_DETAIL_ID, postId.toString())
                )
            }
        }

        private fun setImageMargins(listSize: Int) {
            if (listSize == 1) {
                itemView.productLayout.setMargin(0, 0, 0, 0)
                itemView.tagTypeText.setPadding(itemView.getDimens(R.dimen.dp_16), 0, itemView.getDimens(R.dimen.dp_16), 0)
                itemView.priceText.setPadding(itemView.getDimens(R.dimen.dp_16), 0, itemView.getDimens(R.dimen.dp_16), 0)
            } else {
                itemView.productLayout.setMargin(itemView.getDimens(R.dimen.dp_2), 0, itemView.getDimens(R.dimen.dp_2), 0)
                itemView.tagTypeText.setPadding(itemView.getDimens(R.dimen.dp_4), 0, itemView.getDimens(R.dimen.dp_4), 0)
                itemView.priceText.setPadding(itemView.getDimens(R.dimen.dp_4), 0, itemView.getDimens(R.dimen.dp_4), 0)
            }
        }
    }

    interface GridItemListener {
        fun onGridItemClick(positionInFeed: Int, contentPosition: Int, productPosition: Int,
                            redirectLink: String)

        fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean)
    }
}