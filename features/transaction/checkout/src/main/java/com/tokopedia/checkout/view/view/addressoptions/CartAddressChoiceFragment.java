package com.tokopedia.checkout.view.view.addressoptions;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartAddressChoiceComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartAddressChoiceComponent;
import com.tokopedia.checkout.view.di.module.CartAddressChoiceModule;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.manage.people.address.model.Token;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_CURRENT_ADDRESS;
import static com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_DEFAULT_SELECTED_ADDRESS;
import static com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_NAVIGATION_FROM_ADDRESS_LIST;
import static com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA;
import static com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS;
import static com.tokopedia.core.manage.people.address.ManageAddressConstant.EXTRA_ADDRESS;

/**
 * @author Irfan Khoirul on 05/02/18
 * Aghny A. Putra on 27/02/18
 */

public class CartAddressChoiceFragment extends BaseCheckoutFragment
        implements ICartAddressChoiceView, ShipmentAddressListAdapter.ActionListener {

    private TextView tvChooseOtherAddress;
    private LinearLayout llSendToMultipleAddress;
    private Button btSendToCurrentAddress;
    private RecyclerView rvAddress;
    private LinearLayout llNetworkErrorView;
    private LinearLayout llContent;
    private SwipeToRefresh swipeToRefreshLayout;

    private ICartAddressChoiceActivityListener mCartAddressChoiceListener;

    private Token token;

    @Inject
    CartAddressChoicePresenter mCartAddressChoicePresenter;

    @Inject
    ShipmentAddressListAdapter mShipmentAddressListAdapter;

    public static CartAddressChoiceFragment newInstance(RecipientAddressModel currentAddress) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress);
        CartAddressChoiceFragment cartAddressChoiceFragment = new CartAddressChoiceFragment();
        cartAddressChoiceFragment.setArguments(bundle);
        return cartAddressChoiceFragment;
    }

    @Override
    protected void initInjector() {
        CartAddressChoiceComponent component = DaggerCartAddressChoiceComponent.builder()
                .cartAddressChoiceModule(new CartAddressChoiceModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_address_choice, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_address) {
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), token),
                    ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_address_choice;
    }

    @Override
    protected void initView(View view) {
        tvChooseOtherAddress = view.findViewById(R.id.tv_choose_other_address);
        llSendToMultipleAddress = view.findViewById(R.id.ll_send_to_multiple_address);
        btSendToCurrentAddress = view.findViewById(R.id.bt_send_to_current_address);
        rvAddress = view.findViewById(R.id.rv_address);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        llContent = view.findViewById(R.id.ll_content);
        swipeToRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        tvChooseOtherAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseOtherAddressClick();
            }
        });

        llSendToMultipleAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendToMultipleAddress();
            }
        });

        btSendToCurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendToCurrentAddress();
            }
        });

        setupRecyclerView();
        mCartAddressChoicePresenter.attachView(this);
        if (getArguments() != null) {
            RecipientAddressModel model = getArguments().getParcelable(EXTRA_DEFAULT_SELECTED_ADDRESS);
            if (getArguments().getBoolean(EXTRA_NAVIGATION_FROM_ADDRESS_LIST)) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, model);

                getActivity().setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, intent);
                getActivity().finish();
            } else {
                mCartAddressChoicePresenter.setSelectedRecipientAddress(model);
            }
        }
        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCartAddressChoicePresenter.getAddressShortedList(getActivity(),
                        (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCartAddressChoicePresenter.detachView();
    }

    @Override
    public void renderRecipientData(List<RecipientAddressModel> recipientAddressModels) {
        mShipmentAddressListAdapter.setAddressList(recipientAddressModels);
        mShipmentAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public void renderEmptyRecipientData() {
        llContent.setVisibility(View.GONE);
        btSendToCurrentAddress.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        swipeToRefreshLayout.setEnabled(true);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView,
                getString(R.string.checkout_module_title_error_empty_address),
                getString(R.string.checkout_module_subtitle_error_empty_address),
                getString(R.string.checkout_module_label_button_retry_error_empty_address),
                R.drawable.ic_empty_state,
                () -> startActivityForResult(AddAddressActivity.createInstance(getActivity(), token),
                        ManageAddressConstant.REQUEST_CODE_PARAM_CREATE));
    }

    @Override
    public void renderSaveButtonEnabled() {
        btSendToCurrentAddress.setBackgroundResource(R.drawable.medium_green_button_rounded);
        btSendToCurrentAddress.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        getActivity().setTitle("Tujuan Pengiriman");
    }

    @Override
    protected void setActionVar() {
        mCartAddressChoicePresenter.getAddressShortedList(getActivity(),
                (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
    }

    @Override
    public void showLoading() {
        llContent.setVisibility(View.GONE);
        btSendToCurrentAddress.setVisibility(View.GONE);
        swipeToRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        llContent.setVisibility(View.VISIBLE);
        btSendToCurrentAddress.setVisibility(View.VISIBLE);
        llNetworkErrorView.setVisibility(View.GONE);
        swipeToRefreshLayout.setRefreshing(false);
        swipeToRefreshLayout.setEnabled(false);
    }

    @Override
    public void showNoConnection(@NonNull String message) {
        llContent.setVisibility(View.GONE);
        btSendToCurrentAddress.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        swipeToRefreshLayout.setEnabled(true);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        mCartAddressChoicePresenter.getAddressShortedList(getActivity(),
                                (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
                    }
                });
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        rvAddress.setLayoutManager(linearLayoutManager);
        rvAddress.setAdapter(mShipmentAddressListAdapter);
    }

    private void onChooseOtherAddressClick() {
        ShipmentAddressListFragment fragment = ShipmentAddressListFragment.newInstance(
                (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().beginTransaction()
                .add(R.id.parent_view, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    private void onSendToMultipleAddress() {
        mCartAddressChoiceListener.finishSendResultActionToMultipleAddressForm();
    }

    private void onSendToCurrentAddress() {
        RecipientAddressModel recipientAddress
                = mCartAddressChoicePresenter.getSelectedRecipientAddress();

        if (recipientAddress != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, recipientAddress);

            getActivity().setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, intent);
            getActivity().finish();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onAddressContainerClicked(RecipientAddressModel model) {
        mCartAddressChoicePresenter.setSelectedRecipientAddress(model);
    }

    @Override
    public void onEditClick(RecipientAddressModel model) {
        AddressModelMapper mapper = new AddressModelMapper();

        Intent intent = AddAddressActivity.createInstance(getActivity(), mapper.transform(model), token);
        startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    RecipientAddressModel newRecipientAddressModel = null;
                    if (data != null && data.hasExtra(EXTRA_ADDRESS)) {
                        Destination newAddress = data.getParcelableExtra(EXTRA_ADDRESS);
                        newRecipientAddressModel = new RecipientAddressModel();
                        newRecipientAddressModel.setAddressName(newAddress.getAddressName());
                        newRecipientAddressModel.setDestinationDistrictId(newAddress.getDistrictId());
                        newRecipientAddressModel.setCityId(newAddress.getCityId());
                        newRecipientAddressModel.setProvinceId(newAddress.getProvinceId());
                        newRecipientAddressModel.setRecipientName(newAddress.getReceiverName());
                        newRecipientAddressModel.setRecipientPhoneNumber(newAddress.getReceiverPhone());
                        newRecipientAddressModel.setAddressStreet(newAddress.getAddressStreet());
                    } else {
                        newRecipientAddressModel = (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS);
                    }
                    mCartAddressChoicePresenter.getAddressShortedList(getActivity(), newRecipientAddressModel);
                    break;
                case ManageAddressConstant.REQUEST_CODE_PARAM_EDIT:
                    mCartAddressChoicePresenter.getAddressShortedList(getActivity(),
                            (RecipientAddressModel) getArguments().getParcelable(EXTRA_CURRENT_ADDRESS));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCartAddressChoiceListener = (ICartAddressChoiceActivityListener) activity;
    }

}
