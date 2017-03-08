package com.tokopedia.digital.cart.compoundview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nabilla Sabbaha on 3/3/2017.
 */

public class InputPriceHolderView extends LinearLayout {

    @BindView(R2.id.input_price_info)
    TextView inputPriceInfo;
    @BindView(R2.id.input_price_edittext)
    EditText inputPriceEdittext;
    @BindView(R2.id.error_input_price)
    TextView errorInputError;

    private Context context;
    private long priceInput = 0;
    private ActionListener actionListener;


    public InputPriceHolderView(Context context) {
        super(context);
        init(context);
    }

    public InputPriceHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputPriceHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_holder_checkout_input_price, this, true);
        ButterKnife.bind(this);
    }

    public void setInputPriceInfo(String totalPayment, long minPayment, long maxPayment) {
        inputPriceEdittext.setText(totalPayment);
        inputPriceEdittext.setCursorVisible(true);
        inputPriceEdittext.addTextChangedListener(getEditTextChanged(minPayment,
                maxPayment));
    }

    private TextWatcher getEditTextChanged(final long minPayment, final long maxPayment) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUserInputValid(s.toString(), minPayment, maxPayment)) {
                    priceInput = Long.parseLong(s.toString());
                    errorInputError.setVisibility(GONE);
                    actionListener.onInputPriceByUserFilled(priceInput);
                } else {
                    errorInputError.setVisibility(VISIBLE);
                }
            }
        };
    }

    private boolean isUserInputValid(String userInput, long minPayment, long maxPayment) {
        long priceInput = 0;
        if (!userInput.equals("")) {
            priceInput = Long.parseLong(userInput.toString());
        }
        return (priceInput > minPayment && priceInput < maxPayment);
    }

    public void bindView(String minPayment, String maxPayment) {
        inputPriceInfo.setText(String.format(context.getString(R.string.user_price_info),
                minPayment, maxPayment));
    }

    public interface ActionListener {
        void onInputPriceByUserFilled(long paymentAmount);
    }
}