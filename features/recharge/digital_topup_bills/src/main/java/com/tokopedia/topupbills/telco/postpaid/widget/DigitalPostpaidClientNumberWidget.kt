package com.tokopedia.topupbills.telco.postpaid.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.ACTION_MULTIPLE
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_LEFT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_RIGHT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.WHITE_COLOR
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.postpaid.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.common.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by nabillasabbaha on 26/04/19.
 */
class DigitalPostpaidClientNumberWidget : DigitalClientNumberWidget {

    private lateinit var btnMain: UnifyButton
    private lateinit var btnSecondary: UnifyButton
    private lateinit var enquiryResult: LinearLayout
    private lateinit var titleEnquiryResult: TextView
    private lateinit var postpaidListener: ClientNumberPostpaidListener

    constructor(context: Context) : super(context) {
        initV()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initV()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initV()
    }

    override fun getLayout(): Int {
        return R.layout.view_telco_input_number_postpaid
    }

    fun initV() {
        btnMain = view.findViewById(R.id.telco_main_btn)
        btnSecondary = view.findViewById(R.id.telco_secondary_btn)
    }

    fun resetClientNumberPostpaid() {
        btnMain.show()
    }

    fun resetEnquiryResult() {
        btnMain.show()
    }

    fun setButtonEnquiry(enable: Boolean) {
        btnMain.isClickable = enable
    }

    fun setLoadingButtonEnquiry(loading: Boolean) {
        btnMain.isLoading = loading
    }

    fun setPostpaidListener(listener: ClientNumberPostpaidListener) {
        this.postpaidListener = listener
    }

    fun showMulticheckoutButtonSupport(multiCheckoutButtons: List<MultiCheckoutButtons>) {
        val localCacheHandler =
            LocalCacheHandler(context, MultiCheckoutConst.PREFERENCE_MULTICHECKOUT)
        val coachMarkList = arrayListOf<CoachMark2Item>()

        if (multiCheckoutButtons.size > Int.ONE) {
            val (leftButton, rightButton) = multiCheckoutButtonSeparator(multiCheckoutButtons)
            if (leftButton.position.isNotEmpty() && leftButton.text.isNotEmpty() && leftButton.color.isNotEmpty() && leftButton.type.isNotEmpty()) {
                btnSecondary.show()
                btnSecondary.text = leftButton.text
                btnSecondary.buttonVariant = variantButton(leftButton.color)
                btnSecondary.setOnClickListener {
                    chooseListenerAction(leftButton.type)
                }

                if (leftButton.coachmark.isNotEmpty()) {
                    coachMarkList.add(
                        CoachMark2Item(
                            btnSecondary, "", leftButton.coachmark
                        )
                    )
                }
            } else {
                btnSecondary.hide()
            }

            if (rightButton.position.isNotEmpty() && rightButton.text.isNotEmpty() && rightButton.color.isNotEmpty() && rightButton.type.isNotEmpty()) {
                btnMain.text = rightButton.text
                btnMain.buttonVariant = variantButton(rightButton.color)
                btnMain.setOnClickListener {
                    chooseListenerAction(rightButton.type)
                }

                if (rightButton.coachmark.isNotEmpty()) {
                    coachMarkList.add(
                        CoachMark2Item(
                            btnMain, "", rightButton.coachmark
                        )
                    )
                }
            }
        } else if (multiCheckoutButtons.size == kotlin.Int.ONE) {
            val button = multiCheckoutButtons.first()
            btnMain.text = button.text
            btnMain.buttonVariant = variantButton(button.color)
            btnMain.setOnClickListener {
                chooseListenerAction(button.type)
            }

            if (button.coachmark.isNotEmpty()) {
                coachMarkList.add(
                    CoachMark2Item(
                        btnMain, "", button.coachmark
                    )
                )
            }
        } else {
            if (btnMain.isClickable) {
                btnMain.setOnClickListener {
                    postpaidListener.mainButtonClick()
                }
            }
        }

        val isCoachMarkClosed = localCacheHandler.getBoolean(
            com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.SHOW_COACH_MARK_MULTICHECKOUT_KEY,
            false
        )

        if (!isCoachMarkClosed) {
            val coachmark = CoachMark2(context)
            coachmark.showCoachMark(coachMarkList)
            coachmark.setOnDismissListener {
                localCacheHandler.putBoolean(
                    com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.SHOW_COACH_MARK_MULTICHECKOUT_KEY,
                    true
                )
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

    private fun chooseListenerAction(type: String) {
        if (type.equals(ACTION_MULTIPLE)) {
            postpaidListener.secondaryButtonClick()
        } else {
            postpaidListener.mainButtonClick()
        }
    }

    private fun variantButton(color: String): Int {
        return if (color.equals(WHITE_COLOR)) {
            UnifyButton.Variant.GHOST
        } else {
            UnifyButton.Variant.FILLED
        }
    }
}
