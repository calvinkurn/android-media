package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 07/08/18.
 */

public class MySubmissionsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private List<SubmissionResult> submissionsResultList = new ArrayList<>();
    private Context context;
    private MySubmissionsViewHolder.ISubmissionsViewHolderListner ISubmissionsViewHolderListner;
    private boolean isFooterAdded = false;

    public MySubmissionsListAdapter(Context context, List<SubmissionResult> submissionsResultList, MySubmissionsViewHolder.ISubmissionsViewHolderListner ISubmissionsViewHolderListner) {
        this.context = context;
        if (submissionsResultList == null)
            this.submissionsResultList = new ArrayList<>();
        else
            this.submissionsResultList = submissionsResultList;
        this.ISubmissionsViewHolderListner = ISubmissionsViewHolderListner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;
        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.my_submission_list_item, parent, false);
                holder = new MySubmissionsViewHolder(context, v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            default:
                break;
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((MySubmissionsViewHolder) holder).bind(submissionsResultList.get(position), ISubmissionsViewHolderListner);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    private boolean isLastPosition(int position) {
        return (position == submissionsResultList.size() - 1);
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(new SubmissionResult(), true);
        }
    }


    public void add(SubmissionResult item, boolean refreshItem) {
        submissionsResultList.add(item);
        if (refreshItem)
            notifyItemInserted(submissionsResultList.size() - 1);
    }

    public void clearList() {
        isFooterAdded = false;
        if (submissionsResultList != null)
            submissionsResultList.clear();
    }

    public void addAll(List<SubmissionResult> items, Boolean... refreshItems) {
        this.submissionsResultList = items;
        notifyDataSetChanged();
    }

    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = submissionsResultList.size() - 1;
            SubmissionResult item = submissionsResultList.get(position);

            if (item != null) {
                submissionsResultList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View loadingLayout;

        private FooterViewHolder(View itemView) {
            super(itemView);
            loadingLayout = itemView.findViewById(R.id.loading_fl);
        }
    }

    @Override
    public int getItemCount() {
        return (submissionsResultList == null) ? 0 : submissionsResultList.size();
    }

}
