package com.tokopedia.core.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.shipping.fragment.EditShippingViewListener;
import com.tokopedia.core.shipping.model.editshipping.Courier;

import butterknife.BindView;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public class PackageView extends EditShippingCourierView<Courier,
        EditShippingViewListener>{

    @BindView(R2.id.checkbox_holder) LinearLayout checkBoxHolder;

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
    protected int getLayoutView() {
        return R.layout.shipping_courier_children_adapter;
    }

    @Override
    public void renderData(@NonNull Courier courier, int courierIndex) {
        for(int serviceIndex = 0; serviceIndex < courier.services.size(); serviceIndex++){
            PackageViewCheckBox packageCheckBox = new PackageViewCheckBox(getContext());
            packageCheckBox.setViewListener(mainView);
            packageCheckBox.renderData(courier.services.get(serviceIndex), serviceIndex);
            packageCheckBox.setServiceCheckBoxListener(courierIndex);
            checkBoxHolder.addView(packageCheckBox);
        }
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }
}
