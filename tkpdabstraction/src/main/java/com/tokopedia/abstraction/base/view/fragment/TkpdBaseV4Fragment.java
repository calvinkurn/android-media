package com.tokopedia.abstraction.base.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater;
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver;

import static com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater.DEFAULT;

/**
 * Created by Herdi_WORK on 22.11.16.
 */
public abstract class TkpdBaseV4Fragment extends Fragment {

    protected abstract String getScreenName();

    protected String fragmentInflater = DEFAULT;

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachActivity(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity);
        }
    }

    protected void onAttachActivity(Context context) {
        // to be overriden in child
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            getView().post(() -> {
                if (isVisible() && fragmentInflater.equals(FragmentInflater.ACTIVITY)) {
                    FragmentLifecycleObserver.INSTANCE.onFragmentResume(TkpdBaseV4Fragment.this);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FragmentLifecycleObserver.INSTANCE.onFragmentStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentLifecycleObserver.INSTANCE.onFragmentStop(this);
    }

}