package com.tokopedia.checkout.view.feature.addressoptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.mapper.AddressCornerMapper;
import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.DaggerShipmentAddressListComponent;
import com.tokopedia.checkout.view.di.component.ShipmentAddressListComponent;
import com.tokopedia.checkout.view.di.module.ShipmentAddressListModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.addressoptions.adapter.ShipmentAddressListAdapter;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.logisticcommon.LogisticCommonConstant;
import com.tokopedia.logisticdata.data.entity.address.Destination;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity.EXTRA_CURRENT_ADDRESS;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShipmentAddressListFragment extends BaseCheckoutFragment implements
        ISearchAddressListView<List<RecipientAddressModel>>,
        SearchInputView.Listener,
        SearchInputView.ResetListener,
        ShipmentAddressListAdapter.ActionListener {

    private static final int ORDER_ASC = 1;
    private static final String PARAMS = "params";
    private static final String CHOOSE_ADDRESS_TRACE = "mp_choose_another_address";
    public static final String TAG_CORNER_BS = "TAG_CORNER_BS";

    private RecyclerView mRvRecipientAddressList;
    private SearchInputView mSvAddressSearchBox;
    private SwipeToRefresh swipeToRefreshLayout;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llNoResult;
    private RelativeLayout rlContent;
    private Button btChangeSearch;
    private CornerBottomSheet mCornerBottomSheet;

    private InputMethodManager mInputMethodManager;
    private ICartAddressChoiceActivityListener mCartAddressChoiceActivityListener;
    private int maxItemPosition;
    private boolean isLoading;
    private boolean isMenuVisible;

    private ICartAddressChoiceActivityListener mCartAddressChoiceListener;

    private PerformanceMonitoring chooseAddressTracePerformance;
    private boolean isChooseAddressTraceStopped;

    @Inject
    ShipmentAddressListAdapter mShipmentAddressListAdapter;

    @Inject
    ShipmentAddressListPresenter mShipmentAddressListPresenter;

    @Inject
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;

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
                .cartComponent(getComponent(CartComponent.class))
                .shipmentAddressListModule(new ShipmentAddressListModule(getActivity(), this))
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .build();
        component.inject(this);
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    protected String getScreenName() {
        return ConstantTransactionAnalytics.ScreenName.ADDRESS_LIST_PAGE;
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
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isMenuVisible) {
            menu.clear();
            inflater.inflate(R.menu.menu_address_choice, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_address && getActivity() != null) {
            checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickTambahAlamatBaruFromGantiAlamat();
            checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickTambahFromAlamatPengiriman();
            startActivityForResult(((ICheckoutModuleRouter) getActivity().getApplication()).getAddAddressIntent(
                    getActivity(), null, token, false, false
                    ),
                    LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initialListener(Activity activity) {
        mCartAddressChoiceActivityListener = (ICartAddressChoiceActivityListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_address_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseAddressTracePerformance = PerformanceMonitoring.start(CHOOSE_ADDRESS_TRACE);
    }

    @Override
    protected void initView(View view) {
        mCartAddressChoiceListener.setToolbarTitle(getActivity().getString(R.string.checkout_module_title_shipping_dest_multiple_address));
        checkoutAnalyticsChangeAddress.eventViewAtcCartChangeAddressImpressionChangeAddress();
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
                isLoading = true;
                mSvAddressSearchBox.getSearchTextView().setText("");
                maxItemPosition = 0;
                onSearchReset();
            }
        });
        mRvRecipientAddressList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                int totalItemCount = adapter.getItemCount();
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();

                if (maxItemPosition < lastVisibleItemPosition) {
                    maxItemPosition = lastVisibleItemPosition;
                }

                if ((maxItemPosition + 1) == totalItemCount) {
                    if (!isLoading && mShipmentAddressListPresenter.hasNext()) {
                        performSearch(mSvAddressSearchBox.getSearchText(), false);
                    }
                }
            }
        });

        isMenuVisible = false;
        getActivity().invalidateOptionsMenu();

    }

    @Override
    protected void setViewListener() {
        if (getActivity() != null) {
            mShipmentAddressListPresenter.attachView(this);
            mInputMethodManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    @Override
    protected void initialVar() {
        mRvRecipientAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvRecipientAddressList.setAdapter(mShipmentAddressListAdapter);
    }

    @Override
    protected void setActionVar() {
        initSearchView();
        onSearchReset();
    }

    @Override
    public void showList(List<RecipientAddressModel> recipientAddressModels) {
        if (!isMenuVisible && !recipientAddressModels.isEmpty() && getActivity() != null) {
            isMenuVisible = true;
            getActivity().invalidateOptionsMenu();
        }
        mShipmentAddressListAdapter.setAddressList(recipientAddressModels);
        mShipmentAddressListAdapter.notifyDataSetChanged();
        mRvRecipientAddressList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSampai() {
        mShipmentAddressListAdapter.showSampaiWithoutSelected();
    }

    @Override
    public void setSampai(CornerAddressModel cornerAddressModel) {
        mShipmentAddressListAdapter.setSampai(cornerAddressModel);
    }

    @Override
    public void populateCorner(List<CornerAddressModel> cornerAddressModelList) {
        mCornerBottomSheet = CornerBottomSheet.newInstance(cornerAddressModelList);
        mCornerBottomSheet.setOnBranchChosenListener(
                corner -> mShipmentAddressListAdapter.setSampai(corner));
    }

    @Override
    public void showCornerBottomSheet() {
        if (mCornerBottomSheet != null && getFragmentManager() != null)
            mCornerBottomSheet.show(getFragmentManager(), TAG_CORNER_BS);
    }

    @Override
    public void navigateToCheckoutPage(RecipientAddressModel recipientAddressModel) {
        onAddressContainerClicked(recipientAddressModel, -1);
    }

    @Override
    public void stopTrace() {
        if (!isChooseAddressTraceStopped) {
            chooseAddressTracePerformance.stopTrace();
            isChooseAddressTraceStopped = true;
        }
    }

    @Override
    public void updateList(List<RecipientAddressModel> recipientAddressModels) {
        mShipmentAddressListAdapter.updateAddressList(recipientAddressModels);
        mRvRecipientAddressList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showListEmpty() {
        if (getActivity() != null) {
            isMenuVisible = false;
            getActivity().invalidateOptionsMenu();
        }
        mShipmentAddressListAdapter.setAddressList(new ArrayList<>());
        mShipmentAddressListAdapter.notifyDataSetChanged();
        mRvRecipientAddressList.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        llNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        if (getActivity() != null) {
            isMenuVisible = false;
            getActivity().invalidateOptionsMenu();
        }
        rlContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        llNoResult.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        String keyword = mSvAddressSearchBox.getSearchText();
                        performSearch(!TextUtils.isEmpty(keyword) ? keyword : "", true);
                    }
                });
    }

    @Override
    public void showLoading() {
        isLoading = true;
        swipeToRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        isLoading = false;
        rlContent.setVisibility(View.VISIBLE);
        llNetworkErrorView.setVisibility(View.GONE);
        llNoResult.setVisibility(View.GONE);
        swipeToRefreshLayout.setRefreshing(false);
    }

    @Override
    public void resetPagination() {
        maxItemPosition = 0;
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
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
        performSearch(text, true);
    }

    @Override
    public void onSearchTextChanged(String text) {
        openSoftKeyboard();
    }

    @Override
    public void onSearchReset() {
        if (getArguments() != null) {
            mShipmentAddressListPresenter.resetAddressList(getActivity(), ORDER_ASC,
                    getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
        }
    }

    private void performSearch(String query, boolean resetPage) {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya();
        if (getArguments() != null) {
            if (!query.isEmpty()) {
                mShipmentAddressListPresenter.getAddressList(getActivity(), ORDER_ASC, query,
                        (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS), true);
            } else {
                mShipmentAddressListPresenter.getAddressList(getActivity(), ORDER_ASC, "",
                        (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS), resetPage);
            }
        }
    }

    private void openSoftKeyboard() {
        if (mInputMethodManager != null) {
            mInputMethodManager.showSoftInput(
                    mSvAddressSearchBox.getSearchTextView(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onAddressContainerClicked(RecipientAddressModel model, int position) {
        mShipmentAddressListAdapter.updateSelected(position);
        if (mCartAddressChoiceActivityListener != null && getActivity() != null) {
            KeyboardHandler.hideSoftKeyboard(getActivity());
            sendAnalyticsOnAddressSelectionClicked();
            mCartAddressChoiceActivityListener.finishSendResultActionSelectedAddress(model);
        }
    }

    @Override
    public void onCornerAddressClicked(CornerAddressModel cornerAddressModel, int position) {
        mShipmentAddressListAdapter.updateSelected(position);
        if (mCartAddressChoiceActivityListener != null && getActivity() != null){
            RecipientAddressModel result = AddressCornerMapper.converToCartModel(cornerAddressModel);
            mCartAddressChoiceActivityListener.finishSendResultActionSelectedAddress(result);
        }
    }

    private void sendAnalyticsOnAddressSelectionClicked() {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickChecklistAlamatFromPilihAlamatLainnya();
        checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickRadioButtonFromPilihAlamatLainnya();
    }

    @Override
    public void onEditClick(RecipientAddressModel model) {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickUbahFromPilihAlamatLainnya();
        AddressModelMapper mapper = new AddressModelMapper();

        if (getActivity() != null) {
            Intent intent = ((ICheckoutModuleRouter) getActivity().getApplication()).getAddAddressIntent(
                    getActivity(), mapper.transform(model), token, true, false
            );
            startActivityForResult(intent, LogisticCommonConstant.REQUEST_CODE_PARAM_EDIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE:
                    RecipientAddressModel newRecipientAddressModel = null;
                    if (data != null && data.hasExtra(LogisticCommonConstant.EXTRA_ADDRESS)) {
                        Destination newAddress = data.getParcelableExtra(LogisticCommonConstant.EXTRA_ADDRESS);
                        newRecipientAddressModel = new RecipientAddressModel();
                        newRecipientAddressModel.setAddressName(newAddress.getAddressName());
                        newRecipientAddressModel.setDestinationDistrictId(newAddress.getDistrictId());
                        newRecipientAddressModel.setCityId(newAddress.getCityId());
                        newRecipientAddressModel.setProvinceId(newAddress.getProvinceId());
                        newRecipientAddressModel.setRecipientName(newAddress.getReceiverName());
                        newRecipientAddressModel.setRecipientPhoneNumber(newAddress.getReceiverPhone());
                        newRecipientAddressModel.setStreet(newAddress.getAddressStreet());
                        newRecipientAddressModel.setPostalCode(newAddress.getPostalCode());
                        mShipmentAddressListPresenter.getAddressFromNewCreated(getActivity(), newRecipientAddressModel);
                    }
                    onSearchReset();
                    break;
                case LogisticCommonConstant.REQUEST_CODE_PARAM_EDIT:
                    onSearchReset();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCartAddressChoiceListener = (ICartAddressChoiceActivityListener) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkoutAnalyticsChangeAddress.sendScreenName(getActivity(), getScreenName());
    }

    @Override
    public void onAddAddressButtonClicked() {
        if (getActivity() != null)
            startActivityForResult(((ICheckoutModuleRouter) getActivity().getApplication()).getAddAddressIntent(
                    getActivity(), null, token, false, false),
                    LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE);
    }

    @Override
    public void onCornerButtonClicked() {
        showCornerBottomSheet();
    }
}