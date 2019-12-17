package com.tokopedia.tkpd.utils;

import android.app.Activity;

import com.google.android.play.core.inappreview.InAppReviewInfo;
import com.google.android.play.core.inappreview.InAppReviewManager;
import com.google.android.play.core.inappreview.InAppReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

public class InAppReviewUtils {

    public static void showInAppReview(Activity activity, Callback callback) {
        InAppReviewManager manager = InAppReviewManagerFactory.create(activity);
        manager.requestInAppReviewFlow().addOnCompleteListener(new OnCompleteListener<InAppReviewInfo>() {
            @Override
            public void onComplete(Task<InAppReviewInfo> request) {
                if (request.isSuccessful()) {
                    launchInAppReview(activity, manager, request, callback);
                }
            }
        });
    }

    private static void launchInAppReview(Activity activity, InAppReviewManager manager, Task<InAppReviewInfo> request, Callback callback) {
        manager.launchInAppReviewFlow(activity, request.getResult()).addOnCompleteListener(new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(Task<Integer> task) {
                callback.onFinish();
            }
        });
    }

    public interface Callback {
        void onFinish();
    }
}
