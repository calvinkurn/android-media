package com.tokopedia.core.peoplefave.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.peoplefave.fragment.PeopleFavoritedShopFragment;
import com.tokopedia.core.peoplefave.listener.PeopleFavoritedShopView;
import com.tokopedia.core.peoplefave.presenter.PeopleFavoritedShopImpl;
import com.tokopedia.core.peoplefave.presenter.PeopleFavoritedShopPresenter;

public class PeopleFavoritedShop extends BasePresenterActivity<PeopleFavoritedShopPresenter>
	implements PeopleFavoritedShopView {

	private static final String ARGS_PARAM_KEY_USER_ID = "param_key_user_id";
	private static final String ARGS_FRAGMENT_TAG = PeopleFavoritedShopFragment.class.getSimpleName();

	private String userID;

	public static Intent createIntent(Context context, String userID) {
		Intent intent = new Intent(context, PeopleFavoritedShop.class);
		Bundle bundle = new Bundle();
		bundle.putString(ARGS_PARAM_KEY_USER_ID, userID);
		intent.putExtras(bundle);
		return intent;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_PEOPLE_FAV;
	}

	@Override
	protected void setupURIPass(Uri data) {

	}

	@Override
	protected void setupBundlePass(Bundle extras) {
		userID = extras.getString(ARGS_PARAM_KEY_USER_ID);
	}

	@Override
	protected void initialPresenter() {
		presenter = new PeopleFavoritedShopImpl(this);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_people_favorited_shop;
	}

	@Override
	protected void initView() {
		presenter.setOnInitView(this);
	}

	@Override
	protected void setViewListener() {

	}

	@Override
	protected void initVar() {

	}

	@Override
	protected void setActionVar() {

	}

	@Override
	public void inflateFragment() {
		if (getFragmentManager().findFragmentByTag(ARGS_FRAGMENT_TAG) == null) {
			Fragment fragment = PeopleFavoritedShopFragment.createInstance(userID);
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment, ARGS_FRAGMENT_TAG)
					.commit();
		}

	}

	@Override
	protected boolean isLightToolbarThemes() {
		return true;
	}
}
