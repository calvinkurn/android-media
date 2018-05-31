package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.ArrayList;
import java.util.List;

public class DealDetailsAllLocationsAdapter extends RecyclerView.Adapter<DealDetailsAllLocationsAdapter.ViewHolder> {



    Context context;
    List<OutletViewModel> outlets;

    public DealDetailsAllLocationsAdapter(Context context, List<OutletViewModel> outlets){
        this.context=context;
        this.outlets=new ArrayList<>();
        this.outlets=outlets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.redeem_locations_expandable_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIndex(position);
        holder.bindData(outlets.get(position));
    }

    @Override
    public int getItemCount() {
        if (outlets != null) {
            return outlets.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView outletAddress;
        private TextView outletName;
        private View itemView;
        private ImageView viewMap;
        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            outletName =itemView.findViewById(R.id.tv_location_name);
            outletAddress =itemView.findViewById(R.id.tv_location_address);
            viewMap=itemView.findViewById(R.id.iv_map);
            viewMap.setOnClickListener(this);

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        public void bindData(OutletViewModel outlet) {
            outletName.setText(outlet.getName());
            outletAddress.setText(outlet.getDistrict());
            if(outlet.getCoordinates()!=null && outlet.getCoordinates()!=""){
                viewMap.setVisibility(View.VISIBLE);
            }else{
                viewMap.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.iv_map){
                Uri gmmIntentUri = Uri.parse("geo:"+outlets.get(getIndex()).getCoordinates());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.cannot_find_application), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
