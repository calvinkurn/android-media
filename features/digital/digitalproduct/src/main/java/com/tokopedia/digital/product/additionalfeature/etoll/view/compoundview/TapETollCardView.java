package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.digital.R;

/**
 * Created by Rizky on 15/05/18.
 */
public class TapETollCardView extends RelativeLayout {

    private TextView textLabel;
    private LottieAnimationView lottieAnimationView;

    public TapETollCardView(Context context) {
        super(context);
        init();
    }

    public TapETollCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TapETollCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TapETollCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_tap_etoll_card, this);

        textLabel = view.findViewById(R.id.text_label);
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view);
    }

    public void showLoading() {
        textLabel.setText(getResources().getString(R.string.reading_card_label));
        lottieAnimationView.setVisibility(VISIBLE);
        lottieAnimationView.clearAnimation();
        lottieAnimationView.setAnimation("emoney_loading.json");
        lottieAnimationView.playAnimation();
    }

    public void showInitialState() {
        textLabel.setText(getResources().getString(R.string.emoney_tap_card_instruction));
        lottieAnimationView.setVisibility(VISIBLE);
        lottieAnimationView.clearAnimation();
        lottieAnimationView.setAnimation("emoney_animation.json");
        lottieAnimationView.playAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == GONE) {
            showInitialState();
        }
    }
}
