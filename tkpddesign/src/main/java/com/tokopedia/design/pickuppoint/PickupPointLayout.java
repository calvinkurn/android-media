package com.tokopedia.design.pickuppoint;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.component.TextViewCompat;

/**
 * Created by Irfan Khoirul on 03/01/18.
 */

public class PickupPointLayout extends LinearLayout {

    private TextView tvSendToPickUpBooth;
    private TextView tvPickUpBoothName;
    private ImageButton btnCancelPickUp;
    private TextView tvPickUpBoothAddress;
    private TextViewCompat tvEditPickUpBooth;
    private LinearLayout llContent;

    private ViewListener listener;

    public PickupPointLayout(Context context) {
        super(context);
        initView(context);
    }

    public PickupPointLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PickupPointLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_pickup_point, this, true);

        tvSendToPickUpBooth = view.findViewById(R.id.tv_send_to_pick_up_booth);
        tvPickUpBoothName = view.findViewById(R.id.tv_pick_up_booth_name);
        btnCancelPickUp = view.findViewById(R.id.btn_cancel_pick_up);
        tvPickUpBoothAddress = view.findViewById(R.id.tv_pick_up_booth_address);
        tvEditPickUpBooth = view.findViewById(R.id.tv_edit_pick_up_booth);
        llContent = view.findViewById(R.id.ll_content);

        btnCancelPickUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClearPickupPoint();
                }
            }
        });

        tvEditPickUpBooth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditPickupPoint();
                }
            }
        });

        tvSendToPickUpBooth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChoosePickupPoint();
                }
            }
        });
    }

    public void enableChooserButton(Context context) {
        tvSendToPickUpBooth.setTextColor(ContextCompat.getColor(context, R.color.tkpd_main_green));
        tvSendToPickUpBooth.setVisibility(VISIBLE);
    }

    public void disableChooserButton(Context context) {
        tvSendToPickUpBooth.setTextColor(ContextCompat.getColor(context, R.color.font_black_secondary_54));
        tvSendToPickUpBooth.setVisibility(VISIBLE);
    }

    public void hideChooserButton() {
        tvSendToPickUpBooth.setVisibility(GONE);
    }

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    public void setData(Context context, String storeName, String storeAddress) {
        tvPickUpBoothName.setText(storeName);
        tvPickUpBoothAddress.setText(storeAddress);
        disableChooserButton(context);
        tvSendToPickUpBooth.setOnClickListener(null);
        llContent.setVisibility(VISIBLE);
    }

    public void unSetData(Context context) {
        enableChooserButton(context);
        llContent.setVisibility(GONE);
    }

    public interface ViewListener {
        void onChoosePickupPoint();

        void onClearPickupPoint();

        void onEditPickupPoint();
    }

}
