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

    private FrameLayout buttonView;
    private float ratingValue;
    private String[] ratingDetails;

    private void setRatingBarChangedListener(RatingBar ratingBarView, Typography ratingLevelView) {
        if (ratingBarView != null && ratingLevelView != null && buttonView != null) {
            ratingBarView.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                ratingValue = rating;
                int ratingIndex = ((int) rating) - 1;

                if (ratingIndex >= 0) {
                    ratingLevelView.setText(ratingDetails[ratingIndex]);
                    if (!buttonView.isEnabled()) {
                        buttonView.setEnabled(true);
                    }
                } else {
                    ratingLevelView.setText("");
                    if (buttonView.isEnabled()) {
                        buttonView.setEnabled(false);
                    }
                }
            });
        }
    }

    private void setSendButtonClickListener(FrameLayout button) {
        if (button != null) {
            button.setEnabled(false);

            button.setOnClickListener(v -> {
                if (getActivity() != null) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();

                    if ((int) ratingValue > NpsConstant.Feedback.GOOD_RATING_THRESHOLD) {
                        new AppFeedbackThankYouBottomSheet()
                                .showDialog(manager, ratingValue, "AppFeedbackThankYouBottomSheet");
                    } else {
                        new AppFeedbackMessageBottomSheet()
                                .showDialog(manager, ratingValue, "AppFeedbackMessageBottomSheet");
                    }

                    this.dismiss();
                }
            });
        }
    }

    public void setDialogDismissListener(BottomSheetDismissListener dismissListener) {
        setDismissListener(dismissListener);
    }

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.dialog_feedback_base;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_rating;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FLEXIBLE;
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
        buttonView = view.findViewById(R.id.send_button);

        setRatingBarChangedListener(ratingBarView, ratingLevelView);
        setSendButtonClickListener(buttonView);
    }
}
