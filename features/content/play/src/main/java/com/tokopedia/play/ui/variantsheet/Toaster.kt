package com.tokopedia.play.ui.variantsheet

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.R
import com.tokopedia.unifycomponents.findSuitableParent
import com.tokopedia.unifycomponents.toPx

object Toaster {
    lateinit var snackBar: Snackbar
    lateinit var onCTAClick: View.OnClickListener
    lateinit var ctaText: String
    private val WITHOUT_CTA = 0
    private val WITH_CTA = 1

    /**
     * Toaster normal style
     */
    const val TYPE_NORMAL = 0

    /**
     * Toaster error style
     */
    const val TYPE_ERROR = 1
    const val LENGTH_SHORT = 0
    const val LENGTH_LONG = 1
    var toasterLength = Snackbar.LENGTH_SHORT

    var toasterCustomBottomHeight: Int = 0
    var toastorCustomCtaWidth: Int = 0

    @Deprecated(message = "Please use make() method instead")
    fun showNormal(view: View, text: String, duration: Int) {
        toasterLength = duration
        initToaster(view, text)
    }

    @Deprecated(message = "Please use make() method instead")
    fun showNormalWithAction(view: View, text: String, duration: Int, actionText: String, clickListener: View.OnClickListener) {
        toasterLength = duration
        onCTAClick = clickListener
        ctaText = actionText
        initToaster(view, text, TYPE_NORMAL, WITH_CTA)
    }

    @Deprecated(message = "Please use make() method instead")
    fun showError(view: View, text: String, duration: Int) {
        toasterLength = duration
        initToaster(view, text, TYPE_ERROR)
    }

    @Deprecated(message = "Please use make() method instead")
    fun showErrorWithAction(view: View, text: String, duration: Int, actionText: String, clickListener: View.OnClickListener) {
        toasterLength = duration
        onCTAClick = clickListener
        ctaText = actionText
        initToaster(view, text, TYPE_ERROR, WITH_CTA)
    }

    /**
     * Make a Toaster to display a message. Toaster will try and find a parent view to hold Toaster's view
     * from the value given to view.
     * @param view for finding suitable parent to hold Toaster's view.
     * @param text The text to show
     * @param duration How long to display the message. Either Snackbar.LENGTH_SHORT, Snackbar.LENGTH_LONG or Snackbar.LENGTH_INDEFINITE
     * @param type What style to apply for Toaster. Either [TYPE_NORMAL] or [TYPE_ERROR]
     * @param actionText Text to display for the action
     * @param clickListener callback to be invoked when the action is clicked
     */
    fun make(view: View, text: String, duration: Int = LENGTH_SHORT, type: Int = TYPE_NORMAL, actionText: String = "", clickListener: View.OnClickListener = View.OnClickListener {}) {
        toasterLength = duration
        val cta: Int = if (actionText.isNotEmpty()) WITH_CTA else WITHOUT_CTA
        if (cta == WITH_CTA) {
            onCTAClick = clickListener
            ctaText = actionText
        }
        initToaster(view, text, type, cta)
    }

    private fun initToaster(view: View, text: String, type: Int = TYPE_NORMAL, cta: Int = WITHOUT_CTA) {
        snackBar = Snackbar.make(view.findSuitableParent() as View, "", toasterLength)

        val viewLayout = View.inflate(view.context, R.layout.toaster_custom, null)

        val textView = viewLayout.findViewById<TextView>(R.id.snackbar_txt)
        textView.text = text

        val constraintLayoutToaster = viewLayout.findViewById<View>(R.id.constraintLayoutToaster)

        if (type == TYPE_NORMAL) {
            constraintLayoutToaster.setBackgroundResource(R.drawable.toaster_bg_normal)
        } else {
            constraintLayoutToaster.setBackgroundResource(R.drawable.toaster_bg_error)
        }

        val actionTextButton = viewLayout.findViewById<Button>(R.id.snackbar_btn)
        if(toastorCustomCtaWidth > 0) {
            actionTextButton.maxWidth = toastorCustomCtaWidth
            toastorCustomCtaWidth = 0
        }

        if (cta == WITH_CTA) {
            actionTextButton.text = ctaText
            actionTextButton.setOnClickListener{
                snackBar.dismiss()
                onCTAClick.onClick(it)
            }
            actionTextButton.visibility = View.VISIBLE
        } else {
            actionTextButton.setOnClickListener(null)
            actionTextButton.visibility = View.GONE
        }

        val layout = snackBar.view as Snackbar.SnackbarLayout
        layout.isClickable = false
        layout.setBackgroundColor(Color.TRANSPARENT)
        layout.removeAllViews()
        layout.addView(viewLayout, 0)

        layout.setPadding(16.toPx(), 0, 16.toPx(), 16.toPx())
        layout.setMargin(0, 0, 0, toasterCustomBottomHeight)

        snackBar.show()

        toasterCustomBottomHeight = 0
    }
}
