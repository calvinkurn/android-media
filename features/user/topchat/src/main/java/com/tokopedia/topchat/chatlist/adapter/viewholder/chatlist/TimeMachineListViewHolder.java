package com.tokopedia.topchat.chatlist.adapter.viewholder.chatlist;

import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.viewmodel.TimeMachineListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.TimeMachineListViewModel;

/**
 * @author by nisie on 10/25/17.
 */

public class TimeMachineListViewHolder extends AbstractViewHolder<TimeMachineListViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.time_machine_inbox_layout;

    private final InboxChatContract.View viewListener;
    private TextView timeMachineText;

    public TimeMachineListViewHolder(View itemView, InboxChatContract.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        timeMachineText = (TextView) itemView.findViewById(R.id.time_machine_text);

        Spannable spannable = new SpannableString(MainApplication.getAppContext().getString(R.string
                .time_machine));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(MethodChecker.getColor(MainApplication
                                          .getAppContext(), R.color.medium_green));
                              }
                          }
                , MainApplication.getAppContext().getString(R.string.time_machine).indexOf
                        ("Riwayat")
                , MainApplication.getAppContext().getString(R.string.time_machine).length()
                , 0);

        timeMachineText.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void bind(final TimeMachineListViewModel element) {
        timeMachineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToTimeMachine(element.getUrl());
            }
        });
    }
}
