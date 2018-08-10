package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 07/08/18.
 */

public class ChallengesListAdapter extends RecyclerView.Adapter<ChallengesViewHolder> {
    List<Result> challengesResultList;
    Context context;

    public ChallengesListAdapter(Context context, List<Result> challengesResultList) {
        this.context = context;
        this.challengesResultList = challengesResultList;
    }

    @Override
    public ChallengesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChallengesViewHolder(LayoutInflater.from(context).inflate(R.layout.home_challenge_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChallengesViewHolder holder, int position) {
        holder.bind(challengesResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return challengesResultList.size();
    }

}
