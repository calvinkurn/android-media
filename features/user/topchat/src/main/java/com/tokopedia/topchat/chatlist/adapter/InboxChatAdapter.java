package com.tokopedia.topchat.chatlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.topchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.EmptyChatModel;
import com.tokopedia.topchat.common.InboxMessageConstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by stevenfredian on 10/25/17.
 */

public class InboxChatAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private static final int MAX_MESSAGE_DELETE = 10;
    private final InboxChatTypeFactory typeFactory;
    private List<Visitable> list;
    private List<Visitable> listSearch;
    private List<Pair> listMove;
    private EmptyModel emptyModel;
    private LoadingMoreModel loadingModel;
    private InboxChatPresenter presenter;
    private EmptyChatModel emptyChatModel;
    private EmptyChatModel emptySearchModel;

    public InboxChatAdapter(InboxChatTypeFactory typeFactory, InboxChatPresenter presenter) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
        this.listSearch = new ArrayList<>();
        this.emptyModel = new EmptyModel();
        this.emptyChatModel = new EmptyChatModel();
        this.emptySearchModel = new EmptyChatModel(EmptyChatModel.SEARCH);
        this.loadingModel = new LoadingMoreModel();
        this.presenter = presenter;
        this.listMove = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        if (list.get(position) instanceof ChatListViewModel) {
            showTitle(holder.itemView.getContext(), holder.getAdapterPosition());
        }
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    private void showTitle(Context context, int position) {
        if (list.get(position) instanceof ChatListViewModel) {
            ChatListViewModel now = (ChatListViewModel) list.get(position);
            if (position > 0) {
                if (list.get(position - 1) instanceof ChatListViewModel) {
                    ChatListViewModel prev = (ChatListViewModel) list.get(position - 1);

                    if (now.getSectionSize() > 0 && !compareType(now.getSpanMode(), prev.getSpanMode())) {
                        now.setHaveTitle(true);
                    }
                }

            } else {
                if (now.getSectionSize() > 0) {
                    now.setHaveTitle(true);
                }
            }
        }
    }

    private boolean compareType(int recentMode, int prevMode) {
        return recentMode == prevMode;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Visitable> getList() {
        return list;
    }

    public List<Pair> getListMove() {
        return listMove;
    }

    public void addChecked(int position) {
        if (position != -1
                && !list.isEmpty()
                && list.size() > position
                && listMove.size() + 1 <= MAX_MESSAGE_DELETE) {
            ChatListViewModel item = (ChatListViewModel) list.get(position);
            item.setChecked(true);
            Pair<ChatListViewModel, Integer> pair = new Pair<>(item, position);
            listMove.add(pair);
            notifyItemChanged(position);
        } else if (!list.isEmpty() && list.size() > position) {
            removeChecked(position);
            presenter.getView().showErrorWarningDelete(MAX_MESSAGE_DELETE);
        }
    }

    public void removeWithMessageId(String messageId) {
        if (list != null && !list.isEmpty() && !TextUtils.isEmpty(messageId)) {
//            for (Visitable visitable : list) {
//                if (visitable instanceof ChatListViewModel
//                        && messageId.equals(((ChatListViewModel) visitable).getId())) {
//                    int position = list.indexOf(visitable);
//                    list.remove(visitable);
////                    notifyItemRemoved(position);
//                }
//            }

            for (Iterator<Visitable> iterator = list.iterator(); iterator.hasNext(); ) {
                Visitable visitable = iterator.next();
                if (visitable instanceof ChatListViewModel
                        && messageId.equals(((ChatListViewModel) visitable).getId())) {
                    int position = list.indexOf(visitable);
                    iterator.remove();
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public void removeChecked(int position) {
        if (position != -1
                && !list.isEmpty()
                && list.size() > position) {
            ChatListViewModel item = (ChatListViewModel) list.get(position);
            item.setChecked(false);
            for (Pair pair : listMove) {
                if (position == (int) pair.second) {
                    listMove.remove(pair);
                    break;
                }
            }
            notifyItemChanged(position);
        }
    }

    public void setList(List<Visitable> newList) {
        if (newList != null) {
            List<Visitable> temp = newList;
            int size = this.list.size();
            this.list.clear();
            notifyItemRangeRemoved(0, size);
            this.list.addAll(temp);
            notifyItemRangeInserted(0, temp.size());
        }
    }

    public void addList(ArrayList<Visitable> newList) {
        if (newList != null) {
            int index = this.list.size();
            this.list.addAll(newList);
            notifyItemRangeInserted(index, newList.size());
        }
    }


    public void addList(int index, ArrayList<Visitable> newList) {
        if (newList != null) {
            this.list.addAll(index, newList);
            notifyItemRangeInserted(index, newList.size());
        }
    }

    public void showLoading() {
        this.list.add(loadingModel);
        notifyItemInserted(list.size());
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
        notifyItemRemoved(list.size() - 1);
    }


    public boolean containLoading() {
        return this.list.contains(loadingModel);
    }

    public void showEmptyFull(boolean b) {
        if (b) {
            this.list.add(emptyChatModel);
            notifyItemInserted(0);
        } else {
            if (list.contains(emptyChatModel)) {
                this.list.remove(emptyChatModel);
                notifyItemRemoved(0);
            }
        }
    }

    public void showEmptySearch(boolean b) {
        if (b) {
            this.list.add(emptySearchModel);
            notifyItemInserted(0);
        } else {
            if (list.contains(emptySearchModel)) {
                this.list.remove(emptySearchModel);
                notifyItemRemoved(0);
            }
        }
    }

    public void showLoadingFull(boolean b) {

    }

    public boolean checkLoadMore(int index) {
        if (index >= 0) {
            return (list.get(index) instanceof LoadingMoreModel);
        }
        return false;
    }

    public void moveToTop(String messageId, String lastReply, WebSocketResponse response, boolean
            showNotif, boolean isMoveToTop) {
        boolean isNew = true;
        String currentId;
        for (int i = 0; i < list.size(); i++) {
            try {
                ChatListViewModel temp = (ChatListViewModel) list.get(i);
                currentId = String.valueOf(temp.getId());
                if (currentId.equals(messageId)) {

                    if(!isMoveToTop){
                        break;
                    }

                    if (showNotif) {
                        int unread = temp.getUnreadCounter();
                        unread++;
                        temp.setMessage(MethodChecker.fromHtml(lastReply.trim()).toString());
                        temp.setUnreadCounter(unread);
                        temp.setReadStatus(InboxMessageConstant.STATE_CHAT_UNREAD);
                        temp.setTime(String.valueOf(new Date().getTime()));
                        temp.setTyping(false);
                    } else {
                        temp.setMessage(MethodChecker.fromHtml(lastReply.trim()).toString());
                        temp.setUnreadCounter(0);
                        temp.setReadStatus(InboxMessageConstant.STATE_CHAT_READ);
                        temp.setTime(String.valueOf(new Date().getTime()));
                        temp.setTyping(false);
                    }
                    list.remove(i);
                    notifyItemRemoved(i);
                    list.add(0, temp);
                    notifyItemInserted(0);
                    notifyItemRangeChanged(0, i);
                    presenter.moveViewToTop();
                    isNew = false;
                    break;
                }

            } catch (ClassCastException e) {
                e.printStackTrace();
                break;
            }
        }
        if (isNew && response != null) {
            ChatListViewModel temp = new ChatListViewModel();
            temp.setId(messageId);
            temp.setUnreadCounter(1);
            temp.setMessage(MethodChecker.fromHtml(lastReply.trim()).toString());
            temp.setReadStatus(InboxMessageConstant.STATE_CHAT_UNREAD);
            temp.setTime(response.getData().getMessage().getTimeStampUnix());
            temp.setName(response.getData().getFrom());
            temp.setRole(response.getData().getFromRole());
            temp.setImage(response.getData().getImageUri());
            if (this.list.size() == 1 && list.get(0) instanceof EmptyChatModel) {
                this.list.clear();
                this.list.add(0, temp);
                notifyDataSetChanged();
            } else {
                list.add(0, temp);
                notifyItemInserted(0);
                notifyItemRangeChanged(0, 1);
            }
            presenter.moveViewToTop();

        }
    }

    public void updateListCache(String messageId, String lastReply, boolean showNotif,
                                List<Visitable> list) {
        String currentId;
        for (int i = 0; i < list.size(); i++) {
            try {
                ChatListViewModel temp = (ChatListViewModel) list.get(i);
                currentId = String.valueOf(temp.getId());
                if (currentId.equals(messageId)) {
                    if (showNotif) {
                        int unread = temp.getUnreadCounter();
                        unread++;
                        temp.setMessage(MethodChecker.fromHtml(lastReply.trim()).toString());
                        temp.setUnreadCounter(unread);
                        temp.setReadStatus(InboxMessageConstant.STATE_CHAT_UNREAD);
                        temp.setTime(String.valueOf(new Date().getTime()));
                        temp.setTyping(false);
                    } else {
                        temp.setMessage(MethodChecker.fromHtml(lastReply.trim()).toString());
                        temp.setUnreadCounter(0);
                        temp.setReadStatus(InboxMessageConstant.STATE_CHAT_READ);
                        temp.setTime(String.valueOf(new Date().getTime()));
                        temp.setTyping(false);
                    }
                    notifyItemChanged(i);
                    break;
                }

            } catch (ClassCastException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void clearSelection() {
        for (Visitable message : list) {
            if (message instanceof ChatListViewModel)
                ((ChatListViewModel) message).setChecked(false);
        }
        listMove.clear();
        notifyDataSetChanged();
    }

    public void removeList(List<Pair> originList, List<DeleteChatViewModel> list) {
        if (this.list.size() == list.size()) {
            this.list.clear();
            this.list.add(emptyChatModel);
            notifyDataSetChanged();
        } else {
            for (Pair pair : originList) {
                ChatListViewModel first = (ChatListViewModel) pair.first;
                int position = (int) pair.second;
                for (DeleteChatViewModel model : list) {
                    if (model.getMsgId() == Integer.valueOf(first.getId())) {
                        this.list.remove(position);
                        notifyItemRemoved(position);
                        break;
                    }
                }
            }
        }
    }


    public void showTyping(int msgId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ChatListViewModel) {
                ChatListViewModel tempModel = ((ChatListViewModel) list.get(i));
                String temp = tempModel.getId();
                if (msgId == Integer.valueOf(temp)) {
                    tempModel.setTyping(true);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void removeTyping(int msgId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ChatListViewModel) {
                ChatListViewModel tempModel = ((ChatListViewModel) list.get(i));
                String temp = tempModel.getId();
                if (msgId == Integer.valueOf(temp)) {
                    tempModel.setTyping(false);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

}
