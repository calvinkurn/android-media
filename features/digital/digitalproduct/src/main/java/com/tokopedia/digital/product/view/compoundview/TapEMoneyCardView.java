package com.tokopedia.digital.product.view.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.digital.R;

/**
 * Created by Rizky on 15/05/18.
 */
public class TapEMoneyCardView extends RelativeLayout {

//    private ProgressBar progressBar;
    private TextView textLabel;
//    private ImageView imageDontMoveTheCard;
    private LottieAnimationView lottieAnimationView;

    public TapEMoneyCardView(Context context) {
        super(context);
        init();
    }

    public TapEMoneyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TapEMoneyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TapEMoneyCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_tap_emoney_card, this);

//        progressBar = view.findViewById(R.id.progress_bar);
        textLabel = view.findViewById(R.id.text_label);
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view);
//        imageDontMoveTheCard = view.findViewById(R.id.image_dont_move_the_card);
    }

    public void showLoading(String textLoading) {
        setVisibility(VISIBLE);
//        progressBar.setVisibility(VISIBLE);
        textLabel.setText(textLoading);
        lottieAnimationView.clearAnimation();
        lottieAnimationView.setAnimation("emoney_loading.json");
        lottieAnimationView.playAnimation();
//        lottieAnimationView.setVisibility(GONE);
//        imageDontMoveTheCard.setVisibility(VISIBLE);
    }

//    @Override
//    public void setVisibility(int visibility) {
//        if (visibility == GONE) {
//            progressBar.setVisibility(GONE);
//        }
//        super.setVisibility(visibility);
//    }

    public void stopLoading() {
//        progressBar.setVisibility(GONE);
        lottieAnimationView.setVisibility(VISIBLE);
        lottieAnimationView.playAnimation();
//        imageDontMoveTheCard.setVisibility(GONE);
        textLabel.setText(getResources().getString(R.string.emoney_tap_card_instruction));
    }
}
