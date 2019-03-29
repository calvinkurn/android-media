package com.tokopedia.tokocash.autosweepmf.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.autosweepmf.view.activity.SetAutoSweepLimitActivity;
import com.tokopedia.tokocash.autosweepmf.view.contract.AutoSweepHomeContract;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.view.presenter.AutoSweepHomePagePresenter;
import com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant;
import com.tokopedia.tokocash.autosweepmf.view.util.MfUtils;
import com.tokopedia.tokocash.common.di.TokoCashComponent;

import javax.inject.Inject;

/**
 * Landing page for auto sweep features which can be added into any activity or fragment
 * It will taking the current tokocash balance as an argument param
 */
public class AutoSweepHomeFragment extends BaseDaggerFragment implements AutoSweepHomeContract.View, View.OnClickListener {

    private static final int AUTO_SWEEP_INACTIVE = 0;
    private static final int AUTO_SWEEP_ACTIVE = 1;
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private static String WEB_LINK_MF_DASHBOARD;
    private static String MF_INFO_LINK = CommonConstant.NOT_AVAILABLE;
    private TextView mTextLimitTokocash, mTextLimitTokocashValue, mTextDescription;
    private Button mBtnPositive, mBtnNegative;
    private LinearLayout mContainerWarning;
    private TextView mTextWaningTitle, mTextWarningMessage, mTextError;
    private SwitchCompat mSwitchAutoSweep;
    private ViewFlipper mContainerMain;
    private int mAutoSweepStatus = -1;
    private long mValueAutoSweepLimit = -1;
    private BottomSheetView mToolTip;
    private AlertDialog.Builder mDialogBuilder;
    private String mDialogNegativeLink = CommonConstant.NOT_AVAILABLE;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(CommonConstant.EVENT_KEY_NEEDED_RELOADING, false)) {
                mPresenter.getAutoSweepDetail();
            }
        }
    };

    @Inject
    AutoSweepHomePagePresenter mPresenter;

    public static AutoSweepHomeFragment newInstance(@NonNull Bundle bundle) {
        AutoSweepHomeFragment fragment = new AutoSweepHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_autosweepmf, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
        mPresenter.getAutoSweepDetail();

        //initially hide the view to avoid the loader in case of feature has already disable from server
        view.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getAppContext()).unregisterReceiver(mMessageReceiver);
        mMessageReceiver = null;
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void hideLoading() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    /**
     * mutualfund_account_status
     * 0=> user do not have mutual fund mutualfund_account
     * 1=> user have active mutual fund account
     * 2=> user have pending for mutual fund registrion (On Hold)
     * <p>
     * autosweep_status
     * 0=> Autosweep turn off
     * 1=> AutoSweep turn On
     */
    @Override
    public void onSuccessAutoSweepDetail(@NonNull AutoSweepDetail data) {
        if (getView() == null) {
            //To ensure UI get setup
            return;
        }

        //Checking for feature visibility  mode, If its disable by server then hide self and do nothing
        if (!data.isEnable()) {
            getView().setVisibility(View.GONE);
            return;
        }

        getView().setVisibility(View.VISIBLE);

        if (data.getAutoSweepStatus() == AUTO_SWEEP_ACTIVE) {
            onAutoSweepActive();
        } else if (data.getAutoSweepStatus() == AUTO_SWEEP_INACTIVE) {
            onAutoSweepInActive();
        }

        //init dashboard url
        if (URLUtil.isValidUrl(data.getDashboardLink())) {
            WEB_LINK_MF_DASHBOARD = data.getDashboardLink();
        }

        //init MF auto sweep url
        if (URLUtil.isValidUrl(data.getMfInfoLink())) {
            MF_INFO_LINK = data.getMfInfoLink();
        }

        //init auto sweep limit
        this.mValueAutoSweepLimit = data.getAmountLimit();

        //Tooltip tooltip
        if (!MfUtils.isNullOrEmpty(data.getTooltipContent())) {
            initToolTip(data.getTitle(), data.getTooltipContent());
            mTextWaningTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mf_info, 0);
        } else {
            mToolTip = null;
            mTextWaningTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        //Setting description
        if (!MfUtils.isNullOrEmpty(data.getDescription())) {
            mTextDescription.setVisibility(View.VISIBLE);
            mTextDescription.setText(MethodChecker.fromHtml(data.getDescription()));
        } else {
            mTextDescription.setVisibility(View.GONE);
        }

        //Setting warning container
        if (!MfUtils.isNullOrEmpty(data.getTitle())) {
            mContainerWarning.setVisibility(View.VISIBLE);
            mTextWaningTitle.setText(data.getTitle());
            mTextWarningMessage.setText(MethodChecker.fromHtml(data.getContent()));
            //For supporting html anchor tag
            mTextWarningMessage.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mContainerWarning.setVisibility(View.GONE);
        }

        //Setting limit amount
        if (data.getAmountLimit() > 0) {
            mTextLimitTokocash.setVisibility(View.VISIBLE);
            mTextLimitTokocashValue.setVisibility(View.VISIBLE);
            mTextLimitTokocashValue.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat((int)
                    data.getAmountLimit(), false));
        } else {
            mTextLimitTokocash.setVisibility(View.GONE);
            mTextLimitTokocashValue.setVisibility(View.GONE);
        }

        //Setting dialog
        if (!MfUtils.isNullOrEmpty(data.getDialogContent())) {
            initDialog(data);
        } else {
            mDialogBuilder = null;
        }
    }

    @Override
    public void onErrorAutoSweepDetail(@NonNull String error) {
        showError(error);
    }

    @Override
    public void onAutoSweepActive() {
        mAutoSweepStatus = AUTO_SWEEP_ACTIVE;
        mSwitchAutoSweep.setVisibility(View.VISIBLE);
        mSwitchAutoSweep.setChecked(true);
        mBtnPositive.setText(R.string.mf_action_goto_dashboard);
    }

    @Override
    public void onAutoSweepInActive() {
        mAutoSweepStatus = AUTO_SWEEP_INACTIVE;
        mSwitchAutoSweep.setVisibility(View.GONE);
        mSwitchAutoSweep.setChecked(false);
        mBtnPositive.setText(R.string.mf_action_enable);
    }

    @Override
    public void initDialog(AutoSweepDetail data) {
        mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setTitle(data.getDialogTitle());

        //setting negative button link
        this.mDialogNegativeLink = data.getDialogNegativeButtonLink();
        mDialogBuilder.setMessage(MethodChecker.fromHtml(data.getDialogContent()));

        mDialogBuilder.setPositiveButton(data.getDialogLabelPositive(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                navigateToLimitPage();
            }
        });

        mDialogBuilder.setNegativeButton(data.getDialogLabelNegative(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!mDialogNegativeLink.equalsIgnoreCase(CommonConstant.NOT_AVAILABLE)
                        && URLUtil.isValidUrl(mDialogNegativeLink)) {
                    openWebView(mDialogNegativeLink);
                }
            }
        });
    }

    @Override
    public void showDialog() {
        if (mDialogBuilder != null) {
            AlertDialog dialog = mDialogBuilder.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    R.color.grey_warm));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        }
    }

    @Override
    public void showError(String message) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
        mTextError.setText(message);
    }

    @Override
    public void learMore() {
        //startActivity(HelpActivity.getCallingIntent(getAppContext()));
        //TODO for opening WenView need to discuss it with @Hameer
        openWebView(MF_INFO_LINK);
    }

    @Override
    public void navigateToLimitPage() {
        Bundle extras = new Bundle();
        extras.putString(CommonConstant.EXTRA_AVAILABLE_TOKOCASH,
                getArguments().getString(CommonConstant.EXTRA_AVAILABLE_TOKOCASH,
                        CommonConstant.NOT_AVAILABLE));
        extras.putLong(CommonConstant.EXTRA_AUTO_SWEEP_LIMIT, mValueAutoSweepLimit);
        startActivity(SetAutoSweepLimitActivity.getCallingIntent(getActivityContext(),
                extras));
    }

    @Override
    public void onAttach(Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter(CommonConstant.EVENT_AUTOSWEEPMF_STATUS_CHANGED));
        super.onAttach(context);

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
        mPresenter.attachView(this);

    }

    @Override
    public void openWebView(String url) {
        Intent intent = ((TokoCashRouter) getActivity().getApplication()).getWebviewActivityWithIntent(getActivityContext(), url);
        startActivity(intent);
    }

    @Override
    public void onSuccessAutoSweepStatus(@NonNull AutoSweepLimit data) {
        //Success mean limit has reset, now wee need to re-fetch details again
        mPresenter.getAutoSweepDetail();
    }

    @Override
    public void onErrorAutoSweepStatus(@NonNull String error) {
        mSwitchAutoSweep.setChecked(true);
        SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.updateAutoSweepStatus(false, (int) mValueAutoSweepLimit);
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void retry() {
        mPresenter.getAutoSweepDetail();
    }

    @Override
    public Context getAppContext() {
        return getActivityContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.button_positive) {
            if (mAutoSweepStatus == AUTO_SWEEP_ACTIVE) {
                openWebView(WEB_LINK_MF_DASHBOARD);
            } else {
                if (mDialogBuilder != null) {
                    //Show explanation dialog before navigating to limit screen if dialog content available
                    showDialog();
                } else {
                    navigateToLimitPage();
                }
            }
        } else if (source.getId() == R.id.button_negative) {
            learMore();
        } else if (source.getId() == R.id.text_warning_title) {
            if (mToolTip != null && !mToolTip.isShowing()) {
                mToolTip.show();
            }
        } else if (source.getId() == R.id.text_limit_tokocash_value) {
            navigateToLimitPage();
        } else if (source.getId() == R.id.button_retry) {
            retry();
        }
    }

    private void initViews(View view) {
        mTextLimitTokocash = view.findViewById(R.id.text_limit_tokocash);
        mTextLimitTokocashValue = view.findViewById(R.id.text_limit_tokocash_value);
        mTextDescription = view.findViewById(R.id.text_description);
        mTextWaningTitle = view.findViewById(R.id.text_warning_title);
        mTextWarningMessage = view.findViewById(R.id.text_warning_message);
        mTextError = view.findViewById(R.id.text_error);

        mBtnPositive = view.findViewById(R.id.button_positive);
        mBtnNegative = view.findViewById(R.id.button_negative);

        mSwitchAutoSweep = view.findViewById(R.id.switch_auto_sweep);
        mContainerMain = view.findViewById(R.id.container_main);
        mContainerWarning = view.findViewById(R.id.container_warning);
    }

    /**
     * Set the listener on view this method should be call from only onViewCreated
     */
    private void initListener() {
        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);
        mTextWaningTitle.setOnClickListener(this);
        mTextLimitTokocashValue.setOnClickListener(this);
        getView().findViewById(R.id.button_retry).setOnClickListener(this);

        mSwitchAutoSweep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //Disabling the autosweep so that passing zero
                    mPresenter.updateAutoSweepStatus(false, (int) mValueAutoSweepLimit);
                }
            }
        });
    }

    private void initToolTip(String title, String content) {
        mToolTip = new BottomSheetView(getActivityContext());
        mToolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(content)
                .setUrlButton(WEB_LINK_MF_DASHBOARD, getString(R.string.mf_action_goto_dashboard))
                .build());
        mToolTip.setListener(new BottomSheetView.ActionListener() {
            @Override
            public void clickOnTextLink(String status) {
            }

            @Override
            public void clickOnButton(String url, String appLink) {
                openWebView(WEB_LINK_MF_DASHBOARD);
            }
        });
    }
}
