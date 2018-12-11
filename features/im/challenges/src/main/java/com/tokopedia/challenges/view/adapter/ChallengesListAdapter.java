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
    private List<Result> challengesResultList;
    private Context context;
    private  boolean isPastChallenge;

    public ChallengesListAdapter(Context context, List<Result> challengesResultList, boolean isPastChallenge) {
        this.context = context;
        this.challengesResultList = challengesResultList;
        this.isPastChallenge=isPastChallenge;
    }

    @Override
    public ChallengesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChallengesViewHolder(context, LayoutInflater.from(context).inflate(R.layout.home_challenge_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChallengesViewHolder holder, int position) {
        holder.bind(challengesResultList.get(position),isPastChallenge);
    }

    @Override
    public int getItemCount() {
        return challengesResultList.size();
    }

    public void setData(List<Result> resultList, boolean isPastChallenge) {
        this.isPastChallenge = isPastChallenge;
        this.challengesResultList = resultList;
    }
}
