package com.tokopedia.home.account.presentation.view.buyercardview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardView extends BaseCustomView implements BuyerCardContract.View {

    private ImageView imageProfileCompleted;
    private ImageView icByme;
    private ImageView eggImage;
    private TextView textUsername;
    private TextView textProfileCompletion;
    private TextView textTokopointTitle;
    private TextView textTokopointAmount;
    private ImageView ivTokopoint;
    private TextView textCouponTitle;
    private TextView textCouponAmount;
    private ImageView ivCoupon;
    private TextView textTokoMemberTitle;
    private TextView textTokoMemberAmount;
    private ImageView ivTokomember;
    private View byMeButton;
    private View tokopointHolder;
    private View couponHolder;
    private View tokomemberHolder;
    private View dividerOne;
    private View dividerTwo;
    private AppCompatImageView ivMemberBadge;
    private BuyerCardPresenter buyerCardPresenter;
    private CardView widget;

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
        imageProfileCompleted = view.findViewById(R.id.image_profile_completed);
        textUsername = view.findViewById(R.id.text_username);
        textProfileCompletion = view.findViewById(R.id.text_profile_completion);
        textTokopointTitle = view.findViewById(R.id.label_tokopoint);
        textTokopointAmount = view.findViewById(R.id.text_tokopoint_amount);
        ivTokopoint = view.findViewById(R.id.image_tokopoint);
        textCouponTitle = view.findViewById(R.id.label_voucher);
        textCouponAmount = view.findViewById(R.id.text_voucher_amount);
        ivCoupon = view.findViewById(R.id.image_voucher);
        byMeButton = view.findViewById(R.id.by_me_button);
        tokopointHolder = view.findViewById(R.id.holder_tokopoint);
        couponHolder = view.findViewById(R.id.holder_coupon);
        tokomemberHolder = view.findViewById(R.id.holder_tokomember);
        textTokoMemberTitle = view.findViewById(R.id.label_tokomember);
        textTokoMemberAmount = view.findViewById(R.id.text_tokomember_amount);
        ivTokomember = view.findViewById(R.id.image_tokomember);
        dividerOne = view.findViewById(R.id.divider1);
        dividerTwo = view.findViewById(R.id.divider2);
        ivMemberBadge = view.findViewById(R.id.ivMemberBadge);
        widget=view.findViewById(R.id.cardView);
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
    public void showCompletedAvatar(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), imageProfileCompleted, imageUrl, R.drawable.ic_big_notif_customerapp);
    }


    @Override
    public void setTokopoint(String tokopoint) {
        textTokopointAmount.setText(tokopoint);
    }

    @Override
    public void setTokopointTitle(String title) {
        textTokopointTitle.setText(title);
    }

    @Override
    public void setTokopointImageUrl(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), ivTokopoint, imageUrl, R.drawable.placeholder_grey);
    }

    @Override
    public void setCouponTitle(String title) {
        textCouponTitle.setText(title);
    }

    @Override
    public void setCouponImageUrl(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), ivCoupon, imageUrl, R.drawable.placeholder_grey);
    }

    @Override
    public void setCoupon(String coupons) {
        textCouponAmount.setText(coupons);
    }

    @Override
    public void setTokoMemberTitle(String title) {
        textTokoMemberTitle.setText(title);
    }

    @Override
    public void setTokomemberImageUrl(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), ivTokomember, imageUrl, R.drawable.placeholder_grey);

    }

    @Override
    public void showBymeIcon() {
        icByme.setVisibility(VISIBLE);
    }

    public void setOnClickProfileCompletion(View.OnClickListener listener) {
        imageProfileCompleted.setOnClickListener(listener);
    }

    public void setOnClickTokoPoint(View.OnClickListener listener) {
        tokopointHolder.setOnClickListener(listener);
    }

    public void setOnClickTokoMember(View.OnClickListener listener) {
        tokomemberHolder.setOnClickListener(listener);
    }

    public void setOnClickVoucher(View.OnClickListener listener) {
        couponHolder.setOnClickListener(listener);
    }

    public void setOnClickByMe(View.OnClickListener listener) {
        byMeButton.setOnClickListener(listener);
    }

    public void setOnClickMemberDetail(View.OnClickListener listener) {
        textProfileCompletion.setOnClickListener(listener);
        ivMemberBadge.setOnClickListener(listener);
    }

    @Override
    public void setTokoMemberAmount(String tokoMemberAmount) {
        textTokoMemberAmount.setText(tokoMemberAmount);
    }

    @Override
    public void setCardVisibility(int visibility) {
        tokopointHolder.setVisibility(visibility);
    }

    @Override
    public void setVisibilityCenterLayout(int visibility) {
        couponHolder.setVisibility(visibility);
    }

    @Override
    public void setVisibilityRightLayout(int visibility) {
        tokomemberHolder.setVisibility(visibility);
    }

    @Override
    public void setVisibilityDividerFirst(int visibility) {
        dividerOne.setVisibility(visibility);
    }

    @Override
    public void setVisibilityDividerSecond(int visibility) {
        dividerTwo.setVisibility(visibility);
    }

    @Override
    public void setWidgetVisibility(int visibility) {
         widget.setVisibility(View.GONE);
    }


    @Override
    public void setMemberStatus(String status) {
        textProfileCompletion.setText(status);
    }

    @Override
    public void setEggImage(String eggImageUrl) {
        if (eggImageUrl != null && eggImageUrl.length() != 0) {
            ivMemberBadge.setVisibility(VISIBLE);
            ImageHandler.loadImageCircle2(getContext(), ivMemberBadge, eggImageUrl, R.drawable.placeholder_grey);
        }
    }
}
