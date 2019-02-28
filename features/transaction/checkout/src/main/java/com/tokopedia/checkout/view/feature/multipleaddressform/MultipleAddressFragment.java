package com.tokopedia.checkout.view.feature.multipleaddressform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.DaggerMultipleAddressComponent;
import com.tokopedia.checkout.view.di.component.MultipleAddressComponent;
import com.tokopedia.checkout.view.di.module.MultipleAddressModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity.TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT;
import static com.tokopedia.checkout.view.feature.addressoptions.CartAddressChoiceActivity.TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS;
import static com.tokopedia.checkout.view.feature.multipleaddressform.AddShipmentAddressActivity.EDIT_MODE;
import static com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressFormActivity.RESULT_CODE_SUCCESS_SET_SHIPPING;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressFragment extends BaseCheckoutFragment
        implements IMultipleAddressView, MultipleAddressAdapter.MultipleAddressAdapterListener,
        AnalyticsActionMultipleAddressPageListener {

    @Inject
    IMultipleAddressPresenter presenter;
    @Inject
    UserSession userSession;
    @Inject
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    @Inject
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress;

    public static final int EDIT_SHIPMENT_ADDRESS_REQUEST_CODE = 22;
    private static final String ADDRESS_EXTRA = "ADDRESS_EXTRA";
    private static final String CART_IDS_EXTRA = "CART_IDS_EXTRA";

    private MultipleAddressAdapter multipleAddressAdapter;
    private RecyclerView rvOrderAddressList;
    private LinearLayout llNetworkErrorView;
    private TkpdProgressDialog progressDialogNormal;
    private SwipeToRefresh swipeToRefresh;

    public static MultipleAddressFragment newInstance(RecipientAddressModel recipientModel, String cartIds) {
        MultipleAddressFragment fragment = new MultipleAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ADDRESS_EXTRA, recipientModel);
        bundle.putString(CART_IDS_EXTRA, cartIds);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return ConstantTransactionAnalytics.ScreenName.MULTI_ADDRESS_PAGE;
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
    protected void initInjector() {
        MultipleAddressComponent component = DaggerMultipleAddressComponent
                .builder()
                .cartComponent(getComponent(CartComponent.class))
                .multipleAddressModule(new MultipleAddressModule(this))
                .trackingAnalyticsModule(new TrackingAnalyticsModule()).build();
        component.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    private List<MultipleAddressAdapterData> initiateAdapterData(CartListData cartListData) {
        RecipientAddressModel recipientAddressModel = getArguments().getParcelable(ADDRESS_EXTRA);
        return presenter.initiateMultipleAddressAdapterData(cartListData, recipientAddressModel);
    }

    @Override
    public void onGoToChooseCourier(List<MultipleAddressAdapterData> dataList) {
        sendAnalyticsOnClickChooseCourierShipmentMultipleAddress();
        presenter.sendData(getActivity(), dataList);
    }

    @Override
    public void onAddNewShipmentAddress(ArrayList<MultipleAddressAdapterData> dataList, int parentPosition) {
        sendAnalyticsOnClickAddNewAddressShipmentMultipleAddress();
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(), dataList, parentPosition);
        startActivityForResult(intent, TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT);
    }

    @Override
    public void onDeleteItem(int position, List<MultipleAddressItemData> multipleAddressItemDataList) {
        String defaultCartId = null;
        for (MultipleAddressItemData multipleAddressItemData : multipleAddressItemDataList) {
            if (!TextUtils.isEmpty(multipleAddressItemData.getCartId()) &&
                    !multipleAddressItemData.getCartId().equals("0")) {
                defaultCartId = multipleAddressItemData.getCartId();
            } else {
                if (!TextUtils.isEmpty(defaultCartId) ||
                        multipleAddressItemData.getCartId().equals("0")) {
                    multipleAddressItemData.setCartId(defaultCartId);
                    break;
                }
            }
        }
        multipleAddressItemDataList.remove(position);
    }

    @Override
    public void onChangeAddress(MultipleAddressItemAdapter adapter,
                                ArrayList<MultipleAddressAdapterData> dataList,
                                RecipientAddressModel recipientAddressModel,
                                int childPosition, int parentPosition) {
        sendAnalyticsOnClickChangeAddressShipmentItemMultipleAddress();
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(), recipientAddressModel,
                dataList, childPosition, parentPosition);
        startActivityForResult(intent, TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS);
    }

    @Override
    public void sendAnalyticsPlusButtonItemMultipleAddress() {
        sendAnalyticsOnClickButtonPlusQuantityItemMultipleAddress();
    }

    @Override
    public void sendAnalyticsMinButtonItemMultipleAddress() {
        sendAnalyticsOnClickButtonMinusQuantityItemMultipleAddress();
    }

    @Override
    public void onItemChoosen(int itemPosition,
                              ArrayList<MultipleAddressAdapterData> dataList,
                              MultipleAddressAdapterData productData,
                              MultipleAddressItemData addressData) {
        startActivityForResult(AddShipmentAddressActivity
                        .createIntent(getActivity(), itemPosition, dataList, productData, addressData, EDIT_MODE),
                EDIT_SHIPMENT_ADDRESS_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS) {
                ArrayList<MultipleAddressAdapterData> dataList = data.getParcelableArrayListExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_DATA_LIST);
                RecipientAddressModel newAddress = data.getParcelableExtra(CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);
                int childPosition = data.getIntExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX, -1);
                int parentPosition = data.getIntExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1);
                setNewShipmentRecipientAddress(dataList, newAddress, childPosition, parentPosition);

                // Re-setup recycler view adapter to prevent crash if don't keep activities is on
                setRecyclerViewAdapter(dataList, parentPosition, false);
            } else if (requestCode == TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT) {
                ArrayList<MultipleAddressAdapterData> dataList = data.getParcelableArrayListExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_DATA_LIST);
                RecipientAddressModel newAddress = data.getParcelableExtra(CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);
                int parentPosition = data.getIntExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1);
                if (newAddress != null && dataList != null && parentPosition != -1) {
                    MultipleAddressItemData newShipmentData = null;
                    for (int i = 0; i < dataList.size(); i++) {
                        if (i == parentPosition && dataList.get(i).getItemListData().size() > 0) {
                            MultipleAddressItemData previousShipment = dataList.get(i).getItemListData().get(0);
                            newShipmentData = new MultipleAddressItemData();
                            newShipmentData.setRecipientAddressModel(newAddress);
                            newShipmentData.setCartId("0");
                            newShipmentData.setProductQty(String.valueOf(previousShipment.getMinQuantity()));
                            newShipmentData.setProductWeightFmt(previousShipment.getProductWeightFmt());
                            newShipmentData.setCartPosition(previousShipment.getCartPosition());
                            newShipmentData.setAddressPosition(previousShipment.getAddressPosition() + 1);
                            newShipmentData.setParentId(previousShipment.getParentId());
                            newShipmentData.setProductId(previousShipment.getProductId());
                            newShipmentData.setProductNotes("");
                            newShipmentData.setErrorCheckoutPriceLimit(previousShipment.getErrorCheckoutPriceLimit());
                            newShipmentData.setErrorFieldBetween(previousShipment.getErrorFieldBetween());
                            newShipmentData.setErrorFieldMaxChar(previousShipment.getErrorFieldMaxChar());
                            newShipmentData.setErrorFieldRequired(previousShipment.getErrorFieldRequired());
                            newShipmentData.setErrorProductAvailableStock(previousShipment.getErrorProductAvailableStock());
                            newShipmentData.setErrorProductAvailableStockDetail(previousShipment.getErrorProductAvailableStockDetail());
                            newShipmentData.setErrorProductMaxQuantity(previousShipment.getErrorProductMaxQuantity());
                            newShipmentData.setErrorProductMinQuantity(previousShipment.getErrorProductMinQuantity());
                            newShipmentData.setMaxQuantity(previousShipment.getMaxQuantity());
                            newShipmentData.setMinQuantity(previousShipment.getMinQuantity());
                            newShipmentData.setMaxRemark(previousShipment.getMaxRemark());
                            break;
                        }
                    }
                    if (newShipmentData != null) {
                        dataList.get(parentPosition).getItemListData().add(newShipmentData);
                    }
                }

                // Re-setup recycler view adapter to prevent crash if don't keep activities is on
                setRecyclerViewAdapter(dataList, parentPosition, false);
            }
        }
    }

    private void setNewShipmentRecipientAddress(ArrayList<MultipleAddressAdapterData> dataList,
                                                RecipientAddressModel newAddress,
                                                int childPosition, int parentPosition) {
        if (newAddress != null && dataList != null && childPosition != -1 && parentPosition != -1) {
            if (dataList.size() > 0) {
                for (int i = 0; i < dataList.size(); i++) {
                    if (i == parentPosition && dataList.get(i).getItemListData().size() > 0) {
                        boolean findItem = false;
                        for (int j = 0; j < dataList.get(i).getItemListData().size(); j++) {
                            if (j == childPosition) {
                                dataList.get(i).getItemListData().get(j).setRecipientAddressModel(newAddress);
                                findItem = true;
                                break;
                            }
                        }
                        if (findItem) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void showInitialLoading() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void hideInitialLoading() {
        swipeToRefresh.setRefreshing(false);
        swipeToRefresh.setEnabled(false);
    }

    @Override
    public void successMakeShipmentData() {
        if (getActivity() != null) {
            getActivity().setResult(RESULT_CODE_SUCCESS_SET_SHIPPING);
            getActivity().finish();
        }
    }

    @Override
    public void renderCartData(CartListData cartListData) {
        setRecyclerViewAdapter(initiateAdapterData(cartListData), 0, true);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> param) {
        return param != null ? AuthUtil.generateParamsNetwork(
                getActivity(), param, userSession.getUserId(), userSession.getDeviceId()
        ) : AuthUtil.generateParamsNetwork(
                getActivity(), userSession.getUserId(), userSession.getDeviceId()
        );
    }

    @Override
    public void onDestroyView() {
        presenter.onUnsubscribe();
        if (multipleAddressAdapter != null) {
            multipleAddressAdapter.unsubscribeSubscription();
        }
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideLoading() {
        progressDialogNormal.dismiss();
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public void showError(String message) {
        if (getView() != null && getActivity() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getActivity().getString(R.string.default_request_error_unknown);
            }
            if (multipleAddressAdapter == null || multipleAddressAdapter.getItemCount() == 0) {
                showErrorLayout(message);
            } else {
                ToasterError.make(getView(), message, BaseToaster.LENGTH_SHORT)
                        .setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Do Nothing, was added just to dismiss toaster
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void showErrorLayout(String message) {
        rvOrderAddressList.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        llNetworkErrorView.setVisibility(View.GONE);
                        rvOrderAddressList.setVisibility(View.VISIBLE);
                        swipeToRefresh.setEnabled(true);
                        presenter.processGetCartList(getArguments().getString(CART_IDS_EXTRA));
                    }
                });
    }

    @Override
    public void navigateToCartList() {
        if (getActivity() != null) {
            getActivity().setResult(MultipleAddressFormActivity.RESULT_CODE_RELOAD_CART_PAGE);
            getActivity().finish();
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.multiple_address_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            presenter.processGetCartList(getArguments().getString(CART_IDS_EXTRA));
        } else {
            swipeToRefresh.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CartListData.class.getSimpleName(), presenter.getCartListData());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            presenter.setCartListData(savedInstanceState.getParcelable(CartListData.class.getSimpleName()));
            renderCartData(presenter.getCartListData());
        }
    }

    @Override
    protected void initView(View view) {
        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        rvOrderAddressList = view.findViewById(R.id.order_address_list);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        rvOrderAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setRecyclerViewAdapter(List<MultipleAddressAdapterData> addressData, int itemPosition, boolean isInitialSetup) {
        multipleAddressAdapter = new MultipleAddressAdapter(addressData, this);
        rvOrderAddressList.setAdapter(multipleAddressAdapter);
        if (isInitialSetup) {
            rvOrderAddressList.addItemDecoration(new CartItemDecoration());
        }
        if (itemPosition != 0) {
            rvOrderAddressList.scrollToPosition(itemPosition);
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    public void backPressed() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickBackArrowFromKirimKeBeberapaAlamat();
    }

    public void deleteChanges() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickKembaliDanHapusPerubahanFromKirimKeBeberapaAlamat();
    }

    public void stayInPage() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickTetapDiHalamanIniFromKirimKeBeberapaAlamat();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkoutAnalyticsChangeAddress.sendScreenName(getActivity(), getScreenName());
    }

    @Override
    public void sendAnalyticsOnClickChangeAddressShipmentItemMultipleAddress() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickEditFromKirimKeBeberapaAlamat();
    }

    @Override
    public void sendAnalyticsOnClickAddNewAddressShipmentMultipleAddress() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickTambahPengirimanBaruFromKirimKeBeberapaAlamat();
    }

    @Override
    public void sendAnalyticsOnClickChooseCourierShipmentMultipleAddress() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickPilihKurirPengirimanFromKirimKeBeberapaAlamat();
    }

    @Override
    public void sendAnalyticsOnClickButtonPlusQuantityItemMultipleAddress() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickPlusFromUbahFromKirimKeBeberapaAlamat();
    }

    @Override
    public void sendAnalyticsOnClickButtonMinusQuantityItemMultipleAddress() {
        checkoutAnalyticsMultipleAddress.eventClickAtcCartMultipleAddressClickMinFromUbahFromKirimKeBeberapaAlamat();
    }
}
