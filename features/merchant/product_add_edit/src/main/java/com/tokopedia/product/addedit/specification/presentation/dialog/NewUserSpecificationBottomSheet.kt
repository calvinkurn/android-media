package com.tokopedia.product.addedit.specification.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_NEW_USER_SPECIFICATION
import com.tokopedia.product.addedit.common.customview.TabletAdaptiveBottomSheet

class NewUserSpecificationBottomSheet: TabletAdaptiveBottomSheet() {

    companion object {
        const val TAG = "Tag New User Specification Bottom Sheet"
    }

    private var ivNewUser: AppCompatImageView? = null

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
        setupImageView()
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupImageView() {
        ivNewUser?.loadImage(PHOTO_NEW_USER_SPECIFICATION)
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_specification_new_user_bottom_sheet_content, null)
        ivNewUser = contentView?.findViewById(R.id.ivNewUser)
        setChild(contentView)
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }
}