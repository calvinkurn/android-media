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

    var state: State = State.GONE
        set(value) {
            field = value
            initView()
        }

    init {
        binding =
                ItemCheckoutDropshipBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
            )
        initView()
    }

    private fun initView() {
        when (state) {
            State.GONE -> setViewGone()
            State.ENABLED -> setViewEnabled()
            State.DISABLED -> setViewDisabled()
        }

        invalidate()
        requestLayout()
    }

    interface DropshipWidgetListener {
        fun onClickDropshipLabel()

        fun isAddOnProtectionOptIn(): Boolean

        fun showToasterErrorProtectionUsage()

    }

    fun setupListener(dropshipListener: DropshipWidgetListener) {
        actionListener = dropshipListener
    }

    private fun setViewGone() {
        binding?.apply {
            containerDropship.gone()
            dropshipName?.editText?.setText("")
            dropshipPhone?.editText?.setText("")
        }
    }

    private fun setViewEnabled() {
        binding?.containerDropship?.visible()
        initSwitch(false)
        renderDefaultDropship()
    }

    private fun setViewDisabled() {
        if (state == State.ENABLED) actionListener?.showToasterErrorProtectionUsage()
        binding?.containerDropship?.visible()
        hideDetailDropship()
        initSwitch(false)
    }

    private fun hideDetailDropship() {
        binding?.tfDropshipName?.gone()
        binding?.tfDropshipPhone?.gone()
    }

    private fun showDetailDropship() {
        binding?.tfDropshipName?.visible()
        binding?.tfDropshipPhone?.visible()
    }

    private fun validateShowingDetailDropship() {
        if (actionListener?.isAddOnProtectionOptIn() == true) {
            binding?.switchDropship?.isChecked = false
            actionListener?.showToasterErrorProtectionUsage()
        } else {
            showDetailDropship()
        }
    }

    private fun renderDefaultDropship() {
        binding?.tvDropshipTitle?.setDropshipLabel()
        hideDetailDropship()
        binding?.tfDropshipName?.editText?.hint = context.getString(checkoutR.string.dropship_name)
        binding?.tfDropshipPhone?.editText?.hint = context.getString(checkoutR.string.dropship_phone)
    }

    private fun initSwitch(isEnabled: Boolean) {
        binding?.switchDropship?.visible()
        binding?.switchDropship?.apply {
            isChecked = isEnabled
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) validateShowingDetailDropship()
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

    enum class State(val id: Int) {
        GONE(-1), // totally gone
        DISABLED(0), // show dropship widget, but show toaster onclick
        ENABLED(1); // show dropship widget and enable to click
    }
}
