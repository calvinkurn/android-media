package com.tokopedia.sellerorder.detail.presentation.widget

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.databinding.WidgetTransparencyFeeSummaryBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper

class TransparencyFeeSummary @JvmOverloads constructor(
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
    private val attributesAdapter = BaseAdapter(typeFactory)

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
        if (attributes.isNotEmpty()) {
            setupRecyclerView()
            setAttributesData(attributes)
        } else {
            hideAttributes()
        }
    }

    private fun setupRecyclerView() {
        binding.rvSummaryIncomeDetailAttributes.run {
            show()
            layoutManager = FlexboxLayoutManager(context).apply {
                alignItems = AlignItems.FLEX_START
            }
            adapter = attributesAdapter
        }
    }

    private fun hideAttributes() {
        binding.rvSummaryIncomeDetailAttributes.hide()
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        attributesAdapter.setElement(attributes)
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
