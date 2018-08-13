package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 07/08/18.
 */

public class MySubmissionsListAdpater extends RecyclerView.Adapter<MySubmissionsViewHolder> {
    List<SubmissionResult> submissionsResultList = new ArrayList<>();
    Context context;

    public MySubmissionsListAdpater(Context context, List<SubmissionResult> submissionsResultList) {
        this.context = context;
        this.submissionsResultList = submissionsResultList;
    }

    @Override
    public MySubmissionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySubmissionsViewHolder(context, LayoutInflater.from(context).inflate(R.layout.home_submissions_list_item, parent, false));
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
