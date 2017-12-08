package com.tokopedia.design.voucher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nabillasabbaha on 11/20/17.
 * this custom view is for voucher view in cart
 */

public class VoucherCartHachikoView extends BaseCustomView {

    TextView labelUseVoucher;
    TextView labelPromoCode;
    TextView textviewPromoCode;
    TextView textviewVoucherDetail;
    ImageView buttonCancel;
    RelativeLayout layoutUsedPromo;
    FrameLayout layoutInputPromo;

    private ActionListener actionListener;
    private String voucherCode = "";

    public VoucherCartHachikoView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VoucherCartHachikoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoucherCartHachikoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.widget_voucher_hachiko_cart, this);
        labelUseVoucher = rootView.findViewById(R.id.textview_voucher);
        labelPromoCode = rootView.findViewById(R.id.label_promo_code);
        textviewPromoCode = rootView.findViewById(R.id.textview_promo_code);
        textviewVoucherDetail = rootView.findViewById(R.id.textview_voucher_detail);
        buttonCancel = rootView.findViewById(R.id.button_cancel);
        layoutUsedPromo = rootView.findViewById(R.id.layout_used_promo);
        layoutInputPromo = rootView.findViewById(R.id.layout_input_promo);

        actionVoucher();
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void actionVoucher() {
        labelUseVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onClickUseVoucher();
            }
        });

        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelVoucher();
            }
        });
    }

    private void cancelVoucher() {
        actionListener.trackingCancelledVoucher();

        voucherCode = "";
        layoutUsedPromo.setVisibility(GONE);
        layoutInputPromo.setVisibility(VISIBLE);
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucher(String voucherCode, String voucherMessage) {
        actionListener.trackingSuccessVoucher(voucherCode);

        layoutInputPromo.setVisibility(GONE);

        this.voucherCode = voucherCode;
        labelPromoCode.setText(getContext().getString(R.string.promo_code));
        textviewPromoCode.setText(voucherCode);
        textviewVoucherDetail.setText(voucherMessage);

        layoutUsedPromo.setVisibility(VISIBLE);
    }

    public void setCoupon(String couponTitle, String couponMessage, String couponCode) {
        actionListener.trackingSuccessVoucher(couponTitle);

        layoutInputPromo.setVisibility(GONE);

        voucherCode = couponCode;
        labelPromoCode.setText(getContext().getString(R.string.my_coupon));
        textviewPromoCode.setText(couponTitle);
        textviewVoucherDetail.setText(couponMessage);

        layoutUsedPromo.setVisibility(VISIBLE);
    }

    public interface ActionListener {
        void onClickUseVoucher();

        void trackingSuccessVoucher(String voucherName);

        void trackingCancelledVoucher();
    }
}
