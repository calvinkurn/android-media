package com.tokopedia.tkpd.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.shipping.fragment.EditShippingViewListener;
import com.tokopedia.tkpd.shipping.model.editshipping.Courier;

import butterknife.Bind;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public class CourierView extends EditShippingCourierView<Courier,
        EditShippingViewListener>{

    @Bind(R.id.courier_name_placeholder) LinearLayout courierNamePlaceHolder;

    @Bind(R.id.name) TextView courierNameText;

    @Bind(R.id.img_courier) ImageView courierImageHolder;

    @Bind(R.id.shipping_settings) LinearLayout shipmentSettings;

    @Bind(R.id.children_layout) PackageView packageView;

    @Bind(R.id.package_view_holder) LinearLayout packageViewHolder;

    @Bind(R.id.courier_unavailable_warning)
    TextView courierUnavailableWarning;

    private EditShippingViewListener mainView;

    public CourierView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.shipping_courier_adapter;
    }

    @Override
    public void renderData(@NonNull final Courier courier, final int courierIndex) {
        courierNameText.setText(courier.name);
        ImageHandler.LoadImage(courierImageHolder, courier.logo);
        packageView.setViewListener(mainView);
        setCourierWeightPolicy(courier);
        setPackageAvailability(courierIndex, courier);
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
            courierNameText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.info_icon, 0);
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
