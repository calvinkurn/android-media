package com.tokopedia.shop.score.detail_old.view.recyclerview

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemShopScoreDetailBinding
import com.tokopedia.shop.score.detail_old.view.model.ShopScoreDetailItem
import com.tokopedia.shop.score.detail_old.view.util.formatShopScore
import com.tokopedia.utils.view.binding.viewBinding


class ShopScoreDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val PACKAGE_NAME = "com.tokopedia.sellerapp"
        private const val MARKET_URI = "market://details?id="
    }
    
    private val context by lazy { itemView.context }

    private val binding: ItemShopScoreDetailBinding? by viewBinding()
    
    fun bind(data: ShopScoreDetailItem) {
        data.run {
            setTitle(title)
            setShopScoreDescription(description)
            setProgressBarColor(progressBarColor)
            setShopScoreValue(value)
            setShopScoreMaxValue(maxValue)
        }
    }

    private fun setTitle(title: String?) {
        binding?.titleShopScoreDetail?.text = title
    }

    private fun setShopScoreValue(value: Float) {
        binding?.run {
            descriptionShopScoreValue.text = value.formatShopScore()
            progressBarShopScoreDetail.progress = value
        }
    }

    private fun setShopScoreMaxValue(maxValue: Float) {
        binding?.run {
            val maxScore = context.getString(R.string.description_shop_score_percent,
                maxValue.formatShopScore())
            descriptionShopScoreValue.text = maxScore
            progressBarShopScoreDetail.max = maxValue
        }
    }

    private fun setShopScoreDescription(description: String?) {
        binding?.run {
            if (description != null) {
                val sequence: CharSequence = MethodChecker.fromHtml(description)
                val strBuilder = SpannableStringBuilder(sequence)
                val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
                for (span in urls) {
                    makeLinkClickable(strBuilder, span)
                }
                descriptionShopScoreDetail.text = strBuilder
                descriptionShopScoreDetail.movementMethod = LinkMovementMethod.getInstance()
                descriptionShopScoreDetail.visibility = View.VISIBLE
            } else {
                descriptionShopScoreDetail.visibility = View.GONE
            }
        }
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                if (span.url == PACKAGE_NAME) {
                    startNewActivity(span.url)
                } else {
                    openLink(span.url)
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    private fun startNewActivity(packageName: String) {
        var intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) { // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else { // Bring user to the market or let them choose an app?
            intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("${MARKET_URI}$packageName")
            context.startActivity(intent)
        }
    }

    private fun openLink(url: String) {
        try {
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun setProgressBarColor(progressBarColor: String?) {
        try {
            val color = Color.parseColor(progressBarColor)
            binding?.progressBarShopScoreDetail?.progressColor = color
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}