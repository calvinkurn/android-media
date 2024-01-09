package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import android.text.TextWatcher
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodItemOrderNoteLayoutBinding

class OrderNoteInputViewHolder(
    private val binding: TokofoodItemOrderNoteLayoutBinding,
    private var textChangeListener: OnNoteTextChangeListener?
) : RecyclerView.ViewHolder(binding.root) {

    interface OnNoteTextChangeListener {
        fun onNoteTextChanged(orderNote: String, dataSetPosition: Int)
    }

    private var context: Context? = null

    private var textWatcher: TextWatcher? = null

    init {
        context = binding.root.context
        binding.catatanInput
    }

    fun bindData(orderNote: String, dataSetPosition: Int) {
        binding.catatanInput.editText.setText(orderNote)
        textWatcher = binding.catatanInput.editText.doAfterTextChanged {
            it?.run {
                textChangeListener?.onNoteTextChanged(
                    orderNote = this.toString(),
                    dataSetPosition = dataSetPosition
                )
            }
        }
    }

    fun removeListeners() {
        textChangeListener = null
        textWatcher?.let { watcher ->
            binding.catatanInput.editText.removeTextChangedListener(watcher)
        }
    }
}
