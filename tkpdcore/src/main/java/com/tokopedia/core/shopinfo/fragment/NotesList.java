package com.tokopedia.core.shopinfo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.R;
import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.ShopNotesDetail;
import com.tokopedia.core.shopinfo.adapter.NoteListAdapterR;
import com.tokopedia.core.shopinfo.facades.GetShopNote;
import com.tokopedia.core.shopinfo.models.NoteModel;
import com.tokopedia.core.shopinfo.models.shopnotes.GetShopNotes;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
public class NotesList extends V2BaseFragment {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    class ViewHolder {
        RecyclerView list;
    }

    ViewHolder holder;
    GetShopNote facadeGetNote;
    NoteListAdapterR adapter;
    ArrayList<NoteModel> noteList = new ArrayList<>();
    private boolean noResult = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = NoteListAdapterR.createAdapter(noteList);
    }

    private void getNoteList() {
        facadeGetNote = new GetShopNote(getActivity());
        facadeGetNote.setOnGetNoteListListener(onGetNoteListListener());
        facadeGetNote.setCompositeSubscription(compositeSubscription);
        facadeGetNote.getNoteListV4(getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_ID, ""), getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_DOMAIN, ""));
    }

    private void loadSavedInstance(Bundle savedInstanceState) {
        noteList = savedInstanceState.getParcelableArrayList("list");
        noResult = savedInstanceState.getBoolean("noresult");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null &&
                    getActivity() instanceof ShopInfoActivity) {
                ((ShopInfoActivity) getActivity()).swipeAble(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", noteList);
        outState.putBoolean("noresult", noResult);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void onCreateView() {
        if (noteList.isEmpty() && !adapter.hasNoResult() && !noResult)
            adapter.setLoading();
        else if(noResult)
            adapter.setNoResult();

        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!noResult && noteList.size()==0) {
            getNoteList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    protected Object getHolder() {
        return holder;
    }

    @Override
    protected void setHolder(Object holder) {
        this.holder = (ViewHolder) holder;
    }

    @Override
    protected void initView() {
        holder = new ViewHolder();
        holder.list = (RecyclerView) findViewById(R.id.list);
        holder.list.setAdapter(adapter);
        holder.list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void setListener() {
        adapter.setListener(new NoteListAdapterR.NoteListAdapterInterface() {
            @Override
            public void onView(int position) {
                Intent intent = ShopNotesDetail.createIntent(getActivity(), getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_ID, ""), getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_DOMAIN, ""), noteList.get(position).id);
                startActivity(intent);
            }

            @Override
            public void onEdit(int position) {

            }

            @Override
            public void onDelete(int position) {

            }
        });
    }

    private GetShopNote.OnGetNoteListListener onGetNoteListListener() {
        return new GetShopNote.OnGetNoteListListener() {
            @Override
            public void onCompleteDataSuccess(GetShopNotes.Data data) {

            }

            @Override
            public void onSuccess(List<NoteModel> notesList) {
                adapter.removeLoading();
                noteList.addAll(notesList);
                adapter.notifyDataSetChanged();
                if(notesList.size() == 0) {
                    adapter.setNoResult();
                    noResult = true;
                }
            }

            @Override
            public void onFailure() {
                adapter.removeLoading();
                adapter.setNoResult();
                noResult = true;
            }
        };
    }
}
