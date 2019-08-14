package com.tokopedia.nps.presentation.di;

import android.content.Context;

import com.tokopedia.nps.presentation.view.activity.FeedbackActivity;
import com.tokopedia.nps.presentation.view.dialog.AppFeedbackMessageBottomSheet;
import com.tokopedia.nps.presentation.view.dialog.AppRatingDialog;

import dagger.Component;

/**
 * Created by meta on 02/07/18.
 */

@FeedbackScope
@Component(modules = FeedbackModule.class)
public interface FeedbackComponent {

    void inject(FeedbackActivity activity);

    void inject(AppRatingDialog dialog);

    void inject(AppFeedbackMessageBottomSheet dialog);

    Context context();
}
