package com.tokopedia.tokofood.purchase.purchasepage.view.subview

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.tokofood.databinding.LayoutBottomSheetPurchaseNotesBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class TokoFoodPurchaseNoteBottomSheet(private val currentNote: String, val listener: Listener) : BottomSheetUnify() {

    private var viewBinding: LayoutBottomSheetPurchaseNotesBinding? = null

    interface Listener {
        fun onSaveNotesClicked(notes: String)
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

    private fun initializeView(): LayoutBottomSheetPurchaseNotesBinding {
        val viewBinding = LayoutBottomSheetPurchaseNotesBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding
        initializeBottomSheet(viewBinding)
        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomSheetPurchaseNotesBinding) {
        setTitle("Catatan pesanan")
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
                    buttonSaveNotes.isEnabled = notes?.length ?: 0 > 0
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            textFieldNote.editText.setText(currentNote)

            buttonSaveNotes.setOnClickListener {
                listener.onSaveNotesClicked(textFieldNote.editText.text.toString())
            }
        }
    }
}