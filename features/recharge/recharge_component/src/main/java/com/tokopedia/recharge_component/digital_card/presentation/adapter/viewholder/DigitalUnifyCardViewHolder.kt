package com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.ItemDigitalUnifyCardBinding
import com.tokopedia.recharge_component.digital_card.presentation.model.*
import com.tokopedia.unifycomponents.ProgressBarUnify

class DigitalUnifyCardViewHolder(
    private val binding: ItemDigitalUnifyCardBinding,
    private val listener: DigitalUnifyCardListener?
) : AbstractViewHolder<DigitalUnifyModel>(binding.root) {

    override fun bind(element: DigitalUnifyModel) {
        // render media
        renderMedia(element)

        // render campaign
        renderCampaign(element)

        // render product info
        renderProductInfo(element)

        // render title
        renderTitle(element)

        // render rating
        renderRating(element)

        // render special info
        renderSpecialInfo(element)

        // render price and discount
        renderPriceAndDiscount(element)

        // render subtitle
        renderSubtitle(element)

        // render sold percentage
        renderSoldPercentage(element)

        // render action button
        renderActionButton(element)

        binding.root.addOnImpressionListener(element) {
            listener?.onItemBinding(element)
        }
    }

    private fun renderMedia(element: DigitalUnifyModel) {
        // render media image
        renderMediaImage(element.mediaUrl, element.mediaType)

        // render label on image
        renderLabelOnImage(element.mediaTitle)

        // render icon image
        renderIcon(element.iconUrl)

        // render icon background
        renderIconBackground(element.iconBackgroundColor)
    }

    private fun renderCampaign(element: DigitalUnifyModel) {
        // render campaign background
        renderCampaignBackground(element.campaign.backgroundUrl)

        // render campaign text
        renderCampaignText(element.campaign.text, element.campaign.textColor)
    }

    private fun renderProductInfo(element: DigitalUnifyModel) {
        // setup left product info
        val leftProductInfoText: Spannable = SpannableString(element.productInfoLeft.text)
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
        val rightProductInfoText: Spannable = SpannableString(element.productInfoRight.text)
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

        with(binding) {
            if (leftProductInfoText.isNotEmpty() || rightProductInfoText.isNotEmpty()) {
                dguProductInfo.text = leftProductInfoText
                dguProductInfo.append(rightProductInfoText)
                dguProductInfo.show()
            } else {
                dguProductInfo.hide()
            }
        }
    }

    private fun renderTitle(element: DigitalUnifyModel) {
        with(binding) {
            if (element.title.isNotEmpty()) {
                dguTitleLabel.text = element.title
                dguTitleLabel.show()
            } else {
                dguTitleLabel.hide()
            }
        }
    }

    private fun renderRating(element: DigitalUnifyModel) {
        if (element.rating.ratingType != null) {
            when (element.rating.ratingType) {
                RatingType.SQUARE -> {
                    renderSquareRating(element.rating)
                    hideStarRating()
                }
                RatingType.STAR -> {
                    renderStarRating(element.rating)
                    hideSquareRating()
                }
            }
        }
    }

    private fun renderSpecialInfo(element: DigitalUnifyModel) {
        with(binding) {
            if (element.specialInfo.text.isNotEmpty()) {
                try {
                    dguSpecialInfo.setTextColor(
                        Color.parseColor(element.specialInfo.textColor)
                    )
                } catch (t: Throwable) {
                    t.printStackTrace()
                }

                dguSpecialInfo.text = element.specialInfo.text
                dguSpecialInfo.show()
            } else {
                dguSpecialInfo.hide()
            }
        }
    }

    private fun renderPriceAndDiscount(element: DigitalUnifyModel) {
        // render price
        renderPrice(element.priceData.price)

        // render discount label
        renderDiscountLabel(element.priceData.discountLabel, element.priceData.discountLabelType)

        // render slashed price
        renderSlashedPrice(element.priceData.slashedPrice)
    }

    private fun renderSubtitle(element: DigitalUnifyModel) {
        with(binding) {
            if (element.subtitle.isNotEmpty()) {
                dguSubtitle.text = element.subtitle
                dguSubtitle.show()
            } else {
                dguSubtitle.hide()
            }
        }
    }

    private fun renderSoldPercentage(element: DigitalUnifyModel) {
        // render progress bar
        renderSoldProgressBar(element.soldPercentage)

        // render label
        renderSoldLabel(element.soldPercentage)
    }

    private fun renderActionButton(element: DigitalUnifyModel) {
        with(binding.dguActionButton) {
            if (element.actionButton.text.isNotEmpty()) {
                text = element.actionButton.text
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

    private fun renderMediaImage(mediaUrl: String, mediaType: MediaType) {
        with(binding) {
            if (mediaUrl.isNotEmpty()) {
                val layoutParams = dguMediaImage.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.dimensionRatio = mediaType.ratio
                dguMediaImage.layoutParams = layoutParams
                dguMediaImage.loadImage(mediaUrl)
                dguMediaImage.show()
            } else {
                dguMediaImage.hide()
            }
        }
    }

    private fun renderLabelOnImage(label: String) {
        with(binding) {
            if (label.isNotEmpty()) {
                dguLabelOnImage.text = label
                dguLabelOnImage.show()
            } else {
                dguLabelOnImage.hide()
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
        with(binding) {
            if (iconBackgroundColor.isNotEmpty()) {
                try {
                    (dguIconBackground.background as? GradientDrawable)?.setColor(
                        Color.parseColor(iconBackgroundColor)
                    )
                    dguIconBackground.show()
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                    dguIconBackground.hide()
                }
            } else {
                dguIconBackground.hide()
            }
        }
    }

    private fun renderCampaignBackground(backgroundUrl: String) {
        with(binding) {
            if (backgroundUrl.isNotEmpty()) {
                dguCampaignImageBackground.loadImage(backgroundUrl)
                dguCampaignImageBackground.show()
            } else {
                dguCampaignImageBackground.hide()
            }
        }
    }

    private fun renderCampaignText(text: String, textColor: String) {
        with(binding) {
            if (text.isNotEmpty()) {
                dguCampaignLabel.text = text

                try {
                    dguCampaignLabel.setTextColor(Color.parseColor(textColor))
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }

                dguCampaignLabel.show()
            } else {
                dguCampaignLabel.hide()
            }
        }
    }

    private fun renderStarRating(rating: DigitalCardRatingModel) {
        with(binding) {
            // render rating
            if (rating.rating > 0.0) {
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
            if (rating.rating > 0.0) {
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

    private fun renderPrice(price: String) {
        with(binding) {
            if (price.isNotEmpty()) {
                dguPriceValue.text = price
                dguPriceValue.show()
            } else {
                dguPriceValue.hide()
            }
        }
    }

    private fun renderDiscountLabel(label: String, type: Int) {
        with(binding) {
            if (label.isNotEmpty()) {
                dguDiscountLabel.text = label
                dguDiscountLabel.setLabelType(type)
                dguDiscountLabel.show()
            } else {
                dguDiscountLabel.hide()
            }
        }
    }

    private fun renderSlashedPrice(slashedPrice: String) {
        with(binding) {
            if (slashedPrice.isNotEmpty()) {
                dguDiscountSlashPrice.text = slashedPrice
                dguDiscountSlashPrice.paintFlags =
                    dguDiscountSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                dguDiscountSlashPrice.show()
            } else {
                dguDiscountSlashPrice.hide()
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
        fun onItemBinding(item: DigitalUnifyModel)
        fun onItemClicked(item: DigitalUnifyModel, index: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_digital_unify_card

        private const val MIN_PROGRESS_TO_SHOW_FIRE = 76

    }

}