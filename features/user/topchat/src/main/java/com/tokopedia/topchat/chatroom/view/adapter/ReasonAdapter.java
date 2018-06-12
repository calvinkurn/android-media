package com.tokopedia.topchat.chatroom.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.topchat.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 6/11/18.
 */
public class ReasonAdapter extends RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder> {

    private List<String> reasonList;

    @Inject
    public ReasonAdapter() {
        reasonList = new ArrayList<>();
    }

    @Override
    public ReasonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_reason, viewGroup, false);
        return new ReasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReasonViewHolder holder, int position) {
        String reason = reasonList.get(position);
        if (!isNullOrEmpty(reason)) {
            holder.reason.setText(MethodChecker.fromHtml(reason));
        }
    }


    private boolean isNullOrEmpty(String string) {
        return (string == null || string.equalsIgnoreCase("null") || string.isEmpty());
    }

    @Override
    public int getItemCount() {
        return reasonList.size();
    }

    public class ReasonViewHolder extends RecyclerView.ViewHolder {
        TextView reason;

        public ReasonViewHolder(View itemView) {
            super(itemView);
            reason = itemView.findViewById(R.id.reason);
        }
    }

}
