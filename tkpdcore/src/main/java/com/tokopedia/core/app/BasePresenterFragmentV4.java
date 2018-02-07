package com.tokopedia.core.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author rizkyfadillah on 10/16/2017.
 */

@Deprecated
public abstract class BasePresenterFragmentV4<P> extends Fragment {

    private static final String TAG = BasePresenterFragment.class.getSimpleName();

    protected P presenter;

    protected Bundle savedState;
    protected Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(getOptionsMenuEnable());
        initialListener(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(isRetainInstance());
        Log.d(TAG, "ON CREATE");
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        initialPresenter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
            Log.d(TAG, "ON ACTIVITY CREATE FIRST");
        } else {
            Log.d(TAG, "ON ACTIVITY CREATE");
        }
    }

    protected abstract boolean isRetainInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "ON CREATE VIEW");
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectView(view);
        initView(view);
        initialVar();
        setViewListener();
        setActionVar();
    }

    protected abstract void onFirstTimeLaunched();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "ON SAVE INSTANCE STATE");
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b == null) b = new Bundle();
            b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    public abstract void onSaveState(Bundle state);

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString("text"));
            onRestoreState(savedState);
        }
    }

    public abstract void onRestoreState(Bundle savedState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
        Log.d(TAG, "ON DESTROY VIEW");
        unbinder.unbind();
    }

    private void injectView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    /**
     * apakah fragment ini support options menu?
     *
     * @return iya atau tidak
     */
    protected abstract boolean getOptionsMenuEnable();

    /**
     * instantiate presenter disini. sesuai dengan Type param di class
     */
    protected abstract void initialPresenter();

    /**
     * Cast si activity ke listener atau bisa juga ini untuk context activity
     *
     * @param activity si activity yang punya fragment
     */
    protected abstract void initialListener(Activity activity);

    /**
     * kalau memang argument tidak kosong. ini data argumentnya
     *
     * @param arguments argument nya
     */
    protected abstract void setupArguments(Bundle arguments);

    /**
     * Layout xml untuk si fragment
     *
     * @return layout id
     */
    protected abstract int getFragmentLayout();

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    protected abstract void initView(View view);

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    protected abstract void setViewListener();

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    protected abstract void initialVar();

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction();
     */
    protected abstract void setActionVar();

}
