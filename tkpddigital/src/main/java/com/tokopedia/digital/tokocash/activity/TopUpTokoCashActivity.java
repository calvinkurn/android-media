package com.tokopedia.digital.tokocash.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.base.BaseDigitalPresenterActivity;
import com.tokopedia.digital.product.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.data.mapper.IProductDigitalMapper;
import com.tokopedia.digital.product.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.domain.DigitalCategoryRepository;
import com.tokopedia.digital.product.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.product.domain.ILastOrderNumberRepository;
import com.tokopedia.digital.product.domain.LastOrderNumberRepository;
import com.tokopedia.digital.product.domain.UssdCheckBalanceRepository;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.interactor.ProductDigitalInteractor;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.tokocash.compoundview.BalanceTokoCashView;
import com.tokopedia.digital.tokocash.compoundview.ReceivedTokoCashView;
import com.tokopedia.digital.tokocash.compoundview.TopUpTokoCashView;
import com.tokopedia.digital.tokocash.domain.ITokoCashRepository;
import com.tokopedia.digital.tokocash.domain.TokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.ITokoCashBalanceInteractor;
import com.tokopedia.digital.tokocash.interactor.TokoCashBalanceInteractor;
import com.tokopedia.digital.tokocash.listener.TopUpTokoCashListener;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;
import com.tokopedia.digital.tokocash.presenter.TopUpTokocashPresenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class TopUpTokoCashActivity extends BaseDigitalPresenterActivity<TopUpTokocashPresenter>
        implements TopUpTokoCashListener {

    @BindView(R2.id.balance_tokocash_layout)
    LinearLayout balanceTokoCashViewLayout;
    @BindView(R2.id.topup_tokocash_layout)
    LinearLayout topupTokoCashViewLayout;
    @BindView(R2.id.received_tokocash_layout)
    LinearLayout receivedTokoCashViewLayout;
    @BindView(R2.id.pb_main_loading)
    ProgressBar progressLoading;
    @BindView(R2.id.main_content)
    RelativeLayout mainContent;

    private CompositeSubscription compositeSubscription;
    private TopUpTokoCashView topUpTokoCashView;
    private BalanceTokoCashView balanceTokoCashView;
    private ReceivedTokoCashView receivedTokoCashView;
    private BottomSheetView bottomSheetTokoCashView;

    public static Intent newInstance(Context context) {
        return new Intent(context, TopUpTokoCashActivity.class);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        SessionHandler sessionHandler = new SessionHandler(this);
        LocalCacheHandler cacheHandler = new LocalCacheHandler(
                this, TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();
        IProductDigitalMapper productDigitalMapper = new ProductDigitalMapper();
        IDigitalCategoryRepository digitalCategoryRepository =
                new DigitalCategoryRepository(digitalEndpointService, productDigitalMapper);
        ILastOrderNumberRepository lastOrderNumberRepository =
                new LastOrderNumberRepository(digitalEndpointService, productDigitalMapper);
        IProductDigitalInteractor productDigitalInteractor =
                new ProductDigitalInteractor(
                        compositeSubscription, digitalCategoryRepository,
                        lastOrderNumberRepository, cacheHandler,
                        new UssdCheckBalanceRepository(digitalEndpointService, productDigitalMapper));
        ITokoCashRepository balanceRepository = new TokoCashRepository(new TokoCashService(
                sessionHandler.getAccessToken(this)));
        ITokoCashBalanceInteractor balanceInteractor = new TokoCashBalanceInteractor(balanceRepository,
                new CompositeSubscription());
        presenter = new TopUpTokocashPresenter(productDigitalInteractor, balanceInteractor, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tokocash;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(getString(R.string.title_tokocash_topup));
        topUpTokoCashView = new TopUpTokoCashView(this);
        balanceTokoCashView = new BalanceTokoCashView(this);
        receivedTokoCashView = new ReceivedTokoCashView(this);
        bottomSheetTokoCashView = new BottomSheetView(this);
        presenter.processGetBalanceTokoCash();
        presenter.processGetCategoryTopUp();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onDestroy() {
        clearHolder(topupTokoCashViewLayout);
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void showProgressLoading() {
        progressLoading.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressLoading() {
        if (progressLoading.getVisibility() == View.VISIBLE) {
            progressLoading.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showToastMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) NetworkErrorHelper.showSnackbar(this, message);
        else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderBalanceTokoCash(TokoCashData tokoCashData) {
        clearHolder(balanceTokoCashViewLayout);
        clearHolder(receivedTokoCashViewLayout);
        balanceTokoCashView.renderDataBalance(tokoCashData);
        balanceTokoCashView.setListener(getBalanceListener());
        receivedTokoCashView.renderReceivedView(tokoCashData);
        receivedTokoCashViewLayout.addView(receivedTokoCashView);
        balanceTokoCashViewLayout.addView(balanceTokoCashView);
        bottomSheetTokoCashView.renderBottomSheet(new BottomSheetView
                        .BottomSheetField.BottomSheetFieldBuilder()
                        .setTitle(getString(R.string.title_tooltip_tokocash))
                .setBody(getString(R.string.body_tooltip_tokocash))
                .setImg(R.drawable.ic_tokocash_activated)
                .build());
    }

    private BalanceTokoCashView.ActionListener getBalanceListener() {
        return new BalanceTokoCashView.ActionListener() {
            @Override
            public void showTooltipHoldBalance() {
                bottomSheetTokoCashView.show();
            }
        };
    }

    @Override
    public void showEmptyPage() {
        NetworkErrorHelper.showEmptyState(getApplicationContext(), mainContent, getRetryListener());
    }

    private NetworkErrorHelper.RetryClickedListener getRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processGetBalanceTokoCash();
                presenter.processGetCategoryTopUp();
            }
        };
    }

    @Override
    public void renderTopUpDataTokoCash(CategoryData categoryData) {
        clearHolder(topupTokoCashViewLayout);
        Operator operatorSelected = null;
        for (Operator operator : categoryData.getOperatorList()) {
            if (operator.getOperatorId().equalsIgnoreCase(categoryData.getDefaultOperatorId())) {
                operatorSelected = operator;
            }
        }
        topUpTokoCashView.renderDataTopUp(categoryData, operatorSelected);
        topUpTokoCashView.setListener(getActionListenerTopUpView());
        topupTokoCashViewLayout.addView(topUpTokoCashView);
    }

    private TopUpTokoCashView.ActionListener getActionListenerTopUpView() {
        return new TopUpTokoCashView.ActionListener() {
            @Override
            public void onDigitalChooserClicked(List<Product> productList, String productText) {
                startActivityForResult(
                        DigitalChooserActivity.newInstanceProductChooser(
                                TopUpTokoCashActivity.this, productList, productText
                        ),
                        IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
                );
            }

            @Override
            public void onProcessAddToCart(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct) {
                presenter.processAddToCartProduct(preCheckoutProduct);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    topUpTokoCashView.renderUpdateDataSelected(
                            (Product) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA));
                break;
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        showToastMessage(message);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_digital_tokocash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_history_tokocash) {
            startActivity(HistoryTokocashActivity.newInstance(this));
            return true;
        } else if (item.getItemId() == R.id.action_menu_help_tokocash) {
            // TODO : add intent to help tokocash page
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(this, originParams);
    }

    @Override
    public Application getMainApplication() {
        return getApplication();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public String getVersionInfoApplication() {
        return VersionInfo.getVersionInfo(this);
    }

    @Override
    public String getUserLoginId() {
        return SessionHandler.getLoginID(this);
    }

    private void clearHolder(LinearLayout holderView) {
        if (holderView.getChildCount() > 0) {
            holderView.removeAllViews();
        }
    }
}