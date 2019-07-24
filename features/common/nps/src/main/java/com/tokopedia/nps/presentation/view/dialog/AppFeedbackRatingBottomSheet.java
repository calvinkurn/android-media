package com.tokopedia.nps.presentation.view.dialog;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.R;

public class AppFeedbackRatingBottomSheet extends BottomSheets {

    private static final int GOOD_RATING_THRESHOLD = 3;

    private AppCompatRatingBar ratingBar;
    private AppCompatTextView ratingLevel;
    private AppCompatButton sendButton;

    private float ratingValue;
    private String[] ratingDetails;

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

        ratingBar = view.findViewById(R.id.rating_bar);
        ratingLevel = view.findViewById(R.id.rating_level);
        sendButton = view.findViewById(R.id.send_button);

        if (ratingBar != null && ratingLevel != null) {
            ratingLevel.setText(ratingDetails[((int) ratingBar.getRating()) - 1]);

            ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                ratingValue = rating;
                int ratingIndex = ((int) rating) - 1;

                ratingLevel.setText(ratingDetails[ratingIndex]);
            });
        }

        if (sendButton != null) {
            sendButton.setOnClickListener(v -> {
                FragmentManager manager = getActivity().getSupportFragmentManager();

                if (manager != null) {
                    if ((int) ratingValue < GOOD_RATING_THRESHOLD) {
                        new AppFeedbackMessageBottomSheet().show(manager, "AppFeedbackMessageBottomSheet");
                    } else {
                        new AppFeedbackThankYouBottomSheet().show(manager, "AppFeedbackThankYouBottomSheet");
                    }

                    this.dismiss();
                }
            });
        }

    }
}
