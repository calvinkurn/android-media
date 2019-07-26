package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * @author by nisie on 2/27/18.
 */

public class GamificationViewHolder extends AbstractViewHolder<GroupChatPointsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_chat_gamification;

    private TextView messageView;
    private View mainView;
    private ImageView icon;
    private final ChatroomContract.ChatItem.GroupChatPointsViewHolderListener listener;

    public GamificationViewHolder(View itemView, ChatroomContract.ChatItem.GroupChatPointsViewHolderListener imageListener) {
        super(itemView);
        mainView = itemView;
        messageView = itemView.findViewById(R.id.text);
        icon = itemView.findViewById(R.id.icon);
        listener = imageListener;
    }

    @Override
    public void bind(final GroupChatPointsViewModel element) {

        Context context = itemView.getContext();
        setIcon(context, element.getType());


        String text = String.format("%s <b>" + context.getString(R.string.check_now) + "</b>",
                element.getText());
        messageView.setText(MethodChecker.fromHtml(text));
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPointsClicked(element.getUrl());
            }
        });
    }

    private void setIcon(Context context, String type) {
        switch (type) {
            case GroupChatPointsViewModel.TYPE_POINTS:
                loadIcon(context,R.drawable.ic_point);
                break;
            case GroupChatPointsViewModel.TYPE_COUPON:
                loadIcon(context,R.drawable.ic_coupon);
                break;
            case GroupChatPointsViewModel.TYPE_LOYALTY:
                loadIcon(context,R.drawable.ic_loyalty);
                break;
            default:
                loadIcon(context,R.drawable.ic_loyalty);
        }
    }

    private void loadIcon( Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(icon, resId);
        } else {
            icon.setImageDrawable(MethodChecker.getDrawable(context,resId));
        }
    }

}
