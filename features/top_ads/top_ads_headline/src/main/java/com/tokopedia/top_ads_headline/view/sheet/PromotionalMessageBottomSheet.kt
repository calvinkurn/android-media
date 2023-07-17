package com.tokopedia.top_ads_headline.view.sheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifyprinciples.Typography

class PromotionalMessageBottomSheet : BottomSheetUnify() {

    private var promotionalMessageInputText: TextFieldUnify? = null
    private var chip1: Typography? = null
    private var chip2: Typography? = null
    private var chip3: Typography? = null
    private var chip4: Typography? = null
    private var tooltipBtn: FloatingButtonUnify? = null
    private var saveBtn: UnifyButton? = null

    private var storeName: String = ""
    private val promoMsgRange = PROMO_INITIAL_RANGE..PROMO_FINAL_RANGE
    private var promotionalMessage = ""

    init {
        showCloseIcon = false
        showKnob = true
        overlayClickDismiss = false
    }

    companion object {
        private const val PROMO_INITIAL_RANGE = 1
        private const val PROMO_FINAL_RANGE = 19
        fun newInstance(
            storeName: String, promotionalMessage: String, onDismissListener: (String) -> Unit,
        ): PromotionalMessageBottomSheet {
            return PromotionalMessageBottomSheet().apply {
                this.storeName = storeName
                this.promotionalMessage = promotionalMessage
                setOnDismissListener {
                    onDismissListener.invoke(promotionalMessageInputText?.getEditableValue()
                        .toString())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val contentView =
            View.inflate(context, R.layout.promotional_message_bottom_sheet_layout, null)
        setChild(contentView)
        setTitle(getString(R.string.topads_headline_promotional_message))
        initView(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(view: View) {
        promotionalMessageInputText = view.findViewById(R.id.promotionalMessageInputText)
        chip1 = view.findViewById(R.id.chip1)
        chip2 = view.findViewById(R.id.chip2)
        chip3 = view.findViewById(R.id.chip3)
        chip4 = view.findViewById(R.id.chip4)
        tooltipBtn = view.findViewById(R.id.tooltipBtn)
        saveBtn = view.findViewById(R.id.saveBtn)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpChipsText()
        setUpToolTip()
        setUpTextField()
        saveBtn?.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpToolTip() {
        val tooltipView =
            layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null)
                .apply {
                    val tvToolTipText = this.findViewById<Typography>(com.tokopedia.topads.common.R.id.tooltip_text)
                    tvToolTipText?.text =
                        getString(R.string.topads_headline_promotional_tooltip_text)
                    val imgTooltipIcon = this.findViewById<ImageUnify>(com.tokopedia.topads.common.R.id.tooltip_icon)
                    imgTooltipIcon?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
                }
        tooltipBtn?.addItem(tooltipView)
        tooltipBtn?.setOnClickListener {
            openTemplateTipsBottomSheet()
        }
    }

    private fun setUpTextField() {
        promotionalMessageInputText?.textFieldInput?.setText(MethodChecker.fromHtml(
            promotionalMessage))
        promotionalMessageInputText?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    when {
                        it.isEmpty() -> {
                            promotionalMessageInputText?.setError(true)
                            promotionalMessageInputText?.setMessage(getString(R.string.topads_headline_promotional_message_empty_error))
                            setBtnEnabled(false)
                        }
                        it.length in promoMsgRange -> {
                            promotionalMessageInputText?.setError(true)
                            promotionalMessageInputText?.setMessage(getString(R.string.topads_headline_promotional_message_length_error))
                            setBtnEnabled(false)
                        }
                        else -> {
                            promotionalMessageInputText?.setError(false)
                            promotionalMessageInputText?.setMessage(getString(R.string.topads_headline_promotional_message_success))
                            setBtnEnabled(true)
                        }
                    }
                }
            }

        })
    }

    private fun setBtnEnabled(isEnabled: Boolean) {
        saveBtn?.isEnabled = isEnabled
    }

    private fun setUpChipsText() {
        chip2?.text = getString(R.string.topads_headline_recommended_template_2, storeName)
        chip3?.text = getString(R.string.topads_headline_recommended_template_3, storeName)
        chip4?.text = getString(R.string.topads_headline_recommended_template_4, storeName)
        chip1?.setOnClickListener {
            promotionalMessageInputText?.textFieldInput?.setText(chip1?.text)
        }
        chip2?.setOnClickListener {
            promotionalMessageInputText?.textFieldInput?.setText(chip2?.text)
        }
        chip3?.setOnClickListener {
            promotionalMessageInputText?.textFieldInput?.setText(chip3?.text)
        }
        chip4?.setOnClickListener {
            promotionalMessageInputText?.textFieldInput?.setText(chip4?.text)
        }
    }

    private fun openTemplateTipsBottomSheet() {
        val tipsList: ArrayList<TipsUiModel> = ArrayList()
        tipsList.apply {
            add(TipsUiHeaderModel(R.string.topads_headline_promotional_message_header))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_1,
                com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_2,
                com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_3,
                com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_4,
                com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_5,
                com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
        }
        val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList) }
        tipsListSheet?.show(childFragmentManager, "")
    }
}
