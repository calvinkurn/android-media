package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.home.account.R;

public class SellerSaldoView extends BaseCustomView {

    private LabelView labelViewDeposit;


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
        labelViewDeposit = view.findViewById(R.id.label_view_seller_saldo);
    }

    public void setOnClickDeposit(View.OnClickListener listener) {
        labelViewDeposit.setOnClickListener(listener);
    }

    public void setBalance(String balance) {
        labelViewDeposit.setContent(balance);
    }
}
