package com.tokopedia.groupchat.vote.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;

import java.util.Locale;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteBarViewHolder extends AbstractViewHolder<VoteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.vote_option_bar;
    private final ChannelVoteContract.View.VoteOptionListener viewListener;
    private ProgressBar progressBar;
    private TextViewCompat option;
    private TextView percent;

    public VoteBarViewHolder(View itemView, ChannelVoteContract.View.VoteOptionListener viewListener) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
        option = itemView.findViewById(R.id.text_view);
        percent = itemView.findViewById(R.id.percent);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final VoteViewModel element) {

        Context context = itemView.getContext();
        if (element.getSelected() == VoteViewModel.DEFAULT) {
            percent.setVisibility(View.GONE);
            progressBar.setProgress(0);
            progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_bar_default));
            option.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            percent.setTextColor(MethodChecker.getColor(context, R.color.black_38));
        } else {
            percent.setVisibility(View.VISIBLE);
            progressBar.setProgress(element.getPercentageInteger());
            if (element.getSelected() == VoteViewModel.SELECTED) {
                option.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                        (context, R.drawable.ic_checked), null);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_bar_selected));
            } else if (element.getSelected() == VoteViewModel.UNSELECTED) {
                option.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_bar_unselected));
            }
        }

        // import com.tokopedia.abstraction.common.utils.view.MethodChecker;

        // kiri
        .setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (context, R.drawable.), null, null , null);

        // kanan
        .setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                (context, R.drawable.), null);

        // kanan dan kiri
        .setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (context, R.drawable.), null, MethodChecker.getDrawable
                (context, R.drawable.), null);

        option.setText(element.getOption());
        percent.setText(String.format(Locale.getDefault(), "%d%%", element.getPercentageInteger()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onVoteOptionClicked(element);
            }
        });
    }
}
