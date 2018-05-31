package com.tokopedia.checkout.view.view.addressoptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.DaggerShipmentAddressListComponent;
import com.tokopedia.checkout.view.di.component.ShipmentAddressListComponent;
import com.tokopedia.checkout.view.di.module.ShipmentAddressListModule;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.design.text.SearchInputView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_CURRENT_ADDRESS;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShipmentAddressListFragment extends BaseCheckoutFragment implements
        ISearchAddressListView<List<RecipientAddressModel>>,
        SearchInputView.Listener,
        SearchInputView.ResetListener,
        ShipmentAddressListAdapter.ActionListener {

    private static final String TAG = ShipmentAddressListFragment.class.getSimpleName();

    private static final int ORDER_ASC = 1;
    private static final String PARAMS = "params";

    RecyclerView mRvRecipientAddressList;
    SearchInputView mSvAddressSearchBox;
    SwipeToRefresh swipeToRefreshLayout;
    LinearLayout llNetworkErrorView;
    LinearLayout llNoResult;
    RelativeLayout rlContent;
    Button btChangeSearch;

    InputMethodManager mInputMethodManager;
    ICartAddressChoiceActivityListener mCartAddressChoiceActivityListener;

    @Inject
    ShipmentAddressListAdapter mShipmentAddressListAdapter;

    @Inject
    ShipmentAddressListPresenter mShipmentAddressListPresenter;

    private Token token;

    public static ShipmentAddressListFragment newInstance(RecipientAddressModel currentAddress) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress);
        ShipmentAddressListFragment shipmentAddressListFragment = new ShipmentAddressListFragment();
        shipmentAddressListFragment.setArguments(bundle);
        return shipmentAddressListFragment;
    }

    public static ShipmentAddressListFragment newInstance(HashMap<String, String> params) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS, params);

        ShipmentAddressListFragment fragment = new ShipmentAddressListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void initInjector() {
        ShipmentAddressListComponent component = DaggerShipmentAddressListComponent.builder()
                .shipmentAddressListModule(new ShipmentAddressListModule(this))
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return TAG;
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

    /**
     * apakah fragment ini support options menu?
     *
     * @return iya atau tidak
     */
    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    /**
     * Cast si activity ke listener atau bisa juga ini untuk context activity
     *
     * @param activity si activity yang punya fragment
     */
    @Override
    protected void initialListener(Activity activity) {
        mCartAddressChoiceActivityListener = (ICartAddressChoiceActivityListener) activity;
    }

    /**
     * kalau memang argument tidak kosong. ini data argumentnya
     *
     * @param arguments argument nya
     */
    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_address_list;
    }

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    @Override
    protected void initView(View view) {
        mRvRecipientAddressList = view.findViewById(R.id.rv_address_list);
        mSvAddressSearchBox = view.findViewById(R.id.sv_address_search_box);
        swipeToRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        llNoResult = view.findViewById(R.id.ll_no_result);
        rlContent = view.findViewById(R.id.rl_content);
        btChangeSearch = view.findViewById(R.id.bt_change_search);
        btChangeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSvAddressSearchBox.getSearchTextView().setText("");
                mSvAddressSearchBox.getSearchTextView().requestFocus();
            }
        });
        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSvAddressSearchBox.getSearchTextView().setText("");
                onSearchReset();
            }
        });
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mShipmentAddressListPresenter.attachView(this);
        mInputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    @Override
    protected void initialVar() {
        mRvRecipientAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvRecipientAddressList.setAdapter(mShipmentAddressListAdapter);
    }

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction();
     */
    @Override
    protected void setActionVar() {
        initSearchView();
        onSearchReset();
    }

    @Override
    public void showList(List<RecipientAddressModel> recipientAddressModels) {
        mShipmentAddressListAdapter.setAddressList(recipientAddressModels);
        mShipmentAddressListAdapter.notifyDataSetChanged();
        mRvRecipientAddressList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showListEmpty() {
        mShipmentAddressListAdapter.setAddressList(new ArrayList<RecipientAddressModel>());
        mShipmentAddressListAdapter.notifyDataSetChanged();
        mRvRecipientAddressList.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        llNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        rlContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        llNoResult.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        String keyword = mSvAddressSearchBox.getSearchText();
                        performSearch(!TextUtils.isEmpty(keyword) ? keyword : "");
                    }
                });
    }

    @Override
    public void showLoading() {
        swipeToRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        rlContent.setVisibility(View.VISIBLE);
        llNetworkErrorView.setVisibility(View.GONE);
        llNoResult.setVisibility(View.GONE);
        swipeToRefreshLayout.setRefreshing(false);
    }

    private void initSearchView() {
        mSvAddressSearchBox.getSearchTextView().setOnClickListener(onSearchViewClickListener());
        mSvAddressSearchBox.getSearchTextView().setOnTouchListener(onSearchViewTouchListener());

        mSvAddressSearchBox.setListener(this);
        mSvAddressSearchBox.setResetListener(this);
        mSvAddressSearchBox.setSearchHint(getString(R.string.label_hint_search_address));
    }

    private View.OnTouchListener onSearchViewTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mSvAddressSearchBox.getSearchTextView().setCursorVisible(true);
                openSoftKeyboard();
                return false;
            }
        };
    }

    private View.OnClickListener onSearchViewClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSvAddressSearchBox.getSearchTextView().setCursorVisible(true);
                openSoftKeyboard();
            }
        };
    }

    @Override
    public void onSearchSubmitted(String text) {
        performSearch(text);
        closeSoftKeyboard();
    }

    @Override
    public void onSearchTextChanged(String text) {
        openSoftKeyboard();
//        performSearch(text);
    }

    @Override
    public void onSearchReset() {
        mShipmentAddressListPresenter.resetAddressList(getActivity(), ORDER_ASC,
                (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
        closeSoftKeyboard();
    }

    private void performSearch(String query) {
        if (!query.isEmpty()) {
            mShipmentAddressListPresenter.getAddressList(getActivity(), ORDER_ASC, query,
                    (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
        } else {
            onSearchReset();
        }
    }

    private void openSoftKeyboard() {
        if (mInputMethodManager != null) {
            mInputMethodManager.showSoftInput(
                    mSvAddressSearchBox.getSearchTextView(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void closeSoftKeyboard() {
        if (mInputMethodManager != null) {
            mInputMethodManager.hideSoftInputFromWindow(
                    mSvAddressSearchBox.getSearchTextView().getWindowToken(), 0);
        }
    }

    @Override
    public void onAddressContainerClicked(RecipientAddressModel model) {
        if (mCartAddressChoiceActivityListener != null) {
            mCartAddressChoiceActivityListener.finishSendResultActionSelectedAddress(model);
        }
    }

    @Override
    public void onEditClick(RecipientAddressModel model) {
        AddressModelMapper mapper = new AddressModelMapper();

        Intent intent = AddAddressActivity.createInstance(getActivity(), mapper.transform(model), token);
        startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
    }

}