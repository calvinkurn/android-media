package com.tokopedia.logisticaddaddress.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.logisticaddaddress.R;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class AddressViewHolder extends AbstractViewHolder<AddressViewModel> implements OnMapReadyCallback {

    private ManageAddressListener listener;

    public static final String DEFAULT_LATITUDE = "-6.1753924";
    public static final String DEFAULT_LONGITUDE = "106.8249641";
    public static final int ADDRESS_STATUS_VISIBLE_CODE = 1;

    public interface ManageAddressListener {

        void setActionEditButton(AddressViewModel viewModel);

        void setActionDeleteButton(AddressViewModel viewModel);

        void setActionDefaultButtonClicked(AddressViewModel viewModel);

    }

    private TextView addressName;
    private TextView addressDetail;
    private MapView mapView;
    private View editBtn, deleteBtn, defaultBtn, layoutMap, noLocationLabel, bottomView;
    private GoogleMap googleMap;

    @LayoutRes
    public static final int LAYOUT = R.layout.logistic_item_manage_people_address;

    public AddressViewHolder(View itemView, ManageAddressListener listener) {
        super(itemView);
        this.listener = listener;
        addressName = itemView.findViewById(R.id.address_name);
        addressDetail = itemView.findViewById(R.id.address_detail);
        mapView = itemView.findViewById(R.id.mapview);
        editBtn = itemView.findViewById(R.id.action_edit);
        deleteBtn = itemView.findViewById(R.id.action_delete);
        defaultBtn = itemView.findViewById(R.id.action_default);
        layoutMap = itemView.findViewById(R.id.layout);
        noLocationLabel = itemView.findViewById(R.id.label_no_location);
        bottomView = itemView.findViewById(R.id.view_bottom);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void bind(AddressViewModel element) {
        setVisibility(element);
        setTitle(element);
        setAddress(element);
        setGoogleMap(googleMap, element);
        setListener(element);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void setVisibility(AddressViewModel viewModel) {
        if (viewModel.getAddressStatus() == ADDRESS_STATUS_VISIBLE_CODE) {
            defaultBtn.setVisibility(View.VISIBLE);
        } else {
            defaultBtn.setVisibility(View.GONE);
        }

        if (googleMap != null) {
            layoutMap.setVisibility(View.VISIBLE);
        } else {
            layoutMap.setVisibility(View.GONE);
        }

        if ((viewModel.getLatitude() == null || viewModel.getLatitude().isEmpty())
                || (viewModel.getLongitude() == null || viewModel.getLongitude().isEmpty())) {
            noLocationLabel.setVisibility(View.VISIBLE);
        } else {
            noLocationLabel.setVisibility(View.INVISIBLE);
        }

    }

    private void setTitle(AddressViewModel viewModel) {
        addressName.setText(viewModel.getAddressName());
    }

    private void setAddress(AddressViewModel viewModel) {
        addressDetail.setText(viewModel.getAddressFull());
    }

    private void setListener(AddressViewModel viewModel) {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setActionEditButton(viewModel);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setActionDeleteButton(viewModel);
            }
        });

        defaultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setActionDefaultButtonClicked(viewModel);
            }
        });
    }

    private void setGoogleMap(GoogleMap googleMap, AddressViewModel viewModel) {
        if (googleMap != null) {

            double latitude = Double.parseDouble(getLatitude(viewModel.getLatitude()));
            double longitude = Double.parseDouble(getLongitude(viewModel.getLongitude()));
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
            return DEFAULT_LATITUDE;
        } else {
            return latitude;
        }
    }

    private String getLongitude(String longitude) {
        if (longitude == null || longitude.isEmpty()) {
            return DEFAULT_LONGITUDE;
        } else {
            return longitude;
        }
    }
}
