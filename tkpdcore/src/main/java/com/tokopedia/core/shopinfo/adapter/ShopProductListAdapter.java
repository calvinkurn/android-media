package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseAdapterModel;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
public class ShopProductListAdapter extends RecyclerView.Adapter {

    private RetryClickedListener retryClickedListener;
    private String shopId;

    public interface ProductListAdapterListener {
        void onListTypeChange();

        void onEtalaseClick(int pos);

        void onFilterClick(View v);

        void onProductClick(int pos);
    }

    public interface RetryClickedListener {
        void onRetryClicked();
    }

    static final int TYPE_LARGE = 6;
    static final int TYPE_MEDIUM = 3;
    static final int TYPE_LIST = 9;
    static final int TYPE_LOADING = 7;
    static final int TYPE_NO_RESULT = 8;
    static final int TYPE_HEADER = 10;
    static final int TYPE_RETRY = 11;
    static final int SPAN_COUNT = 6;
    static final int EMPTY_STATE = 12;

    private int TYPE = TYPE_MEDIUM;
    private int EXTRA_TYPE = 0;

    ProductSmallDelegate small;
    ProductLargeDelegate grid;
    ProductListDelegate list;
    ExtrasDelegate extras;
    ShopProductListHeaderDelegate header;

    GridLayoutManager gridLayoutManager;
    ProductListAdapterListener listener;
    View.OnClickListener onRetryListener;

    ProductModel productModel;
    String messageError;

    public void setListener(ProductListAdapterListener listener) {
        this.listener = listener;
    }

    private ShopProductListAdapter(ProductModel productModel, String shopId) {
        this.shopId = shopId;
        small = new ProductSmallDelegate();
        grid = new ProductLargeDelegate();
        list = new ProductListDelegate();
        extras = new ExtrasDelegate(shopId);
        header = new ShopProductListHeaderDelegate();
        this.productModel = productModel;
    }

    public void setEtalaseAdapter(ArrayAdapter<EtalaseAdapterModel> etalaseAdapter) {
        header.setEtalaseAdapter(etalaseAdapter);
    }

    public void setFeaturedProductAdapter(FeaturedProductAdapter featuredProductAdapter) {
        header.setFeaturedProductAdapter(featuredProductAdapter);
    }

    public void setSelectedEtalasePos(int pos) {
        header.setSelectedEtalase(pos);
    }

    public GridLayoutManager getLayoutManager(Context context) {
        gridLayoutManager = new GridLayoutManager(context, SPAN_COUNT);
        setSpanSize();
        return gridLayoutManager;
    }

    public void toggleView() {
        switch (TYPE) {
            case TYPE_LARGE:
                TYPE = TYPE_LIST;
                break;
            case TYPE_MEDIUM:
                TYPE = TYPE_LARGE;
                break;
            case TYPE_LIST:
                TYPE = TYPE_MEDIUM;
        }
        setSpanSize();
        notifyDataSetChanged();
    }

    public int getListType() {
        return TYPE;
    }

    public void setListType(int type) {
        TYPE = type;
        setSpanSize();
        notifyDataSetChanged();
    }

    public void addRetry() {
        EXTRA_TYPE = TYPE_RETRY;
        setSpanSize();
        notifyDataSetChanged();
    }

    public void removeRetry(){
        removeLoading();
    }

    public void addLoading() {
        EXTRA_TYPE = TYPE_LOADING;
        setSpanSize();
        notifyDataSetChanged();
    }

    public void removeLoading() {
        EXTRA_TYPE = 0;
        setSpanSize();
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return EXTRA_TYPE == TYPE_LOADING;
    }

    public boolean isEmptyState() {
        return EXTRA_TYPE == EMPTY_STATE;
    }

