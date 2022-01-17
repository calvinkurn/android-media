package com.tokopedia.recharge_component.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeBuyWidgetBinding
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeBuyWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeBuyWidgetBinding = WidgetRechargeBuyWidgetBinding.inflate(LayoutInflater.from(context), this, true)

    fun showBuyWidget(product: CatalogProduct, listener: RechargeBuyWidgetListener){
        with(rechargeBuyWidgetBinding){
            tgBuyWidgetTotalPrice.run {
                if (!product.attributes.promo?.newPrice.isNullOrEmpty()){
                    text = product.attributes.promo?.newPrice
                } else {
                    text = product.attributes.price
                    setMargin(
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            .toInt(),
                        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                            .toInt()
                    )
                }

                setOnClickListener {
                    listener.onClickedChevron(product)
                }
            }

            tgBuyWidgetSlashPrice.run {
                if (!product.attributes.promo?.newPrice.isNullOrEmpty()){
                    show()
                    text = product.attributes.price
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else hide()

                setOnClickListener {
                    listener.onClickedChevron(product)
                }
            }

            iconBuyWidgetChevron.setOnClickListener {
                listener.onClickedChevron(product)
            }

            btnBuyWidget.setOnClickListener {
                listener.onClickedButtonLanjutkan(product)
            }
        }
    }
}