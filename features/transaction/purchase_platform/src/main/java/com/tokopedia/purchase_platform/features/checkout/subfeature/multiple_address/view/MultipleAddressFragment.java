package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.MultipleAddressAdapterData;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.MultipleAddressItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.CartAddressChoiceActivity;
import com.tokopedia.purchase_platform.features.cart.view.CartItemDecoration;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsMultipleAddress;
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.di.DaggerNewMultipleAddressComponent;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.CartAddressChoiceActivity.TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT;
import static com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.CartAddressChoiceActivity.TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS;
import static com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.MultipleAddressFormActivity.RESULT_CODE_SUCCESS_SET_SHIPPING;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressFragment extends BaseCheckoutFragment
        implements IMultipleAddressView, MultipleAddressAdapter.MultipleAddressAdapterListener,
        AnalyticsActionMultipleAddressPageListener {

    @Inject
    IMultipleAddressPresenter presenter;
    @Inject
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    @Inject
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress;

    private static final String ADDRESS_EXTRA = "ADDRESS_EXTRA";
    private static final String CART_IDS_EXTRA = "CART_IDS_EXTRA";

    private MultipleAddressAdapter multipleAddressAdapter;
    private RecyclerView rvOrderAddressList;
    private LinearLayout llNetworkErrorView;
    private AlertDialog progressDialogNormal;
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
    protected void initInjector() {
        if (getActivity() != null) {
            BaseMainApplication baseMainApplication = (BaseMainApplication) getActivity().getApplication();
            DaggerNewMultipleAddressComponent.builder()
                    .baseAppComponent(baseMainApplication.getBaseAppComponent())
                    .build()
                    .inject(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.attachView(this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS) {
                ArrayList<MultipleAddressAdapterData> dataList = data.getParcelableArrayListExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_DATA_LIST);
                RecipientAddressModel newAddress = data.getParcelableExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA);
                int childPosition = data.getIntExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX, -1);
                int parentPosition = data.getIntExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1);
                setNewShipmentRecipientAddress(dataList, newAddress, childPosition, parentPosition);

                // Re-setup recycler view adapter to prevent crash if don't keep activities is on
                setRecyclerViewAdapter(dataList, parentPosition, false, false);
            } else if (requestCode == TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT) {
                ArrayList<MultipleAddressAdapterData> dataList = data.getParcelableArrayListExtra(CartAddressChoiceActivity.EXTRA_MULTIPLE_ADDRESS_DATA_LIST);
                RecipientAddressModel newAddress = data.getParcelableExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA);
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
                setRecyclerViewAdapter(dataList, parentPosition, false, false);
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
        setRecyclerViewAdapter(initiateAdapterData(cartListData), 0, true, cartListData.isShowOnboarding());
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> param) {
        UserSessionInterface userSession = new UserSession(getActivity());
        return param != null ? AuthUtil.generateParamsNetwork(
                getActivity(), param, userSession.getUserId(), userSession.getDeviceId()
        ) : AuthUtil.generateParamsNetwork(
                getActivity(), userSession.getUserId(), userSession.getDeviceId()
        );
    }

    @Override
    public void onDestroyView() {
        if (multipleAddressAdapter != null) {
            multipleAddressAdapter.unsubscribeSubscription();
        }
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        if (progressDialogNormal != null && !progressDialogNormal.isShowing())
            progressDialogNormal.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialogNormal != null && progressDialogNormal.isShowing())
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
            CartListData cartListData = savedInstanceState.getParcelable(CartListData.class.getSimpleName());
            if (cartListData != null) {
                presenter.setCartListData(cartListData);
                renderCartData(presenter.getCartListData());
            } else {
                presenter.processGetCartList(getArguments().getString(CART_IDS_EXTRA));
            }
        }
    }

    @Override
    protected void initView(View view) {
        progressDialogNormal = new AlertDialog.Builder(getActivity())
                .setView(R.layout.purchase_platform_progress_dialog_view)
                .setCancelable(false)
                .create();

        rvOrderAddressList = view.findViewById(R.id.order_address_list);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        rvOrderAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setRecyclerViewAdapter(List<MultipleAddressAdapterData> addressData, int itemPosition, boolean isInitialSetup, boolean isShowOnboarding) {
        multipleAddressAdapter = new MultipleAddressAdapter(addressData, this, isShowOnboarding);
        rvOrderAddressList.setAdapter(multipleAddressAdapter);
        if (isInitialSetup) {
            rvOrderAddressList.addItemDecoration(new CartItemDecoration());
        }
        if (itemPosition != 0) {
            rvOrderAddressList.scrollToPosition(itemPosition);
        }
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
