package com.tokopedia.navigation.presentation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.Inbox;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation.presentation.adapter.InboxAdapter;
import com.tokopedia.navigation.presentation.base.ParentFragment;
import com.tokopedia.searchbar.NotificationToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends ParentFragment {

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    private NotificationToolbar toolbar;

    private InboxAdapter adapter;

    @Override
    public int resLayout() {
        return R.layout.fragment_inbox;
    }

    @Override
    public void initView(View view) {
        if (toolbar != null)
            toolbar.setTitle("Inbox");

        adapter = new InboxAdapter();

        SwipeRefreshLayout swipe = view.findViewById(R.id.swipe);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipe.setOnRefreshListener(() -> {
            adapter.clear();
            adapter.addAll(data());
        });

        adapter.addAll(data());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new InboxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private List<Inbox> data() {
        List<Inbox> data = new ArrayList<>();
        data.add(new Inbox(R.drawable.ic_topchat, R.string.chat, R.string.chat_desc));
        data.add(new Inbox(R.drawable.ic_tanyajawab, R.string.diskusi, R.string.diskusi_desc));
        data.add(new Inbox(R.drawable.ic_ulasan, R.string.ulasan, R.string.ulasan_desc));
        data.add(new Inbox(R.drawable.ic_pesan_bantuan, R.string.pesan_bantuan, R.string.pesan_bantuan_desc));
        return data;
    }

    @Override
    public void setupToolbar(View view) {
        try {
            this.toolbar = view.findViewById(R.id.toolbar);
            ((MainParentActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        } catch (Exception ignored) {}
    }

    @Override
    public void loadData() { }

    @Override
    protected String getScreenName() {
        return "";
    }
}
