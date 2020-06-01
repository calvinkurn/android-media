package com.tokopedia.product.addedit.variant.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

import com.tokopedia.product.addedit.R


/**
 * Created by Toped18 on 5/30/2016.
 */
class AddEditProductVariantSizechartDialogFragment : DialogFragment() {

    private var imageMenu: Array<CharSequence>? = null
    private var mListener: OnImageEditListener? = null

    private val imageAddProductListener: DialogInterface.OnClickListener
        get() = DialogInterface.OnClickListener { _, which ->
            if (mListener != null) {
                val stringClicked = imageMenu?.getOrNull(which)
                stringClicked?.let {
                    when (it) {
                        getString(R.string.action_clear_sizechart) -> mListener!!.clickRemoveImage()
                        getString(R.string.action_edit_photo) -> mListener!!.clickChangeImagePath()
                        getString(R.string.action_edit_sizechart) -> mListener!!.clickImageEditor()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        imageMenu = arrayOf(getString(R.string.action_clear_sizechart), getString(R.string.action_edit_photo), getString(R.string.action_edit_sizechart))
        builder.setItems(imageMenu, imageAddProductListener)
        return builder.create()
    }

    companion object {
        val FRAGMENT_TAG = AddEditProductVariantSizechartDialogFragment::class.java.simpleName

        fun newInstance(): AddEditProductVariantSizechartDialogFragment {
            return AddEditProductVariantSizechartDialogFragment()
        }
    }
}
