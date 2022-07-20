package com.tokopedia.home_account.account_settings.presentation.fragment.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.home_account.R;
import com.tokopedia.home_account.account_settings.presentation.adapter.setting.GeneralSettingAdapter;
import com.tokopedia.home_account.account_settings.presentation.viewmodel.SettingItemUIModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

public abstract class BaseGeneralSettingFragment extends TkpdBaseV4Fragment
        implements GeneralSettingAdapter.OnSettingItemClicked {

    protected RecyclerView recyclerView;
    protected UserSessionInterface userSession;
    protected GeneralSettingAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userSession = new UserSession(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new GeneralSettingAdapter(getSettingItems(), this);
        recyclerView.setAdapter(adapter);
    }

    protected abstract List<SettingItemUIModel> getSettingItems();

}
