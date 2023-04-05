package com.tokopedia.editshipping.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.databinding.ShippingCourierChildrenAdapterBinding;
import com.tokopedia.editshipping.domain.model.editshipping.Courier;
import com.tokopedia.editshipping.ui.EditShippingViewListener;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public class PackageView extends EditShippingCourierView<Courier,
        EditShippingViewListener, ShippingCourierChildrenAdapterBinding>{

    private EditShippingViewListener mainView;

    public PackageView(Context context) {
        super(context);
    }

    public PackageView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public PackageView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected ShippingCourierChildrenAdapterBinding getLayoutView(Context context) {
        return ShippingCourierChildrenAdapterBinding.inflate(LayoutInflater.from(context), this, true);
    }

    @Override
    public void renderData(@NonNull Courier courier, int courierIndex) {
        for(int serviceIndex = 0; serviceIndex < courier.services.size(); serviceIndex++){
            PackageViewCheckBox packageCheckBox = new PackageViewCheckBox(getContext());
            packageCheckBox.setViewListener(mainView);
            packageCheckBox.renderData(courier.services.get(serviceIndex), serviceIndex);
            packageCheckBox.setServiceCheckBoxListener(courierIndex);
            packageCheckBox.setServiceWhitelabelLayout(courier.isWhitelabelService());
            getBinding().checkboxHolder.addView(packageCheckBox);
        }
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }
}
