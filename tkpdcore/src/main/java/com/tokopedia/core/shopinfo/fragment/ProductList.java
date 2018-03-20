package com.tokopedia.core.shopinfo.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.discovery.model.Sort;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.goldmerchant.FeaturedProductItem;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.shopinfo.adapter.EtalaseAdapter;
import com.tokopedia.core.shopinfo.adapter.FeaturedProductAdapter;
import com.tokopedia.core.shopinfo.adapter.ShopProductListAdapter;
import com.tokopedia.core.shopinfo.facades.GetFeaturedProductRetrofit;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.facades.GetShopProductCampaignRetrofit;
import com.tokopedia.core.shopinfo.facades.GetShopProductRetrofit;
import com.tokopedia.core.shopinfo.facades.GetSortRetrofit;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseAdapterModel;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseModel;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
@Deprecated
public class ProductList extends Fragment {

    private final String CACHE_SHOP_PRODUCT = "CACHE_SHOP_PRODUCT";
    private final String TAG = ProductList.class.getSimpleName();

    private class ViewHolder {
        RecyclerView list;
        SearchView searchView;
    }

    public static String ETALASE_NAME = "etalase_name";
    public static String ETALASE_ID = "etalase_id";

    private ViewHolder holder;
    private EtalaseModel etalaseModel;
    private ProductModel productModel;
    private ArrayList<EtalaseAdapterModel> etalaseList = new ArrayList<>();
    private List<Sort> sortList = new ArrayList<>();
    private ShopProductListAdapter adapter;
    private EtalaseAdapter etalaseAdapter;
    private FeaturedProductAdapter featuredProductAdapter;
    private GetShopProductParam productShopParam;
    private String shopId;
    private String shopDomain;
    private GetShopInfoRetrofit facadeShopInfo;
    private GetShopProductRetrofit facadeShopProd;
    private GetShopProductCampaignRetrofit facadeShopProdCampaign;
    private GetSortRetrofit facadeSort;
    private GetFeaturedProductRetrofit facadeFeaturedProduct;
    public static final String ETALASE_ID_BUNDLE = "ETALASE_ID";
    public static final String EXTRA_USE_ACE = "EXTRA_USE_ACE";
    private boolean isConnectionErrorShow = false;
    private ProductListCallback callback;
    protected View rootView;

    public static ProductList newInstance(int useAce) {

        Bundle args = new Bundle();
        args.putInt(EXTRA_USE_ACE, useAce);
        ProductList fragment = new ProductList();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductList newInstance(String etalaseId) {

        Bundle args = new Bundle();
        args.putString(ETALASE_ID_BUNDLE, etalaseId);
        ProductList fragment = new ProductList();
        fragment.setArguments(args);
        return fragment;
    }

    public void setSelectedEtalase(String etalaseId) {
        if (adapter != null) {
            int etalaseIndex = indexOfEtalase(etalaseId);
            adapter.setSelectedEtalasePos(etalaseIndex);
        } else {
            initModels();
            initAdapter();
            int etalaseIndex = indexOfEtalase(etalaseId);
            if (etalaseIndex != -1) {
                adapter.setSelectedEtalasePos(etalaseIndex);
            }
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getRootViewId() == 0) {
            throw new RuntimeException("Needs layout ID");
        }

        if(rootView == null){
            rootView = inflater.inflate(getRootViewId(), container, false);
            initView();
            rootView.setTag(getHolder());
        }
        else{
            setHolder(rootView.getTag());
        }
        setListener();
        onCreateView();
        return rootView;
    }

    protected View findViewById(int id){
        return rootView.findViewById(id);
    }

    protected View getRootView(){
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initModels();
        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(), "onResume");
        initFacade();
        initInitialData();
    }

    private void initInitialData() {
        if (etalaseModel == null) {
            getEtalase();
        }
        if (productShopParam.getPage() == 1) {
            getProductNextPage();
            getFeaturedProduct();
        }
        if (sortList.isEmpty()) {
            getSortFilter();
        }
    }

