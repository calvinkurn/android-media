package com.tokopedia.groupchat.chatroom.view.adapter.chatroom;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.BaseChatViewHolder;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.BaseChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.UserActionViewModel;

import java.util.Calendar;
import java.util.List;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private GroupChatTypeFactory typeFactory;
    private LoadingMoreModel loadingModel;
    private boolean canLoadMore;
    private long mLastCursor = 0;

    public GroupChatAdapter(GroupChatTypeFactory typeFactory, List<Visitable> list) {
        this.list = list;
        this.typeFactory = typeFactory;
        this.loadingModel = new LoadingMoreModel();
        this.canLoadMore = true;
        this.mLastCursor = 0;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {

        if (position == list.size() - 1
                && list.get(position) instanceof BaseChatViewModel
                && !canLoadMore) {
            ((BaseChatViewModel) list.get(position)).setShowHeaderTime(true);
            ((BaseChatViewModel) list.get(position)).setHeaderTime(
                    ((BaseChatViewModel) list.get(position)).getUpdatedAt());
        } else if (position != list.size() - 1
                && list.get(position) instanceof BaseChatViewModel
                && list.get(position + 1) instanceof BaseChatViewModel
                && headerTimeIsDifferent(
                ((BaseChatViewModel) list.get(position)).getUpdatedAt(),
                ((BaseChatViewModel) list.get(position + 1)).getUpdatedAt())) {
            ((BaseChatViewModel) list.get(position)).setShowHeaderTime(true);
            ((BaseChatViewModel) list.get(position)).setHeaderTime(
                    ((BaseChatViewModel) list.get(position)).getUpdatedAt());
        } else if (list.get(position) instanceof BaseChatViewModel) {
            ((BaseChatViewModel) list.get(position)).setShowHeaderTime(false);
        }

        holder.bind(list.get(position));

    }

    private boolean headerTimeIsDifferent(long headerTimeNow, long headerTimeAbovePost) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(headerTimeNow);
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(headerTimeAbovePost);
        return newCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) != 0;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static GroupChatAdapter createInstance(GroupChatTypeFactory groupChatTypeFactory, List<Visitable> list) {
        return new GroupChatAdapter(groupChatTypeFactory, list);
    }

    public void setList(List<Visitable> listChat) {
        this.list.addAll(listChat);
        notifyDataSetChanged();
    }

    public void addListPrevious(List<Visitable> listChat) {
        int positionStart = this.list.size();
        this.list.addAll(listChat);
        notifyItemRangeInserted(positionStart, listChat.size());

        if (positionStart != 0) {
            notifyItemChanged(positionStart - 1);
        }

    }

    public void addListNext(List<Visitable> listChat) {
        this.list.addAll(0, listChat);
        notifyItemRangeInserted(0, listChat.size());
        notifyItemChanged(listChat.size());

    }

    public void addReply(ChatViewModel chatItem) {
        this.list.add(0, chatItem);
        if (!TextUtils.isEmpty(chatItem.getMessageId())) {
            mLastCursor = Long.parseLong(chatItem.getMessageId());
        }
    }

    public void addDummyReply(PendingChatViewModel pendingChatViewModel) {
        list.add(0, pendingChatViewModel);
    }

    public void removeDummy(PendingChatViewModel pendingChatViewModel) {
        list.remove(pendingChatViewModel);
    }

    public void setRetry(PendingChatViewModel pendingChatViewModel) {
        if (list.get(list.indexOf(pendingChatViewModel)) instanceof PendingChatViewModel) {
            ((PendingChatViewModel) list.get(list.indexOf(pendingChatViewModel))).setRetry(true);
        }
        notifyItemChanged(list.indexOf(pendingChatViewModel));
    }

    public void addIncomingMessage(Visitable messageItem) {
        this.list.add(0, messageItem);

    }

    public void addAction(UserActionViewModel userActionViewModel) {
        this.list.add(0, userActionViewModel);
    }

    public void showLoadingPrevious() {
        if (!this.list.contains(loadingModel)) {
            this.list.add(list.size(), loadingModel);
            notifyItemInserted(list.size());
        }
    }

    public void showLoadingNext() {
        if (!this.list.contains(loadingModel)) {
            this.list.add(0, loadingModel);
            notifyItemInserted(0);
        }
    }

    public void dismissLoadingNext() {
        this.list.remove(loadingModel);
        notifyItemRemoved(0);

    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public long getlastCursor() {
        return mLastCursor;
    }

    public void setCursor(Visitable chatItem) {
        if (chatItem instanceof BaseChatViewModel
                && !TextUtils.isEmpty(((BaseChatViewModel) chatItem).getMessageId())) {
            mLastCursor = Long.parseLong(((BaseChatViewModel) chatItem).getMessageId());
        }
    }


    public void dismissLoadingPrevious() {
        this.list.remove(loadingModel);
        notifyItemRemoved(list.size());

    }

    public void replaceData(List<Visitable> listChat) {
        this.list.clear();
        this.list.addAll(listChat);
        this.notifyDataSetChanged();
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void deleteMessage(long msgId) {
        for (Visitable visitable : list) {
            if (visitable instanceof BaseChatViewModel
                    && ((BaseChatViewModel) visitable).getMessageId().equals(String.valueOf(msgId))) {
                int position = list.indexOf(visitable);
                list.remove(visitable);
                notifyItemRemoved(position);

                break;
            }
        }
    }

    public void updateMessage(Visitable updatedMessage) {
        if (updatedMessage instanceof BaseChatViewModel) {
            for (Visitable visitable : list) {
                if (visitable instanceof BaseChatViewModel
                        && ((BaseChatViewModel) visitable).getMessageId()
                        .equals(String.valueOf(((BaseChatViewModel) updatedMessage).getMessageId()))) {
                    int position = list.indexOf(visitable);
                    list.remove(visitable);
                    list.add(position, updatedMessage);
                    notifyItemChanged(position);
                    break;
                }
            }
        }
    }

    @Nullable
    public Visitable getItemAt(int position) {
        if (position < list.size() && position >= 0) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void onViewRecycled(AbstractViewHolder holder) {
        super.onViewRecycled(holder);

        if(holder instanceof BaseChatViewHolder){
            ((BaseChatViewHolder)holder).onViewRecycled();
        }
    }

    public List<Visitable> getList() {
        return list;
    }
}
