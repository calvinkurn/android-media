package com.tokopedia.merchantvoucher.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.tokopedia.merchantvoucher.R;

/**
 * Created by hendry on 02/10/18.
 */
public class MerchantVoucherView extends CustomVoucherView {
    public MerchantVoucherView(@NonNull Context context) {
        super(context);
        init();
    }

    public MerchantVoucherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MerchantVoucherView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setClipToPadding(false);
        cornerRadius = dpToPx(4);
        mScallopRadius = dpToPx(8);
        mScallopRelativePosition = 0.6f;
        mShadowRadius = dpToPx(2);
        mDashWidth = dpToPx(2);
        mDashGap = dpToPx(8);
        mDashColor = getResources().getColor(R.color.colorGray);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_merchant_voucher_view,
                this, true);
    }
}
