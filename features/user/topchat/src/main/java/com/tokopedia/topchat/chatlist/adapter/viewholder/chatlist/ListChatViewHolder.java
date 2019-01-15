package com.tokopedia.topchat.chatlist.adapter.viewholder.chatlist;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.util.ChatTimeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by stevenfredian on 10/25/17.
 */

public class ListChatViewHolder extends AbstractViewHolder<ChatListViewModel> {

    TextView userName;

    TextView time;

    ImageView avatar;

    TextView message;

    View mainView;

    TextView label;

    TextView counterUnread;

    ImageView checked;

    TextView section;

    InboxChatContract.View viewListener;

    InboxChatPresenter presenter;

    String[] array;

    @LayoutRes
    public static final int LAYOUT = R.layout.message_item_topchat;

    public ListChatViewHolder(View itemView, InboxChatContract.View viewListener, InboxChatPresenter presenter) {
        super(itemView);

        userName = (TextView) itemView.findViewById(R.id.user_name);
        time = (TextView) itemView.findViewById(R.id.time);
        avatar = (ImageView) itemView.findViewById(R.id.user_ava);
        message = (TextView) itemView.findViewById(R.id.message);
        mainView = itemView.findViewById(R.id.main);
        label = (TextView) itemView.findViewById(R.id.label);
        counterUnread = (TextView) itemView.findViewById(R.id.counter_unread);
        section = (TextView) itemView.findViewById(R.id.section);
        checked = (ImageView) itemView.findViewById(R.id.checked);
        this.viewListener = viewListener;
        this.presenter = presenter;

        array = new String[]{"pengguna", "chat"};
    }

    @Override
    public void bind(ChatListViewModel element) {

        if (element.isTyping()) {
            userName.setText(element.getName());
            message.setText("sedang mengetik...");
            message.setTypeface(null, Typeface.ITALIC);
            message.setTextColor(MethodChecker.getColor(message.getContext(), R.color.medium_green));
        } else {
            if (element.getSpanMode() == ChatListViewModel.SPANNED_MESSAGE) {
                message.setText(highlight(message.getContext(), element.getSpan(), viewListener.getKeyword()));
                userName.setText(element.getName());
            } else if (element.getSpanMode() == ChatListViewModel.SPANNED_CONTACT) {
                userName.setText(highlight(message.getContext(), element.getSpan(), viewListener.getKeyword()));
                message.setText(MethodChecker.fromHtml(element.getMessage().trim()));
            } else {
                message.setText(MethodChecker.fromHtml(element.getMessage().trim()));
                userName.setText(element.getName());
            }
            message.setTypeface(null, Typeface.NORMAL);
            message.setTextColor(MethodChecker.getColor(message.getContext(), R.color.black_54));
        }

        if (element.isHaveTitle()) {
            String magicString = element.getSectionSize() + " " + array[element.getSpanMode() - 1] + " ditemukan";
            section.setText(magicString);
            section.setVisibility(View.VISIBLE);
        } else {
            section.setVisibility(View.GONE);
        }

        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getImage());

