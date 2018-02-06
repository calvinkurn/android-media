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

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.tokocash.TokoCashService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.tokocash.compoundview.BalanceTokoCashView;
import com.tokopedia.digital.tokocash.compoundview.ReceivedTokoCashView;
import com.tokopedia.digital.tokocash.compoundview.TopUpTokoCashView;
import com.tokopedia.digital.tokocash.domain.ITokoCashRepository;
import com.tokopedia.digital.tokocash.domain.TokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.ITokoCashBalanceInteractor;
import com.tokopedia.digital.tokocash.interactor.TokoCashBalanceInteractor;
import com.tokopedia.digital.tokocash.listener.TopUpTokoCashListener;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;
import com.tokopedia.digital.tokocash.presenter.TopUpTokocashPresenter;

import java.util.List;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 8/18/17.
 */
public class TopUpTokoCashActivity extends BasePresenterActivity<TopUpTokocashPresenter>
        implements TopUpTokoCashListener {
    public static final String EXTRA_TOP_UP_AVAILABLE = "EXTRA_TOP_UP_AVAILABLE";
    public static final int REQUEST_CODE_ACCOUNT_SETTING = 112;
    private static final int QR_REQUEST_CODE = 12;

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

    private boolean topUpAvailable;

    private String categoryId;
    private String operatorId;

    @SuppressWarnings("unused")
    @DeepLink(Constants.Applinks.WALLET_HOME)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        boolean topUpVisible = Boolean.parseBoolean(
                extras.getString(
                        Constants.AppLinkQueryParameter.WALLET_TOP_UP_VISIBILITY, "false"
                )
        );
        return TopUpTokoCashActivity.newInstance(context, topUpVisible);
    }

    private static Intent newInstance(Context context, boolean topUpAvailable) {
        return new Intent(context, TopUpTokoCashActivity.class)
                .putExtra(EXTRA_TOP_UP_AVAILABLE, topUpAvailable);
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
        this.topUpAvailable = extras.getBoolean(EXTRA_TOP_UP_AVAILABLE, true);
    }

    @Override
    protected void initialPresenter() {
        SessionHandler sessionHandler = new SessionHandler(this);

        LocalCacheHandler cacheHandler = new LocalCacheHandler(
                this, TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);

        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        ITokoCashRepository balanceRepository = new TokoCashRepository(new TokoCashService(
                SessionHandler.getAccessToken(this)));

        ITokoCashBalanceInteractor balanceInteractor = new TokoCashBalanceInteractor(balanceRepository,
                new CompositeSubscription(),
                new JobExecutor(),
                new UIThread());

        DigitalEndpointService digitalEndpointService = new DigitalEndpointService();

        CategoryDetailDataSource categoryDetailDataSource = new CategoryDetailDataSource(
                digitalEndpointService, new GlobalCacheManager(), new ProductDigitalMapper()
        );

        IDigitalCategoryRepository digitalCategoryRepository = new DigitalCategoryRepository(
                categoryDetailDataSource, null
        );

        GetCategoryByIdUseCase getCategoryByIdUseCase = new GetCategoryByIdUseCase(this,
                digitalCategoryRepository);

        presenter = new TopUpTokocashPresenter(getApplicationContext(), getCategoryByIdUseCase,
                balanceInteractor, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tokocash;
    }

    @Override
    protected void initView() {
        topUpTokoCashView = new TopUpTokoCashView(this);
        balanceTokoCashView = new BalanceTokoCashView(this);
        receivedTokoCashView = new ReceivedTokoCashView(this);
        bottomSheetTokoCashView = new BottomSheetView(this);
        presenter.getTokenWallet();
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
    public void renderBalanceTokoCash(TokoCashBalanceData tokoCashBalanceData) {
        toolbar.setTitle(tokoCashBalanceData.getTitleText());
        clearHolder(balanceTokoCashViewLayout);
        clearHolder(receivedTokoCashViewLayout);
        balanceTokoCashView.renderDataBalance(tokoCashBalanceData);
        balanceTokoCashView.setListener(getBalanceListener());
        receivedTokoCashView.renderReceivedView(tokoCashBalanceData);
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

    /**
     * @value topUpAvailable is for showing or hiding topup
     */

    @Override
    public void renderTopUpDataTokoCash(CategoryData categoryData) {
        clearHolder(topupTokoCashViewLayout);
        Operator operatorSelected = null;
        for (Operator operator : categoryData.getOperatorList()) {
            if (operator.getOperatorId().equalsIgnoreCase(categoryData.getDefaultOperatorId())) {
                operatorSelected = operator;
            }
        }
        categoryId = categoryData.getCategoryId();
        operatorId = operatorSelected.getOperatorId();
        topUpTokoCashView.renderDataTopUp(categoryData, operatorSelected);
        topUpTokoCashView.setListener(getActionListenerTopUpView());
        if (topUpAvailable) topupTokoCashViewLayout.addView(topUpTokoCashView);
    }

    private TopUpTokoCashView.ActionListener getActionListenerTopUpView() {
        return new TopUpTokoCashView.ActionListener() {
            @Override
            public void onDigitalChooserClicked(List<Product> productList, String productText) {
                startActivityForResult(
                        DigitalChooserActivity.newInstanceProductChooser(
                                TopUpTokoCashActivity.this, categoryId, operatorId, productText
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
            case REQUEST_CODE_ACCOUNT_SETTING:
                if (resultCode == Activity.RESULT_OK && data != null &&
                        data.hasExtra(WalletAccountSettingActivity.KEY_INTENT_RESULT)) {
                    setResult(RESULT_OK);
                    finish();
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

            //TODO in next sprint activate this code below to use the new version clean architecture
//            Application application = this.getApplication();
//            if (application != null && application instanceof TokoCashRouter) {
//                Intent intent = ((TokoCashRouter) application).goToHistoryTokoCash(this);
//                startActivity(intent);
//            }

            return true;
        } else if (item.getItemId() == R.id.action_account_setting_tokocash) {
            startActivityForResult(WalletAccountSettingActivity.newInstance(this),
                    REQUEST_CODE_ACCOUNT_SETTING);
            return true;
        } else if (item.getItemId() == R.id.action_menu_help_tokocash) {
            startActivity(DigitalWebActivity.newInstance(this,
                    this.getString(R.string.url_help_center_tokocash)));
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