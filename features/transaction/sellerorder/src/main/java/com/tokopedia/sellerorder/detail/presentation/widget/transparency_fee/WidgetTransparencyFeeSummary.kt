package com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.databinding.WidgetTransparencyFeeSummaryBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSummaryUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper

class WidgetTransparencyFeeSummary @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr),
    DetailTransparencyFeeAdapterFactoryImpl.ActionListener {

    companion object {
        private const val STATE_FINISHED = "finished"
    }

    private val binding = inflateContent()
    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(this)

    private var listener: Listener? = null

    override fun onErrorActionClicked() {
        // noop
    }

    override fun onTransparencyInfoIconClicked(title: String, desc: String) {
        listener?.onTransparencyInfoIconClicked(title, desc)
    }

    fun updateUI(element: TransparencyFeeSummaryUiModel) {
        setupValue(element.value)
        setAttributes(element.attributes)
        setupNote(element.note)
        setupState(element.state)
        show()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun inflateContent(): WidgetTransparencyFeeSummaryBinding {
        return WidgetTransparencyFeeSummaryBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).apply {
            resetColor()
            setupNoteClickListener()
        }
    }

    private fun setupValue(value: String) {
        binding.tvSummaryIncomeDetailValue.text = value
    }

    private fun setupNote(note: String) {
        binding.tvSummaryIncomeDetailNote.run {
            if (note.isNotBlank()) {
                show()
                text = createClickableNote(note)
            } else {
                hide()
            }
        }
    }

    private fun setupState(state: String) {
        when (state) {
            STATE_FINISHED -> binding.containerTransparencyFeeSummary.setContainerColor(ContainerUnify.GREEN)
            else -> binding.resetColor()
        }
    }

    private fun setAttributes(attributes: List<BaseTransparencyFeeAttributes>) {
        setAttributesData(attributes)
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        binding.cgSummaryIncomeDetailAttributes.removeAllViews()
        attributes.map { attribute ->
            val attributeView = createAttributeView(attribute)
            val attributeViewHolder = createAttributeViewHolder(attribute, attributeView)
            binding.cgSummaryIncomeDetailAttributes.addView(attributeView)
            attributeViewHolder.bind(attribute)
        }
    }

    private fun createAttributeView(attribute: BaseTransparencyFeeAttributes): View {
        return LayoutInflater
            .from(context)
            .inflate(attribute.type(typeFactory), null, false)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createAttributeViewHolder(
        attribute: BaseTransparencyFeeAttributes,
        attributeView: View
    ): BaseWidgetTransparencyFeeAttribute<BaseTransparencyFeeAttributes> {
        return typeFactory.createWidget(attributeView, attribute.type(typeFactory)) as BaseWidgetTransparencyFeeAttribute<BaseTransparencyFeeAttributes>
    }

    private fun WidgetTransparencyFeeSummaryBinding.resetColor() {
        containerTransparencyFeeSummary.setCustomContainerColor(android.R.color.transparent)
    }

    private fun WidgetTransparencyFeeSummaryBinding.setupNoteClickListener() {
        tvSummaryIncomeDetailNote.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun createClickableNote(note: String): CharSequence {
        return HtmlLinkHelper(
            context, note
        ).apply {
            urlList.forEach { it.onClick = { listener?.onClickNoteLink(it.linkUrl) } }
        }.spannedString ?: String.EMPTY
    }

    interface Listener {
        fun onTransparencyInfoIconClicked(title: String, desc: String)
        fun onClickNoteLink(url: String)
    }
}
