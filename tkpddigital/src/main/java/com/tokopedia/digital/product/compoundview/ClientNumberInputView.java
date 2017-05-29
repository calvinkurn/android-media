package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tokopedia.digital.product.model.ClientNumber;
import com.tokopedia.digital.product.model.Validation;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/6/17.
 */
public class ClientNumberInputView extends LinearLayout {

    @BindView(R2.id.tv_label_client_number)
    TextView tvLabel;
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
    private Context context;
    private ArrayAdapter<String> adapterAutoComplete;

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

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_holder_client_number_input, this, true);
        ButterKnife.bind(this);
        adapterAutoComplete = new ArrayAdapter<>(
                getContext(), com.tokopedia.core.R.layout.simple_spinner_tv_res
        );
        autoCompleteTextView.setAdapter(adapterAutoComplete);
        autoCompleteTextView.setThreshold(1);
        initialTextWatcher();
        initBackgroundContactButtonAndClearButton();
        setImgOperatorInvisible();
        setBtnClearInvisible();
    }

    private void initialTextWatcher() {

    }

    @SuppressWarnings("deprecation")
    private void initBackgroundContactButtonAndClearButton() {
        Glide.with(getContext()).load(com.tokopedia.core.R.drawable.ic_clear_widget)
                .asBitmap().into(
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawableClear = new BitmapDrawable(
                                getContext().getResources(), resource
                        );
                        btnClear.setBackgroundDrawable(drawableClear);
                    }
                });

        Glide.with(getContext()).load(com.tokopedia.core.R.drawable.ic_phonebook_widget)
                .asBitmap().into(
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {

                        Drawable drawablePhoneBook = new BitmapDrawable(
                                getContext().getResources(), resource
                        );
                        btnContactPicker.setBackgroundDrawable(drawablePhoneBook);
                    }
                }
        );
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

    public void setAdapterAutoCompleteClientNumber(List<String> recentClientNumberList) {
        adapterAutoComplete.clear();
        adapterAutoComplete.notifyDataSetChanged();
        adapterAutoComplete.addAll(recentClientNumberList);
        adapterAutoComplete.notifyDataSetChanged();
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

    public void setImgOperatorInvisible() {
        this.imgOperator.setVisibility(GONE);
    }

    public void setBtnClearVisible() {
        this.btnClear.setVisibility(VISIBLE);
    }

    public void setBtnClearInvisible() {
        this.btnClear.setVisibility(GONE);
    }

    public void enableImageOperator(String imageUrl) {
        imgOperator.setVisibility(VISIBLE);
        Glide.with(context).load(imageUrl).dontAnimate().into(this.imgOperator);
    }

    public void disableImageOperator() {
        imgOperator.setVisibility(GONE);
        Glide.with(context).load("").dontAnimate().into(this.imgOperator);
    }

    public void setInputTypeNumber() {
        this.autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.autoCompleteTextView.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
    }

    public void setInputTypeText() {
        this.autoCompleteTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    public void setImgOperator(String imgUrl) {
        Glide.with(getContext()).load(imgUrl).dontAnimate().into(this.imgOperator);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderData(final ClientNumber clientNumber) {
        if (!TextUtils.isEmpty(clientNumber.getText())) {
            tvLabel.setVisibility(VISIBLE);
            tvLabel.setText(clientNumber.getText());
        } else {
            tvLabel.setVisibility(GONE);
        }
        tvLabel.setText(clientNumber.getText());
        autoCompleteTextView.setHint(clientNumber.getPlaceholder());
        setupLayoutParamAndInputType(clientNumber);
        autoCompleteTextView.setOnFocusChangeListener(getFocusChangeListener());
        final TextWatcher textWatcher = getTextWatcherInput(clientNumber);
        autoCompleteTextView.removeTextChangedListener(textWatcher);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        this.btnClear.setOnClickListener(getButtonClearClickListener());
        this.btnContactPicker.setOnClickListener(getButtonContactPickerClickListener());
    }

    private void setupLayoutParamAndInputType(ClientNumber clientNumber) {
        LayoutParams layoutParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_TEL)) {
            btnContactPicker.setVisibility(View.VISIBLE);
            layoutParams.weight = 0.92f;
        } else {
            btnContactPicker.setVisibility(View.GONE);
            layoutParams.weight = 1;
        }
        pulsaFramelayout.setLayoutParams(layoutParams);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_TEL)
                || clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_NUMERIC)) {
            setInputTypeNumber();
        } else {
            setInputTypeText();
        }
    }

    @NonNull
    private OnClickListener getButtonContactPickerClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onButtonContactPickerClicked();
            }
        };
    }

    @NonNull
    private OnClickListener getButtonClearClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        };
    }

    @NonNull
    private TextWatcher getTextWatcherInput(final ClientNumber clientNumber) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String tempInput = charSequence.toString();
                btnClear.setVisibility(tempInput.length() > 0 ? VISIBLE : GONE);
                if (tempInput.isEmpty()) actionListener.onClientNumberInputInvalid();
                else for (Validation validation : clientNumber.getValidation()) {
                    if (!Pattern.matches(validation.getRegex(), tempInput)) {
                        actionListener.onClientNumberInputInvalid();
                        if (tempInput.isEmpty()) {
                            tvErrorClientNumber.setText("");
                            tvErrorClientNumber.setVisibility(GONE);
                        } else {
                            tvErrorClientNumber.setText(validation.getError());
                            tvErrorClientNumber.setVisibility(VISIBLE);
                        }
                        break;
                    } else {
                        tvErrorClientNumber.setText("");
                        tvErrorClientNumber.setVisibility(GONE);
                        actionListener.onClientNumberInputValid(tempInput);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void resetInputTyped() {
        autoCompleteTextView.setText("");
    }

    public interface ActionListener {
        void onButtonContactPickerClicked();

        void onClientNumberInputValid(String tempClientNumber);

        void onClientNumberInputInvalid();
    }

}