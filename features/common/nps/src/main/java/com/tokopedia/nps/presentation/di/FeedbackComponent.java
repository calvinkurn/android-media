package com.tokopedia.nps.presentation.di;

import android.content.Context;

import com.tokopedia.nps.presentation.view.activity.FeedbackActivity;

import dagger.Component;

/**
 * Created by meta on 02/07/18.
 */

@FeedbackScope
@Component(modules = FeedbackModule.class)
public interface FeedbackComponent {
    void inject(FeedbackActivity activity);

    Context context();
}
