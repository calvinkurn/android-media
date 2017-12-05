package com.tokopedia.design.text;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nakama on 12/5/17.
 */

public class CopyPromoVoucher extends BaseCustomView {

    private TextView textVoucherCode;
    private View buttonCopy;

    public CopyPromoVoucher(@NonNull Context context) {
        super(context);
        init();
    }

    public CopyPromoVoucher(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CopyPromoVoucher(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_copy_voucher_code, this);
        textVoucherCode = (TextView) view.findViewById(R.id.text_voucher_code);
        buttonCopy = view.findViewById(R.id.text_copy_code);
    }

}
