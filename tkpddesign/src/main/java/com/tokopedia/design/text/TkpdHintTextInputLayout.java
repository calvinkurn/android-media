package com.tokopedia.design.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.TintTypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.text.watcher.AfterTextWatcher;

import java.util.Locale;

public class TkpdHintTextInputLayout extends LinearLayout {

    private static final int INVALID_MAX_LENGTH = -1;
    private FrameLayout mFrameLayout;
    private EditText mEditText;

    private ColorStateList mDefaultHintTextColor;
    private ColorStateList mFocusedHintTextColor;
    private ColorStateList mDisabledHintTextColor;

    boolean mHintEnabled;
    private CharSequence mHint;
    private TextView mTvLabel;
    private TextView mTvHelper;
    private TextView mTvError;
    private TextView mTvSuccess;
    private TextView mTvCounter;
    private boolean mHintAnimationEnabled;
    private boolean mErrorEnabled;
    private CharSequence mErrorText;
    private CharSequence mSuccessText;
    private boolean mRestoringSavedState;
    private boolean mInDrawableStateChanged;
    private int mHintAppearance;
    private int mErrorTextAppearance;
    private int mCounterTextAppearance;
    private int mCounterOverflowTextAppearance;

    private float mHintTextSize;
    private Typeface mHintTypeface;
    private ColorStateList mColorNormal;
    private ColorStateList mColorActivated;
    private ColorStateList mColorHighlight;
    private boolean mCounterEnabled;
    private int mCounterMaxLength;

    private boolean mCounterOverflowed;
    private CharSequence mPasswordToggleContentDesc;
    private Drawable mPasswordToggleDrawable;
    private boolean mPasswordToggleEnabled;
    private boolean mPasswordToggledVisible;
    private CheckableImageButton mPasswordToggleView;
    private ColorDrawable mPasswordToggleDummyDrawable;
    private Drawable mOriginalEditTextEndDrawable;
    private boolean mHasPasswordToggleTintList;
    private boolean mHasPasswordToggleTintMode;
    private ColorStateList mPasswordToggleTintList;
    private PorterDuff.Mode mPasswordToggleTintMode;
    private boolean mHelperEnabled;
    private int mHelperTextAppearance;
    private boolean mSuccessEnabled;
    private int mSuccessTextAppearance;
    private CharSequence mHelperText;

    private int mPrefixLength;

    public TkpdHintTextInputLayout(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public TkpdHintTextInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public TkpdHintTextInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public TkpdHintTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.TextInputLayoutv2, defStyleAttr, android.support.design.R.style.Widget_Design_TextInputLayout);
        mHintEnabled = a.getBoolean(R.styleable.TextInputLayoutv2_hintEnabled, true);
        mHint = a.getText(R.styleable.TextInputLayoutv2_android_hint);
        mHintAnimationEnabled = a.getBoolean(
                R.styleable.TextInputLayoutv2_hintAnimationEnabled, true);

        if (a.hasValue(R.styleable.TextInputLayoutv2_android_textColorHint)) {
            mDefaultHintTextColor = mFocusedHintTextColor = mDisabledHintTextColor =
                    a.getColorStateList(R.styleable.TextInputLayoutv2_android_textColorHint);
        }

