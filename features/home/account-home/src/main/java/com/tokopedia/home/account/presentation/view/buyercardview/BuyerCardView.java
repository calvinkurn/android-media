package com.tokopedia.home.account.presentation.view.buyercardview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
public class BuyerCardView extends BaseCustomView implements BuyerCardContract.View {

    private ImageView imageProfileProgress;
    private ImageView imageProfileCompleted;
    private ImageView icByme;
    private TextView textUsername;
    private TextView textProfileCompletion;
    private TextView textTokopointAmount;
    private TextView textCouponAmount;
    private ProgressBar progressBar;
    private View byMeButton;
    private View nameHolder;
    private View tokopointHolder;
    private View couponHolder;
    private BuyerCardPresenter buyerCardPresenter;

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
        icByme = view.findViewById(R.id.ic_affiliate_byme);
        imageProfileProgress = view.findViewById(R.id.image_profile_progress);
        imageProfileCompleted = view.findViewById(R.id.image_profile_completed);
        textUsername = view.findViewById(R.id.text_username);
        textProfileCompletion = view.findViewById(R.id.text_profile_completion);
        textTokopointAmount = view.findViewById(R.id.text_tokopoint_amount);
        textCouponAmount = view.findViewById(R.id.text_voucher_amount);
        progressBar = view.findViewById(R.id.circular_progress_bar);
        byMeButton = view.findViewById(R.id.by_me_button);
        nameHolder = view.findViewById(R.id.holder_title);
        tokopointHolder = view.findViewById(R.id.holder_tokopoint);
        couponHolder = view.findViewById(R.id.holder_coupon);
        buyerCardPresenter = new BuyerCardPresenter();
        buyerCardPresenter.attachView(this);
    }

    public void renderData(BuyerCard buyerCard) {
        buyerCardPresenter.setData(buyerCard);
    }

    @Override
    public void setName(String name) {
        textUsername.setText(name);
    }

    @Override
    public void setAvatarImageUrl(int progress, String imageUrl) {

    }

    @Override
    public void showCompletedAvatar(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), imageProfileCompleted, imageUrl, R.drawable.ic_big_notif_customerapp);
    }

    @Override
    public void showIncompleteAvatar(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), imageProfileProgress, imageUrl, R.drawable.ic_big_notif_customerapp);
    }

    @Override
    public void setTokopoint(String tokopoint) {
        textTokopointAmount.setText(tokopoint);
    }

    @Override
    public void setCoupon(int coupons) {
        textCouponAmount.setText(String.format(getContext().getString(R.string.label_total_coupon), coupons));
    }

    @Override
    public void showProfileProgress(int progress) {
        imageProfileCompleted.setVisibility(GONE);
        imageProfileProgress.setVisibility(VISIBLE);
        progressBar.setVisibility(VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
        animation.setDuration(2000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void hideProfileProgress() {
        imageProfileCompleted.setVisibility(VISIBLE);
        imageProfileProgress.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }

    @Override
    public void setProfileStatusCompleted() {
        textProfileCompletion.setText(getContext().getString(R.string.verified_account));
        textProfileCompletion.setTypeface(null, Typeface.ITALIC);
    }

    @Override
    public void setProfileStatusIncomplete(int progress) {
        textProfileCompletion.setText(String.format(getContext().getString(R.string.label_profile_completion), progress));
        textProfileCompletion.setTextColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
    }

    @Override
    public void showBymeIcon() {
        icByme.setVisibility(VISIBLE);
    }

    public void setOnClickProfile(View.OnClickListener listener) {
        nameHolder.setOnClickListener(listener);
    }

    public void setOnClickProfileCompletion(View.OnClickListener listener) {
        textProfileCompletion.setOnClickListener(listener);
    }

    public void setOnClickTokoPoint(View.OnClickListener listener) {
        tokopointHolder.setOnClickListener(listener);
    }

    public void setOnClickVoucher(View.OnClickListener listener) {
        couponHolder.setOnClickListener(listener);
    }

    public void setOnClickByMe(View.OnClickListener listener) {
        byMeButton.setOnClickListener(listener);
    }
}