    @Override
    public void onStop() {
        if (facadeShopProd != null) {
            facadeShopProd.unsubscribeGetShopProduct();
        }
        if (facadeSort != null) {
            facadeSort.unsubscribeGetSortFilter();
        }
        if (facadeFeaturedProduct != null) {
            facadeFeaturedProduct.unsubscribeGetFeaturedProduct();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadModelsFromBundle(Bundle savedInstanceState) {
        productShopParam = savedInstanceState.getParcelable("shop_param");
    }

    private void initModels() {
        productShopParam = new GetShopProductParam();
        boolean useAce = (getArguments().getInt(EXTRA_USE_ACE) == 1);
        productShopParam.setUseAce(useAce);
        productShopParam.setEtalaseId(
                getActivity().getIntent().getExtras().getString(ETALASE_ID, "etalase")
        );
        productModel = new ProductModel();
        productModel.list = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable("shop_param", productShopParam);
    }

    protected int getRootViewId() {
        return R.layout.fragment_layout_product_shop;
    }

    protected void onCreateView() {
        if (productModel.list.isEmpty() && productShopParam.getPage() > 0)
            if (!adapter.isRetry())
                setLoading();
            else
                adapter.addRetry();
    }

    protected Object getHolder() {
        return holder;
    }

    protected void setHolder(Object holder) {
        this.holder = (ViewHolder) holder;
    }

    protected void initView() {
        holder = new ViewHolder();
        holder.list = (RecyclerView) findViewById(R.id.list);
        holder.searchView = (SearchView) findViewById(R.id.search_product);
        holder.list.setLayoutManager(adapter.getLayoutManager(getActivity()));
        holder.list.setAdapter(adapter);
        adapter.setListType(productShopParam.getListState());
        adapter.setSelectedEtalasePos(productShopParam.getSelectedEtalase());
        adapter.setEtalaseAdapter(etalaseAdapter);
        adapter.setFeaturedProductAdapter(featuredProductAdapter);
        configSearchView();

        TextView searchText = (TextView) holder.searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.black_38));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attachListener(context);
    }

