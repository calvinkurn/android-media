package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_ATC_MAX_ORDER
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_MIN_QTY
import com.tokopedia.product.detail.view.listener.PartialButtonActionListener
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.partial_layout_button_action.view.*
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


class PartialButtonActionView private constructor(val view: View,
                                                  private val buttonListener: PartialButtonActionListener) {
    var visibility: Boolean = false
        set(value) {
            field = value
            with(view) {
                if (value) base_btn_action.visible() else base_btn_action.gone()
            }
        }
    private var hasComponentLoading = false
    private var isExpressCheckout = false
    private var isWarehouseProduct: Boolean = false
    private var hasShopAuthority: Boolean = false
    private var hasTopAdsActive: Boolean = false
    private var isShopOwner: Boolean = false
    private var preOrder: PreOrder? = PreOrder()
    private var onSuccessGetCartType = false
    private var cartTypeData: CartTypeData? = null
    private var textWatcher: TextWatcher? = null
    private var quantityDebounceSubscription: Subscription? = null
    private var localQuantity: Int = 0
    private var tokonowButtonData: TokoNowButtonData? = null

    private val containerTokonowVar = view.findViewById<ConstraintLayout>(R.id.tokonow_button_container)
    private val btnTokonowVar = view.findViewById<UnifyButton>(R.id.btn_atc_tokonow_variant)
    private val txtTotalStockTokonowVar = view.findViewById<Typography>(R.id.txt_total_quantity)
    private val dividerTokonow = view.findViewById<View>(R.id.divider_button_quantity)
    private val txtProductNameTokonowVar = view.findViewById<Typography>(R.id.txt_product_name)

    private val icDeleteNonVar = view.findViewById<IconUnify>(R.id.btn_delete_tokonow_non_var)
    private val qtyButtonPdp = view.findViewById<QuantityEditorUnify>(R.id.qty_tokonow_non_var)

    companion object {
        fun build(_view: View, _buttonListener: PartialButtonActionListener) = PartialButtonActionView(_view, _buttonListener)

        private const val QUANTITY_REGEX = "[^0-9]"
        private const val TEXTWATCHER_QUANTITY_DEBOUNCE_TIME = 500L
        private const val DEFAULT_TOTAL_STOCK = 1
    }

    fun setButtonP1(preOrder: PreOrder?) {
        this.preOrder = preOrder
    }

    fun setTopAdsButton(hasTopAdsActive: Boolean) {
        this.hasTopAdsActive = hasTopAdsActive
        updateTopAdsButton()
    }

    fun renderData(isWarehouseProduct: Boolean,
                   hasShopAuthority: Boolean,
                   isShopOwner: Boolean,
                   hasTopAdsActive: Boolean,
                   tokonowButtonData: TokoNowButtonData? = null,
                   cartTypeData: CartTypeData? = null) {

        this.isWarehouseProduct = isWarehouseProduct
        this.hasShopAuthority = hasShopAuthority
        this.hasTopAdsActive = hasTopAdsActive
        this.cartTypeData = cartTypeData
        this.isShopOwner = isShopOwner
        this.onSuccessGetCartType = cartTypeData != null && cartTypeData.availableButtons.isNotEmpty()
        this.tokonowButtonData = tokonowButtonData
        renderButton()
    }

    private fun updateTopAdsButton() {
        if (hasShopAuthority) {
            showShopManageButton()
        }
    }

    private fun renderButton() {
        if (hasShopAuthority) {
            showShopManageButton()
        } else if (!GlobalConfig.isSellerApp() && tokonowButtonData != null) {
            showTokoNowButton()
        } else if (!GlobalConfig.isSellerApp() && onSuccessGetCartType) {
            showCartTypeButton()
        } else if (!GlobalConfig.isSellerApp() && !onSuccessGetCartType) {
            if (isWarehouseProduct) {
                showWarehouseButton()
            } else {
                showNewCheckoutButton()
            }
        }
    }

    private fun showTokoNowButton() = with(view) {
        btn_empty_stock.hide()
        seller_button_container.hide()
        btn_buy_now.hide()
        btn_add_to_cart.hide()

        if (tokonowButtonData?.isVariant == true) {
            showViewTokoNowVar()
            renderTokoNowVar()
        } else {
            showViewTokoNowNonVar()
            renderTokoNowNonVar(tokonowButtonData?.selectedMiniCart
                    ?: MiniCartItem(), tokonowButtonData?.minQuantity ?: DEFAULT_MIN_QTY,
                    tokonowButtonData?.maxQuantity ?: DEFAULT_ATC_MAX_ORDER)
        }

        txtProductNameTokonowVar.text = tokonowButtonData?.productTitle ?: ""
        renderTopChat(cartTypeData?.unavailableButtons ?: listOf())
        containerTokonowVar.show()
    }


    private fun showViewTokoNowVar() {
        qtyButtonPdp.hide()
        icDeleteNonVar.hide()
        btnTokonowVar.show()
        txtTotalStockTokonowVar.show()
        dividerTokonow.show()
    }

    private fun showViewTokoNowNonVar() {
        qtyButtonPdp.show()
        icDeleteNonVar.show()
        btnTokonowVar.hide()
        txtTotalStockTokonowVar.hide()
        dividerTokonow.hide()
    }

    private fun renderTokoNowVar() = with(view) {
        val availableButton = cartTypeData?.availableButtons ?: listOf()
        btnTokonowVar.text = availableButton.getOrNull(0)?.text
                ?: context.getString(com.tokopedia.product.detail.common.R.string.plus_product_to_cart)
        btnTokonowVar.generateTheme(availableButton.getOrNull(0)?.color
                ?: ProductDetailCommonConstant.KEY_BUTTON_PRIMARY)
        btnTokonowVar.setOnClickListener {
            buttonListener.buttonCartTypeClick(availableButton.getOrNull(0)?.cartType
                    ?: ProductDetailCommonConstant.KEY_NORMAL_BUTTON, btnTokonowVar.text.toString(), true)
        }

        txtTotalStockTokonowVar.text = context.getString(R.string.pdp_pcs_builder, tokonowButtonData?.totalStockAtcVariant
                ?: DEFAULT_TOTAL_STOCK)
    }

    private fun renderTopChat(unavailableButton: List<String>) = with(view) {
        btn_topchat.showWithCondition(ProductDetailCommonConstant.KEY_CHAT !in unavailableButton)
        btn_topchat.setOnClickListener {
            buttonListener.topChatButtonClicked()
        }
    }

    private fun showCartTypeButton() = with(view) {
        hideButtonEmptyAndTopAds()

        renderNormalButtonCartRedirection()

        val unavailableButton = cartTypeData?.unavailableButtons ?: listOf()
        btn_topchat.showWithCondition(ProductDetailCommonConstant.KEY_CHAT !in unavailableButton)
        btn_topchat.setOnClickListener {
            buttonListener.topChatButtonClicked()
        }

    }

    private fun renderNormalButtonCartRedirection() = with(view) {
        qtyButtonPdp.hide()
        val availableButton = cartTypeData?.availableButtons ?: listOf()

        btn_buy_now.showWithCondition(availableButton.firstOrNull() != null)
        btn_add_to_cart.showWithCondition(availableButton.getOrNull(1) != null)

        btn_buy_now.text = availableButton.getOrNull(0)?.text ?: ""
        btn_add_to_cart.text = availableButton.getOrNull(1)?.text ?: ""

        btn_buy_now.setOnClickListener {
            buttonListener.buttonCartTypeClick(availableButton.getOrNull(0)?.cartType
                    ?: "", btn_buy_now.text.toString(), availableButton.getOrNull(0)?.showRecommendation
                    ?: false)
        }

        btn_add_to_cart.setOnClickListener {
            buttonListener.buttonCartTypeClick(availableButton.getOrNull(1)?.cartType
                    ?: "", btn_add_to_cart.text.toString(), availableButton.getOrNull(1)?.showRecommendation
                    ?: false)
        }

        btn_buy_now.generateTheme(availableButton.getOrNull(0)?.color ?: "")
        btn_add_to_cart.generateTheme(availableButton.getOrNull(1)?.color ?: "")
    }

    private fun renderTokoNowNonVar(selectedMiniCart: MiniCartItem, minQuantity: Int, maxQuantity: Int) = with(view) {
        localQuantity = selectedMiniCart.quantity
        qtyButtonPdp?.run {
            minValue = minQuantity
            maxValue = maxQuantity
            setValue(localQuantity)

            if (textWatcher != null) {
                editText.removeTextChangedListener(textWatcher)
            }
            if (quantityDebounceSubscription != null) {
                buttonListener.getRxCompositeSubcription().remove(quantityDebounceSubscription)
                quantityDebounceSubscription = null
            }

            initTextWatcherDebouncer(minQuantity, maxQuantity, selectedMiniCart)
            show()
        }

        icDeleteNonVar?.setOnClickListener {
            buttonListener.onDeleteAtcClicked()
        }
    }

    private fun initTextWatcherDebouncer(minQuantity: Int, maxQuantity: Int, selectedMiniCart: MiniCartItem) {
        quantityDebounceSubscription = Observable.create(
                Observable.OnSubscribe<Int> { subscriber ->
                    textWatcher = object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            val quantityInt: Int = s.toString().replace(QUANTITY_REGEX.toRegex(), "").toIntOrZero()
                            subscriber.onNext(quantityInt)
                        }
                    }
                    qtyButtonPdp?.editText?.addTextChangedListener(textWatcher)
                })
                .debounce(TEXTWATCHER_QUANTITY_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Int>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }

                    override fun onNext(quantity: Int) {
                        onNextValueQuantity(quantity, minQuantity , maxQuantity, selectedMiniCart)
                    }
                })

        buttonListener.getRxCompositeSubcription().add(quantityDebounceSubscription)
    }

    private fun onNextValueQuantity(quantity: Int, minQuantity: Int, maxQuantity: Int, selectedMiniCart: MiniCartItem) {
        qtyButtonPdp?.run {
            if (quantity < minQuantity) {
                setValue(minQuantity)
            } else if (quantity > maxQuantity) {
                setValue(maxQuantity)
            } else {
                if (localQuantity != quantity && quantity != 0) {
                    buttonListener.updateQuantityNonVarTokoNow(
                            quantity = getValue(),
                            miniCart = selectedMiniCart,
                            oldValue = localQuantity
                    )
                    localQuantity = quantity
                    //fire again to update + and - button
                    setValue(localQuantity)
                }
            }
        }
    }

    private fun UnifyButton.generateTheme(colorDescription: String) {
        when (colorDescription) {
            ProductDetailCommonConstant.KEY_BUTTON_PRIMARY -> {
                this.buttonVariant = UnifyButton.Variant.FILLED
                this.buttonType = UnifyButton.Type.TRANSACTION
                this.isEnabled = true
            }
            ProductDetailCommonConstant.KEY_BUTTON_DISABLE -> {
                this.buttonVariant = UnifyButton.Variant.FILLED
                this.buttonType = UnifyButton.Type.MAIN
                this.isEnabled = false
            }
            ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN -> {
                this.buttonVariant = UnifyButton.Variant.FILLED
                this.buttonType = UnifyButton.Type.MAIN
                this.isEnabled = true
            }
            ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GREEN -> {
                this.buttonVariant = UnifyButton.Variant.GHOST
                this.buttonType = UnifyButton.Type.MAIN
                this.isEnabled = true
            }
            ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GRAY -> {
                this.buttonVariant = UnifyButton.Variant.GHOST
                this.buttonType = UnifyButton.Type.ALTERNATE
                this.isEnabled = true
            }
            else -> {
                this.buttonVariant = UnifyButton.Variant.GHOST
                this.buttonType = UnifyButton.Type.TRANSACTION
                this.isEnabled = true
            }
        }
    }

    private fun showNewCheckoutButton() {
        with(view) {
            qtyButtonPdp.hide()
            hideButtonEmptyAndTopAds()
            btn_topchat.visibility = View.VISIBLE
            btn_buy_now.text = context.getString(
                    if (preOrder?.isPreOrderActive() == true) {
                        R.string.action_preorder
                    } else {
                        if (isExpressCheckout) {
                            com.tokopedia.product.detail.common.R.string.buy_now
                        } else {
                            R.string.buy
                        }
                    })
            btn_add_to_cart.text = context.getString(com.tokopedia.product.detail.common.R.string.plus_product_to_cart)
            btn_buy_now.visible()
            btn_add_to_cart.visible()

            btn_buy_now.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                buttonListener.buyNowClick(btn_buy_now.text.toString())
            }
            btn_add_to_cart.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                buttonListener.addToCartClick(btn_add_to_cart.text.toString())
            }

            btn_buy_now.generateTheme(ProductDetailCommonConstant.KEY_BUTTON_SECONDARY)
            btn_add_to_cart.generateTheme(ProductDetailCommonConstant.KEY_BUTTON_PRIMARY)
            btn_topchat.setOnClickListener {
                buttonListener.topChatButtonClicked()
            }
        }
    }

    private fun hideButtonEmptyAndTopAds() = with(view) {
        btn_empty_stock.hide()
        seller_button_container.hide()
        containerTokonowVar.hide()
    }

    private fun showShopManageButton() {
        with(view) {
            containerTokonowVar.hide()
            btn_empty_stock.hide()
            btn_topchat.hide()
            btn_buy_now.hide()
            btn_add_to_cart.hide()
            qtyButtonPdp.hide()

            seller_button_container.show()
            if (hasTopAdsActive) {
                btn_top_ads.setOnClickListener { buttonListener.rincianTopAdsClicked() }
                btn_top_ads.text = context.getString(R.string.rincian_topads)
            } else {
                btn_top_ads.setOnClickListener { buttonListener.advertiseProductClicked() }
                btn_top_ads.text = context.getString(R.string.promote_topads)
            }
            btn_edit_product.setOnClickListener { buttonListener.editProductButtonClicked() }
        }
    }

    private fun showNoStockButton() {
        with(view) {
            seller_button_container.hide()
            containerTokonowVar.hide()
            btn_empty_stock.show()
            qtyButtonPdp.hide()
            btn_topchat.showWithCondition(!isShopOwner)
            btn_topchat.setOnClickListener { buttonListener.topChatButtonClicked() }
        }
    }

    private fun showWarehouseButton() {
        if (isShopOwner) {
            showShopManageButton()
        } else {
            showNoStockButton()
        }
    }

    fun gone() {
        view.base_btn_action.gone()
    }

    fun setBackground(resource: Int) {
        view.base_btn_action.setBackgroundResource(resource)
    }

    fun setBackground(drawable: Drawable) {
        view.base_btn_action.background = drawable
    }
}

data class TokoNowButtonData(
        //var
        val totalStockAtcVariant: Int = 0,
        val productTitle: String = "",
        val isVariant: Boolean = false,

        //non var
        val minQuantity: Int = DEFAULT_MIN_QTY,
        val maxQuantity: Int = DEFAULT_ATC_MAX_ORDER,
        val selectedMiniCart: MiniCartItem? = null
)
