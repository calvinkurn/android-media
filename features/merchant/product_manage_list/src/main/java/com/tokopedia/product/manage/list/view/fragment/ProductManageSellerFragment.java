package com.tokopedia.product.manage.list.view.fragment;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService;
import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.list.di.DaggerProductDraftListCountComponent;
import com.tokopedia.product.manage.list.di.ProductDraftListCountModule;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListCountView;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.track.TrackApp;

import javax.inject.Inject;

public class ProductManageSellerFragment extends ProductManageFragment implements ProductDraftListCountView {

    private BroadcastReceiver draftBroadCastReceiver;

    @Inject
    ProductDraftListCountPresenter productDraftListCountPresenter;

    private TextView tvDraftProductInfo;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_manage_product_seller;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        tvDraftProductInfo = (TextView) view.findViewById(R.id.tv_draft_product);
        tvDraftProductInfo.setVisibility(View.GONE);
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductDraftListCountComponent
                .builder()
                .productDraftListCountModule(new ProductDraftListCountModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productDraftListCountPresenter.attachView(this);
        productManagePresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productDraftListCountPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerDraftReceiver();
        if (isMyServiceRunning(UploadProductService.class)) {
            productDraftListCountPresenter.fetchAllDraftCount();
        } else {
            productDraftListCountPresenter.fetchAllDraftCountWithUpdateUploading();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        if(manager != null && manager.getRunningServices(Integer.MAX_VALUE) != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }else {
            return false;
        }
    }

    private void registerDraftReceiver(){
        if (draftBroadCastReceiver == null) {
            draftBroadCastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(UploadProductService.ACTION_DRAFT_CHANGED)) {
                        productDraftListCountPresenter.fetchAllDraftCount();
                    }
                }
            };
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                draftBroadCastReceiver,new IntentFilter(UploadProductService.ACTION_DRAFT_CHANGED));
    }

    private void unregisterDraftReceiver(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(draftBroadCastReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterDraftReceiver();
    }

    @Override
    public void onDraftCountLoaded(long rowCount) {
        if (rowCount == 0) {
            tvDraftProductInfo.setVisibility(View.GONE);
        } else {
            tvDraftProductInfo.setText(
                    MethodChecker.fromHtml(getString(R.string.product_manage_you_have_x_unfinished_product, rowCount)));
            tvDraftProductInfo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventManageProductClicked(AppEventTracking.EventLabel.DRAFT_PRODUCT);
                    startActivity(new Intent(getActivity(), ProductDraftListActivity.class));
                }
            });
            tvDraftProductInfo.setVisibility(View.VISIBLE);
        }
    }

    public void eventManageProductClicked(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.CLICK_MANAGE_PRODUCT,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.Action.CLICK,
                label);
    }

    @Override
    public void onDraftCountLoadError() {
        // delete all draft when error loading draft
        productDraftListCountPresenter.clearAllDraft();
        tvDraftProductInfo.setVisibility(View.GONE);
    }

}