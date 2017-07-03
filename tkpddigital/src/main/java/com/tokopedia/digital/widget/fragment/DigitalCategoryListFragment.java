package com.tokopedia.digital.widget.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;
import com.tokopedia.digital.widget.data.mapper.CategoryDigitalListDataMapper;
import com.tokopedia.digital.widget.data.mapper.ICategoryDigitalListDataMapper;
import com.tokopedia.digital.widget.domain.DigitalCategoryListRepository;
import com.tokopedia.digital.widget.interactor.DigitalCategoryListInteractor;
import com.tokopedia.digital.widget.listener.IDigitalCategoryListView;
import com.tokopedia.digital.widget.presenter.DigitalCategoryListPresenter;
import com.tokopedia.digital.widget.presenter.IDigitalCategoryListPresenter;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListFragment extends
        BasePresenterFragment<IDigitalCategoryListPresenter> implements
        IDigitalCategoryListView {

    private CompositeSubscription compositeSubscription;

    public static DigitalCategoryListFragment newInstance() {
        return new DigitalCategoryListFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.processGetDigitalCategoryList();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        MojitoService mojitoService = new MojitoService();
        ICategoryDigitalListDataMapper mapperData = new CategoryDigitalListDataMapper();
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        presenter = new DigitalCategoryListPresenter(
                new DigitalCategoryListInteractor(
                        compositeSubscription,
                        new DigitalCategoryListRepository(
                                mojitoService, new GlobalCacheManager(), mapperData
                        )
                ), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_digital_category_list_digital_module;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void renderDigitalCategoryDataList(List<DigitalCategoryItemData> digitalCategoryItemDataList) {

        for (DigitalCategoryItemData data : digitalCategoryItemDataList) {
            Log.d("DIGITAL_CATEGORY", data.getName());
        }
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }


}
