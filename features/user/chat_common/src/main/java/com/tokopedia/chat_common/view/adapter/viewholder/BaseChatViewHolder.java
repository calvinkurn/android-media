package com.tokopedia.chat_common.view.adapter.viewholder;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.chat_common.R;
import com.tokopedia.chat_common.data.BaseChatViewModel;
import com.tokopedia.chat_common.util.ChatTimeConverter;

import java.util.Date;

/**
 * @author by nisie on 5/9/18.
 */
public class BaseChatViewHolder<T extends Visitable> extends AbstractViewHolder<T> {

    protected View view;
    protected TextView hour;
    protected TextView date;

    public BaseChatViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
    }

    @Override
    public void bind(T viewModel) {
        if (viewModel instanceof BaseChatViewModel) {

            BaseChatViewModel element = (BaseChatViewModel) viewModel;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardHandler.DropKeyboard(itemView.getContext(), view);
                }
            });

            setHeaderDate(element);
            setBottomHour(element);
        }

    }

    protected void setBottomHour(BaseChatViewModel element) {
        String hourTime;

        try {
            hourTime = ChatTimeConverter.formatTime(Long.parseLong(element.getReplyTime()));
        } catch (NumberFormatException e) {
            hourTime = element.getReplyTime();
        }

        if (hour != null
                && (TextUtils.isEmpty(hourTime) || !element.isShowTime())) {
            hour.setVisibility(View.GONE);

        } else if (hour != null) {
            hour.setText(hourTime);
            hour.setVisibility(View.VISIBLE);
        }

    }

    protected void setHeaderDate(BaseChatViewModel element) {
        String time;

        try {
            long myTime = Long.parseLong(element.getReplyTime());
            Date date = new Date(myTime);
            if (DateUtils.isToday(myTime)) {
                time = itemView.getContext().getString(R.string.chat_today_date);
            } else if (DateUtils.isToday(myTime + DateUtils.DAY_IN_MILLIS)) {
                time = itemView.getContext().getString(R.string.chat_yesterday_date);
            } else {
                time = DateFormat.getLongDateFormat(itemView.getContext()).format(date);
            }
        } catch (NumberFormatException e) {
            time = element.getReplyTime();
        }

        if (date != null
                && element.isShowDate()
                && !TextUtils.isEmpty(time)) {
            date.setVisibility(View.VISIBLE);
            date.setText(time);
        } else if (date != null) {
            date.setVisibility(View.GONE);
        }
    }

    public void onViewRecycled() {

    }
}
