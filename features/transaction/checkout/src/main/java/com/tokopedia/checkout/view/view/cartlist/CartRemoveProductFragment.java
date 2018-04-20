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

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CheckedCartItemData;
import com.tokopedia.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.checkout.view.base.BaseCheckoutFragment;
import com.tokopedia.checkout.view.di.component.CartRemoveProductComponent;
import com.tokopedia.checkout.view.di.component.DaggerCartRemoveProductComponent;
import com.tokopedia.checkout.view.di.module.CartRemoveProductModule;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

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
    private static final String TAG = CartRemoveProductFragment.class.getSimpleName();
    private static final String ARG_EXTRA_CART_DATA_LIST = "ARG_EXTRA_CART_DATA_LIST";

    private RecyclerView mRvCartRemoveProduct;
    private TextView mTvRemoveProduct;

    @Inject
    CartRemoveProductAdapter mCartRemoveProductAdapter;
    @Inject
    CartRemoveProductPresenter mCartRemoveProductPresenter;
    @Inject
    RecyclerView.ItemDecoration itemDecoration;

    private int mCheckedCartItem = 0;

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
        super.initInjector();
        CartRemoveProductComponent component = DaggerCartRemoveProductComponent.builder()
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
    public void showError() {

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
    public TKPDMapParam<String, String> getGenerateParamAuth(TKPDMapParam<String, String> param) {
        return param == null ? com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity())
                : com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity(), param);
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
    public void onCheckBoxStateChangedListener(boolean checked, int position) {
        mCheckedCartItemList.get(position).setChecked(checked);
        mCheckedCartItem = checked ? mCheckedCartItem + 1 : mCheckedCartItem - 1;

        String btnText = mCheckedCartItem == 0 ? "Hapus" :
                String.format(LOCALE_ID, "Hapus (%d)", mCheckedCartItem);

        if (mCheckedCartItem == 0) {
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
    }


    private void showDeleteCartItemDialog(
            final List<CartItemData> removedCartItemList, List<CartItemData> updatedCartItemList
    ) {
        DialogFragment dialog = CartRemoveItemDialog.newInstance(
                removedCartItemList,
                updatedCartItemList,
                getCallbackActionDialogRemoveCart()
        );

        dialog.show(getFragmentManager(), "dialog");
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
            }

            @Override
            public void onDeleteMultipleItemClicked(
                    List<CartItemData> removedCartItem, List<CartItemData> updatedCartItem
            ) {
                mCartRemoveProductPresenter.processDeleteCart(
                        removedCartItem, updatedCartItem, false
                );
            }

            @Override
            public void onDeleteMultipleItemWithWishListClicked(
                    List<CartItemData> removedCartItem, List<CartItemData> updatedCartItem
            ) {
                mCartRemoveProductPresenter.processDeleteCart(
                        removedCartItem, updatedCartItem, true
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
