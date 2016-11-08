package com.tokopedia.core.shipping.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.shipping.model.EditShippingModel;
import com.tokopedia.core.shipping.presenter.EditShippingInterface;
import com.tokopedia.core.shipping.presenter.EditShippingViewInterface;

/**
 * Created by Kris on 11/12/2015.
 */

public class ShippingDetailDialog extends DialogFragment {

    private static final String COST_KEY = "cost_key";
    private static final String WEIGHT_KEY = "weight_key";
    private static final String COURIER_TAG = "courier_tag";
    private static final String MAXIMUM_ADDITIONAL_COST_KEY = "additional_cost";
    private static final String DIFFERENT_DISTRICT_KEY = "different_district";
    private static final String JNE_AWB_KEY = "awb_jne";
    private static final String JNE_OKE_ACTIVATED = "oke_key";
    private static final String SHOP_PARAMATERS = "shop_parameters_key";

    private CheckBox AWBCheckBox;
    private CheckBox diffDistrictCheckBox;
    private String JNEAWBValue;
    private String okeDiffDistrictValue;
    private boolean jneOkeActivated;
    private String additionalCost;
    private String minimumWeight;
    private String courier;
    private int additionalCostConstraint;
    private EditText additionalCostHolder;
    private EditText minimumWeightHolder;
    private LinearLayout minimumWeightField;
    private LinearLayout mainDialogLayout;
    private RelativeLayout additionalOptions;
    private TextView maximumWeightConstraintInfo;
    private View acceptButton;
    private View cancelButton;
    private ImageView infoButton;
    private EditShippingModel.ParamEditShop shopParameters;

    public ShippingDetailDialog(){

    }

