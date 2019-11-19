package com.tokopedia.topads.common.view.utils;

import android.app.Dialog;
import android.os.Build;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAutoCompleteAdapter;
import com.tokopedia.topads.dashboard.view.widget.TopAdsCustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi.putra on 24/05/18.
 */

public class TopAdsBottomSheetsSelectGroup extends BottomSheets{
    private String title;
    private String selectedGroupName;
    private String hintDescription;
    private TextInputLayout textInputLayoutChooseGroup;
    private TopAdsCustomAutoCompleteTextView inputChooseGroup;
    private ArrayList<String> groupNames = new ArrayList<>();
    private List<GroupAd> groupAds = new ArrayList<>();
    private OnSelectGroupListener onSelectGroupListener;
    private String selectedGroupAdId;

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.topads_widget_bottomsheet;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.partial_top_ads_move_group;
    }

    @Override
    protected String title() {
        return title;
    }

    public void setOnSelectGroupListener(OnSelectGroupListener onSelectGroupListener) {
        this.onSelectGroupListener = onSelectGroupListener;
    }

    @Override
    public void initView(View view) {
        inputChooseGroup =
                (TopAdsCustomAutoCompleteTextView) view.findViewById(R.id.choose_group_auto_text);
        textInputLayoutChooseGroup =
                (TextInputLayout) view.findViewById(R.id.input_layout_choose_group);
        TextView infoTextView = (TextView) view.findViewById(R.id.view_info);
        infoTextView.setText(hintDescription);
        TopAdsAutoCompleteAdapter adapterChooseGroup = new TopAdsAutoCompleteAdapter(getActivity(), R.layout.item_autocomplete_text);
        adapterChooseGroup.setListenerGetData(new TopAdsAutoCompleteAdapter.ListenerGetData() {
            @Override
            public ArrayList<String> getData() {
                return groupNames;
            }
        });
        inputChooseGroup.setAdapter(adapterChooseGroup);
        inputChooseGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if(isFocus && onSelectGroupListener != null){
                    onSelectGroupListener.searchGroup("");
                }
            }
        });
        inputChooseGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputChooseGroup.isPerformingCompletion()){
                    return;
                }
                if (onSelectGroupListener != null){
                    onSelectGroupListener.searchGroup(editable.toString());
                }
            }
        });
        inputChooseGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                inputChooseGroup.lockView();
                if (groupAds.get(i) != null) {
                    selectedGroupAdId = groupAds.get(i).getId();
                }
            }
        });
        if (!TextUtils.isEmpty(selectedGroupName)){
            inputChooseGroup.setText(selectedGroupName);
            inputChooseGroup.lockView();
        }

        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(selectedGroupAdId)){
                    dismiss();
                    if (onSelectGroupListener != null){
                        onSelectGroupListener.submitGroup(selectedGroupAdId);
                    }
                } else {
                    textInputLayoutChooseGroup.setError(getString(R.string.label_top_ads_error_choose_one_group));
                }
            }
        });
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        updateHeight(screenHeight/2);

        ImageButton btnClose = getDialog().findViewById(com.tokopedia.design.R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHintDescription(String hintDescription) {
        this.hintDescription = hintDescription;
    }

    public void setGroupAds(List<GroupAd> groupAds) {
        this.groupAds = groupAds;
        for (GroupAd groupAd : groupAds) {
            groupNames.add(groupAd.getName());
        }
        if (inputChooseGroup != null) {
            inputChooseGroup.showDropDownFilter();
        }
    }

    public void resetDialog(){
        this.groupAds.clear();
        groupNames.clear();
        if (textInputLayoutChooseGroup != null) {
            textInputLayoutChooseGroup.setError(null);
        }
    }

    public void setError(String message) {
        if (textInputLayoutChooseGroup != null) {
            textInputLayoutChooseGroup.setError(message);
        }
    }

    public void setSelectedGroup(String groupName) {
        selectedGroupName = groupName;
    }

    public interface OnSelectGroupListener {
        void searchGroup(String query);
        void submitGroup(String selectedId);
    }
}
