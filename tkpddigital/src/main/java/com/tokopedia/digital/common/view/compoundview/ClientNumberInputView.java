package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.tokopedia.digital.product.view.model.ClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.Validation;
import com.tokopedia.digital.widget.view.adapter.AutoCompleteTVAdapter;

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
    private AutoCompleteTVAdapter autoCompleteTVAdapter;
    private ClientNumber clientNumber;

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
        initialTextWatcher();
        setImgOperatorInvisible();
        setBtnClearInvisible();
    }

    private void initialTextWatcher() {

    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    actionListener.onClientNumberHasFocus(((TextView) view).getText().toString());
                    if (autoCompleteTextView.getText().length() > 0) {
                        setBtnClearVisible();
                    } else {
                        setBtnClearInvisible();
                    }
                }
            }
        };
    }

    public void setAdapterAutoCompleteClientNumber(List<OrderClientNumber> numberList) {
        autoCompleteTVAdapter = new AutoCompleteTVAdapter(getContext(), R.layout.item_autocomplete, numberList);
        autoCompleteTextView.setAdapter(autoCompleteTVAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(getItemClickListener());
    }

    public String getText() {
        return this.autoCompleteTextView.getText().toString();
    }

    public void setText(String text) {
        this.autoCompleteTextView.setText(text);
    }

    public void setErrorText(String errorMessage) {
        tvErrorClientNumber.setText(errorMessage);
        tvErrorClientNumber.setVisibility(VISIBLE);
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

    public void setFilterMaxLength(int maximumLength) {
        this.autoCompleteTextView.setFilters(
                new InputFilter[]{new InputFilter.LengthFilter(maximumLength)});
    }

    public void setImgOperator(String imgUrl) {
        Glide.with(getContext()).load(imgUrl).dontAnimate().into(this.imgOperator);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderData(final ClientNumber clientNumber) {
        this.clientNumber = clientNumber;
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

    private AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderClientNumber orderClientNumber = autoCompleteTVAdapter.getItem(position);
                actionListener.onItemAutocompletedSelected(orderClientNumber);
            }
        };
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
                actionListener.onClientNumberCleared();
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
                tvErrorClientNumber.setText("");
                tvErrorClientNumber.setVisibility(GONE);
                if (tempInput.isEmpty()) {
                    actionListener.onClientNumberInputInvalid();
                } else {
                    actionListener.onClientNumberInputValid(tempInput);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public boolean isValidInput(List<String> prefixList) {
        String clientNumberInput = autoCompleteTextView.getText().toString();
        boolean isStartWithPrefix = false;
        for (String string : prefixList) {
            if (clientNumberInput.startsWith(string)) {
                isStartWithPrefix = true;
                break;
            }
        }
        if (!isStartWithPrefix) return false;
        if (clientNumber != null) {
            boolean isValidRegex = false;
            for (Validation validation : clientNumber.getValidation()) {
                if (Pattern.matches(validation.getRegex(), clientNumberInput))
                    isValidRegex = true;
            }
            return isValidRegex;
        } else {
            return true;
        }
    }

    public void resetInputTyped() {
        autoCompleteTextView.setText("");
    }

    public interface ActionListener {
        void onButtonContactPickerClicked();

        void onClientNumberInputValid(String tempClientNumber);

        void onClientNumberInputInvalid();

        void onClientNumberHasFocus(String clientNumber);

        void onClientNumberCleared();

        void onItemAutocompletedSelected(OrderClientNumber orderClientNumber);
    }

}