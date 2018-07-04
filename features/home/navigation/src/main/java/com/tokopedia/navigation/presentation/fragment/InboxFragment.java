package com.tokopedia.navigation.presentation.fragment;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation.presentation.base.ParentFragment;
import com.tokopedia.searchbar.NotificationToolbar;

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends ParentFragment {

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    NotificationToolbar toolbar;

    @Override
    public int resLayout() {
        return R.layout.fragment_inbox;
    }

    @Override
    public void initView(View view) {
        if (toolbar != null)
            toolbar.setTitle("Inbox");
    }

    @Override
    public void setupToolbar(View view) {
        try {
            this.toolbar = view.findViewById(R.id.toolbar);
            ((MainParentActivity) getActivity()).setSupportActionBar(toolbar);
        } catch (Exception ignored) {}
    }

    @Override
    public void loadData() {

    }

    @Override
    protected String getScreenName() {
        return "";
    }
}
