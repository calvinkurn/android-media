package com.tokopedia.logisticaddaddress.features.manage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
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
import com.tokopedia.logisticaddaddress.features.manage.ManageAddressContract;
import com.tokopedia.logisticaddaddress.features.manageaddress.MPAddressActivityListener;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.logisticaddaddress.AddressConstants.REQUEST_CODE_PARAM_CREATE;
import static com.tokopedia.logisticaddaddress.AddressConstants.REQUEST_CODE_PARAM_EDIT;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
public class ManageAddressFragment extends BaseListFragment<AddressViewModel, AddressTypeFactory>
        implements AddressViewHolder.ManageAddressListener, ManageAddressContract.View {

    private boolean IS_EMPTY = false;
    private MPAddressActivityListener mActivityListener;
    private int mSortId;
    private String mQuery;

    @Inject
    ManageAddressContract.Presenter mPresenter;

    public static ManageAddressFragment newInstance() {
        Bundle args = new Bundle();
        ManageAddressFragment fragment = new ManageAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loadData(int page) {
        mPresenter.getAddress(page, 1, "");
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
                .manageAddressModule(new ManageAddressModule(getContext()))
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
    public void setActionEditButton(AddressViewModel viewModel) {
        openFormAddressView(
                AddressViewModelMapper.convertFromViewModel(viewModel)
        );
    }

    @Override
    public void setActionDeleteButton(AddressViewModel viewModel) {
        showDialogConfirmation("Apakah Anda yakin ingin menghapus alamat: " +
                        "<b>" + viewModel.getAddressName() + "</b>?",
                (dialogInterface, i) -> mPresenter.deleteAddress(viewModel.getAddressId()));
    }

    @Override
    public void setActionDefaultButtonClicked(AddressViewModel viewModel) {
        showDialogConfirmation("Apakah Anda yakin ingin menggunakan alamat:" +
                        "<br/><br/><b>" + viewModel.getAddressName() + "</b><br/>" +
                        "<br/>" + viewModel.getAddressFull() + "<br/>" +
                        "<br/><br/> sebagai alamat utama Anda?",
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
    public void openFormAddressView(AddressModel data) {
        Token token = mPresenter.getToken();
        if (data == null) {
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), token, IS_EMPTY),
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
    public void showNetworkError() {
        getAdapter().showErrorNetwork();
    }

}