    public static ShippingDetailDialog createJNEDetailDialog(EditShippingModel.ParamEditShop shop, int shippingMaxAddFee){
        ShippingDetailDialog dialog = new ShippingDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHOP_PARAMATERS, shop);
        bundle.putString(COST_KEY,shop.jneFeeValue);
        bundle.putString(WEIGHT_KEY, shop.okeMinWeightValue);
        bundle.putString(COURIER_TAG, EditShippingInterface.JNE_TAG);
        bundle.putInt(MAXIMUM_ADDITIONAL_COST_KEY, shippingMaxAddFee);
        bundle.putString(DIFFERENT_DISTRICT_KEY, shop.DiffDistrict);
        bundle.putString(JNE_AWB_KEY, shop.jneAWB);
        bundle.putBoolean(JNE_OKE_ACTIVATED, shop.jneOkeActivated);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static ShippingDetailDialog createTikiDetailDialog(EditShippingModel.ParamEditShop shop, int shippingMaxAddFee){
        ShippingDetailDialog dialog = new ShippingDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHOP_PARAMATERS, shop);
        bundle.putString(COST_KEY, shop.feeTikiValue);
        bundle.putString(COURIER_TAG, EditShippingInterface.TIKI_TAG);
        bundle.putInt(MAXIMUM_ADDITIONAL_COST_KEY, shippingMaxAddFee);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static ShippingDetailDialog createPosDetailDialog(EditShippingModel.ParamEditShop shop, int shippingMaxAddFee){
        ShippingDetailDialog dialog = new ShippingDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHOP_PARAMATERS, shop);
        bundle.putString(COST_KEY, shop.posFeeValue);
        bundle.putString(WEIGHT_KEY, shop.posMinWeightValue);
        bundle.putString(COURIER_TAG, EditShippingInterface.POS_TAG);
        bundle.putInt(MAXIMUM_ADDITIONAL_COST_KEY, shippingMaxAddFee);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shopParameters = getArguments().getParcelable(SHOP_PARAMATERS);
        additionalCost = getArguments().getString(COST_KEY);
        minimumWeight = getArguments().getString(WEIGHT_KEY);
        courier = getArguments().getString(COURIER_TAG);
        additionalCostConstraint = getArguments().getInt(MAXIMUM_ADDITIONAL_COST_KEY, 0);
        JNEAWBValue = getArguments().getString(JNE_AWB_KEY);
        okeDiffDistrictValue = getArguments().getString(DIFFERENT_DISTRICT_KEY);
        jneOkeActivated = getArguments().getBoolean(JNE_OKE_ACTIVATED);
        View view = inflater.inflate(R.layout.dialog_shipping_detail, container, false);
        initView(view);
        setButtonListener();
        addView(view);
        return view;
    }

    private void initView(View view){
        mainDialogLayout = (LinearLayout) view.findViewById(R.id.main_dialog_view);
        additionalOptions = (RelativeLayout) view.findViewById(R.id.additional_option_layout);
        minimumWeightField = (LinearLayout) view.findViewById(R.id.minimum_weight_field);
        maximumWeightConstraintInfo = (TextView) view.findViewById(R.id.maximum_additional_cost_info);
        acceptButton = view.findViewById(R.id.accept_courier_change);
        cancelButton = view.findViewById(R.id.cancel_courier_change);

        infoButton = (ImageView) view.findViewById(R.id.info_button);
        AWBCheckBox = (CheckBox) view.findViewById(R.id.awb_checkbox);
        AWBCheckBox.setOnCheckedChangeListener(awbCheckBoxListener());
        diffDistrictCheckBox = (CheckBox) view.findViewById(R.id.diff_district_checkbox);
        diffDistrictCheckBox.setOnCheckedChangeListener(diffDistrictCheckBoxListener());
        setAdditionalCostView(view);
    }

    private void setButtonListener(){
        acceptButton.setOnClickListener(acceptButtonClicked());
        cancelButton.setOnClickListener(cancelButtonListener());
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogAWBInfo();
            }
        });
    }

    private void addView(View view){
        if((courier.equals(EditShippingInterface.JNE_TAG) && jneOkeActivated) || courier.equals(EditShippingInterface.POS_TAG)){
            setMinimumWeightView(view);
        }
        if(courier.equals(EditShippingInterface.JNE_TAG)){
            addAdditionalOptions();
        }
    }

    private void addAdditionalOptions(){
        if(courier.equals(EditShippingInterface.JNE_TAG)){
            additionalOptions.setVisibility(View.VISIBLE);
            AWBCheckBox.setChecked(convertStringValue(JNEAWBValue));
            if(!jneOkeActivated){
                diffDistrictCheckBox.setVisibility(View.GONE);
            }else{
                diffDistrictCheckBox.setChecked(convertStringValue(okeDiffDistrictValue));
            }
        }
    }
    private void setAdditionalCostView(View view){

        additionalCostHolder = (EditText)view.findViewById(R.id.additional_cost_holder);
        additionalCostHolder.setText(additionalCost);
        maximumWeightConstraintInfo.setText(getActivity().getString(R.string.title_shipping_fee_max) + " " + additionalCostConstraint);
    }
    private void setMinimumWeightView(View view){
        minimumWeightField.setVisibility(View.VISIBLE);
        minimumWeightHolder = (EditText) view.findViewById(R.id.minimum_weight_holder);
        minimumWeightHolder.setText(minimumWeight);
    }
    private View.OnClickListener acceptButtonClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(formValidated()){
                    setDialogFragmentResult();
                    Toast.makeText(getActivity(),getActivity().getString(R.string.success_change_shipping_detail), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        };
    }

    private void setDialogFragmentResult(){
        Intent intent = new Intent();
        switch (courier) {
            case EditShippingInterface.JNE_TAG:
                shopParameters.jneFeeValue = additionalCostHolder.getText().toString();
                shopParameters.okeMinWeightValue = getWeightValue();
                shopParameters.jneAWB = JNEAWBValue;
                shopParameters.DiffDistrict = okeDiffDistrictValue;
                intent.putExtra(EditShippingViewInterface.MAXIMUM_FEE, additionalCostConstraint);
                intent.putExtra(EditShippingViewInterface.SHOP_PARAMS_KEY, shopParameters);
                getTargetFragment().onActivityResult(EditShippingViewInterface.JNE_CODE, Activity.RESULT_OK, intent);
                break;
            case EditShippingInterface.POS_TAG:
                shopParameters.posFeeValue = additionalCostHolder.getText().toString();
                shopParameters.posMinWeightValue = minimumWeightHolder.getText().toString();
                intent.putExtra(EditShippingViewInterface.MAXIMUM_FEE, additionalCostConstraint);
                intent.putExtra(EditShippingViewInterface.SHOP_PARAMS_KEY, shopParameters);
                getTargetFragment().onActivityResult(EditShippingViewInterface.POS_CODE, Activity.RESULT_OK, intent);
                break;
            case EditShippingInterface.TIKI_TAG:
                shopParameters.feeTikiValue = additionalCostHolder.getText().toString();
                intent.putExtra(EditShippingViewInterface.MAXIMUM_FEE, additionalCostConstraint);
                intent.putExtra(EditShippingViewInterface.SHOP_PARAMS_KEY, shopParameters);
                getTargetFragment().onActivityResult(EditShippingViewInterface.TIKI_CODE, Activity.RESULT_OK, intent);
                break;
        }
    }
    private View.OnClickListener cancelButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),getActivity().getString(R.string.cancel_change_shipping_detail), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        };
    }

    private boolean formValidated(){
        if(!checkAdditionalOptionChanges()){
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_returnable_policy_same), Toast.LENGTH_LONG).show();
            return false;
        }else if(!checkAdditionalCost() || !checkWeight())
            return false;
        else return true;
    }

    private boolean checkAdditionalCost(){
        if(additionalCostHolder.getText().toString().isEmpty()){
            additionalCostHolder.setText("0");
            return true;
            //[BUGFIX] AN-1373
            // Settings: App should not be crashed when the user click on 'Change' button after inputting amount in 'Additional Cost' field.
        }else if (Integer.parseInt(additionalCostHolder.getText().toString()) > additionalCostConstraint){
            Toast.makeText(getActivity(),getActivity().getString(R.string.title_additional_fee_max) + Integer.toString(additionalCostConstraint), Toast.LENGTH_LONG).show();
            return false;
        }else return true;
    }

    private boolean checkAdditionalOptionChanges(){
        switch (courier) {
            case EditShippingInterface.JNE_TAG:
                return !(additionalCostHolder.getText().toString().equals(shopParameters.jneFeeValue)
                        && getWeightValue().equals(shopParameters.okeMinWeightValue)
                        && JNEAWBValue.equals(shopParameters.jneAWB)
                        && okeDiffDistrictValue.equals(shopParameters.DiffDistrict));
            case EditShippingInterface.POS_TAG:
                return !(additionalCostHolder.getText().toString().equals(shopParameters.posFeeValue)
                        && minimumWeightHolder.getText().toString().equals(shopParameters.posMinWeightValue));
            case EditShippingInterface.TIKI_TAG:
                return !additionalCostHolder.getText().toString().equals(shopParameters.feeTikiValue);
        }
        return true;
    }

    private boolean checkWeight(){
        if(minimumWeightField.isShown() && minimumWeightHolder.getText().toString().isEmpty()){
            minimumWeightHolder.setText("0");
            return true;
        }else if(minimumWeightField.isShown()
                && (Integer.parseInt(minimumWeightHolder.getText().toString())> 5)){
            Toast.makeText(getActivity(),
                    getActivity().getString(R.string.error_weight_shipping),
                    Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }



    private CompoundButton.OnCheckedChangeListener awbCheckBoxListener(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkBoxStatus) {
                JNEAWBValue = convertBooleanValue(checkBoxStatus);
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener diffDistrictCheckBoxListener(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkBoxStatus) {
                okeDiffDistrictValue = convertBooleanValue(checkBoxStatus);
            }
        };
    }

    private String convertBooleanValue(boolean checkedBoolean){
        if(checkedBoolean)
            return "1";
        else return "";
    }

    private boolean convertStringValue(String valueString){
        return valueString.equals("1");
    }

    private String getWeightValue(){
        if(minimumWeightField.isShown()){
            return minimumWeightHolder.getText().toString();
        }else return "0";
    }

    private void createDialogAWBInfo(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(getActivity().getString(R.string.awb_info));
        dialog.setNeutralButton(getActivity().getString(R.string.dialog_close), null);
        dialog.show();
    }

}
