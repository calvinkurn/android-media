package com.tokopedia.shop.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.shop.common.constant.SellerHomePermissionGroup
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class AdminNoPermissionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        setupView()
    }

    private var titleTextView: Typography? = null
    private var descTextView: Typography? = null
    private var actionButton: UnifyButton? = null

    fun setOnActionButtonClickedListener(action: () -> Unit) {
        actionButton?.setOnClickListener {
            action()
        }
    }

    fun setPermissionType(@SellerHomePermissionGroup permissionGroup: String) {
        titleTextView?.text = context?.getString(R.string.admin_no_permission_title, permissionGroup)
        descTextView?.text = context?.getString(R.string.admin_no_permission_desc, permissionGroup)
    }

    private fun setupView() {
        View.inflate(context, R.layout.admin_no_permission_view, this)

        titleTextView = findViewById(R.id.tv_admin_no_permission_title)
        descTextView = findViewById(R.id.tv_admin_no_permission_desc)
        actionButton = findViewById(R.id.btn_admin_no_permission)
    }

}