    public boolean isRetry(){ return EXTRA_TYPE == TYPE_RETRY;}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LARGE:
            case TYPE_MEDIUM:
                return grid.onCreateViewHolder(parent);
            case TYPE_LIST:
                return list.onCreateViewHolder(parent);
            case TYPE_LOADING:
                return extras.onCreateViewHolderLoading(parent);
            case TYPE_NO_RESULT:
                return extras.onCreateViewHolderNoResult(parent);
            case TYPE_HEADER:
                return header.onCreateViewHolder(parent);
            case TYPE_RETRY:
                return extras.onCreateViewHolderRetry(parent);
            case EMPTY_STATE:
                return extras.onCreateViewHolderEmptyState(parent);
            default:
                return extras.onCreateViewHolderNoResult(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_LARGE:
            case TYPE_MEDIUM:
                grid.onBindViewHolder(productModel.list.get(position - 1), holder);
                grid.onItemClickListener(onProductItemClick(position - 1), holder);
                break;
            case TYPE_LIST:
                list.onBindViewHolder(productModel.list.get(position - 1), holder);
                list.onItemClickListener(onProductItemClick(position - 1), holder);
                break;
            case TYPE_HEADER:
                header.onBindViewHolder(holder, getToggleImageId());
                header.setProductListHeader(onListHeaderListener());
                break;
            case TYPE_RETRY:
                extras.onBindRetry(holder, onRetryListener);
                break;
            case EMPTY_STATE:
                extras.onBindRetryEmpty(holder, retryClickedListener, messageError);
                break;
            default:
                break;
        }
    }

    public void setOnRetryListener(View.OnClickListener listener){
        onRetryListener = listener;
    }

    private View.OnClickListener onProductItemClick(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProductClick(i);
            }
        };
    }

    private int getToggleImageId() {
        switch (TYPE) {
            case TYPE_LARGE:
                return R.drawable.ic_grid_box;
            case TYPE_MEDIUM:
                return R.drawable.ic_grid_2;
            case TYPE_LIST:
                return R.drawable.ic_grid_list;
            default:
                return R.drawable.ic_image_unavailable;
        }
    }

    private ShopProductListHeaderDelegate.ProductHeaderListListener onListHeaderListener() {
        return new ShopProductListHeaderDelegate.ProductHeaderListListener() {
            @Override
            public void onToggleView() {
                toggleView();
                listener.onListTypeChange();
            }

            @Override
            public void onEtalaseClick(int pos) {
                listener.onEtalaseClick(pos);
            }

            @Override
            public void onFilterClick(View v) {
                listener.onFilterClick(v);
            }

            @Override
            public void onSpinnerEtalaseClick() {
            }
        };
    }

    @Override
    public int getItemCount() {
        return ((totalCountWExtras() > 0) ? totalCountWExtras() : 1) + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return isItems(position) ? TYPE : getExtraType();
    }

    private int totalCountWExtras() {
        return productModel.list.size() + (isLoading() ? 1 : 0);
    }

    private boolean isItems(int position) {
        return (productModel.list.size() != 0) && (position - 1 < productModel.list.size());
    }

    private int getExtraType() {
        return (EXTRA_TYPE == 0) ? TYPE_NO_RESULT : EXTRA_TYPE;
    }

    private void setSpanSize() {
        if(gridLayoutManager!=null)
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getGridSpanSize(position);
                }
            });
    }

    private int getGridSpanSize(int position) {
        if (position == 0)
            return SPAN_COUNT;
        else
            return isItems(position) ? getTypeSpan() : SPAN_COUNT;
    }

    private int getTypeSpan() {
        return (TYPE > SPAN_COUNT) ? SPAN_COUNT : getOrientationSpan();
    }

    private int getOrientationSpan() {
        if (MainApplication.isLandscape(MainApplication.getAppContext()))
            return getLandScapeSpanSize();
        else
            return TYPE;
    }

    private int getLandScapeSpanSize() {
        switch (TYPE) {
            case TYPE_LARGE:
                return TYPE;
            case TYPE_LIST:
                return TYPE;
            case TYPE_MEDIUM:
                return TYPE_MEDIUM;
//            case TYPE_SMALL:
//                return TYPE_SMALL;
            default:
                return SPAN_COUNT;
        }
    }

    public static ShopProductListAdapter createAdapter(ProductModel lists, String shopId) {
        return new ShopProductListAdapter(lists, shopId);
    }

    public void showEmptyState(String message, RetryClickedListener retryClickedListener){
        EXTRA_TYPE = EMPTY_STATE;
        this.messageError = message;
        this.retryClickedListener = retryClickedListener;
        notifyDataSetChanged();
    }

    public void removeEmptyState(){
        EXTRA_TYPE = 0;
        notifyDataSetChanged();
    }
}
