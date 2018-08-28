package com.tokopedia.shop.settings.notes.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.touchhelper.OnStartDragListener;
import com.tokopedia.design.touchhelper.SimpleItemTouchHelperCallback;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteReorderAdapter;
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteReorderFactory;
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListReorderPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class ShopSettingsNotesReorderFragment extends BaseListFragment<ShopNoteViewModel, ShopNoteReorderFactory>
        implements ShopSettingNoteListReorderPresenter.View, OnStartDragListener {

    public static final String TAG = ShopSettingsNotesReorderFragment.class.getSimpleName();
    public static final String EXTRA_NOTE_LIST = "note_list";

    @Inject
    ShopSettingNoteListReorderPresenter shopSettingNoteListReorderPresenter;
    private ArrayList<ShopNoteViewModel> shopNoteModels;
    private List<ShopNoteViewModel> shopNoteModelsWithoutTerms;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewTerms;
    private ShopNoteReorderAdapter adapter;
    private ShopNoteReorderAdapter adapterTerms;
    private ItemTouchHelper itemTouchHelper;

    private OnShopSettingsNotesReorderFragmentListener listener;
    public interface OnShopSettingsNotesReorderFragmentListener{
        void onSuccessReorderNotes();
    }

    public static ShopSettingsNotesReorderFragment newInstance(ArrayList<ShopNoteViewModel> shopNoteViewModels) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_NOTE_LIST, shopNoteViewModels);
        ShopSettingsNotesReorderFragment fragment = new ShopSettingsNotesReorderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        shopSettingNoteListReorderPresenter.attachView(this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<ShopNoteViewModel, ShopNoteReorderFactory> createAdapterInstance() {
        adapter = new ShopNoteReorderAdapter(getAdapterTypeFactory());
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        shopNoteModels = getArguments().getParcelableArrayList(EXTRA_NOTE_LIST);
        super.onCreate(savedInstanceState);
        GraphqlClient.init(getContext());
        adapterTerms = new ShopNoteReorderAdapter(new ShopNoteReorderFactory(null));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_reorder_list, container, false);
        recyclerViewTerms = view.findViewById(R.id.recyclerViewTerms);
        recyclerViewTerms.setAdapter(adapterTerms);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        recyclerView = super.getRecyclerView(view);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ItemTouchHelper.Callback itemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void loadData(int page) {
        ArrayList<ShopNoteViewModel> shopNoteModelsTerms = new ArrayList<>();
        if (shopNoteModels!=null && shopNoteModels.size() > 0) {
            if (shopNoteModels.get(0).getTerms()) {
                shopNoteModelsWithoutTerms = shopNoteModels.subList(1, shopNoteModels.size());
                shopNoteModelsTerms.add(shopNoteModels.get(0));
            } else {
                shopNoteModelsWithoutTerms = shopNoteModels;
            }
        }
        renderList(shopNoteModelsWithoutTerms, false);

        //render shop note with terms
        if (shopNoteModelsTerms.size() == 0) {
            recyclerViewTerms.setVisibility(View.GONE);
        } else {
            adapterTerms.clearAllElements();
            adapterTerms.addElement(shopNoteModelsTerms);
            recyclerViewTerms.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ShopNoteReorderFactory getAdapterTypeFactory() {
        return new ShopNoteReorderFactory(this);
    }

    public void saveReorder(){
        showSubmitLoading(getString(R.string.title_loading));
        ArrayList<String> shopNoteList = new ArrayList<>();
        List<ShopNoteViewModel> sortDataList = getAdapter().getData();
        for (ShopNoteViewModel shopNoteViewModel: sortDataList){
            shopNoteList.add(shopNoteViewModel.getId());
        }
        shopSettingNoteListReorderPresenter.reorderShopNotes(shopNoteList);
    }

    @Override
    public void onSuccessReorderShopNote(String successMessage) {
        hideSubmitLoading();
        ToasterNormal.make(getActivity().findViewById(android.R.id.content),
                getString(R.string.note_success_reorder), BaseToaster.LENGTH_LONG)
                .setAction(getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // no-op
                    }
                }).show();
        listener.onSuccessReorderNotes();
    }

    @Override
    public void onErrorReorderShopNote(Throwable throwable) {
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
        if (shopSettingNoteListReorderPresenter != null) {
            shopSettingNoteListReorderPresenter.detachView();
        }
    }

    @Override
    public void onItemClicked(ShopNoteViewModel shopNoteViewModel) {
        // no-op
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        listener = (OnShopSettingsNotesReorderFragmentListener) context;
    }
}
