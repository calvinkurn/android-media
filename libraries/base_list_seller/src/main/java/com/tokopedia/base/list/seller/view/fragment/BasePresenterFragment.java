package com.tokopedia.base.list.seller.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * Created by normansyahputa on 5/26/17.
 * refer to old {@link com.tokopedia.core.app.BasePresenterFragment}
 *
 * PLEASE THIS IS TEMPORARY CONVERT FROM OLD BASE CLASS TO {@link BaseDaggerFragment}
 */

@Deprecated
public abstract class BasePresenterFragment<P> extends BaseDaggerFragment {

    protected P presenter;

    @Override
    protected String getScreenName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        initialPresenter();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initialVar();
        setViewListener();
        setActionVar();
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            onFirstTimeLaunched();
        } else {
            onRestoreState (savedInstanceState);
        }
    }

    public void onRestoreState(Bundle savedInstanceState) {
    }

    /**
     * instantiate presenter disini. sesuai dengan Type param di class
     */
    protected void initialPresenter() {
    }

    /**
     * Cast si activity ke listener atau bisa juga ini untuk context activity
     *
     * @param context si activity yang punya fragment
     */
    protected void onAttachListener(Context context) {
    }

    /**
     * kalau memang argument tidak kosong. ini data argumentnya
     *
     * @param arguments argument nya
     */
    protected void setupArguments(Bundle arguments) {
    }

    /**
     * Layout xml untuk si fragment
     *
     * @return layout id
     */
    protected int getFragmentLayout() {
        return -1;
    }

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    protected void initView(View view) {
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    protected void setViewListener() {
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    protected void initialVar() {
    }

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction(){}
     */
    protected void setActionVar() {
    }

}
