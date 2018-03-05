package com.tokopedia.core.shopinfo.adapter;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.talkview.adapter.TalkViewAdapter;
import com.tokopedia.core.talkview.fragment.TalkViewFragment;
import com.tokopedia.core.talkview.method.DeleteTalkDialog;
import com.tokopedia.core.talkview.method.ReportTalkDialog;
import com.tokopedia.core.talkview.model.TalkBaseModel;
import com.tokopedia.core.talkview.product.model.CommentTalk;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.TokenHandler;
import com.tokopedia.core.util.ToolTipUtils;

import java.util.List;

/**
 * Created by stevenfredian on 8/30/16.
 */
public class ShopTalkViewAdapter extends TalkViewAdapter {

    public static ShopTalkViewAdapter createAdapter(TalkViewFragment fragment, List<TalkBaseModel> items, TkpdCoreRouter tkpdCoreRouter) {
        ShopTalkViewAdapter adapter = new ShopTalkViewAdapter();
        adapter.fragment = fragment;
        adapter.items = items;
        adapter.token = new TokenHandler();
        adapter.tkpdCoreRouter = tkpdCoreRouter;
        return adapter;
    }

    public TkpdCoreRouter tkpdCoreRouter;

    @Override
    protected void bindTalkView(TalkViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        label = LabelUtils.getInstance(context, holder.userView);
        final CommentTalk talk = (CommentTalk) items.get(position);
        holder.timeView.setText(talk.getCommentCreateTime());
        if(talk.getCommentCreateTime().equals(context.getString(R.string.title_sending))){
            bindSendTalk(holder, talk);
        }else{
            bindShowTalk(context, holder, talk, position);
        }
    }

    private void bindSendTalk(TalkViewHolder holder, CommentTalk talk) {
        if (talk.getCommentIsSeller() == 1) {
            holder.userView.setText(talk.getCommentShopName());
        }else if (talk.getCommentIsModerator() == 1) {
            holder.userView.setText(talk.getCommentUserName());
        }else {
            holder.userView.setText(talk.getCommentUserName());
        }
        holder.messageView.setText(talk.getCommentMessage());
        holder.userImageView.setVisibility(View.INVISIBLE);
        holder.reputation.setVisibility(View.GONE);
        holder.reputationUser.setVisibility(View.GONE);
        holder.buttonOverflow.setVisibility(View.INVISIBLE);
    }

