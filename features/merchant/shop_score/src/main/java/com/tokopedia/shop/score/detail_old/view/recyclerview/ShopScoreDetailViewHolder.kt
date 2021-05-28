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
import com.tokopedia.shop.score.detail_old.view.model.ShopScoreDetailItem
import com.tokopedia.shop.score.detail_old.view.util.formatShopScore
import kotlinx.android.synthetic.main.item_shop_score_detail.view.*

class ShopScoreDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val PACKAGE_NAME = "com.tokopedia.sellerapp"
        private const val MARKET_URI = "market://details?id="
    }
    
    private val context by lazy { itemView.context }
    
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
        itemView.title_shop_score_detail.text = title
    }

    private fun setShopScoreValue(value: Float) {
        itemView.apply {
            description_shop_score_value.text = value.formatShopScore()
            progress_bar_shop_score_detail.progress = value
        }
    }

    private fun setShopScoreMaxValue(maxValue: Float) {
        itemView.apply {
            val maxScore = context.getString(R.string.description_shop_score_percent,
                maxValue.formatShopScore())
            description_shop_score_percent.text = maxScore
            progress_bar_shop_score_detail.max = maxValue
        }
    }

    private fun setShopScoreDescription(description: String?) {
        itemView.apply {
            if (description != null) {
                val sequence: CharSequence = MethodChecker.fromHtml(description)
                val strBuilder = SpannableStringBuilder(sequence)
                val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
                for (span in urls) {
                    makeLinkClickable(strBuilder, span)
                }
                description_shop_score_detail.text = strBuilder
                description_shop_score_detail.movementMethod = LinkMovementMethod.getInstance()
                description_shop_score_detail.visibility = View.VISIBLE
            } else {
                description_shop_score_detail.visibility = View.GONE
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
            itemView.progress_bar_shop_score_detail.progressColor = color
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}