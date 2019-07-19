package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.model.Location;

import java.util.List;

public class DealsPopularLocationAdapter extends RecyclerView.Adapter<DealsPopularLocationAdapter.ViewHolder> {

    private Context context;
    private List<Location> locations;

    public DealsPopularLocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locations = locationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DealsPopularLocationAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popular_location_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIndex(position);
        holder.bindData(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int index;
        private TextView locationName, locAddress, locType;
        private ImageView locImage;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            locationName = itemView.findViewById(R.id.location_name);
            locAddress = itemView.findViewById(R.id.location_address);
            locType = itemView.findViewById(R.id.location_type);
            locImage = itemView.findViewById(R.id.popular_loc_img);
        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        void bindData(Location location) {
            locationName.setText(location.getName());
            locAddress.setText(location.getAddress());
            locType.setText(location.getLocType());
            ImageHandler.loadImage(context, locImage, location.getImageApp(), R.color.grey_1100, R.color.grey_1100);
        }

    }
}
