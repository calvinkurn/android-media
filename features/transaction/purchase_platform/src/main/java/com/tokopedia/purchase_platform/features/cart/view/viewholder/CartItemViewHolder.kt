package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.utils.NoteTextWatcher
import com.tokopedia.purchase_platform.common.utils.NoteTextWatcher.TEXTWATCHER_NOTE_DEBOUNCE_TIME
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher.TEXTWATCHER_QUANTITY_DEBOUNCE_TIME
import com.tokopedia.purchase_platform.common.utils.QuantityWrapper
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.features.cart.view.adapter.CartItemAdapter
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.apache.commons.lang3.StringUtils
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
    private val tvProductName: TextView
    private val tvProductPrice: TextView
    private val etQty: AppCompatEditText
    private val btnQtyPlus: ImageView
    private val btnQtyMinus: ImageView
    private val ivIconFreeReturn: ImageView
    private val tvInfoPreOrder: TextView
    private val tvInfoCashBack: TextView
    private val tvCodBadge: TextView
    private val etRemark: AppCompatEditText
    private val tvLabelRemarkOption: TextView
    private val btnDelete: ImageView
    private val btnDeleteOnCartError: ImageView
    private val tvErrorFormValidation: TextView
    private val tvErrorFormRemarkValidation: TextView

    private val layoutError: LinearLayout
    private val tickerError: Ticker
    private val layoutWarning: LinearLayout
    private val tickerWarning: Ticker

    private val tvNoteCharCounter: TextView
    private val productProperties: FlexboxLayout
    private val tvRemark: TextView
    private val tvLabelFormRemark: TextView
    private val imgWishlist: ImageView
    private val tvEllipsize: TextView
    private val divider: View
    private val tvPriceChanges: TextView
    private val tvInvenageText: TextView
    private val rlInvenageText: RelativeLayout
    private val rlProductAction: RelativeLayout
    private val llProductActionOnCartError: LinearLayout
    private val llShopNoteSection: LinearLayout
    private val vDeviderOnCartError: View
    private val tvSimilarProductOnCartError: Typography
    private val imgFreeShipping: ImageView

    private var cartItemHolderData: CartItemHolderData? = null
    private var quantityTextwatcherListener: QuantityTextWatcher.QuantityTextwatcherListener? = null
    private var noteTextwatcherListener: NoteTextWatcher.NoteTextwatcherListener? = null
    private var parentPosition: Int = 0
    private var dataSize: Int = 0
    private var quantityDebounceSubscription: Subscription? = null
    private var noteDebounceSubscription: Subscription? = null

    init {
        context = itemView.context

        llWarningAndError = itemView.findViewById(R.id.ll_warning_and_error)
        flCartItemContainer = itemView.findViewById(R.id.fl_cart_item_container)
        cbSelectItem = itemView.findViewById(R.id.cb_select_item)
        tvErrorFormValidation = itemView.findViewById(R.id.tv_error_form_validation)
        tvErrorFormRemarkValidation = itemView.findViewById(R.id.tv_error_form_remark_validation)
        ivProductImage = itemView.findViewById(R.id.iv_image_product)
        tvProductName = itemView.findViewById(R.id.tv_product_name)
        tvProductPrice = itemView.findViewById(R.id.tv_product_price)
        etQty = itemView.findViewById(R.id.et_qty)
        btnQtyPlus = itemView.findViewById(R.id.btn_qty_plus)
        btnQtyMinus = itemView.findViewById(R.id.btn_qty_min)
        ivIconFreeReturn = itemView.findViewById(R.id.iv_free_return_icon)
        tvInfoPreOrder = itemView.findViewById(R.id.tv_pre_order)
        tvInfoCashBack = itemView.findViewById(R.id.tv_cashback)
        tvCodBadge = itemView.findViewById(R.id.tv_cod)
        tvLabelRemarkOption = itemView.findViewById(R.id.tv_label_remark_option)
        etRemark = itemView.findViewById(R.id.et_remark)
        btnDelete = itemView.findViewById(R.id.btn_delete_cart)
        btnDeleteOnCartError = itemView.findViewById(R.id.btn_delete_on_cart_error)
        vDeviderOnCartError = itemView.findViewById(R.id.v_devider_on_cart_error)
        tvSimilarProductOnCartError = itemView.findViewById(R.id.tv_similar_product_on_cart_error)

        layoutError = itemView.findViewById(R.id.layout_error)
        tickerError = itemView.findViewById(R.id.ticker_error)
        layoutWarning = itemView.findViewById(R.id.layout_warning)
        tickerWarning = itemView.findViewById(R.id.ticker_warning)

        tvNoteCharCounter = itemView.findViewById(R.id.tv_note_char_counter)
        productProperties = itemView.findViewById(R.id.product_properties)
        tvRemark = itemView.findViewById(R.id.tv_remark)
        tvLabelFormRemark = itemView.findViewById(R.id.tv_label_form_remark)
        imgWishlist = itemView.findViewById(R.id.img_wishlist)
        tvEllipsize = itemView.findViewById(R.id.tv_ellipsize)
        divider = itemView.findViewById(R.id.holder_item_cart_divider)
        tvPriceChanges = itemView.findViewById(R.id.tv_price_changes)
        tvInvenageText = itemView.findViewById(R.id.tv_invenage_text)
        rlInvenageText = itemView.findViewById(R.id.rl_invenage_text)
        rlProductAction = itemView.findViewById(R.id.rl_product_action)
        llProductActionOnCartError = itemView.findViewById(R.id.ll_product_action_on_cart_error)
        llShopNoteSection = itemView.findViewById(R.id.ll_shop_note_section)
        imgFreeShipping = itemView.findViewById(R.id.img_free_shipping)

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
        quantityDebounceSubscription = Observable.create(Observable.OnSubscribe<QuantityWrapper> { subscriber -> quantityTextwatcherListener = QuantityTextWatcher.QuantityTextwatcherListener { quantity -> subscriber.onNext(quantity) } }).debounce(TEXTWATCHER_QUANTITY_DEBOUNCE_TIME.toLong(), TimeUnit.MILLISECONDS)
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

        noteDebounceSubscription = Observable.create(Observable.OnSubscribe<Editable> { subscriber -> noteTextwatcherListener = NoteTextWatcher.NoteTextwatcherListener { editable -> subscriber.onNext(editable) } }).debounce(TEXTWATCHER_NOTE_DEBOUNCE_TIME.toLong(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Editable>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(editable: Editable) {
                        itemNoteTextWatcherAction(editable)
                    }
                })

        compositeSubscription.add(noteDebounceSubscription)
    }

    fun bindData(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener, dataSize: Int) {
        this.viewHolderListener = viewHolderListener
        this.parentPosition = parentPosition
        cartItemHolderData = data
        this.dataSize = dataSize

        renderProductInfo(data, parentPosition)
        renderRemark(data, parentPosition, viewHolderListener)
        renderQuantity(data, parentPosition, viewHolderListener)
        renderErrorFormItemValidation(data)
        renderWarningAndError(data)
        renderWishlist(data)
        renderSelection(data, parentPosition)
    }

    private fun renderSelection(data: CartItemHolderData, parentPosition: Int) {
        cbSelectItem.isEnabled = !data.cartItemData.isError
        cbSelectItem.isChecked = !data.cartItemData.isError && data.isSelected
        cbSelectItem.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!data.cartItemData.isError) {
                data.isSelected = isChecked
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    actionListener?.onCartItemCheckChanged(adapterPosition, parentPosition, data.isSelected)
                    viewHolderListener?.onNeedToRefreshAllShop()
                }
            }
        }
    }

    private fun renderWarningAndError(data: CartItemHolderData) {
        // Initial action state
        rendercartItemActionOnNormalProduct()
        if (data.cartItemData.isParentHasErrorOrWarning) {
            if (!data.cartItemData.isDisableAllProducts || data.cartItemData.isError || data.cartItemData.isWarning) {
                renderErrorItemHeader(data)
                renderWarningItemHeader(data)
                setWarningAndErrorVisibility(data)
            } else {
                disableView(data)
            }
        } else {
            if (!data.cartItemData.isSingleChild) {
                renderErrorItemHeader(data)
                renderWarningItemHeader(data)
                setWarningAndErrorVisibility(data)
            } else {
                disableView(data)
            }
        }
    }

    private fun setWarningAndErrorVisibility(data: CartItemHolderData) {
        if ((!TextUtils.isEmpty(data.cartItemData.errorMessageTitle) || !TextUtils.isEmpty(data.cartItemData.warningMessageTitle)) && (data.cartItemData.isError || data.cartItemData.isWarning)) {
            llWarningAndError.visibility = View.VISIBLE
        } else {
            llWarningAndError.visibility = View.GONE
        }
    }

    private fun disableView(data: CartItemHolderData) {
        if (data.cartItemData.isError) {
            flCartItemContainer.foreground = ContextCompat.getDrawable(flCartItemContainer.context, R.drawable.fg_disabled_item)
        } else {
            flCartItemContainer.foreground = ContextCompat.getDrawable(flCartItemContainer.context, R.drawable.fg_enabled_item)
        }
        llWarningAndError.visibility = View.GONE
    }

    private fun renderProductInfo(data: CartItemHolderData, parentPosition: Int) {
        this.tvProductName.text = Html.fromHtml(data.cartItemData.originData?.productName)
        if (data.cartItemData.originData?.wholesalePriceFormatted != null) {
            this.tvProductPrice.text = data.cartItemData.originData?.wholesalePriceFormatted
        } else {
            this.tvProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    data.cartItemData.originData?.pricePlan ?: 0.toDouble(), false)
        }

        ImageHandler.loadImageRounded2(
                this.itemView.context, this.ivProductImage,
                data.cartItemData.originData?.productImage
        )

        this.ivProductImage.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))
        this.tvProductName.setOnClickListener(getOnClickProductItemListener(adapterPosition, parentPosition, data))

        renderProductPropertiesFreereturn(data)
        renderProductPropertiesPreOrder(data)
        renderProductPropertiesCod(data)
        renderProductPropertiesCashback(data)
        renderProductPropertiesLayout(data)
        renderProductPropertiesPriceChanges(data)
        renderProductPropertiesInvenage(data)
        renderProductPropertiesFreeShipping(data)

        btnDelete.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data, adapterPosition, parentPosition)
            }
        }
        btnDeleteOnCartError.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data, adapterPosition, parentPosition)
            }
        }

        divider.visibility = if (layoutPosition == dataSize - 1) View.GONE else View.VISIBLE
    }

    private fun renderProductPropertiesCashback(data: CartItemHolderData) {
        this.tvInfoCashBack.visibility = if (data.cartItemData.originData?.isCashBack == true) View.VISIBLE else View.GONE
        this.tvInfoCashBack.text = data.cartItemData.originData?.cashBackInfo
    }

    private fun renderProductPropertiesCod(data: CartItemHolderData) {
        this.tvCodBadge.visibility = if (data.cartItemData.originData?.isCod == true) View.VISIBLE else View.GONE
    }

    private fun renderProductPropertiesPreOrder(data: CartItemHolderData) {
        if (data.cartItemData.originData?.isPreOrder == true) {
            this.tvInfoPreOrder.text = data.cartItemData.originData?.preOrderInfo
            this.tvInfoPreOrder.visibility = View.VISIBLE
        } else {
            this.tvInfoPreOrder.visibility = View.GONE
        }
    }

    private fun renderProductPropertiesFreereturn(data: CartItemHolderData) {
        if (data.cartItemData.originData?.isFreeReturn == true) {
            this.ivIconFreeReturn.visibility = View.VISIBLE
            ImageHandler.loadImageRounded2(
                    this.itemView.context, this.ivIconFreeReturn,
                    data.cartItemData.originData?.freeReturnLogo
            )
        } else {
            this.ivIconFreeReturn.visibility = View.GONE
        }
    }

    private fun renderProductPropertiesLayout(data: CartItemHolderData) {
        if (data.cartItemData.originData?.isCashBack == true ||
                data.cartItemData.originData?.isPreOrder == true ||
                data.cartItemData.originData?.isFreeReturn == true ||
                data.cartItemData.originData?.isCod == true) {
            productProperties.visibility = View.VISIBLE
        } else {
            productProperties.visibility = View.GONE
        }
    }

    private fun renderProductPropertiesFreeShipping(data: CartItemHolderData) {
        if (data.cartItemData.originData?.isFreeShipping == true &&
                !TextUtils.isEmpty(data.cartItemData.originData?.freeShippingBadgeUrl)) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFreeShipping, data.cartItemData.originData?.freeShippingBadgeUrl
            )
            imgFreeShipping.visibility = View.VISIBLE
        } else {
            imgFreeShipping.visibility = View.GONE
        }
    }

    private fun renderProductPropertiesPriceChanges(data: CartItemHolderData) {
        val priceChangesText = data.cartItemData.originData?.priceChangesDesc
        val priceChangesState = data.cartItemData.originData?.priceChangesState ?: 0
        if (priceChangesText?.isEmpty() == true || priceChangesState >= 0) {
            tvPriceChanges.visibility = View.GONE
        } else {
            tvPriceChanges.visibility = View.VISIBLE
            tvPriceChanges.text = priceChangesText
            actionListener?.onCartItemShowTickerPriceDecrease(data.cartItemData.originData?.productId)
        }
    }

    private fun renderProductPropertiesInvenage(data: CartItemHolderData) {
        if (data.cartItemData.originData?.productInvenageByUserText?.isNotEmpty() == true) {
            this.rlInvenageText.visibility = View.VISIBLE
            val completeText = data.cartItemData.originData?.productInvenageByUserText
            val totalInOtherCart = data.cartItemData.originData?.productInvenageByUserInCart
            val totalRemainingStock = data.cartItemData.originData?.productInvenageByUserLastStockLessThan
            val invenageText = completeText?.replace(context?.getString(R.string.product_invenage_remaining_stock)
                    ?: "", "" + totalRemainingStock)
                    ?.replace(context?.getString(R.string.product_invenage_in_other_cart)
                            ?: "", "" + totalInOtherCart)
            this.tvInvenageText.text = Html.fromHtml(invenageText)
            actionListener?.onCartItemShowTickerStockDecreaseAndAlreadyAtcByOtherUser(data.cartItemData.originData?.productId)
        } else {
            this.rlInvenageText.visibility = View.GONE
        }
    }

    private fun renderRemark(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener) {
        this.tvLabelRemarkOption.setOnClickListener {
            if (!data.cartItemData.isError) {
                actionListener?.onCartItemLabelInputRemarkClicked()
                if (tvLabelRemarkOption.text == tvLabelRemarkOption.context.getString(
                                R.string.label_button_change_note)) {
                    var remark = data.cartItemData.updatedData?.remark
                    remark += " "
                    data.cartItemData.updatedData?.remark = remark
                }
                data.isStateRemarkExpanded = true
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    viewHolderListener.onNeedToRefreshSingleProduct(adapterPosition)
                }
            }
        }

        if (!StringUtils.isBlank(data.cartItemData.updatedData?.remark)) {
            data.isStateRemarkExpanded = true
        }

        if (data.isStateRemarkExpanded) {
            // Has a notes from pdp or not at all but click add notes button
            if (StringUtils.isBlank(data.cartItemData.originData?.originalRemark) || data.cartItemData.updatedData?.remark != data.cartItemData.originData?.originalRemark) {
                // Notes is empty after click add notes button or has value after use click change notes button
                this.tvLabelFormRemark.visibility = View.VISIBLE
                this.tvRemark.visibility = View.GONE
                this.etRemark.setText(Utils.getHtmlFormat(data.cartItemData.updatedData?.remark))
                this.etRemark.visibility = View.VISIBLE
                this.etRemark.setSelection(etRemark.length())
                this.tvLabelRemarkOption.visibility = View.GONE
                this.tvNoteCharCounter.visibility = View.VISIBLE
                this.tvEllipsize.visibility = View.GONE
            } else {
                // Has notes from pdp
                this.tvLabelFormRemark.visibility = View.GONE
                this.etRemark.visibility = View.GONE
                this.tvRemark.text = Utils.getHtmlFormat(data.cartItemData.updatedData?.remark)
                this.tvRemark.visibility = View.VISIBLE
                this.tvLabelRemarkOption.visibility = View.VISIBLE
                this.tvNoteCharCounter.visibility = View.GONE
                this.tvLabelRemarkOption.text = tvLabelRemarkOption.context.getString(R.string.label_button_change_note)
                if (data.cartItemData.updatedData?.remark?.length ?: 0 >= MAX_SHOWING_NOTES_CHAR) {
                    this.tvEllipsize.visibility = View.VISIBLE
                } else {
                    this.tvEllipsize.visibility = View.GONE
                }
            }
        } else {
            // No notes at all
            this.etRemark.visibility = View.GONE
            this.tvRemark.visibility = View.GONE
            this.tvNoteCharCounter.visibility = View.GONE
            this.tvLabelFormRemark.visibility = View.GONE
            this.tvLabelRemarkOption.text = tvLabelRemarkOption.context.getString(R.string.label_button_add_note)
            this.tvLabelRemarkOption.visibility = View.VISIBLE
            this.etRemark.setText("")
            this.tvEllipsize.visibility = View.GONE
        }

        this.etRemark.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(data.cartItemData.updatedData?.maxCharRemark
                ?: 0))
        this.etRemark.addTextChangedListener(NoteTextWatcher(noteTextwatcherListener))
    }

    private fun renderQuantity(data: CartItemHolderData, parentPosition: Int, viewHolderListener: ViewHolderListener) {
        val quantity = data.cartItemData.updatedData?.quantity.toString()
        this.etQty.setText(data.cartItemData.updatedData?.quantity.toString())
        if (quantity.isNotEmpty()) {
            this.etQty.setSelection(quantity.length)
        }
        this.etQty.setOnClickListener { v ->
            if (!data.cartItemData.isError) {
                val qtyStr = (v as AppCompatEditText).text?.toString()
                actionListener?.onCartItemQuantityInputFormClicked(
                        if (!TextUtils.isEmpty(qtyStr)) qtyStr else ""
                )
            }
        }

        this.btnQtyPlus.setOnClickListener {
            if (!data.cartItemData.isError) {
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
            if (!data.cartItemData.isError) {
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
        this.etQty.isEnabled = !data.cartItemData.isError
    }

    private fun handleRefreshType(data: CartItemHolderData, viewHolderListener: ViewHolderListener?, parentPosition: Int) {
        if (data.cartItemData.originData?.wholesalePriceData != null && data.cartItemData.originData?.wholesalePriceData?.size ?: 0 > 0) {
            if (data.cartItemData.originData?.isPreOrder == true) {
                viewHolderListener?.onNeedToRefreshAllShop()
            } else {
                viewHolderListener?.onNeedToRefreshSingleShop(parentPosition)
            }
        } else {
            viewHolderListener?.onNeedToRefreshSingleProduct(adapterPosition)
        }
    }

    private fun renderWishlist(data: CartItemHolderData) {
        if (data.cartItemData.originData?.isWishlisted == true) {
            imgWishlist.setImageResource(R.drawable.ic_wishlist_checkout_on)
        } else {
            imgWishlist.setImageResource(R.drawable.ic_wishlist_checkout_off)
        }

        imgWishlist.setOnClickListener {
            val checked = data.cartItemData.originData?.isWishlisted ?: false
            actionListener?.onWishlistCheckChanged(data.cartItemData.originData?.productId, !checked)
        }
    }

    private fun getOnClickProductItemListener(
            @SuppressLint("RecyclerView") position: Int, parentPosition: Int,
            data: CartItemHolderData): View.OnClickListener {
        return View.OnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemProductClicked(data, position, parentPosition)
            }
        }
    }

    private fun renderErrorFormItemValidation(data: CartItemHolderData) {
        val noteCounter = String.format(tvNoteCharCounter.context.getString(R.string.note_counter_format),
                data.cartItemData.updatedData?.remark?.length,
                data.cartItemData.updatedData?.maxCharRemark)
        tvNoteCharCounter.text = noteCounter
        if (data.getErrorFormItemValidationTypeValue() == CartItemHolderData.ERROR_EMPTY) {
            this.tvErrorFormValidation.text = ""
            this.tvErrorFormValidation.visibility = View.GONE
            this.tvErrorFormRemarkValidation.visibility = View.GONE
            this.tvErrorFormRemarkValidation.text = ""
        } else {
            if (data.getErrorFormItemValidationTypeValue() == CartItemHolderData.ERROR_FIELD_MAX_CHAR) {
                if (TextUtils.isEmpty(data.errorFormItemValidationMessage)) {
                    this.tvErrorFormValidation.text = ""
                    this.tvErrorFormValidation.visibility = View.GONE
                }
                this.tvErrorFormRemarkValidation.visibility = View.VISIBLE
                this.tvErrorFormRemarkValidation.text = data.errorFormItemValidationMessage
                this.tvNoteCharCounter.visibility = View.GONE
            } else {
                this.tvErrorFormValidation.text = data.errorFormItemValidationMessage
                this.tvErrorFormValidation.visibility = View.VISIBLE
                this.tvErrorFormRemarkValidation.visibility = View.GONE
                this.tvErrorFormRemarkValidation.text = ""
                if (data.cartItemData.originData?.originalRemark != data.cartItemData.updatedData?.remark) {
                    this.tvNoteCharCounter.visibility = View.VISIBLE
                } else {
                    this.tvNoteCharCounter.visibility = View.GONE
                }
            }
        }
        actionListener?.onCartItemAfterErrorChecked()
    }

    private fun renderErrorItemHeader(data: CartItemHolderData) {
        if (data.cartItemData.isError) {
            renderCartItemActionOnErrorProduct()
            flCartItemContainer.foreground = ContextCompat.getDrawable(flCartItemContainer.context, R.drawable.fg_disabled_item)

            val similarProductData = data.cartItemData.similarProductData

            if (!TextUtils.isEmpty(data.cartItemData.errorMessageTitle)) {
                val errorDescription = data.cartItemData.errorMessageDescription
                if (!TextUtils.isEmpty(errorDescription)) {
                    tickerError.tickerTitle = data.cartItemData.errorMessageTitle
                    tickerError.setTextDescription(errorDescription ?: "")
                } else {
                    tickerError.tickerTitle = null
                    tickerError.setTextDescription(data.cartItemData.errorMessageTitle ?: "")
                }
            }

            vDeviderOnCartError.visibility = View.GONE
            if (similarProductData != null) {
                vDeviderOnCartError.visibility = View.VISIBLE
                tvSimilarProductOnCartError.text = similarProductData.text
                tvSimilarProductOnCartError.setOnClickListener { view -> actionListener?.onCartItemSimilarProductUrlClicked(similarProductData.url) }
                actionListener?.onCartItemShowTickerOutOfStock(data.cartItemData.originData?.productId)
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
        if (data.cartItemData.isWarning) {
            val warningDescription = data.cartItemData.warningMessageDescription
            if (!TextUtils.isEmpty(warningDescription)) {
                tickerWarning.tickerTitle = data.cartItemData.warningMessageTitle
                tickerWarning.setTextDescription(warningDescription ?: "")
            } else {
                tickerWarning.tickerTitle = null
                tickerWarning.setTextDescription(data.cartItemData.warningMessageTitle ?: "")
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
        if (qty <= cartItemHolderData.cartItemData.originData?.minOrder ?: 0 && qty >= cartItemHolderData.cartItemData.originData?.maxOrder ?: 0) {
            btnQtyMinus.isEnabled = false
            btnQtyPlus.isEnabled = false
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(btnQtyMinus.context, R.drawable.bg_button_counter_minus_checkout_disabled))
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.context, R.drawable.bg_button_counter_plus_checkout_disabled))
        } else if (qty <= cartItemHolderData.cartItemData.originData?.minOrder ?: 0) {
            btnQtyMinus.isEnabled = false
            btnQtyPlus.isEnabled = true
            btnQtyMinus.setImageDrawable(ContextCompat.getDrawable(btnQtyMinus.context, R.drawable.bg_button_counter_minus_checkout_disabled))
            btnQtyPlus.setImageDrawable(ContextCompat.getDrawable(btnQtyPlus.context, R.drawable.bg_button_counter_plus_checkout))
        } else if (qty >= cartItemHolderData.cartItemData.originData?.maxOrder ?: 0) {
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

    private fun validateWithAvailableQuantity(data: CartItemHolderData, qty: Int) {
        if (qty > data.cartItemData.originData?.maxOrder ?: 0) {
            val errorMessage = data.cartItemData.messageErrorData?.errorProductMaxQuantity
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            val numberAsString = numberFormat.format(data.cartItemData.originData?.maxOrder?.toLong())
            val maxValue = numberAsString.replace(",", ".")
            tvErrorFormValidation.text = errorMessage?.replace("{{value}}", maxValue)
            tvErrorFormValidation.visibility = View.VISIBLE
        } else if (qty < data.cartItemData.originData?.minOrder ?: 0) {
            val errorMessage = data.cartItemData.messageErrorData?.errorProductMinQuantity
            tvErrorFormValidation.text = errorMessage?.replace("{{value}}",
                    data.cartItemData.originData?.minOrder.toString())
            tvErrorFormValidation.visibility = View.VISIBLE
        } else {
            tvErrorFormValidation.visibility = View.GONE
        }
        actionListener?.onCartItemAfterErrorChecked()
    }

    private fun itemNoteTextWatcherAction(editable: Editable) {
        if (cartItemHolderData != null) {
            cartItemHolderData?.cartItemData?.updatedData?.remark = editable.toString()
            renderErrorFormItemValidation(cartItemHolderData!!)
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
            cartItemHolderData!!.cartItemData.updatedData!!.quantity = qty
            validateWithAvailableQuantity(cartItemHolderData!!, qty)
            if (needToUpdateView) {
                handleRefreshType(cartItemHolderData!!, viewHolderListener, parentPosition)
            }
        }
    }

    // Render special button delete and similar product button if applicable when cart item has error
    // Hide normal delete, wishlist, plus and minus button and notes section
    private fun renderCartItemActionOnErrorProduct() {
        rlProductAction.visibility = View.GONE
        llShopNoteSection.visibility = View.GONE
        llProductActionOnCartError.visibility = View.VISIBLE
    }

    // Render normal delete, wishlist, plus and minus button, and notes section when cart item has no error
    // Hide special delete and similar product button
    private fun rendercartItemActionOnNormalProduct() {
        llProductActionOnCartError.visibility = View.GONE
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