        mHintAppearance = a.getResourceId(
                R.styleable.TextInputLayoutv2_hintTextAppearance, -1);
        if (mHintAppearance != -1) {
            TintTypedArray hintArr = TintTypedArray.obtainStyledAttributes(getContext(), mHintAppearance,
                    android.support.v7.appcompat.R.styleable.TextAppearance);
            if (hintArr.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) {
                mFocusedHintTextColor = hintArr.getColorStateList(
                        android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
            }
            if (hintArr.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) {
                mHintTextSize = hintArr.getDimensionPixelSize(
                        android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize,
                        (int) mHintTextSize);
            }
            if (Build.VERSION.SDK_INT >= 16) {
                mHintTypeface = readFontFamilyTypeface(mHintAppearance);
            }
            hintArr.recycle();
        }

        mErrorEnabled = a.getBoolean(R.styleable.TextInputLayoutv2_errorEnabled, false);
        mErrorTextAppearance = a.getResourceId(R.styleable.TextInputLayoutv2_errorTextAppearance, 0);

        mHelperEnabled = a.getBoolean(R.styleable.TextInputLayoutv2_helperEnabled, false);
        mHelperTextAppearance = a.getResourceId(R.styleable.TextInputLayoutv2_helperTextAppearance,
                R.style.helperTextAppearance);
        mHelperText = a.getText(
                R.styleable.TextInputLayoutv2_helper);

        mSuccessEnabled = a.getBoolean(R.styleable.TextInputLayoutv2_successEnabled, false);
        mSuccessTextAppearance = a.getResourceId(R.styleable.TextInputLayoutv2_successTextAppearance,
                R.style.successTextAppearance);

        mCounterEnabled = a.getBoolean(
                R.styleable.TextInputLayoutv2_counterEnabled, false);
        mCounterMaxLength = a.getInt(R.styleable.TextInputLayoutv2_counterMaxLength, INVALID_MAX_LENGTH);
        mCounterTextAppearance = a.getResourceId(
                R.styleable.TextInputLayoutv2_counterTextAppearance, 0);
        mCounterOverflowTextAppearance = a.getResourceId(
                R.styleable.TextInputLayoutv2_counterOverflowTextAppearance, 0);

        mPasswordToggleEnabled = a.getBoolean(
                R.styleable.TextInputLayoutv2_passwordToggleEnabled, true);
        mPasswordToggleDrawable = a.getDrawable(
                R.styleable.TextInputLayoutv2_passwordToggleDrawable);
        mPasswordToggleContentDesc = a.getText(
                R.styleable.TextInputLayoutv2_passwordToggleContentDescription);
        if (a.hasValue(R.styleable.TextInputLayoutv2_passwordToggleTint)) {
            mHasPasswordToggleTintList = true;
            mPasswordToggleTintList = a.getColorStateList(
                    R.styleable.TextInputLayoutv2_passwordToggleTint);
        }
        /*if (a.hasValue(R.styleable.TextInputLayoutv2_passwordToggleTintMode)) {
            mHasPasswordToggleTintMode = true;
            mPasswordToggleTintMode = parseTintMode(
                    a.getInt(R.styleable.TextInputLayoutv2_passwordToggleTintMode, -1), null);
        }*/

        Resources.Theme theme = getContext().getTheme();
        if (theme != null) {
            int[] appcompatCheckAttrs = {
                    android.support.v7.appcompat.R.attr.colorControlNormal,
                    android.support.v7.appcompat.R.attr.colorControlActivated,
                    android.support.v7.appcompat.R.attr.colorControlHighlight
            };
            TypedArray arr2 = theme.obtainStyledAttributes(appcompatCheckAttrs);
            mColorNormal = arr2.getColorStateList(0);
            mColorActivated = arr2.getColorStateList(1);
            mFocusedHintTextColor = mColorActivated;
            mColorHighlight = arr2.getColorStateList(2);
            arr2.recycle();
        }

        boolean hasNormalValue = false;
        if (a.hasValue(R.styleable.TextInputLayoutv2_defaultTextColorLabel)) {
            mDefaultHintTextColor = a.getColorStateList(R.styleable.TextInputLayoutv2_defaultTextColorLabel);
            hasNormalValue = true;
        }
        if (a.hasValue(R.styleable.TextInputLayoutv2_focusedTextColorLabel)) {
            mFocusedHintTextColor = a.getColorStateList(R.styleable.TextInputLayoutv2_focusedTextColorLabel);
        } else if (hasNormalValue) {
            mFocusedHintTextColor = mDefaultHintTextColor;
        }
        if (a.hasValue(R.styleable.TextInputLayoutv2_disabledTextColorLabel)) {
            mDisabledHintTextColor = a.getColorStateList(R.styleable.TextInputLayoutv2_disabledTextColorLabel);
        } else if (hasNormalValue) {
            mDisabledHintTextColor = mDefaultHintTextColor;
        }
        String prefixString = a.getString(R.styleable.TextInputLayoutv2_prefixString);
        mPrefixLength = prefixString == null ? 0 : prefixString.length();
        if (mCounterEnabled && mPrefixLength > 0) {
            setCounterMaxLength(mCounterMaxLength + mPrefixLength);
        }
        a.recycle();

    }

