package com.tokopedia.product.addedit.specification.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_NEW_USER_SPECIFICATION
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.add_edit_product_specification_new_user_bottom_sheet_content.*

class NewUserSpecificationBottomSheet: BottomSheetUnify() {

    companion object {
        const val TAG = "Tag New User Specification Bottom Sheet"
    }

    init {
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
        setupImageView()
    }

    private fun setupImageView() {
        ivNewUser.loadImage(PHOTO_NEW_USER_SPECIFICATION)
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_specification_new_user_bottom_sheet_content, null)
        setChild(contentView)
    }
}