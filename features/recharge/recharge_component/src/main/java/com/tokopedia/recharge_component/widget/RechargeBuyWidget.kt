package com.tokopedia.recharge_component.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.constant.MultiCheckoutConst
import com.tokopedia.common.topupbills.data.constant.showMultiCheckoutButton
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeBuyWidgetBinding
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toPx
import org.jetbrains.annotations.NotNull
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class RechargeBuyWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeBuyWidgetBinding = WidgetRechargeBuyWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    private var coachMark2 = CoachMark2(context)
    fun renderBuyWidget(denom: DenomData, listener: RechargeBuyWidgetListener, multiCheckoutButtons: List<MultiCheckoutButtons>,
                        onCloseCoachMark: () -> Unit){

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

            containerPrice.setOnClickListener {
                listener.onClickedChevron(denom)
            }

            showMultiCheckoutButton(multiCheckoutButtons, context, btnBuyRight, btnBuyLeft, coachMark2, {
                listener.onClickedButtonLanjutkan(denom)
            }, {
                listener.onClickedButtonMultiCheckout(denom)
            }, {
                onCloseCoachMark()
            })
        }
    }

    fun showCoachMark() {
        coachMark2.container?.show()
        coachMark2.container?.setPadding(Int.ZERO, MultiCheckoutConst.PADDING_TOP.toPx(), Int.ZERO, Int.ZERO)
    }

    fun hideCoachMark() {
        coachMark2.container?.hide()
    }

    fun closeCoachmark() {
        coachMark2.dismiss()
    }
}
