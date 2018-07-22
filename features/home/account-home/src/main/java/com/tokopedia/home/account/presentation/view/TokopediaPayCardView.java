package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/18/18.
 */
public class TokopediaPayCardView extends BaseCustomView {
    private TextView actionText;
    private TextView textAmountLeft;
    private TextView textDescLeft;
    private TextView textAmountRight;
    private TextView textDesctRight;


    public TokopediaPayCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public TokopediaPayCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TokopediaPayCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_tokopedia_pay_card, this);
        actionText = view.findViewById(R.id.text_action);
        textAmountLeft = view.findViewById(R.id.text_amount_left);
        textDescLeft = view.findViewById(R.id.text_desc_left);
        textAmountRight = view.findViewById(R.id.text_amount_right);
        textDesctRight = view.findViewById(R.id.text_desc_right);
    }

    public void setActionText(@NonNull String text) {
        this.actionText.setText(text);
    }

    public void setTextAmountLeft(@NonNull String text) {
        this.textAmountLeft.setText(text);
    }

    public void setTextDescLeft(@NonNull String text) {
        this.textDescLeft.setText(text);
    }

    public void setTextAmountRight(@NonNull String text) {
        this.textAmountRight.setText(text);
    }

    public void setTextDesctRight(@NonNull String text) {
        textDesctRight.setText(text);
    }

    public void setActionTextClickListener(View.OnClickListener listener) {
        actionText.setOnClickListener(listener);
    }
}
