package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.R;

public class SellerSaldoView extends BaseCustomView {

    private TextView saldoAmtTV;
    private CardView parentView;


    public SellerSaldoView(@NonNull Context context) {
        super(context);
        init();
    }

    public SellerSaldoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SellerSaldoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_seller_saldo, this);
        parentView = view.findViewById(R.id.parent_cardView);
        saldoAmtTV = view.findViewById(R.id.saldo_balance_text_view);
    }

    public void setOnClickDeposit(View.OnClickListener listener) {
        parentView.setOnClickListener(listener);
    }

    public void setBalance(String balance) {
        saldoAmtTV.setText(balance);
    }
}
