package com.tokopedia.sellerorder.common.util

import android.app.Activity
import android.content.Context
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.Spanned
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.media.loader.loadImageWithCacheData
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.PATTERN_DATE_PARAM
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_ANNOUNCEMENT
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_ERROR
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_INFO
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_WARNING
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.shop.common.constant.SellerHomePermissionGroup
import com.tokopedia.shop.common.constant.admin_roles.AdminPermissionUrl
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by fwidjaja on 2019-11-21.
 */
object Utils {
    @JvmStatic
    fun showToasterError(message: String, view: View?) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, SomConsts.ACTION_OK)
        }
    }

    @JvmStatic
    fun mapStringTickerTypeToUnifyTickerType(typeString: String): Int {
        return when (typeString) {
            UNIFY_TICKER_TYPE_ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            UNIFY_TICKER_TYPE_INFO -> Ticker.TYPE_INFORMATION
            UNIFY_TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
            UNIFY_TICKER_TYPE_ERROR -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    @JvmStatic
    fun getL2CancellationReason(text: String, textToPrepend: String = ""): String {
        return if (text.contains('-')) {
            "$textToPrepend ${text.split(" - ").last().replaceFirstChar { it.lowercase(Locale.getDefault()) }}"
        } else {
            text
        }
    }

    fun getColoredIndicator(context: Context, colorHex: String): Drawable? {
        val color = parseUnifyColorHex(context, colorHex, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        val drawable = MethodChecker.getDrawable(context, R.drawable.ic_order_status_indicator)
        val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
        drawable.colorFilter = filter
        return drawable
    }

    fun getColoredDeadlineBackground(context: Context, colorHex: String, defaultColor: Int): Drawable? {
        val color = parseUnifyColorHex(context, colorHex, defaultColor)
        val drawable = MethodChecker.getDrawable(context, R.drawable.bg_order_deadline)
        val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
        drawable.colorFilter = filter
        return drawable
    }

    fun getDeadlineDrawable(context: Context, @ColorRes colorRes: Int): GradientDrawable? {
        val drawable = MethodChecker.getDrawable(context, R.drawable.bg_order_deadline)
        val bgColor = MethodChecker.getColor(context, colorRes)
        val gradientDrawable = (drawable as? GradientDrawable)
        gradientDrawable?.setColor(bgColor)
        return gradientDrawable
    }

    fun getColoredResoDeadlineBackground(context: Context, colorHex: String, defaultColor: Int): Drawable? {
        val color = parseUnifyColorHex(context, colorHex, defaultColor)
        val drawable = MethodChecker.getDrawable(context, R.drawable.bg_due_response)
        val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
        drawable.colorFilter = filter
        return drawable
    }

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun Long.formatDate(pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(this)
    }

    fun getNPastYearTimeStamp(yearBefore: Int): Date {
        val date = Calendar.getInstance(getLocale())
        date.set(Calendar.YEAR, date.get(Calendar.YEAR) - yearBefore)
        return date.time
    }

    private fun getNPastMonthTimeStamp(monthBefore: Int): Long {
        val date = Calendar.getInstance(getLocale())
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - monthBefore)
        return date.timeInMillis
    }

    fun getNowTimeStamp(): Long {
        val date = Calendar.getInstance(getLocale())
        return date.timeInMillis
    }

    fun getNFutureDaysTimeStamp(daysForward: Int): Date {
        val date = Calendar.getInstance(getLocale())
        date.add(Calendar.DATE, daysForward)
        return date.time
    }

    fun getNFutureMonthsTimeStamp(monthsForward: Int): Date {
        val date = Calendar.getInstance(getLocale())
        date.add(Calendar.MONTH, monthsForward)
        return date.time
    }

    fun getNFutureYearsTimeStamp(yearsForward: Int): Date {
        val date = Calendar.getInstance(getLocale())
        date.add(Calendar.YEAR, yearsForward)
        return date.time
    }

    fun getNPastMonthTimeText(monthBefore: Int, format: String = PATTERN_DATE_PARAM): String {
        val pastTwoYear = getNPastMonthTimeStamp(monthBefore)
        return format(pastTwoYear, format)
    }

    fun List<SomFilterUiModel>.copyListParcelable(): List<SomFilterUiModel> {
        return this.mapNotNull { it.copyParcelable() }
    }

    fun <T : Parcelable> T.copyParcelable(): T? {
        var parcel: Parcel? = null

        return try {
            parcel = Parcel.obtain()
            parcel.writeParcelable(this, 0)
            parcel.setDataPosition(0)
            parcel.readParcelable(this::class.java.classLoader)
        } catch (ignore: Throwable) {
            null
        } finally {
            parcel?.recycle()
        }
    }

    fun List<Int>.copyInt(): List<Int> {
        return this.map { it }
    }

    fun String.toStringFormatted(maxChar: Int): Spanned {
        return if (MethodChecker.fromHtml(this).toString().length > maxChar) {
            val subDescription = MethodChecker.fromHtml(this).toString().substring(0, maxChar)
            MethodChecker.fromHtml("$subDescription...")
        } else {
            MethodChecker.fromHtml(this)
        }
    }

    fun View?.hideKeyboard() {
        this?.rootView?.let { view ->
            if (view.context != null) {
                KeyboardHandler.DropKeyboard(view.context, view)
            }
        }
    }

    internal fun GlobalError.setUserNotAllowedToViewSom(onActionClick: () -> Unit) {
        val permissionGroup = SellerHomePermissionGroup.ORDER
        errorIllustration.loadImageWithCacheData(AdminPermissionUrl.ERROR_ILLUSTRATION)
        errorTitle.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_title, permissionGroup)
        errorDescription.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_desc, permissionGroup)
        errorAction.text = context?.getString(com.tokopedia.shop.common.R.string.admin_no_permission_action)
        setButtonFull(true)

        setActionClickListener {
            onActionClick.invoke()
        }
        show()
    }

    fun parseRupiah(price: String): String {
        return "Rp ${CurrencyFormatHelper.convertToRupiah(price)}"
    }

    fun Fragment?.updateShopActive() {
        this?.context?.let { UpdateShopActiveWorker.execute(it) }
    }

    fun Activity.updateShopActive() {
        UpdateShopActiveWorker.execute(this)
    }

    fun View.generateHapticFeedback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }

    fun String.stripLastDot(): String {
        return removeSuffix(".")
    }

    fun parseUnifyColorHex(context: Context, colorHex: String, defaultColor: Int): Int {
        return try {
            stringToUnifyColor(context, colorHex).run { this.unifyColor ?: this.defaultColor }
        } catch (t: Throwable) {
            defaultColor
        }
    }

    @JvmStatic
    fun isEnableOperationalGuideline(): Boolean {
        return try {
            val remoteConfigImpl = RemoteConfigInstance.getInstance().abTestPlatform
            remoteConfigImpl.getString(RollenceKey.KEY_SOM_OG, "") == RollenceKey.KEY_SOM_OG
        } catch (ignore: Exception) {
            true
        }
    }

    fun Throwable.getGlobalErrorType(): Int {
        return when (this) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
    }
}
