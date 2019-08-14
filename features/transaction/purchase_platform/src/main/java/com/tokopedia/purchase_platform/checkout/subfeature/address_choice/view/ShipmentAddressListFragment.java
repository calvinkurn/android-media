package com.tokopedia.purchase_platform.checkout.subfeature.address_choice.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.domain.mapper.AddressModelMapper;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment;
import com.tokopedia.purchase_platform.common.di.component.CartComponent;
import com.tokopedia.purchase_platform.common.di.component.DaggerShipmentAddressListComponent;
import com.tokopedia.purchase_platform.common.di.component.ShipmentAddressListComponent;
import com.tokopedia.purchase_platform.common.di.module.ShipmentAddressListModule;
import com.tokopedia.purchase_platform.common.di.module.TrackingAnalyticsModule;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.view.recyclerview.ShipmentAddressListAdapter;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.logisticaddaddress.AddressConstants;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressActivity;
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics;
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity;
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel;
import com.tokopedia.logisticdata.data.constant.LogisticCommonConstant;
import com.tokopedia.logisticdata.data.entity.address.Destination;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.purchase_platform.checkout.analytics.CornerAnalytics;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.purchase_platform.checkout.view.CartConstant.SCREEN_NAME_CART_EXISTING_USER;
import static com.tokopedia.purchase_platform.checkout.subfeature.address_choice.view.CartAddressChoiceActivity.EXTRA_CURRENT_ADDRESS;
import static com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_ADD_NEW_ADDRESS_KEY;

/**
 * @author Aghny A. Putra on 25/01/18
 * Fajar U N
 */
