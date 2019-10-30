package com.tokopedia.core.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.tkpd.library.ui.utilities.CustomCheckBoxPreference;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TkpdBasePreferenceFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.track.TrackApp;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsFragment extends TkpdBasePreferenceFragment {
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    private static Context context;
    private CustomCheckBoxPreference optionVibrate;
    private CustomCheckBoxPreference optionPromo;
    private CustomCheckBoxPreference optionShakeShake;
    public static final String SETTING_NOTIFICATION_VIBRATE = "notifications_new_message_vibrate";
    public static final String SETTING_NOTIFICATION_SHAKE_SHAKE = Constants.Settings.NOTIFICATION_SHAKE_SHAKE;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SETTING_MANAGE_APP;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        setupSimplePreferencesScreen();
    }


    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(context)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_notification);

        optionShakeShake = (CustomCheckBoxPreference) findPreference(SETTING_NOTIFICATION_SHAKE_SHAKE);
        optionShakeShake.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });
        optionVibrate = (CustomCheckBoxPreference) findPreference(SETTING_NOTIFICATION_VIBRATE);
        optionVibrate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences
                        (context);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(SETTING_NOTIFICATION_VIBRATE, !settings.getBoolean(SETTING_NOTIFICATION_VIBRATE
                        , false));
                editor.apply();
                return true;
            }
        });

        optionPromo = (CustomCheckBoxPreference) findPreference(Constants.Settings.NOTIFICATION_PROMO);
        optionPromo.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
    }


    /** {@inheritDoc} */
    /*@Override
    public boolean onIsMultiPane() {
		return isXLargeTablet(context) && !isSimplePreferences(context);
	}*/

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    /** {@inheritDoc} */
	/*@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(context)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}

	}*/

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference
                        .setSummary(index >= 0 ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                if (!preference.getKey().equals(Constants.Settings.NOTIFICATION_PROMO))
                    preference.setSummary(stringValue);

                if(value instanceof Boolean) {
                    TrackApp.getInstance().getMoEngage().setPushPreference((Boolean) value);
                }
            }

            return true;
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null) {
            ScreenTracking.screen(MainApplication.getAppContext(), getScreenName());
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
