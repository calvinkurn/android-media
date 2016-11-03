package com.tokopedia.tkpd.rescenter.create.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.rescenter.create.customadapter.TroubleSpinnerAdapter;
import com.tokopedia.tkpd.rescenter.create.listener.ChooseTroubleListener;
import com.tokopedia.tkpd.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created on 8/3/16.
 */
public class ChooseTroubleSectionCreateResCenterView extends BaseView<CreateResCenterFormData, ChooseTroubleListener> {

    @Bind(R.id.spinner_trouble)
    public Spinner troubleSpinner;
    @Bind(R.id.view_desc)
    public View descBoxView;
    @Bind(R.id.box_desc)
    public EditText descEditText;


    public ChooseTroubleSectionCreateResCenterView(Context context) {
        super(context);
    }

    public ChooseTroubleSectionCreateResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ChooseTroubleListener chooseTroubleListener) {
        this.listener = chooseTroubleListener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_rescenter_choose_trouble;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull CreateResCenterFormData createResCenterFormData) {

    }

    public CreateResCenterFormData.TroubleData getTroubleChoosen() {
        return (CreateResCenterFormData.TroubleData) troubleSpinner.getItemAtPosition(troubleSpinner.getSelectedItemPosition() - 1);
    }

    public String getDescription() {
        return String.valueOf(descEditText.getText());
    }

    public void renderSpinner(CreateResCenterFormData.TroubleCategoryData troubleCategoryChoosen) {
        TroubleSpinnerAdapter troubleAdapter = new TroubleSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, troubleCategoryChoosen.getTroubleList());
        troubleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        troubleSpinner.setAdapter(troubleAdapter);
    }

    public void resetSpinner() {
        TroubleSpinnerAdapter troubleAdapter = new TroubleSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, new ArrayList<CreateResCenterFormData.TroubleData>());
        troubleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        troubleSpinner.setAdapter(troubleAdapter);
    }
}
