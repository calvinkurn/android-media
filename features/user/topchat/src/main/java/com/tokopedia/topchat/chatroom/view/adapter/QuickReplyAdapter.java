package com.tokopedia.topchat.chatroom.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;

/**
 * @author by yfsx on 08/05/18.
 */

public class QuickReplyAdapter extends RecyclerView.Adapter<QuickReplyAdapter.Holder> {

    private QuickReplyListViewModel quickReplyListViewModel;
    private ChatRoomContract.View mainView;

    public QuickReplyAdapter(QuickReplyListViewModel quickReplyListViewModel, ChatRoomContract.View mainView) {
        this.quickReplyListViewModel = quickReplyListViewModel;
        this.mainView = mainView;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_quick_reply, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final QuickReplyViewModel model = quickReplyListViewModel.getQuickReplies().get(position);
        holder.text.setText(model.getText());
        holder.text.setOnClickListener(view -> mainView.onQuickReplyClicked
                (quickReplyListViewModel, model));
    }

    @Override
    public int getItemCount() {
        return quickReplyListViewModel.getQuickReplies().size();
    }

    public void clearData() {
        quickReplyListViewModel = quickReplyListViewModel.EMPTY();
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView text;
        public Holder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
