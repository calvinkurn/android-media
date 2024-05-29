package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.onKeyboardVisibleListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton.Companion.buttonText
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton.Companion.orEmpty
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.product.detail.common.generateTheme
import com.tokopedia.product.detail.common.generateTopchatButtonPdp
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_ATC_MAX_ORDER
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_MIN_QTY
import com.tokopedia.product.detail.databinding.PartialLayoutButtonActionBinding
import com.tokopedia.product.detail.view.listener.PartialButtonActionListener
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import com.tokopedia.product.detail.R as productdetailR

class PartialButtonActionView private constructor(
    val view: View,
    private val buttonListener: PartialButtonActionListener
) {

    private val binding = PartialLayoutButtonActionBinding.bind(view)

    var visibility: Boolean = false
        set(value) {
            field = value
            if (value) binding.baseBtnAction.visible() else binding.baseBtnAction.gone()
        }
    private var hasComponentLoading = false
    private var isExpressCheckout = false
    private var isWarehouseProduct: Boolean = false
    private var hasShopAuthority: Boolean = false
    private var hasTopAdsActive: Boolean = false
    private var isShopOwner: Boolean = false
    private var isShopModerate: Boolean = false
    private var preOrder: PreOrder? = PreOrder()
    private var onSuccessGetCartType = false
    private var cartTypeData: CartTypeData? = null
    private var textWatcher: TextWatcher? = null
    private var quantityDebounceSubscription: Subscription? = null
    private var localQuantity: Int = 0
    private var tokonowButtonData: TokoNowButtonData? = null

    private val containerTokonowVar =
        view.findViewById<ConstraintLayout>(productdetailR.id.tokonow_button_container)
    private val btnTokonowVar =
        view.findViewById<UnifyButton>(productdetailR.id.btn_atc_tokonow_variant)
    private val txtTotalStockTokonowVar =
        view.findViewById<Typography>(productdetailR.id.txt_total_quantity)
    private val dividerTokonow = view.findViewById<View>(productdetailR.id.divider_button_quantity)
    private val txtProductNameTokonowVar =
        view.findViewById<Typography>(productdetailR.id.txt_product_name)

    private val icDeleteNonVar =
        view.findViewById<IconUnify>(productdetailR.id.btn_delete_tokonow_non_var)
    private val qtyButtonPdp =
        view.findViewById<QuantityEditorUnify>(productdetailR.id.qty_tokonow_non_var)
    private val btnChat = view.findViewById<UnifyButton>(productdetailR.id.btn_topchat)

    companion object {
        fun build(view: View, buttonListener: PartialButtonActionListener) =
            PartialButtonActionView(view, buttonListener)

        private const val QUANTITY_REGEX = "[^0-9]"
        private const val TEXTWATCHER_QUANTITY_DEBOUNCE_TIME = 500L
        private const val TEXTWATCHER_QUANTITY_RESET_DEBOUNCE_TIME = 1000L
        private const val DEFAULT_TOTAL_STOCK = 1
        private const val CART_REDIRECTION_BUTTON_COUNT = 2
    }

    init {
        btnChat.generateTopchatButtonPdp()
    }

    fun setButtonP1(preOrder: PreOrder?) {
        this.preOrder = preOrder
    }

    fun setTopAdsButton(hasTopAdsActive: Boolean) {
        this.hasTopAdsActive = hasTopAdsActive
        updateTopAdsButton()
    }

    fun renderData(
        isWarehouseProduct: Boolean,
        hasShopAuthority: Boolean,
        isShopOwner: Boolean,
        hasTopAdsActive: Boolean,
        tokonowButtonData: TokoNowButtonData? = null,
        cartTypeData: CartTypeData? = null,
        isShopModerate: Boolean
    ) {
        this.isWarehouseProduct = isWarehouseProduct
        this.hasShopAuthority = hasShopAuthority
        this.hasTopAdsActive = hasTopAdsActive
        this.cartTypeData = cartTypeData
        this.isShopOwner = isShopOwner
        this.isShopModerate = isShopModerate
        this.onSuccessGetCartType =
            cartTypeData != null && cartTypeData.availableButtonsPriority.isNotEmpty()
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

    private fun showTokoNowButton() = with(binding) {
        btnEmptyStock.hide()
        sellerButtonContainer.hide()
        btnBuyNow.hide()
        btnAddToCart.hide()

        if (tokonowButtonData?.isVariant == true) {
            showViewTokoNowVar()
            renderTokoNowVar()
        } else {
            showViewTokoNowNonVar()
            renderTokoNowNonVar(
                tokonowButtonData?.selectedMiniCart
                    ?: MiniCartItem.MiniCartItemProduct(),
                tokonowButtonData?.minQuantity ?: DEFAULT_MIN_QTY,
                tokonowButtonData?.maxQuantity ?: DEFAULT_ATC_MAX_ORDER
            )
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
        keyboardVisibleListener()
    }

    private fun keyboardVisibleListener() {
        view.onKeyboardVisibleListener(
            onShow = { rootView, keyboardHeight ->
                if (rootView.paddingBottom == keyboardHeight) return@onKeyboardVisibleListener
                rootView.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, keyboardHeight)
            },
            onHide = { rootView, _ ->
                if (rootView.paddingBottom == Int.ZERO) return@onKeyboardVisibleListener
                rootView.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
            }
        )
    }

    private fun renderTokoNowVar() = with(view) {
        val firstButton = cartTypeData?.availableButtonsPriority?.firstOrNull().orEmpty
            .copy(showRecommendation = true)
        btnTokonowVar.text = firstButton.buttonText
        btnTokonowVar.generateTheme(
            colorDescription = firstButton.color.ifBlank {
                ProductDetailCommonConstant.KEY_BUTTON_PRIMARY
            }
        )
        btnTokonowVar.setOnClickListener {
            buttonListener.buttonCartTypeClick(firstButton)
        }

        txtTotalStockTokonowVar.text = context.getString(
            productdetailR.string.pdp_pcs_builder,
            tokonowButtonData?.totalStockAtcVariant
                ?: DEFAULT_TOTAL_STOCK
        )
    }

    private fun renderTopChat(unavailableButton: List<String>) {
        btnChat.apply {
            showWithCondition(ProductDetailCommonConstant.KEY_CHAT !in unavailableButton)
            setOnClickListener {
                buttonListener.topChatButtonClicked()
            }
        }
    }

    private fun showCartTypeButton() {
        hideButtonEmptyAndTopAds()

        val buttonToRender = cartTypeData
            ?.availableButtons
            .orEmpty()
            .take(CART_REDIRECTION_BUTTON_COUNT)
        renderNormalButtonCartRedirection(buttonToRender)
        buttonListener.onButtonsShowed(buttonToRender.map { it.cartType })

        val unavailableButton = cartTypeData?.unavailableButtons ?: listOf()
        renderTopChat(unavailableButton)
    }

    private fun renderNormalButtonCartRedirection(
        buttonToRender: List<AvailableButton>
    ) = with(binding) {
        qtyButtonPdp.hide()

        val buyNowData = buttonToRender.firstOrNull().orEmpty
        val addToCartData = buttonToRender.getOrNull(Int.ONE).orEmpty
        val shouldParallelLoading =
            buyNowData.cartType.isNotBlank() && addToCartData.cartType.isNotBlank()

        with(btnBuyNow) {
            showWithCondition(buyNowData.cartType.isNotBlank())
            btnBuyNow.generateTheme(buyNowData.color)

            text = buyNowData.text
            isParallelLoading = shouldParallelLoading
            btnBuyNow.setOnClickListener {
                buttonListener.buttonCartTypeClick(buyNowData)
            }
        }

        with(btnAddToCart) {
            showWithCondition(addToCartData.cartType.isNotBlank())
            generateTheme(addToCartData.color)
            text = addToCartData.text
            isParallelLoading = shouldParallelLoading
            btnAddToCart.setOnClickListener {
                buttonListener.buttonCartTypeClick(addToCartData)
            }
        }
    }

    private fun renderTokoNowNonVar(
        selectedMiniCart: MiniCartItem.MiniCartItemProduct,
        minQuantity: Int,
        maxQuantity: Int
    ) = with(view) {
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

    private fun initTextWatcherDebouncer(
        minQuantity: Int,
        maxQuantity: Int,
        selectedMiniCart: MiniCartItem.MiniCartItemProduct
    ) {
        quantityDebounceSubscription = Observable.create(
            Observable.OnSubscribe<Int> { subscriber ->
                textWatcher = object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val quantityInt: Int =
                            s.toString().replace(QUANTITY_REGEX.toRegex(), "").toIntOrZero()
                        subscriber.onNext(quantityInt)
                    }
                }
                qtyButtonPdp?.editText?.addTextChangedListener(textWatcher)
            }
        )
            .debounce {
                if (it < minQuantity) {
                    // Use longer debounce when reset qty, to support automation
                    Observable.just(it)
                        .delay(TEXTWATCHER_QUANTITY_RESET_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                } else {
                    Observable.just(it)
                        .delay(TEXTWATCHER_QUANTITY_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Int>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    Timber.d(e)
                }

                override fun onNext(quantity: Int) {
                    onNextValueQuantity(quantity, minQuantity, maxQuantity, selectedMiniCart)
                }
            })

        buttonListener.getRxCompositeSubcription().add(quantityDebounceSubscription)
    }

    private fun onNextValueQuantity(
        quantity: Int,
        minQuantity: Int,
        maxQuantity: Int,
        selectedMiniCart: MiniCartItem.MiniCartItemProduct
    ) {
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
                    // fire again to update + and - button
                    setValue(localQuantity)
                }
            }
        }
    }

    private fun showNewCheckoutButton() {
        with(binding) {
            qtyButtonPdp.hide()
            hideButtonEmptyAndTopAds()

            btnBuyNow.apply {
                val button = AvailableButton.createBuyNowButton(context = context)
                text = button.text

                setOnClickListener {
                    if (hasComponentLoading) return@setOnClickListener
                    buttonListener.onButtonFallbackClick(button)
                }

                generateTheme(button.color)
                show()
            }

            btnAddToCart.apply {
                val button = AvailableButton.createAddToCartButton(context = context)
                text = button.text
                setOnClickListener {
                    if (hasComponentLoading) return@setOnClickListener
                    buttonListener.onButtonFallbackClick(button)
                }
                generateTheme(button.color)
                show()
            }

            btnChat.apply {
                setOnClickListener {
                    buttonListener.topChatButtonClicked()
                }
                show()
            }
        }
    }

    private fun hideButtonEmptyAndTopAds() = with(binding) {
        btnEmptyStock.hide()
        sellerButtonContainer.hide()
        containerTokonowVar.hide()
    }

    private fun showShopManageButton() {
        with(binding) {
            val context = view.context
            containerTokonowVar.hide()
            btnEmptyStock.hide()
            btnChat.hide()
            btnBuyNow.hide()
            btnAddToCart.hide()
            qtyButtonPdp.hide()

            sellerButtonContainer.show()
            if (hasTopAdsActive) {
                btnTopAds.setOnClickListener { buttonListener.rincianTopAdsClicked() }
                btnTopAds.text = context.getString(productdetailR.string.pdp_rincian_topads)
            } else {
                btnTopAds.setOnClickListener { buttonListener.advertiseProductClicked() }
                btnTopAds.text = context.getString(productdetailR.string.pdp_promote_topads)
            }

            shopModeratedManageButton()
        }
    }

    private fun shopModeratedManageButton() {
        with(binding) {
            if (isShopModerate) {
                btnTopAds.isEnabled = false
                btnEditProduct.isEnabled = false
                btnEditProduct.setOnClickListener(null)
                btnTopAds.setOnClickListener(null)
                textShopModerated.show()
            } else {
                btnEditProduct.isEnabled = true
                btnTopAds.isEnabled = true
                textShopModerated.gone()
                btnEditProduct.setOnClickListener { buttonListener.editProductButtonClicked() }
            }
        }
    }

    private fun showNoStockButton() {
        with(binding) {
            sellerButtonContainer.hide()
            containerTokonowVar.hide()
            btnEmptyStock.show()
            qtyButtonPdp.hide()
            btnChat.apply {
                showWithCondition(!isShopOwner)
                setOnClickListener { buttonListener.topChatButtonClicked() }
            }
        }
    }

    fun showLoading() {
        setButtonToLoading(isLoading = true)
    }

    fun hideLoading() {
        setButtonToLoading(isLoading = false)
    }

    private fun setButtonToLoading(isLoading: Boolean) = with(binding) {
        btnBuyNow.setLoading(isLoading = isLoading)
        btnAddToCart.setLoading(isLoading = isLoading)
        btnTokonowVar.setLoading(isLoading = isLoading)
    }

    private fun UnifyButton.setLoading(isLoading: Boolean) {
        postOnAnimation {
            if (!isVisible) return@postOnAnimation
            this.isClickable = !isLoading
            this.isLoading = isLoading
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
        binding.baseBtnAction.gone()
    }

    fun setBackground(resource: Int) {
        binding.baseBtnAction.setBackgroundResource(resource)
    }

    fun setBackground(drawable: Drawable) {
        binding.baseBtnAction.background = drawable
    }
}

data class TokoNowButtonData(
    // var
    val totalStockAtcVariant: Int = 0,
    val productTitle: String = "",
    val isVariant: Boolean = false,

    // non var
    val minQuantity: Int = DEFAULT_MIN_QTY,
    val maxQuantity: Int = DEFAULT_ATC_MAX_ORDER,
    val selectedMiniCart: MiniCartItem.MiniCartItemProduct? = null
)
