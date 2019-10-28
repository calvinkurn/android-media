package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder;

import android.content.Context;
import android.os.Build;
import androidx.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * @author by nisie on 2/27/18.
 */

public class VoteAnnouncementViewHolder extends BaseChatViewHolder<VoteAnnouncementViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.vote_announcement_view_holder;

    ImageView voteIcon;
    TextView voteTitle;
    TextView voteQuestion;
    View voteLayout;
    Context context;
    private final ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener listener;

    public VoteAnnouncementViewHolder(View itemView, ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener imageListener) {
        super(itemView);
        voteIcon = itemView.findViewById(R.id.vote_icon);
        voteTitle = itemView.findViewById(R.id.vote_title);
        voteQuestion = itemView.findViewById(R.id.vote_question);
        voteLayout = itemView.findViewById(R.id.vote_layout);
        listener = imageListener;
    }

    @Override
    public void bind(final VoteAnnouncementViewModel element) {
        super.bind(element);
        switch (element.getVoteType().toLowerCase()) {
            case VoteAnnouncementViewModel.POLLING_START:
                setVoteStarted(element);
                break;
            case VoteAnnouncementViewModel.POLLING_FINISHED:
            case VoteAnnouncementViewModel.POLLING_END:
                setVoteFinished(element);
                break;
            default:
                break;
        }

        if (TextUtils.isEmpty(element.getVoteInfoViewModel().getQuestion())) {
            voteQuestion.setVisibility(View.GONE);
        } else {
            voteQuestion.setVisibility(View.VISIBLE);
            voteQuestion.setText(MethodChecker.fromHtml(element.getVoteInfoViewModel().getQuestion()));
        }
        voteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVoteComponentClicked("vote", element.getMessage(), "");
            }
        });
    }

    private void setVoteFinished(VoteAnnouncementViewModel element) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(voteIcon, R.drawable.ic_vote_inactive);
        } else {
            voteIcon.setImageDrawable(MethodChecker.getDrawable(voteIcon.getContext(),R.drawable.ic_vote_inactive));
        }

        voteTitle.setText(R.string.title_poll_finished);
        voteTitle.setTextColor(MethodChecker.getColor(voteTitle.getContext(), R.color.black_54));
    }

    private void setVoteStarted(VoteAnnouncementViewModel element) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(voteIcon, R.drawable.ic_play_dynamic_icon);
        } else {
            voteIcon.setImageDrawable(MethodChecker.getDrawable( voteIcon.getContext(),R.drawable.ic_play_dynamic_icon));
        }
        voteTitle.setText(R.string.title_poll_started);
        voteTitle.setTextColor(MethodChecker.getColor(voteTitle.getContext(), R.color.medium_green));

    }

}
