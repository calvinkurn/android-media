package com.tokopedia.checkout.revamp.view.widget

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.databinding.ItemCheckoutDropshipBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
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
    private val phoneNumberRegexPattern: Pattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN)
    private var delayDropshipNameTypingJob: Job? = null
    private var delayDropshipPhoneTypingJob: Job? = null

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
            State.DISABLED -> setViewInit(true)
            State.INIT -> setViewInit(false)
            State.SELECTED -> setViewSelected()
        }

        invalidate()
        requestLayout()
    }

    interface DropshipWidgetListener {
        fun onClickDropshipLabel()

        fun isAddOnProtectionOptIn(): Boolean

        fun showToasterErrorProtectionUsage()

        fun onClickDropshipSwitch(isChecked: Boolean)

        fun setDropshipName(name: String)

        fun setDropshipPhone(phone: String)
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

    private fun setViewInit(disabled: Boolean) {
        binding?.containerDropship?.visible()
        initSwitch(disabled)
        renderDefaultDropship()
    }

    private fun setViewSelected() {
        binding?.containerDropship?.visible()
        initSwitch(false)
        showDetailDropship()
    }

    private fun setViewDisabled() {
        if (state == State.SELECTED) actionListener?.showToasterErrorProtectionUsage()
        binding?.containerDropship?.visible()
        hideDetailDropship()
        renderDefaultDropship()
    }

    private fun hideDetailDropship() {
        binding?.tfDropshipName?.gone()
        binding?.tfDropshipPhone?.gone()
    }

    private fun showDetailDropship() {
        binding?.tfDropshipName?.apply {
            visible()
            isInputError = false
            editText.imeOptions = EditorInfo.IME_ACTION_DONE
            editText.isFocusableInTouchMode = true
            editText.inputType = InputType.TYPE_CLASS_TEXT
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    if (text?.toString()?.isNotEmpty() == true) {
                        delayDropshipNameTypingJob?.cancel()
                        delayDropshipNameTypingJob = GlobalScope.launch(Dispatchers.Main) {
                            delay(DROPSHIP_CHANGE_DELAY)

                            if (text?.toString()?.isNotEmpty() == true &&
                                text.length < DROPSHIPPER_MIN_NAME_LENGTH
                            ) {
                                binding?.tfDropshipName?.isInputError = true
                                binding?.tfDropshipName?.setMessage(context.getString(checkoutR.string.message_error_dropshipper_name_min_length))
                            } else if (text?.toString()?.isNotEmpty() == true &&
                                text.length > DROPSHIPPER_MAX_NAME_LENGTH
                            ) {
                                binding?.tfDropshipName?.isInputError = true
                                binding?.tfDropshipName?.setMessage(context.getString(checkoutR.string.message_error_dropshipper_name_max_length))
                            } else {
                                binding?.tfDropshipName?.isInputError = false
                                binding?.tfDropshipName?.setMessage("")
                                actionListener?.setDropshipName(text.toString())
                            }
                        }
                    }
                }
            })
        }
        binding?.tfDropshipPhone?.apply {
            visible()
            isInputError = false
            editText.imeOptions = EditorInfo.IME_ACTION_DONE
            editText.isFocusableInTouchMode = true
            editText.inputType = InputType.TYPE_CLASS_PHONE
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    if (text?.toString()?.isNotEmpty() == true) {
                        val matcher = phoneNumberRegexPattern.matcher(text)

                        delayDropshipPhoneTypingJob?.cancel()
                        delayDropshipPhoneTypingJob = GlobalScope.launch(Dispatchers.Main) {
                            delay(DROPSHIP_CHANGE_DELAY)

                            if (text?.toString()?.isNotEmpty() == true && !matcher.matches()) {
                                binding?.tfDropshipPhone?.isInputError = true
                                binding?.tfDropshipPhone?.setMessage(context.getString(checkoutR.string.message_error_dropshipper_phone_invalid))
                                actionListener?.setDropshipPhone(text.toString())
                            } else if (text?.toString()?.isNotEmpty() == true &&
                                text.length < DROPSHIPPER_MIN_PHONE_LENGTH
                            ) {
                                binding?.tfDropshipPhone?.isInputError = true
                                binding?.tfDropshipPhone?.setMessage(context.getString(checkoutR.string.message_error_dropshipper_phone_min_length))
                                actionListener?.setDropshipPhone(text.toString())
                            } else if (text?.toString()?.isNotEmpty() == true &&
                                text.length > DROPSHIPPER_MAX_PHONE_LENGTH
                            ) {
                                binding?.tfDropshipPhone?.isInputError = true
                                binding?.tfDropshipPhone?.setMessage(context.getString(checkoutR.string.message_error_dropshipper_phone_max_length))
                                actionListener?.setDropshipPhone(text.toString())
                            } else {
                                binding?.tfDropshipPhone?.isInputError = false
                                binding?.tfDropshipPhone?.setMessage("")
                                actionListener?.setDropshipPhone(text.toString())
                            }
                        }
                    }
                }
            })
        }
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
    }

    private fun initSwitch(disabled: Boolean) {
        binding?.switchDropship?.visible()
        if (disabled) binding?.switchDropship?.isChecked = false
        binding?.switchDropship?.apply {
            setOnCheckedChangeListener { compoundButton, isChecked ->
                if (compoundButton.isPressed) {
                    if (disabled) binding?.switchDropship?.isChecked = false
                    // validateShowingDetailDropship()
                    actionListener?.onClickDropshipSwitch(isChecked)
                }
            }
            /*setOnCheckedChangeListener { _, isChecked ->
                if (disabled) binding?.switchDropship?.isEnabled = false
                actionListener?.onClickDropshipSwitch(isChecked)
            }*/
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

    override fun onDetachedFromWindow() {
        delayDropshipNameTypingJob?.cancel()
        delayDropshipPhoneTypingJob?.cancel()
        super.onDetachedFromWindow()
    }

    enum class State(val id: Int) {
        GONE(-1), // totally gone
        DISABLED(0), // show dropship widget, but show toaster onclick
        INIT(1), // show init dropship widget
        SELECTED(2) // show dropship name & phone
    }

    companion object {
        const val DROPSHIPPER_MIN_NAME_LENGTH = 3
        const val DROPSHIPPER_MAX_NAME_LENGTH = 100
        const val DROPSHIPPER_MIN_PHONE_LENGTH = 6
        const val DROPSHIPPER_MAX_PHONE_LENGTH = 20
        private const val PHONE_NUMBER_REGEX_PATTERN = "\\d+"
        private const val DROPSHIP_CHANGE_DELAY = 1000L
    }
}
