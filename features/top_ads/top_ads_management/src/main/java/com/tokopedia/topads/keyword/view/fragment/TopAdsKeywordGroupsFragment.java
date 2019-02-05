package com.tokopedia.topads.keyword.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.fragment.BaseListFragment;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.base.view.fragment.BaseFilterContentFragment;
import com.tokopedia.seller.base.view.listener.BaseFilterContentViewListener;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordNewChooseGroupComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;
import com.tokopedia.topads.keyword.view.adapter.TopAdsKeywordGroupListAdapter;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordGroupListListener;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordGroupListView;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordOldNewChooseGroupPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupsFragment extends BaseListFragment<TopAdsKeywordOldNewChooseGroupPresenter, GroupAd>
        implements TopAdsKeywordGroupListView, BaseFilterContentViewListener {

    protected BaseFilterContentFragment.Callback callback;
    @Inject
    TopAdsKeywordOldNewChooseGroupPresenter topAdsKeywordOldNewChooseGroupPresenter;
    private EditText groupFilterSearch;
    private GroupAd selection;

    /**
     * Sign for title filter list
     */
    private boolean active;
    private int position;
    private ImageView groupFilterImage;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            topAdsKeywordOldNewChooseGroupPresenter.searchGroupName(editable.toString());
        }
    };
    private View hideThings;
    private PopupMenu popupMenu;
    private TopAdsKeywordGroupListListener groupListAdapterListener;

    public static TopAdsKeywordGroupsFragment createInstance(GroupAd currentGroupAd, int position) {
        TopAdsKeywordGroupsFragment topAdsKeywordGroupsFragment = new TopAdsKeywordGroupsFragment();
        Bundle argument = new Bundle();
        if (currentGroupAd != null)
            argument.putParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, currentGroupAd);
        argument.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_POSITION_LIST, position);
        topAdsKeywordGroupsFragment.setArguments(argument);
        return topAdsKeywordGroupsFragment;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordNewChooseGroupComponent.builder()
                .topAdsKeywordNewChooseGroupModule(new TopAdsKeywordNewChooseGroupModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context != null && context instanceof TopAdsKeywordGroupListListener) {
            groupListAdapterListener = (TopAdsKeywordGroupListListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && getArguments() != null) {
            selection = getArguments().getParcelable(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION);
            position = getArguments().getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_POSITION_LIST, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        topAdsKeywordOldNewChooseGroupPresenter.attachView(this);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        groupFilterSearch = (EditText) view.findViewById(R.id.group_filter_search);
        groupFilterImage = (ImageView) view.findViewById(R.id.group_filter_search_icon);
        groupFilterImage.setImageResource(R.drawable.ic_group_magnifier);
        hideThings = view.findViewById(R.id.hide_things);
        hideThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
        hideThings.setVisibility(View.INVISIBLE);
        popupMenu = new PopupMenu(getActivity(), hideThings);
        popupMenu.getMenuInflater().inflate(R.menu.popup_delete_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete_keyword) {
                    enableSelection();
                    return true;
                }
                return false;
            }
        });

        groupFilterSearch.addTextChangedListener(textWatcher);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        swipeToRefresh.setEnabled(false);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_filter_group_name;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topAdsKeywordOldNewChooseGroupPresenter.detachView();
    }

    @Override
    public void onItemClicked(GroupAd groupAd) {
        if (groupAd != null) {
            groupAd.setSelected(!groupAd.isSelected());
            if (groupAd.isSelected()) {
                notifySelect(groupAd);
            }
        }
    }

    @Override
    public void onGetGroupAdList(List<GroupAd> groupAds) {
        onSearchLoaded(groupAds, groupAds.size());
        if (selection != null) {
            notifySelect(selection);
        }
    }

    @Override
    public void onGetGroupAdListError(Throwable e) {
        if (adapter == null) {
            return;
        }
        adapter.showLoading(false);
        // failed first time, show retry fullscreen
        if (adapter.getDataSize() == 0) {
            adapter.showRetryFull(true);
        } else if (adapter.getDataSize() > 0) {
            // failed to fetch when load more, show snackbar
            String errorMessage = ViewUtils.getGeneralErrorMessage(getContext(), e);
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        }
    }

    @Override
    protected BaseListAdapter<GroupAd> getNewAdapter() {
        TopAdsKeywordGroupListAdapter topAdsKeywordGroupListAdapter = new TopAdsKeywordGroupListAdapter();
        return topAdsKeywordGroupListAdapter;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.top_ads_keyword_group_title);
    }

    @Override
    public Intent addResult(Intent intent) {
        if (selection != null)
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRECT_GROUP_SELECTION, selection);

        return intent;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setCallback(BaseFilterContentFragment.Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void searchForPage(int page) {
        topAdsKeywordOldNewChooseGroupPresenter.searchGroupName(groupFilterSearch.getText().toString());
    }

    @Override
    protected void hideLoading() {
        super.hideLoading();
        swipeToRefresh.setEnabled(false);
    }

    public void notifySelect(GroupAd groupAd) {
        if (groupListAdapterListener != null) {
            groupListAdapterListener.notifySelect(groupAd, this.position);
        }

        this.selection = groupAd;
        groupFilterSearch.removeTextChangedListener(textWatcher);
        groupFilterSearch.setText(groupAd.getName());
        groupFilterSearch.setFocusable(false);
        groupFilterSearch.setEnabled(false);
        groupFilterImage.setImageResource(R.drawable.ic_group_keyword);
        recyclerView.setVisibility(View.GONE);
        hideThings.setVisibility(View.VISIBLE);
        groupFilterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSelection();
            }
        });
    }

    protected void enableSelection() {
        if (groupListAdapterListener != null) {
            groupListAdapterListener.resetSelection(position);
        }

        selection = null;
        groupFilterSearch.setFocusableInTouchMode(true);
        groupFilterSearch.setEnabled(true);
        groupFilterImage.setImageResource(R.drawable.ic_group_magnifier);
        groupFilterImage.setOnClickListener(null);
        groupFilterSearch.addTextChangedListener(textWatcher);
        groupFilterSearch.setText("");
        recyclerView.setVisibility(View.VISIBLE);
        hideThings.setVisibility(View.INVISIBLE);
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.title_no_result));
        return emptyGroupAdsDataBinder;
    }
}