package com.tokopedia.core.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import timber.log.Timber;

/**
 * Extends TkpdBaseV4Fragment from tkpd abstraction
 */
@Deprecated
public abstract class TkpdFragment extends Fragment {
	
	public Context context;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
        Timber.d(getClass().toString());
	}

	protected abstract String getScreenName();
}
