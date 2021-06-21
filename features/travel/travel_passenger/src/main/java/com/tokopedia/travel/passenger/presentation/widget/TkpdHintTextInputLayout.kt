package com.tokopedia.travel.passenger.presentation.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.appcompat.widget.DrawableUtils
import androidx.appcompat.widget.TintTypedArray
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.internal.CheckableImageButton
import com.tokopedia.travel.passenger.R
import java.util.*

class TkpdHintTextInputLayout : LinearLayout {
    private var mFrameLayout: FrameLayout? = null
    var mEditText: EditText? = null
    private var mDefaultHintTextColor: ColorStateList? = null
    private var mFocusedHintTextColor: ColorStateList? = null
    private var mDisabledHintTextColor: ColorStateList? = null
    var mHintEnabled = false
    private var mHint: CharSequence? = null
    private var mTvLabel: TextView? = null
    private var mTvHelper: TextView? = null
    private var mTvError: TextView? = null
    private var mTvSuccess: TextView? = null
    private var mTvCounter: TextView? = null
    private var mHintAnimationEnabled = false
    private var mErrorEnabled = false
    private var mErrorText: CharSequence? = null
    private var mSuccessText: CharSequence? = null
    private var mRestoringSavedState = false
    private var mInDrawableStateChanged = false
    private var mHintAppearance = 0
    private var mErrorTextAppearance = 0
    private var mCounterTextAppearance = 0
    private var mCounterOverflowTextAppearance = 0
    private var mHintTextSize = 0f
    private var mHintTypeface: Typeface? = null
    private var mColorNormal: ColorStateList? = null
    private var mColorActivated: ColorStateList? = null
    private var mColorHighlight: ColorStateList? = null
    private var mCounterEnabled = false
    private var mCounterMaxLength = 0
    private var mCounterOverflowed = false
    private var mPasswordToggleContentDesc: CharSequence? = null
    private var mPasswordToggleDrawable: Drawable? = null
    private var mPasswordToggleEnabled = false
    private var mPasswordToggledVisible = false
    private var mPasswordToggleView: CheckableImageButton? = null
    private var mPasswordToggleDummyDrawable: ColorDrawable? = null
    private var mOriginalEditTextEndDrawable: Drawable? = null
    private var mHasPasswordToggleTintList = false
    private var mHasPasswordToggleTintMode = false
    private var mPasswordToggleTintList: ColorStateList? = null
    private var mPasswordToggleTintMode: PorterDuff.Mode? = null
    private var mHelperEnabled = false
    private var mHelperTextAppearance = 0
    var isSuccessShown = false
    private var mSuccessTextAppearance = 0
    private var mHelperText: CharSequence? = null
    private var mPrefixLength = 0
    private var prefixString: String? = null

