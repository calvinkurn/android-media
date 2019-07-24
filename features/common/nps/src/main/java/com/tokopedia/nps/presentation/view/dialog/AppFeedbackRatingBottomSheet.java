package com.tokopedia.nps.presentation.view.dialog;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.NpsConstant;
import com.tokopedia.nps.R;
import com.tokopedia.unifyprinciples.Typography;

public class AppFeedbackRatingBottomSheet extends BottomSheets {

    private float ratingValue;
    private String[] ratingDetails;

    private void setRatingBarChangedListener(RatingBar ratingBarView, Typography ratingLevelView) {
        if (ratingBarView != null && ratingLevelView != null) {
            ratingLevelView.setText(ratingDetails[((int) ratingBarView.getRating()) - 1]);

            ratingValue = ratingBarView.getRating();

            ratingBarView.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                ratingValue = rating;
                int ratingIndex = ((int) rating) - 1;

                ratingLevelView.setText(ratingDetails[ratingIndex]);
            });
        }
    }

    private void setSendButtonClickListener(FrameLayout button, float rating) {
        if (button != null) {
            button.setOnClickListener(v -> {
                FragmentManager manager = getActivity().getSupportFragmentManager();

                if (manager != null) {
                    if ((int) rating > NpsConstant.Feedback.GOOD_RATING_THRESHOLD) {
                        new AppFeedbackThankYouBottomSheet()
                                .showDialog(manager, rating, "AppFeedbackThankYouBottomSheet");
                    } else {
                        new AppFeedbackMessageBottomSheet()
                                .showDialog(manager, rating, "AppFeedbackMessageBottomSheet");
                    }

                    this.dismiss();
                }
            });
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_rating;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.NORMAL;
    }

    @Override
    protected String title() {
        return "";
    }

    @Override
    public void initView(View view) {
        ratingDetails = getResources().getStringArray(R.array.app_ratings);

        AppCompatRatingBar ratingBarView = view.findViewById(R.id.rating_bar);
        Typography ratingLevelView = view.findViewById(R.id.rating_level);
        FrameLayout sendButtonView = view.findViewById(R.id.send_button);

        setRatingBarChangedListener(ratingBarView, ratingLevelView);
        setSendButtonClickListener(sendButtonView, ratingValue);
    }
}
