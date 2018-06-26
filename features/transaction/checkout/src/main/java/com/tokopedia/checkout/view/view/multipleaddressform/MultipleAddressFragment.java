package com.tokopedia.checkout.view.view.multipleaddressform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.view.adapter.MultipleAddressAdapter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.DaggerMultipleAddressComponent;
import com.tokopedia.checkout.view.di.component.MultipleAddressComponent;
import com.tokopedia.checkout.view.di.module.MultipleAddressModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsMultipleAddress;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.checkout.view.view.multipleaddressform.AddShipmentAddressActivity.ADDRESS_DATA_RESULT;
import static com.tokopedia.checkout.view.view.multipleaddressform.AddShipmentAddressActivity.ADD_MODE;
import static com.tokopedia.checkout.view.view.multipleaddressform.AddShipmentAddressActivity.EDIT_MODE;
import static com.tokopedia.checkout.view.view.multipleaddressform.MultipleAddressFormActivity.RESULT_CODE_SUCCESS_SET_SHIPPING;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressFragment extends BaseCheckoutFragment
        implements IMultipleAddressView, MultipleAddressAdapter.MultipleAddressAdapterListener {

    @Inject
    IMultipleAddressPresenter presenter;
    @Inject
    UserSession userSession;
    @Inject
    CheckoutAnalyticsChangeAddress checkoutAnalyticsChangeAddress;
    @Inject
    CheckoutAnalyticsMultipleAddress checkoutAnalyticsMultipleAddress;

    public static final int ADD_SHIPMENT_ADDRESS_REQUEST_CODE = 21;
    public static final int EDIT_SHIPMENT_ADDRESS_REQUEST_CODE = 22;
    private static final String ADD_SHIPMENT_FRAGMENT_TAG = "ADD_SHIPMENT_FRAGMENT_TAG";
    private static final String CART_LIST_DATA = "CART_LIST_DATA";
    private static final String ADDRESS_EXTRA = "ADDRESS_EXTRA";
    private static final String DISTRICT_RECOMMENDATION_TOKEN = "DISTRICT_RECOMMENDATION_TOKEN";

    private MultipleAddressAdapter multipleAddressAdapter;
    private RecyclerView orderAddressList;
    private TkpdProgressDialog progressDialogNormal;

    public static MultipleAddressFragment newInstance(
            CartListData cartListData,
            RecipientAddressModel recipientModel,
            Token token
    ) {
        MultipleAddressFragment fragment = new MultipleAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ADDRESS_EXTRA, recipientModel);
        bundle.putParcelable(CART_LIST_DATA, cartListData);
        bundle.putParcelable(DISTRICT_RECOMMENDATION_TOKEN, token);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
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

    private List<MultipleAddressAdapterData> initiateAdapterData() {
        CartListData cartListData = getArguments().getParcelable(CART_LIST_DATA);
        RecipientAddressModel recipientAddressModel = getArguments().getParcelable(ADDRESS_EXTRA);
        Token token = getArguments().getParcelable(DISTRICT_RECOMMENDATION_TOKEN);
        return presenter.initiateMultipleAddressAdapterData(cartListData, recipientAddressModel, token);
    }

    @Override
    public void onGoToChooseCourier(List<MultipleAddressAdapterData> dataList) {
        checkoutAnalyticsMultipleAddress.eventClickMultipleAddressClickPilihKurirPengirimanFromKirimKeBeberapaAlamat();
        presenter.sendData(getActivity(), dataList);

    }

    @Override
    public void onAddNewShipmentAddress(int itemPosition,
                                        int addressPositionToAdd,
                                        ArrayList<MultipleAddressAdapterData> dataList,
                                        MultipleAddressAdapterData data,
                                        MultipleAddressItemData addressData) {
        checkoutAnalyticsMultipleAddress.eventClickMultipleAddressClickTambahPengirimanBaruFromKirimKeBeberapaAlamat();
        startActivityForResult(AddShipmentAddressActivity
                        .createIntent(getActivity(), itemPosition, dataList, data, addressData, ADD_MODE),
                ADD_SHIPMENT_ADDRESS_REQUEST_CODE);
    }

    @Override
    public void onItemChoosen(int itemPosition,
                              ArrayList<MultipleAddressAdapterData> dataList,
                              MultipleAddressAdapterData productData,
                              MultipleAddressItemData addressData) {
        checkoutAnalyticsMultipleAddress.eventClickMultipleAddressClickEditFromKirimKeBeberapaAlamat();
        startActivityForResult(AddShipmentAddressActivity
                        .createIntent(getActivity(), itemPosition, dataList, productData, addressData, EDIT_MODE),
                EDIT_SHIPMENT_ADDRESS_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_SHIPMENT_ADDRESS_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            ArrayList<MultipleAddressAdapterData> adapterDataList =
                    data.getParcelableArrayListExtra(AddShipmentAddressActivity.PRODUCT_DATA_LIST_EXTRAS);
            int itemPosition = data.getIntExtra(AddShipmentAddressActivity.ITEM_ADAPTER_POSITION_EXTRA, 0);
            MultipleAddressItemData editedAddressData = data.getParcelableExtra(ADDRESS_DATA_RESULT);
            if (editedAddressData != null) {
                adapterDataList.get(editedAddressData.getCartPosition())
                        .getItemListData()
                        .set(editedAddressData.getAddressPosition(), editedAddressData);
            }

            // Re-setup recycler view adapter to prevent crash if don't keep activities is on
            setRecyclerViewAdapter(adapterDataList, itemPosition);

        } else if (requestCode == ADD_SHIPMENT_ADDRESS_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            ArrayList<MultipleAddressAdapterData> adapterDataList =
                    data.getParcelableArrayListExtra(AddShipmentAddressActivity.PRODUCT_DATA_LIST_EXTRAS);
            MultipleAddressItemData editedAddressData = data.getParcelableExtra(ADDRESS_DATA_RESULT);
            int itemPosition = data.getIntExtra(AddShipmentAddressActivity.ITEM_ADAPTER_POSITION_EXTRA, 0);
            if (editedAddressData != null) {
                adapterDataList.get(editedAddressData.getCartPosition())
                        .getItemListData()
                        .add(editedAddressData.getAddressPosition(), editedAddressData);
            }

            // Re-setup recycler view adapter to prevent crash if don't keep activities is on
            setRecyclerViewAdapter(adapterDataList, itemPosition);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void successMakeShipmentData() {
        getActivity().setResult(RESULT_CODE_SUCCESS_SET_SHIPPING);
        getActivity().finish();
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
    public void showError(String message) {
        if (getView() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getActivity().getString(R.string.default_request_error_unknown);
            }
            ToasterError.make(getView(), message, 5000)
                    .setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();
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
    protected void initView(View view) {
        progressDialogNormal = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        orderAddressList = view.findViewById(R.id.order_address_list);
        orderAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        setRecyclerViewAdapter(initiateAdapterData(), 0);
    }

    private void setRecyclerViewAdapter(List<MultipleAddressAdapterData> addressData, int itemPosition) {
        multipleAddressAdapter = new MultipleAddressAdapter(addressData, this);
        orderAddressList.setAdapter(multipleAddressAdapter);
        if (itemPosition != 0) {
            orderAddressList.scrollToPosition(itemPosition);
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
        checkoutAnalyticsMultipleAddress.eventClickMultipleAddressClickBackArrowFromKirimKeBeberapaAlamat();
    }

    public void deleteChanges() {
        checkoutAnalyticsMultipleAddress.eventClickMultipleAddressClickKembaliDanHapusPerubahanFromKirimKeBeberapaAlamat();
    }

    public void stayInPage() {
        checkoutAnalyticsMultipleAddress.eventClickMultipleAddressClickTetapDiHalamanIniFromKirimKeBeberapaAlamat();
    }
}
