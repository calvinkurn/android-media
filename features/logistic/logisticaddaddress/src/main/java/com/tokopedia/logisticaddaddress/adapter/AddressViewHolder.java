package com.tokopedia.logisticaddaddress.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class AddressViewHolder extends AbstractViewHolder<AddressViewModel> {

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
    private View editBtn, deleteBtn, defaultBtn;
    private Typography locationDesc;
    private ImageView locationImg;

    @LayoutRes
    public static final int LAYOUT = R.layout.logistic_item_manage_people_address;

    public AddressViewHolder(View itemView, ManageAddressListener listener) {
        super(itemView);
        this.listener = listener;
        addressName = itemView.findViewById(R.id.address_name);
        addressDetail = itemView.findViewById(R.id.address_detail);
        editBtn = itemView.findViewById(R.id.action_edit);
        deleteBtn = itemView.findViewById(R.id.action_delete);
        defaultBtn = itemView.findViewById(R.id.action_default);
        locationDesc = itemView.findViewById(R.id.tv_pinpoint_state);
        locationImg = itemView.findViewById(R.id.img_location_state);
    }

    @Override
    public void bind(AddressViewModel element) {
        setVisibility(element);
        addressName.setText(element.getAddressName());
        addressDetail.setText(element.getAddressFull());
        setListener(element);
    }

    private void setVisibility(AddressViewModel viewModel) {
        if (viewModel.getAddressStatus() == ADDRESS_STATUS_VISIBLE_CODE) {
            defaultBtn.setVisibility(View.VISIBLE);
        } else {
            defaultBtn.setVisibility(View.GONE);
        }

        if ((viewModel.getLatitude() == null || viewModel.getLatitude().isEmpty())
                || (viewModel.getLongitude() == null || viewModel.getLongitude().isEmpty())) {
            Drawable icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_no_pinpoint);
            locationImg.setImageDrawable(icon);
            locationDesc.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.ic_disable_pinpoint));
            locationDesc.setText(itemView.getContext().getString(R.string.no_pinpoint));
        } else {
            Drawable icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_pinpoint_green);
            locationImg.setImageDrawable(icon);
            locationDesc.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green_200));
            locationDesc.setText(itemView.getContext().getString(R.string.havent_pinpoint));
        }

    }

    private void setListener(AddressViewModel viewModel) {
        editBtn.setOnClickListener(view -> listener.setActionEditButton(viewModel));
        deleteBtn.setOnClickListener(view -> listener.setActionDeleteButton(viewModel));
        defaultBtn.setOnClickListener(view -> listener.setActionDefaultButtonClicked(viewModel));
    }
}
