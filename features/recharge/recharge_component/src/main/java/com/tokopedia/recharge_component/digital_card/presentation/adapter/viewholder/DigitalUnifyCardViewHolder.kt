package com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.data.PLACEHOLDER_RES_UNIFY
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ItemDigitalUnifyCardBinding
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardRatingModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardSoldPercentageModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyConst
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel
import com.tokopedia.unifycomponents.ProgressBarUnify

class DigitalUnifyCardViewHolder(
    private val binding: ItemDigitalUnifyCardBinding,
    private val listener: DigitalUnifyCardListener?
) : AbstractViewHolder<DigitalUnifyModel>(binding.root) {

    override fun bind(element: DigitalUnifyModel) {

        listener?.onItemImpression(element, adapterPosition)

        renderMedia(element)
        renderCampaign(element)
        renderProductInfo(element)
        renderTitle(element)
        renderRating(element)
        renderSpecialInfo(element)
        renderPriceAndDiscount(element)
        renderSubtitle(element)
        renderSoldPercentage(element)
        renderActionButton(element)

        with(binding.root) {
            setOnClickListener {
                listener?.onItemClicked(element, adapterPosition)
            }
        }

        applyCarousel()
    }

    fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        with(binding) {
            val layoutParams = cardViewProductCard.layoutParams
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            cardViewProductCard.layoutParams = layoutParams
        }
    }

    private fun renderMedia(element: DigitalUnifyModel) {
        renderMediaImage(element.mediaUrl, element.getMediaTypeRatio())
        renderLabelOnImage(element.mediaTitle)
        renderIcon(element.iconUrl)
        renderIconBackground(element.iconBackgroundColor)
    }

    private fun renderCampaign(element: DigitalUnifyModel) {
        renderCampaignBackground(element.campaign.backgroundUrl)
        renderCampaignText(element.campaign.text, element.campaign.textColor)
    }

    private fun renderProductInfo(element: DigitalUnifyModel) {
        // setup left product info
        val leftProductInfoText: Spannable =
            SpannableString(MethodChecker.fromHtml(element.productInfoLeft.text).toString())
        val leftProductInfoColor = try {
            Color.parseColor(element.productInfoLeft.textColor)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            null
        }
        leftProductInfoColor?.let {
            leftProductInfoText.setSpan(
                ForegroundColorSpan(it),
                0,
                leftProductInfoText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // setup right product info
        val rightProductInfoText =
            SpannableString(MethodChecker.fromHtml(element.productInfoRight.text).toString())
        val rightProductInfoColor = try {
            Color.parseColor(element.productInfoRight.textColor)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            null
        }
        rightProductInfoColor?.let {
            rightProductInfoText.setSpan(
                ForegroundColorSpan(it),
                0,
                rightProductInfoText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val fullText = TextUtils.concat(leftProductInfoText, " ", rightProductInfoText)

        with(binding.dguProductInfo) {
            if (leftProductInfoText.isNotEmpty() || rightProductInfoText.isNotEmpty()) {
                text = fullText

                ellipsize = TextUtils.TruncateAt.END
                maxLines = 1

                show()
            } else {
                hide()
            }
        }
    }

    private fun renderTitle(element: DigitalUnifyModel) {
        with(binding.dguTitleLabel) {
            if (element.title.isNotEmpty()) {
                text = MethodChecker.fromHtml(element.title)
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderRating(element: DigitalUnifyModel) {
        if (element.rating.ratingType != null) {
            when (element.rating.ratingType) {
                DigitalUnifyConst.RATING_TYPE_SQUARE -> {
                    renderSquareRating(element.rating)
                    hideStarRating()
                }
                DigitalUnifyConst.RATING_TYPE_STAR -> {
                    renderStarRating(element.rating)
                    hideSquareRating()
                }
                else -> {
                    hideStarRating()
                    hideSquareRating()
                }
            }
        }
    }

    private fun renderSpecialInfo(element: DigitalUnifyModel) {
        with(binding.dguSpecialInfo) {
            if (element.specialInfo.text.isNotEmpty()) {
                try {
                    setTextColor(
                        Color.parseColor(element.specialInfo.textColor)
                    )
                } catch (t: Throwable) {
                    t.printStackTrace()
                }

                text = element.specialInfo.text
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderPriceAndDiscount(element: DigitalUnifyModel) {
        renderPrice(
            element.priceData.price,
            element.priceData.pricePrefix,
            element.priceData.priceSuffix
        )
        renderDiscountLabel(element.priceData.discountLabel, element.priceData.discountLabelType)
        renderSlashedPrice(element.priceData.discountType, element.priceData.slashedPrice)
    }

    private fun renderSubtitle(element: DigitalUnifyModel) {
        with(binding.dguSubtitle) {
            if (element.subtitle.isNotEmpty()) {
                text = MethodChecker.fromHtml(element.subtitle)
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderSoldPercentage(element: DigitalUnifyModel) {
        renderSoldProgressBar(element.soldPercentage)
        renderSoldLabel(element.soldPercentage)
    }

    private fun renderActionButton(element: DigitalUnifyModel) {
        with(binding.dguActionButton) {
            if (element.actionButton.text.isNotEmpty()) {
                text = element.actionButton.text
                buttonType = element.actionButton.getUnifyButtonType()

                setOnClickListener {
                    listener?.onItemClicked(element, adapterPosition)
                    RouteManager.route(context, element.actionButton.applink)
                }
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderMediaImage(mediaUrl: String, mediaRatio: String) {
        with(binding.dguMediaImage) {
            if (mediaUrl.isNotEmpty()) {
                val mlayoutParams = layoutParams as ConstraintLayout.LayoutParams
                mlayoutParams.dimensionRatio = mediaRatio
                layoutParams = mlayoutParams
                loadImage(mediaUrl)
                show()
            } else {
                setImageResource(PLACEHOLDER_RES_UNIFY)
                show()
            }
        }
    }

    private fun renderLabelOnImage(label: String) {
        with(binding.dguLabelOnImage) {
            if (label.isNotEmpty()) {
                text = label
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderIcon(iconUrl: String) {
        with(binding) {
            if (iconUrl.isNotEmpty()) {
                dguIconImage.loadImage(iconUrl)
                dguIconImage.show()
                dguContainerIconImage.show()
            } else {
                dguIconImage.hide()
                dguContainerIconImage.hide()
            }
        }
    }

    private fun renderIconBackground(iconBackgroundColor: String) {
        with(binding.dguIconBackground) {
            if (iconBackgroundColor.isNotEmpty()) {
                try {
                    (background as? GradientDrawable)?.setColor(
                        Color.parseColor(iconBackgroundColor)
                    )
                    show()
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                    hide()
                }
            } else {
                hide()
            }
        }
    }

    private fun renderCampaignBackground(backgroundUrl: String) {
        with(binding.dguCampaignImageBackground) {
            if (backgroundUrl.isNotEmpty()) {
                loadImage(backgroundUrl)
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderCampaignText(label: String, textColor: String) {
        with(binding.dguCampaignLabel) {
            if (label.isNotEmpty()) {
                text = label

                try {
                    setTextColor(Color.parseColor(textColor))
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }

                show()
            } else {
                hide()
            }
        }
    }

    private fun renderStarRating(rating: DigitalCardRatingModel) {
        with(binding) {
            // render rating
            if (rating.rating > 0) {
                dguReviewStarRatingValue.text = rating.rating.toString()
                dguReviewStarRatingValue.show()
                dguReviewStarIcon.show()
            } else {
                dguReviewStarRatingValue.hide()
                dguReviewStarIcon.hide()
            }

            // render review
            if (rating.review.isNotEmpty()) {
                dguReviewStarReviewValue.text = rating.review
                dguReviewStarReviewValue.show()
            } else {
                dguReviewStarReviewValue.hide()
            }
        }
    }

    private fun hideStarRating() {
        with(binding) {
            dguReviewStarIcon.hide()
            dguReviewStarRatingValue.hide()
            dguReviewStarReviewValue.hide()
        }
    }

    private fun renderSquareRating(rating: DigitalCardRatingModel) {
        with(binding) {
            // render rating
            if (rating.rating > 0) {
                dguReviewSquareRatingValue.text = rating.rating.toString()
                dguReviewSquareRatingValue.show()
            } else {
                dguReviewSquareRatingValue.hide()
            }

            // render review
            if (rating.review.isNotEmpty()) {
                dguReviewSquareReviewValue.text = rating.review
                dguReviewSquareReviewValue.show()
            } else {
                dguReviewSquareReviewValue.hide()
            }
        }
    }

    private fun hideSquareRating() {
        with(binding) {
            dguReviewSquareRatingValue.hide()
            dguReviewSquareReviewValue.hide()
        }
    }

    private fun renderPrice(price: String, prefix: String, suffix: String) {
        with(binding) {
            // render price
            if (price.isNotEmpty()) {
                dguPriceValue.text = price
                dguPriceValue.show()
            } else {
                dguPriceValue.hide()
            }

            // render prefix
            if (prefix.isNotEmpty()) {
                dguPricePrefix.text = prefix
                dguPricePrefix.show()
            } else {
                dguPricePrefix.hide()
            }

            // render suffix
            if (suffix.isNotEmpty()) {
                dguPriceSuffix.text = suffix
                dguPriceSuffix.show()
            } else {
                dguPriceSuffix.hide()
            }
        }
    }

    private fun renderDiscountLabel(label: String, type: Int) {
        with(binding.dguDiscountLabel) {
            if (label.isNotEmpty()) {
                text = label
                setLabelType(type)
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderSlashedPrice(discountType: String, slashedPrice: String) {
        with(binding.dguDiscountSlashPrice) {
            if (slashedPrice.isNotEmpty()) {
                text = slashedPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderSoldProgressBar(soldPercentage: DigitalCardSoldPercentageModel) {
        with(binding.dguSoldPercentageBar) {
            if (soldPercentage.showPercentage) {
                setProgressIcon(
                    icon = if (soldPercentage.value >= MIN_PROGRESS_TO_SHOW_FIRE) {
                        ContextCompat.getDrawable(
                            context,
                            com.tokopedia.resources.common.R.drawable.ic_fire_filled_product_card
                        )
                    } else null,
                    width = context.resources.getDimension(R.dimen.digital_card_progress_fire_icon_width)
                        .toInt(),
                    height = context.resources.getDimension(R.dimen.digital_card_progress_fire_icon_height)
                        .toInt()
                )
                progressBarColorType = ProgressBarUnify.COLOR_RED
                setValue(soldPercentage.value, false)
                show()
            } else {
                hide()
            }
        }
    }

    private fun renderSoldLabel(soldPercentage: DigitalCardSoldPercentageModel) {
        with(binding.dguSoldPercentageLabel) {
            if (soldPercentage.showPercentage) {
                text = soldPercentage.label

                try {
                    setTextColor(Color.parseColor(soldPercentage.labelColor))
                } catch (t: Throwable) {
                    t.printStackTrace()
                }

                show()
            } else {
                hide()
            }
        }
    }

    interface DigitalUnifyCardListener {
        fun onItemClicked(item: DigitalUnifyModel, index: Int)
        fun onItemImpression(item: DigitalUnifyModel, index: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_digital_unify_card

        private const val MIN_PROGRESS_TO_SHOW_FIRE = 76

    }

}