package com.tokopedia.recharge_component.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeBuyWidgetBinding
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import org.jetbrains.annotations.NotNull
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class RechargeBuyWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeBuyWidgetBinding = WidgetRechargeBuyWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderBuyWidget(denom: DenomData, listener: RechargeBuyWidgetListener, multiCheckoutButtons: List<MultiCheckoutButtons>){
        with(rechargeBuyWidgetBinding){
            tgBuyWidgetTotalPrice.run {
                text = denom.price
                if (denom.slashPrice.isNullOrEmpty()){
                    setMargin(
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_12)
                    )
                } else {
                    setMargin(
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                }
                setOnClickListener {
                    listener.onClickedChevron(denom)
                }
            }

            tgBuyWidgetSlashPrice.run {
                if (!denom.slashPrice.isNullOrEmpty()){
                    show()
                    text = denom.slashPrice
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else hide()

                setOnClickListener {
                    listener.onClickedChevron(denom)
                }
            }

            iconBuyWidgetChevron.setOnClickListener {
                listener.onClickedChevron(denom)
            }

            if (multiCheckoutButtons.size > Int.ONE) {
                val (leftButton, rightButton)  = multiCheckoutButtonSeparator(multiCheckoutButtons)

                if (leftButton.position.isNotEmpty() && leftButton.text.isNotEmpty() && leftButton.color.isNotEmpty() && leftButton.type.isNotEmpty()) {
                    btnBuyLeft.show()
                    btnBuyLeft.text = leftButton.text
                    btnBuyLeft.buttonVariant = variantButton(leftButton.color)
                    btnBuyLeft.setOnClickListener {
                        chooseListenerAction(listener, denom,  leftButton.type)
                    }
                } else {
                    btnBuyLeft.hide()
                }

                if (rightButton.position.isNotEmpty() && rightButton.text.isNotEmpty() && rightButton.color.isNotEmpty() && rightButton.type.isNotEmpty()) {
                    btnBuyRight.text = rightButton.text
                    btnBuyRight.buttonVariant = variantButton(rightButton.color)
                    btnBuyRight.setOnClickListener {
                        chooseListenerAction(listener, denom, rightButton.type)
                    }
                }

            } else {
                btnBuyRight.setOnClickListener {
                    chooseListenerAction(listener, denom, "")
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

    private fun chooseListenerAction(listener: RechargeBuyWidgetListener, denom: DenomData, type: String) {
        if (type.equals(ACTION_MULTIPLE)) {
            listener.onClickedButtonMultiCheckout(denom)
        } else {
            listener.onClickedButtonLanjutkan(denom)
        }
    }

    private fun variantButton(color: String): Int {
        return if (color.equals(WHITE_COLOR)) {
            UnifyButton.Variant.GHOST
        } else {
            UnifyButton.Variant.FILLED
        }
    }

    companion object {
        private const val POSITION_LEFT = "left"
        private const val POSITION_RIGHT = "right"
        private const val ACTION_MULTIPLE = "multiple"
        private const val WHITE_COLOR = "#FFFFFF"
    }
}
