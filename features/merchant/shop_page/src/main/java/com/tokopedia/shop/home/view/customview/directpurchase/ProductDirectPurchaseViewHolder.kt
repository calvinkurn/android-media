package com.tokopedia.shop.home.view.customview.directpurchase

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.ColorPallete
import com.tokopedia.common.customview.ColorVariantLinearLayout
import com.tokopedia.common.setRetainBackgroundColor
import com.tokopedia.common.setRetainCardBackgroundColor
import com.tokopedia.common.setRetainColorFilter
import com.tokopedia.common.setRetainTextColor
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_BORDER
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.viewallcard.ViewAllCard
import com.tokopedia.viewallcard.ViewAllCard.Companion.MODE_INVERT
import com.tokopedia.viewallcard.ViewAllCard.Companion.MODE_NORMAL


class ProductDirectPurchaseViewHolder private constructor() {

    interface ProductDirectPurchaseContentVHListener {

        fun onAddButtonProductDirectPurchaseClick(data: ProductCardDirectPurchaseDataModel)
        fun onProductDirectPurchaseClick(data: ProductCardDirectPurchaseDataModel)
    }

    interface ProductDirectPurchaseErrorVHListener {

        fun onRetryClick()

    }

    interface ProductDirectPurchaseSeeMoreVHListener {

        fun onSeeMoreClick()

    }


