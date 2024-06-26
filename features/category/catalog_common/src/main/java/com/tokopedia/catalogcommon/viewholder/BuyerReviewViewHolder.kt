package com.tokopedia.catalogcommon.viewholder

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.ItemProductImageReviewAdapter
import com.tokopedia.catalogcommon.databinding.ItemBuyerReviewBinding
import com.tokopedia.catalogcommon.databinding.WidgetBuyerReviewBinding
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel
import com.tokopedia.catalogcommon.util.getBoldSpan
import com.tokopedia.catalogcommon.util.getClickableSpan
import com.tokopedia.catalogcommon.util.getGreenColorSpan
import com.tokopedia.catalogcommon.util.setSpanOnText
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.JvmMediaLoader
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.reviewcommon.R as reviewcommonR

class BuyerReviewViewHolder(
    itemView: View,
    private val listener: BuyerReviewListener? = null
) : AbstractViewHolder<BuyerReviewUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_buyer_review
        private const val MAX_LINE = 3
        private const val MAX_LINE_WITHOUT_IMAGES = 8
        private const val MAX_REVIEW_INDICATOR = 14
    }

    private val binding: WidgetBuyerReviewBinding? by viewBinding()
    private var bindingBuyerReviewCardSlider: ItemBuyerReviewBinding? = null
    private var carouselBuyerReview: CarouselUnify? = null
    private var widgetTitle: Typography? = null
    private var carouselData: ArrayList<Any>? = null
    private var cardData: BuyerReviewUiModel? = null

    init {
        widgetTitle = binding?.tgpBuyerReviewTitle
        carouselBuyerReview = binding?.carouselBuyerReview
        carouselBuyerReview?.apply {
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            infinite = true
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    binding?.pcIndicator?.setCurrentIndicator(current)
                }
            }
        }
    }

    override fun bind(element: BuyerReviewUiModel) {
        val itemListener = { view: View, data: Any ->
            bindingBuyerReviewCardSlider = ItemBuyerReviewBinding.bind(view)
            val shopIcon: ImageUnify? = bindingBuyerReviewCardSlider?.cardBrShopIcon
            val shopName: Typography? = bindingBuyerReviewCardSlider?.cardBrShopName
            val avatar: ImageUnify? = bindingBuyerReviewCardSlider?.cardBrReviewerAvatar
            val reviewerName: Typography? = bindingBuyerReviewCardSlider?.cardBrReviewerName
            val reviewerStatus: Typography? = bindingBuyerReviewCardSlider?.cardBrReviewerStatus
            val separatorStatus: Typography? = bindingBuyerReviewCardSlider?.cardBrSeparatorStatus
            val totalReview: Typography? = bindingBuyerReviewCardSlider?.cardBrTotalReview
            val separatorTotalReview: Typography? = bindingBuyerReviewCardSlider?.cardBrSeparatorTotalHelp
            val reviewTotalHelp: Typography? = bindingBuyerReviewCardSlider?.cardBrReviewTotalHelp
            val rating = bindingBuyerReviewCardSlider?.cardBrProductRating
            val ratingInactive = bindingBuyerReviewCardSlider?.cardBrProductRatingInactive
            val separatorRating: Typography? = bindingBuyerReviewCardSlider?.cardBrSeparatorRating
            val variant: Typography? = bindingBuyerReviewCardSlider?.cardBrVariant
            val separatorTime: Typography? = bindingBuyerReviewCardSlider?.cardBrSeparatorTimestamp
            val dateTime: Typography? = bindingBuyerReviewCardSlider?.cardBrDatetime
            val review: Typography? = bindingBuyerReviewCardSlider?.cardBrProductReview
            val rvImagesReview: RecyclerView? = bindingBuyerReviewCardSlider?.rvImgReview
            val carouselItem = data as BuyerReviewUiModel.ItemBuyerReviewUiModel
            var isTotalCompleteReviewEmpty = true

            setupReviewCardColor(element, bindingBuyerReviewCardSlider)

            shopIcon?.loadImage(carouselItem.shopIcon)
            shopName?.text = carouselItem.shopName
            reviewerName?.text = carouselItem.reviewerName

            avatar?.let {
                JvmMediaLoader.loadImageFitCenter(it, carouselItem.avatar)
            }

            carouselItem.reviewerStatus?.let { status ->
                if (status.isNotEmpty()) {
                    reviewerStatus?.text = status
                    reviewerStatus?.visibility = View.VISIBLE
                    separatorStatus?.visibility = View.VISIBLE
                } else {
                    reviewerStatus?.visibility = View.GONE
                    separatorStatus?.visibility = View.GONE
                }
            }

            carouselItem.totalCompleteReview?.let { total ->
                if (total.isMoreThanZero()) {
                    totalReview?.text = "$total ulasan lengkap"
                    isTotalCompleteReviewEmpty = false
                } else {
                    separatorTotalReview?.visibility = View.GONE
                    isTotalCompleteReviewEmpty = true
                }
            }

            carouselItem.totalHelpedPeople?.let { totalNumber ->
                if (totalNumber.isMoreThanZero()) {
                    reviewTotalHelp?.text = "$totalNumber terbantu"
                    reviewTotalHelp?.visibility = View.VISIBLE
                    if (!isTotalCompleteReviewEmpty) separatorTotalReview?.visibility = View.VISIBLE
                } else {
                    reviewTotalHelp?.visibility = View.GONE
                    separatorTotalReview?.visibility = View.GONE
                }
            }

            val starDrawable = MethodChecker.getDrawable(itemView.context, reviewcommonR.drawable.review_ic_rating_star_five)
            val starInactiveDrawable = MethodChecker.getDrawable(itemView.context, reviewcommonR.drawable.review_ic_rating_star_zero)
            val starActiveBitmap = createRatingBitmap(starDrawable, carouselItem.rating)
            rating?.setImageBitmap(starActiveBitmap)
            ratingInactive?.setImageDrawable(starInactiveDrawable)

            carouselItem.variantName?.let {
                variant?.text = it
                variant?.isVisible = it.isNotEmpty()
                separatorRating?.isVisible = it.isNotEmpty()
            }

            carouselItem.datetime?.let {
                dateTime?.visibility = View.VISIBLE
                dateTime?.text = it
                separatorTime?.visibility = View.VISIBLE
            }

            rvImagesReview?.apply {
                adapter = ItemProductImageReviewAdapter(carouselItem.images) { position ->
                    listener?.onClickImage(carouselItem, position)
                }
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            setupSelengkapnya(carouselItem, review)
        }

        cardData = element
        carouselData = dataWidgetToCarouselData(element)
        binding?.pcIndicator?.setMaximumDisplayedIndicator(MAX_REVIEW_INDICATOR)
        binding?.pcIndicator?.setIndicator(carouselData?.size.orZero())
        binding?.pcIndicator?.isVisible = carouselData?.isNotEmpty().orFalse()
        carouselBuyerReview?.apply {
            stage.removeAllViews()
            carouselData?.let {
                if (stage.childCount.isZero()) {
                    addItems(R.layout.item_buyer_review, it, itemListener)
                    postDelayed({
                        activeIndex = Int.ZERO
                    }, 100)
                }
            }
        }
        widgetTitle?.apply {
            setTextColor(element.widgetTextColor.orZero())
            if (element.title.isEmpty()) {
                hide()
            } else {
                text = element.title
                show()
            }
        }
        listener?.onBuyerReviewImpression(element)
    }

    private fun createRatingBitmap(drawable: Drawable, value: Float): Bitmap {
        val bitmap = Bitmap.createBitmap(
            if (!drawable.intrinsicWidth.isMoreThanZero()) Int.ONE else drawable.intrinsicWidth,
            if (!drawable.intrinsicHeight.isMoreThanZero()) Int.ONE else drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(Int.ZERO, Int.ZERO, canvas.width, canvas.height)
        drawable.draw(canvas)
        return Bitmap.createBitmap(
            bitmap,
            Int.ZERO,
            Int.ZERO,
            (drawable.intrinsicWidth * (value / 5f)).toInt(),
            drawable.intrinsicHeight
        )
    }

    private fun setupReviewCardColor(
        element: BuyerReviewUiModel,
        bindingBuyerReviewCardSlider: ItemBuyerReviewBinding?
    ) {
        bindingBuyerReviewCardSlider?.apply {
            buyerReviewCard.background = MethodChecker.getDrawable(
                itemView.context,
                if (element.darkMode) {
                    R.drawable.bg_rounded_border_dark
                } else {
                    R.drawable.bg_rounded_border_light
                }
            )
            val textColor = MethodChecker.getColor(
                itemView.context,
                if (element.darkMode) {
                    R.color.dms_static_text_color_dark
                } else {
                    R.color.dms_static_text_color_light
                }
            )
            val textColorSmall = MethodChecker.getColor(
                itemView.context,
                if (element.darkMode) {
                    R.color.dms_static_text_color_dark
                } else {
                    R.color.dms_static_text_color_small_light
                }
            )
            cardBrShopName.setTextColor(textColor)
            cardBrReviewerName.setTextColor(textColor)
            cardBrProductReview.setTextColor(textColor)
            llAdditionalInfo.children.forEach {
                (it as? Typography)?.setTextColor(textColorSmall)
            }
            llUserReputation.children.forEach {
                (it as? Typography)?.setTextColor(textColorSmall)
            }
            cardBrSeparatorStatus.setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    R.color.dms_static_divider
                )
            )
            cardBrSeparatorTotalHelp.setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    R.color.dms_static_divider
                )
            )
            divShopInfo.setBackgroundColor(
                MethodChecker.getColor(
                    itemView.context,
                    R.color.dms_static_color_divider
                )
            )
            cardBrShopInfoPrefix.setTextColor(textColorSmall)

            if (element.darkMode) {
                cardBrProductRating.setColorFilter(MethodChecker.getColor(itemView.context, R.color.dms_static_star_dark))
                cardBrProductRatingInactive.setColorFilter(MethodChecker.getColor(itemView.context, R.color.dms_static_star_inactive_dark))
            }
        }
    }

    private fun dataWidgetToCarouselData(element: BuyerReviewUiModel): ArrayList<Any> {
        val mutableString: ArrayList<Any> = ArrayList()
        element.items.map { elementItem ->
            mutableString.add(
                elementItem
            )
        }
        return mutableString
    }

    private fun setupSelengkapnya(
        carouselItem: BuyerReviewUiModel.ItemBuyerReviewUiModel,
        reviewTypography: Typography?
    ) {
        val maxLine = if (carouselItem.images.isNotEmpty()) MAX_LINE else MAX_LINE_WITHOUT_IMAGES
        reviewTypography?.setLines(maxLine)
        reviewTypography?.text = carouselItem.description
        reviewTypography?.post {
            val lineCount = reviewTypography.lineCount
            val reviewDesc = carouselItem.description
            val formattedReviewText = SpannableStringBuilder()
            val highlightedText = itemView.context.getString(R.string.text_selengkapnya)

            if (lineCount > maxLine) {
                val charCountPerLine = reviewDesc.count() / lineCount
                val sanitizedText = reviewDesc.take((charCountPerLine * maxLine) - highlightedText.count())
                val trimmedText = if (sanitizedText.endsWith("")) {
                    sanitizedText.trimEnd()
                } else {
                    sanitizedText
                }
                formattedReviewText.append(
                    itemView.context.getString(
                        R.string.text_selengkapnya_template,
                        trimmedText
                    )
                )

                formattedReviewText.setSpanOnText(
                    highlightedText,
                    getClickableSpan {
                        listener?.onClickSeeMore(carouselItem)
                    },
                    getBoldSpan(),
                    getGreenColorSpan(itemView.context)
                )

                reviewTypography.text = formattedReviewText
                reviewTypography.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    interface BuyerReviewListener {
        fun onClickSeeMore(carouselItem: BuyerReviewUiModel.ItemBuyerReviewUiModel)
        fun onClickImage(carouselItem: BuyerReviewUiModel.ItemBuyerReviewUiModel, position: Int)
        fun onBuyerReviewImpression(buyerReviewUiModel: BuyerReviewUiModel)
    }
}
