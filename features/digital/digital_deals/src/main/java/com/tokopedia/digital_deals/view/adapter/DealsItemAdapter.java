package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.digital_deals.R;

import java.util.ArrayList;


public class DealsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> list;


    public DealsItemAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
        list.add("ashjasj");
        list.add("ashjasjdvdhsh");
        list.add("ashjasj");
        list.add("ashjasjdvdhsh");
        list.add("ashjasj");
        list.add("ashjasjdvdhsh");
        list.add("ashjasj");
        list.add("ashjasjdvdhsh");
        list.add("ashjasj");
        list.add("ashjasjdvdhsh");
        list.add("ashjasj");
        list.add("ashjasjdvdhsh");

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.deals_item_card, parent, false);
        DealsViewHolder holder = new DealsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DealsViewHolder) holder).setViewHolder(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class DealsViewHolder extends RecyclerView.ViewHolder {


        int index;

        View thisView;


        public DealsViewHolder(View view) {
            super(view);
            thisView = view;
        }

        void setViewHolder(String viewModel, int position) {

        }


    }
}
