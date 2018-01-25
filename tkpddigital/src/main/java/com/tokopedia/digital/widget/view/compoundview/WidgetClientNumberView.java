package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.widget.view.adapter.AutoCompleteTVAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 7/18/17.
 */
@Deprecated
public class WidgetClientNumberView extends LinearLayout {

    @BindView(R2.id.client_number_label)
    TextView clientNumberLabel;
    @BindView(R2.id.pulsa_frame_layout)
    FrameLayout pulsaFrameLayout;
    @BindView(R2.id.pulsa_autocomplete_view)
    AutoCompleteTextView pulsaAutocompleteView;
    @BindView(R2.id.img_operator)
    ImageView imgOperator;
    @BindView(R2.id.btn_clear)
    Button btnClear;
    @BindView(R2.id.btn_phone_book)
    Button btnPhoneBook;

    private RechargeEditTextListener rechargeEditTextListener;
    private OnButtonPickerListener buttonPickerListener;

    private AutoCompleteTVAdapter adapter;

    private int oldCount;

    public WidgetClientNumberView(Context context) {
        super(context);
        init();
    }

    public WidgetClientNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetClientNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_widget_client_number, this, true);
        ButterKnife.bind(this);

        initContactAndClearButtonBg();
        actionView();
    }

    @SuppressWarnings("deprecation")
    private void initContactAndClearButtonBg() {
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
                        btnPhoneBook.setBackgroundDrawable(drawablePhoneBook);
                    }
                }
        );
    }

    public void setClientNumberLabel(String label) {
        clientNumberLabel.setText(label);
    }

    public AutoCompleteTextView getAutocompleteView() {
        return pulsaAutocompleteView;
    }

    public String getText() {
        return this.pulsaAutocompleteView.getText().toString();
    }

    public void setText(String text) {
        this.pulsaAutocompleteView.setText(text);
    }

    public void setEmptyString() {
        this.pulsaAutocompleteView.setText("");
    }

    public void setHint(String hint) {
        this.pulsaAutocompleteView.setHint(hint);
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

    public void setInputTypeNumber() {
        this.pulsaAutocompleteView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void setInputTypeText() {
        this.pulsaAutocompleteView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    public void setImgOperator(String imgUrl) {
        Glide.with(getContext())
                .load(imgUrl)
                .dontAnimate()
                .into(this.imgOperator);
    }

    public void setDropdownAutoComplete(List<OrderClientNumber> numberList) {
        adapter = new AutoCompleteTVAdapter(getContext(),
                R.layout.item_autocomplete_widget, numberList);

        this.pulsaAutocompleteView.setAdapter(adapter);
        this.pulsaAutocompleteView.setThreshold(1);
    }

    private void actionView() {
        this.pulsaAutocompleteView.setAdapter(adapter);
        this.pulsaAutocompleteView.setThreshold(1);

        this.pulsaAutocompleteView.setOnItemClickListener(getItemClickListener());
        this.pulsaAutocompleteView.setOnFocusChangeListener(getFocusChangeListener());
        this.pulsaAutocompleteView.addTextChangedListener(getTextChangedListener());

        this.btnClear.setOnClickListener(getClickClearButtonListener());
        this.btnPhoneBook.setOnClickListener(getClickPhonebookListener());
    }

    private AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderClientNumber orderClientNumber = adapter.getItem(position);
                rechargeEditTextListener.onItemAutocompletedSelected(orderClientNumber);
            }
        };
    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (pulsaAutocompleteView.getText().length() > 0) {
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
                if (rechargeEditTextListener != null) {
                    if (s.length() > 0) {
                        rechargeEditTextListener.onRechargeTextChanged(s, start, before, count);
                    } else {
                        rechargeEditTextListener.onRechargeTextClear();
                    }
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
                setEmptyString();
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

    public void setVisibilityPhoneBook(boolean isUsePhoneBook) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (pulsaAutocompleteView != null) {
            if (isUsePhoneBook) {
                btnPhoneBook.setVisibility(View.VISIBLE);
                layoutParams.weight = 0.92f;
            } else {
                btnPhoneBook.setVisibility(View.GONE);
                layoutParams.weight = 1;
            }
        }
        pulsaFrameLayout.setLayoutParams(layoutParams);
    }

    public void setInputType(boolean allowAlphaNumeric) {
        if (allowAlphaNumeric) {
            setInputTypeText();
        } else {
            setInputTypeNumber();
        }
    }

    public void setFilterMaxLength(int maximumLength) {
        pulsaAutocompleteView.setFilters(
                new InputFilter[]{new InputFilter.LengthFilter(maximumLength)});
    }

    public interface RechargeEditTextListener {
        void onRechargeTextChanged(final CharSequence s, int start, int before, int count);

        void onRechargeTextClear();

        void onItemAutocompletedSelected(OrderClientNumber orderClientNumber);
    }

    public interface OnButtonPickerListener {
        void onButtonContactClicked();
    }
}