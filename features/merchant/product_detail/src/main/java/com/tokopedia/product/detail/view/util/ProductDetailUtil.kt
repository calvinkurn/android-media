package com.tokopedia.product.detail.view.util

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.BuildConfig
import com.tokopedia.product.detail.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyCustomTypefaceSpan
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import java.util.concurrent.TimeUnit


object ProductDetailUtil {

    const val HOURS_IN_A_DAY = 24
    const val DAYS_IN_A_MONTH = 30
    const val MONTHS_IN_A_YEAR = 12

    const val LAST_ONLINE_MONTH_THRESHOLD = 3
    const val LAST_ONLINE_DAYS_RANGE_START = 3
    const val LAST_ONLINE_DAYS_RANGE_END = 6
    const val LAST_ONLINE_MINUTES_RANGE_END = 5

    private const val MAX_CHAR = 140
    private const val ALLOW_CLICK = true

    fun reviewDescFormatter(context: Context, review: String): Pair<CharSequence?, Boolean> {
        val formattedText = HtmlLinkHelper(context, review).spannedString ?: ""
        return if (formattedText.length > MAX_CHAR) {
            val subDescription = formattedText.substring(0, MAX_CHAR)
            Pair(HtmlLinkHelper(context, subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... " + context.getString(R.string.review_expand)).spannedString, ALLOW_CLICK)
        } else {
            Pair(formattedText, !ALLOW_CLICK)
        }
    }
}

fun getIntentImagePreviewWithoutDownloadButton(context: Context, imageUrl: ArrayList<String>): Intent {
    return ImagePreviewActivity.getCallingIntent(context = context, imageUris = imageUrl, disableDownloadButton = true)
}

fun String.boldOrLinkText(isLink: Boolean, context: Context,
                          vararg textToBold: Pair<String, () -> Unit>): SpannableString {
    val builder = SpannableString(this)

    if (this.isNotEmpty() || this.isNotBlank()) {
        for (it in textToBold) {
            if (it.first.isEmpty()) continue

            val rawText = this.toLowerCase(Locale.getDefault())
            val rawTextToBold = it.first.toLowerCase(Locale.getDefault())

            val startIndex = rawText.indexOf(rawTextToBold)
            val endIndex = startIndex + rawTextToBold.length

            val typographyBoldTypeFace = getTypeface(context, "RobotoBold.ttf")

            if (startIndex < 0 || endIndex < 0) {
                continue
            } else {
                builder.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.setSpan(CustomTypeSpan(typographyBoldTypeFace), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        it.second.invoke()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        val textColor = if (isLink) com.tokopedia.unifyprinciples.R.color.Unify_GN500 else com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                        ds.color = MethodChecker.getColor(context, textColor)
                    }
                }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    return builder
}

fun String.renderHtmlBold(
    context: Context,
    boldColor: Int = com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
): CharSequence? {
    if (this.isEmpty()) return null
    val spannedHtmlString: Spanned = MethodChecker.fromHtml(this)
    val spanHandler = SpannableStringBuilder(spannedHtmlString)
    val styleSpanArr = spanHandler.getSpans(0, spannedHtmlString.length, StyleSpan::class.java)
    val boldSpanArr: MutableList<StyleSpan> = mutableListOf()
    styleSpanArr.forEach {
        if (it.style == Typeface.BOLD) {
            boldSpanArr.add(it)
        }
    }

    boldSpanArr.forEach {
        val boldStart = spanHandler.getSpanStart(it)
        val boldEnd = spanHandler.getSpanEnd(it)

        spanHandler.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, boldColor)), boldStart, boldEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spanHandler.setSpan(UnifyCustomTypefaceSpan(getTypeface(context, "RobotoBold.ttf")), boldStart, boldEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }

    return spanHandler
}

internal fun Long.getRelativeDateByMinute(context: Context): String {
    if (this == 0L) return ""

    val minuteInput = this
    val minuteDivider = TimeUnit.HOURS.toMinutes(1)
    val dayInMinute = TimeUnit.DAYS.toMinutes(1)

    return if (minuteInput / dayInMinute > 0) {
        context.getString(R.string.shop_chat_speed_in_days, minuteInput / dayInMinute)
    } else if (minuteInput / minuteDivider > 0) {
        context.getString(R.string.shop_chat_speed_in_hours, minuteInput / minuteDivider)
    } else {
        if (minuteInput > 0) {
            context.getString(R.string.shop_chat_speed_in_minute, minuteInput)
        } else {
            context.getString(R.string.shop_chat_speed_in_minute, 1)
        }
    }
}

internal fun Long.getRelativeDateByHours(context: Context): String {
    if (this == 0L) return ""

    val hourInput = this
    val dayInHours = TimeUnit.DAYS.toHours(1)

    return if (hourInput / dayInHours > 0) {
        context.getString(R.string.shop_chat_speed_in_days_with_icon, hourInput / dayInHours)
    } else {
        context.getString(R.string.shop_chat_speed_in_hours_with_icon, hourInput)
    }
}

internal fun String.getRelativeDate(context: Context): String {
    if (this.isEmpty()) return ""

    val idLocale = getIdLocale()
    val unixTime = this.toLongOrZero() * 1000

    val diff: Long = Calendar.getInstance().timeInMillis / 1000 - this.toLongOrZero()
    val date = Date(unixTime)

    val getYear = date.toFormattedString("yyyy", idLocale)
    val getDaysAndMonth = date.toFormattedString("dd MMM", idLocale)
    val getMonthAndYear = date.toFormattedString("MMM yyyy", idLocale)

    val minuteDivider: Long = 60
    val hourDivider = minuteDivider * 60
    val dayDivider = hourDivider * ProductDetailUtil.HOURS_IN_A_DAY
    val monthDivider = dayDivider * ProductDetailUtil.DAYS_IN_A_MONTH
    val yearDivider = monthDivider * ProductDetailUtil.MONTHS_IN_A_YEAR

    return if (diff / yearDivider > 0) {
        context.getString(R.string.shop_online_last_date, getYear)
    } else if (diff / monthDivider >= ProductDetailUtil.LAST_ONLINE_MONTH_THRESHOLD) {
        context.getString(R.string.shop_online_last_date, getMonthAndYear)
    } else if (diff / dayDivider > 0) {
        val days = diff / dayDivider
        when {
            days <= 1 -> {
                context.getString(R.string.shop_online_yesterday)
            }
            days in IntRange(
                ProductDetailUtil.LAST_ONLINE_DAYS_RANGE_START,
                ProductDetailUtil.LAST_ONLINE_DAYS_RANGE_END
            ) -> {
                context.getString(R.string.shop_online_days_ago, diff / dayDivider)
            }
            else -> {
                context.getString(R.string.shop_online_last_date, getDaysAndMonth)
            }
        }
    } else if (diff / hourDivider > 0) {
        context.getString(R.string.shop_online_hours_ago, diff / hourDivider)
    } else {
        val minutes = diff / minuteDivider
        if (minutes in 0..ProductDetailUtil.LAST_ONLINE_MINUTES_RANGE_END) context.getString(R.string.shop_online) else
            context.getString(R.string.shop_online_minute_ago, minutes)
    }
}

infix fun String?.toDate(format: String): String {
    this?.let {
        val isLongFormat = try {
            it.toLong()
            true
        } catch (e: Throwable) {
            false
        }

        return if (isLongFormat) {
            val date = Date(it.toLong() * 1000)
            date.toFormattedString(format)
        } else {
            this
        }
    }
    return ""
}

infix fun String?.toDateId(format: String): String {
    this?.let {
        val isLongFormat = try {
            it.toLong()
            true
        } catch (e: Throwable) {
            false
        }

        return if (isLongFormat) {
            val date = Date(it.toLong() * 1000)
            date.toFormattedString(format, Locale("id", "ID"))
        } else {
            this
        }
    }
    return ""
}

fun <T : Any> Result<T>.doSuccessOrFail(success: (Success<T>) -> Unit, fail: (Fail: Throwable) -> Unit) {
    when (this) {
        is Success -> {
            success.invoke(this)
        }
        is Fail -> {
            fail.invoke(this.throwable)
        }
    }
}

inline fun <reified T> GraphqlResponse.doActionIfNotNull(listener: (T) -> Unit) {
    val data: T? = this.getData<T>(T::class.java)
    data?.let {
        listener.invoke(it)
    }
}

fun getIdLocale() = Locale("id", "ID")

fun String.goToWebView(context: Context) {
    RouteManager.route(context, String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, this))
}

fun <T : Any> T.asSuccess(): Success<T> = Success(this)
fun Throwable.asFail(): Fail = Fail(this)

internal fun View?.animateExpand(duration: Long = 200) = this?.run {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    show()
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f) ConstraintLayout.LayoutParams.WRAP_CONTENT
            else (targetHeight * interpolatedTime).toInt()
            alpha = interpolatedTime
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = duration
    startAnimation(animation)
}

internal fun View?.animateCollapse(duration: Long = 200) = this?.run {
    val initialHeight = measuredHeight
    val animation: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                gone()
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                alpha = 1.0f - interpolatedTime
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = duration
    startAnimation(animation)
}

internal fun String?.checkIfNumber(key: String): String {
    if (this == null || this.isEmpty()) return ""

    return try {
        this.toLong()
        this
    } catch (t: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(Exception(t.localizedMessage, t))
        } else {
            t.printStackTrace()
        }

        ProductDetailLogger.logLocalization("error $key, value : $this , error: ${t.message}")
        ""
    }
}

internal fun RecommendationItem.createProductCardOptionsModel(position: Int): ProductCardOptionsModel {
    val productCardOptionsModel = ProductCardOptionsModel()
    productCardOptionsModel.hasWishlist = true
    productCardOptionsModel.isWishlisted = isWishlist
    productCardOptionsModel.productId = productId.toString()
    productCardOptionsModel.isTopAds = isTopAds
    productCardOptionsModel.topAdsWishlistUrl = wishlistUrl
    productCardOptionsModel.topAdsClickUrl = clickUrl
    productCardOptionsModel.productName = name
    productCardOptionsModel.productImageUrl = imageUrl
    productCardOptionsModel.productPosition = position
    productCardOptionsModel.screenName = header
    return productCardOptionsModel
}

fun ViewStub.inflateWithBinding(onInflate: (View) -> Unit) {
    if (this.parent != null) {
        this.setOnInflateListener { _, view ->
            onInflate(view)
        }
        this.inflate()
    }
}

fun ViewStub.isInflated(): Boolean {
    return this.parent == null
}
