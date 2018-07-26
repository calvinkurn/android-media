package com.tokopedia.searchbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by meta on 04/07/18.
 */
public class NotificationToolbar extends Toolbar {

    public NotificationToolbar(Context context) {
        super(context);
        init();
    }

    public NotificationToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotificationToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView textViewTitle;
    private ImageButton btnNotification;
    private QBadgeView badgeView;

    private UserSession userSession;

    private void init() {

        userSession = ((AbstractionRouter) this.getContext().getApplicationContext()).getSession();

        inflate(getContext(), R.layout.notification_toolbar, this);
        textViewTitle = findViewById(R.id.textview_title);
        btnNotification = findViewById(R.id.btn_notification);

        badgeView = new QBadgeView(getContext());
        badgeView.bindTarget(btnNotification);
        badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
        badgeView.setBadgeNumber(2);

        btnNotification.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                getContext().startActivity(((SearchBarRouter) getContext().getApplicationContext())
                        .gotoNotificationPage(getContext()));
            } else {
                RouteManager.route(this.getContext(), ApplinkConst.LOGIN);
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        if (textViewTitle != null)
            textViewTitle.setText(title);
    }

    @Override
    public void setTitle(int resId) {
        if (textViewTitle != null)
            textViewTitle.setText(resId);
    }
}
