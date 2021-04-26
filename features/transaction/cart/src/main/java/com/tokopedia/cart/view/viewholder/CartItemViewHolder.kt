package com.tokopedia.cart.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.*
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.HolderItemCartNewBinding
import com.tokopedia.cart.domain.model.cartlist.ActionData
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_DELETE
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_NOTES
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_WISHLIST
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_WISHLISTED
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher.TEXTWATCHER_QUANTITY_DEBOUNCE_TIME
import com.tokopedia.purchase_platform.common.utils.QuantityWrapper
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author anggaprasetiyo on 13/03/18.
 */
@SuppressLint("ClickableViewAccessibility")
class CartItemViewHolder constructor(private val binding: HolderItemCartNewBinding,
                                     private val compositeSubscription: CompositeSubscription,
                                     private var actionListener: CartItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    private var context: Context? = null
    private var viewHolderListener: ViewHolderListener? = null

    private var cartItemHolderData: CartItemHolderData? = null
    private var quantityTextWatcher: QuantityTextWatcher? = null
    private var quantityTextwatcherListener: QuantityTextWatcher.QuantityTextwatcherListener? = null
    private var parentPosition: Int = 0
    private var dataSize: Int = 0
    private var quantityDebounceSubscription: Subscription? = null
    private var noteDebounceSubscription: Subscription? = null
    private var cbChangeJob: Job? = null
    private var informationLabel: MutableList<String> = mutableListOf()

    init {
        context = itemView.context

        setNoteTouchListener()

        initTextWatcherDebouncer(compositeSubscription)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setNoteTouchListener() {
        binding.etRemark.setOnTouchListener { view, event ->
            if (view.id == R.id.et_remark) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }

    fun clear() {
        context = null
        actionListener = null
        viewHolderListener = null
        compositeSubscription.remove(quantityDebounceSubscription)
        compositeSubscription.remove(noteDebounceSubscription)
    }

    private fun initTextWatcherDebouncer(compositeSubscription: CompositeSubscription) {
        quantityDebounceSubscription = Observable.create(Observable.OnSubscribe<QuantityWrapper> { subscriber ->
            quantityTextwatcherListener = QuantityTextWatcher.QuantityTextwatcherListener { quantity -> subscriber.onNext(quantity) }
        })
                .debounce(TEXTWATCHER_QUANTITY_DEBOUNCE_TIME.toLong(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<QuantityWrapper>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }

                    override fun onNext(quantity: QuantityWrapper) {
                        itemQuantityTextWatcherAction(quantity)
                    }
                })

        compositeSubscription.add(quantityDebounceSubscription)
    }

    fun bindData(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener, dataSize: Int) {
        this.viewHolderListener = viewHolderListener
        this.parentPosition = parentPosition
        cartItemHolderData = data
        this.dataSize = dataSize

        renderProductInfo(data, parentPosition)
        renderSelection(data, parentPosition)
        renderQuantity(data, parentPosition, viewHolderListener)
        renderDefaultActionState()
        renderProductAction(data, viewHolderListener)
    }

    private fun renderDefaultActionState() {
        with(binding) {
            llShopNoteSection.gone()
            textMoveToWishlist.gone()
            btnDeleteCart.gone()
        }
    }

    private fun renderProductAction(data: CartItemHolderData, viewHolderListener: ViewHolderListener) {
        if (data.actionsData.isNotEmpty()) {
            data.actionsData.forEach {
                when (it.id) {
                    ACTION_NOTES -> {
                        renderActionNotes(data, parentPosition, viewHolderListener)
                    }
                    ACTION_WISHLIST, ACTION_WISHLISTED -> {
                        renderActionWishlist(it, data)
                    }
                    ACTION_DELETE -> {
                        renderActionDelete(data)
                    }
                }
            }
        }
    }

    private fun renderActionDelete(data: CartItemHolderData) {
        binding.btnDeleteCart.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data)
            }
        }
        binding.btnDeleteCart.show()
    }

    private fun renderSelection(data: CartItemHolderData, parentPosition: Int) {
        val cbSelectItem = binding.cbSelectItem
        cbSelectItem.isEnabled = data.cartItemData?.isError == false
        cbSelectItem.isChecked = data.cartItemData?.isError == false && data.isSelected
        cbSelectItem.skipAnimation()

        var prevIsChecked: Boolean = cbSelectItem.isChecked
        cbSelectItem.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != prevIsChecked) {
                prevIsChecked = isChecked

                cbChangeJob?.cancel()
                cbChangeJob = GlobalScope.launch(Dispatchers.Main) {
                    delay(500L)
                    if (isChecked == prevIsChecked && isChecked != data.isSelected) {
                        if (data.cartItemData?.isError == false) {
                            data.isSelected = isChecked
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                actionListener?.onCartItemCheckChanged(adapterPosition, parentPosition, data.isSelected)
                                viewHolderListener?.onNeedToRefreshSingleShop(parentPosition)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderProductInfo(data: CartItemHolderData, parentPosition: Int) {
        renderProductName(data)
        renderImage(data)
        renderPrice(data)
        renderVariant(data)
        renderWarningMessage(data)
        renderSlashPrice(data)
        renderProductProperties(data)
        renderProductPropertyIncidentLabel(data)

        sendAnalyticsInformationLabel(data)

        binding.holderItemCartDivider.visibility = if (layoutPosition == dataSize - 1) View.GONE else View.VISIBLE
    }

    private fun renderProductName(data: CartItemHolderData) {
        binding.textProductName.text = Html.fromHtml(data.cartItemData?.originData?.productName ?: "")
        binding.textProductName.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))
    }

    private fun renderImage(data: CartItemHolderData) {
        data.cartItemData?.originData?.productImage?.let {
            binding.iuImageProduct.loadImage(it)
        }
        binding.iuImageProduct.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))
    }

    private fun sendAnalyticsInformationLabel(data: CartItemHolderData) {
        if (informationLabel.isNotEmpty()) {
            sendAnalyticsShowInformation(informationLabel, data.cartItemData?.originData?.productId
                    ?: "")
        }
    }

    private fun renderProductProperties(data: CartItemHolderData) {
        val layoutProductInfo = binding.layoutProductInfo
        layoutProductInfo.gone()
        val productInformationList = data.cartItemData?.originData?.productInformation
        if (productInformationList?.isNotEmpty() == true) {
            layoutProductInfo.removeAllViews()
            productInformationList.forEach {
                var tmpLabel = it
                if (tmpLabel.toLowerCase(Locale.getDefault()).contains(LABEL_CASHBACK)) {
                    tmpLabel = LABEL_CASHBACK
                }
                informationLabel.add(tmpLabel.toLowerCase(Locale.getDefault()))

                val productInfo = createProductInfoText(it)
                layoutProductInfo.addView(productInfo)
            }
            layoutProductInfo.show()
        }

        if (data.cartItemData?.originData?.wholesalePrice ?: 0 > 0) {
            val wholesaleLabel = itemView.context.getString(R.string.label_wholesale_product)
            val productInfo = createProductInfoText(wholesaleLabel)
            layoutProductInfo.addView(productInfo)
            layoutProductInfo.show()
            informationLabel.add(wholesaleLabel.toLowerCase(Locale.getDefault()))
        }
    }

    private fun createProductInfoText(it: String): Typography {
        return Typography(itemView.context).apply {
            setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N700_68))
            setType(Typography.BODY_3)
            text = if (binding.layoutProductInfo.childCount > 0) ", $it" else it
        }
    }

    private fun sendAnalyticsShowInformation(informationList: List<String>, productId: String) {
        val informations = informationList.joinToString(", ")
        actionListener?.onCartItemShowInformationLabel(productId, informations)
    }

    private fun renderProductPropertyIncidentLabel(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.productAlertMessage?.isNotEmpty() == true) {
            binding.textIncident.text = data.cartItemData?.originData?.productAlertMessage
            binding.textIncident.show()
        } else {
            binding.textIncident.gone()
        }
    }

    private fun renderPrice(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.wholesalePriceFormatted != null) {
            binding.textProductPrice.text = data.cartItemData?.originData?.wholesalePriceFormatted ?: ""
        } else {
            binding.textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    data.cartItemData?.originData?.pricePlan
                            ?: 0.toDouble(), false).removeDecimalSuffix()
        }
    }

    private fun renderSlashPrice(data: CartItemHolderData) {
        val hasPriceOriginal = data.cartItemData?.originData?.priceOriginal != 0L
        val hasWholesalePrice = data.cartItemData?.originData?.wholesalePrice != 0L
        val hasPriceDrop = data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0 > 0 &&
                data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0 > data.cartItemData?.originData?.pricePlan?.toLong() ?: 0
        if (hasPriceOriginal || hasWholesalePrice || hasPriceDrop) {
            if (data.cartItemData?.originData?.slashPriceLabel?.isNotBlank() == true) {
                // Slash price
                renderSlashPriceFromCampaign(data)
            } else if (data.cartItemData?.originData?.initialPriceBeforeDrop != 0L) {
                val wholesalePrice = data.cartItemData?.originData?.wholesalePrice ?: 0
                if (wholesalePrice > 0 && wholesalePrice.toDouble() < data.cartItemData?.originData?.pricePlan ?: 0.0) {
                    // Wholesale
                    renderSlashPriceFromWholesale(data)
                } else {
                    // Price drop
                    renderSlashPriceFromPriceDrop(data)
                }
            } else if (data.cartItemData?.originData?.wholesalePrice != 0L) {
                // Wholesale
                renderSlashPriceFromWholesale(data)
            }

            binding.textSlashPrice.paintFlags = binding.textSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.textSlashPrice.show()
        } else {
            binding.textSlashPrice.gone()
            binding.labelSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromWholesale(data: CartItemHolderData) {
        val priceDropValue = data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0
        val pricePlan = data.cartItemData?.originData?.pricePlanInt ?: 0
        val originalPrice = if (priceDropValue > pricePlan) pricePlan else priceDropValue
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromPriceDrop(data: CartItemHolderData) {
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.cartItemData?.originData?.initialPriceBeforeDrop
                ?: 0, false).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromCampaign(data: CartItemHolderData) {
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.cartItemData?.originData?.priceOriginal
                ?: 0, false).removeDecimalSuffix()
        binding.labelSlashPricePercentage.text = data.cartItemData?.originData?.slashPriceLabel
        binding.labelSlashPricePercentage.show()
        informationLabel.add(LABEL_DISCOUNT)
    }

    private fun renderWarningMessage(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.warningMessage?.isNotBlank() == true) {
            binding.textQtyLeft.text = data.cartItemData?.originData?.warningMessage ?: ""
            binding.textQtyLeft.show()
            actionListener?.onCartItemShowRemainingQty(data.cartItemData?.originData?.productId
                    ?: "")
        } else {
            binding.textQtyLeft.gone()
        }
    }

    private fun renderVariant(data: CartItemHolderData) {
        var paddingRight = 0
        val paddingTop = itemView.resources.getDimensionPixelOffset(R.dimen.dp_2)
        val textProductVariant = binding.textProductVariant
        if (data.cartItemData?.originData?.variant?.isNotBlank() == true) {
            textProductVariant.text = data.cartItemData?.originData?.variant
            textProductVariant.show()
            paddingRight = itemView.resources.getDimensionPixelOffset(R.dimen.dp_4)
        } else {
            if (data.cartItemData?.originData?.warningMessage?.isNotBlank() == true) {
                textProductVariant.text = ""
                textProductVariant.invisible()
            } else {
                textProductVariant.gone()
            }
        }
        textProductVariant.setPadding(0, paddingTop, paddingRight, 0);
    }

    private fun renderActionNotes(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener) {
        binding.etRemark.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                data.isStateNotesOnFocuss = false
                actionListener?.onEditNoteDone(parentPosition)
                KeyboardHandler.DropKeyboard(binding.etRemark.context, itemView)
                true
            } else false
        }

        binding.tvLabelRemarkOption.setOnClickListener {
            if (data.cartItemData?.isError == false) {
                data.isStateNotesOnFocuss = true
                binding.etRemark.requestFocus()
                actionListener?.onCartItemLabelInputRemarkClicked()
                if (binding.tvLabelRemarkOption.text == binding.tvLabelRemarkOption.context.getString(
                                R.string.label_button_change_note)) {
                    var remark = data.cartItemData?.updatedData?.remark
                    remark += " "
                    data.cartItemData?.updatedData?.remark = remark
                }
                data.isStateHasNotes = true
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    viewHolderListener.onNeedToRefreshSingleProduct(adapterPosition)
                }
            }
        }

        data.isStateHasNotes = data.isStateNotesOnFocuss || data.cartItemData?.updatedData?.remark?.isNotBlank() == true

        renderNotesViews(data)

        binding.etRemark.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(data.cartItemData?.updatedData?.maxCharRemark
                ?: 0))
        binding.etRemark.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    itemNoteTextWatcherAction(it)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                charSequence?.let {
                    binding.tvNoteCharCounter.text = "${charSequence.length}/${data.cartItemData?.updatedData?.maxCharRemark ?: 0}"
                }
            }
        })

        binding.llShopNoteSection.show()
    }

    private fun renderNotesViews(data: CartItemHolderData) {
        with(binding) {
            if (data.isStateHasNotes) {
                // Has a notes from pdp or not at all but click add notes button
                if (data.isStateNotesOnFocuss && (data.cartItemData?.originData?.originalRemark.isNullOrBlank() || data.cartItemData?.updatedData?.remark != data.cartItemData?.originData?.originalRemark)) {
                    // Notes is empty after click add notes button or has value after use click change notes button
                    tvRemark.visibility = View.GONE
                    etRemark.setText(Utils.getHtmlFormat(data.cartItemData?.updatedData?.remark))
                    etRemark.visibility = View.VISIBLE
                    tvLabelRemarkTitle.visibility = View.VISIBLE
                    etRemark.setSelection(etRemark.length())
                    tvLabelRemarkOption.visibility = View.GONE
                    tvNoteCharCounter.visibility = View.VISIBLE
                    tvLabelRemarkOption.setPadding(0, 0, 0, 0)
                } else {
                    // Has notes from pdp
                    etRemark.visibility = View.GONE
                    tvLabelRemarkTitle.visibility = View.GONE
                    tvRemark.text = Utils.getHtmlFormat(data.cartItemData?.updatedData?.remark
                            ?: "")
                    tvRemark.visibility = View.VISIBLE
                    tvLabelRemarkOption.visibility = View.VISIBLE
                    tvNoteCharCounter.visibility = View.GONE
                    tvLabelRemarkOption.text = tvLabelRemarkOption.context.getString(R.string.label_button_change_note)
                    tvLabelRemarkOption.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)

                    setNotesWidth()
                }
                tvLabelRemarkOption.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_G500))
            } else {
                // No notes at all
                etRemark.visibility = View.GONE
                tvLabelRemarkTitle.visibility = View.GONE
                tvRemark.visibility = View.GONE
                tvNoteCharCounter.visibility = View.GONE
                tvLabelRemarkOption.text = tvLabelRemarkOption.context.getString(R.string.label_button_add_note)
                tvLabelRemarkOption.visibility = View.VISIBLE
                etRemark.setText("")
                tvLabelRemarkOption.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_G500))
                tvLabelRemarkOption.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun setNotesWidth() {
        val padding = itemView.resources.getDimensionPixelOffset(R.dimen.dp_16)
        val paddingLeftRight = padding * 2
        val remarkOptionWidth = itemView.resources.getDimensionPixelOffset(R.dimen.dp_40)
        val screenWidth = getScreenWidth()
        val maxNotesWidth = screenWidth - paddingLeftRight
        val noteWidth = maxNotesWidth - remarkOptionWidth

        binding.tvRemark.measure(0, 0)
        val currentWidth = binding.tvRemark.measuredWidth
        if (currentWidth >= maxNotesWidth) {
            binding.tvRemark.layoutParams.width = noteWidth
            binding.tvRemark.requestLayout()
        }
    }

    private fun renderQuantity(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener) {
        val qtyEditorCart = binding.qtyEditorCart
        if (quantityTextWatcher != null) {
            // remove previous listener
            qtyEditorCart.editText.removeTextChangedListener(quantityTextWatcher)
        }

        qtyEditorCart.autoHideKeyboard = true
        qtyEditorCart.minValue = data.cartItemData?.originData?.minOrder ?: 0
        qtyEditorCart.maxValue = data.cartItemData?.originData?.maxOrder ?: 0
        // reset listener
        qtyEditorCart.setValueChangedListener { _, _, _ -> /* no-op */ }
        qtyEditorCart.setValue(data.cartItemData?.updatedData?.quantity ?: 0)
        qtyEditorCart.setValueChangedListener { newValue, _, _ ->
            cartItemHolderData?.cartItemData?.updatedData?.quantity = newValue
            actionListener?.onCartItemQuantityChangedThenHitUpdateCartAndValidateUse()
            cartItemHolderData?.let { handleRefreshType(it, viewHolderListener, parentPosition) }
        }
        qtyEditorCart.setSubstractListener {
            if (data.cartItemData?.isError == false && adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
                actionListener?.onCartItemQuantityMinusButtonClicked()
            }
        }
        qtyEditorCart.setAddClickListener {
            if (data.cartItemData?.isError == false && adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
                actionListener?.onCartItemQuantityPlusButtonClicked()
            }
        }
        qtyEditorCart.editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val qtyStr = (v as? AppCompatEditText)?.text?.toString()
                actionListener?.onCartItemQuantityInputFormClicked(
                        if (!TextUtils.isEmpty(qtyStr)) qtyStr else ""
                )
            }
        }
        qtyEditorCart.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.DropKeyboard(binding.qtyEditorCart.editText.context, itemView)
                true
            } else false
        }
        quantityTextWatcher = QuantityTextWatcher(quantityTextwatcherListener)
        qtyEditorCart.editText.addTextChangedListener(quantityTextWatcher)
        qtyEditorCart.editText.isEnabled = data.cartItemData?.isError == false
    }

    private fun handleRefreshType(data: CartItemHolderData, viewHolderListener: ViewHolderListener?, parentPosition: Int) {
        if (data.cartItemData?.originData?.wholesalePriceData != null && data.cartItemData?.originData?.wholesalePriceData?.size ?: 0 > 0) {
            if (data.cartItemData?.originData?.isPreOrder == true) {
                viewHolderListener?.onNeedToRefreshAllShop()
            } else {
                viewHolderListener?.onNeedToRefreshSingleShop(parentPosition)
            }
        } else {
            viewHolderListener?.onNeedToRefreshSingleProduct(adapterPosition)
        }
    }

    private fun renderActionWishlist(action: ActionData, data: CartItemHolderData) {
        val textMoveToWishlist = binding.textMoveToWishlist
        if (data.cartItemData?.originData?.isWishlisted == true && action.id == ACTION_WISHLISTED) {
            textMoveToWishlist.text = action.message
            textMoveToWishlist.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N700_44))
            textMoveToWishlist.setOnClickListener { }
        } else if (data.cartItemData?.originData?.isWishlisted == false && action.id == ACTION_WISHLIST) {
            textMoveToWishlist.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N700_68))
            textMoveToWishlist.setOnClickListener {
                actionListener?.onWishlistCheckChanged(data.cartItemData?.originData?.productId, data.cartItemData?.originData?.cartId
                        ?: 0, binding.iuImageProduct)
            }
        }
        textMoveToWishlist.show()
    }

    private fun getOnClickProductItemListener(
            @SuppressLint("RecyclerView") position: Int, parentPosition: Int,
            data: CartItemHolderData): View.OnClickListener {
        return View.OnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemProductClicked(data.cartItemData)
            }
        }
    }

    private fun itemNoteTextWatcherAction(editable: Editable) {
        if (cartItemHolderData != null) {
            cartItemHolderData?.cartItemData?.updatedData?.remark = editable.toString()
        }
    }

    private fun itemQuantityTextWatcherAction(quantity: QuantityWrapper) {
        if (adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
            val qty = quantity.editable.toString().replace(QUANTITY_REGEX.toRegex(), "").toIntOrZero()
            val needToUpdateView = cartItemHolderData?.cartItemData?.updatedData?.quantity != qty
            if (needToUpdateView) {
                if (qty <= 0) {
                    actionListener?.onCartItemQuantityReseted(adapterPosition, parentPosition)
                }
                binding.qtyEditorCart.setValue(qty)
            }
        }
    }

    interface ViewHolderListener {

        fun onNeedToRefreshSingleProduct(childPosition: Int)

        fun onNeedToRefreshSingleShop(parentPosition: Int)

        fun onNeedToRefreshAllShop()

    }

    companion object {
        val TYPE_VIEW_ITEM_CART = R.layout.holder_item_cart_new

        const val LABEL_CASHBACK = "cashback"
        const val LABEL_DISCOUNT = "label diskon"

        private const val QUANTITY_REGEX = "[^0-9]"
    }
}
