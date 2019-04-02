package com.tokopedia.tokocash.autosweepmf.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.autosweepmf.view.contract.SetAutoSweepLimitContract;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.view.presenter.SetAutoSweepLimitPresenter;
import com.tokopedia.tokocash.common.di.TokoCashComponent;

import javax.inject.Inject;

import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.AUTO_SWEEP_SEEK_BAR_STEPS;

/**
 * Autosweep limit set/reset screen, once scuccessful reset limit it will also fire a broadcasts message for current auto sweep status
 */
public class SetAutoSweepLimitFragment extends BaseDaggerFragment implements SetAutoSweepLimitContract.View, View.OnClickListener {

    private AppCompatSeekBar mSeekBarAmount;
    private AppCompatEditText mEditAmount;
    private TextView mBtnSubmit;

    @Inject
    SetAutoSweepLimitPresenter mPresenter;

    @Inject
    InputFilter[] mAmountInputFilter;

    /*TextWatcher for amount EditText in to format the Rp values on type*/
    private TextWatcher mAmountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //Removing SeekBar listener in order avoid circular callback
            mSeekBarAmount.setOnSeekBarChangeListener(null); /* Null will release the prevoius holds listener */
            try {
                mSeekBarAmount.setProgress(Integer.parseInt(s.toString()));
            } catch (NumberFormatException nfe) {
                //Do nothing, just for safer side
            }
            mSeekBarAmount.setOnSeekBarChangeListener(mSeekBarChangeListener);
        }
    };

    /*SeekBar change listener for updating the EditText values on slide*/
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //Removing SeekBar,EditText listener in order avoid circular callback
            mSeekBarAmount.setOnSeekBarChangeListener(null); /* Null will release the prevoius holds listener */
            mEditAmount.removeTextChangedListener(mAmountTextWatcher);
            if (progress < mPresenter.getAutoSweepMinLimit()) {
                mEditAmount.setText(String.valueOf(mPresenter.getAutoSweepMinLimit()));
            } else {
                //Creating the steps for SeekBar to better selection experience
                progress = Math.round(progress / AUTO_SWEEP_SEEK_BAR_STEPS) * AUTO_SWEEP_SEEK_BAR_STEPS;
                seekBar.setProgress(progress);
                mEditAmount.setText(String.valueOf(progress));
                mEditAmount.setSelection(mEditAmount.getText().length());
            }

            mSeekBarAmount.setOnSeekBarChangeListener(mSeekBarChangeListener);
            mEditAmount.addTextChangedListener(mAmountTextWatcher);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public static SetAutoSweepLimitFragment newInstance(@NonNull Bundle bundle) {
        SetAutoSweepLimitFragment fragment = new SetAutoSweepLimitFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_autosweep_limit, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading() {
        getView().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoading() {
        getView().findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onSuccessAccountStatus(AutoSweepLimit data) {
        if (data != null && data.isStatus()) {
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setMessage(R.string.mf_message_success);
            adb.setPositiveButton(R.string.mf_label_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (getActivity() != null) {
                        getActivity().finish();
                        mPresenter.sendMessage();
                    }
                }
            });

            final AlertDialog dialog = adb.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivityContext(), R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        } else {
            //TODO @lavekush ask error message from Hameer
            SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), "Error", new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    mPresenter.postAutoSweepLimit(true, Integer.parseInt(mEditAmount.getText().toString()));
                }
            });
            snackbarRetry.showRetrySnackbar();
        }
    }

    @Override
    public void onErrorAccountStatus(String error) {
        SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.postAutoSweepLimit(true, Integer.parseInt(mEditAmount.getText().toString()));
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public Long getLongRemoteConfig(String key) {
        return ((TokoCashRouter) getActivity().getApplication()).getLongConfig(key);
    }

    @Override
    public void onAttach(Context context) {
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
    public void onClick(View source) {
        if (source.getId() == R.id.button_submit) {
            if (isValidate()) {
                mPresenter.postAutoSweepLimit(true, Integer.parseInt(mEditAmount.getText().toString()));
            } else {
                //Setting error message if provided autosweep limit is then from AUTO_SWEEP_MF_MIN_LIMIT
                mEditAmount.setError(new StringBuilder(getString(R.string.mf_error_min_auto_sweep_limit))
                        .append(" ")
                        .append(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                mPresenter.getAutoSweepMinLimit(), false)));
            }
        }
    }

    private void initViews(@NonNull View view) {
        mSeekBarAmount = view.findViewById(R.id.seek_bar_amount);
        mSeekBarAmount.setMax((int) mPresenter.getAutoSweepMaxLimit());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // call something for API Level 26+
            mSeekBarAmount.setMin((int) mPresenter.getAutoSweepMinLimit());
        }


        //Setting auto sweep progress limit if exist to maintain the consistency
        mSeekBarAmount.setProgress((int) mPresenter.getAutoSweepLimit(getArguments()));

        TextView textSeekBarMin = view.findViewById(R.id.text_seekbar_min);
        textSeekBarMin.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(mPresenter.getAutoSweepMinLimit(), false));
        TextView textSeekBarMax = view.findViewById(R.id.text_seekbar_max);
        textSeekBarMax.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(mPresenter.getAutoSweepMaxLimit(), false));
        mEditAmount = view.findViewById(R.id.edit_text_amount);
        mEditAmount.clearFocus();
        mBtnSubmit = view.findViewById(R.id.button_submit);
        TextView textTokocashValue = view.findViewById(R.id.text_tokocash_balance_value);
        textTokocashValue.setText(mPresenter.getTokocashBalance(getArguments()));
    }

    private void initListener() {
        //Setting up filter in order to avoid max values
        mEditAmount.setFilters(mAmountInputFilter);
        mEditAmount.addTextChangedListener(mAmountTextWatcher);

        //Setting auto sweep limit if exist to maintain the consistency
        mEditAmount.setText(String.valueOf(mPresenter.getAutoSweepLimit(getArguments())));

        mBtnSubmit.setOnClickListener(this);
        mSeekBarAmount.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    /**
     * Vaidate the entered tokocash limit against min limit
     *
     * @return true for success else false
     */
    private boolean isValidate() {
        try {
            return mEditAmount != null
                    && Integer.parseInt(mEditAmount.getText().toString()) >= mPresenter.getAutoSweepMinLimit();
        } catch (NumberFormatException nfe) {
            //To avoiding space NFE crash
            return false;
        }
    }
}