package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.unifyprinciples.Typography

class NoPermissionsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    lateinit var btnPermission: Typography

    fun getLayout() = R.layout.imagepicker_insta_no_permissions_view

    init {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    fun initViews() {
        btnPermission = findViewById(R.id.btn_perm)
    }
}