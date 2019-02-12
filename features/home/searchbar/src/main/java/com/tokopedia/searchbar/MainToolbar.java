package com.tokopedia.searchbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.badge.BadgeView;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by meta on 22/06/18.
 */
public class MainToolbar extends Toolbar {

    private final static String TAG_INBOX = "inbox";

    private ImageButton btnNotification;
    private ImageButton btnWishlist;
    private BadgeView badgeView;

    private SearchBarAnalytics searchBarAnalytics;
    private UserSessionInterface userSession;

    private String screenName = "";

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
            if (badgeView == null)
                badgeView = new BadgeView(getContext());

            badgeView.bindTarget(btnNotification);
            badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
            badgeView.setBadgeNumber(badgeNumber);
        }
    }

    public void showInboxIconForAbTest(boolean shouldShowInbox) {
        if (shouldShowInbox) {
            btnWishlist.setTag(TAG_INBOX);
            btnWishlist.setImageResource(R.drawable.ic_inbox_searcbar);
        } else {
            btnWishlist.setTag("");
            btnWishlist.setImageResource(R.drawable.ic_wishlist_searchbar);
        }
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        userSession = new UserSession(context);
        searchBarAnalytics = new SearchBarAnalytics(this.getContext());

        inflate(context, R.layout.main_toolbar, this);
        ImageButton btnQrCode = findViewById(R.id.btn_qrcode);
        btnNotification = findViewById(R.id.btn_notification);
        btnWishlist = findViewById(R.id.btn_wishlist);
        EditText editTextSearch = findViewById(R.id.et_search);

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

        btnQrCode.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingSqanQr();
            getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                    .gotoQrScannerPage(false));
        });

        btnWishlist.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                if (btnWishlist.getTag() != null && btnWishlist.getTag().toString()
                        .equalsIgnoreCase(TAG_INBOX)) {
                    searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.INBOX, screenName);
                    getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                            .gotoInboxMainPage(getContext()));
                } else {
                    searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName);
                    getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                            .gotoWishlistPage(getContext()));
                }
            } else {
                RouteManager.route(context, ApplinkConst.LOGIN);
            }
        });

        editTextSearch.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingSearchBar();
            getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                    .gotoSearchPage(getContext()));
        });

        btnNotification.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingNotification(screenName);
            if (userSession.isLoggedIn()) {
                RouteManager.route(context, ApplinkConst.NOTIFICATION);
            } else {
                RouteManager.route(context, ApplinkConst.LOGIN);
            }
        });
    }

    public ImageButton getBtnNotification() {
        return btnNotification;
    }

    public ImageButton getBtnWishlist() {
        return btnWishlist;
    }
}