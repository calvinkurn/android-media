package com.tokopedia.chatbot.view.adapter;

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

    public interface OnReasonClickListener {
        void onClickReason(int adapterPosition);
    }

    private List<String> reasonList;
    private OnReasonClickListener listener;

    @Inject
    public ReasonAdapter(OnReasonClickListener listener) {
        reasonList = new ArrayList<>();
        this.listener = listener;
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

    public void addList(ArrayList<String> reasonList) {
        this.reasonList.clear();
        this.reasonList.addAll(reasonList);
    }

    public class ReasonViewHolder extends RecyclerView.ViewHolder {
        TextView reason;

        public ReasonViewHolder(View itemView) {
            super(itemView);
            reason = itemView.findViewById(R.id.reason);
            reason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickReason(getAdapterPosition());
                }
            });
        }
    }

}
