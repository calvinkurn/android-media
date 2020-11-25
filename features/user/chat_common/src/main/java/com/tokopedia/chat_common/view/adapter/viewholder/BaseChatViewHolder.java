package com.tokopedia.chat_common.view.adapter.viewholder;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.chat_common.R;
import com.tokopedia.chat_common.data.BaseChatViewModel;
import com.tokopedia.chat_common.data.SendableViewModel;
import com.tokopedia.chat_common.util.ChatTimeConverter;

import java.util.Date;

import static com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.ROLE_USER;

/**
 * @author by nisie on 5/9/18.
 */
public class BaseChatViewHolder<T extends Visitable> extends AbstractViewHolder<T> {

    public static final long MILISECONDS = 1000000;
    public static final long START_YEAR = 1230768000;
    protected View view;
    protected TextView hour;
    protected TextView date;
    protected ImageView chatReadStatus;
    protected LinearLayout roleContainer;
    protected TextView roleType;
    protected TextView roleName;

    public BaseChatViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        hour = itemView.findViewById(getHourId());
        date = itemView.findViewById(getDateId());
        chatReadStatus = itemView.findViewById(getChatStatusId());
        roleContainer = itemView.findViewById(getRoleContainerId());
        roleType = itemView.findViewById(getRoleId());
        roleName = itemView.findViewById(getRoleNameId());
    }

    protected void changeHourColor(@ColorInt int color) {
        if (hour != null) {
            hour.setTextColor(color);
        }
    }

    protected int getRoleNameId() {
        return R.id.tvName;
    }

    protected int getRoleId() {
        return R.id.tvRole;
    }

    protected int getHourId() {
        return R.id.hour;
    }

    protected int getDateId() {
        return R.id.date;
    }

    protected int getChatStatusId() {
        return R.id.chat_status;
    }

    protected int getRoleContainerId() {
        return R.id.llRoleUser;
    }

    @Override
    public void bind(T viewModel) {
        if (viewModel instanceof BaseChatViewModel) {

            BaseChatViewModel element = (BaseChatViewModel) viewModel;
            try {
                if (Long.parseLong(element.getReplyTime()) / MILISECONDS < START_YEAR) {

                    element.setReplyTime(String.valueOf((Long.parseLong(element.getReplyTime()) * MILISECONDS)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            view.setOnClickListener(v -> KeyboardHandler.DropKeyboard(itemView.getContext(), view));
            setHeaderDate(element);
            setBottomHour(element);
        }
    }

    protected void setBottomHour(BaseChatViewModel element) {
        String hourTime = getHourTime(element.getReplyTime());

        if (
                (hour != null && (TextUtils.isEmpty(hourTime) || !element.isShowTime())) &&
                        !alwaysShowTime()
        ) {
            hour.setVisibility(View.GONE);

        } else if (hour != null) {
            hour.setText(hourTime);
            hour.setVisibility(View.VISIBLE);
        }
    }

    protected boolean alwaysShowTime() {
        return false;
    }

    protected String getHourTime(String replyTime) {
        String hourTime;
        try {
            hourTime = ChatTimeConverter.formatTime(Long.parseLong(replyTime) /
                    MILISECONDS);
        } catch (NumberFormatException e) {
            hourTime = replyTime;
        }
        return hourTime;
    }

    protected void setHeaderDate(BaseChatViewModel element) {
        if (date == null) return;
        String time;

        try {
            long myTime = Long.parseLong(element.getReplyTime());
            myTime = myTime / MILISECONDS;
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

    protected void bindChatReadStatus(SendableViewModel element) {
        if (chatReadStatus == null) return;
        int imageResource;
        if (element.isShowTime() || alwaysShowTime()) {
            chatReadStatus.setVisibility(View.VISIBLE);
            if (element.isRead()) {
                imageResource = R.drawable.ic_chatcommon_check_read_rounded_green;
            } else {
                imageResource = R.drawable.ic_chatcommon_check_sent_rounded_grey;
            }
            if (element.isDummy()) {
                imageResource = R.drawable.ic_chatcommon_check_rounded_grey;
            }
            Drawable drawable = MethodChecker.getDrawable(chatReadStatus.getContext(), imageResource);
            if (useWhiteReadStatus() && !element.isRead()) {
                drawable.mutate();
                drawable.setColorFilter(androidx.core.content.ContextCompat.getColor(chatReadStatus.getContext(),com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP);
            } else {
                drawable.clearColorFilter();
            }
            chatReadStatus.setImageDrawable(drawable);
        } else {
            chatReadStatus.setVisibility(View.GONE);
        }
        if (element.isSender()) {
            chatReadStatus.setVisibility(View.VISIBLE);
        } else {
            chatReadStatus.setVisibility(View.GONE);
        }
    }

    protected void bindRoleHeader(SendableViewModel chat, int gravity) {
        if (roleContainer == null) {
            hideHeader();
            return;
        }
        if (
                !chat.getFromRole().isEmpty() &&
                        !chat.getFromRole().toLowerCase().equals(ROLE_USER.toLowerCase()) &&
                        chat.isSender() &&
                        !chat.isDummy() &&
                        chat.isShowRole()
        ) {
            roleType.setText(chat.getFrom());
            roleName.setText(chat.getFromRole());
            roleContainer.setVisibility(View.VISIBLE);
            roleContainer.setGravity(gravity);
        } else {
            roleContainer.setVisibility(View.GONE);
        }
    }

    protected Boolean useWhiteReadStatus() {
        return false;
    }

    private void hideHeader() {
        roleContainer.setVisibility(View.GONE);
    }

    @Override
    public void onViewRecycled() {

    }
}
