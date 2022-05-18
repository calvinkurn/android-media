package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.BottomsheetOrderNoteLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class OrderNoteBottomSheet(private val clickListener: OnSaveNoteButtonClickListener) : BottomSheetUnify() {

    interface OnSaveNoteButtonClickListener {
        fun onSaveNoteButtonClicked(orderNote: String)
    }

    companion object {

        const val MIN_LINES = 3

        @JvmStatic
        fun createInstance(clickListener: OnSaveNoteButtonClickListener): OrderNoteBottomSheet {
            return OrderNoteBottomSheet(clickListener)
        }
    }

    private var binding: BottomsheetOrderNoteLayoutBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetOrderNoteLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        val title = context?.getString(R.string.text_order_bottomsheet_title) ?: ""
        setTitle(title)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupView(binding: BottomsheetOrderNoteLayoutBinding?) {
        binding?.notesInput?.minLine = MIN_LINES
        binding?.run {
            binding.notesInput.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    binding.saveNotesButton.isEnabled = s.isNotBlank()
                }
            })
            binding.saveNotesButton.isEnabled = binding.notesInput.editText.text.isNotBlank()
            binding.saveNotesButton.setOnClickListener {
                val orderNote = binding.notesInput.editText.text.toString()
                clickListener.onSaveNoteButtonClicked(orderNote)
            }
        }
    }

    fun renderOrderNote(orderNote: String) {

        binding?.notesInput?.editText?.setText(orderNote)
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}