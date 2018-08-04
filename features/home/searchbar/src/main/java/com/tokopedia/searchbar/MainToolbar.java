package com.tokopedia.searchbar;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by meta on 22/06/18.
 */
public class MainToolbar extends Toolbar {

    private ImageButton btnNotification;
    private ImageButton btnWishlist;

    private QBadgeView badgeView;

    private UserSession userSession;

    private SearchBarAnalytics searchBarAnalytics;

    public MainToolbar(Context context) {
        super(context);
        init();
    }

    public MainToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setNotificationNumber(int badgeNumber) {
        if (btnNotification != null) {
            if (badgeView == null)
                badgeView = new QBadgeView(getContext());

            badgeView.bindTarget(btnNotification);
            badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
            badgeView.setBadgeNumber(badgeNumber);
        }
    }

    private void init() {

        userSession = ((AbstractionRouter) this.getContext().getApplicationContext()).getSession();
        searchBarAnalytics = new SearchBarAnalytics(this.getContext());

        inflate(getContext(), R.layout.main_toolbar, this);
        ImageButton btnQrCode = findViewById(R.id.btn_qrcode);
        btnNotification = findViewById(R.id.btn_notification);
        btnWishlist = findViewById(R.id.btn_wishlist);
        EditText editTextSearch = findViewById(R.id.et_search);

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            editTextSearch.setTextSize(18);
        }

        btnQrCode.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingSqanQr();
            getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                    .gotoQrScannerPage(getContext()));
        });

        btnWishlist.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingWishlist();
            if (userSession.isLoggedIn()) {
                getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                        .gotoWishlistPage(getContext()));
            } else {
                RouteManager.route(this.getContext(), ApplinkConst.LOGIN);
            }
        });

        editTextSearch.setOnClickListener(v -> {
            getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                    .gotoSearchPage(getContext()));
        });

        btnNotification.setOnClickListener(v -> {
            searchBarAnalytics.eventTrackingNotification();
            if (userSession.isLoggedIn()) {
                getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                        .gotoNotificationPage(getContext()));
            } else {
                RouteManager.route(this.getContext(), ApplinkConst.LOGIN);
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
