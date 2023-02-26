package com.tokopedia.usercomponents.loginwidget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by dhaba
 */
class LoginWidget : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val view = View.inflate(context, com.tokopedia.usercomponents.R.layout.layout_login_widget, this)
    }
}
