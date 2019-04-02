package com.tokopedia.checkout.view.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;

import java.util.List;

/**
 * @author Aghny A. Putra on 5/02/18
 */

@Deprecated
public class CartRemoveProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_REMOVE_ALL_CHECKBOX =
            R.layout.view_item_remove_all_checkbox;
    private static final int ITEM_CART_REMOVE_PRODUCT =
            R.layout.item_cart_remove_product;

    private static final int TOP_POSITION = 0;

    private CartRemoveProductActionListener mActionListener;
    private CheckBoxOverrideActionListener mViewHolderActionListener;

    private List<CartItemData> mCartItemModelList;

    private boolean isRemoveAll = false;

    public CartRemoveProductAdapter(CartRemoveProductActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void updateData(List<CartItemData> cartItemModels) {
        mCartItemModelList = cartItemModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(viewType, viewGroup, false);

        if (viewType == ITEM_VIEW_REMOVE_ALL_CHECKBOX) {
            return new SelectRemoveAllCheckboxViewHolder(view);
        } else {
            return new CartProductDataViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == ITEM_VIEW_REMOVE_ALL_CHECKBOX) {
            ((SelectRemoveAllCheckboxViewHolder) viewHolder).bindViewHolder();
        } else {
            int pos = position - 1;
            ((CartProductDataViewHolder) viewHolder).bindViewHolder(mCartItemModelList.get(pos), pos);
        }
    }

    @Override
    public int getItemCount() {
        return mCartItemModelList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TOP_POSITION) {
            return ITEM_VIEW_REMOVE_ALL_CHECKBOX;
        } else {
            return ITEM_CART_REMOVE_PRODUCT;
        }
    }

    class SelectRemoveAllCheckboxViewHolder extends RecyclerView.ViewHolder
            implements CheckBoxOverrideActionListener {

        private CheckBox mCbRemoveAll;

        SelectRemoveAllCheckboxViewHolder(View itemView) {
            super(itemView);

            mCbRemoveAll = itemView.findViewById(R.id.checkBox);
            mViewHolderActionListener = this;
        }

        void bindViewHolder() {
            mCbRemoveAll.setChecked(isRemoveAll);
            itemView.setOnClickListener(checkBoxClickedListener());
            mCbRemoveAll.setOnClickListener(checkBoxClickedListener());
        }

        private View.OnClickListener checkBoxClickedListener() {
            return view -> {
                isRemoveAll = !isRemoveAll;
                if (isRemoveAll) mActionListener.sendAnalyticsOnCheckBoxAllSelected();
                notifyDataSetChanged();
                mActionListener.onAllItemCheckChanged(isRemoveAll);
            };
        }

        /**
         * Executed when checkbox is clicked
         *
         * @param checked  state of checkbox
         * @param position index of list where the checkbox is clicked
         */
        @Override
        public void onCheckBoxClickedListener(boolean checked, int position) {
            if (isRemoveAll && !checked) {
                isRemoveAll = false;
                mCbRemoveAll.setChecked(false);
            }
        }
    }

    public void checkAllItem(boolean checked) {
        isRemoveAll = checked;
        notifyItemChanged(0);
    }

    /**
     * Implemented by container fragment which will receive events and data from adapter
     */
    public interface CartRemoveProductActionListener {

        /**
         * Executed when state of checkbox is changed
         *
         * @param checked  state of checkbox
         * @param position index of list where the checkbox state is changed
         */
        void onCheckBoxStateChanged(boolean checked, int position);

        void sendAnalyticsOnCheckBoxAllSelected();

        void sendAnalyticsOnCheckBoxSelected();

        void onAllItemCheckChanged(boolean checked);
    }

    /**
     * Implemented by another view holder
     */
    interface CheckBoxOverrideActionListener {

        /**
         * Executed when checkbox is clicked
         *
         * @param checked  state of checkbox
         * @param position index of list where the checkbox is clicked
         */
        void onCheckBoxClickedListener(boolean checked, int position);

    }

    class CartProductDataViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCbRemoveProduct;
        private TextView mTvSenderName;
        private ImageView mIvProductImage;
        private TextView mTvProductName;
        private TextView mTvProductPrice;
        private TextView mTvTotalProductItem;
        private TextView mTvCashback;
        private TextView mTvPreOrder;
        private ImageView mIvFreeReturnIcon;

        private boolean isChecked = false;

        CartProductDataViewHolder(View itemView) {
            super(itemView);

            mCbRemoveProduct = itemView.findViewById(R.id.cb_remove_product);
            mTvSenderName = itemView.findViewById(R.id.tv_shop_name);
            mIvProductImage = itemView.findViewById(R.id.iv_product_image);
            mTvProductName = itemView.findViewById(R.id.tv_product_name);
            mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
            mTvTotalProductItem = itemView.findViewById(R.id.tv_product_total_item);
            mTvCashback = itemView.findViewById(R.id.tv_cashback);
            mTvPreOrder = itemView.findViewById(R.id.tv_pre_order);
            mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        }

        void bindViewHolder(CartItemData cartItemModel, int position) {
            isChecked = isRemoveAll;

            CartItemData.OriginData originData = cartItemModel.getOriginData();
            CartItemData.UpdatedData updatedData = cartItemModel.getUpdatedData();

            mCbRemoveProduct.setChecked(isChecked);
            itemView.setOnClickListener(checkBoxClickedListener(position));
            mCbRemoveProduct.setOnClickListener(checkBoxClickedListener(position));
            mCbRemoveProduct.setOnCheckedChangeListener(onChangeStateListener(position));

            mTvProductName.setText(originData.getProductName());
            mTvProductPrice.setText(originData.getPriceFormatted());
            mTvTotalProductItem.setText(String.valueOf(updatedData.getQuantity()));
            ImageHandler.LoadImage(mIvProductImage, originData.getProductImage());

            if (cartItemModel.getOriginData().isFreeReturn()) {
                mIvFreeReturnIcon.setVisibility(View.VISIBLE);
            } else {
                mIvFreeReturnIcon.setVisibility(View.GONE);
            }

            if (cartItemModel.getOriginData().isPreOrder()) {
                mTvPreOrder.setText(cartItemModel.getOriginData().getPreOrderInfo());
                mTvPreOrder.setVisibility(View.VISIBLE);
            } else {
                mTvPreOrder.setVisibility(View.GONE);
            }

            if (cartItemModel.getOriginData().isCashBack()) {
                mTvCashback.setText(cartItemModel.getOriginData().getCashBackInfo());
                mTvCashback.setVisibility(View.VISIBLE);
            } else {
                mTvCashback.setVisibility(View.GONE);
            }
        }

        private CompoundButton.OnCheckedChangeListener onChangeStateListener(final int position) {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    mActionListener.onCheckBoxStateChanged(checked, position);
                    if (checked) mActionListener.sendAnalyticsOnCheckBoxSelected();
                }
            };
        }

        private View.OnClickListener checkBoxClickedListener(final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isChecked = !isChecked;
                    mCbRemoveProduct.setChecked(isChecked);
                    mViewHolderActionListener.onCheckBoxClickedListener(isChecked, position);
                }
            };
        }

    }

}