        try {
            long unixdate = Long.parseLong(element.getTime());
            DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(unixdate * 1000);
            System.out.println("Formatted Date:" + formatter.format(calendar.getTime()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        setTime(element, getAdapterPosition());
        setLabel(element.getLabel());

        setSelectedStatus(viewListener.getActivity(), element, getAdapterPosition());
        setReadStatus(element);


        avatar.setOnClickListener(onGoToProfile(element, getAdapterPosition()));

        mainView.setOnClickListener(onMessageClicked(element, viewListener, getAdapterPosition()));


        mainView.setOnLongClickListener(onLongClickListener(element));
        avatar.setOnLongClickListener(onLongClickListener(element));
    }


    private SpannableString highlight(Context context, Spanned span, String keyword) {

        SpannableString spannableString = new SpannableString(span);

        int indexOfKeyword = spannableString.toString().toLowerCase().indexOf(keyword);

        while (indexOfKeyword < span.length() && indexOfKeyword >= 0) {
            spannableString.setSpan(new ForegroundColorSpan(MethodChecker.getColor(context, R.color.medium_green)), indexOfKeyword, indexOfKeyword + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            indexOfKeyword = spannableString.toString().indexOf(keyword, indexOfKeyword + keyword.length());
        }

        return spannableString;
    }

    private void setSelectedStatus(Context context, ChatListViewModel element, int position) {
        if (element.isChecked())
            setSelectedState();
        else if (!element.isChecked())
            setUnselectedState(context, position);
    }

    private void setReadStatus(ChatListViewModel element) {
        int readStatus = element.getReadStatus();
        int counter = element.getUnreadCounter();
        if (readStatus == InboxMessageConstant.STATE_CHAT_UNREAD)
            setNotReadState(counter);
        else if (readStatus == InboxMessageConstant.STATE_CHAT_READ)
            setReadState();
    }

    private void setLabel(String labelS) {
        if (labelS != null && labelS.length() > 0 && !labelS.equals(InboxChatConstant.USER_TAG)) {
            label.setVisibility(View.VISIBLE);
            label.setText(labelS);
            if (labelS.equals(InboxChatConstant.SELLER_TAG)) {
                label.setBackgroundResource(R.drawable.topchat_seller_label);
                label.setTextColor(itemView.getContext().getResources().getColor(R.color.medium_green));
            } else {
                label.setBackgroundResource(R.drawable.topchat_admin_label);
                label.setTextColor(itemView.getContext().getResources().getColor(R.color.topchat_admin_label_text_color));
            }
        } else {
            label.setVisibility(View.GONE);
        }
    }

    private void setTime(ChatListViewModel element, int position) {
        String unixTime = ChatTimeConverter.formatTimeStamp(Long.parseLong(element.getTime()));
        time.setVisibility(View.VISIBLE);
        time.setText(unixTime);
    }

    private View.OnLongClickListener onLongClickListener(final ChatListViewModel element) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (element.getSpanMode() == ChatListViewModel.NO_SPAN) {
                    int position = getAdapterPosition();
                    if (element.isChecked()) {
                        setReadState();
                        presenter.onDeselect(position);
                    } else {
                        setSelectedState();
                        presenter.onSelected(position);
                    }

                }
                return true;
            }
        };
    }

    private View.OnClickListener onMessageClicked(final ChatListViewModel element, InboxChatContract.View viewListener, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.isInActionMode()) {
                    if (element.isChecked()) {
                        setReadState();
                        presenter.onDeselect(position);
                    } else {
                        setSelectedState();
                        presenter.onSelected(position);
                    }
                } else {
                    if (presenter.getSelected() == 0) {
                        presenter.goToDetailMessage(mainView.getContext(), position, element);
                    } else if (element.isChecked()) {
                        setReadState();
                        presenter.onDeselect(position);
                    } else {
                        setSelectedState();
                        presenter.onSelected(position);
                    }
                }
            }
        };
    }

    private View.OnClickListener onGoToProfile(final ChatListViewModel messageItem, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageItem == null) {
                    return;
                }

                if (presenter.isInActionMode()) {
                    if (messageItem.isChecked()) {
                        setReadState();
                        presenter.onDeselect(position);
                    } else {
                        setSelectedState();
                        presenter.onSelected(position);
                    }
                } else {
                    if (messageItem.getLabel() != null
                            && !messageItem.getLabel().equals(InboxChatConstant.ADMIN_TAG)
                            && !messageItem.getLabel().equals(InboxChatConstant.OFFICIAL_TAG)
                            && messageItem.getSenderId() != null) {
                        if (messageItem.getLabel().equals(InboxChatConstant.SELLER_TAG)) {
                            presenter.goToShop(Integer.parseInt(messageItem.getSenderId()));
                        } else {
                            presenter.goToProfile(Integer.parseInt(messageItem.getSenderId()));
                        }
                    }
                }
            }
        };
    }

    private void setSelectedState() {
        Context context = mainView.getContext();
        mainView.setBackgroundColor(context.getResources().getColor(R.color.green_selected));
        checked.setVisibility(View.VISIBLE);
    }

    private void setUnselectedState(Context context, int position) {
        mainView.setBackgroundColor(context.getResources().getColor(R.color.white));
        checked.setVisibility(View.GONE);
    }

    private void setReadState() {
        counterUnread.setVisibility(View.GONE);
        userName.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

    }

    private void setNotReadState(int counter) {
        counterUnread.setVisibility(View.VISIBLE);
        userName.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

        if (counter > 0) {
            counterUnread.setText(String.valueOf(counter));
        } else {
            counterUnread.setVisibility(View.GONE);
        }
    }

}