    companion object {
        fun bind(
            vh: RecyclerView.ViewHolder,
            model: ProductCarouselDirectPurchaseAdapter.Model,
            colorPallete: ColorPallete?
        ) {
            when (vh) {
                is ContentVH -> vh.bind(
                    (model as ProductCarouselDirectPurchaseAdapter.Model.Content).data,
                    colorPallete
                )

                is EmptyVH -> vh.bind(model as ProductCarouselDirectPurchaseAdapter.Model.Empty)
                is LoadingVH -> vh.bind(model as ProductCarouselDirectPurchaseAdapter.Model.Loading)
                is ErrorVH -> vh.bind(model as ProductCarouselDirectPurchaseAdapter.Model.Error)
                is SeeMoreVH -> vh.bind(model as ProductCarouselDirectPurchaseAdapter.Model.SeeMore)
                else -> {
                    // noop
                }
            }
        }

        fun createEmptyStateView(ctx: Context, colorPallete: ColorPallete?): EmptyStateUnify {
            return EmptyStateUnify(ctx).apply {
                layoutParams =
                    LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    )
                emptyStateCTAFullWidth = false
                emptyStateOrientation = EmptyStateUnify.Orientation.VERTICAL
                emptyStateType = EmptyStateUnify.Type.SECTION
                findViewById<Typography>(com.tokopedia.empty_state.R.id.empty_state_title_id)?.visibility =
                    View.GONE
                (findViewById<Typography>(com.tokopedia.empty_state.R.id.empty_state_description_id) as? TextView)?.setRetainTextColor(
                    colorPallete,
                    ColorPallete.ColorType.PRIMARY_TEXT
                )
            }
        }
    }

    class ContentVH(
        val itemView: View,
        val colorPallete: ColorPallete?,
        private val listener: ProductDirectPurchaseContentVHListener
    ) : RecyclerView.ViewHolder(itemView) {
        val ivProduct: ImageUnify = itemView.findViewById(com.tokopedia.shop.R.id.iv_product)
        val addButtonView = itemView.findViewById<CardView>(com.tokopedia.shop.R.id.add_button)
        val plusImage = itemView.findViewById<AppCompatImageView>(com.tokopedia.shop.R.id.ivplus)
        val llColor: ColorVariantLinearLayout =
            itemView.findViewById(com.tokopedia.shop.R.id.ll_variant_color)
        val tvProductName = itemView.findViewById<Typography>(com.tokopedia.shop.R.id.tvProductName)
        val tvPrice = itemView.findViewById<Typography>(com.tokopedia.shop.R.id.tvPrice)
        val labelDiscount = itemView.findViewById<Label>(com.tokopedia.shop.R.id.labelDiscount)
        val tvSlashedPrice =
            itemView.findViewById<Typography>(com.tokopedia.shop.R.id.tvSlashedPrice)
        val iconRating = itemView.findViewById<View>(com.tokopedia.shop.R.id.iconRating)
        val tvRating = itemView.findViewById<Typography>(com.tokopedia.shop.R.id.tvRating)
        val tvSoldCount = itemView.findViewById<Typography>(com.tokopedia.shop.R.id.tvSoldCount)
        val tvDotSeparator = itemView.findViewById<Typography>(com.tokopedia.shop.R.id.tvDotSeparator)

        init {
            tvSlashedPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            plusImage.setRetainColorFilter(colorPallete, ColorPallete.ColorType.BLACK)
            addButtonView.setRetainCardBackgroundColor(
                colorPallete,
                ColorPallete.ColorType.WHITE
            )
            tvProductName.setRetainTextColor(colorPallete, ColorPallete.ColorType.PRIMARY_TEXT)
            tvPrice.setRetainTextColor(colorPallete, ColorPallete.ColorType.PRIMARY_TEXT)
            tvSlashedPrice.setRetainTextColor(colorPallete, ColorPallete.ColorType.PRIMARY_TEXT)
            tvRating.setRetainTextColor(colorPallete, ColorPallete.ColorType.PRIMARY_TEXT)
            tvSoldCount.setRetainTextColor(colorPallete, ColorPallete.ColorType.PRIMARY_TEXT)
        }

        companion object {

            private const val MAX_COLOR_VARIANT_SHOWN = 5
            fun create(
                parent: ViewGroup,
                colorPallete: ColorPallete?,
                listener: ProductDirectPurchaseContentVHListener
            ): ContentVH {
                val inflater = LayoutInflater.from(parent.context)
                return ContentVH(
                    inflater.inflate(
                        R.layout.item_product_card_direct_purchase,
                        parent,
                        false
                    ),
                    colorPallete,
                    listener
                )
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(data: ProductCardDirectPurchaseDataModel, colorPallete: ColorPallete?) {
            if (data.imageUrl.isEmpty()) {
                ivProduct.setImageUrl("")
            } else {
                ivProduct.setImageUrl(data.imageUrl)
            }
            tvProductName.text = data.name

            if (data.colorVariant.isNotEmpty()) {
                llColor.setColorString(data.colorVariant, MAX_COLOR_VARIANT_SHOWN, colorPallete)
                llColor.visibility = View.VISIBLE
            } else {
                llColor.visibility = View.GONE
            }
            tvPrice.text = data.price
            if (data.discount.isEmpty() || data.discount.toIntOrZero() == 0) {
                labelDiscount.visibility = View.GONE
            } else {
                labelDiscount.text = "${data.discount}%"
                labelDiscount.visibility = View.VISIBLE
            }
            if (data.slashPrice.isEmpty()) {
                tvSlashedPrice.visibility = View.GONE
            } else {
                tvSlashedPrice.text = data.slashPrice
                tvSlashedPrice.visibility = View.VISIBLE
            }
            if (data.ratingAverage.isEmpty()) {
                iconRating.hide()
                tvRating.hide()
            }
            else {
                tvRating.text = data.ratingAverage
            }
            if (data.label.isEmpty()) {
                tvSoldCount.hide()
            }
            else {
                tvSoldCount.text = data.ratingAverage
            }
            if(data.label.isEmpty() || data.ratingAverage.isEmpty()){
                tvDotSeparator.hide()
            } else {
                tvDotSeparator.show()
            }

            addButtonView.setOnClickListener { listener.onAddButtonProductDirectPurchaseClick(data) }

            itemView.setOnClickListener { listener.onProductDirectPurchaseClick(data) }
        }


    }

    class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(
                parent: ViewGroup
            ): LoadingVH {
                val inflater = LayoutInflater.from(parent.context)
                return LoadingVH(
                    inflater.inflate(
                        R.layout.item_product_card_direct_purchase_shimmer,
                        parent,
                        false
                    )
                )
            }

        }

        fun bind(model: ProductCarouselDirectPurchaseAdapter.Model.Loading) {
            // noop for loading
        }
    }

    @Suppress("DEPRECATION")
    class ErrorVH(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val emptyStateUnify = itemView as EmptyStateUnify

        companion object {
            fun create(
                parent: ViewGroup,
                colorPallete: ColorPallete?,
                listener: ProductDirectPurchaseErrorVHListener? = null
            ): ErrorVH {
                return ErrorVH(
                    createEmptyStateView(parent.context, colorPallete).apply {
                        emptyStateImageID.setImage(TokopediaImageUrl.ERROR_NETWORK_IMAGE, 0f)
                        setPrimaryCTAText(parent.context.getString(com.tokopedia.globalerror.R.string.authErrorAction))
                        setPrimaryCTAClickListener { listener?.onRetryClick() }
                    }
                )
            }
        }

        fun bind(model: ProductCarouselDirectPurchaseAdapter.Model.Error) {
            if (model.errorMessage.isEmpty()) {
                emptyStateUnify.setDescription(itemView.context.getString(com.tokopedia.globalerror.R.string.noConnectionDesc))
            } else {
                emptyStateUnify.setDescription(model.errorMessage)
            }
        }
    }

    @Suppress("DEPRECATION")
    class EmptyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(
                parent: ViewGroup,
                colorPallete: ColorPallete?
            ): EmptyVH {
                return EmptyVH(
                    createEmptyStateView(parent.context, colorPallete).apply {
                        emptyStateImageID.setImage(TokopediaImageUrl.PRODUCT_EMPTY, 0f)
                        setDescription(parent.context.getString(R.string.shop_product_empty_title_etalase_desc))
                    }
                )
            }

        }

        fun bind(model: ProductCarouselDirectPurchaseAdapter.Model.Empty) {
            // noop for empty model
        }
    }

    class SeeMoreVH(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            private const val SEE_MORE_WIDTH = 132
            private const val SEE_MORE_HEIGHT_RV = 346 // same with Recycler View height
            private const val SEE_MORE_HEIGHT_IMAGE = 250 // same with Recycler View height
            fun create(
                parent: ViewGroup,
                colorPallete: ColorPallete?,
                cardType: Int? = MODE_NORMAL,
                listener: ProductDirectPurchaseSeeMoreVHListener
            ): SeeMoreVH {
                return SeeMoreVH(
                    ViewAllCard(parent.context).apply {
                        layoutParams = LayoutParams(
                            SEE_MORE_WIDTH.toPx(),
                            LayoutParams.MATCH_PARENT
                        )
                        if (cardType == null || cardType != MODE_INVERT) {
                            mode = MODE_NORMAL
                            cardView.cardType = TYPE_BORDER
                            findViewById<TextView>(com.tokopedia.viewallcard.R.id.view_all_card_description)?.setRetainTextColor(
                                colorPallete,
                                ColorPallete.ColorType.PRIMARY_TEXT
                            )
                            findViewById<TextView>(com.tokopedia.viewallcard.R.id.view_all_card_cta)?.setRetainTextColor(
                                colorPallete,
                                ColorPallete.ColorType.BUTTON_ACCENT
                            )
                            findViewById<ImageView>(com.tokopedia.viewallcard.R.id.view_all_card_cta_icon)?.setRetainColorFilter(
                                colorPallete,
                                ColorPallete.ColorType.BUTTON_ACCENT
                            )
                        } else {
                            mode = MODE_INVERT
                        }

                        title = ""
                        description =
                            parent.context.getString(R.string.shop_page_see_more_product_desc)
                        setCta(parent.context.getString(R.string.shop_page_see_more)) {
                            listener.onSeeMoreClick()
                        }
                    }
                )
            }
        }

        fun bind(model: ProductCarouselDirectPurchaseAdapter.Model.SeeMore) {
            // noop
        }
    }

}
