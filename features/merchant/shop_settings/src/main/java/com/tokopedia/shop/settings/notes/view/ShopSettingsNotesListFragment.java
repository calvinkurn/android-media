package com.tokopedia.shop.settings.notes.view;

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
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteFactory;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.activity.ShopSettingNotesAddEditActivity;
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteAdapter;
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListPresenter;
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteViewHolder;

import java.util.ArrayList;

import javax.inject.Inject;


public class ShopSettingsNotesListFragment extends BaseSearchListFragment<ShopNoteViewModel, ShopNoteFactory>
        implements ShopSettingNoteListPresenter.View, ShopNoteViewHolder.OnShopNoteViewHolderListener {

    private static final int REQUEST_CODE_ADD_NOTE = 818;
    private static final int REQUEST_CODE_EDIT_NOTE = 819;
    @Inject
    ShopSettingNoteListPresenter shopSettingNoteListPresenter;
    private ArrayList<ShopNoteViewModel> shopNoteModels;
    private ShopNoteAdapter shopNoteAdapter;
    private ProgressDialog progressDialog;
    private String shopNoteIdToDelete;
    private boolean needReload;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private OnShopSettingsNoteFragmentListener onShopSettingsNoteFragmentListener;
    public interface OnShopSettingsNoteFragmentListener {
        void goToReorderFragment(ArrayList<ShopNoteViewModel> shopNoteViewModels);
    }

    public static ShopSettingsNotesListFragment newInstance() {
        return new ShopSettingsNotesListFragment();
    }

    @Override
    protected void initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        shopSettingNoteListPresenter.attachView(this);
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
        shopSettingNoteListPresenter.getShopNotes();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideSearchInputView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (shopNoteModels == null || shopNoteModels.size() == 0) {
            inflater.inflate(R.menu.menu_shop_note_list_no_data, menu);
        } else {
            inflater.inflate(R.menu.menu_shop_note_list, menu);
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
            onAddNoteButtonClicked();
            return true;
        } else if (item.getItemId() == R.id.menu_reorder) {
            if (shopNoteModels == null || shopNoteModels.size() == 0) {
                return true;
            }
            onShopSettingsNoteFragmentListener.goToReorderFragment(shopNoteModels);
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

    private void onAddNoteButtonClicked() {
        Menus menus = new Menus(getContext());
        menus.setItemMenuList(getResources().getStringArray(R.array.shop_note_type));
        menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
            @Override
            public void onClick(Menus.ItemMenus itemMenus, int pos) {
                if (pos == 0) {
                    goToAddNote(false);
                } else {
                    goToAddNote(true);
                }
                menus.dismiss();
            }
        });
        menus.show();
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        String searchText = searchInputView.getSearchText();

        if (TextUtils.isEmpty(searchText)) {
            EmptyModel emptyModel = new EmptyModel();
            emptyModel.setIconRes(R.drawable.ic_empty_state);
            emptyModel.setTitle(getString(R.string.shop_has_no_notes));
            emptyModel.setContent(getString(R.string.shop_notes_info));
            emptyModel.setButtonTitleRes(R.string.add_note);
            emptyModel.setCallback(new BaseEmptyViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    onAddNoteButtonClicked();
                }
            });
            return emptyModel;
        } else {
            EmptyResultViewModel emptyModel = new EmptyResultViewModel();
            emptyModel.setIconRes(R.drawable.ic_empty_search);
            emptyModel.setTitle(getString(R.string.shop_has_no_notes_search));
            emptyModel.setContent(getString(R.string.shop_notes_use_other_keyword));
            return emptyModel;
        }

    }

    @Override
    public void loadData(int page) {
        shopSettingNoteListPresenter.getShopNotes();
    }

    @NonNull
    @Override
    protected BaseListAdapter<ShopNoteViewModel, ShopNoteFactory> createAdapterInstance() {
        shopNoteAdapter = new ShopNoteAdapter(getAdapterTypeFactory(), this);
        return shopNoteAdapter;
    }

    @Override
    public void onItemClicked(ShopNoteViewModel shopNoteViewModel) {
        goToEditNote(shopNoteViewModel);
    }

    private void goToEditNote(ShopNoteViewModel shopNoteViewModel) {
        Intent intent = ShopSettingNotesAddEditActivity.createIntent(getContext(),
                shopNoteViewModel.getTerms(), true, shopNoteViewModel);
        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
    }

    private void goToAddNote(boolean isTerms) {
        Intent intent = ShopSettingNotesAddEditActivity.createIntent(getContext(),
                isTerms, false, new ShopNoteViewModel());
        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
    }

    @Override
    protected ShopNoteFactory getAdapterTypeFactory() {
        return new ShopNoteFactory(this);
    }

    @Override
    public void onIconMoreClicked(ShopNoteViewModel shopNoteViewModel) {
        Menus menus = new Menus(getContext());
        menus.setItemMenuList(getResources().getStringArray(R.array.shop_note_menu_more));
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
                if (pos == 0) {
                    goToEditNote(shopNoteViewModel);
                } else {
                    shopNoteIdToDelete = shopNoteViewModel.getId();
                    showSubmitLoading(getString(R.string.title_loading));
                    shopSettingNoteListPresenter.deleteShopNote(shopNoteIdToDelete);
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
    public void onSuccessGetShopNotes(ArrayList<ShopNoteViewModel> shopNoteModels) {
        this.shopNoteModels = shopNoteModels;
        onSearchSubmitted(getKeyword());
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onErrorGetShopNotes(Throwable throwable) {
        showGetListError(throwable);
    }

    @Override
    public void onSearchSubmitted(String text) {
        shopNoteAdapter.clearAllElements();
        isLoadingInitialData = true;
        ArrayList<ShopNoteViewModel> shopNoteViewModels;
        if (shopNoteModels == null) {
            shopNoteViewModels = new ArrayList<>();
        } else if (shopNoteModels.size() > 0 &&
                !TextUtils.isEmpty(text)) {
            String textLowerCase = text.toLowerCase();
            shopNoteViewModels = new ArrayList<>();
            for (ShopNoteViewModel shopNoteModel : shopNoteModels) {
                if (shopNoteModel.getTitle().toLowerCase().contains(textLowerCase) ||
                        shopNoteModel.getContent().toLowerCase().contains(textLowerCase)) {
                    shopNoteViewModels.add(shopNoteModel);
                }
            }
        } else {
            shopNoteViewModels = shopNoteModels;
        }
        renderList(shopNoteViewModels, false);
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
            case REQUEST_CODE_EDIT_NOTE:
            case REQUEST_CODE_ADD_NOTE:
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
    public void onSuccessDeleteShopNote(String successMessage) {
        hideSubmitLoading();
        ToasterNormal.make(getActivity().findViewById(android.R.id.content),
                getString(R.string.note_success_delete), BaseToaster.LENGTH_LONG)
                .setAction(getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // no-op
                    }
                }).show();
        // if somehow id is not there, or if size is 1, we want to reload, so empty state can be shown.
        if (TextUtils.isEmpty(shopNoteIdToDelete) ||
                shopNoteAdapter.getDataSize() == 1) {
            loadInitialData();
        } else {
            shopNoteAdapter.deleteNote(shopNoteIdToDelete);
        }
    }

    public void refreshData(){
        loadInitialData();
    }

    @Override
    public void onErrorDeleteShopNote(Throwable throwable) {
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
        if (shopSettingNoteListPresenter != null) {
            shopSettingNoteListPresenter.detachView();
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        onShopSettingsNoteFragmentListener = (OnShopSettingsNoteFragmentListener) context;
    }
}
