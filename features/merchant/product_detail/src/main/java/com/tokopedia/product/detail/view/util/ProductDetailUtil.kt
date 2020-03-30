package com.tokopedia.product.detail.view.util

import android.text.Spanned
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object ProductDetailUtil {

    private const val MAX_CHAR = 150
    private const val MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>"

    fun reviewDescFormatter(review: String): Spanned {
        return if (MethodChecker.fromHtml(review).length > MAX_CHAR) {
            val subDescription = MethodChecker.fromHtml(review).toString().substring(0, MAX_CHAR)
            MethodChecker
                    .fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                            + MORE_DESCRIPTION)
        } else {
            MethodChecker.fromHtml(review)
        }
    }
}

fun Fragment.doActionOrLogin(isLoggedIn: Boolean, action: () -> Unit) {
    if (isLoggedIn) {
        action.invoke()
    } else {
        this.activity?.let {
            this.startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    ProductDetailConstant.REQUEST_CODE_LOGIN)
        }
    }
}

fun Fragment.doActionOrloginWithExtra(isLoggedIn: Boolean, loginAction: () -> Unit, nonLoginAction: () -> Unit) {
    if(isLoggedIn) {
        loginAction.invoke()
    } else {
        nonLoginAction.invoke()
        this.activity?.let {
            this.startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    ProductDetailConstant.REQUEST_CODE_LOGIN)
        }
    }
}

fun <T> ArrayList<T>.asThrowable(): Throwable = Throwable(message = this.firstOrNull()?.toString()
        ?: "")

fun String.asThrowable(): Throwable = Throwable(message = this)

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

fun <T : Any> T.asSuccess(): Success<T> = Success(this)
fun Throwable.asFail(): Fail = Fail(this)

