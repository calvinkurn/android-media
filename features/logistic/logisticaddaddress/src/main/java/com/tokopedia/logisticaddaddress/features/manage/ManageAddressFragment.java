package com.tokopedia.logisticaddaddress.features.manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.logisticaddaddress.adapter.AddressTypeFactory;
import com.tokopedia.logisticaddaddress.adapter.AddressViewHolder;
import com.tokopedia.logisticaddaddress.adapter.AddressViewModel;
import com.tokopedia.logisticaddaddress.di.AddressModule;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.di.DaggerManageAddressComponent;
import com.tokopedia.logisticaddaddress.di.ManageAddressModule;
import com.tokopedia.logisticaddaddress.domain.AddressViewModelMapper;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressActivity;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressFragment;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.logisticaddaddress.AddressConstants.REQUEST_CODE_PARAM_CREATE;
import static com.tokopedia.logisticaddaddress.AddressConstants.REQUEST_CODE_PARAM_EDIT;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
public class ManageAddressFragment extends BaseListFragment<AddressViewModel, AddressTypeFactory>
        implements AddressViewHolder.ManageAddressListener, ManageAddressContract.View {

    private static final int DEFAULT_PAGE_VALUE = 1;
    private static final int DEFAULT_SORT_ID = 1;
    private static final String DEFAULT_QUERY_VALUE = "";

    private boolean IS_EMPTY_ADDRESS = false;
    private MPAddressActivityListener mActivityListener;

    @Inject
    ManageAddressContract.Presenter mPresenter;

    public static ManageAddressFragment newInstance() {
        Bundle args = new Bundle();
        ManageAddressFragment fragment = new ManageAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ManageAddressFragment() {
    }

    @Override
    public void loadData(int page) {
        mPresenter.getAddress(page, DEFAULT_SORT_ID, DEFAULT_QUERY_VALUE);
    }

    @Override
    protected AddressTypeFactory getAdapterTypeFactory() {
        return new AddressTypeFactory(this);
    }

    @Override
    public void onItemClicked(AddressViewModel viewModel) {

    }

    @Override
    protected void initInjector() {
        BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();
        DaggerManageAddressComponent.builder()
                .baseAppComponent(appComponent)
                .addressModule(new AddressModule())
                .manageAddressModule(new ManageAddressModule())
                .build().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivityListener = (MPAddressActivityListener) getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.setView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected boolean hasInitialSwipeRefresh() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.manage_people_address, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_address) {
            this.openFormAddressView(null);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_PARAM_CREATE || requestCode == REQUEST_CODE_PARAM_EDIT) {
            if(resultCode == Activity.RESULT_OK) {
                loadInitialData();
            } else if(resultCode == AddAddressFragment.ERROR_RESULT_CODE) {
                showErrorSnackbar(getString(R.string.logistic_result_error_message));
            }
        }
    }

    @Override
    public void setActionEditButton(AddressViewModel viewModel) {
        openFormAddressView(
                AddressViewModelMapper.convertFromViewModel(viewModel)
        );
    }

    @Override
    public void setActionDeleteButton(AddressViewModel viewModel) {
        String message = String.format(
                getString(R.string.delete_prompt_message), viewModel.getAddressName());
        showDialogConfirmation(message,
                (dialogInterface, i) -> mPresenter.deleteAddress(viewModel.getAddressId()));
    }

    @Override
    public void setActionDefaultButtonClicked(AddressViewModel viewModel) {
        String message = String.format(getString(R.string.prioritize_message_prompt),
                viewModel.getAddressName(), viewModel.getAddressFull());
        showDialogConfirmation(message,
                (dialogInterface, i) -> mPresenter.prioritizeAddress(viewModel.getAddressId()));
    }

    @Override
    public MPAddressActivityListener getActivityListener() {
        return mActivityListener;
    }

    @Override
    public void refreshView() {
        loadInitialData();
    }

    @Override
    public void filter(int sortId, String query) {
        getAdapter().clearAllElements();
        toggleFilterFab(false);
        showLoading();
        mPresenter.getAddress(DEFAULT_PAGE_VALUE, sortId, query);
    }

    @Override
    public void openFormAddressView(AddressModel data) {
        Token token = mPresenter.getToken();
        if (data == null) {
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), token, IS_EMPTY_ADDRESS),
                    REQUEST_CODE_PARAM_CREATE);
        } else {
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), data, token),
                    REQUEST_CODE_PARAM_EDIT);
        }
    }

    @Override
    public void showData(List<AddressViewModel> data, boolean hasNext) {
        renderList(data, hasNext);
        toggleFilterFab(true);
    }

    @Override
    public void showLoadingView() {
        showLoading();
    }

    @Override
    public void showDialogConfirmation(String message, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(MethodChecker.fromHtml(message))
                .setPositiveButton(R.string.title_yes, onPositiveClickListener)
                .setNegativeButton(R.string.title_no, (dialog, i) -> dialog.cancel());

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void toggleFilterFab(boolean isVisible) {
        mActivityListener.setFilterViewVisibility(isVisible);
    }

    @Override
    public void showErrorSnackbar(String message) {
        if (message != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void showNetworkError() {
        getAdapter().showErrorNetwork();
    }

    @Override
    public void setIsEmptyAddress(boolean isEmpty) {
        IS_EMPTY_ADDRESS = isEmpty;
    }

}
