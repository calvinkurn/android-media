package com.tokopedia.attachproduct.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.attachproduct.R;
import com.tokopedia.attachproduct.analytics.AttachProductAnalytics;
import com.tokopedia.attachproduct.di.DaggerAttachProductComponent;
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapter;
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapterTypeFactory;
import com.tokopedia.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.attachproduct.view.presenter.AttachProductPresenter;
import com.tokopedia.attachproduct.view.viewholder.CheckableInteractionListenerWithPreCheckedAction;
import com.tokopedia.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductFragment extends BaseSearchListFragment<AttachProductItemViewModel, AttachProductListAdapterTypeFactory>
        implements CheckableInteractionListenerWithPreCheckedAction,
        AttachProductContract.View {
    private final static int MAX_CHECKED = 3;
    private static final String IS_SELLER = "isSeller";
    private Button sendButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    AttachProductPresenter presenter;

    private AttachProductContract.Activity activityContract;
    protected AttachProductListAdapter adapter;

    private boolean isSeller = false;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            isSeller = savedInstanceState.getBoolean(IS_SELLER, false);
        } else if (getArguments() != null) {
            isSeller = getArguments().getBoolean(IS_SELLER, false);
        }
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

    public static AttachProductFragment newInstance(AttachProductContract.Activity checkedUIView, boolean isSeller) {
        Bundle args = new Bundle();
        args.putBoolean(IS_SELLER, isSeller);
        AttachProductFragment fragment = new AttachProductFragment();
        fragment.setActivityContract(checkedUIView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSearchSubmitted(String text) {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        loadInitialData();
    }

    @Override
    public void onSearchTextChanged(String text) {
        if (TextUtils.isEmpty(text)) loadInitialData();
    }

    @Override
    public void onItemClicked(AttachProductItemViewModel attachProductItemViewModel) {

    }

    @Override
    protected void loadInitialData() {
        adapter.clearAllElements();
        getRecyclerViewLayoutManager().scrollToPosition(0);
        super.loadInitialData();
    }

    @Override
    public void loadData(int page) {
        presenter.loadProductData(searchInputView.getSearchText(), activityContract.getShopId(), page);
    }

    @Override
    public int getDefaultInitialPage() {
        return 0;
    }

    @NonNull
    @Override
    protected BaseListAdapter<AttachProductItemViewModel, AttachProductListAdapterTypeFactory> createAdapterInstance() {
        adapter = new AttachProductListAdapter(getAdapterTypeFactory());
        return adapter;
    }

    @Override
    public BaseListAdapter<AttachProductItemViewModel, AttachProductListAdapterTypeFactory> getAdapter() {
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
        if (adapter.getCheckedCount() >= MAX_CHECKED && (willCheckedStatusChanged && !isCurrentlyChecked)) {
            String message = getString(R.string.string_attach_product_warning_max_product_format, String.valueOf(MAX_CHECKED));
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
        trackAction();
    }

    @Override
    public void addProductToList(List<AttachProductItemViewModel> products, boolean hasNextPage) {
        if(products.size()> 0){
            sendButton.setVisibility(View.VISIBLE);
        }else{
            sendButton.setVisibility(View.VISIBLE);
        }

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
        sendButton.setText(getString(R.string.string_attach_product_send_button_text, String.valueOf(checkedCount), String.valueOf(MAX_CHECKED)));
        if (checkedCount > 0 && checkedCount <= MAX_CHECKED) {
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
        if (TextUtils.isEmpty(searchInputView.getSearchText())) {
            if(activityContract.isSeller()) {
                emptyResultViewModel.setContent(getString(R.string
                        .string_attach_product_my_empty_product));
            }else{
                emptyResultViewModel.setContent(getString(R.string.string_attach_product_empty_product));
            }
            emptyResultViewModel.setIconRes(R.drawable.bg_attach_product_empty_result);
        } else {
            emptyResultViewModel.setContent(getString(R.string.string_attach_product_search_not_found));
            emptyResultViewModel.setIconRes(R.drawable.ic_empty_search);
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

    private void trackAction() {
        if ((getActivity().getApplicationContext() instanceof AbstractionRouter)) {
            AbstractionRouter abstractionRouter = (AbstractionRouter) getActivity().getApplicationContext();
            abstractionRouter.getAnalyticTracker().sendEventTracking(
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
