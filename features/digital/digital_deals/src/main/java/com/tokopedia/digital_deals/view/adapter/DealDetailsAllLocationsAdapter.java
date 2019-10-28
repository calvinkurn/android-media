package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DealDetailsAllLocationsAdapter extends RecyclerView.Adapter<DealDetailsAllLocationsAdapter.ViewHolder> {
    Context context;
    List<Outlet> outlets;

    public DealDetailsAllLocationsAdapter(List<Outlet> outlets) {
        this.outlets = new ArrayList<>();
        this.outlets = outlets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(com.tokopedia.digital_deals.R.layout.redeem_locations_expandable_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIndex(position);
        holder.bindData(outlets.get(position));
    }

    @Override
    public int getItemCount() {
        return (outlets == null) ? 0 : outlets.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView outletAddress;
        private TextView outletName;
        private ImageView viewMap;
        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            outletName = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_location_name);
            outletAddress = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_location_address);
            viewMap = itemView.findViewById(com.tokopedia.digital_deals.R.id.iv_map);
            viewMap.setOnClickListener(this);

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        public void bindData(Outlet outlet) {
            outletName.setText(outlet.getName());
            outletAddress.setText(outlet.getDistrict());
            if (outlet.getCoordinates() != null && outlet.getCoordinates() != "") {
                viewMap.setVisibility(View.VISIBLE);
            } else {
                viewMap.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == com.tokopedia.digital_deals.R.id.iv_map) {
                Utils.getSingletonInstance().openGoogleMapsActivity(context, outlets.get(getIndex()).getCoordinates());
            }
        }
    }
}
