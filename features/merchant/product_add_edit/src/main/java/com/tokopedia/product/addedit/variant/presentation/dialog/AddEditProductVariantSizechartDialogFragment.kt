package com.tokopedia.product.addedit.variant.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

import com.tokopedia.product.addedit.R

class AddEditProductVariantSizechartDialogFragment : DialogFragment() {

    private var imageMenu: Array<CharSequence>? = null
    private var mListener: OnImageEditListener? = null

    private val imageAddProductListener: DialogInterface.OnClickListener
        get() = DialogInterface.OnClickListener { _, which ->
            mListener?.let { listener ->
                val stringClicked = imageMenu?.getOrNull(which)
                stringClicked?.let {
                    when (it) {
                        getString(com.tokopedia.product.addedit.R.string.action_clear_sizechart) -> listener.clickRemoveImage()
                        getString(com.tokopedia.product.addedit.R.string.action_edit_photo) -> listener.clickChangeImagePath()
                        getString(com.tokopedia.product.addedit.R.string.action_edit_sizechart) -> listener.clickImageEditor()
                    }
                }
            }
        }

    interface OnImageEditListener {
        fun clickChangeImagePath()

        fun clickImageEditor()

        fun clickRemoveImage()
    }

    fun setOnImageEditListener(listener: OnImageEditListener) {
        this.mListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        imageMenu = arrayOf(getString(R.string.action_clear_sizechart), getString(R.string.action_edit_photo), getString(R.string.action_edit_sizechart))
        builder.setItems(imageMenu, imageAddProductListener)
        return builder.create()
    }

    companion object {
        val FRAGMENT_TAG: String = AddEditProductVariantSizechartDialogFragment::class.java.simpleName

        fun newInstance(): AddEditProductVariantSizechartDialogFragment {
            return AddEditProductVariantSizechartDialogFragment()
        }
    }
}
