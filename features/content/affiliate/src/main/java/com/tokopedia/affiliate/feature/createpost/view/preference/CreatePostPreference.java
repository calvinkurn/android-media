package com.tokopedia.affiliate.feature.createpost.view.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/03/18.
 */

public class CreatePostPreference {
    private static final String CREATE_POST_AFFILIATE = "create_post_affiliate";
    private static final String FORMAT_FIRST_TIME = "first_time_%s";

    private final SharedPreferences sharedPrefs;

    @Inject
    public CreatePostPreference(@ApplicationContext Context context) {
        this.sharedPrefs = context.getSharedPreferences(
                CREATE_POST_AFFILIATE,
                Context.MODE_PRIVATE
        );
    }

    public boolean isFirstTimeUser(String userId) {
        return sharedPrefs.getBoolean(String.format(FORMAT_FIRST_TIME, userId), true);
    }

    public void setFirstTime(String userId) {
        sharedPrefs.edit().putBoolean(String.format(FORMAT_FIRST_TIME, userId), false).apply();
    }
}
