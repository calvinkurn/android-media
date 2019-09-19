package com.tokopedia.searchbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.badge.BadgeView;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.searchbar.util.NotifAnalytics;
import com.tokopedia.searchbar.util.NotifPreference;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by meta on 22/06/18.
 */
public class MainToolbar extends Toolbar {

    private static String redDotGimmickRemoteConfigKey = "android_red_dot_gimmick_view";

    protected ImageView btnNotification;
    protected ImageView btnWishlist;
    protected ImageView btnInbox;
    private BadgeView badgeViewInbox;
    private BadgeView badgeViewNotification;

    protected SearchBarAnalytics searchBarAnalytics;
    protected UserSessionInterface userSession;
    protected NotifPreference notifPreference;
    protected RemoteConfig remoteConfig;
    protected NotifAnalytics notifAnalytics;

    protected String screenName = "";

    public MainToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public MainToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MainToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setNotificationNumber(int badgeNumber) {
        if (btnNotification != null) {
            if (badgeViewNotification == null)
                badgeViewNotification = new BadgeView(getContext());

            boolean redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(redDotGimmickRemoteConfigKey, false);
            boolean redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif();

            if(redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus){
                badgeNumber =+ 1;
                if(!notifPreference.isViewedGimmickNotif() && !userSession.isLoggedIn()){
                    notifPreference.setViewedGimmickNotif(true);
                    notifAnalytics.trackImpressionOnGimmickNotif();
                }
            }

            badgeViewNotification.bindTarget(btnNotification);
            badgeViewNotification.setBadgeGravity(Gravity.END | Gravity.TOP);
            badgeViewNotification.setBadgeNumber(badgeNumber);
        }
    }

    public void setInboxNumber(int badgeNumber) {
        if (btnInbox != null) {
            if (badgeViewInbox == null)
                badgeViewInbox = new BadgeView(getContext());

            badgeViewInbox.bindTarget(btnInbox);
            badgeViewInbox.setBadgeGravity(Gravity.END | Gravity.TOP);
            badgeViewInbox.setBadgeNumber(badgeNumber);
        }
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {

        notifAnalytics = new NotifAnalytics();
        userSession = new UserSession(context);
        notifPreference = new NotifPreference(context);
        searchBarAnalytics = new SearchBarAnalytics(this.getContext());

        inflateResource(context);
        ImageButton btnQrCode = findViewById(R.id.btn_qrcode);
        btnNotification = findViewById(R.id.btn_notification);
        btnInbox = findViewById(R.id.btn_inbox);
        btnWishlist = findViewById(R.id.btn_wishlist);
        TextView editTextSearch = findViewById(R.id.et_search);

        remoteConfig = new FirebaseRemoteConfigImpl(context);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainToolbar, 0, 0);
            try {
                screenName = ta.getString(R.styleable.MainToolbar_screenName);
            } finally {
                ta.recycle();
            }
        }

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            editTextSearch.setTextSize(18);
        }

        if (btnQrCode != null) {
            btnQrCode.setOnClickListener(v -> {
                searchBarAnalytics.eventTrackingSqanQr();
                getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                        .gotoQrScannerPage(false));
            });
        }

        btnWishlist.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName);
                RouteManager.route(context, ApplinkConst.WISHLIST);
            } else {
                searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName);
                RouteManager.route(context, ApplinkConst.LOGIN);
            }
        });

        btnInbox.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.INBOX, screenName);
                RouteManager.route(context, ApplinkConst.INBOX);
            } else {
                searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.INBOX, screenName);
                RouteManager.route(context, ApplinkConst.LOGIN);
            }
        });

        editTextSearch.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingSearchBar(screenName);
            RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE);
        });

        btnNotification.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingNotification(screenName);
            if (userSession.isLoggedIn()) {
                RouteManager.route(context, ApplinkConst.NOTIFICATION);
            } else {
                boolean redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(redDotGimmickRemoteConfigKey, false);
                boolean redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif();
                if(redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus){
                    notifAnalytics.trackClickGimmickNotif();
                }

                RouteManager.route(context, ApplinkConst.LOGIN);
            }
        });

        setNotificationNumber(0);
    }

    public void inflateResource(Context context) {
        inflate(context, R.layout.main_toolbar, this);
    }

    public ImageView getBtnNotification() {
        return btnNotification;
    }

    public ImageView getBtnWishlist() {
        return btnWishlist;
    }
}