package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.activation.presentation.activity.ActivateTokoCashActivity;
import com.tokopedia.tokocash.autosweepmf.view.fragment.AutoSweepHomeFragment;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.common.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.compoundview.BalanceTokoCashView;
import com.tokopedia.tokocash.historytokocash.presentation.compoundview.ReceivedTokoCashView;
import com.tokopedia.tokocash.historytokocash.presentation.contract.HomeTokoCashContract;
import com.tokopedia.tokocash.historytokocash.presentation.presenter.HomeTokoCashPresenter;

import javax.inject.Inject;

import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.EXTRA_AVAILABLE_TOKOCASH;

/**
 * Created by nabillasabbaha on 8/18/17.
 */
public class HomeTokoCashFragment extends BaseDaggerFragment implements HomeTokoCashContract.View {

    public static final String EXTRA_TOP_UP_AVAILABLE = "EXTRA_TOP_UP_AVAILABLE";
    private static final int REQUEST_CODE_LOGIN = 1007;

    private RelativeLayout mainContent;
    private LinearLayout mainHome;
    private ProgressBar progressLoading;

    private BalanceTokoCashView balanceTokoCashView;
    private ReceivedTokoCashView receivedTokoCashView;
    private BottomSheetView bottomSheetTokoCashView;
    private FrameLayout topupFrameLayout;

    private boolean topUpAvailable;
    private ActionListener listener;

    @Inject
    HomeTokoCashPresenter presenter;

    public static HomeTokoCashFragment newInstance(boolean isTopUpAvailable) {
        HomeTokoCashFragment fragment = new HomeTokoCashFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_TOP_UP_AVAILABLE, isTopUpAvailable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tokocash, container, false);
        balanceTokoCashView = view.findViewById(R.id.balance_tokocash_layout);
        receivedTokoCashView = view.findViewById(R.id.received_tokocash_layout);
        mainContent = view.findViewById(R.id.main_content);
        progressLoading = view.findViewById(R.id.pb_main_loading);
        topupFrameLayout = view.findViewById(R.id.topup_tokocash_layout);
        mainHome = view.findViewById(R.id.main_home);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.topUpAvailable = getArguments().getBoolean(EXTRA_TOP_UP_AVAILABLE, true);
        bottomSheetTokoCashView = new BottomSheetView(getActivity());
        presenter.getHistoryTokocashForRefreshingTokenWallet();

        if (savedInstanceState == null) {
            if (getActivity().getApplication() != null && getActivity().getApplication() instanceof TokoCashRouter) {
                getChildFragmentManager().beginTransaction().add(R.id.topup_tokocash_layout,
                        ((TokoCashRouter) getActivity().getApplication()).getTopupTokoCashFragment()).commit();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        topupFrameLayout.setVisibility(topUpAvailable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgressLoading() {
        progressLoading.setVisibility(View.VISIBLE);
        mainHome.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressLoading() {
        if (progressLoading.getVisibility() == View.VISIBLE) {
            progressLoading.setVisibility(View.GONE);
            mainHome.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void renderBalanceTokoCash(BalanceTokoCash balanceTokoCash) {
        listener.setTitle(balanceTokoCash.getTitleText());
        balanceTokoCashView.setListener(getBalanceListener());
        balanceTokoCashView.renderDataBalance(balanceTokoCash);
        receivedTokoCashView.renderReceivedView(balanceTokoCash);
        bottomSheetTokoCashView.renderBottomSheet(new BottomSheetView
                .BottomSheetField.BottomSheetFieldBuilder()
                .setTitle(getString(R.string.title_tooltip_tokocash))
                .setBody(getString(R.string.body_tooltip_tokocash))
                .setImg(R.drawable.activate_ic_activated)
                .build());

        //Adding auto sweep fragment on successful retrieval of tokocash balance
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_AVAILABLE_TOKOCASH, balanceTokoCash.getBalance());
        addAutoSweepFragment(bundle);
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
    public void showEmptyPage(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getActivity(), throwable);
        NetworkErrorHelper.showEmptyState(getActivity(), mainContent, message, getRetryListener());
    }

    private NetworkErrorHelper.RetryClickedListener getRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processGetBalanceTokoCash();
            }
        };
    }

    @Override
    public void addAutoSweepFragment(Bundle bundle) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_autosweepmf, AutoSweepHomeFragment.newInstance(bundle))
                .commit();
    }

    @Override
    public void navigatePageToActivateTokocash() {
        startActivity(ActivateTokoCashActivity.newInstance(getActivity()));
        getActivity().finish();
    }

    @Override
    public void navigateToLoginPage() {
        if (getActivity().getApplication() != null && getActivity().getApplication() instanceof TokoCashRouter) {
            startActivityForResult(((TokoCashRouter) getActivity().getApplication()).getLoginIntent(), REQUEST_CODE_LOGIN);
            getActivity().finish();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ActionListener) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        TokoCashComponent tokoCashComponent =
                TokoCashComponentInstance.getComponent(getActivity().getApplication());
        tokoCashComponent.inject(this);
        presenter.attachView(this);
    }

    public interface ActionListener {
        void setTitle(String title);
    }
}
