package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyR

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

class GridPostAdapter(
    private val contentPosition: Int,
    private val gridPostModel: GridPostModel,
    private val listener: GridItemListener
) : RecyclerView.Adapter<GridPostAdapter.GridItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return GridItemViewHolder(
            view,
            gridPostModel.positionInFeed,
            contentPosition,
            listener,
            gridPostModel
        )
    }

    override fun getItemCount(): Int {
        return when {
            gridPostModel.itemList.size >= MAX_FEED_SIZE -> MAX_FEED_SIZE
            gridPostModel.itemList.size >= MAX_FEED_SIZE_SMALL -> MAX_FEED_SIZE_SMALL
            else -> gridPostModel.itemList.size
        }
    }

    @Suppress("ConvertTwoComparisonsToRangeCheck")
    override fun onBindViewHolder(holder: GridItemViewHolder, position: Int) {
        holder.bindImage(
            gridPostModel.itemList[position].thumbnail,
            gridPostModel.itemList.size
        )

        if (gridPostModel.showGridButton
            && gridPostModel.totalItems > MAX_FEED_SIZE
            && position == LAST_FEED_POSITION
        ) {
            val extraProduct = gridPostModel.totalItems - LAST_FEED_POSITION
            holder.bindOthers(
                extraProduct,
                gridPostModel.actionText,
                gridPostModel.actionLink,
                gridPostModel.postId,
                gridPostModel.postType,
                gridPostModel.isFollowed,
                gridPostModel.shopId,
                gridPostModel.hasVoucher
            )

        } else if (gridPostModel.showGridButton
            && gridPostModel.totalItems < MAX_FEED_SIZE
            && gridPostModel.totalItems > MAX_FEED_SIZE_SMALL
            && position == LAST_FEED_POSITION_SMALL
        ) {
            val extraProduct = gridPostModel.totalItems - LAST_FEED_POSITION_SMALL
            holder.bindOthers(
                extraProduct,
                gridPostModel.actionText,
                gridPostModel.actionLink,
                gridPostModel.postId,
                gridPostModel.postType,
                gridPostModel.isFollowed,
                gridPostModel.shopId,
                gridPostModel.hasVoucher
            )

        } else {
            holder.bindProduct(
                gridPostModel.postId,
                gridPostModel.itemList[position],
                gridPostModel.postType,
                gridPostModel.isFollowed,
                gridPostModel.shopId,
                gridPostModel.hasVoucher
            )
        }
    }

    class GridItemViewHolder(
        val v: View,
        private val positionInFeed: Int,
        private val contentPosition: Int,
        private val listener: GridItemListener, private val gridPostModel: GridPostModel
    ) : RecyclerView.ViewHolder(v) {

        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val extraProduct: Typography = itemView.findViewById(R.id.extraProduct)
        private val priceText: Typography = itemView.findViewById(R.id.priceText)
        private val tagTypeText: Typography = itemView.findViewById(R.id.tagTypeText)
        private val textSlashedPrice: Typography = itemView.findViewById(R.id.textSlashedPrice)
        private val productLayout: ConstraintLayout = itemView.findViewById(R.id.productLayout)

        fun bindImage(image: String, listSize: Int) {
            productImage.loadImageRounded(image, RAD_10f)
            setImageMargins(listSize)
        }

        fun bindProduct(
            postId: String,
            item: GridItemModel,
            type: String,
            isFollowed: Boolean,
            shopId: String,
            hasVoucher: Boolean
        ) {
            extraProduct.background = null
            extraProduct.hide()

            priceText.text = item.price

            item.tagsList.firstOrNull()?.let {
                when (it.type) {
                    TYPE_CASHBACK -> {
                        setCashBackTypeTag(tagTypeText, it.text)
                        textSlashedPrice.hide()
                    }
                    TYPE_DISCOUNT -> {
                        setDiscountTypeTag(tagTypeText, it.text)
                        setSlashedPriceText(textSlashedPrice, item.priceOriginal)
                    }
                    else -> {
                        tagTypeText.hide()
                        textSlashedPrice.hide()
                    }
                }
            }

            itemView.setOnClickListener {
                listener.onGridItemClick(
                    positionInFeed,
                    postId,
                    item.id,
                    item.redirectLink,
                    type,
                    isFollowed,
                    shopId,
                    hasVoucher,
                    gridPostModel.itemListFeedXProduct,
                    item.index
                )
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
                background =
                    MethodChecker.getDrawable(itemView.context, R.drawable.discount_text_background)
                setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    )
                )
                show()
            }

        }

        private fun setCashBackTypeTag(tagTypeText: Typography?, typeText: String) {
            tagTypeText?.run {
                text = typeText
                background =
                    MethodChecker.getDrawable(itemView.context, R.drawable.cashback_text_background)
                setTextColor(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                show()
            }

        }

        fun bindOthers(
            numberOfExtraProduct: Int,
            actionText: String,
            actionLink: String,
            postId: String,
            type: String,
            isFollowed: Boolean,
            shopId: String,
            hasVoucher: Boolean
        ) {
            val extra = "+$numberOfExtraProduct $actionText"
            extraProduct.background = ColorDrawable(
                MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
                )
            )
            extraProduct.show()
            extraProduct.text = extra

            priceText.hide()

            val function: (v: View) -> Unit = {
                listener.onGridItemClick(
                    positionInFeed,
                    postId,
                    "0",
                    if (!TextUtils.isEmpty(actionLink)) actionLink
                    else ApplinkConst.FEED_DETAILS.replace(EXTRA_DETAIL_ID, postId.toString()),
                    type,
                    isFollowed,
                    shopId,
                    hasVoucher,
                    gridPostModel.itemListFeedXProduct,
                    0
                )
            }
            itemView.setOnClickListener(function)
        }

        private fun setImageMargins(listSize: Int) {
            if (listSize == 1) {
                val dimens16 = itemView.getDimens(unifyR.dimen.spacing_lvl4)

                productLayout.setMargin(0, 0, 0, 0)
                tagTypeText.setPadding(
                    dimens16,
                    0,
                    dimens16,
                    0
                )
                priceText.setPadding(
                    dimens16,
                    0,
                    dimens16,
                    0
                )
            } else {
                val dimens2 = itemView.getDimens(unifyR.dimen.spacing_lvl1)
                val dimens4 = itemView.getDimens(unifyR.dimen.spacing_lvl2)

                productLayout.setMargin(
                    dimens2,
                    0,
                    dimens2,
                    0
                )
                tagTypeText.setPadding(
                    dimens4,
                    0,
                    dimens4,
                    0
                )
                priceText.setPadding(
                    dimens4,
                    0,
                    dimens4,
                    0
                )
            }
        }
    }

    interface GridItemListener {
        fun onGridItemClick(
            positionInFeed: Int, activityId: String, productId: String,
            redirectLink: String, type: String, isFollowed: Boolean,
            shopId: String, hasVoucher: Boolean,
            feedXProducts: List<FeedXProduct>,
            index: Int
        )

        fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean)
    }

    companion object {
        var isMute = true
    }
}
