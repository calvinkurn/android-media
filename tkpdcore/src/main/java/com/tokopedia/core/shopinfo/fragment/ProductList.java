package com.tokopedia.core.shopinfo.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.adapter.ShopProductListAdapter;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.facades.GetShopProductRetrofit;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseModel;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
public class ProductList extends V2BaseFragment {

    private boolean mHasFocus;

    private class ViewHolder {
        RecyclerView list;
        SearchView searchView;
    }


    public static String ETALASE_NAME = "etalase_name";
    public static String ETALASE_ID = "etalase_id";

    private ViewHolder holder;
    private EtalaseModel etalaseModel;
    private ProductModel productModel;
    private List<String> etalaseNameList = new ArrayList<>();
    private List<String> etalaseIdList = new ArrayList<>();
    private ShopProductListAdapter adapter;
    private SimpleSpinnerAdapter etalaseAdapter;
    private GetShopProductParam productShopParam;
    private String shopId;
    private String shopDomain;
    private GetShopInfoRetrofit facadeShopInfo;
    private GetShopProductRetrofit facadeShopProd;
    public static final String ETALASE_ID_BUNDLE = "ETALASE_ID";

    public static ProductList newInstance() {

        Bundle args = new Bundle();

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
            int etalaseIndex = etalaseIdList.indexOf(etalaseId);
            adapter.setSelectedEtalasePos(etalaseIndex);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_ID, "");
        shopDomain = getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_DOMAIN, "");

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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadModelsFromBundle(Bundle savedInstanceState) {
        productShopParam = savedInstanceState.getParcelable("shop_param");
//        etalaseList = savedInstanceState.getParcelableArrayList("etalase"); TODO ganti orientasi
//        prodList = savedInstanceState.getParcelableArrayList("product_list");
    }

    private void initModels() {
        productShopParam = new GetShopProductParam();
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
//        outState.putParcelableArrayList("etalase", etalaseList); TODO save orientasi
//        outState.putParcelableArrayList("product_list", prodList);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_layout_product_shop;
    }

    @Override
    protected void onCreateView() {
        if (productModel.list.isEmpty() && productShopParam.getPage() > 0)
            if (!adapter.isRetry())
                setLoading();
            else
                adapter.addRetry();
    }

    @Override
    protected Object getHolder() {
        return holder;
    }

    @Override
    protected void setHolder(Object holder) {
        this.holder = (ViewHolder) holder;
    }

    @Override
    protected void initView() {
        holder = new ViewHolder();
        holder.list = (RecyclerView) findViewById(R.id.list);
        holder.searchView = (SearchView) findViewById(R.id.search_product);
        holder.list.setLayoutManager(adapter.getLayoutManager(getActivity()));
        holder.list.setAdapter(adapter);
        adapter.setListType(productShopParam.getListState());
        adapter.setSelectedEtalasePos(productShopParam.getSelectedEtalase());
        adapter.setEtalaseAdapter(etalaseAdapter);
        configSearchView();
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
                                if (getActivity() != null && getActivity() instanceof ShopInfoActivity) {
                                    ((ShopInfoActivity) getActivity()).setToolbarCollapse();
                                }
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
                if (getActivity() != null && getActivity() instanceof ShopInfoActivity) {
                    ((ShopInfoActivity) getActivity()).setToolbarCollapse();
                }
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
                    if (getActivity() != null && getActivity() instanceof ShopInfoActivity) {
                        ((ShopInfoActivity) getActivity()).setToolbarCollapse();
                    }
                }
            }
        });
    }

    private void initAdapter() {
        adapter = ShopProductListAdapter.createAdapter(productModel);
        initEtalaseAdapter();
    }

    private void initEtalaseAdapter() {
        initEtalaseList();
        etalaseAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), etalaseNameList);
    }

    private void initEtalaseList() {
        updateEtalaseNameList();
    }

    private void updateEtalaseNameList() {
        etalaseNameList.clear();
        etalaseIdList.clear();
        if (etalaseModel != null) {
            int totalEtalase = etalaseModel.list.size();
            int totalOtherEtalase = etalaseModel.listOther.size();
            for (int i = 0; i < totalOtherEtalase; i++) {
                etalaseNameList.add(MethodChecker.fromHtml(etalaseModel.listOther.get(i).etalaseName).toString());
                etalaseIdList.add(etalaseModel.listOther.get(i).etalaseId);
            }
            for (int i = 0; i < totalEtalase; i++) {
                etalaseNameList.add(MethodChecker.fromHtml(etalaseModel.list.get(i).etalaseName).toString());
                etalaseIdList.add(etalaseModel.list.get(i).etalaseId);
            }
        } else {
            etalaseNameList.add(getActivity().getIntent().getExtras().getString(ETALASE_NAME, getString(R.string.title_all_etalase)));
            etalaseIdList.add(getActivity().getIntent().getExtras().getString(ETALASE_ID, "etalase"));
        }
        String selectedId = getArguments().getString(ETALASE_ID_BUNDLE);
        if (selectedId != null) {
            int index = etalaseIdList.indexOf(selectedId);

        }

    }

    @Override
    protected void setListener() {
        holder.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isAtBottom() && canLoadItem()) {
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
                showSortDialog();
            }

            @Override
            public void onProductClick(int pos) {
                getActivity().startActivity(
                        ProductDetailRouter.createInstanceProductDetailInfoActivity(
                                getActivity(), getProductDataToPass(pos)
                        )
                );
            }
        };
    }

    private void actionChangeEtalase(int pos) {
        if (productShopParam.getSelectedEtalase() != pos) {
            productShopParam.setEtalaseId(etalaseIdList.get(pos));
            productShopParam.setSelectedEtalase(pos);
            refreshProductList();
        }
    }

    private void showSortDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final String[] Value = getResources().getStringArray(R.array.sort_value);
        ArrayAdapter<CharSequence> adapterSort = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1, getResources().getStringArray(R.array.sort_option)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView textView = (TextView) v.findViewById(android.R.id.text1);

                try {
                    if (Integer.parseInt(productShopParam.getOrderBy()) - 2 == position) {
                        textView.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.green_500));
                    } else {
                        textView.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.black));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return v;
            }
        };

        alert.setAdapter(adapterSort, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                productShopParam.setOrderBy(Value[which + 1]);
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
    }

    private GetShopProductRetrofit.OnGetShopProductListener onGetShopProductListener() {
        return new GetShopProductRetrofit.OnGetShopProductListener() {
            @Override
            public void onSuccess(ProductModel model) {
                removeLoading();
                productModel.list.addAll(model.list);
                adapter.notifyDataSetChanged();
                if (!model.list.isEmpty())
                    productShopParam.setPage(productShopParam.getPage() + 1);
                else
                    productShopParam.setPage(-1);
            }

            @Override
            public void onFailure(int connectionTypeError, String message) {
                removeLoading();
                switch (connectionTypeError) {
                    case GetShopProductRetrofit.CONNECTION_TYPE_ERROR:
                        if (productShopParam.getPage() == 1 && productModel.list.size() == 0) {

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

    private GetShopInfoRetrofit.OnGetShopEtalase onGetEtalaseListener() {
        return new GetShopInfoRetrofit.OnGetShopEtalase() {
            @Override
            public void onSuccess(EtalaseModel model) {
                etalaseModel = model;
                updateEtalaseNameList();
                if (getArguments().getString(ETALASE_ID_BUNDLE) != null) {
                    productShopParam.setSelectedEtalase(etalaseIdList.indexOf(getArguments().getString(ETALASE_ID_BUNDLE)));
                } else {
                    productShopParam.setSelectedEtalase(etalaseNameList.indexOf(getActivity().getIntent().getExtras().getString(ETALASE_NAME, getString(R.string.title_all_etalase))));
                }
                adapter.setSelectedEtalasePos(productShopParam.getSelectedEtalase());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
            }
        };
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

    private boolean isAtBottom() {
        int visibleItem = holder.list.getLayoutManager().getChildCount();
        int totalItem = holder.list.getLayoutManager().getItemCount();
        int pastVisibleItem = ((GridLayoutManager) holder.list.getLayoutManager()).findFirstVisibleItemPosition();
        return ((visibleItem + pastVisibleItem) >= totalItem);
    }

    private boolean canLoadItem() {
        return !adapter.isLoading() && productShopParam.getPage() > 0;
    }

    private void refreshProductList() {
        productModel.list.clear();
        adapter.notifyDataSetChanged();
        adapter.addLoading();
        productShopParam.setPage(1);
        facadeShopProd.getShopProduct(productShopParam);
    }

    public void refreshProductList(GetShopProductParam getShopProductParam) {
        if (adapter != null) {
            int etalaseIndex = etalaseIdList.indexOf(getShopProductParam.getEtalaseId());
            if (etalaseIndex != -1) {
                adapter.setSelectedEtalasePos(etalaseIndex);
            }
        }
        this.productShopParam = getShopProductParam;
        productModel.list.clear();
        adapter.notifyDataSetChanged();
        adapter.addLoading();
        facadeShopProd.getShopProduct(getShopProductParam);
    }

    private void getProductNextPage() {
        facadeShopProd.getShopProduct(productShopParam);
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
}
