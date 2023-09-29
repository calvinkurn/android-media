package com.tokopedia.common.topupbills.data.constant

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.ACTION_MULTIPLE
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_LEFT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_RIGHT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.PREFERENCE_MULTICHECKOUT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.SHOW_COACH_MARK_MULTICHECKOUT_KEY
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.WHITE_COLOR
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton

object MultiCheckoutConst {
    const val POSITION_LEFT = "left"
    const val POSITION_RIGHT = "right"
    const val ACTION_MULTIPLE = "mybills_page"
    const val WHITE_COLOR = "#FFFFFF"
    const val PREFERENCE_MULTICHECKOUT = "pdp_dg_multichekout"
    const val SHOW_COACH_MARK_MULTICHECKOUT_KEY = "pdp_dg_multichekout_is_coachmark_closed"
}

fun showMultiCheckoutButton(
    multiCheckoutButtons: List<MultiCheckoutButtons>, context: Context,
    btnCheckoutNext: UnifyButton, btnMultiCheckout: UnifyButton,
    onClickNextBuyButton: () -> Unit, onClickMultiCheckout: () -> Unit
) {
    val localCacheHandler = LocalCacheHandler(context, PREFERENCE_MULTICHECKOUT)
    val coachMarkList = arrayListOf<CoachMark2Item>()
    if (multiCheckoutButtons.size > Int.ONE) {
        val (leftButton, rightButton) = multiCheckoutButtonSeparator(multiCheckoutButtons)
        if (leftButton.position.isNotEmpty() && leftButton.text.isNotEmpty() && leftButton.color.isNotEmpty() && leftButton.type.isNotEmpty()) {
            btnMultiCheckout.show()
            btnMultiCheckout.text = leftButton.text
            btnMultiCheckout.buttonVariant = variantButton(leftButton.color)
            btnMultiCheckout.setOnClickListener {
                chooseListenerAction(leftButton.type, onClickNextBuyButton, onClickMultiCheckout)
            }

            if (leftButton.coachmark.isNotEmpty()) {
                coachMarkList.add(
                    CoachMark2Item(
                        btnMultiCheckout, "", leftButton.coachmark
                    )
                )
            }
        } else {
            btnMultiCheckout.hide()
        }

        if (rightButton.position.isNotEmpty() && rightButton.text.isNotEmpty() && rightButton.color.isNotEmpty() && rightButton.type.isNotEmpty()) {
            btnCheckoutNext.text = rightButton.text
            btnCheckoutNext.buttonVariant = variantButton(rightButton.color)
            btnCheckoutNext.setOnClickListener {
                chooseListenerAction(rightButton.type, onClickNextBuyButton, onClickMultiCheckout)
            }

            if (rightButton.coachmark.isNotEmpty()) {
                coachMarkList.add(
                    CoachMark2Item(
                        btnCheckoutNext, "", rightButton.coachmark
                    )
                )
            }
        }
    } else if (multiCheckoutButtons.size == Int.ONE) {
        val button = multiCheckoutButtons.first()
        btnCheckoutNext.text = button.text
        btnCheckoutNext.buttonVariant = variantButton(button.color)
        btnCheckoutNext.setOnClickListener {
            chooseListenerAction(button.type, onClickNextBuyButton, onClickMultiCheckout)
        }

        if (button.coachmark.isNotEmpty()) {
            coachMarkList.add(
                CoachMark2Item(
                    btnCheckoutNext, "", button.coachmark
                )
            )
        }
    } else {
        btnCheckoutNext.setOnClickListener {
            onClickNextBuyButton()
        }
    }

    val isCoachMarkClosed = localCacheHandler.getBoolean(SHOW_COACH_MARK_MULTICHECKOUT_KEY, false)

    if (!isCoachMarkClosed) {
        val coachmark = CoachMark2(context)
        coachmark.showCoachMark(coachMarkList)
        coachmark.setOnDismissListener {
            localCacheHandler.putBoolean(SHOW_COACH_MARK_MULTICHECKOUT_KEY, true)
            localCacheHandler.applyEditor()
        }
    }
}

private fun multiCheckoutButtonSeparator(multiCheckoutButtons: List<MultiCheckoutButtons>): Pair<MultiCheckoutButtons, MultiCheckoutButtons> {
    val leftButton = multiCheckoutButtons.first {
        it.position == POSITION_LEFT
    }

    val rightButton = multiCheckoutButtons.first {
        it.position == POSITION_RIGHT
    }

    return Pair(leftButton, rightButton)
}

private fun chooseListenerAction(
    type: String,
    onClickNextBuyButton: () -> Unit,
    onClickMultiCheckout: () -> Unit
) {
    if (type.equals(ACTION_MULTIPLE)) {
        onClickMultiCheckout()
    } else {
        onClickNextBuyButton()
    }
}

private fun variantButton(color: String): Int {
    return if (color.equals(WHITE_COLOR)) {
        UnifyButton.Variant.GHOST
    } else {
        UnifyButton.Variant.FILLED
    }
}
