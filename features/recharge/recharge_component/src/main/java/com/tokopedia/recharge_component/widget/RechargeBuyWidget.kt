package com.tokopedia.recharge_component.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeBuyWidgetBinding
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeBuyWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeBuyWidgetBinding = WidgetRechargeBuyWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    fun showBuyWidget(denom: DenomData, listener: RechargeBuyWidgetListener){
        with(rechargeBuyWidgetBinding){
            root.show()
            tgBuyWidgetTotalPrice.run {
                text = denom.price
                if (denom.slashPrice.isNullOrEmpty()){
                    setMargin(
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                            .toInt()
                    )
                } else {
                    setMargin(
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt()
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

            btnBuyWidget.setOnClickListener {
                listener.onClickedButtonLanjutkan(denom)
            }
        }
    }

    fun hideBuyWidget(){
        with(rechargeBuyWidgetBinding){
            root.hide()
        }
    }

    fun isLoadingButton(isLoadingActive: Boolean){
        with(rechargeBuyWidgetBinding){
            btnBuyWidget.run {
                isLoading = isLoadingActive
            }
        }
    }
}