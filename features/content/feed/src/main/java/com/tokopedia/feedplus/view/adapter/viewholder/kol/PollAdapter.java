package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.feedplus.view.viewmodel.kol.PollOptionViewModel;

import java.util.List;

/**
 * @author by milhamj on 15/05/18.
 */

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder> {
    List<PollOptionViewModel> list;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
