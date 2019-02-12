package com.tokopedia.shop.etalase.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.DaggerShopEtalaseComponent;
import com.tokopedia.shop.common.di.component.ShopEtalaseComponent;
import com.tokopedia.shop.etalase.view.adapter.ShopEtalaseAdapterTypeFactory;
import com.tokopedia.shop.etalase.view.listener.ShopEtalaseView;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.etalase.view.presenter.ShopEtalasePresenter;
import com.tokopedia.shopetalasepicker.R;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseFragment extends BaseListFragment<ShopEtalaseViewModel, ShopEtalaseAdapterTypeFactory>
        implements ShopEtalaseView, HasComponent<ShopEtalaseComponent> {
    public static final int DEFAULT_INDEX_SELECTION = 0;

    private String shopId;

    @Inject
    ShopEtalasePresenter shopEtalasePresenter;

    private String selectedEtalaseId;

    public static ShopEtalaseFragment createInstance(String shoId, String selectedEtalaseId) {
        ShopEtalaseFragment fragment = new ShopEtalaseFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ShopParamConstant.EXTRA_SHOP_ID, shoId);
        arguments.putString(ShopParamConstant.EXTRA_ETALASE_ID, selectedEtalaseId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID);
        selectedEtalaseId = getArguments().getString(ShopParamConstant.EXTRA_ETALASE_ID);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_list_swipe, container, false);
    }

    @Nullable
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void onSwipeRefresh() {
        super.onSwipeRefresh();
        shopEtalasePresenter.clearEtalaseCache();
    }

    @Override
    public void loadData(int i) {
        shopEtalasePresenter.getShopEtalase(shopId);
    }

    @Override
    protected ShopEtalaseAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopEtalaseAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(ShopEtalaseViewModel shopEtalaseViewModel) {
        Intent intent = new Intent();
        intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, shopEtalaseViewModel.getEtalaseId());
        intent.putExtra(ShopParamConstant.EXTRA_ETALASE_NAME, shopEtalaseViewModel.getEtalaseName());
        intent.putExtra(ShopParamConstant.EXTRA_USE_ACE, shopEtalaseViewModel.isUseAce());
        intent.putExtra(ShopParamConstant.EXTRA_ETALASE_BADGE, shopEtalaseViewModel.getEtalaseBadge());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void renderList(@NonNull List<ShopEtalaseViewModel> list, boolean isHasNext) {
        if (selectedEtalaseId != null) {
            for (int i = 0; i < list.size(); i++)
                if (list.get(i).getEtalaseId().equalsIgnoreCase(selectedEtalaseId))
                    list.get(i).setSelected(true);
        } else {
            list.get(DEFAULT_INDEX_SELECTION).setSelected(true);
        }
        super.renderList(list, isHasNext);
    }

    @Override
    protected void initInjector() {
        getComponent().inject(this);
        shopEtalasePresenter.attachView(this);
    }

    @Override
    public ShopEtalaseComponent getComponent() {
        return DaggerShopEtalaseComponent.builder()
                .baseAppComponent(((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shopEtalasePresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }


}