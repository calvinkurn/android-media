package com.tokopedia.editshipping.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.databinding.ShippingCourierAdapterBinding;
import com.tokopedia.editshipping.domain.model.editshipping.Courier;
import com.tokopedia.editshipping.ui.EditShippingViewListener;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public class CourierView extends EditShippingCourierView<Courier,
        EditShippingViewListener, ShippingCourierAdapterBinding>{
    private EditShippingViewListener mainView;

    public CourierView(Context context) {
        super(context);
    }

    @Override
    protected ShippingCourierAdapterBinding getLayoutView(Context context) {
        return ShippingCourierAdapterBinding.inflate(LayoutInflater.from(context), this, true);
    }

    @SuppressLint({"DeprecatedMethod", "PII Data Exposure"})
    @Override
    public void renderData(@NonNull final Courier courier, final int courierIndex) {
        if (courier.isWhitelabelService()) {
            getBinding().name.setVisibility(View.GONE);
            getBinding().imgCourier.setVisibility(View.GONE);
        } else {
            getBinding().name.setText(courier.name);
            ImageHandler.LoadImage(getBinding().imgCourier, courier.logo);
            getBinding().imgCourier.setVisibility(View.VISIBLE);
        }
        getBinding().childrenLayout.setViewListener(mainView);
        setPackageAvailability(courierIndex, courier);
        setCourierWeightPolicy(courier);
        getBinding().shippingSettings.setOnClickListener(new OnClickListener() {
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
            getBinding().shippingSettings.setVisibility(View.GONE);
        } else{
            getBinding().shippingSettings.setVisibility(View.VISIBLE);
        }
    }

    private void setPackageAvailability(int courierIndex, Courier courier){
        if(courier.available.equals("1")) getBinding().childrenLayout.renderData(courier, courierIndex);
        else {
            getBinding().courierUnavailableWarning.setVisibility(VISIBLE);
            getBinding().shippingSettings.setVisibility(View.GONE);
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
            getBinding().name.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.tokopedia.design.R.drawable.info_icon, 0);
            getBinding().courierNamePlaceholder.setOnClickListener(new OnClickListener() {
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
