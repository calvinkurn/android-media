package com.tokopedia.checkout.view.feature.emptycart;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveView;
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveWithBackView;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.emptycart.di.DaggerEmptyCartComponent;
import com.tokopedia.checkout.view.feature.emptycart.di.EmptyCartComponent;
import com.tokopedia.checkout.view.feature.emptycart.di.EmptyCartModule;
import com.tokopedia.navigation_common.listener.EmptyCartListener;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public class EmptyCartFragment extends BaseCheckoutFragment implements EmptyCartContract.View {

    public static final String ARG_AUTO_APPLY_MESSAGE = "ARG_AUTO_APPLY_MESSAGE";

    private View toolbar;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isToolbarWithBackButton = true;

    @Inject
    UserSession userSession;
    @Inject
    EmptyCartContract.Presenter presenter;

    public static EmptyCartFragment newInstance(String autoApplyMessage, String args) {
        EmptyCartFragment emptyCartFragment = new EmptyCartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EmptyCartFragment.class.getSimpleName(), args);
        bundle.putString(ARG_AUTO_APPLY_MESSAGE, autoApplyMessage);
        emptyCartFragment.setArguments(bundle);

        return emptyCartFragment;
    }

    @Override
    protected void initInjector() {
        EmptyCartComponent component = DaggerEmptyCartComponent.builder()
                .cartComponent(CartComponentInjector.newInstance(getActivity().getApplication()).getCartApiServiceComponent())
                .emptyCartModule(new EmptyCartModule())
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .build();
        component.inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        String args = arguments.getString(EmptyCartFragment.class.getSimpleName());
        if (args != null && !args.isEmpty()) {
            isToolbarWithBackButton = false;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_empty_cart;
    }

    @Override
    protected void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        setupToolbar(view);
        presenter.attachView(this);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return originParams == null
                ? AuthUtil.generateParamsNetwork(
                getActivity(), userSession.getUserId(),
                userSession.getDeviceId()
        )
                : AuthUtil.generateParamsNetwork(
                getActivity(), originParams,
                userSession.getUserId(),
                userSession.getDeviceId()
        );
    }

    @Override
    public void navigateToCartFragment(CartListData cartListData) {
        if (getActivity() instanceof EmptyCartListener) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EmptyCartListener.ARG_CART_LIST_DATA, cartListData);
            ((EmptyCartListener) getActivity()).onCartNotEmpty(bundle);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            presenter.processInitialGetCartData();
        }
    }

    private void setupToolbar(View view) {
        Toolbar appbar = view.findViewById(R.id.toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        if (isToolbarWithBackButton) {
            toolbar = toolbarRemoveWithBackView();
        } else {
            toolbar = toolbarRemoveView();
        }
        appbar.addView(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(appbar);
    }

    private ToolbarRemoveWithBackView toolbarRemoveWithBackView() {
        ToolbarRemoveWithBackView toolbar = new ToolbarRemoveWithBackView(getActivity());
        toolbar.navigateUp(getActivity());
        toolbar.setTitle(getString(R.string.cart));
        return toolbar;
    }

    private ToolbarRemoveView toolbarRemoveView() {
        ToolbarRemoveView toolbar = new ToolbarRemoveView(getActivity());
        toolbar.setTitle(getString(R.string.cart));
        return toolbar;
    }

}
