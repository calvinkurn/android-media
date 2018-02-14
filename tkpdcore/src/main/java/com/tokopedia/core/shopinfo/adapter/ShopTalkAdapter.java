package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalk;
import com.tokopedia.core.shopinfo.models.talkmodel.TalkUserReputation;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.ToolTipUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 11/18/16.
 */

public class ShopTalkAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_TALK = 100;
    private boolean actionsEnabled;
    private boolean isHaveNext;

    public void setActionsEnabled(boolean actionsEnabled) {
        this.actionsEnabled = actionsEnabled;
    }

    public boolean isActionsEnabled() {
        return actionsEnabled;
    }

    public interface ActionShopTalkListener {
        void onDeleteTalk(ShopTalk shopTalk);

        void onReportTalk(ShopTalk shopTalk);

        void onFollowTalk(ShopTalk shopTalk);

        void onUnfollowTalk(ShopTalk shopTalk);

        void onGoToDetail(ShopTalk shopTalk);

        void onGoToProfile(ShopTalk shopTalk);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.but_overflow)
        View overflowButton;

        @BindView(R2.id.prod_image)
        ImageView productImage;

        @BindView(R2.id.user_name)
        TextView userName;

        @BindView(R2.id.reputation_view)
        View reputationView;

        @BindView(R2.id.rep_icon)
        ImageView reputationIcon;

        @BindView(R2.id.rep_rating)
        TextView reputationRating;

        @BindView(R2.id.product_name)
        TextView productName;

        @BindView(R2.id.message)
        TextView message;

        @BindView(R2.id.create_time)
        TextView createTime;

        @BindView(R2.id.total_comment)
        TextView totalComment;

        @BindView(R2.id.main_view)
        View mainView;

        @BindView(R2.id.empty_view)
        View emptyView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    protected ArrayList<ShopTalk> list;
    private final Context context;
    ActionShopTalkListener listener;

    public ShopTalkAdapter(Context context, ActionShopTalkListener listener) {
        this.context = context;
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    public static ShopTalkAdapter createInstance(Context context, ActionShopTalkListener listener) {
        return new ShopTalkAdapter(context, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_TALK:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_discussion, null);
                ViewHolder holder = new ShopTalkAdapter.ViewHolder(itemLayoutView);
                return holder;
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TALK:
                bindShopTalk((ShopTalkAdapter.ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindShopTalk(ViewHolder holder, final int position) {
        if (!isHaveNext && position == list.size()) {
            holder.emptyView.setVisibility(View.VISIBLE);
            holder.mainView.setVisibility(View.GONE);
        } else {
            holder.emptyView.setVisibility(View.GONE);
            holder.mainView.setVisibility(View.VISIBLE);
            list.get(position).setPosition(position);
            ImageHandler.LoadImage(holder.productImage, list.get(position).getTalkProductImage());
            holder.userName.setText(list.get(position).getTalkUserName());
            holder.productName.setText(list.get(position).getTalkProductName());
            holder.message.setText(list.get(position).getTalkMessage());
            holder.createTime.setText(list.get(position).getTalkCreateTimeFmt());
            holder.totalComment.setText(list.get(position).getTalkTotalComment());
            if (list.get(position).getTalkUserReputation().getNoReputation() == 0) {
                holder.reputationRating.setText(list.get(position).getTalkUserReputation().getPositivePercentage() + "%");
                holder.reputationRating.setVisibility(View.VISIBLE);
                holder.reputationIcon.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            } else {
                holder.reputationRating.setVisibility(View.INVISIBLE);
                holder.reputationIcon.setImageResource(R.drawable.ic_icon_repsis_smile);
            }

            if (!SessionHandler.isV4Login(context))
                holder.overflowButton.setVisibility(View.GONE);
            else
                holder.overflowButton.setVisibility(View.VISIBLE);

            setListener(list.get(position), holder);
        }
    }

    private void setListener(final ShopTalk shopTalk, ViewHolder holder) {
        holder.reputationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionsEnabled)
                    ToolTipUtils.showToolTip(setViewToolTip(shopTalk.getTalkUserReputation()), view);
            }
        });
        holder.overflowButton.setOnClickListener(onOverflowClick(holder.getAdapterPosition()));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionsEnabled) {
                    listener.onGoToDetail(shopTalk);
                }
            }
        });
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionsEnabled)
                    listener.onGoToProfile(shopTalk);
            }
        });
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionsEnabled)
                    listener.onGoToDetail(shopTalk);
            }
        });
    }

    private View.OnClickListener onOverflowClick(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionsEnabled) {
                    PopupMenu popup = new PopupMenu(context, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(getMenuID(position), popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.action_follow) {
                                listener.onFollowTalk(list.get(position));
                                return true;
                            } else if (item.getItemId() == R.id.action_unfollow) {
                                listener.onUnfollowTalk(list.get(position));
                                return true;
                            } else if (item.getItemId() == R.id.action_delete
                                    || item.getItemId() == R.id.action_delete_talk) {
                                listener.onDeleteTalk(list.get(position));
                                return true;
                            } else if (item.getItemId() == R.id.action_report) {
                                listener.onReportTalk(list.get(position));
                                return true;
                            } else {
                                return false;
                            }
                        }

                    });
                    String userId = SessionHandler.getLoginID(context);
                    if (userId.equals("")) {
                        popup.getMenu().findItem(R.id.action_follow).setVisible(false);
                    }
                    popup.show();
                }
            }
        };
    }

    private int getMenuID(int position) {
        int menuID;
        ShopTalk talk = list.get(position);
        SessionHandler sessionHandler = new SessionHandler(context);
        String loginUserID = sessionHandler.getLoginID();
        if (talk.getTalkShopId().equals(sessionHandler.getShopID(context))) {
            if (loginUserID.equals(talk.getTalkUserId())) {
                menuID = R.menu.delete_menu;
            }
            else {
                menuID = R.menu.report_menu;
            }
        } else {
            if (loginUserID.equals(talk.getTalkUserId())) {
                if (talk.getTalkFollowStatus() == 1) {
                    menuID = R.menu.unfollow_delete_menu;
                } else {
                    menuID = R.menu.follow_delete_menu;
                }
            } else {
                if (talk.getTalkFollowStatus() == 1) {
                    menuID = R.menu.unfollow_report_menu;
                } else {
                    menuID = R.menu.follow_report_menu;
                }
            }
        }
        return menuID;
    }

    private View setViewToolTip(final TalkUserReputation model) {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView neutral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);
                smile.setText(model.positive);
                neutral.setText(model.neutral);
                bad.setText(model.negative);
            }

            @Override
            public void setListener() {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_TALK;
        }

    }

    public void setHaveNext(boolean haveNext) {
        isHaveNext = haveNext;
        notifyDataSetChanged();
    }

    private boolean isLastItemPosition(int position) {
        return position == list.size() + ((!isHaveNext && !list.isEmpty())? 1 : 0);
    }

    @Override
    public int getItemCount() {
        int count = list.size() + super.getItemCount() + ((!isHaveNext && !list.isEmpty())? 1 : 0);
        return count;
    }

    public ArrayList<ShopTalk> getList() {
        return list;
    }

    public void addList(List<ShopTalk> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(ShopTalk list) {
        this.list.add(list);
        notifyDataSetChanged();
    }

}
