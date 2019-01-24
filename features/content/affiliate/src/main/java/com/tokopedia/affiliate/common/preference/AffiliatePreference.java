package com.tokopedia.affiliate.common.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/03/18.
 */

public class AffiliatePreference {
    private static final String AFFILIATE_PREFERENCE = "affiliate_preference";
    private static final String FORMAT_CREATE_POST = "create_post_%s";
    private static final String FORMAT_EDUCATION = "education_%s";

    private final SharedPreferences sharedPrefs;

    @Inject
    public AffiliatePreference(@ApplicationContext Context context) {
        this.sharedPrefs = context.getSharedPreferences(
                AFFILIATE_PREFERENCE,
                Context.MODE_PRIVATE
        );
    }

    public boolean isFirstTimeCreatePost(String userId) {
        return sharedPrefs.getBoolean(String.format(FORMAT_CREATE_POST, userId), true);
    }

    public void setFirstTimeCreatePost(String userId) {
        sharedPrefs.edit().putBoolean(String.format(FORMAT_CREATE_POST, userId), false).apply();
    }

    public boolean isFirstTimeEducation(String userId) {
        return sharedPrefs.getBoolean(String.format(FORMAT_EDUCATION, userId), true);
    }

    public void setFirstTimeEducation(String userId) {
        sharedPrefs.edit().putBoolean(String.format(FORMAT_EDUCATION, userId), false).apply();
    }
}
