package com.tokopedia.common.topupbills.data.constant

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.analytics.PromotionMultiCheckout
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.ACTION_MULTIPLE
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_LEFT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_RIGHT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_TWO
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.PREFERENCE_MULTICHECKOUT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.SHOW_COACH_MARK_MULTICHECKOUT_KEY
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.TYPE_TWO
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.WHITE_COLOR
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton

object MultiCheckoutConst {
    const val POSITION_LEFT = "left"
    const val POSITION_RIGHT = "right"
    const val ACTION_MULTIPLE = "multiple"
    const val ACTION_GENERAL_MYBILLS = "mybills_page"
    @SuppressLint("UnsupportedDarkModeColor")
    const val WHITE_COLOR = "#FFFFFF"
    const val PREFERENCE_MULTICHECKOUT = "pdp_dg_multichekout"
    const val SHOW_COACH_MARK_MULTICHECKOUT_KEY = "pdp_dg_multichekout_is_coachmark_closed"
    const val POSITION_TWO = 2
    const val TYPE_TWO = 2
    const val PADDING_TOP = -4
}

fun showMultiCheckoutButton(
    multiCheckoutButtons: List<MultiCheckoutButtons>, context: Context,
    btnCheckoutNext: UnifyButton, btnMultiCheckout: UnifyButton,
    coachmark: CoachMark2,
    onClickNextBuyButton: () -> Unit, onClickMultiCheckout: () -> Unit,
    onCloseCoachMark: () -> Unit,
) {
    val localCacheHandler = LocalCacheHandler(context, PREFERENCE_MULTICHECKOUT)
    val coachMarkList = arrayListOf<CoachMark2Item>()
    if (multiCheckoutButtons.size > Int.ONE) {
        processMultipleButton(multiCheckoutButtons, btnCheckoutNext, btnMultiCheckout,
            onClickNextBuyButton, onClickMultiCheckout, coachMarkList)
    } else if (multiCheckoutButtons.size == Int.ONE) {
        processOneButton(multiCheckoutButtons, btnCheckoutNext, coachMarkList, onClickNextBuyButton,
            onClickMultiCheckout)
    } else {
        btnCheckoutNext.setOnClickListener {
            onClickNextBuyButton()
        }
    }

    coachMarkUpdate(coachmark, localCacheHandler, coachMarkList, onCloseCoachMark)
}

private fun processMultipleButton(multiCheckoutButtons: List<MultiCheckoutButtons>,
                                  btnCheckoutNext: UnifyButton, btnMultiCheckout: UnifyButton,
                                  onClickNextBuyButton: () -> Unit, onClickMultiCheckout: () -> Unit,
                                  coachMarkList: ArrayList<CoachMark2Item>
) {
    val (leftButton, rightButton) = multiCheckoutButtonSeparator(multiCheckoutButtons)
    processLeftButton(leftButton, btnMultiCheckout, onClickNextBuyButton, onClickMultiCheckout, coachMarkList)
    processRightButton(rightButton, btnCheckoutNext, onClickNextBuyButton, onClickMultiCheckout, coachMarkList)
}


private fun processLeftButton(leftButton: MultiCheckoutButtons, btnMultiCheckout: UnifyButton,
                              onClickNextBuyButton: () -> Unit, onClickMultiCheckout: () -> Unit,
                              coachMarkList: ArrayList<CoachMark2Item>) {
    if (leftButton.position.isNotEmpty() && leftButton.text.isNotEmpty() && leftButton.color.isNotEmpty()
        && leftButton.type.isNotEmpty()) {
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
}

private fun processRightButton(rightButton: MultiCheckoutButtons,
                               btnCheckoutNext: UnifyButton,
                               onClickNextBuyButton: () -> Unit, onClickMultiCheckout: () -> Unit,
                               coachMarkList: ArrayList<CoachMark2Item>) {
    if (rightButton.position.isNotEmpty() && rightButton.text.isNotEmpty() && rightButton.color.isNotEmpty()
        && rightButton.type.isNotEmpty()) {
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
}
private fun processOneButton(multiCheckoutButtons: List<MultiCheckoutButtons>, btnCheckoutNext: UnifyButton,
                                 coachMarkList: ArrayList<CoachMark2Item>, onClickNextBuyButton: () -> Unit,
                                 onClickMultiCheckout: () -> Unit) {
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
}

private fun coachMarkUpdate(coachmark: CoachMark2, localCacheHandler: LocalCacheHandler,
                            coachMarkList: ArrayList<CoachMark2Item>, onCloseCoachMark: () -> Unit) {
    val isCoachMarkClosed = localCacheHandler.getBoolean(SHOW_COACH_MARK_MULTICHECKOUT_KEY, false)

    if (!isCoachMarkClosed) {
        coachmark.showCoachMark(coachMarkList)
        coachmark.setOnDismissListener {
            onCloseCoachMark()
            localCacheHandler.putBoolean(SHOW_COACH_MARK_MULTICHECKOUT_KEY, true)
            localCacheHandler.applyEditor()
        }
    }
}

fun multiCheckoutButtonPromotionTracker(multiCheckoutButtons: List<MultiCheckoutButtons>): PromotionMultiCheckout {
    val multiple = multiCheckoutButtons.firstOrNull {
        it.type == ACTION_MULTIPLE
    }
    return PromotionMultiCheckout(
        multiple?.text ?: "",
        if (multiple?.position == POSITION_LEFT) { Int.ONE } else { POSITION_TWO }
    )
}

fun multiCheckoutButtonImpressTrackerButtonType(multiCheckoutButtons: List<MultiCheckoutButtons>): Int {
    val multiple = multiCheckoutButtons.firstOrNull {
        it.type == ACTION_MULTIPLE
    }
    return when {
        multiCheckoutButtons.size == Int.ONE -> Int.ZERO
        multiple?.position == POSITION_LEFT -> Int.ONE
        multiple?.position == POSITION_RIGHT -> TYPE_TWO
        else -> Int.ZERO
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
