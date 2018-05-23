package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.geolocation.model.coordinate.Location;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

public class DealsLocationAdapter extends RecyclerView.Adapter<DealsLocationAdapter.ViewHolder> {


    private Context context;
    private List<LocationViewModel> locations;
    private ActionListener actionListener;

    public DealsLocationAdapter(Context context) {
        this.context = context;
    }

    public DealsLocationAdapter(Context context, List<LocationViewModel> locations, ActionListener actionListener) {
        this.context = context;
        this.locations = new ArrayList<>();
        this.locations = locations;
        this.actionListener=actionListener;
    }

    public void updateAdapter(List<LocationViewModel> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIndex(position);
        holder.bindData(locations.get(position));
    }

    @Override
    public int getItemCount() {
//        Log.d("gjwdgsjjsa", ""+locations.size());
        if (locations != null) {
            return locations.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView locationName;
        private View itemView;
        private int index;
        private String name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            locationName = itemView.findViewById(R.id.tv_location_name);

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        public void bindData(LocationViewModel location) {
            locationName.setText(location.getName());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            LocationViewModel location=Utils.getSingletonInstance().getLocation(context);
            Utils.getSingletonInstance().updateLocation(context, locations.get(getIndex()));
            actionListener.onLocationItemSelected(location!=null);
        }
    }
    public interface ActionListener {
        void onLocationItemSelected(boolean locationUpdated);
    }

}
