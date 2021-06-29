package com.tokopedia.play.view.custom.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.R

/**
 * Created by jegul on 29/06/21
 */
class InteractiveWinningDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_interactive_winning_dialog, container)
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    override fun onResume() {
        super.onResume()
        val context = this.context ?: return
        val params = dialog?.window?.attributes ?: return
        params.width = context.resources.getDimensionPixelSize(R.dimen.play_interactive_winning_dialog_width)
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params
    }

    companion object {

        private const val TAG = "Interactive Winning Dialog"

        fun get(manager: FragmentManager): InteractiveWinningDialogFragment? {
            return manager.findFragmentByTag(TAG) as? InteractiveWinningDialogFragment
        }

        fun newInstance(): InteractiveWinningDialogFragment {
            return InteractiveWinningDialogFragment()
        }
    }
}