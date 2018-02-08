package com.tokopedia.core.manage.people.address.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.manage.people.address.presenter.ManagePeopleAddressFragmentPresenter;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/23/16.
 */
public class ManagePeopleAddressAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_ADDRESS_ITEM = 100;

    private final List<AddressModel> list;
    private final ManagePeopleAddressFragmentPresenter presenter;

    public class MPAddressViewHolder extends RecyclerView.ViewHolder
            implements OnMapReadyCallback {

        @BindView(R2.id.address_name)
        TextView addressName;

        @BindView(R2.id.address_detail)
        TextView addressDetail;

        @BindView(R2.id.mapview)
        MapView mapView;

        @BindView(R2.id.action_edit)
        View editBtn;

        @BindView(R2.id.action_delete)
        View deleteBtn;

        @BindView(R2.id.action_default)
        View defaultBtn;

        @BindView(R2.id.layout)
        View layoutMap;

        @BindView(R2.id.label_no_location)
        View noLocationLabel;

        @BindView(R2.id.view_bottom)
        View bottomView;

        GoogleMap googleMap;

        public MPAddressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            this.googleMap = googleMap;
        }
    }

    public ManagePeopleAddressAdapter(List<AddressModel> list, ManagePeopleAddressFragmentPresenter presenter) {
        super();
        this.list = list;
        this.presenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_ADDRESS_ITEM:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.listview_manage_people_address2, parent, false);
                return new MPAddressViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_ADDRESS_ITEM:
                bindAddressHolder((MPAddressViewHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        try {
            MPAddressViewHolder viewHolder = (MPAddressViewHolder) holder;
            if (viewHolder.googleMap != null) {
                viewHolder.googleMap.clear();
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void bindAddressHolder(MPAddressViewHolder viewHolder, int position) {
        setVisibility(viewHolder, position);
        setTitle(viewHolder.addressName, position);
        setAddress(viewHolder.addressDetail, position);
        setGoogleMap(viewHolder.googleMap, position);
        setListener(viewHolder, position);
    }

    private void setVisibility(MPAddressViewHolder viewHolder, int position) {
        if (list.get(position).getAddressStatus() == 1) {
            viewHolder.defaultBtn.setVisibility(View.VISIBLE);
        } else {
            viewHolder.defaultBtn.setVisibility(View.GONE);
        }

        if (viewHolder.googleMap != null) {
            viewHolder.layoutMap.setVisibility(View.VISIBLE);
        } else {
            viewHolder.layoutMap.setVisibility(View.GONE);
        }

        if ((list.get(position).getLatitude() == null || list.get(position).getLatitude().isEmpty())
                || (list.get(position).getLongitude() == null || list.get(position).getLongitude().isEmpty())) {
            viewHolder.noLocationLabel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.noLocationLabel.setVisibility(View.INVISIBLE);
        }

        if (position == list.size() - 1) {
            viewHolder.bottomView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.bottomView.setVisibility(View.GONE);
        }
    }

    private void setTitle(TextView addressName, int position) {
        addressName.setText(list.get(position).getAddressName());
    }

    private void setAddress(TextView addressDetail, int position) {
        addressDetail.setText(list.get(position).getAddressFull());
    }

    private void setListener(MPAddressViewHolder viewHolder, final int position) {
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setActionEditButton(list.get(position));
            }
        });

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setActionDeleteButton(list.get(position));
            }
        });

        viewHolder.defaultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setActionDefaultButtonClicked(list.get(position));
            }
        });
    }

    private void setGoogleMap(GoogleMap googleMap, int position) {
        if (googleMap != null) {

            double latitude = Double.parseDouble(getLatitude(list.get(position).getLatitude()));
            double longitude = Double.parseDouble(getLongitude(list.get(position).getLongitude()));
            LatLng latLng = new LatLng(latitude, longitude);

            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.addMarker(
                    new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pointer_toped))
            ).setDraggable(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // need this even it's not used
                    // it's used to override default function of OnMapClickListener
                    // which is navigate to default Google Map Apps
                }
            });
        }
    }

    private String getLatitude(String latitude) {
        if (latitude == null || latitude.isEmpty()) {
            return TkpdState.Geolocation.defaultLatitude;
        } else {
            return latitude;
        }
    }

    private String getLongitude(String longitude) {
        if (longitude == null || longitude.isEmpty()) {
            return TkpdState.Geolocation.defaultLongitude;
        } else {
            return longitude;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_ADDRESS_ITEM;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }
}
