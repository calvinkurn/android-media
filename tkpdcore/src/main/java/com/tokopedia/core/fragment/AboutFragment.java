package com.tokopedia.core.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.TkpdBasePreferenceFragment;
import com.tokopedia.core.manage.general.ManageWebViewActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.core.util.RouterUtils;

/**
 * Created by Angga.Prasetiyo on 13/01/2016.
 */
public class AboutFragment extends TkpdBasePreferenceFragment {
    private static final String TAG = AboutFragment.class.getSimpleName();

    private Preference prefVersion;
    private Preference prefTerm;
    private Preference prefPrivacy;
    private Preference prefShare;
    private Preference prefReview;
    private Preference prefDevOpt;
    private Preference prefContactUs;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    public AboutFragment() {
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SETTING_ABOUT_US;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_about);

        prefVersion = findPreference("about_version");
        prefTerm = findPreference("about_term");
        prefPrivacy = findPreference("about_privacy");
        prefShare = findPreference("about_share");
        prefReview = findPreference("about_review");
        prefContactUs = findPreference("about_contact");
        prefDevOpt = findPreference("about_dev_option");
        setListener();
    }

    private void setListener() {
        prefVersion.setSummary(GlobalConfig.VERSION_NAME);
        prefTerm.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent;
                String termUrl = "https://www.tokopedia.com/terms.pl";
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent = ManageWebViewActivity.getCallingIntent(getActivity(), termUrl,
                            getString(R.string.manage_terms_and_conditions));
                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(termUrl));
                }
                getActivity().startActivity(intent);
                return false;
            }
        });
        prefPrivacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent;
                String privacyUrl = "https://www.tokopedia.com/privacy.pl";
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent = ManageWebViewActivity.getCallingIntent(getActivity(), privacyUrl,
                            getString(R.string.manage_privacy));
                }else{
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(privacyUrl));
                }
                getActivity().startActivity(intent);
                return false;
            }
        });
        prefShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String urlPlayStore = "https://play.google.com/store/apps/details?id=" + getActivity().getApplication().getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getActivity().getResources().getString(R.string.msg_share_apps) + "\n" + urlPlayStore);
                sendIntent.setType("text/plain");
                getActivity().startActivity(Intent.createChooser(sendIntent, getActivity().getResources().getText(R.string.title_share)));
                return false;
            }
        });
        prefReview.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getApplication().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    getActivity().startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + getActivity().getApplication().getPackageName())));
                }
                return false;
            }
        });

        prefContactUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = InboxRouter.getContactUsActivityIntent(getActivity());
                getActivity().startActivity(intent);
                return false;
            }
        });

        if (GlobalConfig.isAllowDebuggingTools()) {
            prefDevOpt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    getActivity().startActivity(new Intent(getActivity(), DeveloperOptions.class));
                    return false;
                }
            });
        } else {
            getPreferenceScreen().removePreference(prefDevOpt);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() !=null) {
            ScreenTracking.screen(getScreenName());
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
