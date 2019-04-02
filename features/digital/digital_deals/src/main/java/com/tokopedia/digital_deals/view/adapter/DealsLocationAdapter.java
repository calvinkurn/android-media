package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.model.Location;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class DealsLocationAdapter extends RecyclerView.Adapter<DealsLocationAdapter.ViewHolder> {


    private Context context;
    private List<Location> locations;
    private ActionListener actionListener;
    private boolean isPopular;
    DealsAnalytics dealsAnalytics;
    private String selectedLocation;

    public DealsLocationAdapter(List<Location> locations, ActionListener actionListener, String selectedLocation) {
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_item, viewGroup, false));
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

        private TextView locationName, selectedLocText;
        private ImageView locImage;
        private View itemView;
        private LinearLayout mainContent;
        private int index;
        private String name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mainContent = itemView.findViewById(R.id.mainContent);
            locationName = itemView.findViewById(R.id.tv_location_name);
            locImage = itemView.findViewById(R.id.location_img);
            selectedLocText = itemView.findViewById(R.id.selected_location_name);

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
            if (location.getName().equalsIgnoreCase(selectedLocation)) {
                mainContent.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_green_selected_border));
                selectedLocText.setVisibility(View.VISIBLE);
            } else {
                selectedLocText.setVisibility(View.INVISIBLE);
            }
            locationName.setText(location.getName());
            ImageHandler.loadImage(context, locImage, location.getImageApp(), R.color.grey_1100, R.color.grey_1100);
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
            actionListener.onLocationItemSelected(location != null);
        }
    }

    public interface ActionListener {
        void onLocationItemSelected(boolean locationUpdated);
    }

}