    constructor(context: Context?) : super(context) {
        apply(null, 0)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        apply(attrs, 0)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        apply(attrs, defStyleAttr)
        init()
    }

    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        apply(attrs, defStyleAttr)
        init()
    }

    @SuppressLint("RestrictedApi", "ResourceType")
    private fun apply(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.TkpdHintTextInputLayout, defStyleAttr, com.google.android.material.R.style.Widget_Design_TextInputLayout)
        mHintEnabled = a.getBoolean(R.styleable.TkpdHintTextInputLayout_hintEnabled, true)
        mHint = a.getText(R.styleable.TkpdHintTextInputLayout_android_hint)
        mHintAnimationEnabled = a.getBoolean(
                R.styleable.TkpdHintTextInputLayout_hintAnimationEnabled, true)
        if (a.hasValue(R.styleable.TkpdHintTextInputLayout_android_textColorHint)) {
            mDisabledHintTextColor = a.getColorStateList(R.styleable.TkpdHintTextInputLayout_android_textColorHint)
            mFocusedHintTextColor = mDisabledHintTextColor
            mDefaultHintTextColor = mFocusedHintTextColor
        }
        mHintAppearance = a.getResourceId(
                R.styleable.TkpdHintTextInputLayout_hintTextAppearance, -1)
        if (mHintAppearance != -1) {
            val hintArr = TintTypedArray.obtainStyledAttributes(context, mHintAppearance,
                    androidx.appcompat.R.styleable.TextAppearance)
            if (hintArr.hasValue(androidx.appcompat.R.styleable.TextAppearance_android_textColor)) {
                mFocusedHintTextColor = hintArr.getColorStateList(
                        androidx.appcompat.R.styleable.TextAppearance_android_textColor)
            }
            if (hintArr.hasValue(androidx.appcompat.R.styleable.TextAppearance_android_textSize)) {
                mHintTextSize = hintArr.getDimensionPixelSize(
                        androidx.appcompat.R.styleable.TextAppearance_android_textSize,
                        mHintTextSize.toInt()).toFloat()
            }
            if (Build.VERSION.SDK_INT >= 21) {
                mHintTypeface = readFontFamilyTypeface(mHintAppearance)
            }
            hintArr.recycle()
        }
        mErrorEnabled = a.getBoolean(R.styleable.TkpdHintTextInputLayout_errorEnabled, false)
        mErrorTextAppearance = a.getResourceId(R.styleable.TkpdHintTextInputLayout_errorTextAppearance, 0)
        mHelperEnabled = a.getBoolean(R.styleable.TkpdHintTextInputLayout_helperEnabled, false)
        mHelperTextAppearance = a.getResourceId(R.styleable.TkpdHintTextInputLayout_helperTextAppearance, R.style.helperTextAppearance)
        mHelperText = a.getText(R.styleable.TkpdHintTextInputLayout_helper)
        isSuccessShown = a.getBoolean(R.styleable.TkpdHintTextInputLayout_successEnabled, false)
        mSuccessTextAppearance = a.getResourceId(R.styleable.TkpdHintTextInputLayout_successTextAppearance, R.style.successTextAppearance)
        mCounterEnabled = a.getBoolean(R.styleable.TkpdHintTextInputLayout_counterEnabled, false)
        mCounterMaxLength = a.getInt(R.styleable.TkpdHintTextInputLayout_counterMaxLength, INVALID_MAX_LENGTH)
        mCounterTextAppearance = a.getResourceId(R.styleable.TkpdHintTextInputLayout_counterTextAppearance, 0)
        mCounterOverflowTextAppearance = a.getResourceId(R.styleable.TkpdHintTextInputLayout_counterOverflowTextAppearance, 0)
        mPasswordToggleEnabled = a.getBoolean(R.styleable.TkpdHintTextInputLayout_passwordToggleEnabled, true)
        mPasswordToggleDrawable = a.getDrawable(R.styleable.TkpdHintTextInputLayout_passwordToggleDrawable)
        mPasswordToggleContentDesc = a.getText(R.styleable.TkpdHintTextInputLayout_passwordToggleContentDescription)
        if (a.hasValue(R.styleable.TkpdHintTextInputLayout_passwordToggleTint)) {
            mHasPasswordToggleTintList = true
            mPasswordToggleTintList = a.getColorStateList(
                    R.styleable.TkpdHintTextInputLayout_passwordToggleTint)
        }

        val theme = context.theme
        if (theme != null) {
            val appcompatCheckAttrs = intArrayOf(
                    androidx.appcompat.R.attr.colorControlNormal,
                    androidx.appcompat.R.attr.colorControlActivated,
                    androidx.appcompat.R.attr.colorControlHighlight
            )
            val arr2 = theme.obtainStyledAttributes(appcompatCheckAttrs)
            mColorNormal = arr2.getColorStateList(0)
            mColorActivated = arr2.getColorStateList(1)
            mFocusedHintTextColor = mColorActivated
            mColorHighlight = arr2.getColorStateList(2)
            arr2.recycle()
        }
        var hasNormalValue = false
        if (a.hasValue(R.styleable.TkpdHintTextInputLayout_defaultTextColorLabel)) {
            mDefaultHintTextColor = a.getColorStateList(R.styleable.TkpdHintTextInputLayout_defaultTextColorLabel)
            hasNormalValue = true
        }
        if (a.hasValue(R.styleable.TkpdHintTextInputLayout_focusedTextColorLabel)) {
            mFocusedHintTextColor = a.getColorStateList(R.styleable.TkpdHintTextInputLayout_focusedTextColorLabel)
        } else if (hasNormalValue) {
            mFocusedHintTextColor = mDefaultHintTextColor
        }
        if (a.hasValue(R.styleable.TkpdHintTextInputLayout_disabledTextColorLabel)) {
            mDisabledHintTextColor = a.getColorStateList(R.styleable.TkpdHintTextInputLayout_disabledTextColorLabel)
        } else if (hasNormalValue) {
            mDisabledHintTextColor = mDefaultHintTextColor
        }
        prefixString = a.getString(R.styleable.TkpdHintTextInputLayout_prefixString)
        a.recycle()
    }

    private fun init() {
        val view = inflate(context, R.layout.hint_text_input_layout, this)
        mFrameLayout = view.findViewById<View>(R.id.frame_content) as FrameLayout
        mTvLabel = view.findViewById<View>(R.id.tv_label) as TextView
        mTvHelper = view.findViewById<View>(R.id.tv_helper) as TextView
        mTvError = view.findViewById<View>(R.id.tv_error) as TextView
        mTvSuccess = view.findViewById<View>(R.id.tv_success) as TextView
        mTvCounter = view.findViewById<View>(R.id.tv_counter) as TextView
        mPrefixLength = prefixString?.length ?: 0
        if (mCounterEnabled && mPrefixLength > 0) {
            counterMaxLength = mCounterMaxLength + mPrefixLength
        }
        setUIHint()
        setUICounter()
        setUIError()
        setUIHelper()
        setUISuccess()
        setUIPasswordToogle()
        applyPasswordToggleTint()
        setAddStatesFromChildren(true)
        mFrameLayout?.setAddStatesFromChildren(true)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mTvLabel?.isEnabled = enabled
            mEditText?.isEnabled = enabled
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return if (mEditText != null) {
            mEditText!!.requestFocus()
        } else {
            super.requestFocus(direction, previouslyFocusedRect)
        }
    }

    private fun setUIHint() {
        if (mHintEnabled && !TextUtils.isEmpty(mHint)) {
            mTvLabel?.text = mHint
            mTvLabel?.visibility = VISIBLE
        } else {
            mTvLabel?.visibility = GONE
        }
        if (mDefaultHintTextColor != null) {
            mTvLabel?.setTextColor(mDefaultHintTextColor)
        }
        mTvLabel?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHintTextSize)
        if (mHintTypeface != null) {
            mTvLabel?.typeface = mHintTypeface
        }
        if (mEditText != null) {
            updateLabelState(false)
        }
    }

    fun setLabel(label: CharSequence?) {
        mHint = label
        setUIHint()
    }

    fun setHint(hint: CharSequence?) {
            mEditText?.hint = hint
    }

    fun setUICounter() {
        mTvCounter?.let {
            TextViewCompat.setTextAppearance(it, mCounterTextAppearance)
            if (mCounterEnabled) {
                updateCounter()
                it.visibility = VISIBLE
            } else {
                it.visibility = GONE
            }
        }
    }

    private fun setUIError() {
        mTvError?.let {
            if (mErrorTextAppearance != 0) {
                TextViewCompat.setTextAppearance(it, mErrorTextAppearance)
            }
            error = mErrorText
        }
    }

    private fun setUISuccess() {
        mTvSuccess?.let {
            if (mSuccessTextAppearance != 0) {
                TextViewCompat.setTextAppearance(it, mSuccessTextAppearance)
            }
            setSuccess(mSuccessText)
        }
    }

    private fun setUIHelper() {
        mTvHelper?.let {
            if (mHelperTextAppearance != 0) {
                TextViewCompat.setTextAppearance(it, mHelperTextAppearance)
            }
            setHelper(mHelperText)
        }
    }

    fun setErrorEnabled(enabled: Boolean) {
        if (mErrorEnabled != enabled) {
            mErrorEnabled = enabled
            checkErrorVisible()
            updateEditTextBackground()
        }
    }

    private fun checkErrorVisible() {
        if (mErrorEnabled) {
            if (TextUtils.isEmpty(mErrorText)) {
                mTvError?.visibility = GONE
                mErrorEnabled = false
            } else { // not empty
                mTvError?.visibility = VISIBLE
                mTvSuccess?.visibility = GONE
                isSuccessShown = false
            }
        } else {
            mTvError?.visibility = GONE
        }
    }

    private fun checkSuccessVisible() {
        if (isSuccessShown) {
            if (TextUtils.isEmpty(mSuccessText)) {
                mTvSuccess?.visibility = GONE
                isSuccessShown = false
            } else { // not empty
                mTvSuccess?.visibility = VISIBLE
                mTvError?.visibility = GONE
                mErrorEnabled = false
            }
        } else {
            mTvSuccess?.visibility = GONE
        }
    }

    fun setSuccessEnabled(enabled: Boolean) {
        if (isSuccessShown != enabled) {
            isSuccessShown = enabled
            checkSuccessVisible()
            setUISuccess()
        }
    }

    fun setHelperEnabled(enabled: Boolean) {
        if (mHelperEnabled != enabled) {
            mHelperEnabled = enabled
            setUIHelper()
        }
    }

    fun setErrorTextAppearance(@StyleRes resId: Int) {
        mTvError?.let {
            mErrorTextAppearance = resId
            TextViewCompat.setTextAppearance(it, resId)
        }
    }

    fun setHelperTextAppearance(@StyleRes resId: Int) {
        mTvHelper?.let {
            mHelperTextAppearance = resId
            TextViewCompat.setTextAppearance(it, resId)
        }
    }

    fun setSuccessTextAppearance(@StyleRes resId: Int) {
        mTvSuccess?.let {
            mSuccessTextAppearance = resId
            TextViewCompat.setTextAppearance(it, resId)
        }
    }

    private fun setError(error: CharSequence?, animate: Boolean) {
        mErrorText = error
        if (!mErrorEnabled) {
            if (TextUtils.isEmpty(error)) {
                if (mTvError?.visibility == VISIBLE) {
                    mTvError?.visibility = GONE
                }
                // If error isn't enabled, and the error is empty, just return
                return
            }
            // Else, we'll assume that they want to enable the error functionality
            setErrorEnabled(true)
        }
        if (!TextUtils.isEmpty(error)) {
            mTvError?.text = error
            mTvError?.visibility = VISIBLE
        } else { // empty error
            if (mTvError?.visibility == VISIBLE) {
                mTvError?.text = error
                mTvError?.visibility = GONE
            }
        }
        updateEditTextBackground()
        updateLabelState(animate)
    }

    fun setSuccess(success: CharSequence?) {
        // Only animate if we're enabled, laid out, and we have a different error message
        setSuccess(success, ViewCompat.isLaidOut(this) && isEnabled
                && !TextUtils.equals(mTvSuccess?.text, success))
    }

    fun hideSuccessError() {
        if (!mErrorEnabled && isSuccessShown) {
            return
        }
        mTvSuccess?.text = null
        mTvError?.text = null
        mTvSuccess?.visibility = VISIBLE
        isSuccessShown = true
        mTvError?.visibility = GONE
        mErrorEnabled = false
        updateEditTextBackground()
    }

    fun disableSuccessError() {
        if (!isSuccessShown && !mErrorEnabled) {
            return
        }
        isSuccessShown = false
        mErrorEnabled = false
        mTvSuccess?.visibility = GONE
        mTvError?.visibility = GONE
        updateEditTextBackground()
    }

    private fun setSuccess(successText: CharSequence?, animate: Boolean) {
        mSuccessText = successText
        if (!isSuccessShown) {
            if (TextUtils.isEmpty(successText)) {
                if (mTvSuccess?.visibility == VISIBLE) {
                    mTvSuccess?.visibility = GONE
                }
                // If success isn't enabled, and the error is empty, just return
                return
            }
            // Else, we'll assume that they want to enable the success functionality
            setSuccessEnabled(true)
        }
        if (!TextUtils.isEmpty(successText)) {
            mTvSuccess?.text = successText
            mTvSuccess?.visibility = VISIBLE
        } else { // empty error
            if (mTvSuccess?.visibility == VISIBLE) {
                mTvSuccess?.text = successText
                mTvSuccess?.visibility = GONE
            }
        }
        updateEditTextBackground()
        updateLabelState(animate)
    }

    fun setHelper(helper: CharSequence?) {
        mHelperText = helper
        if (!mHelperEnabled) {
            if (TextUtils.isEmpty(helper)) {
                if (mTvHelper?.visibility == VISIBLE) {
                    mTvHelper?.visibility = GONE
                }
                // If error isn't enabled, and the error is empty, just return
                return
            }
            // Else, we'll assume that they want to enable the error functionality
            setHelperEnabled(true)
        }
        if (!TextUtils.isEmpty(helper)) {
            mTvHelper?.text = helper
            mTvHelper?.visibility = VISIBLE
        } else { // empty helper
            if (mTvHelper?.visibility == VISIBLE) {
                mTvHelper?.text = helper
                mTvHelper?.visibility = GONE
            }
        }
    }

    fun setUIPasswordToogle() {
        if (!mPasswordToggleEnabled && mPasswordToggledVisible && mEditText != null) {
            // If the toggle is no longer enabled, but we remove the PasswordTransformation
            // to make the password visible, add it back
            mEditText?.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        // Reset the visibility tracking flag
        mPasswordToggledVisible = false
        updatePasswordToggleView()
    }

    fun setPasswordVisibilityToggleEnabled(enabled: Boolean) {
        if (mPasswordToggleEnabled != enabled) {
            mPasswordToggleEnabled = enabled
            setUIPasswordToogle()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun updatePasswordToggleView() {
        if (mEditText == null) {
            // If there is no EditText, there is nothing to update
            return
        }
        if (shouldShowPasswordIcon()) {
            if (mPasswordToggleView == null) {
                mPasswordToggleView = LayoutInflater.from(context)
                        .inflate(R.layout.design_text_input_password_icon,
                                mFrameLayout, false) as CheckableImageButton
                mPasswordToggleView?.setImageDrawable(mPasswordToggleDrawable)
                mPasswordToggleView?.contentDescription = mPasswordToggleContentDesc
                mFrameLayout?.addView(mPasswordToggleView)
                mPasswordToggleView?.setOnClickListener { passwordVisibilityToggleRequested() }
            }
            mPasswordToggleView?.let {
                mEditText?.let { et ->
                    if (ViewCompat.getMinimumHeight(et) <= 0) {
                        // We should make sure that the EditText has the same min-height as the password
                        // toggle view. This ensure focus works properly, and there is no visual jump
                        // if the password toggle is enabled/disabled.
                        et.minimumHeight = ViewCompat.getMinimumHeight(it)
                    }
                    it.visibility = VISIBLE
                    it.isChecked = mPasswordToggledVisible

                    // We need to add a dummy drawable as the end compound drawable so that the text is
                    // indented and doesn't display below the toggle view
                    if (mPasswordToggleDummyDrawable == null) {
                        mPasswordToggleDummyDrawable = ColorDrawable()
                    }
                    it.post(object : Runnable {
                        override fun run() {
                            mPasswordToggleDummyDrawable?.setBounds(0, 0, it.measuredWidth, 1)
                            val compounds = TextViewCompat.getCompoundDrawablesRelative(et)
                            // Store the user defined end compound drawable so that we can restore it later
                            if (compounds[2] !== mPasswordToggleDummyDrawable) {
                                mOriginalEditTextEndDrawable = compounds[2]
                            }
                            TextViewCompat.setCompoundDrawablesRelative(et, compounds[0], compounds[1],
                                    mPasswordToggleDummyDrawable, compounds[3])

                            // Copy over the EditText's padding so that we match
                            it.setPadding(et.paddingLeft,
                                    et.paddingTop, et.paddingRight,
                                    et.paddingBottom)
                        }
                    })
                }
            }
        } else {
            if (mPasswordToggleView?.visibility == VISIBLE) {
                mPasswordToggleView?.visibility = GONE
            }
            mEditText?.let {
                if (mPasswordToggleDummyDrawable != null) {
                    // Make sure that we remove the dummy end compound drawable if it exists, and then
                    // clear it
                    val compounds = TextViewCompat.getCompoundDrawablesRelative(it)
                    if (compounds[2] === mPasswordToggleDummyDrawable) {
                        TextViewCompat.setCompoundDrawablesRelative(it, compounds[0],
                                compounds[1], mOriginalEditTextEndDrawable, compounds[3])
                        mPasswordToggleDummyDrawable = null
                    }
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun passwordVisibilityToggleRequested() {
        if (mPasswordToggleEnabled) {
            // Store the current cursor position
            var selection = mEditText?.selectionEnd ?: 0
            if (hasPasswordTransformation()) {
                mEditText?.transformationMethod = null
                mPasswordToggledVisible = true
            } else {
                mEditText?.transformationMethod = PasswordTransformationMethod.getInstance()
                mPasswordToggledVisible = false
            }
            mPasswordToggleView?.isChecked = mPasswordToggledVisible
            if (selection < 0) selection = 0
            // And restore the cursor position
            mEditText?.setSelection(selection)
        }
    }

    fun setPasswordVisibilityToggleTintList(tintList: ColorStateList?) {
        mPasswordToggleTintList = tintList
        mHasPasswordToggleTintList = true
        applyPasswordToggleTint()
    }

    fun setPasswordVisibilityToggleTintMode(mode: PorterDuff.Mode?) {
        mPasswordToggleTintMode = mode
        mHasPasswordToggleTintMode = true
        applyPasswordToggleTint()
    }

    private fun applyPasswordToggleTint() {
        mPasswordToggleDrawable?.let {
            if (mHasPasswordToggleTintList || mHasPasswordToggleTintMode) {
                mPasswordToggleDrawable = DrawableCompat.wrap(it).mutate()
                if (mHasPasswordToggleTintList) {
                    DrawableCompat.setTintList(it, mPasswordToggleTintList)
                }
                if (mHasPasswordToggleTintMode) {
                    mPasswordToggleTintMode?.let { passTintMode ->
                        DrawableCompat.setTintMode(it, passTintMode)
                    }
                }
                if (mPasswordToggleView?.drawable !== mPasswordToggleDrawable) {
                    mPasswordToggleView?.setImageDrawable(mPasswordToggleDrawable)
                }
            }
        }
    }

    private fun shouldShowPasswordIcon(): Boolean {
        return mPasswordToggleEnabled && (hasPasswordTransformation() || mPasswordToggledVisible)
    }

    private fun hasPasswordTransformation(): Boolean {
        return (mEditText != null
                && mEditText?.transformationMethod is PasswordTransformationMethod)
    }

    var isCounterEnabled: Boolean
        get() = mCounterEnabled
        set(counterEnabled) {
            if (mCounterEnabled != counterEnabled) {
                mCounterEnabled = counterEnabled
                setUICounter()
            }
        }
    var counterMaxLength: Int
        get() = mCounterMaxLength
        set(maxLength) {
            if (mCounterMaxLength != maxLength) {
                mCounterMaxLength = if (maxLength > 0) {
                    maxLength
                } else {
                    INVALID_MAX_LENGTH
                }
                if (mCounterEnabled) {
                    updateCounter()
                }
            }
        }

    private fun updateCounter() {
        var length = 0
        if (mEditText != null && mEditText?.text?.isNotEmpty() == true) {
            length = mEditText?.text?.length ?: 0
        }
        val wasCounterOverflowed = mCounterOverflowed
        val currentLength = if (length > mPrefixLength) length - mPrefixLength else length
        if (mCounterMaxLength == INVALID_MAX_LENGTH) {
            mTvCounter?.text = (currentLength - mPrefixLength).toString()
            mCounterOverflowed = false
        } else {
            mCounterOverflowed = length > mCounterMaxLength
            if (wasCounterOverflowed != mCounterOverflowed) {
                mTvCounter?.let {
                    TextViewCompat.setTextAppearance(it, if (mCounterOverflowed) mCounterOverflowTextAppearance else mCounterTextAppearance)
                }
            }
            mTvCounter?.text = String.format(Locale.US, "%1\$d / %2\$d",
                    currentLength, mCounterMaxLength - mPrefixLength)
        }
        if (mEditText != null && wasCounterOverflowed != mCounterOverflowed) {
            updateLabelState(false)
            updateEditTextBackground()
        }
    }

    fun resetCounter() {
        mTvCounter?.text = String.format(Locale.US, "%1\$d / %2\$d",
                0, mCounterMaxLength - mPrefixLength)
    }

    private fun readFontFamilyTypeface(resId: Int): Typeface? {
        val a = context.obtainStyledAttributes(resId, intArrayOf(android.R.attr.fontFamily))
        try {
            val family = a.getString(0)
            if (family != null) {
                return Typeface.create(family, Typeface.NORMAL)
            }
        } finally {
            a.recycle()
        }
        return null
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is EditText) {
            // Make sure that the EditText is vertically at the bottom, so that it sits on the
            // EditText's underline
            mFrameLayout?.addView(child)

            // Now use the EditText's LayoutParams as our own and update them to make enough space
            // for the label
            setEditText(child)
            setUIPasswordToogle()
            applyPasswordToggleTint()
        } else {
            // Carry on adding the View...
            super.addView(child, index, params)
        }
    }

    private fun setEditText(editText: EditText) {
        // If we already have an EditText, throw an exception
        require(mEditText == null) { "We already have an EditText, can only have one" }
        mEditText= editText

        // Add a TextWatcher so that we know when the text input has changed
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                updateLabelState(!mRestoringSavedState)
                if (mCounterEnabled) {
                    updateCounter()
                }
            }
        })

        // Use the EditText's hint colors if we don't have one set
        if (mDefaultHintTextColor == null) {
            mDefaultHintTextColor = editText.hintTextColors
        }
        if (mFocusedHintTextColor == null) {
            mFocusedHintTextColor = editText.hintTextColors
        }

        // If we do not have a valid hint, try and retrieve it from the EditText, if enabled
        if (mHintEnabled && TextUtils.isEmpty(mHint)) {
            mHint = editText.hint
            mTvLabel?.text = mHint
        }
        // Update the label visibility with no animation, but force a state change
        updateLabelState(false, true)
    }

    override fun drawableStateChanged() {
        if (mInDrawableStateChanged) {
            // Some of the calls below will update the drawable state of child views. Since we're
            // using addStatesFromChildren we can get into infinite recursion, hence we'll just
            // exit in this instance
            return
        }
        mInDrawableStateChanged = true
        super.drawableStateChanged()

        // Drawable state has changed so see if we need to update the label
        updateLabelState(ViewCompat.isLaidOut(this) && isEnabled)
        updateEditTextBackground()
        invalidate()
        mInDrawableStateChanged = false
    }

    @SuppressLint("RestrictedApi")
    private fun updateEditTextBackground() {
        if (mEditText == null) {
            return
        }
        var editTextBackground = mEditText?.background ?: return
        if (DrawableUtils.canSafelyMutateDrawable(editTextBackground)) {
            editTextBackground = editTextBackground.mutate()
        }
        val isErrorShowing = !TextUtils.isEmpty(error)
        if (isErrorShowing) {
            // Set a color filter of the error color
            mTvError?.let {
                editTextBackground.colorFilter = AppCompatDrawableManager.getPorterDuffColorFilter(
                        it.currentTextColor, PorterDuff.Mode.SRC_IN)
            }
        } else if (mCounterOverflowed) {
            // Set a color filter of the counter color
            mTvCounter?.let {
                editTextBackground.colorFilter = AppCompatDrawableManager.getPorterDuffColorFilter(
                        it.currentTextColor, PorterDuff.Mode.SRC_IN)
            }
        } else {
            // Else reset the color filter and refresh the drawable state so that the
            // normal tint is used
            DrawableCompat.clearColorFilter(editTextBackground)
            mEditText?.refreshDrawableState()
        }
    }

    @JvmOverloads
    fun updateLabelState(animate: Boolean, force: Boolean = false) {
        val isEnabled = isEnabled
        if (mEditText == null) {
            return
        }
        val isFocused = mEditText?.isFocused ?: false
        if (!isEnabled) {
            mTvLabel?.setTextColor(mDisabledHintTextColor)
        } else if (isFocused) {
            mTvLabel?.setTextColor(mFocusedHintTextColor)
        } else {
            mTvLabel?.setTextColor(mDefaultHintTextColor)
        }
    }

    private fun parseTintMode(value: Int, defaultMode: PorterDuff.Mode): PorterDuff.Mode {
        return when (value) {
            3 -> PorterDuff.Mode.SRC_OVER
            5 -> PorterDuff.Mode.SRC_IN
            9 -> PorterDuff.Mode.SRC_ATOP
            14 -> PorterDuff.Mode.MULTIPLY
            15 -> PorterDuff.Mode.SCREEN
            else -> defaultMode
        }
    }

    // Only animate if we're enabled, laid out, and we have a different error message
    var error: CharSequence?
        get() = if (mErrorEnabled) mErrorText else null
        set(error) {
            // Only animate if we're enabled, laid out, and we have a different error message
            setError(error, ViewCompat.isLaidOut(this) && isEnabled
                    && !TextUtils.equals(mTvError?.text, error))
        }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        mRestoringSavedState = true
        super.dispatchRestoreInstanceState(container)
        mRestoringSavedState = false
    }

    companion object {
        private const val INVALID_MAX_LENGTH = -1
        private fun arrayContains(array: IntArray, value: Int): Boolean {
            for (v in array) {
                if (v == value) {
                    return true
                }
            }
            return false
        }
    }
}