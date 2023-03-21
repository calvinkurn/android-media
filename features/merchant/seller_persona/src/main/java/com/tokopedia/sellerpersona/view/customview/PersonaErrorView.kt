package com.tokopedia.sellerpersona.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.sellerpersona.R
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by @ilhamsuaib on 08/02/23.
 */

class PersonaErrorView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    companion object {
        private const val IMG_ERROR_WIDTH = 184
        private const val IMG_ERROR_HEIGHT = 144
        private const val BTN_ERROR_TOP_MARGIN = 16
    }

    private var onActionClickCallback: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.view_persona_error, this)

        val globalError = findViewById<GlobalError>(R.id.globalErrorViewPersona)
        with(globalError) {
            val btnTopMargin = context.dpToPx(BTN_ERROR_TOP_MARGIN).toInt()
            setActionClickListener {
                onActionClickCallback?.invoke()
            }
            errorTitle.text = context.getString(
                R.string.sp_common_global_error_title
            )
            val imgWidth = context.dpToPx(IMG_ERROR_WIDTH)
            val imgHeight = context.dpToPx(IMG_ERROR_HEIGHT)
            val imgViewLayoutParams = errorIllustration.layoutParams.apply {
                width = imgWidth.toInt()
                height = imgHeight.toInt()
            }

            (errorAction as? UnifyButton)?.buttonSize = UnifyButton.Size.SMALL
            errorAction.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            errorAction.text = context.getString(R.string.sp_reload)
            errorAction.setMargin(Int.ZERO, btnTopMargin, Int.ZERO, Int.ZERO)
            errorAction.requestLayout()
            errorIllustration.layoutParams = imgViewLayoutParams
            errorDescription.gone()
        }
    }

    fun setOnActionClicked(callback: () -> Unit) {
        this.onActionClickCallback = callback
    }
}