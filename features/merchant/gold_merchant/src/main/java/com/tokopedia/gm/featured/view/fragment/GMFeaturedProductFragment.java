package com.tokopedia.gm.featured.view.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.base.list.seller.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.base.list.seller.view.fragment.BaseListFragment;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;
import com.tokopedia.base.list.seller.view.old.Pair;
import com.tokopedia.base.list.seller.view.old.RetryDataBinder;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.gm.R;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.featured.constant.GMFeaturedProductTypeView;
import com.tokopedia.gm.featured.di.component.DaggerGMFeaturedProductComponent;
import com.tokopedia.gm.featured.domain.interactor.GMFeaturedProductSubmitUseCase;
import com.tokopedia.gm.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.gm.featured.helper.OnStartDragListener;
import com.tokopedia.gm.featured.helper.SimpleItemTouchHelperCallback;
import com.tokopedia.gm.featured.view.adapter.GMFeaturedProductAdapter;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;
import com.tokopedia.gm.featured.view.listener.GMFeaturedProductView;
import com.tokopedia.gm.featured.view.presenter.GMFeaturedProductPresenterImpl;
import com.tokopedia.gm.statistic.view.adapter.GMStatRetryDataBinder;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.picker.common.ProductListPickerConstant;
import com.tokopedia.seller.product.picker.view.ProductListPickerActivity;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductFragment extends BaseListFragment<BlankPresenter, GMFeaturedProductModel>
        implements GMFeaturedProductView, OnStartDragListener,
        GMFeaturedProductAdapter.UseCaseListener, SimpleItemTouchHelperCallback.isEnabled,
        BaseMultipleCheckListAdapter.CheckedCallback<GMFeaturedProductModel> {

    private static final int REQUEST_CODE = 12314;
    private static final int MAX_ITEM = 5;
    @Inject
    GMFeaturedProductPresenterImpl featuredProductPresenter;
    private FloatingActionButton fab;
    private ItemTouchHelper mItemTouchHelper;
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayoutContainer;
    @GMFeaturedProductTypeView
    private int featuredProductTypeView = GMFeaturedProductTypeView.DEFAULT_DISPLAY;
    private List<GMFeaturedProductModel> gmFeaturedProductModelListFromServer;
    private List<Pair<Integer, GMFeaturedProductModel>> gmTemporaryDelete;

    public static GMFeaturedProductFragment createInstance() {
        return new GMFeaturedProductFragment();
    }

    @Override
    protected BaseListAdapter<GMFeaturedProductModel> getNewAdapter() {
        GMFeaturedProductAdapter gmFeaturedProductAdapter = new GMFeaturedProductAdapter(this);
        gmFeaturedProductAdapter.setUseCaseListener(this);
        gmFeaturedProductAdapter.setCheckedCallback(this);
        return gmFeaturedProductAdapter;
    }

    @Override
    public RetryDataBinder getRetryViewDataBinder(BaseListAdapter adapter) {
        return new GMStatRetryDataBinder(adapter);
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyGroupAdsDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_empty_featured_product);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.gm_featured_product_title_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.gm_featured_product_description_empty));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.gm_featured_product_add_title_empty));
        emptyGroupAdsDataBinder.setCallback(new BaseEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {
                moveToProductPicker();
            }

            @Override
            public void onEmptyButtonClicked() {
                moveToProductPicker();
            }
        });
        return emptyGroupAdsDataBinder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initInjector() {
        DaggerGMFeaturedProductComponent
                .builder()
                .gMComponent(getComponent(GMComponent.class))
                .build().inject(this);
        featuredProductPresenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gm_featured_product;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickAddFeaturedProduct(getActivity());
                moveToProductPicker();
            }
        });
        coordinatorLayoutContainer = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout_container);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    private void showSnackbarWithUndo() {
        if (gmTemporaryDelete != null) {
            String textToPresent = getString(R.string.success_delete_n_product_text, gmTemporaryDelete.size());
            if (adapter.getDataSize() == 0) {
                textToPresent = getString(R.string.success_empty_delete_featured_product_empty);
            }
            NetworkErrorHelper.createSnackbarWithAction(
                    coordinatorLayoutContainer, textToPresent, Snackbar.LENGTH_LONG, getString(R.string.undo_text),
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            if (adapter.getDataSize() == 0) {
                                List<GMFeaturedProductModel> datas = new ArrayList<>();
                                for (int i = 0; i < gmTemporaryDelete.size(); i++) {
                                    datas.add(gmTemporaryDelete.get(i).getModel2());
                                }
                                onSearchLoaded(datas, datas.size());
                            } else {
                                for (Pair<Integer, GMFeaturedProductModel> gmFeaturedProductModelPair : gmTemporaryDelete) {
                                    adapter.addSingleDataWithPosition(gmFeaturedProductModelPair);
                                }
                            }

                        }
                    }
            ).showRetrySnackbar();
        }
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) adapter, this);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onSubmitSuccess() {
        hideDialogLoading();
        closeActivity();
    }

    @Override
    public void onSubmitError(Throwable t) {
        hideDialogLoading();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.gm_featured_product_submit_failed_title);
        builder.setMessage(R.string.gm_featured_product_submit_failed_desc);
        builder.setPositiveButton(R.string.label_try_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                submitData();
            }
        });
        builder.setNegativeButton(R.string.label_exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                closeActivity();
            }
        });
        builder.setNeutralButton(R.string.label_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void searchForPage(int page) {
        featuredProductPresenter.loadData();
    }

    @Override
    public void onSearchLoaded(@NonNull List<GMFeaturedProductModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        // Initiate gm features model for comparison purpose
        if (gmFeaturedProductModelListFromServer == null) {
            gmFeaturedProductModelListFromServer = list;
        }
    }

    @Override
    protected void showViewList(@NonNull List<GMFeaturedProductModel> list) {
        super.showViewList(list);
        getActivity().invalidateOptionsMenu();
        updateTitle();
        updateFabDisplay();
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        getActivity().invalidateOptionsMenu();
        updateTitle();
        hideFab();
    }

    @Override
    public void onLoadSearchError(Throwable t) {
        super.onLoadSearchError(t);
        getActivity().invalidateOptionsMenu();
        updateTitle();
        hideFab();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        switch (featuredProductTypeView) {
            case GMFeaturedProductTypeView.ARRANGE_DISPLAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    @GMFeaturedProductTypeView
    public int getFeaturedProductTypeView() {
        return featuredProductTypeView;
    }

    public void setFeaturedProductTypeView(@GMFeaturedProductTypeView int featuredProductTypeView) {
        this.featuredProductTypeView = featuredProductTypeView;
        getActivity().invalidateOptionsMenu();
        adapter.notifyDataSetChanged();
        updateTitle();
        updateFabDisplay();
    }

    @Override
    public void onItemChecked(GMFeaturedProductModel gmFeaturedProductModel, boolean checked) {
        if (featuredProductTypeView == GMFeaturedProductTypeView.DELETE_DISPLAY) {
            int totalChecked = ((GMFeaturedProductAdapter) adapter).getTotalChecked();
            if (getActivity() instanceof BaseSimpleActivity) {
                ((BaseSimpleActivity) getActivity()).updateTitle(String.valueOf(totalChecked), null);
            }
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void onItemClicked(GMFeaturedProductModel GMFeaturedProductModel) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE && intent != null) {
            List<GMFeaturedProductModel> gmFeaturedProductModelList = new ArrayList<>();
            List<ProductListPickerViewModel> productListPickerViewModelList = intent.getParcelableArrayListExtra(ProductListPickerConstant.EXTRA_PRODUCT_LIST_SUBMIT);
            if (productListPickerViewModelList != null) {
                for (ProductListPickerViewModel productListPickerViewModel : productListPickerViewModelList) {
                    GMFeaturedProductModel gmFeaturedProductModel = new GMFeaturedProductModel();
                    gmFeaturedProductModel.setProductId(Long.valueOf(productListPickerViewModel.getId()));
                    gmFeaturedProductModel.setImageUrl(productListPickerViewModel.getIcon());
                    gmFeaturedProductModel.setProductPrice(productListPickerViewModel.getProductPrice());
                    gmFeaturedProductModel.setProductName(productListPickerViewModel.getTitle());
                    gmFeaturedProductModelList.add(gmFeaturedProductModel);
                }
                if (isAdded() && getView() != null) {
                    Snackbar.make(getView(),
                            getString(R.string.success_add_n_featured_product_text, productListPickerViewModelList.size()),
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            }
            onSearchLoaded(gmFeaturedProductModelList, gmFeaturedProductModelList.size());
        }
    }

    private void updateFabDisplay() {
        switch (featuredProductTypeView) {
            case GMFeaturedProductTypeView.DELETE_DISPLAY:
            case GMFeaturedProductTypeView.ARRANGE_DISPLAY:
                hideFab();
                break;
            case GMFeaturedProductTypeView.DEFAULT_DISPLAY:
            default:
                if (totalItem <= 0 || totalItem >= MAX_ITEM) {
                    hideFab();
                } else {
                    showFab();
                }
        }
    }

    private void showDialogLoading() {
        if (isAdded() && progressDialog != null) {
            progressDialog.show();
        }
    }

    protected void hideDialogLoading() {
        if (isAdded() && progressDialog != null) {
            progressDialog.dismiss();
        }
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
        }
    }

    protected void moveToProductPicker() {
        List<ProductListPickerViewModel> productListPickerViewModels = new ArrayList<>();
        for (GMFeaturedProductModel gmFeaturedProductModel : adapter.getData()) {
            ProductListPickerViewModel productListPickerViewModel = new ProductListPickerViewModel();
            productListPickerViewModel.setId(gmFeaturedProductModel.getId());
            productListPickerViewModel.setProductPrice(gmFeaturedProductModel.getProductPrice());
            productListPickerViewModel.setTitle(gmFeaturedProductModel.getProductName());
            productListPickerViewModel.setImageUrl(gmFeaturedProductModel.getImageUrl());
            productListPickerViewModels.add(productListPickerViewModel);
        }
        Intent intent = ProductListPickerActivity.createIntent(getActivity(), productListPickerViewModels, false);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void updateTitle() {
        switch (featuredProductTypeView) {
            case GMFeaturedProductTypeView.DELETE_DISPLAY:
                if (getActivity() instanceof BaseSimpleActivity) {
                    ((BaseSimpleActivity) getActivity()).updateTitle(String.valueOf(((GMFeaturedProductAdapter) adapter).getTotalChecked()), null);
                }
                break;
            case GMFeaturedProductTypeView.ARRANGE_DISPLAY:
            case GMFeaturedProductTypeView.DEFAULT_DISPLAY:
            default:
                if (getActivity() instanceof BaseSimpleActivity) {
                    ((BaseSimpleActivity) getActivity()).updateTitle(getString(R.string.featured_product_title),
                            getString(R.string.gm_featured_product_subtitle_counter, adapter.getDataSize(), MAX_ITEM));
                }
        }
    }

    public void onBackPressed() {
        switch (featuredProductTypeView) {
            case GMFeaturedProductTypeView.ARRANGE_DISPLAY:
            case GMFeaturedProductTypeView.DELETE_DISPLAY:
                ((GMFeaturedProductAdapter) adapter).resetCheckedItemSet();
                setFeaturedProductTypeView(GMFeaturedProductTypeView.DEFAULT_DISPLAY);
                break;
            case GMFeaturedProductTypeView.DEFAULT_DISPLAY:
            default:
                if (isFeaturedProductListChanged(gmFeaturedProductModelListFromServer, adapter.getData())) {
                    submitData();
                } else {
                    closeActivity();
                }
        }
    }

    private void submitData() {
        showDialogLoading();
        featuredProductPresenter.postData(GMFeaturedProductSubmitUseCase.createParam(adapter.getData()));
    }

    private void closeActivity() {
        if (isAdded()) {
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        switch (featuredProductTypeView) {
            case GMFeaturedProductTypeView.DELETE_DISPLAY:
                if (((GMFeaturedProductAdapter) adapter).getTotalChecked() > 0) {
                    inflater.inflate(R.menu.menu_gm_featured_product_delete_mode, menu);
                }
                break;
            case GMFeaturedProductTypeView.DEFAULT_DISPLAY:
                if (totalItem > 0) {
                    inflater.inflate(R.menu.menu_gm_featured_product, menu);
                }
                break;
            case GMFeaturedProductTypeView.ARRANGE_DISPLAY:
                inflater.inflate(R.menu.menu_gm_featured_product_arrange_mode, menu);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_arrange_mode) {
            setFeaturedProductTypeView(GMFeaturedProductTypeView.ARRANGE_DISPLAY);
            return true;
        } else if (item.getItemId() == R.id.menu_delete_mode) {
            ((GMFeaturedProductAdapter) adapter).resetCheckedItemSet();
            setFeaturedProductTypeView(GMFeaturedProductTypeView.DELETE_DISPLAY);
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            if (((GMFeaturedProductAdapter) adapter).getTotalChecked() == 0) {
                reloadAfterDeleteAction();
            } else {
                showDeleteDialog();
            }
            return true;
        } else if (item.getItemId() == R.id.menu_done) {
            if (isFeaturedProductListChanged(gmFeaturedProductModelListFromServer, adapter.getData())) {
                UnifyTracking.eventSortFeaturedProductChange(getActivity());
            } else {
                UnifyTracking.eventSortFeaturedProductNotChange(getActivity());
            }
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setMessage(getString(R.string.gm_featured_product_delete_desc, ((GMFeaturedProductAdapter) adapter).getTotalChecked()));
        builder.setPositiveButton(R.string.label_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UnifyTracking.eventDeleteFeaturedProduct(getActivity());
                gmTemporaryDelete = ((GMFeaturedProductAdapter) adapter).deleteCheckedItem();
                showSnackbarWithUndo();
                reloadAfterDeleteAction();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void reloadAfterDeleteAction() {
        setFeaturedProductTypeView(GMFeaturedProductTypeView.DEFAULT_DISPLAY);
        List<GMFeaturedProductModel> gmFeaturedProductModelListTemp = new ArrayList<GMFeaturedProductModel>(adapter.getData());
        onSearchLoaded(gmFeaturedProductModelListTemp, gmFeaturedProductModelListTemp.size());
    }

    public void showFab() {
        fab.show();
    }

    public void hideFab() {
        fab.hide();
    }

    private boolean isFeaturedProductListChanged(
            List<GMFeaturedProductModel> gmFeaturedProductModelListFromServer,
            List<GMFeaturedProductModel> gmFeaturedProductModelListLocal) {
        if (gmFeaturedProductModelListFromServer == null || gmFeaturedProductModelListLocal == null) {
            return false;
        }
        if (gmFeaturedProductModelListFromServer.size() != gmFeaturedProductModelListLocal.size()) {
            return true;
        }
        for (int i = 0; i < gmFeaturedProductModelListFromServer.size(); i++) {
            if (!gmFeaturedProductModelListFromServer.get(i).getId().equalsIgnoreCase(gmFeaturedProductModelListLocal.get(i).getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        featuredProductPresenter.detachView();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }
}