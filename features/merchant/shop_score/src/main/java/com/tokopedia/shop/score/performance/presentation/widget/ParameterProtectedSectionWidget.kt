package com.tokopedia.shop.score.performance.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.shop.score.databinding.ItemParamaterProtectedSectionWidgetBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemProtectedParameterAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ProtectedParameterListener
import com.tokopedia.shop.score.performance.presentation.model.BaseProtectedParameterSectionUiModel

class ParameterProtectedSectionWidget : ConstraintLayout {

    val binding: ItemParamaterProtectedSectionWidgetBinding?
        get() = _binding

    private var _binding: ItemParamaterProtectedSectionWidgetBinding? = null

    private var protectedParameterListener: ProtectedParameterListener? = null

    private var itemProtectedParameterAdapter: ItemProtectedParameterAdapter? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        _binding = ItemParamaterProtectedSectionWidgetBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun setData(
        element: BaseProtectedParameterSectionUiModel?,
        protectedParameterListener: ProtectedParameterListener,
        setCardItemProtectedBackground: () -> Unit
    ) {
        this.protectedParameterListener = protectedParameterListener
        setupViews(element, setCardItemProtectedBackground)
    }

    private fun setupViews(
        element: BaseProtectedParameterSectionUiModel?,
        setCardItemProtectedBackground: () -> Unit
    ) {
        binding?.run {
            setCardItemProtectedBackground()
            tvTitleParameterRelief.text = element?.titleParameterRelief.orEmpty()
            tvDescParameterRelief.text = element?.descParameterRelief.orEmpty()
            cardDescParameterRelief.setOnClickListener {
                protectedParameterListener?.onProtectedParameterChevronClicked(
                    element?.descParameterReliefBottomSheet.orEmpty()
                )
            }
            icDescParameterRelief.setOnClickListener {
                protectedParameterListener?.onProtectedParameterChevronClicked(
                    element?.descParameterReliefBottomSheet.orEmpty()
                )
            }
        }
        setAdapterProtectedParameter(element)
    }

    private fun setAdapterProtectedParameter(data: BaseProtectedParameterSectionUiModel?) {
        binding?.run {
            itemProtectedParameterAdapter = ItemProtectedParameterAdapter()
            rvParameterReliefDetail.run {
                layoutManager = LinearLayoutManager(context)
                adapter = itemProtectedParameterAdapter
                isNestedScrollingEnabled = false
            }
            itemProtectedParameterAdapter?.setProtectedParameterList(data?.itemProtectedParameterList)
        }
    }
}