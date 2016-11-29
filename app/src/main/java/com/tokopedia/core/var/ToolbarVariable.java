package com.tokopedia.core.var;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by Nisie on 12/08/15.
 */
public class ToolbarVariable {


    public interface OnDrawerToggleClickListener {
        void onDrawerToggleClick();
    }

    protected static final int TYPE_MAIN = 0;
    private static final int TYPE_DETAIL = 1;
    private static final int TYPE_SEARCH = 2;

    protected ViewHolder holder;
    protected Model model;
    private AppCompatActivity context;
    protected int type;
    private OnDrawerToggleClickListener listener;

    public class Model {
        NotificationItem notification = new NotificationItem(context);
    }

    public class ViewHolder {
        Toolbar toolbar;
        View notif;
        View title;
        View searchView;
        ImageView drawerToggle;
        TextView notifRed;
        TextView titleTextView;
    }

    public void setTitleText(String text){
        if(text != null && !text.isEmpty()) {
            holder.titleTextView.setText(text);
        }
    }

    public ToolbarVariable(AppCompatActivity context) {
        this.context = context;
    }

    public void createToolbarWithDrawer() {
        holder = new ViewHolder();
        model = new Model();
        this.type = TYPE_MAIN;
        initView();
        initListener();
        setAsActionBar();
    }

    public void createToolbarWithoutDrawer() {
        holder = new ViewHolder();
        this.type = TYPE_DETAIL;
        initView();
        setAsActionBar();
    }

    public void createToolbarWithSearchBox() {
        holder = new ViewHolder();
        this.type = TYPE_SEARCH;
        initView();
        initListener();
        setAsActionBar();
    }

    private void initListener() {
        holder.drawerToggle.setOnClickListener(onDrawerToggleClicked());
    }

    private View.OnClickListener onDrawerToggleClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDrawerToggleClick();
            }
        };
    }

    public void setSearchViewClickListener(View.OnClickListener clickListener) {
        holder.searchView.setOnClickListener(clickListener);
    }


    protected void initView() {
        initView((Toolbar) context.findViewById(R.id.app_bar));
    }

    protected void initView(Toolbar toolbar){
        holder.toolbar = toolbar;
        holder.toolbar.removeAllViews();
        switch (type) {
            case TYPE_MAIN: {
                initViewMain();
                break;
            }
            case TYPE_SEARCH: {
                initSearchView();
            }
            default: {
                initViewDetail();
            }
        }
    }

    private void initViewDetail() {
        initTitle();
    }

    private void initViewMain() {
        initNotif();
        initTitle();
    }

    protected void setAsActionBar() {
        switch (type) {
            case TYPE_DETAIL: {
                context.setSupportActionBar(holder.toolbar);
                context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            }
            default: {
                context.setSupportActionBar(holder.toolbar);
                break;
            }
        }
        context.setSupportActionBar(holder.toolbar);
        context.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initSearchView() {
        View view = context.getLayoutInflater().inflate(R.layout.custom_action_bar_searchview, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        holder.searchView = view.findViewById(R.id.search_container);
        holder.notif = view.findViewById(R.id.burger_menu);
        holder.drawerToggle = (ImageView) holder.notif.findViewById(R.id.toggle_but_ab);
        holder.notifRed = (TextView) holder.notif.findViewById(R.id.toggle_count_notif);
        holder.toolbar.addView(view);
    }

    private void initTitle() {
        holder.title = context.getLayoutInflater().inflate(R.layout.custom_action_bar_title, null);
        holder.titleTextView = (TextView) holder.title.findViewById(R.id.actionbar_title);
        holder.titleTextView.setText(context.getTitle());
        holder.toolbar.addView(holder.title);
    }

    private void initNotif() {
        holder.notif = context.getLayoutInflater().inflate(
                R.layout.custom_actionbar_drawer_notification, null);
        holder.drawerToggle = (ImageView) holder.notif.findViewById(R.id.toggle_but_ab);
        holder.notifRed = (TextView) holder.notif.findViewById(R.id.toggle_count_notif);
        holder.toolbar.addView(holder.notif);
    }

    public void setOnDrawerToggleClick(OnDrawerToggleClickListener listener) {
        this.listener = listener;
    }

    public void updateToolbar(NotificationItem notificationItem) {
        model.notification = notificationItem;
        int notificationCount;
        if(SessionHandler.isUserSeller(context)) {
            notificationCount = model.notification.getNotifShop() + model.notification.getNotifMessageSellerSpecific();
        } else {
            notificationCount = model.notification.getTotalNotif();
        }

        if (notificationCount <= 0) {
            holder.notifRed.setVisibility(View.GONE);
        } else {
            holder.notifRed.setVisibility(View.VISIBLE);
            String totalNotif = model.notification.getTotalNotif() > 999 ? "999+" : String.valueOf(model.notification.getTotalNotif());
            holder.notifRed.setText(totalNotif);
        }

        if (Build.VERSION.SDK_INT > 16) {
            if (model.notification.isUnread()) {
                holder.notifRed.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
            } else {
                holder.notifRed.setBackground(context.getResources().getDrawable(R.drawable.red_circle));
            }
        } else {
            if (model.notification.isUnread()) {
                holder.notifRed.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.green_circle));
            } else {
                holder.notifRed.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_circle));
            }
        }
    }

    public void setTitle(int res_id) {
        if (holder.titleTextView != null)
            holder.titleTextView.setText(context.getString(res_id));
    }

    public void setTitle(String title) {
        if (holder.titleTextView != null)
            holder.titleTextView.setText(title);
    }

}
