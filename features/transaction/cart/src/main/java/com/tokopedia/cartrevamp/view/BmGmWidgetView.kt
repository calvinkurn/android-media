package com.tokopedia.cartrevamp.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.databinding.ItemCartBmgmTickerBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.cart.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.LoaderUnify

class BmGmWidgetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: ItemCartBmgmTickerBinding? = ItemCartBmgmTickerBinding.inflate(LayoutInflater.from(context), this, true)
    private var bmGmWidgetViewListener: BmGmWidgetViewListener? = null

    var state: State = State.ACTIVE
        set(value) {
            field = value
            initView()
        }

    var title: String = ""
        set(value) {
            field = value
            initView()
        }

    var urlLeftIcon: String = ""
        set(value) {
            field = value
            initView()
        }

    var urlRightIcon: String = ""
        set(value) {
            field = value
            initView()
        }

    init {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.BmGmWidgetView)
        try {
            state = State.fromId(styledAttributes.getInteger(R.styleable.BmGmWidgetView_stateTicker, 0))
            title = styledAttributes.getString(R.styleable.BmGmWidgetView_title) ?: ""
            urlLeftIcon = styledAttributes.getString(R.styleable.BmGmWidgetView_icon_left) ?: ""
            urlRightIcon = styledAttributes.getString(R.styleable.BmGmWidgetView_icon_right) ?: ""
        } finally {
            styledAttributes.recycle()
        }
    }

    fun setListener(listener: BmGmWidgetViewListener) {
        this.bmGmWidgetViewListener = listener
        initView()
    }

    private fun initView() {
        when (state) {
            State.LOADING -> setViewLoading()
            State.ACTIVE -> setViewActive()
            State.INACTIVE -> setViewInactive()
        }

        invalidate()
        requestLayout()
    }

    private fun loadLeftImage() {
        binding?.icBmgmTicker?.loadImage(urlLeftIcon)
    }

    private fun setViewLoading() {
        binding?.run {
            layoutBasketBuildingTicker.visible()
            tvBmgmTicker.gone()
            icBmgmTicker.gone()
            iuTickerRightIcon.gone()
            vBmgmProductSeparator.gone()

            vBmgmProductLoaderSeparator.visible()
            cartShopTickerLargeLoader.type =
                    LoaderUnify.TYPE_LINE
            cartShopTickerLargeLoader.show()
            cartShopTickerSmallLoader.type =
                    LoaderUnify.TYPE_RECT
            cartShopTickerSmallLoader.show()
            ivTickerBg.setImageResource(com.tokopedia.cart.R.drawable.bg_cart_basket_building_loading_ticker)
            layoutBasketBuildingTicker.setOnClickListener(null)
        }
    }

    private fun setViewActive() {
        binding?.run {
            vBmgmProductLoaderSeparator.gone()
            cartShopTickerSmallLoader.gone()
            cartShopTickerLargeLoader.gone()

            ivTickerBg.setImageResource(com.tokopedia.cart.R.drawable.bg_cart_bmgm)
            vBmgmProductSeparator.visible()
            tvBmgmTicker.visible()
            tvBmgmTicker.text = title

            icBmgmTicker.visible()
            icBmgmTicker.loadImage(urlLeftIcon)

            iuTickerRightIcon.visible()
            iuTickerRightIcon.setOnClickListener {
                bmGmWidgetViewListener?.onChevronRightClicked()
            }
        }
    }

    private fun setViewInactive() {
        binding?.run {
            cartShopTickerLargeLoader.gone()
            cartShopTickerSmallLoader.gone()
            tvBmgmTicker.text = "Info promosi gagal dimuat. Muat ulang, yuk!"
            tvBmgmTicker.show()
            icBmgmTicker.gone()
            val iconColor = MethodChecker.getColor(
                    root.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN900
            )
            val reloadIcon = getIconUnifyDrawable(root.context, IconUnify.RELOAD, iconColor)
            iuTickerRightIcon.setImageDrawable(reloadIcon)
            iuTickerRightIcon.show()
            ivTickerBg.setImageResource(R.drawable.bg_cart_basket_building_error_ticker)
            layoutBasketBuildingTicker.setOnClickListener {
                bmGmWidgetViewListener?.onReloadClicked()
            }
            layoutBasketBuildingTicker.show()
        }
    }

    enum class State(val id: Int) {
        LOADING(0),
        ACTIVE(1),
        INACTIVE(2);

        companion object {
            fun fromId(id: Int): State {
                for (state: State in values()) {
                    if (id == state.id) {
                        return state
                    }
                }
                return ACTIVE
            }
        }
    }
}
