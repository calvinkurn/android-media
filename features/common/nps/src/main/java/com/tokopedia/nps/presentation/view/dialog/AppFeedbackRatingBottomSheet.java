package com.tokopedia.nps.presentation.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.nps.NpsConstant;
import com.tokopedia.nps.R;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.unifyprinciples.Typography;

public class AppFeedbackRatingBottomSheet extends AppFeedbackDialog {

    private FrameLayout buttonView;
    private float ratingValue;
    private String[] ratingDetails;
    private LocalCacheHandler cacheHandler;
    private boolean isCancelled = true;

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_rating;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ratingDetails = getResources().getStringArray(R.array.app_ratings);

        AppCompatRatingBar ratingBarView = view.findViewById(R.id.rating_bar);
        Typography ratingLevelView = view.findViewById(R.id.rating_level);
        buttonView = view.findViewById(R.id.send_button);

        setRatingBarChangedListener(ratingBarView, ratingLevelView);
        setSendButtonClickListener(buttonView);

        npsAnalytics.eventAppRatingImpression(getClass().getSimpleName());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (isCancelled) {
            npsAnalytics.eventCancelAppRating(LABEL_CANCEL_ADVANCED_APP_RATING);
            PersistentCacheManager.instance.put(HIDE_FEEDBACK_RATING, DEFAULT_CACHE_VALUE, EXPIRED_DURATION);
        }

        super.onDismiss(dialog);
    }

    public void showDialog(FragmentManager manager, Context context) {
        if (context != null && manager != null) {
            if (isFeedbackRatingNeeded(context)) {
                super.show(manager, "AppFeedbackRatingBottomSheet");
            }
        }
    }

    public void setDialogDismissListener(BottomSheetDismissListener dismissListener) {
        setDismissListener(dismissListener);
    }

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
                    handleOnSendState();

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

    private void handleOnSendState() {
        npsAnalytics.eventClickAppRating(LABEL_CLICK_ADVANCED_APP_RATING + ratingValue);

        cacheHandler.putInt(NpsConstant.Key.KEY_ADVANCED_APP_RATING_VERSION, GlobalConfig.VERSION_CODE);
        cacheHandler.putInt(NpsConstant.Key.KEY_RATING, (int) ratingValue);
        cacheHandler.applyEditor();

        isCancelled = false;
    }

    private boolean isFeedbackRatingNeeded(Context context) {
        boolean isCacheValid = false, isVersionValid = false, isRatingValid = false;

        if (context != null) {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
            isCacheValid = remoteConfig.getBoolean(getConfigKey(), false) &&
                    PersistentCacheManager.instance.isExpired(HIDE_FEEDBACK_RATING);

            cacheHandler = new LocalCacheHandler(context, NpsConstant.Key.APP_RATING);
            int feedbackRatingVersion = cacheHandler.getInt(NpsConstant.Key.KEY_ADVANCED_APP_RATING_VERSION);
            int rating = cacheHandler.getInt(NpsConstant.Key.KEY_RATING);
            isVersionValid = feedbackRatingVersion == -1 || feedbackRatingVersion < GlobalConfig.VERSION_CODE;

            isRatingValid = rating <= NpsConstant.Feedback.GOOD_RATING_THRESHOLD;
        }

        return isCacheValid && isVersionValid && isRatingValid;
    }
}
