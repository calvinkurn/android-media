package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 07/08/18.
 */

public class MySubmissionsListAdapter extends RecyclerView.Adapter<MySubmissionsViewHolder> {
    private List<SubmissionResult> submissionsResultList = new ArrayList<>();
    private Context context;
    private MySubmissionsViewHolder.ISubmissionsViewHolderListner ISubmissionsViewHolderListner;

    public MySubmissionsListAdapter(Context context, List<SubmissionResult> submissionsResultList, MySubmissionsViewHolder.ISubmissionsViewHolderListner ISubmissionsViewHolderListner) {
        this.context = context;
        this.submissionsResultList = submissionsResultList;
        this.ISubmissionsViewHolderListner = ISubmissionsViewHolderListner;
    }

    @Override
    public MySubmissionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySubmissionsViewHolder(context, LayoutInflater.from(context).inflate(R.layout.home_submissions_list_item, parent, false), ISubmissionsViewHolderListner);
    }

    @Override
    public void onBindViewHolder(MySubmissionsViewHolder holder, int position) {
        holder.bind(submissionsResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return submissionsResultList.size();
    }

}
