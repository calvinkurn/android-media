package com.tokopedia.design.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ReplacementSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat

class LabelUtils {

    private var context: Context? = null
    private var userName: TextView? = null
    private var labelWidth: Float = 0.toFloat()
    private var detailView: Boolean = false
    internal var textLength = DEFAULT_MAX_TEXT_LENGTH

    fun giveSquareLabel(userRole: String) {
        detailView = false
        userNameTextViewLabelling(userRole)
    }

    private fun userNameFilter(userNameString: String, userRole: String): String {
        var tmpString = userNameString
        if (userNameString == "CustomerWrapper Service Tokopedia")
            tmpString = "CS Tokopedia"

        userName?.ellipsize = null

        if (tmpString.length > maximumString(userRole) && !detailView) {
            tmpString = userNameString.substring(0, maximumString(userRole)) + "..."
        } else if (tmpString.length > maximumString(userRole) && detailView) {
            tmpString = userNameString.substring(0, maximumString(userRole)) + "..."
        }
        return tmpString
    }

    inner class RoundedBackgroundSpan(private val roleLabelText: String) : ReplacementSpan() {

        override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int,
                             fm: Paint.FontMetricsInt?): Int = Math.round(labelWidth)

        override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float,
                          top: Int, y: Int, bottom: Int, paint: Paint) {
            val rect = RectF(x, (top + 5).toFloat(), x + labelWidth, bottom.toFloat())
            paint.color = userPrivilegeSelection(roleLabelText)
            canvas.drawRoundRect(rect, 3f, 3f, paint)
            context?.let { context ->
                paint.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
    }

    private fun userRoleWithGap(userRole: String): String = if (detailView) " $userRole  " else " $userRole "

    private fun checkTextTypeFace(): Boolean {
        if (userName?.typeface != null && userName?.typeface!!.style == Typeface.BOLD) {
            userName!!.setTypeface(null, Typeface.NORMAL)
            return true
        } else
            return false
    }

    private fun reboldText(textIsBold: Boolean, wordToSpan: Spannable, roleLength: Int, userLength: Int) {
        if (textIsBold) {
            wordToSpan.setSpan(StyleSpan(Typeface.BOLD), roleLength+1,
                    userLength+roleLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun userNameTextViewLabelling(userRole: String) {
        var user = userName?.text.toString()
        user = userNameFilter(user, userRole)
        val role = userRoleWithGap(userRole)
        labelWidth = userName!!.paint.measureText(role) * 0.7f
        val wordToSpan = SpannableString("$role $user")
        wordToSpan.setSpan(RelativeSizeSpan(0.7f), 0, role.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (context != null) {
            wordToSpan.setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, com.tokopedia.unifyprinciples.R.color.Unify_N0)), 0, role.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        reboldText(checkTextTypeFace(), wordToSpan, role.length, user.length)
        wordToSpan.setSpan(RoundedBackgroundSpan(userRole), 0, role.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (userRole != "")
            userName?.text = wordToSpan
    }

    private fun maximumString(userRole: String): Int {
        if (userRole.length > 8)
            textLength -= 4
        if (userName?.typeface != null && userName?.typeface?.style == Typeface.BOLD)
            textLength -= 1
        return textLength
    }

    private fun userPrivilegeSelection(role: String): Int {
        val ctx = context ?: return -1

        return ContextCompat.getColor(ctx, when (role) {
            "Penjual" -> com.tokopedia.unifyprinciples.R.color.Unify_Y600
            "Pembeli" -> com.tokopedia.unifyprinciples.R.color.Unify_B400
            "Pengguna" -> com.tokopedia.unifyprinciples.R.color.Unify_G400
            else -> com.tokopedia.unifyprinciples.R.color.Unify_Y400
        })
    }

    companion object {

        const val DEFAULT_MAX_TEXT_LENGTH = 21

        fun getInstance(context: Context, userName: TextView): LabelUtils {
            val privilege = LabelUtils()
            privilege.userName = userName
            privilege.context = context
            return privilege
        }

        fun getInstance(context: Context, userName: TextView, textLength: Int): LabelUtils {
            val privilege = LabelUtils()
            privilege.userName = userName
            privilege.context = context
            privilege.textLength = textLength
            return privilege
        }
    }
}