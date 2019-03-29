package com.tokopedia.affiliate.feature.onboarding.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 10/4/18.
 */
public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {
    private final List<String> list;
    private final UsernameInputContract.View listener;

    public SuggestionAdapter(UsernameInputContract.View listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_af_username, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position));
        holder.itemView.setOnClickListener(v -> {
            listener.onSuggestionClicked(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<String> suggestions) {
        this.list.clear();
        this.list.addAll(suggestions);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
