package com.tokopedia.posapp.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.Outlet;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletItemViewModel;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 8/1/17.
 */

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardOutlet;
        TextView textName;
        TextView textAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            cardOutlet = itemView.findViewById(R.id.card_outlet);
            textName = itemView.findViewById(R.id.text_name);
            textAddress = itemView.findViewById(R.id.text_address);
        }
    }

    private Context context;
    private Outlet.View viewListener;
    private List<OutletItemViewModel> outletList;

    public OutletAdapter(Context context, Outlet.View viewListener) {
        this.context = context;
        this.viewListener = viewListener;
        this.outletList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_outlet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textName.setText(this.outletList.get(position).getOutletName());
        holder.textAddress.setText(this.outletList.get(position).getOutletAddres());

        holder.cardOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onOutletClicked(outletList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(outletList != null
                && !outletList.isEmpty()) {
            return outletList.size();
        }

        return 0;
    }

    public void clearData() {
        this.outletList.clear();
        notifyDataSetChanged();
    }

    public void setData(OutletViewModel outletViewModel) {
        this.outletList.addAll(outletViewModel.getOutletList());
        notifyDataSetChanged();
    }
}
