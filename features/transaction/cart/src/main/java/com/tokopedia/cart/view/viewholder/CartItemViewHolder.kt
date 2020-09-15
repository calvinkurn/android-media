package com.tokopedia.cart.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.*
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.domain.model.cartlist.ActionData
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_DELETE
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_NOTES
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_WISHLIST
import com.tokopedia.cart.domain.model.cartlist.ActionData.Companion.ACTION_WISHLISTED
import com.tokopedia.cart.view.adapter.CartItemAdapter
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher.TEXTWATCHER_QUANTITY_DEBOUNCE_TIME
import com.tokopedia.purchase_platform.common.utils.QuantityWrapper
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.*
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author anggaprasetiyo on 13/03/18.
 */
class CartItemViewHolder constructor(itemView: View,
                                     private val compositeSubscription: CompositeSubscription,
                                     private var actionListener: CartItemAdapter.ActionListener?) : RecyclerView.ViewHolder(itemView) {

    private var context: Context? = null
    private var viewHolderListener: ViewHolderListener? = null

    private val llWarningAndError: LinearLayout
    private val flCartItemContainer: FrameLayout
    private val cbSelectItem: CheckBox
    private val ivProductImage: ImageView

    private val textProductName: Typography
    private val textProductVariant: Typography
    private val textQtyLeft: Typography
    private val textProductPrice: TextView
    private val labelSlashPricePercentage: Label
    private val textSlashPrice: Typography
    private val textWholesalePrice: Typography
    private val textPriceDrop: Typography
    private val textCashback: Typography
    private val textIncidentLabel: Typography

    private val textMoveToWishlist: Typography

    private val etQty: AppCompatEditText
    private val btnQtyPlus: ImageView
    private val btnQtyMinus: ImageView
    private val etRemark: AppCompatEditText
    private val tvLabelRemarkOption: TextView
    private val btnDelete: ImageView
    private val tvErrorFormValidation: TextView
    private val tvErrorFormRemarkValidation: TextView

    private val layoutError: LinearLayout
    private val tickerError: Ticker
    private val layoutWarning: LinearLayout
    private val tickerWarning: Ticker

    private val tvNoteCharCounter: TextView
    private val tvRemark: TextView
    private val divider: View
    private val rlProductAction: RelativeLayout
    private val llShopNoteSection: LinearLayout

    private var cartItemHolderData: CartItemHolderData? = null
    private var quantityTextwatcherListener: QuantityTextWatcher.QuantityTextwatcherListener? = null
    private var parentPosition: Int = 0
    private var dataSize: Int = 0
    private var quantityDebounceSubscription: Subscription? = null
    private var noteDebounceSubscription: Subscription? = null
    private var cbChangeJob: Job? = null
    private var informationLabel: MutableList<String> = mutableListOf()

    init {
        context = itemView.context

        llWarningAndError = itemView.findViewById(R.id.ll_warning_and_error)
        flCartItemContainer = itemView.findViewById(R.id.fl_cart_item_container)
        cbSelectItem = itemView.findViewById(R.id.cb_select_item)
        tvErrorFormValidation = itemView.findViewById(R.id.tv_error_form_validation)
        tvErrorFormRemarkValidation = itemView.findViewById(R.id.tv_error_form_remark_validation)
        ivProductImage = itemView.findViewById(R.id.iv_image_product)

        textProductName = itemView.findViewById(R.id.text_product_name)
        textProductVariant = itemView.findViewById(R.id.text_product_variant)
        textQtyLeft = itemView.findViewById(R.id.text_qty_left)
        textProductPrice = itemView.findViewById(R.id.text_product_price)
        labelSlashPricePercentage = itemView.findViewById(R.id.label_slash_price_percentage)
        textSlashPrice = itemView.findViewById(R.id.text_slash_price)
        textWholesalePrice = itemView.findViewById(R.id.text_wholesale_price)
        textPriceDrop = itemView.findViewById(R.id.text_price_drop)
        textCashback = itemView.findViewById(R.id.text_cashback)
        textIncidentLabel = itemView.findViewById(R.id.text_incident)

        textMoveToWishlist = itemView.findViewById(R.id.text_move_to_wishlist)

        etQty = itemView.findViewById(R.id.et_qty)
        btnQtyPlus = itemView.findViewById(R.id.btn_qty_plus)
        btnQtyMinus = itemView.findViewById(R.id.btn_qty_min)
        tvLabelRemarkOption = itemView.findViewById(R.id.tv_label_remark_option)
        etRemark = itemView.findViewById(R.id.et_remark)
        btnDelete = itemView.findViewById(R.id.btn_delete_cart)

        layoutError = itemView.findViewById(R.id.layout_error)
        tickerError = itemView.findViewById(R.id.ticker_error)
        layoutWarning = itemView.findViewById(R.id.layout_warning)
        tickerWarning = itemView.findViewById(R.id.ticker_warning)

        tvNoteCharCounter = itemView.findViewById(R.id.tv_note_char_counter)
        tvRemark = itemView.findViewById(R.id.tv_remark)
        divider = itemView.findViewById(R.id.holder_item_cart_divider)
        rlProductAction = itemView.findViewById(R.id.rl_product_action)
        llShopNoteSection = itemView.findViewById(R.id.ll_shop_note_section)

        etRemark.setOnTouchListener { view, event ->
            if (view.id == R.id.et_remark) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        initTextWatcherDebouncer(compositeSubscription)
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
                        e.printStackTrace()
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
        renderWarningAndError(data)
        renderSelection(data, parentPosition)
        renderQuantity(data, parentPosition, viewHolderListener)
        renderDefaultActionState()
        renderProductAction(data, viewHolderListener)
    }

    private fun renderDefaultActionState() {
        llShopNoteSection.gone()
        textMoveToWishlist.gone()
        btnDelete.gone()
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
        btnDelete.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data, adapterPosition, parentPosition)
            }
        }
        btnDelete.show()
    }

    private fun renderSelection(data: CartItemHolderData, parentPosition: Int) {
        cbSelectItem.isEnabled = data.cartItemData?.isError == false
        cbSelectItem.isChecked = data.cartItemData?.isError == false && data.isSelected

        var prevIsChecked: Boolean = cbSelectItem.isChecked
        cbSelectItem.setOnCheckedChangeListener { buttonView, isChecked ->
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
                                viewHolderListener?.onNeedToRefreshAllShop()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderWarningAndError(data: CartItemHolderData) {
        // Initial action state
        rendercartItemActionOnNormalProduct()
        if (data.cartItemData?.isParentHasErrorOrWarning == true) {
            if (data.cartItemData?.isDisableAllProducts == false || data.cartItemData?.isError == true || data.cartItemData?.isWarning == true) {
                renderErrorItemHeader(data)
                renderWarningItemHeader(data)
                setWarningAndErrorVisibility(data)
            } else {
                disableView(data)
            }
        } else {
            if (data.cartItemData?.isSingleChild == false) {
                renderErrorItemHeader(data)
                renderWarningItemHeader(data)
                setWarningAndErrorVisibility(data)
            } else {
                disableView(data)
            }
        }
    }

    private fun setWarningAndErrorVisibility(data: CartItemHolderData) {
        if ((!TextUtils.isEmpty(data.cartItemData?.errorMessageTitle) || !TextUtils.isEmpty(data.cartItemData?.warningMessageTitle)) && (data.cartItemData?.isError == true || data.cartItemData?.isWarning == true)) {
            llWarningAndError.visibility = View.VISIBLE
        } else {
            llWarningAndError.visibility = View.GONE
        }
    }

    private fun disableView(data: CartItemHolderData) {
        if (data.cartItemData?.isError == true) {
            flCartItemContainer.foreground = ContextCompat.getDrawable(flCartItemContainer.context, R.drawable.fg_disabled_item)
        } else {
            flCartItemContainer.foreground = ContextCompat.getDrawable(flCartItemContainer.context, R.drawable.fg_enabled_item)
        }
        llWarningAndError.visibility = View.GONE
    }

    private fun renderProductInfo(data: CartItemHolderData, parentPosition: Int) {
        textProductName.text = Html.fromHtml(data.cartItemData?.originData?.productName ?: "")
        ImageHandler.loadImageRounded2(
                this.itemView.context, this.ivProductImage,
                data.cartItemData?.originData?.productImage
        )

        renderPrice(data)
        renderVariant(data)
        renderWarningMessage(data)
        renderSlashPrice(data)
        renderProductProperties(data)

        setClickListener(parentPosition, data)

        divider.visibility = if (layoutPosition == dataSize - 1) View.GONE else View.VISIBLE
    }

    private fun renderProductProperties(data: CartItemHolderData) {
        // Render from the last information label
        // Level 4
        renderProductPropertyWholesalePrice(data)
        renderProductPropertyPriceDrop(data)
        renderProductPropertyCashback(data)

        // Level 5
        renderProductPropertyIncidentLabel(data)

        sendAnalyticsShowInformation(informationLabel, data.cartItemData?.originData?.productId
                ?: "")
    }

    private fun sendAnalyticsShowInformation(informationList: List<String>, productId: String) {
        val informations = informationList.joinToString(", ")
        actionListener?.onCartItemShowInformationLabel(productId, informations)
    }

    private fun renderProductPropertyIncidentLabel(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.productAlertMessage?.isNotEmpty() == true) {
            textIncidentLabel.text = data.cartItemData?.originData?.productAlertMessage
            textIncidentLabel.show()
        } else {
            textIncidentLabel.gone()
        }
    }

    private fun renderProductPropertyWholesalePrice(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.wholesalePrice ?: 0 > 0) {
            textWholesalePrice.text = "Harga Grosir"
            textWholesalePrice.show()
            informationLabel.add("harga grosir")
        } else {
            textWholesalePrice.gone()
        }
    }

    private fun renderProductPropertyPriceDrop(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0 > 0 &&
                data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0 > data.cartItemData?.originData?.pricePlan?.toInt() ?: 0) {
            if (textWholesalePrice.visibility == View.VISIBLE) {
                textPriceDrop.text = "Harga Turun, "
            } else {
                textPriceDrop.text = "Harga Turun"
            }
            textPriceDrop.show()
            informationLabel.add("harga turun")
        } else {
            textPriceDrop.gone()
        }
    }

    private fun renderProductPropertyCashback(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.productCashBack?.isNotBlank() == true) {
            if (textWholesalePrice.visibility == View.VISIBLE || textPriceDrop.visibility == View.VISIBLE) {
                textCashback.text = "${data.cartItemData?.originData?.cashBackInfo ?: ""}, "
            } else {
                textCashback.text = data.cartItemData?.originData?.cashBackInfo ?: ""
            }
            textCashback.show()
            informationLabel.add("cashback")
        } else {
            textCashback.gone()
        }
    }

    private fun setClickListener(parentPosition: Int, data: CartItemHolderData) {
        ivProductImage.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))
        textProductName.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))
    }

    private fun renderPrice(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.wholesalePriceFormatted != null) {
            textProductPrice.text = data.cartItemData?.originData?.wholesalePriceFormatted ?: ""
        } else {
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    data.cartItemData?.originData?.pricePlan
                            ?: 0.toDouble(), false).removeDecimalSuffix()
        }
    }

    private fun renderSlashPrice(data: CartItemHolderData) {
        val hasPriceOriginal = data.cartItemData?.originData?.priceOriginal != 0
        val hasWholesalePrice = data.cartItemData?.originData?.wholesalePrice != 0
        val hasPriceDrop = data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0 > 0 &&
                data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0 < data.cartItemData?.originData?.pricePlan?.toInt() ?: 0
        if (hasPriceOriginal || hasWholesalePrice || hasPriceDrop) {
            if (data.cartItemData?.originData?.slashPriceLabel?.isNotBlank() == true) {
                // Slash price
                renderSlashPriceFromCampaign(data)
            } else if (data.cartItemData?.originData?.initialPriceBeforeDrop != 0) {
                val wholesalePrice = data.cartItemData?.originData?.wholesalePrice ?: 0
                if (wholesalePrice > 0 && wholesalePrice.toDouble() < data.cartItemData?.originData?.pricePlan ?: 0.0) {
                    // Wholesale
                    renderSlashPriceFromWholesale(data)
                } else {
                    // Price drop
                    renderSlashPriceFromPriceDrop(data)
                }
            } else if (data.cartItemData?.originData?.wholesalePrice != 0) {
                // Wholesale
                renderSlashPriceFromWholesale(data)
            }

            textSlashPrice.paintFlags = textSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            textSlashPrice.show()
        } else {
            textSlashPrice.gone()
            labelSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromWholesale(data: CartItemHolderData) {
        val priceDropValue = data.cartItemData?.originData?.initialPriceBeforeDrop ?: 0
        val pricePlan = data.cartItemData?.originData?.pricePlanInt ?: 0
        val originalPrice = if (priceDropValue > 0) priceDropValue else pricePlan
        textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromPriceDrop(data: CartItemHolderData) {
        textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.cartItemData?.originData?.initialPriceBeforeDrop
                ?: 0, false).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromCampaign(data: CartItemHolderData) {
        textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(data.cartItemData?.originData?.priceOriginal
                ?: 0, false).removeDecimalSuffix()
        labelSlashPricePercentage.text = data.cartItemData?.originData?.slashPriceLabel
        labelSlashPricePercentage.show()
        informationLabel.add("label diskon")
    }

    private fun renderWarningMessage(data: CartItemHolderData) {
        if (data.cartItemData?.originData?.maxOrder ?: 0 in 0..5) {
            textQtyLeft.text = data.cartItemData?.originData?.warningMessage ?: ""
            textQtyLeft.show()
            actionListener?.onCartItemShowRemainingQty(data.cartItemData?.originData?.productId
                    ?: "")
        } else {
            textQtyLeft.gone()
        }
    }

    private fun renderVariant(data: CartItemHolderData) {
        var paddingRight = 0
        val paddingTop = itemView.resources.getDimensionPixelOffset(R.dimen.dp_2)
        if (data.cartItemData?.originData?.variant?.isNotBlank() == true) {
            textProductVariant.text = data.cartItemData?.originData?.variant
            textProductVariant.show()
            paddingRight = itemView.resources.getDimensionPixelOffset(R.dimen.dp_4)
        } else {
            if (data.cartItemData?.originData?.maxOrder ?: 0 in 0..5) {
                textProductVariant.text = ""
                textProductVariant.invisible()
            } else {
                textProductVariant.gone()
            }
        }
        textProductVariant.setPadding(0, paddingTop, paddingRight, 0);
    }

    private fun renderActionNotes(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener) {
        etRemark.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                data.isStateNotesOnFocuss = false
                actionListener?.onEditNoteDone(parentPosition)
                KeyboardHandler.DropKeyboard(etRemark.context, itemView)
                true
            } else false
        }

        this.tvLabelRemarkOption.setOnClickListener {
            if (data.cartItemData?.isError == false) {
                data.isStateNotesOnFocuss = true
                etRemark.requestFocus()
                actionListener?.onCartItemLabelInputRemarkClicked()
                if (tvLabelRemarkOption.text == tvLabelRemarkOption.context.getString(
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

        if (data.isStateHasNotes) {
            // Has a notes from pdp or not at all but click add notes button
            if (data.isStateNotesOnFocuss && (data.cartItemData?.originData?.originalRemark.isNullOrBlank() || data.cartItemData?.updatedData?.remark != data.cartItemData?.originData?.originalRemark)) {
                // Notes is empty after click add notes button or has value after use click change notes button
                this.tvRemark.visibility = View.GONE
                this.etRemark.setText(Utils.getHtmlFormat(data.cartItemData?.updatedData?.remark))
                this.etRemark.visibility = View.VISIBLE
                this.etRemark.setSelection(etRemark.length())
                this.tvLabelRemarkOption.visibility = View.GONE
                this.tvNoteCharCounter.visibility = View.VISIBLE
                tvLabelRemarkOption.setPadding(0, 0, 0, 0)
            } else {
                // Has notes from pdp
                this.etRemark.visibility = View.GONE
                this.tvRemark.text = Utils.getHtmlFormat(data.cartItemData?.updatedData?.remark
                        ?: "")
                this.tvRemark.visibility = View.VISIBLE
                this.tvLabelRemarkOption.visibility = View.VISIBLE
                this.tvNoteCharCounter.visibility = View.GONE
                this.tvLabelRemarkOption.text = tvLabelRemarkOption.context.getString(R.string.label_button_change_note)
                tvLabelRemarkOption.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_4), 0, 0, 0)

                setNotesWidth()
            }
            tvLabelRemarkOption.setTextColor(ContextCompat.getColor(itemView.context, R.color.light_G500))
        } else {
            // No notes at all
            this.etRemark.visibility = View.GONE
            this.tvRemark.visibility = View.GONE
            this.tvNoteCharCounter.visibility = View.GONE
            this.tvLabelRemarkOption.text = tvLabelRemarkOption.context.getString(R.string.label_button_add_note)
            this.tvLabelRemarkOption.visibility = View.VISIBLE
            this.etRemark.setText("")
            tvLabelRemarkOption.setTextColor(ContextCompat.getColor(itemView.context, R.color.Neutral_N700_68))
            tvLabelRemarkOption.setPadding(0, 0, 0, 0)
        }

        this.etRemark.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(data.cartItemData?.updatedData?.maxCharRemark
                ?: 0))
        etRemark.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    itemNoteTextWatcherAction(it)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        llShopNoteSection.show()
    }

    private fun setNotesWidth() {
        val padding = itemView.resources.getDimensionPixelOffset(R.dimen.dp_16)
        val paddingLeftRight = padding * 2
        val remarkOptionWidth = itemView.resources.getDimensionPixelOffset(R.dimen.dp_35)
        val screenWidth = getScreenWidth()
        val maxNotesWidth = screenWidth - paddingLeftRight
        val noteWidth = maxNotesWidth - remarkOptionWidth

        tvRemark.measure(0, 0)
        val currentWidth = tvRemark.measuredWidth
        if (currentWidth >= maxNotesWidth) {
            tvRemark.layoutParams.width = noteWidth
            tvRemark.requestLayout()
        }
    }

    private fun renderQuantity(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener) {
        val quantity = data.cartItemData?.updatedData?.quantity.toString()
        this.etQty.setText(data.cartItemData?.updatedData?.quantity.toString())
        if (quantity.isNotEmpty()) {
            this.etQty.setSelection(quantity.length)
        }
        this.etQty.setOnClickListener { v ->
            if (data.cartItemData?.isError == false) {
                val qtyStr = (v as AppCompatEditText).text?.toString()
                actionListener?.onCartItemQuantityInputFormClicked(
                        if (!TextUtils.isEmpty(qtyStr)) qtyStr else ""
                )
            }
        }

        this.btnQtyPlus.setOnClickListener {
            if (data.cartItemData?.isError == false) {
                try {
                    if (adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
                        actionListener?.onCartItemQuantityPlusButtonClicked(data, adapterPosition, parentPosition)
                        validateWithAvailableQuantity(cartItemHolderData!!, Integer.parseInt(etQty.text.toString()
                                ?: "0"))
                        handleRefreshType(data, viewHolderListener, parentPosition)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        }

        this.btnQtyMinus.setOnClickListener {
            if (data.cartItemData?.isError == false) {
                try {
                    if (adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
                        actionListener?.onCartItemQuantityMinusButtonClicked(data, adapterPosition, parentPosition)
                        validateWithAvailableQuantity(cartItemHolderData!!, Integer.parseInt(etQty.text.toString()))
                        handleRefreshType(data, viewHolderListener, parentPosition)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

            }
        }

        if (!TextUtils.isEmpty(etQty.text.toString()) && cartItemHolderData != null) {
            checkQtyMustDisabled(cartItemHolderData!!, Integer.parseInt(etQty.text.toString()))
        }
        this.etQty.addTextChangedListener(QuantityTextWatcher(quantityTextwatcherListener))
        this.etQty.isEnabled = data.cartItemData?.isError == false
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
        if (data.cartItemData?.originData?.isWishlisted == true && action.id == ACTION_WISHLISTED) {
            textMoveToWishlist.text = action.message
            textMoveToWishlist.setTextColor(ContextCompat.getColor(itemView.context, R.color.Neutral_N700_32))
            textMoveToWishlist.setOnClickListener { }
        } else if (data.cartItemData?.originData?.isWishlisted == false && action.id == ACTION_WISHLIST) {
            textMoveToWishlist.setTextColor(ContextCompat.getColor(itemView.context, R.color.Neutral_N700_68))
            textMoveToWishlist.setOnClickListener {
                actionListener?.onWishlistCheckChanged(data.cartItemData?.originData?.productId, data.cartItemData?.originData?.cartId
                        ?: 0)
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

    private fun renderErrorItemHeader(data: CartItemHolderData) {
        if (data.cartItemData?.isError == true) {
            flCartItemContainer.foreground = ContextCompat.getDrawable(flCartItemContainer.context, R.drawable.fg_disabled_item)

            if (!TextUtils.isEmpty(data.cartItemData?.errorMessageTitle)) {
                val errorDescription = data.cartItemData?.errorMessageDescription
                if (!TextUtils.isEmpty(errorDescription)) {
                    tickerError.tickerTitle = data.cartItemData?.errorMessageTitle
                    tickerError.setTextDescription(errorDescription ?: "")
                } else {
                    tickerError.tickerTitle = null
                    tickerError.setTextDescription(data.cartItemData?.errorMessageTitle ?: "")
                }
            }

            tickerError.tickerType = Ticker.TYPE_ERROR
            tickerError.tickerShape = Ticker.SHAPE_LOOSE
            tickerError.closeButtonVisibility = View.GONE
            tickerError.visibility = View.VISIBLE
            tickerError.post {
                tickerError.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                tickerError.requestLayout()
            }
            layoutError.visibility = View.VISIBLE
        } else {
            rendercartItemActionOnNormalProduct()
            flCartItemContainer.foreground = ContextCompat.getDrawable(flCartItemContainer.context, R.drawable.fg_enabled_item)
            layoutError.visibility = View.GONE
        }
    }

    private fun renderWarningItemHeader(data: CartItemHolderData) {
        if (data.cartItemData?.isWarning == true) {
            val warningDescription = data.cartItemData?.warningMessageDescription
            if (!TextUtils.isEmpty(warningDescription)) {
                tickerWarning.tickerTitle = data.cartItemData?.warningMessageTitle
                tickerWarning.setTextDescription(warningDescription ?: "")
            } else {
                tickerWarning.tickerTitle = null
                tickerWarning.setTextDescription(data.cartItemData?.warningMessageTitle ?: "")
            }
            tickerWarning.tickerType = Ticker.TYPE_WARNING
            tickerWarning.tickerShape = Ticker.SHAPE_LOOSE
            tickerWarning.closeButtonVisibility = View.GONE
            tickerWarning.visibility = View.VISIBLE
            tickerWarning.post {
                tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                tickerWarning.requestLayout()
            }
            layoutWarning.visibility = View.VISIBLE
        } else {
            tickerWarning.visibility = View.GONE
            layoutWarning.visibility = View.GONE
        }
    }

    private fun checkQtyMustDisabled(cartItemHolderData: CartItemHolderData, qty: Int) {
        if (qty <= cartItemHolderData.cartItemData?.originData?.minOrder ?: 0 && qty >= cartItemHolderData.cartItemData?.originData?.maxOrder ?: 0) {
            btnQtyMinus.isEnabled = false
            btnQtyPlus.isEnabled = false
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(btnQtyMinus.context, R.drawable.bg_button_counter_minus_checkout_disabled))
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.context, R.drawable.bg_button_counter_plus_checkout_disabled))
        } else if (qty <= cartItemHolderData.cartItemData?.originData?.minOrder ?: 0) {
            btnQtyMinus.isEnabled = false
            btnQtyPlus.isEnabled = true
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(btnQtyMinus.context, R.drawable.bg_button_counter_minus_checkout_disabled))
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.context, R.drawable.bg_button_counter_plus_checkout))
        } else if (qty >= cartItemHolderData.cartItemData?.originData?.maxOrder ?: 0) {
            btnQtyPlus.isEnabled = false
            btnQtyMinus.isEnabled = true
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.context, R.drawable.bg_button_counter_plus_checkout_disabled))
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(btnQtyMinus.context, R.drawable.bg_button_counter_minus_checkout))
        } else {
            btnQtyPlus.isEnabled = true
            btnQtyMinus.isEnabled = true
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.context, R.drawable.bg_button_counter_plus_checkout))
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(btnQtyMinus.context, R.drawable.bg_button_counter_minus_checkout))
        }
    }

    private fun validateWithAvailableQuantity(data: CartItemHolderData, qty: Int): Boolean {
        val maxOrder = data.cartItemData?.originData?.maxOrder ?: 0
        val minOrder = data.cartItemData?.originData?.minOrder ?: 0
        if (qty > maxOrder) {
            etQty.setText(maxOrder.toString())
            return false
        } else if (qty < minOrder) {
            etQty.setText(minOrder.toString())
            return false
        }

        return true
    }

    private fun itemNoteTextWatcherAction(editable: Editable) {
        if (cartItemHolderData != null) {
            cartItemHolderData?.cartItemData?.updatedData?.remark = editable.toString()
        }
    }

    private fun itemQuantityTextWatcherAction(quantity: QuantityWrapper) {
        if (adapterPosition != RecyclerView.NO_POSITION && cartItemHolderData != null) {
            var needToUpdateView = quantity.qtyBefore.toString() != quantity.editable.toString()
            if (quantity.editable.length != 0) {
                var zeroCount = 0
                for (i in 0 until quantity.editable.length) {
                    if (quantity.editable[i] == '0') {
                        zeroCount++
                    } else {
                        break
                    }
                }
                if (zeroCount == quantity.editable.length) {
                    actionListener?.onCartItemQuantityReseted(adapterPosition, parentPosition, needToUpdateView)
                    if (needToUpdateView) {
                        handleRefreshType(cartItemHolderData!!, viewHolderListener, parentPosition)
                        needToUpdateView = false
                    }
                } else if (quantity.editable[0] == '0') {
                    etQty.setText(quantity.editable.toString()
                            .substring(zeroCount, quantity.editable.toString().length))
                    etQty.setSelection(etQty.length())
                    needToUpdateView = true
                }
            } else if (TextUtils.isEmpty(etQty.text)) {
                actionListener?.onCartItemQuantityReseted(adapterPosition, parentPosition,
                        quantity.qtyBefore.toString() != quantity.editable.toString())
                if (needToUpdateView) {
                    handleRefreshType(cartItemHolderData!!, viewHolderListener, parentPosition)
                    needToUpdateView = false
                }
            }

            var qty = 0
            try {
                qty = Integer.parseInt(quantity.editable.toString())
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            checkQtyMustDisabled(cartItemHolderData!!, qty)
            cartItemHolderData!!.cartItemData!!.updatedData!!.quantity = qty
            val tmpNeedToUpdateView = validateWithAvailableQuantity(cartItemHolderData!!, qty)
            if (!tmpNeedToUpdateView) needToUpdateView = tmpNeedToUpdateView
            if (needToUpdateView) {
                actionListener?.onCartItemQuantityChangedThenHitUpdateCartAndValidateUse()
                handleRefreshType(cartItemHolderData!!, viewHolderListener, parentPosition)
            }
        }
    }

    private fun rendercartItemActionOnNormalProduct() {
        rlProductAction.visibility = View.VISIBLE
        llShopNoteSection.visibility = View.VISIBLE
    }

    interface ViewHolderListener {

        fun onNeedToRefreshSingleProduct(childPosition: Int)

        fun onNeedToRefreshSingleShop(parentPosition: Int)

        fun onNeedToRefreshAllShop()

    }

    companion object {
        val TYPE_VIEW_ITEM_CART = R.layout.holder_item_cart_new
        private val MAX_SHOWING_NOTES_CHAR = 20
    }
}
