package com.tokopedia.checkout.view.view.cartlist;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CheckedCartItemData;
import com.tokopedia.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartRemoveProductComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartRemoveProductComponent;
import com.tokopedia.checkout.view.di.module.CartRemoveProductModule;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartPage;
import com.tokopedia.transactionanalytics.EnhancedECommerceCartMapData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public class CartRemoveProductFragment extends BaseCheckoutFragment
        implements IRemoveProductListView<List<CartItemData>>,
        CartRemoveProductAdapter.CartRemoveProductActionListener {

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final String ARG_EXTRA_CART_DATA_LIST = "ARG_EXTRA_CART_DATA_LIST";

    private RecyclerView mRvCartRemoveProduct;
    private TextView mTvRemoveProduct;

    @Inject
    CartRemoveProductAdapter mCartRemoveProductAdapter;
    @Inject
    CartRemoveProductPresenter mCartRemoveProductPresenter;
    @Inject
    RecyclerView.ItemDecoration itemDecoration;
    @Inject
    CheckoutAnalyticsCartPage cartPageAnalytics;

    private List<CartItemData> mCartItemDataList = new ArrayList<>();
    private List<CheckedCartItemData> mCheckedCartItemList = new ArrayList<>();

    public static CartRemoveProductFragment newInstance(List<CartItemData> cartItemDataList) {
        CartRemoveProductFragment fragment = new CartRemoveProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_EXTRA_CART_DATA_LIST,
                (ArrayList<? extends Parcelable>) cartItemDataList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        CartRemoveProductComponent component = DaggerCartRemoveProductComponent.builder()
                .cartComponent(getComponent(CartComponent.class))
                .cartRemoveProductModule(new CartRemoveProductModule(this))
                .build();
        component.inject(this);
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
        mCartItemDataList = arguments.getParcelableArrayList(ARG_EXTRA_CART_DATA_LIST);
        if (mCartItemDataList != null) {
            for (CartItemData cartItemData : mCartItemDataList) {
                mCheckedCartItemList.add(new CheckedCartItemData(false, cartItemData));
            }
        }
    }

    /**
     * Layout xml untuk si fragment
     *
     * @return layout id
     */
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_remove_product;
    }

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    @Override
    protected void initView(View view) {
        mRvCartRemoveProduct = view.findViewById(R.id.rv_cart_remove_product);
        mTvRemoveProduct = view.findViewById(R.id.tv_remove_product);
        mTvRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartPageAnalytics.eventClickCartClickHapusFormHapus();
                removeCheckedProducts();
            }
        });

        mRvCartRemoveProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCartRemoveProduct.setAdapter(mCartRemoveProductAdapter);
        mRvCartRemoveProduct.addItemDecoration(itemDecoration);
        mCartRemoveProductPresenter.attachView(this);
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mCartRemoveProductPresenter.getCartItems(mCartItemDataList);
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    @Override
    protected void initialVar() {
        setHasOptionsMenu(true);
        getActivity().setTitle(getActivity().getString(R.string.action_delete));
        getActivity().invalidateOptionsMenu();
    }

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction();
     */
    @Override
    protected void setActionVar() {

    }

    @Override
    public void showList(List<CartItemData> cartItemDataList) {
        mCartRemoveProductAdapter.updateData(cartItemDataList);
        mCartRemoveProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListEmpty() {

    }

    @Override
    public void showError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    public void removeCheckedProducts() {
        List<CartItemData> selectedCartList = new ArrayList<>();
        List<CartItemData> unselectedCartList = new ArrayList<>();

        for (CheckedCartItemData checkedCartItemData : mCheckedCartItemList) {
            if (checkedCartItemData.isChecked()) {
                selectedCartList.add(checkedCartItemData.getCartItemData());
            } else {
                unselectedCartList.add(checkedCartItemData.getCartItemData());
            }
        }

        showDeleteCartItemDialog(selectedCartList, unselectedCartList);
    }

    @Override
    public com.tokopedia.abstraction.common.utils.TKPDMapParam<String, String> getGenerateParamAuth(
            com.tokopedia.abstraction.common.utils.TKPDMapParam<String, String> originParams
    ) {
        return originParams == null
                ? AuthUtil.generateParamsNetwork(
                getActivity(), SessionHandler.getLoginID(getActivity()),
                GCMHandler.getRegistrationId(getActivity())
        )
                : AuthUtil.generateParamsNetwork(
                getActivity(), originParams,
                SessionHandler.getLoginID(getActivity()),
                GCMHandler.getRegistrationId(getActivity()
                )
        );
    }

    @Override
    public void renderSuccessDeletePartialCart(String message) {
        performDeleteCart(message);
        getActivity().onBackPressed();
    }

    @Override
    public void renderSuccessDeleteAllCart(String message) {
        performDeleteCart(message);
        getActivity().onBackPressed();
    }

    @Override
    public void renderOnFailureDeleteCart(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public List<CartItemData> getAllCartItemList() {
        return mCartItemDataList;
    }

    /**
     * Executed when state of checkbox is changed
     *
     * @param checked  state of checkbox
     * @param position index of list where the state of checkbox is changed
     */
    @Override
    public void onCheckBoxStateChanged(boolean checked, int position) {
        mCheckedCartItemList.get(position).setChecked(checked);
        checkAllItemChecked();
    }

    private void checkAllItemChecked() {
        int selectedItemCount = 0;
        for (CheckedCartItemData checkedCartItemData : mCheckedCartItemList) {
            if (checkedCartItemData.isChecked()) {
                selectedItemCount++;
            }
        }

        String btnText = selectedItemCount == 0 ? getString(R.string.label_delete_cart_tem) :
                String.format(LOCALE_ID, getString(R.string.label_delete_cart_item_formatted), selectedItemCount);

        if (selectedItemCount == 0) {
            mTvRemoveProduct.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_button_disabled));
            mTvRemoveProduct.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey_500));
            mTvRemoveProduct.setClickable(false);
        } else {
            mTvRemoveProduct.setBackground(ContextCompat.getDrawable(getActivity(),
                    R.drawable.bg_button_green_enabled));
            mTvRemoveProduct.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            mTvRemoveProduct.setClickable(true);
        }
        mTvRemoveProduct.setText(btnText);

        final boolean isAllChecked = selectedItemCount == mCartRemoveProductAdapter.getItemCount() - 1;

        if (mRvCartRemoveProduct.isComputingLayout()) {
            mRvCartRemoveProduct.post(new Runnable() {
                @Override
                public void run() {
                    mCartRemoveProductAdapter.checkAllItem(isAllChecked);
                }
            });
        } else {
            mCartRemoveProductAdapter.checkAllItem(isAllChecked);
        }

    }

    @Override
    public void onAllItemCheckChanged(boolean checked) {
        for (CheckedCartItemData checkedCartItemData : mCheckedCartItemList) {
            checkedCartItemData.setChecked(checked);
        }
        checkAllItemChecked();
    }

    @Override
    public void onCheckBoxCheckAll() {
        cartPageAnalytics.eventClickCartClickPilihSemuaFormHapus();
    }


    private void showDeleteCartItemDialog(
            final List<CartItemData> removedCartItemList, List<CartItemData> updatedCartItemList
    ) {
        DialogFragment dialog = CartRemoveItemDialog.newInstance(
                removedCartItemList,
                updatedCartItemList,
                getCallbackActionDialogRemoveCart()
        );

        dialog.show(getFragmentManager(), CartRemoveItemDialog.class.getSimpleName());
    }


    @NonNull
    private CartRemoveItemDialog.CartItemRemoveCallbackAction getCallbackActionDialogRemoveCart() {
        return new CartRemoveItemDialog.CartItemRemoveCallbackAction() {
            @Override
            public void onDeleteSingleItemClicked(
                    CartItemData removedCartItem, List<CartItemData> updatedCartItem
            ) {
                List<CartItemData> cartItemDataList
                        = new ArrayList<>(Collections.singletonList(removedCartItem));
                mCartRemoveProductPresenter.processDeleteCart(
                        cartItemDataList, updatedCartItem, false
                );
                cartPageAnalytics.enhanceECommerceCartClickHapusFromClickHapus(
                        mCartRemoveProductPresenter.generateCartDataAnalytics(
                                removedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }

            @Override
            public void onDeleteSingleItemWithWishListClicked(
                    CartItemData removedCartItem, List<CartItemData> updatedCartItem
            ) {
                List<CartItemData> cartItemDataList
                        = new ArrayList<>(Collections.singletonList(removedCartItem));
                mCartRemoveProductPresenter.processDeleteCart(
                        cartItemDataList, updatedCartItem, true
                );
                cartPageAnalytics.enhanceECommerceCartClickHapusDanTambahWishlistFromClickHapus(
                        mCartRemoveProductPresenter.generateCartDataAnalytics(
                                removedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }

            @Override
            public void onDeleteMultipleItemClicked(
                    List<CartItemData> removedCartItem, List<CartItemData> updatedCartItem
            ) {
                mCartRemoveProductPresenter.processDeleteCart(
                        removedCartItem, updatedCartItem, false
                );
                cartPageAnalytics.enhanceECommerceCartClickHapusFromClickHapus(
                        mCartRemoveProductPresenter.generateCartDataAnalytics(
                                removedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }

            @Override
            public void onDeleteMultipleItemWithWishListClicked(
                    List<CartItemData> removedCartItem, List<CartItemData> updatedCartItem
            ) {
                mCartRemoveProductPresenter.processDeleteCart(
                        removedCartItem, updatedCartItem, true
                );
                cartPageAnalytics.enhanceECommerceCartClickHapusDanTambahWishlistFromClickHapus(
                        mCartRemoveProductPresenter.generateCartDataAnalytics(
                                removedCartItem, EnhancedECommerceCartMapData.REMOVE_ACTION
                        )
                );
            }
        };
    }

    private void performDeleteCart(String message) {
        for (CheckedCartItemData checkedCartItemData : mCheckedCartItemList) {
            if (checkedCartItemData.isChecked())
                mCartItemDataList.remove(checkedCartItemData.getCartItemData());
            mCartRemoveProductAdapter.notifyDataSetChanged();
        }
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }
}
