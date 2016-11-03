package com.tokopedia.tkpd.people.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.tkpd.ManagePeople;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.BasePresenterFragment;
import com.tokopedia.tkpd.inboxmessage.activity.SendMessageActivity;
import com.tokopedia.tkpd.inboxmessage.fragment.SendMessageFragment;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.tkpd.people.customview.PeopleInfoDetailView;
import com.tokopedia.tkpd.people.customview.PeopleInfoHeaderView;
import com.tokopedia.tkpd.people.customview.PeopleInfoReputationView;
import com.tokopedia.tkpd.people.customview.PeopleInfoShopOwnerView;
import com.tokopedia.tkpd.people.listener.PeopleInfoFragmentView;
import com.tokopedia.tkpd.people.listener.PeopleInfoView;
import com.tokopedia.tkpd.people.model.InputOutputData;
import com.tokopedia.tkpd.people.model.PeopleInfoData;
import com.tokopedia.tkpd.people.presenter.PeopleInfoFragmentImpl;
import com.tokopedia.tkpd.people.presenter.PeopleInfoFragmentPresenter;
import com.tokopedia.tkpd.peoplefave.activity.PeopleFavoritedShop;
import com.tokopedia.tkpd.shopinfo.ShopInfoActivity;

import butterknife.Bind;

/**
 * A placeholder fragment containing a simple view.
 */
public class PeopleInfoFragment extends BasePresenterFragment<PeopleInfoFragmentPresenter>
        implements PeopleInfoFragmentView {

    public static final String TAG = PeopleInfoFragment.class.getSimpleName();
    public static final String EXTRA_PARAM_USER_ID = "EXTRA_PARAM_USER_ID";

    @Bind(R.id.layout_people_info_header_view)
    PeopleInfoHeaderView peopleInfoHeaderView;
    @Bind(R.id.layout_people_info_detail_view)
    PeopleInfoDetailView peopleInfoDetailView;
    @Bind(R.id.layout_people_info_reputation_view)
    PeopleInfoReputationView peopleInfoReputationView;
    @Bind(R.id.layout_people_info_shop_owner_view)
    PeopleInfoShopOwnerView peopleInfoShopOwnerView;


    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Bundle bundle;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private PeopleInfoView listener;
    private String userID;
    private TkpdProgressDialog loadingHandler;

    public static Fragment newInstance(String userID) {
        PeopleInfoFragment fragment = new PeopleInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_USER_ID, userID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PeopleInfoFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnFirstTimeLaunch(getActivity(), userID);
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
    protected void initialPresenter() {
        presenter = new PeopleInfoFragmentImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        this.listener = (PeopleInfoView) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.bundle = arguments;
        this.setUserID(bundle.getString(EXTRA_PARAM_USER_ID));
    }

    @Override
    public String getUserID() {
        return this.userID;
    }

    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_people_info;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        peopleInfoHeaderView.setPresenter(presenter);
        peopleInfoDetailView.setPresenter(presenter);
        peopleInfoReputationView.setPresenter(presenter);
        peopleInfoShopOwnerView.setPresenter(presenter);
    }

    @Override
    protected void initialVar() {
        loadingHandler = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS, getActivity().getWindow().getDecorView().getRootView());
        loadingHandler.setLoadingViewId(R.id.include_loading);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void setLoadingView(boolean isVisible) {
        if (isVisible) {
            loadingHandler.showDialog();
        } else {
            loadingHandler.dismiss();
        }
    }

    @Override
    public void renderShopInfoView(PeopleInfoData peopleInfoData) {
        peopleInfoShopOwnerView.renderData(peopleInfoData);
    }

    @Override
    public void renderReputationView(PeopleInfoData peopleInfoData) {
        peopleInfoReputationView.renderData(peopleInfoData);
    }

    @Override
    public void renderPeopleInfoView(InputOutputData data) {
        peopleInfoDetailView.renderData(data);
    }

    @Override
    public void renderHeaderView(InputOutputData data) {
        peopleInfoHeaderView.renderData(data);
    }

    @Override
    public void openCreateMessage(PeopleInfoData.UserInfo userInfo) {
        Intent intent = new Intent(getActivity(), SendMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SendMessageFragment.PARAM_USER_ID, userInfo.getUserId());
        bundle.putString(SendMessageFragment.PARAM_OWNER_FULLNAME, userInfo.getUserName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openManageUser(PeopleInfoData.UserInfo userInfo) {
        Intent intent = new Intent(getActivity(), ManagePeople.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void openDetailFavoritedShop(PeopleInfoData.UserInfo userInfo) {
        startActivity(PeopleFavoritedShop.createIntent(getActivity(), userInfo.getUserId()));
    }

    @Override
    public void openShopDetail(String shopId) {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shop_id", shopId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            presenter.refreshPeopleInfo(getActivity(), getUserID());
        }
    }

    @Override
    public void setShopnView(boolean isVisible) {
        if (isVisible) {
            peopleInfoShopOwnerView.setVisibility(View.VISIBLE);
        } else {
            peopleInfoShopOwnerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserReputationView(boolean isVisible) {
        if (isVisible) {
            peopleInfoReputationView.setVisibility(View.VISIBLE);
        } else {
            peopleInfoReputationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPeopleInfoView(boolean isVisible) {
        if (isVisible) {
            peopleInfoDetailView.setVisibility(View.VISIBLE);
        } else {
            peopleInfoDetailView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setHeaderView(boolean isVisible) {
        if (isVisible) {
            peopleInfoHeaderView.setVisibility(View.VISIBLE);
        } else {
            peopleInfoHeaderView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTimeOut(RetryClickedListener clickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
    }

    @Override
    public void showErrorMessage(String message) {
        NetworkErrorHelper.showEmptyState(
                getActivity(),
                getView(),
                message,
                null);
    }

    @Override
    public void onDestroyView() {
        presenter.setOnDestroyView(getActivity());
        super.onDestroyView();
    }
}
