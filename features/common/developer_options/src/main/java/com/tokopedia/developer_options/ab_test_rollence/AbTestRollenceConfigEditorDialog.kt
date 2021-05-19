package com.tokopedia.developer_options.ab_test_rollence

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.tokopedia.developer_options.R

class AbTestRollenceConfigEditorDialog : BottomSheetDialogFragment() {

    private lateinit var listener: AbTestRollenceConfigListener

    fun showDialog(fragmentManager: FragmentManager, listener: AbTestRollenceConfigListener) {
        this.listener = listener

        super.show(fragmentManager, "AbTestRollenceEditorDialog")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_remote_config_editor, container, false)
        val editedKeyView: AppCompatTextView = view.findViewById(R.id.edited_key)
        val editorSaveButton: FrameLayout = view.findViewById(R.id.save_button)
        val editorDeleteKeyButton: FrameLayout = view.findViewById(R.id.delete_key_button)
        val editorValueInput: AppCompatEditText = view.findViewById(R.id.new_config_value)

        val selectedKey = arguments?.getString(AbTestRollenceConfigFragmentActivity.ARGS_SELECTED_KEY) ?: "-"
        val selectedKeyValue = arguments?.getString(AbTestRollenceConfigFragmentActivity.ARGS_SELECTED_KEY_VALUE) ?: "-"
        editedKeyView.text = selectedKey
        editorValueInput.setText(selectedKeyValue)
        editorSaveButton.setOnClickListener {
            listener.onEditorSaveButtonClick(selectedKey, editorValueInput.text.toString().trim())
            dismiss()
        }
        editorDeleteKeyButton.setOnClickListener {
            listener.onEditorDeleteKeyButtonClick(selectedKey)
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet: FrameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return bottomSheetDialog
    }
}
