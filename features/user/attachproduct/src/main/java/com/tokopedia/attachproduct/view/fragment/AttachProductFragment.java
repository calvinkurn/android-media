package com.tokopedia.attachproduct.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.attachproduct.R;
import com.tokopedia.attachproduct.analytics.AttachProductAnalytics;
import com.tokopedia.attachproduct.di.DaggerAttachProductComponent;
import com.tokopedia.attachproduct.view.activity.AttachProductActivity;
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapter;
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapterTypeFactory;
import com.tokopedia.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.attachproduct.view.presenter.AttachProductPresenter;
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;
import com.tokopedia.attachproduct.view.viewholder.CheckableInteractionListenerWithPreCheckedAction;
import com.tokopedia.track.TrackApp;
import com.tokopedia.unifycomponents.SearchBarUnify;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID;
import static com.tokopedia.attachproduct.view.activity.AttachProductActivity.MAX_CHECKED_DEFAULT;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductFragment extends BaseListFragment<AttachProductItemUiModel, AttachProductListAdapterTypeFactory>
        implements CheckableInteractionListenerWithPreCheckedAction,
        AttachProductContract.View {
    private static final String IS_SELLER = "isSeller";
    private static final String SOURCE = "source";
    private static final String MAX_CHECKED = "max_checked";
    private static final String HIDDEN_PRODUCTS = "hidden_products";
    private Button sendButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchBarUnify searchBar;

    @Inject
    AttachProductPresenter presenter;

    private AttachProductContract.Activity activityContract;
    protected AttachProductListAdapter adapter;

    private boolean isSeller = false;
    private String source = "";
    private String warehouseId = "0";
    private int maxChecked = MAX_CHECKED_DEFAULT;
    private ArrayList<String> hiddenProducts = new ArrayList<>();

    public static AttachProductFragment newInstance(
            AttachProductContract.Activity checkedUIView,
            boolean isSeller, String source, int maxChecked,
            ArrayList<String> hiddenProducts, String warehouseId
    ) {
        Bundle args = new Bundle();
        args.putBoolean(IS_SELLER, isSeller);
        args.putString(SOURCE, source);
        args.putString(TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID, warehouseId);
        args.putInt(MAX_CHECKED, maxChecked);
        args.putStringArrayList(HIDDEN_PRODUCTS, hiddenProducts);
        AttachProductFragment fragment = new AttachProductFragment();
        fragment.setActivityContract(checkedUIView);
        fragment.setArguments(args);
        return fragment;
    }

    public void setActivityContract(AttachProductContract.Activity activityContract) {
        this.activityContract = activityContract;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerAttachProductComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()
        ).build().inject(this);
        presenter.attachView(this);
        presenter.attachActivityContract(activityContract);
    }

    @Override
    public int getRecyclerViewResourceId() {
        return R.id.recycler_view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initSearchBar();
        setupWarehouseId();
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            isSeller = savedInstanceState.getBoolean(IS_SELLER, false);
            source = savedInstanceState.getString(SOURCE, "");
            maxChecked = savedInstanceState.getInt(MAX_CHECKED, MAX_CHECKED_DEFAULT);
            hiddenProducts = savedInstanceState.getStringArrayList(HIDDEN_PRODUCTS);
        } else if (getArguments() != null) {
            isSeller = getArguments().getBoolean(IS_SELLER, false);
            source = getArguments().getString(SOURCE, "");
            maxChecked = getArguments().getInt(MAX_CHECKED, MAX_CHECKED_DEFAULT);
            hiddenProducts = getArguments().getStringArrayList(HIDDEN_PRODUCTS);
        }

        updateButtonBasedOnChecked(adapter.getCheckedCount());
    }

    private void setupWarehouseId() {
        if (getArguments() != null) {
            warehouseId = getArguments().getString(
                    TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID, "0"
            );
        }
    }

    private void initSearchBar() {
        searchBar = getActivity().findViewById(R.id.search_input_view);
        searchBar.getSearchBarTextField().addTextChangedListener(new TextWatcher() {
            Timer timer;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Handler mainHandler = new Handler(searchBar.getContext().getMainLooper());
                        Runnable myRunnable = () -> onSearchTextChanged(s.toString());
                        mainHandler.post(myRunnable);
                    }
                }, 300);
            }
        });
        searchBar.getSearchBarTextField().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBar.clearFocus();
                InputMethodManager in = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                onSearchSubmitted();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SELLER, isSeller);
        outState.putString(SOURCE, source);
        outState.putInt(MAX_CHECKED, maxChecked);
        outState.putStringArrayList(HIDDEN_PRODUCTS, hiddenProducts);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attach_product, container, false);
        sendButton = view.findViewById(R.id.send_button_attach_product);
        sendButton.setOnClickListener(view1 -> sendButtonClicked());
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> loadInitialData());
        updateButtonBasedOnChecked(0);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (activityContract == null) {
            if (getActivity() instanceof AttachProductContract.Activity) {
                activityContract = (AttachProductContract.Activity) getActivity();
            }
        }
    }

    private void onSearchSubmitted() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        loadInitialData();
    }

    private void onSearchTextChanged(String text) {
        if (TextUtils.isEmpty(text)) loadInitialData();
    }

    @Override
    public void onItemClicked(AttachProductItemUiModel attachProductItemUiModel) {

    }

    @Override
    protected void loadInitialData() {
        adapter.clearAllElements();
        getRecyclerViewLayoutManager().scrollToPosition(0);
        super.loadInitialData();
    }

    @Override
    public void loadData(int page) {
        if (activityContract != null
                && activityContract.getShopId() != null
                && presenter != null
                && searchBar != null) {
            presenter.loadProductData(
                    searchBar.getSearchBarTextField().getText().toString(),
                    activityContract.getShopId(),
                    page, warehouseId
            );
        } else if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public int getDefaultInitialPage() {
        return 1;
    }

    @NonNull
    @Override
    protected BaseListAdapter<AttachProductItemUiModel, AttachProductListAdapterTypeFactory> createAdapterInstance() {
        adapter = new AttachProductListAdapter(getAdapterTypeFactory());
        return adapter;
    }

    @Override
    public BaseListAdapter<AttachProductItemUiModel, AttachProductListAdapterTypeFactory> getAdapter() {
        return adapter;
    }

    @Override
    protected AttachProductListAdapterTypeFactory getAdapterTypeFactory() {
        return new AttachProductListAdapterTypeFactory(this);
    }


    @Override
    public boolean shouldAllowCheckChange(int position, boolean checked) {
        boolean isCurrentlyChecked = isChecked(position);
        boolean willCheckedStatusChanged = (isCurrentlyChecked ^ checked);
        if (adapter.getCheckedCount() >= maxChecked && (willCheckedStatusChanged && !isCurrentlyChecked)) {
            String message = getString(R.string.string_attach_product_warning_max_product_format, String.valueOf(maxChecked));
            NetworkErrorHelper.showSnackbar(getActivity(), message);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isChecked(int position) {
        return adapter.isChecked(position);
    }

    @Override
    public void updateListByCheck(boolean isChecked, int position) {
        adapter.itemChecked(isChecked, position);
        presenter.updateCheckedList(adapter.getCheckedDataList());
        if (position != RecyclerView.NO_POSITION) {
            trackAction(source, adapter.getData().get(position).getProductId());
        }
    }

    @Override
    public void addProductToList(List<AttachProductItemUiModel> products, boolean hasNextPage) {
        if (products.size() > 0) {
            sendButton.setVisibility(View.VISIBLE);
        } else {
            sendButton.setVisibility(View.VISIBLE);
        }

        removeHiddenProducts(products);
        renderList(products, hasNextPage);
    }

    @Override
    public void hideAllLoadingIndicator() {
        hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        throwable.printStackTrace();
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void updateButtonBasedOnChecked(int checkedCount) {
        sendButton.setText(getString(R.string.string_attach_product_send_button_text, String.valueOf(checkedCount), String.valueOf(maxChecked)));
        if (checkedCount > 0 && checkedCount <= maxChecked) {
            sendButton.setEnabled(true);
        } else {
            sendButton.setEnabled(false);
        }
    }

    private void sendButtonClicked() {
        presenter.completeSelection();
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyResultViewModel emptyResultViewModel = new EmptyResultViewModel();
        if (TextUtils.isEmpty(searchBar.getSearchBarTextField().getText())) {
            if (activityContract.isSeller()) {
                emptyResultViewModel.setContent(getString(R.string
                        .string_attach_product_my_empty_product));
            } else {
                emptyResultViewModel.setContent(getString(R.string.string_attach_product_empty_product));
            }
            emptyResultViewModel.setIconRes(R.drawable.bg_attach_product_empty_result);
        } else {
            emptyResultViewModel.setContent(getString(R.string.string_attach_product_search_not_found));
            emptyResultViewModel.setIconRes(R.drawable.ic_attach_product_empty_search);
        }
        if (activityContract.isSeller()) {
            emptyResultViewModel.setButtonTitleRes(R.string.string_attach_product_add_product_now);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {
                }

                @Override
                public void onEmptyButtonClicked() {
                    addProductClicked();
                }
            });
        }

        return emptyResultViewModel;
    }

    public void addProductClicked() {
        activityContract.goToAddProduct(activityContract.getShopId());
    }

    private void removeHiddenProducts(List<AttachProductItemUiModel> products) {
        if (hiddenProducts == null) {
            return;
        }

        Iterator<AttachProductItemUiModel> iterator = products.iterator();
        while (iterator.hasNext()) {
            AttachProductItemUiModel product = iterator.next();
            boolean shouldHide = false;
            for (String hiddenProduct : hiddenProducts) {
                if (TextUtils.equals(String.valueOf(product.getProductId()), hiddenProduct)) {
                    shouldHide = true;
                    break;
                }
            }

            if (shouldHide) {
                iterator.remove();
            }
        }
    }

    private void trackAction(String source, String productId) {

        if(source.equals(AttachProductActivity.SOURCE_TALK)){
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                        AttachProductAnalytics.getEventCheckProductTalk(productId).getEvent()
                );
            }else{
                TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                        AttachProductAnalytics.getEventCheckProduct().getEvent()
                );
            }
    }

    @Override
    public void setShopName(String shopName) {
        activityContract.setShopName(shopName);
    }

    @Override
    public void onDestroy() {
        if (presenter != null) presenter.detachView();
        super.onDestroy();
    }
}
