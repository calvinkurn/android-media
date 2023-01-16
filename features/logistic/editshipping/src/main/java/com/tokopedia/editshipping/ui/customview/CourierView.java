package com.tokopedia.editshipping.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.domain.model.editshipping.Courier;
import com.tokopedia.editshipping.ui.EditShippingViewListener;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public class CourierView extends EditShippingCourierView<Courier,
        EditShippingViewListener>{

    LinearLayout courierNamePlaceHolder;
    Typography courierNameText;
    ImageView courierImageHolder;
    LinearLayout shipmentSettings;
    PackageView packageView;
    LinearLayout packageViewHolder;
    Typography courierUnavailableWarning;
    private EditShippingViewListener mainView;

    public CourierView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.shipping_courier_adapter;
    }

    @Override
    protected void bindView(View view) {
        courierNamePlaceHolder = (LinearLayout) view.findViewById(R.id.courier_name_placeholder);
        courierNameText = (Typography) view.findViewById(R.id.name);
        courierImageHolder = (ImageView) view.findViewById(R.id.img_courier);
        shipmentSettings = (LinearLayout) view.findViewById(R.id.shipping_settings);

        packageView = (PackageView) view.findViewById(R.id.children_layout);
        packageViewHolder = (LinearLayout) view.findViewById(R.id.package_view_holder);
        courierUnavailableWarning = (Typography) view.findViewById(R.id.courier_unavailable_warning);

    }


    @SuppressLint({"DeprecatedMethod", "PII Data Exposure"})
    @Override
    public void renderData(@NonNull final Courier courier, final int courierIndex) {
        if (courier.isWhitelabelService()) {
            courierNameText.setVisibility(View.GONE);
            courierImageHolder.setVisibility(View.GONE);
        } else {
            courierNameText.setText(courier.name);
            ImageHandler.LoadImage(courierImageHolder, courier.logo);
            courierImageHolder.setVisibility(View.VISIBLE);
        }
        packageView.setViewListener(mainView);
        setPackageAvailability(courierIndex, courier);
        setCourierWeightPolicy(courier);
        shipmentSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.showLoading();
                mainView.openDataWebViewResources(courierIndex);
            }
        });
        setAdditionalOptionVisibility(courier);
    }

    private void setAdditionalOptionVisibility(Courier courier){
        if(courier.urlAdditionalOption.isEmpty()
                || courier.urlAdditionalOption.equals("0")
                || !courier.available.equals("1")){
            shipmentSettings.setVisibility(View.GONE);
        } else{
            shipmentSettings.setVisibility(View.VISIBLE);
        }
    }

    private void setPackageAvailability(int courierIndex, Courier courier){
        if(courier.available.equals("1")) packageView.renderData(courier, courierIndex);
        else {
            courierUnavailableWarning.setVisibility(VISIBLE);
            shipmentSettings.setVisibility(View.GONE);
        }
    }

    private int weightInformationVisibility(String information){
        if(information == null
                || information.isEmpty()
                || information.length()< 3
                || information.equals("0"))  return View.GONE;
        else return View.VISIBLE;
    }

    private void setCourierWeightPolicy(Courier currentCourier){
        if(currentCourier.available.equals("1")) {
            setCourierInformation(currentCourier.weightPolicy, currentCourier.name,
                    weightInformationVisibility(currentCourier.weightPolicy));
        } else setCourierInformation(mainView.getMainContext().getString(R.string.info_shipping_unavailable),
                currentCourier.name, View.GONE);
    }

    private void setCourierInformation(final String information, final String courierName,
                                       int visibility){
        if(visibility == VISIBLE){
            courierNameText.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.tokopedia.design.R.drawable.info_icon, 0);
            courierNamePlaceHolder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainView.showInfoBottomSheet(information, courierName);
                }
            });
        }
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }

}
