package com.tokopedia.tkpd.shopinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.customadapter.AbstractRecyclerAdapter;
import com.tokopedia.tkpd.shopinfo.models.talkmodel.TalkModel;
import com.tokopedia.tkpd.shopinfo.models.talkmodel.TalkUserReputation;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.util.TokenHandler;
import com.tokopedia.tkpd.util.ToolTipUtils;

import java.util.List;

/**
 * Created by Tkpd_Eka on 10/29/2015.
 */
public class DiscussionAdapter extends AbstractRecyclerAdapter {

    private TokenHandler token;

    public interface DiscussionAdapterListener {
        void onOpenTalk(int pos);

        void onDeleteTalk(int pos);

        void onFollowTalk(int pos);

        void onReportTalk(int pos);

        void onUnfollowTalk(int pos);

        void onUserClick(int pos);

        void onProductClick(int pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mainView;
        ImageView prodImage;
        TextView userName;
        View reputationView;
        ImageView repIcon;
        TextView repRating;
        TextView productName;
        View butOverflow;
        TextView message;
        TextView createTime;
        TextView totalComment;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(onMainItemClick(getAdapterPosition()));
            butOverflow = view.findViewById(R.id.but_overflow);
            prodImage = (ImageView) view.findViewById(R.id.prod_image);
            userName = (TextView) view.findViewById(R.id.user_name);
            reputationView = view.findViewById(R.id.reputation_view);
            repIcon = (ImageView) view.findViewById(R.id.rep_icon);
            repRating = (TextView) view.findViewById(R.id.rep_rating);
            productName = (TextView) view.findViewById(R.id.product_name);
            message = (TextView) view.findViewById(R.id.message);
            createTime = (TextView) view.findViewById(R.id.create_time);
            totalComment = (TextView) view.findViewById(R.id.total_comment);
            mainView = view;
        }
    }

    private Context context;
    private DiscussionAdapterListener listener;
    private TalkModel modelList;
    private View.OnClickListener onRetryClick;

    public static DiscussionAdapter createAdapter(TalkModel models, Context context) {
        DiscussionAdapter adapter = new DiscussionAdapter();
        adapter.modelList = models;
        adapter.context = context;
        adapter.token = new TokenHandler();
        return adapter;
    }

    public void setAdapterListener(DiscussionAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateVHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_discussion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindVHolder(RecyclerView.ViewHolder holder, int position) {
        bindView(modelList.list.get(position), (ViewHolder) holder);
    }

    @Override
    public int getChildItemCount() {
        return modelList.list.size();
    }

    @Override
    public int getItemType(int pos) {
        return 0;
    }

    public void setRetryListener(View.OnClickListener listener){
        onRetryClick = listener;
    }

    @Override
    public View.OnClickListener getRetryClickListener() {
        return onRetryClick;
    }

    private void bindView(com.tokopedia.tkpd.shopinfo.models.talkmodel.List model, ViewHolder holder) {
        ImageHandler.LoadImage(holder.prodImage, model.talkProductImage);
        holder.userName.setText(Html.fromHtml(model.talkUserName));
        holder.productName.setText(Html.fromHtml(model.talkProductName));
        holder.message.setText(Html.fromHtml(model.talkMessage));
        holder.createTime.setText(model.talkCreateTime);
        holder.totalComment.setText("" + model.talkTotalComment);
        if (model.talkUserReputation.noReputation == 0) {
            holder.repRating.setText(model.talkUserReputation.positivePercentage + "%");
            holder.repRating.setVisibility(View.VISIBLE);
            holder.repIcon.setImageResource(R.drawable.ic_icon_repsis_smile_active);
        } else {
            holder.repRating.setVisibility(View.INVISIBLE);
            holder.repIcon.setImageResource(R.drawable.ic_icon_repsis_smile);
        }
        setListener(model, holder);
    }

    private void setListener(final com.tokopedia.tkpd.shopinfo.models.talkmodel.List model, ViewHolder holder) {
        holder.reputationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolTipUtils.showToolTip(setViewToolTip(model.talkUserReputation), view);
            }
        });
        holder.butOverflow.setOnClickListener(onOverflowClick(holder.getAdapterPosition()));
        holder.mainView.setOnClickListener(onMainItemClick(holder.getAdapterPosition()));
        holder.userName.setOnClickListener(onUserClick(holder.getAdapterPosition()));
        holder.prodImage.setOnClickListener(onProdClick(holder.getAdapterPosition()));
    }

    private View.OnClickListener onProdClick(final int adapterPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onProductClick(adapterPosition);
            }
        };
    }

    private View.OnClickListener onUserClick(final int adapterPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUserClick(adapterPosition);
            }
        };
    }

    private View.OnClickListener onMainItemClick(final int adapterPosition) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOpenTalk(adapterPosition);
            }
        };
    }


    private View.OnClickListener onOverflowClick(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOverflow(view, position);
            }
        };
    }

    private void createOverflow(View v, final int position) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(getMenuID(position), popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CommonUtils.dumper(item.getItemId());
                switch (item.getItemId()) {
                    case R.id.action_follow:
                        listener.onFollowTalk(position);
                        return true;
                    case R.id.action_unfollow:
                        listener.onUnfollowTalk(position);
                        return true;
                    case R.id.action_delete_talk:
                        listener.onDeleteTalk(position);
                        return true;
                    case R.id.action_report:
                        listener.onReportTalk(position);
                        return true;
                    default:
                        return false;
                }
            }

        });
        String userId = SessionHandler.getLoginID(context);
        if(userId.equals("")){
            popup.getMenu().findItem(R.id.action_follow).setVisible(false);
        }
        popup.show();
    }

    private int getMenuID(int position) {
        int menuID;
        com.tokopedia.tkpd.shopinfo.models.talkmodel.List talk = modelList.list.get(position);
        if (talk.getTalkShopId().equals(SessionHandler.getShopID(context))) {
            menuID = R.menu.delete_report_menu;
        } else {
            if (token.getLoginID(context).equals(talk.getTalkUserId())) {
                if (talk.getTalkFollowStatus()==1) {
                    menuID = R.menu.unfollow_delete_menu;
                } else {
                    menuID = R.menu.follow_delete_menu;
                }
            } else {
                if (talk.getTalkFollowStatus()==1) {
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

}
