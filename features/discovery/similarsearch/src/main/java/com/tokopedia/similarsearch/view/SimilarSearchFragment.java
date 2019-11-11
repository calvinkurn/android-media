package com.tokopedia.similarsearch.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.similarsearch.R;
import com.tokopedia.similarsearch.analytics.SimilarSearchTracking;
import com.tokopedia.similarsearch.di.DaggerSimilarSearchComponent;
import com.tokopedia.similarsearch.di.SimilarSearchComponent;
import com.tokopedia.similarsearch.model.ProductsItem;
import com.tokopedia.similarsearch.view.presenter.SimilarSearchPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.discovery.common.constants.SearchConstant.SimilarSearch.QUERY;

public class SimilarSearchFragment extends BaseDaggerFragment implements SimilarSearchContract.View, WishListActionListener, SimilarSearchAdapter.WishListClickListener {

    static final String PRODUCT_ID = "product_id";
    @Inject
    SimilarSearchPresenter mPresenter;
    private UserSessionInterface userSession;
    private SimilarSearchAdapter mAdapter;
    private RecyclerView mSimilarItemList;
    private View mContentLayout;
    private View mEmptyLayout;
    private View mProgressBar;
    private View mClose;
    public static final int REQUEST_CODE_LOGIN = 561;
    private SimilarSearchComponent similarSearhComponent;


    public static SimilarSearchFragment newInstance(String productId, String query) {

        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        args.putString(QUERY, query);
        SimilarSearchFragment fragment = new SimilarSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getContext());
    }

    @Override
        public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
            final int animatorId = (enter) ? R.anim.similar_search_slide_in_up : R.anim.similar_search_slide_in_down;
            final Animation anim = AnimationUtils.loadAnimation(getActivity(), animatorId);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    onAnimationCompletlistner.onAnimaitonComplete();
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            AnimationSet animSet = new AnimationSet(true);
            animSet.addAnimation(anim);

            return animSet;
        }

    OnAnimationCompletelistner onAnimationCompletlistner;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAnimationCompletlistner = (OnAnimationCompletelistner)context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_similar_search, container, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initView(v);
        mAdapter = new SimilarSearchAdapter(this);
        mPresenter.attachView(this);
        mPresenter.setWishListListener(this);
        mSimilarItemList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mSimilarItemList.setAdapter(mAdapter);
        return v;
    }

    @Override
    protected void initInjector() {
        if (getAppContext() != null) {
            similarSearhComponent = DaggerSimilarSearchComponent.builder()
                    .baseAppComponent(getBaseAppComponent())
                    .build();
            similarSearhComponent.inject(this);
        }
    }

    private BaseAppComponent getBaseAppComponent() {
        if(getActivity() == null || getActivity().getApplication() == null) return null;

        return ((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent();
    }

    @Override
    public String getScreenName() {
        return "/searchproduct - product";
    }

    @Override
    @Nullable
    public Context getAppContext() {
        if (getActivity() != null) {
            return getActivity().getApplicationContext();
        } else {
            return null;
        }
    }

    @Override
    public String getProductID() {
        if (getArguments() == null) return "";

        return getArguments().getString(PRODUCT_ID);
    }

    @Override
    public String getQueryKey() {
        if (getArguments() == null) return "";

        return getArguments().getString(QUERY);
    }

    @Override
    public void setProductList(List<ProductsItem> productList) {
        mProgressBar.setVisibility(View.GONE);
        mSimilarItemList.setVisibility(View.VISIBLE);
        List<Object> dataLayerList = new ArrayList<>();
        if(productList != null) {
            int i = 0;
            for(ProductsItem productsItem: productList) {
                if(productsItem.getId() != 0) {
                    productsItem.setOriginProductID(getProductID());
                    dataLayerList.add(productsItem.getProductAsObjectDataLayer(String.valueOf(i++)));
                }
            }
        }
        if(dataLayerList.size() > 0)
            SimilarSearchTracking.eventUserSeeSimilarProduct(getActivity(), getProductID(),dataLayerList);
        mAdapter.setProductsItems(productList);
    }

    @Override
    public void setEmptyLayoutVisible() {
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setContentLayoutGone() {
            mContentLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean isUserHasLogin() {
        return userSession.isLoggedIn();
    }


    @Override
    public void disableWishlistButton(int adapterPosition) {
        mAdapter.setWishlistButtonEnabled(adapterPosition, false);
    }

    @Override
    public void enableWishlistButton(int adapterPosition) {
        mAdapter.setWishlistButtonEnabled(adapterPosition, true);
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    @Override
    public void launchLoginActivity(Bundle extras) {
        if (getActivity() == null) return;

        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    private void initView(View v) {
        mProgressBar = v.findViewById(R.id.progress_bar);
        mSimilarItemList = v.findViewById(R.id.similar_item_list);
        mContentLayout = v.findViewById(R.id.layout_content);
        mEmptyLayout = v.findViewById(R.id.layout_empty);
        mClose = v.findViewById(R.id.fragment_similar_search_close_button);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }


    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        int adapterPosition = mAdapter.getIndex(productId);
        if (adapterPosition < 0 || adapterPosition >= mAdapter.getItemCount()) return;

        enableWishlistButton(mAdapter.getIndex(productId));
        mAdapter.notifyItemChanged(mAdapter.getIndex(productId));
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        mAdapter.updateWishlistStatus(productId, true);
        enableWishlistButton(mAdapter.getIndex(productId));
        mAdapter.notifyItemChanged(mAdapter.getIndex(productId));
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        enableWishlistButton(mAdapter.getIndex(productId));
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        mAdapter.updateWishlistStatus(productId, false);
        enableWishlistButton(mAdapter.getIndex(productId));
        mAdapter.notifyItemChanged(mAdapter.getIndex(productId));
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void onWishlistButtonClicked(ProductsItem productItem, int adapterPosition) {
        mPresenter.handleWishlistButtonClicked(productItem,adapterPosition);
    }

    public interface OnAnimationCompletelistner {
        public void onAnimaitonComplete();
    }
}
