package com.tokopedia.purchase_platform.common.insurance.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.purchase_platform.common.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

@JvmField
val VALIDATION_TYPE_MAX_DATE = "maxDate"

@JvmField
val VALIDATION_TYPE_MIN_DATE = "minDate"

@JvmField
val VALIDATION_TYPE_MAX_LENGTH = "maxLength"

@JvmField
val VALIDATION_TYPE_MIN_LENGTH = "minLength"

@JvmField
val VALIDATION_TYPE_PATTERN = "pattern"

@JvmField
val PAGE_TYPE_CART = "cart"

@JvmField
val PAGE_TYPE_CHECKOUT = "checkout"

@JvmField
val INSURANCE_RECOMMENDATION_PARAM_GQL = "digitalProducts"

@JvmField
val INSURANCE_APPLICATION_TYPE_TEXT = "text"

@JvmField
val INSURANCE_APPLICATION_TYPE_NUMBER = "number"

@JvmField
val INSURANCE_APPLICATION_TYPE_DATE = "date"

@JvmField
val INSURANCE_APPLICATION_TYPE_DROPDOWN = "dropdown"

private val DATE_FORMAT_VIEW = "dd MMM yyyy"
private val DATE_FORMAT_SERVER = "yyyy-MM-dd"

fun getDateInServerFormat(inputValue: String): String {
    var outputValue = ""
    var date = Date()
    val formatter = SimpleDateFormat(DATE_FORMAT_VIEW)
    try {
        date = formatter.parse(inputValue)

    } catch (exception: ParseException) {
        exception.printStackTrace()
    }
    outputValue = SimpleDateFormat(DATE_FORMAT_SERVER).format(date)

    return outputValue
}

fun validateMinDate(value: CharSequence?, minValue: String): Boolean {

    val sdfo1 = SimpleDateFormat(DATE_FORMAT_VIEW)
    val incomingValue = sdfo1.parse(value.toString())

    val sdfo2 = SimpleDateFormat(DATE_FORMAT_SERVER)
    val minDate = sdfo2.parse(minValue)

    return incomingValue.compareTo(minDate) >= 0
}

fun validateMaxDate(value: CharSequence?, maxValue: String): Boolean {
    val sdfo1 = SimpleDateFormat(DATE_FORMAT_VIEW)
    val incomingValue = sdfo1.parse(value.toString())

    val sdfo2 = SimpleDateFormat(DATE_FORMAT_SERVER)
    val minDate = sdfo2.parse(maxValue)

    return incomingValue.compareTo(minDate) <= 0
}

@SuppressLint("RestrictedApi")
fun updateEditTextBackground(mEditText: TextView, colorInt: Int, isErrorShowing: Boolean) {
    if (mEditText == null) {
        return
    }

    var editTextBackground: Drawable = mEditText.background ?: return

    if (androidx.appcompat.widget.DrawableUtils.canSafelyMutateDrawable(editTextBackground)) {
        editTextBackground = editTextBackground.mutate()
    }

    if (isErrorShowing) {
        editTextBackground.colorFilter = AppCompatDrawableManager.getPorterDuffColorFilter(
                colorInt, PorterDuff.Mode.SRC_IN)
    } else {
        DrawableCompat.clearColorFilter(editTextBackground)
        mEditText.refreshDrawableState()
    }
}

fun getDateStringInUIFormat(value: String): String {

    var date = Date()
    val formatter = SimpleDateFormat(DATE_FORMAT_SERVER)
    try {
        date = formatter.parse(value)

    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return SimpleDateFormat(DATE_FORMAT_VIEW).format(date)
}

fun openBottomSheetWebView(context: Context, appLinkUrl: String, title: String) {
    val closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)


    val infoDialogView = (context as Activity).layoutInflater.inflate(R.layout.insurance_info_bottom_sheet, null)

    val webView = infoDialogView.findViewById<WebView>(R.id.bottom_sheet_webview)

    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
    webView.getSettings().setJavaScriptEnabled(true)
    webView.loadUrl(appLinkUrl)

    val tvTitle = infoDialogView.findViewById<TextView>(R.id.info_bottom_sheet_title_tv)
    tvTitle.setText(title)

    val closeImageView = infoDialogView.findViewById<ImageView>(R.id.ic_close_icon)

    closeImageView.setOnClickListener{ closeableBottomSheetDialog.dismiss() }

    val progressBar: ProgressBar = infoDialogView.findViewById(R.id.progbar)

    webView.webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            progressBar.visibility = View.GONE
        }
    }


    closeableBottomSheetDialog.setOnShowListener { dialog ->
        val d = dialog as BottomSheetDialog
        val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    closeableBottomSheetDialog.setCustomContentView(infoDialogView, "", true)
    closeableBottomSheetDialog.show()
}

fun validatePattern(value: CharSequence?, regExPattern: String): Boolean {
    if (value == null) {
        return false
    } else {
        val pattern = Pattern.compile(regExPattern)
        val matcher = pattern.matcher(value)
        return matcher.matches()
    }
}

fun validateMaxLength(text: CharSequence, maxLength: String): Boolean {
    return TextUtils.isEmpty(text) || text.length <= Integer.valueOf(maxLength)
}

fun validateMinLength(text: CharSequence, minLength: String): Boolean {
    return !TextUtils.isEmpty(text) && text.length >= Integer.valueOf(minLength)
}


fun dateFormatter(date: String): String {
    val sdf = SimpleDateFormat(DATE_FORMAT_VIEW)
    val sdf_ws = SimpleDateFormat("dd/MM/yyyy")
    var formattedStart: Date? = null
    try {
        formattedStart = sdf.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return sdf_ws.format(formattedStart)
}

fun getDate(year: Int, month: Int, day: Int): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_VIEW)
    val date = Date()
    val cal = GregorianCalendar()
    cal.time = date
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.DAY_OF_MONTH, day)
    cal.set(Calendar.MONTH, month - 1)
    return dateFormat.format(cal.time)
}

fun getStartYear(date: String): Int {
    val year = date.substring(6, 10)
    return Integer.parseInt(year)
}

fun getStartMonth(date: String): Int {
    val month = date.substring(3, 5)
    return Integer.parseInt(month)
}

fun getDay(date: String): Int {
    val day = date.substring(0, 2)
    return Integer.parseInt(day)
}