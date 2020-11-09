package com.tokopedia.top_ads_headline.view.sheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.promotional_message_bottom_sheet_layout.*

class PromotionalMessageBottomSheet : BottomSheetUnify() {
    private var storeName: String = ""

    init {
        showCloseIcon = false
        showKnob = true
    }

    companion object {
        fun newInstance(storeName: String, onDismissListener: (String) -> Unit): PromotionalMessageBottomSheet {
            return PromotionalMessageBottomSheet().apply {
                this.storeName = storeName
                setOnDismissListener {
                    onDismissListener.invoke(promotionalMessageInputText.getEditableValue().toString())
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = View.inflate(context, R.layout.promotional_message_bottom_sheet_layout, null)
        setChild(contentView)
        setTitle(getString(R.string.topads_headline_promotional_message))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpChipsText()
        setUpTextField()
        saveBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpTextField() {
        promotionalMessageInputText.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    when {
                        it.isEmpty() -> {
                            promotionalMessageInputText.setError(true)
                            promotionalMessageInputText.setMessage(getString(R.string.topads_headline_promotional_message_empty_error))
                        }
                        it.length in 1..19 -> {
                            promotionalMessageInputText.setError(true)
                            promotionalMessageInputText.setMessage(getString(R.string.topads_headline_promotional_message_length_error))
                        }
                        else -> {
                            promotionalMessageInputText.setError(false)
                            promotionalMessageInputText.setMessage(getString(R.string.topads_headline_promotional_message_success))
                        }
                    }
                }
            }

        })
    }

    private fun setUpChipsText() {
        chip2.text = getString(R.string.topads_headline_recommended_template_2, storeName)
        chip3.text = getString(R.string.topads_headline_recommended_template_3, storeName)
        chip4.text = getString(R.string.topads_headline_recommended_template_4, storeName)
        chip1.setOnClickListener { openTemplateTipsBottomSheet() }
        chip2.setOnClickListener { openTemplateTipsBottomSheet() }
        chip3.setOnClickListener { openTemplateTipsBottomSheet() }
        chip4.setOnClickListener { openTemplateTipsBottomSheet() }
    }

    private fun openTemplateTipsBottomSheet() {
        val tipsList: ArrayList<TipsUiModel> = ArrayList()
        tipsList.apply {
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_1, R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_2, R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_3, R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_4, R.drawable.topads_create_ic_checklist))
            add(TipsUiRowModel(R.string.topads_headline_promotional_message_5, R.drawable.topads_create_ic_checklist))
        }
        val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, getString(R.string.topads_headline_promotional_message_header), tipsList) }
        tipsListSheet?.show(parentFragmentManager, "")
    }
}