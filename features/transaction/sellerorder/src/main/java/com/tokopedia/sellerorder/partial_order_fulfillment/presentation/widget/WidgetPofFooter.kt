package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.updateLayoutParams
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.FooterPofBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.PofFooterUiState
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2

class WidgetPofFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding: FooterPofBinding = inflateContent()
    private var listener: Listener? = null

    private fun inflateContent(): FooterPofBinding {
        return FooterPofBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun updateUi(state: PofFooterUiState) {
        when (state) {
            is PofFooterUiState.InitialLoadError -> onInitialLoadError(state.priceEstimationData, state.buttonData)
            is PofFooterUiState.Loading -> onLoading(state.priceEstimationData, state.buttonData)
            is PofFooterUiState.ErrorReFetch -> onErrorReFetch(state.priceEstimationData, state.buttonData)
            is PofFooterUiState.ReFetch -> onReFetch(state.priceEstimationData, state.buttonData)
            is PofFooterUiState.ShowingPofRequestData -> onShowingPofRequestData(state.priceEstimationData, state.buttonData)
            is PofFooterUiState.ShowingPofResultData -> onShowingPofResultData(state.priceEstimationData, state.buttonData)
        }
    }

    fun attachListener(listener: Listener) {
        this.listener = listener
    }

    private fun onInitialLoadError(
        priceEstimationData: PofFooterUiState.PriceEstimationData,
        buttonData: PofFooterUiState.SendPofButtonData
    ) {
        setupTitle(title = priceEstimationData.title, show = true)
        setupPrice(price = priceEstimationData.price, show = true, showButton = buttonData.show)
        setupIcon(
            icon = priceEstimationData.icon,
            show = priceEstimationData.showIcon,
            eventData = priceEstimationData.iconEventData,
            wrap = priceEstimationData.iconWrapped,
            size = priceEstimationData.iconSize
        )
        setupButton(buttonData = buttonData)
    }

    private fun onLoading(
        priceEstimationData: PofFooterUiState.PriceEstimationData,
        buttonData: PofFooterUiState.SendPofButtonData
    ) {
        setupTitle(title = priceEstimationData.title, show = false)
        setupPrice(price = priceEstimationData.price, show = false, showButton = buttonData.show)
        setupIcon(
            icon = Int.ZERO,
            show = false,
            eventData = null,
            wrap = priceEstimationData.iconWrapped,
            size = priceEstimationData.iconSize
        )
        setupButton(buttonData = buttonData)
    }

    private fun onErrorReFetch(
        priceEstimationData: PofFooterUiState.PriceEstimationData,
        buttonData: PofFooterUiState.SendPofButtonData
    ) {
        setupTitle(title = priceEstimationData.title, show = true)
        setupPrice(price = priceEstimationData.price, show = true, showButton = buttonData.show)
        setupIcon(
            icon = priceEstimationData.icon,
            show = priceEstimationData.showIcon,
            eventData = priceEstimationData.iconEventData,
            wrap = priceEstimationData.iconWrapped,
            size = priceEstimationData.iconSize
        )
        setupButton(buttonData = buttonData)
    }

    private fun onReFetch(
        priceEstimationData: PofFooterUiState.PriceEstimationData,
        buttonData: PofFooterUiState.SendPofButtonData
    ) {
        setupTitle(title = priceEstimationData.title, show = false)
        setupPrice(price = priceEstimationData.price, show = false, showButton = buttonData.show)
        setupIcon(
            icon = Int.ZERO,
            show = false,
            eventData = null,
            wrap = priceEstimationData.iconWrapped,
            size = priceEstimationData.iconSize
        )
        setupButton(buttonData = buttonData)
    }

    private fun onShowingPofRequestData(
        priceEstimationData: PofFooterUiState.PriceEstimationData,
        buttonData: PofFooterUiState.SendPofButtonData
    ) {
        setupTitle(title = priceEstimationData.title, show = true)
        setupPrice(price = priceEstimationData.price, show = true, showButton = buttonData.show)
        setupIcon(
            icon = priceEstimationData.icon,
            show = priceEstimationData.showIcon,
            eventData = priceEstimationData.iconEventData,
            wrap = priceEstimationData.iconWrapped,
            size = priceEstimationData.iconSize
        )
        setupButton(buttonData = buttonData)
    }

    private fun onShowingPofResultData(
        priceEstimationData: PofFooterUiState.PriceEstimationData,
        buttonData: PofFooterUiState.SendPofButtonData
    ) {
        setupTitle(title = priceEstimationData.title, show = true)
        setupPrice(price = priceEstimationData.price, show = true, showButton = buttonData.show)
        setupIcon(
            icon = priceEstimationData.icon,
            show = priceEstimationData.showIcon,
            eventData = priceEstimationData.iconEventData,
            wrap = priceEstimationData.iconWrapped,
            size = priceEstimationData.iconSize
        )
        setupButton(buttonData = buttonData)
    }

    private fun setupTitle(title: StringRes, show: Boolean) {
        binding.tvPofFooterTitle.showIfWithBlock(show) {
            text = title.getStringWithDefaultValue(context)
        }
        binding.loaderTvPofFooterTitle.showWithCondition(!show)
    }

    private fun setupPrice(price: StringRes, show: Boolean, showButton: Boolean) {
        updateConstraints(showButton)
        binding.tvPofFooterPrice.showIfWithBlock(show) {
            text = price.getStringWithDefaultValue(context)
        }
        binding.loaderTvPofFooterPrice.showWithCondition(!show)
    }

    private fun setupIcon(
        icon: Int,
        show: Boolean,
        eventData: UiEvent?,
        wrap: Boolean,
        @DimenRes size: Int
    ) {
        binding.cardPofFooterIcon.showIfWithBlock(show) {
            radius = resources.getDimensionPixelSize(R.dimen.som_pof_footer_icon_wrapper_radius).toFloat()
            cardType = if (wrap) {
                CardUnify2.TYPE_SHADOW
            } else {
                CardUnify2.TYPE_CLEAR
            }
        }
        binding.icPofFooter.showIfWithBlock(show) {
            setImage(icon)
            updateLayoutParams<ViewGroup.LayoutParams> {
                width = resources.getDimensionPixelSize(size)
                height = resources.getDimensionPixelSize(size)
            }
            setOnClickListener { eventData?.let { listener?.onEvent(eventData) } }
        }
        binding.tvPofFooterPrice.setMargin(
            left = binding.tvPofFooterPrice.marginLeft,
            right = binding.tvPofFooterPrice.marginRight,
            top = if (wrap) {
                resources.getDimensionPixelSize(R.dimen.som_pof_footer_price_margin_top_with_wrapped_icon)
            } else Int.ZERO,
            bottom = binding.tvPofFooterPrice.marginBottom,
        )
    }

    private fun setupButton(buttonData: PofFooterUiState.SendPofButtonData) {
        binding.btnPofFooter.showIfWithBlock(buttonData.show) {
            text = buttonData.buttonText.getStringWithDefaultValue(context)
            isLoading = buttonData.isButtonLoading
            isEnabled = buttonData.isButtonEnabled
            setOnClickListener { listener?.onEvent(buttonData.eventData) }
        }
    }

    private fun updateConstraints(showButton: Boolean) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.updateTitleLayout(showButton)
        constraintSet.updatePriceLayout(showButton)
        constraintSet.applyTo(binding.root)
    }

    private fun ConstraintSet.updateTitleLayout(showButton: Boolean) {
        if (showButton) {
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.BOTTOM,
                binding.layoutPofFooterPrice.id,
                ConstraintSet.TOP
            )
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                getDimens(R.dimen.som_pof_footer_horizontal_distance)
            )
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.END,
                binding.btnPofFooter.id,
                ConstraintSet.START,
                getDimens(R.dimen.som_pof_footer_horizontal_distance)
            )
            setHorizontalBias(binding.tvPofFooterTitle.id, Float.ZERO)
            setVerticalChainStyle(binding.tvPofFooterTitle.id, ConstraintSet.CHAIN_PACKED)
            constrainedWidth(binding.tvPofFooterTitle.id, true)
        } else {
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                getDimens(R.dimen.som_pof_footer_horizontal_distance)
            )
            connect(
                binding.tvPofFooterTitle.id,
                ConstraintSet.END,
                binding.layoutPofFooterPrice.id,
                ConstraintSet.START,
                getDimens(R.dimen.som_pof_footer_horizontal_distance)
            )
            setHorizontalBias(binding.tvPofFooterTitle.id, Float.ZERO)
            constrainedWidth(binding.tvPofFooterTitle.id, true)
        }
    }

    private fun ConstraintSet.updatePriceLayout(showButton: Boolean) {
        if (showButton) {
            connect(
                binding.layoutPofFooterPrice.id,
                ConstraintSet.TOP,
                binding.tvPofFooterTitle.id,
                ConstraintSet.BOTTOM
            )
            connect(
                binding.layoutPofFooterPrice.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            connect(
                binding.layoutPofFooterPrice.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                getDimens(R.dimen.som_pof_footer_horizontal_distance)
            )
            connect(
                binding.layoutPofFooterPrice.id,
                ConstraintSet.END,
                binding.btnPofFooter.id,
                ConstraintSet.START,
                getDimens(R.dimen.som_pof_footer_horizontal_distance)
            )
            setHorizontalBias(binding.layoutPofFooterPrice.id, Float.ZERO)
            constrainedWidth(binding.layoutPofFooterPrice.id, true)
        } else {
            connect(
                binding.layoutPofFooterPrice.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            connect(
                binding.layoutPofFooterPrice.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            clear(binding.layoutPofFooterPrice.id, ConstraintSet.START)
            connect(
                binding.layoutPofFooterPrice.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
                getDimens(R.dimen.som_pof_footer_horizontal_distance)
            )
            constrainedWidth(binding.layoutPofFooterPrice.id, false)
        }
    }

    interface Listener {
        fun onEvent(event: UiEvent)
    }
}
