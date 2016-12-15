package com.tokopedia.core.drawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.deposit.activity.DepositActivity;
import com.tokopedia.core.drawer.model.DrawerHeader;
import com.tokopedia.core.drawer.model.DrawerItem;
import com.tokopedia.core.drawer.model.DrawerItemList;
import com.tokopedia.core.loyaltysystem.LoyaltyDetail;
import com.tokopedia.core.people.activity.PeopleInfoDrawerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

import static com.tokopedia.core.drawer.var.UserType.TYPE_PEOPLE;

/**
 * Created by Nisie on 5/08/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String DEFAULT_BANNER = "web_service-shopnocover.png";
    private static final String TAG = "DrawerAdapter";

    public interface GroupClickedListener {
        void OnExpanded(int position);

        void OnCollapsed(int position);
    }

    public interface ChildClickedListener {
        void OnClicked(int position);
    }

    public interface DrawerListener {
        void OnClosed();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView name;
        TextView deposit;
        TextView topPoint;
        ImageView coverImg;
        RelativeLayout gradientBlack;
        LinearLayout drawerPointsLayout;
        RelativeLayout saldoLayout;
        RelativeLayout topPointsLayout;
        View loadingSaldo;
        View loadingLoyalty;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.user_avatar);
            name = (TextView) itemView.findViewById(R.id.name_text);
            deposit = (TextView) itemView.findViewById(R.id.deposit_text);
            topPoint = (TextView) itemView.findViewById(R.id.toppoints_text);
            coverImg = (ImageView) itemView.findViewById(R.id.cover_img);
            gradientBlack = (RelativeLayout) itemView.findViewById(R.id.gradient_black);
            drawerPointsLayout = (LinearLayout) itemView.findViewById(R.id.drawer_points_layout);
            saldoLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_saldo);
            topPointsLayout = (RelativeLayout) itemView.findViewById(R.id.drawer_top_points);
            loadingSaldo = itemView.findViewById(R.id.loading_saldo);
            loadingLoyalty = itemView.findViewById(R.id.loading_loyalty);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView label;
        ImageView icon;
        TextView notificationAlert;
        TextView notification;
        RelativeLayout layout;
        ImageView arrow;

        public ListViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            notification = (TextView) itemView.findViewById(R.id.notif);
            notificationAlert = (TextView) itemView.findViewById(R.id.toggle_notif);
            layout = (RelativeLayout) itemView.findViewById(R.id.drawer_item);
            arrow = (ImageView) itemView.findViewById(R.id.arrow);
        }
    }

    public class SingleItemViewHolder extends RecyclerView.ViewHolder {

        TextView label;
        ImageView icon;
        TextView notification;
        RelativeLayout layout;

        public SingleItemViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            notification = (TextView) itemView.findViewById(R.id.notif);
            layout = (RelativeLayout) itemView.findViewById(R.id.drawer_item);

        }
    }

    public DrawerAdapter(Context context, List<RecyclerViewItem> data) {
        this.context = context;
        this.data = data;
    }

    private List<RecyclerViewItem> data;
    private Context context;
    private GroupClickedListener groupListener;
    private ChildClickedListener childListener;
    private DrawerListener drawerListener;
    private int drawerPosition;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TkpdState.DrawerItem.TYPE_HEADER:
                return createHeaderViewHolder(parent);
            case TkpdState.DrawerItem.TYPE_LIST:
                return createListViewHolder(parent);
            case TkpdState.DrawerItem.TYPE_ITEM:
                return createSingleItemViewHolder(parent);
            case TkpdState.DrawerItem.TYPE_SEPARATOR:
                return createSeparatorViewHolder(parent);
            default:
                return null;
        }
    }

    private RecyclerView.ViewHolder createSeparatorViewHolder(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_child_separator, parent, false);
        return new RecyclerView.ViewHolder(itemLayoutView) {
        };
    }

    private RecyclerView.ViewHolder createSingleItemViewHolder(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_item, parent, false);
        return new SingleItemViewHolder(itemLayoutView);
    }

    private RecyclerView.ViewHolder createListViewHolder(ViewGroup parent) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_group, parent, false);
        return new ListViewHolder(itemLayoutView);
    }

    private HeaderViewHolder createHeaderViewHolder(ViewGroup parent) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_header, null);
        return new HeaderViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TkpdState.DrawerItem.TYPE_HEADER:
                bindHeaderViewHolder((HeaderViewHolder) holder, position);
                break;
            case TkpdState.DrawerItem.TYPE_LIST:
                bindGroupViewHolder((ListViewHolder) holder, position);
                break;
            case TkpdState.DrawerItem.TYPE_ITEM:
                bindSingleViewHolder((SingleItemViewHolder) holder, position);
                break;
            default:
                break;
        }

    }

    private void bindSingleViewHolder(SingleItemViewHolder holder, int position) {
        DrawerItem menu = (DrawerItem) data.get(position);
        holder.label.setText(menu.label);
        setNotif(menu.notif, holder);
        setSelectedBackground(menu, holder);
        holder.icon.setImageResource(menu.iconId);
        holder.layout.setOnClickListener(onChildClicked(position));
        if (menu.iconId != 0) {
            holder.label.setTypeface(null, Typeface.BOLD);
        } else {
            holder.label.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setSelectedBackground(DrawerItem menu, SingleItemViewHolder holder) {
        if (menu.id == drawerPosition) {
            holder.label.setTextColor(context.getResources().getColor(R.color.green_500));
        } else if (menu.iconId != 0) {
            holder.label.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.label.setTextColor(context.getResources().getColor(R.color.grey_500));
        }
    }

    private View.OnClickListener onGroupExpandedListener(final DrawerItemList group, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!group.isExpanded) {
                    groupListener.OnExpanded(position);
                } else {
                    groupListener.OnCollapsed(position);
                }
            }
        };
    }

    private View.OnClickListener onChildClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childListener.OnClicked(position);
            }
        };
    }

    private void setNotif(int notif, SingleItemViewHolder holder) {
        holder.notification.setVisibility(View.VISIBLE);
        if (notif > 0) {
            holder.notification.setText(notif + "");
        } else if (notif > 999) {
            holder.notification.setText("999+");
        } else {
            holder.notification.setVisibility(View.GONE);
        }
    }

    private void bindGroupViewHolder(ListViewHolder holder, int position) {
        DrawerItemList group = (DrawerItemList) data.get(position);
        setArrow(holder, group);
        setNotification(holder, group);
        holder.label.setText(group.label);
        holder.layout.setOnClickListener(onGroupExpandedListener(group, position));
        setGroupIcon(holder, group);

    }

    private void setNotification(ListViewHolder holder, DrawerItemList group) {
        holder.notification.setVisibility(View.GONE);
        if (group.notif > 0) {
            holder.notificationAlert.setVisibility(View.VISIBLE);
        } else {
            holder.notificationAlert.setVisibility(View.GONE);

        }

    }

    private void setGroupIcon(ListViewHolder holder, DrawerItemList group) {
        if (group.iconId != 0) {
            holder.icon.setImageResource(group.iconId);
        }
    }

    private void setArrow(ListViewHolder holder, DrawerItemList group) {
        if (group.isExpandable) {
            holder.arrow.setVisibility(View.VISIBLE);
        } else {
            holder.arrow.setVisibility(View.GONE);
        }

        setArrowPosition(holder, group);

    }

    private void setArrowPosition(ListViewHolder holder, DrawerItemList group) {
        if (group.isExpanded) {
            holder.arrow.setImageResource(R.drawable.arrow_up);
        } else {
            holder.arrow.setImageResource(R.drawable.arrow_drop_down);
        }
    }

    private void bindHeaderViewHolder(HeaderViewHolder holder, int position) {

        switch (((DrawerHeader) data.get(position)).userType) {
            case TYPE_PEOPLE:
                bindHeaderPeople(holder, position);
                break;
            default:
                bindHeaderGuest(holder, position);
                break;
        }
    }

    private void bindHeaderPeople(HeaderViewHolder holder, int position) {
        DrawerHeader header = (DrawerHeader) data.get(0);
        holder.drawerPointsLayout.setVisibility(View.VISIBLE);
        holder.name.setVisibility(View.VISIBLE);
//        holder.favorite.setVisibility(View.VISIBLE);
        holder.icon.setVisibility(View.VISIBLE);
        //  holder.home.setVisibility(View.VISIBLE);

        CommonUtils.dumper("DrawerTag : ShopCover Link : " + header.shopCover);

        if (header.shopCover != null && !header.shopCover.equals("") && !header.shopCover.endsWith(DEFAULT_BANNER)) {
            holder.gradientBlack.setBackgroundResource(R.drawable.gradient_black);
            holder.coverImg.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(holder.coverImg, header.shopCover);
            holder.name.setShadowLayer(1.5f, 2, 2, R.color.trans_black_40);
        } else {
            holder.gradientBlack.setBackgroundResource(0);
            holder.coverImg.setVisibility(View.INVISIBLE);
            holder.name.setShadowLayer(0, 0, 0, 0);
        }

        if (header.userIcon != null && !header.userIcon.equals(""))
            ImageHandler.loadImageCircle2(context, holder.icon, header.userIcon);
//            ImageHandler.LoadImageCircle(holder.icon, header.userIcon);
        holder.name.setText(header.userName);
        if (header.deposit.equals("")) {
            holder.loadingSaldo.setVisibility(View.VISIBLE);
            holder.deposit.setVisibility(View.GONE);
        } else {
            holder.loadingSaldo.setVisibility(View.GONE);
            holder.deposit.setText(header.deposit);
            holder.deposit.setVisibility(View.VISIBLE);
        }

        if (header.Loyalty.equals("")) {
            holder.loadingLoyalty.setVisibility(View.VISIBLE);
            holder.topPoint.setVisibility(View.GONE);
        } else {
            holder.loadingLoyalty.setVisibility(View.GONE);
            holder.topPoint.setVisibility(View.VISIBLE);
            holder.topPoint.setText(header.Loyalty);
        }

        holder.saldoLayout.setOnClickListener(onDepositClicked());
        holder.topPointsLayout.setOnClickListener(onTopPointsClicked(header.LoyaltyUrl));
        holder.name.setOnClickListener(onPeopleClicked());
        holder.icon.setOnClickListener(onPeopleClicked());

//        holder.topupSaldoButton.setOnClickListener(onTopupSaldoClickedListener(generateTopupUrl()));

    }

    private View.OnClickListener onTopPointsClicked(final String loyaltyUrl) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.OnClosed();
                Bundle bundle = new Bundle();
                bundle.putString("url", loyaltyUrl);
                Intent intent = new Intent(context, LoyaltyDetail.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.TOPPOINTS);
                finishActivity();
            }
        };
    }

    private View.OnClickListener onDepositClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.OnClosed();
                if (drawerPosition != TkpdState.DrawerPosition.PEOPLE_DEPOSIT) {
                    Intent intent = new Intent(context, DepositActivity.class);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.DEPOSIT);
                    finishActivity();
                }
            }
        };
    }

    private void finishActivity() {
        if (drawerPosition != TkpdState.DrawerPosition.INDEX_HOME) {
            ((TActivity) context).finish();
        }
    }

    private View.OnClickListener onPeopleClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.OnClosed();

                if (drawerPosition != TkpdState.DrawerPosition.PEOPLE) {
                    context.startActivity(
                            PeopleInfoDrawerActivity.createInstance(context, SessionHandler.getLoginID(context))
                    );
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.PROFILE);
                }


            }
        };
    }

    private void bindHeaderGuest(HeaderViewHolder holder, int position) {
        holder.name.setVisibility(View.GONE);
        holder.icon.setVisibility(View.GONE);
        ImageHandler.loadImageWithId(holder.coverImg, R.drawable.drawer_header_bg);
        holder.gradientBlack.setBackgroundResource(0);
        holder.drawerPointsLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    public void setGroupClickedListener(GroupClickedListener groupListener) {
        this.groupListener = groupListener;
    }

    public void setChildClickedListener(ChildClickedListener childListener) {
        this.childListener = childListener;
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    public void setItemSelected(int drawerPosition) {
        this.drawerPosition = drawerPosition;
    }

    private void sendGTMNavigationEvent(String label) {
        UnifyTracking.eventDrawerClick(label);
    }
}
