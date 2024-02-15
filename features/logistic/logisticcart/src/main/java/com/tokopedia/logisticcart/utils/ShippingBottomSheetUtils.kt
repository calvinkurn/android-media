package com.tokopedia.logisticcart.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

object ShippingBottomSheetUtils {

    fun constructErrorUi(context: Context, errorMessage: String, errorId: String): CharSequence? {
        errorMessage.takeIf { it.isNotEmpty() }?.let { errorMessage ->
            val errorAction = getErrorAction(context, errorId)
            val wording = "$errorMessage $errorAction".trim()
            val result = SpannableString(wording).apply {
                setErrorMessageSpan(context, errorMessage)
                setActionErrorSpan(context, errorAction, errorMessage, wording)
            }
            return result
        }
        return null
    }

    val String.errorPinpoint: Boolean
        get() {
            return this == ErrorProductData.ERROR_PINPOINT_NEEDED
        }

    private fun SpannableString.setErrorMessageSpan(context: Context, errorMessage: String) {
        setSpan(
            ForegroundColorSpan(
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_RN500
                )
            ),
            0,
            errorMessage.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun SpannableString.setActionErrorSpan(
        context: Context,
        action: String,
        errorMessage: String,
        fullWording: String
    ) {
        if (action.isNotEmpty()) {
            setSpan(
                StyleSpan(Typeface.BOLD),
                fullWording.indexOf(action, errorMessage.length),
                fullWording.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(
                    MethodChecker.getColor(
                        context,
                        unifyprinciplesR.color.Unify_GN500
                    )
                ),
                fullWording.indexOf(action, errorMessage.length),
                fullWording.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun getErrorAction(context: Context, errorId: String): String {
        return if (errorId.errorPinpoint) context.getString(logisticcartR.string.service_error_pinpoint_action) else ""
    }
}
