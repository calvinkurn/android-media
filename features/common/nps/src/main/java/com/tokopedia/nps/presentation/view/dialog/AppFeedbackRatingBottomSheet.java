package com.tokopedia.nps.presentation.view.dialog;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.R;

public class AppFeedbackRatingBottomSheet extends BottomSheets {

    private static final int GOOD_RATING_THRESHOLD = 3;

    private AppCompatRatingBar ratingBar;
    private AppCompatTextView ratingLevel;
    private AppCompatButton sendButton;

    private int ratingValue = 1;
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
            ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                ratingLevel.setText(ratingDetails[(int) rating]);
            });
        }

        if (sendButton != null) {
            sendButton.setOnClickListener(v -> {
                Log.d(">>>>>", "@@");
                if (ratingValue < GOOD_RATING_THRESHOLD) {
                    Log.d("Bad Rating", "");
                } else {
                    Log.d("Good Rating", "");
                }
            });
        }

    }

}
