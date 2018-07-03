package com.tokopedia.navigation.presentation.fragment;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation.presentation.base.ParentFragment;

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends ParentFragment {

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public int resLayout() {
        return R.layout.fragment_inbox;
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void setupToolbar(View view) {
        try {
            toolbar = view.findViewById(R.id.toolbar);
            TextView title = toolbar.findViewById(R.id.title_toolbar);
            title.setText("Inbox");
            ((MainParentActivity) getActivity()).setSupportActionBar(toolbar);
            getActivity().invalidateOptionsMenu();
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
