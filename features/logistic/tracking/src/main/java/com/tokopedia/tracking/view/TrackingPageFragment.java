package com.tokopedia.tracking.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tracking.R;
import com.tokopedia.tracking.adapter.EmptyTrackingNotesAdapter;
import com.tokopedia.tracking.adapter.TrackingHistoryAdapter;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPageFragment extends BaseDaggerFragment{

    private static final int ERROR_STATUS_NUMBER = 400;

    private TextView referenceNumber;
    private ImageView courierLogo;
    private TextView deliveryDate;
    private TextView storeName;
    private TextView storeAddress;
    private TextView serviceCode;
    private TextView buyerName;
    private TextView buyerLocation;
    private TextView currentStatus;
    private RecyclerView trackingHistory;
    private LinearLayout emptyUpdateNotification;
    private RecyclerView notificationHelpStep;
    private TextView furtherInformationText;
    private ViewGroup liveTrackingButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tracking_page_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        referenceNumber = view.findViewById(R.id.reference_number);
        courierLogo = view.findViewById(R.id.courier_logo);
        deliveryDate = view.findViewById(R.id.delivery_date);
        storeName = view.findViewById(R.id.store_name);
        storeAddress = view.findViewById(R.id.store_address);
        serviceCode = view.findViewById(R.id.service_code);
        buyerName = view.findViewById(R.id.buyer_name);
        buyerLocation = view.findViewById(R.id.buyer_location);
        currentStatus = view.findViewById(R.id.currenct_status);
        trackingHistory = view.findViewById(R.id.tracking_history);
        emptyUpdateNotification = view.findViewById(R.id.empty_update_notification);
        notificationHelpStep = view.findViewById(R.id.notification_help_step);
        furtherInformationText = view.findViewById(R.id.further_information_text);
        liveTrackingButton = view.findViewById(R.id.live_tracking_button);
    }

    private void populateView(TrackingViewModel model) {
        referenceNumber.setText(model.getReferenceNumber());
        ImageHandler.LoadImage(courierLogo, model.getCourierLogoUrl());
        deliveryDate.setText(model.getDeliveryDate());
        storeName.setText(model.getSellerStore());
        storeAddress.setText(model.getSellerAddress());
        serviceCode.setText(model.getServiceCode());
        buyerName.setText(model.getBuyerName());
        buyerLocation.setText(model.getBuyerAddress());
        currentStatus.setText(model.getStatus());
        trackingHistory.setAdapter(new TrackingHistoryAdapter(model.getHistoryList()));
        if (model.getHistoryList().size() == 0) {
            emptyUpdateNotification.setVisibility(View.VISIBLE);
            if(model.getStatusNumber() > ERROR_STATUS_NUMBER) {
                notificationHelpStep.setVisibility(View.VISIBLE);
                notificationHelpStep.setAdapter(new EmptyTrackingNotesAdapter());
            }
        } else {
            trackingHistory.setVisibility(View.VISIBLE);
        }
        furtherInformationText.setOnClickListener(onFurtherInformationClicked());
        liveTrackingButton.setOnClickListener(onLiveTrackingClickedListener());
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    private View.OnClickListener onFurtherInformationClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener onLiveTrackingClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

}
