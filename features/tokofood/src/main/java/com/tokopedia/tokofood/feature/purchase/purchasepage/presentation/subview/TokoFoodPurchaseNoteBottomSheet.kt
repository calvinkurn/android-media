package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.LayoutBottomSheetPurchaseNotesBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class TokoFoodPurchaseNoteBottomSheet(private val currentNote: String? = null,
                                      val listener: Listener? = null) : BottomSheetUnify() {

    private var viewBinding: LayoutBottomSheetPurchaseNotesBinding? = null

    interface Listener {
        fun onSaveNotesClicked(notes: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PurchaseNotesSheetStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initializeView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            renderNotes(it)
        }
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun initializeView(): LayoutBottomSheetPurchaseNotesBinding {
        val viewBinding = LayoutBottomSheetPurchaseNotesBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding
        initializeBottomSheet(viewBinding)
        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomSheetPurchaseNotesBinding) {
        setTitle(context?.getString(com.tokopedia.tokofood.R.string.text_purchase_order_notes).orEmpty())
        showCloseIcon = true
        showHeader = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setChild(viewBinding.root)
    }

    private fun renderNotes(viewBinding: LayoutBottomSheetPurchaseNotesBinding) {
        with(viewBinding) {
            textFieldNote.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(notes: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (currentNote?.isBlank() == true) {
                        buttonSaveNotes.isEnabled = (notes?.length ?: Int.ZERO) > Int.ZERO
                    } else {
                        buttonSaveNotes.isEnabled = true
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            textFieldNote.editText.setText(currentNote)
            textFieldNote.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldNote.editText.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    textFieldNote.editText.context?.let {
                        KeyboardHandler.DropKeyboard(it, v)
                    }
                    textFieldNote.editText.clearFocus()
                    true
                } else false
            }

            buttonSaveNotes.setOnClickListener {
                listener?.onSaveNotesClicked(textFieldNote.editText.text.toString())
                dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "TokoFoodPurchaseNoteBottomSheet"
    }
}
