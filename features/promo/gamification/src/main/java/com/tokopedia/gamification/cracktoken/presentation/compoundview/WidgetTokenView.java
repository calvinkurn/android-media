package com.tokopedia.gamification.cracktoken.presentation.compoundview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.presentation.customview.MaskedHeightImageView;

/**
 * Created by Rizky on 28/03/18.
 */

public class WidgetTokenView extends FrameLayout {

    private ImageView imageViewFull;
    private MaskedHeightImageView imageViewCracked;
    private ImageView imageViewLeft;
    private ImageView imageViewRight;

    private ImageView imageViewLightLeft;
    private ImageView imageViewLightRight;

    private Bitmap rightCrackedEgg;
    private Bitmap leftCrackedEgg;

    private boolean isTokenClicked;

    private WidgetTokenListener listener;

    public interface WidgetTokenListener {
        void onClick();
    }

    public WidgetTokenView(@NonNull Context context) {
        super(context);
        init();
    }

    public WidgetTokenView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetTokenView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(WidgetTokenListener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_token, this, true);
        imageViewFull = view.findViewById(R.id.imagefull);
        imageViewCracked = view.findViewById(R.id.imagecracked);
        imageViewLeft = view.findViewById(R.id.imageleft);
        imageViewRight = view.findViewById(R.id.imageright);
        imageViewLightLeft = view.findViewById(R.id.image_light_left);
        imageViewLightRight = view.findViewById(R.id.image_light_top);

        imageViewFull.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTokenClicked) {
                    clearTokenAnimation();
                    shakeHard();
                    crack();
                    listener.onClick();
                }
                isTokenClicked = true;
            }
        });
    }

    public void setToken(Bitmap full, Bitmap cracked, Bitmap right, Bitmap left) {
        this.rightCrackedEgg = right;
        this.leftCrackedEgg = left;
        imageViewFull.setImageBitmap(full);
        imageViewCracked.setImageBitmap(cracked);
        shake();
        showLightAnimation();
    }

    private void showLightAnimation() {
        AlphaAnimation alphaAnimationLeft = new AlphaAnimation(0f, 1f);
        alphaAnimationLeft.setDuration(100);
        alphaAnimationLeft.setRepeatCount(Animation.INFINITE);
        alphaAnimationLeft.setStartOffset(5000);
        alphaAnimationLeft.setRepeatMode(Animation.REVERSE);

        AlphaAnimation alphaAnimationRight = new AlphaAnimation(0f, 1f);
        alphaAnimationRight.setDuration(100);
        alphaAnimationRight.setRepeatCount(Animation.INFINITE);
        alphaAnimationRight.setStartOffset(8000);
        alphaAnimationRight.setRepeatMode(Animation.REVERSE);

        imageViewLightLeft.setAnimation(alphaAnimationLeft);
        imageViewLightRight.setAnimation(alphaAnimationRight);
    }

    private void shake() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        imageViewFull.setAnimation(animation);
        imageViewCracked.setAnimation(animation);
    }

    private void shakeHard() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake_hard);
        imageViewFull.setAnimation(animation);
        imageViewCracked.setAnimation(animation);
    }

    public void stopShaking() {
        imageViewFull.clearAnimation();
        imageViewCracked.clearAnimation();
    }

    public void crack() {
        ValueAnimator crackingEggValAnimator = ValueAnimator.ofInt(100, 0);
        crackingEggValAnimator.setInterpolator(new DecelerateInterpolator());
        crackingEggValAnimator.setDuration(1000);
        crackingEggValAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                imageViewCracked.setPercentMasked(animatedValue);
            }
        });
        crackingEggValAnimator.start();
    }

    public void split() {
        imageViewFull.setVisibility(View.GONE);
        imageViewCracked.setVisibility(View.GONE);

        imageViewLeft.setImageBitmap(leftCrackedEgg);
        imageViewRight.setImageBitmap(rightCrackedEgg);
        imageViewLeft.setVisibility(View.VISIBLE);
        imageViewRight.setVisibility(View.VISIBLE);

        imageViewFull.clearAnimation();
        imageViewCracked.clearAnimation();

        Animation rotateRightAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_right_and_translate);
        imageViewRight.setAnimation(rotateRightAnimation);
        Animation rotateLeftAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_left_and_translate);
        imageViewLeft.setAnimation(rotateLeftAnimation);
    }

    public void reset() {
        isTokenClicked = false;

        imageViewRight.clearAnimation();
        imageViewRight.setVisibility(View.GONE);

        imageViewLeft.clearAnimation();
        imageViewLeft.setVisibility(View.GONE);

        imageViewFull.setVisibility(View.VISIBLE);
        imageViewCracked.setPercentMasked(100);

        showLightAnimation();
    }

    public void clearTokenAnimation() {
        imageViewLightLeft.clearAnimation();
        imageViewLightLeft.setVisibility(View.GONE);
        imageViewLightRight.clearAnimation();
        imageViewLightRight.setVisibility(View.GONE);

        imageViewFull.clearAnimation();
        imageViewCracked.clearAnimation();
    }

}