    protected void bindShowTalk(final Context context, TalkViewHolder holder, final CommentTalk talk, final int position) {
        holder.userImageView.setVisibility(View.VISIBLE);
        holder.buttonOverflow.setVisibility(View.VISIBLE);
        if (talk.getCommentIsSeller() == 1) {
            holder.userView.setText(talk.getCommentShopName());
            ImageHandler.LoadImageWGender(holder.userImageView, talk.getCommentShopImage(),
                    (Activity) context, talk.getCommentUserGender());
            holder.reputation.setVisibility(View.VISIBLE);
            holder.reputationUser.setVisibility(View.GONE);
            ReputationLevelUtils.setReputationMedals(context, holder.reputation,
                    talk.getCommentShopReputation().getReputationBadge().getSet(),
                    talk.getCommentShopReputation().getReputationBadge().getLevel(),
                    talk.getCommentShopReputation().getReputationScore());
        } else if (talk.getCommentIsModerator() == 1) {
            holder.userView.setText(talk.getCommentUserName());
            ImageHandler.LoadImageWGender(holder.userImageView, talk.getCommentUserImage(),
                    (Activity) context, talk.getCommentUserGender());
            holder.reputation.setVisibility(View.GONE);
            holder.reputationUser.setVisibility(View.GONE);
        } else {
            holder.userView.setText(talk.getCommentUserName());
            ImageHandler.LoadImageWGender(holder.userImageView, talk.getCommentUserImage(),
                    (Activity) context, talk.getCommentUserGender());
            holder.reputation.setVisibility(View.GONE);
            holder.reputationUser.setVisibility(View.VISIBLE);
            holder.textReputation.setText(String.format("%s%%", talk.getCommentUserReputation().getPositivePercentage()));
            if (talk.getCommentUserReputation().getNoReputation() == 0) {
                holder.iconReputation.setImageResource(R.drawable.ic_icon_repsis_smile_active);
                holder.textReputation.setVisibility(View.VISIBLE);
            } else {
                holder.iconReputation.setImageResource(R.drawable.ic_icon_repsis_smile);
                holder.textReputation.setVisibility(View.GONE);
            }
        }
        holder.reputationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(showToolTip(context, talk), v);
            }
        });

        holder.userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (talk.getCommentIsSeller() == 1) {
                    Intent intent = tkpdCoreRouter.getShopPageIntent(context, talk.getCommentShopId());
                    context.startActivity(intent);
                } else {
                    context.startActivity(
                            PeopleInfoNoDrawerActivity.createInstance(context, String.valueOf(talk.getCommentUserId()))
                    );
                }
            }
        });

        holder.messageView.setText(talk.getCommentMessageSpanned());
        holder.messageView.setMovementMethod(new SelectableSpannedMovementMethod());
        label.giveLabel(talk.getCommentUserLabel());

        if (SessionHandler.isV4Login(context)) {
            holder.buttonOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v, talk, position);
                }
            });
        } else {
            holder.buttonOverflow.setVisibility(View.GONE);
        }
    }

    protected View showToolTip(Context context, final CommentTalk commentTalk) {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView netral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);

                smile.setText(commentTalk.getCommentUserReputation().getPositive());
                netral.setText(commentTalk.getCommentUserReputation().getNeutral());
                bad.setText(commentTalk.getCommentUserReputation().getNegative());
            }

            @Override
            public void setListener() {

            }
        });
    }

    public void showPopup(final View v, final CommentTalk talk, final int position) {
        final Context context = v.getContext();
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        if (getMenuID(context, talk) != 0)
            inflater.inflate(getMenuID(context, talk), popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogFragment dialog;
                if (item.getItemId() == R.id.action_delete) {
                    dialog = DeleteTalkDialog.createInstance(deleteListener(talk, position));
                    dialog.show(fragment.getFragmentManager(), DeleteTalkDialog.FRAGMENT_TAG);
                    return true;
                } else if (item.getItemId() == R.id.action_report) {
                    dialog = ReportTalkDialog.createInstance(reportListener(talk, position));
                    dialog.show(fragment.getFragmentManager(), ReportTalkDialog.FRAGMENT_TAG);
                    return true;
                } else {
                    return false;
                }
            }

        });
        popup.show();
    }

    private int getMenuID(Context context, CommentTalk talk) {
        int menuID ;
        String userID = String.valueOf(talk.getCommentUserId());
        if (talk.getCommentIsOwner() == 1) {
            if (!token.getLoginID(context).equals(userID)) {
                menuID = R.menu.delete_report_menu;
            } else {
                menuID = R.menu.delete_menu;
            }
        } else {
            if (token.getLoginID(context).equals(userID)) {
                menuID = R.menu.delete_menu;
            } else {
                menuID = R.menu.report_menu;
            }
        }
        return menuID;
    }
    protected ReportTalkDialog.ReportTalkListener reportListener(final CommentTalk talk, final int position) {
        return  new ReportTalkDialog.ReportTalkListener() {
            @Override
            public void reportTalk(String s) {
                fragment.reportCommentTalk(talk, position);
            }
        };
    }

    protected DeleteTalkDialog.DeleteTalkListener deleteListener(final CommentTalk talk, final int position) {
        return new DeleteTalkDialog.DeleteTalkListener() {
            @Override
            public void deleteTalk() {
                fragment.setSwipe(true);
                fragment.deleteCommentTalk(talk,position);
            }
        };
    }
}
