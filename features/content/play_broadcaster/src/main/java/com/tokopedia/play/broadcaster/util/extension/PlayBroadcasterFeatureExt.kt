package com.tokopedia.play.broadcaster.util.extension

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.Toaster
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by jegul on 16/07/20
 */
internal fun GlobalError.productNotFoundState() {
    errorIllustration.setImageResource(com.tokopedia.resources.common.R.drawable.ic_empty_search_wishlist)
    errorTitle.text = context.getString(R.string.play_product_not_found_title)
    errorDescription.text = context.getString(R.string.play_product_not_found_desc)
    errorAction.gone()
}

internal fun GlobalError.productEtalaseEmpty() {
    errorIllustration.setImageResource(R.drawable.ic_empty_product_etalase)
    errorTitle.text = context.getString(R.string.play_product_etalase_empty_title)
    errorDescription.text = context.getString(R.string.play_product_etalase_empty_desc)
    errorAction.gone()
}

internal fun GlobalError.channelNotFound(onAction: () -> Unit) {
    this.errorTitle.setTextColor(ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
    this.errorDescription.setTextColor(ContextCompat.getColor(this.context, R.color.play_dms_white_68))
    this.setType(GlobalError.PAGE_NOT_FOUND)
    this.setActionClickListener { onAction() }
}

internal fun View.showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { },
        bottomMargin: Int? = null
) {
    if (actionLabel.isNotEmpty()) Toaster.toasterCustomCtaWidth = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl8)
    if (bottomMargin != null) Toaster.toasterCustomBottomHeight = bottomMargin
    Toaster.make(this,
            text = message,
            duration = duration,
            type = type,
            actionText = actionLabel,
            clickListener = actionListener)
}

internal fun ImageView.loadImageFromUrl(url: String, requestListener: RequestListener<Drawable>) {
    Glide.with(context)
            .load(url)
            .placeholder(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
            .dontAnimate()
            .error(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
            .addListener(requestListener)
            .into(this)
}

internal fun Long.convertMillisToMinuteSecond(): String = String.format("%02d:%02d",
        this.convertMillisToMinute(),
        this.convertMillisToSecond()
)

internal fun Long.convertMillisToMinute() = TimeUnit.MILLISECONDS.toMinutes(this)

internal fun Long.convertMillisToSecond() = TimeUnit.MILLISECONDS.toSeconds(this) -
        TimeUnit.MINUTES.toSeconds(this.convertMillisToMinute())

internal fun Date.dayLater(amount: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, amount)
    return calendar.time
}

internal fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

const val DATE_FORMAT_RFC3339 = "yyyy-MM-dd'T'HH:mm:ssXXX"
const val DATE_FORMAT_BROADCAST_SCHEDULE = "EEEE, d MMMM y - HH:mm"

internal fun String.toDateWithFormat(format: String): Date {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(this)?:Date()
    } catch (throwable: Throwable){
        Date()
    }
}