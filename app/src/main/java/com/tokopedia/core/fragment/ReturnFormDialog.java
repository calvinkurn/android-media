package com.tokopedia.core.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;
import com.tokopedia.core.facade.FacadeSendFormDialog;
import com.tokopedia.core.prototype.ProductCache;
import com.tokopedia.core.var.TkpdUrl;

import java.util.ArrayList;

/**
 * Created by Kris on 3/16/2015.
 */
public class ReturnFormDialog extends DialogFragment{

    private ViewHolder viewHolder;
    private View rootView;
    private String URL;
    private Context context;
    private String shopId;
    private String policyContent;
    private String oldContent;
    private String policyAct;
    private Spinner returnableSpinner;
    private int spinnerIntendedPosition;
    private FacadeSendFormDialog facadeSendForm;
    private boolean changePolicySuccess;
    TkpdProgressDialog progressDialog;

    private  class ViewHolder{
        TextView title;
        EditText editPolicyContent;
        TextView setPolicyButton;
        TextView dismissButton;
        LinearLayout dismissLayout;
        LinearLayout saveLayout;
    }
    public static ReturnFormDialog createInstance(String shopId, String policyContent, String policyAct, Spinner returnableSpinner, int spinnerIntendedPosition){
        ReturnFormDialog dialog = new ReturnFormDialog();
        CommonUtils.dumper(shopId.toString() + "  " + policyContent.toString());
        dialog.shopId = shopId;
        dialog.policyContent = policyContent;
        dialog.oldContent = policyContent;
        dialog.policyAct = policyAct;
        dialog.returnableSpinner = returnableSpinner;
        dialog.spinnerIntendedPosition = spinnerIntendedPosition;
        return dialog;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        initVariable();
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_form_policy, container, false);
        rootView.setFocusableInTouchMode(true);
        setCancelable(false);
        initView();
        setListener();
        return rootView;
    }

    private void initVariable(){
        context = getActivity();
        URL = TkpdUrl.GET_SHOP_NOTES;
        viewHolder = new ViewHolder();
        progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        facadeSendForm = FacadeSendFormDialog.createDialogFacadeInstance(context, shopId, policyAct);
        changePolicySuccess = false;
    }
    private void initView(){
        viewHolder.title = (TextView) rootView.findViewById(R.id.edit_return_policy_title);
        viewHolder.editPolicyContent = (EditText) rootView.findViewById(R.id.edit_return_policy_content);
        viewHolder.setPolicyButton = (TextView) rootView.findViewById(R.id.confirm_new_policy_button);
        viewHolder.dismissButton = (TextView) rootView.findViewById(R.id.dismiss_return_policy_form);
        viewHolder.dismissLayout = (LinearLayout) rootView.findViewById(R.id.policy_form_dismiss);
        viewHolder.saveLayout = (LinearLayout) rootView.findViewById(R.id.policy_form_save);
        viewHolder.editPolicyContent.setText(Html.fromHtml(policyContent));
        viewHolder.editPolicyContent.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    private void setListener(){
        viewHolder.setPolicyButton.setOnClickListener(setPolicyButtonListener());
        viewHolder.saveLayout.setOnClickListener(setPolicyButtonListener());
        viewHolder.dismissLayout.setOnClickListener(dismissPolicyListener());
        viewHolder.dismissButton.setOnClickListener(dismissPolicyListener());
    }
    private View.OnClickListener setPolicyButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policyContent = getContent();
                if(contentValidation(policyContent)){
                    //progressDialog.showDialog();
                    facadeSendForm.sendForm(policyContent, OnPolicyChanged());
                    //setReturnPolicy();
                }

            }
        };
    }
    private FacadeSendFormDialog.AddNewPolicyListener OnPolicyChanged(){
        return new FacadeSendFormDialog.AddNewPolicyListener() {
            @Override
            public void OnSuccess(String NoteID) {
                changePolicySuccess = true;
                ProductCache productCache = new ProductCache();
                productCache.ClearCache(context);
                dismiss();

            }

            @Override
            public void OnFailure() {

            }
        };
    }
    private View.OnClickListener dismissPolicyListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }
    private String getContent(){
        String policyContentString = viewHolder.editPolicyContent.getText().toString();
        policyContentString = policyContentString.replaceAll("\\n", "<br />");
        return policyContentString;
    }
    private boolean contentValidation(String contentToValidate){
        boolean validation = true;
        CommonUtils.dumper(contentToValidate + "  " + oldContent);
            if(contentToValidate.length()<1){
                validation = false;
                viewHolder.editPolicyContent.setError(context.getString(R.string.error_field_required));
                viewHolder.editPolicyContent.requestFocus();
            }else if(contentToValidate.equals(oldContent)){
                validation = false;
                viewHolder.editPolicyContent.setError(context.getString(R.string.error_returnable_policy_same));
                viewHolder.editPolicyContent.requestFocus();
            }
        return validation;
    }
    private void ErrorMessage(ArrayList<String> MessageError){
        for(int i=0; i<MessageError.size(); i++){
            Toast.makeText(context, MessageError.get(i), Toast.LENGTH_LONG).show();
            if((i+1)<MessageError.size())
                Toast.makeText(context, "No Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        if(changePolicySuccess && returnableSpinner!=null)
            returnableSpinner.setSelection(spinnerIntendedPosition);
        super.onStop();
    }
}
