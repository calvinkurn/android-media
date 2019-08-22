package com.tokopedia.digital.product.additionalfeature.etoll.view.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.digital.R;

/**
 * Created by Rizky on 15/05/18.
 */
public class TapETollCardView extends RelativeLayout {

    private TextView textTitle;
    private TextView textLabel;
    private LottieAnimationView lottieAnimationView;
    private AppCompatButton buttonTryAgain;
    private ImageView imageviewError;

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

        textTitle = view.findViewById(R.id.text_title);
        textLabel = view.findViewById(R.id.text_label);
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view);
        buttonTryAgain = view.findViewById(R.id.button_try_again);
        imageviewError = view.findViewById(R.id.imageview_error);

        buttonTryAgain.setOnClickListener(v -> {
            showInitialState();
        });
    }

    public void showLoading() {
        textTitle.setText(getResources().getString(R.string.reading_card_label_title));
        textTitle.setTextColor(getResources().getColor(R.color.black));
        textLabel.setText(getResources().getString(R.string.reading_card_label_message));
        lottieAnimationView.setVisibility(VISIBLE);
        lottieAnimationView.clearAnimation();
        lottieAnimationView.setAnimation("emoney_loading.json");
        lottieAnimationView.playAnimation();
        imageviewError.setVisibility(GONE);
        buttonTryAgain.setVisibility(GONE);
    }

    public void showInitialState() {
        textTitle.setText(getResources().getString(R.string.emoney_tap_card_instruction_title));
        textTitle.setTextColor(getResources().getColor(R.color.black));
        textLabel.setText(getResources().getString(R.string.emoney_tap_card_instruction_message));
        lottieAnimationView.setVisibility(VISIBLE);
        lottieAnimationView.clearAnimation();
        lottieAnimationView.setAnimation("emoney_animation.json");
        lottieAnimationView.playAnimation();
        imageviewError.setVisibility(GONE);
        buttonTryAgain.setVisibility(GONE);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == GONE) {
            showInitialState();
        }
    }

    public void showErrorState(String errorMessage) {
        textTitle.setText(getResources().getString(R.string.emoney_tap_card_instruction_title));
        textTitle.setTextColor(getResources().getColor(R.color.red_600));
        textLabel.setText(errorMessage);
        lottieAnimationView.setVisibility(GONE);
        imageviewError.setVisibility(VISIBLE);
        buttonTryAgain.setVisibility(VISIBLE);
    }

}