    public EditText getEditText(){
        return mEditText;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.hint_text_input_layout, this);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.frame_content);
        mTvLabel = (TextView) view.findViewById(R.id.tv_label);
        mTvHelper = (TextView) view.findViewById(R.id.tv_helper);
        mTvError = (TextView) view.findViewById(R.id.tv_error);
        mTvSuccess = (TextView) view.findViewById(R.id.tv_success);
        mTvCounter = (TextView) view.findViewById(R.id.tv_counter);

        setUIHint();
        setUICounter();
        setUIError();
        setUIHelper();
        setUISuccess();
        setUIPasswordToogle();
        applyPasswordToggleTint();

        setAddStatesFromChildren(true);
        mFrameLayout.setAddStatesFromChildren(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTvLabel.setEnabled(enabled);
        if (mEditText != null) {
            mEditText.setEnabled(enabled);
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (mEditText != null) {
            return mEditText.requestFocus();
        } else {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
    }

    private void setUIHint() {
        if (mHintEnabled && !TextUtils.isEmpty(mHint)) {
            mTvLabel.setText(mHint);
            mTvLabel.setVisibility(View.VISIBLE);
        } else {
            mTvLabel.setVisibility(View.GONE);
        }
        if (mDefaultHintTextColor != null) {
            mTvLabel.setTextColor(mDefaultHintTextColor);
        }
        mTvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHintTextSize);
        if (mHintTypeface != null) {
            mTvLabel.setTypeface(mHintTypeface);
        }
        if (mEditText != null) {
            updateLabelState(false);
        }
    }

    public void setLabel(CharSequence label){
        mHint = label;
        setUIHint();
    }

    public void setHint(CharSequence hint){
        if (getEditText()!= null) {
            getEditText().setHint(hint);
        }
    }

    private void setUICounter() {
        TextViewCompat.setTextAppearance(mTvCounter, mCounterTextAppearance);
        if (mCounterEnabled) {
            updateCounter();
            mTvCounter.setVisibility(View.VISIBLE);
        } else {
            mTvCounter.setVisibility(View.GONE);
        }
    }

    private void setUIError() {
        if (mErrorTextAppearance!= 0) {
            TextViewCompat.setTextAppearance(mTvError, mErrorTextAppearance);
        }
        setError(mErrorText);
    }

    private void setUISuccess() {
        if (mSuccessTextAppearance!= 0) {
            TextViewCompat.setTextAppearance(mTvSuccess, mSuccessTextAppearance);
        }
        setSuccess(mSuccessText);
    }

    private void setUIHelper() {
        if (mHelperTextAppearance!= 0) {
            TextViewCompat.setTextAppearance(mTvHelper, mHelperTextAppearance);
        }
        setHelper(mHelperText);
    }

    public void setErrorEnabled(boolean enabled) {
        if (mErrorEnabled != enabled) {
            /*if (mErrorView != null) {
                ViewCompat.animate(mErrorView).cancel();
            }*/
            mErrorEnabled = enabled;
            checkErrorVisible();
            updateEditTextBackground();
        }
    }

    private void checkErrorVisible(){
        if (mErrorEnabled) {
            if (TextUtils.isEmpty( mErrorText)){
                mTvError.setVisibility(View.GONE);
                mErrorEnabled = false;
            } else { // not empty
                mTvError.setVisibility(View.VISIBLE);
                mTvSuccess.setVisibility(View.GONE);
                mSuccessEnabled = false;
            }
        } else {
            mTvError.setVisibility(View.GONE);
        }
    }

    private void checkSuccessVisible(){
        if (mSuccessEnabled) {
            if (TextUtils.isEmpty( mSuccessText)){
                mTvSuccess.setVisibility(View.GONE);
                mSuccessEnabled = false;
            } else { // not empty
                mTvSuccess.setVisibility(View.VISIBLE);
                mTvError.setVisibility(View.GONE);
                mErrorEnabled = false;
            }
        } else {
            mTvSuccess.setVisibility(View.GONE);
        }
    }


    public void setSuccessEnabled(boolean enabled) {
        if (mSuccessEnabled != enabled) {
            /*if (mErrorView != null) {
                ViewCompat.animate(mErrorView).cancel();
            }*/
            mSuccessEnabled = enabled;
            checkSuccessVisible();
            setUISuccess();
        }
    }

    public void setHelperEnabled(boolean enabled) {
        if (mHelperEnabled != enabled) {
            /*if (mErrorView != null) {
                ViewCompat.animate(mErrorView).cancel();
            }*/
            mHelperEnabled = enabled;
            setUIHelper();
        }
    }

    public void setErrorTextAppearance(@StyleRes int resId) {
        mErrorTextAppearance = resId;
        TextViewCompat.setTextAppearance(mTvError, resId);
    }

    public void setHelperTextAppearance(@StyleRes int resId) {
        mHelperTextAppearance = resId;
        TextViewCompat.setTextAppearance(mTvHelper, resId);
    }

    public void setSuccessTextAppearance(@StyleRes int resId) {
        mSuccessTextAppearance = resId;
        TextViewCompat.setTextAppearance(mTvSuccess, resId);
    }

    public void setError(@Nullable final CharSequence error) {
        // Only animate if we're enabled, laid out, and we have a different error message
        setError(error, ViewCompat.isLaidOut(this) && isEnabled()
                && !TextUtils.equals(mTvError.getText(), error));
    }

    private void setError(@Nullable final CharSequence error, final boolean animate) {
        mErrorText = error;

        if (!mErrorEnabled) {
            if (TextUtils.isEmpty(error)) {
                if (mTvError.getVisibility() == VISIBLE) {
                    mTvError.setVisibility(GONE);
                }
                // If error isn't enabled, and the error is empty, just return
                return;
            }
            // Else, we'll assume that they want to enable the error functionality
            setErrorEnabled(true);
        }

        if (!TextUtils.isEmpty(error)) {
            mTvError.setText(error);
            mTvError.setVisibility(VISIBLE);
        } else { // empty error
            if (mTvError.getVisibility() == VISIBLE) {
                mTvError.setText(error);
                mTvError.setVisibility(GONE);
            }
        }

        updateEditTextBackground();
        updateLabelState(animate);
    }

    public void setSuccess(@Nullable final CharSequence success) {
        // Only animate if we're enabled, laid out, and we have a different error message
        setSuccess(success, ViewCompat.isLaidOut(this) && isEnabled()
                && !TextUtils.equals(mTvSuccess.getText(), success));
    }

    public void hideSuccessError(){
        if (!mErrorEnabled && mSuccessEnabled) {
            return;
        }
        mTvSuccess.setText(null);
        mTvError.setText(null);
        mTvSuccess.setVisibility(View.VISIBLE);
        mSuccessEnabled = true;
        mTvError.setVisibility(View.GONE);
        mErrorEnabled = false;
        updateEditTextBackground();
    }

    public void disableSuccessError(){
        if (!mSuccessEnabled && !mErrorEnabled) {
            return;
        }
        mSuccessEnabled = false;
        mErrorEnabled = false;
        mTvSuccess.setVisibility(View.GONE);
        mTvError.setVisibility(View.GONE);
        updateEditTextBackground();
    }

    private void setSuccess(@Nullable final CharSequence successText, final boolean animate) {
        mSuccessText = successText;

        if (!mSuccessEnabled) {
            if (TextUtils.isEmpty(successText)) {
                if (mTvSuccess.getVisibility() == VISIBLE) {
                    mTvSuccess.setVisibility(GONE);
                }
                // If success isn't enabled, and the error is empty, just return
                return;
            }
            // Else, we'll assume that they want to enable the success functionality
            setSuccessEnabled(true);
        }

        if (!TextUtils.isEmpty(successText)) {
            mTvSuccess.setText(successText);
            mTvSuccess.setVisibility(VISIBLE);
        } else { // empty error
            if (mTvSuccess.getVisibility() == VISIBLE) {
                mTvSuccess.setText(successText);
                mTvSuccess.setVisibility(GONE);
            }
        }
        updateEditTextBackground();
        updateLabelState(animate);
    }

    public boolean isSuccessShown(){
        return mSuccessEnabled;
    }

    public void setHelper(@Nullable final CharSequence helper) {
        mHelperText = helper;

        if (!mHelperEnabled) {
            if (TextUtils.isEmpty(helper)) {
                if (mTvHelper.getVisibility() == VISIBLE) {
                    mTvHelper.setVisibility(GONE);
                }
                // If error isn't enabled, and the error is empty, just return
                return;
            }
            // Else, we'll assume that they want to enable the error functionality
            setHelperEnabled(true);
        }

        if (!TextUtils.isEmpty(helper)) {
            mTvHelper.setText(helper);
            mTvHelper.setVisibility(VISIBLE);
        } else { // empty helper
            if (mTvHelper.getVisibility() == VISIBLE) {
                mTvHelper.setText(helper);
                mTvHelper.setVisibility(GONE);
            }
        }
    }

    public void setUIPasswordToogle(){
        if (!mPasswordToggleEnabled && mPasswordToggledVisible && mEditText != null) {
            // If the toggle is no longer enabled, but we remove the PasswordTransformation
            // to make the password visible, add it back
            mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        // Reset the visibility tracking flag
        mPasswordToggledVisible = false;

        updatePasswordToggleView();
    }

    public void setPasswordVisibilityToggleEnabled(final boolean enabled) {
        if (mPasswordToggleEnabled != enabled) {
            mPasswordToggleEnabled = enabled;
            setUIPasswordToogle();
        }
    }

    @SuppressWarnings("RestrictedApi")
    private void updatePasswordToggleView() {
        if (mEditText == null) {
            // If there is no EditText, there is nothing to update
            return;
        }

        if (shouldShowPasswordIcon()) {
            if (mPasswordToggleView == null) {
                mPasswordToggleView = (CheckableImageButton) LayoutInflater.from(getContext())
                        .inflate(android.support.design.R.layout.design_text_input_password_icon,
                                mFrameLayout, false);
                mPasswordToggleView.setImageDrawable(mPasswordToggleDrawable);
                mPasswordToggleView.setContentDescription(mPasswordToggleContentDesc);
                mFrameLayout.addView(mPasswordToggleView);

                mPasswordToggleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordVisibilityToggleRequested();
                    }
                });
            }

            if (mEditText != null && ViewCompat.getMinimumHeight(mEditText) <= 0) {
                // We should make sure that the EditText has the same min-height as the password
                // toggle view. This ensure focus works properly, and there is no visual jump
                // if the password toggle is enabled/disabled.
                mEditText.setMinimumHeight(ViewCompat.getMinimumHeight(mPasswordToggleView));
            }

            mPasswordToggleView.setVisibility(VISIBLE);
            mPasswordToggleView.setChecked(mPasswordToggledVisible);

            // We need to add a dummy drawable as the end compound drawable so that the text is
            // indented and doesn't display below the toggle view
            if (mPasswordToggleDummyDrawable == null) {
                mPasswordToggleDummyDrawable = new ColorDrawable();
            }
            mPasswordToggleView.post(new Runnable() {
                @Override
                public void run() {
                    mPasswordToggleDummyDrawable.setBounds(0, 0, mPasswordToggleView.getMeasuredWidth(), 1);

                    final Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(mEditText);
                    // Store the user defined end compound drawable so that we can restore it later
                    if (compounds[2] != mPasswordToggleDummyDrawable) {
                        mOriginalEditTextEndDrawable = compounds[2];
                    }
                    TextViewCompat.setCompoundDrawablesRelative(mEditText, compounds[0], compounds[1],
                            mPasswordToggleDummyDrawable, compounds[3]);

                    // Copy over the EditText's padding so that we match
                    mPasswordToggleView.setPadding(mEditText.getPaddingLeft(),
                            mEditText.getPaddingTop(), mEditText.getPaddingRight(),
                            mEditText.getPaddingBottom());
                }
            });
        } else {
            if (mPasswordToggleView != null && mPasswordToggleView.getVisibility() == VISIBLE) {
                mPasswordToggleView.setVisibility(View.GONE);
            }

            if (mPasswordToggleDummyDrawable != null) {
                // Make sure that we remove the dummy end compound drawable if it exists, and then
                // clear it
                final Drawable[] compounds = TextViewCompat.getCompoundDrawablesRelative(mEditText);
                if (compounds[2] == mPasswordToggleDummyDrawable) {
                    TextViewCompat.setCompoundDrawablesRelative(mEditText, compounds[0],
                            compounds[1], mOriginalEditTextEndDrawable, compounds[3]);
                    mPasswordToggleDummyDrawable = null;
                }
            }
        }
    }

    @SuppressWarnings("RestrictedApi")
    void passwordVisibilityToggleRequested() {
        if (mPasswordToggleEnabled) {
            // Store the current cursor position
            final int selection = mEditText.getSelectionEnd();

            if (hasPasswordTransformation()) {
                mEditText.setTransformationMethod(null);
                mPasswordToggledVisible = true;
            } else {
                mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mPasswordToggledVisible = false;
            }

            mPasswordToggleView.setChecked(mPasswordToggledVisible);

            // And restore the cursor position
            mEditText.setSelection(selection);
        }
    }

    public void setPasswordVisibilityToggleTintList(@Nullable ColorStateList tintList) {
        mPasswordToggleTintList = tintList;
        mHasPasswordToggleTintList = true;
        applyPasswordToggleTint();
    }

    public void setPasswordVisibilityToggleTintMode(@Nullable PorterDuff.Mode mode) {
        mPasswordToggleTintMode = mode;
        mHasPasswordToggleTintMode = true;
        applyPasswordToggleTint();
    }

    private void applyPasswordToggleTint() {
        if (mPasswordToggleDrawable != null
                && (mHasPasswordToggleTintList || mHasPasswordToggleTintMode)) {
            mPasswordToggleDrawable = DrawableCompat.wrap(mPasswordToggleDrawable).mutate();

            if (mHasPasswordToggleTintList) {
                DrawableCompat.setTintList(mPasswordToggleDrawable, mPasswordToggleTintList);
            }
            if (mHasPasswordToggleTintMode) {
                DrawableCompat.setTintMode(mPasswordToggleDrawable, mPasswordToggleTintMode);
            }

            if (mPasswordToggleView != null
                    && mPasswordToggleView.getDrawable() != mPasswordToggleDrawable) {
                mPasswordToggleView.setImageDrawable(mPasswordToggleDrawable);
            }
        }
    }

    private boolean shouldShowPasswordIcon() {
        return mPasswordToggleEnabled && (hasPasswordTransformation() || mPasswordToggledVisible);
    }

    private boolean hasPasswordTransformation() {
        return mEditText != null
                && mEditText.getTransformationMethod() instanceof PasswordTransformationMethod;
    }

    public void setCounterEnabled(boolean counterEnabled) {
        if (mCounterEnabled != counterEnabled) {
            mCounterEnabled = counterEnabled;
            setUICounter();
        }
    }

    public boolean isCounterEnabled() {
        return mCounterEnabled;
    }

    public int getCounterMaxLength() {
        return mCounterMaxLength;
    }

    public void setCounterMaxLength(int maxLength) {
        if (mCounterMaxLength != maxLength) {
            if (maxLength > 0) {
                mCounterMaxLength = maxLength;
            } else {
                mCounterMaxLength = INVALID_MAX_LENGTH;
            }
            if (mCounterEnabled) {
                updateCounter();
            }
        }
    }

    private void updateCounter() {
        int length = 0;
        if (mEditText != null && !TextUtils.isEmpty(mEditText.getText())) {
            length = mEditText.getText().length();
        }
        boolean wasCounterOverflowed = mCounterOverflowed;
        int currentLength = length > mPrefixLength ? length - mPrefixLength : length;
        if (mCounterMaxLength == INVALID_MAX_LENGTH) {
            mTvCounter.setText(String.valueOf(currentLength - mPrefixLength));
            mCounterOverflowed = false;
        } else {
            mCounterOverflowed = length > mCounterMaxLength;
            if (wasCounterOverflowed != mCounterOverflowed) {
                TextViewCompat.setTextAppearance(mTvCounter, mCounterOverflowed
                        ? mCounterOverflowTextAppearance : mCounterTextAppearance);
            }
            mTvCounter.setText(String.format(Locale.US, "%1$d / %2$d",
                    currentLength, mCounterMaxLength - mPrefixLength));
        }
        if (mEditText != null && wasCounterOverflowed != mCounterOverflowed) {
            updateLabelState(false);
            updateEditTextBackground();
        }
    }

    private Typeface readFontFamilyTypeface(int resId) {
        final TypedArray a = getContext().obtainStyledAttributes(resId,
                new int[]{android.R.attr.fontFamily});
        try {
            final String family = a.getString(0);
            if (family != null) {
                return Typeface.create(family, Typeface.NORMAL);
            }
        } finally {
            a.recycle();
        }
        return null;
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            // Make sure that the EditText is vertically at the bottom, so that it sits on the
            // EditText's underline
//            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(params);
//            flp.gravity = Gravity.CENTER_VERTICAL | (flp.gravity & ~Gravity.VERTICAL_GRAVITY_MASK);
            mFrameLayout.addView(child);

            // Now use the EditText's LayoutParams as our own and update them to make enough space
            // for the label
//            mFrameLayout.setLayoutParams(params);
            setEditText((EditText) child);

            setUIPasswordToogle();
            applyPasswordToggleTint();
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private void setEditText(final EditText editText) {
        // If we already have an EditText, throw an exception
        if (mEditText != null) {
            throw new IllegalArgumentException("We already have an EditText, can only have one");
        }

        mEditText = editText;

        // Add a TextWatcher so that we know when the text input has changed
        mEditText.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateLabelState(!mRestoringSavedState);
                if (mCounterEnabled) {
                    updateCounter();
                }
            }
        });

        // Use the EditText's hint colors if we don't have one set
        if (mDefaultHintTextColor == null) {
            mDefaultHintTextColor = mEditText.getHintTextColors();
        }

        if (mFocusedHintTextColor == null) {
            mFocusedHintTextColor = mEditText.getHintTextColors();
        }

        // If we do not have a valid hint, try and retrieve it from the EditText, if enabled
        if (mHintEnabled && TextUtils.isEmpty(mHint)) {
            mHint = mEditText.getHint();
            mTvLabel.setText(mHint);
        }
