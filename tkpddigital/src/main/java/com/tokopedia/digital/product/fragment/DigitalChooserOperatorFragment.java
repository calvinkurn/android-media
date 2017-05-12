package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.adapter.OperatorChooserAdapter;
import com.tokopedia.digital.product.model.Operator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserOperatorFragment extends BasePresenterFragment {

    private static final String ARG_PARAM_EXTRA_OPERATOR_LIST_DATA =
            "ARG_PARAM_EXTRA_OPERATOR_LIST_DATA";
    private static final String ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW =
            "ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW";

    @BindView(R2.id.rv_list_chooser)
    RecyclerView rvOperatorList;
    @BindView(R2.id.search_operator)
    SearchView operatorSerachView;

    private List<Operator> operatorListData;
    private String operatorStyleView;
    private ActionListener actionListener;
    private OperatorChooserAdapter operatorChooserAdapter;

    public static Fragment newInstance(List<Operator> operatorListData, String operatorStyleView) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) operatorListData);
        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW, operatorStyleView);
        Fragment fragment = new DigitalChooserOperatorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
        operatorSerachView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fiterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                checkEmptyQuery(newText);
                return false;
            }
        });
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        operatorListData = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_OPERATOR_LIST_DATA);
        this.operatorStyleView = arguments.getString(ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_chooser_product_digital_module;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        rvOperatorList.setAdapter(operatorChooserAdapter);
    }

    @Override
    protected void initialVar() {
        operatorChooserAdapter = new OperatorChooserAdapter(this, operatorListData,
                "1",
                actionListener);
    }

    @Override
    protected void setActionVar() {

    }

    public interface ActionListener {
        void onOperatorItemSelected(Operator operator);

        void onOperatortItemChooserCanceled();
    }

    private void fiterData(String query) {
        List<Operator> searchOperatorList = new ArrayList<>();
        for (int i = 0; i < operatorListData.size(); i++) {
            if(operatorListData.get(i).getName().toLowerCase()
                    .contains(query.toLowerCase())) {
                searchOperatorList.add(operatorListData.get(i));
            }
        }
        operatorChooserAdapter.setSearchResultData(searchOperatorList);
        operatorChooserAdapter.notifyDataSetChanged();
    }

    private void checkEmptyQuery(String query) {
        if(query.isEmpty())
        operatorChooserAdapter.setSearchResultData(operatorListData);
    }
}
