package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.adapter.NumberListAdapter;
import com.tokopedia.digital.product.listener.ISearchNumberDigitalView;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.presenter.ISearchNumberDigitalPresenter;
import com.tokopedia.digital.product.presenter.SearchNumberDigitalPresenter;
import com.tokopedia.digital.widget.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.interactor.DigitalWidgetInteractor;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class DigitalSearchNumberFragment extends BasePresenterFragment<ISearchNumberDigitalPresenter>
        implements ISearchNumberDigitalView, NumberListAdapter.OnClientNumberClickListener {

    @BindView(R2.id.recyclerview_number_list)
    RecyclerView rvNumberList;
    @BindView(R2.id.edittext_search_number)
    EditText editTextSearchNumber;

    private NumberListAdapter numberListAdapter;

    private List<OrderClientNumber> clientNumbers;

    private static final String ARG_PARAM_EXTRA_NUMBER_LIST = "ARG_PARAM_EXTRA_NUMBER_LIST";
    private static final String ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER";
    private static final String ARG_PARAM_EXTRA_CATEGORY_ID = "ARG_PARAM_EXTRA_CATEGORY_ID";

    private String categoryId;
    private String clientNumber;

    public static Fragment newInstance(String categoryId, String clientNumber, List<OrderClientNumber> numberList) {
        Fragment fragment = new DigitalSearchNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumber);
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST,
                (ArrayList<? extends Parcelable>) numberList);
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
        IDigitalWidgetInteractor digitalWidgetInteractor = new DigitalWidgetInteractor(new CompositeSubscription(),
                new DigitalWidgetRepository(new RechargeService(), new DigitalEndpointService(), new FavoriteNumberListDataMapper()));

        presenter = new SearchNumberDigitalPresenter(this, digitalWidgetInteractor);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        categoryId = arguments.getString(ARG_PARAM_EXTRA_CATEGORY_ID);
        clientNumber = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER);
        clientNumbers = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search_number_digital;
    }

    @Override
    protected void initView(View view) {
        numberListAdapter = new NumberListAdapter(this, clientNumbers);
        rvNumberList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNumberList.setAdapter(numberListAdapter);
    }

    @Override
    protected void setViewListener() {
        editTextSearchNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterData(String query) {
        List<OrderClientNumber> searchClientNumbers = new ArrayList<>();
        for (OrderClientNumber orderClientNumber : clientNumbers) {
            if (orderClientNumber.getClientNumber().contains(query)) {
                searchClientNumbers.add(orderClientNumber);
            }
        }
        numberListAdapter.setNumbers(searchClientNumbers);
        numberListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void renderNumberList(List<OrderClientNumber> orderClientNumbers) {
//        this.clientNumbers.clear();
//        this.clientNumbers.addAll(orderClientNumbers);
    }

    @Override
    public void onClientNumberClicked(String number) {
        editTextSearchNumber.setText(number);
    }

}
