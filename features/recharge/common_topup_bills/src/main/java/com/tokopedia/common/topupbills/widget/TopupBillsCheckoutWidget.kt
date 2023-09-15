package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
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
import com.tokopedia.common.topupbills.databinding.ViewTopupBillsCheckoutBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.unifycomponents.UnifyButton
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 17/05/19.
 */
class TopupBillsCheckoutWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTopupBillsCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

    var listener: ActionListener? = null
        set(value) {
            field = value
        }
    var promoListener: TickerPromoStackingCheckoutView.ActionListener? = null
        set(value) {
            field = value
            promoListener?.run {
                binding.rechargeCheckoutPromoTicker.actionListener = this
            }
        }

    init {
        binding.rechargeCheckoutPromoTicker.enableView()
    }

    fun getPromoTicker(): TickerPromoStackingCheckoutView {
        return binding.rechargeCheckoutPromoTicker
    }

    fun getCheckoutButton(): UnifyButton {
        return binding.btnRechargeCheckoutNext
    }

    fun setTotalPrice(price: String) {
        binding.txtRechargeCheckoutPrice.text = price
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            binding.buyLayout.show()
        } else {
            binding.buyLayout.hide()
        }
    }

    fun setBuyButtonLabel(label: String) {
        binding.btnRechargeCheckoutNext.text = label
    }

    fun onBuyButtonLoading(isLoading: Boolean) {
        binding.btnRechargeCheckoutNext.isLoading = isLoading
    }

    fun showMulticheckoutButtonSupport(multiCheckoutButtons: List<MultiCheckoutButtons>) {
        val localCacheHandler = LocalCacheHandler(context, PREFERENCE_MULTICHECKOUT)
        val coachMarkList = arrayListOf<CoachMark2Item>()
        with(binding) {
            if (multiCheckoutButtons.size > Int.ONE) {
                val (leftButton, rightButton)  = multiCheckoutButtonSeparator(multiCheckoutButtons)
                if (leftButton.position.isNotEmpty() && leftButton.text.isNotEmpty() && leftButton.color.isNotEmpty() && leftButton.type.isNotEmpty()) {
                    btnRechargeMultiCheckout.show()
                    btnRechargeMultiCheckout.text = leftButton.text
                    btnRechargeMultiCheckout.buttonVariant = variantButton(leftButton.color)
                    btnRechargeMultiCheckout.setOnClickListener {
                        chooseListenerAction(leftButton.type)
                    }

                    if (leftButton.coachmark.isNotEmpty()) {
                        coachMarkList.add(
                            CoachMark2Item(
                                btnRechargeMultiCheckout, "", leftButton.coachmark
                            )
                        )
                    }
                } else {
                    btnRechargeMultiCheckout.hide()
                }

                if (rightButton.position.isNotEmpty() && rightButton.text.isNotEmpty() && rightButton.color.isNotEmpty() && rightButton.type.isNotEmpty()) {
                    btnRechargeCheckoutNext.text = rightButton.text
                    btnRechargeCheckoutNext.buttonVariant = variantButton(rightButton.color)
                    btnRechargeCheckoutNext.setOnClickListener {
                        chooseListenerAction(rightButton.type)
                    }

                    if (rightButton.coachmark.isNotEmpty()) {
                        coachMarkList.add(CoachMark2Item(
                            btnRechargeCheckoutNext, "", rightButton.coachmark
                        ))
                    }
                }
            } else if(multiCheckoutButtons.size == Int.ONE) {
                val button = multiCheckoutButtons.first()
                btnRechargeCheckoutNext.text = button.text
                btnRechargeCheckoutNext.buttonVariant = variantButton(button.color)
                btnRechargeCheckoutNext.setOnClickListener {
                    chooseListenerAction(button.type)
                }

                if (button.coachmark.isNotEmpty()) {
                    coachMarkList.add(CoachMark2Item(
                        btnRechargeCheckoutNext, "", button.coachmark
                    )
                    )
                }
            } else {
                listener?.run {
                    binding.btnRechargeCheckoutNext.setOnClickListener {
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
        if (type.equals(ACTION_MULTIPLE)) {
            listener?.onClickMultiCheckout()
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

        fun onClickMultiCheckout()
    }
}
