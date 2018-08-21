package com.tokopedia.shop.settings.etalase.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.view.activity.ShopSettingsEtalaseAddEditActivity;
import com.tokopedia.shop.settings.etalase.view.adapter.ShopEtalaseAdapter;
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseFactory;
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingEtalaseListPresenter;
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseViewHolder;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteAdapter;

import java.util.ArrayList;

import javax.inject.Inject;


public class ShopSettingsEtalaseListFragment extends BaseSearchListFragment<ShopEtalaseViewModel, ShopEtalaseFactory>
        implements ShopSettingEtalaseListPresenter.View, ShopEtalaseViewHolder.OnShopEtalaseViewHolderListener {

    private static final int REQUEST_CODE_ADD_ETALASE = 605;
    private static final int REQUEST_CODE_EDIT_ETALASE = 606;
    @Inject
    ShopSettingEtalaseListPresenter shopSettingEtalaseListPresenter;
    private ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels;
    private ShopEtalaseAdapter shopEtalaseAdapter;
    private ProgressDialog progressDialog;
    private String shopEtalaseIdToDelete;
    private String shopEtalaseNameToDelete;
    private boolean needReload;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private OnShopSettingsEtalaseFragmentListener onShopSettingsEtalaseFragmentListener;

    public interface OnShopSettingsEtalaseFragmentListener {
        void goToReorderFragment(ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels);
    }

    public static ShopSettingsEtalaseListFragment newInstance() {
        return new ShopSettingsEtalaseListFragment();
    }

    @Override
    protected void initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        shopSettingEtalaseListPresenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        GraphqlClient.init(getContext());
        shopSettingEtalaseListPresenter.getShopEtalase();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideSearchInputView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (shopEtalaseViewModels == null || shopEtalaseViewModels.size() == 0) {
            inflater.inflate(R.menu.menu_shop_etalase_list_no_data, menu);
        } else {
            inflater.inflate(R.menu.menu_shop_etalase_list, menu);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemReorder = menu.findItem(R.id.menu_reorder);
        if (menuItemReorder != null) {
            menuItemReorder.setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            onAddEtalaseButtonClicked();
            return true;
        } else if (item.getItemId() == R.id.menu_reorder) {
            if (shopEtalaseViewModels == null || shopEtalaseViewModels.size() == 0) {
                return true;
            }
            onShopSettingsEtalaseFragmentListener.goToReorderFragment(shopEtalaseViewModels);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        swipeRefreshLayout = super.getSwipeRefreshLayout(view);
        return swipeRefreshLayout;
    }

    @Override
    public void onSwipeRefresh() {
        super.onSwipeRefresh();
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        recyclerView = super.getRecyclerView(view);
        return recyclerView;
    }

    private void onAddEtalaseButtonClicked() {
        goToAddEtalase();
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        String searchText = searchInputView.getSearchText();

        if (TextUtils.isEmpty(searchText)) {
            EmptyModel emptyModel = new EmptyModel();
            emptyModel.setIconRes(R.drawable.ic_empty_state);
            emptyModel.setTitle(getString(R.string.shop_has_no_etalase));
            emptyModel.setContent(getString(R.string.shop_etalase_info));
            emptyModel.setButtonTitleRes(R.string.shop_settings_add_etalase);
            emptyModel.setCallback(new BaseEmptyViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    onAddEtalaseButtonClicked();
                }
            });
            return emptyModel;
        } else {
            EmptyResultViewModel emptyModel = new EmptyResultViewModel();
            emptyModel.setIconRes(R.drawable.ic_empty_search);
            emptyModel.setTitle(getString(R.string.shop_has_no_etalase_search, searchText));
            emptyModel.setContent(getString(R.string.change_your_keyword));
            return emptyModel;
        }

    }

    @Override
    public void loadData(int page) {
        shopSettingEtalaseListPresenter.getShopEtalase();
    }

    @NonNull
    @Override
    protected BaseListAdapter<ShopEtalaseViewModel, ShopEtalaseFactory> createAdapterInstance() {
        shopEtalaseAdapter = new ShopEtalaseAdapter(getAdapterTypeFactory(), this);
        return shopEtalaseAdapter;
    }

    @Override
    public void onItemClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        goToEditEtalase(shopEtalaseViewModel);
    }

    private void goToEditEtalase(ShopEtalaseViewModel shopEtalaseViewModel) {
        //TODO string etalase
        Intent intent = ShopSettingsEtalaseAddEditActivity.createIntent(getContext(), true,
                new ArrayList<>(), shopEtalaseViewModel);
        startActivityForResult(intent, REQUEST_CODE_EDIT_ETALASE);
    }

    private void goToAddEtalase() {
        //TODO string etalase
        Intent intent = ShopSettingsEtalaseAddEditActivity.createIntent(getContext(),
                false, new ArrayList<>(), new ShopEtalaseViewModel());
        startActivityForResult(intent, REQUEST_CODE_ADD_ETALASE);
    }

    @Override
    protected ShopEtalaseFactory getAdapterTypeFactory() {
        return new ShopEtalaseFactory(this);
    }

    @Override
    public void onIconMoreClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        Menus menus = new Menus(getContext());
        if (shopEtalaseViewModel.getCount() > 0) {
            menus.setItemMenuList(getResources().getStringArray(R.array.shop_etalase_menu_more_change));
        } else {
            menus.setItemMenuList(getResources().getStringArray(R.array.shop_etalase_menu_more_change_delete));
        }
        menus.setActionText(getString(R.string.close));
        menus.setOnActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menus.dismiss();
            }
        });
        menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
            @Override
            public void onClick(Menus.ItemMenus itemMenus, int pos) {
                if (itemMenus.title.equalsIgnoreCase(getString(R.string.label_change))) {
                    goToEditEtalase(shopEtalaseViewModel);
                } else {
                    shopEtalaseIdToDelete = shopEtalaseViewModel.getId();
                    shopEtalaseNameToDelete = shopEtalaseViewModel.getName();
                    showSubmitLoading(getString(R.string.title_loading));
                    shopSettingEtalaseListPresenter.deleteShopEtalase(shopEtalaseIdToDelete);
                }
                menus.dismiss();
            }
        });
        menus.show();
    }

    @Override
    public String getKeyword() {
        return searchInputView.getSearchText();
    }

    @Override
    public void onSuccessGetShopEtalase(ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels) {
        this.shopEtalaseViewModels = shopEtalaseViewModels;
        onSearchSubmitted(getKeyword());
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onErrorGetShopEtalase(Throwable throwable) {
        showGetListError(throwable);
    }

    @Override
    public void onSearchSubmitted(String text) {
        shopEtalaseAdapter.clearAllElements();
        isLoadingInitialData = true;
        ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels;
        if (this.shopEtalaseViewModels == null) {
            shopEtalaseViewModels = new ArrayList<>();
        } else if (this.shopEtalaseViewModels.size() > 0 &&
                !TextUtils.isEmpty(text)) {
            String textLowerCase = text.toLowerCase();
            shopEtalaseViewModels = new ArrayList<>();
            for (ShopEtalaseViewModel shopEtalaseViewModel : this.shopEtalaseViewModels) {
                if (shopEtalaseViewModel.getName().toLowerCase().contains(textLowerCase) ) {
                    shopEtalaseViewModels.add(shopEtalaseViewModel);
                }
            }
        } else {
            shopEtalaseViewModels = this.shopEtalaseViewModels;
        }
        renderList(shopEtalaseViewModels, false);
        showSearchViewWithDataSizeCheck();
    }

    @Override
    public void onSearchTextChanged(String text) {
        onSearchSubmitted(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDIT_ETALASE:
            case REQUEST_CODE_ADD_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    needReload = true;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needReload) {
            loadInitialData();
            needReload = false;
        }
    }

    @Override
    public void onSuccessDeleteShopEtalase(String successMessage) {
        hideSubmitLoading();
        String deleteMessage = TextUtils.isEmpty(shopEtalaseNameToDelete)?
                getString(R.string.etalase_success_delete):
                getString(R.string.etalase_x_success_delete, shopEtalaseNameToDelete);
        ToasterNormal.make(getActivity().findViewById(android.R.id.content),
                deleteMessage, BaseToaster.LENGTH_LONG)
                .setAction(getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // no-op
                    }
                }).show();
        // if somehow id is not there, or if size is 1, we want to reload, so empty state can be shown.
        if (TextUtils.isEmpty(shopEtalaseIdToDelete) ||
                shopEtalaseAdapter.getDataSize() == 1) {
            loadInitialData();
        } else {
            shopEtalaseAdapter.deleteEtalase(shopEtalaseIdToDelete);
        }
    }

    public void refreshData() {
        loadInitialData();
    }

    @Override
    public void onErrorDeleteShopEtalase(Throwable throwable) {
        hideSubmitLoading();
        String message = ErrorHandler.getErrorMessage(getContext(), throwable);
        ToasterError.make(getActivity().findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_LONG)
                .setAction(getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // no-op
                    }
                }).show();
    }

    public void showSubmitLoading(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void hideSubmitLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopSettingEtalaseListPresenter != null) {
            shopSettingEtalaseListPresenter.detachView();
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onShopSettingsEtalaseFragmentListener = (OnShopSettingsEtalaseFragmentListener) context;
    }
}
