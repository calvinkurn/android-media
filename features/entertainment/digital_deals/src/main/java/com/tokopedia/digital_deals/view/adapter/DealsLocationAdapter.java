package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DealsLocationAdapter extends RecyclerView.Adapter<DealsLocationAdapter.ViewHolder> {


    private Context context;
    private List<Location> locations;
    private SelectCityListener actionListener;
    private boolean isPopular;
    DealsAnalytics dealsAnalytics;
    private String selectedLocation;

    public DealsLocationAdapter(List<Location> locations, SelectCityListener actionListener, String selectedLocation) {
        this.locations = new ArrayList<>();
        this.locations = locations;
        this.actionListener = actionListener;
        this.selectedLocation = selectedLocation;
    }

    public void updateAdapter(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        dealsAnalytics = new DealsAnalytics();
        dealsAnalytics=new DealsAnalytics();
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(com.tokopedia.digital_deals.R.layout.location_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIndex(position);
        holder.bindData(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return (locations == null) ? 0 : locations.size();
    }

    public void setIsPopular(boolean isPopular) {
        this.isPopular = isPopular;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView locationName;
        private ImageView locImage;
        private View itemView;
        private ConstraintLayout mainContent;
        private int index;
        private String name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mainContent = itemView.findViewById(com.tokopedia.digital_deals.R.id.mainContent);
            locationName = itemView.findViewById(com.tokopedia.digital_deals.R.id.tv_location_name);
            locImage = itemView.findViewById(com.tokopedia.digital_deals.R.id.location_img);

            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (lp instanceof FlexboxLayoutManager.LayoutParams) {
                FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
                flexboxLp.setAlignSelf(AlignItems.CENTER);
            }

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        public void bindData(Location location) {
            locationName.setText(location.getName());
            ImageHandler.loadImage(context, locImage, location.getIcon(), com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isPopular)
                dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_ON_POPULAR_LOCATION,
                        String.format("%s - %s", locations.get(getIndex()).getName(), getIndex()));
            else {
                dealsAnalytics.sendEventDealsDigitalClick(String.format(DealsAnalytics.EVENT_CLICK_ON_LOCATION
                        , locations.get(getIndex()).getName()),
                        String.format("%s - %s", locations.get(getIndex()).getName(), getIndex()));
            }
            Location location = Utils.getSingletonInstance().getLocation(context);
            Utils.getSingletonInstance().updateLocation(context, locations.get(getIndex()));
            actionListener.onCityItemSelected(location != null);
        }
    }

    public interface SelectCityListener {
        void onCityItemSelected(boolean locationUpdated);
    }

}
