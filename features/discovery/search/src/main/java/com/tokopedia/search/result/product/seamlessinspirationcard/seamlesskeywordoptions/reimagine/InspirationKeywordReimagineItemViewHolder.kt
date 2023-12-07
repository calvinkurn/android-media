package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationKeywordReimagineItemBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.dialog.R as dialogR

class InspirationKeywordReimagineItemViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val type: LayoutType,
    private val searchTerm: String,
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_keyword_reimagine_item
    }
    private var binding: SearchInspirationKeywordReimagineItemBinding? by viewBinding()

    fun bind(
        inspirationKeywordDataView: InspirationKeywordDataView,
    ) {
        val binding = binding ?: return
        setContainerView()
        setImage(inspirationKeywordDataView)
        setTitleKeyword(inspirationKeywordDataView)
        binding.root.setOnClickListener {
            inspirationKeywordListener.onInspirationKeywordItemClicked(inspirationKeywordDataView)
        }
    }

    private fun setContainerView() {
        val isIconKeyword = type.isIconKeyword()
        if(isIconKeyword) {
            containerIconReimagine()
        } else {
            containerImageReimagine()
        }
    }

    private fun containerImageReimagine() {
        val containerView = binding?.searchInspirationKeywordReimagineContainer
        val context = containerView?.context?:return
        val heightContainer = context.getHeightImageDimension()
        val marginToImage = context.getMarginLeftTextToImage()
        setMarginLeftTextKeyword(marginToImage)
        setContainerLayoutHeight(heightContainer)
        containerView.background = null
    }

    private fun containerIconReimagine() {
        val containerView = binding?.searchInspirationKeywordReimagineContainer
        val context = containerView?.context?:return
        val heightContainer = context.getHeightIconDimension()
        val marginToIcon = context.getMarginLeftTextToIcon()
        setMarginLeftTextKeyword(marginToIcon)
        setContainerLayoutHeight(heightContainer)
        containerView.background = ContextCompat.getDrawable(
            context,
            R.drawable.search_background_inspiration_keyword_reimagine)
    }

    private fun setMarginLeftTextKeyword(marginLeft: Int){
        val textView = binding?.searchInspirationKeywordReimagineKeyword
        val marginRight = textView?.context?.getMarginRight().orZero()
        textView?.setMargin(marginLeft, 0,marginRight,0)
    }

    private fun setContainerLayoutHeight(height: Int) {
        val containerView = binding?.searchInspirationKeywordReimagineContainer
        containerView?.setLayoutHeight(height)
    }

    private fun setImage(inspirationKeywordDataView: InspirationKeywordDataView) {
        setIconKeyword(inspirationKeywordDataView)
        setImageKeyword(inspirationKeywordDataView)
    }

    private fun setIconKeyword(inspirationKeywordDataView: InspirationKeywordDataView) {
        val iconKeyword = binding?.searchInspirationKeywordReimagineIcon
        iconKeyword?.shouldShowWithAction(type.isIconKeyword()){
            iconKeyword.loadImage(inspirationKeywordDataView.imageKeyword)
        }
    }

    private fun setImageKeyword(inspirationKeywordDataView: InspirationKeywordDataView) {
        val iconKeyword = binding?.searchInspirationKeywordReimagineImage
        iconKeyword?.shouldShowWithAction(!type.isIconKeyword()) {
            iconKeyword.apply {
                loadImageRounded(
                    inspirationKeywordDataView.imageKeyword,
                    8f.toPx()
                )
                setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.search_dms_inspiration_keyword_image_overlay,
                    ),
                    PorterDuff.Mode.SRC_OVER
                )
            }
        }
    }

    private fun setTitleKeyword(inspirationKeywordDataView: InspirationKeywordDataView) {
        val titleTextView = binding?.searchInspirationKeywordReimagineKeyword
        val isFunneling = type.isFunneling()
        titleTextView?.isSingleLine = type.isSingleLine()
        titleTextView?.text = inspirationKeywordDataView.keyword.generateTitleText(isFunneling)
    }

    private fun String.generateTitleText(isFunneling: Boolean): CharSequence {
        val startIndex = this.indexOf(searchTerm, ignoreCase = true)
        return if(!isFunneling || startIndex < 0) {
            this
        } else {
            this.toSpanTitle(startIndex)
        }
    }
    private fun String.toSpanTitle(startIndex: Int) : SpannableString {
        val textKeyword = this
        val highlightedTitle = SpannableString(textKeyword)
        highlightedTitle.setSpan(
            getBoldStyle(itemView.context),
            startIndex,
            startIndex + searchTerm.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return highlightedTitle
    }

    private fun getBoldStyle(context: Context?): Any {
        val typeface = context?.let { getTypeface(it, "OpenSauceOneExtraBold.ttf") }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && typeface != null)
            TypefaceSpan(typeface)
        else
            StyleSpan(Typeface.BOLD)
    }

    private fun Context.getMarginLeftTextToImage(): Int =
        this.resources?.getDimensionPixelSize(dialogR.dimen.unify_space_8).orZero()
    private fun Context.getMarginLeftTextToIcon(): Int =
        this.resources?.getDimensionPixelSize(dialogR.dimen.unify_space_4).orZero()
    private fun Context.getMarginRight(): Int? =
        this.resources?.getDimensionPixelSize(dialogR.dimen.unify_space_8)
    private fun Context.getHeightIconDimension(): Int =
        this.resources?.getDimensionPixelSize(dialogR.dimen.unify_space_40).orZero()
    private fun Context.getHeightImageDimension(): Int =
        this.resources?.getDimensionPixelSize(dialogR.dimen.unify_space_48).orZero()
}
