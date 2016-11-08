package com.tokopedia.core.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.shipping.fragment.EditShippingViewListener;
import com.tokopedia.core.shipping.model.editshipping.Service;

import butterknife.Bind;

/**
 * Created by kris on 8/23/16. Tokopedia
 */
public class PackageViewCheckBox extends EditShippingCourierView<Service,
        EditShippingViewListener>{

    @Bind(R2.id.service_checkbox)
    CheckBox serviceCheckbox;

    EditShippingViewListener mainView;

    private int serviceIndex;

    public PackageViewCheckBox(Context context) {
        super(context);
    }

    public PackageViewCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PackageViewCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.package_view_checkbox;
    }

    @Override
    public void renderData(@NonNull Service service, int serviceIndex) {
        serviceCheckbox.setText(service.name);
        serviceCheckbox.setChecked(service.getActive());
        this.serviceIndex = serviceIndex;

        serviceCheckbox.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.info_icon, 0);
        serviceCheckbox.setOnTouchListener(onDescriptionTouchedListener(serviceCheckbox,
                service.description, service.name));
    }

    public void setServiceCheckBoxListener(final int courierIndex){
        serviceCheckbox.setOnCheckedChangeListener(onServiceCheckedChanged(courierIndex));
    }

    private CompoundButton.OnCheckedChangeListener onServiceCheckedChanged(final int courierIndex){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mainView.setServiceCondition(isChecked, serviceIndex, courierIndex);
            }
        };
    }

    private OnTouchListener onDescriptionTouchedListener(final CheckBox checkBox, final String description, final String serviceName){
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= checkBox.getRight() - checkBox.getTotalPaddingRight()) {
                        mainView.showInfoBottomSheet(description, serviceName);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }
}
