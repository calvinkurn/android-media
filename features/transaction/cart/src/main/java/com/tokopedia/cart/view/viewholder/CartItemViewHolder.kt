package com.tokopedia.cart.view.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cart.databinding.ItemCartProductRevampBinding
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.customview.BmGmWidgetView
import com.tokopedia.cart.view.customview.CartSwipeRevealLayout
import com.tokopedia.cart.view.customview.CartViewBinderHelper
import com.tokopedia.cart.view.uimodel.CartDeleteButtonSource
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData.Companion.BUNDLING_ITEM_FOOTER
import com.tokopedia.cart.view.uimodel.CartItemHolderData.Companion.BUNDLING_ITEM_HEADER
import com.tokopedia.cart.view.uimodel.CartMainCoachMarkUiModel
import com.tokopedia.cart.view.uimodel.CartProductLabelData
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.nest.components.quantityeditor.QtyButton
import com.tokopedia.nest.components.quantityeditor.QtyField
import com.tokopedia.nest.components.quantityeditor.QtyState
import com.tokopedia.nest.components.quantityeditor.view.QuantityEditorView
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_BMGM_STATE_TICKER_ACTIVE
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_BMGM_STATE_TICKER_INACTIVE
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_BMGM_STATE_TICKER_LOADING
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import com.tokopedia.nest.components.R as nestcomponentsR
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@SuppressLint("ClickableViewAccessibility")
class CartItemViewHolder(
    private val binding: ItemCartProductRevampBinding,
    private var actionListener: CartItemAdapter.ActionListener?,
    private var mainCoachMark: CartMainCoachMarkUiModel,
    private val binderHelper: CartViewBinderHelper
) : RecyclerView.ViewHolder(binding.root) {

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(itemView.context) }

    private var viewHolderListener: ViewHolderListener? = null

    private var dataSize: Int = 0
    private var delayChangeCheckboxState: Job? = null
    private var delayChangeWishlistStatus: Job? = null
    private var delayChangeQty: Job? = null
    private var informationLabel: MutableList<String> = mutableListOf()
    private var qtyTextWatcher: TextWatcher? = null
    private var lastQty: Int = 0
    private var isDeleteFromDoneImeButton: Boolean = false

    init {
        binding.qtyEditorProduct.apply {
            isExpand = true
            expandState.value = true
            enableManualInput.value = true
            configState.value = configState.value.copy(
                qtyField = QtyField(cursorColor = unifyprinciplesR.color.Unify_NN1000)
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun clear() {
        actionListener = null
        viewHolderListener = null
        delayChangeCheckboxState?.cancel()
        delayChangeQty?.cancel()
        delayChangeWishlistStatus?.cancel()
        qtyTextWatcher = null
    }

    fun bindData(
        data: CartItemHolderData,
        viewHolderListener: ViewHolderListener?,
        dataSize: Int
    ) {
        this.viewHolderListener = viewHolderListener
        this.dataSize = dataSize
        this.isDeleteFromDoneImeButton = false

        itemView.addOnImpressionListener(data, onView = {
            if (!data.isError) {
                actionListener?.onAvailableCartItemImpression(listOf(data))
                if (data.isEnableCartVariant) {
                    actionListener?.onViewChangeVariant(data.cartId)
                }
            }
        })

        initSwipeLayout(data)
        initCoachMark()
        setNoteAnimationResource()
        renderAlpha(data)
        renderContainer(data)
        renderDivider(data)
        renderShopInfo(data)
        renderLeftAnchor(data)
        renderProductInfo(data)
        renderQuantityWidget(data)
        renderProductAction(data)
        renderBmGmOfferTicker(data)
        renderProductTagInfo(data)
        renderPurchaseBenefitWidget(data)
        renderCartCampaignFestivityTicker(data)
    }

    private fun initSwipeLayout(data: CartItemHolderData) {
        binderHelper.bind(binding.swipeLayout, data.getSwipeLayoutId())
        if (data.isError) {
            binderHelper.lockSwipe(data.getSwipeLayoutId())
        }
        if (data.isBundlingItem) {
            binderHelper.bind(binding.swipeLayoutBundling, data.getSwipeLayoutBundlingId())
            if (data.isMultipleBundleProduct) {
                binderHelper.lockSwipe(data.getSwipeLayoutId())
            }
            if (data.isError || !data.isMultipleBundleProduct) {
                binderHelper.lockSwipe(data.getSwipeLayoutBundlingId())
            }
        }
        if (shouldLockSwipeLayout()) {
            binderHelper.lockSwipe(data.getSwipeLayoutId())
        }
        if (shouldLockBundlingSwipeLayout() && data.isBundlingItem) {
            binderHelper.lockSwipe(data.getSwipeLayoutBundlingId())
        }
        setSwipeLayoutColor()
        setSwipeLayoutSwipeListener(data)
        setSwipeLayoutClickListener(data)
    }

    private fun setSwipeLayoutColor() {
        // Icon Delete Color for Light & Dark
        val rn500Color = ResourcesCompat.getColor(
            itemView.context.resources,
            unifyprinciplesR.color.Unify_RN500,
            null
        )
        val rn700Color = ResourcesCompat.getColor(
            itemView.context.resources,
            unifyprinciplesR.color.Unify_RN700,
            null
        )

        // Swipe Delete Background for Light & Dark
        val rn100Color = ResourcesCompat.getColor(
            itemView.context.resources,
            unifyprinciplesR.color.Unify_RN100,
            null
        )
        val rn300Color = ResourcesCompat.getColor(
            itemView.context.resources,
            unifyprinciplesR.color.Unify_RN300,
            null
        )

        binding.apply {
            btnSwipeDelete.setImage(
                null,
                rn500Color,
                null,
                rn700Color,
                null
            )
            btnSwipeDeleteBundling.setImage(
                null,
                rn500Color,
                null,
                rn700Color,
                null
            )
            if (itemView.context.isDarkMode()) {
                flSwipeDelete.setBackgroundColor(rn300Color)
                flSwipeDeleteBundling.setBackgroundColor(rn300Color)
            } else {
                flSwipeDelete.setBackgroundColor(rn100Color)
                flSwipeDeleteBundling.setBackgroundColor(rn100Color)
            }
        }
    }

    private fun setSwipeLayoutSwipeListener(data: CartItemHolderData) {
        binding.apply {
            swipeLayout.setSwipeListener(object : CartSwipeRevealLayout.SwipeListener {
                override fun onClosed(view: CartSwipeRevealLayout?) {
                    actionListener?.onSwipeToDeleteClosed(data.productId)
                }

                override fun onOpened(view: CartSwipeRevealLayout?) {
                    // no-op
                }

                override fun onSlide(view: CartSwipeRevealLayout?, slideOffset: Float) {
                    // no-op
                }
            })
        }
    }

    private fun setSwipeLayoutClickListener(data: CartItemHolderData) {
        binding.apply {
            flSwipeDelete.setOnClickListener {
                if (swipeLayout.isOpen()) {
                    actionListener?.onCartItemDeleteButtonClicked(
                        data,
                        CartDeleteButtonSource.SwipeToDelete
                    )
                    binderHelper.closeAll()
                }
            }
            flSwipeDeleteBundling.setOnClickListener {
                if (swipeLayoutBundling.isOpen()) {
                    actionListener?.onCartItemDeleteButtonClicked(
                        data,
                        CartDeleteButtonSource.SwipeToDelete
                    )
                    binderHelper.closeAll()
                }
            }
        }
    }

    private fun shouldLockSwipeLayout(): Boolean {
        return false
    }

    private fun shouldLockBundlingSwipeLayout(): Boolean {
        return false
    }

    private fun initCoachMark() {
        if (mainCoachMark.coachMark != null && !CoachMarkPreference.hasShown(
                itemView.context,
                CART_MAIN_COACH_MARK
            )
        ) {
            val coachMarkItems = mainCoachMark.coachMarkItems
            val wishlistCoachMark = CoachMark2Item(
                binding.buttonToggleWishlist,
                String.EMPTY,
                mainCoachMark.wishlistOnBoardingData.text,
                CoachMark2.POSITION_BOTTOM
            )
            val noteCoachMark = CoachMark2Item(
                binding.buttonChangeNote,
                String.EMPTY,
                mainCoachMark.noteOnBoardingData.text,
                CoachMark2.POSITION_BOTTOM
            )
            coachMarkItems.addAll(Int.ZERO, listOf(noteCoachMark, wishlistCoachMark))
            mainCoachMark.coachMark?.showCoachMark(coachMarkItems)
            CoachMarkPreference.setShown(itemView.context, CART_MAIN_COACH_MARK, true)
        }
    }

    private fun setNoteAnimationResource() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.buttonChangeNoteLottie.setAnimation(R.raw.anim_cart_note_dark)
        } else {
            binding.buttonChangeNoteLottie.setAnimation(R.raw.anim_cart_note)
        }
    }

    private fun renderLeftAnchor(data: CartItemHolderData) {
        binding.vBundlingProductSeparator.show()
        if (data.isBundlingItem) {
            with(binding) {
                checkboxProduct.gone()
                vBundlingProductSeparator.show()
                val marginStart = if (data.isError) {
                    0
                } else {
                    BUNDLING_SEPARATOR_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
                }
                val marginErrorBundling =
                    MARGIN_12.dpToPx(itemView.resources.displayMetrics)
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProductInformation)
                if (data.isError) {
                    constraintSet.connect(
                        R.id.fl_image_product,
                        ConstraintSet.START,
                        R.id.v_bundling_product_separator,
                        ConstraintSet.START,
                        marginErrorBundling
                    )
                    constraintSet.connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.START,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.START
                    )
                    constraintSet.clear(R.id.v_bundling_product_separator, ConstraintSet.END)
                } else {
                    constraintSet.connect(
                        R.id.fl_image_product,
                        ConstraintSet.START,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.START,
                        marginStart
                    )
                    constraintSet.connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.START,
                        R.id.checkbox_anchor,
                        ConstraintSet.START
                    )
                    constraintSet.connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.END,
                        R.id.checkbox_anchor,
                        ConstraintSet.END,
                        MARGIN_VERTICAL_SEPARATOR.dpToPx(itemView.resources.displayMetrics)
                    )
                }
                constraintSet.connect(
                    R.id.fl_image_product,
                    ConstraintSet.TOP,
                    R.id.swipe_layout_bundling,
                    ConstraintSet.BOTTOM
                )
                constraintSet.applyTo(containerProductInformation)
                renderCheckBoxBundle(data)
            }
        } else {
            with(binding) {
                vBundlingProductSeparator.gone()
                checkboxProduct.show()
                val marginStart = if (data.isError) {
                    0
                } else {
                    MARGIN_4.dpToPx(itemView.resources.displayMetrics)
                }
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerProductInformation)
                constraintSet.connect(
                    R.id.fl_image_product,
                    ConstraintSet.START,
                    R.id.checkbox_product,
                    ConstraintSet.END,
                    marginStart
                )
                constraintSet.connect(
                    R.id.fl_image_product,
                    ConstraintSet.TOP,
                    R.id.container_product_information,
                    ConstraintSet.TOP
                )
                constraintSet.applyTo(containerProductInformation)
                renderCheckBoxProduct(data)
            }
        }
        adjustProductVerticalSeparatorConstraint(data)
    }

    private fun renderProductAction(data: CartItemHolderData) {
        with(binding) {
            buttonChangeNote.gone()
            buttonChangeNote.gone()
            buttonToggleWishlist.gone()
            ivAnimatedWishlist.gone()
            textProductUnavailableAction.gone()
            buttonDeleteCart.gone()
        }
        if (data.actionsData.isNotEmpty()) {
            data.actionsData.forEach {
                when (it.id) {
                    Action.ACTION_NOTES -> {
                        renderProductNotes(data)
                    }

                    Action.ACTION_WISHLIST, Action.ACTION_WISHLISTED -> {
                        renderActionWishlist(data)
                    }

                    Action.ACTION_CHECKOUTBROWSER, Action.ACTION_SIMILARPRODUCT, Action.ACTION_FOLLOWSHOP, Action.ACTION_VERIFICATION -> {
                        when {
                            data.selectedUnavailableActionId == Action.ACTION_CHECKOUTBROWSER && it.id == Action.ACTION_CHECKOUTBROWSER -> {
                                renderActionCheckoutInBrowser(it, data)
                            }

                            data.selectedUnavailableActionId == Action.ACTION_SIMILARPRODUCT && it.id == Action.ACTION_SIMILARPRODUCT -> {
                                renderActionSimilarProduct(it, data)
                            }

                            data.selectedUnavailableActionId == Action.ACTION_FOLLOWSHOP && it.id == Action.ACTION_FOLLOWSHOP -> {
                                renderFollowShop(it, data)
                            }

                            data.selectedUnavailableActionId == Action.ACTION_VERIFICATION && it.id == Action.ACTION_VERIFICATION -> {
                                renderVerification(it, data)
                            }
                        }
                    }

                    Action.ACTION_DELETE -> {
                        if (data.isError) {
                            renderActionDelete(data)
                        }
                    }
                }
            }
        }
    }

    private fun renderActionDelete(data: CartItemHolderData) {
        binding.buttonDeleteCart.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemDeleteButtonClicked(data, CartDeleteButtonSource.TrashBin)
            }
        }
        binding.buttonDeleteCart.show()
    }

    private fun renderCheckBoxProduct(data: CartItemHolderData) {
        val checkboxProduct = binding.checkboxProduct
        if (data.isError) {
            checkboxProduct.gone()
            return
        }

        checkboxProduct.show()
        checkboxProduct.setOnCheckedChangeListener { _, _ ->
            // disable listener before setting current selection state
        }
        checkboxProduct.isChecked = data.isSelected
        checkboxProduct.skipAnimation()

        var prevIsChecked: Boolean = checkboxProduct.isChecked
        checkboxProduct.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked != prevIsChecked) {
                prevIsChecked = isChecked

                delayChangeCheckboxState?.cancel()
                delayChangeCheckboxState = GlobalScope.launch(Dispatchers.Main) {
                    delay(DEBOUNCE_TIME)
                    if (isChecked == prevIsChecked && isChecked != data.isSelected) {
                        if (!data.isError) {
                            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                                actionListener?.onCartItemCheckChanged(bindingAdapterPosition, data)
                                handleCheckboxRefresh(data)
                            }
                        }
                    }
                }
            }

            if (compoundButton.isPressed) {
                if (!data.isError) {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        actionListener?.onCartItemCheckboxClickChanged(
                            bindingAdapterPosition,
                            data,
                            isChecked
                        )
                        viewHolderListener?.onNeedToRefreshSingleShop(
                            data,
                            bindingAdapterPosition
                        )
                    }
                }
            }
        }
    }

    private fun renderCheckBoxBundle(data: CartItemHolderData) {
        val checkboxBundle = binding.checkboxBundle
        val padding16 = MARGIN_16.dpToPx(itemView.resources.displayMetrics)
        binding.productBundlingInfo.setPadding(0, 0, 0, padding16)
        if (data.isError) {
            checkboxBundle.gone()
            return
        }
        checkboxBundle.show()
        checkboxBundle.setOnCheckedChangeListener { _, _ ->
            // disable listener before setting current selection state
        }
        checkboxBundle.isChecked = data.isSelected
        checkboxBundle.skipAnimation()

        var prevIsChecked: Boolean = checkboxBundle.isChecked
        checkboxBundle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != prevIsChecked) {
                prevIsChecked = isChecked

                delayChangeCheckboxState?.cancel()
                delayChangeCheckboxState = GlobalScope.launch(Dispatchers.Main) {
                    delay(DEBOUNCE_TIME)
                    if (isChecked == prevIsChecked && isChecked != data.isSelected) {
                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            actionListener?.onBundleItemCheckChanged(data)
                            handleCheckboxRefresh(data)
                        }
                    }
                }
            }
        }
    }

    private fun handleCheckboxRefresh(data: CartItemHolderData) {
        viewHolderListener?.onNeedToRefreshSingleShop(
            data,
            bindingAdapterPosition
        )
    }

    private fun renderShopInfo(data: CartItemHolderData) {
        if (data.isShopShown) {
            binding.llShopHeader.visible()
            val shopHolderData = data.shopHolderData
            binding.tvShopName.text = Utils.getHtmlFormat(shopHolderData.shopName)
            binding.tvShopName.setOnClickListener {
                actionListener?.onCartShopNameClicked(
                    data.shopHolderData.shopId,
                    data.shopHolderData.shopName,
                    data.shopHolderData.isTokoNow
                )
            }
            if (shopHolderData.shopTypeInfo.shopBadge.isNotBlank()) {
                binding.imageShopBadge.loadImageWithoutPlaceholder(shopHolderData.shopTypeInfo.shopBadge)
                binding.imageShopBadge.contentDescription = itemView.context.getString(
                    purchase_platformcommonR.string.pp_cd_image_shop_badge_with_shop_type,
                    shopHolderData.shopTypeInfo.title
                )
                binding.imageShopBadge.show()
            } else {
                binding.imageShopBadge.gone()
            }
        } else {
            binding.llShopHeader.gone()
        }
    }

    private fun renderProductInfo(data: CartItemHolderData) {
        renderBundlingInfo(data)
        renderProductName(data)
        renderImage(data)
        renderPrice(data)
        renderVariant(data)
        renderProductPropertiesContainer(data)
        renderQuantityLeft(data)
        renderSlashPrice(data)
        renderProductProperties(data)
        renderProductActionSection(data)
        renderProductAddOns(data)
        sendAnalyticsInformationLabel(data)
    }

    private fun renderProductPropertiesContainer(data: CartItemHolderData) {
        if ((data.productQtyLeft.isNotBlank() && !data.isError) || data.productInformation.isNotEmpty()) {
            binding.containerProductProperties.visible()
        } else {
            binding.containerProductProperties.gone()
        }
    }

    private fun renderBundlingInfo(data: CartItemHolderData) {
        if (data.isBundlingItem && data.bundlingItemPosition == BUNDLING_ITEM_HEADER) {
            binding.swipeLayoutBundling.show()
            binding.productBundlingInfo.show()

            binding.productBundlingInfo.updateLayoutParams<MarginLayoutParams> {
                if (data.isError) {
                    marginStart = 0
                    binding.checkboxBundle.gone()
                } else {
                    marginStart = MARGIN_4.dpToPx(itemView.resources.displayMetrics)
                    binding.checkboxBundle.show()
                }
            }

            renderBundlingInfoDetail(data)
        } else {
            binding.swipeLayoutBundling.gone()
            binding.productBundlingInfo.gone()
            binding.checkboxBundle.gone()
        }
    }

    private fun renderProductActionSection(data: CartItemHolderData) {
        val quantityEditorView = getQuantityEditorView()
        if (data.isBundlingItem) {
            if (data.isMultipleBundleProduct && (data.bundlingItemPosition == BUNDLING_ITEM_HEADER || data.bundlingItemPosition == CartItemHolderData.BUNDLING_ITEM_DEFAULT)) {
                quantityEditorView.gone()
            } else {
                quantityEditorView.show()
            }
            binding.buttonChangeNote.show()
            binding.buttonToggleWishlist.show()
        } else {
            quantityEditorView.show()
            binding.buttonChangeNote.show()
            binding.buttonToggleWishlist.show()
        }
        adjustProductActionConstraint(data)
    }

    private fun adjustProductActionConstraint(data: CartItemHolderData) {
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.containerProductInformation)
            val margin = MARGIN_12.dpToPx(itemView.resources.displayMetrics)
            val marginTop = WISHLIST_ANIMATED_MARGIN_TOP.dpToPx(itemView.resources.displayMetrics)
            if (data.isBundlingItem) {
                connect(
                    R.id.button_change_note,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_change_note_lottie,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_toggle_wishlist,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.iv_animated_wishlist,
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    marginTop
                )
                if (data.bundlingItemPosition == BUNDLING_ITEM_FOOTER || (!data.isMultipleBundleProduct)) {
                    connect(
                        getQuantityEditorLayoutId(),
                        ConstraintSet.TOP,
                        R.id.button_change_note,
                        ConstraintSet.BOTTOM,
                        marginTop
                    )
                } else {
                    connect(
                        getQuantityEditorLayoutId(),
                        ConstraintSet.TOP,
                        R.id.item_addon_cart,
                        ConstraintSet.BOTTOM,
                        marginTop
                    )
                }
                clear(R.id.button_change_note, ConstraintSet.BOTTOM)
                clear(R.id.button_change_note_lottie, ConstraintSet.BOTTOM)
                clear(R.id.button_toggle_wishlist, ConstraintSet.BOTTOM)
                clear(R.id.iv_animated_wishlist, ConstraintSet.BOTTOM)
                clear(getQuantityEditorLayoutId(), ConstraintSet.BOTTOM)
            } else {
                connect(
                    R.id.button_change_note,
                    ConstraintSet.TOP,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.button_change_note_lottie,
                    ConstraintSet.TOP,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.button_change_note,
                    ConstraintSet.BOTTOM,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_change_note_lottie,
                    ConstraintSet.BOTTOM,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.button_toggle_wishlist,
                    ConstraintSet.TOP,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.iv_animated_wishlist,
                    ConstraintSet.TOP,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.TOP,
                    margin
                )
                connect(
                    R.id.button_toggle_wishlist,
                    ConstraintSet.BOTTOM,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    R.id.iv_animated_wishlist,
                    ConstraintSet.BOTTOM,
                    getQuantityEditorLayoutId(),
                    ConstraintSet.BOTTOM,
                    margin
                )
                connect(
                    getQuantityEditorLayoutId(),
                    ConstraintSet.TOP,
                    R.id.item_addon_cart,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
            applyTo(binding.containerProductInformation)
        }
    }

    private fun adjustProductVerticalSeparatorConstraint(data: CartItemHolderData) {
        if (!data.isBundlingItem && !data.cartBmGmTickerData.isShowBmGmDivider) {
            binding.vBundlingProductSeparator.gone()
            return
        }
        binding.vBundlingProductSeparator.visible()
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.containerProductInformation)

            // Top
            if (data.cartBmGmTickerData.isShowBmGmDivider) {
                connect(
                    R.id.v_bundling_product_separator,
                    ConstraintSet.TOP,
                    R.id.checkbox_product,
                    ConstraintSet.BOTTOM,
                    MARGIN_6.dpToPx(itemView.resources.displayMetrics)
                )
            } else {
                if (data.isMultipleBundleProduct) {
                    if (data.bundlingItemPosition != BUNDLING_ITEM_HEADER) {
                        connect(
                            R.id.v_bundling_product_separator,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP,
                            0
                        )
                    } else {
                        connect(
                            R.id.v_bundling_product_separator,
                            ConstraintSet.TOP,
                            if (data.isError) R.id.swipe_layout_bundling else R.id.checkbox_anchor,
                            ConstraintSet.BOTTOM,
                            MARGIN_VERTICAL_SEPARATOR.dpToPx(itemView.resources.displayMetrics)
                        )
                    }
                } else {
                    connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.TOP,
                        if (data.isError) R.id.swipe_layout_bundling else R.id.checkbox_anchor,
                        ConstraintSet.BOTTOM,
                        MARGIN_VERTICAL_SEPARATOR.dpToPx(itemView.resources.displayMetrics)
                    )
                }
            }

            // Bottom
            if (data.cartBmGmTickerData.isShowBmGmDivider) {
                connect(
                    R.id.v_bundling_product_separator,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    MARGIN_4.dpToPx(itemView.resources.displayMetrics)
                )
            } else {
                if (data.isMultipleBundleProduct) {
                    if (data.bundlingItemPosition == BUNDLING_ITEM_FOOTER) {
                        connect(
                            R.id.v_bundling_product_separator,
                            ConstraintSet.BOTTOM,
                            R.id.fl_image_product,
                            ConstraintSet.BOTTOM,
                            0
                        )
                    } else {
                        connect(
                            R.id.v_bundling_product_separator,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM,
                            0
                        )
                    }
                } else {
                    connect(
                        R.id.v_bundling_product_separator,
                        ConstraintSet.BOTTOM,
                        R.id.fl_image_product,
                        ConstraintSet.BOTTOM,
                        MARGIN_VERTICAL_SEPARATOR.dpToPx(itemView.resources.displayMetrics)
                    )
                }
            }

            applyTo(binding.containerProductInformation)
        }
    }

    private fun renderBundlingInfoDetail(data: CartItemHolderData) {
        with(binding) {
            textBundleTitle.text = data.bundleTitle
            textBundlePrice.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(data.bundlePrice, false)
                    .removeDecimalSuffix()

            if (!data.isError && data.bundleSlashPriceLabel.isNotBlank()) {
                labelBundleSlashPricePercentage.text = String.format(
                    itemView.context.getString(R.string.cart_label_discount_percentage),
                    data.bundleSlashPriceLabel
                )
                labelBundleSlashPricePercentage.show()
            } else {
                labelBundleSlashPricePercentage.gone()
            }

            if (!data.isError && data.bundleOriginalPrice > 0) {
                textBundleSlashPrice.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(data.bundleOriginalPrice, false)
                        .removeDecimalSuffix()
                textBundleSlashPrice.paintFlags =
                    binding.textBundleSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textBundleSlashPrice.show()
            } else {
                textBundleSlashPrice.gone()
            }

            if (data.isError) {
                textChangeBundle.gone()
            } else {
                textChangeBundle.show()
                textChangeBundle.setOnClickListener {
                    actionListener?.onEditBundleClicked(data)
                }
            }
        }
    }

    private fun renderProductName(data: CartItemHolderData) {
        val marginTop = itemView.context.resources.getDimension(R.dimen.dp_2).toInt()
        if (data.isBundlingItem && data.bundleLabelQuantity > 0) {
            binding.textProductName.updateLayoutParams<MarginLayoutParams> {
                topMargin = marginTop
            }
            val labelBundleQuantityText = String.format(
                itemView.context.getString(R.string.cart_label_product_name_with_quantity),
                data.bundleLabelQuantity,
                data.productName
            )
            binding.textProductName.text = Utils.getHtmlFormat(labelBundleQuantityText)
        } else {
            binding.textProductName.updateLayoutParams<MarginLayoutParams> {
                topMargin = 0
            }
            binding.textProductName.text = Utils.getHtmlFormat(data.productName)
        }
        if (data.isError) {
            binding.textProductName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN400
                )
            )
        } else {
            binding.textProductName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN600
                )
            )
        }
        binding.textProductName.setOnClickListener(
            getOnClickProductItemListener(
                absoluteAdapterPosition,
                data
            )
        )
    }

    private fun renderImage(data: CartItemHolderData) {
        val frameBackground = ResourcesCompat.getDrawable(
            binding.root.resources,
            R.drawable.bg_cart_product_image,
            null
        )
        if (data.isError) {
            val nn900Color = ResourcesCompat.getColor(
                binding.root.resources,
                unifyprinciplesR.color.Unify_NN900,
                null
            )
            val nn900ColorAlpha = ColorUtils.setAlphaComponent(nn900Color, 127)
            val loadingDrawable = frameBackground as? GradientDrawable
            loadingDrawable?.mutate()
            loadingDrawable?.setColor(nn900ColorAlpha)
            binding.flImageProduct.foreground = frameBackground
        } else {
            val transparentColor = ResourcesCompat.getColor(
                binding.root.resources,
                android.R.color.transparent,
                null
            )
            val loadingDrawable = frameBackground as? GradientDrawable
            loadingDrawable?.mutate()
            loadingDrawable?.setColor(transparentColor)
            binding.flImageProduct.foreground = frameBackground
        }
        data.productImage.let {
            binding.iuImageProduct.loadImage(it)
        }
        binding.iuImageProduct.setOnClickListener(
            getOnClickProductItemListener(
                bindingAdapterPosition,
                data
            )
        )
        if (data.isError && data.isBundlingItem) {
            binding.flImageProduct.updateLayoutParams {
                width = 66.dpToPx(binding.root.resources.displayMetrics)
                height = 66.dpToPx(binding.root.resources.displayMetrics)
            }
            binding.iuImageProduct.updateLayoutParams {
                width = 66.dpToPx(binding.root.resources.displayMetrics)
                height = 66.dpToPx(binding.root.resources.displayMetrics)
            }
        } else {
            binding.flImageProduct.updateLayoutParams {
                width = 80.dpToPx(binding.root.resources.displayMetrics)
                height = 80.dpToPx(binding.root.resources.displayMetrics)
            }
            binding.iuImageProduct.updateLayoutParams {
                width = 80.dpToPx(binding.root.resources.displayMetrics)
                height = 80.dpToPx(binding.root.resources.displayMetrics)
            }
        }
    }

    private fun renderProductAddOns(data: CartItemHolderData) {
        if (data.addOnsProduct.listData.isNotEmpty() && data.addOnsProduct.widget.title.isNotEmpty()) {
            binding.itemAddonCart.apply {
                root.show()
                this.descAddon.text = MethodChecker.fromHtml(data.addOnsProduct.widget.title)
                this.priceAddon.text = MethodChecker.fromHtml(data.addOnsProduct.widget.price)
                val addOnType = data.addOnsProduct.listData.firstOrNull()?.type ?: 0
                root.setOnClickListener {
                    actionListener?.onProductAddOnClicked(data)
                    actionListener?.onClickAddOnsProductWidgetCart(addOnType, data.productId)
                }
                if (data.addOnsProduct.listData.isNotEmpty()) {
                    actionListener?.onAddOnsProductWidgetImpression(addOnType, data.productId)
                }
            }
        } else {
            binding.itemAddonCart.root.gone()
        }
    }

    private fun sendAnalyticsInformationLabel(data: CartItemHolderData) {
        if (informationLabel.isNotEmpty()) {
            sendAnalyticsShowInformation(informationLabel, data.productId)
        }
    }

    private fun renderProductProperties(data: CartItemHolderData) {
        val layoutProductInfo = binding.layoutProductInfo
        layoutProductInfo.gone()

        if (data.needPrescription) {
            binding.iuPrescription.visible()
            binding.textPrescription.visible()
            binding.iuPrescription.loadIcon(data.butuhResepIconUrl)
            binding.textPrescription.text = data.butuhResepText
        } else {
            binding.iuPrescription.gone()
            binding.textPrescription.gone()
        }

        val productInformationList = data.productInformation
        if (productInformationList.isNotEmpty()) {
            val isQuantityLeftShown = data.productQtyLeft.isNotBlank() && !data.isError
            binding.textProductInformation.visible()
            binding.textProductInformationSeparator.showWithCondition(isQuantityLeftShown)

            productInformationList.getOrNull(0)?.let {
                var tmpLabel = it
                if (tmpLabel.lowercase(Locale.ROOT).contains(LABEL_CASHBACK)) {
                    tmpLabel = LABEL_CASHBACK
                }
                informationLabel.add(tmpLabel.lowercase(Locale.ROOT))

                binding.textProductInformation.text = it
            }
        } else {
            binding.textProductInformation.gone()
            binding.textProductInformationSeparator.gone()
        }
    }

    private fun sendAnalyticsShowInformation(informationList: List<String>, productId: String) {
        val informations = informationList.joinToString(", ")
        actionListener?.onCartItemShowInformationLabel(productId, informations)
    }

    private fun renderPrice(data: CartItemHolderData) {
        if (data.isBundlingItem && !data.showBundlePrice) {
            binding.textProductPrice.gone()
        } else {
            binding.textProductPrice.visible()
        }
        if (data.isError) {
            binding.textProductPrice.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN400
                )
            )
        } else {
            binding.textProductPrice.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN950
                )
            )
        }

        if (data.wholesalePriceFormatted != null && data.isSelected) {
            binding.textProductPrice.text = data.wholesalePriceFormatted
                ?: ""
        } else {
            binding.textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                data.productPrice,
                false
            ).removeDecimalSuffix()
        }
    }

    private fun renderSlashPrice(data: CartItemHolderData) {
        val hasPriceOriginal = data.productOriginalPrice > 0
        val hasWholesalePrice = data.wholesalePrice > 0 && data.isSelected
        val hasPriceDrop = data.productInitialPriceBeforeDrop > 0 &&
            data.productInitialPriceBeforeDrop > data.productPrice
        if (!data.isError && (hasPriceOriginal || hasWholesalePrice || hasPriceDrop) && !data.isBundlingItem) {
            if (data.productSlashPriceLabel.isNotBlank()) {
                // Slash price
                renderSlashPriceFromCampaign(data)
            } else if (data.productInitialPriceBeforeDrop > 0) {
                val wholesalePrice = data.wholesalePrice
                if (hasWholesalePrice && wholesalePrice < data.productPrice) {
                    // Wholesale
                    renderSlashPriceFromWholesale(data)
                } else {
                    // Price drop
                    renderSlashPriceFromPriceDrop(data)
                }
            } else if (hasWholesalePrice) {
                // Wholesale
                renderSlashPriceFromWholesale(data)
            }

            binding.textSlashPrice.paintFlags =
                binding.textSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.textSlashPrice.show()
        } else {
            binding.textSlashPrice.gone()
            binding.groupSlashPricePercentage.gone()
        }
    }

    private fun renderSlashPriceFromWholesale(data: CartItemHolderData) {
        val priceDropValue = data.productInitialPriceBeforeDrop
        val productPrice = data.productPrice
        val originalPrice = if (priceDropValue > productPrice) productPrice else priceDropValue
        binding.textSlashPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice, false)
                .removeDecimalSuffix()
    }

    private fun renderSlashPriceFromPriceDrop(data: CartItemHolderData) {
        binding.textSlashPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
            data.productInitialPriceBeforeDrop,
            false
        ).removeDecimalSuffix()
    }

    private fun renderSlashPriceFromCampaign(data: CartItemHolderData) {
        binding.textSlashPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.productOriginalPrice, false)
                .removeDecimalSuffix()
        binding.labelSlashPricePercentage.text = data.productSlashPriceLabel
        binding.groupSlashPricePercentage.show()
        informationLabel.add(LABEL_DISCOUNT)
    }

    private fun renderQuantityLeft(data: CartItemHolderData) {
        if (data.productQtyLeft.isNotBlank() && !data.isError) {
            binding.textQtyLeft.text = data.productQtyLeft
            binding.textQtyLeft.show()
            actionListener?.onCartItemShowRemainingQty(data.productId)
        } else {
            binding.textQtyLeft.gone()
        }
    }

    private fun renderVariant(data: CartItemHolderData) {
        var paddingRight = 0
        val paddingTop = itemView.resources.getDimensionPixelOffset(R.dimen.dp_2)
        val textProductVariant = binding.itemVariantCart.textProductVariant
        if (!data.isError && data.variant.isNotBlank()) {
            textProductVariant.text = data.variant
            textProductVariant.show()
            paddingRight = itemView.resources.getDimensionPixelOffset(R.dimen.dp_4)
            if (data.isEnableCartVariant) {
                textProductVariant.setOnClickListener {
                    actionListener?.onClickChangeVariant(
                        data.productId,
                        data.shopHolderData.shopId,
                        data.cartId,
                        data.quantity
                    )
                }
                binding.itemVariantCart.iconVariant.setImage(IconUnify.CHEVRON_DOWN)
                binding.itemVariantCart.iconVariant.setOnClickListener {
                    actionListener?.onClickChangeVariant(
                        data.productId,
                        data.shopHolderData.shopId,
                        data.cartId,
                        data.quantity
                    )
                }
                binding.itemVariantCart.iconVariant.show()
            } else {
                binding.itemVariantCart.iconVariant.setOnClickListener(null)
                binding.itemVariantCart.iconVariant.gone()
            }
        } else {
            textProductVariant.setOnClickListener(null)
            textProductVariant.gone()
            binding.itemVariantCart.iconVariant.setOnClickListener(null)
            binding.itemVariantCart.iconVariant.gone()
        }
        textProductVariant.setPadding(0, paddingTop, paddingRight, 0)
        if (data.isError) {
            textProductVariant.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN400
                )
            )
        } else {
            textProductVariant.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN600
                )
            )
        }
    }

    private fun renderProductNotes(element: CartItemHolderData) {
        if (element.isError) return
        binding.buttonChangeNote.show()
        binding.buttonChangeNoteLottie.gone()
        binding.buttonChangeNote.setOnClickListener {
            actionListener?.onNoteClicked(
                element,
                binding.buttonChangeNote,
                binding.buttonChangeNoteLottie,
                absoluteAdapterPosition
            )
        }

        if (element.notes.isNotBlank()) {
            renderProductNotesFilled()
        } else {
            renderProductNotesEmpty()
        }
    }

    private fun renderProductNotesEmpty() {
        binding.buttonChangeNote.setImageResource(purchase_platformcommonR.drawable.ic_pp_add_note)
        binding.buttonChangeNote.contentDescription =
            binding.root.context.getString(purchase_platformcommonR.string.cart_button_notes_empty_content_desc)
    }

    private fun renderProductNotesFilled() {
        binding.buttonChangeNote.setImageResource(purchase_platformcommonR.drawable.ic_pp_add_note_completed)
        binding.buttonChangeNote.contentDescription =
            binding.root.context.getString(purchase_platformcommonR.string.cart_button_notes_filled_content_desc)
    }

    private fun renderOldQuantity(
        data: CartItemHolderData,
        viewHolderListener: ViewHolderListener?
    ) {
        val qtyEditorProduct = binding.oldQtyEditorProduct
        if (data.isError) {
            qtyEditorProduct.gone()
            return
        }
        qtyEditorProduct.autoHideKeyboard = true
        qtyEditorProduct.errorMessage.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        qtyEditorProduct.errorMessage.setType(Typography.DISPLAY_3)

        if (data.isAlreadyShowMinimumQuantityPurchasedError) {
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_min_quantity_error),
                data.minOrder
            )
            binding.labelQuantityError.visible()
        }

        if (data.isAlreadyShowMaximumQuantityPurchasedError) {
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_max_quantity_error),
                data.maxOrder
            )
            binding.labelQuantityError.visible()
        }

        if (!data.isAlreadyShowMinimumQuantityPurchasedError && !data.isAlreadyShowMaximumQuantityPurchasedError) {
            binding.labelQuantityError.gone()
        }

        if (qtyTextWatcher != null) {
            // reset listener
            qtyEditorProduct.editText.removeTextChangedListener(qtyTextWatcher)
        }
        qtyEditorProduct.minValue = 0
        qtyEditorProduct.maxValue = data.maxOrder
        if (data.isBundlingItem) {
            qtyEditorProduct.setValue(data.bundleQuantity)
        } else {
            qtyEditorProduct.setValue(data.quantity)
        }
        qtyTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                delayChangeQty?.cancel()
                delayChangeQty = GlobalScope.launch(Dispatchers.Main) {
                    val newValue = s.toString().replace(".", "").toIntOrZero()
                    lastQty = newValue
                    val minOrder = data.minOrder
                    if (newValue >= minOrder) {
                        delay(DEBOUNCE_TIME)
                    } else {
                        // Use longer delay for reset qty, to support automation
                        delay(RESET_QTY_DEBOUNCE_TIME)
                    }
                    val previousQuantity =
                        if (data.isBundlingItem) data.bundleQuantity else data.quantity
                    if (isActive && previousQuantity != newValue) {
                        if (!qtyEditorProduct.editText.isFocused) {
                            if (isDeleteFromDoneImeButton) {
                                isDeleteFromDoneImeButton = false
                            } else {
                                validateOldQty(newValue, data)
                            }
                            if (isActive && newValue != 0) {
                                actionListener?.onCartItemQuantityChanged(data, newValue)
                                handleRefreshType(data, viewHolderListener)
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        qtyEditorProduct.editText.addTextChangedListener(qtyTextWatcher)
        qtyEditorProduct.setSubstractListener {
            if (!data.isError && bindingAdapterPosition != RecyclerView.NO_POSITION) {
                val currentQuantity =
                    if (data.isBundlingItem) data.bundleQuantity else data.quantity
                if ((currentQuantity == 1 && data.minOrder == 1) || (currentQuantity == data.minOrder && data.isAlreadyShowMinimumQuantityPurchasedError)) {
                    delayChangeQty?.cancel()
                    actionListener?.onCartItemDeleteButtonClicked(
                        data,
                        CartDeleteButtonSource.TrashBin
                    )
                    actionListener?.sendRemoveCartFromSubtractButtonAnalytic(data)
                }
                actionListener?.onCartItemQuantityMinusButtonClicked()
            }
        }
        qtyEditorProduct.setAddClickListener {
            if (!data.isError && bindingAdapterPosition != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemQuantityPlusButtonClicked()
            }
        }
        qtyEditorProduct.editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val qtyStr = (v as? AppCompatEditText)?.text?.toString() ?: ""
                actionListener?.onCartItemQuantityInputFormClicked(
                    if (!TextUtils.isEmpty(qtyStr)) qtyStr else ""
                )
            }
        }
        qtyEditorProduct.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.DropKeyboard(qtyEditorProduct.editText.context, itemView)
                val isDeletion = qtyEditorProduct.editText.text.toString() == "0"
                isDeleteFromDoneImeButton = isDeletion
                if (isDeletion) {
                    delayChangeQty?.cancel()
                    actionListener?.onCartItemDeleteButtonClicked(
                        data,
                        CartDeleteButtonSource.QuantityEditorImeAction
                    )
                }
                if (lastQty > data.maxOrder) {
                    binding.labelQuantityError.text = String.format(
                        itemView.context.getString(R.string.cart_max_quantity_error),
                        data.maxOrder
                    )
                    data.isAlreadyShowMaximumQuantityPurchasedError = true
                    binding.labelQuantityError.show()
                } else if (lastQty > data.minOrder && lastQty < data.maxOrder) {
                    data.isAlreadyShowMaximumQuantityPurchasedError = false
                    binding.labelQuantityError.gone()
                }
                true
            } else {
                false
            }
        }
        qtyEditorProduct.editText.imeOptions = EditorInfo.IME_ACTION_DONE
        qtyEditorProduct.editText.isEnabled = data.isError == false
    }

    private fun renderQuantityWidget(data: CartItemHolderData) {
        if (isUsingNewQuantityEditor()) {
            renderQuantity(data, viewHolderListener)
        } else {
            renderOldQuantity(data, viewHolderListener)
        }
    }

    private fun renderQuantity(data: CartItemHolderData, viewHolderListener: ViewHolderListener?) {
        val qtyEditorProduct = binding.qtyEditorProduct
        if (data.isError) {
            qtyEditorProduct.gone()
            return
        }

        if (data.isAlreadyShowMinimumQuantityPurchasedError) {
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_min_quantity_error),
                data.minOrder
            )
            binding.labelQuantityError.visible()
        }

        if (data.isAlreadyShowMaximumQuantityPurchasedError) {
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_max_quantity_error),
                data.maxOrder
            )
            binding.labelQuantityError.visible()
        }

        if (!data.isAlreadyShowMinimumQuantityPurchasedError && !data.isAlreadyShowMaximumQuantityPurchasedError) {
            binding.labelQuantityError.gone()
        }

        qtyEditorProduct.apply {
            onFocusChanged = { focus ->
                val currentFocus = qtyState.value
                if (currentFocus is QtyState.Focus && !focus.isFocused) {
                    val newQty = qtyValue.value
                    if (newQty == 0) {
                        actionListener?.onCartItemDeleteButtonClicked(
                            data,
                            CartDeleteButtonSource.QuantityEditorImeAction
                        )
                    } else {
                        validateQty(newQty, data)
                        lastQty = qtyValue.value
                        actionListener?.onCartItemQuantityChanged(data, qtyValue.value)
                        handleRefreshType(data, viewHolderListener)
                        hideKeyboard()
                        actionListener?.clearAllFocus()
                    }
                }
                qtyState.value = if (focus.isFocused) QtyState.Focus else QtyState.Enabled
            }
            keyboardOptions.value = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            )
            keyboardActions.value = KeyboardActions(
                onDone = {
                    val newQty = qtyValue.value
                    if (newQty == 0) {
                        actionListener?.onCartItemDeleteButtonClicked(
                            data,
                            CartDeleteButtonSource.QuantityEditorImeAction
                        )
                        return@KeyboardActions
                    }
                    validateQty(newQty, data)
                    lastQty = qtyValue.value
                    actionListener?.onCartItemQuantityChanged(data, qtyValue.value)
                    handleRefreshType(data, viewHolderListener)
                    hideKeyboard()
                    actionListener?.clearAllFocus()
                }
            )
            qtyValue.value = if (data.isBundlingItem) data.bundleQuantity else data.quantity
            configState.value = configState.value.copy(
                qtyMinusButton = getQuantityEditorMinButton(
                    if (data.isBundlingItem) data.bundleQuantity else data.quantity,
                    data
                ),
                qtyPlusButton = configState.value.qtyPlusButton.copy(
                    onClick = {
                        if (!data.isError && bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            actionListener?.onCartItemQuantityPlusButtonClicked()
                        }
                    }
                ),
                minInt = 0,
                maxInt = data.maxOrder
            )

            onValueChanged = { qty ->
                configState.value = configState.value.copy(
                    qtyMinusButton = getQuantityEditorMinButton(qty, data)
                )
                if (qtyState.value !is QtyState.Focus) {
                    validateQty(qty, data)
                    if (qty != 0) {
                        lastQty = qty
                        actionListener?.onCartItemQuantityChanged(data, qty)
                        handleRefreshType(data, viewHolderListener)
                    }
                } else {
                    qtyValue.value = qty
                }
            }
        }
    }

    private fun getQuantityEditorMinButton(quantity: Int, data: CartItemHolderData): QtyButton {
        return if (quantity == data.minOrder) {
            binding.qtyEditorProduct.configState.value.qtyMinusButton.copy(
                iconUnifyId = IconUnify.DELETE_SMALL,
                layoutId = CART_TRASH_ICON_LAYOUT_ID,
                colorInt = nestcomponentsR.color.Unify_NN950,
                qtyEnabledCondition = { _, _ -> true },
                onClick = {
                    actionListener?.onCartItemDeleteButtonClicked(
                        data,
                        CartDeleteButtonSource.TrashBin
                    )
                    actionListener?.clearAllFocus()
                }
            )
        } else {
            QtyButton.defaultMinButton
        }
    }

    private fun validateOldQty(newValue: Int, element: CartItemHolderData) {
        val qtyEditorCart = binding.oldQtyEditorProduct
        if (newValue > element.minOrder) {
            element.isAlreadyShowMinimumQuantityPurchasedError = false
        }
        if (newValue < element.maxOrder) {
            element.isAlreadyShowMaximumQuantityPurchasedError = false
        }
        if (newValue < element.minOrder) {
            if (element.minOrder <= 1) {
                actionListener?.onCartItemDeleteButtonClicked(
                    element,
                    CartDeleteButtonSource.TrashBin
                )
                return
            }
            binding.labelQuantityError.show()
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_min_quantity_error),
                element.minOrder
            )
            if (!element.isAlreadyShowMinimumQuantityPurchasedError) {
                qtyEditorCart.setValue(element.minOrder)
                element.isAlreadyShowMinimumQuantityPurchasedError = true
            } else {
                element.isAlreadyShowMinimumQuantityPurchasedError = false
                actionListener?.onCartItemDeleteButtonClicked(
                    element,
                    CartDeleteButtonSource.TrashBin
                )
            }
        }
        qtyEditorCart.addButton.isEnabled = true
        qtyEditorCart.subtractButton.isEnabled = true
    }

    private fun validateQty(newQty: Int, data: CartItemHolderData) {
        validateMaximumQty(newQty, data)
        validateMinimumQty(newQty, data)
    }

    private fun validateMaximumQty(newQty: Int, data: CartItemHolderData) {
        if (newQty > data.maxOrder) {
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_max_quantity_error),
                data.maxOrder
            )
            data.isAlreadyShowMaximumQuantityPurchasedError = true
            binding.qtyEditorProduct.qtyValue.value = data.maxOrder
            binding.labelQuantityError.show()
        } else if (newQty > data.minOrder && newQty < data.maxOrder) {
            data.isAlreadyShowMaximumQuantityPurchasedError = false
            binding.labelQuantityError.gone()
        }
    }

    private fun validateMinimumQty(newQty: Int, data: CartItemHolderData) {
        val qtyEditorCart = binding.qtyEditorProduct
        if (newQty > data.minOrder) {
            data.isAlreadyShowMinimumQuantityPurchasedError = false
        }
        if (newQty < data.maxOrder) {
            data.isAlreadyShowMaximumQuantityPurchasedError = false
        }
        if (lastQty > data.minOrder && newQty == data.minOrder && data.minOrder > 1) {
            binding.labelQuantityError.show()
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_min_quantity_error),
                data.minOrder
            )
            if (!data.isAlreadyShowMinimumQuantityPurchasedError) {
                qtyEditorCart.qtyValue.value = data.minOrder
                data.isAlreadyShowMinimumQuantityPurchasedError = true
            } else {
                data.isAlreadyShowMinimumQuantityPurchasedError = false
                actionListener?.onCartItemDeleteButtonClicked(data, CartDeleteButtonSource.TrashBin)
                actionListener?.clearAllFocus()
            }
            return
        }
        if (newQty < data.minOrder) {
            if (data.minOrder <= 1) {
                actionListener?.onCartItemDeleteButtonClicked(data, CartDeleteButtonSource.TrashBin)
                actionListener?.clearAllFocus()
                return
            }
            binding.labelQuantityError.show()
            binding.labelQuantityError.text = String.format(
                itemView.context.getString(R.string.cart_min_quantity_error),
                data.minOrder
            )
            if (!data.isAlreadyShowMinimumQuantityPurchasedError) {
                qtyEditorCart.qtyValue.value = data.minOrder
                data.isAlreadyShowMinimumQuantityPurchasedError = true
            } else {
                data.isAlreadyShowMinimumQuantityPurchasedError = false
                actionListener?.onCartItemDeleteButtonClicked(data, CartDeleteButtonSource.TrashBin)
                actionListener?.clearAllFocus()
            }
            return
        }
        return
    }

    private fun handleRefreshType(
        data: CartItemHolderData,
        viewHolderListener: ViewHolderListener?
    ) {
        if (data.wholesalePriceData.isNotEmpty()) {
            viewHolderListener?.onNeedToRefreshSingleShop(data, bindingAdapterPosition)
        } else if (data.shouldValidateWeight) {
            viewHolderListener?.onNeedToRefreshWeight(data)
            viewHolderListener?.onNeedToRefreshSingleProduct(bindingAdapterPosition)
        } else if (data.shouldCheckBoAffordability) {
            viewHolderListener?.onNeedToRefreshBoAffordability(data)
            viewHolderListener?.onNeedToRefreshSingleProduct(bindingAdapterPosition)
        } else {
            viewHolderListener?.onNeedToRefreshSingleProduct(bindingAdapterPosition)
        }
    }

    private fun renderActionWishlist(data: CartItemHolderData) {
        if (data.isError) {
            return
        }
        if (data.isWishlisted) {
            val inWishlistColor = ContextCompat.getColor(
                itemView.context,
                unifyprinciplesR.color.Unify_RN500
            )
            binding.buttonToggleWishlist.setImage(
                IconUnify.HEART_FILLED,
                inWishlistColor,
                inWishlistColor,
                inWishlistColor,
                inWishlistColor
            )
        } else {
            val notInWishlistColor = ContextCompat.getColor(
                itemView.context,
                unifyprinciplesR.color.Unify_NN500
            )
            binding.buttonToggleWishlist.setImage(
                IconUnify.HEART,
                notInWishlistColor,
                notInWishlistColor,
                notInWishlistColor,
                notInWishlistColor
            )
        }
        binding.buttonToggleWishlist.setOnClickListener {
            delayChangeWishlistStatus?.cancel()
            delayChangeWishlistStatus = GlobalScope.launch(Dispatchers.Main) {
                delay(DEBOUNCE_TIME)
                data.isWishlisted = !data.isWishlisted
                actionListener?.onWishlistCheckChanged(
                    data,
                    binding.buttonToggleWishlist,
                    binding.ivAnimatedWishlist,
                    absoluteAdapterPosition
                )
            }
        }
        binding.buttonToggleWishlist.show()
    }

    private fun getOnClickProductItemListener(
        @SuppressLint("RecyclerView") position: Int,
        data: CartItemHolderData
    ): View.OnClickListener {
        return View.OnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                actionListener?.onCartItemProductClicked(data)
            }
        }
    }

    private fun renderActionCheckoutInBrowser(action: Action, data: CartItemHolderData) {
        binding.textProductUnavailableAction.apply {
            text = action.message
            setOnClickListener {
                if (data.selectedUnavailableActionLink.isNotBlank()) {
                    actionListener?.onTobaccoLiteUrlClicked(
                        data.selectedUnavailableActionLink,
                        data,
                        action
                    )
                }
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN600
                )
            )
            actionListener?.onShowTickerTobacco()
            show()
        }
    }

    private fun renderActionSimilarProduct(action: Action, data: CartItemHolderData) {
        if (data.isBundlingItem) {
            binding.textProductUnavailableAction.gone()
        } else {
            binding.textProductUnavailableAction.apply {
                text = action.message
                setOnClickListener {
                    if (data.selectedUnavailableActionLink.isNotBlank()) {
                        actionListener?.onSimilarProductUrlClicked(data)
                    }
                }
                actionListener?.onShowActionSeeOtherProduct(data.productId, data.errorType)
                show()
            }
        }
    }

    private fun renderFollowShop(action: Action, data: CartItemHolderData) {
        binding.textProductUnavailableAction.apply {
            text = action.message
            setOnClickListener {
                if (data.shopHolderData.shopId.isNotEmpty()) {
                    actionListener?.onFollowShopClicked(data.shopHolderData.shopId, data.errorType)
                }
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_GN500
                )
            )
            show()
        }
    }

    private fun renderVerification(action: Action, data: CartItemHolderData) {
        binding.textProductUnavailableAction.apply {
            text = action.message
            setOnClickListener {
                if (data.selectedUnavailableActionLink.isNotEmpty()) {
                    actionListener?.onVerificationClicked(data.selectedUnavailableActionLink)
                }
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN600
                )
            )
            show()
        }
    }

    private fun renderAlpha(cartItemHolderData: CartItemHolderData) {
        with(binding) {
            if (cartItemHolderData.isError) {
                productBundlingInfo.alpha = ALPHA_HALF
            } else {
                productBundlingInfo.alpha = ALPHA_FULL
            }
        }
    }

    private fun renderContainer(cartItemHolderData: CartItemHolderData) {
        if (cartItemHolderData.isBundlingItem && cartItemHolderData.isMultipleBundleProduct) {
            binding.flImageProduct.updateLayoutParams<MarginLayoutParams> {
                topMargin = if (cartItemHolderData.bundlingItemPosition != BUNDLING_ITEM_HEADER) {
                    MARGIN_12.dpToPx(itemView.resources.displayMetrics)
                } else {
                    0
                }
            }

            binding.containerProductInformation.updateLayoutParams<MarginLayoutParams> {
                bottomMargin =
                    if (cartItemHolderData.bundlingItemPosition == BUNDLING_ITEM_FOOTER) {
                        MARGIN_16.dpToPx(itemView.resources.displayMetrics)
                    } else {
                        0
                    }
            }
        } else {
            binding.flImageProduct.updateLayoutParams<MarginLayoutParams> {
                topMargin = 0
            }

            binding.containerProductInformation.updateLayoutParams<MarginLayoutParams> {
                if (cartItemHolderData.cartBmGmTickerData.isShowTickerBmGm || cartItemHolderData.cartBmGmTickerData.isShowBmGmDivider) {
                    bottomMargin = MARGIN_4.dpToPx(itemView.resources.displayMetrics)
                    binding.bmgmHelperView1.visible()
                    binding.bmgmHelperView2.visible()
                } else {
                    bottomMargin = MARGIN_16.dpToPx(itemView.resources.displayMetrics)
                    binding.bmgmHelperView1.gone()
                    binding.bmgmHelperView2.gone()
                }
            }

            binding.bottomDivider.gone()
        }
    }

    private fun renderDivider(cartItemHolderData: CartItemHolderData) {
        binding.bottomDivider.updateLayoutParams<MarginLayoutParams> {
            if (cartItemHolderData.showErrorBottomDivider) {
                height = DEFAULT_DIVIDER_HEIGHT.dpToPx(itemView.resources.displayMetrics)
                marginStart = if (cartItemHolderData.shouldDivideHalfErrorBottomDivider) {
                    BOTTOM_DIVIDER_MARGIN_START.dpToPx(itemView.resources.displayMetrics)
                } else {
                    0
                }
                binding.bottomDivider.visible()
            } else if (cartItemHolderData.showBmGmBottomDivider) {
                marginStart = MARGIN_18.dpToPx(itemView.resources.displayMetrics)
                bottomMargin = MARGIN_16.dpToPx(itemView.resources.displayMetrics)
                binding.bottomDivider.visible()
            } else {
                binding.bottomDivider.gone()
            }
        }
    }

    private fun renderBmGmOfferTicker(data: CartItemHolderData) {
        if (data.cartBmGmTickerData.isShowTickerBmGm) {
            binding.itemCartBmgm.root.visible()
            when (data.cartBmGmTickerData.stateTickerBmGm) {
                CART_BMGM_STATE_TICKER_LOADING -> {
                    binding.itemCartBmgm.bmgmWidgetView.state = BmGmWidgetView.State.LOADING
                }

                CART_BMGM_STATE_TICKER_ACTIVE -> {
                    binding.itemCartBmgm.bmgmWidgetView.state = BmGmWidgetView.State.ACTIVE
                    binding.itemCartBmgm.bmgmWidgetView.title =
                        data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerMessage.joinToString(
                            " • "
                        )
                    binding.itemCartBmgm.bmgmWidgetView.urlLeftIcon =
                        data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerIcon
                    binding.itemCartBmgm.bmgmWidgetView.offerId =
                        data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId
                    binding.itemCartBmgm.bmgmWidgetView.setOnClickListener {
                        actionListener?.onBmGmChevronRightClicked(
                            data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerLandingPageLink,
                            data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId,
                            data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerMessage.joinToString(
                                " • "
                            ),
                            data.shopHolderData.shopId
                        )
                    }

                    actionListener?.onCartViewBmGmTicker(
                        data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId,
                        data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerMessage.joinToString(
                            " • "
                        ),
                        data.shopHolderData.shopId
                    )
                }

                CART_BMGM_STATE_TICKER_INACTIVE -> {
                    binding.itemCartBmgm.bmgmWidgetView.state = BmGmWidgetView.State.INACTIVE
                    binding.itemCartBmgm.bmgmWidgetView.setOnClickListener {
                        actionListener?.onBmGmTickerReloadClicked()
                    }
                }
            }
        } else {
            binding.itemCartBmgm.root.gone()
        }
    }

    private fun renderProductTagInfo(data: CartItemHolderData) {
        if (data.productTagInfo.isNotEmpty()) {
            binding.textProductTagInfo.apply {
                visible()
                text = data.productTagInfo.firstOrNull()?.message ?: ""
            }
        } else {
            binding.textProductTagInfo.gone()
        }
    }

    private fun renderPurchaseBenefitWidget(data: CartItemHolderData) {
        val tierProductData =
            data.cartBmGmTickerData.bmGmCartInfoData.bmGmTierProductList.firstOrNull()
        val isShown = tierProductData?.purchaseBenefitData?.isShown == true
        binding.purchaseBenefitContainer.isVisible = isShown
        binding.purchaseBenefitContainer.setPadding(
            0,
            0,
            0,
            if (isShown) MARGIN_16.dpToPx(itemView.resources.displayMetrics) else 0
        )
        if (data.cartBmGmTickerData.stateTickerBmGm == CART_BMGM_STATE_TICKER_LOADING) {
            binding.benefitPurchaseWidget.showLoadingState()
        } else if (data.cartBmGmTickerData.stateTickerBmGm == CART_BMGM_STATE_TICKER_INACTIVE) {
            binding.benefitPurchaseWidget.showErrorState {
                actionListener?.onBmGmTickerReloadClicked()
            }
        } else if (tierProductData != null) {
            if (isShown) {
                actionListener?.onViewPurchaseBenefit(
                    data,
                    data.cartBmGmTickerData.bmGmCartInfoData,
                    tierProductData
                )
            }
            val purchaseBenefitData = tierProductData.purchaseBenefitData
            binding.benefitPurchaseWidget.setRibbonText(purchaseBenefitData.benefitWording)
            binding.benefitPurchaseWidget.updateData(purchaseBenefitData.getProductGiftUiModel())
            binding.benefitPurchaseWidget.setupCtaClickListener(purchaseBenefitData.actionWording) {
                actionListener?.onClickPurchaseBenefitActionListener(
                    data,
                    data.cartBmGmTickerData.bmGmCartInfoData,
                    tierProductData
                )
            }
        }
    }

    private fun renderCartCampaignFestivityTicker(data: CartItemHolderData) {
        val productLabelData = data.cartProductLabelData
        when (productLabelData.type) {
            CartProductLabelData.TYPE_DEFAULT -> {
                val useImageLogo = productLabelData.imageLogoUrl.isNotBlank()
                val useTextLogo = productLabelData.text.isNotBlank()
                if (useImageLogo) {
                    binding.cartCampaignProductLabel.showImageLabel(
                        logoUrl = productLabelData.imageLogoUrl,
                        backgroundStartColor = productLabelData.backgroundStartColor,
                        backgroundEndColor = productLabelData.backgroundEndColor
                    )
                } else if (useTextLogo) {
                    binding.cartCampaignProductLabel.showTextLabel(
                        text = productLabelData.text,
                        textColor = productLabelData.textColor,
                        backgroundStartColor = productLabelData.backgroundStartColor,
                        backgroundEndColor = productLabelData.backgroundEndColor
                    )
                } else {
                    binding.cartCampaignProductLabel.hideTicker()
                }
            }

            CartProductLabelData.TYPE_TIMER -> {
                val remainingTimeMillis =
                    productLabelData.localExpiredTimeMillis - System.currentTimeMillis()
                if (productLabelData.alwaysShowTimer) {
                    binding.cartCampaignProductLabel.showTimedLabel(
                        remainingTimeMillis = remainingTimeMillis,
                        iconUrl = productLabelData.iconUrl,
                        backgroundColor = productLabelData.lineColor,
                        alwaysShowTimer = true
                    )
                } else {
                    binding.cartCampaignProductLabel.hideTicker()
                }
            }

            else -> {
                binding.cartCampaignProductLabel.hideTicker()
            }
        }
    }

    fun getItemViewBinding(): ItemCartProductRevampBinding {
        return binding
    }

    interface ViewHolderListener {

        fun onNeedToRefreshSingleProduct(childPosition: Int)

        fun onNeedToRefreshSingleShop(cartItemHolderData: CartItemHolderData, itemPosition: Int)

        fun onNeedToRefreshWeight(cartItemHolderData: CartItemHolderData)

        fun onNeedToRefreshBoAffordability(cartItemHolderData: CartItemHolderData)
    }

    companion object {
        val TYPE_VIEW_ITEM_CART = R.layout.item_cart_product_revamp

        const val LABEL_CASHBACK = "cashback"
        const val LABEL_DISCOUNT = "label diskon"

        private const val DEBOUNCE_TIME = 500L
        private const val RESET_QTY_DEBOUNCE_TIME = 1000L
        const val ALPHA_HALF = 0.5f
        const val ALPHA_FULL = 1.0f

        private const val MARGIN_4 = 4
        private const val MARGIN_6 = 6
        private const val MARGIN_12 = 12
        private const val MARGIN_16 = 16
        private const val MARGIN_18 = 18
        private const val MARGIN_VERTICAL_SEPARATOR = 8

        private const val DEFAULT_DIVIDER_HEIGHT = 2
        private const val WISHLIST_ANIMATED_MARGIN_TOP = 13
        private const val BUNDLING_SEPARATOR_MARGIN_START = 38
        private const val BOTTOM_DIVIDER_MARGIN_START = 114

        private const val CART_MAIN_COACH_MARK = "cart_main_coach_mark"

        private const val CART_TRASH_ICON_LAYOUT_ID = "quantity_editor_trash"
    }

    private fun getQuantityEditorView(): View {
        return if (isUsingNewQuantityEditor()) binding.qtyEditorProduct else binding.oldQtyEditorProduct
    }

    fun getQuantityEditorLayoutId(): Int {
        return if (isUsingNewQuantityEditor()) R.id.qty_editor_product else R.id.old_qty_editor_product
    }

    fun getOldQuantityEditorAnchorView(): QuantityEditorUnify {
        return binding.oldQtyEditorProduct
    }

    fun getNewQuantityEditorAnchorView(): QuantityEditorView {
        return binding.qtyEditorProduct
    }

    fun isUsingNewQuantityEditor(): Boolean {
        return remoteConfig.getBoolean(
            RemoteConfigKey.ANDROID_ENABLE_NEW_CART_QUANTITY_EDITOR,
            false
        )
    }
}
