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

import java.util.Arrays;
import java.util.List;

/**
 * @author kulomady on 7/12/2016.
 */
public class RechargeEditText extends LinearLayout implements
        View.OnFocusChangeListener, TextWatcher, View.OnClickListener {

    private static final String TAG = "RechargeEditText";
    private RechargeEditTextListener rechargeEditTextListener;
    private OnButtonPickerListener buttonPickerListener;
    private AutoCompleteTextView autoCompleteTextView;
    private Button btnClear;
    private ImageView imgOperator;
    private Button btnContactPicker;
    private FrameLayout pulsaFramelayout;

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
        this.autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.pulsa_autocompleteview);
        this.btnClear = (Button) findViewById(R.id.btnClear);
        this.imgOperator = (ImageView) findViewById(R.id.imgOperator);
        this.btnContactPicker = (Button) findViewById(R.id.btnPhoneBook);
        this.pulsaFramelayout = (FrameLayout) findViewById(R.id.pulsa_frameLayout);

        initBackgroundContactButtonAndClearButton();

        this.autoCompleteTextView.setOnFocusChangeListener(this);
        this.autoCompleteTextView.addTextChangedListener(this);
        this.btnClear.setOnClickListener(this);
        this.btnContactPicker.setOnClickListener(this);

        setImgOperatorInvisible();
        setBtnClearInvisible();
    }

    private void initBackgroundContactButtonAndClearButton() {
        Glide.with(getContext()).load(R.drawable.ic_clear).asBitmap().into(
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawableClear = new BitmapDrawable(getContext().getResources(), resource);
                        btnClear.setBackgroundDrawable(drawableClear);
                    }
                });

        Glide.with(getContext()).load(R.drawable.ic_phonebook).asBitmap().into(
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


    @Override
    public void onFocusChange(View view, boolean hasfocus) {

        if (hasfocus) {
            if (this.autoCompleteTextView.getText().length() > 0) setBtnClearVisible();
            else setBtnClearInvisible();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (rechargeEditTextListener != null && s.length()>0) {
            rechargeEditTextListener.onRechargeTextChanged(s, start, before, count);
        }
        if (s.length() > 0) {
//            this.btnContactPicker.setVisibility(GONE);
            setBtnClearVisible();
        } else {
            setBtnClearInvisible();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnClear) {
            this.autoCompleteTextView.setText("");
            setImgOperatorInvisible();
        }
        if (view.getId() == R.id.btnPhoneBook) {
            if (buttonPickerListener != null) {
                buttonPickerListener.onButtonContactClicked();
            }
        }
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
    }

    public interface OnButtonPickerListener {
        void onButtonContactClicked();
    }

    public AutoCompleteTextView getAutoCompleteTextView() {
        return autoCompleteTextView;
    }
}