package com.tokopedia.sellerapp.home.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.sellerapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CloseAppsDialogFragment extends DialogFragment {
    public static final String CLOSE_APPS_CACHE = "CLOSE_APPS_CACHE";
    public static final String DONT_SHOW_FLAG = "DONT_SHOW_FLAG";
    private LocalCacheHandler mHandler;
    @BindView(R.id.dont_show)
    CheckBox mDontShowAgain;

    public CloseAppsDialogFragment() {
    }

    public static CloseAppsDialogFragment newInstance() {
        CloseAppsDialogFragment fragment = new CloseAppsDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mHandler = new LocalCacheHandler(getActivity(), CLOSE_APPS_CACHE);
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.yes_but)
    public void confirmClose(){
        if (mDontShowAgain.isChecked()) {
            mHandler.putBoolean(DONT_SHOW_FLAG, true);
            mHandler.applyEditor();
        }
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            getActivity().finishAffinity();
        }else{
            ActivityCompat.finishAffinity(getActivity());
        }
        getActivity().finish();
    }

    @OnClick(R.id.no_but)
    public void cancelClose(){
        dismiss();
    }
    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mHandler = new LocalCacheHandler(getActivity(), CLOSE_APPS_CACHE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_close_apps, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.qc_launcher);
        builder.setTitle(getString(R.string.exit_apps_dialog));
        builder.setView(view);
        ButterKnife.bind(this, view);

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDontShowAgain.isChecked()) {
                    mHandler.putBoolean(DONT_SHOW_FLAG, true);
                    mHandler.applyEditor();
                }
                getActivity().finish();
            }
        });
        builder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }*/

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private int getFragmentLayout() {
        return R.layout.fragment_dialog_close_apps;
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
