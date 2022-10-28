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

class OrderNoteBottomSheet : BottomSheetUnify() {

    private var selectedProductId: String = ""
    private var orderNote: String = ""
    private var clickListener: OnSaveNoteButtonClickListener? = null

    interface OnSaveNoteButtonClickListener {
        fun onSaveNoteButtonClicked(productId: String, orderNote: String)
    }

    companion object {

        private const val TAG = "OrderNoteBottomSheet"
        const val MIN_LINES = 3

        @JvmStatic
        fun createInstance(): OrderNoteBottomSheet {
            return OrderNoteBottomSheet()
        }
    }

    private var binding: BottomsheetOrderNoteLayoutBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetOrderNoteLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        val title = context?.getString(R.string.text_order_bottomsheet_title) ?: ""
        setTitle(title)
        isKeyboardOverlap = false
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
        binding?.run {
            this@OrderNoteBottomSheet.setShowListener {
                this.notesInput.editText.setText(orderNote)
            }
            this.notesInput.minLine = MIN_LINES
            this.notesInput.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    binding.saveNotesButton.isEnabled = s.isNotBlank() || orderNote.isNotBlank()
                }
            })
            this.saveNotesButton.isEnabled = binding.notesInput.editText.text.isNotBlank()
            this.saveNotesButton.setOnClickListener {
                val orderNote = binding.notesInput.editText.text.toString()
                clickListener?.onSaveNoteButtonClicked(selectedProductId, orderNote)
            }
        }
    }

    fun setSelectedProductId(productId: String) {
        this.selectedProductId = productId
    }

    fun setOrderNote(orderNote: String) {
        this.orderNote = orderNote
    }

    fun setClickListener(clickListener: OnSaveNoteButtonClickListener) {
        this.clickListener = clickListener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }
}