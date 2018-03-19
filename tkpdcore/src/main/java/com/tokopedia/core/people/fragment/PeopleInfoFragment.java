package com.tokopedia.core.people.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.ManagePeople;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.core.people.customview.PeopleInfoDetailView;
import com.tokopedia.core.people.customview.PeopleInfoHeaderView;
import com.tokopedia.core.people.customview.PeopleInfoReputationView;
import com.tokopedia.core.people.customview.PeopleInfoShopOwnerView;
import com.tokopedia.core.people.listener.PeopleInfoFragmentView;
import com.tokopedia.core.people.listener.PeopleInfoView;
import com.tokopedia.core.people.model.InputOutputData;
import com.tokopedia.core.people.model.PeopleInfoData;
import com.tokopedia.core.people.presenter.PeopleInfoFragmentImpl;
import com.tokopedia.core.people.presenter.PeopleInfoFragmentPresenter;
import com.tokopedia.core.peoplefave.activity.PeopleFavoritedShop;
import com.tokopedia.core.router.TkpdInboxRouter;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PeopleInfoFragment extends BasePresenterFragment<PeopleInfoFragmentPresenter>
        implements PeopleInfoFragmentView {

    public static final String TAG = PeopleInfoFragment.class.getSimpleName();
    public static final String EXTRA_PARAM_USER_ID = "EXTRA_PARAM_USER_ID";

    @BindView(R2.id.layout_people_info_header_view)
    PeopleInfoHeaderView peopleInfoHeaderView;
    @BindView(R2.id.layout_people_info_detail_view)
    PeopleInfoDetailView peopleInfoDetailView;
    @BindView(R2.id.layout_people_info_reputation_view)
    PeopleInfoReputationView peopleInfoReputationView;
    @BindView(R2.id.layout_people_info_shop_owner_view)
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
        if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
            Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                    .getAskUserIntent(getActivity(),
                            userInfo.getUserId(),
                            userInfo.getUserName(),
                            TkpdInboxRouter.PROFILE,
                            userInfo.getUserImage());
            startActivity(intent);
        }
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
    public void openFollowingPage(int userId) {
        startActivity(((TkpdCoreRouter) getActivity().getApplicationContext())
                .getKolFollowingPageIntent(getActivity(), userId));
    }

    @Override
    public void openShopDetail(String shopId) {
        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shopId);
        context.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
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
