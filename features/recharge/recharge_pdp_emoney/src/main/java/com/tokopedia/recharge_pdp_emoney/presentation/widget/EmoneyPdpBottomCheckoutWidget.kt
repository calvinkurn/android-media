package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_LEFT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.POSITION_RIGHT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.PREFERENCE_MULTICHECKOUT
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.SHOW_COACH_MARK_MULTICHECKOUT_KEY
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst.WHITE_COLOR
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.WidgetEmoneyPdpCheckoutViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 30/04/21
 */
class EmoneyPdpBottomCheckoutWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0) : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
        set(value) {
            field = value
        }

    private val binding : WidgetEmoneyPdpCheckoutViewBinding
    
    init {
        binding = WidgetEmoneyPdpCheckoutViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun onBuyButtonLoading(isLoading: Boolean) {
        binding.emoneyPdpCheckoutViewButton.isLoading = isLoading
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            binding.emoneyPdpCheckoutViewLayout.show()
        } else {
            binding.emoneyPdpCheckoutViewLayout.hide()
        }
    }

    fun setTotalPrice(price: String) {
        binding.emoneyPdpCheckoutViewTotalPayment.text = price
    }

    fun showMulticheckoutButtonSupport(multiCheckoutButtons: List<MultiCheckoutButtons>) {
        val localCacheHandler = LocalCacheHandler(context, PREFERENCE_MULTICHECKOUT)
        val coachMarkList = arrayListOf<CoachMark2Item>()
        with(binding) {
            if (multiCheckoutButtons.size > Int.ONE) {
                val (leftButton, rightButton)  = multiCheckoutButtonSeparator(multiCheckoutButtons)
                if (leftButton.position.isNotEmpty() && leftButton.text.isNotEmpty() && leftButton.color.isNotEmpty() && leftButton.type.isNotEmpty()) {
                    emoneyPdpLeftButton.show()
                    emoneyPdpLeftButton.text = leftButton.text
                    emoneyPdpLeftButton.buttonVariant = variantButton(leftButton.color)
                    emoneyPdpLeftButton.setOnClickListener {
                        chooseListenerAction(leftButton.type)
                    }

                    if (leftButton.coachmark.isNotEmpty()) {
                        coachMarkList.add(
                            CoachMark2Item(
                                emoneyPdpLeftButton, "", leftButton.coachmark
                            )
                        )
                    }
                } else {
                    emoneyPdpLeftButton.hide()
                }

                if (rightButton.position.isNotEmpty() && rightButton.text.isNotEmpty() && rightButton.color.isNotEmpty() && rightButton.type.isNotEmpty()) {
                    emoneyPdpCheckoutViewButton.text = rightButton.text
                    emoneyPdpCheckoutViewButton.buttonVariant = variantButton(rightButton.color)
                    emoneyPdpCheckoutViewButton.setOnClickListener {
                        chooseListenerAction(rightButton.type)
                    }

                    if (rightButton.coachmark.isNotEmpty()) {
                        coachMarkList.add(CoachMark2Item(
                            emoneyPdpCheckoutViewButton, "", rightButton.coachmark
                        ))
                    }
                }
            } else if(multiCheckoutButtons.size == Int.ONE) {
                val button = multiCheckoutButtons.first()
                emoneyPdpCheckoutViewButton.text = button.text
                emoneyPdpCheckoutViewButton.buttonVariant = variantButton(button.color)
                emoneyPdpCheckoutViewButton.setOnClickListener {
                    chooseListenerAction(button.type)
                }

                if (button.coachmark.isNotEmpty()) {
                    coachMarkList.add(CoachMark2Item(
                        emoneyPdpCheckoutViewButton, "", button.coachmark
                    ))
                }
            } else {
                listener?.run {
                    binding.emoneyPdpCheckoutViewButton.setOnClickListener {
                        onClickNextBuyButton()
                    }
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
        if (type.equals(MultiCheckoutConst.ACTION_MULTIPLE)) {
            listener?.onClickMultiCheckoutButton()
        } else {
            listener?.onClickNextBuyButton()
        }
    }

    private fun variantButton(color: String): Int {
        return if (color.equals(WHITE_COLOR)) {
            UnifyButton.Variant.GHOST
        } else {
            UnifyButton.Variant.FILLED
        }
    }

    interface ActionListener {
        fun onClickNextBuyButton()

        fun onClickMultiCheckoutButton()
    }

}