//
//        if (mCounterView != null) {
//            setUICounter(mEditText.getText().length());
//        }
//
//        if (mIndicatorArea != null) {
//            adjustIndicatorPadding();
//        }
//
//        updatePasswordToggleView();
//
//        // Update the label visibility with no animation, but force a state change
        updateLabelState(false, true);
    }

    @Override
    protected void drawableStateChanged() {
        if (mInDrawableStateChanged) {
            // Some of the calls below will update the drawable state of child views. Since we're
            // using addStatesFromChildren we can get into infinite recursion, hence we'll just
            // exit in this instance
            return;
        }

        mInDrawableStateChanged = true;

        super.drawableStateChanged();

        // Drawable state has changed so see if we need to update the label
        updateLabelState(ViewCompat.isLaidOut(this) && isEnabled());

        updateEditTextBackground();
        invalidate();
        mInDrawableStateChanged = false;
    }

    @SuppressWarnings("RestrictedApi")
    private void updateEditTextBackground() {
        if (mEditText == null) {
            return;
        }

        Drawable editTextBackground = mEditText.getBackground();
        if (editTextBackground == null) {
            return;
        }

        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(editTextBackground)) {
            editTextBackground = editTextBackground.mutate();
        }

        final boolean isErrorShowing = !TextUtils.isEmpty(getError());

        if (isErrorShowing) {
            // Set a color filter of the error color
            editTextBackground.setColorFilter(
                    AppCompatDrawableManager.getPorterDuffColorFilter(
                            mTvError.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
        } else if (mCounterOverflowed) {
            // Set a color filter of the counter color
            editTextBackground.setColorFilter(
                    AppCompatDrawableManager.getPorterDuffColorFilter(
                            mTvCounter.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
        } else {
            // Else reset the color filter and refresh the drawable state so that the
            // normal tint is used
            DrawableCompat.clearColorFilter(editTextBackground);
            mEditText.refreshDrawableState();
        }
    }


    void updateLabelState(boolean animate) {
        updateLabelState(animate, false);
    }

    void updateLabelState(final boolean animate, final boolean force) {
        final boolean isEnabled = isEnabled();
        if (mEditText == null) {
            return;
        }
        final boolean isFocused = mEditText.isFocused();
        if (!isEnabled) {
            mTvLabel.setTextColor(mDisabledHintTextColor);
        } else if (isFocused) {
            mTvLabel.setTextColor(mFocusedHintTextColor);
        } else {
            mTvLabel.setTextColor(mDefaultHintTextColor);
        }
    }

    private PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode){
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            default:
                return defaultMode;
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int v : array) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public CharSequence getError() {
        return mErrorEnabled ? mErrorText : null;
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        mRestoringSavedState = true;
        super.dispatchRestoreInstanceState(container);
        mRestoringSavedState = false;
    }


}