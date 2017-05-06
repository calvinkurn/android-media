package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/6/17.
 */
public class ClientNumberInputView extends LinearLayout {

    @BindView(R2.id.ac_client_number)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R2.id.btn_clear_client_number)
    Button btnClear;
    @BindView(R2.id.iv_pic_operator)
    ImageView imgOperator;
    @BindView(R2.id.btn_contact_picker)
    Button btnContactPicker;
    @BindView(R2.id.fl_holder_input_client_number)
    RelativeLayout pulsaFramelayout;
    @BindView(R2.id.tv_error_client_number)
    TextView tvErrorClientNumber;

    private ActionListener actionListener;

    public ClientNumberInputView(Context context) {
        super(context);
        init(context);
    }

    public ClientNumberInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClientNumberInputView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        init(context);
    }


    public RelativeLayout getPulsaFramelayout() {
        return pulsaFramelayout;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_holder_client_number_input, this, true);
        ButterKnife.bind(this);

        initBackgroundContactButtonAndClearButton();
        setImgOperatorInvisible();
        setBtnClearInvisible();

        actionView();
    }

    private void initBackgroundContactButtonAndClearButton() {
        Glide.with(getContext()).load(com.tokopedia.core.R.drawable.ic_clear_widget).asBitmap().into(
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawableClear = new BitmapDrawable(getContext().getResources(), resource);
                        btnClear.setBackgroundDrawable(drawableClear);
                    }
                });

        Glide.with(getContext()).load(com.tokopedia.core.R.drawable.ic_phonebook_widget).asBitmap().into(
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
                if (actionListener != null && s.length() > 0) {
                    actionListener.onClientNumberTextChanged(s, start, before, count);
                }
                if (s.length() > 0) {
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
                actionListener.onClientNumberClear();
            }
        };
    }

    private OnClickListener getClickPhonebookListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onButtonContactPickerClicked();
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

    public void setInputTypeNumber() {
        this.autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void setInputTypeText() {
        this.autoCompleteTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    public void setDropdownAutoComplete(List<String> displayList) {
        String[] wannaDisplay = new String[displayList.size()];
        wannaDisplay = displayList.toArray(wannaDisplay);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), com.tokopedia.core.R.layout.simple_spinner_tv_res, wannaDisplay
        );
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
    }

    public void setImgOperator(String imgUrl) {
        Glide.with(getContext()).load(imgUrl).dontAnimate().into(this.imgOperator);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void enableErrorClientNumber(String errorMessage) {
        tvErrorClientNumber.setText(errorMessage);
        tvErrorClientNumber.setVisibility(VISIBLE);
    }

    public void disableErrorClientNumber() {
        tvErrorClientNumber.setText("");
        tvErrorClientNumber.setVisibility(VISIBLE);
    }

    public interface ActionListener {
        void onButtonContactPickerClicked();

        void onClientNumberTextChanged(final CharSequence charSequence,
                                       final int start,
                                       final int before,
                                       final int count);

        void onClientNumberClear();
    }

    public AutoCompleteTextView getAutoCompleteTextView() {
        return autoCompleteTextView;
    }
}