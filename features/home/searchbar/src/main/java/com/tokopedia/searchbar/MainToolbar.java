package com.tokopedia.searchbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
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

    private ImageButton btnQrCode;
    private ImageButton btnNotification;
    private ImageButton btnWishlist;
    private EditText editTextSearch;

    private QBadgeView badgeView;

    private UserSession userSession;

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

    private void init() {

        userSession = ((AbstractionRouter) this.getContext().getApplicationContext()).getSession();

        inflate(getContext(), R.layout.main_toolbar, this);
        btnQrCode = findViewById(R.id.btn_qrcode);
        btnNotification = findViewById(R.id.btn_notification);
        btnWishlist = findViewById(R.id.btn_wishlist);
        editTextSearch = findViewById(R.id.et_search);

        badgeView = new QBadgeView(getContext());
        badgeView.bindTarget(btnNotification);
        badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
        badgeView.setBadgeNumber(2);

        btnQrCode.setOnClickListener(v ->
                getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                        .gotoQrScannerPage(getContext())));

        btnWishlist.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                        .gotoWishlistPage(getContext()));
            } else {
                RouteManager.route(this.getContext(), ApplinkConst.LOGIN);
            }
        });

        btnNotification.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                getContext().startActivity(((SearchBarRouter) this.getContext().getApplicationContext())
                        .gotoNotificationPage(getContext()));
            } else {
                RouteManager.route(this.getContext(), ApplinkConst.LOGIN);
            }
        });
    }
}
