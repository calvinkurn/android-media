package com.tokopedia.developer_options.remote_config

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

class RemoteConfigEditorDialog : BottomSheetDialogFragment() {

    private lateinit var listener: RemoteConfigListener

    fun showDialog(fragmentManager: FragmentManager, listener: RemoteConfigListener) {
        this.listener = listener

        super.show(fragmentManager, "RemoteConfigEditorDialog")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_remote_config_editor, container, false)
        val editedKeyView: AppCompatEditText = view.findViewById(R.id.edited_key)
        val editorSendButton: FrameLayout = view.findViewById(R.id.save_button)
        val editorValueInput: AppCompatEditText = view.findViewById(R.id.new_config_value)

        val selectedKey = arguments?.getString(RemoteConfigFragmentActivity.ARGS_SELECTED_KEY) ?: ""
        if (selectedKey.isEmpty()){
            editedKeyView.setText("")
            editedKeyView.isEnabled = true
        } else {
            editedKeyView.setText(selectedKey)
            editedKeyView.isEnabled = false
        }

        editorSendButton.setOnClickListener {
            listener.onEditorSaveButtonClick(
                    editedKeyView.text.toString().trim(),
                    editorValueInput.text.toString().trim())
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
