package com.tokopedia.bmsm_widget.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.bmsm_widget.databinding.ViewGwpGiftWidgetBinding
import com.tokopedia.bmsm_widget.presentation.adapter.GiftWidgetAdapter
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible


/**
 * Created by @ilhamsuaib on 06/12/23.
 */

class GwpGiftWidget : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ViewGwpGiftWidgetBinding? = null
    private val adapter: GiftWidgetAdapter by lazy { GiftWidgetAdapter() }
    private val viewPool by lazy { RecyclerView.RecycledViewPool() }

    init {
        binding = ViewGwpGiftWidgetBinding.inflate(LayoutInflater.from(context), this, true)
        initRecyclerView()
    }

    fun setRibbonText(text: String) {
        if (text.isNotBlank()) {
            binding?.giftRibbonView?.setText(text)
        }
        binding?.giftRibbonView?.isVisible = text.isNotBlank()
    }

    fun setupCtaClickListener(text: String, onClicked: () -> Unit) {
        binding?.run {
            giftWidgetCta.visible()
            giftWidgetCta.text = text
            giftWidgetCta.setOnClickListener {
                onClicked()
            }
        }
    }

    fun removeCta() {
        binding?.run {
            giftWidgetCta.gone()
            giftWidgetCta.text = String.EMPTY
            giftWidgetCta.setOnClickListener(null)
        }
    }

    fun updateData(giftList: List<ProductGiftUiModel>) {
        adapter.submitList(giftList)

        showSuccessState()
    }

    fun showLoadingState() {
        binding?.run {
            giftWidgetCta.gone()
            giftRibbonView.gone()
            rvGiftList.gone()
            giftErrorState.gone()
            giftLoadingState.containerLoadingState.visible()
        }
    }

    fun showErrorState(onReloadClicked: (() -> Unit)? = null) {
        binding?.run {
            giftWidgetCta.gone()
            giftRibbonView.gone()
            rvGiftList.gone()
            giftLoadingState.containerLoadingState.gone()

            giftErrorState.visible()
            tvReloadGift.setOnClickListener {
                onReloadClicked?.invoke()
            }
        }
    }

    private fun dismissLoadingState() {
        binding?.giftLoadingState?.containerLoadingState?.gone()
    }

    private fun showSuccessState() {
        binding?.run {
            giftWidgetCta.visible()
            giftRibbonView.visible()
            rvGiftList.visible()
            giftErrorState.gone()
            giftLoadingState.containerLoadingState.gone()

            dismissLoadingState()
        }
    }

    private fun initRecyclerView() {
        binding?.rvGiftList?.run {
            layoutManager = LinearLayoutManager(
                this@GwpGiftWidget.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = this@GwpGiftWidget.adapter
            setRecycledViewPool(viewPool)
        }
    }
}
