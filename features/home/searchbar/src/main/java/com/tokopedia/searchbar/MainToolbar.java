package com.tokopedia.searchbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.design.component.badge.BadgeView;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.searchbar.util.NotifAnalytics;
import com.tokopedia.searchbar.util.NotifPreference;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by meta on 22/06/18.
 */
public class MainToolbar extends Toolbar {

    private static final String RED_DOT_GIMMICK_REMOTE_CONFIG_KEY = "android_red_dot_gimmick_view";
    private boolean wishlistNewPage = false;
    protected ImageView btnNotification;
    protected ImageView btnWishlist;
    protected ImageView btnInbox;
    protected TextView editTextSearch;
    private BadgeView badgeViewInbox;
    private BadgeView badgeViewNotification;

    protected SearchBarAnalytics searchBarAnalytics;
    protected UserSessionInterface userSession;
    protected NotifPreference notifPreference;
    protected RemoteConfig remoteConfig;
    protected NotifAnalytics notifAnalytics;

    public String searchApplink = ApplinkConstInternalDiscovery.AUTOCOMPLETE;
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

            boolean redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(RED_DOT_GIMMICK_REMOTE_CONFIG_KEY, false);
            boolean redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif();
            if(redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus){
                badgeNumber += 1;
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

        FirebaseRemoteConfigImpl firebaseRemoteConfig = new FirebaseRemoteConfigImpl(context);
        wishlistNewPage = firebaseRemoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_WISHLIST_PAGE, true);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainToolbar, 0, 0);
            try {
                screenName = ta.getString(R.styleable.MainToolbar_screenName);
            } finally {
                ta.recycle();
            }
        }
        inflateResource(context);
    }

    protected void actionAfterInflation(Context context, View view){
        btnNotification = view.findViewById(R.id.btn_notification);
        btnInbox = view.findViewById(R.id.btn_inbox);
        btnWishlist = view.findViewById(R.id.btn_wishlist);
        editTextSearch = view.findViewById(R.id.et_search);

        remoteConfig = new FirebaseRemoteConfigImpl(context);



        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            editTextSearch.setTextSize(18);
        }

        btnWishlist.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName);
                if(wishlistNewPage) RouteManager.route(context, ApplinkConst.NEW_WISHLIST);
                else RouteManager.route(context, ApplinkConst.WISHLIST);
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
            searchBarAnalytics.eventTrackingSearchBar(screenName, "");
            RouteManager.route(context, searchApplink);
        });

        btnNotification.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingNotification(screenName);
            searchBarAnalytics.eventTrackingNotifCenter();
            if (userSession.isLoggedIn()) {
                RouteManager.route(context, ApplinkConst.NOTIFICATION);
            } else {
                boolean redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(RED_DOT_GIMMICK_REMOTE_CONFIG_KEY, false);
                boolean redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif();
                if(redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus){
                    notifAnalytics.trackClickGimmickNotif();
                }

                RouteManager.route(context, ApplinkConst.LOGIN);
            }
        });
    }

    public void setQuerySearch(String querySearch) {
        if (editTextSearch != null) {
            editTextSearch.setHint(querySearch);
        }
    }

    public void inflateResource(Context context) {
        inflate(context, R.layout.main_toolbar, this);
        actionAfterInflation(context, this);
    }

    public ImageView getBtnNotification() {
        return btnNotification;
    }

    public ImageView getBtnWishlist() {
        return btnWishlist;
    }

    public ImageView getBtnInbox() {
        return btnInbox;
    }
}