package com.tokopedia.checkout.view.feature.removecartitem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.view.common.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartRemoveProductComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartRemoveProductComponent;
import com.tokopedia.checkout.view.di.module.CartRemoveProductModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartProductHeaderViewModel;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 24/05/18.
 */
@Deprecated
public class RemoveCartItemFragment extends BaseCheckoutFragment
        implements RemoveCartItemContract.View, RemoveCartItemViewListener {

    private static final String ARG_EXTRA_CART_DATA_LIST = "ARG_EXTRA_CART_DATA_LIST";
    private static final int TOASTER_DURATION = 3000;

    private TextView tvRemoveProduct;
    private RecyclerView rvCartRemoveProduct;
    private ProgressDialog progressDialog;

    @Inject
    RemoveCartItemAdapter removeCartItemAdapter;
    @Inject
    RemoveCartItemPresenter removeCartItemPresenter;
    @Inject
    CheckoutAnalyticsCart cartPageAnalytics;

    public static RemoveCartItemFragment newInstance(List<CartItemData> cartItemDataList) {
        RemoveCartItemFragment fragment = new RemoveCartItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_EXTRA_CART_DATA_LIST, (ArrayList<? extends Parcelable>) cartItemDataList);
        fragment.setArguments(bundle);

        return fragment;
    }

    public CheckoutAnalyticsCart getCheckoutAnalyticsCart() {
        return cartPageAnalytics;
    }

    @Override
    protected void initInjector() {
        CartRemoveProductComponent component = DaggerCartRemoveProductComponent.builder()
                .cartComponent(getComponent(CartComponent.class))
                .cartRemoveProductModule(new CartRemoveProductModule())
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .build();
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeCartItemPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        removeCartItemPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

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
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        if (arguments != null) {
            List<CartItemData> cartItemDataList = arguments.getParcelableArrayList(ARG_EXTRA_CART_DATA_LIST);
            removeCartItemPresenter.setCartProductItemViewModelList(cartItemDataList);
            removeCartItemPresenter.setCartProductHeaderViewModel(new CartProductHeaderViewModel());
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_remove_product;
    }

    @Override
    protected void initView(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        progressDialog.setCancelable(false);
        tvRemoveProduct = view.findViewById(R.id.tv_remove_product);
        rvCartRemoveProduct = view.findViewById(R.id.rv_cart_remove_product);
        ((SimpleItemAnimator) rvCartRemoveProduct.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void setViewListener() {
        removeCartItemAdapter.setRemoveCartItemViewListener(this);

        rvCartRemoveProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCartRemoveProduct.setAdapter(removeCartItemAdapter);
        rvCartRemoveProduct.addItemDecoration(new CartItemDecoration());

        removeCartItemAdapter.addCartProductHeaderViewModel(removeCartItemPresenter.getCartProductHeaderViewModel());
        removeCartItemAdapter.addCartProductItemViewModel(removeCartItemPresenter.getCartProductItemViewModelList());

        tvRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removeCartItemAdapter.getCheckedItemCount() > 0) {
                    cartPageAnalytics.eventClickAtcCartClickHapusFromHapus();
                    showDeleteCartItemDialog(removeCartItemAdapter.getCheckedItemCount());
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        if (getView() != null) {
            ToasterError.make(getView(), message, TOASTER_DURATION)
                    .setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();
        }
    }

    @Override
    public void renderOnFailureDeleteCart(String message) {
        showError(message);
        getActivity().onBackPressed();
    }

    @Override
    public void renderSuccessDeleteAllCart(String message) {
        if (getView() != null) {
            ToasterNormal.make(getView(), message, TOASTER_DURATION)
                    .setAction(getActivity().getString(R.string.label_action_snackbar_close), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
        getActivity().onBackPressed();
    }

    private void showDeleteCartItemDialog(int itemCount) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(getString(R.string.label_dialog_title_delete_item));
        dialog.setDesc(String.format(getString(R.string.label_dialog_message_remove_cart_multiple_item),
                String.valueOf(itemCount)));
        dialog.setBtnOk(getString(R.string.label_dialog_action_delete_and_add_to_wishlist));
        dialog.setBtnCancel(itemCount > 0 ? getString(R.string.label_dialog_action_delete) :
                getString(R.string.label_dialog_action_delete_all));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartPageAnalytics.enhanceECommerceRemoveFromCartClickHapusDanTambahWishlistFromClickHapus(
                        removeCartItemPresenter.generateCartDataAnalytics(
                                removeCartItemAdapter.getCheckedCartIds(), EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
                removeCartItemPresenter.processRemoveCartItem(removeCartItemAdapter.getCheckedCartIds(), true);
                dialog.dismiss();
            }
        });
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartPageAnalytics.enhanceECommerceRemoveFromCartClickHapusFromClickHapus(
                        removeCartItemPresenter.generateCartDataAnalytics(
                                removeCartItemAdapter.getCheckedCartIds(), EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
                removeCartItemPresenter.processRemoveCartItem(removeCartItemAdapter.getCheckedCartIds(), false);
                dialog.dismiss();
            }
        });
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onSingleItemCheckChanged(boolean checked, final int position) {
        if (checked) {
            cartPageAnalytics.eventClickAtcCartClickChecklistBoxFromHapus();
        }
        if (rvCartRemoveProduct.isComputingLayout()) {
            rvCartRemoveProduct.post(new Runnable() {
                @Override
                public void run() {
                    updateSingleItem(position);
                }
            });
        } else {
            updateSingleItem(position);
        }
    }

    private void updateSingleItem(int position) {
        int checkedItemCount = removeCartItemAdapter.getCheckedItemCount();
        updateSelectedItemCount(checkedItemCount);
        if (checkedItemCount == removeCartItemAdapter.getItemCount() - 1) {
            removeCartItemAdapter.updateHeader(true);
        } else {
            removeCartItemAdapter.updateHeader(false);
        }
        removeCartItemAdapter.notifyItemChanged(position);
        removeCartItemAdapter.notifyItemChanged(0);
    }

    @Override
    public void onAllItemCheckChanged(final boolean checked) {
        if (checked) {
            cartPageAnalytics.eventClickAtcCartClickPilihSemuaFromHapus();
        }
        if (rvCartRemoveProduct.isComputingLayout()) {
            rvCartRemoveProduct.post(new Runnable() {
                @Override
                public void run() {
                    updateAllItem(checked);
                }
            });
        } else {
            updateAllItem(checked);
        }
    }

    private void updateAllItem(boolean checked) {
        removeCartItemAdapter.updateAllItem(checked);
        removeCartItemAdapter.notifyDataSetChanged();
        updateSelectedItemCount(removeCartItemAdapter.getCheckedItemCount());
    }

    private void updateSelectedItemCount(int count) {
        String buttonlabel = getString(R.string.label_delete_cart_item);
        if (count > 0) {
            buttonlabel = String.format(getString(R.string.label_delete_cart_item_formatted), count);
        }
        tvRemoveProduct.setText(buttonlabel);
        if (count > 0) {
            tvRemoveProduct.setBackgroundResource(R.drawable.bg_button_green_enabled);
            tvRemoveProduct.setTextColor(getResources().getColor(R.color.white));
            tvRemoveProduct.setEnabled(true);
        } else {
            tvRemoveProduct.setBackgroundResource(R.drawable.bg_button_disabled);
            tvRemoveProduct.setTextColor(getResources().getColor(R.color.grey_500));
            tvRemoveProduct.setEnabled(false);
        }
    }

}
