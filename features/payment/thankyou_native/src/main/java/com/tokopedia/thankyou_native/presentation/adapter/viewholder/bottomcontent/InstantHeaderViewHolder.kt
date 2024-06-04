package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankInstantHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.InstantHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_PLUS_BADGE
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isDeviceAnimationDisabled
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.thankyou_native.helper.radiusClip
import com.tokopedia.thankyou_native.presentation.views.listener.HeaderListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import com.tokopedia.utils.view.binding.viewBinding
import org.json.JSONArray
import timber.log.Timber

class InstantHeaderViewHolder(
    view: View,
    private val listener: HeaderListener
): AbstractViewHolder<InstantHeaderUiModel>(view) {

    private val binding: ThankInstantHeaderBinding? by viewBinding()
    private var hasAnimationStarted: Boolean = false

    override fun bind(data: InstantHeaderUiModel?) {
        if (data == null) return

        if (!hasAnimationStarted && !itemView.context.isDeviceAnimationDisabled()) {
            setupIcon(data)
        } else {
            binding?.headerIcon?.scaleX = 1f
            binding?.headerIcon?.scaleY = 1f
            binding?.headerIcon?.setImageUrl(data.gatewayImage)
        }
        binding?.headerTitle?.text = data.title
        binding?.headerDescription?.text = data.description
        binding?.headerTips?.shouldShowWithAction(data.note.isNotEmpty()) {
            binding?.headerTips?.description = data.note.joinToString("\n")
        }
        binding?.primaryButton?.shouldShowWithAction(!data.shouldHidePrimaryButton) {
            binding?.primaryButton?.text = data.primaryButtonText
        }
        binding?.secondaryButton?.shouldShowWithAction(!data.shouldHideSecondaryButton) {
            binding?.secondaryButton?.text = data.secondaryButtonText
        }
        binding?.cardContainer?.setOnClickListener {
            listener.onSeeDetailInvoice()
        }
        binding?.primaryButton?.setOnClickListener {
            listener.onButtonClick(data.primaryButtonApplink, data.primaryButtonType, true, data.primaryButtonText)
        }
        binding?.secondaryButton?.setOnClickListener {
            listener.onButtonClick(data.secondaryButtonApplink, data.secondaryButtonType, false, data.secondaryButtonText)
        }
        setUpTotalDeduction(data)
    }

    private fun setupIcon(data: InstantHeaderUiModel) {
        val checkDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.check_circle)

        binding?.headerIcon?.setImageDrawable(checkDrawable)
        binding?.headerIcon?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(UnifyMotion.T3)?.setInterpolator(UnifyMotion.EASE_OVERSHOOT)?.start()

        Glide.with(itemView.context).asBitmap().load(data.gatewayImage).into(object: CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val bitmapDrawable = BitmapDrawable(itemView.context.resources, resource)

                binding?.headerIcon?.animate()?.rotationY(90f)?.setInterpolator(UnifyMotion.LINEAR)?.setDuration(UnifyMotion.T3)?.withEndAction {
                    binding?.headerIcon?.setImageDrawable(bitmapDrawable)
                    binding?.headerIcon?.rotationY = 270f
                    binding?.headerIcon?.animate()?.rotationY(360f)?.setDuration(UnifyMotion.T3)?.setStartDelay(0)?.start()
                }?.setStartDelay(2000)?.start()
            }
            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })

        hasAnimationStarted = true
    }

    private fun setUpTotalDeduction(data: InstantHeaderUiModel?) {
        val stringList = mutableListOf<String>()

        try {
            val jsonArray = JSONArray(data?.promoFlags)

            for (i in 0 until jsonArray.length()) {
                val element = jsonArray.getString(i)
                stringList.add(element)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }


        binding?.shimmerView?.shouldShowWithAction(stringList.isNotEmpty() && data?.totalDeduction != 0f) {
            binding?.shimmerView?.radiusClip(8.toPx().toFloat(), 8.toPx().toFloat(), 8.toPx().toFloat(), 8.toPx().toFloat())
            binding?.plusBenefitIcon?.shouldShowWithAction(stringList.contains(PLUS)) {
                binding?.plusBenefitIcon?.loadImageWithoutPlaceholder(IMG_PLUS_BADGE)
            }
            binding?.etcBenefitText?.showWithCondition(stringList.contains(PLUS) && stringList.size > Int.ONE)
            itemView.context?.let {
                binding?.totalDeductionText?.text = HtmlLinkHelper(
                    it,
                    String.format(
                        it.getString(if (stringList.contains(PLUS)) R.string.thank_benefit_with_plus else R.string.thank_benefit_without_plus),
                        convertPriceValueToIdrFormat(data?.totalDeduction?.toLong() ?: 0, false)
                    )).spannedString
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_instant_header
        private const val PLUS = "plus"
    }
}
