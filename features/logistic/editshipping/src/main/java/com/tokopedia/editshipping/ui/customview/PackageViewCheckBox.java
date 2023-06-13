package com.tokopedia.editshipping.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.databinding.PackageViewCheckboxBinding;
import com.tokopedia.editshipping.domain.model.editshipping.Service;
import com.tokopedia.editshipping.ui.EditShippingViewListener;
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify;

/**
 * Created by kris on 8/23/16. Tokopedia
 */
public class PackageViewCheckBox extends EditShippingCourierView<Service,
        EditShippingViewListener, PackageViewCheckboxBinding>{

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
    protected PackageViewCheckboxBinding getLayoutView(Context context) {
        return PackageViewCheckboxBinding.inflate(LayoutInflater.from(context), this, true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void renderData(@NonNull Service service, int serviceIndex) {
        getBinding().serviceCheckbox.setText(service.name);
        getBinding().serviceCheckbox.setChecked(service.getActive());
        this.serviceIndex = serviceIndex;
        getBinding().serviceCheckbox.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.tokopedia.design.R.drawable.info_icon, 0);
        getBinding().serviceCheckbox.setOnTouchListener(onDescriptionTouchedListener(getBinding().serviceCheckbox,
                service.description, service.name));
    }

    public void setServiceCheckBoxListener(final int courierIndex){
        getBinding().serviceCheckbox.setOnCheckedChangeListener(onServiceCheckedChanged(courierIndex));
    }

    public void setServiceWhitelabelLayout(boolean isWhitelabel) {
        getBinding().serviceCheckbox.setTextBold(isWhitelabel);
    }

    private CompoundButton.OnCheckedChangeListener onServiceCheckedChanged(final int courierIndex){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mainView.setServiceCondition(isChecked, serviceIndex, courierIndex);
            }
        };
    }

    private View.OnTouchListener onDescriptionTouchedListener(final CheckboxUnify checkBox, final String description, final String serviceName){
        return new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
