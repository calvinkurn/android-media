package com.tokopedia.home.account.presentation.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.circular_progress_bar);
    }

    public void setName(String name) {
        textUsername.setText(name);
    }

    public void setAvatarImageUrl(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), imageProfile, imageUrl, R.drawable.ic_big_notif_customerapp);
    }

    public void setTokopoint(String tokopoint) {
        textTokopointAmount.setText(tokopoint);
    }

    public void setVoucher(String coupons) {
        textVoucherAmount.setText(coupons);
    }

    public void setProfileCompletion(int progress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar,
                "progress", 0, progress);
        animation.setDuration(2000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        if(progress > 0 && progress < 100) {
            textProfileCompletion.setText(String.format(getContext().getString(R.string.label_profile_completion), progress));
        }
    }

    public void setOnClickName(View.OnClickListener listener) {
        textUsername.setOnClickListener(listener);
    }

    public void setOnClickAvatar(View.OnClickListener listener) {
        imageProfile.setOnClickListener(listener);
    }

    public void setOnClickProfileCompletion(View.OnClickListener listener) {
        textProfileCompletion.setOnClickListener(listener);
    }

    public void setOnClickTokoPoint(View.OnClickListener listener) {
        textTokopointAmount.setOnClickListener(listener);
    }

    public void setOnClickVoucher(View.OnClickListener listener) {
        textVoucherAmount.setOnClickListener(listener);
    }
}