    /**
     * We need to implement this to support backward compatibility
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        attachListener(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }

    private void attachListener(Context context) {
        if (context instanceof ProductListCallback) {
            this.callback = (ProductListCallback) context;
        } else {
            throw new RuntimeException("Please implement ProductListCallback in the Activity");
        }
    }

    private void configSearchView() {
        holder.searchView.setSuggestionsAdapter(null);
        holder.searchView.setFocusable(false);
        holder.searchView.clearFocus();
        holder.searchView.requestFocusFromTouch();

        View searchPlate = holder.searchView.findViewById(R.id.search_plate);
        if (searchPlate != null) {
            searchPlate.setPadding(0, 0, 0, 0);
            EditText searchText = (EditText) searchPlate.findViewById(R.id.search_src_text);
            if (searchText != null) {
                searchText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                searchText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                searchText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                if (getActivity() != null) {
                                    UnifyTracking.eventDiscoverySearchShopDetail();
                                }
                                break;
                        }
                        return false;
                    }
                });
            }

            int searchIconHint = searchPlate.getContext().getResources().getIdentifier("android:id/abs__search_button", null, null);
            ImageView imageView = (ImageView) searchPlate.findViewById(searchIconHint);
            if (imageView != null) {
                ViewGroup.LayoutParams layoutParamsimage = new ViewGroup.LayoutParams(20, 20);
                imageView.setLayoutParams(layoutParamsimage);
            }

            ImageView closeButton = (ImageView) holder.searchView.findViewById(R.id.search_close_btn);

            if (closeButton != null) {
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productShopParam.setKeyword("");
                        if (!adapter.isLoading()) {
                            refreshProductList();
                        }
                        //Clear query
                        holder.searchView.setQuery("", true);
                    }
                });
            }

        }

        holder.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                productShopParam.setKeyword("");
                if (!adapter.isLoading()) {
                    refreshProductList();
                }
                return false;
            }
        });

        holder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productShopParam.setKeyword(query);
                if (!adapter.isLoading()) {
                    refreshProductList();
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.searchView.getWindowToken(), 0);
                holder.searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productShopParam.setKeyword(newText);
                return false;
            }
        });
        holder.searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
            }
        });
    }

    private void initAdapter() {
        adapter = ShopProductListAdapter.createAdapter(productModel, shopId);
        initEtalaseAdapter();
        initFeaturedProductAdapter();
    }

    private void initEtalaseAdapter() {
        initEtalaseList();
        etalaseAdapter = new EtalaseAdapter(getActivity(), etalaseList);
    }

    private void initFeaturedProductAdapter() {
        featuredProductAdapter = new FeaturedProductAdapter(new FeaturedProductAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                FeaturedProductItem productItem = featuredProductAdapter.getItem(position);
                if (productItem != null) {
                    launchProductDetail(getFeaturedProductDataToPass(productItem));
                }
            }
        });
    }

    private void initEtalaseList() {
        updateEtalaseNameList();
    }

    private void updateEtalaseNameList() {
        etalaseList.clear();
        if (etalaseModel != null) {
            int totalEtalase = etalaseModel.list.size();
            int totalOtherEtalase = etalaseModel.listOther.size();
            for (int i = 0; i < totalOtherEtalase; i++) {
                EtalaseAdapterModel etalaseAdapterModel = new EtalaseAdapterModel();
                etalaseAdapterModel.setEtalaseName(MethodChecker.fromHtml(etalaseModel.listOther.get(i).etalaseName).toString());
                etalaseAdapterModel.setEtalaseId(etalaseModel.listOther.get(i).etalaseId);
                etalaseAdapterModel.setUseAce(etalaseModel.listOther.get(i).useAce);
                etalaseAdapterModel.setEtalaseBadge(etalaseModel.listOther.get(i).etalaseBadge);
                etalaseList.add(etalaseAdapterModel);
            }
            for (int i = 0; i < totalEtalase; i++) {
                EtalaseAdapterModel etalaseAdapterModel = new EtalaseAdapterModel();
                etalaseAdapterModel.setEtalaseName(MethodChecker.fromHtml(etalaseModel.list.get(i).etalaseName).toString());
                etalaseAdapterModel.setEtalaseId(etalaseModel.list.get(i).etalaseId);
                etalaseAdapterModel.setUseAce(etalaseModel.list.get(i).useAce);
                etalaseAdapterModel.setEtalaseBadge(etalaseModel.list.get(i).etalaseBadge);
                etalaseList.add(etalaseAdapterModel);
            }
        } else {
            EtalaseAdapterModel etalaseAdapterModel = new EtalaseAdapterModel();
            etalaseAdapterModel.setEtalaseName(removeDash(
                    getActivity().getIntent().getExtras().getString(
                            ETALASE_NAME, getString(R.string.title_all_etalase))));
            etalaseList.add(etalaseAdapterModel);
            etalaseAdapterModel.setEtalaseId(getActivity().getIntent().getExtras().getString(ETALASE_ID, "etalase"));
            etalaseAdapterModel.setUseAce(1);
            etalaseList.add(etalaseAdapterModel);
        }
        if (etalaseAdapter != null) {
            etalaseAdapter.setList(etalaseList);
        }
    }

    protected void setListener() {
        holder.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isAtBottom() && canLoadItem() && !isConnectionErrorShow) {
                    setLoading();
                    getProductNextPage();
                }
            }
        });
        adapter.setListener(onAdapterHeaderListener());
        adapter.setOnRetryListener(onRetryClick());
    }

    private ShopProductListAdapter.ProductListAdapterListener onAdapterHeaderListener() {
        return new ShopProductListAdapter.ProductListAdapterListener() {
            @Override
            public void onListTypeChange() {
                productShopParam.setListState(adapter.getListType());
            }

            @Override
            public void onEtalaseClick(int pos) {
                actionChangeEtalase(pos);
            }

            @Override
            public void onFilterClick(View v) {
                if (getActivity() != null) {
                    CommonUtils.dumper("GAv4 clicked filter shops");
                }
                if (sortList.isEmpty()) {
                    facadeSort.getSort(new GetSortRetrofit.OnGetSortFilterListener() {
                        @Override
                        public void onSuccess(List<Sort> sorts) {
                            sortList = sorts;
                            showSortDialog(sortList);
                        }

                        @Override
                        public void onFailure(int connectionTypeError, String message) {
                            if (isAdded() && getActivity() != null) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    showSortDialog(sortList);
                }
            }

            @Override
            public void onProductClick(int pos) {
                launchProductDetail(getProductDataToPass(pos));
            }
        };
    }

    private void launchProductDetail(ProductPass productPass) {
        ((PdpRouter) (getActivity())
                .getApplication())
                .goToProductDetail(getActivity(), productPass);
    }

    private void actionChangeEtalase(int pos) {
        if (productShopParam.getSelectedEtalase() != pos) {
            productShopParam.setEtalaseId(etalaseList.get(pos).getEtalaseId());
            productShopParam.setSelectedEtalase(pos);
            adapter.setSelectedEtalasePos(productShopParam.getSelectedEtalase());
            if (getArguments().getInt(EXTRA_USE_ACE) == 1) {
                productShopParam.setUseAce(etalaseList.get(pos).isUseAce());
            }
            refreshProductList();
        }
    }

    private void showSortDialog(final List<Sort> sorts) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final String[] Value = getResources().getStringArray(R.array.sort_value);
        final ArrayAdapter<Sort> adapterSort = new ArrayAdapter<Sort>(getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1, sorts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                if (productShopParam.getOrderBy().equals(sorts.get(position).getValue())) {
                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.green_500));
                } else {
                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                }
                return v;
            }
        };

        alert.setAdapter(adapterSort, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Sort sort = adapterSort.getItem(which);
                productShopParam.setOrderBy(sort.getValue());
                if (!adapter.isLoading())
                    refreshProductList();
            }
        });
        alert.show();
    }

    private void initFacade() {
        facadeShopInfo = new GetShopInfoRetrofit(getActivity(), shopId, shopDomain);
        facadeShopInfo.setOnGetShopEtalase(onGetEtalaseListener());
        facadeShopProd = new GetShopProductRetrofit(getActivity(), shopId, shopDomain);
        facadeShopProd.setOnGetShopProductListener(onGetShopProductListener());
        facadeShopProdCampaign = new GetShopProductCampaignRetrofit(getActivity());
        facadeShopProdCampaign.setProductsCampaignListener(onGetProductCampaign());
        facadeFeaturedProduct = new GetFeaturedProductRetrofit(getActivity(), shopId);
        facadeFeaturedProduct.setOnGetFeaturedProductListener(onGetFeaturedProductListener());
        facadeSort = new GetSortRetrofit(getActivity());
    }

    private GetSortRetrofit.OnGetSortFilterListener onGetSortListener() {
        return new GetSortRetrofit.OnGetSortFilterListener() {
            @Override
            public void onSuccess(List<Sort> sorts) {
                sortList = sorts;
            }

            @Override
            public void onFailure(int connectionTypeError, String message) {
                Log.e(TAG, "onFailure " + message);
            }
        };
    }

    private GetShopProductRetrofit.OnGetShopProductListener onGetShopProductListener() {
        return new GetShopProductRetrofit.OnGetShopProductListener() {
            @Override
            public void onSuccess(ProductModel model) {
                if (callback.isOfficialStore() && !model.list.isEmpty()) {
                    getProductCampaign(model);
                } else {
                    renderProductList(model);
                }
            }

            @Override
            public void onFailure(int connectionTypeError, String message) {
                removeLoading();
                switch (connectionTypeError) {
                    case GetShopProductRetrofit.CONNECTION_TYPE_ERROR:
                        if (productShopParam.getPage() == 1 && productModel.list.size() == 0) {
                            isConnectionErrorShow = true;
                            adapter.showEmptyState(message, new ShopProductListAdapter.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    adapter.removeEmptyState();
                                    isConnectionErrorShow = false;
                                    refreshProductList();
                                }
                            });
                        } else {
                            if (getActivity() != null && isAdded()) {
                                NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        refreshProductList();
                                    }
                                }).showRetrySnackbar();
                            }
                        }
                        break;
                    case GetShopProductRetrofit.WS_TYPE_ERROR:
                        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                refreshProductList();
                            }
                        }).showRetrySnackbar();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private GetFeaturedProductRetrofit.OnGetFeaturedProductListener onGetFeaturedProductListener() {
        return new GetFeaturedProductRetrofit.OnGetFeaturedProductListener() {
            @Override
            public void onSuccess(List<FeaturedProductItem> featuredProductItemList) {
                featuredProductAdapter.setDataList(featuredProductItemList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int connectionTypeError, String message) {
                NetworkErrorHelper.showSnackbar(getActivity(), message);
            }
        };
    }

    private void saveToCache(ProductModel product) {
        try {
            GlobalCacheManager cache = new GlobalCacheManager();
            cache.delete(CACHE_SHOP_PRODUCT);

            cache.setKey(CACHE_SHOP_PRODUCT);
            cache.setValue(CacheUtil.convertModelToString(
                    product,
                    new TypeToken<ProductModel>() {
                    }.getType()
            ));
            cache.store();
            if (callback != null) callback.onProductListCompleted();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private GetShopInfoRetrofit.OnGetShopEtalase onGetEtalaseListener() {
        return new GetShopInfoRetrofit.OnGetShopEtalase() {
            @Override
            public void onSuccess(EtalaseModel model) {
                etalaseModel = model;
                updateEtalaseNameList();
                int index = -1;
                if (getArguments().getString(ETALASE_ID_BUNDLE) != null) {
                    index = indexOfEtalase(getArguments().getString(ETALASE_ID_BUNDLE));
                } else if (getActivity().getIntent().getExtras().getString(ETALASE_NAME) != null) {
                    for (int i = 0; i < etalaseList.size(); i++) {
                        if (etalaseList.get(i).getEtalaseName().equalsIgnoreCase(
                                removeDash(getActivity().getIntent().getExtras().getString(ETALASE_NAME))
                        )) {
                            index = i;
                            break;
                        }
                    }
                } else if (getActivity().getIntent().getExtras() != null &&
                        !TextUtils.isEmpty(getActivity().getIntent().getExtras().getString(ETALASE_ID, ""))){
                    String etalaseId = getActivity().getIntent().getExtras().getString(ETALASE_ID);
                    for (int i = 0; i < etalaseList.size(); i++) {
                        if (etalaseList.get(i).getEtalaseId().equalsIgnoreCase(etalaseId)) {
                            index = i;
                            break;
                        }
                    }
                } else {
                    String etalaseId = productShopParam.getEtalaseId();
                    for (int i = 0; i < etalaseList.size(); i++) {
                        if (etalaseList.get(i).getEtalaseId().equalsIgnoreCase(etalaseId)) {
                            index = i;
                            break;
                        }
                    }
                }
                adapter.setSelectedEtalasePos(index);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
            }
        };
    }

    private String removeDash(String param) {
        return param.replace("-", " ");
    }

    private GetShopProductCampaignRetrofit.ProductsCampaignListener onGetProductCampaign() {
        return new GetShopProductCampaignRetrofit.ProductsCampaignListener() {
            @Override
            public void onSuccess(ProductModel model) {
                renderProductList(model);
            }

            @Override
            public void onFailure(int connectionTypeError, String message) {
                removeLoading();
                switch (connectionTypeError) {
                    case GetShopProductRetrofit.CONNECTION_TYPE_ERROR:
                        if (productShopParam.getPage() == 1 && productModel.list != null
                                && productModel.list.isEmpty()) {

                            adapter.showEmptyState(message, new ShopProductListAdapter.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    adapter.removeEmptyState();
                                    refreshProductList();
                                }
                            });
                        } else {
                            NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    refreshProductList();
                                }
                            }).showRetrySnackbar();
                        }
                        break;
                    case GetShopProductRetrofit.WS_TYPE_ERROR:
                        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                refreshProductList();
                            }
                        }).showRetrySnackbar();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void getProductCampaign(ProductModel model) {
        facadeShopProdCampaign.unsubscribeGetProductsCampaign();
        facadeShopProdCampaign.getProductsCampaign(model);
    }

    private void renderProductList(ProductModel model) {
        removeLoading();
        productModel.list.addAll(model.list);
        adapter.notifyDataSetChanged();
        if (!model.list.isEmpty())
            productShopParam.setPage(productShopParam.getPage() + 1);
        else
            productShopParam.setPage(-1);

        if (productShopParam.getPage() == 2
                && productShopParam.getEtalaseId().equalsIgnoreCase("etalase")) {
            saveToCache(model);
        }
    }

    private View.OnClickListener onRetryClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeRetry();
                adapter.addLoading();
                getProductNextPage();
            }
        };
    }

    private void getEtalase() {
        facadeShopInfo.getShopEtalase();
    }

    private void getSortFilter() {
        facadeSort.getSort(onGetSortListener());
    }

    private boolean isAtBottom() {
        int visibleItem = holder.list.getLayoutManager().getChildCount();
        int totalItem = holder.list.getLayoutManager().getItemCount();
        int pastVisibleItem = ((GridLayoutManager) holder.list.getLayoutManager()).findFirstVisibleItemPosition();
        return ((visibleItem + pastVisibleItem) >= totalItem);
    }

    private boolean canLoadItem() {
        return !adapter.isEmptyState() && !adapter.isLoading() && productShopParam.getPage() > 0;
    }

    private void refreshProductList() {
        productModel.list.clear();
        adapter.notifyDataSetChanged();
        adapter.addLoading();
        productShopParam.setPage(1);
        facadeShopProd.unsubscribeGetShopProduct();
        facadeShopProd.getShopProduct(productShopParam);
    }

    public void refreshProductListFromOffStore() {
        if (productModel != null && productModel.list != null) {
            productModel.list.clear();
            GetShopProductParam newProductParam = new GetShopProductParam();
            if (etalaseList.size() > 1) {
                int selectedEtalase = indexOfEtalase(getString(R.string.title_all_etalase));
                newProductParam.setSelectedEtalase(selectedEtalase == -1 ? 0 : selectedEtalase);
            } else {
                newProductParam.setSelectedEtalase(0);
            }
            newProductParam.setListState(productShopParam.getListState());
            newProductParam.setUseAce(productShopParam.isUseAce());
            productShopParam = newProductParam;
            adapter.setListType(productShopParam.getListState());
            adapter.setSelectedEtalasePos(productShopParam.getSelectedEtalase());
            adapter.notifyDataSetChanged();
            adapter.addLoading();
            facadeShopProd.unsubscribeGetShopProduct();
            facadeShopProd.getShopProduct(productShopParam);
        }
    }

    public void refreshProductList(GetShopProductParam getShopProductParam) {
        if (adapter != null) {
            int etalaseIndex = indexOfEtalase(getShopProductParam.getEtalaseId());
            if (etalaseIndex != -1) {
                adapter.setSelectedEtalasePos(etalaseIndex);
            }
            this.productShopParam = getShopProductParam;
            productModel.list.clear();
            adapter.notifyDataSetChanged();
            adapter.addLoading();
            facadeShopProd.unsubscribeGetShopProduct();
            facadeShopProd.getShopProduct(getShopProductParam);
        }
    }

    public void refreshProductListByKeyword(String keyword) {
        holder.searchView.setQuery(keyword, false);
        this.productShopParam.setKeyword(keyword);
        refreshProductList(this.productShopParam);
    }

    private void getProductNextPage() {
        facadeShopProd.unsubscribeGetShopProduct();
        facadeShopProd.getShopProduct(productShopParam);
    }

    private void getFeaturedProduct() {
        facadeFeaturedProduct.unsubscribeGetFeaturedProduct();
        facadeFeaturedProduct.getFeaturedProduct();
    }

    private void setLoading() {
        adapter.addLoading();
    }

    private void removeLoading() {
        adapter.removeLoading();
    }

    private ProductPass getProductDataToPass(int position) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(productModel.list.get(position).productPrice)
                .setProductId(productModel.list.get(position).productId)
                .setProductName(productModel.list.get(position).productName)
                .setProductImage(productModel.list.get(position).productImage)
                .build();
    }

    private ProductPass getFeaturedProductDataToPass(FeaturedProductItem featuredProductItem) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(featuredProductItem.getPrice())
                .setProductId(featuredProductItem.getProductId())
                .setProductName(featuredProductItem.getName())
                .setProductImage(featuredProductItem.getImageUri())
                .build();
    }

    public interface ProductListCallback {
        void onProductListCompleted();

        boolean isOfficialStore();
    }

    private int indexOfEtalase(String etalaseId) {
        for (EtalaseAdapterModel model : etalaseList) {
            if (model.getEtalaseId().equals(etalaseId)) {
                return etalaseList.indexOf(model);
            }
        }
        return -1;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }
}
