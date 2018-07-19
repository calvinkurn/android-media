package com.tokopedia.home.account.presentation.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardView extends BaseCustomView {

    private ImageView imageProfile;
    private TextView textUsername;
    private TextView textProfileCompletion;
    private TextView textTokopointAmount;
    private TextView textVoucherAmount;

    public BuyerCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public BuyerCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BuyerCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_buyer_card, this);
        imageProfile = view.findViewById(R.id.image_profile);
        textUsername = view.findViewById(R.id.text_username);
        textProfileCompletion = view.findViewById(R.id.text_profile_completion);
        textTokopointAmount = view.findViewById(R.id.text_tokopoint_amount);
        textVoucherAmount = view.findViewById(R.id.text_voucher_amount);
    }

    public void setName(String name) {
        textUsername.setText(name);
    }

    public void setAvatarImageUrl(String imageUrl) {
        ImageHandler.loadImage(getContext(), imageProfile, imageUrl, R.drawable.ic_big_notif_customerapp);
    }

    public void setTokopoint(String tokopoint) {
        textTokopointAmount.setText(tokopoint);
    }

    public void setVoucher(String coupons) {
        textVoucherAmount.setText(coupons);
    }

    public void setProgress(int progress) {
        textProfileCompletion.setText(Integer.toString(progress));
    }
}