public class ShipmentAddressListFragment extends BaseCheckoutFragment implements
        AddressListContract.View, SearchInputView.Listener,
        SearchInputView.ResetListener, ShipmentAddressListAdapter.ActionListener {

    private static final String CHOOSE_ADDRESS_TRACE = "mp_choose_another_address";
    public static final String ARGUMENT_DISABLE_CORNER = "ARGUMENT_DISABLE_CORNER";
    public static final String ARGUMENT_ORIGIN_DIRECTION_TYPE = "ARGUMENT_ORIGIN_DIRECTION_TYPE";
    public static final int ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM = 1;
    public static final int ORIGIN_DIRECTION_TYPE_DEFAULT = 0;
    public final String EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW";

    private RecyclerView mRvRecipientAddressList;
    private SearchInputView mSvAddressSearchBox;
    private SwipeToRefresh swipeToRefreshLayout;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llNoResult;
    private RelativeLayout rlContent;
    private Button btChangeSearch;

    private InputMethodManager mInputMethodManager;
    private ICartAddressChoiceActivityListener mActivityListener;
    private int maxItemPosition;
    private boolean isLoading;
    private boolean isDisableCorner;
    private RecipientAddressModel mCurrentAddress;

    private PerformanceMonitoring chooseAddressTracePerformance;
    private boolean isChooseAddressTraceStopped;

    private FirebaseRemoteConfigImpl remoteConfig;

    ShipmentAddressListAdapter mAdapter;
    @Inject
    AddressListContract.Presenter mPresenter;
    @Inject
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    @Inject
    CornerAnalytics mCornerAnalytics;
    @Inject
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress;

    private Token token;
    private int originDirectionType;

    public static ShipmentAddressListFragment newInstance(RecipientAddressModel currentAddress) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress);
        bundle.putBoolean(ARGUMENT_DISABLE_CORNER, false);
        bundle.putInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_DEFAULT);
        ShipmentAddressListFragment shipmentAddressListFragment = new ShipmentAddressListFragment();
        shipmentAddressListFragment.setArguments(bundle);
        return shipmentAddressListFragment;
    }

    public static ShipmentAddressListFragment newInstance(RecipientAddressModel currentAddress,
                                                          boolean isDisableCorner) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress);
        bundle.putBoolean(ARGUMENT_DISABLE_CORNER, isDisableCorner);
        bundle.putInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_DEFAULT);
        ShipmentAddressListFragment shipmentAddressListFragment = new ShipmentAddressListFragment();
        shipmentAddressListFragment.setArguments(bundle);
        return shipmentAddressListFragment;
    }

    public static ShipmentAddressListFragment newInstanceFromMultipleAddressForm
            (RecipientAddressModel currentAddress,
             boolean isDisableCorner) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress);
        bundle.putBoolean(ARGUMENT_DISABLE_CORNER, isDisableCorner);
        bundle.putInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM);
        ShipmentAddressListFragment shipmentAddressListFragment = new ShipmentAddressListFragment();
        shipmentAddressListFragment.setArguments(bundle);
        return shipmentAddressListFragment;
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
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_address_list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityListener = (ICartAddressChoiceActivityListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseAddressTracePerformance = PerformanceMonitoring.start(CHOOSE_ADDRESS_TRACE);
        remoteConfig = new FirebaseRemoteConfigImpl(getContext());
        if (getArguments() != null) {
            mCurrentAddress = getArguments().getParcelable(EXTRA_CURRENT_ADDRESS);
            isDisableCorner = getArguments().getBoolean(ARGUMENT_DISABLE_CORNER, false) ||
                    isDisableSampaiView();
            originDirectionType = getArguments().getInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_DEFAULT);
        }
    }

    @Override
    protected void initView(View view) {
        checkoutAnalyticsChangeAddress.eventViewAtcCartChangeAddressImpressionChangeAddress();
        mRvRecipientAddressList = view.findViewById(R.id.rv_address_list);
        mSvAddressSearchBox = view.findViewById(R.id.sv_address_search_box);
        swipeToRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        llNoResult = view.findViewById(R.id.ll_no_result);
        rlContent = view.findViewById(R.id.rl_content);
        btChangeSearch = view.findViewById(R.id.bt_change_search);
        btChangeSearch.setOnClickListener(view1 -> {
            mSvAddressSearchBox.getSearchTextView().setText("");
            mSvAddressSearchBox.getSearchTextView().requestFocus();
        });
        swipeToRefreshLayout.setOnRefreshListener(() -> {
            isLoading = true;
            mSvAddressSearchBox.getSearchTextView().setText("");
            maxItemPosition = 0;
            onSearchReset();
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

                if ((maxItemPosition + 1) == totalItemCount && !isLoading && dy > 0) {
                    mPresenter.loadMore();
                }
            }
        });
        mAdapter = new ShipmentAddressListAdapter(this);

        mPresenter.attachView(this);
    }

    @Override
    protected void setViewListener() {
        if (getActivity() != null) {
            mInputMethodManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    @Override
    protected void initialVar() {
        mRvRecipientAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvRecipientAddressList.setAdapter(mAdapter);
        if (!mAdapter.isHavingCornerAddress()) {
            RecipientAddressModel cached = mPresenter.getLastCorner();
            if (cached != null) {
                mAdapter.setCorner(cached);
            }
        }
        if (isDisableCorner) mAdapter.hideCornerOption();
    }

    @Override
    protected void setActionVar() {
        initSearchView();
        onSearchReset();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.detachView();
    }

    @Override
    public void showList(@NotNull List<RecipientAddressModel> list) {
        maxItemPosition = 0;
        String selectedId = (mCurrentAddress != null) ? mCurrentAddress.getId() : "";
        mAdapter.setAddressList(list, selectedId);
        mRvRecipientAddressList.setVisibility(View.VISIBLE);
        llNetworkErrorView.setVisibility(View.GONE);
        llNoResult.setVisibility(View.GONE);
    }

    @Override
    public void onChooseCorner(@NotNull RecipientAddressModel cornerAddressModel) {
        mCurrentAddress = cornerAddressModel;
        mAdapter.setCorner(cornerAddressModel);

        // Immediately choose the corner then go to shipment page
        onCornerAddressClicked(cornerAddressModel, 0);
    }

    @Override
    public void navigateToCheckoutPage(@NotNull RecipientAddressModel recipientAddressModel) {
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
    public void updateList(@NotNull List<RecipientAddressModel> recipientAddressModels) {
        mAdapter.updateAddressList(recipientAddressModels);
        mRvRecipientAddressList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showListEmpty() {
        mAdapter.setAddressList(new ArrayList<>(), null);
        mRvRecipientAddressList.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        llNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(@NotNull Throwable e) {
        rlContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        llNoResult.setVisibility(View.GONE);
        String message = ErrorHandler.getErrorMessage(getContext(), e);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                () -> {
                    String keyword = mSvAddressSearchBox.getSearchText();
                    performSearch(keyword, true);
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
        swipeToRefreshLayout.setRefreshing(false);
    }

    @Override
    public void resetPagination() {
        maxItemPosition = 0;
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
        mPresenter.getAddress();
    }

    private void performSearch(String query, boolean resetPage) {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya();
        checkoutAnalyticsChangeAddress.eventClickAddressCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya();
        if (!query.isEmpty()) {
            mPresenter.searchAddress(query);
        } else {
            mPresenter.getAddress();
        }
    }

    @Override
    public void onAddressContainerClicked(RecipientAddressModel model, int position) {
        mAdapter.updateSelected(position);
        if (mActivityListener != null && getActivity() != null) {
            KeyboardHandler.hideSoftKeyboard(getActivity());
            sendAnalyticsOnAddressSelectionClicked();
            mActivityListener.finishAndSendResult(model);
        }
    }

    @Override
    public void onCornerAddressClicked(RecipientAddressModel addressModel, int position) {
        mPresenter.saveLastCorner(addressModel);
        mAdapter.updateSelected(position);
        if (mActivityListener != null && getActivity() != null) {
            mCornerAnalytics.sendChooseCornerAddress();
            mActivityListener.finishAndSendResult(addressModel);
        } else {
            // Show error in case of unexpected behaviour
            this.showError(new Throwable());
        }
    }

    @Override
    public void onEditClick(RecipientAddressModel model) {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickUbahFromPilihAlamatLainnya();
        AddressModelMapper mapper = new AddressModelMapper();

        if (getActivity() != null) {
            Intent intent;
            if (originDirectionType == ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM) {
                intent = AddAddressActivity.createInstanceEditAddressFromCheckoutMultipleAddressForm(
                        getActivity(), mapper.transform(model), token
                );
            } else {
                intent = AddAddressActivity.createInstanceEditAddressFromCheckoutSingleAddressForm(
                        getActivity(), mapper.transform(model), token
                );
            }
            startActivityForResult(intent, LogisticCommonConstant.REQUEST_CODE_PARAM_EDIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LogisticCommonConstant.REQUEST_CODE_PARAM_EDIT:
                case LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE:
                    RecipientAddressModel address = null;
                    if (data != null && data.hasExtra(LogisticCommonConstant.EXTRA_ADDRESS)) {
                        Destination intentModel = data.getParcelableExtra(LogisticCommonConstant.EXTRA_ADDRESS);
                        address = new RecipientAddressModel();
                        address.setId(intentModel.getAddressId());
                        address.setAddressName(intentModel.getAddressName());
                        address.setDestinationDistrictId(intentModel.getDistrictId());
                        address.setCityId(intentModel.getCityId());
                        address.setProvinceId(intentModel.getProvinceId());
                        address.setRecipientName(intentModel.getReceiverName());
                        address.setRecipientPhoneNumber(intentModel.getReceiverPhone());
                        address.setStreet(intentModel.getAddressStreet());
                        address.setPostalCode(intentModel.getPostalCode());
                    }
                    mActivityListener.finishAndSendResult(address);
                    break;
                case LogisticCommonConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY:
                case LogisticCommonConstant.ADD_NEW_ADDRESS_CREATED:
                    RecipientAddressModel newAddress = new RecipientAddressModel();
                    if (data != null && data.hasExtra(EXTRA_ADDRESS_NEW)) {
                        SaveAddressDataModel intentModel = data.getParcelableExtra(EXTRA_ADDRESS_NEW);
                        newAddress.setId(String.valueOf(intentModel.getId()));
                        newAddress.setAddressName(intentModel.getAddressName());
                        newAddress.setDestinationDistrictId(String.valueOf(intentModel.getDistrictId()));
                        newAddress.setCityId(String.valueOf(intentModel.getCityId()));
                        newAddress.setProvinceId(String.valueOf(intentModel.getProvinceId()));
                        newAddress.setRecipientName(intentModel.getReceiverName());
                        newAddress.setRecipientPhoneNumber(intentModel.getPhone());
                        newAddress.setStreet(intentModel.getFormattedAddress());
                        newAddress.setPostalCode(intentModel.getPostalCode());
                    }
                    mActivityListener.finishAndSendResult(newAddress);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAddAddressButtonClicked() {
        if (getActivity() != null) {
            if (originDirectionType == ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM) {
                checkoutAnalyticsMultipleAddress.eventClickAddressCartMultipleAddressClickPlusFromMultiple();

                if (isAddNewAddressEnabled()) {
                    AddNewAddressAnalytics.sendScreenName(getActivity(), SCREEN_NAME_CART_EXISTING_USER);
                    startActivityForResult(PinpointMapActivity.newInstance(getActivity(),
                            AddressConstants.MONAS_LAT, AddressConstants.MONAS_LONG, true, token,
                            false, false, false, null,
                            false), LogisticCommonConstant.ADD_NEW_ADDRESS_CREATED);

                } else {
                    startActivityForResult(
                            AddAddressActivity.createInstanceAddAddressFromCheckoutMultipleAddressForm(
                                    getActivity(), token
                            ), LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE);
                }

            } else {
                checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickTambahAlamatBaruFromGantiAlamat();
                checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickTambahFromAlamatPengiriman();

                if (isAddNewAddressEnabled()) {
                    AddNewAddressAnalytics.sendScreenName(getActivity(), SCREEN_NAME_CART_EXISTING_USER);
                    startActivityForResult(PinpointMapActivity.newInstance(getActivity(),
                            AddressConstants.MONAS_LAT, AddressConstants.MONAS_LONG, true, token,
                            false, false, false, null,
                            false), LogisticCommonConstant.ADD_NEW_ADDRESS_CREATED);

                } else {
                    startActivityForResult(
                            AddAddressActivity.createInstanceAddAddressFromCheckoutSingleAddressForm(
                                    getActivity(), token
                            ), LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE
                    );
                }
            }


        }
    }

    @Override
    public void onCornerButtonClicked() {
        mActivityListener.requestCornerList();
    }

    private void openSoftKeyboard() {
        if (mInputMethodManager != null) {
            mInputMethodManager.showSoftInput(
                    mSvAddressSearchBox.getSearchTextView(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void sendAnalyticsOnAddressSelectionClicked() {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickChecklistAlamatFromPilihAlamatLainnya();
        checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickRadioButtonFromPilihAlamatLainnya();
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
        return view -> {
            mSvAddressSearchBox.getSearchTextView().setCursorVisible(true);
            openSoftKeyboard();
        };
    }

    public interface ICartAddressChoiceActivityListener {
        void finishAndSendResult(RecipientAddressModel selectedAddressResult);

        void requestCornerList();
    }

    @Override
    protected void initialListener(Activity activity) {

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

    private boolean isAddNewAddressEnabled() {
        return remoteConfig.getBoolean(ENABLE_ADD_NEW_ADDRESS_KEY, false);
    }

    private boolean isDisableSampaiView() {
        return remoteConfig.getBoolean(RemoteConfigKey.APP_HIDE_SAMPAI_VIEW, false);
    }

}