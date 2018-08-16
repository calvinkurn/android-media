package com.tokopedia.shop.settings.notes.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.design.component.Menus;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopNoteFactory;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListPresenter;
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteViewHolder;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Toped10 on 5/19/2016.
 */
public class ShopSettingsNotesFragment extends BaseSearchListFragment<ShopNoteViewModel, ShopNoteFactory> implements ShopSettingNoteListPresenter.View, ShopNoteViewHolder.OnShopNoteViewHolderListener {

    @Inject
    ShopSettingNoteListPresenter shopSettingNoteListPresenter;
    private ArrayList<ShopNoteViewModel> shopNoteModels;
    private BaseListAdapter<ShopNoteViewModel, ShopNoteFactory> shopNoteAdapter;

    public static ShopSettingsNotesFragment newInstance() {
        return new ShopSettingsNotesFragment();
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

//        AddShopNoteUseCase addShopNoteUseCase = new AddShopNoteUseCase(getContext());
//        addShopNoteUseCase.execute(AddShopNoteUseCase.createRequestParams("Kebijakan pengembalian",
//                "<b>Sebuah kebijakan</b>",
//                true)
//                , new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("Test", "test");
//                    }
//
//                    @Override
//                    public void onNext(String successMessage) {
//                        Log.i("Test", successMessage);
//                    }
//                });
//
//        UpdateShopNoteUseCase updateShopNoteUseCase = new UpdateShopNoteUseCase(getContext());
//        updateShopNoteUseCase.execute(UpdateShopNoteUseCase.createRequestParams(
//                "123",
//                "Kebijakan pengembalian",
//                "<b>Sebuah kebijakan</b>")
//                , new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("Test", "test");
//                    }
//
//                    @Override
//                    public void onNext(String successMessage) {
//                        Log.i("Test", successMessage);
//                    }
//                });
//
//        DeleteShopNoteUseCase deleteShopNoteUseCase = new DeleteShopNoteUseCase(getContext());
//        deleteShopNoteUseCase.execute(DeleteShopNoteUseCase.createRequestParams("123")
//                , new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("Test", "test");
//                    }
//
//                    @Override
//                    public void onNext(String successMessage) {
//                        Log.i("Test", successMessage);
//                    }
//                });
//
//        ReorderShopNoteUseCase reorderShopNoteUseCase = new ReorderShopNoteUseCase(getContext());
//        ArrayList<String> idList = new ArrayList<>();
//        idList.add("123");
//        idList.add("456");
//        idList.add("789");
//        idList.add("012");
//        reorderShopNoteUseCase.execute(ReorderShopNoteUseCase.createRequestParams(idList)
//                , new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("Test", "test");
//                    }
//
//                    @Override
//                    public void onNext(String successMessage) {
//                        Log.i("Test", successMessage);
//                    }
//                });
    }

    //    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        inflater?.inflate(R.menu.menu_add_shop_address, menu)
//    }
//
//    override fun onDestroyView() {
//        presenter.detachView()
//        super.onDestroyView()
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item?.itemId == R.id.menu_add){
//            createAddress()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_shop_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            Menus menus = new Menus(getContext());
            menus.setItemMenuList(getResources().getStringArray(R.array.shop_note_type));
            menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
                @Override
                public void onClick(Menus.ItemMenus itemMenus, int pos) {
                    if (pos == 0) {
                        //TODO add new shop note
                        Toast.makeText(getContext(), "add new shop note", Toast.LENGTH_LONG).show();
                    } else {
                        //TODO add new return terms
                        Toast.makeText(getContext(), "add new return terms", Toast.LENGTH_LONG).show();
                    }
                    menus.dismiss();
                }
            });
            menus.show();
            return true;
        } else if (item.getItemId() == R.id.menu_reorder) {
            Toast.makeText(getContext(), "go to reorder mode", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(getContext(), "go to add notes", Toast.LENGTH_LONG).show();
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
        shopNoteAdapter = super.createAdapterInstance();
        return shopNoteAdapter;
    }

    @Override
    public void onItemClicked(ShopNoteViewModel shopNoteViewModel) {
        //TODO go to edit notes
        Toast.makeText(getContext(), "go to edit notes " + shopNoteViewModel.getId(), Toast.LENGTH_LONG).show();
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
                    //TODO go to edit notes
                    Toast.makeText(getContext(), "go to edit notes " + shopNoteViewModel.getId(), Toast.LENGTH_LONG).show();
                } else {
                    //TODO go to delete notes
                    Toast.makeText(getContext(), "go to delete notes " + shopNoteViewModel.getId(), Toast.LENGTH_LONG).show();
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
        renderList(shopNoteModels, false);
    }

    @Override
    public void onErrorGetShopNotes(Throwable throwable) {
        showGetListError(throwable);
    }

    @Override
    public void onSearchSubmitted(String text) {
        if (shopNoteModels == null || shopNoteModels.size() == 0) {
            return;
        }
        String textLowerCase = text.toLowerCase();
        ArrayList<ShopNoteViewModel> shopNoteViewModels = new ArrayList<>();
        for (ShopNoteViewModel shopNoteModel : shopNoteModels) {
            if (shopNoteModel.getTitle().toLowerCase().contains(textLowerCase) ||
                    shopNoteModel.getContent().toLowerCase().contains(textLowerCase)) {
                shopNoteViewModels.add(shopNoteModel);
            }
        }
        shopNoteAdapter.clearAllElements();
        isLoadingInitialData = true;
        renderList(shopNoteViewModels, false);
    }

    @Override
    public void onSearchTextChanged(String text) {
        onSearchSubmitted(text);
    }

    @Override
    public void onSuccessReorderShopNote(String successMessage) {

    }

    @Override
    public void onErrorReorderShopNote(Throwable throwable) {

    }

    @Override
    public void onSuccessDeleteShopNote(String successMessage) {

    }

    @Override
    public void onErrorDeleteShopNote(Throwable throwable) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopSettingNoteListPresenter != null) {
            shopSettingNoteListPresenter.detachView();
        }
    }


}
