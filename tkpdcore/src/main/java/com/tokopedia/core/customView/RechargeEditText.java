package com.tokopedia.core.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author kulomady on 7/12/2016.
 */
public class RechargeEditText extends LinearLayout {

    private static final String TAG = RechargeEditText.class.getSimpleName();

    private RechargeEditTextListener rechargeEditTextListener;
    private OnButtonPickerListener buttonPickerListener;

    @BindView(R2.id.pulsa_autocompleteview)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R2.id.btnClear)
    Button btnClear;
    @BindView(R2.id.imgOperator)
    ImageView imgOperator;
    @BindView(R2.id.btnPhoneBook)
    Button btnContactPicker;
    @BindView(R2.id.pulsa_frameLayout)
    FrameLayout pulsaFramelayout;

    public RechargeEditText(Context context) {
        super(context);
        init();
    }
    public RechargeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RechargeEditText(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        init();
    }

    public FrameLayout getPulsaFramelayout() {
        return pulsaFramelayout;
    }

    private void init() {
        inflate(getContext(), R.layout.recharge_edittext_view, this);
        ButterKnife.bind(this);

        initBackgroundContactButtonAndClearButton();
        setImgOperatorInvisible();
        setBtnClearInvisible();

        actionView();
    }

    private void initBackgroundContactButtonAndClearButton() {
        Glide.with(getContext()).load(R.drawable.ic_clear_widget).asBitmap().into(
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawableClear = new BitmapDrawable(getContext().getResources(), resource);
                        btnClear.setBackgroundDrawable(drawableClear);
                    }
                });

        Glide.with(getContext()).load(R.drawable.ic_phonebook_widget).asBitmap().into(
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {

                        Drawable drawablePhoneBook = new BitmapDrawable(getContext().getResources(), resource);
                        btnContactPicker.setBackgroundDrawable(drawablePhoneBook);
                    }
                }
        );
    }

    private void actionView() {
        this.autoCompleteTextView.setOnFocusChangeListener(getFocusChangeListener());
        this.autoCompleteTextView.addTextChangedListener(getTextChangedListener());
        this.btnClear.setOnClickListener(getClickClearButtonListener());
        this.btnContactPicker.setOnClickListener(getClickPhonebookListener());
    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (autoCompleteTextView.getText().length() > 0) {
                        setBtnClearVisible();
                    } else {
                        setBtnClearInvisible();
                    }
                }
            }
        };
    }

    private TextWatcher getTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rechargeEditTextListener != null && s.length()>0) {
                    rechargeEditTextListener.onRechargeTextChanged(s, start, before, count);
                }
                if (s.length() > 0) {
                    //this.btnContactPicker.setVisibility(GONE);
                    setBtnClearVisible();
                } else {
                    setBtnClearInvisible();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private OnClickListener getClickClearButtonListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
                setImgOperatorInvisible();
                rechargeEditTextListener.onRechargeTextClear();
            }
        };
    }

    private OnClickListener getClickPhonebookListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonPickerListener != null) {
                    buttonPickerListener.onButtonContactClicked();
                }
            }
        };
    }

    public Button getBtnContactPicker() {
        return btnContactPicker;
    }

    public String getText() {
        return this.autoCompleteTextView.getText().toString();
    }

    public void setText(String text) {
        this.autoCompleteTextView.setText(text);
    }

    public void setEmptyString() {
        this.autoCompleteTextView.setText("");
    }

    public void setHint(String hint) {
        this.autoCompleteTextView.setHint(hint);
    }

    public void setRechargeEditTextListener(RechargeEditTextListener rechargeEditTextListener) {
        this.rechargeEditTextListener = rechargeEditTextListener;
    }

    public void setButtonPickerListener(OnButtonPickerListener buttonPickerListener) {
        this.buttonPickerListener = buttonPickerListener;
    }

    public void setImgOperatorVisible() {
        this.imgOperator.setVisibility(VISIBLE);
    }

    public void setImgOperatorInvisible() {
        this.imgOperator.setVisibility(GONE);
    }

    public void setBtnClearVisible() {
        this.btnClear.setVisibility(VISIBLE);
    }

    public void setBtnClearInvisible() {
        this.btnClear.setVisibility(GONE);
    }

    public void setDropdownAutoComplete(List<String> displayList) {
        String[] wannaDisplay = new String[displayList.size()];
        wannaDisplay = displayList.toArray(wannaDisplay);
        Log.d(TAG, "setDropdownAutoComplete() called with: "
                + "wanna display = [" + Arrays.toString(wannaDisplay) + "]");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),R.layout.simple_spinner_tv_res, wannaDisplay
        );

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
    }

    public void setImgOperator(String imgUrl) {
        Glide.with(getContext())
                .load(imgUrl)
                .dontAnimate()
                .into(this.imgOperator);
    }

    public interface RechargeEditTextListener {
        void onRechargeTextChanged(final CharSequence s,
                                   final int start,
                                   final int before,
                                   final int count);
        void onRechargeTextClear();
    }

    public interface OnButtonPickerListener {
        void onButtonContactClicked();
    }

    public AutoCompleteTextView getAutoCompleteTextView() {
        return autoCompleteTextView;
    }
}