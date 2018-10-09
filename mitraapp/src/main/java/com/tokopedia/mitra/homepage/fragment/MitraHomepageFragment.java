package com.tokopedia.mitra.homepage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.common.MitraBaseFragment;
import com.tokopedia.mitra.common.MitraComponentInstance;
import com.tokopedia.mitra.homepage.adapter.HomepageCategoriesAdapter;
import com.tokopedia.mitra.homepage.adapter.HomepageCategoriesAdapterTypeFactory;
import com.tokopedia.mitra.homepage.adapter.HomepageCategoriesTypeFactory;
import com.tokopedia.mitra.homepage.adapter.HomepageCategoryClickListener;
import com.tokopedia.mitra.homepage.contract.MitraHomepageContract;
import com.tokopedia.mitra.homepage.di.DaggerMitraHomepageComponent;
import com.tokopedia.mitra.homepage.di.MitraHomepageComponent;
import com.tokopedia.mitra.homepage.domain.model.CategoryRow;
import com.tokopedia.mitra.homepage.presenter.MitraHomepagePresenter;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MitraHomepageFragment extends MitraBaseFragment<MitraHomepageContract.Presenter, MitraHomepageContract.View> implements MitraHomepageContract.View {

    private static final int REQUEST_CODE_LOGIN = 1001;
    LinearLayout loginLayout;
    LinearLayout categoriesLoadingLayout;
    AppCompatButton loginBtn;
    RecyclerView categoriesRecyclerView;

    private HomepageCategoriesAdapter adapter;

    @Inject
    MitraHomepagePresenter presenter;

    public MitraHomepageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mitra_homepage, container, false);
        categoriesLoadingLayout = view.findViewById(R.id.category_loading_layout);
        loginLayout = view.findViewById(R.id.layout_login);
        loginBtn = view.findViewById(R.id.btn_login);
        categoriesRecyclerView = view.findViewById(R.id.rv_categories);
        return view;
    }

    @Override
    public MitraHomepageContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        setupView(view);
        presenter.onViewCreated();
    }

    private void setupView(View view) {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoginBtnClicked();
            }
        });
    }

    public static MitraHomepageFragment newInstance() {
        return new MitraHomepageFragment();
    }

    @Override
    protected void initInjector() {
        MitraHomepageComponent component = DaggerMitraHomepageComponent.builder()
                .mitraComponent(MitraComponentInstance.getComponent(getActivity().getApplication()))
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoginContainer() {
        loginLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginContainer() {
        loginLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMessageInRedSnackBar(int resId) {
        SnackbarManager.makeRed(getView(), getString(resId), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageInRedSnackBar(String message) {
        SnackbarManager.makeRed(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showCategoriesLoading() {
        categoriesLoadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCategories() {
        categoriesRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideCategoriesLoading() {
        categoriesLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showCategories() {
        categoriesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToLoginPage() {
        startActivityForResult(LoginPhoneNumberActivity.getCallingIntent(getActivity()), REQUEST_CODE_LOGIN);
    }

    @Override
    public void renderCategories(List<CategoryRow> categoryRows) {
        HomepageCategoriesTypeFactory adapterTypeFactory = new HomepageCategoriesAdapterTypeFactory(new HomepageCategoryClickListener() {
            @Override
            public void actionClick(CategoryRow categoryRow) {
                presenter.onApplinkReceive(categoryRow.getApplinks());
            }
        });
        adapter = new HomepageCategoriesAdapter(adapterTypeFactory);
        GridLayoutManager layoutManager
                = new GridLayoutManager(getActivity(), 3);
        categoriesRecyclerView.setLayoutManager(layoutManager);
        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setNestedScrollingEnabled(false);
        categoriesRecyclerView.setAdapter(adapter);
        adapter.addElement(categoryRows);
    }

    @Override
    public void navigateToNextPage(Intent applinkIntent) {
        startActivity(applinkIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                presenter.onLoginResultReceived();
                break;
        }
    }

}
