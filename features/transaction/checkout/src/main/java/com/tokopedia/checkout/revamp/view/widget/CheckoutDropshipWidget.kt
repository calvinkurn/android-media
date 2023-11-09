package com.tokopedia.checkout.revamp.view.widget

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.databinding.ItemCheckoutDropshipBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.checkout.R as checkoutR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CheckoutDropshipWidget : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ItemCheckoutDropshipBinding? = null
    private var actionListener: DropshipWidgetListener? = null

    val dropshipName: TextFieldUnify2?
        get() = binding?.tfDropshipName

    val dropshipPhone: TextFieldUnify2?
        get() = binding?.tfDropshipPhone

    init {
        binding =
                ItemCheckoutDropshipBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
            )
        renderDefaultDropship()
    }

    interface DropshipWidgetListener {
        fun onClickDropshipLabel()

    }

    private fun hideDetailDropship() {
        binding?.tfDropshipName?.gone()
        binding?.tfDropshipPhone?.gone()
    }

    private fun showDetailDropship() {
        binding?.tfDropshipName?.visible()
        binding?.tfDropshipPhone?.visible()
    }

    private fun renderDefaultDropship() {
        binding?.tvDropshipTitle?.setDropshipLabel()
        initSwitch(false)
    }

    private fun initSwitch(isEnabled: Boolean) {
        binding?.switchDropship?.apply {
            isChecked = isEnabled
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) showDetailDropship()
                else hideDetailDropship()
            }
        }
    }

    private fun Typography.setDropshipLabel() {
        val dropshipLabel = context.getString(checkoutR.string.dropship_label)
        val title = context.getString(checkoutR.string.dropship_title_widget, dropshipLabel)

        val onDropshipClicked: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                actionListener?.onClickDropshipLabel()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN600
                )
            }
        }

        val firstIndex = title.indexOf(dropshipLabel)
        val lastIndex = firstIndex.plus(dropshipLabel.length)

        val tncText = SpannableString(title).apply {
            setSpan(
                    onDropshipClicked,
                firstIndex,
                lastIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        this.run {
            movementMethod = LinkMovementMethod.getInstance()
            isClickable = true
            setText(tncText, TextView.BufferType.SPANNABLE)
        }
    }
}
