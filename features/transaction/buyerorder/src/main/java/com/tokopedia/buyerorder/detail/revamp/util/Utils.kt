package com.tokopedia.buyerorder.detail.revamp.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unifyprinciples.R as UnifyPrinciplesR
import com.tokopedia.unifyprinciples.Typography

/**
 * created by @bayazidnasir on 6/9/2022
 */

object Utils {

    object Const{
        const val ZERO_MARGIN = 0
        const val STROKE_WIDTH = 1
        const val DELIMITERS = ","
        const val ZERO_QUANTITY = 0
        const val KEY_TEXT = "text"
        const val ITEMS_DEALS = 1
        const val ITEM_EVENTS = 3
        const val KEY_BUTTON = "button"
        const val KEY_REDIRECT = "redirect"
        const val KEY_REFRESH = "refresh"
        const val KEY_VOUCHER_CODE = "vouchercodes"
        const val KEY_QRCODE = "qrcode"
        const val KEY_POPUP = "popup"
        const val KEY_REDIRECT_EXTERNAL = "redirectexternal"
        const val CONTENT_TYPE = "application/pdf"
        const val IS_ENTERTAIN = 1
        const val E_TICKET_FORMAT = "%s %s"
        const val TEXT_STYLE_BOLD = "bold"
        const val TEXT_SIZE_LARGE = 14F
        const val PREFERENCES_NAME = "deals_banner_preferences"
    }

    fun renderActionButtons(
        context: Context,
        position: Int,
        actionButton: ActionButton,
        item: Items,
    ) : Typography {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(
                Const.ZERO_MARGIN,
                getDimension(context, com.tokopedia.resources.common.R.dimen.dp_8),
                Const.ZERO_MARGIN,
                Const.ZERO_MARGIN,
            )
        }

        return Typography(context).apply {
            setPadding(
                getDimension(context, UnifyPrinciplesR.dimen.unify_space_16),
                getDimension(context, UnifyPrinciplesR.dimen.unify_space_16),
                getDimension(context, UnifyPrinciplesR.dimen.unify_space_16),
                getDimension(context, UnifyPrinciplesR.dimen.unify_space_16),
            )
            setTextColor(MethodChecker.getColor(context, UnifyPrinciplesR.color.Unify_N0))
            layoutParams = params
            gravity = Gravity.CENTER_HORIZONTAL
            text = actionButton.label

            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE

            if (actionButton.actionColor.background.isNotEmpty()) {
                shape.setColor(Color.parseColor(actionButton.actionColor.background))
            } else {
                shape.setColor(MethodChecker.getColor(context, UnifyPrinciplesR.color.Unify_G400))
            }

            if (actionButton.actionColor.border.isNotEmpty()) {
                shape.setStroke(
                    Const.STROKE_WIDTH,
                    Color.parseColor(actionButton.actionColor.border)
                )
            }

            if (actionButton.actionColor.textColor.isNotEmpty()) {
                setTextColor(Color.parseColor(actionButton.actionColor.textColor))
            } else {
                setTextColor(MethodChecker.getColor(context, UnifyPrinciplesR.color.Unify_N0))
            }

            if (position == item.actionButtons.size - 1 &&  item.actionButtons.isEmpty()){
                val radius = context.resources.getDimension(UnifyPrinciplesR.dimen.unify_space_4)
                shape.cornerRadii = floatArrayOf(0F, 0F, 0F, 0F, radius, radius, radius, radius)
            } else {
                shape.cornerRadius = context.resources.getDimension(UnifyPrinciplesR.dimen.unify_space_4)
            }

            background = shape
        }
    }

    private fun getDimension(context: Context, @DimenRes dimenId: Int): Int {
        return context.resources.getDimension(dimenId).toIntSafely()
    }

}