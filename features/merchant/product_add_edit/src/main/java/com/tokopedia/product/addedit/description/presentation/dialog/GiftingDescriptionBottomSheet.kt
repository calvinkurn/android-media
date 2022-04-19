package com.tokopedia.product.addedit.description.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class GiftingDescriptionBottomSheet: BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Gifting Description Bottom Sheet"
    }

    private var onCopyTemplateButtonListener: () -> Unit = {}

    init {
        isKeyboardOverlap = false
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(getString(R.string.title_gifting_description_bottom_sheet))
        setupCopyTemplateButton()
    }

    fun setOnCopyTemplateButtonListener(listener: () -> Unit) {
        onCopyTemplateButtonListener = listener
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun initChildLayout() {
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_gifting_description_bottom_sheet_content, null)
        setChild(contentView)
    }

    private fun setupCopyTemplateButton() {
        requireView().findViewById<UnifyButton>(R.id.button_copy_template).setOnClickListener {
            onCopyTemplateButtonListener()
            dismiss()
        }
    }
}